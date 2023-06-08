package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.entity.Score;
import com.nttdata.beca.entity.ScoreConfig;
import com.nttdata.beca.repository.CandidateRepository;
import com.nttdata.beca.repository.ScoreConfigRepository;
import com.nttdata.beca.repository.ScoreRepository;
import com.nttdata.beca.repository.SessionRepository;
import com.nttdata.beca.service.ScoreService;
import com.nttdata.beca.transformer.ScoreTransformer;
import com.nttdata.beca.transformer.Transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScoreServiceImpl extends GenericServiceImpl<Score, ScoreDTO, Long>
        implements ScoreService {
    private static final Transformer<Score, ScoreDTO> scoreTransformer = new ScoreTransformer();

    public ScoreServiceImpl() {
        super(scoreTransformer);
    }

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @Override
    public ScoreDTO findScoreById(Long id) {
        return scoreTransformer.toDTO(scoreRepository.findScoreByScoreId(id));
    }

    @Override
    public ScoreDTO saveScore(ScoreDTO dto) throws Exception {
        ScoreDTO configDto = findScoreById(dto.getScoreId());
        if (configDto != null) {
            configDto.setScoreId((long) 1);
            throw new Exception("Scores already added");
        } else {
            Score score = scoreTransformer.toEntity(dto);
            return scoreTransformer.toDTO(scoreRepository.save(score));
        }
    }

    @Override
    public ScoreDTO updateScore(ScoreDTO scoreDTO) throws Exception {
        ScoreDTO dto = findScoreById(scoreDTO.getScoreId());
        dto.setTechnicalDesc(scoreDTO.getTechnicalDesc());
        dto.setTechnicalTestScore(scoreDTO.getTechnicalTestScore());
        dto.setTechnicalInterviewScore(scoreDTO.getTechnicalInterviewScore());
        //dto.setIsAccepted(scoreDTO.getTechnicalTestScore() >= sessionRepository.getEliminatingMark() || scoreDTO.getTechnicalInterviewScore() >= sessionRepository.getEliminatingMark());
        dto.setPeopleDesc(scoreDTO.getPeopleDesc());
        dto.setPeopleScore(scoreDTO.getPeopleScore());
        dto.setSoftSkillsDesc(scoreDTO.getSoftSkillsDesc());
        dto.setSoftSkillsScore(scoreDTO.getSoftSkillsScore());
        dto.setEnglishLevel(scoreDTO.getEnglishLevel());
        //dto.setIsAccepted(scoreDTO.getTechnicalTestScore() >= sessionRepository.getEliminatingMark() && (scoreDTO.getEnglishLevel() != null && checkEnglishLevel(scoreDTO.getEnglishLevel(), sessionRepository.getEliminatingEnglishLevel())));
        dto.setIsAccepted(isAccepted(scoreDTO));
        dto.setSpanishLevel(scoreDTO.getSpanishLevel());
        dto.setFinalScore(scoreDTO.getFinalScore());
        return super.save(dto);
    }

    private boolean isAccepted(ScoreDTO scoreDTO){
        if(scoreDTO.getTechnicalTestScore() < sessionRepository.getEliminatingMark() || scoreDTO.getTechnicalInterviewScore() < sessionRepository.getEliminatingMark()){
            return false;
        } else if(!scoreDTO.getEnglishLevel().isEmpty() && !checkEnglishLevel(scoreDTO.getEnglishLevel(), sessionRepository.getEliminatingEnglishLevel())) {
            return false;
        }
        return true;
    }

    private boolean checkEnglishLevel(String englishLevel, String eliminatingLevel){
        Integer eliminatingLevelKey = 0;
        Integer englishLevelKey = 0;
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"A1");
        map.put(2,"A2");
        map.put(3,"B1");
        map.put(4,"B2");
        map.put(5,"C1");
        map.put(6,"C2");
        for(Map.Entry m : map.entrySet()){
            if(eliminatingLevel.equals(m.getValue())){
                eliminatingLevelKey = (Integer) m.getKey();
            }
            if(englishLevel.equals(m.getValue())){
                englishLevelKey = (Integer) m.getKey();
            }
        }
        return englishLevelKey >= eliminatingLevelKey;
    }

}
