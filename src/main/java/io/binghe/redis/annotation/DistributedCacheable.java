package io.binghe.redis.annotation;// io.binghe.redis.cache.annotation.DistributedCacheable.javabak
// io.binghe.redis.cache.annotation.DistributedCacheable.javabak

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedCacheable {
    String cachePrefix() default "";

    long ttl() default 60L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    Class<?> clazz();

    String strategy() default "passThrough"; // passThrough, logicalExpire, mutex
}
