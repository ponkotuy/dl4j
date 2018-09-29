package utils

import awscala._
import awscala.s3._
import com.typesafe.config.{Config, ConfigFactory}

object MyConfig {
  private[this] val config: Config = ConfigFactory.load()
  val aws = new AWS(config.getConfig("aws"))
}

class AWS(config: Config) {
  val s3 = new S3(config.getConfig("s3"))
}

class S3(config: Config) {
  val bucketName: String = config.getString("bucket")
  val client = S3.at(Region.Tokyo)
  val bucket = client.bucket(bucketName).get
}
