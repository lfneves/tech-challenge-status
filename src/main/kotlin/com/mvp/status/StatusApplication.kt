package com.mvp.status

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(excludeName = ["de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration"])
@EnableMongoRepositories(basePackages = ["com.mvp.status.infrastruture.repository"])
class PaymentApplication

fun main(args: Array<String>) {
    runApplication<PaymentApplication>(*args)
}
