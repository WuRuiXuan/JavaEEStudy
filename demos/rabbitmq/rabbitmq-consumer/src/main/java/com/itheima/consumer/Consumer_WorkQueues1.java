package com.itheima.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列模式：多个消费端消费一个队列，比简单模式多了消费端
 */
public class Consumer_WorkQueues1 {
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
        // 6. 接收消息
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
            }
        };

        channel.basicConsume("work_queues", true, consumer);

        // 7. 保持消息监听，不要释放资源
    }
}
