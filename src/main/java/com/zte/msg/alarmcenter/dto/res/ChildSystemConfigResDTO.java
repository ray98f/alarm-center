package com.zte.msg.alarmcenter.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/2/19 10:23
 */
@Data
@ApiModel
public class ChildSystemConfigResDTO {

    private Long id;

    @ApiModelProperty(value = "父节点id")
    private Integer pid;

    @ApiModelProperty(value = "子系统名称")
    private String name;

    @ApiModelProperty(value = "SID")
    private Integer sid;

    @ApiModelProperty(value = "服务器ip")
    private String serverIp;

    @ApiModelProperty(value = "服务器端口")
    private Integer serverPort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "状态, 0-离线，1-在线")
    private Integer isOnline;

    @ApiModelProperty(value = "数据子集")
    private List<ChildSystemConfigResDTO> children;
}
