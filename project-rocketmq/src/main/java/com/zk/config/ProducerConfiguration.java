package com.zk.config;

import com.zk.annotation.MQProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * <p>ClassName: ProducerConfiguration</p>
 * <p>Description: </p>
 * <p>Company: 爱用科技有限公司</p>
 *
 * @author zhikang.du
 * @version v1.0
 * @date: 2020/1/15
 * @since JDK 1.8
 */
@Configuration
public class ProducerConfiguration{

    private static final String IYONG_GROUP_A = "IYONG_GROUP_A";

    private static final Integer SEED_MSG_TIME_OUT = 3000;

    private static final Boolean TRACE_ENABLED = Boolean.TRUE;

    private static final Boolean VIP_CHANNEL_ENABLED = Boolean.TRUE;

    /**
     * @Param 
     * @Description The producer service state not OK, maybe started once, RUNNING， 用注解的形式不用自己去启动 producer 服务
     * @Author zhikang.du
     * @Date 2020/1/15
     * @return 
     **/
    @Bean
    public DefaultMQProducer exposeProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(IYONG_GROUP_A);
        producer.setNamesrvAddr("192.168.4.84:9876");
        producer.setSendMsgTimeout(SEED_MSG_TIME_OUT);
        return producer;

    }

}
