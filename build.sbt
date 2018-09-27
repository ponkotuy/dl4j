
scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % "0.9.1",
  "org.deeplearning4j" % "deeplearning4j-nlp" % "0.9.1",
  "org.deeplearning4j" %% "deeplearning4j-ui" % "0.9.1",
  "org.nd4j" %% "nd4s" % "0.9.1",
  "org.nd4j" % "nd4j-native-platform" % "0.9.1",
  "com.typesafe" % "config" % "1.3.2"
)

run / fork := true
run / javaOptions ++= Seq(
  "-Xmx2G",
  "-Xms1G",
  "-Dorg.bytedeco.javacpp.maxbytes=8G",
  "-Dorg.bytedeco.javacpp.maxphysicalbytes=8G"
)
