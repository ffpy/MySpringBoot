package com.ganguomob.dev.myspringboot.cachehelper.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient provideRedissonClient() {
        Config config = new Config();
        config.setNettyThreads(0);
        config.setThreads(0);
        config.setCodec(new JsonJacksonCodec(objectMapper));

        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress("redis://" + host + ":" + port)
                .setTimeout(5000)
                .setRetryAttempts(10)
                .setRetryInterval(3000);
        if (StringUtils.isNotEmpty(password)) {
            serverConfig.setPassword(password);
        }

        return Redisson.create(config);
    }
}
