resource "aws_ecs_task_definition" "dl4j_training" {
  container_definitions = "${file("dl4j_training_container.json")}"
  family = ""
}
