package com.nttdata.beca.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.config.services.EmailService;
import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.exception.records.ErrorResponse;
import com.nttdata.beca.service.CandidateService;
import com.nttdata.beca.service.InterviewService;

import com.nttdata.beca.service.impl.CandidateServiceImpl;
import com.nttdata.beca.service.impl.InterviewServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@Tag(name = "Interview", description = "The Interview API")
public class InterviewController {

    private final InterviewService interviewService;

    private final CandidateService candidateService;

    private final EmailService emailService;
    @Autowired
    private CandidateServiceImpl candidateserviceimpl;
    @Autowired
    private InterviewServiceImpl interviewserviceimpl;

    @Operation(summary = "Get all Interview", description = "Return list of interviews", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/all")
    public Iterable<InterviewDTO> getInterviewList() {
        return interviewService.findAll();
    }

    @Operation(summary = "Find Interview", description = "Find an Interview by searching with date", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/findByCurrentSession")
    public ResponseEntity<List<InterviewDTO>> getInterviewByDate(@RequestParam String sessionStatus) {
        return new ResponseEntity<>(interviewService.findInterviewByCurrentSession(sessionStatus), HttpStatus.OK);
    }

    @Operation(summary = "Add Interview", description = "Adding a new Interview", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/add")
    public ResponseEntity<List<InterviewDTO>> addInterview(@RequestBody List<Date> dates) {

        return new ResponseEntity<>(interviewService.saveInterviews(dates),HttpStatus.OK);
    }

    @Operation(summary = "Invitation to the Interview ", description = "Send an Email to all the accepted candidate to pass the interview", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/invitation")
    public ResponseEntity<String> acceptedCandidate(@RequestBody List<String> emails) {
        try {
            emailService.sendEmailToAcceptedCandidate(emails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error in the server : " + e.getMessage());
        }
        return ResponseEntity.ok("Check your inbox");
    }

    @Operation(summary = "Update Interview", description = "Update Interview by finding Interview id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(path = "/update")
    public InterviewDTO updateInterview(@RequestBody InterviewDTO interDTO) {
        InterviewDTO cdto = interviewService.findById(interDTO.getInterviewId());
        cdto.setDate(interDTO.getDate());
        //interviewService.saveInterview(cdto);
        return cdto;

    }

    @Operation(summary = "Delete Interview", description = "Delete Interview by interview id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(path = "/delete/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteInterviewById(@PathVariable Long id) {
        System.out.println("Interview has been deleted successfully");
        return new ResponseEntity<>(interviewService.deleteInterviewById(id),HttpStatus.OK);
    }
    @Operation(summary = "Find Interview By date ", description = "Find an Interview by searching with date", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/findByDateInterview")
    public ResponseEntity<List<Integer>> getInterviewIdByDateInterview(@RequestParam("date") String dateString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);
        System.out.println(date);
        return new ResponseEntity<>(interviewService.findInterviewIdsByDate(date), HttpStatus.OK);
    }
    }






