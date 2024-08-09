package org.example.t;

import com.reflectTool.LLMInvoke;
import com.reflectTool.JsonRequirement;
import org.example.*;
public class Test2 {
    @LLMInvoke(value = "验证账号密码，返回验证结果",title = "Login")//要求两个都要有，前者为描述，后者为函数调用使用的名字，可以和函数名不一样
    public boolean test1(@JsonRequirement( requiredType = "要求为必须大于0小于1000",name="第一个传入值") String a, String b) throws Exception {
        //该注解无具体使用要求，requireType为对传入参数的要求的描述，自然语言即可.name为调用时比实际参数名优先使用，便于ai判断，可以使用自然语言，都是
        //推荐使用name
        System.out.println(a);
        System.out.println(b);
        return a.equals(b);
    }
    @LLMInvoke(value = "计算两整数之和",title = "add")
    public int test2(@JsonRequirement( requiredType = "要求为必须大于0小于1000",name="第一个传入值") int a,int b) throws Exception {
        return a+b;
    }
    @LLMInvoke(value = "根据传入的card获得最新记录",title = "Test4")
    public int test5(@JsonRequirement(name="value") card e){
        System.out.println(e.entity.getName());
        return e.id*e.num;
    }
}