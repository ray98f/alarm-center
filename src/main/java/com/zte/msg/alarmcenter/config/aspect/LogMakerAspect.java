package com.zte.msg.alarmcenter.config.aspect;

import com.zte.msg.alarmcenter.annotation.LogMaker;
import com.zte.msg.alarmcenter.entity.OperationLog;
import com.zte.msg.alarmcenter.mapper.OperationLogMapper;
import com.zte.msg.alarmcenter.mapper.RoleMapper;
import com.zte.msg.alarmcenter.utils.IpUtils;
import com.zte.msg.alarmcenter.utils.JSONUtils;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author frp
 */
@Aspect
@Component
public class LogMakerAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 注解Pointcut切入点
     * 定义出一个或一组方法，当执行这些方法时可产生通知
     * 指向你的切面类方法
     * 由于这里使用了自定义注解所以指向你的自定义注解
     */
    @Pointcut("@annotation(com.zte.msg.alarmcenter.annotation.LogMaker)")
    public void logPointCut() {
    }

    /**
     * 使用环绕通知
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //异步保存日志
        saveLog(point, time);
        return result;
    }

    void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = new OperationLog();
        //获取方法上的自定义注解
        LogMaker syslog = method.getAnnotation(LogMaker.class);
        if (syslog != null) {
            // 注解上的描述
            operationLog.setOperationType(syslog.value());
        }
        //获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        //请求的参数
        operationLog.setParams(JSONUtils.beanToJsonString(request.getParameterMap()));
        //设置IP地址
        operationLog.setHostIp(IpUtils.getIpAddr(request));
        //用户名
        if (null == TokenUtil.getCurrentUserName()) {
            operationLog.setUserName("获取用户信息为空");
        } else {
            operationLog.setUserName(TokenUtil.getCurrentUserName());
        }
        //用时
        operationLog.setOperationTime(new Timestamp(time));
        //系统当前时间
        operationLog.setUseTime(System.currentTimeMillis());
        //保存系统日志
        operationLogMapper.addOperationLog(operationLog);
    }

}
