package com.nor.sdpplugin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AddToJiraModel {
    @JsonProperty("id")
    String id;

    @JsonProperty("summary")
    String summary;

    @JsonProperty("description")
    String description;
}