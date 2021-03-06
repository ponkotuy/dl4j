import java.io.File
import java.nio.file.{Paths, StandardCopyOption}

import labels.Rate
import org.datavec.image.loader.NativeImageLoader
import org.deeplearning4j.util.ModelSerializer
import utils.Files

object Execution {
  def main(args: Array[String]): Unit = {
    import utils.MyConfig._
    aws.bucket.download(path.modelName, path.modelPath, StandardCopyOption.REPLACE_EXISTING)
    val model = ModelSerializer.restoreMultiLayerNetwork(path.modelPath.toFile)
    val loader = new NativeImageLoader(property.width, property.height, Training.NChannels)
    println(model.conf())
    args.foreach { fname =>
      val matrix = loader.asMatrix(new File(fname))
      val score = model.output(matrix).getDouble(0)
      println(s"${fname}: ${score}")
      splitDir(fname, Rate.fromScore(score))
    }
  }

  def splitDir(fname: String, rate: Rate): Unit = {
    val file = Paths.get(fname)
    val destDir = rate.path(file.getParent)
    Files.mkdirs(destDir)
    Files.move(file, destDir.resolve(file.getFileName))
  }
}
