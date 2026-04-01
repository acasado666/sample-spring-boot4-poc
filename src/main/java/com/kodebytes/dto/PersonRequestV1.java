package com.kodebytes.dto;

public record PersonRequestV1(
        String name,
        String email,
        String passport
) {
}
