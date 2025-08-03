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
public class RequestForUpdate {
//    @JsonProperty("requester")
//    Requester requester = new Requester();
    @JsonProperty("status")
    StatusModel status = new StatusModel();
}


