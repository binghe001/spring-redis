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
package io.binghe.redis.lock.redisson;

import io.binghe.redis.lock.DistributedLock;
import io.binghe.redis.lock.factory.DistributedLockFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 基于Redisson的分布式锁实现
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@Component
@ConditionalOnProperty(name = "distribute.type.lock", havingValue = "redisson")
public class RedissonLockFactory implements DistributedLockFactory {
    private final Logger logger = LoggerFactory.getLogger(RedissonLockFactory.class);

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public DistributedLock getDistributedLock(String key) {
        RLock rLock = redissonClient.getLock(key);
        return new DistributedLock() {
            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                boolean isLockSuccess = rLock.tryLock(waitTime, leaseTime, unit);
                logger.info("{} get lock result:{}", key, isLockSuccess);
                return isLockSuccess;
            }

            @Override
            public boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException {
                return rLock.tryLock(waitTime, unit);
            }

            @Override
            public boolean tryLock() throws InterruptedException {
                return rLock.tryLock();
            }

            @Override
            public void lock(long leaseTime, TimeUnit unit) {
                rLock.lock(leaseTime, unit);
            }

            @Override
            public void unlock() {
                if (isLocked() && isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
            @Override
            public boolean isLocked() {
                return rLock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return rLock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }
        };
    }
}
