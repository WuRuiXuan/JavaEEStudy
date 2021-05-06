package com.itheima.anno;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component("myAspect")
@Aspect // 标注切面类
public class MyAspect {

    @Before("pointcut()") // 配置前置通知
    public void before() {
        System.out.println("前置增强...");
    }

    @AfterReturning("MyAspect.pointcut()")
    public void afterReturning() {
        System.out.println("后置增强...");
    }

    // Proceeding JoinPoint: 正在执行的连接点===切点
    @Around("execution(* com.itheima.anno.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("环绕前增强...");
        Object proceed = pjp.proceed();// 切点方法
        System.out.println("环绕后增强...");
        return proceed;
    }

    @AfterThrowing("execution(* com.itheima.anno.*.*(..))")
    public void afterThrowing() {
        System.out.println("异常抛出增强...");
    }

    @After("execution(* com.itheima.anno.*.*(..))")
    public void after() {
        System.out.println("最终增强...");
    }

    // 定义切点表达式
    @Pointcut("execution(* com.itheima.anno.*.*(..))")
    public void pointcut() {}
}
