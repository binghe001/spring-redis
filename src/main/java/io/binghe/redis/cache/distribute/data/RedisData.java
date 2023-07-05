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
package io.binghe.redis.cache.distribute.data;

import java.time.LocalDateTime;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description 缓存到Redis中的数据，主要配合使用数据的逻辑过期
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
public class RedisData {
    //实际业务数据
    private Object data;
    //过期时间点
    private LocalDateTime expireTime;

    public RedisData() {
    }

    public RedisData(Object data, LocalDateTime expireTime) {
        this.data = data;
        this.expireTime = expireTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
