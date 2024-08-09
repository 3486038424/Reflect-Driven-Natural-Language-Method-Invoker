package org.example;

import com.LLMask.Connector.GLM;
import com.LLMask.LLMUseContorller;
import com.reflectTool.getTheWay;

import java.util.Scanner;


/*
此案例中，
Test2为可调用函数的类
card和cardOwner为测试getDescFiled方法的自定义参数类型
Main为主函数

由于现阶段的代码依旧处于demo期，所以部分代码转移到main函数中以便调整调用

提供若干个该示例的输入方式和结果
{"method":"Login","第一个传入值":"张三","arg1":"张三"}
判断两个字符串相等与否

{"method":"add","第一个传入值":15,"arg1":30}
计算两个整数之和

{"method":"Test4","value":{"id":312,"num":20,"entity":{"name":"well"}}}
输出name id和num的乘积

 */
@getTheWay(way="org.example")//在指定路径下扫描可调用函数
public class Main {
    public static void main(String[] args) {

        LLMUseContorller llmUseContorller=LLMUseContorller.run((Class)Main.class,(Class)GLM.class, args);//初始化控制器
        System.out.println(llmUseContorller.promat());//验证关于任务和可调用函数的描述的正确性
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                //Object obj = llmUseContorller.exec(scanner.nextLine());//将输入作为任务描述进行运行
                Object obj = llmUseContorller.execInvoke(scanner.nextLine());//将输入作为JSON调用方法进行运行，验证函数调用的正确性
                //Object obj = llmUseContorller.execLLM(scanner.nextLine());//将输入作为任务描述进行运行，验证LLM返回值的正确性
                if (obj != null)
                    System.out.println(obj.toString());//输出结果
                else
                    System.out.println("null");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}