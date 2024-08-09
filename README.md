# Reflect-Driven Natural Language Method Invoker
### 项目概述
该项目采用反射机制获取可调用函数的描述,将自然语言输入和函数描述输入大语言模型,获取函数调用的 JSON,再使用反射机制根据 JSON 调用函数,并返回函数调用结果。该项目参照 Spring Boot 的实现方法。

### 项目结构
com.LLMask: LLM 调用部分
Connector: 大语言模型调用部分,要求提供一个无参构造函数
LLMConnector: 大语言模型调用需要实现的接口
LLMRouter: 单个函数的包装
LLMUseContorler: 整个项目的总控部分
com.reflectTool: 反射机制部分
ClassScanner 和 PackageScanner: 获取可调用函数的方法
getDescFiled: 处理函数输入的自定义类的类结构的序列化方法
getTheWay: 指定扫描的类的注解
JsonRequirement: 描述函数输入参数的注解
LLMinvoke: 描述可调用函数的注解
org.example: 示例
具体使用细节在 org.example 中查阅注释,实现细节在 com 部分查阅代码。

### 函数调用 JSON 格式
内部包括method参数，其内容为函数名，表现调用的函数然后是若干个key为参数名，然后value为参数值的键值对

如

``
{  
"method": "addUser",  
"User": {  
"name": "张三",  
"age": 20,  
"gender": "男"  
},  
"pwdKey": 123456  
}  
``

其中 method 参数表示要调用的函数名,其余键值对表示函数的输入参数。

### 未来目标
1. 添加多轮对话和返回值作为下轮对话的必然输入
2. 添加单次对话的单次函数调用时允许调用多个函数且输出可以作为另外一个任务的函数输入
3. 提高ai任务的复杂性，如使用指定的函数，使得ai可以获取新的相关数据，即单次自然语言任务间，ai可以进行多轮函数调用并获取函数返回值
4. 提供更多输入方案，如语音、图片等
5. 提供更多语言版本，如js版
6. 修复bug
7. 提供更多的ai调用策略

### 目前状态
由于使用 OLLaMA 运行 LLaVA 6B 作为 LLM 部分的效果不佳,预期基本不会更新。