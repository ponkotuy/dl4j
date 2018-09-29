resource "aws_instance" "dl4j" {
  ami = "ami-7172b611"
  instance_type = "r5.xlarge"
  monitoring = false
  tags {
    Name = "dl4j"
  }
}
