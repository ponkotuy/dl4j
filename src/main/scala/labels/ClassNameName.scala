package labels

import com.google.common.base.CaseFormat

trait ClassNameName {
  def name: String =
    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.getClass.getSimpleName).init
}
