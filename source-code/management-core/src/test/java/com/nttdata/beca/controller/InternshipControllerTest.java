package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nttdata.beca.config.jwt.JwtUtils;
import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.config.services.UserDetailsImpl;
import com.nttdata.beca.config.services.UserDetailsServiceImpl;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import com.nttdata.beca.service.impl.InternshipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InternshipController.class)
@RunWith(SpringRunner.class)
class InternshipControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InternshipServiceImpl internshipService;


    private InternshipDTO internship, internship2;
    List<InternshipDTO> internships;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @SpyBean
    private JwtUtils jwtUtils;
    private String jwt;


    @BeforeEach
    void setUp() {
        Role role = Role.builder()
                .name(ERole.ROLE_ADMIN)
                .build();
        Recruiter recruiter = Recruiter.builder()
                .recruiterId(1L)
                .email("admin@gmail.com")
                .deleted(false)
                .role(List.of(role))
                .username("admin")
                .lastName("User")
                .firstName("Admin")
                .password("admin@123")
                .build();
        UserDetailsImpl user = UserDetailsImpl.build(recruiter);
        given(userDetailsService.loadUserByUsername(any())).willReturn(user);
        Authentication authentication = new TestingAuthenticationToken(user, "");
        jwt = jwtUtils.generateJwtToken(authentication);

        internship = InternshipDTO.builder()
                .internshipId(1l)
                .internshipRating(12)
                .reportRating(12)
                .internshipStatus("in progress")
                .candidate(null)
                .session(null)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .deleted(false)
                .subject("springBoot&angular")
                .build();
        internship2 = InternshipDTO.builder()
                .internshipId(2l)
                .internshipRating(12)
                .reportRating(12)
                .internshipStatus("in progress")
                .candidate(null)
                .session(null)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .deleted(false)
                .subject("springBoot&angular")
                .build();
        internships = Arrays.asList(internship, internship2);
    }

    @Test
    void Should_getInternshipList() throws Exception {

        given(internshipService.getAllInternships()).willReturn(internships);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/internship/all")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(internships.size())))
                .andReturn();
    }


    @Test
    void Should_getCurrentSessionInternshipList() throws Exception {
        given(internshipService.getCurrentSessionInternships()).willReturn(internships);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/internship/allCurrentInternshipSession")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(internships.size())))
                .andReturn();
    }

    @Test
    void Should_getOneById() throws Exception {
        given(internshipService.getInternshipsById(anyLong())).willReturn(internship);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/internship/find/{internshipId}", internship.getInternshipId())
                .accept(MediaType.APPLICATION_JSON);

        Long internshipId = internship.getInternshipId();

        MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.internshipId", is(Integer.parseInt(internshipId.toString()))))
                .andReturn();
    }

    @Test
    void Should_addInternship() throws Exception {

        given(internshipService.addInternship(any(InternshipDTO.class))).willReturn(internship);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/internship/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JsonMapper().registerModule(new JavaTimeModule()).writeValueAsString(internship));

        Long internshipId = internship.getInternshipId();

        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internshipId", is(Integer.parseInt(internshipId.toString()))))
                .andReturn();
    }

    @Test
    void Should_deleteById() throws Exception {
        given(internshipService.deleteInternshipById(anyLong())).willReturn(new MessageResponse("Internship has been deleted successfully"));


        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/internship/delete/{internshipId}", internship.getInternshipId())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt);

        Long internshipId = internship.getInternshipId();

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void Should_updateInternship() throws Exception {

        given(internshipService.updateInternship(any(InternshipDTO.class))).willReturn(internship);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/internship/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JsonMapper().registerModule(new JavaTimeModule()).writeValueAsString(internship));

        Long internshipId = internship.getInternshipId();

        MvcResult response = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internshipId", is(Integer.parseInt(internshipId.toString()))))
                .andReturn();
    }
}