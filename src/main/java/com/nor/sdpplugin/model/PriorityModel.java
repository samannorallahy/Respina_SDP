package com.nor.sdpplugin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nor.sdpplugin.other.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PriorityModel {
    public PriorityModel(String priority) {
        if (priority == null || priority.isEmpty() || priority.isBlank())
            this.id = "1";
        else if (Utils.isNumeric(priority)) this.id = priority;
        else this.name = priority;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}
