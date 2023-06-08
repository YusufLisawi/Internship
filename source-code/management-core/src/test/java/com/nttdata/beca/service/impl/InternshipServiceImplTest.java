package com.nttdata.beca.service.impl;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Internship;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.InternshipRepository;
import com.nttdata.beca.transformer.InternshipTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternshipServiceImplTest {

    @Mock
    private InternshipRepository internshipRepository;

    @InjectMocks
    private InternshipTransformer internshipTransformer;

    @InjectMocks
    private InternshipServiceImpl internshipService;

    private Internship internship, internship2;


    @BeforeEach
    public void setUp() {
        internship = Internship.builder()
                .internshipId(1l)
                .candidate(null)
                .session(null)
                .startDate(new Date(2023, 02, 06).toLocalDate())
                .endDate(new Date(2023, 02, 26).toLocalDate())
                .deleted(false)
                .subject("springBoot&angular")
                .build();
        internship2 = Internship.builder()
                .internshipId(2l)
                .candidate(null)
                .session(null)
                .startDate(new Date(2023, 02, 06).toLocalDate())
                .endDate(new Date(2023, 02, 26).toLocalDate())
                .deleted(false)
                .subject("RPA")
                .build();
    }

    @Test
    void getAllInternships_Basic() {
        when(internshipRepository.findAll()).thenReturn(List.of(internship, internship2));

        List<InternshipDTO> internships = internshipService.getAllInternships();

        assertThat(internships).isNotEmpty();
        assertThat(internships.size()).isEqualTo(2);
        verify(internshipRepository).findAll();
    }

    @Test
    void getAllInternships_Empty() {
        when(internshipRepository.findAll()).thenReturn(Collections.emptyList());

        List<InternshipDTO> internships = internshipService.getAllInternships();

        assertThat(internships).isEmpty();
        assertThat(internships.size()).isEqualTo(0);
        verify(internshipRepository).findAll();

    }


    @Test
    void getCurrentSessionInternships_Basic() {
        when(internshipRepository.getCurrentSessionInternships()).thenReturn(List.of(internship, internship2));

        List<InternshipDTO> internships = internshipService.getCurrentSessionInternships();

        assertThat(internships).isNotEmpty();
        assertThat(internships.size()).isEqualTo(2);
        verify(internshipRepository).getCurrentSessionInternships();
    }

    @Test
    void getCurrentSessionInternships_Empty() {
        when(internshipRepository.getCurrentSessionInternships()).thenReturn(Collections.EMPTY_LIST);

        List<InternshipDTO> internships = internshipService.getCurrentSessionInternships();

        assertThat(internships).isEmpty();
        assertThat(internships.size()).isEqualTo(0);
        verify(internshipRepository).getCurrentSessionInternships();
    }

    @Test
    void getInternshipsById_Basic() {
        long id = internship.getInternshipId();
        when(internshipRepository.findById(anyLong())).thenReturn(internship);

        InternshipDTO internship = internshipService.getInternshipsById(id);

        assertThat(internship.getInternshipId()).isEqualTo(this.internship.getInternshipId());
        verify(internshipRepository).findById(id);
    }

    @Test
    void getInternshipsById_NotFound() {

        long id = internship.getInternshipId();

        when(internshipRepository.findById(anyLong())).thenReturn(internship2);

        InternshipDTO internship = internshipService.getInternshipsById(id);

        assertThat(internship.getInternshipId()).isNotEqualTo(id);
        verify(internshipRepository).findById(id);
    }

    @Test
    void addInternship_Added() throws Exception {
        when(internshipRepository.save(any(Internship.class))).thenReturn(internship);

        InternshipDTO actual = internshipService.addInternship(new InternshipDTO());
        assertThat(actual).usingRecursiveComparison().isEqualTo(internship);
        verify(internshipRepository, times(1)).save(any(Internship.class));

    }


    @Test
    void addInternship_AlreadyExists() throws Exception {

        internship.setCandidate(Candidate.builder().candidateId(1l).build());
        when(internshipRepository.getCurrentSessionInternships()).thenReturn(List.of(internship));

        InternshipDTO internshipDTO = internshipTransformer.toDTO(internship);


        Exception exception = assertThrows(Exception.class, () -> {
            internshipService.addInternship(internshipDTO);
        });

        String expectedMessage = "This Candidate is already exists in the current session !!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(internshipRepository, times(1)).getCurrentSessionInternships();

    }

    @Test
    void updateInternship() {
        when(internshipRepository.findById(anyLong())).thenReturn(internship);
        when(internshipRepository.save(any(Internship.class))).thenReturn(internship);

        InternshipDTO actual = internshipService.updateInternship(internshipTransformer.toDTO(internship));
        assertThat(actual).usingRecursiveComparison().isEqualTo(internship);
        verify(internshipRepository, times(1)).save(any(Internship.class));

    }

    @Test
    void deleteInternshipById() {
        MessageResponse expectedMessageResponse = new MessageResponse("Internship has been deleted successfully");

        when(internshipRepository.findById(anyLong())).thenReturn(internship);
        MessageResponse actualMessageResponse = internshipService.deleteInternshipById(anyLong());

        assertThat(expectedMessageResponse).toString().equals(actualMessageResponse.toString());
        verify(internshipRepository, times(1)).save(any(Internship.class));
        verify(internshipRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(internshipRepository);
    }

}