package com.tpg.unemployment.component.filter;


import com.tpg.unemployment.config.UnemploymentConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RowFilter {

    public boolean isValidCsvRow(String csvRow, String filter) {
        String patternString = "^("+filter+").*$";
        Pattern pattern = Pattern.compile(patternString, Pattern.UNIX_LINES);
        Matcher matcher = pattern.matcher(csvRow);
        return matcher.matches();
    }

    public String[] MonthExtractor(String row, String filter) {
        String returnable = "";
        String patternString = "("+filter+").*$";
        Pattern pattern = Pattern.compile(patternString, Pattern.UNIX_LINES);
        Matcher matcher = pattern.matcher(row.trim());
        if (matcher.matches()) {
            return row.trim().split(" ");
        } else {
            return null;
        }
    }


}
