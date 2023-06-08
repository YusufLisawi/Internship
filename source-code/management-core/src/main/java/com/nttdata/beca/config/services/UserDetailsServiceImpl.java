package com.nttdata.beca.config.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.repository.RecruiterRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final RecruiterRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Recruiter user = userRepository.findByUsername(username);
    // .orElseThrow(() -> new UsernameNotFoundException("User Not Found with
    // username: " + username));
    return UserDetailsImpl.build(user);
  }

}
