package com.github.romanqed.nem.util;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class WrapClassLoader implements DefineLoader {
    private static final Method DEFINE_METHOD = findDefineMethod();

    private final ClassLoader body;
    private final Method define;

    WrapClassLoader(ClassLoader body, Method define) {
        this.body = body;
        this.define = define;
    }

    private static Method findDefineMethod() {
        Class<ClassLoader> clazz = ClassLoader.class;
        try {
            Method method = clazz.getDeclaredMethod("defineClass",
                    String.class,
                    byte[].class,
                    int.class,
                    int.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Can't find defineClass method from ClassLoader");
        }
    }

    public static DefineLoader of(ClassLoader loader) {
        return new WrapClassLoader(loader, DEFINE_METHOD);
    }

    @Override
    public Class<?> define(String name, byte[] buffer) {
        return (Class<?>) AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                return define.invoke(body, name, buffer, 0, buffer.length);
            } catch (Throwable e) {
                throw new IllegalStateException("Can't define class due to", e);
            }
        });
    }

    @Override
    public Class<?> load(String name) {
        try {
            return body.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return body;
    }
}
