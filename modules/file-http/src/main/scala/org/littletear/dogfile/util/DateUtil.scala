package org.littletear.dogfile.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {
  final private  lazy val dayPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  final private  lazy val hourPattern = DateTimeFormatter.ofPattern("HH:mm:ss")
  final private  lazy val now = LocalDateTime.now()

  def getDayDateString: String = now.format(dayPattern)
  def getHourDateString: String = now.format(hourPattern)

}
