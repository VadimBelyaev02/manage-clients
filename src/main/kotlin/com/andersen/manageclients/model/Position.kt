package com.andersen.manageclients.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID


@Entity
@Table(name = "positions")
data class Position(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    var name: String,


    @OneToMany(mappedBy = "position")
    var positionJobs: List<JobPosition>
) {
}