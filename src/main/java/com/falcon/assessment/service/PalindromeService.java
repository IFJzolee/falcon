package com.falcon.assessment.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.falcon.assessment.dao.PalindromeRepository;
import com.falcon.assessment.dao.PalindromeTaskEntity;
import com.falcon.assessment.domain.CalculatedPalindrome;
import com.falcon.assessment.domain.PalindromeTask;
import com.falcon.assessment.messaging.UIConnector;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PalindromeService {

    private final PalindromeRepository palindromeRepository;
    private final PalindromeTaskMapper taskMapper;
    private final UIConnector uiConnector;
    private final PalindromeCalculator palindromeCalculator;

    public void processTask(PalindromeTask task) {
        /* TODO generate unique ID on publish side and ignore db conflict
        inorder to prevent multiple inserts in a multi instance env.
        Could be easily avoided if the task was stored at the time of rest reception.
        */
        PalindromeTaskEntity entity = taskMapper.taskToEntity(task);
        palindromeRepository.save(entity);

        uiConnector.broadcastPalindromeTask(task);
    }

    public List<CalculatedPalindrome> getCalculatedPalindromes() {
        var entities = new ArrayList<PalindromeTaskEntity>();
        for (PalindromeTaskEntity entity : palindromeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))) {
            entities.add(entity);
        }

        // TODO It's ineffective to calculate it on the fly, but it is in the requirement...
        return entities.stream()
            .map(taskMapper::entityToTask)
            .map(palindromeCalculator::calculate)
            .collect(toList());
    }

}
