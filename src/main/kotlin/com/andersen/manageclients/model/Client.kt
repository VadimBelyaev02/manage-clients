package com.andersen.manageclients.model

import jakarta.persistence.*
import lombok.Setter
import java.util.*

@Setter
@Entity
@Table(name = "clients")
data class Client(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        val id: UUID? = null,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "email")
        val email: String,

        val gender: String
)
