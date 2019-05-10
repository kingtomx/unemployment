package com.tpg.unemployment.component.converter;

import com.tpg.unemployment.component.download.Downloader;
import com.tpg.unemployment.component.filter.RowFilter;
import com.tpg.unemployment.dto.county.CountyRow;
import com.tpg.unemployment.dto.county.YearlyCounties;
import com.tpg.unemployment.dto.state.States;
import com.tpg.unemployment.dto.state.YearlyStates;
import com.tpg.unemployment.dto.state.StateRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Component
public class FileConverter {

    @Autowired
    private Downloader downloader;

    @Autowired
    private RowFilter rowFilter;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public YearlyStates extractData(String file, String rFilter, String monthFilter) {

        YearlyStates yearlyStates = new YearlyStates();
        try {
            String[] rows = file.split("\n");
            int RowNum=0;

            String month = "";
            String year = "";

            NumberFormat format = NumberFormat.getInstance(Locale.US);

            for (int rowIndex = 0; rowIndex<rows.length; rowIndex++) {

                String[] monthYear = rowFilter.MonthExtractor(rows[rowIndex], monthFilter);
                if (monthYear!=null) {
                    month = monthYear[0];
                    year = monthYear[1];
                    log.info("By State: " + month+" "+year);
                }

                if (rowFilter.isValidCsvRow(rows[rowIndex], rFilter)) {
                    String[] cells = (month + "@" + year + "@" + rows[rowIndex]).replace("    ", "@").split("@+");

                    StateRow stateRow = new StateRow();
                    stateRow.setMonth(cells[0].trim());
                    stateRow.setYear(Integer.parseInt(cells[1].trim()));
                    stateRow.setState(cells[2].trim().replaceAll("\\.", ""));
                    stateRow.setNonInstitutionalPopulation(format.parse(cells[3].trim()).intValue());
                    stateRow.setTotalLaborForce(format.parse(cells[4].trim()).intValue());
                    stateRow.setTotalPercentOfPopulation(format.parse(cells[5].trim()).floatValue());
                    stateRow.setEmployedTotal(format.parse(cells[6].trim()).intValue());
                    stateRow.setEmployedPercentOfPopulation(format.parse(cells[7].trim()).floatValue());
                    stateRow.setUnemployedTotal(format.parse(cells[8].trim()).intValue());
                    stateRow.setUnemployedPercentOfLaborForce(format.parse(cells[9].trim()).floatValue());

                    yearlyStates.addStateRow(stateRow);

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return yearlyStates;

    }

    public String generateResult(YearlyStates yearlyStates, int start, int end) {
        StringBuffer result = new StringBuffer();
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        for (int year = start; year< end+1; year++) {
            try {
                HashMap<String, List<StateRow>> anual = yearlyStates.getYearlyStates().get(year).getStateRows();
                Iterator it = anual.keySet().iterator();
                while (it.hasNext()) {
                    String state = it.next().toString();
                    result.append(year + "," + state.trim() + ",");
                    Iterator<StateRow> it2 = anual.get(state).iterator();
                    float sum = 0;
                    int counter = 0;
                    while (it2.hasNext()) {
                        counter++;
                        StateRow sr = it2.next();
                        sum = sum + sr.getUnemployedPercentOfLaborForce();
                    }

                    result.append(numberFormat.format(sum / counter) + "\r\n");
                }
            } catch (Exception ex) {
                log.info("Unavailable year");
            }
        }
        return result.toString();
    }


    public YearlyCounties extractDataCounties(String file, String rFilter) {

        YearlyCounties yearlyCounties = new YearlyCounties();
        try {
            String[] rows = file.split("\n");

            NumberFormat format = NumberFormat.getInstance(Locale.US);

            for (int rowIndex = 0; rowIndex<rows.length; rowIndex++) {

                if (rowFilter.isValidCsvRow(rows[rowIndex], rFilter)) {
                    String[] cells = rows[rowIndex].split("\\s{2,}");

                    try {
                        CountyRow countyRow = new CountyRow();
                        countyRow.setLAUSCode(cells[0].trim());
                        countyRow.setStateFIPSCode(cells[1].trim());
                        countyRow.setCountyFipsCode(cells[2].trim());
                        countyRow.setCounty(cells[3].trim());
                        countyRow.setYear(format.parse(cells[4].trim()).intValue());
                        countyRow.setUnemployedRate(format.parse(cells[8].trim()).floatValue());
                        countyRow.setNoData(false);
                        yearlyCounties.addCountyRow(countyRow);

                    } catch (Exception ex) {
                        CountyRow countyRow = new CountyRow();
                        countyRow.setLAUSCode(cells[0].trim());
                        countyRow.setStateFIPSCode(cells[1].trim());
                        countyRow.setCountyFipsCode(cells[2].trim());
                        countyRow.setCounty(cells[3].trim());
                        countyRow.setYear(format.parse(cells[4].trim()).intValue());
                        countyRow.setNoData(true);
                        yearlyCounties.addCountyRow(countyRow);
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return yearlyCounties;

    }



}
