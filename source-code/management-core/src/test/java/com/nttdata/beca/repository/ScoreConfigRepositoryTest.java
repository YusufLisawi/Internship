package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
import com.nttdata.beca.entity.ScoreConfig;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BecaApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScoreConfigRepositoryTest {
    @Autowired
    ScoreConfigRepository scoreConfigRepository;
    private ScoreConfig scoreConfig;
    @Before
    public void SetUp(){
        scoreConfig=ScoreConfig.builder()
                .peopleDenominator(5)
                .peopleWeight(4)
                .softSkillsDenominator(5)
                .softSkillsWeight(5)
                .technicalTestWeight(7)
                .technicalTestDenominator(8)
                .status("current")
                .build();
    }
    @DisplayName("Testing save")
    @Test
    public void BeforeAll_SaveScoreConfig_Test() {

        ScoreConfig Save_scoreConfig =scoreConfigRepository.save(scoreConfig);
        assertThat(Save_scoreConfig).isNotNull();

    }
    @DisplayName("Testing update status of scoreConfig")
    @Test
    public void     A_UpdateStatusOf_ScoreConfig_Test(){
        scoreConfigRepository.changeStatus();

    }
    @DisplayName("Testing update status of scoreConfig")
    @Test
    public void GetCurrent_ScoreConfig_Test(){
        ScoreConfig CurrentScoreC=scoreConfigRepository.getCurrentConfig();
        assertThat(CurrentScoreC).isNotNull();
        assertThat(CurrentScoreC.getStatus()).isEqualTo("current");

    }
    @DisplayName("Delete ScoreConfig")
    @Test
    public void X_ScoreConfig_Delete(){
       scoreConfigRepository.delete(scoreConfig);

    }
}
