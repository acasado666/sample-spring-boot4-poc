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
@RequestMapping("/apiReqParam")
public class PersonController_ReqParam {

    private static final Logger log = LoggerFactory.getLogger(PersonController_ReqParam.class);
    private final PersonService personService;

    public PersonController_ReqParam(PersonService personService) {
        this.personService = personService;
    }

    // USING REQUEST PARAMETER (Query Parameter) ===================================

    ///  GET -> localhost:8080/apiReqParam/persons?version=v1
    @GetMapping(value = "/persons", params = "version=1.0")
    public ResponseEntity<List<PersonResponseV1>> listPersonsV1() {
        log.info("Find All persons using request parameter, version=1.0");

        List<PersonResponseV1> allV1 = personService.listPersonsV1();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allV1);
    }

    ///  GET -> localhost:8080/apiReqParam/persons?version=v2
    @GetMapping(value = "/persons", params = "version=v2")
    public ResponseEntity<List<PersonResponseV2>> listPersonsV2() {
        log.info("Find All persons using request parameter, version=v2");

        List<PersonResponseV2> allV2 = personService.listPersonsV2();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allV2);
    }


}
