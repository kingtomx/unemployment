package com.tpg.unemployment.dto.state;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StateRow {

    private String month;
    private int year;
    private String state;
    private int nonInstitutionalPopulation;
    private int totalLaborForce;
    private float totalPercentOfPopulation;
    private int employedTotal;
    private float employedPercentOfPopulation;
    private int unemployedTotal;
    private float unemployedPercentOfLaborForce;

}
