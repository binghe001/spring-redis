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
package io.binghe.redis.cache.local.guava.factoty;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 基于Guava的本地缓存工厂类
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public class LocalGuavaCacheFactory {

    public static <K, V> Cache<K, V> getLocalCache(){
        return CacheBuilder.newBuilder().initialCapacity(200).concurrencyLevel(5).expireAfterWrite(300, TimeUnit.SECONDS).build();
    }

    public static <K, V> Cache<K, V> getLocalCache(long duration){
        return CacheBuilder.newBuilder().initialCapacity(200).concurrencyLevel(5).expireAfterWrite(duration, TimeUnit.SECONDS).build();
    }

    public static <K, V> Cache<K, V> getLocalCache(int initialCapacity, long duration){
        return CacheBuilder.newBuilder().initialCapacity(initialCapacity).concurrencyLevel(5).expireAfterWrite(duration, TimeUnit.SECONDS).build();
    }
}
