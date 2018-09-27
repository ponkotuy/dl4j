package utils

import java.nio.file.attribute.{BasicFileAttributes, FileAttribute}
import java.nio.file.{CopyOption, Path => JPath, Files => JFiles}
import java.util.function.{BiPredicate, Consumer}

import scala.collection.JavaConverters._

object Files {
  import JFunction._

  def find(path: JPath, depth: Int = Int.MaxValue)(matcher: (JPath, BasicFileAttributes) => Boolean): Iterator[JPath] =
    JFiles.find(path, depth, matcher.asJava).iterator().asScala

  def findFile(path: JPath, depth: Int = Int.MaxValue): Iterator[JPath] =
    find(path, depth) { (_, attr) => !attr.isDirectory }

  def walk(path: JPath, depth: Int = Int.MaxValue): Iterator[JPath] =
    JFiles.walk(path, depth).iterator().asScala

  def probeContentType(path: JPath): String = JFiles.probeContentType(path)
  def move(src: JPath, dest: JPath, options: CopyOption*): JPath = JFiles.move(src, dest, options:_*)
  def mkdirs(src: JPath, attrs: FileAttribute[_]*): JPath = JFiles.createDirectories(src, attrs:_*)
  def createSymbolicLink(link: JPath, src: JPath, attrs: FileAttribute[_]*): JPath =
    JFiles.createSymbolicLink(link, src, attrs:_*)
  def delete(path: JPath): Unit = JFiles.delete(path)

}

object JFunction {
  implicit class BiPredicateAsJava[A, B](val biPred: (A, B) => Boolean) {
    def asJava: BiPredicate[A, B] = new BiPredicate[A, B] {
      override def test(a: A, b: B): Boolean = biPred(a, b)
    }
  }

  implicit class ConsumerAsJava[A](val consumer: A => Unit) {
    def asJava: Consumer[A] = new Consumer[A] {
      override def accept(a: A): Unit = consumer(a)
    }
  }
}
