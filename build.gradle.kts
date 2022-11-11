plugins {
    id("io.cloudflight.autoconfigure-gradle") version "0.8.5"
}

group = "io.cloudflight.semver"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("org.apache.maven:maven-artifact:3.5.0")
    implementation("com.github.zafarkhaja:java-semver:0.9.0")
    implementation("com.vdurmont:semver4j:3.1.0")
    implementation("io.github.g00fy2:versioncompare:1.5.0")
}