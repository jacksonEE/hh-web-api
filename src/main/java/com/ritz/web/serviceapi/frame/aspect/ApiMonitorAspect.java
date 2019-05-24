package com.ritz.web.serviceapi.frame.aspect;

import com.ritz.web.serviceapi.frame.annotation.Api;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
@Slf4j
public class ApiMonitorAspect {

    @Pointcut("execution(public * com.zj.match.score.api..*.handle(..))")
    public void apiMonitorPointcut() {
    }

    @Value("${api.time-out:10}")
    private Integer timeOut;

    @Around(value = "apiMonitorPointcut()")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        Class declaringType = jp.getSignature().getDeclaringType();
        Api api = (Api) declaringType.getDeclaredAnnotation(Api.class);
        long start = System.currentTimeMillis();
        Throwable[] err = new Throwable[1];
        Object o = CompletableFuture.supplyAsync(() -> {
            try {
                return jp.proceed();
            } catch (Throwable throwable) {
                return throwable;
            }
        }).whenCompleteAsync((r, e) -> {
            if (r != null) {
                err[0] = (Throwable) r;
                log.error(((Throwable) r).getMessage());
            }
            log.info("`{}`执行完成,耗时{}ms", api.value(),
                    System.currentTimeMillis() - start);
        }).get(timeOut, TimeUnit.SECONDS);
        if (err[0] != null) {
            throw err[0];
        }
        return o;
    }
}
