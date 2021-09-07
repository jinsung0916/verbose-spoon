package com.benope.verbose.spoon.core_backend.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.concurrent.TimeUnit

@Profile("web")
@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
class WebSecurityConfig(
    private val userDetailService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    companion object {
        private val URLS_NOT_AUTHENTICATED = arrayOf(
            "/static/**",
            "/h2-console/**",
            "/actuator/**",
            "/error"
        )
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(*URLS_NOT_AUTHENTICATED).permitAll()
            .anyRequest().authenticated()

        http
            .formLogin()
            .defaultSuccessUrl("/")
            .and()
            .httpBasic().disable()

        http
            .csrf().disable()

        http
            .rememberMe()
            .tokenValiditySeconds(TimeUnit.DAYS.toSeconds(21).toInt())
            .rememberMeParameter("remember-me")

        http
            .sessionManagement()
            .maximumSessions(1)
            .expiredUrl("/")

        http
            .logout()
            .deleteCookies("JSESSIONID", "remember-me")

    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}