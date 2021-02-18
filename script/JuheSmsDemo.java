import com.alibaba.fastjson.JSONObject;
import com.zte.msg.pushcenter.pcscript.PcScript;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class JuheSmsDemo implements PcScript {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    @Override
    public Res execute(Map<String, Object> params) {
        StringBuilder var = new StringBuilder();
        Map<String, String> vars = (Map<String, String>) params.get("vars");
        vars.forEach((k, v) -> var.append("#").append(k).append("#").append("=").append(v).append("&"));
        String url = "http://v.juhe.cn/sms/send?";
        StringBuilder url1 = new StringBuilder(url);
        String encode = var.toString();
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url1.append("mobile").append("=").append(params.get("phoneNum"))
                .append("&").append("tpl_id").append("=").append(params.get("code"))
                .append("&").append("tpl_value").append("=").append(encode)
                .append("&").append("key").append("=").append(params.get("secretKey"));
        return parseResponse(net(url1.toString(), "GET"));
    }

    /**
     * @param strUrl 请求地址
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, String method) {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        StringBuilder sb = new StringBuilder();
        try {
            System.out.println(strUrl);
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        System.out.println(rs);
        return rs;
    }


    private Res parseResponse(String res) {
        JSONObject resObj = JSONObject.parseObject(res);
        ErrorCodes errorCode = ErrorCodes.find(resObj.getString("error_code"));
        return new Res(errorCode.getPcCode(), resObj.getString("reason"));
    }

    public enum ErrorCodes {

        ERROR_PHONE_NUM("205401", 32100001, "错误的手机号码"),

        ERROR_TEMPLATE_ID("205402", 32100002, "错误的模版id"),

        NET_ERROR("205403", 32100003, "网络错误"),

        TEMPLATE_VAR_NOR_FORMAT("205404", 32100006, "模版变量不符合规范"),

        EXCEED_LIMIT("205405", 32100004, "号码异常/同一号码发送次数过于频繁"),

        ERROR_KEY("10001", 32100005, "错误的请求KEY"),

        PERMISSION_DEFINED_KEY("10002", 32100005, "该KEY无请求权限"),

        SUCCESS("0", 0, "成功"),

        UNKNOWN("-1", -1, "位置错误");

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
}