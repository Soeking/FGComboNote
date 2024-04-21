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

val exposedVersion: String by project

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.ktor.server.negotiation)

    //exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    //postgres
    implementation(libs.postgres)
}
