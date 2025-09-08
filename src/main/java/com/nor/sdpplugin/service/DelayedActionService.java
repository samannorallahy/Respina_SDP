package com.nor.sdpplugin.service;

import com.nor.sdpplugin.dataBase.SQLiteDao;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DelayedActionService {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void scheduleAction(int reqID_SDP, int minute) {
        log.info("Add requestID {} to Scheduler to start in {} minutes", reqID_SDP, minute);
        Runnable task = () -> {
            log.info("Starting scheduled task for requestID: {}", reqID_SDP);
            SQLiteDao sqLiteDao = new SQLiteDao();
            SdpAddRequestService service = new SdpAddRequestService();
            boolean customerReactionStillNull = sqLiteDao.isCustomerReactionStillNull(reqID_SDP);
            if (customerReactionStillNull) {
                log.info("need to update requestID: {} due to customerReactionStillNull after {} minutes", reqID_SDP, minute);
                sqLiteDao.updateCustomerReactionWhenThereIsNoReaction(reqID_SDP);
                try {
                    service.putCallSdpAddWorklogs(String.valueOf(reqID_SDP), 3);
                    service.putCallSdpUpdateStatus(String.valueOf(reqID_SDP), 5);
                } catch (IOException e) {
                    log.error(e.toString());
                }
            } else
                log.info("NO need to update requestID: {} due to customerReaction IN NOT null", reqID_SDP);
        };
        scheduler.schedule(task, minute, TimeUnit.MINUTES);
    }
}
