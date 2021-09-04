package com.benope.verbose.spoon.core_backend.security.config

import com.benope.verbose.spoon.core_backend.security.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Profile("api")
@Configuration
class ApiSecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) : WebSecurityConfigurerAdapter() {

    companion object {
        private val URLS_NOT_AUTHENTICATED = arrayOf(
            "/static/**",
            "/h2-console/**",
            "/api/v1/auth/**",
            "/actuator/**",
            "/error"
        )

        private val SC_UNAUTHORIZED: (HttpServletRequest, HttpServletResponse, Exception) -> Unit =
            { _, response, _ -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED) }

        private val SC_FORBIDDEN: (HttpServletRequest, HttpServletResponse, Exception) -> Unit =
            { _, response, _ -> response.sendError(HttpServletResponse.SC_FORBIDDEN) }
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(*URLS_NOT_AUTHENTICATED).permitAll()
            .anyRequest().authenticated()

        http
            .formLogin().disable()
            .httpBasic().disable()

        http
            .csrf().disable()

        http
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .logout().disable()

        http
            .exceptionHandling()
            .authenticationEntryPoint(SC_UNAUTHORIZED)
            .accessDeniedHandler(SC_FORBIDDEN)
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