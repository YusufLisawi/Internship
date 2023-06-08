package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.entity.ScoreConfig;
import com.nttdata.beca.repository.ScoreConfigRepository;
import com.nttdata.beca.service.ScoreConfigService;
import com.nttdata.beca.transformer.ScoreConfigTransformer;
import com.nttdata.beca.transformer.Transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreConfigServiceImpl extends GenericServiceImpl<ScoreConfig, ScoreConfigDTO, Long> implements ScoreConfigService{

    public static Transformer<ScoreConfig, ScoreConfigDTO> scoreConfigTransformer = new ScoreConfigTransformer();

    public ScoreConfigServiceImpl() {
        super(scoreConfigTransformer);
    }

    @Autowired
    ScoreConfigRepository scoreConfigRepository;

    @Override
    public ScoreConfigDTO save(ScoreConfigDTO scoreConfigDTO) {
        changeStatus();
        scoreConfigDTO.setStatus("current");
        return super.save(scoreConfigDTO);
    }

    @Override
    public void changeStatus(){
        this.scoreConfigRepository.changeStatus();
    }

    @Override
    public ScoreConfigDTO getCurrentConfig() {
        return scoreConfigTransformer.toDTO(scoreConfigRepository.getCurrentConfig());
    }

}
