package com.andersen.manageclients.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class JobPositionId (

    @Column(name = "job_id")
    var jobId: UUID, // = UUID.randomUUID(),

    @Column(name = "position_id")
    var positionsId: UUID// = UUID.randomUUID()
) : Serializable

// Composite key class must implement Serializable and have defaults.
// class PropertyId(
//    val uuid: UUID = UUID.randomUUID(),
//    val name: String = "") : Serializable