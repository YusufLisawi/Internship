package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
import com.nttdata.beca.entity.Recruiter;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BecaApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecruiterRepositoryTest {
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    PasswordEncoder encoder;
    private Recruiter recruiter;
    @Before
    public void setUp() {
        recruiter = Recruiter.builder()
                .recruiterId(2L)
                .deleted(false)
                .email("rababbbbbb@gmail,com")
                .username("rababmehdy")
                .firstName("rabab")
                .lastName("elmehdy")
                .password(encoder.encode("123456789"))
                .phoneNumber("0654407151")
                .picture("pic.png")
                .build();
    }
    @Test
    public void BeforeAll_Save_Recruiter() {
        Recruiter saveRecruiter = recruiterRepository.save(recruiter);
        assertThat(saveRecruiter).isNotNull();
        assertThat(saveRecruiter.getRecruiterId()).isGreaterThan(0);
    }
    @DisplayName("List")
    @Test
    public void Get_ListOf_Recruiters() {
        List<Recruiter> listRec = (List<Recruiter>) recruiterRepository.findAll();
        assertThat(listRec).isNotNull();
        assertThat(listRec.size()).isEqualTo(2);
    }

    @Test
    public void FindRecruiter_ByUsername_Test() {

        Recruiter findByUsername = recruiterRepository.findByUsername(recruiter.getUsername());
        assertThat(findByUsername).isNotNull();

    }
    @Test
    public void FindRecruiter_ByEmail_Test() {

        Boolean findByEmail = recruiterRepository.existsByEmail("admin@gmail.com");
        assertThat(findByEmail).isTrue();
    }
    @Test
    public void Last_Delete_Recruiter(){
Recruiter findRec= recruiterRepository.findByUsername(recruiter.getUsername());
recruiterRepository.delete(findRec);
    }
}
