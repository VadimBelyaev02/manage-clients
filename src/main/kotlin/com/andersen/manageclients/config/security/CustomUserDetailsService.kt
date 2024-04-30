package com.andersen.manageclients.config.security


import com.andersen.manageclients.repository.ClientRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val clientRepository: ClientRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val client = clientRepository.findByEmail(username).orElseThrow {
            EntityNotFoundException("Client with email = $username not found")
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(client?.email)
            .password(client?.password)
            .authorities(getGrantedAuthorities(setOf("ROLE_CLIENT")))
            .build()
    }

    private fun getGrantedAuthorities(roles: Set<String>): List<GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        for (role in roles) {
            authorities.add(SimpleGrantedAuthority(role))
        }
        return authorities
    }
}