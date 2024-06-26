package com.example.springboot.demos.controller;

import com.example.springboot.demos.entity.ExportUser;
import com.example.springboot.demos.service.Impl.ExportImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fzk
 * @version 1.0
 * @date 2024/4/27  0:51
 */

//导出任务方案

@RestController
@RequestMapping("/export")
@Slf4j
public class ExportController {

    @Autowired
    private ExportImpl export;


    @PostMapping("/exportFile")
    public void exportFile() {
        new Thread(
                () -> {
                    Thread thread1 = Thread.currentThread();
                    ExportUser sysUser =new ExportUser();
                    sysUser.setName(thread1.getName());

                    try {
                        export.export(sysUser);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
    }
}
