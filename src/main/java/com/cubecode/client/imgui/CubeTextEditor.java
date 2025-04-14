package com.cubecode.client.imgui;

import com.cubecode.utils.Icons;
import com.cubecode.utils.StringUtils;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;
import imgui.flag.*;
import imgui.type.ImString;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CubeTextEditor {
    private final TextEditor basic = new TextEditor();
    private final String title;
    private final float startWidth;
    private final float startHeight;

    public float x = 0;
    public float y = 0;
    public float width = 0;
    public float height = 0;

    private int windowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.HorizontalScrollbar;

    private boolean showBackground = true;
    private int colorBackground = ImGui.getColorU32(0.15f, 0.15f, 0.15f, 1f);

    private boolean isDebug = false;
    private boolean isFinder = true;
    private boolean isReplacement = true;

    private boolean isFocused = false;

    private Runnable contextMenuRender;
    private final LinkedHashMap<String, ContextItem> contextItems = new LinkedHashMap<>();

    private boolean isRenderContextMenu = false;
    private boolean isRenderReplacement = false;
    private boolean isRenderFinder = false;


    public CubeTextEditor(String title) {
        this(title, 0, 0);
    }

    public CubeTextEditor(String title, float startWidth, float startHeight) {
        this.title = title;
        this.startWidth = startWidth;
        this.startHeight = startHeight;

        this.basic.setPalette(this.basic.getDarkPalette());
        this.basic.setColorizerEnable(true);
        this.basic.setImGuiChildIgnored(true);
    }

    public void render() {
        if (this.isFinder && (this.isRenderReplacement || this.isRenderFinder)) {
            renderFinder();
        }
        this.x = ImGui.getCursorScreenPosX();
        this.y = ImGui.getCursorScreenPosY();
        this.width = ImGui.getWindowWidth();
        this.height = ImGui.getWindowHeight();

        if (ImGui.beginChild(this.title, this.startWidth, this.startHeight, false, this.windowFlags)) {
            if (this.showBackground) {
                renderBackground();
            }

            boolean hadSelectionBeforeRender = this.basic.hasSelection();

            this.basic.render(this.title);

            handleAutoscrollOnSelectionDrag(hadSelectionBeforeRender);

            this.isFocused = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);

            highlightSearchMatches();

            if (this.isDebug) {
                renderDebug();
            }
        }
        ImGui.endChild();

        manageMouse();
        manageKeyboard();
        renderContextMenu();
    }

    ImString search = new ImString(150);
    int numberSearchWord = 0;
    boolean isFinderRegex = false;

    private void renderFinder() {
        float height = this.isRenderReplacement ? ImGui.calcTextSize("A").y*3 + ImGui.getStyle().getItemSpacingY() * 2 : ImGui.calcTextSize("A").y*2;

        CubeImGui.beginChild("replacementAndFinder", 0, height, true, ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoScrollbar, () -> {
            float initialPosY = ImGui.getCursorPosY();
            ImGui.image(Icons.SEARCH.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());

            ImGui.sameLine();

            ImGui.pushItemWidth(ImGui.getFontSize() + 150);
            ImGui.inputTextWithHint("##Search", "Search", this.search);
            ImGui.popItemWidth();

            ImGui.sameLine();


            if (ImGui.radioButton("Regex", this.isFinderRegex)) {
                this.isFinderRegex = !this.isFinderRegex;
            }

            ImGui.sameLine();

            ImGui.text("|");

            ImGui.sameLine();
            ImGui.text(this.currentMatchIndex + 1 + "/" + this.numberSearchWord);

            ImGui.sameLine();

            ImGui.text("|");

            ImGui.sameLine();

            if (ImGui.button("\ue5d8", ImGui.getFontSize(), ImGui.getFontSize())) {
                this.goToPreviousMatch();
            }

            ImGui.sameLine();

            if (ImGui.button("\ue5db", ImGui.getFontSize(), ImGui.getFontSize())) {
                this.goToNextMatch();
            }

            if (this.isReplacement && this.isRenderReplacement) {
                this.renderReplacement();
            }


            float rightEdge = ImGui.getWindowWidth() - ImGui.getFontSize() - ImGui.getStyle().getWindowPaddingX() - ImGui.getStyle().getItemSpacingX();

            ImGui.setCursorPosX(rightEdge);
            ImGui.setCursorPosY(initialPosY);

            CubeImGui.imageButton(Icons.MINUS, "Close", ImGui.getFontSize(), ImGui.getFontSize(), () -> {
                this.isRenderFinder = false;
                this.isRenderReplacement = false;
                this.search.clear();
            });
        });
    }

    ImString replace = new ImString(150);

    private void renderReplacement() {
        ImGui.image(Icons.SEARCH.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());
        ImGui.sameLine();

        ImGui.pushItemWidth(ImGui.getFontSize() + 150);
        ImGui.inputTextWithHint("##Replace", "Replace", this.replace);
        ImGui.popItemWidth();

        ImGui.sameLine();

        CubeImGui.button("Replace", () -> {
            String replacedCode = StringUtils.replaceFirst(this.getText(), this.search.get(), this.replace.get(), this.isFinderRegex);
            this.setText(replacedCode.substring(0, replacedCode.length() - 1));
        });

        ImGui.sameLine();

        CubeImGui.button("Replace All", () -> {
            String replacedCode = StringUtils.replaceFirst(this.getText(), search.get(), replace.get(), this.isFinderRegex);
            this.setText(replacedCode.substring(0, replacedCode.length() - 1));
        });
    }

    private final List<SearchMatch> searchMatches = new ArrayList<>();
    private int currentMatchIndex = -1;

    private void highlightSearchMatches() {
        if (search == null || search.get().isEmpty()) {
            searchMatches.clear();
            currentMatchIndex = -1;
            this.numberSearchWord = 0;
            return;
        }

        final String[] textLines = this.getTextLines();
        if (textLines == null || textLines.length == 0) return;

        final String searchTerm = search.get();
        final int searchLength = searchTerm.length();
        if (searchLength == 0) return; // Защита от пустого поискового запроса

        final int highlightColor = ImGui.getColorU32(0, 0.8f, 1, 0.5f);
        final int borderColor = ImGui.getColorU32(1, 1, 1, 0.5f);
        final int currentMatchColor = ImGui.getColorU32(1, 0.5f, 0, 0.7f);

        final ImDrawList drawList = ImGui.getWindowDrawList();
        final float searchTermWidth = ImGui.calcTextSize(searchTerm).x;
        final float lineHeight = ImGui.calcTextSize("A").y;

        searchMatches.clear();
        int matchCount = 0;

        for (int lineNum = 0; lineNum < textLines.length; lineNum++) {
            final String line = textLines[lineNum];
            if (line == null || line.isEmpty()) continue;

            int fromIndex = 0;
            while (fromIndex <= line.length() - searchLength) {
                fromIndex = line.indexOf(searchTerm, fromIndex);
                if (fromIndex == -1) break;

                // Безопасное получение экранных координат
                ImVec2 startPos, endPos;
                try {
                    startPos = getScreenPos(lineNum, fromIndex);
                    endPos = getScreenPos(lineNum, fromIndex + searchLength);
                } catch (Exception e) {
                    fromIndex += searchLength;
                    continue;
                }

                if (startPos == null || endPos == null) {
                    fromIndex += searchLength;
                    continue;
                }

                searchMatches.add(new SearchMatch(lineNum, fromIndex, fromIndex + searchLength));

                boolean isCurrentMatch = (matchCount == currentMatchIndex);
                drawList.addRectFilled(
                        startPos.x, startPos.y,
                        endPos.x, endPos.y + lineHeight,
                        isCurrentMatch ? currentMatchColor : highlightColor
                );
                drawList.addRect(
                        startPos.x, startPos.y,
                        endPos.x, endPos.y + lineHeight,
                        borderColor
                );

                matchCount++;
                fromIndex += searchLength;
            }
        }

        this.numberSearchWord = matchCount;
    }

    public void goToNextMatch() {
        if (searchMatches.isEmpty()) return;
        currentMatchIndex = (currentMatchIndex + 1) % searchMatches.size();
        scrollToCurrentMatch();
    }

    public void goToPreviousMatch() {
        if (searchMatches.isEmpty()) return;
        currentMatchIndex = (currentMatchIndex - 1 + searchMatches.size()) % searchMatches.size();
        scrollToCurrentMatch();
    }

    private void scrollToCurrentMatch() {
        if (currentMatchIndex < 0 || currentMatchIndex >= searchMatches.size()) return;

        SearchMatch match = searchMatches.get(currentMatchIndex);
        this.setCursorPosition(match.line, match.startPos);
    }

    private void manageKeyboard() {
        if (this.isFocused) {
            if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || ImGui.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_R)) {
                    this.isRenderReplacement = !this.isRenderReplacement;
                    this.isRenderFinder = false;
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_F)) {
                    this.isRenderFinder = !this.isRenderFinder;
                    this.isRenderReplacement = false;
                }
            }
        }
    }

    private void manageMouse() {
        if (this.isFocused) {
            float mouseX = ImGui.getMousePosX();
            float mouseY = ImGui.getMousePosY();

            boolean clickedOutside = (mouseX >= this.x) &&
                    (mouseX <= this.x + this.width) &&
                    (mouseY >= this.y) &&
                    (mouseY <= this.y + this.height);

            if (ImGui.isMouseReleased(ImGuiMouseButton.Right) && clickedOutside) {
                this.isRenderContextMenu = true;
            }
        }
    }

    private void renderDebug() {
        ImVec2 screenPos = this.getScreenPos(this.getCursorPosition().line, this.getCursorPosition().column);

        ImGui.getWindowDrawList().addRectFilled(screenPos.x, screenPos.y, screenPos.x + ImGui.calcTextSize("A").x, screenPos.y + ImGui.calcTextSize("A").y, ImGui.getColorU32(1, 1, 1, 0.5f));
    }

    private void renderContextMenu() {
        if (!isRenderContextMenu)
            return;

        CubeImGui.popup(this.title+"_context_menu", ImGuiWindowFlags.AlwaysAutoResize,
            () -> {
                if (contextMenuRender == null) {
                    this.contextItems.forEach((title, item) -> {
                        ImGui.image(item.icon.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());

                        ImGui.sameLine();

                        ImVec2 cursorPos = ImGui.getCursorPos();

                        if (ImGui.button("##"+item.title, ImGui.calcTextSize(item.title).x, ImGui.calcTextSize(item.title).y)) {
                            item.onClick.run();
                            ImGui.closeCurrentPopup();
                            this.isRenderContextMenu = false;
                        }

                        ImGui.setCursorPos(cursorPos.x, cursorPos.y);

                        int index = item.title.indexOf(" ") + 1;

                        ImGui.textColored(255, 207, 64, 255, item.title.substring(0, index));

                        ImGui.sameLine(0, 0);

                        ImGui.text(item.title.substring(index));
                    });
                } else {
                    contextMenuRender.run();
                }
            },
            () -> {
                this.isRenderContextMenu = false;
            }
        );
    }

    public void addContextItem(Icons icon, String title, Runnable onClick) {
        this.contextItems.putIfAbsent(title, new ContextItem(icon, title, onClick));
    }

    public void removeContextItem(String title) {
        contextItems.remove(title);
    }

    public void setCustomContextMenu(Runnable contextMenuRender) {
        this.contextMenuRender = contextMenuRender;
    }

    public void disableCustomContextMenu() {
        this.contextMenuRender = null;
    }

    public void setFinder(boolean isFinder) {
        this.isFinder = isFinder;
    }

    public void setReplacement(boolean isReplacement) {
        this.isReplacement = isReplacement;
        if (isReplacement)
            this.isFinder = true;
    }

    public void setDebugMode(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isDebugMode() {
        return this.isDebug;
    }

    public void setFlags(int flags) {
        this.windowFlags = flags;
    }

    public void addFlags(int flags) {
        this.windowFlags |= flags;
    }

    private void handleAutoscrollOnSelectionDrag(boolean hadSelectionBeforeRender) {
        if (this.basic.hasSelection()) {
            ImVec2 mousePos = new ImVec2();
            ImGui.getMousePos(mousePos);

            boolean outsideRight = mousePos.x > this.x + ImGui.getScrollX() + this.width;
            boolean outsideLeft = mousePos.x < this.x + ImGui.getScrollX();
            boolean outsideBottom = mousePos.y > this.y + ImGui.getScrollY() + this.height;
            boolean outsideTop = mousePos.y < this.y + ImGui.getScrollY();

            if (hadSelectionBeforeRender && (outsideRight || outsideLeft || outsideBottom || outsideTop)) {
                float scrollX = ImGui.getScrollX();
                float scrollY = ImGui.getScrollY();

                if (outsideRight) {
                    scrollX += 20.0f;
                } else if (outsideLeft) {
                    scrollX -= 20.0f;
                }

                if (outsideBottom) {
                    scrollY += 20.0f;
                } else if (outsideTop) {
                    scrollY -= 20.0f;
                }

                ImGui.setScrollX(scrollX);
                ImGui.setScrollY(scrollY);
            }
        }
    }

    private void renderBackground() {
        ImGui.getWindowDrawList().addRectFilled(
                this.x,
                this.y,
                this.x + this.width,
                this.y + this.height,
                this.colorBackground,
                ImGui.getStyle().getWindowRounding()
        );
    }

    public ImVec2 getPos() {
        return new ImVec2(this.x, this.y);
    }

    public ImVec2 getSize() {
        return new ImVec2(this.width, this.height);
    }

    public void setColorBackground(float r, float g, float b, float a) {
        this.colorBackground = ImGui.getColorU32(r, g, b, a);
    }

    public ImVec4 getColorBackground() {
        ImVec4 rgba = new ImVec4();
        ImGui.colorConvertU32ToFloat4(this.colorBackground, rgba);
        return rgba;
    }

    public final ImVec2 getScreenPos(int line, int column) {
        String tab = "";
        for (int i = 0; i < this.basic.getTabSize(); i++) {
            tab += " ";
        }
        float widthLeftBar = ImGui.calcTextSize(""+this.getTextLines().length).x +
                ImGui.getFontSize() +
                ImGui.getStyle().getItemSpacingX() - ImGui.getScrollX();
        float x = this.x +
                widthLeftBar +
                ImGui.calcTextSize(this.getLineText(line).replaceAll("\t", tab).substring(0, column)).x;
        float y = this.y + ImGui.calcTextSize("A").y * line - ImGui.getScrollY();

        return new ImVec2(x, y);
    }

    public CursorPosition getCursorPosition() {
        return new CursorPosition(
            this.basic.getCursorPositionLine(),
            this.basic.getCursorPositionColumn()
        );
    }

    public String getLineText(int line) {
        return this.getTextLines()[line];
    }





    /// ////////////////////////////////////////////////////////////////////////
    /// BASIC
    /// ////////////////////////////////////////////////////////////////////////






    public void setText(String text) {
        this.basic.setText(text);
    }

    public String getText() {
        return this.basic.getText();
    }

    public String getSelectedText() {
        return this.basic.getSelectedText();
    }

    public void setLanguageDefinition(TextEditorLanguageDefinition definition) {
        this.basic.setLanguageDefinition(definition);
    }

    public int[] getPalette() {
        return this.basic.getPalette();
    }

    public void setPalette(int[] palette) {
        this.basic.setPalette(palette);
    }

    public void setErrorMarkers(Map<Integer, String> errorMarkers) {
        this.basic.setErrorMarkers(errorMarkers);
    }

    public void setBreakpoints(int[] breakpoints) {
        this.basic.setBreakpoints(breakpoints);
    }

    public void setShowBackground(boolean isShowBackground) {
        this.showBackground = isShowBackground;
    }

    public boolean isShowBackground() {
        return this.showBackground;
    }

    public void setTextLines(String[] lines) {
        this.basic.setTextLines(lines);
    }

    public String[] getTextLines() {
        return this.basic.getTextLines();
    }

    public String getCurrentLineText() {
        return this.basic.getCurrentLineText();
    }

    public int getTotalLines() {
        return basic.getTotalLines();
    }

    public boolean isOverwrite() {
        return this.basic.isOverwrite();
    }

    public void setReadOnly(boolean readOnly) {
        this.basic.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return this.basic.isReadOnly();
    }

    public boolean isTextChanged() {
        return this.basic.isTextChanged();
    }

    public boolean isCursorPositionChanged() {
        return this.basic.isCursorPositionChanged();
    }

    public boolean isColorizerEnabled() {
        return this.basic.isColorizerEnabled();
    }

    public void setColorizerEnable(boolean colorizer) {
        this.basic.setColorizerEnable(colorizer);
    }

    public void setCursorPosition(int line, int column) {
        this.basic.setCursorPosition(line, column);
    }

    public void setHandleMouseInputs(boolean handleMouseInputs) {
        this.basic.setHandleMouseInputs(handleMouseInputs);
    }

    public boolean isHandleMouseInputsEnabled() {
        return this.basic.isHandleMouseInputsEnabled();
    }

    public void setHandleKeyboardInputs(boolean handleKeyboardInputs) {
        this.basic.setHandleMouseInputs(handleKeyboardInputs);
    }

    public boolean isHandleKeyboardInputsEnabled() {
        return this.basic.isHandleKeyboardInputsEnabled();
    }

    public void setImGuiChildIgnored(boolean imGuiChildIgnored) {
        this.basic.setImGuiChildIgnored(imGuiChildIgnored);
    }

    public boolean isImGuiChildIgnored() {
        return this.basic.isImGuiChildIgnored();
    }

    public void setShowWhitespaces(boolean showWhitespaces) {
        this.basic.setShowWhitespaces(showWhitespaces);
    }

    public boolean isShowingWhitespaces() {
        return this.basic.isShowingWhitespaces();
    }

    public void setTabSize(int tabSize) {
        this.basic.setTabSize(tabSize);
    }

    public int getTabSize() {
        return this.basic.getTabSize();
    }

    public void insertText(String text) {
        this.basic.insertText(text);
    }

    public void moveUp(int amount, boolean select) {
        this.basic.moveUp(amount, select);
    }

    public void moveDown(int amount, boolean select) {
        this.basic.moveDown(amount, select);
    }

    public void moveLeft(int amount, boolean select, boolean wordMode) {
        this.basic.moveLeft(amount, select, wordMode);
    }

    public void moveRight(int amount, boolean select, boolean wordMode) {
        this.basic.moveRight(amount, select, wordMode);
    }

    public void moveTop(boolean select) {
        this.basic.moveTop(select);
    }

    public void moveBottom(boolean select) {
        this.basic.moveBottom(select);
    }

    public void moveHome(boolean select) {
        this.basic.moveHome(select);
    }

    public void moveEnd(boolean select) {
        this.basic.moveEnd(select);
    }

    public void setSelectionStart(int line, int column) {
        this.basic.setSelectionStart(line, column);
    }

    public void setSelectionEnd(int line, int column) {
        this.basic.setSelectionEnd(line, column);
    }

    public void setSelection(int lineStart, int columnStart, int lineEnd, int columnEnd, int selectionMode) {
        this.basic.setSelection(lineStart, columnStart, lineEnd, columnEnd, selectionMode);
    }

    public void selectWordUnderCursor() {
        this.basic.selectWordUnderCursor();
    }

    public void selectAll() {
        this.basic.selectAll();
    }

    public boolean hasSelection() {
        return this.basic.hasSelection();
    }

    public void copy() {
        this.basic.copy();
    }

    public void cut() {
        this.basic.cut();
    }

    public void paste() {
        this.basic.paste();
    }

    public void delete() {
        this.basic.delete();
    }

    public boolean canUndo() {
        return this.basic.canUndo();
    }

    public boolean canRedo() {
        return this.basic.canRedo();
    }

    public void undo(int steps) {
        this.basic.undo(steps);
    }

    public void redo(int steps) {
        this.basic.redo(steps);
    }

    public int[] getDarkPalette() {
        return this.basic.getDarkPalette();
    }

    public int[] getLightPalette() {
        return this.basic.getLightPalette();
    }

    public int[] getRetroBluePalette() {
        return this.basic.getRetroBluePalette();
    }

    public record CursorPosition(int line, int column) {
    }

    private record SearchMatch(int line, int startPos, int endPos) {
    }

    private static final class ContextItem {
        Icons icon;
        String title;
        Runnable onClick;

        public ContextItem(Icons icon, String title, Runnable onClick) {
            this.icon = icon;
            this.title = title;
            this.onClick = onClick;
        }
    }
}
