package org.littletear.dogfile.domain

import sttp.model.Part
import sttp.tapir.TapirFile


case class FileForm(fileField: Part[TapirFile]) extends Serializable
