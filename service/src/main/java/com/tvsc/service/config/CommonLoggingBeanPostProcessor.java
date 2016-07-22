package com.tvsc.service.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * BPP for trivial logging (entering end returning from method, logging last stage of CompletableFuture)
 *
 * @author Taras Zubrei
 */
@Log4j2
public class CommonLoggingBeanPostProcessor implements BeanPostProcessor {
    private Class<?> current;
    private List<String> excludeWithAnnotationName = Arrays.asList("ControllerAdvice", "RestController");

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().getName().startsWith("com.tvsc") && !beanName.startsWith("com.tvsc")
                && Arrays.stream(bean.getClass().getAnnotations())
                .map(annotation -> annotation.annotationType().getSimpleName())
                .noneMatch(excludeWithAnnotationName::contains))
            current = bean.getClass();
        else
            current = null;
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> javaClass = current;
        if (javaClass == null)
            return bean;
        return Proxy.newProxyInstance(javaClass.getClassLoader(), javaClass.getInterfaces(), (proxy, method, args) -> {
            log.debug(String.format("Entering:\t\t\t%s.%s", javaClass.getName(), method.getName()));
            Object result = method.invoke(bean, args);
            log.debug(String.format("Returning from:\t%s.%s", javaClass.getName(), method.getName()));
            if (result instanceof CompletableFuture) {
                CompletableFuture<?> future = (CompletableFuture<?>) result;
                return future.thenApply(res -> {
                    log.debug(String.format("In CompletableFuture of %s from method %s.%s",
                            Optional.ofNullable(res).map(Object::getClass).map(Class::getSimpleName).orElse("null"),
                            javaClass.getName(),
                            method.getName()));
                    return res;
                });
            }
            return result;
        });
    }
}
