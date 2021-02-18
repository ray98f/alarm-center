package com.zte.msg.alarmcenter.enums;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/14 9:03
 */
public enum AppPlatforms {

    /**
     * 安卓
     */
    ANDROID(1),

    /**
     * IOS
     */
    IOS(2),

    /**
     * IOS和android
     */
    ALL(3),


    UNKNOWN(-1);

    private Integer type;

    AppPlatforms(Integer type) {
        this.type = type;
    }

    public static AppPlatforms valueOf(Integer type) {
        for (AppPlatforms appPlatforms : AppPlatforms.values()) {
            if (appPlatforms.type.equals(type)) {
                return appPlatforms;
            }
        }
        return UNKNOWN;
    }

    public Integer value() {
        return this.type;
    }
}
