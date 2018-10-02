package utils

import java.nio.file.{Paths, Path => JPath}

import com.typesafe.config.{Config, ConfigFactory}

object MyConfig {
  private[this] val config = ConfigFactory.load()

  val path = new Path(config.getConfig("path"))

  val property = new Property(config.getConfig("property"))

  val bucket = config.getString("s3.bucket")
}

class Path(config: Config) {
  val modelName = config.getString("model_file")
  val dl4jPath: JPath = Paths.get(config.getString("dl4j_dir"))
  val imagesPath: JPath = dl4jPath.resolve(config.getString("images_dir"))
  val modelPath: JPath = dl4jPath.resolve(modelName)
  val imageRsync: String = config.getString("image_rsync")
  val originalImagePath: JPath = Paths.get(config.getString("original_images_dir"))
}

class Property(config: Config) {
  val width = config.getInt("image.width")
  val height = config.getInt("image.height")
  val epoch = config.getInt("epoch")
}
