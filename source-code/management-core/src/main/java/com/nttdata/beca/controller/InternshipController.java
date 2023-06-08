package com.nttdata.beca.controller;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Internship;
import com.nttdata.beca.exception.records.ErrorResponse;
import com.nttdata.beca.service.InternshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/internship")
@RequiredArgsConstructor
@Tag(name = "Internship", description = "The Internship API")
public class InternshipController {


    private final InternshipService internshipService;

    @Operation(summary = "Get all Internships", description = "Return a list of Internships", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InternshipDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/all")
    public ResponseEntity<List<InternshipDTO>> getInternshipList() {
        return new ResponseEntity<>(internshipService.getAllInternships(), HttpStatus.OK);
    }

    @Operation(summary = "Get all Internships of The Current Session", description = "Return a list of Internships of The Current Session", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InternshipDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/allCurrentInternshipSession")
    public ResponseEntity<List<InternshipDTO>> getCurrentSessionInternshipList() {
        return new ResponseEntity<>(internshipService.getCurrentSessionInternships(), HttpStatus.OK);
    }

    @Operation(summary = "Find Internship", description = "Finding Internship by searching with internship id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InternshipDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/find/{internship_id}")
    public ResponseEntity<InternshipDTO> getOneById(@PathVariable long internship_id) {
        return new ResponseEntity<>(internshipService.getInternshipsById(internship_id), HttpStatus.OK);
    }

    @Operation(summary = "Add Internship", description = "Adding a new Internship", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InternshipDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/add")
    public ResponseEntity<InternshipDTO> addInternship(@RequestBody InternshipDTO internshipDTO) throws Exception {
        return new ResponseEntity<>(internshipService.addInternship(internshipDTO), HttpStatus.OK);
    }

    @Operation(summary = "Delete Internship", description = "Delete Internship by internship id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(path = "/delete/{internship_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteById(@PathVariable long internship_id) {
        return new ResponseEntity<>(internshipService.deleteInternshipById(internship_id), HttpStatus.OK);
    }

    @Operation(summary = "Update Internship", description = "Update Internship by finding internship id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InternshipDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(path = "/update")
    public ResponseEntity<InternshipDTO> updateInternship(@RequestBody InternshipDTO internshipDTO) {
        return new ResponseEntity<>(internshipService.updateInternship(internshipDTO), HttpStatus.OK);
    }
}
