resource "aws_s3_bucket" "dl4j_s3" {
  bucket = "dl4j-s3"
  acl = "private"
  region = "ap-northeast-1"
}
