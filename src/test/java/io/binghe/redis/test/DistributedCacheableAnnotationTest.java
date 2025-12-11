package io.binghe.redis.test;

import cn.hutool.json.JSONUtil;
import io.binghe.redis.test.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DistributedCacheableAnnotationTest {

    @Autowired
    private AnnotatedUserService annotatedUserService;

    @Test
    public void testQueryWithPassThrough() {
        System.out.println("=== 测试帶參數 PassThrough 策略 ===");

        // 第一次调用，应该执行方法并将结果缓存
        long startTime1 = System.currentTimeMillis();
        User user1 = annotatedUserService.getUserById(1001L);
        long endTime1 = System.currentTimeMillis();
        System.out.println("第一次获取用户耗时: " + (endTime1 - startTime1) + "ms");
        System.out.println("第一次获取用户: " + JSONUtil.toJsonStr(user1));

        // 第二次调用，应该从缓存中获取
        long startTime2 = System.currentTimeMillis();
        User user2 = annotatedUserService.getUserById(1001L);
        long endTime2 = System.currentTimeMillis();
        System.out.println("第二次获取用户耗时: " + (endTime2 - startTime2) + "ms");
        System.out.println("第二次获取用户: " + JSONUtil.toJsonStr(user2));

    }

    @Test
    public void testQueryWithPassThroughWithoutArgs() {
        System.out.println("=== 测试不帶參數PassThrough 策略 ===");

        // 第一次调用，应该执行方法并将结果缓存
        long startTime1 = System.currentTimeMillis();
        User user1 = annotatedUserService.getUserWithoutArgs();
        long endTime1 = System.currentTimeMillis();
        System.out.println("第一次获取用户耗时: " + (endTime1 - startTime1) + "ms");
        System.out.println("第一次获取用户: " + JSONUtil.toJsonStr(user1));

        // 第二次调用，应该从缓存中获取
        long startTime2 = System.currentTimeMillis();
        User user2 = annotatedUserService.getUserWithoutArgs();
        long endTime2 = System.currentTimeMillis();
        System.out.println("第二次获取用户耗时: " + (endTime2 - startTime2) + "ms");
        System.out.println("第二次获取用户: " + JSONUtil.toJsonStr(user2));
    }

    @Test
    public void testQuerySimpleDataWithPassThrough() {
        Integer id1 = annotatedUserService.getId(3);
        System.out.println(id1);
    }

    @Test
    public void testQuerySimpleDataWithPassThroughWithoutArgs() {
        Integer id = annotatedUserService.getIdWithoutArgs();
        System.out.println(id);
    }

    @Test
    public void testQueryWithPassThroughList() {
        System.out.println("=== 测试帶参数方法缓存 ===");
        // 测试带参数的方法缓存
        long startTime1 = System.currentTimeMillis();
        List<User> list1 = annotatedUserService.getUserListWithPassThroughList("vip");
        long endTime1 = System.currentTimeMillis();
        System.out.println("第一次获取VIP用户耗时: " + (endTime1 - startTime1) + "ms");
        System.out.println("第一次获取VIP用户: " + JSONUtil.toJsonStr(list1));

        long startTime2 = System.currentTimeMillis();
        List<User> list2 = annotatedUserService.getUserListWithPassThroughList("vip");
        long endTime2 = System.currentTimeMillis();
        System.out.println("第二次获取VIP用户耗时: " + (endTime2 - startTime2) + "ms");
        System.out.println("第二次获取VIP用户: " + JSONUtil.toJsonStr(list2));
    }

    @Test
    public void testQueryWithPassThroughListWithoutArgs() {
        List<User> list = annotatedUserService.getUserListWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithPassThroughList() {
        List<Integer> list = annotatedUserService.simpleDataWithPassThroughList(9);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithPassThroughListWithoutArgs() {
        List<Integer> list = annotatedUserService.simpleDataWithPassThroughListWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithLogicalExpire() {
        User userlogicalExpire = annotatedUserService.getUserlogicalExpire(3L);
        System.out.println(JSONUtil.toJsonStr(userlogicalExpire));
    }

    @Test
    public void testQueryWithLogicalExpireWithoutArgs() {
        User user = annotatedUserService.getUserlogicalExpireWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQuerySimpleDataWithLogicalExpire() {
        Integer id = annotatedUserService.getIdWithLogicalExpire(100285212);
        System.out.println(id);
    }

    @Test
    public void testQuerySimpleDataWithLogicalExpireWithoutArgs() {
        Integer id = annotatedUserService.getIdWithLogicalExpireWithoutArgs();
        System.out.println(id);
    }

    @Test
    public void testQueryWithLogicalExpireList() {
        List<User> list = annotatedUserService.getUserListWithLogicalExpire();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithLogicalExpireListWithoutArgs() {
        List<User> list = annotatedUserService.getUserListWithLogicalExpireWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithLogicalExpireList() {
        List<Integer> list = annotatedUserService.getIdsWithLogicalExpireList(100285213);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithLogicalExpireListWithoutArgs() {
        List<Integer> list = annotatedUserService.getIdsWithLogicalExpireListWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithMutex() {
        User user = annotatedUserService.getUserWithMutex(1002852L);
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQueryWithMutexWithoutArgs() {
        User user = annotatedUserService.getUserWithMutexWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQuerySimpleDataWithMutex() {
        Integer id = annotatedUserService.getIdWithMutex(100285214);
        System.out.println(id);
    }

    @Test
    public void testQuerySimpleDataWithMutexWithoutArgs() {
        Integer id = annotatedUserService.getIdWithMutexWithoutArgs();
        System.out.println(id);
    }

    @Test
    public void testQueryWithMutexList() {
        List<User> list = annotatedUserService.getUserListWithMutex();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithMutexListWithoutArgs() {
        List<User> list = annotatedUserService.getUserListWithMutexWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithMutexList() {
        List<Integer> list = annotatedUserService.getIdsWithMutexList();
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQuerySimpleDataWithMutexListWithoutArgs() {
        List<Integer> list = annotatedUserService.getIdsWithMutexListWithoutArgs();
        System.out.println(JSONUtil.toJsonStr(list));
    }
}
