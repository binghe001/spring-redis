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
package io.binghe.redis.cache.distribute;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import io.binghe.redis.cache.distribute.conversion.TypeConversion;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 分布式缓存接口，通用型接口，在满足分布式缓存的需求时，解决了缓存击穿、穿透和雪崩的问题
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public interface DistributeCacheService {

    /**
     * 永久缓存
     * @param key 缓存key
     * @param value 缓存value
     */
    void set(String key, Object value);

    /**
     * 将数据缓存一段时间
     * @param key 缓存key
     * @param value 缓存value
     * @param timeout 物理缓存的时长
     * @param unit 物理时间单位
     */
    void set(String key, Object value, Long timeout, TimeUnit unit);

    /**
     * 保存缓存时设置逻辑过期时间
     * @param key 缓存key
     * @param value 缓存value
     * @param timeout 缓存逻辑过期时长
     * @param unit 缓存逻辑时间单位
     */
    void setWithLogicalExpire(String key, Object value, Long timeout, TimeUnit unit);

    /**
     * 获取缓存中的数据
     * @param key 缓存key
     * @return 缓存value
     */
    String get(String key);

    /**
     * 带参数查询对象和简单类型数据，防止缓存穿透
     * @param keyPrefix 缓存key的前缀
     * @param id 缓存的业务标识，
     * @param type 缓存的实际对象类型
     * @param dbFallback 查询数据库的Function函数
     * @param timeout 缓存的时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     * @param <ID> 查询数据库参数泛型，也是参数泛型类型
     */
    <R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 不带参数查询对象和简单类型数据，防止缓存穿透
     * @param keyPrefix key的前缀
     * @param type 缓存的实际对象类型
     * @param dbFallback 无参数查询数据库数据
     * @param timeout 缓存的时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     */
    <R> R queryWithPassThroughWithoutArgs(String keyPrefix, Class<R> type, Supplier<R> dbFallback, Long timeout, TimeUnit unit);
    /**
     * 带参数查询集合数据，防止缓存穿透
     * @param keyPrefix 缓存key的前缀
     * @param id 缓存的业务标识，
     * @param type 缓存的实际对象类型
     * @param dbFallback 查询数据库的Function函数
     * @param timeout 缓存的时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     * @param <ID> 查询数据库参数泛型，也是参数泛型类型
     */
    <R,ID> List<R> queryWithPassThroughList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 不带参数查询集合数据，防止缓存穿透
     * @param keyPrefix 缓存key的前缀
     * @param type 缓存的实际对象类型
     * @param dbFallback 无参数查询数据库数据
     * @param timeout 缓存的时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     */
    <R> List<R> queryWithPassThroughListWithoutArgs(String keyPrefix, Class<R> type, Supplier<List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 带参数查询数据，按照逻辑过期时间读取缓存数据，新开线程重建缓存，其他线程直接返回逻辑过期数据，不占用资源
     * @param keyPrefix 缓存key的前缀
     * @param id 缓存业务标识，也是查询数据库的参数
     * @param type 缓存的实际对象类型
     * @param dbFallback 查询数据库的Function函数
     * @param timeout 缓存逻辑过期时长
     * @param unit 缓存逻辑过期时间单位
     * @return 业务数据
     * @param <R> 结果数据泛型类型
     * @param <ID> 查询数据库泛型类型，也是参数泛型类型
     */
    <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 不带参数查询数据，按照逻辑过期时间读取缓存数据，新开线程重建缓存，其他线程直接返回逻辑过期数据，不占用资源
     * @param keyPrefix 缓存key的前缀
     * @param type 缓存的实际对象类型
     * @param dbFallback 无参数查询数据库数据
     * @param timeout 缓存的时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     */
    <R> R queryWithLogicalExpireWithoutArgs(String keyPrefix, Class<R> type, Supplier<R> dbFallback, Long timeout, TimeUnit unit);
    /**
     * 带参数查询集合数据，按照逻辑过期时间读取缓存数据，新开线程重建缓存，其他线程直接返回逻辑过期数据，不占用资源
     * @param keyPrefix 缓存key的前缀
     * @param id 缓存业务标识，也是查询数据库的参数
     * @param type 缓存的实际对象类型
     * @param dbFallback 查询数据库的Function函数
     * @param timeout 缓存逻辑过期时长
     * @param unit 缓存逻辑过期时间单位
     * @return 业务数据
     * @param <R> 结果数据泛型类型
     * @param <ID> 查询数据库泛型类型，也是参数泛型类型
     */
    <R, ID> List<R> queryWithLogicalExpireList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 不带参数查询集合数据，按照逻辑过期时间读取缓存数据，新开线程重建缓存，其他线程直接返回逻辑过期数据，不占用资源
     * @param keyPrefix 缓存key的前缀
     * @param type 缓存的实际对象类型
     * @param dbFallback 无参数查询数据库数据
     * @param timeout 缓存的时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     */
    <R> List<R> queryWithLogicalExpireListWithoutArgs(String keyPrefix, Class<R> type, Supplier<List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 带参数查询数据，按照互斥锁方式获取缓存数据，同一时刻只有一个线程访问数据库，其他线程访问不到数据重试
     * @param keyPrefix 缓存key的前缀
     * @param id 缓存业务标识，也是查询数据库的参数
     * @param type 缓存的实际对象类型
     * @param dbFallback 查询数据库的Function函数
     * @param timeout 缓存时长
     * @param unit 时间单位
     * @return 业务数据
     * @param <R> 结果数据泛型类型
     * @param <ID> 查询数据库泛型类型，也是参数泛型类型
     */
    <R, ID> R queryWithMutex(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 不带参数查询数据，按照互斥锁方式获取缓存数据，同一时刻只有一个线程访问数据库，其他线程访问不到数据重试
     * @param keyPrefix 缓存key的前缀
     * @param type 缓存的实际对象类型
     * @param dbFallback 无参数查询数据库数据
     * @param timeout 缓存时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     */
    <R> R queryWithMutexWithoutArgs(String keyPrefix, Class<R> type, Supplier<R> dbFallback, Long timeout, TimeUnit unit);
    /**
     * 带参数查询数据，按照互斥锁方式获取缓存数据，同一时刻只有一个线程访问数据库，其他线程访问不到数据重试
     * @param keyPrefix 缓存key的前缀
     * @param id 缓存业务标识，也是查询数据库的参数
     * @param type 缓存的实际对象类型
     * @param dbFallback 查询数据库的Function函数
     * @param timeout 缓存时长
     * @param unit 时间单位
     * @return 业务数据
     * @param <R> 结果数据泛型类型
     * @param <ID> 查询数据库泛型类型，也是参数泛型类型
     */
    <R, ID> List<R> queryWithMutexList(String keyPrefix, ID id, Class<R> type, Function<ID, List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 不带参数查询数据，按照互斥锁方式获取缓存数据，同一时刻只有一个线程访问数据库，其他线程访问不到数据重试
     * @param keyPrefix 缓存key的前缀
     * @param type 缓存的实际对象类型
     * @param dbFallback 无参数查询数据库数据
     * @param timeout 缓存时长
     * @param unit 时间单位
     * @return 返回业务数据
     * @param <R> 结果泛型
     */
    <R> List<R> queryWithMutexListWithoutArgs(String keyPrefix, Class<R> type, Supplier<List<R>> dbFallback, Long timeout, TimeUnit unit);

    /**
     * 将对象类型的json字符串转换成泛型类型
     * @param obj 未知类型对象
     * @param type 泛型Class类型
     * @return 泛型对象
     * @param <R> 泛型
     */
    default <R> R getResult(Object obj, Class<R> type){
        if (obj == null){
            return null;
        }
        //简单类型
        if (TypeConversion.isSimpleType(obj)){
            return Convert.convert(type, obj);
        }
        return JSONUtil.toBean(JSONUtil.toJsonStr(obj), type);
    }

    /**
     * 将对象类型的json字符串转换成泛型类型的List集合
     * @param str json字符串
     * @param type 泛型Class类型
     * @return 泛型List集合
     * @param <R> 泛型
     */
    default <R> List<R> getResultList(String str, Class<R> type){
        if (StrUtil.isEmpty(str)){
            return null;
        }
        return JSONUtil.toList(JSONUtil.parseArray(str), type);
    }

    /**
     * 获取简单的key
     * @param key key
     * @return 返回key
     */
    default String getKey(String key){
        return getKey(key, null);
    }

    /**
     * 不确定参数类型的情况下，使用MD5计算参数的拼接到Redis中的唯一Key
     * @param keyPrefix 缓存key的前缀
     * @param id 泛型参数
     * @return 拼接好的缓存key
     * @param <ID> 参数泛型类型
     */
    default <ID> String getKey(String keyPrefix, ID id){
        if (id == null){
            return keyPrefix;
        }
        String key = "";
        //简单数据类型与简单字符串
        if (TypeConversion.isSimpleType(id)){
            key = StrUtil.toString(id);
        }else {
            key = MD5.create().digestHex(JSONUtil.toJsonStr(id));
        }
        if (StrUtil.isEmpty(key)){
            key = "";
        }
        return keyPrefix.concat(key);
    }

    /**
     * 获取要保存到缓存中的value字符串，可能是简单类型，也可能是对象类型，也可能是集合数组等
     * @param value 要保存的value值
     * @return 处理好的字符串
     */
    default String getValue(Object value){
        return TypeConversion.isSimpleType(value) ? String.valueOf(value) : JSONUtil.toJsonStr(value);
    }
}
