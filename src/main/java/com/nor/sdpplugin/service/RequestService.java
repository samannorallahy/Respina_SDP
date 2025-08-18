//package com.nor.sdpplugin.service;
//
//import com.nor.sdpplugin.dataBase.SQLiteDao;
//import com.nor.sdpplugin.model.*;
//import jakarta.servlet.http.HttpServletRequest;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//public class RequestService {
//
//    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
//    AddRequestService addRequestService = new AddRequestService();
//
//    public void addRequest(@RequestBody AddRequest_InputModel inputJson, HttpServletRequest httpServletRequest) throws IOException {
//        logger.info("\nCalling api/v1/request/add? service from ip address: {}\nJson:{}", httpServletRequest.getRemoteAddr(), inputJson);
//        AddRequest addRequest = prepareAddRequestModel(inputJson);
//        AddRequest_OutputModel addRequestOutputModel = callServiceAndGetOutput(addRequest, inputJson.getAttachments()/*, inputJson.getIdSource()*/);
//        new SQLiteDao().insertLog(addRequestOutputModel.getIdSource(), inputJson.getKeyJira(), inputJson.toString(), addRequestOutputModel.toString());
//    }
//
//    private @NotNull AddRequest_OutputModel callServiceAndGetOutput(AddRequest addRequest, List<Attachment> attachments/*, String sourceID*/) throws IOException {
//        Response response = addRequestService.addRequest(addRequest, attachments);
//        AddRequest_OutputModel addRequest_outputModel = new AddRequest_OutputModel();
//        addRequest_outputModel.setAttachmentModels(response.getAttachmentModels());
//        addRequest_outputModel.setIdSource(response.getId());
//        addRequest_outputModel.setDescription(response.getDescription());
//        addRequest_outputModel.setBody(response.getBody());
//        addRequest_outputModel.setInput_data(response.getInput_data());
//        return addRequest_outputModel;
//    }
//
//    private static @NotNull AddRequest prepareAddRequestModel(AddRequest_InputModel inputJson) {
//        AddRequest addRequest = new AddRequest();
//        Request request = new Request();
//        request.setSubject(inputJson.getSubject());
//        request.setDescription(inputJson.getDescription());
//        Requester requester = new Requester(inputJson.getRequester());
//        request.setRequester(requester);
//        StatusModel status = new StatusModel(inputJson.getStatus());
//        request.setStatus(status);
//        GroupModel group = new GroupModel(inputJson.getGroup());
//        request.setGroup(group);
//        addRequest.setRequest(request);
//        return addRequest;
//    }
//}
