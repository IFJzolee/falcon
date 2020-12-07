package com.falcon.assessment.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.falcon.assessment.dao.PalindromeRepository;
import com.falcon.assessment.dto.CalculatedPalindromeDto;
import com.falcon.assessment.dto.PalindromeTaskDto;
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

    public void processTask(PalindromeTaskDto taskDto) {
        /* TODO generate unique ID on publish side and ignore db conflict
        inorder to prevent multiple inserts in a multi instance env.
        Could be easily avoided if the task was stored at the time of rest reception.
        */
        PalindromeTask task = taskMapper.dtoToTask(taskDto);
        palindromeRepository.save(task);

        uiConnector.broadcastPalindromeTask(taskDto);
    }

    public List<CalculatedPalindromeDto> getCalculatedPalindromes() {
        var tasks = new ArrayList<PalindromeTask>();
        for (PalindromeTask task : palindromeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))) {
            tasks.add(task);
        }

        // TODO It's ineffective to calculate it on the fly, but it is in the requirement...
        return tasks.stream()
            .map(palindromeCalculator::calculate)
            .collect(toList());
    }

}
