package com.nor.sdpplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ConfigurationJIRA {
    private String serviceAddress;
    private String username;
    private String password;
    private String itmsGroupField;
    private String keyNameToCreateTaskInIt;
}
