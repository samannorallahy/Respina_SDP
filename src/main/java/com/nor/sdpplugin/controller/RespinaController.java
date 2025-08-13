package com.nor.sdpplugin.controller;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.Snippet;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RespinaController {

    private static final Logger logger = LoggerFactory.getLogger(RespinaController.class);

    @PostMapping("/callCenter")
    public ResponseEntity<String> callCenter(@RequestBody String str, HttpServletRequest httpServletRequest) {
        logger.info("Calling api/v1/callCenter service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), str);

        try {
            String requestID, templateName, requesterMobile;
            JSONObject obj = new JSONObject(str);
            if (obj.has("requestID"))
                requestID = obj.getString("requestID");
            else
                return new ResponseEntity<>("your request doesn't have requestID", HttpStatus.BAD_REQUEST);
            if (obj.has("template")) {
                JSONObject templateObj = new JSONObject(obj.getString("template"));
                if (templateObj.has("name"))
                    templateName = templateObj.getString("name");
                else
                    return new ResponseEntity<>("your request doesn't have name", HttpStatus.BAD_REQUEST);
            } else
                return new ResponseEntity<>("your request doesn't have template", HttpStatus.BAD_REQUEST);

            if (obj.has("requester")) {
                JSONObject requesterObj = new JSONObject(obj.getString("requester"));
                if (requesterObj.has("mobile"))
                    requesterMobile = requesterObj.getString("mobile");
                else
                    return new ResponseEntity<>("your request doesn't have mobile", HttpStatus.BAD_REQUEST);
            } else
                return new ResponseEntity<>("your request doesn't have requester", HttpStatus.BAD_REQUEST);


            System.out.println("Template Name: " + templateName);
            System.out.println("Requester mobile: " + requesterMobile);
//        System.out.println("Requester Name: " + requesterObj.getString("name"));
            System.out.println("requestID: " + requestID);

            SQLiteDao sqLiteDao = new SQLiteDao();
            sqLiteDao.insertIntoRequestsFromSDP(requestID, str);

            return new ResponseEntity<>("Done", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/customer-reaction")
    public ResponseEntity<ResponseModel> customerReaction(@RequestBody CustomerReaction customerReaction, HttpServletRequest httpServletRequest) throws Exception {
        logger.info("Calling api/v1/customer-reaction service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), customerReaction.toString());
        ResponseModel responseModel = new ResponseModel();
        try {
            System.out.println(customerReaction);
            SQLiteDao sqLiteDao = new SQLiteDao();
            sqLiteDao.insertIntoRequestsFromTelsi(customerReaction.getMobile(), customerReaction.getReaction());
            responseModel.setResponseMessage("Success");
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setErrorCode(100);
            responseModel.setResponseMessage("Internal Error");
            logger.error(e.toString());
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
