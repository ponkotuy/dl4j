package utils

import java.io.File

import awscala._
import awscala.s3._

class S3Wrapper(bucketName: String) {
  implicit val s3 = S3.at(Region.Oregon)
  val bucket = s3.bucket(bucketName).getOrElse(throw new RuntimeException(s"Not found bucket: ${bucketName}"))

  def upload(key: String, file: File) = {
    bucket.put(key, file)
  }
}
