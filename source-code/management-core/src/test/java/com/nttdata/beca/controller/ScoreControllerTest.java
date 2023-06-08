package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.entity.Score;
import com.nttdata.beca.service.impl.ScoreServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class ScoreControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    ScoreServiceImpl scoreService;
    @InjectMocks
    ScoreController scoreController;

    private Score score;
    private ScoreDTO scoreDTO;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(scoreController).build();
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
