package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.controller.InterviewController;
import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.service.impl.InterviewServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class InterviewControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    InterviewServiceImpl interviewService;
    @InjectMocks
    InterviewController interviewController;

    private Interview interview;
    private InterviewDTO interviewDTO,interviewDTO_Up;
    private List interviewList,interviewDtoList;
    private PasswordEncoder encoder;
    private java.util.Date interviewDate=  new Date(2022,8,18);

    private long interviewId=1l;
    @Before
    public  void  setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(interviewController).build();
        this.encoder = new BCryptPasswordEncoder();
        interviewDTO=InterviewDTO.builder()
                .interviewId(1L)
                .date(new Date(2022,8,18))
                .session(SessionDTO.builder()
                        .sessionId(1L)
                        .sessionName("session1")
                        .sessionDate(new Date(2022,8,4))
                        .admittedNumber(10)
                        .sessionStatus("current")
                        .dayInterviewNumber(3)
                        .technology("java")
                        .recruiter(null)
                        .build())
                .build();

        interviewDTO_Up=InterviewDTO.builder()
                .date(new Date(2022,8,28))
                .session(SessionDTO.builder()
                        .sessionId(1L)
                        .sessionName("session1")
                        .sessionDate(new Date(2022,8,4))
                        .admittedNumber(10)
                        .sessionStatus("current")
                        .dayInterviewNumber(3)
                        .technology("java")
                        .recruiter(null)
                        .build())
                .build();

        interviewDtoList=new ArrayList<>();
        interviewDtoList.add(interviewDTO);
    }
    @Test
    public void get_AllInterviews_Test() throws Exception {

        //given
        given(interviewService.findAll()).willReturn(interviewDtoList);
        //when
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/interview/all"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(interviewDtoList.size())));
    }
//    @Test
//    public void getInterviewByDate() throws Exception{
//
//        given(interviewService.findInterviewByDate((any()))).willReturn(interviewDtoList);
//        ResultActions response = mockMvc.perform(get("/api/interview/findByDate")
//                        .param("date", String.valueOf(interviewDate))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print());
//
//
//    }
    @Test
    public void saveInterviewObject() throws Exception {

        when(interviewService.saveInterview(any())).thenReturn(interviewDTO);
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.post("/api/interview/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interviewDTO)));
        response.andDo(print()).
                andExpect(status().isOk());
        verify(interviewService,times(1)).saveInterview(any());


    }
    public void UpdateInterview() throws Exception {
        given(interviewService.findInterviewById(interviewId)).willReturn(interviewDTO);


        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/interview/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interviewDTO_Up)));

        //return
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.date", is(interviewDTO_Up.getDate())));


    }
    @Test
    public void DeleteById() throws Exception {
        willDoNothing().given(interviewService).deleteById(interviewId);
        ResultActions response = mockMvc.perform(delete("/api/interview/delete/{id}",interviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interviewDTO)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
