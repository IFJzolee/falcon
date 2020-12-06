package com.falcon.assessment.service;

import com.falcon.assessment.dao.PalindromeRepository;
import com.falcon.assessment.dao.PalindromeTaskEntity;
import com.falcon.assessment.domain.PalindromeTask;
import com.falcon.assessment.messaging.UIConnector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PalindromeService {

    private final PalindromeRepository palindromeRepository;
    private final PalindromeTaskMapper taskMapper;
    private final UIConnector uiConnector;

    public void processTask(PalindromeTask task) {
        PalindromeTaskEntity entity = taskMapper.taskToEntity(task);
        palindromeRepository.save(entity);

        uiConnector.broadcastPalindromeTask(task);
    }

}
