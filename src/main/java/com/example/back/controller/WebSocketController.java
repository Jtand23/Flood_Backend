package com.example.back.controller;

import com.example.back.model.entity.FloodData;
import com.example.back.model.dto.FloodDataDTO;
import com.example.back.service.FloodDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 定义一个控制器类
 * 开启定时任务支持
 * 接收请求后，定时将洪水数据发送到前端
 *
 * @author russ2
 */
@Slf4j
@Controller
@EnableScheduling
@CrossOrigin(origins = "http://localhost:5174")
@Async
public class WebSocketController {

    private final FloodDataService floodDataService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ThreadPoolTaskScheduler scheduler;


    private ScheduledFuture<?> scheduledTask;
    private int currentTableId = 0;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    //存储当前请求的时序编号
    private Integer currentSId;


    @Autowired
    public WebSocketController(FloodDataService floodDataService,
                               SimpMessagingTemplate messagingTemplate,
                               ThreadPoolTaskScheduler scheduler) {
        this.floodDataService = floodDataService;
        this.messagingTemplate = messagingTemplate;
        this.scheduler = scheduler;

    }


    @MessageMapping("/subscribe-flood-data")
    public void handleSubscribe(@Payload Map<String, Object> payload) {
        if (!isRunning.getAndSet(true)) {
            Object rawSId = payload.get("sId");
            if(rawSId instanceof Integer) {
                this.currentSId = (Integer) rawSId;
                scheduledTask = scheduler.scheduleAtFixedRate(this::sendFloodData, 5000);
                log.info("Client subscribed to flood data updates with sId: {}", currentSId);
            } else {
                log.error("Invalid sId received: {}.", rawSId);
            }

        }
    }


    @MessageMapping("/unsubscribe-flood-data")
    public void handleUnsubscribe() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTask = null;
        }

        if (isRunning.compareAndSet(true, false)) {
            log.info("Client unsubscribed from flood data updates.");
        }

    }

    /**
     * 定时任务，每${websocket.send.delay}毫秒执行一次
     * 异步执行以避免阻塞主线程
     */
    @Scheduled(fixedRateString = "${websocket.send.delay}")
    @Async
    public void sendFloodData() {
        // 如果定时任务未被激活，则直接返回
        if (!isRunning.get()) {
            return;
        }

        // 预设的数据表数量
        int tableCount = 314;
        // 检查当前数据表ID是否小于等于总数
        if (currentTableId <= tableCount) {
            // 格式化数据表ID为三位数
            String tableId = String.format("%03d", currentTableId);
            // 查询指定数据表和指定时序的洪水数据
            List<FloodData> floodDataList = floodDataService.findByTableAndSequence(tableId, currentSId);
            // 转换数据格式为DTO
            List<FloodDataDTO> dtoList = floodDataList.stream()
                    .map(floodData -> new FloodDataDTO(floodData.getTotalWate(), floodData.parseGeomYclToDoubles())).toList();
            // 发送数据给所有订阅的客户端
            messagingTemplate.convertAndSend("/topic/flood-data", dtoList);
            // 记录日志信息
            log.info("Sent data for tableId: {},sequenceId: {}", tableId, currentSId);
            log.info("返回了 {} 条洪水数据条目.", dtoList.size());
            // 增加数据表ID计数器
            currentTableId++;
        }
    }
}

