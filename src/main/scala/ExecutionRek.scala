import java.nio.ByteBuffer
import java.nio.file.Paths
import java.util.concurrent.Executors

import com.amazonaws.services.rekognition.model.Image
import utils.{Files, MyRekognition}

import scala.concurrent.{ExecutionContext, Future}

object ExecutionRek {
  def main(args: Array[String]): Unit = {
    val client = new MyRekognition
    val es = Executors.newFixedThreadPool(4)
    implicit val ec = ExecutionContext.fromExecutorService(es)
    args.foreach { fname =>
      Future {
        val image = loadImage(fname)
        val res = client.detectModeration(image)
        println(s"${fname}: ${res.map{ x => x.getName -> x.getConfidence.formatted("%.1f") }.toMap}")
      }
    }
  }

  def loadImage(fname: String): Image = {
    val path = Paths.get(fname)
    val bb = ByteBuffer.wrap(Files.readAllBytes(path))
    (new Image).withBytes(bb)
  }
}
