package com.tpg.unemployment.component.scheduler;

import com.tpg.unemployment.component.county.CountyYearlyAverage;
import com.tpg.unemployment.component.download.Downloader;
import com.tpg.unemployment.component.state.StateYearlyAverage;
import com.tpg.unemployment.config.UnemploymentConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;

@Component
public class ScheduledRetriever {


    @Autowired
    private Downloader downloader;

    @Autowired
    private StateYearlyAverage stateYearlyAverage;

    @Autowired
    private CountyYearlyAverage countyYearlyAverage;

    @Autowired
    private UnemploymentConfigurationProperties unemploymentConfigurationProperties;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //YEARLY @Scheduled(cron="0 0 0 1 1/1 *")
    //TESTING @Scheduled(fixedDelay = 600000, initialDelay = 5000)
    @Scheduled(fixedDelay = 600000, initialDelay = 5000)
    private void generateCSVFiles() throws IOException {

        //Counties (several txt files (be warned actual year file could be unavailable)
        // ------------------------------------------------------------------------------
        StringBuffer result = new StringBuffer();
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year=1990; year<actualYear+1; year++) {
            try {
                result.append(downloader.retrieveString("https://www.bls.gov/lau/laucnty" + String.format("%02d", (Math.abs(year) % 100)) + ".txt") + "\n");
            } catch (Exception ex) {
                log.error("A requested file was not downloaded and/or parsed correctly (https://www.bls.gov/lau/laucnty" + String.format("%02d", (Math.abs(year) % 100)) + ".txt). Maybe the data is not available yet");
            }
        }
        byte[] counties = countyYearlyAverage.getCSV(result.toString()).getBytes(Charset.forName("UTF-8"));
        try (FileOutputStream fos = new FileOutputStream(unemploymentConfigurationProperties.getStoragePath() + "unemployment_counties.csv")) {
            fos.write(counties);
        }


        //States (all data is stored in just a file so the download will be unique)
        // ------------------------------------------------------------------------------
        String file = downloader.retrieveString("https://www.bls.gov/web/laus/ststdsadata.txt");
        byte[] states = stateYearlyAverage.getCSV(file, 1990, actualYear+1).getBytes(Charset.forName("UTF-8"));
        try (FileOutputStream fos = new FileOutputStream(unemploymentConfigurationProperties.getStoragePath() + "unemployment_states.csv")) {
            fos.write(states);
        }

    }

}
