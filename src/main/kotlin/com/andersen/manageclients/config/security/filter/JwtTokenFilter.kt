package com.andersen.manageclients.config.security.filter

import com.andersen.manageclients.config.security.JwtTokenProvider
import com.andersen.manageclients.exception.AuthorizationException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtTokenFilter(
    @Qualifier("customUserDetailsService") private val userDetailsService: UserDetailsService,
    private val tokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private lateinit var resolver: HandlerExceptionResolver

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            var token: String? = null
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
                token = authorizationHeader.substring(BEARER.length)
            }
            if (token != null) {
                val username = tokenProvider.getUsername(token)
                val userDetails = userDetailsService.loadUserByUsername(username)
                val authentication: Authentication =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: AuthorizationException) {
            resolver.resolveException(request, response, null, e)
            return
        }
        filterChain.doFilter(request, response)
    }

    companion object {
        const val BEARER = "Bearer "
    }
}
