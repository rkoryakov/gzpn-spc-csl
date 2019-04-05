package ru.gzpn.spc.csl.ui.common.data.imp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExcelParser {
	public static final Logger logger = LoggerFactory.getLogger(ExcelParser.class);
	protected Workbook workbook;
	protected Sheet sheet;
	protected int lastRow;
	
	public ExcelParser(InputStream inp) {
		 try {
			workbook = WorkbookFactory.create(inp);
			sheet = workbook.getSheetAt(0);
			lastRow = sheet.getLastRowNum();
		} catch (EncryptedDocumentException | IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public int getLastRowNumber() {
		return lastRow;
	}
	
	public Cell getCell(int row, int col) {
		Cell result = null;
		Row r = sheet.getRow(row);
		result = r.getCell(col, MissingCellPolicy.RETURN_BLANK_AS_NULL);
		return result;
	}
	
	public Optional<String> getCellTextValue(int row, int col) {
		Optional<String> result = Optional.empty();
		Cell cell = getCell(row, col);
		
		if (cell != null) {
			DataFormatter formatter = new DataFormatter();
			String text = formatter.formatCellValue(cell);
			result = Optional.ofNullable(text);
		}
		
		return result;
	}
	
	public Optional<BigDecimal> getCellNumValue(int row, int col) {
		Optional<BigDecimal> result = Optional.empty();
		Cell cell = getCell(row, col);
		
		if (cell != null) {
			BigDecimal value = new BigDecimal(cell.getNumericCellValue());
			result = Optional.ofNullable(value);
		}
		
		return result;
	}
}
