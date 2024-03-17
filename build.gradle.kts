import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jacksonVersion = "2.9.8"

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	kotlin("plugin.serialization") version "1.9.21"
	kotlin("plugin.jpa") version "1.9.21"
	id("com.sourcemuse.mongo") version "2.0.0"
	id("org.sonarqube") version "4.4.1.3373"
	jacoco
}

group = "com.mvp.status"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

configurations.all {
	exclude(group = "commons-logging", module = "commons-logging")
}

val ktorVersion by extra { "2.3.7" }
val mongodbDriver = "4.11.1"
val awsSpringCloud = "3.1.0"
val awsSdk = "2.22.5"
val cucumber = "7.15.0"
val jjwt = "0.11.5"
val restAssured = "5.4.0"
val kotlinxSerialization = "1.6.2"
val jacksonModuleKotlin = "2.14.2"
val openapi = "2.3.0"
val jacocoVersion = "0.8.11"
val mockkVersion = "1.13.9"
val embedMongoVersion = "4.11.0"

dependencies {

	//Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.mongodb:mongodb-driver-sync:$mongodbDriver")

	//AWS
	implementation ("io.awspring.cloud:spring-cloud-aws-starter:$awsSpringCloud")
	implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs:$awsSpringCloud")

	//Ktor
	implementation("io.ktor:ktor-client-core:$ktorVersion")
	implementation("io.ktor:ktor-client-apache:$ktorVersion")
	implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
	implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerialization")
	implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

	// Kotlin utils
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

	//SQS and SNS
	implementation("software.amazon.awssdk:sqs:$awsSdk")
	implementation("software.amazon.awssdk:sns:$awsSdk")

	// mongo
	implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring30x:$embedMongoVersion")

	// rest-assured
	implementation("io.rest-assured:json-schema-validator:$restAssured")

	//Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$openapi")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:$jjwt")
	implementation("io.jsonwebtoken:jjwt-impl:$jjwt")
	implementation("io.jsonwebtoken:jjwt-jackson:$jjwt")

	// Cucumber
	implementation("io.cucumber:cucumber-spring:$cucumber")
	implementation("io.cucumber:cucumber-java:$cucumber")
	implementation("io.cucumber:cucumber-junit:$cucumber")

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("io.mockk:mockk:$mockkVersion")
	testImplementation("io.rest-assured:rest-assured:$restAssured")
	testImplementation("org.jacoco:org.jacoco.core:$jacocoVersion")
	testImplementation("io.rest-assured:json-path:$restAssured")
	testImplementation("io.rest-assured:xml-path:$restAssured")
	testImplementation("io.rest-assured:spring-mock-mvc:$restAssured")
	testImplementation("io.rest-assured:kotlin-extensions:$restAssured")

}

mongo {
	logging = "console"
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	enabled = true
	useJUnitPlatform()
	systemProperty("cucumber.junit-platform.naming-strategy", "long")
	finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Test>("cucumber") {
	useJUnitPlatform {
		includeEngines("cucumber")
	}
	reports {
		html.required.set(true)
		junitXml.required.set(true)
	}
	testClassesDirs = sourceSets["test"].output.classesDirs
	classpath = sourceSets["test"].runtimeClasspath
	finalizedBy(tasks.jacocoTestReport)
}

jacoco {
	toolVersion = jacocoVersion
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required.set(true)
		html.required.set(true)
	}
	val excludes = listOf("**/configuration/*", "**/model/*", "**/utils/*", "**DTO**",
		"**/com/mvp/status/StatusApplication", "**/com/mvp/status/infrastruture/entity/*")
	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it).apply {
			exclude(excludes)
		}.filter{ file -> !file.name.contains("logger") }
	}))
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		val excludes = listOf("**/configuration/*", "**/model/*", "**/utils/*", "**DTO**",
			"**/com/mvp/status/StatusApplication", "**/com/mvp/status/infrastruture/entity/*")
		classDirectories.setFrom(files(classDirectories.files.map {
			fileTree(it).exclude(excludes)
		}).filter{ file -> !file.name.contains("logger") })
		rule {
			limit {
				minimum = BigDecimal.valueOf(0.8)  // 80% coverage
			}
		}
	}
}

sonar {
	properties {
		property("sonar.projectKey", "lfneves_tech-challenge-status")
		property("sonar.organization", "lfneves")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}
