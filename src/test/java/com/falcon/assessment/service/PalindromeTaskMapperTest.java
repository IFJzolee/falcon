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
        PalindromeTask task = new PalindromeTask("content", OffsetDateTime.parse("2007-12-03T10:15:30+02:00"));

        PalindromeTaskEntity entity = mapper.taskToEntity(task);
        assertThat(entity.getContent()).isEqualTo(task.getContent());
        assertThat(entity.getTimestamp()).isEqualTo(task.getTimestamp());
    }

    @Test
    public void entityToTask() {
        var entity = PalindromeTaskEntity.builder()
            .content("content")
            .timestamp(OffsetDateTime.parse("2007-12-03T10:15:30+02:00"))
            .build();

        PalindromeTask task = mapper.entityToTask(entity);

        assertThat(task.getContent()).isEqualTo(entity.getContent());
        assertThat(task.getTimestamp()).isEqualTo(entity.getTimestamp());
    }

}
