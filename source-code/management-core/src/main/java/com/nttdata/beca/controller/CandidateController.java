package com.nttdata.beca.controller;

import java.io.IOException;
import java.util.List;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.config.services.EmailService;
import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.exception.records.ErrorResponse;
import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.service.CandidateService;
import com.nttdata.beca.service.InterviewService;
import com.nttdata.beca.service.SessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
@Tag(name = "Candidate", description = "The Candidate API")
public class CandidateController {

    private final CandidateService candidateService;
    private final SessionService sessionService;
    private final EmailService emailService;

    private final InterviewService interviewService;

    @Operation(summary = "Get all Recruitment Candidate", description = "Return a list of Recruitment Candidates", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/all")
    public ResponseEntity<List<CandidateDTO>> getCandidateList() {
        return new ResponseEntity<>(candidateService.getCurrentSessionCandidates(),HttpStatus.OK);
    }
    @Operation(summary = "Find Candidate", description = "Finding Candidate by searching with email", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/findByEmail")
    public ResponseEntity<List<CandidateDTO>> getCandidateByEmail(@RequestParam String email) {
        return new ResponseEntity<>(candidateService.findByEmail(email),HttpStatus.OK);
    }

    @Operation(summary = "Find Candidate", description = "Finding Candidate by searching with candidate id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/find/{candidateId}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long candidateId) {
        return new ResponseEntity<>(candidateService.findById(candidateId),HttpStatus.OK);
    }

    @Operation(summary = "Find count of Candidate", description = "Return number of Candidate in a Session", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/countBySession/{sessionId}")
    public ResponseEntity<Long> countInSession(@PathVariable Long sessionId) {
        return new ResponseEntity<>(candidateService.countInSession(sessionId),HttpStatus.OK);
    }

    @Operation(summary = "Find Candidate by session", description = "Finding all Candidates in a Session", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/findBySession/{sessionId}")
    public ResponseEntity<List<CandidateDTO>> getAllBySession(@PathVariable Long sessionId) {
        return new ResponseEntity<>(candidateService.findBySession(sessionId),HttpStatus.OK);
    }

    @Operation(summary = "Add Recruitment Candidate", description = "Adding a new Candidate", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/add")
    public ResponseEntity<CandidateDTO> addCandidate(@RequestBody CandidateDTO candidateDTO) throws Exception {
        return new ResponseEntity<>(candidateService.addCandidate(candidateDTO),HttpStatus.OK);
    }

    @Operation(summary = "Get accepted Candidate", description = "Git a list of candidate accepted", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/accepted")
    public ResponseEntity<List<CandidateDTO>> findByPreselectionStatus() {
        return new ResponseEntity<>(candidateService.findByPreselectionStatus(), HttpStatus.OK);
    }


    @Operation(summary = "Get accepted internship Candidate", description = "Git a list of internship candidate accepted", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/accepted-internship")
    public ResponseEntity<List<CandidateDTO>> findAcceptedInternshipCandidate() {
        return new ResponseEntity<>(candidateService.findAcceptedInternshipCandidate(), HttpStatus.OK);
    }

    @Operation(summary = "Get preselected Candidate", description = "Get the count of preselected candidate", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/preselected-count")
    public ResponseEntity<Long> countPreselectionStatus() {
        return new ResponseEntity<>(candidateService.countPreselectedStatus(), HttpStatus.OK);
    }

    @Operation(summary = "Add Score Candidate", description = "Add the score of candidate by passing the candidate id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/save-score/{candidateId}")
    public ResponseEntity<CandidateDTO> saveScoreCandidate(@RequestBody ScoreDTO scoreDTO, @PathVariable Long candidateId) throws Exception {
        return new ResponseEntity<>(candidateService.saveScore(candidateId,scoreDTO), HttpStatus.OK);
    }

    @Operation(summary = "Update Candidate", description = "Update Candidate by finding candidate id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(path = "/update")
    public ResponseEntity<CandidateDTO> updateCandidate(@RequestBody CandidateDTO candidateDTO) {
        return new ResponseEntity<>(candidateService.updateCandidate(candidateDTO),HttpStatus.OK);
    }
    @Operation(summary = "move Waiting List To new Session", description = "Update Candidate by finding candidate id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(path = "/moveWaitingListToSession/{sessionId}")
    public ResponseEntity<CandidateDTO> moveWaitingListToSession(@RequestBody CandidateDTO candidateDTO,@PathVariable Long sessionId) {
        return new ResponseEntity<>(candidateService.moveWaitingListToSession(candidateDTO,sessionId),HttpStatus.OK);
    }
    @Operation(summary = "Delete Candidate", description = "Delete Candidate by candidate id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteCandidateById(@PathVariable Long id) {
        return new ResponseEntity<>(candidateService.delete(id),HttpStatus.OK);
    }

    @Operation(summary = "Count of Candidate", description = "Get count of candidate when session status is previous", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/count")
    public ResponseEntity<Long> getTotalNumber() {
        return new ResponseEntity<>(candidateService.count(),HttpStatus.OK);
    }

    @Operation(summary = "Count of Candidate", description = "Get count of candidate when session status is previous", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/set-preselection-status")
    public ResponseEntity<MessageResponse> setPreselectionStatus(@RequestParam Long candidateId, @RequestParam Boolean preselectionStatus) {
        return new ResponseEntity<>(candidateService.setPreselectionStatus(candidateId,preselectionStatus),HttpStatus.OK);
    }

    @Operation(summary = "Get Preselected candidate", description = "Return preselected candidate when preselected status is true and session status is previous", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/count-preselected")
    public ResponseEntity<Long> getPreselected() {
        return new ResponseEntity<>(candidateService.preselectedCount(),HttpStatus.OK);
    }

    @Operation(summary = "Get Admitted Candidate", description = "Return the numbre of candidate admitted in a session", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/count-admitted")
    public ResponseEntity<Long> getAdmittedNumber() {
        return new ResponseEntity<>(candidateService.admittedCount(),HttpStatus.OK);
    }

    @Operation(summary = "Get Email", description = "Return the number of registered account by one email", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/count-email")
    public ResponseEntity<Long> nbrTimes(@RequestParam String email) {
        return new ResponseEntity<>(candidateService.countEmail(email),HttpStatus.OK);
    }

    @Operation(summary = "Get Final list", description = "Return all candidate that have been passed the test successfully", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/final-list")
    public ResponseEntity<List<CandidateDTO>> getFinalList() throws Exception {
        return new ResponseEntity<>(candidateService.getFinalList(sessionService.getAdmittedNumber()),HttpStatus.OK);
    }

    @Operation(summary = "Admitted Candidates", description = "Send an Email to all the admitted candidates of the final list", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/admitted-candidates")
    public ResponseEntity<?> admittedCandidate(@RequestBody List<String> emails) {
        try {
            emailService.sendEmailToAdmittedCandidate(emails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error in the server : " + e.getMessage());
        }
        return ResponseEntity.ok("Emails sent successfully");
    }

    @Operation(summary = "Admitted Candidate", description = "Return all Candidate that been admitted", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/admitted")
    public ResponseEntity<List<CandidateDTO>> getAdmitted() {
        return new ResponseEntity<>(candidateService.getAdmitted(),HttpStatus.OK);
    }

    @Operation(summary = "Waiting Candidate", description = "Return all Candidate that been classed in the waiting list", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/waiting-list")
    public ResponseEntity<List<CandidateDTO>> getWaiting() throws Exception {
        return new ResponseEntity<>(candidateService.getWaiting(sessionService.getAdmittedNumber(),sessionService.getAdmittedNumber()),HttpStatus.OK);
    }

    @Operation(summary = "Get All Universities", description = "Return a list of Universities", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/allUniversities")
    public ResponseEntity<List<String>> getAllUniversities() {
        return new ResponseEntity<>(candidateService.findAllUniversities(),HttpStatus.OK);
    }

    @Operation(summary = "Get All Cities", description = "Return a list of cities", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/allCities")
    public ResponseEntity<List<String>> getAllCities() {
        return new ResponseEntity<>(candidateService.findAllCities(),HttpStatus.OK);
    }

    @Operation(summary = "Get all Internship Candidate", description = "Return a list of Internship Candidates", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/internship/all")
    public ResponseEntity<List<CandidateDTO>> getInternshipCandidateList() {
        return new ResponseEntity<>(candidateService.getCurrentSessionInternshipCandidates(),HttpStatus.OK);
    }

    @Operation(summary = "Add Internship Candidate", description = "Adding a new Candidate", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/internship/add")
    public ResponseEntity<CandidateDTO> addInternshipCandidate(@RequestBody CandidateDTO candidateDTO) throws Exception {
        return new ResponseEntity<>(candidateService.addInternshipCandidate(candidateDTO),HttpStatus.OK);
    }

    @Operation(summary = "Get All Candidates", description = "Return a list of candidates", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = CandidateDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
   @GetMapping(path = "/findByInterview/{idInterview}")
    public ResponseEntity<List<CandidateDTO>> getCandidateByInterview(@PathVariable Long idInterview) {
        return new ResponseEntity<>(candidateService.findByInterview(idInterview),HttpStatus.OK);
    }



}