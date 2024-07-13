package com.cubecode.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextUtils {
    public static MutableText formatText(String input) {
        MutableText result = Text.empty();
        StringBuilder currentText = new StringBuilder();
        Style currentStyle = Style.EMPTY;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '&' && i + 1 < input.length()) {
                char formatChar = input.charAt(i + 1);
                Style newStyle = getStyleFromChar(formatChar, currentStyle);

                if (newStyle != null || formatChar == 'r') {
                    if (!currentText.isEmpty()) {
                        result.append(Text.literal(currentText.toString()).setStyle(currentStyle));
                        currentText = new StringBuilder();
                    }
                    currentStyle = (formatChar == 'r') ? Style.EMPTY : newStyle;
                    i++; // Skip the next character
                    continue;
                }
            }
            currentText.append(c);
        }

        if (!currentText.isEmpty()) {
            result.append(Text.literal(currentText.toString()).setStyle(currentStyle));
        }

        return result;
    }

    private static Style getStyleFromChar(char c, Style currentStyle) {
        Formatting formatting = Formatting.byCode(c);
        if (formatting == null) return null;

        return switch (formatting) {
            case BOLD -> currentStyle.withBold(true);
            case ITALIC -> currentStyle.withItalic(true);
            case UNDERLINE -> currentStyle.withUnderline(true);
            case STRIKETHROUGH -> currentStyle.withStrikethrough(true);
            case OBFUSCATED -> currentStyle.withObfuscated(true);
            default -> currentStyle.withColor(formatting);
        };
    }
}
