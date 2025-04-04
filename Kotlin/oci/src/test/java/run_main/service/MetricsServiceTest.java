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

package run_main.service;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class MetricsServiceTest {

    @Inject
    MeterRegistry meterRegistry;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    MetricsService metricsService;

    @Test
    void testExpectedMeters() {

        Set<String> names = meterRegistry.getMeters().stream()
                .map(meter -> meter.getId().getName())
                .collect(Collectors.toSet());

        // check that a subset of expected meters exist
        assertTrue(names.contains("jvm.memory.max"));
        assertTrue(names.contains("process.uptime"));
        assertTrue(names.contains("system.cpu.usage"));
        assertTrue(names.contains("process.files.open"));
        assertTrue(names.contains("logback.events"));
        assertTrue(names.contains("custom.thread.count.value"));
    }

    @Test
    void testMetricsEndpoint() {

        Map<String, Object> response = httpClient.toBlocking().retrieve(
                HttpRequest.GET("/metrics"),
                Argument.mapOf(String.class, Object.class));

        assertTrue(response.containsKey("names"));
        assertTrue(response.get("names") instanceof List);

        List<String> names = (List<String>) response.get("names");

        // check that a subset of expected meters exist
        assertTrue(names.contains("jvm.memory.max"));
        assertTrue(names.contains("process.uptime"));
        assertTrue(names.contains("system.cpu.usage"));
        assertTrue(names.contains("process.files.open"));
        assertTrue(names.contains("logback.events"));
        assertTrue(names.contains("custom.thread.count.value"));
    }

    @Test
    void testOneMetricEndpoint() {

        Map<String, Object> response = httpClient.toBlocking().retrieve(
                HttpRequest.GET("/metrics/jvm.memory.used"),
                Argument.mapOf(String.class, Object.class));

        String name = (String) response.get("name");
        assertEquals("jvm.memory.used", name);

        List<Map<String, Object>> measurements = (List<Map<String, Object>>) response.get("measurements");
        assertEquals(1, measurements.size());

        double value = (double) measurements.get(0).get("value");
        assertTrue(value > 0);
    }

    @Test
    void testMetricsUpdates() throws InterruptedException {

        int checks = 3;

        for (int i = 0; i < checks; i++) {
            metricsService.refreshThreadCount();
        }

        Optional<Meter> timer = meterRegistry.getMeters().stream().filter(x -> x.getId().getName().equals("custom.thread.count.time")).findFirst();

        assertEquals(checks, ((Timer) timer.get()).count(), 0.000001);
    }
}
