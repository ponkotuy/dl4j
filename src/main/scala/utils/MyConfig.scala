package utils

import java.nio.file.{Paths, Path => JPath}

import com.typesafe.config.{Config, ConfigFactory}

object MyConfig {
  private[this] val config = ConfigFactory.load()

  lazy val path = new Path(config.getConfig("path"))
  lazy val property = new Property(config.getConfig("property"))
  lazy val aws = new Aws(config.getConfig("aws"))
}

class Path(config: Config) {
  lazy val modelName = config.getString("model_file")
  lazy val dl4jPath: JPath = Paths.get(config.getString("dl4j_dir"))
  lazy val imagesPath: JPath = dl4jPath.resolve(config.getString("images_dir"))
  lazy val modelPath: JPath = dl4jPath.resolve(modelName)
  lazy val imageRsync: String = config.getString("image_rsync")
  lazy val originalImagePath: JPath = Paths.get(config.getString("original_images_dir"))
}

class Property(config: Config) {
  lazy val width = config.getInt("image.width")
  lazy val height = config.getInt("image.height")
  lazy val epoch = config.getInt("epoch")
}

class Aws(config: Config) {
  lazy val instanceId = config.getString("ec2.instance_id")
  def instance = new EC2Instance(instanceId)
  lazy val bucketName = config.getString("s3.bucket")
  lazy val bucket = new S3Bucket(bucketName)
}
