package com.cubecode.client.imgui;

import com.cubecode.utils.Vec2i;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;

import java.util.Map;

public class CubeTextEditor {
    public TextEditor textEditor = new TextEditor();

    public Vec2i getCursorPosition() {
        return new Vec2i(
            this.textEditor.getCursorPositionColumn(),
            this.textEditor.getCursorPositionLine()
        );
    }

    public void setText(String text) {
        this.textEditor.setText(text);
    }

    public String getText() {
        return this.textEditor.getText();
    }

    public String getSelectedText() {
        return this.textEditor.getSelectedText();
    }

    public void setLanguageDefinition(TextEditorLanguageDefinition definition) {
        this.textEditor.setLanguageDefinition(definition);
    }

    public int[] getPalette() {
        return this.textEditor.getPalette();
    }

    public void setPalette(int[] palette) {
        this.textEditor.setPalette(palette);
    }

    public void setErrorMarkers(Map<Integer, String> errorMarkers) {
        this.textEditor.setErrorMarkers(errorMarkers);
    }

    public void setBreakpoints(int[] breakpoints) {
        this.textEditor.setBreakpoints(breakpoints);
    }
    public void render(String title) {
        this.textEditor.render(title);
    }

    public void setTextLines(String[] lines) {
        this.textEditor.setTextLines(lines);
    }

    public String[] getTextLines() {
        return this.textEditor.getTextLines();
    }

    public String getCurrentLineText() {
        return this.textEditor.getCurrentLineText();
    }

    public int getTotalLines() {
        return textEditor.getTotalLines();
    }

    public boolean isOverwrite() {
        return this.textEditor.isOverwrite();
    }

    public void setReadOnly(boolean readOnly) {
        this.textEditor.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return this.textEditor.isReadOnly();
    }

    public boolean isTextChanged() {
        return this.textEditor.isTextChanged();
    }

    public boolean isCursorPositionChanged() {
        return this.textEditor.isCursorPositionChanged();
    }

    public boolean isColorizerEnabled() {
        return this.textEditor.isColorizerEnabled();
    }

    public void setColorizerEnable(boolean colorizer) {
        this.textEditor.setColorizerEnable(colorizer);
    }

    public void setCursorPosition(int line, int column) {
        this.textEditor.setCursorPosition(line, column);
    }

    public void setHandleMouseInputs(boolean handleMouseInputs) {
        this.textEditor.setHandleMouseInputs(handleMouseInputs);
    }

    public boolean isHandleMouseInputsEnabled() {
        return this.textEditor.isHandleMouseInputsEnabled();
    }

    public void setHandleKeyboardInputs(boolean handleKeyboardInputs) {
        this.textEditor.setHandleMouseInputs(handleKeyboardInputs);
    }



    public boolean isHandleKeyboardInputsEnabled() {
        return this.textEditor.isHandleKeyboardInputsEnabled();
    }

    public void setImGuiChildIgnored(boolean imGuiChildIgnored) {
        this.textEditor.setImGuiChildIgnored(imGuiChildIgnored);
    }

    public boolean isImGuiChildIgnored() {
        return this.textEditor.isImGuiChildIgnored();
    }

    public void setShowWhitespaces(boolean showWhitespaces) {
        this.textEditor.setShowWhitespaces(showWhitespaces);
    }

    public boolean isShowingWhitespaces() {
        return this.textEditor.isShowingWhitespaces();
    }

    public void setTabSize(int tabSize) {
        this.textEditor.setTabSize(tabSize);
    }

    public int getTabSize() {
        return this.textEditor.getTabSize();
    }

    public void insertText(String text) {
        this.textEditor.insertText(text);
    }

    public void moveUp(int amount, boolean select) {
        this.textEditor.moveUp(amount, select);
    }

    public void moveDown(int amount, boolean select) {
        this.textEditor.moveDown(amount, select);
    }

    public void moveLeft(int amount, boolean select, boolean wordMode) {
        this.textEditor.moveLeft(amount, select, wordMode);
    }

    public void moveRight(int amount, boolean select, boolean wordMode) {
        this.textEditor.moveRight(amount, select, wordMode);
    }

    public void moveTop(boolean select) {
        this.textEditor.moveTop(select);
    }

    public void moveBottom(boolean select) {
        this.textEditor.moveBottom(select);
    }

    public void moveHome(boolean select) {
        this.textEditor.moveHome(select);
    }

    public void moveEnd(boolean select) {
        this.textEditor.moveEnd(select);
    }

    public void setSelectionStart(int line, int column) {
        this.textEditor.setSelectionStart(line, column);
    }

    public void setSelectionEnd(int line, int column) {
        this.textEditor.setSelectionEnd(line, column);
    }

    public void setSelection(int lineStart, int columnStart, int lineEnd, int columnEnd, int selectionMode) {
        this.textEditor.setSelection(lineStart, columnStart, lineEnd, columnEnd, selectionMode);
    }

    public void selectWordUnderCursor() {
        this.textEditor.selectWordUnderCursor();
    }

    public void selectAll() {
        this.textEditor.selectAll();
    }

    public boolean hasSelection() {
        return this.textEditor.hasSelection();
    }

    public void copy() {
        this.textEditor.copy();
    }

    public void cut() {
        this.textEditor.cut();
    }

    public void paste() {
        this.textEditor.paste();
    }

    public  void delete() {
        this.textEditor.delete();
    }

    public boolean canUndo() {
        return this.textEditor.canUndo();
    }

    public boolean canRedo() {
        return this.textEditor.canRedo();
    }

    public void undo(int steps) {
        this.textEditor.undo(steps);
    }

    public void redo(int steps) {
        this.textEditor.redo(steps);
    }

    public int[] getDarkPalette() {
        return this.textEditor.getDarkPalette();
    }

    public int[] getLightPalette() {
        return this.textEditor.getLightPalette();
    }

    public int[] getRetroBluePalette() {
        return this.textEditor.getRetroBluePalette();
    }
}
