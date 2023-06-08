package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
import com.nttdata.beca.entity.Score;
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

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BecaApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScoreRepositoryTest {
    @Autowired
    ScoreRepository scoreRepository;
    @Autowired
    ScoreConfigRepository scoreConfigRepository;
    private Score score;
    private ScoreConfig scoreConfig;
    @Before
    public void SetUp(){
        scoreConfig=ScoreConfig.builder()
                .scoreConfigId(1l)
                .peopleDenominator(5)
                .peopleWeight(4)
                .softSkillsDenominator(5)
                .softSkillsWeight(5)
                .technicalTestWeight(7)
                .technicalTestDenominator(8)
                .status("current")
                .build();

        ScoreConfig scoreConfigSave=scoreConfigRepository.save(scoreConfig);
        score=Score.builder()
                .scoreId(1l)
                .peopleDesc(null)
                .softSkillsDesc(null)
                .technicalDesc(null)
                .finalScore(0)
                .englishLevel("B2")
                .spanishLevel("B2")
                .peopleScore(5)
                .technicalTestScore(5)
                .build();
    }
    @Test
    @DisplayName("Save Testing")
    public void BeforeAll_Save_Test() {


        Score scoreSave =scoreRepository.save(score);
        assertThat(scoreSave).isNotNull();


    }
    @Test
    @DisplayName("Testing findById ")
    public void FindCandidatesById_Test() {
        Score findScoreId_1 = scoreRepository.findScoreByScoreId(1l);
        assertThat(findScoreId_1).isNotNull();
    }
    @Test
    @DisplayName("Delete Score")
    public void Last_DeleteScore(){
        scoreRepository.deleteAll();
    }
}
