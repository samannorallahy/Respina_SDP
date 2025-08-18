//package com.nor.sdpplugin.controller;
//
//import com.nor.sdpplugin.dataBase.SQLiteDao;
//import com.nor.sdpplugin.model.*;
//import com.nor.sdpplugin.other.Utils;
//import com.nor.sdpplugin.service.AddRequestService;
//import com.nor.sdpplugin.service.Telsi;
//import com.nor.sdpplugin.service.RequestService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.PushBuilder;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@RestController
//@RequestMapping("/api/v1")
//public class WebhooksController {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebhooksController.class);
//
//    AddRequestService addRequestService = new AddRequestService();
//
//
//    @PostMapping("/addtosdp")
//    public String addtosdp(@RequestBody String str, HttpServletRequest httpServletRequest) throws IOException {
//        logger.info("Calling api/v1/addtosdp service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), str);
//        JSONObject obj = new JSONObject(str);
//        String key = "", description = "", summary = "", labels = "";
//        if (obj.has("changelog")) {
//            JSONObject changelogOBJ = obj.getJSONObject("changelog");
//            JSONArray jsonArray = changelogOBJ.getJSONArray("items");
//            Object object = jsonArray.get(0);
//
//            String field = ((JSONObject) object).get("field").toString();
//            String fromString = ((JSONObject) object).get("fromString").toString();
//            String toString = ((JSONObject) object).get("toString").toString();
//
//            if (!field.equals("status")) {
//                logger.info("this request is not about changing the status");
//                return "this request is not about changing the status";
//            } else if (!(fromString.equals("To Do") && toString.equals("In Progress"))) {
//                logger.info("FromString is:{} and ToString is:{}", fromString, toString);
//                return "FromString is:" + fromString + " and ToString is:" + toString;
//            }
//        } else {
//            logger.info("doesnt have changelog object");
//            return "doesnt have changelog object";
//        }
//        if (obj.has("issue")) {
//            JSONObject issueOBJ = obj.getJSONObject("issue");
//            if (issueOBJ.has("key"))
//                key = issueOBJ.getString("key");
//            else {
//                logger.info("doesnt have key");
//                return "doesnt have key";
//            }
//            if (issueOBJ.has("fields")) {
//                JSONObject fieldsOBJ = issueOBJ.getJSONObject("fields");
//                if (fieldsOBJ.isNull("description"))
//                    description = "برای این درخواست توضیحاتی در jira ثبت نگردیده است";
//                else
//                    description = fieldsOBJ.getString("description").replaceAll("\r\n", "<br>");
//                summary = fieldsOBJ.getString("summary");
//                Telsi telsi = new Telsi();
//                if (fieldsOBJ.isNull(telsi.getItmsGroupField())) {
//                    logger.info("{} is null and we cant make any request in sdp", telsi.getItmsGroupField());
//                    return telsi.getItmsGroupField() + " is null and we cant make any request in sdp";
//                }
//                labels = fieldsOBJ.getJSONObject(telsi.getItmsGroupField()).getString("value");
//                labels = Utils.extractAllNumbers(labels);
//            } else {
//                logger.info("doesnt have fields");
//                return "doesnt have fields";
//            }
//        }
//        AddRequest_InputModel addRequestInputModel = new AddRequest_InputModel();
//        addRequestInputModel.setRequester("jira_ticket");
//        addRequestInputModel.setSubject(summary);
//        addRequestInputModel.setDescription(description);
//        addRequestInputModel.setKeyJira(key);
//        addRequestInputModel.setGroup(labels);
//        addRequestInputModel.setStatus("open");
//        addRequestInputModel.setStatus("1");
//        RequestService requestService = new RequestService();
//
//        SQLiteDao sqLiteDao = new SQLiteDao();
//        try {
//            ArrayList<HashMap<String, String>> hashMaps = sqLiteDao.selectQuery("select count(*) from request where reqID_JIRA='" + key + "'");
////            System.out.println(hashMaps);
//            if (!hashMaps.get(0).get("COUNT(*)").equals("0")) {
//                logger.info("has already created request for Key:" + key);
//                return "has already called for key:" + key;
//            }
//        } catch (Exception e) {
//            return e.toString();
//        }
//        requestService.addRequest(addRequestInputModel, httpServletRequest);
//        return ("Done");
//    }
//
//    @PostMapping("/updateJira")
//    public String updateJira(@RequestBody UpdateJiraModel inputJson, HttpServletRequest httpServletRequest) throws IOException {
//        logger.info("Calling api/v1/request/updateJira? service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), inputJson);
//        String reqIDJira = "";
//        SQLiteDao sqLiteDao = new SQLiteDao();
//        try {
//            ArrayList<HashMap<String, String>> hashMaps = sqLiteDao.selectQuery("select count(*) from request where reqID_SDP='" + inputJson.getId() + "'");
////            System.out.println(hashMaps);
//            if (hashMaps.get(0).get("COUNT(*)").equals("0")) {
//                logger.info("RequestID:" + inputJson.getId() + " is not created from JIRA");
//                return "RequestID:" + inputJson.getId() + " is not created from JIRA";
//            } else {
//                ArrayList<HashMap<String, String>> hashMaps2 = sqLiteDao.selectQuery("select reqID_JIRA from request where reqID_SDP='" + inputJson.getId() + "'");
//                reqIDJira = hashMaps2.get(0).get("REQID_JIRA");
//            }
//        } catch (Exception e) {
//            return e.toString();
//        }
//        inputJson.setJiraKey(reqIDJira);
//        Response response = addRequestService.updateTransitionsJiraService(inputJson);
//        if (response.getStatus() == -1) {
//            logger.info(response.getDescription());
//            return response.getDescription();
//        } else
//            new SQLiteDao().updateSdpToJira(inputJson.getId(), reqIDJira);
//        return "Done";
//    }
//
//    @PostMapping("/addtojira")
//    public String addToJira(@RequestBody AddToJiraModel inputJson, HttpServletRequest httpServletRequest) throws IOException {
//        logger.info("\nCalling api/v1/request/addtojira? service from ip address: {}\nJson:{}", httpServletRequest.getRemoteAddr(), inputJson);
//        String plainText = Jsoup.parse(inputJson.getDescription()).text();
//        inputJson.setDescription(plainText);
//        SQLiteDao sqLiteDao = new SQLiteDao();
//        try {
//            ArrayList<HashMap<String, String>> hashMaps = sqLiteDao.selectQuery("select count(*) from request where reqID_SDP='" + inputJson.getId() + "'");
////            System.out.println(hashMaps);
//            if (!hashMaps.get(0).get("COUNT(*)").equals("0")) {
//                logger.info("has already created task for id:{}", inputJson.getId());
//                return "has already called for id:" + inputJson.getId();
//            }
//        } catch (Exception e) {
//            return e.toString();
//        }
//        Response response = addRequestService.addToJiraService(inputJson);
////        new SQLiteDao().insertQuery("insert into main.request(requestID_IN,requestID_OUT,inoutJSON,outputJSON)VALUES(\"" + inputJson.getIdSource() + "\",\"" + addRequestOutputModel.getIdSource() + "\",\"" + inputJson + "\",\"" + addRequestOutputModel + "\")");
//        if (response.getStatus() == 201)
//            new SQLiteDao().insertLog(inputJson.getId(), response.getId(), inputJson.toString(), response.toString());
//        return "Done";
//    }
//
//    @PostMapping("/updateSDP")
//    public String updateSDP(@RequestBody String str, HttpServletRequest httpServletRequest, PushBuilder pushBuilder) throws IOException {
//        logger.info("Calling api/v1/request/updateSDP? service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), str);
//        JSONObject obj = new JSONObject(str);
//        String key = "", reqIdSDP = "";
//        if (obj.has("changelog")) {
//            JSONObject changelogOBJ = obj.getJSONObject("changelog");
//            JSONArray jsonArray = changelogOBJ.getJSONArray("items");
////            Object object = jsonArray.get(0);
//            AtomicBoolean flag = new AtomicBoolean(false);
//            jsonArray.forEach(object -> {
//                String field = ((JSONObject) object).get("field").toString();
//                String fromString = ((JSONObject) object).get("fromString").toString();
//                String toString = ((JSONObject) object).get("toString").toString();
//                if (!field.equals("status"))
//                    logger.info("this item in request is not about changing the status");
//                else if (!(fromString.equals("In Review") && toString.equals("Done")))
//                    logger.info("this status change is not valid because: FromString is:" + fromString + " and ToString is:" + toString);
//                else flag.set(true);
//            });
//            if (!flag.get())
//                return "";
//        } else {
//            logger.info("doesnt have changelog object");
//            return "doesnt have changelog object";
//        }
//        if (obj.has("issue")) {
//            JSONObject issueOBJ = obj.getJSONObject("issue");
//            if (issueOBJ.has("key"))
//                key = issueOBJ.getString("key");
//            else {
//                logger.info("doesnt have key");
//                return "doesnt have key";
//            }
//        } else {
//            logger.info("doesnt have issue property");
//            return "doesnt have issue property";
//        }
//        SQLiteDao sqLiteDao = new SQLiteDao();
//        try {
//            ArrayList<HashMap<String, String>> hashMaps = sqLiteDao.selectQuery("select count(*) from request where reqID_JIRA = '" + key + "'");
////            System.out.println(hashMaps);
//            if (hashMaps.get(0).get("COUNT(*)").equals("0")) {
//                logger.info("RequestID:{} is not created from SDP", str);
//                return "RequestID:" + str + " is not created from SDP";
//            } else {
//                ArrayList<HashMap<String, String>> hashMaps2 = sqLiteDao.selectQuery("select reqID_SDP from request where reqID_JIRA = '" + key + "'");
//                reqIdSDP = hashMaps2.get(0).get("REQID_SDP");
//            }
//        } catch (Exception e) {
//            return e.toString();
//        }
//
//        Response response1 = addRequestService.putCallSdpUpdateStatus(reqIdSDP);
//        if (response1.getStatus() == 200)
//            new SQLiteDao().updateJiraToSDP(reqIdSDP, key);
//        return "Done";
//    }
//
//
//    @PostMapping("/test2")
//    public String test2(@RequestBody String str, HttpServletRequest httpServletRequest) {
//        logger.info("{}{}", httpServletRequest.getRemoteAddr(), str);
//        return ("<h1>Hello World!</h1>");
//    }
//}
