package com.nttdata.beca.service;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.InterviewDTO;

import java.util.Date;
import java.util.List;


public interface InterviewService extends GenericService<InterviewDTO, Long> {
    List<InterviewDTO> findInterviewByCurrentSession(String sessionStatus);

    List<InterviewDTO> saveInterviews(List<Date> dates);

    InterviewDTO saveInterview(Date date);

    MessageResponse deleteInterviewById(Long id);

    InterviewDTO findInterviewById(Long id);

    List<Integer> findInterviewIdsByDate(Date date);



}
