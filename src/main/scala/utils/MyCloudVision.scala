package utils

import java.nio.file.{Paths, Path => JPath}

import com.google.cloud.vision.v1._
import com.google.protobuf.ByteString

import scala.collection.JavaConverters._

class MyCloudVision {
  private[this] val client = ImageAnnotatorClient.create()

  def safeSearchDetections(images: Image*): Seq[SafeSearchAnnotation] = {
    val feature = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION)
    val reqs = images.map{ image => AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build() }
    val batch = BatchAnnotateImagesRequest.newBuilder().addAllRequests(reqs.asJava).build()
    val raw = client.batchAnnotateImages(batch)
    val res = raw.getResponsesList.asScala
    res.map(_.getSafeSearchAnnotation)
  }
}

object MyCloudVision {
  def loadImagePath(path: JPath): Option[Image] = {
    if(Files.isRegularFile(path) && Files.isReadable(path)) {
      val bytes = Files.readAllBytes(path)
      val byteString = ByteString.copyFrom(bytes)
      Some(Image.newBuilder().setContent(byteString).build())
    } else None
  }

  def loadImage(fname: String): Option[Image] = {
    loadImagePath(Paths.get(fname))
  }
}
