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
public class Requester {
    public Requester(String requester) {
        if (Utils.isNumeric(requester)) this.id = requester;
        else this.name = requester;
    }

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;
}