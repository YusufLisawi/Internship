package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Score;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.CandidateRepository;
import com.nttdata.beca.repository.SessionRepository;
import com.nttdata.beca.service.impl.CandidateServiceImpl;
import com.nttdata.beca.transformer.CandidateTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.CrudRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {
   @Mock
    private CandidateRepository candidateRepository;
   @Mock
   private SessionRepository sessionRepository;
   @Mock
    CrudRepository crudRepository;
   @InjectMocks
   private CandidateTransformer candidateTransformer;
   @InjectMocks
    private CandidateServiceImpl candidateService;
    private Candidate candidate, candidate1;
    private CandidateDTO candidateDTO;

    @Before
    public void SetUp(){

        MockitoAnnotations.initMocks(this);
        candidate = Candidate.builder()
                .candidateId(1l)
                .email("rababelmehdy@gmail,com")
                .firstName("rabab")
                .lastName("mhdy")
                .anapec("false")
                .diplome("Bac+4")
                .city("Tetouan")
                .university("ENA")
                .CVname("CV-RABAB EL MEHDY.pdf")
                .gender("female")
                .session(null)
                .finalAdmissionStatus("true")
                .preselectionStatus(true)
                .interview(null)
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .deleted(false)
                .build();



        candidate1 = new Candidate(2L, "sara", "elmakoudi", "female", "fatima@gmail.com", "065421368", "Rabat", "true", "rababCv.pdf", "Bac+5", "true" , true, "FS", null, null,null, null,null,null, true);
        candidateDTO=candidateTransformer.toDTO(candidate);
    }
    @Test
    public void ShouldSaveCandidate_Successfully(){

        Mockito.when(candidateRepository.save(candidate)).thenReturn(candidate);
        candidateService.save(candidateDTO);


        verify(candidateRepository,times(1)).save(candidate);
    }

    @DisplayName(" testing findByEmail method ")
    @Test
    public void FindByEmailMethod_Test() {
    String  Email = "rababelmehdy@gmail,com";

   Mockito.when(candidateRepository.findByEmail(anyString())).thenReturn(List.of(candidate));

   List<CandidateDTO> findByEmail = candidateService.findByEmail(Email);

   assertThat(findByEmail).isNotNull();
   assertThat(findByEmail.get(0).getEmail()).isEqualTo(Email);
}

    @DisplayName("testing getCurrentSessionCandidates method negative scenario")
    @Test
    public void Get_CurrentSessionCandidates_Test_N(){

    Mockito.when(candidateRepository.getCurrentSessionCandidates()).thenReturn(Collections.emptyList());
    List<CandidateDTO> getCurrentSessionCandidates = candidateService.getCurrentSessionCandidates();

    assertThat(getCurrentSessionCandidates).isEmpty();
    assertThat(getCurrentSessionCandidates.size()).isEqualTo(0);
}

    @DisplayName("testing getCurrentSessionCandidates method")
    @Test
    public void Get_CurrentSessionCandidates_Test(){

    Mockito.when(candidateRepository.getCurrentSessionCandidates()).thenReturn(List.of(candidate1,candidate
    ));
    List<CandidateDTO> getCurrentSessionCandidates = candidateService.getCurrentSessionCandidates();

    assertThat(getCurrentSessionCandidates).isNotNull();
    assertThat(getCurrentSessionCandidates.size()).isEqualTo(2);
        verify(candidateRepository, times(1)).getCurrentSessionCandidates();

}

    @DisplayName("testing findBySession method")
    @Test
    public void FindBySession_Test(){

    Mockito.when(candidateRepository.findBySession(1l)).thenReturn((List.of(candidate)));
    List<CandidateDTO> findBySession = candidateService.findBySession(Session.builder().sessionId(2L).build().getSessionId());
      assertThat(findBySession).isNotNull();
}

    @DisplayName("testing getFinalList method")
    @Test
    public void Get_FinalList_Test(){
    Mockito.when(candidateRepository.getFinalList(5)).thenReturn(List.of(candidate
    ));
    List<CandidateDTO> getFinalList = candidateService.getFinalList(5);

    assertThat(getFinalList).isNotNull();
    assertThat(getFinalList.size()).isEqualTo(1);
}

    @DisplayName("testing getAdmitted method")
    @Test
    public void GetAdmitted_Test(){
        Mockito.when(candidateRepository.getAdmitted()).thenReturn(List.of(candidate1,candidate
        ));
        List<CandidateDTO> getAdmitted = candidateService.getAdmitted();

        assertThat(getAdmitted).isNotNull();
        assertThat(getAdmitted.size()).isEqualTo(2);
    }
    @DisplayName("testing getWaiting method")
    @Test
    public void GetWaiting_Test(){
        Mockito.when(candidateRepository.getWaiting(2,2)).thenReturn(List.of(candidate1
        ));
        List<CandidateDTO> getWaiting = candidateService.getWaiting(2,2);

        assertThat(getWaiting).isNotNull();
        assertThat(getWaiting.size()).isEqualTo(1);
    }
    @DisplayName("testing countInSession method")
    @Test
    public  void  CountInSession_Test(){
        Mockito.when(candidateRepository.countInSession(1L)).thenReturn(2l);

        long countInSession = candidateService.countInSession(1l);
        assertThat(countInSession).isEqualTo(2l);
    }
    @DisplayName("testing preselectedCount method")
    @Test
    public  void  PreselectedCount_Test(){

        when(candidateRepository.preselectedCount()).thenReturn(1l);
        long preselectedCount = candidateService.preselectedCount();
        assertThat(preselectedCount).isEqualTo(1l);
    }
    @DisplayName("testing admittedCount method")
    @Test
    public  void  AdmittedCount_Test(){
        Mockito.when(candidateRepository.admittedCount()).thenReturn(2l);

        long admittedCount = candidateService.admittedCount();
        assertThat(admittedCount).isEqualTo(2l);
    }
    @DisplayName("testing countEmail method")
    @Test
    public  void  CountEmail_Test(){
        String  Email = "fatima@gmail.com";
        Mockito.when(candidateRepository.countEmail(Email)).thenReturn(1l);

        long countEmail = candidateService.countEmail(Email);
        assertThat(countEmail).isEqualTo(1l);
    }
    @DisplayName("testing findAllUniversities method")
    @Test
    public  void  FindAllUniversities_Test(){

        Mockito.when(candidateRepository.findAllUniversities()).thenReturn(List.of("ENSA","ENA"));

        List AllUniversities = candidateService.findAllUniversities();
        assertThat(AllUniversities).isNotNull();
        assertThat(AllUniversities.size()).isEqualTo(2);
    }
    @DisplayName("Testing findAllCities method")
    @Test
    public  void  FindAllCities_Test(){

        Mockito.when(candidateRepository.findAllCities()).thenReturn(List.of("Tétouan","Rabat","Tanger","Fés"));

        List AllCities = candidateService.findAllCities();
        assertThat(AllCities).isNotNull();
        assertThat(AllCities.size()).isEqualTo(4);
    }


}

