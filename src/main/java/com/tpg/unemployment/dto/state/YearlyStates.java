package com.tpg.unemployment.dto.state;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyStates {

    private HashMap<Integer, States> yearlyStates = new HashMap<Integer, States>();

    public void addStateRow(StateRow stateRow) {
        // IF NO YEAR FOUND
        if (!yearlyStates.containsKey(stateRow.getYear())) {
            States states = new States();
            HashMap<String, List<StateRow>> aux = new HashMap<>();
            ArrayList<StateRow> stateRows = new ArrayList<>();
            stateRows.add(stateRow);
            aux.put(stateRow.getState(), stateRows);
            states.setStateRows(aux);
            yearlyStates.put(stateRow.getYear(), states);
        } else {
            // IF NO STATE FOUND IN THAT YEAR
            if (!yearlyStates.get(stateRow.getYear()).getStateRows().containsKey(stateRow.getState())) {
                ArrayList<StateRow> stateRows = new ArrayList<>();
                stateRows.add(stateRow);
                yearlyStates.get(stateRow.getYear()).getStateRows().put(stateRow.getState(), stateRows);
            } else { // IF STATE IS PRESENT
                yearlyStates.get(stateRow.getYear()).getStateRows().get(stateRow.getState()).add(stateRow);
            }
        }
    }

}
