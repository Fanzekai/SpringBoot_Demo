package com.example.springboot.demos.task;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author fzk
 * @version 1.0
 * @date 2024/4/20  22:44
 */
@Component
@Slf4j
public class ScheduledTasks {

    //@Scheduled(fixedRate = 3000)
    public void run1() {

        log.info("定时任务执行>>>>>>"+System.currentTimeMillis());
    }
}
