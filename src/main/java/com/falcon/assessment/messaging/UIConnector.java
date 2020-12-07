package com.falcon.assessment.messaging;

import static com.falcon.assessment.messaging.WsTopicNameFactory.websocketTopic;

import com.falcon.assessment.dto.PalindromeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UIConnector {

    private final SimpMessagingTemplate messagingTemplate;
    private final String topic;

    public UIConnector(SimpMessagingTemplate messagingTemplate, @Value("${ui.palindrome-task.topic}") String topic) {
        this.messagingTemplate = messagingTemplate;
        this.topic = websocketTopic(topic);
    }

    public void broadcastPalindromeTask(PalindromeTask task) {
        log.info("Broadcasting on topic `{}`, message: {}", topic, task);
        messagingTemplate.convertAndSend(topic, task);
    }

}
