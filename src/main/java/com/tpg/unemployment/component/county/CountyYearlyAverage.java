package com.tpg.unemployment.component.county;

import com.tpg.unemployment.component.converter.FileConverter;
import com.tpg.unemployment.config.UnemploymentConfigurationProperties;
import com.tpg.unemployment.dto.county.CountyRow;
import com.tpg.unemployment.dto.county.YearlyCounties;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class CountyYearlyAverage {

    @Autowired
    private FileConverter fileConverter;

    @Autowired
    private UnemploymentConfigurationProperties unemploymentConfigurationProperties;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public String getCSV(String file) {
        return format(fileConverter.extractDataCounties(file, unemploymentConfigurationProperties.getValidCsvRowCounty()));
    }



    private String format(YearlyCounties yearlyCounties) {

        StringBuffer returnable = new StringBuffer();

        Set<Integer> years = yearlyCounties.getYearlyCounties().keySet();

        Iterator<Integer> it = years.iterator();

        while (it.hasNext()) {
            int year = it.next();
            List<CountyRow> countyRows = yearlyCounties.getYearlyCounties().get(year);
            Iterator<CountyRow> it2 = countyRows.iterator();
            while (it2.hasNext()) {
                CountyRow countyRow = it2.next();
                returnable.append(countyRow.getYear() + ";" + countyRow.getCounty() + ";" + countyRow.getUnemployedRate() + "\n");
            }
        }

        return returnable.toString();

    }

}
