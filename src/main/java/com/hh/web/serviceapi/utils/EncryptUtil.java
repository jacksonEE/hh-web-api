package com.hh.web.serviceapi.utils;


import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncryptUtil {

    /**
     * 里层sha-1加密，外层md5加密
     *
     */
    public static String shaAndMd5(String inputStr) {
        return md5(sha(inputStr));
    }

    /**
     * md5加密 32位长度
     *
     */
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }

    /**
     * sha-1加密 40位长度
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }

    /**
     * md5或者sha-1加密
     *
     * @param inputText     要加密的内容
     * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
     * @return 加密后的内容 异常的时候返回null
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (StringUtils.isBlank(inputText)) {
            return null;
        }
        if (StringUtils.isBlank(algorithmName)) {
            algorithmName = "md5";
        }
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            return hex(s);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 返回十六进制字符串
     *
     */
    private static String hex(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString();
    }

    private EncryptUtil() {
    }
}
