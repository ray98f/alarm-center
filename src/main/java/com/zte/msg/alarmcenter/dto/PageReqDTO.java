package com.zte.msg.alarmcenter.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Data
@ApiModel
public class PageReqDTO {

    @Min(value = 1L, message = "32000008")
    @ApiModelProperty(value = "当前页码", required = true)
    private Long page;

    @Range(min = 1, max = 500, message = "32000003")
    @ApiModelProperty(value = "每页条数。范围：1-50", required = true)
    private Long size;

    public <T> Page<T> of() {
        return new Page<>(this.getPage(), this.getSize());
    }

}
