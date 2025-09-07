package com.nor.sdpplugin.service;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.CustomerReaction;
import com.nor.sdpplugin.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class RespinaService {
    public boolean insertIntoRequests(int requestID, String templateName, String requesterMobile, String inputJson) {
        SQLiteDao sqLiteDao = new SQLiteDao();
        boolean canChangeStatus = false;
        try {
            ArrayList<HashMap<String, String>> requestId = sqLiteDao.findRequestId(requestID);
            if (requestId != null && !requestId.isEmpty()) {
                log.info("already Inserted a request with id {} into database", requestID);
                if (sqLiteDao.templateExistInDB(templateName)) {
                    sqLiteDao.updateTemplateChanged(requestID, requesterMobile, 1);
                    canChangeStatus = true;
                }
            } else {
                if (templateName.isEmpty()) {
                    sqLiteDao.insertIntoRequestsFromSDP(requestID, requesterMobile, inputJson, 0);
                } else {
                    sqLiteDao.insertIntoRequestsFromSDP(requestID, requesterMobile, inputJson, 1);
                    canChangeStatus = true;
                }
            }
            if (canChangeStatus) {
                log.info("Changing Request Status");
                SdpAddRequestService service = new SdpAddRequestService();
                Response response = service.putCallSdpUpdate(String.valueOf(requestID), 1);
            } else log.info("can't Changing Request Status");
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    public boolean callCustomer(int requestID) {
        log.info("callCustomer requestID: {}", requestID);
        SQLiteDao sqLiteDao = new SQLiteDao();
        try {
            ArrayList<HashMap<String, String>> requestId = sqLiteDao.findRequestId(requestID);
            if (requestId != null && !requestId.isEmpty()) {
                String mobileNo = requestId.get(0).get("MOBILENO");
                Telsi telsi = new Telsi();
                String telsiResult = telsi.callTelsi(mobileNo);
                sqLiteDao.update_callCustomer(requestID, 1);
                return true;
            } else {
                log.info("no request id found to get it's mobileNo and call it!");
                return false;
            }
        } catch (Exception e) {
            log.error(e.toString());
            try {
                sqLiteDao.update_callCustomer(requestID, -1);
            } catch (Exception ex) {
                log.error("sqLiteDao.update_callCustomer({},-1):{}", requestID, ex.toString());
            }
            return false;
        }
    }

    public boolean getUserAllowedTime(int requesterId) {
        log.info("getting user allowed time to make a call");
        try {
            Response userData = new ServiceDeskPlus().getUserData(requesterId);
            JSONObject obj = new JSONObject(userData.getBody());

            if (obj.has("user")) {
                JSONObject userObj = new JSONObject(obj.getJSONObject("user").toString());
                if (userObj.has("user_udf_fields")) {
                    JSONObject user_udf_fieldsObj = new JSONObject(userObj.getJSONObject("user_udf_fields").toString());
                    String userUdfField = new ServiceDeskPlus().getUser_udf_field();
                    if (user_udf_fieldsObj.has(userUdfField)) {
                        String allowedTime = "";
                        boolean allTime = user_udf_fieldsObj.isNull(userUdfField);
                        if (allTime)
                            allowedTime = "0-24";
                        else
                            allowedTime = user_udf_fieldsObj.getString(userUdfField);
                        log.info("allowed time is: {}", allTime ? "allTime" : allowedTime);

                        int startHour = Integer.parseInt(allowedTime.split("-")[0]);
                        int endHour = Integer.parseInt(allowedTime.split("-")[1]) - 1;// mines 1 needed to make it correct hour
                        LocalTime start = LocalTime.of(startHour, 59);
                        LocalTime end = LocalTime.of(endHour, 59);
                        LocalTime now = LocalTime.now();
                        boolean isInRange = !now.isBefore(start) && !now.isAfter(end);
                        log.info("current time is: {}", now);
                        log.info("is in ranged time to call: {}", isInRange);

                        return isInRange;
                    } else return false;
                } else return false;
            } else return false;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    public void customerReaction(CustomerReaction customerReaction) throws Exception {
        SQLiteDao sqLiteDao = new SQLiteDao();
        sqLiteDao.insertIntoRequestsFromTelsi(customerReaction.getMobile(), customerReaction.getReaction());
        String reqID_SDP;
        int id;
        ArrayList<HashMap<String, String>> records = sqLiteDao.findMobileNumber(customerReaction.getMobile());
        if (!records.isEmpty()) {
            reqID_SDP = records.get(0).get("REQID_SDP");
            id = Integer.parseInt(records.get(0).get("ID"));
            log.info("reqID_SDP: {} and id: {}", reqID_SDP, id);
        } else {
            log.info("there is no requestID in requests with mobile: {} and customerReaction: null", customerReaction.getMobile());
            return;
        }
        sqLiteDao.updateCustomerReaction(id, customerReaction.getReaction());
        SdpAddRequestService service = new SdpAddRequestService();
        Response response = null;
        if (customerReaction.getReaction() == 1) {
            service.putCallSdpAddWorklogs(reqID_SDP, 1); // add work log for closing
            service.putCallSdpUpdate(reqID_SDP, 2); // egdam tavasot moshtari
        } else if (customerReaction.getReaction() == 2)
            response = service.putCallSdpUpdate(reqID_SDP, 4); // erja be karshenas
        log.info(response.toString());
    }

    public void notAllowedToCall(int reqID_SDP) {
        SdpAddRequestService service = new SdpAddRequestService();
        Response response = null;
        SQLiteDao sqLiteDao = new SQLiteDao();
        try {
            sqLiteDao.update_callCustomer(reqID_SDP, 0);
            response = service.putCallSdpAddWorklogs(String.valueOf(reqID_SDP), 2);
            response = service.putCallSdpUpdate(String.valueOf(reqID_SDP), 3);
        } catch (Exception e) {
            log.error(e.toString());
        }
        log.info(response.toString());
    }

}
