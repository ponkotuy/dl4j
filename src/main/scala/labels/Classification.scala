package labels

import java.nio.file.{Path => JPath}

import utils.MyConfig

sealed abstract class Classification(val name: String) {
  def path: JPath = MyConfig.path.imagesPath.resolve(name)
}

object Classification {
  case object NonH extends Classification("nonh")
  case object Ero extends Classification("ero")
  case object Title extends Classification("title")

  val values = NonH :: Ero :: Title :: Nil
  val names = values.map(_.name)

  def findName(name: String): Option[Classification] = values.find(_.name == name)
}
