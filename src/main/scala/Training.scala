
import org.datavec.api.io.filters.BalancedPathFilter
import org.datavec.api.io.labels.PathLabelGenerator
import org.datavec.api.split.{FileSplit, InputSplit}
import org.datavec.image.loader.BaseImageLoader
import org.datavec.image.recordreader.ImageRecordReader
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.distribution.{GaussianDistribution, NormalDistribution}
import org.deeplearning4j.nn.conf.inputs.InputType
import org.deeplearning4j.nn.conf.layers.{ConvolutionLayer, DenseLayer, LocalResponseNormalization, OutputLayer, SubsamplingLayer}
import org.deeplearning4j.nn.conf.{GradientNormalization, LearningRatePolicy, NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.ui.api.UIServer
import org.deeplearning4j.ui.stats.StatsListener
import org.deeplearning4j.ui.storage.InMemoryStatsStorage
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.activations.impl.{ActivationLReLU, ActivationSoftmax}
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions
import utils.{Files, MyPathLabelGen, RsyncOption, RsyncWrapper, S3Wrapper, Streams}

import scala.collection.JavaConverters._
import scala.util.Random

object Training {
  import utils.MyConfig._

  val NChannels = 3
  val OutputNum = 2
  val BatchSize = 128
  val Iterations = 2
  val Seed = 1234
  val NonZeroBias = 1
  val DropOut = 0.5
  val TestRate: Int = 10
  val TrainRate: Int = 100 - TestRate

  val rsync = new RsyncWrapper(Streams.Stdout)(
    RsyncOption.Archive,
    RsyncOption.Delete,
    RsyncOption.Verbose,
    RsyncOption.CopyLinks,
    RsyncOption.Rsh("ssh")
  )

  def main(args: Array[String]): Unit = {
    Files.mkdirs(path.imagesPath)
    rsync.run(path.imageRsync, path.imagesPath.toString)
    setThreads()
    val random = new Random(Seed)
    val labelGen = MyPathLabelGen
    val fileSplit = new FileSplit(path.imagesPath.toFile, BaseImageLoader.ALLOWED_FORMATS, random.self)
    val pathFilter = new BalancedPathFilter(random.self, BaseImageLoader.ALLOWED_FORMATS, labelGen)
    val Array(testInput, trainInput) = fileSplit.sample(pathFilter, TestRate, TrainRate)

    val trainData = inputToData(trainInput, labelGen)
    val testData = inputToData(testInput, labelGen)

    val conf = new NeuralNetConfiguration.Builder()
        .seed(Seed)
        .weightInit(WeightInit.DISTRIBUTION)
        .dist(new NormalDistribution(0.0, 0.01))
        .activation(new ActivationLReLU())
        .updater(Updater.NESTEROVS)
        .iterations(Iterations)
        .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
        .learningRate(1e-2)
        .biasLearningRate(1e-2 * 2)
        .learningRateDecayPolicy(LearningRatePolicy.Step)
        .lrPolicyDecayRate(0.1)
        .lrPolicySteps(100000)
        .regularization(true)
        .l2(5 * 1e-4)
        .updater(new Nesterovs(0.9))
        .miniBatch(false)
        .list()
        .layer(0, new ConvolutionLayer.Builder(Array(11, 11), Array(4, 4), Array(3, 3)).name("cnn1").nIn(NChannels).nOut(96).biasInit(0).build())
        .layer(1, new LocalResponseNormalization.Builder().name("lrn1").build())
        .layer(2, new SubsamplingLayer.Builder(Array(3, 3), Array(2, 2)).name("maxpool1").build())
        .layer(3, new ConvolutionLayer.Builder(Array(5, 5), Array(1, 1), Array(2, 2)).name("cnn2").nOut(256).biasInit(NonZeroBias).build())
        .layer(4, new LocalResponseNormalization.Builder().name("lrn2").build())
        .layer(5, new SubsamplingLayer.Builder(Array(3, 3), Array(2, 2)).name("maxpool2").build())
        .layer(6, new ConvolutionLayer.Builder(Array(3, 3), Array(1, 1), Array(1, 1)).name("cnn3").nOut(384).biasInit(0).build())
        .layer(7, new ConvolutionLayer.Builder(Array(3, 3), Array(1, 1), Array(1, 1)).name("cnn4").nOut(384).biasInit(NonZeroBias).build())
        .layer(8, new ConvolutionLayer.Builder(Array(3, 3), Array(1, 1), Array(1, 1)).name("cnn5").nOut(256).biasInit(NonZeroBias).build())
        .layer(9, new SubsamplingLayer.Builder(Array(3, 3), Array(2, 2)).name("maxpool3").build())
        .layer(10, new DenseLayer.Builder().name("ffn1").nOut(4096).biasInit(NonZeroBias).dropOut(DropOut).dist(new GaussianDistribution(0, 0.005)).build())
        .layer(11, new DenseLayer.Builder().name("ffn2").nOut(4096).biasInit(NonZeroBias).dropOut(DropOut).dist(new GaussianDistribution(0, 0.005)).build())
        .layer(12, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).name("output").nOut(OutputNum).activation(new ActivationSoftmax).build())
        .backprop(true)
        .pretrain(false)
        .setInputType(InputType.convolutional(property.height, property.width, NChannels))
        .build()
    val model = new MultiLayerNetwork(conf)
    model.init()

    val uiServer = UIServer.getInstance()
    val statsStorage = new InMemoryStatsStorage()
    uiServer.attach(statsStorage)
    model.setListeners(new StatsListener(statsStorage))

    (1 to property.epoch).foreach { i =>
      model.fit(trainData)
      println(s"Complete epoch $i")
      val eval = new Evaluation(OutputNum)
      testData.asScala.foreach { ds =>
        val output = model.output(ds.getFeatureMatrix, false)
        eval.eval(ds.getLabels, output)
      }
      println(eval.stats())
      testData.reset()
    }

    ModelSerializer.writeModel(model, path.modelPath.toFile, false)
    new S3Wrapper(bucket).upload(path.modelName, path.modelPath)
  }

  private def setThreads(): Unit = {
    import org.nd4j.linalg.factory.Nd4j
    import org.nd4j.nativeblas.NativeOpsHolder
    import org.nd4j.nativeblas.Nd4jBlas

    val nd4jBlas = Nd4j.factory.blas.asInstanceOf[Nd4jBlas]
    nd4jBlas.setMaxThreads(nd4jBlas.getMaxThreads * 2)

    val instance = NativeOpsHolder.getInstance
    val deviceNativeOps = instance.getDeviceNativeOps
    deviceNativeOps.setOmpNumThreads(deviceNativeOps.ompGetNumThreads() * 2)
  }

  private def inputToData(input: InputSplit, labelGen: PathLabelGenerator): RecordReaderDataSetIterator = {
    val dataReader = new ImageRecordReader(property.height, property.width, NChannels, labelGen)
    dataReader.initialize(input)
    new RecordReaderDataSetIterator(dataReader, BatchSize, 1, OutputNum)
  }
}

