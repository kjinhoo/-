package com.jinhook.res_project.dao;

import redis.clients.jedis.Jedis;

public class RedisHelper {
    public static Jedis getRedisInstance(){
        return new Jedis(  DbProperties.getInstance().getProperty("redis.host"),
                Integer.parseInt(DbProperties.getInstance().getProperty("redis.port"))   );
    }
}
