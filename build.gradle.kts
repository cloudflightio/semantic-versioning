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
}