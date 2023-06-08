package com.nttdata.beca.controller;


import com.nttdata.beca.dto.DashboardStatistics;
import com.nttdata.beca.exception.records.ErrorResponse;
import com.nttdata.beca.service.CandidateService;
import com.nttdata.beca.service.InternshipService;
import com.nttdata.beca.service.SessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "The Dashboard API")
public class DashboardController {
    private final CandidateService candidateService;
    private final SessionService sessionService;

    private final InternshipService internshipService;

    @Operation(summary = "Get beca session statistics", description = "Return statistic of a session", responses = {
        @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = DashboardStatistics.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/getStatistics")
    public ResponseEntity<DashboardStatistics> getStatistics(@RequestParam Long sessionId) {
        return new ResponseEntity<>(new DashboardStatistics(candidateService.findBySession(sessionId), null,sessionId),HttpStatus.OK);
    }

    @Operation(summary = "Get internship session statistics", description = "Return statistic of a internship session", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = DashboardStatistics.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/getInternshipStatistics")
    public ResponseEntity<DashboardStatistics> getInternshipStatistics(@RequestParam Long sessionId) {
        return new ResponseEntity<>(new DashboardStatistics(candidateService.findBySession(sessionId), internshipService.getInternshipsBySessionId(sessionId),sessionId), HttpStatus.OK);
    }

    @Operation(summary = "Get statistics of latest internship session", description = "Return statistic of latest internship session", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = DashboardStatistics.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/getStatisticsOfLatestInternshipSession")
    public ResponseEntity<DashboardStatistics> getStatisticsOfLatestInternshipSession() {
        return this.getInternshipStatistics(sessionService.getLatestInternshipSessionId());
    }

    @Operation(summary = "Get statistics of latest beca session", description = "Return statistic of latest beca session", responses = {
        @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = DashboardStatistics.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/getStatisticsOfLatestSession")
    public ResponseEntity<DashboardStatistics> getStatisticsOfLatestSession() {
        return this.getStatistics(sessionService.getLatestSessionId());
    }

    @Operation(summary = "Get number of admitted candidates in previous internship session", description = "Return number of admitted candidates in the internship  session where session_status = previous", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping(path = "/countAdmittedPreviousInternshipSession")
    public ResponseEntity<Long> countAdmittedPreviousInternshipSession() {
        return new ResponseEntity<>(candidateService.countAdmittedPreviousInternshipSession(),HttpStatus.OK);
    }

    @Operation(summary = "Get number of admitted candidates in previous beca session", description = "Return number of admitted candidates in the beca session where session_status = previous", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/countAdmittedPreviousSession")
    public ResponseEntity<Long> countAdmittedPreviousSession() {
        return new ResponseEntity<>(candidateService.countAdmittedPreviousSession(),HttpStatus.OK);
    }
}