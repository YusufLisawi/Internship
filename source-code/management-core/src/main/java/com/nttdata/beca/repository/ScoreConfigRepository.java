package com.nttdata.beca.repository;



import com.nttdata.beca.entity.ScoreConfig;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ScoreConfigRepository extends CrudRepository<ScoreConfig, Long> {

    @Modifying
    @Transactional
    @Query(value="UPDATE ScoreConfig sc SET sc.status='previous'")
    void changeStatus();

    @Query(value = "FROM ScoreConfig WHERE status='current'")
    ScoreConfig getCurrentConfig();

}
 