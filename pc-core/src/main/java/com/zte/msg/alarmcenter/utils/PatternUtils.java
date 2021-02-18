package com.zte.msg.alarmcenter.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/12 11:26
 */
public class PatternUtils {

    public static List<String> getParams(String content) {

        List<String> params = new ArrayList<>();
        Matcher matcher = Constants.SMS_CONTENT_PATTERN_1.matcher(content);
        while (matcher.find()) {
            params.add(matcher.group(1));
        }
        Matcher matcher1 = Constants.SMS_CONTENT_PATTERN_2.matcher(content);
        while (matcher1.find()) {
            params.add(matcher1.group(1));
        }
        return params;
    }

    public static String buildContent(String origin, Object... replacement) {

        return String.format(origin
                .replaceAll("#.*?#", "%s")
                .replaceAll("\\{.*?}", "%s"), replacement);
    }

    public static boolean validPhoneNums(String... phoneNums) {
        Pattern compile = Pattern.compile(Constants.PHONE_NUM_PATTERN);
        if (phoneNums.length == 0) {
            return false;
        }
        for (String phoneNum : phoneNums) {
            if (!compile.matcher(phoneNum).matches()) {
                return false;
            }
        }
        return true;
    }

    public static boolean validEmails(String... emailNums) {
        Pattern compile = Pattern.compile(Constants.EMAIL_PATTERN);
        if (emailNums.length == 0) {
            return true;
        }
        for (String email : emailNums) {
            if (!compile.matcher(email).matches()) {
                return false;
            }
        }
        return true;
    }
}
