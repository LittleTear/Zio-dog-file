package org.littletear.dogfile.api
import org.littletear.dogfile.domain.{Animal, FileForm, UploadResult}
import org.littletear.dogfile.service.FileApiService
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{CodecFormat, query}
import sttp.tapir.ztapir._
import zio._
import zio.stream.ZStream

import java.io.{File, FileReader, IOException}
import java.nio.charset.StandardCharsets

case class EndPointImpl(fileService:FileApiService[FileForm]) extends ComEndPoint{

  override def uploadFileEndPoint: UIO[ZServerEndpoint[Any, Any]] =
    ZIO.succeed(
      endpoint
        .post
        .in("upload")
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

  override def checkFileIsAnalyzeComplete: UIO[ZServerEndpoint[Any, Any]] = {
    ZIO.succeed(
      endpoint
        .get
        .in("isCompleted" / query[String]("filePath"))
        .out(jsonBody[UploadResult])
        .errorOut(stringBody)
        .zServerLogic { filePath =>
          for{
            isCompleted  <- fileService.fileIsAnalyzed(filePath)
            result       <- if (isCompleted)

                   ZIO.succeed(UploadResult("分析完成", filePath.substring(0, filePath.lastIndexOf("/") + 1) + "result.txt"))
            else   ZIO.succeed(UploadResult("未分析完成", filePath.substring(0, filePath.lastIndexOf("/") + 1) + "result.txt"))
          } yield result
        }
    )
  }

  override def downloadFile: ZIO[Any,Nothing,ZServerEndpoint[Any, ZioStreams]]= {
    ZIO.succeed(
      endpoint
        .get
        .in("download" / query[String]("filePath"))
        .out(header[String]("Content-Type"))
        .out(header[String]("Content-Disposition"))
        .out(streamTextBody(ZioStreams)(CodecFormat.TextPlain(), Some(StandardCharsets.UTF_8)))
        .errorOut(stringBody)
        .zServerLogic {filePath =>
          val fileName = new File(filePath).getName
          val contentType = "application/octet-stream"
          val disposition = s"attachment; filename=$fileName"
          val bStream: ZStream[Any, Throwable, Byte] = ZStream.fromFileName(filePath)
          ZIO.succeed((contentType,disposition,bStream))
        }
    )
  }
}

object EndPointImpl{
  lazy val live: ZLayer[FileApiService[FileForm], Nothing, EndPointImpl] = ZLayer.fromFunction(EndPointImpl.apply _)
}