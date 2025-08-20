package com.nor.sdpplugin.service;

import com.nor.sdpplugin.dataBase.SQLiteDao;
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
                if (templateName.equals("Keyboard problem")) {
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
                AddRequestService service = new AddRequestService();
                Response response = service.putCallSdpUpdateStatusAfterCalling(String.valueOf(requestID));
            } else log.info("can't Changing Request Status");

//            Telsi telsi = new Telsi();
//            String telsiResult = telsi.callTelsi(requesterMobile);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }
}
