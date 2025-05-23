package se.backend1.pensionat.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        if (source == null || source.trim().isEmpty() || source.equalsIgnoreCase("null")) {
            return null;
        }
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ogiltigt heltalsvärde: " + source);
        }
    }
}
