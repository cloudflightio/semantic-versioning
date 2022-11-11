package io.cloudflight.semver

import com.github.zafarkhaja.semver.Version
import com.vdurmont.semver4j.Semver
import org.apache.maven.artifact.versioning.ComparableVersion
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.VersionInfo
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.DefaultVersionComparator
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.VersionParser
import org.gradle.util.internal.VersionNumber

private val versions = listOf(
    "1.0.0",
    "1.0.0-sp",
    "1.0.0-SNAPSHOT",
    "1.0.0-snapshot",
    "1.0.0-release",
    "1.0.0-final",
    "1.0.0-rc.1.8+3bb4161",
    "1.0.0-rc.1",
    "1.0.0-ga",
    "1.0.0-milestone.1.0+2cc3321",
    "1.0.0-rc.1.10+4cc4322",
)

const val COLUMN_WIDTH = 30

fun main() {

    val table = listOf(
        SortedVersionList(
            name = "Maven-Artifact",
            versions = versions.map { ComparableVersion(it) }.sorted().map { it.toString() }),
        SortedVersionList(
            name = "Gradle VersionNumber",
            versions = versions.map { VersionNumber.parse(it) }.sorted().map { it.toString() }),
        SortedVersionList(
            name = "Gradle Ivy-VersionInfo",
            versions = versions.map { VersionInfo(VersionParser().transform(it)) }
                .sortedWith(DefaultVersionComparator())
                .map { it.version.toString() }),
        SortedVersionList(
            name = "SemVer",
            versions = versions.map { Version.valueOf(it) }.sorted().map { it.toString() }),
        SortedVersionList(
            name = "semver4j",
            versions = versions.map { Semver(it) }.sorted().map { it.toString() }),
        SortedVersionList(
            name = "versioncompare",
            versions = versions.map { io.github.g00fy2.versioncompare.Version(it) }.sorted().map { it.toString() }),
        SortedVersionList(
            name = "ModuleDescriptor.Version",
            versions = versions.map { java.lang.module.ModuleDescriptor.Version.parse(it) }.sorted().map { it.toString() })
    )

    println("| " + table.joinToString(separator = " | ") { it.name.toColumn() } + " |")
    println("| " + table.joinToString(separator = " | ") { "-".repeat(COLUMN_WIDTH) } + " |")
    for (i in versions.indices) {
        println("| " + table.joinToString(separator = " | ") { ("`" + it.versions[i] + "`").toColumn() } + " |")
    }
}

private fun String.toColumn(): String {
    return this.padEnd(COLUMN_WIDTH, ' ')
}

data class SortedVersionList(val name: String, val versions: List<String>)