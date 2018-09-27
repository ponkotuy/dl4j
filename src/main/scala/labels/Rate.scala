package labels

import java.nio.file.{Path => JPath}

sealed abstract class Rate(val score: Double) extends ClassNameName {
  def path(dir: JPath): JPath = dir.resolve(name)
}

object Rate {
  case object Negative extends Rate(0.15)
  case object Neutral extends Rate(0.85)
  case object Positive extends Rate(1.0)

  val values = Negative :: Neutral :: Positive :: Nil

  def find(score: Double): Rate = values.find(score <= _.score).getOrElse(Positive)
}
