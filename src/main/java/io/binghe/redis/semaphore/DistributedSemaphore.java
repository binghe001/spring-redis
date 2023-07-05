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
package io.binghe.redis.semaphore;

import java.util.concurrent.TimeUnit;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 分布式可过期信号量
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public interface DistributedSemaphore {

    String acquire() throws InterruptedException;

    String acquire(long leaseTime, TimeUnit unit) throws InterruptedException;

    String tryAcquire();

    String tryAcquire(long waitTime, TimeUnit unit) throws InterruptedException;

    String tryAcquire(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    boolean tryRelease(String permitId);

    void release(String permitId);

    int availablePermits();

    boolean trySetPermits(int permits);

    void addPermits(int permits);

    boolean updateLeaseTime(String permitId, long leaseTime, TimeUnit unit);
}
