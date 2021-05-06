package com.itheima.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_WorkQueues {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 2. 设置参数
        factory.setHost("192.168.3.26"); // ip 默认值 localhost
        factory.setPort(5672); // 端口 默认值 5672
        factory.setVirtualHost("/itcast"); // 虚拟机 默认值 /
        factory.setUsername("heima"); // 用户名 默认值 guest
        factory.setPassword("heima"); // 密码 默认值 guest
        // 3. 创建连接 Connection
        Connection connection = factory.newConnection();
        // 4. 创建Channel
        Channel channel = connection.createChannel();
        // 5. 创建队列Queue
        /**
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * 参数：
         * queue 队列名称
         * durable 是否持久化，当mq重启之后是否还在
         * exclusive:
         *     1. 是否独占，只能有一个消费者监听这队列
         *     2. 当Connection关闭时，是否删除队列
         * autoDelete 是否自动删除，当没有Consumer时，自动删除掉
         * arguments 参数
         */
        // 如果没有名字叫hello_world的队列，则会创建该队列，如果有则不会创建
        channel.queueDeclare("work_queues", true, false, false, null);
        // 6. 发送消息
        /**
         * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
         * 参数：
         * exchange 交换机名称，简单模式下交换机会使用默认的""
         * routingKey 路由名称
         * props 配置信息
         * body 发送消息数据
         */
        for (int i = 0; i <= 10; i ++) {
            String body = i + "hello rabbitmq!";
            channel.basicPublish("", "work_queues", null, body.getBytes());
        }

        // 7. 释放资源
//        channel.close();
//        connection.close();
    }


}
