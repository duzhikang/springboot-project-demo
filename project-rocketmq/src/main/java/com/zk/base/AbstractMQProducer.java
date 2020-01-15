package com.zk.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>ClassName: AbstractMQProducer</p>
 * <p>Description: </p>
 * <p>Company: 爱用科技有限公司</p>
 *
 * @author zhikang.du
 * @version v1.0
 * @date: 2020/1/15
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractMQProducer {

    @Autowired
    private DefaultMQProducer producer;

    public void setProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }

    public void syncSend(Message message) {
        try {
            SendResult sendResult = producer.send(message);
            log.debug("send rocketmq message ,messageId : ", sendResult.getMsgId());
            this.doAfterSyncSend(message, sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Param message  发送消息体
     * @Param sendResult  发送结果
     * @Description 重写此方法处理发送后的逻辑
     * @Author zhikang.du
     * @Date 2019/12/12
     * @return
     **/
    public abstract void doAfterSyncSend(Message message, SendResult sendResult);

}
