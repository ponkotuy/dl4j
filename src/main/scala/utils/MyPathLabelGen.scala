package utils

import java.net.URI
import java.nio.file.{Paths, Path => JPath}

import labels.Classification
import org.datavec.api.io.labels.PathLabelGenerator
import org.datavec.api.writable.{Text, Writable}

import scala.collection.JavaConverters._

object MyPathLabelGen extends PathLabelGenerator {
  override def getLabelForPath(path: String): Writable = {
    getLabelForPath(Paths.get(path))
  }

  override def getLabelForPath(uri: URI): Writable =
    getLabelForPath(Paths.get(uri))

  def getLabelForPath(path: JPath): Writable = {
    val elems = path.iterator().asScala.map(_.toString)
    val cls = Classification.values.find { v => elems.contains(v.name) }
    new Text(cls.fold("")(_.name))
  }
}
