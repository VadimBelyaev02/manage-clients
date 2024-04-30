package com.andersen.manageclients.service

import com.andersen.manageclients.model.Gender
import com.andersen.manageclients.model.external.genderize.GenderizeResponse

interface GenderizeService {

    fun determineGenderProbability(name: String): GenderizeResponse

}