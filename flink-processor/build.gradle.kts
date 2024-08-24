plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.martishin"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

val flinkVersion = "1.16.0"
val jacksonVersion = "2.13.4"
val junitJupiterVersion = "5.8.2"
val kafkaVersion = "3.2.0"
val log4jVersion = "2.17.2"
val postgresVersion = "42.5.0"

dependencies {
    implementation("org.apache.flink:flink-streaming-java:$flinkVersion")
    implementation("org.apache.flink:flink-clients:$flinkVersion")
    implementation("org.apache.flink:flink-table:$flinkVersion")
    implementation("org.apache.flink:flink-table-api-java-bridge:$flinkVersion")
    implementation("org.apache.flink:flink-table-api-java:$flinkVersion")
    implementation("org.apache.flink:flink-runtime:$flinkVersion")
    implementation("org.apache.flink:flink-table-planner_2.12:$flinkVersion")
    implementation("org.apache.flink:flink-connector-files:$flinkVersion")
    implementation("org.apache.flink:flink-connector-jdbc:$flinkVersion")
    implementation("org.apache.flink:flink-json:$flinkVersion")
    implementation("org.apache.flink:flink-csv:$flinkVersion")
    implementation("org.apache.flink:flink-connector-kafka:$flinkVersion")

    implementation("org.postgresql:postgresql:${postgresVersion}")

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    runtimeOnly("org.apache.logging.log4j:log4j-api:$log4jVersion")
    runtimeOnly("org.apache.logging.log4j:log4j-core:$log4jVersion")

    testImplementation("com.github.javafaker:javafaker:1.0.2")
    testImplementation("net.mguenther.kafka:kafka-junit:$kafkaVersion") {
        exclude(group = "ch.qos.reload4j", module = "reload4j")
        exclude(group = "org.apache.kafka", module = "kafka-log4j-appender")
    }
    testImplementation("org.apache.flink:flink-test-utils:$flinkVersion") {
        exclude(group = "log4j", module = "log4j")
    }
    testImplementation("org.apache.flink:flink-runtime-web:$flinkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    systemProperty("illegal-access", "permit")
}

application {
    mainClass.set("Main")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("jar-with-dependencies")
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = "Main"
    }
}
