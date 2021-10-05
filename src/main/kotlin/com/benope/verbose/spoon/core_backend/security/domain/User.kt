package com.benope.verbose.spoon.core_backend.security.domain

import com.benope.verbose.spoon.core_backend.common.jpa.BaseEntity
import org.hibernate.annotations.Where
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.stream.Collectors
import javax.persistence.*

@Entity
@Table
@Where(clause = BaseEntity.NOT_DELETED_CLAUSE)
class User(
    @Column(nullable = false, unique = true)
    private val username: String,
    @Column(nullable = false)
    private var password: String,
    @Embedded
    var name: FullName?,
    @Embedded
    @AttributeOverrides(
        value = [AttributeOverride(name = "value", column = Column(name = "nickname"))]
    )
    var nickname: Nickname?,
    @Embedded
    @AttributeOverrides(
        value = [AttributeOverride(name = "value", column = Column(name = "email"))]
    )
    var email: Email?
) : BaseEntity<User>(), UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val userId: Long? = null

    private var loginFailureCount: Int = 0

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")],
    )
    @Column(name = "role_id", nullable = false)
    private var authorities: MutableSet<String> = mutableSetOf()

    @Embedded
    @AttributeOverrides(
        value = [AttributeOverride(name = "value", column = Column(name = "refresh_token"))]
    )
    var refreshToken: JwtToken? = null

    fun addAuthority(role: Role) {
        this.authorities.add(role.roleId)
    }

    override fun getAuthorities(): MutableSet<GrantedAuthority> {
        return authorities.stream()
            .map { SimpleGrantedAuthority(it) }
            .collect(Collectors.toSet())
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun getPassword(): String {
        return this.password
    }

    fun setPassword(password: String?, passwordEncoder: PasswordEncoder?) {
        this.password = passwordEncoder?.encode(password) ?: throw IllegalArgumentException("PasswordEncoder is null.")
    }

    fun resetPassword(passwordEncoder: PasswordEncoder?) {
        val initialPassword = "qpshvm4good"
        this.password =
            passwordEncoder?.encode(initialPassword) ?: throw IllegalArgumentException("PasswordEncoder is null.")
        this.loginFailureCount = 0
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return loginFailureCount < 5
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun handleLoginSuccess() {
        resetLoginFailureCount()
    }

    private fun resetLoginFailureCount() {
        this.loginFailureCount = 0
    }

    fun handleLoginFailure() {
        increaseLoginFailureCount()
    }

    private fun increaseLoginFailureCount() {
        this.loginFailureCount++
    }

    fun isValidRefreshToken(refreshToken: JwtToken): Boolean {
        return this.refreshToken == refreshToken
    }

    fun hasRole(role: Role): Boolean {
        return authorities.any() { it == role.roleId }
    }
}