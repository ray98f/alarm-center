import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zte.msg.pushcenter.pcscript.ParamConstants;
import com.zte.msg.pushcenter.pcscript.PcScript;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/22 10:36
 */
public class TencentSmsDemo implements PcScript {

    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    private final static Charset UTF8 = StandardCharsets.UTF_8;

    private final static String CT_JSON = "application/json";
    public static final String CODE = "Code";
    public static final String OK = "Ok";

    public static byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(UTF8));
    }

    public static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(UTF8));
        return DatatypeConverter.printHexBinary(d).toLowerCase();
    }

    public String net(Map<String, Object> params) {
        String secretId = params.get(ParamConstants.SECRET_ID).toString();
        String secretKey = params.get(ParamConstants.SECRET_KEY).toString();
        StringBuilder res = new StringBuilder();
        try {
            String service = "sms";
            String host = "sms.tencentcloudapi.com";
            String action = "SendSms";
            String version = "2019-07-11";
            String algorithm = "TC3-HMAC-SHA256";
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = sdf.format(new Date(Long.parseLong(timestamp + "000")));
            String httpRequestMethod = "POST";
            String canonicalUri = "/";
            String canonicalQueryString = "";
            String canonicalHeaders = "content-type:application/json\n" + "host:" + host + "\n";
            String signedHeaders = "content-type;host";

            String payload = buildPayload(params);
            String hashedRequestPayload = sha256Hex(payload);
            String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                    + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;
            String credentialScope = date + "/" + service + "/" + "tc3_request";
            String hashedCanonicalRequest = sha256Hex(canonicalRequest);
            String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
            byte[] secretDate = hmac256(("TC3" + secretKey).getBytes(UTF8), date);
            byte[] secretService = hmac256(secretDate, service);
            byte[] secretSigning = hmac256(secretService, "tc3_request");
            String signature = DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
            String authorization = algorithm + " " + "Credential=" + secretId + "/" + credentialScope + ", "
                    + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;
            HttpURLConnection conn = null;
            BufferedReader reader;
            try {
                URL url = new URL("https://" + host);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(DEF_CONN_TIMEOUT);
                conn.setReadTimeout(DEF_READ_TIMEOUT);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("Authorization", authorization);
                conn.setRequestProperty("Content-Type", CT_JSON);
                conn.setRequestProperty("Host", host);
                conn.setRequestProperty("X-TC-Action", action);
                conn.setRequestProperty("X-TC-Timestamp", timestamp);
                conn.setRequestProperty("X-TC-Version", version);
                conn.setRequestProperty("X-TC-Language", "zh-CN");
                conn.connect();

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(payload);
                writer.flush();

                InputStream is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String strRead;
                while ((strRead = reader.readLine()) != null) {
                    res.append(strRead);
                }
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private String buildPayload(Map<String, Object> params) {

        Map<String, String> vars = (Map<String, String>) params.get(ParamConstants.VARS);
        return "{\"PhoneNumberSet\":[\"+86" +
                params.get(ParamConstants.PHONE_NUM).toString() +
                "\"],\"TemplateParamSet\":" +
                JSON.toJSONString(vars.values()) +
                ",\"TemplateID\":\"" +
                params.get(ParamConstants.TEMPLATE_ID).toString() +
                "\",\"SmsSdkAppid\":\"" +
                params.get(ParamConstants.APP_ID).toString() +
                "\",\"Sign\":\"" +
                params.get(ParamConstants.SIGN).toString() +
                "\"}";
    }


    @Override
    public Res execute(Map<String, Object> params) {
        String res = net(params);
        System.out.println(res);
        if (res == null || "".equals(res)) {
            return new Res(-1, "未知错误");
        }

        Map<String, Object> innerMap = JSONObject.parseObject(res).getInnerMap();

        JSONObject error = (JSONObject) innerMap.get("Error");
        if (null != error) {
            String code = error.getString(CODE);
            ErrorCodes errorCodes = ErrorCodes.find(code);
            return new Res(errorCodes.pcCode, errorCodes.message);
        }

        JSONObject response = (JSONObject) innerMap.get("Response");

        JSONArray sendStatusSet = (JSONArray) response.get("SendStatusSet");
        JSONObject sendStatus = (JSONObject) sendStatusSet.get(0);
        if (!sendStatus.get(CODE).toString().equals(OK)) {
            String code = sendStatus.get(CODE).toString();
            ErrorCodes errorCodes = ErrorCodes.find(code);
            return new Res(errorCodes.pcCode, errorCodes.message);
        }
        return new Res(0, "success");
    }

    public enum ErrorCodes {

        ERROR_PHONE_NUM("InvalidParameterValue.IncorrectPhoneNumber", 32100001, "手机号格式错误"),

        ERROR_TEMPLATE_ID("FailedOperation.TemplateIncorrectOrUnapproved", 32100002, "模版未审批或请求的内容与审核通过的模版内容不匹配"),

        NET_ERROR("InternalError.SendAndRecvFail", 32100003, "接口超时或后短信收发包超时，请检查您的网络是否有波动"),

        TEMPLATE_VAR_NOR_FORMAT("InvalidParameterValue.TemplateParameterFormatError", 32100006, "验证码模板参数格式错误，验证码类模版，模版变量只能传入0 - 6位（包括6位）纯数字"),

        EXCEED_30S_LIMIT("LimitExceeded.PhoneNumberThirtySecondLimit", 32100004, "单个手机号30秒内下发短信条数超过设定的上限，可自行到控制台调整短信频率限制策略。"),

        ERROR_KEY("AuthFailure.SignatureFailure", 32100005, "签名错误。 签名计算错误，请对照调用方式中的签名方法文档检查签名计算过程。"),

        EXCEED_DAILY_LIMIT("LimitExceeded.PhoneNumberDailyLimit", 32100004, "单个手机号日内下发短信条数超过设定的上限"),

        SUCCESS("0", 0, "成功"),

        UNKNOWN("-1", -1, "未知错误");

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