package com.kodebytes.dto;

public record PersonResponseV2(
        Long id,
        String firstName,
        String lastName,
        String email,
        String passport
) {
}
