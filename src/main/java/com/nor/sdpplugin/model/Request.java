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
public class Request {
    @JsonProperty("subject")
    String subject;
    @JsonProperty("description")
    String description;
    @JsonProperty("requester")
    Requester requester = new Requester();
    @JsonProperty("status")
    StatusModel status = new StatusModel();
    @JsonProperty("group")
    GroupModel group = new GroupModel();
//    @JsonProperty("category")
//    CategoryModel category = new CategoryModel();
//    @JsonProperty("subcategory")
//    SubcategoryModel subcategory = new SubcategoryModel();
//    @JsonProperty("service_category")
//    ServiceCategoryModel serviceCategory = new ServiceCategoryModel();
//    @JsonProperty("impact")
//    ImpactModel impact = new ImpactModel();
//    @JsonProperty("priority")
//    PriorityModel priority = new PriorityModel();
//    @JsonProperty("udf_fields")
//    Udf_fieldsModel udfFields = new Udf_fieldsModel("123456789");
}


