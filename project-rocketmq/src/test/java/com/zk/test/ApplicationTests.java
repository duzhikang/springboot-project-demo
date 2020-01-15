package com.zk.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>ClassName: ApplicationTests</p>
 * <p>Description: </p>
 * <p>Company: 爱用科技有限公司</p>
 *
 * @author zhikang.du
 * @version v1.0
 * @date: 2020/1/15
 * @since JDK 1.8
 */
@SpringBootTest
public class ApplicationTests {


    @Resource
    private TestProducer testProducer;

    @Test
    public void seedMsg() throws UnsupportedEncodingException {
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("topic-zk" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ ....." + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            testProducer.syncSend(msg);
        }
    }

    public static void main(String[] args) throws MQClientException, InterruptedException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("IYONG_GROUP_A");

        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.4.84:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe("topic-zk", "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("消费者消费数据:"+new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //Launch the consumer instance.
        consumer.start();
        System.out.println("1111111111111111111111");
        System.out.printf("Consumer Started.%n");
        Thread.sleep(10000L);
    }


}
