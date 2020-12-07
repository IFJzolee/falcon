package com.falcon.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

import com.falcon.assessment.dto.PalindromeTaskDto;
import org.junit.Test;

public class PalindromeTaskMapperTest {

    private PalindromeTaskMapper mapper = new PalindromeTaskMapperImpl();

    @Test
    public void taskToEntity() {
        PalindromeTaskDto task = new PalindromeTaskDto("content", OffsetDateTime.parse("2007-12-03T10:15:30+02:00"));

        PalindromeTask entity = mapper.dtoToTask(task);
        assertThat(entity.getContent()).isEqualTo(task.getContent());
        assertThat(entity.getTimestamp()).isEqualTo(task.getTimestamp());
    }

    @Test
    public void entityToTask() {
        var entity = PalindromeTask.builder()
            .content("content")
            .timestamp(OffsetDateTime.parse("2007-12-03T10:15:30+02:00"))
            .build();

        PalindromeTaskDto task = mapper.taskToDto(entity);

        assertThat(task.getContent()).isEqualTo(entity.getContent());
        assertThat(task.getTimestamp()).isEqualTo(entity.getTimestamp());
    }

}
