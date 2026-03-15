package io.github.amsatrio.spring_crud_demo.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    public static Map<String, String> modelToHeaderMap(Object model) {
        Map<String, String> modelMap = new HashMap<>();
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            modelMap.put(field.getName(), field.getType().getSimpleName().toLowerCase());
        }
        return modelMap;
    }

    public static String camelCaseToTitleCase(String camelCase) {
        StringBuilder titleCase = new StringBuilder();
        boolean prevCharWasLowerCase = false;
        for (char c : camelCase.toCharArray()) {
            if (Character.isLowerCase(c)) {

                titleCase.append(Character.toUpperCase(c));
                prevCharWasLowerCase = true;
            } else {
                if (prevCharWasLowerCase && !titleCase.isEmpty()) {
                    titleCase.append(" ");
                }
                titleCase.append(c);
                prevCharWasLowerCase = false;
            }
        }
        return titleCase.toString();
    }
}
