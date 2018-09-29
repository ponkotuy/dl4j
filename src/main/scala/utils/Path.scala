package utils

import java.nio.file.{Paths, Path => JPath}

import com.typesafe.config.ConfigFactory

object Path {
  private[this] val config = ConfigFactory.load()
  private[this] val path = config.getConfig("path")
  val dl4j: String = path.getString("dl4j_dir")
  val images: String = path.getString("images_dir")
  val model: String = path.getString("model_file")

  val dl4jPath: JPath = Paths.get(dl4j)
  val imagesPath: JPath = dl4jPath.resolve(images)
  val modelPath: JPath = dl4jPath.resolve(model)

  val OriginalImagePath: JPath = Paths.get(path.getString("original_images_dir"))
}

object S3Path {
  def imageDir = Path.images
  def images(key: String) = s"${Path.images}/${key}"
  def model = Path.model
}
