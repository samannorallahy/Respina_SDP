package com.nor.sdpplugin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AddRequest_OutputModel {
    @JsonProperty("id")
    private String idSource;

    @JsonProperty("attachments")
    private ArrayList<AttachmentModel> attachmentModels = new ArrayList<>();

    @JsonProperty("description")
    private String description;
    @JsonIgnore
    private String body;
    @JsonIgnore
    private String input_data;
}
