@file:Suppress("MISSING_DEPENDENCY_SUPERCLASS_IN_TYPE_ARGUMENT", "VulnerableLibrariesLocal", "UnstableApiUsage")

plugins {
    application

    // Plugin uses a transitive dependency with known vulnerabilities
    // - org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r - https://www.cve.org/CVERecord?id=CVE-2025-4949
    id("com.diffplug.spotless").version("6.17.0")
}

dependencies {
    // Dependency known to have vulnerability
    // - org.springframework:spring-core:6.2.10 - https://www.cve.org/CVERecord?id=CVE-2025-41249
    implementation("org.springframework:spring-core:6.2.10")

    // Dependency with NO known vulnerabilities, but includes 2 vulnerable transitive dependencies, one of which is EXCLUDED
    // - org.bouncycastle:bcprov-jdk18on:1.76 - https://www.cve.org/CVERecord?id=CVE-2025-8885
    // - org.apache.commons:commons-compress:1.24.0 - several, including https://www.cve.org/CVERecord?id=CVE-2024-26308
    implementation("io.minio:minio:8.5.8") {
        exclude("org.apache.commons", "commons-compress")
    }

    // Dependency with known vulnerabilities, only present at RUNTIME
    // - org.apache.commons:commons-lang3:3.14.0 - https://www.cve.org/CVERecord?id=CVE-2025-48924
    runtimeOnly("org.apache.commons:commons-lang3:3.18.0")

    // Dependency declaration specifies only a minimum version - but a vulnerable version will be RESOLVED by the resolutionStrategy
    // - org.apache.avro:avro:1.11.13 - https://www.cve.org/CVERecord?id=CVE-2024-47561
    implementation("org.apache.avro:avro:1.11.+")

    // Dependency declaration specifies a fixed version with no known vulnerabilities - but this will be REPLACED by a vulnerable version via dependency substitution
    // - net.minidev:json-smart:2.5.1 - https://www.cve.org/CVERecord?id=CVE-2024-57699
    implementation("org.json:json:20250517")

    // Dependency declaration specifies no version - version is determined by constraints and resolves MULTIPLE DIFFERENT vulnerable versions
    // - org.apache.logging.log4j:log4j-core:2.13.0 and 2.14.0 - https://www.cve.org/CVERecord?id=CVE-2021-44228
    implementation("org.apache.logging.log4j:log4j-core")
}

// Constraints produce different vulnerable version as compile vs. runtime
dependencies {
    constraints {
        compileOnly("org.apache.logging.log4j:log4j-core:2.13.0")
        testCompileOnly("org.apache.logging.log4j:log4j-core:2.13.0")

        runtimeOnly("org.apache.logging.log4j:log4j-core:2.14.0")
        testRuntimeOnly("org.apache.logging.log4j:log4j-core:2.14.0")
    }
}

// Simulate some complex build-time logic that results in a vulnerable version being chosen
configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.apache.avro" && requested.name == "avro") {
                if (project.configurations.findByName("implementation") != null) {
                    useTarget("org.apache.avro:avro:1.11.3")
                }
            }
        }
    }
}

// Completely replace a dependency with a vulnerable version via dependency substitution
configurations.all {
    resolutionStrategy.dependencySubstitution.all {
        requested.let {
            if (it is ModuleComponentSelector && it.group == "org.json") {
                useTarget("net.minidev:json-smart:2.5.1")
            }
        }
    }
}

// Test dependency declared outside the main dependencies block, also contains a known vulnerability
// - junit:junit:4.12 - https://www.cve.org/CVERecord?id=CVE-2021-44228
testing {
    suites {
        named<JvmTestSuite>("test") {
            dependencies {
                implementation("junit:junit:4.12")
            }
        }
    }
}

application {
    mainClass = "org.example.App"
}
