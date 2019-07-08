package com.hh.web.serviceapi.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云SDK配置
 *
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "aliyun")
public class AliyunProperties {
    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * regionId
     */
    private String regionId;
}
