package com.nttdata.beca.controller;

import com.nttdata.beca.controller.FileController;
import com.nttdata.beca.dto.File;
import com.nttdata.beca.service.impl.FileServiceImpl;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(locations = "src/main/resources")
@ExtendWith(SpringExtension.class)
public class FileControllerTest {

    @Autowired
    MockMvc mockMvc ;
    @Mock
    FileServiceImpl fileService = new FileServiceImpl();
    @InjectMocks
    FileController fileController;
    private File file;
    private long internshipId=3l;
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
        file = File.builder()
                .filename("1680696052095_certificate-demo_3.pdf")
                .url("http://localhost:8080/api/file/find/1680696052095_certificate-demo_3.pdf")
                .build();
    }
    @Test
    public void FindById_test() throws Exception {
        given(fileService.findById(internshipId)).willReturn(file.getFilename());
        ResultActions response = mockMvc.perform(get("/api/file/findById/{internshipId}",internshipId)
                .contentType(MediaType.ALL));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message", is(file.getFilename())));
    }
}
