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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller // <1>
class AuthController {

    @Secured(IS_ANONYMOUS) // <2>
    @View("auth") // <3>
    @Get // <4>
    Map<String, Object> index(@Nullable Authentication authentication) { // <5>
        Map<String, Object> model = new HashMap<>();
        if (authentication != null) {
            model.put("username", authentication.getAttributes().get("sub"));
        } else {
            model.put("username", "Anonymous");
        }
        return model;
    }

    @Secured(IS_AUTHENTICATED) // <6>
    @Get("/secure") // <7>
    Map<String, Object> secured() {
        return Collections.singletonMap("secured", true); // <8>
    }
}
