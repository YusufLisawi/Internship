package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.service.impl.SessionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class SessionControllerTest {
    @Autowired
   MockMvc mockMvc;


    @Mock
    SessionServiceImpl sessionService;
    @InjectMocks
    SessionController sessionController;
    private Session session;
    private SessionDTO sessionDTO,sessionDTO1,sessionDTO2;
    private  List sessionList;
    private long sessionId = 1L;
    private String sessionName="session3";
    private int InterviewNumber=1;

    @Before
    public  void  setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();

        sessionDTO= SessionDTO.builder()
        .sessionId(1L)
        .sessionName("session3")
        .admittedNumber(10)
        .sessionStatus("current")
        .dayInterviewNumber(4)
        .sessionDate(new Date(222,8,15))
        .technology("Java")
        .recruiter(RecruiterDTO.builder()
                .recruiterId(1l)
                .build())
        .build();
         sessionDTO1= SessionDTO.builder()
                .sessionName("sessiion_2")
                .technology("Java")
                 .admittedNumber(10)
                .dayInterviewNumber(1)
                .sessionDate(new Date(2022,8,4))
                .sessionStatus("current")
                .build();
        sessionDTO2= SessionDTO.builder()
                .sessionId(4l)
                .sessionName("sessiion_3")
                .technology("Java")
                .admittedNumber(10)
                .dayInterviewNumber(3)
                .sessionDate(new Date(2022,8,7))
                .sessionStatus("current")
                .build();
        sessionList= new ArrayList<>();
        sessionList.add(sessionDTO);
    }
    @Test
    @DisplayName("testing add session")
    public void givenSessionObject_whenCreateSession_thenReturnSavedSession() throws Exception {

        when(sessionService.save(any())).thenReturn(sessionDTO);
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.post("/api/session/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionDTO)));
        response.andDo(print()).
                andExpect(status().isOk());
        verify(sessionService,times(1)).save(any());


       }

   @Test
   public void Get_AllSession_Test() throws Exception {

        //given
        given(sessionService.findAll()).willReturn(sessionList);
       //when
       ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/session/all"));
       // then - verify the output
       response.andExpect(status().is(200))
               .andDo(print())
               .andExpect(jsonPath("$.size()", is(sessionList.size())));
            }
    @Test
    public void Get_SessionById_Test() throws Exception{

        //given
        given(sessionService.findSessionById(sessionDTO.getSessionId())).willReturn(sessionDTO);
        //when
        ResultActions response = mockMvc.perform(get("/api/session/findById/{sessionId}",sessionId)
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.sessionName", is(sessionDTO.getSessionName())))
                //.andExpect(jsonPath("$.duration", is(sessionDTO.getDuration())))
                .andExpect(jsonPath("$.sessionStatus", is(sessionDTO.getSessionStatus())));

    }
    @Test
    public void Get_SessionByName_Test() throws Exception{

        given(sessionService.findSessionByName(sessionDTO.getSessionName())).willReturn(sessionDTO);
        ResultActions response = mockMvc.perform(get("/api/session/findByName")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sessionName",sessionName));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.sessionName", is(sessionDTO.getSessionName())))
               // .andExpect(jsonPath("$.duration", is(sessionDTO.getDuration())))
                .andExpect(jsonPath("$.sessionStatus", is(sessionDTO.getSessionStatus())));

    }
    @Test
    @DisplayName("testing update ")
    public void UpdateSession_Test() throws Exception {
        given(sessionService.findById(any())).willReturn(sessionDTO);

        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/session/update")
                .content(asJsonString(sessionDTO1))
                        .contentType(MediaType.APPLICATION_JSON)
                );

        //return
        response.andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void Get_AllTechnologies_Test() throws Exception {
        when(sessionService.findAllTechnologies()).thenReturn(List.of("java","Language C"));
        ResultActions response = mockMvc.perform(get("/api/session/allTechnologies")
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(2)))
                .andDo(print());
    }
    @Test
    @DisplayName("testing update session status to  previous")
    @WithMockUser
    public void UpdateSessionToPrevious_Test() throws Exception {

        willDoNothing().given(sessionService).ToPrevious();
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.put("/api/session/update-session-previous")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionDTO)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(content().string("true"));



    }
    @Test
    @DisplayName("testing update session status to Closed")
    @WithMockUser
    public void UpdateSessionToClosed_Test() throws Exception {

        willDoNothing().given(sessionService).ToClosed();
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.put("/api/session/update-session-close")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionDTO)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
    @Test
    @DisplayName("testing update InterviewNumber of session")
    public void UpdateSessionInterviewNumber_Test() throws Exception {

        given(sessionService.findSessionByStatus("current")).willReturn(sessionDTO);
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.get("/api/session//update-interview/{interviewCount}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionDTO)));

        response.andDo(print()).
                andExpect(status().isOk());
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

