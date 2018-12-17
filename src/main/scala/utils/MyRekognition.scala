package utils

import scala.collection.JavaConverters._
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder
import com.amazonaws.services.rekognition.model.{DetectModerationLabelsRequest, Image}
import org.apache.commons.lang.WordUtils

class MyRekognition {
  val client = AmazonRekognitionClientBuilder.defaultClient()

  def detectModeration(image: Image) = {
    val req = new DetectModerationLabelsRequest
    req.withImage(image)
    req.withMinConfidence(0f)
    client.detectModerationLabels(req).getModerationLabels.asScala
  }
}

sealed abstract class MyModerationLabel {
  def value: Float
  def name: String = WordUtils.capitalizeFully(getClass.getName)
}

object MyModerationLabel {
  case class PartialNudity(value: Float) extends MyModerationLabel
  case class Nudity(value: Float) extends MyModerationLabel
  case class FemaleSwimwearOrUnderwear(value: Float) extends MyModerationLabel
  case class MaleSwimwearOrUnderwear(value: Float) extends MyModerationLabel
  case class Suggestive(value: Float) extends MyModerationLabel
  case class ExplicitNudity(value: Float) extends MyModerationLabel
  case class SexualActivity(value: Float) extends MyModerationLabel
  case class RevealingClothes(value: Float) extends MyModerationLabel
}
