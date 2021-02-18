import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.zte.msg.pushcenter.pcscript.PcScript;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.stream.Collectors;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/18 12:40
 */
public class JiGuangAppPush implements PcScript {

    private static final String URL = "https://api.jpush.cn/v3/push";

    public String getAuthorization(Map<String, Object> params) {
        String appKey = params.get("appKey").toString();
        String masterSecret = params.get("masterSecret").toString();
        return "Basic " + Base64.getUrlEncoder().encodeToString((appKey + ":" + masterSecret).getBytes());
    }

    public String convertAppPushParam(Map<String, Object> params) {
        String pushParamJsonStr = convert(params);
        System.out.println("JiGuang pushing:  " + pushParamJsonStr);
        return pushParamJsonStr;
    }

    private String post(String authorization, String pushParmJsonStr) {
        String returnJson = null;
        Header[] headers = HttpHeader.custom()
                .other("Authorization", authorization.trim())
                .build();
        try {
            CloseableHttpClient client = HCB.custom().build();
            HttpConfig config = HttpConfig.custom()
                    .headers(headers)
                    .url(URL)
                    .json(pushParmJsonStr)
                    .encoding("utf-8")
                    .client(client)
                    .inenc("utf-8")
                    .inenc("utf-8");
            returnJson = HttpClientUtil.post(config);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnJson;
    }

    @Override
    public Res execute(Map<String, Object> params) {
        String res = post(getAuthorization(params), convertAppPushParam(params));
        System.out.println(res);

        JSONObject resObj = JSONObject.parseObject(res);
        JSONObject error = resObj.getObject("error", JSONObject.class);

        if (Objects.nonNull(error)) {
            String code = error.getString("code");
            ErrorCodes errorCode = ErrorCodes.find(code);
            System.out.println("app push error code: " + code);
            return new Res(errorCode.getPcCode(), errorCode.getMessage());
        }
        return new Res(0, "成功");
    }

    public enum ErrorCodes {

        ERROR_PARAM("1002", 32100001, "缺少了必须的参数"),

        ERROR_PARAM_1("1003", 32100002, "参数值不合法"),

        AUTH_FAIL("1004", 32100003, "验证失败"),

        DATA_TOO_LARGE("1005", 32100006, "消息体太大"),

        ERROR_APP_KEY("1008", 32100004, "app_key 参数非法"),

        NOT_SUPPORT_KEY("1009", 32100005, "推送对象中有不支持的 key"),

        SUCCESS("0", 0, "成功"),

        UNKNOWN("-1", -1, "未收录错误");

        private String code;

        private Integer pcCode;

        private String message;

        ErrorCodes(String code, Integer pcCode, String message) {
            this.code = code;
            this.pcCode = pcCode;
            this.message = message;
        }

        private static ErrorCodes find(String code) {
            for (ErrorCodes errorCode : ErrorCodes.values()) {
                if (errorCode.code.equals(code)) {
                    return errorCode;
                }
            }
            return UNKNOWN;
        }

        public String getCode() {
            return code;
        }

        public Integer getPcCode() {
            return pcCode;
        }

        public String getMessage() {
            return message;
        }
    }

    private enum Platform {
        /**
         * 安卓
         */
        ANDROID(1, "android"),

        /**
         * IOS
         */
        IOS(2, "ios"),

        /**
         * 所有
         */
        ALL(3, "all");

        private Integer type;

        private String platform;

        Platform(Integer type, String platform) {
            this.type = type;
            this.platform = platform;
        }

        public static Platform valueOf(Integer type) {
            for (Platform platforms : Platform.values()) {
                if (platforms.type.equals(type)) {
                    return platforms;
                }
            }
            return null;
        }

        public Integer getType() {
            return this.type;
        }

        public String getPlatform() {
            return this.platform;
        }
    }

    public enum AppMessageType {
        /**
         * 通知
         */
        NOTIFICATION(1, "notification"),

        /**
         * 自定义消息
         */
        MESSAGE(2, "message"),


        UNKNOWN(-1, "unknown");

        private Integer type;

        private String messageType;

        AppMessageType(Integer type, String messageType) {
            this.type = type;
            this.messageType = messageType;
        }

        public static AppMessageType valueOf(Integer type) {
            for (AppMessageType messageType : AppMessageType.values()) {
                if (messageType.type.equals(type)) {
                    return messageType;
                }
            }
            return UNKNOWN;
        }

        public Integer value() {
            return this.type;
        }
    }

    /**
     * @param params
     * @return
     */
    public String convert(Map<String, Object> params) {

        JSONObject pushParamJson = new JSONObject();
        List<Platform> platformList = Collections.singletonList(Platform.valueOf(Integer.parseInt(params.get("targetPlatform").toString())));
        String[] registrationIds = (String[]) params.get("registrationId");
        //构建推送平台信息
        JSONArray platform = buildPlatform(platformList, new JSONArray());
        //构建推送目标信息
        JSONObject audience = buildAudience(registrationIds, new JSONObject());
        //构建安卓的推送信息
        JSONObject notification = new JSONObject();
        buildAndroidInfo(notification, platformList, params);
        buildIosInfo(notification, platformList, params);
        //构建IOS参数信息
        //设置推送平台信息
        setPlatform(platform, pushParamJson);
        //设置推送目标信息
        setAudience(audience, pushParamJson);
        //设置推送信息
//        pushParamJson.put(AppMessageType.valueOf(Integer.parseInt(params.get("messageType").toString())).messageType, notification);
        pushParamJson.put(AppMessageType.NOTIFICATION.messageType, notification);

        JSONObject options = new JSONObject();
        buildOptions(options);
        //设置options信息
        pushParamJson.put("options", options);
        return pushParamJson.toString();
    }

    private static void setAudience(JSONObject audience, JSONObject pushParamJson) {
        if (audience == null) {
            pushParamJson.put("audience", "all");//发广播
        } else {
            pushParamJson.put("audience", audience);
        }

    }

    private static void setPlatform(JSONArray platform, JSONObject pushParamJson) {
        if (platform == null) {
            pushParamJson.put("platform", "all");
        } else {
            pushParamJson.put("platform", platform);
        }

    }

    private static void buildOptions(JSONObject options) {
        options.put("time_to_live", 60);
        options.put("apns_production", false);
    }

    private static void buildIosInfo(JSONObject notification,
                                     List<Platform> platformList, Map<String, Object> appPushParam) {
        if (Objects.isNull(platformList)) {
            return;
        }
        if (platformList.contains(Platform.ALL) || platformList.contains(Platform.IOS)) {
            JSONObject ios = new JSONObject();
            ios.put("alert", appPushParam.get("content"));
            //通知提示声音或警告通知
            ios.put("sound", "default");
            //应用角标
            ios.put("badge", "+1");
            //ios额外参数
            JSONObject iosExtras = new JSONObject();
            notification.put("ios", ios);
        }
    }

    private static void buildAndroidInfo(JSONObject notification,
                                         List<Platform> platformList, Map<String, Object> appPushParam) {
        if (Objects.isNull(platformList)) {
            return;
        }
        if (platformList.contains(Platform.ANDROID) || platformList.contains(Platform.ALL)) {
            JSONObject android = new JSONObject();
            android.put("alert", appPushParam.get("content"));
            android.put("title", appPushParam.get("title"));
            //设置通知栏样式
//            android.put("builder_id", 1);
            notification.put("android", android);
            System.out.println("android message: " + android.toString());
        }

    }

    private static JSONObject buildAudience(String[] audienceMap, JSONObject audience) {
        if (audienceMap == null || audienceMap.length == 0) {
            return null;
        }
        JSONArray audienceArray = new JSONArray();
        audienceArray.addAll(Arrays.asList(audienceMap));
        audience.put("registration_id", audienceArray);
        return audience;

    }

    /**
     * 构建推送平台
     *
     * @param platformList
     * @param platform
     */
    private static JSONArray buildPlatform(List<Platform> platformList, JSONArray platform) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        if (platformList.contains(Platform.ALL)) {
            platform.addAll(Arrays.stream(Platform.values()).filter(o -> o != Platform.ALL).map(Platform::getPlatform).collect(Collectors.toList()));
            return platform;
        }
        if (platformList.size() > 0) {
            for (Platform platFormEnum : platformList) {
                String platformStr = platFormEnum.toString().toLowerCase();
                platform.add(platformStr);
            }
            return platform;
        } else {
            return null;
        }
    }

}