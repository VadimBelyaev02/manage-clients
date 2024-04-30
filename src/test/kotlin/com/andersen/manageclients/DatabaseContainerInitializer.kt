//package com.andersen.manageclients
//
//import org.springframework.boot.test.util.TestPropertyValues
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection
//import org.springframework.context.ApplicationContextInitializer
//import org.springframework.context.ConfigurableApplicationContext
//import org.springframework.context.annotation.Bean
//import org.testcontainers.containers.GenericContainer
//import org.testcontainers.containers.PostgreSQLContainer
//import org.testcontainers.utility.DockerImageName
//
//class DatabaseContainersInitializer : ApplicationContextInitializer<ConfigurableApplicationContext?> {
//
//    override fun initialize(applicationContext: ConfigurableApplicationContext) {
//        val postgresContainer = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:latest"))
//        postgresContainer.start()
//
//        TestPropertyValues.of(
//            "spring.datasource.url=" + postgresContainer.jdbcUrl,
//            "spring.datasource.username=" + postgresContainer.username,
//            "spring.datasource.password=" + postgresContainer.password
//        ).applyTo(applicationContext.environment)
//    }
//}