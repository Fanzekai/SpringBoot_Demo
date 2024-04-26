package com.example.springboot.demos.service.Impl;

import com.alibaba.excel.ExcelWriter;

import com.example.springboot.demos.entity.ExportUser;
import com.example.springboot.demos.service.AbstractExport;
import com.example.springboot.demos.service.ExportQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fzk
 * @version 1.0
 * @date 2024/4/27  0:36
 */
@Service
@Slf4j
public class ExportImpl extends AbstractExport {

    @Autowired
    private ExportQueue exportQueue;


    @Override
    public void export(ExportUser sysUser) throws InterruptedException {

        //导出
        log.info("导出文件方法执行～～～～～～～～～");
//        export(response,pageSize,t,k,fileName);
        LinkedList<ExportUser> queue = exportQueue.add(sysUser);
        log.info("导出队列：" + queue);
        //休眠时间稍微设置大点，模拟导出处理时间
        Thread.sleep(20000);
        //导出成功后移除当前导出用户
        ExportUser nextSysUser = exportQueue.getNextSysUser();
        log.info("导出执行完毕，移出队列: " + nextSysUser.getName());

    }


    @Override
    public void export(HttpServletResponse response, int pageSize, Object o, Class k, String fileName) throws Exception {
        super.export(response, pageSize, o, k, fileName);
    }

    @Override
    public ExcelWriter getExcelWriter(HttpServletResponse response, String fileName) throws IOException {
        return super.getExcelWriter(response, fileName);
    }

    @Override
    public void complexFillWithTable(Object o, String fileName, HttpServletResponse response) {

    }

    @Override
    public int countExport(Object o) {
        return 0;
    }

    @Override
    public List getExportDetail(Object o) {
        return null;
    }
}
