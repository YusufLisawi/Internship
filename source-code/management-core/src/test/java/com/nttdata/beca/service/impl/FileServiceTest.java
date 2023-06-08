package com.nttdata.beca.service.impl;

import com.nttdata.beca.controller.FileController;
import com.nttdata.beca.dto.File;
import com.nttdata.beca.service.FileService;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileServiceImpl fileService = new FileServiceImpl();
    private final long internshipId = 1l;
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testFindById() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        FileServiceImpl fileServiceMock = mock(FileServiceImpl.class);
        List<File> files = Arrays.asList(
                new File("1680696672095_certificate-demo_1.pdf", "http://localhost:8080/api/file/find/1680696672095_certificate-demo_1.pdf"),
                new File("1680435452095_certificate-demo_2.pdf", "http://localhost:8080/api/file/find/1680435452095_certificate-demo_2.pdf"),
                new File("1680696052095_certificate-demo_3.pdf", "http://localhost:8080/api/file/find/1680696052095_certificate-demo_3.pdf")
        );
        when(fileServiceMock.loadAll()).thenReturn(files.stream().map(file -> Paths.get(file.getFilename())));
        String result1 = fileService.findById(internshipId);
        assertEquals(files.get(0).getFilename(), result1);
    }
}