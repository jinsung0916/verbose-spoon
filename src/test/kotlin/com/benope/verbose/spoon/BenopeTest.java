package com.benope.verbose.spoon;

import com.benope.verbose.spoon.core_backend.security.domain.Role;
import com.benope.verbose.spoon.core_backend.security.domain.User;
import com.benope.verbose.spoon.core_backend.security.repository.RoleRepository;
import com.benope.verbose.spoon.core_backend.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BenopeTest {

    protected static final String USERNAME = "username";
    protected static final String PASSWORD = "password";

    @BeforeEach
    public void setUp(@Autowired RoleRepository roleRepository,
                      @Autowired UserRepository userRepository,
                      @Autowired PasswordEncoder passwordEncoder) {

        Role role = new Role("ROLE_USER");
        roleRepository.save(role);

        User user = new User(USERNAME, passwordEncoder.encode(PASSWORD), null, null, null);
        user.addAuthority(role);
        userRepository.save(user);

    }

}
