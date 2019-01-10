package ru.gzpn.spc.csl.ui.common.data.export;

import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Grid;

public class ExcelFileBuilder<T> extends FileBuilder<T> {
    private static final String EXCEL_FILE_EXTENSION = ".xls";

    private Workbook workbook;
    private Sheet sheet;
    private int rowNr;
    private int colNr;
    private Row row;
    private Cell cell;
    private CellStyle boldStyle;

    ExcelFileBuilder(Grid<T> grid) {
        super(grid);
    }

    @Override
    public String getFileExtension() {
        return EXCEL_FILE_EXTENSION;
    }

    @Override
    protected void writeToFile() {
        try {
            workbook.write(new FileOutputStream(file));
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error("Error writing excel file", e);
        }
    }

    @Override
    protected void onNewRow() {
        row = sheet.createRow(rowNr);
        rowNr++;
        colNr = 0;
    }

    @Override
    protected void onNewCell() {
        cell = row.createCell(colNr);
        colNr++;
    }

    @Override
    protected void buildCell(Object value) {
        if (value == null) {
            cell.setCellType(CellType.BLANK);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellType(CellType.BOOLEAN);
        } else if (value instanceof Calendar) {
            Calendar calendar = (Calendar) value;
            cell.setCellValue(calendar.getTime());
            cell.setCellType(CellType.STRING);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            cell.setCellType(CellType.NUMERIC);
        } else {
            cell.setCellValue(value.toString());
            cell.setCellType(CellType.STRING);
        }
    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        buildCell(header);
        cell.setCellStyle(getBoldStyle());
    }

    private CellStyle getBoldStyle() {
        if (boldStyle == null) {
            Font bold = workbook.createFont();
            bold.setBold(true);

            boldStyle = workbook.createCellStyle();
            boldStyle.setFont(bold);
        }
        return boldStyle;
    }

    @Override
    protected void buildFooter() {
        for (int i = 0; i < getNumberOfColumns(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    protected void resetContent() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        colNr = 0;
        rowNr = 0;
        row = null;
        cell = null;
        boldStyle = null;
    }
}
