import java.nio.file.{Path, Paths}

import labels.Rate
import utils.{Files, MyCloudVision}

object ExecutionCV {
  def main(args: Array[String]): Unit = {
    val client = new MyCloudVision
    args.grouped(10).foreach { xs =>
      val paths = xs.map(Paths.get(_)).filter(Files.isRegularFile(_)).filter(Files.isReadable)
      val images = paths.map(MyCloudVision.loadImagePath).map(_.get)
      val annotations = client.safeSearchDetections(images: _*).toList
      paths.zip(annotations).foreach{ case (path, anno) =>
        val fname = path.toString
        println(s"${fname}: Adult=${anno.getAdultValue} Racy=${anno.getRacyValue}")
        Execution.splitDir(fname, Rate.fromCV(anno))
      }
    }
  }
}
