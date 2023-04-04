package org.littletear.dogfile.service
import org.littletear.dogfile.domain.UploadResult
import zio._
trait FileApiService[T]{

  def uploadFile(fileForm:T):UIO[UploadResult]

  def analyzeFile(filePath:String,model:String): ZIO[Any, Throwable, Unit]

  def fileIsAnalyzed(filePath:String): UIO[Boolean]
}
