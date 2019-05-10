package com.tpg.unemployment.controller;

import com.tpg.unemployment.component.converter.FileConverter;
import com.tpg.unemployment.component.county.CountyYearlyAverage;
import com.tpg.unemployment.component.download.Downloader;
import com.tpg.unemployment.component.state.StateYearlyAverage;
import com.tpg.unemployment.config.UnemploymentConfigurationProperties;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.Path;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/unemployment")
public class WebController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Downloader downloader;

    @Autowired
    private StateYearlyAverage stateYearlyAverage;

    @Autowired
    private CountyYearlyAverage countyYearlyAverage;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UnemploymentConfigurationProperties unemploymentConfigurationProperties;

    @RequestMapping(value="/county/{start}/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity unemploymentCounty(@PathVariable int start, @PathVariable int end)  {
        try {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=counties_unemployment_" + start + "_" + end + ".csv");

            StringBuffer result = new StringBuffer();
            for (int year=start; year<end+1; year++) {
                result.append(downloader.retrieveString("https://www.bls.gov/lau/laucnty" + String.format("%02d", (Math.abs(year) % 100)) + ".txt") + "\n");
            }
            return new ResponseEntity(countyYearlyAverage.getCSV(result.toString()).getBytes(Charset.forName("UTF-8")), header, HttpStatus.OK);
        }catch (Exception ex) {
            log.error(ex.getMessage());
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @RequestMapping(value="/state/{start}/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity unemploymentState(@PathVariable int start, @PathVariable int end)  {
        try {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=states_unemployment_" + start + "_" + end + ".csv");

            String file = downloader.retrieveString("https://www.bls.gov/web/laus/ststdsadata.txt");
            String csvFile = stateYearlyAverage.getCSV(file, start, end);
            return new ResponseEntity(csvFile.getBytes(Charset.forName("UTF-8")), header, HttpStatus.OK);
        }catch (Exception ex) {
            log.error(ex.getMessage());
            Map<String, String> error = new HashMap<String, String>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }





}
