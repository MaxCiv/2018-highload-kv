// See https://gradle.org and https://github.com/gradle/kotlin-dsl

// Apply the java plugin to add support for Java
plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    // Our beloved one-nio
    compile("ru.odnoklassniki:one-nio:1.0.2")

    // Annotations for better code documentation
    compile("com.intellij:annotations:12.0")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

    // HTTP client for unit tests
    compile("org.apache.httpcomponents:fluent-hc:4.5.3")

    // SHA-3
    compile("org.bouncycastle:bcpkix-jdk15on:1.60")

    // Cache2k
    compile("org.cache2k:cache2k-core:1.2.0.Final")

    // One NIO
    compile("ru.odnoklassniki:one-nio:1.0.2")

    // Nitrite DB
    compile("org.dizitart:nitrite:3.1.0")

    // Guava for tests
    testCompile("com.google.guava:guava:23.1-jre")
}

tasks {
    "test"(Test::class) {
        maxHeapSize = "128m"
        useJUnitPlatform()
    }
}

application {
    // Define the main class for the application
    mainClassName = "ru.mail.polis.Cluster"

    // And limit Xmx
    applicationDefaultJvmArgs = listOf("-Xmx128m")
}
