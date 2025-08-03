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
public class CategoryModel {
    public CategoryModel(String category) {
        if (Utils.isNumeric(category)) this.id = category;
        else this.name = category;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}
