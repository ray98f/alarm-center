package com.zte.msg.alarmcenter.core.javac;

import lombok.extern.slf4j.Slf4j;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/31 17:37
 */
@Slf4j
public class ScriptFileManager implements JavaFileManager {
    private final StandardJavaFileManager fileManager;
    private final Map<String, ByteArrayOutputStream> buffers = new LinkedHashMap<>();

    public ScriptFileManager(StandardJavaFileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Iterable<Set<Location>> listLocationsForModules(final Location location) throws IOException {
        return invokeNamedMethodIfAvailable(location, "listLocationsForModules");
    }

    public String inferModuleName(final Location location) throws IOException {
        return invokeNamedMethodIfAvailable(location, "inferModuleName");
    }

    public StandardJavaFileManager getFileManager() {
        return fileManager;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return fileManager.getClassLoader(location);
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
        return fileManager.list(location, packageName, kinds, recurse);
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        return fileManager.inferBinaryName(location, file);
    }

    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return fileManager.isSameFile(a, b);
    }

    @Override
    public boolean handleOption(String current, Iterator<String> remaining) {
        return fileManager.handleOption(current, remaining);
    }

    @Override
    public boolean hasLocation(Location location) {
        return fileManager.hasLocation(location);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        if (location == StandardLocation.CLASS_OUTPUT && buffers.containsKey(className) && kind == JavaFileObject.Kind.CLASS) {
            final byte[] bytes = buffers.get(className).toByteArray();
            return new SimpleJavaFileObject(URI.create(className), kind) {

                @Override
                public InputStream openInputStream() {
                    return new ByteArrayInputStream(bytes);
                }
            };
        }
        return fileManager.getJavaFileForInput(location, className, kind);
    }


    @Override
    public JavaFileObject getJavaFileForOutput(Location location, final String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        return new SimpleJavaFileObject(URI.create(className), kind) {

            @Override
            public OutputStream openOutputStream() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                buffers.put(className, baos);
                return baos;
            }
        };
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return fileManager.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        return fileManager.getFileForOutput(location, packageName, relativeName, sibling);
    }

    @Override
    public void flush() {


    }

    @Override
    public void close() throws IOException {
        fileManager.close();
    }

    @Override
    public int isSupportedOption(String option) {
        return fileManager.isSupportedOption(option);
    }

    public void clearBuffers() {
        buffers.clear();
    }


    public Map<String, byte[]> getAllBuffers() {
        Map<String, byte[]> ret = new LinkedHashMap<>(buffers.size() * 2);
        for (Map.Entry<String, ByteArrayOutputStream> entry : buffers.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().toByteArray());
            log.info(entry.getKey());
        }
        return ret;
    }

    public void remove(String name) {
        buffers.keySet().removeIf(o -> o.startsWith(name));
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeNamedMethodIfAvailable(final Location location, final String name) {
        final Method[] methods = fileManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(name) && method.getParameterTypes().length == 1 &&
                    method.getParameterTypes()[0] == Location.class) {
                try {
                    return (T) method.invoke(fileManager, location);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new UnsupportedOperationException("Unable to invoke method " + name);
                }
            }
        }
        throw new UnsupportedOperationException("Unable to find method " + name);
    }
}
