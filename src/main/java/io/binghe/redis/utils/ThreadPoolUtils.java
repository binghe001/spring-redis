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
package io.binghe.redis.utils;

import java.util.concurrent.*;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 线程工具类
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public class ThreadPoolUtils {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16,
            16,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4096),
            new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * execute task in thread pool
     */
    public static void execute(Runnable command){
        executor.execute(command);
    }

    public static <T> Future<T> shumit(Callable<T> task){
        return executor.submit(task);
    }

    public static void shutdown(){
        if (executor != null){
            executor.shutdown();
        }
    }
}
