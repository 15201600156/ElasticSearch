package com.zyf.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zyf.service.DemoJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.xxl.job.core.biz.model.ReturnT.FAIL;
import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

@Component
@Slf4j
public class DemoHandler {

    @Autowired
    private  DemoJobService demoJobService;

    @XxlJob("demoHandler")
    public ReturnT<String> execute(String s1) throws Exception {
        XxlJobHelper.log("xxl-job测试任务开始执行。【args: {}】", s1);
        try {
            demoJobService.demoTest(s1);
            XxlJobHelper.log("xxl-job测试任务执行结束。");
            return SUCCESS;
        } catch (Exception e) {
            XxlJobHelper.log("xxl-job测试任务执行出错!", e);
            return FAIL;
        }
    }
    public void init(){
        log.info("init");
    }
    public void destroy(){
        log.info("destory");
    }
}