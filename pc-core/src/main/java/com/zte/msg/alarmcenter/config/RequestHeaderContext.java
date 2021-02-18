package com.zte.msg.alarmcenter.config;

import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import lombok.Data;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/4/30 15:00
 */
@Data
public class RequestHeaderContext {

    private static final ThreadLocal<RequestHeaderContext> REQUEST_HEADER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private SimpleTokenInfo user;

    public static RequestHeaderContext getInstance() {
        return REQUEST_HEADER_CONTEXT_THREAD_LOCAL.get();
    }

    public void setContext(RequestHeaderContext context) {
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static void clean() {
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.remove();
    }

    private RequestHeaderContext(RequestHeaderContextBuild requestHeaderContextBuild) {
        this.user = requestHeaderContextBuild.user;
        setContext(this);
    }

    @Data
    public static class RequestHeaderContextBuild {

        private SimpleTokenInfo user;

        public RequestHeaderContextBuild user(SimpleTokenInfo user) {
            this.user = user;
            return this;
        }

        public RequestHeaderContext build() {
            return new RequestHeaderContext(this);
        }
    }

}
