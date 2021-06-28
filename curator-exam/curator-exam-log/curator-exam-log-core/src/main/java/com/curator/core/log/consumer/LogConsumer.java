package com.curator.core.log.consumer;

import com.curator.api.log.pojo.vo.info.InfoRequestLogInfo;
import com.curator.common.util.Help;
import com.curator.common.util.JsonUtil;
import com.curator.core.log.service.InfoRequestLogService;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志消息消费
 *
 * @author Jun
 * @date 2021/4/17
 */
@Component
@RocketMQMessageListener(consumerGroup = "LogConsumerGroup", topic = "logTopic", consumeMode = ConsumeMode.CONCURRENTLY)
public class LogConsumer implements RocketMQListener<String> {

    private final Logger logger = LoggerFactory.getLogger(LogConsumer.class);

    @Autowired
    private InfoRequestLogService requestLogService;

    @Override
    public void onMessage(String message) {
        if(Help.isNotEmpty(message)) {
            InfoRequestLogInfo logInfo = JsonUtil.string2Obj(message, InfoRequestLogInfo.class);
            requestLogService.saveInfoRequestLog(logInfo);
        } else {
            logger.info("消息为空，不做出保存操作！");
        }
    }
}
