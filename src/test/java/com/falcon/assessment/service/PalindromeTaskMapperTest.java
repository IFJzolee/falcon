package com.falcon.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

import com.falcon.assessment.domain.PalindromeTask;
import com.falcon.assessment.dao.PalindromeTaskEntity;
import org.junit.Test;

public class PalindromeTaskMapperTest {

    @Test
    public void taskToEntity() {
        PalindromeTaskMapper mapper = new PalindromeTaskMapperImpl();
        String content = "content";
        String offset = "+02:00";
        OffsetDateTime timestamp = OffsetDateTime.parse("2007-12-03T10:15:30" + offset);
        PalindromeTask task = new PalindromeTask(content, timestamp);

        PalindromeTaskEntity entity = mapper.taskToEntity(task);
        assertThat(entity.getContent()).isEqualTo(content);
        assertThat(entity.getTimestamp()).isEqualTo(timestamp);
        assertThat(entity.getTimestampOffset()).isEqualTo(offset);
    }

}
