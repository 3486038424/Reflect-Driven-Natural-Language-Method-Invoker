package com.reflectTool;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LLMInvoke {
    String value();//函数描述
    String title();//函数名称或另称
}//要求两个都要有，前者为描述，后者为函数调用使用的名字，可以和函数名不一样