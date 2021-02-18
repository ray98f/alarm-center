package com.zte.msg.alarmcenter.config.aspect;

import com.zte.msg.alarmcenter.annotation.PermissionCheck;
import com.zte.msg.alarmcenter.mapper.RoleMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author frp
 */
@Aspect
@Component
public class PermissionCheckAspect {

    public static final String STRING_NULL = "";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ARG_NAMES = "pjp,permissionCheck";
    public static final String REGEX;

    static {
        REGEX = ",";
    }

    @Resource
    private RoleMapper roleMapper;

    /**
     * Spring中使用@Pointcut注解来定义方法切入点
     *
     * @Pointcut 用来定义切点，针对方法
     * @Aspect 用来定义切面，针对类 后面的增强均是围绕此切入点来完成的
     * 此处仅配置被我们刚才定义的注解：AuthToken修饰的方法即可
     */
    @Pointcut("@annotation(permissionCheck)")
    public void doPermissionCheck(PermissionCheck permissionCheck) {
    }

    /**
     * 此处我使用环绕增强，在方法执行之前或者执行之后均会执行。
     */
    @Around(value = "doPermissionCheck(permissionCheck)", argNames = ARG_NAMES)
    public Object deBefore(ProceedingJoinPoint pjp, PermissionCheck permissionCheck) throws Throwable {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
//        // 获取访问该方法所需的permissionName信息
//        String[] permissionName = permissionCheck.permissionName();
//        if (permissionName.length == 0) {
//            String userName = TokenUtil.getCurrentUserName();
//            if (userName != null && !STRING_NULL.equals(userName)) {
//                return pjp.proceed();
//            }
//            throw new CommonException(ErrorCode.USER_NOT_LOGIN_IN);
//        } else {
//            String token = request.getHeader(AUTHORIZATION);
//            SimpleTokenInfo simpleTokenInfo = TokenUtil.getSimpleTokenInfo(token);
//            List<String> roles = roleMapper.selectMenuRoles(simpleTokenInfo.getRoleId());
//            roles.remove(STRING_NULL);
//            for (String str : permissionName) {
//                if (roles.contains(str)) {
                    return pjp.proceed();
//                }
//            }
//            throw new CommonException(ErrorCode.PERMISSION_FAILED);
//        }
    }
}
