package com.zte.msg.alarmcenter.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/4 13:54
 */
public class JavaCodecUtils {


    public static final String JAR = ".jar";

//    public static String getJarFiles(String jarPath) {
//        File sourceFile = new File(jarPath);
//        AtomicReference<String> jars = new AtomicReference<>("");
//        if (sourceFile.exists()) {
//            if (sourceFile.isDirectory()) {
//
//                File[] files = sourceFile.listFiles(pathname -> {
//                    if (pathname.isDirectory()) {
//                        return true;
//                    } else {
//                        String name = pathname.getName();
//                        if (name.endsWith(JAR)) {
//                            jars.set(jars + pathname.getPath() + ";");
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//            }
//        }
//        return jars.get();
//    }

    public static String replaceCodeJavaName(String code, String replacement) {
        if (StringUtils.isBlank(code)){
            return null;
        }
        int start = code.indexOf("public class");
        String substring = StringUtils.substring(code, start, StringUtils.ordinalIndexOf(code, "{", 1));
        return code.replaceAll(StringUtils.substring(substring,
                StringUtils.ordinalIndexOf(substring, " ", 2) + 1,
                StringUtils.ordinalIndexOf(substring, " ", 3)), replacement);
    }


}
