package com.yyw.yhyc.order.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lizhou on 2016/8/9
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
    /**
     * 在需要生成token的controller上增加该属性 ，如： @Token(save=true)
     * @return
     */
    boolean save() default false;

    /**
     * 在需要检查重复提交的controller上添加该属性，如： @Token(remove=true)
     * @return
     */
    boolean remove() default false;
}
