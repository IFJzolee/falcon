package com.falcon.assessment.service;

import com.falcon.assessment.dto.PalindromeTaskDto;
import org.mapstruct.Mapper;

@Mapper
public interface PalindromeTaskMapper {

    PalindromeTask dtoToTask(PalindromeTaskDto task);

    PalindromeTaskDto taskToDto(PalindromeTask entity);

}
