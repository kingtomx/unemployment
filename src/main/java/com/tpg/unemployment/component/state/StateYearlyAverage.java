package com.tpg.unemployment.component.state;

import com.tpg.unemployment.component.converter.FileConverter;
import com.tpg.unemployment.config.UnemploymentConfigurationProperties;
import com.tpg.unemployment.dto.state.YearlyStates;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class StateYearlyAverage {

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private UnemploymentConfigurationProperties unemploymentConfigurationProperties;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public String getCSV(String file, int start, int end) {
        YearlyStates yearlyStates = fileConverter.extractData(file, unemploymentConfigurationProperties.getValidCsvRow(), unemploymentConfigurationProperties.getMonthNames());
        return fileConverter.generateResult(yearlyStates, start, end);
    }



    private ByteArrayOutputStream returnAsCSV(XSSFWorkbook workbook, String sheetName) {
        CSVPrinter csvPrinter = null;
        ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();

        try {
            csvPrinter = new CSVPrinter(new OutputStreamWriter(os), CSVFormat.DEFAULT);
            if (workbook != null) {
                XSSFSheet sheet = workbook.getSheet(sheetName); // Sheet #0 in this example
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        csvPrinter.print(cell.getStringCellValue());
                    }
                    csvPrinter.println();
                }
            }

        } catch (Exception e) {
            log.error("Failed to write CSV file to output stream", e);
        } finally {
            try {
                if (csvPrinter != null) {
                    csvPrinter.flush();
                    csvPrinter.close();
                }
            }
            catch (IOException ioe) {
                log.error("Error when closing CSV Printer", ioe);
            }
        }

        return os;
    }



}
