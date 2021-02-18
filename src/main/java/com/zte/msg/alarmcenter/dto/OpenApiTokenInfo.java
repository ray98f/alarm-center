package com.zte.msg.alarmcenter.dto;

import lombok.Data;

/**
 * @author frp
 */
@Data
public class OpenApiTokenInfo {
    private Integer appId;
    private String appName;
    private String appKey;
    private String appSecret;
    private String role;

    public OpenApiTokenInfo(){}

    public OpenApiTokenInfo(Integer appId, String appName, String appKey, String appSecret, String role) {
        this.appId = appId;
        this.appName = appName;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.role = role;
    }

    public OpenApiTokenInfo(Integer appId, String appName, String appKey, String appSecret) {
        this.appId = appId;
        this.appName = appName;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }
}
