package org.littletear.dogfile.util

import io.circe._
import io.circe.parser._
import org.littletear.dogfile.domain.WriteResultRow
import zio._

object JsonUtil {
  def parse2WriteRow(jsonStr: String): ZIO[Any, Nothing, WriteResultRow] = {
    ZIO.succeed {
      val doc: Json = parse(jsonStr).getOrElse(Json.Null)
      val cursor: HCursor = doc.hcursor
      val input: String = cursor.downField("input").as[String].getOrElse("")
      val ner: String = cursor.downField("nluResult").downField("rawOutput").downField("intent_entity_re").downField("ner").as[Json].getOrElse(Json.Null).noSpaces
      val intent: String = cursor.downField("nluResult").downField("rawOutput").downField("intent_entity_re").downField("intent").as[String].getOrElse("")
      val intent_property: String = cursor.downField("nluResult").downField("rawOutput").downField("intent_property").as[Json].getOrElse(Json.Null).noSpaces
      val entities_list: String = cursor.downField("nluResult").downField("rawOutput").downField("entities_list").as[Json].getOrElse(Json.Null).noSpaces
      val skillResult = cursor.downField("skillResult").downField("data").as[Json].getOrElse(Json.Null).noSpaces
      val model_time_cost: String = cursor.downField("nluResult").downField("rawOutput").downField("model_time_cost").as[String].getOrElse("")
      val align_time_cost: String = cursor.downField("nluResult").downField("rawOutput").downField("align_time_cost").as[String].getOrElse("")
      WriteResultRow(input, ner, intent,
        intent_property, entities_list, skillResult,
        model_time_cost, align_time_cost
      )
    }

  }
}
