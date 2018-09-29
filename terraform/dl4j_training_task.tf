resource "aws_ecs_task_definition" "dl4j_training" {
  family = "dl4j_training"
  container_definitions = "${file("dl4j_training_container.json")}"
  requires_compatibilities = ["FARGATE"]
  network_mode = "awsvpc"
  execution_role_arn = "arn:aws:iam::632747955816:role/dl4j-learning"
  cpu = "4096"
  memory = "30720"
}
