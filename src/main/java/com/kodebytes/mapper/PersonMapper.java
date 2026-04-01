package com.kodebytes.mapper;

import com.kodebytes.dto.PersonRequestV1;
import com.kodebytes.dto.PersonRequestV2;
import com.kodebytes.dto.PersonResponseV1;
import com.kodebytes.dto.PersonResponseV2;
import com.kodebytes.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {

    @Mapping(target = "name", expression = "java(combineName(person))")
    PersonResponseV1 toV1(Person person);

    PersonResponseV2 toV2(Person person);

    @Mapping(target = "firstName", expression = "java(extractFirstName(dto))")
    @Mapping(target = "lastName", expression = "java(extractLastName(dto))")
    Person toEntity(PersonResponseV1 dto);

    Person toEntity(PersonResponseV2 dto);

    @Mapping(target = "firstName", expression = "java(extractFirstName(dto))")
    @Mapping(target = "lastName", expression = "java(extractLastName(dto))")
    Person updateEntity(PersonResponseV1 dto);

    Person updateEntity(PersonResponseV2 dto);

    @Mapping(target = "firstName", expression = "java(extractFirstName(dto))")
    @Mapping(target = "lastName", expression = "java(extractLastName(dto))")
    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonRequestV1 dto);

    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonRequestV2 dto);

    default String combineName(Person person) {
        if (person == null) return null;
        String first = person.getFirstName() != null ? person.getFirstName().trim() : "";
        String last = person.getLastName() != null ? person.getLastName().trim() : "";
        if (first.isEmpty()) return last;
        if (last.isEmpty()) return first;
        return first + " " + last;
    }

    default String extractFirstName(PersonResponseV1 dto) {
        if (dto == null || dto.name() == null) return null;
        String[] parts = splitName(dto.name());
        return parts[0];
    }

    default String extractLastName(PersonResponseV1 dto) {
        if (dto == null || dto.name() == null) return null;
        String[] parts = splitName(dto.name());
        return parts[1];
    }

    default String extractFirstName(PersonRequestV1 dto) {
        if (dto == null || dto.name() == null) return null;
        String[] parts = splitName(dto.name());
        return parts[0];
    }

    default String extractLastName(PersonRequestV1 dto) {
        if (dto == null || dto.name() == null) return null;
        String[] parts = splitName(dto.name());
        return parts[1];
    }

    private String[] splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", ""};
        }
        String trimmed = fullName.trim();
        int lastSpaceIndex = trimmed.lastIndexOf(' ');
        if (lastSpaceIndex == -1) {
            return new String[]{trimmed, ""};
        }
        return new String[]{
                trimmed.substring(0, lastSpaceIndex),
                trimmed.substring(lastSpaceIndex + 1)
        };
    }
}
