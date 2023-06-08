package com.nttdata.beca.repository;


import com.nttdata.beca.entity.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class InternshipRepositoryTest {

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SessionRepository sessionRepository;

    private Session session;
    private Candidate candidate;

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
                .type("internship")
                .recruiter(null)
                .build();


        this.candidate = Candidate.builder()
                .candidateId(1l)
                .email("anass@gmail,com")
                .firstName("anass")
                .lastName("elfarrouh")
                .anapec("false")
                .diplome("Bac+5")
                .city("Tetouan")
                .university("ENSA")
                .CVname("CV")
                .gender("male")
                .session(session)
                .finalAdmissionStatus("")
                .interview(null)
                .phoneNumber("0649133307")
                .preselectionStatus(true)
                .deleted(false)
                .type("internship")
                .build();

    }

    @Test
    public void GetCurrentSessionInternships_exists() {

        sessionRepository.save(session);
        candidateRepository.save(candidate);

        Internship internship;
        internship = Internship.builder()
                .internshipId(1l)
                .candidate(candidate)
                .session(session)
                .startDate(new Date(2023, 02, 06).toLocalDate())
                .endDate(new Date(2023, 02, 26).toLocalDate())
                .deleted(false)
                .subject("spring boot&angular")
                .build();
        internshipRepository.save(internship);

        List<Internship> internshipsFound = internshipRepository.getCurrentSessionInternships();
        assertThat(internshipsFound.get(0).getInternshipId()).isEqualTo(internship.getInternshipId());
    }

    @Test
    public void GetCurrentSessionInternships_notExists() {

        session.setSessionStatus("closed");
        sessionRepository.save(session);
        candidateRepository.save(candidate);

        Internship internship;
        internship = Internship.builder()
                .internshipId(1l)
                .candidate(candidate)
                .session(session)
                .startDate(new Date(2023, 02, 06).toLocalDate())
                .endDate(new Date(2023, 02, 26).toLocalDate())
                .deleted(false)
                .subject("spring boot&angular")
                .build();
        internshipRepository.save(internship);

        List<Internship> internshipsFound = internshipRepository.getCurrentSessionInternships();
        assertThat(internshipsFound.size()).isEqualTo(0);

    }
}