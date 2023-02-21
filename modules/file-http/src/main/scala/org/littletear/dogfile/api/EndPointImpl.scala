package org.littletear.dogfile.api
import org.littletear.dogfile.domain.{Animal, FileForm, UploadResult}
import org.littletear.dogfile.service.FileApiService
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.query
import sttp.tapir.ztapir._
import zio._

case class EndPointImpl(fileService:FileApiService[FileForm]) extends ComEndPoint{

  override def uploadFileEndPoint: UIO[ZServerEndpoint[Any, Any]] =
    ZIO.succeed(
      endpoint
        .post
        .in("file")
        .in(multipartBody[FileForm])
        .out(jsonBody[UploadResult])
        .errorOut(stringBody)
        .zServerLogic { file =>
          fileService.uploadFile(file)
        }
    )



  override def  checkHealthEndPoint: UIO[ZServerEndpoint[Any, Any]] = {
    ZIO.succeed(
      endpoint
        .post
        .in("check")
        .in(jsonBody[Animal])
        .out(jsonBody[UploadResult])
        .errorOut(stringBody)
        .zServerLogic { _ =>
          ZIO.succeed(UploadResult("获取成功", "check"))
        }
    )

  }

   override def analyzeEndPoint: UIO[ZServerEndpoint[Any, Any]] = {
    ZIO.succeed(
      endpoint
        .get
        .in("analyze" / query[String]("filePath") / query[String]("model"))
        .out(jsonBody[UploadResult])
        .errorOut(stringBody)
        .zServerLogic { tp =>
          val input = tp._1
          val model = tp._2
          Unsafe.unsafe(implicit runtime => Runtime.default.unsafe.runToFuture(fileService.analyzeFile(input,model)))
        ZIO.succeed(UploadResult("正在分析", input.substring(0,input.lastIndexOf("/") + 1) + "result.txt"))
        }
    )

  }
}

object EndPointImpl{
  lazy val live: ZLayer[FileApiService[FileForm], Nothing, EndPointImpl] = ZLayer.fromFunction(EndPointImpl.apply _)
}