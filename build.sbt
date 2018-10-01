
name := "dl4j"

scalaVersion := "2.11.12"
val dl4jVersion = "0.9.1"
val awsScalaVersion = "8.0.3"

libraryDependencies ++= Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % dl4jVersion,
  "org.deeplearning4j" % "deeplearning4j-nlp" % dl4jVersion,
  "org.deeplearning4j" %% "deeplearning4j-ui" % dl4jVersion,
  "org.nd4j" %% "nd4s" % dl4jVersion,
  "org.nd4j" % "nd4j-native-platform" % dl4jVersion,
  "com.typesafe" % "config" % "1.3.2",
  "com.github.seratch" %% "awscala-s3" % "0.8.+",
  "com.github.fracpete" % "rsync4j" % "3.1.2-8"
)

run / fork := true
run / javaOptions ++= Seq(
  "-Xmx2G",
  "-Xms1G",
  "-Dorg.bytedeco.javacpp.maxbytes=8G",
  "-Dorg.bytedeco.javacpp.maxphysicalbytes=8G"
)
