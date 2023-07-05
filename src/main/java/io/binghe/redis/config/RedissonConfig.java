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
package io.binghe.redis.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author binghe(微信 : hacker_binghe)
 * @version 1.0.0
 * @description Redisson配置
 * @github https://github.com/binghe001
 * @copyright 公众号: 冰河技术
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.address}")
    private String redisAddress;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    @Bean(name = "redissonClient")
    @ConditionalOnProperty(name = "redis.arrange.type", havingValue = "single")
    public RedissonClient singleRedissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redisAddress).setDatabase(database);
        if (!StrUtil.isEmpty(password)){
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    @Bean(name = "redissonClient")
    @ConditionalOnProperty(name = "redis.arrange.type", havingValue = "cluster")
    public RedissonClient clusterRedissonClient(){
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        clusterServersConfig.setNodeAddresses(Arrays.asList(redisAddress));
        if (!StrUtil.isEmpty(password)){
            clusterServersConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}
