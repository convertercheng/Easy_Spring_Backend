# 前海爱翼api后台2.0.1版
创建于2018-1-16 15:45

## 1 启动项目

1. 直接在IDE中运行ApiApplication

2. mvn spring-boot:run

3. mvn install -> target dir: java -jar {jar包}

4. java -jar {jar包} --spring.profiles.active=prod\

5. mvn clean package 打包时会自动执行单元测试

6. mvn clean package -D maven.test.skip=true 打包时跳过单元测试

## 2 规范说明

### 类文件头需要加如下注释
```Java
/**
 * @author ${author} ${email}
 * @version 2.0.1 创建时间: ${DATE} ${TIME}
 * 
 * 类说明：
 *       ${description}
 */
```
### 返回JSON格式使用@RestController注解
```Java
@RestController
public class ControllerClass{
    // body
}
```
### 采用组合注解替换@RequsMapping + Method
```
@PostMapping  @PutMapping  @GetMapping  @DeleteMapping
```
### 每个接口需要书写单元测试用例，写在相应目录下

### 使用jasypt对配置文件中的敏感字段加密

### 注意使用@Transactional完成事务性操作

### 使用@Valid进行表单验证

### 自定义异常并通过handle捕获

### 命名规范
1. 常量命名全部大写
2. 变量必须加访问修饰符，限制访问权限

### 项目结构（待扩展）

## 3 更新

### 2018-01-16 20:15
1. 创建项目，形成雏形

### 2018-01-17 10:03
1. 添加README.md中的规范说明

### 2018-01-18 15:19
1. 添加单元测试用例

### 2018-02-08 22:49
1. 添加日志管理说明
2. 定义日志打印结构

## 4 Log管理

### 1.使用工具
- 统一使用lombok框架，使用注解的方式打印日志。在common-utils的pom.xml文件中添加如下依赖
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.16.18</version>
</dependency>
```
- 在IDEA中Plugins中安装Lombok Plugin插件

- 打印日志时，直接在类上使用`@Slf4j`注解，在具体的类中直接使用
```java
@RestController
@RequestMapping("/plate")
@Slf4j
public class PlateWeb {

    @Autowired
    PlateService plateService;

    @GetMapping("/list")
    public List<Plate> list() {
        log.info("test");
        return plateService.list();
    }
}
```

### 2. 日志结构和规则

分为以下四类，必须满足：
- 业务日志：用户的操作日志，用来监控业务逻辑的执行情况
- 错误日志：系统错误信息
- 摘要日志：系统操作上下文的摘要信息
- 统计日志：可以汇总的统计信息

## 5 app后台在接口安全性上的一些做法

### 流程：

1. 客户端登录后获取Token(后台生成的uuid)，同时将Token-userId以键值对方式存入Redis中，暂时存活时间不限，退出app则删除该token
2. 客户端在发送请求时，必须携带参数token，signature和timestamp，signature的生成方式为md5(接口名 + timestamp + token)，提交到服务器
3. 服务器写一个过滤器，先验证时间戳，如果时间戳与服务器超过正负2分钟，则拒绝访问；2. 服务器端同样生成signature，对比签名，一致则通过服务器验证



