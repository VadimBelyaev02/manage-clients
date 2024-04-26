package com.andersen.manageclients.model

import jakarta.persistence.*
import lombok.Builder
import java.util.*

@Builder

@Entity
@Table(name = "clients")
data class Client(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        val id: UUID? = null,

        @Column(name = "first_name", nullable = false)
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "email", unique = true)
        val email: String,

)
