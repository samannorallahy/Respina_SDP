package com.nor.sdpplugin.controller;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.AddRequest_InputModel;
import com.nor.sdpplugin.model.AddToJiraModel;
import com.nor.sdpplugin.model.Response;
import com.nor.sdpplugin.model.UpdateJiraModel;
import com.nor.sdpplugin.other.Utils;
import com.nor.sdpplugin.service.AddRequestService;
import com.nor.sdpplugin.service.Jira;
import com.nor.sdpplugin.service.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.PushBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/v1")
public class RespinaController {

    private static final Logger logger = LoggerFactory.getLogger(RespinaController.class);

    @PostMapping("/callCenter")
    public String callCenter(@RequestBody String str, HttpServletRequest httpServletRequest) throws Exception {
        logger.info("Calling api/v1/callCenter service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), str);



        String requestID, templateName, requesterMobile;
        JSONObject obj = new JSONObject(str);
        if (obj.has("requestID"))
            requestID = obj.getString("requestID");
        else
            return "your request doesn't have requestID";

        if (obj.has("template")) {
            JSONObject templateObj = new JSONObject(obj.getString("template"));
            if (templateObj.has("name"))
                templateName = templateObj.getString("name");
            else
                return "your request doesn't have name";
        } else
            return "your request doesn't have template";

        if (obj.has("requester")) {
            JSONObject requesterObj = new JSONObject(obj.getString("requester"));
            if (requesterObj.has("mobile"))
                requesterMobile = requesterObj.getString("mobile");
            else
                return "your request doesn't have mobile";
        } else
            return "your request doesn't have requester";


        System.out.println("Template Name: " + templateName);
        System.out.println("Requester mobile: " + requesterMobile);
//        System.out.println("Requester Name: " + requesterObj.getString("name"));
        System.out.println("requestID: " + requestID);

        SQLiteDao sqLiteDao = new SQLiteDao();
        sqLiteDao.insertIntoRequests(requestID, str);

        return ("Done");

    }
}
