package org.littletear.dogfile.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtil {
  final private  lazy val dayPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  final private  lazy val hourPattern = DateTimeFormatter.ofPattern("HH:mm:ss")

  def getDayDateString(now: ZonedDateTime = ZonedDateTime.now()): String = now.format(dayPattern)
  def getHourDateString(now: ZonedDateTime = ZonedDateTime.now()): String = now.format(hourPattern)

}
