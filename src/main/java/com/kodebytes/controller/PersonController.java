package com.kodebytes.controller;

import com.kodebytes.dto.PersonRequestV1;
import com.kodebytes.dto.PersonRequestV2;
import com.kodebytes.dto.PersonResponseV1;
import com.kodebytes.dto.PersonResponseV2;
import com.kodebytes.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personSerVice;

    public PersonController(PersonService personSerVice) {
        this.personSerVice = personSerVice;
    }


    // USING REQUEST HEADER ======================================================

    /// GET -> localhost:8080/api/v1/persons
    @GetMapping(value = "/persons", version = "1.0")
    public ResponseEntity<List<PersonResponseV1>> getPersonsV1() {
        log.info("Finding all persons v1 using request header");

        List<PersonResponseV1> personsV1 = personSerVice.getPersonsV1();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personsV1);
    }

    /// GET -> localhost:8080/api/v2/persons
    @GetMapping(value = "/persons", version = "2.0")
    public ResponseEntity<List<PersonResponseV2>> getPersonsV2() {
        log.info("Finding all persons v2 using request header");

        List<PersonResponseV2> personsV2 = personSerVice.getPersonsV2();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personsV2);
    }

    /// GET -> localhost:8080/api/persons/1
    @GetMapping(value = "/persons/{id}", version = "1.0")
    public ResponseEntity<PersonResponseV1> findById(@PathVariable @Positive @NotNull Long id) {
        log.info("Finding a person by ID using request header version 1");

        PersonResponseV1 personByIdV1 = personSerVice.getPersonByIdV1(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personByIdV1);
    }

    /// GET -> localhost:8080/api/persons/1
    @GetMapping(value = "/persons/{id}", version = "2.0")
    public ResponseEntity<PersonResponseV2> fetchById(@PathVariable @Positive @NotNull Long id) {
        log.info("Finding a person by ID using request header version 2");

        PersonResponseV2 personByIdV2 = personSerVice.getPersonByIdV2(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personByIdV2);
    }

    /// POST -> localhost:8080/api/persons
    /// {
    ///     "name": "Steve Martin",
    ///     "email": "s.martin@gmail.com",
    ///     "passport": "678901"
    /// }
    @PostMapping(value = "/persons", version = "1.0")
    public ResponseEntity<PersonResponseV1> add(@RequestBody @Valid PersonRequestV1 personRequestV1) {
        log.info("Adding a person by PersonRequestV1 using request header version 1");

        PersonResponseV1 personResponseV1 = personSerVice.addPersonV1(personRequestV1);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personResponseV1);
    }

    /// POST -> localhost:8080/api/persons
    /// {
    ///     "firstName": "Daniel",
    ///     "lastName": "Lewis",
    ///     "email": "d.lewis@gmail.com",
    ///     "passport": "789012"
    /// }
    @PostMapping(value = "/persons", version = "2.0")
    public ResponseEntity<PersonResponseV2> add(@RequestBody @Valid PersonRequestV2 personRequestV2) {
        log.info("Adding a person by PersonRequestV2 using request header version 2");

        PersonResponseV2 personResponseV2 = personSerVice.addPersonV2(personRequestV2);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(personResponseV2);
    }

    /// PUT -> localhost:8080/api/persons
    /// {
    ///   "id": 6,
    ///   "name": "Steve Merkel",
    ///   "email": "s.merkel@gmail.com",
    ///   "passport": "890123"
    /// }
    @PutMapping(value = "/persons", version = "1.0")
    public ResponseEntity<PersonResponseV1> update(@RequestBody @Valid PersonResponseV1 personResponseV1) {
        log.info("Update person data by PersonRequestV1 using request header version 1");

        PersonResponseV1 personUpdatedV1 = personSerVice.updatePersonV1(personResponseV1);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personUpdatedV1);
    }

    /// PUT -> localhost:8080/api/persons
    /// {
    ///   "id": 7,
    ///   "firstName": "Dani",
    ///   "lastName": "DeVito",
    ///   "email": "d.devito@gmail.com",
    ///   "passport": "901234"
    /// }
    @PutMapping(value = "/persons", version = "2.0")
    public ResponseEntity<PersonResponseV2> update(@RequestBody @Valid PersonResponseV2 personResponseV2) {
        log.info("Update person data by PersonRequestV2 using request header version 2");

        PersonResponseV2 personUpdatedV2 = personSerVice.updatePersonV2(personResponseV2);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personUpdatedV2);
    }

    /// DELETE -> localhost:8080/api/persons/6
    @DeleteMapping(value = "/persons/{id}", version = "1.0+")
    public ResponseEntity<Void> delete(@PathVariable @Positive @NotNull Long id) {
        log.info("Delete person data by Id using request header version 2");

        personSerVice.deletePerson(id);
        ;
        return ResponseEntity
                .noContent()
                .build();
    }
}
