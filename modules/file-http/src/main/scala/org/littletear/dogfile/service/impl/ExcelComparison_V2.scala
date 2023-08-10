//package org.littletear.dogfile.service.impl
//
//import org.apache.poi.ss.usermodel._
//import org.apache.poi.xssf.usermodel.XSSFWorkbook
//import java.io.{FileInputStream, FileOutputStream}
//
//object ExcelComparison_V2 {
//  def main(args: Array[String]): Unit = {
//    // 第一步：读取第一个Excel文件中的“项目总表”sheet页内容
//    val inputFilePath1 = "path_to_first_excel.xlsx"
//    val sheetName1 = "项目总表"
//    val workbook1 = readExcel(inputFilePath1)
//    val sheet1 = workbook1.getSheet(sheetName1)
//
//    // 第二步：读取第二个Excel文件中的“项目总表”sheet页内容
//    val inputFilePath2 = "path_to_second_excel.xlsx"
//    val workbook2 = readExcel(inputFilePath2)
//    val sheet2 = workbook2.getSheet(sheetName1)
//
//    // 进行对比和处理
//    val updatedWorkbook = compareAndUpdateSheets(sheet1, sheet2)
//
//    // 将更新后的结果写入新的Excel文件
//    val outputFilePath = "output_path.xlsx"
//    writeExcel(updatedWorkbook, outputFilePath)
//
//    println("对比和更新完成")
//  }
//
//  // 读取Excel文件
//  def readExcel(filePath: String): Workbook = {
//    val fis = new FileInputStream(filePath)
//    val workbook = new XSSFWorkbook(fis)
//    fis.close()
//    workbook
//  }
//
//  // 根据项目简称进行对比和更新
//  def compareAndUpdateSheets(sheet1: Sheet, sheet2: Sheet): Workbook = {
//    val updatedWorkbook = new XSSFWorkbook()
//    val updatedSheet = updatedWorkbook.createSheet(sheet1.getSheetName)
//
//    // 复制第一个Excel文件的内容到新的工作表
//    for (rowIndex <- 0 until sheet1.getPhysicalNumberOfRows) {
//      val row = updatedSheet.createRow(rowIndex)
//      val sourceRow = sheet1.getRow(rowIndex)
//      if (sourceRow != null) {
//        copyRow(sourceRow, row)
//      }
//    }
//
//    // 遍历第二个Excel文件的内容，与第一个工作表中的内容进行比较和更新
//    for (rowIndex <- 0 until sheet2.getPhysicalNumberOfRows) {
//      val sourceRow = sheet2.getRow(rowIndex)
//      if (sourceRow != null) {
//        val projectAbbreviation = getCellValue(sourceRow.getCell(0)) // 假设项目简称在第一列
//        val matchingRow = findMatchingRow(updatedSheet, projectAbbreviation)
//
//        if (matchingRow.isDefined) {
//          // 如果找到匹配的行，覆盖第一个工作表中的对应行内容
//          copyRow(sourceRow, matchingRow.get)
//        } else {
//          // 如果未找到匹配的行，将内容追加到第一个工作表的末尾行
//          val newRow = updatedSheet.createRow(updatedSheet.getLastRowNum + 1)
//          copyRow(sourceRow, newRow)
//        }
//      }
//    }
//  }
//    // 写入Excel文件
//    def writeExcel(workbook: Workbook, filePath: String): Unit = {
//      val fos = new FileOutputStream(filePath)
//      workbook.write(fos)
//      fos.close()
//    }
//
//    // 复制行内容
//    def copyRow(sourceRow: Row, targetRow: Row): Unit = {
//      for (cellIndex <- 0 until sourceRow.getPhysicalNumberOfCells) {
//        val sourceCell = sourceRow.getCell(cellIndex)
//        val targetCell = targetRow.createCell(cellIndex)
//        if (sourceCell != null) {
//          targetCell.setCellValue(getCellValue(sourceCell))
//        }
//      }
//    }
//
//    // 获取单元格的值
//    def getCellValue(cell: Cell): String = {
//      cell.getCellType match {
//        case CellType.STRING => cell.getStringCellValue
//        case CellType.NUMERIC => cell.getNumericCellValue.toString
//        case CellType.BOOLEAN => cell.getBooleanCellValue.toString
//        case _ => ""
//      }
//    }
//
//    // 根据项目简称查找匹配的行
//    def findMatchingRow(sheet: Sheet, projectAbbreviation: String): Option[Row] = {
//      for (rowIndex <- 0 until sheet.getPhysicalNumberOfRows) {
//        val row = sheet.getRow(rowIndex)
//        if (row != null) {
//          val abbreviationCell = row.getCell(0) // 假设项目简称在第一列
//          if (abbreviationCell != null && getCellValue(abbreviationCell) == projectAbbreviation) {
//            return Some(row)
//          }
//        }
//      }
//      None
//    }
//
//}

