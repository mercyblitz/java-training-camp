/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.biz.web.bean;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.web.bytebuddy.LoggingBeanInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 基于 ByteBuddy 实现 Bean 日志输出
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
@Component
public class ByteBuddyLoggingBeanPostProcessor implements BeanFactoryPostProcessor,
        SmartInstantiationAwareBeanPostProcessor,
        MergedBeanDefinitionPostProcessor,
        BeanClassLoaderAware {

    private ClassLoader classLoader;

    private List<Class<?>> interceptedClasses = Arrays.asList(UserRegistrationService.class);

    private boolean isInterceptedClass(Class<?> beanClass) {
        for (Class<?> interceptedClass : interceptedClasses) {
            if (interceptedClass.isAssignableFrom(beanClass)) {
                return true;
            }
        }
        return false;
    }

    // 尝试四：可实现 Bean 替换
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 找到类型为 UserRegistrationService 的 BeanDefinition 集合
        // beanFactory.getBeanNamesForType(UserRegistrationService.class, true, false);
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            for (Class<?> interceptedClass : interceptedClasses) {
                if (beanFactory.isTypeMatch(beanName, interceptedClass)) {
                    BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                    if (beanDefinition instanceof AbstractBeanDefinition) {
                        AbstractBeanDefinition definition = (AbstractBeanDefinition) beanDefinition;
                        String beanClassName = definition.getBeanClassName();
                        Class<?> beanClass = beanClassName == null ? definition.getBeanClass() :
                                ClassUtils.resolveClassName(beanClassName, classLoader);
                        if (beanClass != null && isInterceptedClass(beanClass)) {
                            Class<?> proxyClass = replaceBeanTypeUsingProxyClass(definition,
                                    beanClass,
                                    beanName);
                            // classLoader 是 ByteBuddy ClassLoader, 其 parent 为 this.classLoader
                            // this.classLoader 是 Spring Application ClassLoader
                            ClassLoader classLoader = proxyClass.getClassLoader();
                            // 设置 ByteBuddy ClassLoader 到 BeanFactory
                            beanFactory.setBeanClassLoader(classLoader);
                            // 重置 Spring ClassLoader : Application ClassLoader ->  ByteBuddy ClassLoader
                            this.classLoader = classLoader;
                        }
                    }
                }
            }
        }
    }

    // 尝试二：无法实现 Bean 替换
    @Deprecated
    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
//        if (UserRegistrationService.class.isAssignableFrom(beanClass)) {
//            return newProxyClass(beanClass, beanName);
//        }
        return SmartInstantiationAwareBeanPostProcessor.super.predictBeanType(beanClass, beanName);
    }

    // 尝试三：无法实现 Bean 替换
    @Deprecated
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        // 无法实现 Bean 替换
//        if (UserRegistrationService.class.isAssignableFrom(beanClass)) {
//            try {
//                return newProxyClass(beanClass, beanName).newInstance();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    // 尝试一：无法实现 Bean 替换
    @Deprecated
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {

    }

    @Deprecated
    private Class<?> replaceBeanTypeUsingProxyClass(AbstractBeanDefinition beanDefinition, String beanName) {
        String beanClassName = beanDefinition.getBeanClassName();
        Class<?> beanClass = ClassUtils.resolveClassName(beanClassName, classLoader);
        return replaceBeanTypeUsingProxyClass(beanDefinition, beanClass, beanName);
    }

    private Class<?> replaceBeanTypeUsingProxyClass(AbstractBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        Class<?> proxyClass = newProxyClass(beanType, beanName);
        beanDefinition.setBeanClass(proxyClass);
        // beanDefinition.setBeanClassName(proxyClass.getName());
        return proxyClass;
    }

    private Class<?> newProxyClass(Class<?> beanType, String beanName) {
        Class<?> dynamicProxyClass = null;
        try {
            dynamicProxyClass = new ByteBuddy()
                    .subclass(beanType)
                    .method(named("toString"))
                    .intercept(FixedValue.value("UserRegistrationService"))
                    .make()
                    .load(classLoader)
                    .getLoaded();
        } catch (Exception e) {
            throw new BeanCreationException("Bean[name : " + beanName + "] creation is failed", e);
        }
        return dynamicProxyClass;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // bean 有可能被 Spring AOP 字节提升
        // 还得排除其他字节码提升
        Class<?> beanClass = AopUtils.getTargetClass(bean);
        // UserRegistrationService 子类均可以拦截
        // 老实现
//        if (UserRegistrationService.class.isAssignableFrom(beanClass)) {
//            return newProxy(bean, beanName, (Class<UserRegistrationService>) beanClass);
//        }

        if (isInterceptedClass(beanClass)) {
            return newProxy(bean, beanName, beanClass);
        }

        return bean;
    }

    private Object newProxy(Object bean, String beanName, Class<?> beanClass) {
        Object dynamicProxy = null;
        try {
            dynamicProxy = new ByteBuddy()
                    .subclass(beanClass)
                    .method(named("registerUser"))
                    .intercept(MethodDelegation.withDefaultConfiguration()
                            .withBinders(Pipe.Binder.install(Function.class))
                            .to(new LoggingBeanInterceptor(bean)))
                    .make()
                    .load(classLoader)
                    .getLoaded()
                    .newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("Bean[name : " + beanName + "] creation is failed", e);
        }

        return dynamicProxy;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
