package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Session;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BecaApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CandidateRepositoryTest {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    SessionRepository sessionRepository;
    private Session session,session1,session2;
    private Candidate candidate, candidate1,candidate2;

    @Before
    public void setUp() {

        session = Session.builder()
                .sessionId(1l)
                .sessionName("session1")
                .sessionDate(new Date(2022, 8, 4))
                .admittedNumber(10)
                .sessionStatus("current")
                .dayInterviewNumber(3)
                .technology("java")
                .recruiter(Recruiter.builder()
                        .recruiterId(1L)
                        .build())

                .build();
        session1 = Session.builder()
                .sessionId(2l)
                .sessionName("session2")
                .sessionDate(new Date(2022, 8, 11))
                .admittedNumber(10)
                .sessionStatus("closed")
                .dayInterviewNumber(3)
                .technology("Python")
                .recruiter(Recruiter.builder()
                        .recruiterId(1L)
                        .build())

                .build();
        session2 = Session.builder()
                .sessionId(3l)
                .sessionName("session3")
                .sessionDate(new Date(2022, 8, 5))
                .admittedNumber(10)
                .sessionStatus("Previous")
                .dayInterviewNumber(3)
                .technology("C")
                .recruiter(Recruiter.builder()
                        .recruiterId(1L)
                        .build())

                .build();

    }
    @DisplayName("save Candidate")
    @Test
    public void BeforeAll_Candidate_save() {
        Session SaveSession = sessionRepository.save(session);
        Session SaveSession1 = sessionRepository.save(session1);
        Session SaveSession2 = sessionRepository.save(session2);

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
                .session(SaveSession)
                .finalAdmissionStatus("true")
                .interview(null)
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .deleted(false)
                .build();

        candidate2 = Candidate.builder()
                .candidateId(2l)
                .email("mariameBen@gmail,com")
                .firstName("mariame")
                .lastName("ben")
                .anapec("false")
                .diplome("Bac+3")
                .city("Tanger")
                .university("ENSATE")
                .CVname("CV-RABAB EL MEHDY.pdf")
                .gender("female")
                .session(SaveSession1)
                .finalAdmissionStatus("true")
                .preselectionStatus(true)
                .interview(null)
                .phoneNumber("0655407151")
                .preselectionStatus(true)
                .deleted(false)
                .build();

            candidate1 =Candidate.builder()
                    .candidateId(2L)
                    .firstName("sara")
                    .lastName("elmakoudi")
                    .email("fatima@gmail.com")
                    .phoneNumber("065421368")
                    .city("Rabat")
                    .diplome("Bac+5")
                    .anapec("false")
                    .CVname("rababCv.pdf")
                    .gender("female" )
                    .preselectionStatus(true)
                    .finalAdmissionStatus("true")
                    .university("FS")
                    .session(SaveSession2)
                    .build();

        Candidate saveCandidate= candidateRepository.save(candidate);
        assertThat(saveCandidate).isNotNull();
        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);

    }


    @DisplayName("find by Email")
    @Test
    public void  Find_ByEmail(){

        Candidate findByEmail = candidateRepository.findByEmail("rababelmehdy@gmail,com").get(0);
        assertThat(findByEmail).isNotNull();

    }
    @DisplayName("find by Session")
    @Test
    public void  Find_ByEmail_BySession(){

        List<Candidate> findBySession = candidateRepository.findBySession(1L);
        assertThat(findBySession).isNotNull();

    }
    @DisplayName("Get current SESSION Candidates")
    @Test
    public void  Get_CurrentSession_Candidates(){

        List<Candidate>  CurrentSessionCandidate = candidateRepository.getCurrentSessionCandidates();
        assertThat(CurrentSessionCandidate).isNotNull();
        assertThat(CurrentSessionCandidate.size()).isEqualTo(1);
    }

    @DisplayName("List of Admitted Candidates")
    @Test
    public void ListOf_AdmittedCandidates() {

        List<Candidate> listAdmitted = candidateRepository.getAdmitted();
        assertThat(listAdmitted).isNotNull();

    }
    @DisplayName(" Final List")
    @Test
    public void Get_FinalList() {

        List<Candidate> listFinal = candidateRepository.getFinalList(3);
        assertThat(listFinal).isNotNull();

    }
    @DisplayName("Waiting Candidates")
    @Test
    public void ListOf_WaitingCandidates() {

        List<Candidate> listWaiting = candidateRepository.getWaiting(2,2);
        assertThat(listWaiting).isNotNull();

    }


    @DisplayName("Number total of Candidates")
    @Test
    public void Get_NumberOfCandidate() {
        long NumberOfCandidate = candidateRepository.getTotalNumber();
        assertThat(NumberOfCandidate).isEqualTo(1);
    }


    @DisplayName("Number of preSelected Candidates ")
    @Test
    public void Get_NumberOfPreselected_Candidates() {
        long preSelected = candidateRepository.preselectedCount();
        assertThat(preSelected).isEqualTo(1);
    }


    @DisplayName("Number  of admitted Candidates")
    @Test
    public void Get_NumberOfAdmitted_Candidate() {
        long Admitted = candidateRepository.admittedCount();
        assertThat(Admitted).isEqualTo(1);
    }


    /*@DisplayName("Number of candidates in session ")
    @Test
    public void Get_NumberOfCandidates_Session() {
        long Session_Candidates = candidateRepository.countInSession(session.getSessionId());
        assertThat(Session_Candidates).isEqualTo(1);
    }
*/

    @DisplayName("Email Count ")
    @Test
    public void Email_Count() {
        long Count = candidateRepository.countEmail("rababelmehdy@gmail,com");
        assertThat(Count).isEqualTo(1);
    }
    @DisplayName("List Of cities")
    @Test
    public void ListOf_Cities() {

        List<String> cities = candidateRepository.findAllCities();
        assertThat(cities).isNotNull();
        assertThat(cities).isEqualTo(List.of("Tetouan","Rabat","Tanger"));
    }
    @DisplayName("List Of Universities")
    @Test
    public void ListOf_Universities() {

        List<String> Universities = candidateRepository.findAllUniversities();
        assertThat(Universities).isNotNull();
        assertThat(Universities).isEqualTo(List.of("ENA","FS","ENSATE"));
    }
    @DisplayName("List Of Diplomas")
    @Test
    public void ListOf_Diplomas() {

        List<String> Diplomas = candidateRepository.findAllDiplomas();
        assertThat(Diplomas).isNotNull();
        assertThat(Diplomas).isEqualTo(List.of("Bac+4","Bac+5","Bac+3"));
    }
    @Test
    public void k_DeletedAll(){

        candidateRepository.deleteAll();
        sessionRepository.deleteAll();


    }
}
