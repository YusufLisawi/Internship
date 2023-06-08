package com.nttdata.beca.config.request;

import java.util.List;

import javax.validation.constraints.*;

import com.nttdata.beca.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;
  private String firstName;
  private String lastName;
  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
  private String phoneNumber;
  private String picture;
  private List<Role> role;

}
