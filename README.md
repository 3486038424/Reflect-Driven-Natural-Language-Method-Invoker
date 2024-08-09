##该项目是参照spring boot的实现方法，采取反射机制获取可调用函数的描述，将自然语言输入和函数描述输入大语言模型获取函数调用的json，再使用反射机制根据json调用函数，并且返回函数调用结果。
#项目结构：
com.LLMask为LLM调用部分，其中Connector为大语言模型调用部分，要求提供一个没有参数的构造函数，LLMConnector作为是大语言模型调用需要实现的接口。LLMRouter是单个函数的包装，LLMUseContorler是整个项目的总控部分
com.reflectTool为反射机制部分，其中ClassScanner和PackageScanner共同提供了获取可调用函数的方法。getDescFiled是处理函数输入的自定义类的类结构的序列化方法。getTheWay是指定扫描的类的注解。JsonRequirement是描述函数输入的参数的注解。LLMinvoke是描述可调用函数的注解。
Org.example是一个示例。

具体使用细节在org.example中翻阅注释
实现细节翻阅com部分的代码

##函数调用的json要求如下
内部包括method参数，其内容为函数名，表现调用的函数然后是若干个key为参数名，然后value为参数值的键值对如

{    
"method":"addUser",    
"User":{        
"name":"张三",       
"age":20,        	
"gender":"男"    
},
"pwdKey":123456
}


##此项目为个人闲余所为
##未来目标：
1.添加多轮对话和返回值作为下轮对话的必然输入
2.添加单次对话的单次函数调用时允许调用多个函数且输出可以作为另外一个任务的函数输入
3.提高ai任务的复杂性，如使用指定的函数，使得ai可以获取新的相关数据，即单次自然语言任务间，ai可以进行多轮函数调用并获取函数返回值
4.提供更多输入方案，如语音、图片等
5.提供更多语言版本，如js版
6.修复bug
7.提供更多的ai调用策略

以上皆是画饼。
由于使用ollama运行llava 6b作为llm部分的效果不算很好，预期基本不会更新。