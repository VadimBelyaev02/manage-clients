package com.andersen.manageclients.model

import jakarta.persistence.*
import lombok.Data
import lombok.Setter
import java.util.*


@Entity
@Table(name = "clients")
data class Client(

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        var id: UUID? = null,

        @Column(name = "first_name")
        var firstName: String,

        @Column(name = "last_name")
        var lastName: String,

        @Column(name = "email")
        var email: String,

        var password: String? = null,

        var gender: String,

        @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        var clientPositions: List<ClientPosition>? = listOf()
)
