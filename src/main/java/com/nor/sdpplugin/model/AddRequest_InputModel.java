package com.nor.sdpplugin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@ToString
public class AddRequest_InputModel {
    @JsonProperty("requester")
    private String requester;// = new Requester();

    @JsonProperty("keyJira")
    private String keyJira;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

//    @JsonProperty("category")
//    private String category;
//
//    @JsonProperty("subCategory")
//    private String subCategory;

//    @JsonProperty("serviceCategory")
//    private String serviceCategory;

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

//    @JsonProperty("priority")
//    private String priority;

    @JsonProperty("attachments")
    private List<Attachment> attachments = new ArrayList<>();

    public AddRequest_InputModel() {
        this.requester = "";
        this.keyJira = "";
        this.subject = "";
        this.description = "";
        this.status = "";
//        this.category = "";
//        this.subCategory = "";
//        this.serviceCategory = "";
        this.item = "";
        this.mode = 0;
        this.sla = 0;
        this.requestType = 0;
//        this.group = "";
        this.technician = "";
        this.urgencyId = 0;
//        this.priority = "";
        this.attachments = new ArrayList<>();
    }
}
