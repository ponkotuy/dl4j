import labels.Rate
import utils.MyCloudVision

object ExecutionCV {
  def main(args: Array[String]): Unit = {
    val client = new MyCloudVision
    args.grouped(10).foreach { xs =>
      val images = xs.map(MyCloudVision.loadImage)
      val annotations = client.safeSearchDetections(images: _*).toList
      xs.zip(annotations).foreach{ case (fname, anno) =>
        println(s"${fname}: Adult=${anno.getAdultValue} Racy=${anno.getRacyValue}")
        Execution.splitDir(fname, Rate.fromCV(anno))
      }
    }
  }
}
