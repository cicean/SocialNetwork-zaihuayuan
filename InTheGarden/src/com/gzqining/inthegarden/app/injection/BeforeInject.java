package com.gzqining.inthegarden.app.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 注解前调用对应方法
 * @author xjm
 */
public @interface BeforeInject {
	
}