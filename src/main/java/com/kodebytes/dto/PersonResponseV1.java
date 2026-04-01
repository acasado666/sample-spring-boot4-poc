package com.kodebytes.dto;

public record PersonResponseV1(
        Long id,
        String name,
        String email,
        String passport
) {
}
