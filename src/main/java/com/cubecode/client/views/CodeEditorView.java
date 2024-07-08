package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.treesitter.CodeLabel;
import com.cubecode.client.treesitter.TreeSitterParser;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

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

    @Nullable
    private ImVec2 cursorText;
    @Nullable
    private ImVec2 lastCursorText;
    @Nullable
    private ImVec2 cursor;
    @Nullable
    private ImVec2 lastCursor;

    private ImVec2 mCharAdvance;

    private int lineNumberOffset;
    private boolean mOverwrite;

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
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("");
        handleEditorChanged();
    }

    public CodeEditorView(ArrayList<String> lines) {
        this.lines = lines;
        handleEditorChanged();
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

            ImGui.setNextWindowPos(windowWidth / 2f - width / 2f, windowHeight / 2f - height / 2f);
        } catch (Exception ignored) {
        }
        ImGui.setNextWindowSize(width, height);
    }


    @Override
    public void render() {
        if (ImGui.begin(getName(), CLOSE, ImGuiWindowFlags.NoScrollbar)) {
            if (!CLOSE.get()) {
                ImGuiLoader.removeView(this);
            }
            ImVec2 windowSize = ImGui.getWindowSize();

            ImGui.pushStyleColor(ImGuiCol.ChildBg, TreeSitterParser.background);
            ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
            ImGui.beginChild(getName(), windowSize.x - 15, windowSize.y - 35, true, ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.AlwaysHorizontalScrollbar | ImGuiWindowFlags.NoMove);
            drawCode();
            handleKeyboardInputs();
            handleCursor();
            ImGui.endChild();
            ImGui.popStyleVar();
            ImGui.popStyleColor();
        }
        ImGui.end();
    }

    @Override
    public void handleKeyReleased(int keyCode, int scanCode, int modifiers) {
        try {
            char enteredChar = InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString().charAt(0);
            boolean isShiftPressed = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
            enterCharacter(enteredChar, isShiftPressed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMouseReleased(double mouseX, double mouseY, int button) {
        if (lastCursor != null) {
            cursor = lastCursor;
            cursorText = lastCursorText;
        } else {
            cursor = null;
            cursorText = null;
        }
    }

    public void handleKeyboardInputs() {
        ImGuiIO io = ImGui.getIO();
        boolean shift = io.getKeyShift();
        boolean ctrl = io.getConfigMacOSXBehaviors() ? io.getKeySuper() : io.getKeyCtrl();
        boolean alt = io.getConfigMacOSXBehaviors() ? io.getKeyCtrl() : io.getKeyAlt();

        if (ImGui.isWindowFocused()) {
            if (ImGui.isWindowHovered())
                ImGui.setMouseCursor(ImGuiMouseCursor.TextInput);

            io.setWantCaptureKeyboard(true);
            io.setWantTextInput(true);

            if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Z))) {
                undo();
            } else if (!ctrl && !shift && alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Backspace))) {
                undo();
            } else if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Y))) {
                redo();
            } else if (!ctrl && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.UpArrow))) {
                moveUp(1, shift);
            } else if (!ctrl && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.DownArrow))) {
                moveDown(1, shift);
            } else if (!alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.LeftArrow))) {
                moveLeft(1, shift, ctrl);
            } else if (!alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.RightArrow))) {
                moveRight(1, shift, ctrl);
            } else if (!alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.PageUp))) {
                moveUp(getPageSize() - 4, shift);
            } else if (!alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.PageDown))) {
                moveDown(getPageSize() - 4, shift);
            } else if (!alt && ctrl && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Home))) {
                moveTop(shift);
            } else if (ctrl && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.End))) {
                moveBottom(shift);
            } else if (!ctrl && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Home))) {
                moveHome(shift);
            } else if (!ctrl && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.End))) {
                moveEnd(shift);
            } else if (!ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Delete))) {
                delete();
            } else if (!ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Backspace))) {
                backspace();
            } else if (!ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Insert))) {
                mOverwrite = !mOverwrite;
            } else if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Insert))) {
                copy();
            } else if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.C))) {
                copy();
            } else if (!ctrl && shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Insert))) {
                paste();
            } else if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.V))) {
                paste();
            } else if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.X))) {
                cut();
            } else if (!ctrl && shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Delete))) {
                cut();
            } else if (ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.A))) {
                selectAll();
            } else if (!ctrl && !shift && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Enter))) {
                enterCharacter('\n', false);
            } else if (!ctrl && !alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Tab))) {
                enterCharacter('\t', shift);
            }
        }
    }

    private void handleCursor() {
        ImVec2 cursorPosInWindow = getCursorPosInWindow();

        float x = cursorPosInWindow.x;
        float y = cursorPosInWindow.y;

        int row = (int) (y / getFontSize());

        if (row < 0 || row >= lines.size()) {
            return;
        }

        String line = lines.get(row);
        if (line == null) {
            return;
        }

        if (line.isEmpty()) {
            lastCursor = new ImVec2(0, row * getFontSize());
            lastCursorText = new ImVec2(0, row);
        }

        if (x < lineNumberOffset) {
            lastCursor = null;
            return;
        } else {
            x -= lineNumberOffset;
        }

        float renderSize = ImGui.calcTextSize(line).x;
        if (!(x <= renderSize)) {
            lastCursor = new ImVec2(renderSize, row * getFontSize());
            lastCursorText = new ImVec2(line.length(), row);
            return;
        }

        int pos = line.length() - 1;
        for (int i = 0; i < line.length(); i++) {
            String substring = line.substring(0, i);
            float size = ImGui.calcTextSize(substring).x;
            if (size > x) {
                pos = i;
                break;
            }
        }

        try {
            String substring = line.substring(0, pos);
            lastCursor = new ImVec2(ImGui.calcTextSize(substring).x, row * getFontSize());
            lastCursorText = new ImVec2(pos, row);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawCode() {
        float fontSize = getFontSize();
        mCharAdvance = new ImVec2(fontSize, ImGui.getTextLineHeightWithSpacing() * 10);
        int numberOfDigits = String.valueOf(lines.size()).length();
        lineNumberOffset = numberOfDigits * 15;

        ImVec2 windowPos = getWindowPos();

        float x = windowPos.x;
        float y = windowPos.y;
        float offsetX = lineNumberOffset;
        float offsetY = 0;
        int row = 0;

        ImDrawList drawList = ImGui.getWindowDrawList();

        for (String line : lines) {
            if (String.valueOf(row).length() != numberOfDigits) {
                drawList.addText(x, y + offsetY, TreeSitterParser.currentLineEdge, " ".repeat(numberOfDigits + 1) + row);
            } else {
                drawList.addText(x, y + offsetY, TreeSitterParser.currentLineEdge, String.valueOf(row));
            }
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
            drawList.addText(x + offsetX + cursor.x, y + cursor.y, TreeSitterParser.cursor, "|");
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
        timer.schedule(currentTask, 175);
    }

    private void handleEditorChanged() {
        linesHighlights = TreeSitterParser.parse(lines);
    }

    private int getPageSize() {
        return (int) Math.floor(ImGui.getWindowHeight() - 20.0f / mCharAdvance.y);
    }

    private void moveEnd(boolean shift) {

    }

    private void moveHome(boolean shift) {

    }

    private void moveBottom(boolean shift) {

    }

    private void enterCharacter(char c, boolean b) {
        if (cursorText != null) {
            try {
                String string = lines.get((int) cursorText.y);
                StringBuilder builder = new StringBuilder(string);
                builder.insert((int) cursorText.x, c);
                lines.set((int) cursorText.y, builder.toString());
                cursorText.x += 1;
                handleEditorChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void selectAll() {

    }

    private void cut() {

    }

    private void delete() {

    }

    private void backspace() {
    }

    private void paste() {

    }

    private void copy() {

    }

    private void moveTop(boolean shift) {

    }

    private void moveRight(int i, boolean shift, boolean ctrl) {

    }

    private void moveLeft(int i, boolean shift, boolean ctrl) {
    }

    private void moveDown(int i, boolean shift) {

    }

    private void moveUp(int i, boolean shift) {

    }

    private void redo() {

    }

    private void undo() {

    }

    private ImVec2 getCursorPosInWindow() {
        ImVec2 windowPos = getWindowPos();
        ImVec2 mousePos = ImGui.getMousePos();
        float x = mousePos.x - windowPos.x;
        float y = mousePos.y - windowPos.y;
        return new ImVec2(x, y);
    }

    private ImVec2 getWindowPos() {
        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 cursorPos = ImGui.getCursorPos();
        float x = windowPos.x + cursorPos.x;
        float y = windowPos.y + cursorPos.y;
        return new ImVec2(x, y);
    }

    private float getFontSize() {
        return ImGui.getFont().getFontSize();
    }

}