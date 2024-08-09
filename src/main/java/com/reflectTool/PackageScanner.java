package com.reflectTool;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {
    private static final PackageScanner instance = new PackageScanner();

    private List<Method> annotatedMethods;

    private PackageScanner() {
        this.annotatedMethods = new ArrayList<>();
    }

    public static PackageScanner getInstance() {
        return instance;
    }

    public void scanPackage(String packageName) throws Exception {
        // 扫描指定包下的所有类及方法
        ClassScanner classScanner = new ClassScanner(packageName);
        List<Class<?>> classes = classScanner.getClasses();
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(LLMInvoke.class)) {
                    annotatedMethods.add(method);
                }
            }
        }
    }

    public List<Method> getAnnotatedMethods() {
        return annotatedMethods;
    }
}