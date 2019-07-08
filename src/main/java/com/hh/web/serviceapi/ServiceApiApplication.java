package com.hh.web.serviceapi;

import com.hh.web.serviceapi.frame.core.ApiEngine;
import com.hh.web.serviceapi.frame.http.RequestAttr;
import com.hh.web.serviceapi.frame.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@EnableJpaAuditing
@RestController
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class ServiceApiApplication implements ApplicationListener<ApplicationReadyEvent> {


    @Autowired
    private ApiEngine apiEngine;

    @PostMapping("api")
    public Response api(@RequestBody RequestAttr requestModel, HttpServletRequest request, HttpServletResponse response) {
        return apiEngine.handle(requestModel, request, response);
    }

    @PostMapping("api_batch")
    public List<Response> apiBatch(@RequestBody List<RequestAttr> requestModels,
                             HttpServletRequest request, HttpServletResponse response) {
        return apiEngine.handleBatch(requestModels, request, response);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceApiApplication.class).run(args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("=========================================");
        log.info("*******ServiceApiApplication**启动成功********");
        log.info("=========================================");
    }


}
