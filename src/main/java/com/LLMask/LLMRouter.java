package com.LLMask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reflectTool.JsonRequirement;
import com.reflectTool.LLMInvoke;
import com.reflectTool.getDescFiled;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LLMRouter {
    public String exegesis;//包装的函数的描述
    public Method method;//包装的函数对象
    public String className;//包装的函数所属的类名
    Parameter[] parameters ;//需要的输入参数
    public Object exec(Object obj,JsonObject jsonObject) throws Exception{
        //该函数负责处理LLM返回的JSON，并且调用包装的函数，obj为包装的函数的类，由LLMUseContorller唯一持有。
        List<Object>  args = new ArrayList<>();
        String name;
        for(Parameter parameter : parameters) {
            JsonRequirement jsonRequirement = parameter.getAnnotation(JsonRequirement.class);
            if(jsonRequirement!=null)name=jsonRequirement.name();
            else name=parameter.getName();
            args.add(new Gson().fromJson(jsonObject.get(name),parameter.getType()));
        }
        return method.invoke(obj,args.toArray());
    }
    public Map<String,Object> getDesc(){//返回函数的描述
        List<Map<String,Object>> obj = new ArrayList<>();
        for(Parameter parameter : parameters) {
            Map<String,Object> obj1 = new HashMap<>();
            JsonRequirement jsonRequirement = parameter.getAnnotation(JsonRequirement.class);
            if(jsonRequirement!=null) {
                if(jsonRequirement.name()!="")
                    obj1.put("参数名",jsonRequirement.name());
                else
                    obj1.put("参数名",parameter.getName());

                if(jsonRequirement.requiredType()!="")
                    obj1.put("参数要求",jsonRequirement.requiredType());

                if (parameter.getType().isPrimitive() || parameter.getType() == String.class){
                    obj1.put("参数类型",parameter.getType().toString());
                }
                else{
                    obj1.put("参数类型", getDescFiled.getFieldInfo(parameter.getType()));
                }
                obj.add(obj1);
            }
            else {
                obj1.put("参数名",parameter.getName());
                if (parameter.getType().isPrimitive() || parameter.getType() == String.class){
                    obj1.put("参数类型",parameter.getType().toString());
                }
                else{
                    obj1.put("参数类型", getDescFiled.getFieldInfo(parameter.getType()));
                }
                obj.add(obj1);
            }
        }
        Map<String,Object> desc = new HashMap<>();
        desc.put("方法名",method.getAnnotation(LLMInvoke.class).title());
        desc.put("方法描述",exegesis);
        desc.put("参数列表",obj);
        return desc;
    }
    public LLMRouter(){}
    public LLMRouter(Method method){
        exegesis=method.getAnnotation(LLMInvoke.class).value();
        this.method=method;
        className=method.getDeclaringClass().getName();
        parameters = method.getParameters();
    }
}
