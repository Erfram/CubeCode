package com.cubecode.client.treesitter;

import org.jetbrains.annotations.NotNull;
import org.treesitter.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeSitterParser {

    public static HashMap<String, Integer> highlightsMap = new HashMap<>();

    public static int defaultCode = rgbaToImguiColor(248, 248, 242, 255);
    public static int punctuation = rgbaToImguiColor(200, 200, 200, 255);
    public static int number = rgbaToImguiColor(174, 129, 255, 255);
    public static int string = rgbaToImguiColor(230, 219, 116, 255);
    public static int charLiteral = rgbaToImguiColor(255, 143, 128, 220);
    public static int keywords = rgbaToImguiColor(106, 90, 205, 255);
    public static int syntax = rgbaToImguiColor(255, 143, 128, 220);
    public static int identifier = punctuation;
    public static int commentSingle = rgbaToImguiColor(180, 180, 180, 100);
    public static int commentMulti = rgbaToImguiColor(166, 226, 46, 200);
    public static int background = rgbaToImguiColor(39, 40, 34, 150);
    public static int cursor = rgbaToImguiColor(255, 255, 255, 255);
    public static int selection = rgbaToImguiColor(108, 153, 255, 80);
    public static int breakpoint = rgbaToImguiColor(80, 80, 80, 200);
    public static int lineNumber = rgbaToImguiColor(0, 0, 0, 30);
    public static int currentLineFill = rgbaToImguiColor(255, 255, 255, 50);
    public static int currentLineFillInactive = rgbaToImguiColor(255, 255, 255, 10);
    public static int currentLineEdge = rgbaToImguiColor(255, 255, 255, 50);

    static {
        highlightsMap.put("declaration", syntax);
        highlightsMap.put("expression", syntax);
        highlightsMap.put("pattern", syntax);
        highlightsMap.put("statement", syntax);
        highlightsMap.put("array", syntax);
        highlightsMap.put("array_pattern", syntax);
        highlightsMap.put("arrow_function", syntax);
        highlightsMap.put("assignment_pattern", syntax);
        highlightsMap.put("break_statement", syntax);
        highlightsMap.put("catch_clause", syntax);
        highlightsMap.put("class", syntax);
        highlightsMap.put("class_body", syntax);
        highlightsMap.put("class_declaration", syntax);
        highlightsMap.put("class_heritage", syntax);
        highlightsMap.put("class_static_block", syntax);
        highlightsMap.put("comment", commentSingle);
        highlightsMap.put("computed_property_name", syntax);
        highlightsMap.put("continue_statement", syntax);
        highlightsMap.put("debugger_statement", syntax);
        highlightsMap.put("decorator", syntax);
        highlightsMap.put("do_statement", syntax);
        //highlightsMap.put("else_clause", syntax);
        highlightsMap.put("empty_statement", syntax);
        highlightsMap.put("export_clause", syntax);
        highlightsMap.put("export_specifier", syntax);
        highlightsMap.put("export_statement", syntax);
        highlightsMap.put("field_definition", syntax);
        highlightsMap.put("finally_clause", syntax);
        highlightsMap.put("for_in_statement", syntax);
        highlightsMap.put("for_statement", syntax);
        highlightsMap.put("glimmer_closing_tag", syntax);
        highlightsMap.put("glimmer_opening_tag", syntax);
        highlightsMap.put("glimmer_template", syntax);
        //highlightsMap.put("if_statement", syntax);
        highlightsMap.put("import", syntax);
        highlightsMap.put("import_attribute", syntax);
        highlightsMap.put("import_clause", syntax);
        highlightsMap.put("import_specifier", syntax);
        highlightsMap.put("import_statement", syntax);
        highlightsMap.put("jsx_attribute", syntax);
        highlightsMap.put("jsx_closing_element", syntax);
        highlightsMap.put("jsx_element", syntax);
        highlightsMap.put("jsx_namespace_name", syntax);
        highlightsMap.put("jsx_opening_element", syntax);
        highlightsMap.put("jsx_self_closing_element", syntax);
        highlightsMap.put("jsx_text", syntax);
        highlightsMap.put("labeled_statement", syntax);
        highlightsMap.put("lexical_declaration", syntax);
        highlightsMap.put("meta_property", syntax);
        highlightsMap.put("method_definition", syntax);
        highlightsMap.put("named_imports", syntax);
        highlightsMap.put("namespace_export", syntax);
        highlightsMap.put("namespace_import", syntax);
        highlightsMap.put("object", syntax);
        highlightsMap.put("object_assignment_pattern", syntax);
        highlightsMap.put("object_pattern", syntax);
        highlightsMap.put("pair", syntax);
        highlightsMap.put("pair_pattern", syntax);
        highlightsMap.put("program", syntax);
        highlightsMap.put("regex", string);
        highlightsMap.put("rest_pattern", syntax);
        highlightsMap.put("return_statement", syntax);
        highlightsMap.put("spread_element", syntax);
        highlightsMap.put("string", string);
        highlightsMap.put("switch_body", syntax);
        highlightsMap.put("switch_case", syntax);
        highlightsMap.put("switch_default", syntax);
        highlightsMap.put("switch_statement", syntax);
        highlightsMap.put("template_string", syntax);
        highlightsMap.put("template_substitution", syntax);
        highlightsMap.put("throw_statement", syntax);
        highlightsMap.put("try_statement", syntax);
        highlightsMap.put("while_statement", syntax);
        highlightsMap.put("with_statement", syntax);

        highlightsMap.put("!", punctuation);
        highlightsMap.put("!=", punctuation);
        highlightsMap.put("!==", punctuation);
        highlightsMap.put("\"\"\"", punctuation);
        highlightsMap.put("${", punctuation);
        highlightsMap.put("%", punctuation);
        highlightsMap.put("%=", punctuation);
        highlightsMap.put("&", punctuation);
        highlightsMap.put("&&", punctuation);
        highlightsMap.put("&&=", punctuation);
        highlightsMap.put("&=", punctuation);
        highlightsMap.put("(", punctuation);
        highlightsMap.put(")", punctuation);
        highlightsMap.put("**", punctuation);
        highlightsMap.put("**=", punctuation);
        highlightsMap.put("*=", punctuation);
        highlightsMap.put("++", punctuation);
        highlightsMap.put("+=", punctuation);
        highlightsMap.put(",", punctuation);
        highlightsMap.put("--", punctuation);
        highlightsMap.put("-=", punctuation);
        highlightsMap.put(".", punctuation);
        highlightsMap.put("...", punctuation);
        highlightsMap.put("/", punctuation);
        highlightsMap.put("/=", punctuation);
        highlightsMap.put("/>", punctuation);
        highlightsMap.put(":", punctuation);
        highlightsMap.put(";", punctuation);
        highlightsMap.put("<", punctuation);
        highlightsMap.put("</", punctuation);
        highlightsMap.put("</template>", punctuation);
        highlightsMap.put("<<", punctuation);
        highlightsMap.put("<<=", punctuation);
        highlightsMap.put("<=", punctuation);
        highlightsMap.put("<template>", punctuation);
        highlightsMap.put("=", punctuation);
        highlightsMap.put("==", punctuation);
        highlightsMap.put("===", punctuation);
        highlightsMap.put("=>", punctuation);
        highlightsMap.put(">", punctuation);
        highlightsMap.put(">=", punctuation);
        highlightsMap.put(">>=", punctuation);
        highlightsMap.put(">>", punctuation);
        highlightsMap.put(">>>=", punctuation);
        highlightsMap.put(">>>", punctuation);
        highlightsMap.put("?", punctuation);
        highlightsMap.put("??", punctuation);
        highlightsMap.put("??=", punctuation);
        highlightsMap.put("@", punctuation);
        highlightsMap.put("[", punctuation);
        highlightsMap.put("]", punctuation);
        highlightsMap.put("^", punctuation);
        highlightsMap.put("^=", punctuation);
        highlightsMap.put("`", punctuation);
        highlightsMap.put("as", syntax);
        highlightsMap.put("async", syntax);
        highlightsMap.put("await", syntax);
        highlightsMap.put("break", syntax);
        highlightsMap.put("case", syntax);
        highlightsMap.put("catch", syntax);
        highlightsMap.put("const", syntax);
        highlightsMap.put("continue", syntax);
        highlightsMap.put("debugger", syntax);
        highlightsMap.put("default", syntax);
        highlightsMap.put("delete", syntax);
        highlightsMap.put("do", syntax);
        highlightsMap.put("else", syntax);
        highlightsMap.put("escape_sequence", syntax);
        highlightsMap.put("export", syntax);
        highlightsMap.put("extends", syntax);
        highlightsMap.put("false", syntax);
        highlightsMap.put("finally", syntax);
        highlightsMap.put("for", syntax);
        highlightsMap.put("from", syntax);
        highlightsMap.put("function", syntax);
        highlightsMap.put("get", syntax);
        highlightsMap.put("hash_bang_line", string);
        highlightsMap.put("html_character_reference", string);
        highlightsMap.put("html_comment", commentSingle);
        highlightsMap.put("identifier", identifier);
        highlightsMap.put("if", syntax);
        highlightsMap.put("in", syntax);
        highlightsMap.put("instanceof", syntax);
        highlightsMap.put("let", syntax);
        highlightsMap.put("new", syntax);
        highlightsMap.put("null", syntax);
        highlightsMap.put("number", number);
        highlightsMap.put("of", syntax);
        highlightsMap.put("optional_chain", syntax);
        highlightsMap.put("private_property_identifier", syntax);
        highlightsMap.put("property_identifier", syntax);
        highlightsMap.put("regex_flags", string);
        highlightsMap.put("regex_pattern", string);
        highlightsMap.put("return", syntax);
        highlightsMap.put("set", syntax);
        highlightsMap.put("shorthand_property_identifier", syntax);
        highlightsMap.put("shorthand_property_identifier_pattern", syntax);
        highlightsMap.put("statement_identifier", syntax);
        highlightsMap.put("static", syntax);
        highlightsMap.put("static get", syntax);
        highlightsMap.put("super", syntax);
        highlightsMap.put("switch", syntax);
        highlightsMap.put("target", syntax);
        highlightsMap.put("this", syntax);
        highlightsMap.put("throw", syntax);
        highlightsMap.put("true", syntax);
        highlightsMap.put("try", syntax);
        highlightsMap.put("typeof", syntax);
        highlightsMap.put("undefined", syntax);
        highlightsMap.put("var", syntax);
        highlightsMap.put("void", syntax);
        highlightsMap.put("while", syntax);
        highlightsMap.put("with", syntax);
        highlightsMap.put("yield", syntax);
        highlightsMap.put("{", punctuation);
        highlightsMap.put("|", punctuation);
        highlightsMap.put("|=", punctuation);
        highlightsMap.put("||", punctuation);
        highlightsMap.put("||=", punctuation);
        highlightsMap.put("}", punctuation);
        highlightsMap.put("~", punctuation);

    }

    private static int rgbaToImguiColor(int r, int g, int b, int a) {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public static HashMap<Integer, ArrayList<CodeLabel>> parse(ArrayList<String> lines) {
        StringBuilder code = new StringBuilder();
        for (String line : lines) {
            code.append(line).append("\n");
        }
        return parse(code.toString());
    }

    public static HashMap<Integer, ArrayList<CodeLabel>> parse(String code) {
        TSParser parser = new TSParser();
        TreeSitterJavascript js = new TreeSitterJavascript();
        parser.setLanguage(js);

        TSTree tree = parser.parseString(null, code.toString());

        ArrayList<HighLight> highlights = getHighLights(tree);
        HashMap<Integer, ArrayList<CodeLabel>> all = new HashMap<>();

        int i = 0;
        while (i < highlights.size()) {
            HighLight current = highlights.get(i);
            String highlight = code.substring(current.start(), current.end());

            all.computeIfAbsent(current.point().getRow(), k -> new ArrayList<>())
                    .add(new CodeLabel(highlight, current.color(), current.point(), current.type()));


            if (i == highlights.size() - 1) break;

            HighLight next = highlights.get(i + 1);

            if (current.end() + 1 != next.start()) {
                if (current.end() + 1 > next.start()) {
                    i++;
                    continue;
                }
            }
            i++;
        }

        return all;
    }

    @NotNull
    private static ArrayList<HighLight> getHighLights(TSTree tree) {
        TSNode rootNode = tree.getRootNode();
        TSTreeCursor cursor = new TSTreeCursor(rootNode);
        cursor.gotoFirstChild();

        ArrayList<HighLight> highlights = new ArrayList<>();
        boolean reached_root = false;

        while (!reached_root) {
            TSNode current = cursor.currentNode();
            if (!current.isNull()) {
                String type = current.getGrammarType();

                if (highlightsMap.containsKey(type)) {
                    Integer color = highlightsMap.get(type);
                    TSPoint point = current.getStartPoint();
                    highlights.add(
                            new HighLight(
                                    current.getStartByte(),
                                    current.getEndByte(),
                                    point,
                                    color,
                                    type
                            )
                    );
                }
            }

            if (cursor.gotoFirstChild()) {
                continue;
            }

            if (cursor.gotoNextSibling()) {
                continue;
            }

            boolean retracing = true;

            while (retracing) {

                if (!cursor.gotoParent()) {
                    retracing = false;
                    reached_root = true;
                }

                if (cursor.gotoNextSibling()) {
                    retracing = false;
                }

            }
        }
        return highlights;
    }

}
