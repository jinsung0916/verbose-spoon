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
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
            .defaultSuccessUrl("/", true)
            .and()
            .httpBasic().disable()

        http
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

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

        http
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(
                defaultAuthenticationEntryPoint(),
                IS_NOT_APPLICATION_JSON
            )
            .defaultAuthenticationEntryPointFor(
                http401AuthenticationEntryPoint(),
                IS_APPLICATION_JSON
            )
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun defaultAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return LoginUrlAuthenticationEntryPoint("/login")
    }

    @Bean
    fun http401AuthenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { _, response, _ -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED) }
    }
}

private val URLS_NOT_AUTHENTICATED = arrayOf(
    "/static/**",
    "/h2-console/**",
    "/actuator/**",
    "/error"
)

private val IS_APPLICATION_JSON: (HttpServletRequest) -> Boolean = { request ->
    request.contentType == "application/json"
}

private val IS_NOT_APPLICATION_JSON: (HttpServletRequest) -> Boolean = { request ->
    !IS_APPLICATION_JSON.invoke(request)
}