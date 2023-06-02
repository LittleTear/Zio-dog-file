package org.littletear.dogfile.service.impl
import spoiwo.model.{Cell, CellStyle, Color, Row, Sheet}
import spoiwo.natures.xlsx.Model2XlsxConversions._
import spoiwo.model._
import spoiwo.model.enums._
import spoiwo.natures.xlsx.Model2XlsxConversions.XlsxSheet

object ExcelCopyToOtherExcel {
  val gettingStartedSheet: Sheet = Sheet(name = "Some serious stuff")
//    .withRows(
//      Row(style = headerStyle).withCellValues("NAME", "BIRTH DATE", "DIED AGED", "FEMALE"),
//      Row().withCellValues("Marie Curie", LocalDate.of(1867, 11, 7), 66, true),
//      Row().withCellValues("Albert Einstein", LocalDate.of(1879, 3, 14), 76, false),
//      Row().withCellValues("Erwin Shrodinger", LocalDate.of(1887, 8, 12), 73, false)
//    )
//    .withColumns(
//      Column(index = 0, style = CellStyle(font = Font(bold = true)), autoSized = true)
//    )
  def main(args: Array[String]): Unit = {
    val pathToFirstExcel = "/path/to/first/excel/file.xlsx"  // 替换为第一个Excel文件路径
    val pathToSecondExcel = "/path/to/second/excel/file.xlsx" // 替换为第二个Excel文件路径

    val firstSheetName = "项目总表"
    val secondSheetName = "项目总表"

    val firstSheet: Sheet = Sheet.fromFile(pathToFirstExcel).getSheetByName(firstSheetName).get
    val secondSheet: Sheet = Sheet.fromFile(pathToSecondExcel).getSheetByName(secondSheetName).get

    val updatedSheet: Sheet = updateExcelSheet(firstSheet, secondSheet)

    // 保存更新后的Sheet对象到一个新的Excel文件
    updatedSheet.saveAs("/path/to/updated/excel/file.xlsx")  // 替换为保存更新后的Excel文件路径
  }

  def updateExcelSheet(firstSheet: Sheet, secondSheet: Sheet): Sheet = {
    val updatedSheet: Sheet = firstSheet.copy()

    val columnToMatch = "项目简称"
    val firstRowsMap = getRowsMapByColumn(updatedSheet, columnToMatch)

    for (secondRow <- secondSheet.rows) {
      val secondCellValue = getCellValue(secondRow, columnToMatch)
      val matchedRowIndexOption = firstRowsMap.get(secondCellValue)

      matchedRowIndexOption match {
        case Some(matchedRowIndex) =>
          val matchedRow = updatedSheet.getRow(matchedRowIndex)
          val updatedRow = updateRow(matchedRow, secondRow)
          updatedSheet.updateRow(updatedRow)

        case None =>
          updatedSheet.addRow(secondRow)
      }
    }

    updatedSheet
  }

  def getRowsMapByColumn(sheet: Sheet, columnName: String): Map[Any, Int] = {
    val columnIndex = sheet.columnIndex(columnName)
    sheet.rows.zipWithIndex.map { case (row, rowIndex) =>
      val cellValue = getCellValue(row, columnIndex)
      cellValue -> rowIndex
    }.toMap
  }

  def getCellValue(row: Row, columnIndex: Int): Any = {
    row.cells(columnIndex).value
  }

  def updateRow(existingRow: Row, newRow: Row): Row = {
    val updatedCells: Seq[Cell] = existingRow.cells.zip(newRow.cells).map { case (existingCell, newCell) =>
      val newStyle: Option[CellStyle] = existingCell.style.map(style => style.copy())
      newCell.copy(style = newStyle)
    }

    existingRow.copy(cells = updatedCells)
  }
}
