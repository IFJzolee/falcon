package com.falcon.assessment.service;

import com.falcon.assessment.dao.PalindromeTaskEntity;
import com.falcon.assessment.dto.PalindromeTask;
import org.mapstruct.Mapper;

@Mapper
public interface PalindromeTaskMapper {

    PalindromeTaskEntity taskToEntity(PalindromeTask task);

    PalindromeTask entityToTask(PalindromeTaskEntity entity);

}
