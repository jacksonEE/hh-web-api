package com.hh.web.serviceapi.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wx")
public class WxMaProperties {

    /**
     * 设置微信小程序的appid
     */
    private String miniAppid;

    /**
     * 设置微信小程序的Secret
     */
    private String miniSecret;

    /**
     * 设置微信小程序消息服务器配置的token
     */
    private String token;

    /**
     * 设置微信小程序消息服务器配置的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;

}
