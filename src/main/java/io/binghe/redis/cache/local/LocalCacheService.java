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
package io.binghe.redis.cache.local;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 本地缓存接口
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public interface LocalCacheService<K, V> {
    /**
     * 向缓存中添加数据
     * @param key 缓存的key
     * @param value 缓存的value
     */
    void put(K key, V value);

    /**
     * 根据key从缓存中查询数据
     * @param key 缓存的key
     * @return 缓存的value值
     */
    V getIfPresent(Object key);

    /**
     * 移除缓存中的数据
     * @param key 缓存的key
     */
    void remove(K key);
}
