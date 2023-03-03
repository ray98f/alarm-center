package com.zte.msg.alarmcenter.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class MapperAspect {
    @AfterReturning("execution(* com.zte.msg.alarmcenter.mapper.AlarmLevelMapper.modifyAlarmLevel(..))")
    public void logServiceAccess(JoinPoint joinPoint) {

    }

    @Pointcut("execution(* com.zte.msg.alarmcenter.mapper.AlarmLevelMapper.modifyAlarmLevel(..))")
    private void pointCutMethod() {
    }

    /**
     * 声明环绕通知
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}


