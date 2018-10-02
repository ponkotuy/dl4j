import java.io.File
import java.nio.file.{Paths, StandardCopyOption}

import labels.Rate
import org.datavec.image.loader.NativeImageLoader
import org.deeplearning4j.util.ModelSerializer
import utils.{Files, S3Wrapper}

object Execution {

  val Negative = 0.15
  val Positive = 0.85

  def main(args: Array[String]): Unit = {
    import utils.MyConfig._
    new S3Wrapper(bucket).download(path.modelName, path.modelPath, StandardCopyOption.REPLACE_EXISTING)
    val model = ModelSerializer.restoreMultiLayerNetwork(path.modelPath.toFile)
    val loader = new NativeImageLoader(property.width, property.height, Training.NChannels)
    println(model.conf())
    args.foreach { fname =>
      val matrix = loader.asMatrix(new File(fname))
      val score = model.output(matrix).getDouble(0)
      println(s"${fname}: ${score}")
      splitDir(fname, Rate.find(score))
    }
  }

  def splitDir(fname: String, rate: Rate): Unit = {
    val file = Paths.get(fname)
    val destDir = rate.path(file.getParent)
    Files.mkdirs(destDir)
    Files.move(file, destDir.resolve(file.getFileName))
  }
}
