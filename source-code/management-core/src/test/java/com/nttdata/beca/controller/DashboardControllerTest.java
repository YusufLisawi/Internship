package com.nttdata.beca.controller;

import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.service.CandidateService;
import com.nttdata.beca.service.SessionService;
import com.nttdata.beca.transformer.CandidateTransformer;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class DashboardControllerTest {
    @Autowired
    MockMvc mockMvc;
    @InjectMocks
    DashboardController dashboardController;
    @Mock
    CandidateService candidateService;
    @Mock
    SessionService sessionService;

    Candidate candidate;

    CandidateDTO candidateDTO;

    @InjectMocks
    private CandidateTransformer candidateTransformer;
   long sessionId=1l;

    @Before
    public  void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
        candidate = Candidate.builder()
                .email("rabab@gmail,com")
                .firstName("sara")
                .lastName("elmehdy")
                .anapec("false")
                .diplome("Bac+3")
                .city("Tetouan")
                .CVname("CV-RABAB EL MEHDY.pdf")
                .gender("female")
                .interview(null)
                .session(Session.builder()
                        .sessionId(1l)
                        .build())
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .finalAdmissionStatus("true")
                .build();

        candidateDTO = candidateTransformer.toDTO(candidate);
    }
    @Test
    public void Get_sessionStatics_Test() throws Exception {
        when(candidateService.findBySession(anyLong())).thenReturn(List.of(candidateDTO));
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.get("/api/Dashboard/getStatistics")
                        .param("sessionId", String.valueOf(sessionId))
                .contentType(MediaType.APPLICATION_JSON));
        response.andDo(print())
        .andExpect(status().isOk());
              //  .andExpect(jsonPath("$.size()", CoreMatchers.is(List.of(candidate).size())));
    }
    @Test
    public void GetStatics_ofLatest_session_Test() throws Exception {

        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.get("/api/Dashboard/getStatisticsOfLatestSession")
                .contentType(MediaType.APPLICATION_JSON));
        response.andDo(print()).
                andExpect(status().isOk());


    }
    @Test
    public void Count_Admitted_PreviousSession_Test() throws Exception {
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.get("/api/Dashboard/countAdmittedPreviousSession")
                .contentType(MediaType.APPLICATION_JSON));
        response.andDo(print()).
                andExpect(status().isOk());
    }
}
