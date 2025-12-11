package io.binghe.redis.test;

import io.binghe.redis.annotation.DistributedCacheable;
import io.binghe.redis.test.bean.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AnnotatedUserService {

    @DistributedCacheable(
        cachePrefix = "pass:through:",
        clazz = User.class,
        strategy = "passThrough",
        ttl = 60,
        timeUnit = TimeUnit.SECONDS
    )
    public User getUserById(Long id) {
        System.out.println("执行数据库查询: getUserById(" + id + ")");
        return new User(id, "user_" + id);
    }

    @DistributedCacheable(
            cachePrefix = "pass:through2",
            clazz = Integer.class,
            strategy = "PassThrough",
            ttl = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public Integer getId(Integer id){
        return id;
    }
    @DistributedCacheable(
            cachePrefix = "pass:through2002:",
            clazz = Integer.class,
            strategy = "PassThroughWithoutArgs",
            ttl = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public Integer getIdWithoutArgs(){
        return 0;
    }
    @DistributedCacheable(
            cachePrefix = "pass:through001:",
            clazz = User.class,
            strategy = "PassThroughWithoutArgs",
            ttl = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public User getUserWithoutArgs(){
        return new User(1L, "binghe");
    }
    @DistributedCacheable(
        cachePrefix = "user:list:",
        clazz = User.class,
        strategy = "logicalExpireList",
        ttl = 30,
        timeUnit = TimeUnit.SECONDS
    )
    public List<User> getAllUsers() {
        System.out.println("执行数据库查询: getAllUsers()");
        return Arrays.asList(
            new User(1L, "user_1"),
            new User(2L, "user_2"),
            new User(3L, "user_3")
        );
    }

    @DistributedCacheable(
        cachePrefix = "simple:data:",
        clazz = Integer.class,
        strategy = "mutex",
        ttl = 30,
        timeUnit = TimeUnit.SECONDS
    )
    public Integer getSimpleData(Integer id) {
        System.out.println("执行数据库查询: getSimpleData(" + id + ")");
        return id * 100;
    }

    @DistributedCacheable(
        cachePrefix = "user:list:type:",
        clazz = User.class,
        strategy = "passThroughList",
        ttl = 90,
        timeUnit = TimeUnit.SECONDS
    )
    public List<User> getUserListWithPassThroughList(String type) {
        System.out.println("执行数据库查询: getUserListWithPassThroughList(" + type + ")\n");
        return Arrays.asList(
            new User(1L, "type_" + type + "_1"),
            new User(2L, "type_" + type + "_2")
        );
    }
    @DistributedCacheable(
            cachePrefix = "user:list:type:",
            clazz = User.class,
            strategy = "passThroughListWithoutArgs",
            ttl = 90,
            timeUnit = TimeUnit.SECONDS
    )
    public List<User> getUserListWithoutArgs() {
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }
    /**
     * 模拟带参数从数据库查询简单数据类型数据列表
     */
    @DistributedCacheable(
            cachePrefix = "pass:through:list2:",
            clazz = Integer.class,
            strategy = "passThroughList",
            ttl = 90,
            timeUnit = TimeUnit.SECONDS
    )
    public List<Integer> simpleDataWithPassThroughList(Integer id) {
        return Arrays.asList(0,0,0);
    }
    public List<Integer> getIds(Integer id) {
        return Arrays.asList(0,0,0);
    }

    @DistributedCacheable(
            cachePrefix = "pass:through:list2:",
            clazz = Integer.class,
            strategy = "passThroughList",
            ttl = 90,
            timeUnit = TimeUnit.SECONDS
    )
    public List<Integer> simpleDataWithPassThroughListWithoutArgs() {
        return Arrays.asList(0,0,0);
    }
    @DistributedCacheable(
            cachePrefix = "logical:expire:",
            clazz = User.class,
            strategy = "logicalExpire",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public User getUserlogicalExpire(Long id) {
        return new User(id, "binghe");
    }
    @DistributedCacheable(
            cachePrefix = "logical:expire005",
            clazz = User.class,
            strategy = "logicalExpireWithoutArgs",
            ttl = 60,
            timeUnit = TimeUnit.SECONDS
    )
    public User getUserlogicalExpireWithoutArgs() {
        return new User(1L, "binghe");
    }

    @DistributedCacheable(
            cachePrefix = "logical:expire2:",
            clazz = Integer.class,
            strategy = "logicalExpire",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public Integer getIdWithLogicalExpire(Integer  id) {
        return id;
    }

   @DistributedCacheable(
            cachePrefix = "logical:expire2006:",
            clazz = Integer.class,
            strategy = "logicalExpireWithoutArgs",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public Integer getIdWithLogicalExpireWithoutArgs() {
        return 0;
    }

    @DistributedCacheable(
            cachePrefix = "logical:expire:list:",
            clazz = User.class,
            strategy = "logicalExpireList",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public List<User> getUserListWithLogicalExpire() {
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }
    @DistributedCacheable(
            cachePrefix = "logical:expire:list007:",
            clazz = User.class,
            strategy = "logicalExpireListWithoutArgs",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public List<User> getUserListWithLogicalExpireWithoutArgs() {
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }
    @DistributedCacheable(
            cachePrefix = "logical:expire:list2:",
            clazz = Integer.class,
            strategy = "logicalExpireList",
            ttl = 30
    )
    public List<Integer> getIdsWithLogicalExpireList(int i) {
        return Arrays.asList(0,0,0);
    }
   @DistributedCacheable(
            cachePrefix = "logical:expire:list2008:",
            clazz = Integer.class,
            strategy = "logicalExpireListWithoutArgs",
            ttl = 30
    )
    public List<Integer> getIdsWithLogicalExpireListWithoutArgs() {
        return Arrays.asList(0,0,0);
    }

    @DistributedCacheable(
            cachePrefix = "mutex:",
            clazz = User.class,
            strategy = "mutex",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public User getUserWithMutex(long id) {
        return new User(id, "binghe");
    }

    @DistributedCacheable(
            cachePrefix = "mutex009:",
            clazz = User.class,
            strategy = "mutexWithoutArgs",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public User getUserWithMutexWithoutArgs() {
        return new User(1L, "binghe");
    }
    @DistributedCacheable(
            cachePrefix = "mutex2:",
            clazz = Integer.class,
            strategy = "mutex",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public Integer getIdWithMutex(int i) {
        return i;
    }
    @DistributedCacheable(
            cachePrefix = "mutex2010:",
            clazz = Integer.class,
            strategy = "mutexWithoutArgs",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public Integer getIdWithMutexWithoutArgs() {
        return 0;
    }
    @DistributedCacheable(
            cachePrefix = "mutex:list:",
            clazz = User.class,
            strategy = "mutexList",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public List<User> getUserListWithMutex() {
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }
    @DistributedCacheable(
            cachePrefix = "mutex:list011:",
            clazz = User.class,
            strategy = "mutexListWithoutArgs",
            ttl = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public List<User> getUserListWithMutexWithoutArgs() {
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }
  @DistributedCacheable(
            cachePrefix = "mutex:list2:",
            clazz = Integer.class,
            strategy = "mutexList",
            ttl = 30
    )
    public List<Integer> getIdsWithMutexList() {
        return Arrays.asList(0,0,0);
    }
    @DistributedCacheable(
            cachePrefix = "mutex:list2012:",
            clazz = Integer.class,
            strategy = "mutexListWithoutArgs",
            ttl = 30
    )
    public List<Integer> getIdsWithMutexListWithoutArgs() {
        return Arrays.asList(0,0,0);
    }
}
