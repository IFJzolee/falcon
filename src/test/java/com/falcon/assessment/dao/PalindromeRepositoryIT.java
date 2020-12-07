package com.falcon.assessment.dao;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.OffsetDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class PalindromeRepositoryIT {

    @Autowired
    private PalindromeRepository palindromeRepo;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void savesAndGet() {
        var date = OffsetDateTime.parse("2007-12-03T10:15:30+02:00");
        var expectedUtcDate = date.withOffsetSameInstant(UTC);
        var palindromeTask = PalindromeTaskEntity.builder()
            .content("content")
            .timestamp(date)
            .build();

        PalindromeTaskEntity saved = palindromeRepo.save(palindromeTask);
        flushAndClearPersistenceCtx();

        assertThat(saved.getId()).isNotNull();
        saved = palindromeRepo.findById(saved.getId()).get();
        assertThat(saved.getContent()).isEqualTo(palindromeTask.getContent());
        assertThat(saved.getTimestamp()).isEqualTo(expectedUtcDate);
    }

    private void flushAndClearPersistenceCtx() {
        entityManager.flush();
        entityManager.clear();
    }

}
