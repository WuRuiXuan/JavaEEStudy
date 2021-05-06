package com.itheima;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

public class JedisTest {

    @Test
    public void testJedis() {
        // 连接redis
        Jedis jedis = new Jedis("localhost", 6379);
        // 操作redis
        jedis.set("name", "itheima");
        System.out.println(jedis.get("name"));
        // 关闭连接
        jedis.close();
    }

    @Test
    public void testList() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.lpush("list1", "a", "b", "c");
        jedis.rpush("list1", "x");
        List<String> list1 = jedis.lrange("list1", 0, -1);
        for (String s : list1) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println(jedis.llen("list1"));
        jedis.close();
    }

    @Test
    public void testHash() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.hset("hash1", "f1", "v1");
        jedis.hset("hash1", "f2", "v2");
        jedis.hset("hash1", "f3", "v3");
        Map<String, String> hash1 = jedis.hgetAll("hash1");
        System.out.println(hash1);
        System.out.println();
        System.out.println(jedis.hlen("hash1"));
        jedis.close();
    }
}
