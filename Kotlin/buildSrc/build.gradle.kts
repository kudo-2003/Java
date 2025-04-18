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
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.github.johnrengelman:shadow:8.1.1")
    implementation("io.micronaut.gradle:micronaut-test-resources-plugin:4.4.4")
    implementation("io.micronaut.gradle:micronaut-gradle-plugin:4.4.4")
    implementation("gg.jte:jte-native-resources:3.1.14")
    implementation("gg.jte:jte-gradle-plugin:3.1.14")
    implementation("com.google.cloud.tools.jib:com.google.cloud.tools.jib.gradle.plugin:2.8.0")
}
