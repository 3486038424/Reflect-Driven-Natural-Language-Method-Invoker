package com.reflectTool;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class getDescFiled  {
    public static List<FieldInfo> getFieldInfo(Class<?> clazz) {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            FieldInfo fieldInfo = new FieldInfo(field.getName(), getFieldType(field.getType(), field));
            fieldInfoList.add(fieldInfo);
        }
        return fieldInfoList;
    }

    private static FieldType getFieldType(Class<?> type, Field field) {
        FieldType fieldType = new FieldType();
        fieldType.setType(type.getSimpleName());
        if (type.isPrimitive() || type == String.class) {
            return fieldType;
        }
        if (type.isArray()) {
            fieldType.setIsArray(true);
            fieldType.setComponentType(getFieldType(type.getComponentType(), field));
        } else if (List.class.isAssignableFrom(type)) {
            fieldType.setIsList(true);
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    fieldType.setComponentType(getFieldType((Class<?>) actualTypeArguments[0], field));
                }
            }
        } else {
            fieldType.setIsClass(true);
            fieldType.setFieldInfoList(getFieldInfo(type));
        }
        return fieldType;
    }

}

class FieldInfo {
    private String name;
    private FieldType fieldType;

    public FieldInfo(String name, FieldType fieldType) {
        this.name = name;
        this.fieldType = fieldType;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "name='" + name + '\'' +
                ", fieldType=" + fieldType +
                '}';
    }
}

class FieldType {
    private String type;
    private boolean isArray;
    private boolean isList;
    private boolean isClass;
    private FieldType componentType;
    private List<FieldInfo> fieldInfoList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public boolean isList() {
        return isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    public boolean isClass() {
        return isClass;
    }

    public void setIsClass(boolean isClass) {
        this.isClass = isClass;
    }

    public FieldType getComponentType() {
        return componentType;
    }

    public void setComponentType(FieldType componentType) {

        this.componentType = componentType;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<FieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }

    @Override
    public String toString() {
        return "FieldType{" +
                "type='" + type + '\'' +
                ", isArray=" + isArray +
                ", isList=" + isList +
                ", isClass=" + isClass +
                ", componentType=" + componentType +
                ", fieldInfoList=" + fieldInfoList +
                '}';
    }
}