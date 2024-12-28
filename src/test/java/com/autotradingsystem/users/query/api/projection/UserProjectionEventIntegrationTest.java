package com.autotradingsystem.users.query.api.projection;

import com.autotradingsystem.users.query.api.PostgresTestContainerConfiguration;
import com.autotradingsystem.users.query.api.data.repository.UserRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(PostgresTestContainerConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserProjectionEventIntegrationTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @Ignore
    public void test(){
        System.out.println(userRepository.findAll());
    }
}
