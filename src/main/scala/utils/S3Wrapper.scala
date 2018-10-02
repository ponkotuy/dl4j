package utils

import java.nio.file.{CopyOption, Path => JPath}

import awscala._
import awscala.s3._

class S3Wrapper(bucketName: String) {
  implicit val s3 = S3.at(Region.Oregon)
  val bucket = s3.bucket(bucketName).getOrElse(throw new RuntimeException(s"Not found bucket: ${bucketName}"))

  def upload(key: String, file: JPath) = {
    bucket.put(key, file.toFile)
  }

  def download(key: String, dest: JPath, options: CopyOption*) = {
    bucket.get(key).foreach { obj =>
      Files.copy(obj.content, dest, options:_*)
    }
  }
}
