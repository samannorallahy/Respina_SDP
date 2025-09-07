package com.nor.sdpplugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nor.sdpplugin.model.*;
import com.nor.sdpplugin.other.PersianDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SdpAddRequestService {
    /**
     * @param id   requestID
     * @param type 1: assign to user - 2: closed - 3: NOT AllowedInThisTime
     * @return
     * @throws IOException
     */
    public Response putCallSdpUpdate(String id, int type) throws IOException {
        Response sdpResponse = new Response();
        ServiceDeskPlus serviceDeskPlus = new ServiceDeskPlus();
        UpdateRequest request = new UpdateRequest();
        StatusModel statusModel = null;
        if (type == 1)
            statusModel = serviceDeskPlus.getStatusNameForCalling();
        else if (type == 2)
            statusModel = serviceDeskPlus.getStatusNameForClosing();
        else if (type == 3)
            statusModel = serviceDeskPlus.getStatusNameForNotAllowedInThisTime();
        else if (type == 4)
            statusModel = serviceDeskPlus.getStatusNameForReferredToAnExpert();
        RequestForUpdate requestForUpdate = new RequestForUpdate();
        requestForUpdate.setStatus(statusModel);
        request.setRequestForUpdate(requestForUpdate);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            sdpResponse.setInput_data(ow.writeValueAsString(request));
            sdpResponse = serviceDeskPlus.putCallSdpUpdateStatus(id, sdpResponse.getInput_data());
            if (sdpResponse.getStatus() != 200) {
                sdpResponse.setDescription("could not update the request");
                return sdpResponse;
            }
        } catch (JsonProcessingException e) {
            sdpResponse.setExceptionMessage(e.toString());
            return sdpResponse;
        }
        return sdpResponse;
    }

    /**
     * @param id   requestID
     * @param type 1: closed, 2: Not AllowedToCall in this Time
     * @return
     * @throws IOException
     */
    public Response putCallSdpAddWorklogs(String id, int type) throws IOException {
        Response sdpResponse = new Response();
        ServiceDeskPlus serviceDeskPlus = new ServiceDeskPlus();
        String workLog = getInputBody(type);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            sdpResponse.setInput_data(workLog);
            sdpResponse = serviceDeskPlus.putCallSdpUpdateWorklogs(id, sdpResponse.getInput_data());
            if (sdpResponse.getStatus() != 200) {
                sdpResponse.setDescription("could not update the request");
                return sdpResponse;
            }
        } catch (JsonProcessingException e) {
            sdpResponse.setExceptionMessage(e.toString());
            return sdpResponse;
        }
        return sdpResponse;
    }

    @NotNull
    private static String getInputBody(int type) {
        String workLog, str = "";
        if (type == 1) {
            str = "پیرو تماس خودکار، با توجه به اطلاع مشترک از موضوع قطعی و با تایید ایشان،";
            str = str + " " + "در تاریخ:" + " " + PersianDateTimeUtil.getDate();
            str = str + " " + "و ساعت:" + " " + PersianDateTimeUtil.getTime();
            str = str + " " + "تیکت بسته شد";
        } else if (type == 2) {
            str = "به علت عدم امکان تماس در خارج از محدوده تعریف شده،";
            str = str + " " + "در تاریخ:" + " " + PersianDateTimeUtil.getDate();
            str = str + " " + "و ساعت:" + " " + PersianDateTimeUtil.getTime();
            str = str + " " + "تیکت به کارشناس ارجاع شد";
        }
        workLog = " {\n" +
                "  \"worklog\": {\n" +
                "    \"include_nonoperational_hours\": true,\n" +
                "    \"owner\": {\n" +
                "      \"name\": \"administrator\"\n" +
                "    },\n" +
                "    \"time_spent\": {\n" +
                "      \"hours\": \"1\",\n" +
                "      \"minutes\": \"0\"\n" +
                "    },\n" +
                "    \"description\": \"" + str + "\"\n" +
                "  }\n" +
                "}";
        return workLog;
    }
}

