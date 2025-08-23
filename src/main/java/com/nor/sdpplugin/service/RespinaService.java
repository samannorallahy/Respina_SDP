package com.nor.sdpplugin.service;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.CustomerReaction;
import com.nor.sdpplugin.model.Response;
import lombok.extern.slf4j.Slf4j;

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
                log.info("already created a request with id {}", requestID);
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
                log.info("no request id in this request found!");
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
        sqLiteDao.updateCalledFromTelsi(id, customerReaction.getReaction());
        if (customerReaction.getReaction() == 1) {
            SdpAddRequestService service = new SdpAddRequestService();
            Response response = service.putCallSdpUpdate(reqID_SDP, 3);
            response = service.putCallSdpUpdate(reqID_SDP, 2);
        }
    }
}
