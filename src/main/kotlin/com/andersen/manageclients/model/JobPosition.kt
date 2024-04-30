package com.andersen.manageclients.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "jobs_positions")
data class JobPosition(

    @EmbeddedId
    var id: JobPositionId? = null,

    @ManyToOne
    @JoinColumn(name ="job_id", insertable = false, updatable = false)
    var job: Job,

    @ManyToOne
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    var position: Position
)
