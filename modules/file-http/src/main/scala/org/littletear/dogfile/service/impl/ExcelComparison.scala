package org.littletear.dogfile.service.impl
import org.apache.poi.ss.usermodel.{Cell, CellType, FillPatternType, Sheet, WorkbookFactory}

import java.io.{File, FileOutputStream}
import java.text.SimpleDateFormat
import scala.collection.mutable
object ExcelComparison {
  def main(args: Array[String]): Unit = {
    val sourceExcelFile = "E:\\测试文件\\工业互联网项目总表.xlsm"
    val targetExcelFile = "E:\\测试文件\\工业互联网项目总表ljc.xlsm"
    val outputExcelFile = "E:\\测试文件\\工业互联网项目总表out.xlsm"

    // 第一步：读取第一个Excel文件中的“项目总表”sheet页内容和表格颜色
    val sourceData = readExcelData(sourceExcelFile, "1-项目总表")
    println(sourceData.toString())
    val sourceColors = readExcelCellColors(sourceExcelFile, "1-项目总表")

    // 第二步：读取第二个Excel文件中的“项目总表”sheet页内容
    val targetData = readExcelData(targetExcelFile, "1-项目总表")

    // 第三步：对比并更新或追加数据到第一个Excel文件
    val updatedData = compareAndUpdateData(sourceData, targetData)

    // 第四步：追加未匹配数据到第一个Excel文件
    val unmatchedData = targetData.filterNot(row => sourceData.exists(_.get("项目简称") == row.get("项目简称")))
    val updatedDataWithUnmatched = updatedData ++ unmatchedData

    // 第五步：输出所有sheet页内容到新的Excel文件中
    writeExcelData(outputExcelFile, "2-项目总表", updatedDataWithUnmatched, sourceColors)
  }

  def readExcelData(file: String, sheetName: String): List[Map[String, String]] = {
    // 使用Apache POI库读取Excel文件，解析数据，返回每一行数据作为一个Map
    // 每个Map表示一行数据，Map的键为列名，值为单元格内容
    // 参考：https://poi.apache.org/apidocs/4.1/org/apache/poi/ss/usermodel/Workbook.html
    // 返回结果示例：List(Map("项目简称" -> "项目1", "项目名称" -> "示例项目1", ...), Map("项目简称" -> "项目2", "项目名称" -> "示例项目2", ...))
    val workbook = WorkbookFactory.create(new File(file))
    val sheet: Sheet = workbook.getSheet(sheetName)

    // 获取表头
    val headerRow = sheet.getRow(1)

    val headers = (0 until headerRow.getLastCellNum).map{col =>
      val header_cell = headerRow.getCell(col)
      val sdf = new SimpleDateFormat("yyyy/M/d");

      header_cell.getCellType match {
        case CellType.STRING => header_cell.getStringCellValue
        case CellType.NUMERIC => sdf.format(header_cell.getDateCellValue)
        case CellType.FORMULA => sdf.format(header_cell.getDateCellValue)
        case _ => " "
      }
    }
    // 读取每一行数据并转化为Map
    val data = (2 to sheet.getLastRowNum).map { rowIndex =>
      val row = sheet.getRow(rowIndex)
      val rowData = (0 until row.getLastCellNum).map { colIndex =>
        val cell: Cell = row.getCell(colIndex)
        val cellValue = if (cell != null) {cell.getCellType match {
          case CellType.STRING => cell.getStringCellValue
          case CellType.NUMERIC => cell.getNumericCellValue.toString
          case CellType.FORMULA => cell.getNumericCellValue.toString
          case _ => ""
        }} else {
          " "
        }
        (headers(colIndex), cellValue)
      }
      rowData.toMap
    }
    data.toList
  }

  def readExcelCellColors(file: String, sheetName: String): Map[(Int, Int), Short] = {
    // 使用Apache POI库读取Excel文件，解析每个单元格的颜色，返回一个Map
    // Map的键为元组，表示单元格的坐标（行号，列号），值为单元格的颜色代码
    // 参考：https://poi.apache.org/apidocs/4.1/org/apache/poi/ss/usermodel/Workbook.html
    // 返回结果示例：Map((0, 0) -> 32767, (0, 1) -> 16776960, ...)
    val workbook = WorkbookFactory.create(new File(file))
    val sheet = workbook.getSheet(sheetName)

    val colors: mutable.Map[(Int, Int), Short] = scala.collection.mutable.Map[(Int, Int), Short]()

    // 遍历所有行和列，获取单元格的颜色
    for (rowIndex <- 0 to sheet.getLastRowNum) {
      val row = sheet.getRow(rowIndex)
      for (colIndex <- 0 until row.getLastCellNum) {
        val cell = row.getCell(colIndex)
        if (cell != null) {colors((rowIndex, colIndex)) = cell.getCellStyle.getFillForegroundColor} else colors((rowIndex, colIndex)) = 0.toShort
      }
    }

    colors.toMap
  }

  def compareAndUpdateData(
                            sourceData: List[Map[String, String]],
                            targetData: List[Map[String, String]]
                          ): List[Map[String, String]] = {
    // 按照"项目简称"列进行匹配，如果匹配上，将第二个Excel中匹配上的那一行内容覆盖到第一个Excel中的那一行中
    // 返回结果示例：List(Map("项目简称" -> "项目1", "项目名称" -> "示例项目1", ...), Map("项目简称" -> "项目2", "项目名称" -> "示例项目2", ...))
    sourceData.map { sourceRow =>
      val matchingRowOpt = targetData.find(_.getOrElse("项目简称", "") == sourceRow.getOrElse("项目简称", ""))
      matchingRowOpt.map { matchingRow =>
        sourceRow ++ matchingRow // 合并两行数据
      }.getOrElse(sourceRow) // 如果没有匹配，则保留原来的数据
    }
  }

  def writeExcelData(
                      file: String,
                      sheetName: String,
                      data: List[Map[String, String]],
                      cellColors: Map[(Int, Int), Short]
                    ): Unit = {
    // 使用Apache POI库创建新的Excel文件，并将数据和颜色写入到指定sheet页中
    // 参考：https://poi.apache.org/apidocs/4.1/org/apache/poi/ss/usermodel/Workbook.html
    val workbook = WorkbookFactory.create(new File(file))
    val sheet = workbook.createSheet(sheetName)

    // 写入数据
    data.zipWithIndex.foreach { case (row, rowIndex) =>
      val excelRow = sheet.createRow(rowIndex)
      row.zipWithIndex.foreach { case ((header, cellValue), colIndex) =>
        val cell = excelRow.createCell(colIndex)
        cell.setCellValue(cellValue)
      }
    }

    // 设置单元格颜色
    cellColors.foreach { case ((rowIndex, colIndex), color) =>
      val excelRow = sheet.getRow(rowIndex) match {
        case null => sheet.createRow(rowIndex)
        case existingRow => existingRow
      }
      val cell = excelRow.createCell(colIndex)
      val cellStyle = workbook.createCellStyle()
      cellStyle.setFillForegroundColor(color)
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
      cell.setCellStyle(cellStyle)
    }

    // 保存数据到文件
    val fileOut = new FileOutputStream(file)
    workbook.write(fileOut)
    fileOut.close()

    workbook.close()
  }
}
