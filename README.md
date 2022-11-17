# 自己动手写一个 java 热加载插件

# 环境

- jdk 1.8
- maven 3.6.3 +

## 说明

- agent: jvm agent，实现动态字节码更新 
- attach: 通过 VirtualMachine api 加载 agent
- app: 测试项目


## 演示

```bash
# 打包项目
bash build.sh

# 启动示例项目
java -jar .dist/app.jar

# 另起一个终端，替换 class
javac reload/Hello.java
cd .dist
java -Xbootclasspath/a:${JAVA_HOME}/lib/tools.jar -jar attach.jar reload/Hello.class
```












