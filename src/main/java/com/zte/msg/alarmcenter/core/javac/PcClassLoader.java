package com.zte.msg.alarmcenter.core.javac;

import com.zte.msg.alarmcenter.utils.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.zte.msg.alarmcenter.core.javac.CodeJavac.allBuffers;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/31 17:35
 */
@Slf4j
public class PcClassLoader extends ClassLoader {

    private final Hashtable<String, ClassInfo> classInfos = new Hashtable<>();

    @Override
    protected Class<?> findClass(String name) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (!classInfos.containsKey(name)) {
            byte[] bytes = allBuffers.get(name);
            Class<?> result = null;
            try {
                result = defineClass(name, bytes, 0, bytes.length);
                ClassInfo classInfo = new ClassInfo(System.currentTimeMillis());
                classInfo.getClassType().put(name, result);
                classInfos.put(name, classInfo);
            } catch (Exception e) {
                try {
                    return contextClassLoader.loadClass(name);
                } catch (ClassNotFoundException ignored) {
                }
            }
            return result;
        }
        ClassInfo loadedClassInfo = classInfos.get(name);
        return loadedClassInfo.getClassType().firstEntry().getValue();
    }

    protected void flushClazzInfo(List<ScriptName> names) {
        for (ScriptName scriptName : names) {
            String scriptTag = scriptName.getScriptTag();
            String className = scriptTag + Constants.UNDER_LINE + scriptName.getSuffix();
            ClassInfo classInfo = classInfos.get(scriptTag);
            if (Objects.isNull(classInfo)) {
                classInfo = new ClassInfo();
                classInfo.setClassType(new TreeMap<>());
            }
            Map<String, Class<?>> classType = classInfo.getClassType();
            if (!classType.isEmpty()) {
                classType.clear();
            }
            allBuffers.keySet().forEach(o -> {
                if (o.startsWith(className)) {
                    byte[] bytes = allBuffers.get(o);
                    Class<?> aClass = defineClass(o, bytes, 0, bytes.length);
                    classType.put(o, aClass);
                }
            });

            classInfo.setLastModified(System.currentTimeMillis());
            classInfos.put(scriptTag, classInfo);
        }
    }

    @Data
    public static class ScriptName {

        private String scriptTag;

        private String suffix;

        public ScriptName(String scriptTag, String suffix) {
            this.scriptTag = scriptTag;
            this.suffix = suffix;
        }
    }
}
