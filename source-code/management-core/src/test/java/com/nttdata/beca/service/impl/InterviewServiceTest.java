package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.InterviewRepository;
import com.nttdata.beca.service.impl.InterviewServiceImpl;
import com.nttdata.beca.transformer.InterviewTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {
    @Mock
    private InterviewRepository interviewRepository;

    @InjectMocks
    InterviewTransformer interviewTransformer;

    @InjectMocks
    private InterviewServiceImpl interviewService;
private Interview interview;
private  InterviewDTO interviewDTO;
private RecruiterDTO recruiterDTO;

private PasswordEncoder encoder;

    @Before
    public  void  setUp(){
        MockitoAnnotations.initMocks(this);

        recruiterDTO=RecruiterDTO.builder()
                .recruiterId(1L)
                .email("rababbbbbb@gmail,com")
                .username("rababmehdy")
                .firstName("rabab")
                .lastName("elmehdy")
                .phoneNumber("0654407151")
                .picture("pic.png")
                .build();
        /*interviewDTO=InterviewDTO.builder()
                .interviewId(1L)
                .time(new Time(9,0,0))
                .date(new Date(2022,8,24))
                .session(SessionDTO.builder()
                        .sessionId(1L)
                        .sessionName("session1")
                        .sessionDate(new Date(2022,8,4))
                        .duration(3)
                        .sessionStatus("current")
                        .interviewNumber(3)
                        .technology("java")
                        .recruiter(null)
                        .build())
                .recruiter(recruiterDTO)
                .build();*/

        interview=Interview.builder()
                .interviewId(1L)
                .date(new Date(2022,8,24))
                .session(Session.builder()
                        .sessionId(1l)
                        .build())
                .build();
        interviewDTO=interviewTransformer.toDTO(interview);


    }
    @DisplayName("testing Save method ")
    @Test
    public void  Save_Test(){

        Mockito.when(interviewRepository.save(interview)).thenReturn(interview);

        InterviewDTO saveInterview= interviewService.save(interviewDTO);

        verify(interviewRepository, times(1)).save(any());
    }
    @DisplayName("testing findInterviewById method")
    @Test
    public void findInterviewById_Test(){
        long Id=  1l;

        Mockito.when(interviewRepository.findById(Id)).thenReturn(Optional.ofNullable(interview));

        Optional<InterviewDTO> findInterviewById= Optional.ofNullable(interviewService.findInterviewById(Id));
        assertThat(findInterviewById).isNotNull();
    }
}
