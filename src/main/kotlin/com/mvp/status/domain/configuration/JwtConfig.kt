package com.mvp.status.domain.configuration

import com.mvp.payment.domain.configuration.jwt.JWTUtils
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JWTConfig {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @PostConstruct
    fun init() {
        JWTUtils.initializeSecret(secret)
    }
}
