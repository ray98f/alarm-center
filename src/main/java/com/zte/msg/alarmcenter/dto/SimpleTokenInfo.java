package com.zte.msg.alarmcenter.dto;

import lombok.Data;

/**
 * @author frp
 */
@Data
public class SimpleTokenInfo {
    private String userId;
    private String userName;
    private String userRealName;
    private Long roleId;

    public SimpleTokenInfo(){}

    public SimpleTokenInfo(String userId, String userName, String userRealName, Long roleId) {
        this.userId = userId;
        this.userName = userName;
        this.userRealName = userRealName;
        this.roleId = roleId;
    }

    public SimpleTokenInfo(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
