package com.andersen.manageclients.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "clients_positions")
data class ClientPosition(

    @EmbeddedId
    var id: ClientPositionId? = null,

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    var client: Client,

    @ManyToOne
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    var position: Position
) {}