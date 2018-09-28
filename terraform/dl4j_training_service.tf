resource "aws_ecs_service" "dl4j_training" {
  name = "dl4j_training"
  cluster = "${aws_ecs_cluster.default.arn}"
  task_definition = ""
}
