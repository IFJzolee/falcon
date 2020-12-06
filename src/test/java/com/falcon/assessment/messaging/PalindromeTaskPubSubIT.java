package com.falcon.assessment.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.falcon.assessment.config.ObjectMapperConfiguration;
import com.falcon.assessment.config.PubSubConfiguration;
import com.falcon.assessment.config.RedisConnectionConfiguration;
import com.falcon.assessment.domain.PalindromeTask;
import com.falcon.assessment.service.PalindromeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import({
    PubSubConfiguration.class,
    RedisConnectionConfiguration.class,
    PalindromeTaskListener.class,
    PalindromeTaskPublisher.class,
    ObjectMapperConfiguration.class
})
@DataRedisTest
public class PalindromeTaskPubSubIT {

    @Autowired
    private PalindromeTaskPublisher taskPublisher;
    @MockBean
    private PalindromeService palindromeServiceMock;

    @Test
    public void pubSubIntegration() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(palindromeServiceMock).processTask(any());
        PalindromeTask task = new PalindromeTask("content", OffsetDateTime.parse("2007-12-03T10:15:30+02:00"));

        taskPublisher.publish(task);
        if (!latch.await(2, TimeUnit.SECONDS)) {
            fail("Latch timed out");
        }

        var captor = ArgumentCaptor.forClass(PalindromeTask.class);
        verify(palindromeServiceMock).processTask(captor.capture());
        assertThat(captor.getValue()).isEqualTo(task);
    }

}
