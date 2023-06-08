package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.entity.ScoreConfig;
import com.nttdata.beca.service.impl.ScoreConfigServiceImpl;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "../src/main/resources")
@ExtendWith(SpringExtension.class)
public class ScoreConfigControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    ScoreConfigServiceImpl scoreConfigService;
    @InjectMocks
    ScoreConfigController scoreConfigController;
    private ScoreConfig scoreConfig;
    private ScoreConfigDTO scoreConfigDTO, scoreConfigDTO_Up;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(scoreConfigController).build();

        scoreConfigDTO=ScoreConfigDTO.builder()
                .scoreConfigId(1l)
                .peopleDenominator(5)
                .peopleWeight(4)
                .softSkillsDenominator(5)
                .softSkillsWeight(5)
                .technicalTestWeight(7)
                .technicalTestDenominator(8)
                .status("current")
                .build();

        scoreConfigDTO_Up=ScoreConfigDTO.builder()
                .peopleDenominator(5)
                .peopleWeight(4)
                .softSkillsDenominator(5)
                .softSkillsWeight(5)
                .technicalTestWeight(7)
                .technicalTestDenominator(8)
                .status("current")
                .build();

    }
    /*@Test
    public void Get_CurrentScoreConfig_Test() throws Exception {

        //given(scoreConfigService.).willReturn(List.of(scoreConfigDTO));
        //when
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/scoreConfig/all"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(1)));
    }*/
    @Test
    public void Save_ScoreConfig_Test() throws Exception {

        when(scoreConfigService.save(any())).thenReturn(scoreConfigDTO);
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.post("/api/scoreConfig/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(scoreConfigDTO)));
        response.andDo(print()).
                andExpect(status().isOk());
        verify(scoreConfigService,times(1)).save(scoreConfigDTO);


    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
