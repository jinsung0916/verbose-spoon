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

    protected static final String ADMIN_USERNAME = "admin_username";
    protected static final String ADMIN_PASSWORD = "admin_password";

    @BeforeEach
    public void setUp(@Autowired RoleRepository roleRepository,
                      @Autowired UserRepository userRepository,
                      @Autowired PasswordEncoder passwordEncoder) {

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);

        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);

        User user = new User(USERNAME, passwordEncoder.encode(PASSWORD), null, null, null);
        user.addAuthority(userRole);
        userRepository.save(user);

        User admin = new User(ADMIN_USERNAME, passwordEncoder.encode(ADMIN_PASSWORD), null, null, null);
        admin.addAuthority(adminRole);
        userRepository.save(admin);
    }

}
