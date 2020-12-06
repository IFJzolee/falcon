package com.falcon.assessment.messaging;

import static com.falcon.assessment.config.WebSocketConfiguration.TOPIC_DESTINATION_PREFIX;

public class WsTopicNameFactory {

    public static String websocketTopic(String topicName) {
        return TOPIC_DESTINATION_PREFIX + topicName;
    }

}
