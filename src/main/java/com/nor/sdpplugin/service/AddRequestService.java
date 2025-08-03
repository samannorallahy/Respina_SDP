package com.nor.sdpplugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nor.sdpplugin.model.*;
import com.nor.sdpplugin.other.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AddRequestService {
    public Response addRequest(AddRequest addRequestModel, List<Attachment> attachments) throws IOException {
        Response sdpResponse = new Response();
        ServiceDeskPlus serviceDeskPlus = new ServiceDeskPlus();
//        String id = "";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            sdpResponse.setInput_data(ow.writeValueAsString(addRequestModel));
            sdpResponse = serviceDeskPlus.postCallSDP(sdpResponse.getInput_data(), "/api/v3/requests");
            if (sdpResponse.getStatus() == 201) {
                JSONObject obj = new JSONObject(sdpResponse.getBody());
                if (obj.has("request")) {
                    obj = obj.getJSONObject("request");
                    if (obj.has("id")) {
//                        id = obj.getString("id");
                        sdpResponse.setId(obj.getString("id"));
                    }
                }
            } else {
                sdpResponse.setDescription("could not create the request");
                return sdpResponse;
            }
        } catch (JsonProcessingException e) {
            sdpResponse.setExceptionMessage(e.toString());
            return sdpResponse;
        }
        if (attachments != null) {
            sdpResponse.setAttachmentModels(new ArrayList<>());
            for (Attachment file : attachments) {
                File destinationFile = null;
                try {
                    byte[] decode = Base64.getDecoder().decode(file.getBase64());
                    destinationFile = new File("." + File.separator + file.getFileName());
                    try (OutputStream os = Files.newOutputStream((destinationFile.toPath()))) {
                        os.write(decode);
                    }
                    Response putCallSDPResponse;
                    AttachmentModel model = new AttachmentModel();
                    model.setName(file.getFileName());
                    model.setSize(Utils.displayFileSize(destinationFile.length()));
                    putCallSDPResponse = serviceDeskPlus.putCallSDP(destinationFile, sdpResponse.getId());
                    model.setStatus(putCallSDPResponse.getStatus());
                    if (putCallSDPResponse.getStatus() != 200)
                        model.setMessage("failed");
                    else model.setMessage("success");
                    sdpResponse.getAttachmentModels().add(model);
                } finally {
                    destinationFile.delete();
                }
            }
        }
        return sdpResponse;
    }

    public Response addToJiraService(AddToJiraModel model) throws IOException {
        Response jiraResponse = new Response();
        Jira jira = new Jira();
        jiraResponse = jira.addRequestInJira(model);
        if (jiraResponse.getStatus() == 201) {
            JSONObject obj = new JSONObject(jiraResponse.getBody());
            if (obj.has("key"))
                jiraResponse.setId(obj.getString("key"));
        } else {
            jiraResponse.setDescription("could not create the request");
            return jiraResponse;
        }
        return jiraResponse;
    }

    public Response updateTransitionsJiraService(UpdateJiraModel model) throws IOException {
        Response jiraResponse;
        Jira jira = new Jira();
        Response donTransitionsID = jira.getDonTransitionsID(model);
        JSONObject obj = new JSONObject(donTransitionsID.getBody());
        JSONArray jsonArray = obj.getJSONArray("transitions");
        for (int i = 0; i < jsonArray.length(); i++) {
            String value = ((JSONObject) jsonArray.get(i)).get("name").toString();
            value = value.replaceAll("_", "").replaceAll(" ", "").replaceAll("-", "").toUpperCase();
            if (value.equals("INREVIEW"))
                model.setTransitionID(((JSONObject) jsonArray.get(i)).get("id").toString());
        }
        if (model.getTransitionID().equals("0")) {
            donTransitionsID.setDescription("there is no INREVIEW state");
            donTransitionsID.setStatus(-1);
            return donTransitionsID;
        }
        jiraResponse = jira.updateTransitionsInJira(model);
        return jiraResponse;
    }

    public Response putCallSdpUpdateStatus(String id) throws IOException {
        Response sdpResponse = new Response();
        ServiceDeskPlus serviceDeskPlus = new ServiceDeskPlus();
        UpdateRequest request = new UpdateRequest();
        StatusModel statusModel = serviceDeskPlus.getStatusNameForJiraUpdate();
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

