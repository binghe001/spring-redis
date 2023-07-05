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
package io.binghe.redis.semaphore.redisson;

import io.binghe.redis.semaphore.DistributedSemaphore;
import io.binghe.redis.semaphore.factory.DistributedSemaphoreFactory;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 基于Redisson的分布式可过期信号量工厂
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@Component
@ConditionalOnProperty(name = "distribute.type.semaphore", havingValue = "redisson")
public class RedissonSemaphoreFactory implements DistributedSemaphoreFactory {

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public DistributedSemaphore getDistributedSemaphore(String key) {
        RPermitExpirableSemaphore permitExpirableSemaphore = redissonClient.getPermitExpirableSemaphore(key);
        return new DistributedSemaphore() {
            @Override
            public String acquire() throws InterruptedException {
                return permitExpirableSemaphore.acquire();
            }

            @Override
            public String acquire(long leaseTime, TimeUnit unit) throws InterruptedException {
                return permitExpirableSemaphore.acquire(leaseTime, unit);
            }

            @Override
            public String tryAcquire() {
                return permitExpirableSemaphore.tryAcquire();
            }

            @Override
            public String tryAcquire(long waitTime, TimeUnit unit) throws InterruptedException {
                return permitExpirableSemaphore.tryAcquire(waitTime, unit);
            }

            @Override
            public String tryAcquire(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                return permitExpirableSemaphore.tryAcquire(waitTime, leaseTime, unit);
            }

            @Override
            public boolean tryRelease(String permitId) {
                return permitExpirableSemaphore.tryRelease(permitId);
            }

            @Override
            public void release(String permitId) {
                permitExpirableSemaphore.release(permitId);
            }

            @Override
            public int availablePermits() {
                return permitExpirableSemaphore.availablePermits();
            }

            @Override
            public boolean trySetPermits(int permits) {
                return permitExpirableSemaphore.trySetPermits(permits);
            }

            @Override
            public void addPermits(int permits) {
                permitExpirableSemaphore.addPermits(permits);
            }

            @Override
            public boolean updateLeaseTime(String permitId, long leaseTime, TimeUnit unit) {
                return permitExpirableSemaphore.updateLeaseTime(permitId, leaseTime, unit);
            }
        };
    }
}
