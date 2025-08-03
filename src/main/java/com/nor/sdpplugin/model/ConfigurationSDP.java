package com.nor.sdpplugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ConfigurationSDP {
    private String serviceAddress;
    private String authtoken;
    private String statusNameForJiraUpdate;
}
