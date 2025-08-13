package com.nor.sdpplugin.controller;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import com.nor.sdpplugin.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigController.class);

    @PostMapping("/db")
    public String configSDP(@RequestBody String str, HttpServletRequest httpServletRequest) {
        logger.info("Calling api/config/db service from ip address: {}\t\tinput:{}", httpServletRequest.getRemoteAddr(), str);
        SQLiteDao sqLiteDao = new SQLiteDao();
        String result;
        result = sqLiteDao.executeQuery(str);
        logger.info(result);
        return result;
//        alter table main.request add sdpUpdateCount integer default 0;
//        alter table main.sdpConfig add statusNameForJiraUpdate text;
//        alter table main.jiraConfig add keyNameToCreateTaskInIt TEXT;
    }

    @PostMapping("/sdp")
    public String configSDP(@RequestBody ConfigurationSDP inputModel, HttpServletRequest httpServletRequest) {
        logger.info("Calling api/config/sdp service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), inputModel);
        SQLiteDao sqLiteDao = new SQLiteDao();
        String result;
        result = sqLiteDao.executeQuery("delete from sdpConfig where id>0");
        result = result + "\t\t\t" + sqLiteDao.executeQuery("insert into sdpConfig(serviceAddress, authtoken, statusNameForJiraUpdate) VALUES (\"" + inputModel.getServiceAddress() + "\",\"" + inputModel.getAuthtoken() + "\",\"" + inputModel.getStatusNameForJiraUpdate() + "\")");
        logger.info(result);
        return result;
/*
{
  "serviceAddress": "https://localhost:15000",
  "authtoken": "B34F774A-E200-494F-B2D2-EDD860ED8321",
  "statusNameForJiraUpdate": "Open"
}
{
  "serviceAddress": "https://itsm.csis.ir",
  "authtoken": "25A8098D-7529-4BD2-957A-A00206AE374B",
  "statusNameForJiraUpdate": "4"
}
*/
    }

    @PostMapping("/jira")
    public String configJIRA(@RequestBody ConfigurationJIRA inputModel, HttpServletRequest httpServletRequest) {
        logger.info("Calling api/config/jira service from ip address: {}\t\tJson:{}", httpServletRequest.getRemoteAddr(), inputModel);
        SQLiteDao sqLiteDao = new SQLiteDao();
        String result;
        result = sqLiteDao.executeQuery("delete from jiraConfig where id>0");
        result = result + "\t\t\t" + sqLiteDao.executeQuery("insert into jiraConfig(serviceAddress, username,password,itmsGroupField,keyNameToCreateTaskInIt) VALUES (\"" + inputModel.getServiceAddress() + "\",\"" + inputModel.getUsername() + "\",\"" + inputModel.getPassword() + "\",\"" + inputModel.getItmsGroupField() + "\",\"" + inputModel.getKeyNameToCreateTaskInIt() + "\")");
        logger.info(result);
        return result;
/*
{
  "serviceAddress": "http://localhost:8095",
  "username": "samannorallahy",
  "password": "a~z6Mw5c-VTUt4F",
  "itmsGroupField": "customfield_10202",
  "keyNameToCreateTaskInIt": "SAM"
}
{
  "serviceAddress": "https://jira.csis.ir",
  "username": "servicedeskplus",
  "password": "1qaz@1234",
  "itmsGroupField": "customfield_10600",
  "keyNameToCreateTaskInIt": "ITSMSUPPORT"
}
*/
    }

}
