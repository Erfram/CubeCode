package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.treesitter.CodeLabel;
import com.cubecode.client.treesitter.TreeSitterParser;
import imgui.ImDrawList;
import imgui.ImFont;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CodeEditorView extends View {

    private final Timer timer = new Timer();
    private final ImBoolean CLOSE = new ImBoolean(true);
    private ArrayList<String> lines = new ArrayList<>();
    private HashMap<Integer, ArrayList<CodeLabel>> linesHighlights;
    private TimerTask currentTask = null;
    private ImVec2 cursor;

    public CodeEditorView() {
        lines.add("var Player = Player.getPlayer();");
        lines.add("");
        lines.add("// it`s example commented code");
        lines.add("");
        lines.add("/* it`s example function */");
        lines.add("function example() {");
        lines.add("    var isSigma = true;");
        lines.add("    ");
        lines.add("    if (isSigma) {");
        lines.add("        \"I am sigma!\"");
        lines.add("        Player.send(\"1\");");
        lines.add("        Player.send(\"I am sigma!\");");
        lines.add("    } else {");
        lines.add("        Player.send(\"I am not sigma :(\");");
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

    public CodeEditorView(ArrayList<String> lines) {
        this.lines = lines;
    }

    @Override
    public String getName() {
        return String.format("Cube IDEA##%s", uniqueID);
    }

    @Override
    public void init() {
        int width = 500;
        int height = 400;
        try {
            int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
            int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

            ImGui.setNextWindowPos(width / 2f - windowWidth / 2f, height / 2f - windowHeight / 2f);
        } catch (Exception ignored) {
        }
        ImGui.setNextWindowSize(width, height);
    }

    @Override
    public void render() {
        if (ImGui.begin(getName(), CLOSE)) {
            if (!CLOSE.get()) {
                ImGuiLoader.removeView(this);
            }
            drawCode();
        }
        ImGui.end();
    }

    @Override
    public void handleMouseReleased(double mouseX, double mouseY, int button) {
        // TODO
    }

    @Override
    public void handleKeyReleased(int keyCode, int scanCode, int modifiers) {
        // TODO
    }

    public void drawCode() {
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

        int row = 0;
        for (String line : lines) {
            ArrayList<CodeLabel> lineTokens = linesHighlights.get(row);
            if (lineTokens != null) {
                if (lineTokens.size() == 1) {
                    drawList.addText(x + offsetX, y + offsetY, lineTokens.get(0).color(), line);
                } else {
                    float tokenX = x + offsetX;
                    int currentPos = 0;

                    for (CodeLabel token : lineTokens) {
                        String tokenText = token.text();

                        int tokenStart = line.indexOf(tokenText, currentPos);
                        int spacesCount = tokenStart - currentPos;

                        if (spacesCount > 0) {
                            String spaces = line.substring(currentPos, tokenStart);
                            drawList.addText(tokenX, y + offsetY, -1, spaces);
                            tokenX += ImGui.calcTextSize(spaces).x;
                        }

                        drawList.addText(tokenX, y + offsetY, token.color(), tokenText);

                        tokenX += ImGui.calcTextSize(tokenText).x;
                        currentPos = tokenStart + tokenText.length();
                    }
                }
            }
            offsetY += fontSize;
            row++;
        }
        if (cursor != null) {
            drawList.addText(cursor.x, cursor.y, -1, "|");
        }
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
        /**
         * debounce timeout
         */
        timer.schedule(currentTask, 175);
    }

    private void handleEditorChanged() {
        linesHighlights = TreeSitterParser.parse(lines);
    }

}