package com.zte.msg.alarmcenter.core.javac;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/31 17:36
 */
public class JavaSourceFromString extends SimpleJavaFileObject {

    /**
     * The source code of this "file".
     */
    private final String code;

    /**
     * Constructs a new JavaSourceFromString.
     *
     * @param name the name of the compilation unit represented by this file object
     * @param code the source code for the compilation unit represented by this file object
     */
    public JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @SuppressWarnings("RefusedBequest")
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
