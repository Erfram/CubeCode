package com.cubecode.client.imgui.languages;

import imgui.extension.texteditor.TextEditorLanguageDefinition;

import java.util.HashMap;

public class JavaScriptDefinition {

    public static TextEditorLanguageDefinition build() {
        String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'";
        String SINGLE_COMMENT_PATTERN = "(//(?!\\bTODO\\b).*)";
        String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?\\b";
        String TODO_PATTERN = "(//TODO.*)";
        String PUNCTUATION_PATTERN = "[\\[\\]\\{\\}\\!\\%\\^\\&\\*\\(\\)\\-\\+\\=\\~\\|\\<\\>\\?\\/\\;\\,\\.]";

        String[] cubecodeKeywords = new String[]{
                "Player", "World", "API" // TODO LAMA
        };

        String[] syntaxKeywords = new String[]{
                "break", "continue", "switch", "case", "default", "try",
                "catch", "delete", "do", "while", "else", "finally", "if",
                "else", "for", "each", "in", "instanceof",
                "new", "throw", "typeof", "with", "yield", "return",
                // TODO maybe раздельно ?
                "const", "function", "var", "let", "prototype",
                // TODO maybe раздельно ?
                "this", "arguments"
        };

        String[] typeKeywords = new String[]{
                "true", "false", "null", "undefined"
        };

        HashMap<String, Integer> regex = new HashMap<>();

        // Default
        regex.put(PUNCTUATION_PATTERN, 1);
        regex.put(NUMBER_PATTERN, 2);
        regex.put(STRING_PATTERN, 3);
        regex.put(TODO_PATTERN, 3);
        regex.put(SINGLE_COMMENT_PATTERN, 10);
        // Default

        // CubeCode
        regex.put(collectRegex(typeKeywords), 2);
        regex.put(collectRegex(syntaxKeywords), 4);
        regex.put(collectRegexVariable(cubecodeKeywords), 5);
        // CubeCode

        TextEditorLanguageDefinition base = new TextEditorLanguageDefinition();

        base.setName("JavaScript");
        base.setTokenRegexStrings(regex);
        base.setCommentStart("/*");
        base.setCommentEnd("*/");
        return base;
    }

    private static String collectRegex(String[] objects) {
        StringBuilder builder = new StringBuilder();
        builder.append("([\\s]|^)(");
        for (String object : objects) {
            builder.append(object).append("|");
        }
        builder.replace(builder.length() - 1, builder.length(), "");
        builder.append(")\\b");
        return builder.toString();
    }

    private static String collectRegexVariable(String[] objects) {
        StringBuilder builder = new StringBuilder();
        builder.append("([\\s]|^)(");
        for (String object : objects) {
            builder.append(object).append("|");
        }
        builder.setLength(builder.length() - 1);
        builder.append(")(?=\\.)");
        return builder.toString();
    }

    private static int rgbaToImguiColor(int r, int g, int b, int a) {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public static int[] buildPallet() {
        return new int[]{
                /* + 0  Default code */                    rgbaToImguiColor(248, 248, 242, 255),
                /* - 1  Punctuation @TODO */               rgbaToImguiColor(200, 200, 200, 255),
                /* + 2  Number */                          rgbaToImguiColor(174, 129, 255, 255),
                /* + 3  String */                          rgbaToImguiColor(230, 219, 116, 255),
                /* - 4  Char literal @TODO */              rgbaToImguiColor(255, 143, 128, 220),
                /* - 5  CubeCode Keywords */               rgbaToImguiColor(106, 90, 205, 255),
                /* - 6  Preprocessor @TODO */              rgbaToImguiColor(255, 0, 0, 255),
                /* - 7  Identifier @TODO */                rgbaToImguiColor(0, 255, 0, 255),
                /* - 8  Known identifier @TODO */          rgbaToImguiColor(0, 0, 255, 255),
                /* - 9  Preproc identifier @TODO */        rgbaToImguiColor(255, 0, 0, 255),
                /* + 10 Comment (single line) */           rgbaToImguiColor(180, 180, 180, 100),
                /* + 11 Comment (multi line) */            rgbaToImguiColor(166, 226, 46, 200),
                /* + 12 Background */                      rgbaToImguiColor(39, 40, 34, 150),
                /* + 13 Cursor */                          rgbaToImguiColor(255, 255, 255, 255),
                /* + 14 Selection */                       rgbaToImguiColor(108, 153, 255, 80),
                /* - 15 ErrorMarker */                     rgbaToImguiColor(255, 0, 0, 255),
                /* - 16 ControlCharacter @TODO */          rgbaToImguiColor(255, 0, 0, 255),
                /* + 17 Breakpoint number */               rgbaToImguiColor(80, 80, 80, 200),
                /* + 18 Line number */                     rgbaToImguiColor(0, 0, 0, 30),
                /* + 19 Current line fill */               rgbaToImguiColor(255, 255, 255, 50),
                /* + 20 Current line fill (inactive) */    rgbaToImguiColor(255, 255, 255, 10),
                /* + 21 Current line edge */               rgbaToImguiColor(255, 255, 255, 50),
        };
    }

}
