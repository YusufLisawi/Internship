package com.nttdata.beca.controller;

import com.nttdata.beca.entity.ScoreConfig;
import com.nttdata.beca.exception.records.ErrorResponse;
import com.nttdata.beca.dto.ScoreConfigDTO;
import com.nttdata.beca.service.ScoreConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoreConfig")
@Tag(name = "ScoreConfig", description = "The ScoreConfig API")
public class ScoreConfigController {

    ScoreConfigService scoreConfigService;

    public ScoreConfigController(ScoreConfigService scoreConfigService){
        this.scoreConfigService = scoreConfigService;
        if(this.scoreConfigService.count()==0){
            ScoreConfigDTO scoreConfig = new ScoreConfigDTO();
            scoreConfig.setStatus("current");
            scoreConfig.setPeopleWeight(1);
            scoreConfig.setPeopleDenominator(20);
            scoreConfig.setSoftSkillsWeight(1);
            scoreConfig.setSoftSkillsDenominator(20);
            scoreConfig.setTechnicalTestWeight(1);
            scoreConfig.setTechnicalTestDenominator(20);
            scoreConfig.setTechnicalInterviewWeight(1);
            scoreConfig.setTechnicalInterviewDenominator(20);
            this.scoreConfigService.save(scoreConfig);
        }
    }

    @Operation(summary = "Add ScoreConfig", description = "Adding a new ScoreConfig", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = ScoreConfigController.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @PostMapping(path = "/add")
    public ResponseEntity<ScoreConfigDTO> addScoreConfig(@RequestBody ScoreConfigDTO scoreConfigDTO) {
            return new ResponseEntity<>(scoreConfigService.save(scoreConfigDTO), HttpStatus.OK);
    }

    @Operation(summary = "Get ScoreConfig", description = "Get the current ScoreConfig", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = ScoreConfigDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/current-scoreConfig")
    public ResponseEntity<ScoreConfigDTO> getCurrentConfig() {
        return new ResponseEntity<>(scoreConfigService.getCurrentConfig(),HttpStatus.OK);
    }

}
    