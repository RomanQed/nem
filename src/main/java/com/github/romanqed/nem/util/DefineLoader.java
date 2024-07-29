package com.github.romanqed.nem.util;

public interface DefineLoader {

    Class<?> define(String name, byte[] buffer);

    Class<?> load(String name);

    ClassLoader getClassLoader();
}
