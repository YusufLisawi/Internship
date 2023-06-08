package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
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
public class SessionRepositoryTest {
    @Autowired
    SessionRepository sessionRepository;
    private Session session;

    @Before
    public void SetUp(){

        session = Session.builder()
                .sessionId(1l)
                .sessionName("session1")
                .sessionDate(new Date(2022,8,4))
                .admittedNumber(10)
                .sessionStatus("current")
                .dayInterviewNumber(3)
                .technology("java")
                .recruiter(Recruiter.builder()
                        .recruiterId(1L)
                        .build())
                .build();

    }

    @Test
    public void BeforeAll_SaveSession(){
        Session saveSession = sessionRepository.save(session);
        assertThat(saveSession).isNotNull();
        assertThat(saveSession.getSessionId()).isGreaterThan(0);

    }
    @Test
    public void FindSession_BySessionStatus(){

        Session Status_Session = sessionRepository.findBySessionStatus("current");
        assertThat(Status_Session).isNotNull();
    }
    @Test
    public void FindSession_BySessionName(){
        Session Name_Session = sessionRepository.findBySessionName("session1");
        assertThat(Name_Session).isNotNull();
        assertThat(Name_Session.getSessionStatus()).isEqualTo("current");
        assertThat(Name_Session.getSessionDate()).isEqualTo(new Date(2022,8,4));
    }

    @DisplayName("close session")
    @Test
    public void Make_closedSession(){
        int Closed = sessionRepository.toClosed();
        assertThat(Closed).isNotNegative();

    }
    @DisplayName("Previous session")
    @Test
    public void PreviousSession(){
        int Previous = sessionRepository.toPrevious();
        assertThat(Previous).isNotNegative();

    }
    @DisplayName("Find All Technologies ")
    @Test
    public void Find_AllTechnologies(){
        List<String> Technologies=sessionRepository.findAllTechnologies();
        assertThat(Technologies).isNotNull();
        assertThat(Technologies.size()).isEqualTo(1);
    }
    @DisplayName("Delete All Session")
    @Test
    public void Session_Delete(){
        sessionRepository.deleteAll();
    }


}
