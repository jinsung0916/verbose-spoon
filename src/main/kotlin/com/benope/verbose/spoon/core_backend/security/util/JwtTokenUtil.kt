package com.benope.verbose.spoon.core_backend.security.util

import com.benope.verbose.spoon.core_backend.security.domain.JwtToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

fun doGenerateToken(subject: String?, claims: Map<String, Any?>?, expiration: Date, secretKey: String): JwtToken {
    val token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, secretKey).compact()
    return JwtToken(token)
}

fun <T> JwtToken?.getClaimFromToken(secretKey: String, claimsResolver: (Claims?) -> T): T {
    val claims = this?.getAllClaimsFromToken(secretKey)
    return claimsResolver.invoke(claims)
}

fun JwtToken?.getSubjectFromToken(secretKey: String): String? {
    return this?.getClaimFromToken(secretKey) { obj: Claims? -> obj?.subject }
}

fun JwtToken?.getExpirationDateFromToken(secretKey: String): Date? {
    return this?.getClaimFromToken(secretKey) { obj: Claims? -> obj?.expiration }
}

private fun JwtToken?.getAllClaimsFromToken(secretKey: String): Claims {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(this?.value).body
}

fun JwtToken?.isTokenExpired(secretKey: String): Boolean {
    val expiration = this.getExpirationDateFromToken(secretKey)
    return expiration?.before(Date()) ?: true
}