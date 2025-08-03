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
public class StatusModel {

    public StatusModel(String status) {
        if (Utils.isNumeric(status)) this.id = status;
        else this.name = status;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}
