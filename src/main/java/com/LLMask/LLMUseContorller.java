package com.LLMask;

import com.google.gson.*;
import com.reflectTool.LLMInvoke;
import com.reflectTool.PackageScanner;
import com.reflectTool.getTheWay;

import java.lang.reflect.Method;
import java.util.*;

public class LLMUseContorller {
    public LLMConnector connector;//LLM连接的实体类
    Map<String,LLMRouter> methods;//记录的所有可调用函数方法
    Map<String,Object> beans;//记录所有可调用函数方法需要的唯一类，在调用函数方法时传入
    List<Map<String,Object>> descriptors;//函数的描述记录
    LLMUseContorller(LLMConnector connector){
        this.connector = connector;
        methods = new HashMap<>();
        beans = new HashMap<>();
        descriptors =new ArrayList<>();
    }
    public void addMethod(LLMRouter obj){//往methods中添加函数，descriptors中添加描述
        methods.put(obj.method.getAnnotation(LLMInvoke.class).title(),obj);
        descriptors.add(obj.getDesc());
    }
    public String getDesc(){//获取对可调用函数的描述
        String jsonObject = new Gson().toJson(descriptors);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String formatStr = gson.toJson(jsonObject);
        return  toPrettyFormat(jsonObject);
    }
    public String beginOfDesc(){
        return "接下来的内容是你作为分析器可以调用的所有函数以及对其的描述：";
    }//可调用函数的描述段的起始描述
    public String getRequire(){//对任务的描述
        return "要求以json进行输出，内部包括method参数，其内容为函数名，表现调用的函数\n" +
                "然后是若干个key为参数名，然后value为参数值的键值对\n" +
                "如\n" +
                "{\n" +
                "    \"method\":\"addUser\",\n" +
                "    \"User\":{\n" +
                "        \"name\":\"张三\",\n" +
                "        \"age\":20,\n" +
                "        \"gender\":\"男\"\n" +
                "    },\n" +
                "    \"pwdKey\":123456\n" +
                "}\n";
    }
    public String beginOfPromat(){//对任务的起始描述
        return "接下来你需要作为一个自然语言分析器，对用户输入进行分析，然后仅输出一个json格式，用来表示用户输入所表示期望调用的功能。\n" +
                "如下是对你的输出的描述:";
    }
    public String promat(){//根据顺序组合出对任务和对可调用函数的描述 作为初始提示词
        return beginOfPromat()+getRequire()+beginOfDesc()+getDesc()+"接下来开始任务：\n";
    }
    public static String toPrettyFormat(String json) {//确保json格式正确性
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonObject = jsonParser.parse(json).getAsJsonArray();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }
    public void addBean(Method method){//将唯一可调用类加入beans
        if(!beans.containsKey(method.getDeclaringClass().getName())){
            try {
                beans.put(
                        method.getDeclaringClass().getName(),
                        method.getDeclaringClass().newInstance()
                );
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public Object exec(String cmd) throws Exception{//处理传入的自然语言描述，并且根据LLM返回调用函数
        cmd=connector.getExec(promat()+cmd);
        JsonObject jsonObject = new JsonParser().parse(cmd).getAsJsonObject();
        try {
            cmd = jsonObject.get("method").getAsString();
            LLMRouter l = methods.get(cmd);
            return l.exec(beans.get(l.className), jsonObject);
        }
        catch (Exception e){
            System.out.println(e.toString());
            return e;
        }
    }
    public Object execInvoke(String cmd) throws Exception{//默认传入的即为json格式的调用方法，测试根据json调用函数的功能

        JsonObject jsonObject = new JsonParser().parse(cmd).getAsJsonObject();
        try {
            cmd = jsonObject.get("method").getAsString();
            LLMRouter l = methods.get(cmd);
            return l.exec(beans.get(l.className), jsonObject);
        }
        catch (Exception e){
            e.printStackTrace();
            return e;
        }
    }
    public Object execLLM(String cmd) throws Exception{//默认传入的为命令，返回llm处理结果，测试llm的返回值
        cmd=connector.getExec(promat()+cmd);
        return cmd;
    }
    public static LLMUseContorller run(Class<Object> mainClass,Class<Object> LLMClass, String[] args) {//根据预设值，初始化LLMUseContorller，获取可调用函数和beans等
        try {
            LLMUseContorller connector = new LLMUseContorller((LLMConnector) LLMClass.newInstance());
            PackageScanner packageScanner = PackageScanner.getInstance();
            getTheWay t=mainClass.getAnnotation(getTheWay.class);
            packageScanner.scanPackage(t.way()); // 指定需要扫描的包名
            List<Method> annotatedMethods = packageScanner.getAnnotatedMethods();
            for (Method method : annotatedMethods) {
                connector.addBean(method);
                connector.addMethod(new LLMRouter(method));
            }
            return connector;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
