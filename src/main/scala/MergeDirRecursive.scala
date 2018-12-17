import java.nio.file.Paths

import utils.Files
import scala.collection.JavaConverters._

object MergeDirRecursive {
  def main(args: Array[String]): Unit = {
    args.foreach { fname =>
      val base = Paths.get(fname)
      Files.walk(base).filterNot(Files.isDirectory(_)).foreach { path =>
        val newName = base.relativize(path).iterator().asScala.mkString("_")
        println(base.resolve(newName))
        Files.move(path, base.resolve(newName))
      }
    }
  }
}
