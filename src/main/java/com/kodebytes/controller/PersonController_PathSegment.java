package com.kodebytes.controller;

import com.kodebytes.dto.PersonResponseV1;
import com.kodebytes.dto.PersonResponseV2;
import com.kodebytes.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiPathSegment")
public class PersonController_PathSegment {
    private static final Logger log = LoggerFactory.getLogger(PersonController_PathSegment.class);
    private final PersonService personService;

    public PersonController_PathSegment(PersonService personService) {
        this.personService = personService;
    }

    // USING PATH SEGMENT ======================================================

    ///  GET -> localhost:8080/apiPathSegment/v1/persons
    @GetMapping(value = "/persons/{version}", version = "1.0")
    public ResponseEntity<List<PersonResponseV1>> findAllv1() {
        log.info("Finding all persons v1 using path segment");

        List<PersonResponseV1> allV1 = personService.findAllV1();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allV1);
    }

    ///  GET -> localhost:8080/apiPathSegment/v2/persons
    @GetMapping(value = "/persons/{version}", version = "2.0")
    public ResponseEntity<List<PersonResponseV2>>findAllv2() {
        log.info("Finding all persons v2 using path segment");

        List<PersonResponseV2> allV2 = personService.findAllV2();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allV2);
    }

}
