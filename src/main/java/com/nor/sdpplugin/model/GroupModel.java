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
public class GroupModel {
    public GroupModel(String group) {
        if (Utils.isNumeric(group)) this.id = group;
        else this.name = group;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

}
