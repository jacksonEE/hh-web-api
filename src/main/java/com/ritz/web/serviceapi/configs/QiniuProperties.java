package com.ritz.web.serviceapi.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛配置
 *
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiniuProperties {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * bucket
     */
    private String bucket;

    /**
     * 默认外链域名
     */
    private String domain;
}
