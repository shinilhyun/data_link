package com.enjoybt.data_link.test.controller;

import com.enjoybt.data_link.test.service.TestService;
import org.apache.ibatis.io.ResolverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    TestService testService;

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("test")
    @ResponseBody
    public String test() throws Exception {
        return testService.sqlTest();
    }

    @GetMapping("batchTest")
    @ResponseBody
    public void test2() throws Exception {
        testService.batchTest();
    }
}