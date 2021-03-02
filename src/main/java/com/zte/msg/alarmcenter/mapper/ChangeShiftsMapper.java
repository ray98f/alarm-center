package com.zte.msg.alarmcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zte.msg.alarmcenter.entity.ChangeShifts;
import com.zte.msg.alarmcenter.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * @author frp
 */
@Mapper
@Repository
public interface ChangeShiftsMapper extends BaseMapper<ChangeShifts> {
    /**
     * 添加交接班记录
     * @param changeShifts
     * @return
     */
    int addChangeShifts(ChangeShifts changeShifts);

}
