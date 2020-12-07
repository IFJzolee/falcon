package com.falcon.assessment.service;

import static com.falcon.assessment.messaging.WsTopicNameFactory.websocketTopic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.falcon.assessment.dao.PalindromeRepository;
import com.falcon.assessment.dao.PalindromeTaskEntity;
import com.falcon.assessment.domain.CalculatedPalindrome;
import com.falcon.assessment.domain.PalindromeTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(statements = "TRUNCATE TABLE palindrome;", executionPhase = AFTER_TEST_METHOD)
public class PalindromeServiceIT {

    public static final int LATCH_TIMEOUT_SECONDS = 20;
    @Autowired
    private PalindromeService palindromeService;
    @Autowired
    private PalindromeRepository palindromeRepo;
    @Autowired
    private PalindromeTaskMapper taskMapper;
    @Autowired
    private MessageConverter messageConverter;
    @Value("${ui.palindrome-task.topic}")
    private String topic;
    @LocalServerPort
    private int port;

    @Test
    public void processTask_storesTaskAndPublishesToUIClients() throws InterruptedException {
        var initLatch = new CountDownLatch(1);
        var finishLatch = new CountDownLatch(1);
        var stompHandler = new PalindromeTaskStompConsumer(initLatch, finishLatch, websocketTopic(topic));

        var wsClient = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(wsClient);
        stompClient.setMessageConverter(messageConverter);
        stompClient.connect(String.format("ws://localhost:%d/connect", port),
            stompHandler);

        var dateString = "2007-12-03T10:15:30+02:00";
        var date = OffsetDateTime.parse(dateString);
        var content = "content";
        var task = new PalindromeTask(content, date);
        if (!initLatch.await(LATCH_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            fail("Init latch timed out");
        }

        palindromeService.processTask(task);

        PalindromeTaskEntity taskEntity = palindromeRepo.findAll().iterator().next();
        PalindromeTask actualTask = taskMapper.entityToTask(taskEntity);
        assertThat(actualTask).isEqualTo(task);

        if (!finishLatch.await(LATCH_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            fail("Finish latch timed out");
        }
        assertThat(stompHandler.getResult()).isEqualTo(task);
    }

    @Test
    public void getCalculatedPalindromes_emptyResult() throws InterruptedException {
        assertThat(palindromeService.getCalculatedPalindromes()).isEmpty();
    }

    @Test
    public void getCalculatedPalindromes() throws InterruptedException {
        var task1 = new PalindromeTask("aba", OffsetDateTime.now());
        var task2 = new PalindromeTask("a", OffsetDateTime.now());
        palindromeService.processTask(task1);
        palindromeService.processTask(task2);
        var expectedCalcedPalindrome1 = new CalculatedPalindrome(task1.getContent(), task1.getTimestamp(), 3);
        var expectedCalcedPalindrome2 = new CalculatedPalindrome(task2.getContent(), task2.getTimestamp(), 1);

        List<CalculatedPalindrome> actualCalculatedPalindromes = palindromeService.getCalculatedPalindromes();

        assertThat(actualCalculatedPalindromes).contains(expectedCalcedPalindrome1, expectedCalcedPalindrome2);
    }

    @Slf4j
    private static class PalindromeTaskStompConsumer extends StompSessionHandlerAdapter {

        private final CountDownLatch initLatch;
        private final CountDownLatch finishLatch;
        private final String topic;
        private PalindromeTask result;

        private PalindromeTaskStompConsumer(CountDownLatch initLatch, CountDownLatch finishLatch, String topic) {
            this.initLatch = initLatch;
            this.finishLatch = finishLatch;
            this.topic = topic;
        }

        @Override
        public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
            var subs = session.subscribe(topic, new StompFrameHandler() {

                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return PalindromeTask.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    if (payload instanceof PalindromeTask) {
                        result = (PalindromeTask) payload;
                    } else {
                        log.error("Payload has the wrong instance type");
                    }
                    session.disconnect();
                    finishLatch.countDown();
                }
            });
            initLatch.countDown();
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.error("Transport error", exception);
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            log.error("Handling exception", ex);
        }

        public PalindromeTask getResult() {
            return result;
        }

    }

}
