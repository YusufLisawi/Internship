package com.nttdata.beca.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.nttdata.beca.config.services.EmailService;
import com.nttdata.beca.exception.records.ErrorResponse;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.nttdata.beca.config.jwt.JwtUtils;
import com.nttdata.beca.config.request.LoginRequest;
import com.nttdata.beca.config.response.JwtResponse;
import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.repository.RecruiterRepository;
import com.nttdata.beca.config.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "The Authentication API")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RecruiterRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Operation(summary = "Login", description = "Check if a user can logged in successfully", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws MessagingException, TemplateException, IOException {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if(!userDetails.getUsername().isEmpty() && !userDetails.getUsername().isBlank()){
                if(userRepository.findByUsername(userDetails.getUsername()).isDeleted()){
                    return ResponseEntity.badRequest().body("This account has being disabled you have to contact the administration.");
                }
            }

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().body("Invalid Username/Password");
        }
    }

    @Operation(summary = "Logged In ", description = "Check if the user is logged in or not", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/authenticated")
    public ResponseEntity<?> isLoggedIn(@RequestBody JwtResponse jwtResponse) {
        if (jwtResponse.getId() != null && jwtResponse.getAccessToken() != null) {
            try {
                String username = jwtUtils.getUserNameFromJwtToken(jwtResponse.getAccessToken());
                if (username != null) {
                    Recruiter recruiter = userRepository.findRecruiterByRecruiterId(jwtResponse.getId());
                    if (recruiter != null && recruiter.getUsername().equals(username) && !recruiter.isDeleted()) {
                        return ResponseEntity.ok(true);
                    }
                    else if(recruiter != null && recruiter.getUsername().equals(username) && recruiter.isDeleted()){
                        return ResponseEntity.badRequest().body("Your account has being disabled !! please contact the administration.");
                    }
                }
            }catch (Exception e){
                return ResponseEntity.badRequest().body("Your session has expired please login again.");
            }
        }
        return ResponseEntity.badRequest().body(false);
    }

    @Operation(summary = "Logged In ", description = "Check if the user is logged in or not", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (userRepository.existsByEmail(email)) {
            if(userRepository.findByEmail(email).isDeleted()){
                return ResponseEntity.badRequest().body("This account has been disabled please contact the administration.");
            }
            try {
                emailService.sendEmail(userRepository.findByEmail(email));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error in the server : " + e.getMessage());
            }
            return ResponseEntity.ok(new MessageResponse("Check your inbox"));
        }
        return ResponseEntity.badRequest().body("No user exists with this email !");
    }

    @Operation(summary = "Logged In ", description = "Check if the user is logged in or not", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(path = "/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String password) {
        try {
            Recruiter recruiter = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
            if (recruiter != null && !recruiter.isDeleted()) {
                recruiter.setPassword(encoder.encode(password));
                userRepository.save(recruiter);
                return ResponseEntity.ok(new MessageResponse("Password reseted successfully"));
            }
            else{
                return ResponseEntity.badRequest().body("This account has been disabled please contact the administration.");
            }
        } catch (Exception exception) {
        }
        return ResponseEntity.badRequest().body("Token Invalid!");
    }
}
