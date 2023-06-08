package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.entity.Score;
import com.nttdata.beca.entity.ScoreConfig;

public class ScoreTransformer extends Transformer<Score, ScoreDTO> {

    @Override
    public Score toEntity(ScoreDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Score score = new Score();
            score.setScoreId(dto.getScoreId());
            score.setPeopleDesc(dto.getPeopleDesc());
            score.setTechnicalDesc(dto.getTechnicalDesc());
            score.setSoftSkillsDesc(dto.getSoftSkillsDesc());
            score.setFinalScore(dto.getFinalScore());
            score.setEnglishLevel(dto.getEnglishLevel());
            score.setSpanishLevel(dto.getSpanishLevel());
            score.setPeopleScore(dto.getPeopleScore());
            score.setSoftSkillsScore(dto.getSoftSkillsScore());
            score.setTechnicalTestScore(dto.getTechnicalTestScore());
            score.setTechnicalInterviewScore(dto.getTechnicalInterviewScore());
            score.setIsAccepted(dto.getIsAccepted());
            return score;
        }
    }

    @Override
    public ScoreDTO toDTO(Score entity) {
        if (entity == null)
            return null;
        else {
            return new ScoreDTO(entity.getScoreId(), entity.getPeopleDesc(), entity.getSoftSkillsDesc(),
                    entity.getTechnicalDesc(), entity.getFinalScore(),
                    entity.getEnglishLevel(), entity.getSpanishLevel(),
                    entity.getPeopleScore(), entity.getSoftSkillsScore(), entity.getTechnicalTestScore(), entity.getTechnicalInterviewScore(), entity.getIsAccepted());
        }
    }

}
