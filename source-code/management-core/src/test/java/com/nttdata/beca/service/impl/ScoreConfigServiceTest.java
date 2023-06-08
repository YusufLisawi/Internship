package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.entity.ScoreConfig;
import com.nttdata.beca.repository.ScoreConfigRepository;
import com.nttdata.beca.service.impl.ScoreConfigServiceImpl;
import com.nttdata.beca.transformer.ScoreConfigTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScoreConfigServiceTest {

    @Mock
    ScoreConfigRepository scoreConfigRepository;

    @InjectMocks
    ScoreConfigServiceImpl scoreConfigService;

    @InjectMocks
    ScoreConfigTransformer scoreConfigTransformer;





    private ScoreConfigDTO scoreConfigDTO;
    private ScoreConfig scoreConfig;



    @Before
    public  void  setUp() {
        MockitoAnnotations.initMocks(this);
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

        scoreConfig = scoreConfigTransformer.toEntity(scoreConfigDTO);

    }
    @DisplayName("testing save method")
    @Test
    public void saveMethod_test(){

        Mockito.when(scoreConfigRepository.findById(any())).thenReturn(null);
       
        Mockito.when(scoreConfigRepository.save(any())).thenReturn(scoreConfig);

        ScoreConfigDTO scoreSave = scoreConfigService.save(scoreConfigDTO);

        verify(scoreConfigRepository, times(1)).save(scoreConfig);
    }
}
