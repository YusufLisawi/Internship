package com.nttdata.beca.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class RecruiterDTO {

	private Long recruiterId;
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String phoneNumber;
	private List<RoleDTO> role = new ArrayList<>();
	private String picture;
	private boolean deleted;

	public RecruiterDTO(String email, String username, String firstName, String lastName, String password, String phoneNumber, String picture, List<RoleDTO> roleDTOS) {
		this.email = email;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.picture = picture;
		this.role = roleDTOS;
	}
}
