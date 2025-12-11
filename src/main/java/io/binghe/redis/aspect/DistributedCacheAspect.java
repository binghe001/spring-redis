package io.binghe.redis.aspect;


import io.binghe.redis.annotation.DistributedCacheable;
import io.binghe.redis.cache.distribute.DistributeCacheService;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
@Component
public class DistributedCacheAspect {
    
    @Autowired
    private DistributeCacheService distributeCacheService;
    
    @Around("@annotation(distributedCacheable)")
    public Object handleDistributedCache(ProceedingJoinPoint joinPoint, DistributedCacheable distributedCacheable) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String cachePrefix = distributedCacheable.cachePrefix();
        Class<?> targetClass = distributedCacheable.clazz();
        long ttl = distributedCacheable.ttl();
        TimeUnit timeUnit = distributedCacheable.timeUnit();
        String strategy = distributedCacheable.strategy();
        
        Object id = args.length > 0 ? args[0] : null;
        
        switch (strategy) {
            case "passThrough":
                return distributeCacheService.queryWithPassThrough(
                    cachePrefix, 
                    id, 
                    (Class<Object>) targetClass,
                    (Function<Object, Object>) (param) -> {
                        try {
                            return joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }, 
                    ttl, 
                    timeUnit
                );
            case "passThroughWithoutArgs":
                return distributeCacheService.queryWithPassThroughWithoutArgs(
                        cachePrefix,
                        (Class<Object>) targetClass,
                        (Supplier<Object>)() -> {
                            try {
                                return joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            case "passThroughList":
                return distributeCacheService.queryWithPassThroughList(
                        cachePrefix,
                        id,
                        (Class<Object>) targetClass,
                        (Function<Object, List<Object>>) (param) -> {
                            try {
                                Object result = joinPoint.proceed();
                                if (result instanceof List) {
                                    return (List<Object>) result;
                                } else {
                                    return Collections.singletonList(result);
                                }
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            case "passThroughListWithoutArgs":
                return distributeCacheService.queryWithPassThroughListWithoutArgs(
                        cachePrefix,
                        (Class<Object>) targetClass,
                         () -> {
                            try {
                                Object result = joinPoint.proceed();
                                if (result instanceof List) {
                                    return (List<Object>) result;
                                } else {
                                    return Collections.singletonList(result);
                                }
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            case "logicalExpire":
                return distributeCacheService.queryWithLogicalExpire(
                    cachePrefix, 
                    id, 
                    (Class<Object>) targetClass,
                    (Function<Object, Object>) (param) -> {
                        try {
                            return joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }, 
                    ttl, 
                    timeUnit
                );
            case "logicalExpireWithoutArgs":
                return distributeCacheService.queryWithLogicalExpire(
                        cachePrefix,
                        id,
                        (Class<Object>) targetClass,
                        (Function<Object, Object>) (param) -> {
                            try {
                                return joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            case "mutex":
                return distributeCacheService.queryWithMutex(
                    cachePrefix, 
                    id, 
                    (Class<Object>) targetClass,
                    (Function<Object, Object>) (param) -> {
                        try {
                            return joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    }, 
                    ttl, 
                    timeUnit
                );
            case "mutexWithoutArgs":
                return distributeCacheService.queryWithMutexWithoutArgs(
                        cachePrefix,
                        (Class<Object>) targetClass,
                        (Supplier<Object>) () -> {
                            try {
                                return joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            case "mutexList":
                return distributeCacheService.queryWithMutexList(
                        cachePrefix,
                        id,
                        (Class<Object>) targetClass,
                        (Function<Object, List<Object>>) (param) -> {
                            try {
                                Object result = joinPoint.proceed();
                                if (result instanceof List) {
                                    return (List<Object>) result;
                                } else {
                                    return Collections.singletonList(result);
                                }
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            case "mutexListWithoutArgs":
                return distributeCacheService.queryWithMutexList(
                        cachePrefix,
                        id,
                        (Class<Object>) targetClass,
                        (Function<Object, List<Object>>) (param) -> {
                            try {
                                Object result = joinPoint.proceed();
                                if (result instanceof List) {
                                    return (List<Object>) result;
                                } else {
                                    return Collections.singletonList(result);
                                }
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );

            case "logicalExpireList":
                return distributeCacheService.queryWithLogicalExpireList(
                    cachePrefix,
                    id,
                    (Class<Object>) targetClass,
                    (Function<Object, List<Object>>) (param) -> {
                        try {
                            Object result = joinPoint.proceed();
                            if (result instanceof List) {
                                return (List<Object>) result;
                            } else {
                                return Collections.singletonList(result);
                            }
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    },
                    ttl,
                    timeUnit
                );
            case "logicalExpireListWithoutArgs":
                return distributeCacheService.queryWithLogicalExpireListWithoutArgs(
                        cachePrefix,
                        (Class<Object>) targetClass,
                        ( Supplier<List<Object>>) () -> {
                            try {
                                Object result = joinPoint.proceed();
                                if (result instanceof List) {
                                    return (List<Object>) result;
                                } else {
                                    return Collections.singletonList(result);
                                }
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                        },
                        ttl,
                        timeUnit
                );
            default:
                return joinPoint.proceed();
        }
    }
}
