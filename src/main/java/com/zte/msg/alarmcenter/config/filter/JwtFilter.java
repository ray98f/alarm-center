package com.zte.msg.alarmcenter.config.filter;

import com.alibaba.fastjson.JSON;
import com.zte.msg.alarmcenter.config.RequestHeaderContext;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.enums.TokenStatus;
import com.zte.msg.alarmcenter.exception.CommonException;
import com.zte.msg.alarmcenter.utils.Constants;
import com.zte.msg.alarmcenter.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * jwt过滤器
 *
 * @author frp
 */
@Slf4j
public class JwtFilter implements Filter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 排除拦截的请求
     */
    private final String[] excludedPages = {
            "/api/v1/login",
            "/api/v1/openapi/token",
            "/api/v1/alarm/level/list",
            "/api/v1/home/alarm/export",
            "/api/v1/alarm/history/export",
            "/api/v1/device/export",
            "/api/v1/map",
            "/api/v1/alarmCodesSync",
            "/api/v1/alarmCodeSync",
            "/api/v1/devicesSync",
            "/api/v1/deviceSync",
            "/api/v1/slotsSync",
            "/api/v1/slotSync",
            "/api/v1/system/dto",
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String openApiPages = Constants.OPENAPI_URL;
        if (httpRequest.getRequestURI().contains(openApiPages) || Arrays.asList(excludedPages).contains(httpRequest.getRequestURI())) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String token = httpRequest.getHeader("Authorization");
            if (token == null || StringUtils.isBlank(token)) {
                request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_EMPTY));
                request.getRequestDispatcher("/error/exthrow").forward(request, response);
                return;
            }
            TokenStatus tokenStatus = TokenUtil.verifySimpleToken(token);
            switch (Objects.requireNonNull(tokenStatus)) {
                //有效
                case VALID:
                    SimpleTokenInfo simpleTokenInfo = TokenUtil.getSimpleTokenInfo(token);
                    new RequestHeaderContext.RequestHeaderContextBuild().user(simpleTokenInfo).build();
                    httpRequest.setAttribute("tokenInfo", simpleTokenInfo);
                    chain.doFilter(httpRequest, httpResponse);
                    break;
                //过期
                case EXPIRED:
                    request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_IS_OVERDUE));
                    request.getRequestDispatcher("/error/exthrow").forward(request, response);
                    break;
                //无效
                default:
                    request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_INVALID));
                    request.getRequestDispatcher("/error/exthrow").forward(request, response);
                    break;
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("jwtFilter init ...");
    }

    @Override
    public void destroy() {
        log.info("jwtFilter destroy ...");
    }
}