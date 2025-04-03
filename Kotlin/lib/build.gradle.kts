/*
 * Copyright 2025 Oracle and/or its affiliates
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("cloud.graal.gdk.gdk-bom") 
    id("io.micronaut.library")
    id("gg.jte.gradle")
    id("com.google.cloud.tools.jib")
}

version = "1.0-SNAPSHOT"
group = "run_main"

repositories {
    mavenCentral()
    
    maven("https://maven.oracle.com/public")
}

dependencies {
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.micrometer:micronaut-micrometer-annotation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.tracing:micronaut-tracing-opentelemetry-annotation")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    micronautBoms(platform("cloud.graal.gdk:gdk-bom:4.7.3.2"))
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.kafka:micronaut-kafka")
    implementation("io.micronaut.kubernetes:micronaut-kubernetes-client")
    implementation("io.micronaut.kubernetes:micronaut-kubernetes-discovery-client")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.objectstorage:micronaut-object-storage-core")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry")
    implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-http")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("jakarta.validation:jakarta.validation-api")
    compileOnly("io.micronaut:micronaut-http-server")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.mysql:mysql-connector-j")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("run_main.*")
    }
}

jte {
    sourceDirectory.set(file("src/main/jte").toPath())
    generate()
    jteExtension("gg.jte.nativeimage.NativeResourcesExtension")
}

// Gradle requires that generateJte is run before some tasks
tasks.configureEach {
    if (name == "inspectRuntimeClasspath") {
        mustRunAfter("generateJte")
    }
}


graalvmNative {
    metadataRepository {
        version.set("0.3.15")
    }
}
