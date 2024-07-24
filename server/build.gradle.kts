plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "net.soeki.fcn"
version = "1.0.0"
application {
    mainClass.set("net.soeki.fcn.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.serialization.json)

    //exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)

    //postgres
    implementation(libs.postgres)

    //kotest
    testImplementation(libs.kotest.assertion.core)
    testImplementation(libs.kotest.assertion.ktor)
    testImplementation(libs.kotest.runner)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
