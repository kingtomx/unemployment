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
public class States {

    private HashMap<String, List<StateRow>> stateRows = new HashMap<>();


}
