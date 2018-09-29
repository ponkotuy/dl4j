import com.amazonaws.regions.{Region, Regions}

name := "dl4j"
organization := "com.ponkotuy"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % "0.9.1",
  "org.deeplearning4j" % "deeplearning4j-nlp" % "0.9.1",
  "org.deeplearning4j" %% "deeplearning4j-ui" % "0.9.1",
  "org.nd4j" %% "nd4s" % "0.9.1",
  "org.nd4j" % "nd4j-native-platform" % "0.9.1",
  "com.typesafe" % "config" % "1.3.2",
  "com.github.seratch" %% "awscala-s3" % "0.8.+"
)

run / fork := true
run / javaOptions ++= Seq(
  "-Xmx2G",
  "-Xms1G",
  "-Dorg.bytedeco.javacpp.maxbytes=27G",
  "-Dorg.bytedeco.javacpp.maxphysicalbytes=27G"
)

// Docker
enablePlugins(EcrPlugin)
enablePlugins(JavaAppPackaging)
dockerUpdateLatest := true
Docker / packageName := "dl4j"
Ecr / repositoryName := (packageName in Docker).value
Ecr / region := Region.getRegion(Regions.AP_NORTHEAST_1)
Ecr / login := ((login in Ecr) dependsOn (createRepository in Ecr)).value
Ecr / push := ((push in Ecr) dependsOn (publishLocal in Docker, login in Ecr)).value
