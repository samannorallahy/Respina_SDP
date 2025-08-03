package com.nor.sdpplugin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AddRequest_InputModel2 {
    @JsonProperty("requester")
    private String requester;// = new Requester();

    @JsonProperty("idSource")
    private String idSource;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("category")
    private String category;

    @JsonProperty("subCategory")
    private String subCategory;

    @JsonProperty("serviceCategory")
    private String serviceCategory;

//    @JsonProperty("impact")
//    private String impact;

    @JsonProperty("item")
    private String item;

    @JsonProperty("mode")
    private int mode;

    @JsonProperty("sla")
    private int sla;

    @JsonProperty("requestType")
    private int requestType;

//    @JsonProperty("level")
//    private int level;

    @JsonProperty("group")
    private String group;

    @JsonProperty("technician")
    private String technician;

    @JsonProperty("urgencyId")
    private int urgencyId;

    @JsonProperty("priority")
    private String priority;

//    @JsonProperty("attachments")
//    private List<Attachment> attachments = new ArrayList<>();

//    @JsonProperty("ticktingAttachmets")
    @JsonProperty("ticketingAttachments")
    private List<Attachments> ticketingAttachments = new ArrayList<>();

}
