package com.zte.msg.alarmcenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/21 10:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Dic extends BaseEntity {

    @TableField(value = "`name`")
    private String name;

    private String type;

    private Integer isEnable;

    private String description;

}
