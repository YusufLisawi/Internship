package com.nttdata.beca.repository;

import com.nttdata.beca.BecaApplication;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BecaApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    private ERole ROLE;

    @Test
    public void FindAll_Roles_Test() {

        Optional<Role> role = roleRepository.findByName(ROLE.ROLE_ADMIN);
        assertThat(role).isNotNull();

    }
}
