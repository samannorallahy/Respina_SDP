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
public class SubcategoryModel {
    public SubcategoryModel(String subcategory) {
        if (Utils.isNumeric(subcategory)) this.id = subcategory;
        else this.name = subcategory;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}
