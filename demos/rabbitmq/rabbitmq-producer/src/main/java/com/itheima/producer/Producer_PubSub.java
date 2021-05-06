package com.itheima.producer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_PubSub {
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
        // 5. 创建交换机
        /**
         * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
         * exchange 交换机名称
         * type 交换机类型：
         *              DIRECT - 定向
         *              FANOUT - 扇形（广播），发送消息到每一个与之绑定队列
         *              TOPIC - 通配符的方式
         *              HEADERS - 参数匹配
         * durable 是否持久化
         * autoDelete 是否自动删除
         * internal 内部使用，一般为false
         * arguments 参数
         */
        String exchangeName = "test_fanout";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, false, null);
        // 6. 创建队列
        String queue1Name = "test_fanout_queue1";
        String queue2Name = "test_fanout_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);
        // 7. 绑定队列和交换机
        /**
         * queueBind(String queue, String exchange, String routingKey)
         * queue - 队列名称
         * exchange - 交换机名称
         * routingKey - 路由键，绑定规则：如果交换机类型为fanout，routingKey设置为""
         */
        channel.queueBind(queue1Name, exchangeName, "");
        channel.queueBind(queue2Name, exchangeName, "");
        // 8. 发送消息
        String body = "日志信息：张三调用了findAll方法...日志级别：info";
        channel.basicPublish(exchangeName, "", null, body.getBytes());
        // 9. 释放资源
        channel.close();
        connection.close();
    }


}
