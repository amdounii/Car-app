package com.car.car_app.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }

        return attribute.stream()
                .map(String::trim)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
