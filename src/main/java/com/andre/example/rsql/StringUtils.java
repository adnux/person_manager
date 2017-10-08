package com.andre.example.rsql;

import static java.util.Objects.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

public final class StringUtils {

    private static final char SEPARATOR = '-';
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final Map<String, String> ACENTOS_TEMPLATE;

    static {
        Map<String, String> acentosTemplate = new HashMap<String, String>();

        acentosTemplate.put("á", "a");
        acentosTemplate.put("à", "a");
        acentosTemplate.put("â", "a");
        acentosTemplate.put("ä", "a");
        acentosTemplate.put("ã", "a");

        acentosTemplate.put("é", "e");
        acentosTemplate.put("è", "e");
        acentosTemplate.put("ê", "e");
        acentosTemplate.put("ë", "e");
        acentosTemplate.put("ẽ", "e");

        acentosTemplate.put("í", "i");
        acentosTemplate.put("ì", "i");
        acentosTemplate.put("î", "i");
        acentosTemplate.put("ï", "i");
        acentosTemplate.put("ĩ", "i");

        acentosTemplate.put("ó", "o");
        acentosTemplate.put("ò", "o");
        acentosTemplate.put("ô", "o");
        acentosTemplate.put("ö", "o");
        acentosTemplate.put("õ", "o");

        acentosTemplate.put("ú", "u");
        acentosTemplate.put("ù", "u");
        acentosTemplate.put("û", "u");
        acentosTemplate.put("ü", "u");
        acentosTemplate.put("ũ", "u");

        acentosTemplate.put("ñ", "n");

        acentosTemplate.put("ç", "c");

        acentosTemplate.put("Á", "A");
        acentosTemplate.put("À", "A");
        acentosTemplate.put("Â", "A");
        acentosTemplate.put("Ä", "A");
        acentosTemplate.put("Ã", "A");

        acentosTemplate.put("É", "E");
        acentosTemplate.put("È", "E");
        acentosTemplate.put("Ê", "E");
        acentosTemplate.put("Ë", "E");
        acentosTemplate.put("Ẽ", "E");

        acentosTemplate.put("Í", "I");
        acentosTemplate.put("Ì", "I");
        acentosTemplate.put("Î", "I");
        acentosTemplate.put("Ï", "I");
        acentosTemplate.put("Ĩ", "I");

        acentosTemplate.put("Ó", "O");
        acentosTemplate.put("Ò", "O");
        acentosTemplate.put("Ô", "O");
        acentosTemplate.put("Ö", "O");
        acentosTemplate.put("Õ", "O");

        acentosTemplate.put("Ú", "U");
        acentosTemplate.put("Ù", "U");
        acentosTemplate.put("Û", "U");
        acentosTemplate.put("Ü", "U");
        acentosTemplate.put("Ũ", "U");

        acentosTemplate.put("Ñ", "N");

        acentosTemplate.put("Ç", "C");

        ACENTOS_TEMPLATE = Collections.unmodifiableMap(acentosTemplate);
    }

    private StringUtils() {
    }

    public static <T> String defaultIfNull(T value, Function<T, String> getValue, String defaultValue) {
        if (nonNull(value)) {
            return getValue.apply(value);
        }
        return defaultValue;
    }

    public static Optional<Range<Long>> parseRange(String range) {

        final List<Long> results = Splitter.on(SEPARATOR)
                .trimResults()
                .omitEmptyStrings()
                .splitToList(Strings.nullToEmpty(range))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            return Optional.empty();
        }

        final Long initialValue = results.get(0);
        final Long finalValue = results.stream().skip(1).findFirst().orElse(initialValue);

        return Optional.of(Range.between(initialValue, finalValue));
    }

    public static String safeSubstring(String str, int start, int end) {

        if (Strings.isNullOrEmpty(str) || start > Strings.nullToEmpty(str).length()) {
            return EMPTY;
        }

        if (str.length() >= end) {
            return str.substring(start, end);
        }

        if (str.length() - 1 <= end) {
            return str.substring(start, str.length());
        }

        throw new IllegalArgumentException("Tipo de substring não esperado");
    }

    public static String retiraAcentos(String str) {
        if (isNull(str)) {
            return "";
        }
        return replaceTemplates(str, ACENTOS_TEMPLATE);
    }

    private static String replaceTemplates(String txt,
            Map<String, String> templates) {
        StringBuilder sb = new StringBuilder(txt);
        Set<String> keys = templates.keySet();
        for (String key : keys) {
            int p;
            int l = key.length();
            String s = templates.get(key);
            while ((p = sb.indexOf(key)) != -1) {
                sb.replace(p, p + l, s);
            }
        }
        return sb.toString();
    }

    public static String build(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object);
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String somenteNumeros(String valor) {
        if (nonNull(valor)) {
            return valor.replaceAll("[^0-9]", "");
        }
        return valor;
    }
}
