package com.zk.test;

import com.zk.annotation.MQProducer;
import com.zk.base.AbstractMQProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>ClassName: TestProducer</p>
 * <p>Description: </p>
 * <p>Company: 爱用科技有限公司</p>
 *
 * @author zhikang.du
 * @version v1.0
 * @date: 2020/1/15
 * @since JDK 1.8
 */
@Component
public class TestProducer extends AbstractMQProducer {

    @Override
    public void doAfterSyncSend(Message message, SendResult sendResult) {

    }
}
