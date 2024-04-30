package com.andersen.manageclients.model.external.genderize

import com.fasterxml.jackson.annotation.JsonProperty

data class GenderizeResponse(
    @JsonProperty("count") val count: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("gender") val gender: String,
    @JsonProperty("probability") val probability: Double
)