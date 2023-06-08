package com.nttdata.beca.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "recruiter", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recruiter extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recruiter_id")
	private Long recruiterId;
	@Column(nullable = false, length = 50)
	private String email;
	@Column(nullable = false, length = 50)
	private String username;
	@Column(nullable = false, length = 50)
	private String firstName;
	@Column(nullable = false, length = 50)
	private String lastName;
	@Column(nullable = false, length = 120)
	private String password;
	@Column(nullable = false, length = 50)
	private String phoneNumber;
	@Column(length = 50)
	private String picture;

	private boolean deleted;

	@OneToMany(mappedBy = "recruiter", orphanRemoval = true)
	@JsonIgnore
	private List<Session> session = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "recruiter_roles", joinColumns = @JoinColumn(name = "recruiter_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> role = new ArrayList<>();


	public Recruiter(String email, String username, String firstName, String lastName, String password, String phoneNumber, String picture) {
		this.email = email;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.picture = picture;
	}
	public Recruiter(String email, String username, String firstName, String lastName, String password, String phoneNumber, String picture, List<Role> role) {
		this.email = email;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.picture = picture;
		this.role = role;
	}
}
