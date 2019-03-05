package ru.gzpn.spc.csl.ui.common.data.imp;

import java.io.InputStream;
import java.util.Optional;

public class LocalEstimateExcelParser extends ExcelParser {

	public LocalEstimateExcelParser(InputStream inp) {
		super(inp);
	}

	public Optional<String> getDrawing(int row) {
		return getCellTextValue(row, 0);
	}
}
