package com.andersen.manageclients.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

@Embeddable
data class ClientPositionId(

    @Column(name = "client_id")
    var clientId: UUID,// = UUID.randomUUID(),

    @Column(name = "position_id")
    var positionId: UUID// = UUID.randomUUID()
) : Serializable

// Composite key class must implement Serializable and have defaults.
// class PropertyId(
//    val uuid: UUID = UUID.randomUUID(),
//    val name: String = "") : Serializable