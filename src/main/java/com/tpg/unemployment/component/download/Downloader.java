package com.tpg.unemployment.component.download;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sun.security.provider.certpath.OCSPResponse;

@Component
public class Downloader {


    @Autowired
    private RestTemplate restTemplate;

    public String retrieveString(String url) {
        ResponseEntity<String> file = null;
        try {
            file = restTemplate.getForEntity(url, String.class);
            HttpStatus status = file.getStatusCode();
        } catch (Exception ex) {
            throw ex;
        }
        return file.getBody();

    }


    public XSSFWorkbook retrieveExcel(String url) {
        ResponseEntity<XSSFWorkbook> wb = null;
        try {
             wb = restTemplate.getForEntity(url, XSSFWorkbook.class);

            HttpStatus status = wb.getStatusCode();
        } catch (Exception ex) {
            throw ex;
        }
        return wb.getBody();
    }


}
