package utils

import java.nio.file.{Paths, Path => JPath}

import com.typesafe.config.ConfigFactory

object Path {
  private[this] val config = ConfigFactory.load()
  private[this] val path = config.getConfig("path")
  val Dl4jPath: JPath = Paths.get(path.getString("dl4j_dir"))
  val ImagesPath: JPath = Dl4jPath.resolve(path.getString("images_dir"))
  val ModelPath: JPath = Dl4jPath.resolve(path.getString("model_file"))

  val OriginalImagePath: JPath = Paths.get(path.getString("original_images_dir"))
}
