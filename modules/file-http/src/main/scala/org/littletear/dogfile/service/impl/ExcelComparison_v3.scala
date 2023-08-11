package org.littletear.dogfile.service.impl
import org.apache.poi.ss.usermodel.{Cell, CellType, Row, Sheet, Workbook}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.{ FileInputStream, FileOutputStream}
import scala.jdk.CollectionConverters._

object ExcelComparison_v3 {
  private val logger = org.slf4j.LoggerFactory.getLogger("ExcelComparison_v3-LOGGER")

  def excelComparisonStart(targetExcelFilePath:String, newExcelFilePath:String, outputExcelFilePath:String): String = {
    //bug：更新之后日期问题，格式问题
    //追加和更新时的序号问题（更新时序号保留，追加时序号用索引）
    //有一些追加不上的问题
    //数字内容添加
    val targetExcelFile = targetExcelFilePath
    //"E:\\测试文件\\工业互联网项目总表.xlsm"
    val newExcelFile = newExcelFilePath
      //"E:\\测试文件\\工业互联网项目总表-庞松-0804.xlsm"

    // 第一步：读取第一个Excel文件中的“项目总表”sheet页内容和表格颜色
    val workbook1: Workbook = readExcel(targetExcelFile)
    val sheet1: Sheet = workbook1.getSheet("1-项目总表")
    // 第二步：读取第二个Excel文件中的“项目总表”sheet页内容
    val workbook2: Workbook = readExcel(newExcelFile)
    val sheet2: Sheet = workbook2.getSheet("1-项目总表")
    // 第三步：对比并更新或追加数据到第一个Excel文件
    compareAndUpdateSheets(sheet1, sheet2)

    // 第五步：输出所有sheet页内容到新的Excel文件中
    val outputFilePath = outputExcelFilePath
      //"E:\\测试文件\\工业互联网项目总表out.xlsm"
    val outputStream = new FileOutputStream(outputFilePath)
    workbook1.write(outputStream)

    outputStream.close()
    workbook1.close()
    workbook2.close()
    outputFilePath
  }

  private def readExcel(filePath: String):Workbook = {
    val fis = new FileInputStream(filePath)
    val workbook = new XSSFWorkbook(fis)
    fis.close()
    workbook
  }

  private def getCellValue(cell: Cell): Either[String, Double] = {
    val cellValue = if (cell != null) {
      cell.getCellType match {
        case CellType.STRING => Left(cell.getStringCellValue)
        case CellType.NUMERIC => Right(cell.getNumericCellValue)
        case CellType.FORMULA => Right(cell.getNumericCellValue)
        case _ => Left("")
      }
    } else {
      Left(" ")
    }
    cellValue
  }


  private def compareAndUpdateSheets(firstSheet: Sheet, secondSheet: Sheet): Unit = {
    //获取sheet1 所有项目简称
    //遍历sheet2每一条与sheet1中进行匹配
    //匹配上则替换掉匹配上的那一条
    //匹配补上则追加到最后
    val firstHeaderRow = firstSheet.getRow(1)
    val secondHeaderRow = secondSheet.getRow(1)

    val projectNameIndex = findColumnIndex(firstHeaderRow, "项目简称")
    logger.info("sheet1项目简称所在的索引位置:" + projectNameIndex.toString)
    val firstRowIterator = firstSheet.iterator()
    firstRowIterator.next()
    firstRowIterator.next()// Skip the header row

    val secondRowIterator = secondSheet.iterator()
    secondRowIterator.next()
    secondRowIterator.next()// Skip the header row

    while (secondRowIterator.hasNext) {
      val secondRow = secondRowIterator.next()
      val projectName = secondRow.getCell(projectNameIndex).getStringCellValue.trim

      val matchingRow: Option[Row] = findMatchingRow(firstSheet, projectName, projectNameIndex)

      if (matchingRow.isDefined) {
        updateRow(matchingRow.get, secondRow)
      } else {
        appendRow(firstSheet, secondRow)
      }
    }

  }

  private def findColumnIndex(headerRow: Row, columnName: String): Int = {
    val cellIterator = headerRow.cellIterator()
    var columnIndex = -1

    while (cellIterator.hasNext && columnIndex == -1) {
      val cell = cellIterator.next()
      val cellValue: String = getCellValue(cell) match {
        case Left(value) => value
        case Right(value) => value.toString
      }
      if ( cellValue== columnName) {
        columnIndex = cell.getColumnIndex
      }
    }
    columnIndex
  }

  private def findMatchingRow(sheet: Sheet, value: String, columnIndex: Int): Option[Row] = {
    val rowIterator = sheet.iterator()
    rowIterator.next()
    rowIterator.next() //跳过表头
    while (rowIterator.hasNext) {
      val row = rowIterator.next()
      val projVal = row.getCell(columnIndex).getStringCellValue.trim
      if (projVal == value) {
        return Some(row)
      }
    }
    None
  }

  private def updateRow(existingRow: Row, newRow: Row): Unit = {
    for (i <- 1 until newRow.getLastCellNum) {
      val existingCell = existingRow.getCell(i)
      val newCell = newRow.getCell(i)
      if (existingCell == null) {
        val newCellStyle = newCell.getCellStyle
        val createCell =  existingRow.createCell(i)
        val createCellStyle = createCell.getCellStyle
//        newCell.setCellStyle(newRow.getCell(i).getCellStyle)
        newCell.getCellType match {
          case CellType.STRING => createCell.setCellValue(getCellValue(newCell).left.get)
          case CellType.NUMERIC => createCell.setCellValue(getCellValue(newCell).right.get)
          case CellType.FORMULA => createCell.setCellValue(getCellValue(newCell).right.get)
          case _ => createCell.setCellValue(getCellValue(newCell).left.get)
        }
//        createCell.setCellValue(getCellValue(newCell))
        createCellStyle.cloneStyleFrom(newCellStyle)
      } else if(newCell == null) {
        existingCell.getCellType match {
          case CellType.STRING => existingCell.setCellValue(getCellValue(newCell).left.get)
          case CellType.NUMERIC => existingCell.setCellValue(getCellValue(newCell).right.get)
          case CellType.FORMULA => existingCell.setCellValue(getCellValue(newCell).right.get)
          case _ => existingCell.setCellValue(getCellValue(newCell).left.get)
        }
//        existingCell.setCellValue(getCellValue(existingCell))
      }
      else {
        val newCellStyle = newCell.getCellStyle
        val existingCellStyle = existingCell.getCellStyle

//        existingCell.setCellStyle(newRow.getCell(i).getCellStyle)
        existingCell.getCellType match {
          case CellType.STRING => existingCell.setCellValue(getCellValue(newCell).left.get)
          case CellType.NUMERIC => existingCell.setCellValue(getCellValue(newCell).right.get)
          case CellType.FORMULA => existingCell.setCellValue(getCellValue(newCell).right.get)
          case _ => existingCell.setCellValue(getCellValue(newCell).left.get)
        }
//        existingCell.setCellValue(getCellValue(newCell))
        existingCellStyle.cloneStyleFrom(newCellStyle)

      }
    }
  }

  private def appendRow(sheet: Sheet, row: Row): Unit = {
    logger.info("append row:"+ row.iterator().asScala.toBuffer.toString)
    val newRow = sheet.createRow(sheet.getLastRowNum + 1)
    for (i <- 0 until row.getLastCellNum) {
      if (i == 0) {
//        println("append row index:"+ sheet.getLastRowNum.toString)
        newRow.createCell(i).setCellValue(sheet.getLastRowNum)
      } else {
        val createCell = newRow.createCell(i)
        val newCell = row.getCell(i)
        if (newCell != null) {
//          println("append row cells:" + getCellValue(newCell))

        //          .setCellValue(getCellValue(row.getCell(i)))
        newCell.getCellType match {
          case CellType.STRING => createCell.setCellValue(getCellValue(newCell).left.get)
          case CellType.NUMERIC => createCell.setCellValue(getCellValue(newCell).right.get)
          case CellType.FORMULA => createCell.setCellValue(getCellValue(newCell).right.get)
          case _ => createCell.setCellValue(getCellValue(newCell).left.get)
        }
      }
      }
    }
  }


  private def removeEmptyRows(sheet:Sheet): Unit = {
    var rowIndex = 1 // Start from the first row (excluding header)
    while (rowIndex <= sheet.getLastRowNum) {
      val row = sheet.getRow(rowIndex)
      var isEmptyRow = true

      if (row != null ) {
        for (cellIndex <- 0 until row.getLastCellNum) {
          val cell = row.getCell(cellIndex)
          if (cell != null && (cell.getCellType != CellType.STRING || cell.getStringCellValue.trim().nonEmpty)) {
            isEmptyRow = false
            // Exit the loop early if any non-blank cell is found in the row
            // This avoids unnecessary checking of other cells in the row
          }
        }
        if (isEmptyRow) {
          sheet.removeRow(row)
          shiftRowsUp(sheet, rowIndex)
        } else {
          rowIndex += 1
        }
      } else {
        rowIndex += 1
      }
    }
  }

  private def shiftRowsUp(sheet: Sheet, rowIndex: Int): Unit = {
    if (rowIndex >= 0 && rowIndex < sheet.getLastRowNum) {
      sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum, -1)
    }
  }


}

