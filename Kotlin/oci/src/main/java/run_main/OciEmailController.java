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

import io.micronaut.email.Attachment;
import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.template.TemplateBody;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;

import static io.micronaut.email.BodyType.HTML;
import static io.micronaut.http.MediaType.APPLICATION_OCTET_STREAM_TYPE;
import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static java.util.Collections.singletonMap;

@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/email") // <2>
class OciEmailController {

    private final EmailSender<?, ?> emailSender;

    OciEmailController(EmailSender<?, ?> emailSender) { // <3>
        this.emailSender = emailSender;
    }

    @Post(uri = "/basic", produces = TEXT_PLAIN) // <4>
    String index() {
        emailSender.send(Email.builder()
                .to("basic@gdk.example")
                .subject("Micronaut Email Basic Test: " + LocalDateTime.now())
                .body("Basic email"));  // <5>
        return "Email sent.";
    }

    @Post(uri = "/template/{name}", produces = TEXT_PLAIN) // <4>
    String template(String name) {
        emailSender.send(Email.builder()
                .to("template@gdk.example")
                .subject("Micronaut Email Template Test: " + LocalDateTime.now())
                .body(new TemplateBody<>(HTML,
                        new ModelAndView<>("email", singletonMap("name", name))))); // <6>
        return "Email sent.";
    }

    @Post(uri = "/attachment", produces = TEXT_PLAIN, consumes = MULTIPART_FORM_DATA) // <7>
    String attachment(CompletedFileUpload file) throws IOException {
        emailSender.send(Email.builder()
                .to("attachment@gdk.example")
                .subject("Micronaut Email Attachment Test: " + LocalDateTime.now())
                .body("Attachment email")
                .attachment(Attachment.builder()
                        .filename(file.getFilename())
                        .contentType(file.getContentType().orElse(APPLICATION_OCTET_STREAM_TYPE).toString())
                        .content(file.getBytes())
                        .build()
                )); // <8>
        return "Email sent.";
    }
}
