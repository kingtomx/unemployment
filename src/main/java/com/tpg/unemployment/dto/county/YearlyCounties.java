package com.tpg.unemployment.dto.county;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyCounties {

    private HashMap<Integer, List<CountyRow>> yearlyCounties = new HashMap<>();

    public void addCountyRow(CountyRow countyRow) {
        if (!yearlyCounties.containsKey(countyRow.getYear())) {
            ArrayList<CountyRow> aux = new ArrayList<>();
            aux.add(countyRow);
            yearlyCounties.put(countyRow.getYear(), aux);
        } else {
            yearlyCounties.get(countyRow.getYear()).add(countyRow);
        }
    }

}
