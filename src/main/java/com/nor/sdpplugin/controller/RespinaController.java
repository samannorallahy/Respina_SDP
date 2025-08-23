package com.nor.sdpplugin.controller;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.*;
import com.nor.sdpplugin.service.RespinaService;
import com.nor.sdpplugin.service.SdpAddRequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class RespinaController {

    private static final Logger logger = LoggerFactory.getLogger(RespinaController.class);

    @PostMapping("/add-to-requests")
    public ResponseEntity<String> addToRequests(@RequestBody String str, HttpServletRequest httpServletRequest) {
        logger.info("Calling api/v1/callCenter service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), str);
        String templateName, requesterMobile;
        int requestID;
        try {
            JSONObject obj = new JSONObject(str);
            if (obj.has("requestID"))
                requestID = Integer.parseInt(obj.getString("requestID"));
            else {
                logger.error("there is no requestID in this request");
                return new ResponseEntity<>("there is no requestID in this request", HttpStatus.BAD_REQUEST);
            }
            if (obj.has("template")) {
                templateName = obj.getString("template");
            } else {
                logger.error("your request doesn't have template");
                return new ResponseEntity<>("your request doesn't have template", HttpStatus.BAD_REQUEST);
            }

            if (obj.has("requester")) {
                JSONObject requesterObj = new JSONObject(obj.getString("requester"));
                if (requesterObj.has("mobile"))
                    requesterMobile = requesterObj.getString("mobile");
                else {
                    logger.error("your request doesn't have requester.mobile");
                    return new ResponseEntity<>("your request doesn't have mobile", HttpStatus.BAD_REQUEST);
                }
            } else {
                logger.error("your request doesn't have requester");
                return new ResponseEntity<>("your request doesn't have requester", HttpStatus.BAD_REQUEST);
            }

//            System.out.println("Template Name: " + templateName);
//            System.out.println("Requester mobile: " + requesterMobile);
////        System.out.println("Requester Name: " + requesterObj.getString("name"));
//            System.out.println("requestID: " + requestID);

            RespinaService service = new RespinaService();
            boolean insert = service.insertIntoRequests(requestID, templateName, requesterMobile, str);
            return new ResponseEntity<>("Done", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/call-customer")
    public ResponseEntity<ResponseModel> callCustomer(@RequestBody String str, HttpServletRequest httpServletRequest) {
        logger.info("Calling api/v1/call-customer service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), str);
        ResponseModel responseModel = new ResponseModel();

        JSONObject obj = new JSONObject(str);
        int requestID;
        if (obj.has("requestID"))
            requestID = Integer.parseInt(obj.getString("requestID"));
        else {
            logger.error("there is no requestID in this request");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        }

        RespinaService service = new RespinaService();
        boolean b = service.callCustomer(requestID);

        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @PostMapping("/customer-reaction")
    public ResponseEntity<ResponseModel> customerReaction(@RequestBody CustomerReaction customerReaction, HttpServletRequest httpServletRequest) throws Exception {
        logger.info("Calling api/v1/customer-reaction service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), customerReaction.toString());
        ResponseModel responseModel = new ResponseModel();
        try {
            RespinaService service = new RespinaService();
            service.customerReaction(customerReaction);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setErrorCode(100);
            responseModel.setResponseMessage("Internal Error");
            logger.error(e.toString());
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/test2")
    public String test2(@RequestBody String str, HttpServletRequest httpServletRequest) {
        logger.info("{}{}", httpServletRequest.getRemoteAddr(), str);
        return ("<h1>Hello World!</h1>");
    }
}
