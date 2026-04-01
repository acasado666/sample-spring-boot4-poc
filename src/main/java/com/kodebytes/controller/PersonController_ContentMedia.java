package com.kodebytes.controller;

import com.kodebytes.dto.PersonResponseV1;
import com.kodebytes.dto.PersonResponseV2;
import com.kodebytes.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiMedia")
public class PersonController_ContentMedia {

    private static final Logger log = LoggerFactory.getLogger(PersonController_ReqParam.class);
    private final PersonService personService;

    public PersonController_ContentMedia(PersonService personService) {
        this.personService = personService;
    }

    // USING MEDIA TYPE (Content Negotiation) =======================================

    ///  GET -> localhost:8080/apiMedia/persons
    @GetMapping(value = "/persons", version = "1.0", produces = "application/json")
    public ResponseEntity<List<PersonResponseV1>> getPersonsMediaV1() {
        log.info("Find All persons using media type versioning produces = \"application/json\" version=1.0");

        List<PersonResponseV1> allV1 = personService.getPersonsMediaV1();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allV1);
    }

    ///  GET -> localhost:8080/apiMedia/persons
    @GetMapping(value = "/persons", version = "2.0", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonResponseV2>> getPersonsMediaV2() {
        log.info("Find All persons using media type versioning produces = MediaType.APPLICATION_JSON_VALUE version=2.0");

        List<PersonResponseV2> allV2 = personService.getPersonsMediaV2();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allV2);
    }
}
