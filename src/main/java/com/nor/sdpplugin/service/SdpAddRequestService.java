package com.nor.sdpplugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nor.sdpplugin.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SdpAddRequestService {
    /**
     * @param id   requestID
     * @param type 1: assign to user - 2: closed - 3: add workLog
     * @return
     * @throws IOException
     */
    public Response putCallSdpUpdate(String id, int type) throws IOException {
        Response sdpResponse = new Response();
        ServiceDeskPlus serviceDeskPlus = new ServiceDeskPlus();
        UpdateRequest request = new UpdateRequest();
        StatusModel statusModel = null;
        String workLog = "";
        if (type == 1)
            statusModel = serviceDeskPlus.getStatusNameForCalling();
        else if (type == 2)
            statusModel = serviceDeskPlus.getStatusNameForClosing();
        else if (type == 3)
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
                    "    \"description\": \"پیرو تماس خودکار، با توجه به اطلاع مشترک از موضوع قطعی و با تایید ایشان، تیکت بسته شد.\"\n" +
                    "  }\n" +
                    "}";
        RequestForUpdate requestForUpdate = new RequestForUpdate();
//        requestForUpdate.setRequester(new Requester(id));
        requestForUpdate.setStatus(statusModel);
        request.setRequestForUpdate(requestForUpdate);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            if (type == 3)
                sdpResponse.setInput_data(workLog);
            else
                sdpResponse.setInput_data(ow.writeValueAsString(request));
            sdpResponse = serviceDeskPlus.putCallSdpUpdateStatus(id, sdpResponse.getInput_data(), type);
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
}

