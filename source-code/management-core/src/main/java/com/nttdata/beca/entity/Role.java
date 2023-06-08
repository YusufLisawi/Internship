package com.nttdata.beca.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import com.nttdata.beca.entity.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  @JsonIgnore
  @ManyToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private List<Recruiter> recruiters = new ArrayList<>();

}