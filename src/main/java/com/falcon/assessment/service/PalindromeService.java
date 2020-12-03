package com.falcon.assessment.service;

import com.falcon.assessment.domain.PalindromeTask;
import com.falcon.assessment.dao.PalindromeRepository;
import com.falcon.assessment.dao.PalindromeTaskEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PalindromeService {

    private final PalindromeRepository palindromeRepository;
    private final PalindromeTaskMapper taskMapper;

    public void processTask(PalindromeTask task) {
        PalindromeTaskEntity entity = taskMapper.taskToEntity(task);
        palindromeRepository.save(entity);
    }

}
