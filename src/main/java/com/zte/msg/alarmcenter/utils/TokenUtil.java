package com.zte.msg.alarmcenter.utils;

import com.zte.msg.alarmcenter.config.RequestHeaderContext;
import com.zte.msg.alarmcenter.dto.SimpleTokenInfo;
import com.zte.msg.alarmcenter.dto.req.UserReqDTO;
import com.zte.msg.alarmcenter.enums.ErrorCode;
import com.zte.msg.alarmcenter.enums.TokenStatus;
import com.zte.msg.alarmcenter.exception.CommonException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * description:
 *
 * @author frp
 * @version 1.0
 * @date 2020/12/23 15:42
 */
public class TokenUtil {

    private static final String SIMPLE_TOKEN_SECRET = "ZTE96952f774ce244fcb42af56062e519b3lFOGZ3YaWuCZS";

    /**
     * 获得UUID
     * 32位
     *
     * @return String UUID
     */
    public static String getUuId() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", Constants.EMPTY);
    }

    /**
     * 生成随机字符
     *
     * @param length 字符长度
     * @return String
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成时间戳
     *
     * @return String
     */
    public static String getTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        return (time + Constants.EMPTY);
    }

    /**
     * 完成Unicode到String格式转换
     *
     * @param unicode 待转换字符串
     */
    public static String unicodeToString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * 生成项目密匙
     */
    private static SecretKey generalKey(String stringKey) {
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    /**
     * Simple
     * 生成Token
     *
     * @param item OpenApiToken信息
     * @return String
     * @throws Exception Token校验失败
     */
    public static String createSimpleToken(UserReqDTO item) throws Exception {
        //默认token有效时间为2小时
        return createSimpleToken(item, 60 * 60 * 2 * 1000);
    }

    /**
     * Simple
     * 根据请求登录的信息生成令牌
     *
     * @param item      登录请求相关信息，同时也是令牌解密所需验证信息
     * @param ttlMillis 令牌有效时间
     * @return 返还生成的令牌
     */
    public static String createSimpleToken(UserReqDTO item, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setId(String.valueOf(item.getId()))
                .setSubject(item.getUserName())
                .claim("userRealName", item.getUserRealName())
                .claim("roleIds", item.getRoleIds())
                .setIssuedAt(now)
                .signWith(generalKey(SIMPLE_TOKEN_SECRET));
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * Simple
     * 验证令牌，成功则返还令牌所携带的信息
     */
    private static SimpleTokenInfo simpleParseToken(String token) throws JwtException {
        Jws<Claims> jws;
        try {
            jws = Jwts.parser()
                    .setSigningKey(generalKey(SIMPLE_TOKEN_SECRET))
                    .parseClaimsJws(token);
        } catch (JwtException ex) {
            return null;
        }
        Claims res = jws.getBody();
        return new SimpleTokenInfo(
                res.getId(),
                res.getSubject(),
                res.get("userRealName", String.class),
                res.get("roleIds", List.class)
        );
    }

    /**
     * Simple
     * 获取开放平台登录信息
     *
     * @return
     */
    public static SimpleTokenInfo getSimpleTokenInfo(String token) {
        SimpleTokenInfo simpleTokenInfo = null;
        try {
            simpleTokenInfo = simpleParseToken(token);
        } catch (JwtException e) {
            e.printStackTrace();
        }
        // 401
        if (token == null || Constants.EMPTY.equals(token) || simpleTokenInfo == null) {
            throw new CommonException(ErrorCode.AUTHORIZATION_CHECK_FAIL);
        }
        return simpleTokenInfo;
    }

    public static String getCurrentUserName() {
        String userName;
        try {
            userName = RequestHeaderContext.getInstance().getUser().getUserName();
        } catch (Exception e) {
            userName = "";
        }
        return userName;
    }

    /**
     * Simple
     * 校验token
     *
     * @param authorization
     * @return TokenStatus
     */
    public static TokenStatus verifySimpleToken(String authorization) {
        TokenStatus result;
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SIMPLE_TOKEN_SECRET)
                    .parseClaimsJws(authorization)
                    .getBody();
            final Date exp = claims.getExpiration();
            if (exp.before(new Date(System.currentTimeMillis()))) {
                result = TokenStatus.EXPIRED;
            } else {
                result = TokenStatus.VALID;
            }
        } catch (Exception e) {
            result = TokenStatus.INVALID;
        }
        return result;
    }
}