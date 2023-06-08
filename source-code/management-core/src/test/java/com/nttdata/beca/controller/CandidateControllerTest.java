package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.dto.*;
import com.nttdata.beca.dto.DashboardStatistics;
import com.nttdata.beca.entity.*;
import com.nttdata.beca.service.impl.CandidateServiceImpl;
import com.nttdata.beca.transformer.RecruiterTransformer;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class CandidateControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    CandidateServiceImpl candidateService;
    @InjectMocks
    CandidateController candidateController;
    @Mock
    DashboardStatistics dashboardStatistics;
    @InjectMocks
    RecruiterTransformer recruiterTransformer;


    private CandidateDTO candidateDTO,candidateDTO_Up;
    private Candidate candidate;
    private List candidateDTOList, candidateList;
    private String candidateEmail="oumaimamehdy@gmail,com";
    private  long candidateId = 1L;
    private long sessionId=1L;

    private Session session;

    private List<String> universitiesList,citiesList,diplomasList;
    @Before
    public  void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(candidateController).build();

        candidateDTO = CandidateDTO.builder()
                .candidateId(1L)
                .email("oumaimamehdy@gmail,com")
                .firstName("sara")
                .lastName("mhdy")
                .anapec("false")
                .diplome("Bac+4")
                .city("Tetouan")
                .CVname("CV-RABAB EL MEHDY.pdf")
                .gender("female")
                .session(SessionDTO.builder()
                        .sessionId(1L)
                        .build())
                .interview(InterviewDTO.builder()
                        .interviewId(1l)
                        .session(SessionDTO.builder()
                                .sessionId(1l)
                                .build())
                        .build())
                .finalAdmissionStatus("true")
                .preselectionStatus(true)
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .build();

        candidate = Candidate.builder()
                .candidateId(2L)
                .email("rababelmehdy@gmail,com")
                .firstName("rabab")
                .lastName("mhdy")
                .anapec("false")
                .diplome("Bac+4")
                .city("Tetouan")
                .CVname("CV-RABAB EL MEHDY.pdf")
                .gender("female")
                .session(Session.builder()
                        .sessionId(1l)
                        .build())
                .finalAdmissionStatus("true")
                .preselectionStatus(true)
                .interview(Interview.builder().interviewId(1L).build())
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .interview(Interview.builder()
                        .interviewId(1l)
                        .session(Session.builder()
                                .sessionId(1l)
                                .build())
                        .build())
                .build();

        candidateDTO_Up = CandidateDTO.builder()
                .email("saramhdy@gmail,com")
                .firstName("sara")
                .lastName("mhdy")
                .anapec("false")
                .diplome("Bac+4")
                .city("rababt")
                .CVname("CV-RABAB EL MEHDY.pdf")
                .gender("female")
                .session(SessionDTO.builder()
                        .sessionId(1L)
                        .build())
                .finalAdmissionStatus("true")
                .preselectionStatus(true)
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .build();

        candidateDTOList = new ArrayList<>();
        candidateDTOList.add(candidateDTO);

        candidateList = new ArrayList<>();
        candidateList.add(candidate);

        session=Session.builder().sessionId(1l).build();





    }
    @Test
    public void Save_Candidate_Test() throws Exception {
       given(candidateService.save(candidateDTO)).willReturn(candidateDTO);
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.post("/api/candidate/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(candidateDTO)));
        response.andDo(print()).
                andExpect(status().isOk());
        //verify(candidateService,times(1)).save(candidateDTO);

}
    /*@Test
    public void Get_AllCandidates_Test() throws Exception {

        //given
        given(candidateService.findAllCandidate()).willReturn(List.of(candidate));
        //when
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/all"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(1)));
    }*/
    @Test
    public void Get_Candidate_ByEmail() throws Exception{

        given(candidateService.findByEmail(candidateEmail)).willReturn(List.of(candidateDTO));

        ResultActions response = mockMvc.perform(get("/api/candidate/findByEmail")
                        .param("email",candidateEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(candidateDTO)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(1)));



    }
    @Test
    public void Get_Candidate_ById() throws Exception{

        given(candidateService.findById(candidateId)).willReturn(candidateDTO);

        ResultActions response = mockMvc.perform(get("/api/candidate//find/{candidateId}",candidateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(candidateDTO)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(candidateDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(candidateDTO.getLastName())));


    }
    @Test
    public void Count_InSession_BySessionId() throws Exception{

        given(candidateService.countInSession(sessionId)).willReturn(2l);

        ResultActions response = mockMvc.perform(get("/api/candidate/countBySession/{sessionId}",sessionId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("2"));
    }
    @Test
    public void Get_AllCandidate_InSession_BySession() throws Exception{

        given(candidateService.findBySession(session.getSessionId())).willReturn((candidateList));

        ResultActions response = mockMvc.perform(get("/api/candidate/findBySession/{sessionId}",sessionId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public  void  Update_Candidate_Test() throws Exception {
        given(candidateService.findById(any())).willReturn(candidateDTO);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/candidate/update")
                        .content(asJsonString(candidateDTO_Up))
                .contentType(MediaType.APPLICATION_JSON));

        //return
        response.andExpect(status().isOk())
                .andDo(print());
               // .andExpect(jsonPath("$.email", is(candidateDTO_Up.getEmail())))
              //.andExpect(jsonPath("$.city", is(candidateDTO_Up.getCity())));



    }
    @Test
    public void Delete_ById() throws Exception {

        willDoNothing().given(candidateService).deleteById(candidateId);
        ResultActions response = mockMvc.perform(delete("/api/candidate/delete/{id}",candidateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(candidate)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void Get_TotalNumber() throws Exception {
        given(candidateService.count()).willReturn(1l);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/count"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(content().string("1"));
    }
    @Test
    public void Get_PreselectedCandidate_Number() throws Exception {
        given(candidateService.preselectedCount()).willReturn(2l);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/count-preselected"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(content().string("2"));
    }
    @Test
    public void Get_AdmittedCandidate_Number() throws Exception {
        given(candidateService.admittedCount()).willReturn(5l);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/count-admitted"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(content().string("5"));
    }
    @Test
    public void Get_EmailCount() throws Exception {
        given(candidateService.countEmail(candidateEmail)).willReturn(1l);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/count-email")
                .param("email",candidateEmail));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(content().string("1"));
    }
    @Test
    public void Get_FinalList_Test() throws Exception {

        //given
        given(candidateService.getFinalList(5)).willReturn(candidateList);
        //when
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/final-List"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(candidateList.size())));
    }
    @Test
    public void Get_AdmittedCandidates_Test() throws Exception {

        //given
        given(candidateService.getAdmitted()).willReturn(candidateList);
        //when
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/admitted"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(candidateDTOList.size())));
    }
    @Test
    public void Get_WaitingCandidates_Test() throws Exception {

        //given
        given(candidateService.getWaiting(2,1)).willReturn(candidateList);
        //when
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/waiting"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(candidateDTOList.size())));
    }

    @Test
    public void Set_PreselectionStatus() throws Exception {
        when(candidateService.findById(candidateId)).thenReturn(candidateDTO);
        when(candidateService.save(candidateDTO)).thenReturn(candidateDTO);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/set-preselection-status")
                .param("candidateId", String.valueOf(1l))
                .param("preselectionStatus","false"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print());

    }


    @Test
    public void Get_AllUniversities() throws Exception {
        when(candidateService.findAllUniversities()).thenReturn(List.of("ENA","ENSATE","ENS"));
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/allUniversities"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(3)));

    }
    @Test
    public void Get_AllCities() throws Exception {
        when(candidateService.findAllCities()).thenReturn(List.of("Tétouan","Rabat"));
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/candidate/allCities"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(2)));

    }
    
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

