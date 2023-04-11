package org.littletear.dogfile.domain

case class WriteResultRow(input: String, ner: String, intent: String,
                          intent_property: String, entities_list: String,skillResult: String,
                          model_time_cost: String, align_time_cost: String
                          )
