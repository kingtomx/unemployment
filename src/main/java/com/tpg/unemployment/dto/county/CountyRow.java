package com.tpg.unemployment.dto.county;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountyRow {


    private String LAUSCode;
    private String stateFIPSCode;
    private String countyFipsCode;
    private String county;
    private String state;
    private int year;
    private int laborForce;
    private int employed;
    private int unemployedLevel;
    private float unemployedRate;
    private boolean noData;


}
