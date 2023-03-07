package com.zte.msg.alarmcenter.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/14 16:54
 */
@Configuration
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //默认未删除
        setFieldValByName("isDeleted", Constants.DATA_NOT_DELETED, metaObject);
        //创建时间默认当前时间
        setFieldValByName("createdAt", new Timestamp(System.currentTimeMillis()), metaObject);

        setFieldValByName("createdBy", TokenUtil.getCurrentUserName(), metaObject);

        setFieldValByName("updatedBy", TokenUtil.getCurrentUserName(), metaObject);
        //更新时间默认当前时间
        setFieldValByName("updatedAt", new Timestamp(System.currentTimeMillis()), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        setFieldValByName("updateBy", TokenUtil.getCurrentUserName(), metaObject);

        setFieldValByName("updatedAt", new Timestamp(System.currentTimeMillis()), metaObject);
    }

}
