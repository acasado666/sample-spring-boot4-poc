package com.kodebytes.dto;

public record PersonRequestV2(
        String firstName,
        String lastName,
        String email,
        String passport
) {
}
