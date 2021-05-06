package com.itheima;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认模式：
     * 步骤：
     * 1. 开启确认模式：rabbit:connection-factory中设置publisher-confirms="true"
     * 2. 在rabbitTemplate中定义ConfirmCallback回调函数
     */
    @Test
    public void testConfirm() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关配置信息
             * @param ack exchange交换机是否成功收到消息
             * @param cause 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirm方法被执行了...");
                if (ack) {
                    System.out.println("接收消息成功");
                }
                else {
                    System.out.println("接收消息失败：" + cause);
                    // 做一些处理，让消息再次发送
                }
            }
        });
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
    }

    /**
     * 回退模式：当消息发送给exchange后，exchange路由到queue失败时才会执行ReturnCallback
     * 步骤：
     * 1. 开启回退模式：rabbit:connection-factory中设置publisher-returns="true"
     * 2. 在rabbitTemplate中定义ReturnCallback回调函数
     * 3. 设置exchange处理消息的模式：
     *                          1. 如果消息没有路由到Queue，则丢弃消息（默认）
     *                          2. 如果消息没有路由到Queue，则返回给消息发送方ReturnCallback
     */
    @Test
    public void testReturn() {
        // 设置交换机处理消息失败的模式
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             *
             * @param message 消息对象
             * @param replyCode 错误码
             * @param replyText 错误信息
             * @param exchange 交换机
             * @param routingKey 路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("return方法被执行了...");
                System.out.println(new String(message.getBody()));
                System.out.println(replyCode);
                System.out.println(replyText);
                System.out.println(exchange);
                System.out.println(routingKey);
                // 处理
            }
        });
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm111", "message confirm...");
    }

    @Test
    public void testSend() {
        for (int i = 0; i < 10; i ++) {
            rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
        }
    }

    /**
     * TTL：过期时间
     * 1. 队列统一过期
     * 2. 消息单独过期
     * 如果设置了消息过期时间和队列过期时间，以时间短的为准
     * 队列过期后，会将队列所有消息全部移除
     * 只有消息在队列顶端（将要被消费），才会判断其是否过期（移除掉）
     */
    @Test
    public void testTTL1() {
        for (int i = 0; i < 10; i ++) {
            rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...");
        }
    }

    @Test
    public void testTTL2() {
        // 消息后处理对象，设置一些消息的参数信息
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {

            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 1. 设置message的信息
                message.getMessageProperties().setExpiration("5000"); // 消息的过期时间
                // 2. 返回该消息
                return message;
            }
        };
        // 消息单独过期
//        rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...", messagePostProcessor);

        for (int i = 0; i < 10; i ++) {
            if (i == 5) {
                // 会过期的消息
                rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...", messagePostProcessor);
            } else {
                // 不过期的消息
                rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...");
            }
        }
    }

    /**
     * 发送测试死信消息：
     * 1. 过期时间
     * 2. 长度限制
     * 3. 消息拒收
     */
    @Test
    public void testDLX() {
        // 测试过期时间
//        rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.haha", "我是一条消息，我会死吗？");
        // 测试长度限制
//        for (int i = 0; i < 20; i ++) {
//            rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.haha", "我是一条消息，我会死吗？");
//        }
        // 测试消息拒收
        rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.haha", "我是一条消息，我会死吗？");
    }

    @Test
    public void testDelay() throws InterruptedException {
        // 订单系统下单成功后发送消息
        rabbitTemplate.convertAndSend("order_exchange", "order.msg", "订单信息：id=1, time=2021-03-23 17:18");
        // 打印倒计时10秒
        for (int i = 10; i > 0; i --) {
            System.out.println(i + "...");
            Thread.sleep(1000);
        }
    }
}
