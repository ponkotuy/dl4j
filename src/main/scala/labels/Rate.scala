package labels

import java.nio.file.{Path => JPath}

import com.google.cloud.vision.v1.SafeSearchAnnotation

sealed abstract class Rate extends ClassNameName {
  def path(dir: JPath): JPath = dir.resolve(name)
}

object Rate {
  case object Negative extends Rate
  case object Neutral extends Rate
  case object Positive extends Rate

  val values = Negative :: Neutral :: Positive :: Nil

  def fromScore(score: Double): Rate = {
    if(score < 0.1) Negative
    else if(score < 0.5) Neutral
    else Positive
  }

  def fromCV(anno: SafeSearchAnnotation): Rate = {
    val adult = anno.getAdultValue
    val racy = anno.getRacyValue
    if(racy == 5) {
      adult match {
        case 5 => Positive
        case 4 | 3 => Neutral
        case _ => Negative
      }
    } else Negative
  }
}
