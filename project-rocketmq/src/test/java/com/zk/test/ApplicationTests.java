package com.zk.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

    private static final String TOPIC = "topic-zk";


    @Resource
    private TestProducer testProducer;

    @Resource
    private DefaultMQProducer producer;

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
    }

    /**
     * @Param
     * @Description 异步发送消息,异步传输通常用于响应时间敏感的业务场景。
     * @Author zhikang.du
     * @Date 2020/1/16
     * @return
     **/
    @Test
    public void asyncProducer() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i <10; i++) {
            final int index = i;
            Message msg = new Message("topic-zk" /* Topic */,
                    "TagA" /* Tag */, "OrderID188",
                    ("Hello RocketMQ 1-17....." + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(10000);
    }

    /**
     * @Param
     * @Description 顺序消息,FIFO order.
     * @Author zhikang.du
     * @Date 2020/1/18
     * @return
     **/
    @Test
    public void orderProducer() throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String [] tags = new String[] {"TagA", "TagB", "TagC", "TagD"};
        for (int i = 0; i < 50; i++) {
            int orderId = i % 5;
            Message msg = new Message("topic-zk" /* Topic */,
                    tags[ i % tags.length] /* Tag */, "key" + i,
                    ("Hello RocketMQ 1-18-926....." + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message message, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);

                }
            }, orderId);
            System.out.printf("%s%n", sendResult);
        }
    }

    /**
     * @Param
     * @Description 订阅消息
     * @Author zhikang.du
     * @Date 2020/1/18
     * @return
     **/
    @Test
    public void subscriptionMessage() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("IYONG_GROUP_A");
        // 程序第一次启动从消息队列头获取数据
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅TOPIC下Tag为TagA || TagC || TagD的消息
        consumer.subscribe(TOPIC, "TagA || TagC || TagD");
        // 注册消费的监听
        // MessageListenerConcurrently 拉取到消息直接消费
        // MessageListenerOrderly顺序消费是直接从processQueue，里面取，processQueue里面做了临时存储,但是，并发消费没有做临时存储
        consumer.registerMessageListener(new MessageListenerOrderly() {
            AtomicLong consumeTimes = new AtomicLong(0);
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                // 是否自动提交
                context.setAutoCommit(false);
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                this.consumeTimes.incrementAndGet();

                return null;
            }
        });
    }


}
