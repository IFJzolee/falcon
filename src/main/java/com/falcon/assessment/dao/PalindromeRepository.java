package com.falcon.assessment.dao;

import com.falcon.assessment.service.PalindromeTask;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PalindromeRepository extends PagingAndSortingRepository<PalindromeTask, Long> {
}
