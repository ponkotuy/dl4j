resource "aws_ecs_service" "dl4j_training" {
  name = "dl4j_training"
  cluster = "${aws_ecs_cluster.default.arn}"
  task_definition = "${aws_ecs_task_definition.dl4j_training.arn}"
  launch_type = "FARGATE"
  network_configuration {
    subnets = [
      "subnet-5c704628",
      "subnet-fcb2e4ba",
      "subnet-0bfb8923"
    ]
  }
}
