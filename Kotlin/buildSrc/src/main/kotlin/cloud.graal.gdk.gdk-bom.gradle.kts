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

dependencies {
    components {
        withModule<EnforceBomDependencies>("cloud.graal.gdk:gdk-bom")
    }
}

open class EnforceBomDependencies : ComponentMetadataRule {
    private val gdkBomConstraints: MutableMap<String, String> = mutableMapOf()

    override fun execute(ctx: ComponentMetadataContext) {
        val isGdkBom = ctx.details.id.group == "cloud.graal.gdk" && ctx.details.id.name == "gdk-bom"

        if (isGdkBom) {
            ctx.details.allVariants {
                withDependencyConstraints {
                    forEach { constraint ->
                        val key = "${constraint.group}:${constraint.module}"
                        val version = constraint.versionConstraint.requiredVersion

                        if (version.matches(Regex(".*-oracle-\\d+")) == true) {
                            gdkBomConstraints[key] = version
                        }

                        constraint.version {
                            strictly(version)
                        }
                    }
                }
            }
        }

        ctx.details.allVariants {
            withDependencyConstraints {
                forEach { constraint ->
                    val key = "${constraint.group}:${constraint.module}"
                    if (gdkBomConstraints.containsKey(key)) {
                        constraint.version {
                            gdkBomConstraints[key]?.let { newVersion ->
                                strictly(newVersion)
                            }
                        }
                    }
                }
            }
        }

    }
}