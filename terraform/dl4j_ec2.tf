resource "aws_instance" "dl4j" {
  ami = "ami-7172b611"
  instance_type = "r5.xlarge"
  monitoring = false
  tags {
    Name = "dl4j"
  }
  vpc_security_group_ids = [
    "${var.default_security_group}",
    "${aws_security_group.ssh_allow.id}"
  ]
  key_name = "${var.ec2_key_name}"
}

resource "aws_security_group" "ssh_allow" {
  name = "ssh_allow"
  description = "Allow SSH Access"
  vpc_id = "${var.vpc_id}"
  ingress {
    from_port = 0
    protocol = "tcp"
    to_port = 22
    cidr_blocks = ["124.41.70.210/32"]
  }
}
