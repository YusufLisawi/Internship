package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
import com.nttdata.beca.entity.Interview;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BecaApplication.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InterviewRepositoryTest {
    @Autowired
    InterviewRepository interviewRepository;
    private Interview interview;

    @Before
    public void SetUp(){
        interview= Interview.builder()
                .interviewId(1l)
                .date(new Date(2022,8,24))
                .session(null)
                .build();
    }
    @DisplayName(" save interview")
    @Test
    public void BeforeAll_saveInterview(){
        Interview saveInter = interviewRepository.save(interview);
        AssertionsForClassTypes.assertThat(saveInter).isNotNull();
    }

    @DisplayName("find by Date")
    @Test
    public  void  Find_ById_ByDate(){
        AssertionsForClassTypes.assertThat(interviewRepository.findByDate(interview.getDate())).isNotNull();
    }
    @DisplayName("Delete Interview")
    @Test
    public void Interview_Delete(){
        interviewRepository.deleteAll();
    }
}
