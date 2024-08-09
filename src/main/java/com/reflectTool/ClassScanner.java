package com.reflectTool;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {//扫描类方法
    private List<Class<?>> classes;

    public ClassScanner(String packageName) throws Exception {
        this.classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageUrl = classLoader.getResource(packagePath);
        if (packageUrl != null) {
            File packageDirectory = new File(packageUrl.getFile());
            if (packageDirectory.exists() && packageDirectory.isDirectory()) {
                scanClasses(packageDirectory, packageName);
            }
        }
    }

    public List<Class<?>> getClasses() {
        return classes;
    }

    private void scanClasses(File directory, String packageName) throws ClassNotFoundException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                scanClasses(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
    }
}