package labels

import java.nio.file.{Path => JPath}

import utils.Path

sealed abstract class Classification(val name: String) {
  def path: JPath = Path.ImagesPath.resolve(name)
}

object Classification {
  case object NonH extends Classification("nonh")
  case object Ero extends Classification("ero")

  val values = NonH :: Ero :: Nil
  val names = values.map(_.name)

  def findName(name: String): Option[Classification] = values.find(_.name == name)
}
