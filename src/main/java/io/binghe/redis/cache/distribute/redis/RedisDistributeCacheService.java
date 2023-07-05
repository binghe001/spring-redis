/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.binghe.redis.cache.distribute.redis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.binghe.redis.cache.distribute.DistributeCacheService;
import io.binghe.redis.cache.distribute.data.RedisData;
import io.binghe.redis.lock.DistributedLock;
import io.binghe.redis.lock.factory.DistributedLockFactory;
import io.binghe.redis.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 基于Redis实现的分布式缓存，在满足分布式缓存的需求时，解决了缓存击穿、穿透和雪崩的问题
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@Component
@ConditionalOnProperty(name = "cache.type.distribute", havingValue = "redis")
public class RedisDistributeCacheService implements DistributeCacheService {

    private final Logger logger = LoggerFactory.getLogger(RedisDistributeCacheService.class);

    //缓存空数据的时长，单位秒
    private static final Long CACHE_NULL_TTL = 60L;
    //缓存的空数据
    private static final String EMPTY_VALUE = "";
    //分布式锁key的后缀
    private static final String LOCK_SUFFIX = "_lock";
    //等待锁时间，默认2秒
    private static final Long LOCK_WAIT_TIMEOUT = 2000L;
    //线程休眠的毫秒数
    private static final long THREAD_SLEEP_MILLISECONDS = 50;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DistributedLockFactory distributedLockFactory;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, this.getValue(value));
    }

    @Override
    public void set(String key, Object value, Long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, this.getValue(value), timeout, unit);
    }

    @Override
    public void setWithLogicalExpire(String key, Object value, Long timeout, TimeUnit unit) {
        RedisData redisData = new RedisData(value, LocalDateTime.now().plusSeconds(unit.toSeconds(timeout)));
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis查询缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //缓存存在数据，直接返回
        if (StrUtil.isNotBlank(str)){
            //返回数据
            return this.getResult(str, type);
        }
        //缓存中存储的是空字符串
        if (str != null){
            //直接返回空
            return null;
        }
        //从数据库查询数据
        R r = dbFallback.apply(id);
        //数据数据为空
        if (r == null){
            redisTemplate.opsForValue().set(key, EMPTY_VALUE, CACHE_NULL_TTL, TimeUnit.SECONDS);
            return null;
        }
        //缓存数据
        this.set(key, r, timeout, unit);
        return r;
    }

    @Override
    public <R, ID> List<R> queryWithPassThroughList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis查询缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //缓存存在数据，直接返回
        if (StrUtil.isNotBlank(str)){
            //返回数据
            return this.getResultList(str, type);
        }
        if (str != null){
            //直接返回数据
            return null;
        }
        List<R> r = dbFallback.apply(id);
        //数据库数据为空
        if (r == null || r.isEmpty()){
            redisTemplate.opsForValue().set(key, EMPTY_VALUE, CACHE_NULL_TTL, TimeUnit.SECONDS);
            return null;
        }
        this.set(key, r, timeout, unit);
        return r;
    }

    @Override
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //判断数据是否存在
        if (StrUtil.isBlank(str)){
            try{
                // 构建缓存数据
                buildCache(id, dbFallback, timeout, unit, key);
                Thread.sleep(THREAD_SLEEP_MILLISECONDS);
                //重试
                return queryWithLogicalExpire(keyPrefix, id, type, dbFallback, timeout, unit);
            }catch (InterruptedException e){
                logger.error("query data with logical expire|{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        //命中，需要先把json反序列化为对象
        RedisData redisData = this.getResult(str, RedisData.class);
        R r = this.getResult(redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            // 未过期，直接返回数据
            return r;
        }
        //缓存获取，构建缓存数据
        buildCache(id, dbFallback, timeout, unit, key);
        //返回逻辑过期数据
        return r;
    }

    /**
     * 构建缓存逻辑过期数据
     */
    private <R, ID> void buildCache(ID id, Function<ID, R> dbFallback, Long timeout, TimeUnit unit, String key) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        // 分布式锁
        String lockKey = this.getLockKey(key);
        //获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        //获取成功, Double Check
        ThreadPoolUtils.execute(() -> {
            try{
                boolean isLock = distributedLock.tryLock(LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
                if (isLock){
                    R newR = null;
                    //从Redis获取缓存数据
                    String str = redisTemplate.opsForValue().get(key);
                    if (StrUtil.isEmpty(str)){
                        //查询数据库
                        newR = dbFallback.apply(id);
                    }else{
                        //命中，需要先把json反序列化为对象
                        RedisData redisData = this.getResult(str, RedisData.class);
                        LocalDateTime expireTime = redisData.getExpireTime();
                        if (expireTime.isBefore(LocalDateTime.now())){
                            //查询数据库
                            newR = dbFallback.apply(id);
                        }
                    }
                    if (newR != null){
                        // 重建缓存
                        this.setWithLogicalExpire(key, newR, timeout, unit);
                    }
                }
            }catch (InterruptedException e){
                logger.error("build cache | {}", e.getMessage());
                throw new RuntimeException(e);
            }finally {
               distributedLock.unlock();
            }
        });
    }

    //分布式锁Key
    private String getLockKey(String key){
        return key.concat(LOCK_SUFFIX);
    }

    @Override
    public <R, ID> List<R> queryWithLogicalExpireList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        //判断数据是否存在
        if (StrUtil.isBlank(str)){
            try{
                // 构建缓存数据
                buildCacheList(id, dbFallback, timeout, unit, key);
                Thread.sleep(THREAD_SLEEP_MILLISECONDS);
                //重试
                return queryWithLogicalExpireList(keyPrefix, id, type, dbFallback, timeout, unit);
            }catch (InterruptedException e){
                logger.error("query data with logical expire|{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        //命中，需要先把json反序列化为对象
        RedisData redisData = this.getResult(str, RedisData.class);
        List<R> list = this.getResultList(JSONUtil.toJsonStr(redisData.getData()), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            // 未过期，直接返回数据
            return list;
        }
        //缓存获取，构建缓存数据
        buildCacheList(id, dbFallback, timeout, unit, key);
        //返回逻辑过期数据
        return list;
    }

    /**
     * 构建缓存逻辑过期数据
     */
    private <R, ID> void buildCacheList(ID id, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit, String key) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        // 分布式锁
        String lockKey = this.getLockKey(key);
        //获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        ThreadPoolUtils.execute(() -> {
            try{
                boolean isLock = distributedLock.tryLock(LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
                if (isLock){
                    List<R> newR = null;
                    //从Redis获取缓存数据
                    String str = redisTemplate.opsForValue().get(key);
                    if (StrUtil.isEmpty(str)){
                        //查询数据库
                        newR = dbFallback.apply(id);
                    }else{
                        //命中，需要先把json反序列化为对象
                        RedisData redisData = this.getResult(str, RedisData.class);
                        LocalDateTime expireTime = redisData.getExpireTime();
                        //缓存已经逻辑过期
                        if (expireTime.isBefore(LocalDateTime.now())){
                            //查询数据库
                            newR = dbFallback.apply(id);
                        }
                    }
                    if (newR != null){
                        // 重建缓存
                        this.setWithLogicalExpire(key, newR, timeout, unit);
                    }
                }
            }catch (InterruptedException e){
                logger.error("build cache list | {}", e.getMessage());
                throw new RuntimeException(e);
            }finally {
                distributedLock.unlock();
            }
        });
    }

    @Override
    public <R, ID> R queryWithMutex(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(str)){
            //存在数据，直接返回
            return this.getResult(str, type);
        }
        //缓存了空字符串
        if (str != null){
            return null;
        }
        String lockKey = this.getLockKey(key);
        R r = null;
        //获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        try {
            boolean isLock = distributedLock.tryLock(LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
            //获取信号量失败，重试
            if (!isLock){
                Thread.sleep(THREAD_SLEEP_MILLISECONDS);
                return queryWithMutex(keyPrefix, id, type, dbFallback, timeout, unit);
            }
            //成功获取到锁
            r = dbFallback.apply(id);
            //数据库本身不存在数据
            if (r == null){
                //缓存空数据
                redisTemplate.opsForValue().set(key, EMPTY_VALUE, CACHE_NULL_TTL, TimeUnit.SECONDS);
                return null;
            }
            //数据库存在数据
            this.set(key, r, timeout, unit);
        } catch (InterruptedException e) {
            logger.error("query data with mutex |{}", e.getMessage());
            throw new RuntimeException(e);
        }finally {
            distributedLock.unlock();
        }
        return r;
    }

    @Override
    public <R, ID> List<R> queryWithMutexList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit) {
        //获取存储到Redis中的数据key
        String key = this.getKey(keyPrefix, id);
        //从Redis获取缓存数据
        String str = redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(str)){
            //存在数据，直接返回
            return this.getResultList(str, type);
        }
        //缓存了空字符串
        if (str != null){
            return null;
        }
        String lockKey = this.getLockKey(key);
        List<R> list = null;
        // 获取分布式锁
        DistributedLock distributedLock = distributedLockFactory.getDistributedLock(lockKey);
        try {
            boolean isLock = distributedLock.tryLock(LOCK_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
            //获取信号量失败，重试
            if (!isLock){
                Thread.sleep(THREAD_SLEEP_MILLISECONDS);
                //重试
                return queryWithMutexList(keyPrefix, id, type, dbFallback, timeout, unit);
            }
            //成功获取到锁
            list = dbFallback.apply(id);
            //数据库本身不存在数据
            if (list == null){
                //缓存空数据
                redisTemplate.opsForValue().set(key, EMPTY_VALUE, CACHE_NULL_TTL, TimeUnit.SECONDS);
                return null;
            }
            //数据库存在数据
            this.set(key, list, timeout, unit);

        } catch (InterruptedException e) {
            logger.error("query data with mutex list |{}", e.getMessage());
            throw new RuntimeException(e);
        }finally {
            distributedLock.unlock();
        }
        return list;
    }
}
