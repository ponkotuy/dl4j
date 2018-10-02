terraform {
  required_version = "= 0.11.8"
}

provider "aws" {
  region = "us-west-2"
  version = "= 1.38.0"
}

variable "vpc_id" {
  default = "vpc-3800105a"
}

variable "ec2_key_name" {
  default = "ec2"
}

variable "default_security_group" {
  default = "sg-73372c11"
}
