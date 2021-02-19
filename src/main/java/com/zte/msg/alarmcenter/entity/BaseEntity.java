package com.zte.msg.alarmcenter.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/15 8:54
 */
@Data
public class BaseEntity {

    @ApiModelProperty(hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @JsonIgnore
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Timestamp createdAt;

    @JsonIgnore
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "updated_at", fill = FieldFill.INSERT, update = "NOW()")
    private Timestamp updatedAt;

    @TableField(value = "updated_by", fill = FieldFill.INSERT)
    private String updatedBy;

    @JsonIgnore
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer isDeleted;
}
