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
public class ServiceCategoryModel {
    public ServiceCategoryModel(String serviceCategory) {
        if (Utils.isNumeric(serviceCategory)) this.id = serviceCategory;
        else this.name = serviceCategory;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}
