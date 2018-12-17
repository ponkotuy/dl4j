
name := "dl4j"

scalaVersion := "2.11.12"
val dl4jVersion = "0.9.1"
val awscalaVersion = "0.8.+"

libraryDependencies ++= Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % dl4jVersion,
  "org.deeplearning4j" % "deeplearning4j-nlp" % dl4jVersion,
  "org.deeplearning4j" %% "deeplearning4j-ui" % dl4jVersion,
  "org.nd4j" %% "nd4s" % dl4jVersion,
  "org.nd4j" % "nd4j-native-platform" % dl4jVersion,
  "com.typesafe" % "config" % "1.3.2",
  "com.github.seratch" %% "awscala-s3" % awscalaVersion,
  "com.github.seratch" %% "awscala-ec2" % awscalaVersion,
  "com.github.fracpete" % "rsync4j" % "3.1.2-8",
  "com.google.cloud" % "google-cloud-vision" % "1.55.0",
  "com.amazonaws" % "aws-java-sdk" % "1.11.468"
)

run / fork := true
