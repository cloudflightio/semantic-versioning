# Semantic Versioning in Java
### There are many truths

While committing to the [Reckon-Plugin](https://github.com/ajoberstar/reckon) we came around [an issue](https://github.com/ajoberstar/reckon/issues/189)
which raised [another issue](https://github.com/zafarkhaja/jsemver/issues/62) and led to total confusion how semantic versioning is done in Java in detail.

First, there would be [an official specification](https://semver.org/), but just like with SQL there exist multiple implementations:

* **Maven-Artifact**, using the class `org.apache.maven.artifact.versioning.ComparableVersion` from `org.apache.maven:maven-artifact` (version `3.5.0`)
* **Gradle VersionNumber**, using the method `org.gradle.util.internal.VersionNumber.parse` from the official `gradleApi()` (version `7.5.1`)
* **Gradle Ivy VersionInfo**, another implementation from the same `gradleApi()`, using `org.gradle.api.internal.artifacts.ivyservice.ivyresolve.VersionInfo` and `org.gradle.api.internal.artifacts.ivyservice.ivyresolve.strategy.DefaultVersionComparator`
* **SemVer**, using the class `com.github.zafarkhaja.semver.Version` from `com.github.zafarkhaja:java-semver` (version `0.9.0`), a library used by i.e. the Reckon-Plugin mentioned above, following the specification strictly. 
* **semver4j**, using the class `com.github.zafarkhaja.semver.Version` from `com.vdurmont:semver4j` (version `3.1.0`) 
* **versioncompare**, using the class `io.github.g00fy2.versioncompare.Version` from `io.github.g00fy2:versioncompare` (version `1.5.0`)
* **ModuleDescriptor.Version**, using `java.lang.module.ModuleDescriptor.Version.parse` from JDK 9

Each of these implementations can parse a `String` and give you the possibility to sort any semantic version. While they 
all agree on the basics (i.e. `0.9.1` < `1.0.0` < `10.0.0`), there are differences especially when using version classifiers like `-rc`, or `-SNAPSHOT`.

Consider the following list of versions:

* `1.0.0`
* `1.0.0-sp`
* `1.0.0-SNAPSHOT`
* `1.0.0-snapshot`
* `1.0.0-release`
* `1.0.0-final`
* `1.0.0-rc.1.8+3bb4161`
* `1.0.0-rc.1`
* `1.0.0-ga`
* `1.0.0-milestone.1.0+2cc3321`
* `1.0.0-rc.1.10+4cc4322`

Inside this repository you find the class [SemanticVersioning.kt](src/main/kotlin/io/cloudflight/semver/SemanticVersioning.kt) which converts those Strings to 
sortable objects using the above-mentioned classes and prints those sorted lists (ascending) to a table.

The output is quite surprising:

| Maven-Artifact                 | Gradle VersionNumber           | Gradle Ivy-VersionInfo         | SemVer                         | semver4j                       | versioncompare                 | ModuleDescriptor.Version       |
| ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ |
| `1.0.0-milestone.1.0+2cc3321`  | `1.0.0-final`                  | `1.0.0-milestone.1.0+2cc3321`  | `1.0.0-SNAPSHOT`               | `1.0.0-final`                  | `1.0.0-SNAPSHOT`               | `1.0.0-SNAPSHOT`               |
| `1.0.0-rc.1`                   | `1.0.0-ga`                     | `1.0.0-rc.1`                   | `1.0.0-final`                  | `1.0.0-ga`                     | `1.0.0-snapshot`               | `1.0.0-final`                  |
| `1.0.0-rc.1.8+3bb4161`         | `1.0.0-milestone.1.0+2cc3321`  | `1.0.0-rc.1.8+3bb4161`         | `1.0.0-ga`                     | `1.0.0-milestone.1.0+2cc3321`  | `1.0.0-rc.1.8+3bb4161`         | `1.0.0-ga`                     |
| `1.0.0-rc.1.10+4cc4322`        | `1.0.0-rc.1`                   | `1.0.0-rc.1.10+4cc4322`        | `1.0.0-milestone.1.0+2cc3321`  | `1.0.0-rc.1`                   | `1.0.0-rc.1`                   | `1.0.0-milestone.1.0+2cc3321`  |
| `1.0.0-SNAPSHOT`               | `1.0.0-rc.1.10+4cc4322`        | `1.0.0-SNAPSHOT`               | `1.0.0-rc.1`                   | `1.0.0-rc.1.8+3bb4161`         | `1.0.0-rc.1.10+4cc4322`        | `1.0.0-rc.1`                   |
| `1.0.0-snapshot`               | `1.0.0-rc.1.8+3bb4161`         | `1.0.0-snapshot`               | `1.0.0-rc.1.8+3bb4161`         | `1.0.0-rc.1.10+4cc4322`        | `1.0.0`                        | `1.0.0-rc.1.8+3bb4161`         |
| `1.0.0`                        | `1.0.0-release`                | `1.0.0-final`                  | `1.0.0-rc.1.10+4cc4322`        | `1.0.0-release`                | `1.0.0-sp`                     | `1.0.0-rc.1.10+4cc4322`        |
| `1.0.0-final`                  | `1.0.0-SNAPSHOT`               | `1.0.0-ga`                     | `1.0.0-release`                | `1.0.0-snapshot`               | `1.0.0-release`                | `1.0.0-release`                |
| `1.0.0-ga`                     | `1.0.0-snapshot`               | `1.0.0-release`                | `1.0.0-snapshot`               | `1.0.0-SNAPSHOT`               | `1.0.0-final`                  | `1.0.0-snapshot`               |
| `1.0.0-sp`                     | `1.0.0-sp`                     | `1.0.0-sp`                     | `1.0.0-sp`                     | `1.0.0-sp`                     | `1.0.0-ga`                     | `1.0.0-sp`                     |
| `1.0.0-release`                | `1.0.0`                        | `1.0.0`                        | `1.0.0`                        | `1.0.0`                        | `1.0.0-milestone.1.0+2cc3321`  | `1.0.0`                        |

7 implementations, 6 truths, only SemVer and ModuleDescriptor.Version are equal (following the specs).  

* There are two different implementations inside the Gradle API (both within internal packages, so not for publis use), returning different results
* SemVer follows the specification strictly, but the two biggest frameworks in that area (Maven+Gradle) decided to do something on top. Gradle [explained that also in its docu](https://docs.gradle.org/current/userguide/single_versions.html#version_ordering).
  
So especially if you're using any of those libraries, sorting numbers, or probably rely on something like the *latest* version, be careful!

Also be careful, when you are mixing any of those libraries, i.e. when running the Reckon-Plugin with Gradle.

### Wanna contribute?

If you know of any other implementation, feel free to submit a pull request:

* Update the [SemanticVersioning.kt](src/main/kotlin/io/cloudflight/semver/SemanticVersioning.kt) with your implementation
* Run the class
* Paste the output to this `README`
