package com.cubecode.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * Заменяет первое вхождение подстроки или regex-паттерна
     * @param source исходная строка
     * @param pattern что заменяем (может быть regex или обычной строкой)
     * @param replacement на что заменяем
     * @param regexMode true - использовать regex, false - обычную замену
     */
    public static String replaceFirst(String source, String pattern, String replacement, boolean regexMode) {
        if (!regexMode) {
            // Экранируем специальные символы, чтобы они воспринимались как литералы
            pattern = Pattern.quote(pattern);
            replacement = Matcher.quoteReplacement(replacement);
        }
        return source.replaceFirst(pattern, replacement);
    }

    /**
     * Заменяет все вхождения подстроки или regex-паттерна
     * @param source исходная строка
     * @param pattern что заменяем (может быть regex или обычной строкой)
     * @param replacement на что заменяем
     * @param regexMode true - использовать regex, false - обычную замену
     */
    public static String replaceAll(String source, String pattern, String replacement, boolean regexMode) {
        if (!regexMode) {
            // Экранируем специальные символы, чтобы они воспринимались как литералы
            pattern = Pattern.quote(pattern);
            replacement = Matcher.quoteReplacement(replacement);
        }
        return source.replaceAll(pattern, replacement);
    }
}
