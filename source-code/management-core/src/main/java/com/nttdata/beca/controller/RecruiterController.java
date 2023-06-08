package com.nttdata.beca.controller;

import java.util.*;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.RoleDTO;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import com.nttdata.beca.exception.records.ErrorResponse;
import com.nttdata.beca.repository.RoleRepository;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.service.RecruiterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
@Tag(name = "Recruiter", description = "The Recruiter API")
public class RecruiterController {

    private final RecruiterService recruiterService;
    private final RoleRepository roleRepository;

    public RecruiterController(RecruiterService recruiterService, RoleRepository roleRepository) {
        if (roleRepository.count() == 0) {
            Role roleAdmin = new Role(1,ERole.ROLE_ADMIN,null);
            Role roleBeca = new Role(2,ERole.ROLE_BECA,null);
            Role roleInternship = new Role(3,ERole.ROLE_INTERNSHIP,null);
            Role roleCertification = new Role(4,ERole.ROLE_CERTIFICATION,null);
            roleRepository.saveAll(List.of(roleAdmin, roleBeca, roleInternship, roleCertification));
        }
        if (recruiterService.count() == 0) {
            RecruiterDTO recruiter = new RecruiterDTO();
            recruiter.setEmail("admin@gmail.com");
            recruiter.setUsername("admin");
            recruiter.setPassword("admin@123");
            recruiter.setFirstName("Admin");
            recruiter.setLastName("User");
            recruiter.setPhoneNumber("");
            recruiter.setPicture("");
            recruiter.setRole(List.of(new RoleDTO(1, ERole.ROLE_ADMIN)));
            recruiterService.save(recruiter,true);
        }
        this.recruiterService = recruiterService;
        this.roleRepository = roleRepository;
    }

    @Operation(summary = "Get all Recruiter", description = "Return a list of Recruiters", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecruiterDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<RecruiterDTO>> getAll() {
        return new ResponseEntity<>(recruiterService.findAll(),HttpStatus.OK);
    }

    @Operation(summary = "Get all Recruiter", description = "Return a list of Recruiters", responses = {
        @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecruiterDTO.class)))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/archived")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<RecruiterDTO>> getArchived() {
        return new ResponseEntity<>(recruiterService.findArchived(),HttpStatus.OK);
    }

    @Operation(summary = "Get all Roles", description = "Return a list of recruiter roles", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = RoleDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/all-roles")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        return new ResponseEntity<>(recruiterService.getAllRoles(),HttpStatus.OK);
    }

    @Operation(summary = "Find Recruiter", description = "Find a Recruiter by searching with role user", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = RecruiterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<RecruiterDTO> getByID(@PathVariable Long id) {
        return new ResponseEntity<>(recruiterService.findById(id),HttpStatus.OK);
    }

    @Operation(summary = "Find Recruiter", description = "Find a Recruiter by Email", responses = {
        @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = RecruiterDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<RecruiterDTO> getByEmail(@PathVariable String email) {
        return new ResponseEntity<>(recruiterService.findByEmail(email),HttpStatus.OK);
    }

    @Operation(summary = "Find Recruiter", description = "Find a Recruiter by searching with role user", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecruiterDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/find-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecruiterDTO>> getUsers() {
        return new ResponseEntity<>(recruiterService.findUsers(),HttpStatus.OK);
    }

    @Operation(summary = "Add Recruiter", description = "Adding a new Recruiter", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addRecruiter(@RequestBody RecruiterDTO recruiterDTO) {

        if (recruiterService.existsByEmail(recruiterDTO.getEmail())) {
            if(recruiterService.findByEmail(recruiterDTO.getEmail()).isDeleted())
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            else
                return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use !!"));
        }

        if (recruiterService.findByUsername(recruiterDTO.getUsername()) != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        recruiterService.save(recruiterDTO,true);
        return ResponseEntity.ok(new MessageResponse("Recruiter has been added successfully"));
    }

    @Operation(summary = "Update Recruiter", description = "Update Recruiter by finding Recruiter id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = RecruiterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(path = "/update")
    public ResponseEntity<RecruiterDTO> updateRecruiter(@RequestBody RecruiterDTO recruiterDTO) {
        return new ResponseEntity<>(recruiterService.save(recruiterDTO, false),HttpStatus.OK);
    }

    @Operation(summary = "Delete Recruiter", description = "Delete Recruiter by Recruiter id", responses = {
            @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteRecruiterById(@PathVariable Long id) {
        return new ResponseEntity<>(recruiterService.delete(id),HttpStatus.OK);
    }

    @Operation(summary = "Restore deleted Recruiter", description = "Restore deleted Recruiter by Recruiter id", responses = {
        @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping(path = "/restore/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> restoreRecruiterById(@PathVariable Long id) {
        return new ResponseEntity<>(recruiterService.restore(getByID(id).getBody()),HttpStatus.OK);
    }

    @Operation(summary = "Restore deleted Recruiter", description = "Restore deleted Recruiter by Email", responses = {
        @ApiResponse(responseCode = "200", description = "OK",content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/restore/{email}")
    public ResponseEntity<MessageResponse> restoreRecruiterByEmail(@PathVariable String email) {
        if(recruiterService.existsByEmail(email)){
            recruiterService.restore(recruiterService.findByEmail(email));
            return ResponseEntity.ok(new MessageResponse("Recruiter has been restored successfully"));
        }
        return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Failed to restore Recruiter"));
    }
}
