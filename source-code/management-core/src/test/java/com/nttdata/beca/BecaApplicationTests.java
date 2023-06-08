package com.nttdata.beca;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import com.nttdata.beca.controller.*;
import com.nttdata.beca.dto.RecruiterDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class BecaApplicationTests {
	@Autowired
	AuthController AuthController;
	@Autowired
    CandidateController CandidateController;
	@Autowired
	InterviewController InterviewController;
	@Autowired
	DashboardController dashboardController;
	@Autowired
	RecruiterController recruiterController;
	@Autowired
	ScoreController scoreController;
	@Autowired
	ScoreConfigController scoreConfigController;
	@Autowired
	SessionController sessionController;

	
	@Test
	void contextLoads() {
		assertThat(AuthController).isNotNull();
		assertThat(CandidateController).isNotNull();
		assertThat(InterviewController).isNotNull();
		assertThat(dashboardController).isNotNull();
		assertThat(recruiterController).isNotNull();
		assertThat(scoreController).isNotNull();
		assertThat(scoreConfigController).isNotNull();
		assertThat(sessionController).isNotNull();
	}

}
