package com.nttdata.beca.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.config.jwt.JwtUtils;
import com.nttdata.beca.config.request.LoginRequest;
import com.nttdata.beca.config.request.SignupRequest;
import com.nttdata.beca.config.response.JwtResponse;
import com.nttdata.beca.config.services.EmailService;
import com.nttdata.beca.config.services.UserDetailsImpl;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import com.nttdata.beca.repository.RecruiterRepository;
import com.nttdata.beca.repository.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    AuthController authController;
    @Mock
    RecruiterRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    PasswordEncoder encoder;
   @Mock
   JwtUtils jwtUtils;
   @Mock
   EmailService emailService;
   @Mock
   UserDetailsImpl userDetails;
   @Mock
   Authentication authentication;

    LoginRequest loginRequest;
    SignupRequest signupRequest;
    JwtResponse jwtResponse;
    Role role;
    private String email="admin@gmail.com";
    ObjectMapper objectMapper = new ObjectMapper();

    Recruiter recruiter;
    @Before
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        role = Role.builder()
                .name(ERole.ROLE_ADMIN)
                .build();
        signupRequest= SignupRequest.builder()
                .email("rababelmehdy@gmail.com")
                .firstName("rabab")
                .lastName("elmehdy")
                .password("123456789")
                .phoneNumber("0654407151")
                .picture("pic.png")
                .username("rabab")
                .role((List.of(role)))
                .build();
        loginRequest= LoginRequest.builder()
                .username("admin")
                .password("admin@123")
                .build();
        recruiter= Recruiter.builder()
                .recruiterId(1L)
                .email("admin@gmail.com")
                .deleted(false)
                .role(List.of(role))
                .username("admin")
                .lastName("User")
                .firstName("Admin")
                .password("admin@123")
                .build();

        jwtResponse=new JwtResponse("admin",1L,"admin","admin@gmail.com",List.of("admin"));
    }

    @Test
    public void Login_Test() throws JsonProcessingException,Exception {

        when(authenticationManager.authenticate(any())).thenReturn(this.authentication);
        when(authentication.getPrincipal()).thenReturn(this.userDetails);
        when(userDetails.getUsername()).thenReturn("admin");
        when(userRepository.findByUsername(any())).thenReturn(recruiter);


        String JsonRequest = objectMapper.writeValueAsString(loginRequest);

        ResultActions response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonRequest))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /*@Test
    public void Register_Test() throws JsonProcessingException,Exception {
        when(userRepository.findByUsername(signupRequest.getUsername())).thenReturn(null);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        String JsonRequest = objectMapper.writeValueAsString(signupRequest);

        ResultActions response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonRequest))
                .andDo(print())
                .andExpect(status().isOk());

    }*/

    @Test
    public void Authenticated_Test() throws JsonProcessingException,Exception {
when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn("admin");
when(userRepository.findRecruiterByRecruiterId(any())).thenReturn(recruiter);
        String JsonRequest = objectMapper.writeValueAsString(jwtResponse);
        ResultActions response = mockMvc.perform(post("/api/auth/authenticated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonRequest))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void Forgot_Password_Test() throws JsonProcessingException,Exception {
        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(true);
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(recruiter);
       //String messageResponse = ResponseEntity.ok(new MessageResponse("Check your inbox"));
        ResultActions response = mockMvc.perform(get("/api/auth/forgot-password")
                        .param("email","admin@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
   /*@Test
    public void Reset_Password_Test() throws JsonProcessingException,Exception {
        ResultActions response = mockMvc.perform(get("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
*/

}

