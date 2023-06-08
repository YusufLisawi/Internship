package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.repository.RecruiterRepository;
import com.nttdata.beca.service.impl.RecruiterServiceImpl;
import com.nttdata.beca.transformer.RecruiterTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecruiterServiceTest {


	@Mock
	private RecruiterRepository recruiterRepository;

	@InjectMocks
	private RecruiterServiceImpl recruiterService;
	@InjectMocks
	private RecruiterTransformer recruiterTransformer;
	@Mock
	private PasswordEncoder passwordEncoder;
    private Recruiter recruiter,recruiter1;
	private  RecruiterDTO recruiterDTO, recruiterDTO1;
    private List <Recruiter> RecruiterList;
	private List <RecruiterDTO> RecruiterListDTO;



	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);


		 recruiter = Recruiter.builder()
				.recruiterId(2L)
				.email("rababelmehdy123@gmail,com")
				.username("rabab_mehdy")
				.firstName("rabab")
				.lastName("elmehdy")
				.password(this.passwordEncoder.encode("123456789"))
				.phoneNumber("0654407151")
				.picture("pic.png")
				.build();

		 recruiter1 = Recruiter.builder()
				 .recruiterId(3L)
				 .firstName("hafsa")
				 .lastName("benaissa")
				 .email("hafsabenaissa@gmail.com")
				 .username("hafsa_benaissa")
				 .phoneNumber("061867415")
				 .picture("pic.png")
				 .password(this.passwordEncoder.encode("123456789"))
				 .build();

		recruiterDTO=recruiterTransformer.toDTO(recruiter);
		 RecruiterList = new ArrayList<>();
		 RecruiterList.add(recruiter);
		 RecruiterList.add(recruiter1);
		 RecruiterListDTO = recruiterTransformer.toDTOList(RecruiterList);
	}
	@DisplayName(" testing save method")
	@Test
	public void ShouldSaveRecruiterSuccessfully(){
		when(recruiterRepository.save(any())).thenReturn(recruiter);
		recruiterService.save(recruiterDTO);
		verify(recruiterRepository,times(1)).save(any());
	}
	@DisplayName("testing findAll method")
	@Test
	public void  ShouldReturnAllRecruiters(){
		recruiterRepository.save(recruiter1);
		//stubbing mock to return specific data
		when(recruiterRepository.findAll()).thenReturn(RecruiterList);
		Iterable<RecruiterDTO> RecruiterList1 =recruiterService.findAll();
		assertEquals(RecruiterList1,RecruiterListDTO);
		verify(recruiterRepository,times(1)).save(recruiter1);
		verify(recruiterRepository,times(1)).findAll();
	}



	@DisplayName("testing IsEmailExist method ")
	@Test
	public void IsEmailExist_Test(){
		String Email = "fatima@gmail.com";
		when(recruiterRepository.findByEmail(Email)).thenReturn(null);
		boolean recruiterExist = recruiterService.existsByEmail(Email);
		assertFalse(recruiterExist);

	}


	@DisplayName("testing findByUsername method")
	@Test
	public  void  findByUsername_Test(){
		String Username= "sara";
		when(recruiterRepository.findByUsername(Username)).thenReturn(recruiter);

		RecruiterDTO findByUsername= recruiterService.findByUsername(Username);
		assertThat(findByUsername).isNotNull();

	}




}
