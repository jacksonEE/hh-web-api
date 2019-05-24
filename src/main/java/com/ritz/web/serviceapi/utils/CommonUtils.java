package com.ritz.web.serviceapi.utils;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

    /**
     * 如果含有 % 转义为 \%
     *
     */
    public static String replacePercentSymbol(String val) {
        if (StringUtils.isNotBlank(val) && val.contains("%")) {
            return val.replace("%", "\\%");
        }
        return val;
    }
}
