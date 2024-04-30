//package com.andersen.manageclients.mapper.impl
//
//import com.andersen.manageclients.mapper.   ClientMapper
//import com.andersen.manageclients.model.Client
//import com.andersen.manageclients.model.ClientRequestDto
//import com.andersen.manageclients.model.ClientResponseDto
//import com.andersen.manageclients.model.Gender
//import com.andersen.manageclients.model.RegistrationRequestDto
//import org.springframework.stereotype.Component
//
//@Component
//class ClientMapperImpl : ClientMapper {
//    override fun toResponseDto(client: Client): ClientResponseDto {
//        return ClientResponseDto(
//            id = client.id.toString(),
//            firstName = client.firstName,
//            lastName = client.lastName,
//            email = client.email,
//            gender = Gender.valueOf(client.gender.lowercase())
//        )
//    }
//
//    override fun toEntity(clientRequestDto: ClientRequestDto): Client {
//        return Client(
//            firstName = clientRequestDto.firstName,
//            lastName = clientRequestDto.lastName,
//            email = clientRequestDto.email,
//            gender = clientRequestDto.gender?.value ?: "Male"
//        )
//    }
//
//    override fun toEntity(registrationRequestDto: RegistrationRequestDto): Client {
//        return Client(
//            firstName = registrationRequestDto.firstName,
//            lastName = registrationRequestDto.lastName,
//            email = registrationRequestDto.email,
//            gender = registrationRequestDto.gender?.value ?: "Male",
//            password = registrationRequestDto.password
//        )
//    }
//
//    override fun updateEntityFromRequestDto(clientRequestDto: ClientRequestDto, client: Client) {
//
//    }
//}