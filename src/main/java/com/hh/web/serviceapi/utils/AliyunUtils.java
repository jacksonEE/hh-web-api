package com.hh.web.serviceapi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hh.web.serviceapi.configs.AliyunProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 阿里云工具类
 */
@Slf4j
@AllArgsConstructor
@Component
@EnableConfigurationProperties(AliyunProperties.class)
public class AliyunUtils {
    private final AliyunProperties aliyunProperties;

    /**
     * 文本审核
     *
     * @param content 要审核的文本
     * @return 审核结果 true:通过 false:不通过
     */
    public boolean contentCensor(String content) {
        IClientProfile profile = DefaultProfile.getProfile(aliyunProperties.getRegionId(),
                aliyunProperties.getAccessKeyId(), aliyunProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setSysAcceptFormat(FormatType.JSON);
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setSysMethod(com.aliyuncs.http.MethodType.POST);
        textScanRequest.setSysEncoding("UTF-8");
        textScanRequest.setSysRegionId(aliyunProperties.getRegionId());

        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task = new LinkedHashMap<>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("content", content);
        tasks.add(task);

        JSONObject data = new JSONObject();
        data.put("scenes", Collections.singletonList("antispam"));
        data.put("tasks", tasks);

        textScanRequest.setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setSysConnectTimeout(5000);
        textScanRequest.setSysReadTimeout(5000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                if (!"pass".equals(suggestion)) {
                                    return false;
                                }
                            }
                            return true;
                        } else {
                            log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    log.error("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                log.error("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            log.error(e.getMessage(), e);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

}
