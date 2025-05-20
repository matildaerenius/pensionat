package se.backend1.pensionat.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLongConverter implements Converter<String, Long> {
    @Override
    public Long convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(source);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ogiltigt ID-värde: " + source);
        }
    }
}
