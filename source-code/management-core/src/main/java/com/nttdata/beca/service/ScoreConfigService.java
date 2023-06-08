package com.nttdata.beca.service;

import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.entity.ScoreConfig;

public interface ScoreConfigService extends GenericService<ScoreConfigDTO, Long> {

    void changeStatus();
    ScoreConfigDTO getCurrentConfig();

}
