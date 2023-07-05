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
package io.binghe.redis.test;

import cn.hutool.json.JSONUtil;
import io.binghe.redis.cache.distribute.DistributeCacheService;
import io.binghe.redis.test.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 测试分布式缓存
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DistributeCacheServiceTest {

    @Autowired
    private DistributeCacheService distributeCacheService;


    @Test
    public void testQueryWithPassThrough(){
        User user = distributeCacheService.queryWithPassThrough("pass:through:", 1002852L, User.class,  this::getUser, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }
    @Test
    public void testQueryWithPassThroughWithoutArgs(){
        User user = distributeCacheService.queryWithPassThroughWithoutArgs("pass:through001:", User.class,  this::getUserWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQuerySimpleDataWithPassThrough(){
        Integer id = distributeCacheService.queryWithPassThrough("pass:through2:", 100285210, Integer.class,  this::getId, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }
    @Test
    public void testQuerySimpleDataWithPassThroughWithoutArgs(){
        Integer id = distributeCacheService.queryWithPassThroughWithoutArgs("pass:through2002:",  Integer.class,  this::getIdWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }

    @Test
    public void testQueryWithPassThroughList(){
        List<User> list = distributeCacheService.queryWithPassThroughList("pass:through:list:", null, User.class, this::getUserList, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQueryWithPassThroughListWithoutArgs(){
        List<User> list = distributeCacheService.queryWithPassThroughListWithoutArgs("pass:through:list003:", User.class, this::getUserListWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithPassThroughList(){
        List<Integer> list = distributeCacheService.queryWithPassThroughList("pass:through:list2:", 100285211, Integer.class, this::getIds, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQuerySimpleDataWithPassThroughListWithoutArgs(){
        List<Integer> list = distributeCacheService.queryWithPassThroughListWithoutArgs("pass:through:list2004:", Integer.class, this::getIdsWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithLogicalExpire(){
        User user = distributeCacheService.queryWithLogicalExpire("logical:expire:", 1002852L, User.class,  this::getUser, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQueryWithLogicalExpireWithoutArgs(){
        User user = distributeCacheService.queryWithLogicalExpireWithoutArgs("logical:expire005:", User.class,  this::getUserWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQuerySimpleDataWithLogicalExpire(){
        Integer id = distributeCacheService.queryWithLogicalExpire("logical:expire2:", 100285212, Integer.class,  this::getId, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }
    @Test
    public void testQuerySimpleDataWithLogicalExpireWithoutArgs(){
        Integer id = distributeCacheService.queryWithLogicalExpireWithoutArgs("logical:expire2006:", Integer.class,  this::getIdWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }

    @Test
    public void testQueryWithLogicalExpireList(){
        List<User> list = distributeCacheService.queryWithLogicalExpireList("logical:expire:list:", null, User.class, this::getUserList, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQueryWithLogicalExpireListWithoutArgs(){
        List<User> list = distributeCacheService.queryWithLogicalExpireListWithoutArgs("logical:expire:list007:", User.class, this::getUserListWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithLogicalExpireList(){
        List<Integer> list = distributeCacheService.queryWithLogicalExpireList("logical:expire:list2:", 100285213, Integer.class, this::getIds, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQuerySimpleDataWithLogicalExpireListWithoutArgs(){
        List<Integer> list = distributeCacheService.queryWithLogicalExpireListWithoutArgs("logical:expire:list2008:", Integer.class, this::getIdsWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithMutex(){
        User user = distributeCacheService.queryWithMutex("mutex:", 1002852L, User.class,  this::getUser, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }
    @Test
    public void testQueryWithMutexWithoutArgs(){
        User user = distributeCacheService.queryWithMutexWithoutArgs("mutex009:", User.class,  this::getUserWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQuerySimpleDataWithMutex(){
        Integer id = distributeCacheService.queryWithMutex("mutex2:", 100285214, Integer.class,  this::getId, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }
    @Test
    public void testQuerySimpleDataWithMutexWithoutArgs(){
        Integer id = distributeCacheService.queryWithMutexWithoutArgs("mutex2010:", Integer.class,  this::getIdWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }

    @Test
    public void testQueryWithMutexList(){
        List<User> list = distributeCacheService.queryWithMutexList("mutex:list:", null, User.class, this::getUserList, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQueryWithMutexListWithoutArgs(){
        List<User> list = distributeCacheService.queryWithMutexListWithoutArgs("mutex:list011:", User.class, this::getUserListWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithMutexList(){
        List<Integer> list = distributeCacheService.queryWithMutexList("mutex:list2:", 123, Integer.class, this::getIds, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithMutexListWithoutArgs(){
        List<Integer> list = distributeCacheService.queryWithMutexListWithoutArgs("mutex:list2012:", Integer.class, this::getIdsWithoutArgs, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    /**
     * 模拟带参数从数据库查询对象
     */
    public User getUser(Long id){
        return new User(id, "binghe");
    }

    /**
     * 默认不带参数从数据库查询对象
     */
    public User getUserWithoutArgs(){
        return new User(1L, "binghe");
    }

    /**
     * 模拟带参数查询从数据库对象列表
     */
    public List<User> getUserList(String type){
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }

    /**
     * 模拟不带参数从数据库查询对象列表
     */
    public List<User> getUserListWithoutArgs(){
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }

    /**
     * 模拟带参数从数据库查询简单数据类型数据
     */
    public Integer getId(Integer id){
        return id;
    }

    /**
     * 模拟不带参数从数据库查询简单数据类型数据
     */
    public Integer getIdWithoutArgs(){
        return 0;
    }

    /**
     * 模拟带参数从数据库查询简单数据类型数据列表
     */
    public List<Integer> getIds(Integer id){
        return Arrays.asList(0,0,0);
    }

    /**
     * 模拟不带参数从数据库查询简单数据类型数据列表
     */
    public List<Integer> getIdsWithoutArgs(){
        return Arrays.asList(0,0,0);
    }
}
