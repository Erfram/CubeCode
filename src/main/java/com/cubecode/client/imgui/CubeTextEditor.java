package com.cubecode.client.imgui;

import com.cubecode.utils.Vec2i;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;

import java.util.Map;

public class CubeTextEditor {
    TextEditor textEditor = new TextEditor();

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

    public native int getTabSize(); /*
        return TEXT_EDITOR->GetTabSize();
    */

    public native void insertText(String value); /*
        TEXT_EDITOR->InsertText(value);
    */

    public native void moveUp(int amount, boolean select); /*
        TEXT_EDITOR->MoveUp(amount, select);
    */

    public native void moveDown(int amount, boolean select); /*
        TEXT_EDITOR->MoveDown(amount, select);
    */

    public native void moveLeft(int amount, boolean select, boolean wordMode); /*
        TEXT_EDITOR->MoveLeft(amount, select, wordMode);
    */

    public native void moveRight(int amount, boolean select, boolean wordMode); /*
        TEXT_EDITOR->MoveRight(amount, select, wordMode);
    */

    public native void moveTop(boolean select); /*
        TEXT_EDITOR->MoveTop(select);
    */

    public native void moveBottom(boolean select); /*
        TEXT_EDITOR->MoveBottom(select);
    */

    public native void moveHome(boolean select); /*
        TEXT_EDITOR->MoveHome(select);
    */

    public native void moveEnd(boolean select); /*
        TEXT_EDITOR->MoveEnd(select);
    */

    public native void setSelectionStart(int line, int column); /*
        TEXT_EDITOR->SetSelectionStart({ line, column });
    */

    public native void setSelectionEnd(int line, int column); /*
        TEXT_EDITOR->SetSelectionEnd({ line, column });
    */

    public native void setSelection(int lineStart, int columnStart, int lineEnd, int columnEnd, int selectionMode); /*
        TEXT_EDITOR->SetSelection({ lineStart, columnStart }, { lineEnd, columnEnd },
            static_cast<TextEditor::SelectionMode>(selectionMode));
    */

    public native void selectWordUnderCursor(); /*
        TEXT_EDITOR->SelectWordUnderCursor();
    */

    public native void selectAll(); /*
        TEXT_EDITOR->SelectAll();
    */

    public native boolean hasSelection(); /*
        return TEXT_EDITOR->HasSelection();
    */

    public native void copy(); /*
        TEXT_EDITOR->Copy();
    */

    public native void cut(); /*
        TEXT_EDITOR->Cut();
    */

    public native void paste(); /*
        TEXT_EDITOR->Paste();
    */

    public native void delete(); /*
        TEXT_EDITOR->Delete();
    */

    public native boolean canUndo(); /*
        return TEXT_EDITOR->CanUndo();
    */

    public native boolean canRedo(); /*
        return TEXT_EDITOR->CanRedo();
    */

    public native void undo(int steps); /*
        TEXT_EDITOR->Undo(steps);
    */

    public native void redo(int steps); /*
        TEXT_EDITOR->Redo(steps);
    */

    public native int[] getDarkPalette(); /*
        const auto& palette = TEXT_EDITOR->GetDarkPalette();

        jintArray res = env->NewIntArray(palette.size());

        jint arr[palette.size()];
        for (int i = 0; i < palette.size(); i++) {
            arr[i] = palette[i];
        }

        env->SetIntArrayRegion(res, 0, palette.size(), arr);

        return res;
    */

    public native int[] getLightPalette(); /*
        const auto& palette = TEXT_EDITOR->GetLightPalette();

        jintArray res = env->NewIntArray(palette.size());

        jint arr[palette.size()];
        for (int i = 0; i < palette.size(); i++) {
            arr[i] = palette[i];
        }

        env->SetIntArrayRegion(res, 0, palette.size(), arr);

        return res;
    */

    public native int[] getRetroBluePalette(); /*
        const auto& palette = TEXT_EDITOR->GetRetroBluePalette();

        jintArray res = env->NewIntArray(palette.size());

        jint arr[palette.size()];
        for (int i = 0; i < palette.size(); i++) {
            arr[i] = palette[i];
        }

        env->SetIntArrayRegion(res, 0, palette.size(), arr);

        return res;
    */
}
