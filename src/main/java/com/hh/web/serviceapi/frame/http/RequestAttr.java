package com.hh.web.serviceapi.frame.http;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class RequestAttr implements Serializable {

    // 服务名
    private String api;

    // 请求参数
    private Map<String, Object> params;

    /**
     * constructor
     */
    public RequestAttr(String api) {
        this.params = new HashMap<>();
        this.api = api;
    }

    /**
     * 添加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void addParameter(String name, Object value) {
        params.put(name, value);
    }

    /**
     * 移除参数
     *
     * @param name 参数名
     */
    public void removeParameter(String name) {
        params.remove(name);
    }

    /**
     * 获取参数
     *
     * @param key key
     * @param <T> 值类型
     * @return 参数值
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key) {
        return (T) params.get(key);
    }


}
