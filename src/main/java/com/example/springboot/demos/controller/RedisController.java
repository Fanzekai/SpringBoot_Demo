package com.example.springboot.demos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author fzk
 * @version 1.0
 * @date 2024/4/21  13:47
 */
@RestController
public class RedisController {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_KEY = "distributed_lock";

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 获取分布式锁
     *
     * @param clientId   客户端标识
     * @param expireTime 锁的过期时间（单位：毫秒）
     * @return 是否成功获取锁
     */
    @GetMapping("/acquireLock")
    public boolean acquireLock(String clientId, Long expireTime) {
        try {
            RedisScript<Long> redisScript = new DefaultRedisScript<>(
                    "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end",
                    Long.class
            );
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(LOCK_KEY), clientId, String.valueOf(expireTime));
            return result != null && result.equals(1L);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param clientId 客户端标识
     * @return 是否成功释放锁
     */
    @GetMapping("/releaseLock")
    public boolean releaseLock(String clientId) {
        try {
            RedisScript<Long> redisScript = new DefaultRedisScript<>(
                    "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end",
                    Long.class
            );
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(LOCK_KEY), clientId);
            return result != null && result.equals(RELEASE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 模拟业务逻辑
     *
     * @return
     */
    @GetMapping("/doBusiness")
    public String doBusiness() {
        // 在获取锁后执行业务逻辑
        if (acquireLock("client1", 10000L)) {
            try {
                // 模拟业务处理时间
                Thread.sleep(5000); // 模拟业务处理时间为5秒
                return "Business logic executed successfully";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "Error occurred during business logic execution";
            } finally {
                releaseLock("client1");
            }
        } else {
            return "Failed to acquire lock, unable to execute business logic";
        }
    }
}
