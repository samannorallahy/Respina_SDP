package com.nor.sdpplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AttachmentModel {
    private String name;
    private String size;
    private String message;
    private int status;
}
