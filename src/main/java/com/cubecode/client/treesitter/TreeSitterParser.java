package com.cubecode.client.treesitter;

import com.cubecode.client.imgui.basic.Theme;
import com.cubecode.client.imgui.themes.CodeTheme;
import com.cubecode.client.views.CodeEditorView;
import com.cubecode.utils.ColorUtils;
import org.jetbrains.annotations.NotNull;
import org.treesitter.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TreeSitterParser {

    public static HashMap<Integer, ArrayList<CodeLabel>> parse(ArrayList<String> lines, Theme theme) {
        StringBuilder code = new StringBuilder();
        for (String line : lines) {
            code.append(line).append("\n");
        }
        return parse(code.toString(), theme);
    }

    public static HashMap<Integer, ArrayList<CodeLabel>> parse(String code, Theme theme) {
        TSParser parser = new TSParser();
        TreeSitterJavascript js = new TreeSitterJavascript();
        parser.setLanguage(js);

        TSTree tree = parser.parseString(null, code);

        ArrayList<HighLight> highlights = getHighLights(tree, theme);
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
    private static ArrayList<HighLight> getHighLights(TSTree tree, Theme theme) {
        TSNode rootNode = tree.getRootNode();
        TSTreeCursor cursor = new TSTreeCursor(rootNode);
        cursor.gotoFirstChild();

        ArrayList<HighLight> highlights = new ArrayList<>();
        boolean reached_root = false;

        while (!reached_root) {
            TSNode current = cursor.currentNode();
            if (!current.isNull()) {
                HighLight highlight = theme.getCodeHighlight(current);

                if (highlight != null) {
                    highlights.add(highlight);
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
