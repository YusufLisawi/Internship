package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Score;
import com.nttdata.beca.entity.ScoreConfig;
import com.nttdata.beca.repository.ScoreRepository;
import com.nttdata.beca.service.impl.ScoreServiceImpl;
import com.nttdata.beca.transformer.ScoreTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {
    @Mock
    ScoreRepository scoreRepository;

    @InjectMocks
    ScoreServiceImpl scoreService;
    private ScoreDTO scoreDTO;
    private Score score;
    private Candidate candidate;
    private ScoreConfig scoreConfig;
    @InjectMocks
    private ScoreTransformer scoreTransformer;
    @Before
    public  void  setUp() {
        MockitoAnnotations.initMocks(this);
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
                .session(null)
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .finalAdmissionStatus("true")
                .build();
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
        score=Score.builder()
                .scoreId(1L)
                .scoreId(1l)
                .peopleDesc(null)
                .softSkillsDesc(null)
                .technicalDesc(null)
                .finalScore(0)
                .englishLevel("b2")
                .spanishLevel("b2")
                .peopleScore(5)
                .technicalTestScore(5)
                .build();
        scoreDTO = scoreTransformer.toDTO(score);
    }
    @DisplayName("testing save method")
    @Test
    public void SaveInterview_test(){

        Mockito.when(scoreRepository.save(score)).thenReturn(score);

        ScoreDTO scoreSave = scoreService.save(scoreDTO);

        verify(scoreRepository, times(1)).save(any());
    }
}
