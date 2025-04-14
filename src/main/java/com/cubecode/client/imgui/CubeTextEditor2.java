package com.cubecode.client.imgui;

import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CubeTextEditor2 {
    private final TextEditor basic = new TextEditor();

    private String title;

    public CubeTextEditor2(String title) {
        this.title = title;
        this.basic.setReadOnly(true);
    }

    public void render() {
        this.basic.render(this.title);

        manageKeybinding();
    }

    private void manageKeybinding() {
        if (ImGui.isKeyPressed(GLFW.GLFW_KEY_F)) {
            String[] textLines = this.basic.getTextLines();
            int line = this.basic.getCursorPositionLine();
            int column = this.basic.getCursorPositionColumn();

            String textLine = textLines[line];

            StringBuilder stringBuilder = new StringBuilder(textLine);

            stringBuilder.insert(column, "f");

            textLines[line] = stringBuilder.toString();

            this.basic.setTextLines(textLines);
            this.basic.setCursorPosition(line, column+1);
        } else if (ImGui.isKeyPressed(GLFW.GLFW_KEY_B)) {
            String[] textLines = this.basic.getTextLines();
            int line = this.basic.getCursorPositionLine();
            int column = this.basic.getCursorPositionColumn();

            String textLine = textLines[line];

            StringBuilder stringBuilder = new StringBuilder(textLine);

            stringBuilder.insert(column, "b");

            textLines[line] = stringBuilder.toString();

            this.basic.setTextLines(textLines);
            this.basic.setCursorPosition(line, column+1);
        } else if (ImGui.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)) {
            String[] textLines = this.basic.getTextLines();
            int line = this.basic.getCursorPositionLine();
            int column = this.basic.getCursorPositionColumn();

            String textLine = textLines[line];

            StringBuilder stringBuilder = new StringBuilder(textLine);

            if (column > 0) {
                stringBuilder.deleteCharAt(column-1);

                textLines[line] = stringBuilder.toString();

                this.basic.setTextLines(textLines);
                this.basic.setCursorPosition(line, column-1);
            }
        }
    }
}
