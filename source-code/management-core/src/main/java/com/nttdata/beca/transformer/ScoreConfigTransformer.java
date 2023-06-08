package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.entity.ScoreConfig;

public class ScoreConfigTransformer extends Transformer<ScoreConfig, ScoreConfigDTO> {

    @Override
    public ScoreConfig toEntity(ScoreConfigDTO dto) {
        if (dto == null)
            return null;
        else {
            ScoreConfig scoreConfig = new ScoreConfig();
            scoreConfig.setScoreConfigId(dto.getScoreConfigId());
            scoreConfig.setPeopleDenominator(dto.getPeopleDenominator());
            scoreConfig.setSoftSkillsDenominator(dto.getSoftSkillsDenominator());
            scoreConfig.setTechnicalTestDenominator(dto.getTechnicalTestDenominator());
            scoreConfig.setTechnicalInterviewDenominator(dto.getTechnicalInterviewDenominator());
            scoreConfig.setPeopleWeight(dto.getPeopleWeight());
            scoreConfig.setSoftSkillsWeight(dto.getSoftSkillsWeight());
            scoreConfig.setTechnicalTestWeight(dto.getTechnicalTestWeight());
            scoreConfig.setTechnicalInterviewWeight(dto.getTechnicalInterviewWeight());
            scoreConfig.setStatus(dto.getStatus());

            return scoreConfig;
        }

    }

    @Override
    public ScoreConfigDTO toDTO(ScoreConfig entity) {
        if (entity == null) {
            return null;
        } else {
            return new ScoreConfigDTO(entity.getScoreConfigId(), entity.getPeopleDenominator(),
                    entity.getSoftSkillsDenominator(), entity.getTechnicalTestDenominator(), entity.getTechnicalInterviewDenominator(),
                    entity.getPeopleWeight(), entity.getSoftSkillsWeight(), entity.getTechnicalTestWeight(), entity.getTechnicalInterviewWeight(), entity.getStatus());

        }
    }

}
