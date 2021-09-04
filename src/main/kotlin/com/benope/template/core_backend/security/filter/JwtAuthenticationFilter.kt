package com.benope.template.core_backend.security.filter

import com.benope.template.core_backend.security.domain.JwtToken
import com.benope.template.core_backend.security.exception.InvalidJwtTokenException
import com.benope.template.core_backend.security.service.AuthTokenService
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Profile("api")
@Component
@Slf4j
class JwtAuthenticationFilter(
    private val authTokenService: AuthTokenService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = getJwtFromRequest(request)
            SecurityContextHolder.getContext().authentication = getAuthentication(jwt, request)
        } catch (e: InvalidJwtTokenException) {
            // bypass
        } finally {
            filterChain.doFilter(request, response)
        }
    }

    private fun getJwtFromRequest(request: HttpServletRequest): JwtToken {
        val token = request.getHeader("Authorization") ?: ""
        return if (isIncludeBearer(token)) {
            return JwtToken(token.substring(7))
        } else JwtToken(token)
    }

    private fun isIncludeBearer(token: String?): Boolean {
        return StringUtils.hasText(token) && token?.startsWith("Bearer ") ?: false
    }

    private fun getAuthentication(jwt: JwtToken, request: HttpServletRequest): Authentication {
        val userDetails = authTokenService.getUserDetailsFromToken(jwt)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        return authentication
    }

}