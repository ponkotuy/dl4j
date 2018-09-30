package utils

import java.nio.file.{Paths, Path => JPath}

import com.typesafe.config.ConfigFactory

object Path {
  private[this] val config = ConfigFactory.load()
  private[this] val path = config.getConfig("path")
  val modelName = path.getString("model_file")

  val dl4jPath: JPath = Paths.get(path.getString("dl4j_dir"))
  val imagesPath: JPath = dl4jPath.resolve(path.getString("images_dir"))
  val modelPath: JPath = dl4jPath.resolve(modelName)

  val originalImagePath: JPath = Paths.get(path.getString("original_images_dir"))

  val bucket = config.getString("s3.bucket")
}
