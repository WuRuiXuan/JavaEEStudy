package com.itheima.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisUtils {
    static JedisPool jp;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("redis");
        String host = rb.getString("redis.host");
        int port = Integer.parseInt(rb.getString("redis.port"));
        int maxTotal = Integer.parseInt(rb.getString("redis.maxTotal"));
        int maxIdle = Integer.parseInt(rb.getString("redis.maxIdle"));

        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(maxTotal);
        jpc.setMaxIdle(maxIdle);
        jp = new JedisPool(jpc, host, port);
    }

    public static Jedis getJedis() {
        return jp.getResource();
    }

    public static void main(String[] args) {
        JedisUtils.getJedis();
    }
}
