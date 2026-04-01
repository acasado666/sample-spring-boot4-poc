package com.kodebytes.service;

import com.kodebytes.dto.PersonRequestV1;
import com.kodebytes.dto.PersonRequestV2;
import com.kodebytes.dto.PersonResponseV1;
import com.kodebytes.dto.PersonResponseV2;
import com.kodebytes.exception.PersonAlreadyExistsException;
import com.kodebytes.exception.ResourceNotFoundException;
import com.kodebytes.mapper.PersonMapper;
import com.kodebytes.model.Person;
import com.kodebytes.repository.PersonRepository;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@EnableResilientMethods
@Validated
@NullMarked
public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    // USING PATH SEGMENT ======================================================

    public List<PersonResponseV1> findAllV1() {
        log.info("Finding all persons v1");
        return personRepository.findAll().stream().map(personMapper::toV1).toList();
    }

    public List<PersonResponseV2> findAllV2() {
        log.info("Finding all persons v2");
        return personRepository.findAll().stream().map(personMapper::toV2).toList();
    }

    // USING REQUEST HEADER ======================================================

    @ConcurrencyLimit(value = 3) // Allow only 3 concurrent calls
    public List<PersonResponseV1> getPersonsV1() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV1)
                .toList();
    }

    @ConcurrencyLimit(value = 2) // Allow only 2 concurrent calls
    public List<PersonResponseV2> getPersonsV2() {
        log.info("Find All Persons using request header: {}", "v2");
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV2)
                .toList();
    }

    @Nullable
    @Retryable(
            includes = {ResourceNotFoundException.class, TimeoutException.class}, // Specific exceptions
            maxRetries = 3,            // Number of retries AFTER the first failure
            delay = 1000,              // 1-second base delay
            multiplier = 2.0,          // Exponential backoff (1s, 2s, 4s)
            maxDelay = 10000,          // Cap delay at 10 seconds
            jitter = 200               // Adds +/- 200ms randomness to prevent "thundering herd"
    )
    public PersonResponseV1 getPersonByIdV1(Long id) {
        return personMapper.toV1(personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person", "id", id.toString()))
        );
    }

    @Nullable
    @Retryable(
            includes = {ResourceNotFoundException.class, TimeoutException.class}, // Specific exceptions
            maxRetries = 3,            // Number of retries AFTER the first failure
            delay = 1000,              // 1-second base delay
            multiplier = 2.0,          // Exponential backoff (1s, 2s, 4s)
            maxDelay = 10000,          // Cap delay at 10 seconds
            jitter = 200               // Adds +/- 200ms randomness to prevent "thundering herd"
    )
    public PersonResponseV2 getPersonByIdV2(Long id) {
        return personMapper.toV2(personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person", "id", id.toString()))
        );
    }

    public PersonResponseV1 addPersonV1(PersonRequestV1 personRequestV1) {
        Optional<Person> optionalPerson = personRepository.findByPassport(personRequestV1.passport());
        if (optionalPerson.isPresent()) {
            throw new PersonAlreadyExistsException("Person already exists");
        }
        Person newPerson = personMapper.toEntity(personRequestV1);
        return personMapper.toV1(personRepository.save(newPerson));
    }

    public PersonResponseV2 addPersonV2(PersonRequestV2 personRequestV2) {
        Optional<Person> optionalPerson = personRepository.findByPassport(personRequestV2.passport());
        if (optionalPerson.isPresent()) {
            throw new PersonAlreadyExistsException("Person already exists");
        }
        Person newPerson = personMapper.toEntity(personRequestV2);
        return personMapper.toV2(personRepository.save(newPerson));
    }

    public PersonResponseV1 updatePersonV1(PersonResponseV1 personResponseV1) {
        Optional<Person> optionalPerson = personRepository.findByPassport(personResponseV1.passport());
        if (optionalPerson.isPresent()) {
            throw new ResourceNotFoundException("Person", "passport", personResponseV1.passport());
        }
        Person updated = personMapper.updateEntity(personResponseV1);
        return personMapper.toV1(personRepository.save(updated));
    }

    public PersonResponseV2 updatePersonV2(PersonResponseV2 personResponseV2) {
        Optional<Person> optionalPerson = personRepository.findByPassport(personResponseV2.passport());
        if (optionalPerson.isPresent()) {
            throw new ResourceNotFoundException("Person", "passport", personResponseV2.passport());
        }
        Person updated = personMapper.updateEntity(personResponseV2);
        return personMapper.toV2(personRepository.save(updated));
    }

    public boolean deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "id", id.toString())
        );
        personRepository.deleteById(person.getId());
        return true;
    }


    // USING REQUEST PARAMETER (Query Parameter) ===================================

    public List<PersonResponseV1> listPersonsV1() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV1)
                .toList();
    }

    public List<PersonResponseV2> listPersonsV2() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV2)
                .toList();
    }

    // USING MEDIA TYPE (Content Negotiation) =======================================

    public List<PersonResponseV1> getPersonsMediaV1() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV1)
                .toList();
    }

    public List<PersonResponseV2> getPersonsMediaV2() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV2)
                .toList();
    }
}
