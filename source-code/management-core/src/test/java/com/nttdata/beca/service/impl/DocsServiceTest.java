package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.DocsDTO;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Docs;
import com.nttdata.beca.repository.DocsRepository;
import com.nttdata.beca.transformer.DocsTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocsServiceTest {

    @Mock
    private DocsRepository docsRepository;

    @InjectMocks
    private DocsServiceImpl docsService;

    @InjectMocks
    private DocsTransformer docsTransformer;

    private Docs docs;
    private DocsDTO docsDTO;
    private long docsId = 1L;
    private long internshipId = 2L;
    private String docsType = "TYPE_CV";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        docsDTO = DocsDTO.builder()
                .docsId(docsId)
                .type(docsType)
                .internship(InternshipDTO.builder()
                        .internshipId(1l)
                        .build())
                .build();
        docs = docsTransformer.toEntity(docsDTO);
    }

    @Test
    public void saveDocs_Test() {
        Mockito.when(docsRepository.save(docs)).thenReturn(docs);

        DocsDTO SaveDocs = docsService.saveDocs(docsDTO);

        assertThat(SaveDocs).isNotNull();
    }

    @Test
    public void findAll_Test() {
        when(docsRepository.findAll()).thenReturn(List.of(docs));
        List<DocsDTO> AllDocs=docsService.findAll();
        assertThat(AllDocs).isNotNull();
        assertThat(AllDocs.size()).isEqualTo(1);
    }

    @Test
    public void findByCandidateId_Test() {
        when(docsRepository.findByInternship_InternshipId(anyLong()))
                .thenReturn(List.of(docs));
        List<DocsDTO> foundDocs = docsService.findByInternshipId(internshipId);
        assertThat(foundDocs).isNotNull();
        assertThat(foundDocs.size()).isEqualTo(1);
        assertThat(foundDocs.get(0).getType()).isEqualTo(docsType);
    }

    @Test
    public void findDocsById_Test() {
        when(docsRepository.findDocsById(anyLong())).thenReturn(docs);
        DocsDTO foundDocs = docsService.findDocsById(docsId);
        assertThat(foundDocs).isNotNull();
    }

    @Test
    public void deleteDocsById_Test() {
        when(docsRepository.findById(anyLong())).thenReturn(Optional.of(docs));
        docsService.deleteDocsById(docsId);
    }

    @Test
    public void deleteAllDocsByCandidateId_Test() {
        when(docsRepository.findById(anyLong())).thenReturn(Optional.of(docs));
        docsService.deleteAllDocsByInternshipId(internshipId);
    }

}