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
public class ImpactModel {
    public ImpactModel(String impact) {
        if (Utils.isNumeric(impact)) this.id = impact;
        else this.name = impact;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}