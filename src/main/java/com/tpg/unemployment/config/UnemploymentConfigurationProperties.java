package com.tpg.unemployment.config;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "unemployment")
@Component
@Data
@Getter
@Setter
public class UnemploymentConfigurationProperties {

    private String validCsvRow;
    private String monthNames;
    private String validCsvRowCounty;
    private String storagePath;

}
