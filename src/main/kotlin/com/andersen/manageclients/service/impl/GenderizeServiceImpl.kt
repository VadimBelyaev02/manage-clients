package com.andersen.manageclients.service.impl

import com.andersen.manageclients.model.external.genderize.GenderizeResponse
import com.andersen.manageclients.service.GenderizeService
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GenderizeServiceImpl(private val webClient: WebClient) : GenderizeService {

    override fun determineGenderProbability(name: String): Double? {
        val response = webClient.get()
            .uri("https://api.genderize.io/?name=$name")
            .retrieve()
            .bodyToMono(GenderizeResponse::class.java)
            .block()

        return response?.probability;
    }
}