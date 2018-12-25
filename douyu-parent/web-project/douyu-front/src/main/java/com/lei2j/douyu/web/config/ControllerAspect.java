package com.lei2j.douyu.web.config;

import com.lei2j.douyu.web.response.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by leijinjun on 2018/12/24.
 */
@Order(1)
@Aspect
@Component
public class ControllerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(public * com.lei2j.douyu.web.controller.*.*(..))")
    public void common(){}

    @Around("common()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            Object object = proceedingJoinPoint.proceed(args);
            return object;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Response.INTERNAL_SERVER_ERROR;
    }
}
