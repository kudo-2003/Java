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

package run_main;

import com.oracle.bmc.core.ComputeClient;
import com.oracle.bmc.core.model.Instance;
import com.oracle.bmc.core.requests.GetInstanceRequest;
import com.oracle.bmc.core.requests.InstanceActionRequest;
import com.oracle.bmc.core.responses.InstanceActionResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oracle.bmc.core.model.Instance.LifecycleState.Running;
import static com.oracle.bmc.core.model.Instance.LifecycleState.Stopped;

@Controller("/compute")
class OciInstanceController {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private static final String START = "START";
    private static final String STOP = "STOP";

    private final ComputeClient computeClient;

    OciInstanceController(ComputeClient computeClient) {
        this.computeClient = computeClient;
    }

    @Get("/status/{ocid}")
    OciInstanceData status(String ocid) {
        return new OciInstanceData(getInstance(ocid));
    }

    @Post("/start/{ocid}")
    OciInstanceData start(String ocid) {
        log.info("Starting Instance: {}", ocid);

        Instance instance = getInstance(ocid);
        if (instance.getLifecycleState() == Stopped) {
            InstanceActionResponse response = instanceAction(ocid, START);
            log.info("Start response code: {}", response.get__httpStatusCode__());
            instance = response.getInstance();
        } else {
            log.info("The instance was in the incorrect state ({}) to start: {}",
                    instance.getLifecycleState(), ocid);
        }

        log.info("Started Instance: {}", ocid);
        return new OciInstanceData(instance);
    }

    @Post("/stop/{ocid}")
    OciInstanceData stop(String ocid) {
        log.info("Stopping Instance: {}", ocid);

        Instance instance = getInstance(ocid);
        if (instance.getLifecycleState() == Running) {
            InstanceActionResponse response = instanceAction(ocid, STOP);
            log.info("Stop response code: {}", response.get__httpStatusCode__());
            instance = response.getInstance();
        } else {
            log.info("The instance was in the incorrect state ({}) to stop: {}",
                    instance.getLifecycleState(), ocid);
        }

        log.info("Stopped Instance: {}", ocid);
        return new OciInstanceData(instance);
    }

    private InstanceActionResponse instanceAction(String ocid, String action) {
        InstanceActionRequest request = InstanceActionRequest.builder()
                .instanceId(ocid)
                .action(action)
                .build();
        return computeClient.instanceAction(request);
    }

    private Instance getInstance(String ocid) {
        GetInstanceRequest getInstanceRequest = GetInstanceRequest.builder()
                .instanceId(ocid)
                .build();
        return computeClient.getInstance(getInstanceRequest).getInstance();
    }
}
