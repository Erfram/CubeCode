package com.cubecode.client.imgui;

import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiFocusedFlags;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class CubeTextEditor2 {
    private final TextEditor basic = new TextEditor();

    private String title;

    private boolean isFocused = false;

    private CursorPosition selectionStart = new CursorPosition(-1, -1);
    private CursorPosition selectionEnd = new CursorPosition(-1, -1);

    public CubeTextEditor2(String title) {
        this.title = title;
        this.basic.setReadOnly(true);
    }

    public void render() {
        this.basic.render(this.title);

        this.isFocused = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);

        updateSelectionBounds();

        manageKeyboard();
    }

    private void manageKeyboard() {
        if (this.isFocused) {
            GLFW.glfwSetCharModsCallback(MinecraftClient.getInstance().getWindow().getHandle(), (window, unicode, mods) -> {
                pressKey((char) unicode);
            });

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)) {
                pressBackspace();
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
                pressEnter();
            }
        }
    }

    private void pressKey(char key) {
        CursorPosition cursorPos = this.getCursorPosition();

        String textLine = this.getTextLine(cursorPos.line);

        StringBuilder stringBuilder = new StringBuilder(textLine);

        stringBuilder.insert(cursorPos.column, key);

        this.setTextLine(cursorPos.line, stringBuilder.toString());
        this.basic.setCursorPosition(cursorPos.line, cursorPos.column+1);
    }

    private void pressBackspace() {
        CursorPosition cursorPos = this.getCursorPosition();
        if (cursorPos.column > 0) {
            String textLine = this.getTextLine(cursorPos.line);

            StringBuilder stringBuilder = new StringBuilder(textLine);

            stringBuilder.deleteCharAt(cursorPos.column-1);

            this.setTextLine(cursorPos.line, stringBuilder.toString());
            this.basic.setCursorPosition(cursorPos.line, cursorPos.column-1);
        } else if (cursorPos.line > 0) {
            List<String> textLines = new ArrayList<>(List.of(this.basic.getTextLines()));

            textLines.remove(cursorPos.line);

            this.basic.setTextLines(textLines.toArray(new String[0]));
            this.basic.setCursorPosition(cursorPos.line-1, this.getTextLine(cursorPos.line-1).length());
        }
    }

    private void pressEnter() {
        CursorPosition cursorPos = this.getCursorPosition();

        List<String> textLines = new ArrayList<>(List.of(this.basic.getTextLines()));

        textLines.add(cursorPos.line+1, "");

        this.basic.setTextLines(textLines.toArray(new String[0]));
        this.basic.setCursorPosition(cursorPos.line+1, 0);
    }

    public CubeTextEditor2.CursorPosition getCursorPosition() {
        return new CubeTextEditor2.CursorPosition(
                this.basic.getCursorPositionLine(),
                this.basic.getCursorPositionColumn()
        );
    }

    public void updateSelectionBounds() {
        CursorPosition cursorPos = this.getCursorPosition();

        this.selectionStart = new CursorPosition(0, 0);
        this.selectionEnd = new CursorPosition(0, 0);

        String selectedText = this.basic.getSelectedText();
        if (selectedText.isEmpty() || !this.basic.hasSelection()) {
            return;
        }

        int savedLine = cursorPos.line;
        int savedColumn = cursorPos.column;

        this.basic.setCursorPosition(0, 0);

        String fullText = this.basic.getText();
        int globalPos = fullText.indexOf(selectedText);

        if (globalPos == -1) {
            this.basic.setCursorPosition(savedLine, savedColumn);
            return;
        }

        int currentPos = 0;
        int startLine = 0, startColumn = 0;
        String[] lines = fullText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (currentPos + line.length() + 1 > globalPos) {
                startLine = i;
                startColumn = globalPos - currentPos;
                break;
            }
            currentPos += line.length() + 1;
        }

        int endGlobalPos = globalPos + selectedText.length();
        int endLine = startLine;
        int endColumn = startColumn + selectedText.length();

        String selectedTextUntilEnd = fullText.substring(globalPos, endGlobalPos);
        if (selectedTextUntilEnd.contains("\n")) {
            String[] selectedLines = selectedTextUntilEnd.split("\n");
            if (selectedLines.length > 0) {
                endLine = startLine + selectedLines.length - 1;
                endColumn = selectedLines[selectedLines.length - 1].length();
            } else {
                endColumn = 0;
            }
        }


        this.basic.setCursorPosition(savedLine, savedColumn);

        this.selectionStart = new CursorPosition(startLine, startColumn);
        this.selectionEnd = new CursorPosition(endLine, endColumn);
    }

    public void setCursorPosition(CursorPosition cursorPosition) {
        this.basic.setCursorPosition(cursorPosition.line, cursorPosition.column);
    }

    private String getTextLine(int line) {
        return this.basic.getTextLines()[line];
    }

    private void setTextLine(int line, String text) {
        String[] textLines = this.basic.getTextLines();

        textLines[line] = text;

        this.basic.setTextLines(textLines);
    }

    public record CursorPosition(int line, int column) { }
}
