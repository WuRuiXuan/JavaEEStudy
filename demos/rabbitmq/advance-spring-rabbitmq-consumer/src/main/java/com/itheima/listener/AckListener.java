package com.itheima.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Consumer Ack机制：
 * 1. 设置手动签收：rabbit:listener-container中设置acknowledge="manual"
 * 2. 让监听器类实现ChannelAwareMessageListener接口
 * 3. 如果消息处理成功，则调用channel的basicAck()签收
 * 4. 如果消息处理失败，则调用channel的basicNack()拒绝签收，broker重新发送给consumer
 */
@Component
public class AckListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Thread.sleep(2000);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 1. 接收转换消息
            System.out.println(new String(message.getBody()));
            // 2. 处理业务逻辑
            System.out.println("处理业务逻辑...");
//            int i = 1/0;
            // 3. 手动签收
            // long deliveryTag, boolean multiple - 是否签收多条消息
            channel.basicAck(deliveryTag, true);
        }
        catch (Exception e) {
            // 4. 拒绝签收
            // 第三个参数 requeue - 是否重回队列，如果设置为true，则消息重新回到queue，broker会重新发送该消息给消费端
            channel.basicNack(deliveryTag, true, true);
//            channel.basicReject(deliveryTag, true);
        }
    }
}
