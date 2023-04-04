package org.littletear.dogfile.api

import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir.ZServerEndpoint
import zio.UIO
import zio.macros.accessible

@accessible
trait ComEndPoint {
  def uploadFileEndPoint: UIO[ZServerEndpoint[Any, Any]]

  def checkHealthEndPoint: UIO[ZServerEndpoint[Any, Any]]

  def analyzeEndPoint: UIO[ZServerEndpoint[Any, Any]]

  def checkFileIsAnalyzeComplete: UIO[ZServerEndpoint[Any, Any]]

  def downloadFile: UIO[ZServerEndpoint[Nothing, ZioStreams]]
}
