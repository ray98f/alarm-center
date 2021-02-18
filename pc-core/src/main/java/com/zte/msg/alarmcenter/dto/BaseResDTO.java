package com.zte.msg.alarmcenter.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/13 17:17
 */
@Data
public class BaseResDTO {
    private Long id;

    private Timestamp createdAt;

    private String createdBy;

    private Timestamp updatedAt;

    private String updatedBy;

}
