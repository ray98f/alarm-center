package com.zte.msg.alarmcenter.dto;

import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class SimpleTokenInfo {
    private String userId;
    private String userName;
    private String userRealName;
    private List<Long> roleIds;

    public SimpleTokenInfo(){}

    public SimpleTokenInfo(String userId, String userName, String userRealName, List<Long> roleIds) {
        this.userId = userId;
        this.userName = userName;
        this.userRealName = userRealName;
        this.roleIds = roleIds;
    }

    public SimpleTokenInfo(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
