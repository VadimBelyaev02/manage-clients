package com.andersen.manageclients

import com.andersen.manageclients.model.Gender
import java.util.UUID

class Constants {
    companion object {
        val GENDER = Gender.male
        val ID: UUID = UUID.fromString("c7b7c414-99be-11ee-b9d1-0242ac120002")
        const val EMAIL = "v.beliayeu@andersen.com"
        const val PASSWORD = "password"
        const val TOKEN = "token"
        const val FIRST_NAME = "Vadzim"
        const val LAST_NAME = "Beliayeu"
        const val JOB_NAME = "Andersen"
        const val POSITION_NAME = "Kotlin Dev"
    }
}