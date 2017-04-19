package com.hc.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by soft01 on 2017/4/18.
 * View注解的annotation
 *
 * @Target(ElementType.FIELD)代表annotation的位置  FIELD:属性  TYPE:类  METHOD:方法  CONSTRUCTOR构造函数
 *
 * @Retention(RetentionPolicy.CLASS)什么时候生效  CLASS编译时   RUNTIME运行时   SOURCE源码资源
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    int value();
}
