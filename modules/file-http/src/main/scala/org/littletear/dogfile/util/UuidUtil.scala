package org.littletear.dogfile.util
import zio._

import java.util.UUID

object UuidUtil {
  /** 64位随机UUID
   */
  def getUuid64: String = (UUID.randomUUID.toString + UUID.randomUUID.toString).replace("-", "")

  /** 32位随机UUID
   */
  def getUuid32: String =
    UUID.randomUUID.toString.replace("-", "")

}