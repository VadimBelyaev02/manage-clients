package com.andersen.manageclients.service

interface GenderizeService {

    fun determineGenderProbability(name: String): Double?

}