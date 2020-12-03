package com.falcon.assessment.service;

import com.falcon.assessment.domain.PalindromeTask;
import com.falcon.assessment.dao.PalindromeTaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PalindromeTaskMapper {

    @Mapping(target = "timestampOffset", expression = "java(task.getTimestamp().getOffset().toString())")
    PalindromeTaskEntity taskToEntity(PalindromeTask task);

}
