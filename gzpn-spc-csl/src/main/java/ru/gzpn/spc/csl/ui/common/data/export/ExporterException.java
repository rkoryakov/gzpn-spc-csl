package ru.gzpn.spc.csl.ui.common.data.export;

class ExporterException extends RuntimeException {
    ExporterException(String message) {
        super(message);
    }

    ExporterException(String message, Exception e) {
        super(message, e);
    }
}
