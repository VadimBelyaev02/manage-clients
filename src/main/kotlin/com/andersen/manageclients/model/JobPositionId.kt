package com.andersen.manageclients.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class JobPositionId (

    @Column(name = "job_id")
    var jobId: UUID,

    @Column(name = "position_id")
    var positionsId: UUID
) : Serializable

