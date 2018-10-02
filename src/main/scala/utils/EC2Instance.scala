package utils

import awscala._
import awscala.ec2._

class EC2Instance(id: String, region: Region = Region.Oregon) {
  implicit val ec2 = EC2.at(region)

  private val instance = ec2.instances(id).head

  def stop() = instance.stop()
}
