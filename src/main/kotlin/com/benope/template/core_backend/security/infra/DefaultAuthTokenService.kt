package com.benope.template.core_backend.security.infra

import com.benope.template.core_backend.security.domain.JwtToken
import com.benope.template.core_backend.security.exception.InvalidJwtTokenException
import com.benope.template.core_backend.security.service.AuthTokenService
import com.benope.template.core_backend.security.util.doGenerateToken
import com.benope.template.core_backend.security.util.getClaimFromToken
import com.benope.template.core_backend.security.util.getSubjectFromToken
import com.benope.template.core_backend.security.util.isTokenExpired
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class DefaultAuthTokenService(
    private val objectMapper: ObjectMapper
) : AuthTokenService {

    companion object {
        private const val AUTHORITY_KEY = "authorities"

        private const val JWT_TOKEN_VALIDITY = (5 * 60 * 60).toLong()
    }

    @Value("\${jwt.secret}")
    private lateinit var SECRET_KEY: String

    override fun generateToken(userDetails: UserDetails?): JwtToken {
        val subject = userDetails?.username
        val claims =
            mutableMapOf<String, Any?>(
                Pair(
                    AUTHORITY_KEY,
                    objectMapper.writeValueAsString(userDetails?.authorities?.map { it.authority })
                )
            )
        val expiration = Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)
        return doGenerateToken(subject, claims, expiration, SECRET_KEY)
    }

    override fun getUserDetailsFromToken(jwtToken: JwtToken?): UserDetails {
        try {
            return doGetUserDetailsFromToken(jwtToken)
        } catch (e: JwtException) {
            throw InvalidJwtTokenException().initCause(e)
        } catch (e: IllegalArgumentException) {
            throw InvalidJwtTokenException().initCause(e)
        }
    }

    private fun doGetUserDetailsFromToken(jwtToken: JwtToken?): UserDetails {
        if (jwtToken.isTokenExpired(SECRET_KEY)) {
            throw InvalidJwtTokenException()
        } else {
            val username = jwtToken?.getSubjectFromToken(SECRET_KEY)
            val authorities = getAuthoritiesFrmToken(jwtToken)
            return SimpleUserDetails(username, authorities)
        }
    }

    private fun getAuthoritiesFrmToken(jwtToken: JwtToken?): MutableSet<GrantedAuthority>? {
        return jwtToken?.getClaimFromToken(SECRET_KEY) { obj: Claims? ->
            val authorities = obj?.get(AUTHORITY_KEY) as String
            objectMapper.readValue(
                authorities,
                object : TypeReference<List<String>>() {}
            )
                .map { SimpleGrantedAuthority(it) }
                .toMutableSet()
        }
    }

    class SimpleUserDetails(
        private val username: String?,
        private val authorities: MutableSet<GrantedAuthority>?
    ) : UserDetails {

        override fun getUsername(): String {
            return username ?: throw InvalidJwtTokenException()
        }

        override fun getPassword(): String {
            return "UNKNOWN"
        }

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
            return authorities ?: throw InvalidJwtTokenException()
        }

        override fun isAccountNonExpired(): Boolean {
            return true
        }

        override fun isAccountNonLocked(): Boolean {
            return true
        }

        override fun isCredentialsNonExpired(): Boolean {
            return true
        }

        override fun isEnabled(): Boolean {
            return true
        }

    }
}