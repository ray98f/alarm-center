package com.zte.msg.alarmcenter.core.javac;


import lombok.Data;

import java.util.TreeMap;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/11 12:40
 */
@Data
public class ClassInfo {
    private TreeMap<String, Class<?>> classType;

    private long lastModified;

    public ClassInfo(long lastModified) {
        this.classType = new TreeMap<>();
        this.lastModified = lastModified;
    }

    public ClassInfo() {
    }
}
