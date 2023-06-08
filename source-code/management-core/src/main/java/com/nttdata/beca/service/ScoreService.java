package com.nttdata.beca.service;

import com.nttdata.beca.dto.ScoreDTO;

public interface ScoreService extends GenericService<ScoreDTO, Long> {
    ScoreDTO findScoreById(Long id);
    ScoreDTO saveScore(ScoreDTO dto) throws Exception;
    ScoreDTO updateScore(ScoreDTO scoreDTO) throws Exception;
}
