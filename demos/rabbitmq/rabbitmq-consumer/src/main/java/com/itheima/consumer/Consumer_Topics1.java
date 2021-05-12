package com.itheima.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 通配符模式：交换机使用通配符分发消息到不同的队列中，每个队列由不同的消费端消费，与路由模式相比，使用通配符分发而不是路由
 */
public class Consumer_Topics1 {
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
        // 5. 接收消息
        /**
         * basicConsume(String queue, boolean autoAck, Consumer callback)
         * 参数：
         * queue 队列名称
         * autoAck 是否自动确认
         * callback 回调对象
         */
        Consumer consumer = new DefaultConsumer(channel) {
            /**
             * 回调方法，当收到消息后会自动执行该方法
             * @param consumerTag 标识
             * @param envelope 获取一些信息，如交换机、路由key...
             * @param properties 配置信息
             * @param body 数据
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                System.out.println("consumerTag: " + consumerTag);
//                System.out.println("Exchange: " + envelope.getExchange());
//                System.out.println("RoutingKey: " + envelope.getRoutingKey());
//                System.out.println("properties: " + properties);
                System.out.println("body: " + new String(body));
                System.out.println("将日志信息保存到数据库...");
            }
        };

        channel.basicConsume("test_direct_queue1", true, consumer);

        // 6. 保持消息监听，不要释放资源
    }
}
