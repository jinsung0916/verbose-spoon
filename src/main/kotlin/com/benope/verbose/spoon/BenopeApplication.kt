package com.benope.verbose.spoon;

import com.benope.verbose.spoon.web.user.dto.CreateUserRequest
import com.benope.verbose.spoon.web.user.service.UserManageService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaAuditing
class BenopeApplication : SpringBootServletInitializer() {

    @Configuration
    @Profile("local")
    class IntegrationTestPostConstruct(
        private val userManageService: UserManageService
    ) {

        @PostConstruct
        fun postConstruct() {
            val createUserRequest = CreateUserRequest(
                username = "username",
                password = "password",
                firstName = "firstName",
                lastName = "lastName",
                nickname = "nickname",
                email = "email@benope.com"
            )

            userManageService.createUser(createUserRequest)
        }
    }

}

fun main(args: Array<String>) {
    runApplication<BenopeApplication>(*args)
}