package dev.gepi.userjob.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DTOComparatorUtil {
    public static <T> List<String> getDifferentFields(T prevDto, T curDto) throws IllegalAccessException {
        List<String> differentFields = new ArrayList<>();
        String className = prevDto.getClass().getSimpleName();
        for (Field field : prevDto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value1 = field.get(prevDto);
            Object value2 = field.get(curDto);
            if (value1 == null && value2 == null) {
                continue;
            }
            if (value1 == null || !value1.equals(value2)) {
                differentFields.add(className + '.' +field.getName());
            }
        }
        return differentFields;
    }
}
