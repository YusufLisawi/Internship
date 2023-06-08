package com.nttdata.beca.repository;

import com.nttdata.beca.entity.Score;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
    Score findScoreByScoreId(Long id);

}
