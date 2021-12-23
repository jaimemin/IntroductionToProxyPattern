package com.tistory.jaimemin.proxy.config.v6_aop.aspect;

import com.tistory.jaimemin.proxy.trace.TraceStatus;
import com.tistory.jaimemin.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* com.tistory.jaimemin.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();
//            Method method = invocation.getMethod();
//            String message = method.getDeclaringClass().getSimpleName()
//                    + "."
//                    + method.getName() + "()";
            status = logTrace.begin(message);

            // 로직 호출
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);

            throw e;
        }
    }
}