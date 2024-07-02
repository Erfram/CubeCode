package com.cubecode.client.editor;

import com.cubecode.client.imgui.basic.View;
import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;
import org.treesitter.TSTree;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CodeEditor extends View {

    private final Timer timer = new Timer();
    /**
     * debounce timeout
     */
    private final long delay = 175;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<CodeLabel> linesHighlights;
    private TSTree beforeTree;
    private TimerTask currentTask = null;

    public CodeEditor() {
        lines.add("var Player = Player.getPlayer();");
        lines.add("");
        lines.add("// it`s example commented code");
        lines.add("");
        lines.add("/* it`s example function */");
        lines.add("function example() {");
        lines.add("    var isSigma = true;");
        lines.add("    ");
        lines.add("    if (isSigma) {");
        lines.add("        Player.send(\"I am sigma!\");");
        lines.add("    } else {");
        lines.add("        Player.send('I am not sigma :(');");
        lines.add("    }");
        lines.add("    ");
        lines.add("    this.ping();");
        lines.add("    this.number(123);");
        lines.add("    ");
        lines.add("    //TODO aaaaa");
        lines.add("}");
        lines.add("");
        lines.add("/* it`s ping function */");
        lines.add("function ping() {");
        lines.add("    this.lox(); //TODO PING");
        lines.add("}");
        lines.add("");
        lines.add("/* it`s number function */");
        lines.add("function number(num) {");
        lines.add("    //TODO NUM");
        lines.add("}");
        handleEditorChanged();
    }

    public CodeEditor(ArrayList<String> lines) {
        this.lines = lines;
    }

    public void render() {
        if (linesHighlights == null) return;

        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 cursorPos = ImGui.getCursorPos();
        ImDrawList drawList = ImGui.getWindowDrawList();

        float x = windowPos.x + cursorPos.x;
        float y = windowPos.y + cursorPos.y;

        float offsetX = 0;
        float offsetY = 0;
        ImFont font = ImGui.getFont();
        float fontSize = font.getFontSize();

        String lastWord = "";
        int row = 0;
        for (CodeLabel linesHighlight : linesHighlights) {
            int currentColum = linesHighlight.point().getColumn();
            int currentRow = linesHighlight.point().getRow();
            String text = linesHighlight.text();
            boolean flag = false;
            if (row != currentRow) {
                offsetX = 0;
                row = currentRow;
                offsetY += fontSize;
                flag = true;
            }
            if (linesHighlight.type().equals("if_statement") || linesHighlight.type().equals("else_clause")) continue;
            if (flag) {
                boolean contains = lastWord.contains("\n");
                String[] split = lastWord.split("\n");
                if (contains) {
                    if (split.length >= 2) {
                        offsetY += fontSize;
                        offsetX = font.calcTextSizeA(fontSize, Float.MAX_VALUE, -1.f, split[split.length - 1]).x;
                    } else {
                        offsetX += font.calcTextSizeA(fontSize, Float.MAX_VALUE, -1.f, lastWord).x;
                    }
                }
            }
            drawList.addText(x + offsetX, y + offsetY, linesHighlight.color(), text);
            offsetX += font.calcTextSizeA(fontSize, Float.MAX_VALUE, -1.f, text).x;
            lastWord = text;
        }

        //for (String line : lines) {
        //    float currentOffsetX = 0;
        //    String[] tokens = line.split("(?<= )|(?= )");
        //    for (String token : tokens) {
        //        String[] characters = token.split("(?<=\\s)|(?=\\s)|(?=[.,()])|(?<=[.,()])");
        //        for (String character : characters) {
        //            int color = TreeSitterParser.defaultCode;
        //            for (CodeLabel highlight : linesHighlights) {
        //                if (highlight.text().contains(character)) {
        //                    color = highlight.color();
        //                    break;
        //                }
        //            }
        //            drawList.addText(x + currentOffsetX, y + offsetY, color, character);
        //            currentOffsetX += font.calcTextSizeA(fontSize, Float.MAX_VALUE, -1.f, character).x;
        //        }
        //    }
        //    offsetY += fontSize;
        //}
    }

    private void debounce() {
        if (currentTask != null) {
            currentTask.cancel();
        }

        currentTask = new TimerTask() {
            @Override
            public void run() {
                handleEditorChanged();
            }
        };
        timer.schedule(currentTask, delay);
    }

    private void handleEditorChanged() {
        linesHighlights = TreeSitterParser.parse(lines);
    }

}
