package com.itheima.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * Consumer 限流机制：
 * 1. 确保ack机制为手动确认
 * 2. rabbit:listener-container中设置prefetch="[消费端每次从MQ拉取消息的数量，直到手动确认消费完毕后，才会继续拉取]"
 */
@Component
public class QosListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Thread.sleep(1000);
        // 1. 获取消息
        System.out.println(new String(message.getBody()));
        // 2. 处理业务逻辑

        // 3. 签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }
}
