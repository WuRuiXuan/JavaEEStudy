##### Spring基本概念

内核：IOC（反转控制）、AOP（面向切面编程）

展现层：SpringMVC

持久层：JDBCTemplate

##### IOC

加载配置文件，解析成 BeanDefinition 放在 Map 里，调用 getBean 的时候，从 BeanDefinition 所属的 Map 里，取出 Class 对象通过反射进行实例化，同时，如果有依赖关系，将递归调用 getBean 方法完成依赖注入

##### Bean标签的配置

id: Bean 的唯一标识

class: Bean 的全限定名

scope：Bean 的作用范围

| 取值范围       | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| singleton      | 默认值，单例的                                               |
| prototype      | 多例的                                                       |
| request        | Web 项目中，Spring 创建一个 Bean 对象，将对象存入到 request 域中 |
| session        | Web 项目中，Spring 创建一个 Bean 对象，将对象存入到 session 域中 |
| global session | Web 项目中，应用在 Portlet 环境，如果没有 Portlet 环境，那么 globalSession 相当于 session |

init-method：Bean 的初始化方法（创建后调用）

destroy-method：Bean 的销毁方法

##### Bean的生命周期

scope="singleton"

- 实例化个数：一个
- 实例化时机：当 Spring 核心文件被加载时，实例化 Bean

- Bean 创建：当应用加载，创建容器时，对象就被创建了
- Bean 运行：只要容器在，对象一直活着
- Bean 销毁：当应用卸载，销毁容器时，对象就被销毁了

scope="prototype"

- 实例化个数：多个
- 实例化时机：当调用 getBean() 方法时，实例化 Bean

- Bean 创建：当使用对象时，创建新的对象实例
- Bean 运行：只要对象在使用中，就一直活着
- Bean 销毁：当对象长时间不用时，就被 Java 的垃圾回收器回收了

##### Bean实例化的三种方式

无参构造

```xml
<bean id="userDao" class="com.xxx.dao.impl.UserDaoImpl"/>
```

静态代理

```xml
<bean id="userDao" class="com.xxx.factory.StaticFactory" factory-method="getUserDao"/>
```

动态代理

```xml
<bean id="factory" class="com.xxx.factory.DynamicFactory"/>
<bean id="userDao" factory-bean="factory" factory-method="getUserDao"/>
```

##### Bean的依赖注入方式

set 注入（需要 p 命名空间）

```xml
<bean id="userService" class="com.xxx.service.impl.UserServiceImpl" p:userDao-ref="userDao"/>
```

构造方法注入

```java
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

}
```

```xml
<bean id="userService" class="com.xxx.service.impl.UserServiceImpl">
    <constructor-arg name="userDao" ref="userDao"/>
</bean>
```

##### Spring注解-加载配置文件

加载配置文件

```java
@PropertySource("classpath:jdbc.properties")
```

注入配置项

```java
@Value("${jdbc.driver}")
private String driver;

@Value("${jdbc.url}")
private String url;

@Value("${jdbc.username}")
private String username;

@Value("${jdbc.password}")
private String password;
```

标注该类是 Spring 核心配置类

```java
@Configuration
```

核心配置类加载其它配置类

```java
@Import({[class]})
```

##### Spring注解-Bean对象

| 注解                                                         | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| @ComponentScan("[package]")                                  | 扫描包下的 Bean 对象（一般标注在 Spring 核心配置类上，需要先扫描才能实现实例化注入） |
| @Bean("[bean_id]")                                           | 实例化一个 Bean 对象，标注在方法上（返回一个实例化了的对象） |
| @Component("[bean_id]")<br />@Controller("[bean_id]")<br />@Service("[bean_id]")<br />@Repository("[bean_id]") | 实例化一个 Bean 对象，标注在类上，四种注解作用完全相同，只是语义不同：<br />Component - 普通类<br />Controller - web层<br />Service - service层<br />Repository - dao层 |
| @Scope("[scope_value]")                                      | 标注 Bean 对象的作用范围                                     |
| @PostConstruct("[init_method_name]")                         | 标注 Bean 对象的初始化方法                                   |
| @PreDestory("[destory_method_name]")                         | 标注 Bean 对象的销毁方法                                     |
| @Autowired                                                   | 注入 Bean 对象                                               |
| @Value                                                       | 注入普通属性                                                 |
| @Qualifier("[bean_id]")<br />@Autowired                      | 按照 id 注入 Bean 对象，要一起使用                           |
| @Resource(name = "[bean_id]")                                | 按照 id 注入 Bean 对象，等同于 @Qualifier + @Autowired       |

##### 动态代理

反射：将 java 源码编译成字节码文件加载进内存，成为 Class 类对象，这时可以获取类的各个组成部分，比如成员变量、构造方法、成员方法等

概念：利用反射机制，在 Class 类对象阶段直接操作类

优点：在程序运行期间，在不修改源码的情况下，对目标方法进行相应的增强

##### 常用动态代理技术

| 名称       | 说明                                                         | 实现     |
| ---------- | ------------------------------------------------------------ | -------- |
| JDK 代理   | 目标对象基于接口动态生成代理对象，目标对象与代理对象之间是兄弟关系 | Proxy    |
| cglib 代理 | 为目标对象动态地生成子对象（代理对象），目标对象与代理对象之间是父子关系 | Enhancer |

##### AOP

核心概念：

- Target：目标对象
- Proxy：代理对象
- Joinpoint：连接点（可以被增强的方法）
- Pointcut：切入点（实际被增强的方法）
- Advice：通知/增强（如何去增强的逻辑方法）
- Aspect：切面（切入点和通知的结合）
- Weaving：织入（把切入点和通知编织成切面）

底层使用哪种动态代理技术：

- 有接口时，使用 JDK 代理技术
- 没有接口时，使用 cglib 代理技术

