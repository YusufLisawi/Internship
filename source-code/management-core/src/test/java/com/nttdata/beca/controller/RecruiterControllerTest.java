package com.nttdata.beca.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.RoleDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import com.nttdata.beca.repository.RoleRepository;
import com.nttdata.beca.service.impl.RecruiterServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class RecruiterControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Mock
    RecruiterServiceImpl recruiterService;
    @InjectMocks
    RecruiterController recruiterController;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;


    private Recruiter recruiter;
    private List<RecruiterDTO> recruiterDTOList;
    private List<Role> rolesList;
    private Role role;
    private RecruiterDTO recruiterDTO,recruiterDTO_Up;
    private long recruiterId=5l;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(recruiterController).build();

        recruiterDTO = RecruiterDTO.builder()
                .recruiterId(5L)
                .email("rababbbbbb@gmail,com")
                .username("rababmehdy")
                .firstName("rabab")
                .lastName("elmehdy")
                .phoneNumber("0654407151")
                .picture("pic.png")
                .role((List<RoleDTO>) role)
                .build();

        recruiterDTO_Up = RecruiterDTO.builder()
                .recruiterId(5L)
                .email("rababbbbbb@gmail,com")
                .username("rabab_el_mehdy")
                .firstName("rabab")
                .lastName("el_mehdy")
                .phoneNumber("0654407151")
                .picture("pic.png")
                .role((List<RoleDTO>) role)
                .build();

        recruiter = Recruiter.builder()
                .recruiterId(2L)
                .email("rababelmehdy123@gmail,com")
                .username("rabab_mehdy")
                .firstName("rabab")
                .lastName("elmehdy")
                .password(this.passwordEncoder.encode("123456789"))
                .phoneNumber("0654407151")
                .picture("pic.png")
                .build();

        role = Role.builder()
                 .name(ERole.ROLE_ADMIN)
        .build();

        rolesList = new ArrayList<>();
        rolesList.add(role);

      recruiterDTOList=new ArrayList<>();
      recruiterDTOList.add(recruiterDTO);



    }

   @Test
   public void Get_allRecruiter_Test() throws Exception {
       when(recruiterService.findAll()).thenReturn(recruiterDTOList);
       ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/recruiter/all"));
       // then - verify the output
       response.andExpect(status().is(200))
               .andDo(print())
               .andExpect(jsonPath("$.size()", is(recruiterDTOList.size())));

       verify(recruiterService).findAll();
       verify(recruiterService,times(1)).findAll();
   }
   @Test
    public void FindRecruiterBYId_Test() throws Exception {
    when(recruiterService.findById(recruiterId)).thenReturn(recruiterDTO);
       ResultActions response = mockMvc.perform(get("/api/recruiter/find/{id}",recruiterId));

       // then - verify the output
       response.andExpect(status().isOk())
               .andDo(print())
               .andExpect(jsonPath("$.firstName", is(recruiterDTO.getFirstName())))
               .andExpect(jsonPath("$.lastName", is(recruiterDTO.getLastName())))
               .andExpect(jsonPath("$.email", is(recruiterDTO.getEmail())));

   }

    @Test
    public void Get_allRoles_Test() throws Exception {

        when(roleRepository.findAll()).thenReturn(rolesList);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/recruiter/all-roles"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(rolesList.size())));


    }

    @Test
    public void Get_allUsers_Test() throws Exception {

        when(recruiterService.findUsers()).thenReturn(recruiterDTOList);
        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/recruiter/find-users"));
        // then - verify the output
        response.andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(recruiterDTOList.size())));

        verify(recruiterService).findUsers();
        verify(recruiterService,times(1)).findUsers();
    }


    @Test
    public void givenRecruiterObject_whenCreateRecruiter_thenReturnSavedRecruiter() throws Exception {

        when(recruiterService.save(any())).thenReturn(recruiterDTO);
        ResultActions response=this.mockMvc.perform(MockMvcRequestBuilders.post("/api/recruiter/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recruiterDTO)));
        response.andDo(print()).
                andExpect(status().isOk());



    }


    @Test
    public void UpdateRecruiter_Test() throws Exception {
        when(recruiterService.findById(2L)).thenReturn(recruiterDTO);

        ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/recruiter/update")
                .content(asJsonString(recruiterDTO_Up))
                .contentType(MediaType.APPLICATION_JSON)
               );

        //return
        response.andExpect(status().isOk())
                .andDo(print());


    }
    @Test
    public void DeleteById_Test() throws Exception {
        willDoNothing().given(recruiterService).deleteById(recruiterId);
        ResultActions response = mockMvc.perform(delete("/api/recruiter/delete/{id}",recruiterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recruiterDTO)));

        // then - verify the output
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
