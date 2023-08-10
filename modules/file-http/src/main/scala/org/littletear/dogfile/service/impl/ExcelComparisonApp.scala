//package org.littletear.dogfile.service.impl
//
//import org.apache.poi.ss.usermodel.Sheet
//import org.apache.poi.ss.usermodel.{CellStyle, WorkbookFactory}
//
//import java.io.{File, FileOutputStream}
//
//object ExcelComparisonApp {
//  def main(args: Array[String]): Unit = {
//    // Step 1: Read the first Excel file
//    val firstFilePath = "path_to_first_excel.xlsx"
//    val secondFilePath = "path_to_second_excel.xlsx"
//
//    val firstWorkbook = WorkbookFactory.create(new File(firstFilePath))
//    val secondWorkbook = WorkbookFactory.create(new File(secondFilePath))
//
//    val firstSheetName = "项目总表"
//    val firstSheet = firstWorkbook.getSheet(firstSheetName)
//    val firstSheetColorMap = extractCellColors(firstSheet)
//
//    // Step 2-4: Compare and update rows
//    val secondSheet = secondWorkbook.getSheet(firstSheetName)
//    val newFirstSheet = updateFirstSheetRows(firstSheet, secondSheet)
//
//    // Step 5: Create a new Excel file with the same colors
//    val newWorkbook = WorkbookFactory.create(true) // Create a new workbook with the same colors
//
//    val newFirstSheetCopy = newWorkbook.createSheet(firstSheetName)
//    copySheet(newFirstSheet, newFirstSheetCopy)
//
//    // Save the new Excel file
//    val newExcelFilePath = "path_to_new_excel.xlsx"
//    val newExcelFileOutputStream = new FileOutputStream(newExcelFilePath)
//    newWorkbook.write(newExcelFileOutputStream)
//    newExcelFileOutputStream.close()
//
//    // Clean up resources
//    firstWorkbook.close()
//    secondWorkbook.close()
//    newWorkbook.close()
//  }
//
//  def extractCellColors(sheet: Sheet): Map[String, CellStyle] = {
//    // 提取并存储单元格颜色
//    val colorMap = scala.collection.mutable.Map[String, CellStyle]()
//    for (row <- sheet) {
//      for (cell <- row) {
//        colorMap(cell.getAddress.toString) = cell.getCellStyle
//      }
//    }
//    colorMap.toMap
//  }
//
//  def updateFirstSheetRows(firstSheet: Sheet, secondSheet: Sheet): Sheet = {
//    // 比较并更新行
//    val newFirstSheet = firstSheet.getWorkbook.createSheet()
//
//    for (i <- 0 until firstSheet.getLastRowNum + 1) {
//      val firstRow = firstSheet.getRow(i)
//      val secondRow = findMatchingRow(firstRow, secondSheet)
//
//      val newRow = if (secondRow != null) copyRow(secondRow, newFirstSheet.createRow(i)) else newFirstSheet.createRow(i)
//
//      copyRowStyle(firstRow, newRow)
//    }
//
//    newFirstSheet
//  }
//
//  def findMatchingRow(targetRow: Row, sheet: Sheet): Row = {
//    // 根据"项目简称"列查找匹配的行
//    val targetCellValue = targetRow.getCell(targetCellIndex).getStringCellValue
//
//    for (row <- sheet) {
//      val cellValue = row.getCell(targetCellIndex).getStringCellValue
//      if (cellValue == targetCellValue) {
//        return row
//      }
//    }
//
//    null
//  }
//
//  def copyRow(sourceRow: Row, targetRow: Row): Row = {
//    // 复制行数据
//    for (i <- 0 until sourceRow.getLastCellNum) {
//      val sourceCell = sourceRow.getCell(i)
//      val targetCell = targetRow.createCell(i)
//      targetCell.setCellValue(sourceCell.getStringCellValue)
//    }
//
//    targetRow
//  }
//
//  def copyRowStyle(sourceRow: Row, targetRow: Row): Unit = {
//    // 复制行样式
//    val targetWorkbook = targetRow.getSheet.getWorkbook
//    val targetCellStyle = targetWorkbook.createCellStyle()
//
//    for (i <- 0 until sourceRow.getLastCellNum) {
//      val sourceCell = sourceRow.getCell(i)
//      val sourceCellStyle = sourceCell.getCellStyle
//
//      targetCellStyle.cloneStyleFrom(sourceCellStyle)
//      targetRow.getCell(i).setCellStyle(targetCellStyle)
//    }
//  }
//
//  def copySheet(sourceSheet: Sheet, targetSheet: Sheet): Unit = {
//    // 复制数据和格式从sourceSheet到targetSheet
//    val targetWorkbook = targetSheet.getWorkbook
//
//    // 复制数据
//    for (i <- 0 until sourceSheet.getLastRowNum + 1) {
//      val sourceRow = sourceSheet.getRow(i)
//      val targetRow = targetSheet.createRow(i)
//
//      for (j <- 0 until sourceRow.getLastCellNum) {
//        val sourceCell = sourceRow.getCell(j)
//        val targetCell = targetRow.createCell(j)
//
//        if (sourceCell != null) {
//          copyCellValue(sourceCell, targetCell)
//          copyCellStyle(sourceCell, targetCell, targetWorkbook)
//        }
//      }
//    }
//  }
//
//  def copyCellValue(sourceCell: Cell, targetCell: Cell): Unit = {
//    // 复制单元格值
//    targetCell.setCellType(sourceCell.getCellType)
//
//    sourceCell.getCellType match {
//      case CellType.BOOLEAN => targetCell.setCellValue(sourceCell.getBooleanCellValue)
//      case CellType.NUMERIC => targetCell.setCellValue(sourceCell.getNumericCellValue)
//      case CellType.STRING => targetCell.setCellValue(sourceCell.getStringCellValue)
//      case CellType.FORMULA => targetCell.setCellFormula(sourceCell.getCellFormula)
//      case CellType.BLANK => // No action needed for blank cells
//      case CellType.ERROR => targetCell.setCellValue(sourceCell.getErrorCellValue.toString)
//    }
//  }
//
//  def copyCellStyle(sourceCell: Cell, targetCell: Cell, targetWorkbook: Workbook): Unit = {
//    // 复制单元格样式
//    val sourceCellStyle = sourceCell.getCellStyle
//    val targetCellStyle = targetWorkbook.createCellStyle()
//    targetCellStyle.cloneStyleFrom(sourceCellStyle)
//
//    targetCell.setCellStyle(targetCellStyle)
//  }
//}
//
