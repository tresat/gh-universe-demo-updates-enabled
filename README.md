# GitHub Universe 2025 Demo
## Avoiding the next supply chain disaster using GitHub and Gradle

This repository contains a demo project for [GitHub Universe 2025](https://githubuniverse.com/).
The demo showcases how to use GitHub Dependency Submission API and the Gradle Dependency Submission Action to discover and avoid vulnerable dependencies in your projects.

### To build and run the demo:

- `./gradlew run`

This will simulate running a simple Java application that uses some dependencies at runtime.

```text
> Task :app:run
Running application...
        Using class: org.springframework.core.io.PathResource
        Using class: org.bouncycastle.x509.X509StreamParser
        Using class: org.apache.commons.lang3.StringUtils
        Using class: org.apache.avro.reflect.AvroName
        Using class: net.minidev.json.JSONArray
        Using class: org.apache.logging.log4j.Logger
Done!
```

### To see the resolved vulnerabilities:

- `./gradlew :app:dependencies --configuration compileClasspath`
- `./gradlew :app:dependencies --configuration runtimeClasspath`
- `./gradlew :app:dependencies --configuration testRuntimeClasspath`

This will output a report to the console listing dependencies that is demonstrated below.
A [web-based, searchable dependency report](https://docs.gradle.org/current/userguide/build_scans.html) is also available by adding the `--scan` option.

```text
------------------------------------------------------------
Project ':app'
------------------------------------------------------------

compileClasspath - Compile classpath for source set 'main'.
+--- org.springframework:spring-core:6.2.10
|    \--- org.springframework:spring-jcl:6.2.10
+--- io.minio:minio:8.5.8
|    +--- com.carrotsearch.thirdparty:simple-xml-safe:2.7.1
|    +--- com.google.guava:guava:32.1.3-jre
|    |    +--- com.google.guava:failureaccess:1.0.1
|    |    +--- com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava
|    |    +--- com.google.code.findbugs:jsr305:3.0.2
|    |    +--- org.checkerframework:checker-qual:3.37.0
|    |    +--- com.google.errorprone:error_prone_annotations:2.21.1
|    |    \--- com.google.j2objc:j2objc-annotations:2.8
|    +--- com.squareup.okhttp3:okhttp:4.12.0
|    |    +--- com.squareup.okio:okio:3.6.0
|    |    |    \--- com.squareup.okio:okio-jvm:3.6.0
|    |    |         +--- org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.10
|    |    |         |    +--- org.jetbrains.kotlin:kotlin-stdlib:1.9.10
|    |    |         |    |    +--- org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10
|    |    |         |    |    \--- org.jetbrains:annotations:13.0
|    |    |         |    \--- org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10
|    |    |         |         \--- org.jetbrains.kotlin:kotlin-stdlib:1.9.10 (*)
|    |    |         \--- org.jetbrains.kotlin:kotlin-stdlib-common:1.9.10
|    |    \--- org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21 -> 1.9.10 (*)
|    +--- com.fasterxml.jackson.core:jackson-annotations:2.15.3
|    |    \--- com.fasterxml.jackson:jackson-bom:2.15.3
|    |         +--- com.fasterxml.jackson.core:jackson-annotations:2.15.3 (c)
|    |         +--- com.fasterxml.jackson.core:jackson-core:2.15.3 (c)
|    |         \--- com.fasterxml.jackson.core:jackson-databind:2.15.3 (c)
|    +--- com.fasterxml.jackson.core:jackson-core:2.15.3
|    |    \--- com.fasterxml.jackson:jackson-bom:2.15.3 (*)
|    +--- com.fasterxml.jackson.core:jackson-databind:2.15.3
|    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.15.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-core:2.15.3 (*)
|    |    \--- com.fasterxml.jackson:jackson-bom:2.15.3 (*)
|    +--- org.bouncycastle:bcprov-jdk18on:1.76
|    \--- org.xerial.snappy:snappy-java:1.1.10.5
+--- org.apache.avro:avro:1.11.+ -> 1.11.3
|    +--- com.fasterxml.jackson.core:jackson-core:2.14.2 -> 2.15.3 (*)
|    +--- com.fasterxml.jackson.core:jackson-databind:2.14.2 -> 2.15.3 (*)
|    +--- org.apache.commons:commons-compress:1.22
|    \--- org.slf4j:slf4j-api:1.7.36
+--- org.json:json:20250517 -> net.minidev:json-smart:2.5.1
|    \--- net.minidev:accessors-smart:2.5.1
|         \--- org.ow2.asm:asm:9.6
+--- org.apache.logging.log4j:log4j-core -> 2.13.0
|    \--- org.apache.logging.log4j:log4j-api:2.13.0
\--- org.apache.logging.log4j:log4j-core:2.13.0 (c)

(c) - A dependency constraint, not a dependency. The dependency affected by the constraint occurs elsewhere in the tree.
(*) - Indicates repeated occurrences of a transitive dependency subtree. Gradle expands transitive dependency subtrees only once per project; repeat occurrences only display the root of the subtree, followed by this annotation.
```