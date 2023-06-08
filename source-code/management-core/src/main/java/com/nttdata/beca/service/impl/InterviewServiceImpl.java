package com.nttdata.beca.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.CandidateRepository;
import com.nttdata.beca.repository.InterviewRepository;
import com.nttdata.beca.repository.RecruiterRepository;
import com.nttdata.beca.repository.SessionRepository;
import com.nttdata.beca.service.CandidateService;
import com.nttdata.beca.service.InterviewService;
import com.nttdata.beca.transformer.InterviewTransformer;
import com.nttdata.beca.transformer.SessionTransformer;
import com.nttdata.beca.transformer.Transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class InterviewServiceImpl extends GenericServiceImpl<Interview, InterviewDTO, Long>
        implements InterviewService {
    public InterviewServiceImpl() {
        super(trans);
    }

    public static Transformer<Interview, InterviewDTO> trans = new InterviewTransformer();
    Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    CandidateService candidateService;
    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    RecruiterRepository recruiterRepository;

    @Autowired
    SessionRepository sessionRepository;





    @Override
    public List<InterviewDTO> findInterviewByCurrentSession(String sessionStatus) {
        return trans.toDTOList(interviewRepository.findBySession_SessionStatus(sessionStatus));
    }

    @Override
    public MessageResponse deleteInterviewById(Long id) {
        List<CandidateDTO> candidateDTOs = candidateService.findByInterview(id);
        if (candidateDTOs != null) {
            candidateDTOs.forEach(candidate -> {
                candidate.setInterview(null);
                candidate.setTimeOfInterview(null);
                System.out.println(candidate);
                candidateService.save(candidate);
            });
        }
        interviewRepository.deleteById(id);
        return  new MessageResponse("Interview has been deleted successfully");
    }

    @Override
    public InterviewDTO findInterviewById(Long id) {
        return trans.toDTO(interviewRepository.findInterviewByInterviewId(id));
    }
    @Transactional
    @Override
    public List<InterviewDTO> saveInterviews(List<Date> dates) {
        List<InterviewDTO> interviewDTOS = trans.toDTOList(interviewRepository.findBySession_SessionStatus("current"));
        List<InterviewDTO> newInterviews = new ArrayList<>();
        List<Date> existingDates = new ArrayList<>();
        for (InterviewDTO interview : interviewDTOS) {
            existingDates.add(interview.getDate());
        }
        for(Date date : dates) {
            boolean isExistingDate = existingDates.contains(date);
            if (!isExistingDate) {
                InterviewDTO iDTO = saveInterview(date);
                newInterviews.add(iDTO);
                }
            }
        interviewDTOS.addAll(newInterviews);
//        if (!interviewDTOS.isEmpty()) {
//            candidateService.divideCandidates(interviewDTOS);
//        }
        return interviewDTOS;
    }

    @Override
    public InterviewDTO saveInterview(Date date) {
        InterviewDTO interviewDTO = new InterviewDTO();
        SessionDTO sessionDTO = sessionTransformer.toDTO(sessionRepository.findBySessionStatus("current"));
        interviewDTO.setDate(date);
        interviewDTO.setSession(sessionDTO);
        return super.save(interviewDTO);
    }



    public InterviewDTO updateInterview(InterviewDTO interviewDTO) {
       Interview entity= new InterviewTransformer().toEntity(interviewDTO);
       interviewRepository.save(entity);
       return interviewDTO;
    }

    public List<Integer> findInterviewIdsByDate(Date date){
        List<InterviewDTO> interviews = trans.toDTOList(interviewRepository.findByDate(date));
        List<Integer> interviewIds = new ArrayList<>();
        for (InterviewDTO interview : interviews) {
            String session =interview.getSession().getSessionStatus();
            if(session.equals("current")) {
                interviewIds.add(Math.toIntExact(interview.getInterviewId()));
            }
        }
        return interviewIds;
    }
    }






