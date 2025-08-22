package com.nor.sdpplugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nor.sdpplugin.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SdpAddRequestService {
    public Response putCallSdpUpdateStatusAfterCalling(String id, int type) throws IOException {
        Response sdpResponse = new Response();
        ServiceDeskPlus serviceDeskPlus = new ServiceDeskPlus();
        UpdateRequest request = new UpdateRequest();
        StatusModel statusModel;
        if (type == 1)
            statusModel = serviceDeskPlus.getStatusNameForCalling();
        else
            statusModel = serviceDeskPlus.getStatusNameForClosing();
        RequestForUpdate requestForUpdate = new RequestForUpdate();
//        requestForUpdate.setRequester(new Requester(id));
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
}

