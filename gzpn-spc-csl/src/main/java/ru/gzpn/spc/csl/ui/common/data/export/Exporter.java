package ru.gzpn.spc.csl.ui.common.data.export;

import java.io.InputStream;

import com.vaadin.ui.Grid;

public class Exporter {

    private Exporter(){}

    public static <T> InputStream exportAsExcel(Grid<T> grid){
        return new ExcelFileBuilder<>(grid).build();
    }

    public static <T> InputStream exportAsCSV(Grid<T> grid){
        return new CSVFileBuilder<>(grid).build();
    }
}
