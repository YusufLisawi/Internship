package com.nttdata.beca.controller;

import com.nttdata.beca.config.jwt.JwtUtils;
import com.nttdata.beca.config.services.UserDetailsImpl;
import com.nttdata.beca.config.services.UserDetailsServiceImpl;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.dto.DocsDTO;
import com.nttdata.beca.service.impl.DocsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocsController.class)
@ExtendWith(SpringExtension.class)
public class DocsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @SpyBean
    private JwtUtils jwtUtils;
    private String jwt;

    @MockBean
    DocsServiceImpl docsService;
    private DocsDTO docsDTO, docsDTO1, docsDTO2, docsDTO_up;
    private List docsList;
    private long docsId = 1L;
    private String docsType = "CV";
    private String docsPath = "path/to/file";

    @BeforeEach
    public void setUp() {
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

       docsDTO = DocsDTO.builder()
                .docsId(1l)
                .type("TYPE_CV")
                .path("path/to/file1")
               .internship(InternshipDTO.builder().internshipId(1l).build())
                .build();
        docsDTO1 = DocsDTO.builder()
                .docsId(2l)
                .type("TYPE_CIN")
                .path("path/to/file2")
                .internship(InternshipDTO.builder()
                        .internshipId(2l)
                        .build())
                .build();
        docsDTO2 = DocsDTO.builder()
                .docsId(3l)
                .type("TYPE_CV")
                .path("path/to/file3")
                .internship(InternshipDTO.builder()
                        .internshipId(2l)
                        .build())
                .build();
        docsDTO_up = DocsDTO.builder()
                .type("TYPE_CV")
                .path("path/to/file4")
                .internship(InternshipDTO.builder()
                        .internshipId(2l)
                        .build())
                .build();
        docsList= new ArrayList<>();
        docsList.add(docsDTO);
    }

    @Test
    public void getDocsList() throws Exception {
        given(docsService.findAll()).willReturn(docsList);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/docs/all")
                .accept(MediaType.APPLICATION_JSON);

        ResultActions response = mockMvc.perform(request);

        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(docsList.size())));

        verify(docsService).findAll();
        verify(docsService,times(1)).findAll();
    }

    @Test
    public void getDocById() throws Exception {
        given(docsService.findDocsById(docsDTO.getDocsId())).willReturn(docsDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/docs/find/{docsId}", docsId)
                .accept(MediaType.APPLICATION_JSON);

        ResultActions response = mockMvc.perform(request);

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.type", is(docsDTO.getType())))
                .andExpect(jsonPath("$.path", is(docsDTO.getPath())));
        verify(docsService).findDocsById(docsDTO.getDocsId());
        verify(docsService,times(1)).findDocsById(docsDTO.getDocsId());
    }

    @Test
    public void getInternshipDocs() throws Exception {
        List<DocsDTO> docs = new ArrayList<DocsDTO>();
        docs.add(docsDTO1);
        docs.add(docsDTO2);

        given(docsService.findByInternshipId(docsDTO1.getInternship().getInternshipId())).willReturn(docs);

        ResultActions response = mockMvc.perform(get("/api/docs/findByInternship/{Id}",
                docsDTO1.getInternship().getInternshipId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(docs.size())));
    }

    @Test
    public void addDocument() throws Exception {
        given(docsService.save(docsDTO)).willReturn(docsDTO);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/docs/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(docsDTO)));

        response.andDo(print()).
                andExpect(status().isOk());

        verify(docsService).saveDocs(docsDTO);
        verify(docsService,times(1)).saveDocs(docsDTO);
    }

    @Test
    public void deleteDocById() throws Exception {
        willDoNothing().given(docsService).deleteById(docsId);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/docs/delete/{id}", docsId)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt);

        ResultActions response = mockMvc.perform(request);

        response.andExpect(status().isOk())
                .andDo(print());

        verify(docsService).deleteById(docsId);
        verify(docsService,times(1)).deleteById(docsId);
    }

    @Test
    public void deleteAllDocsByInternshipId() throws Exception {
        Long internshipId = docsDTO.getInternship().getInternshipId();

        willDoNothing().given(docsService).deleteAllDocsByInternshipId(internshipId);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/docs/deleteAll/{internshipId}", internshipId)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt);;

        ResultActions response = mockMvc.perform(request);

        response.andExpect(status().isOk())
                .andDo(print());

        verify(docsService).deleteAllDocsByInternshipId(internshipId);
        verify(docsService,times(1)).deleteAllDocsByInternshipId(internshipId);
    }

    @Test
    public void updateDocument() throws Exception {
        given(docsService.findById(any())).willReturn(docsDTO);

        ResultActions response = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/docs/update")
                .content(asJsonString(docsDTO_up))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}