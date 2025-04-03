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
    id("com.github.johnrengelman.shadow")
    id("io.micronaut.application")
    id("gg.jte.gradle")
    id("com.google.cloud.tools.jib")
    id("io.micronaut.test-resources")
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
    annotationProcessor("io.micronaut.security:micronaut-security-annotations")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    implementation(project(":lib"))
    micronautBoms(platform("cloud.graal.gdk:gdk-bom:4.7.3.2"))
    implementation("com.oracle.oci.sdk:oci-java-sdk-core")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.email:micronaut-email-javamail")
    implementation("io.micronaut.email:micronaut-email-template")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.kafka:micronaut-kafka")
    implementation("io.micronaut.kubernetes:micronaut-kubernetes-client")
    implementation("io.micronaut.kubernetes:micronaut-kubernetes-discovery-client")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.objectstorage:micronaut-object-storage-oracle-cloud")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-bmc-monitoring")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-httpclient-netty")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-logging")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-micrometer")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-sdk")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-vault")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.security:micronaut-security-oauth2")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.tracing:micronaut-tracing-opentelemetry-zipkin-exporter")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut.views:micronaut-views-jte")
    implementation("jakarta.validation:jakarta.validation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("org.eclipse.angus:angus-mail")
    runtimeOnly("org.flywaydb:flyway-mysql")
    testImplementation("org.mockito:mockito-core")
}


application {
    mainClass = "run_main.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}


tasks {
    jib {
        to {
            image = "gcr.io/myapp/jib-image"
        }
    }
}
graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("run_main.*")
    }
    testResources {
        sharedServer = true
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}

jte {
    sourceDirectory.set(file("src/main/jte").toPath())
    generate()
    jteExtension("gg.jte.nativeimage.NativeResourcesExtension")
}

// Gradle requires that generateJte is run before some tasks
tasks.configureEach {
    if (name == "kaptGenerateStubsKotlin" || name == "inspectRuntimeClasspath") {
        mustRunAfter("generateJte")
    }
}



tasks.named<com.bmuschko.gradle.docker.tasks.image.DockerBuildImage>("dockerBuild") {
    images.add("${rootProject.name}-${project.name}")
}

tasks.named<com.bmuschko.gradle.docker.tasks.image.DockerBuildImage>("dockerBuildNative") {
    images.add("${rootProject.name}-${project.name}")
}

tasks.shadowJar {
    setProperty("zip64", true)
}
graalvmNative {
    metadataRepository {
        version.set("0.3.15")
    }
}

graalvmNative.binaries.main.imageName = "${rootProject.name}-${project.name}"
