package com.ritz.web.serviceapi.configs;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfig {
    private WxMaProperties properties;

    @Autowired
    public WxMaConfig(WxMaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WxMaService services() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(properties.getMiniAppid());
        config.setSecret(properties.getMiniSecret());
        config.setToken(properties.getToken());
        config.setAesKey(properties.getAesKey());
        config.setMsgDataFormat(properties.getMsgDataFormat());
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }

}
