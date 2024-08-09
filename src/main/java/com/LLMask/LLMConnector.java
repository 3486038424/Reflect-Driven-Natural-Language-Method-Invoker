package com.LLMask;

public interface LLMConnector {
    public String getExec(String cmd);//该方法为传入命令，由LLM处理返回json调用格式语言
}
