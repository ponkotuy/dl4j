package utils

import com.google.common.base.CaseFormat

sealed abstract class GoogleValue(val value: Int) {
  lazy val name: String = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.getClass.getSimpleName).init
}

object GoogleValue {
  case object VeryUnlikely extends GoogleValue(1)
  case object Unlikely extends GoogleValue(2)
  case object Possible extends GoogleValue(3)
  case object Likely extends GoogleValue(4)
  case object VeryLikely extends GoogleValue(5)

  val values = VeryUnlikely :: Unlikely :: Possible :: Likely :: VeryLikely :: Nil

  def find(name: String): Option[GoogleValue] = values.find(_.name == name)
}
