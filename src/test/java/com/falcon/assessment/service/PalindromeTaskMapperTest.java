package com.falcon.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

import com.falcon.assessment.dao.PalindromeTaskEntity;
import com.falcon.assessment.dto.PalindromeTask;
import org.junit.Test;

public class PalindromeTaskMapperTest {

    private PalindromeTaskMapper mapper = new PalindromeTaskMapperImpl();

    @Test
    public void taskToEntity() {

        String content = "content";
        String offset = "+02:00";
        OffsetDateTime timestamp = OffsetDateTime.parse("2007-12-03T10:15:30" + offset);
        PalindromeTask task = new PalindromeTask(content, timestamp);

        PalindromeTaskEntity entity = mapper.taskToEntity(task);
        assertThat(entity.getContent()).isEqualTo(content);
        assertThat(entity.getTimestamp()).isEqualTo(timestamp);
        assertThat(entity.getTimestampOffset()).isEqualTo(offset);
    }

    @Test
    public void entityToTask() {
        String content = "content";
        String offset = "+02:00";
        OffsetDateTime timestamp = OffsetDateTime.parse("2007-12-03T10:15:30" + offset);
        var entity = PalindromeTaskEntity.builder()
            .content(content)
            .timestamp(timestamp)
            .timestampOffset(offset)
            .build();

        PalindromeTask task = mapper.entityToTask(entity);

        assertThat(task.getContent()).isEqualTo(content);
        assertThat(task.getTimestamp()).isEqualTo(timestamp);
    }

}
