package com.nttdata.beca.repository;

import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Docs;
import com.nttdata.beca.entity.Internship;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocsRepositoryTest {

    @Autowired
    DocsRepository docsRepository;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    InternshipRepository internshipRepository;
    private Docs docs;
    private Candidate candidate;
    private Internship internship;

    @Before
    public void SetUp(){
        candidate = Candidate.builder()
                .email("yusufisawi@gmail,com")
                .firstName("yusuf")
                .lastName("isawi")
                .anapec("false")
                .diplome("Bac+4")
                .city("Tetouan")
                .university("ENA")
                .CVname("CV-YUSUf.pdf")
                .gender("male")
                .session(null)
                .finalAdmissionStatus("true")
                .interview(null)
                .phoneNumber("0682860421")
                .preselectionStatus(true)
                .deleted(false)
                .build();

        internship= Internship.builder()
                .internshipId(1l)
                .subject("java")
                .deleted(false)
                .candidate(candidate)
                .build();
    }

    @Test
    @Before
    public void BeforeAll_SaveDocs(){
        Internship internshipSaved = internshipRepository.save(internship);
        docs = Docs.builder()
                .type("TYPE_CV")
                .path("path/to/doc")
                .internship(internship)
                .build();
        Docs saveDocs = docsRepository.save(docs);
        assertThat(saveDocs).isNotNull();
        assertThat(saveDocs.getId()).isGreaterThan(0);
    }
    @Test
    public void findDocsById() {
        Docs foundedDocs = docsRepository.findDocsById(docs.getId());
        assertThat(foundedDocs).isNotNull();
    }

    @Test
    public void findByCandidate_CandidateId() {
        docs = Docs.builder()
                .type("TYPE_CIN")
                .path("path/to/doc")
                .internship(internship)
                .build();
        Docs saveDocs = docsRepository.save(docs);
        List<Docs> foundedDocs = docsRepository.findByInternship_InternshipId(candidate.getCandidateId());
        assertThat(foundedDocs).isNotNull();
        assertThat(foundedDocs.size()).isGreaterThan(1);
    }

    @Test
    @Transactional
    public void deleteAllByCandidate_CandidateId() {
        docsRepository.deleteAllByInternship_InternshipId(candidate.getCandidateId());
        List<Docs> foundedDocs = docsRepository.findByInternship_InternshipId(candidate.getCandidateId());
        assertThat(foundedDocs.size()).isZero();
    }
}