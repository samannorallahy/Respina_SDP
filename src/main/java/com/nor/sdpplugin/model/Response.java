package com.nor.sdpplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Response {
    private int status;
    private String body;
    private String id;
    private ArrayList<AttachmentModel> attachmentModels = new ArrayList<>();
    private String exceptionMessage;
    private String description;
    private String input_data;
}
