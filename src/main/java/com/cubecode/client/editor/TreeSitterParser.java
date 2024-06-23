package com.cubecode.client.editor;

import org.jetbrains.annotations.NotNull;
import org.treesitter.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeSitterParser {

    public static HashMap<String, Integer> highlightsMap = new HashMap<>();

    static {
        highlightsMap.put("declaration", -1);
        highlightsMap.put("expression", -1);
        highlightsMap.put("pattern", -1);
        highlightsMap.put("statement", -1);
        highlightsMap.put("array", -1);
        highlightsMap.put("array_pattern", -1);
        highlightsMap.put("arrow_function", -1);
        highlightsMap.put("assignment_pattern", -1);
        highlightsMap.put("break_statement", -1);
        highlightsMap.put("catch_clause", -1);
        highlightsMap.put("class", -1);
        highlightsMap.put("class_body", -1);
        highlightsMap.put("class_declaration", -1);
        highlightsMap.put("class_heritage", -1);
        highlightsMap.put("class_static_block", -1);
        highlightsMap.put("comment", -1);
        highlightsMap.put("computed_property_name", -1);
        highlightsMap.put("continue_statement", -1);
        highlightsMap.put("debugger_statement", -1);
        highlightsMap.put("decorator", -1);
        highlightsMap.put("do_statement", -1);
        highlightsMap.put("else_clause", -1);
        highlightsMap.put("empty_statement", -1);
        highlightsMap.put("export_clause", -1);
        highlightsMap.put("export_specifier", -1);
        highlightsMap.put("export_statement", -1);
        highlightsMap.put("field_definition", -1);
        highlightsMap.put("finally_clause", -1);
        highlightsMap.put("for_in_statement", -1);
        highlightsMap.put("for_statement", -1);
        highlightsMap.put("glimmer_closing_tag", -1);
        highlightsMap.put("glimmer_opening_tag", -1);
        highlightsMap.put("glimmer_template", -1);
        highlightsMap.put("if_statement", -1);
        highlightsMap.put("import", -1);
        highlightsMap.put("import_attribute", -1);
        highlightsMap.put("import_clause", -1);
        highlightsMap.put("import_specifier", -1);
        highlightsMap.put("import_statement", -1);
        highlightsMap.put("jsx_attribute", -1);
        highlightsMap.put("jsx_closing_element", -1);
        highlightsMap.put("jsx_element", -1);
        highlightsMap.put("jsx_namespace_name", -1);
        highlightsMap.put("jsx_opening_element", -1);
        highlightsMap.put("jsx_self_closing_element", -1);
        highlightsMap.put("jsx_text", -1);
        highlightsMap.put("labeled_statement", -1);
        highlightsMap.put("lexical_declaration", -1);
        highlightsMap.put("meta_property", -1);
        highlightsMap.put("method_definition", -1);
        highlightsMap.put("named_imports", -1);
        highlightsMap.put("namespace_export", -1);
        highlightsMap.put("namespace_import", -1);
        highlightsMap.put("object", -1);
        highlightsMap.put("object_assignment_pattern", -1);
        highlightsMap.put("object_pattern", -1);
        highlightsMap.put("pair", -1);
        highlightsMap.put("pair_pattern", -1);
        highlightsMap.put("program", -1);
        highlightsMap.put("regex", -1);
        highlightsMap.put("rest_pattern", -1);
        highlightsMap.put("return_statement", -1);
        highlightsMap.put("spread_element", -1);
        highlightsMap.put("string", -1);
        highlightsMap.put("switch_body", -1);
        highlightsMap.put("switch_case", -1);
        highlightsMap.put("switch_default", -1);
        highlightsMap.put("switch_statement", -1);
        highlightsMap.put("template_string", -1);
        highlightsMap.put("template_substitution", -1);
        highlightsMap.put("throw_statement", -1);
        highlightsMap.put("try_statement", -1);
        highlightsMap.put("while_statement", -1);
        highlightsMap.put("with_statement", -1);

        highlightsMap.put("!", -1);
        highlightsMap.put("!=", -1);
        highlightsMap.put("!==", -1);
        highlightsMap.put("\"\"\"", -1);
        highlightsMap.put("${", -1);
        highlightsMap.put("%", -1);
        highlightsMap.put("%=", -1);
        highlightsMap.put("&", -1);
        highlightsMap.put("&&", -1);
        highlightsMap.put("&&=", -1);
        highlightsMap.put("&=", -1);
        highlightsMap.put("(", -1);
        highlightsMap.put(")", -1);
        highlightsMap.put("**", -1);
        highlightsMap.put("**=", -1);
        highlightsMap.put("*=", -1);
        highlightsMap.put("++", -1);
        highlightsMap.put("+=", -1);
        highlightsMap.put(",", -1);
        highlightsMap.put("--", -1);
        highlightsMap.put("-=", -1);
        highlightsMap.put(".", -1);
        highlightsMap.put("...", -1);
        highlightsMap.put("/", -1);
        highlightsMap.put("/=", -1);
        highlightsMap.put("/>", -1);
        highlightsMap.put(":", -1);
        highlightsMap.put(";", -1);
        highlightsMap.put("<", -1);
        highlightsMap.put("</", -1);
        highlightsMap.put("</template>", -1);
        highlightsMap.put("<<", -1);
        highlightsMap.put("<<=", -1);
        highlightsMap.put("<=", -1);
        highlightsMap.put("<template>", -1);
        highlightsMap.put("=", -1);
        highlightsMap.put("==", -1);
        highlightsMap.put("===", -1);
        highlightsMap.put("=>", -1);
        highlightsMap.put(">", -1);
        highlightsMap.put(">=", -1);
        highlightsMap.put(">>=", -1);
        highlightsMap.put(">>", -1);
        highlightsMap.put(">>>=", -1);
        highlightsMap.put(">>>", -1);
        highlightsMap.put("?", -1);
        highlightsMap.put("??", -1);
        highlightsMap.put("??=", -1);
        highlightsMap.put("@", -1);
        highlightsMap.put("[", -1);
        highlightsMap.put("]", -1);
        highlightsMap.put("^", -1);
        highlightsMap.put("^=", -1);
        highlightsMap.put("`", -1);
        highlightsMap.put("as", -1);
        highlightsMap.put("async", -1);
        highlightsMap.put("await", -1);
        highlightsMap.put("break", -1);
        highlightsMap.put("case", -1);
        highlightsMap.put("catch", -1);
        highlightsMap.put("const", -1);
        highlightsMap.put("continue", -1);
        highlightsMap.put("debugger", -1);
        highlightsMap.put("default", -1);
        highlightsMap.put("delete", -1);
        highlightsMap.put("do", -1);
        highlightsMap.put("else", -1);
        highlightsMap.put("escape_sequence", -1);
        highlightsMap.put("export", -1);
        highlightsMap.put("extends", -1);
        highlightsMap.put("false", -1);
        highlightsMap.put("finally", -1);
        highlightsMap.put("for", -1);
        highlightsMap.put("from", -1);
        highlightsMap.put("function", -1);
        highlightsMap.put("get", -1);
        highlightsMap.put("hash_bang_line", -1);
        highlightsMap.put("html_character_reference", -1);
        highlightsMap.put("html_comment", -1);
        highlightsMap.put("identifier", -1);
        highlightsMap.put("if", -1);
        highlightsMap.put("in", -1);
        highlightsMap.put("instanceof", -1);
        highlightsMap.put("let", -1);
        highlightsMap.put("new", -1);
        highlightsMap.put("null", -1);
        highlightsMap.put("number", -1);
        highlightsMap.put("of", -1);
        highlightsMap.put("optional_chain", -1);
        highlightsMap.put("private_property_identifier", -1);
        highlightsMap.put("property_identifier", -1);
        highlightsMap.put("regex_flags", -1);
        highlightsMap.put("regex_pattern", -1);
        highlightsMap.put("return", -1);
        highlightsMap.put("set", -1);
        highlightsMap.put("shorthand_property_identifier", -1);
        highlightsMap.put("shorthand_property_identifier_pattern", -1);
        highlightsMap.put("statement_identifier", -1);
        highlightsMap.put("static", -1);
        highlightsMap.put("static get", -1);
        highlightsMap.put("super", -1);
        highlightsMap.put("switch", -1);
        highlightsMap.put("target", -1);
        highlightsMap.put("this", -1);
        highlightsMap.put("throw", -1);
        highlightsMap.put("true", -1);
        highlightsMap.put("try", -1);
        highlightsMap.put("typeof", -1);
        highlightsMap.put("undefined", -1);
        highlightsMap.put("var", -1);
        highlightsMap.put("void", -1);
        highlightsMap.put("while", -1);
        highlightsMap.put("with", -1);
        highlightsMap.put("yield", -1);
        highlightsMap.put("{", -1);
        highlightsMap.put("|", -1);
        highlightsMap.put("|=", -1);
        highlightsMap.put("||", -1);
        highlightsMap.put("||=", -1);
        highlightsMap.put("}", -1);
        highlightsMap.put("~", -1);

    }

    public static ArrayList<CodeLabel> parse(ArrayList<String> lines) {
        StringBuilder code = new StringBuilder();
        for (String line : lines) {
            code.append(line).append("\n");
        }
        return parse(code.toString());
    }

    public static ArrayList<CodeLabel> parse(String code) {
        TSParser parser = new TSParser();
        TreeSitterJavascript js = new TreeSitterJavascript();
        parser.setLanguage(js);


        TSTree tree = parser.parseString(null, code.toString());
        ArrayList<HighLight> highlights = getHighLights(tree);

        int i = 0;

        ArrayList<CodeLabel> all = new ArrayList<>();

        while (i < highlights.size()) {
            HighLight current = highlights.get(i);
            String highlight = code.substring(current.start(), current.end());
            all.add(new CodeLabel(highlight, current.color(), current.type()));

            if (i == highlights.size() - 1) break;

            HighLight next = highlights.get(i + 1);

            if (current.end() + 1 != next.start()) {
                if (current.end() + 1 > next.start()) {
                    i++;
                    continue;
                }
                String noneHighlight = code.substring(current.end(), next.start());
                all.add(new CodeLabel(noneHighlight, -1, "tab"));
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
                    highlights.add(new HighLight(current.getStartByte(), current.getEndByte(), color, type));
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
