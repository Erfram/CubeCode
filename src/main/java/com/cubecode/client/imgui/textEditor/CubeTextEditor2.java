package com.cubecode.client.imgui.textEditor;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.utils.Icons;
import com.cubecode.utils.StringUtils;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.*;
import imgui.type.ImString;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CubeTextEditor2 {
    public static List<CubeTextEditor2> textEditors = new ArrayList<>();

    private final TextEditor EDITOR = new TextEditor();
    private final String title;

    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;

    private Selection selection = Selection.EMPTY;
    private final HistoryManager historyManager;
    private final SnippetManager snippetManager;

    private final Background background = new Background(ImGui.getColorU32(0.15f, 0.15f, 0.15f, 1f));
    private final Debug debug = new Debug();

    private boolean focused = false;

    private boolean showBackground = true;
    private boolean isDebug;
    private boolean isSearch;
    private boolean isReplace;

    public CubeTextEditor2(String title) {
        this.title = title;
        this.historyManager = new HistoryManager();
        this.snippetManager = new SnippetManager();

        EDITOR.setReadOnly(true);
        EDITOR.setImGuiChildIgnored(true);

        textEditors.add(this);
    }

    public void render() {
        if (this.isSearch) {
            renderSearch();
        }
        this.x = ImGui.getCursorScreenPosX();
        this.y = ImGui.getCursorScreenPosY();
        this.width = ImGui.getWindowWidth();
        this.height = ImGui.getWindowHeight();

        if (ImGui.beginChild(this.title, 0, 0, false, ImGuiWindowFlags.NoMove | ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.NoNav)) {
            if (this.showBackground) {
                this.background.render(this.x, this.y, this.width, this.height);
            }

            boolean hasSelection = EDITOR.hasSelection();
            renderAutocomplete();

            EDITOR.render(this.title);

            autoScrollOnDrag(hasSelection);

            updateState();

            if (focused) {
                handleKeys();
                handleMouse();
            }

            highlightSearchMatches();

            if (this.isDebug) {
                ImVec2 cursorScreenPos = this.getCursorScreenPos();
                this.debug.render(cursorScreenPos.x, cursorScreenPos.y);
            }
        }

        ImGui.endChild();
    }

    private void autoScrollOnDrag(boolean wasSelected) {
        if (EDITOR.hasSelection()) {
            ImVec2 mousePos = new ImVec2();
            ImGui.getMousePos(mousePos);

            boolean outsideRight = mousePos.x > this.x + ImGui.getScrollX() + this.width;
            boolean outsideLeft = mousePos.x < this.x + ImGui.getScrollX();
            boolean outsideBottom = mousePos.y > this.y + ImGui.getScrollY() + this.height;
            boolean outsideTop = mousePos.y < this.y + ImGui.getScrollY();

            if (wasSelected && (outsideRight || outsideLeft || outsideBottom || outsideTop)) {
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

    private void updateState() {
        focused = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);
        selection = getSelection();
    }

    private void handleKeys() {
        if (ImGui.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)) {
            backspace();
        }

        if (!ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) && ImGui.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
            if (!isRenderAutocomplete){
                enter();
            } else {
                List<String> matchingSnippets = snippetManager.getMatchingSnippets(autocomplete);

                if (index >= 0 && index < matchingSnippets.size())
                    applySnippet();
            }
        }

        if (ImGui.isKeyPressed(GLFW.GLFW_KEY_TAB)) {
            tab();
        }

        if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT)) {
                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_UP)) {
                    moveLineUp();
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
                    moveLineDown();
                }
            }

            if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) ) {
                List<String> matchingSnippets = snippetManager.getMatchingSnippets(autocomplete);
                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
                    if (this.index < matchingSnippets.size()-1) {
                        selectAutocomplete.name = matchingSnippets.get(this.index+=1);
                    }
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_UP)) {
                    if (this.index > 0) {
                        selectAutocomplete.name = matchingSnippets.get(this.index-=1);
                    }
                }
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_Z)) {
                undo();
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_Y)) {
                redo();
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_D)) {
                duplicateLine();
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_SLASH)) {
                commentLine();
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_R)) {
                this.isSearch = !this.isReplace;
                this.isReplace = !this.isReplace;
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_F)) {
                this.isSearch = !this.isSearch;
                this.isReplace = false;
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_V)) {
                paste();
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_X)) {
                cut();
            }

            if (!ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) && ImGui.isKeyPressed(GLFW.GLFW_KEY_UP)) {
                moveScrollUp();
            }

            if (!ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) && ImGui.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
                moveScrollDown();
            }
        }
    }


    private void handleMouse() {
        if (ImGui.isAnyMouseDown()) {
            isRenderAutocomplete = false;
        }
    }

    /// /////////////////CHARS////////////////////////

    public void typeChar(char c) {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        if (!EDITOR.hasSelection()) {
            insert(c);
        } else {
            replaceSelection(c);
        }

        handleSnippets();
    }

    private void handleSnippets() {
        String modifiedCurrentLine = this.getCurrentLine().replace(" ", "");

        this.index = 0;

        List<String> names = this.snippetManager.getNames();

        for (String key : names) {
            if (!modifiedCurrentLine.isEmpty() && !this.snippetManager.getMatchingSnippets(modifiedCurrentLine).isEmpty()) {
                this.isRenderAutocomplete = true;
                this.autocomplete = modifiedCurrentLine;
                break;
            } else if (modifiedCurrentLine.isEmpty() || !key.contains(modifiedCurrentLine)) {
                this.isRenderAutocomplete = false;
            }
        }
    }

    private boolean isRenderAutocomplete = false;
    private String autocomplete = "";
    private SnippetManager.Snippet selectAutocomplete = new SnippetManager.Snippet("", "");
    private int index = -1;

    private void renderAutocomplete() {
        if (!isRenderAutocomplete) return;

        ImVec2 cursorPos = getCursorScreenPos();
        
        ImGui.setNextWindowPos(cursorPos.x - 100, cursorPos.y + ImGui.getTextLineHeight());
        ImGui.setNextWindowSize(200, 100);

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 5, 5);
        ImGui.beginTooltip();
        {
            List<String> matchingSnippets = snippetManager.getMatchingSnippets(autocomplete);

            for (int i = 0; i < matchingSnippets.size(); i++) {
                String snippet = matchingSnippets.get(i);
                if (ImGui.selectable(snippet, i == index)) {
                    selectAutocomplete = snippetManager.getSnippet(snippet);
                    if (ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                        String currentLine = getCurrentLine();
                        int cursorCol = getCursorPos().column;

                        int snippetStart = currentLine.lastIndexOf(selectAutocomplete.name, cursorCol);
                        if (snippetStart == -1) snippetStart = Math.max(0, cursorCol - selectAutocomplete.name.length());

                        String before = currentLine.substring(0, snippetStart);
                        String afterSnippet = selectAutocomplete.code.replace("$END$", "");
                        this.setCurrentLine(before + afterSnippet);

                        int caretOffset = selectAutocomplete.code.indexOf("$END$");
                        if (caretOffset == -1) caretOffset = afterSnippet.length();
                        this.moveCursor(getCursorPos().line, before.length() + caretOffset);

                        isRenderAutocomplete = false;
                    }
                }
            }
        }
        ImGui.endTooltip();
        ImGui.popStyleVar();

        // Возвращаем фокус в редактор
        ImGui.setKeyboardFocusHere();
    }

    private void enter() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        if (EDITOR.hasSelection()) {
            replaceSelectionWithNewline();
        } else {
            newline();
        }
    }

    private void applySnippet() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        SnippetManager.Snippet snippet = snippetManager.getSnippet(snippetManager.getMatchingSnippets(autocomplete).get(index));
        String currentLine = this.getCurrentLine();

        this.setCurrentLine(currentLine.substring(0, this.getCursorPos().column - autocomplete.length()) + snippet.code);
        this.moveCursor(this.getCursorPos().line, getCurrentLine().indexOf("$END$"));
        this.setCurrentLine(getCurrentLine().replace("$END$", ""));

        isRenderAutocomplete = false;
        index = 0;
    }

    private void backspace() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        if (!EDITOR.hasSelection()) {
            deleteChar();
        } else {
            deleteSelection();
        }

        handleSnippets();
    }

    private void tab() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        Position position = getCursorPos();
        String line = getLine(position.line);
        String tab = " ".repeat(EDITOR.getTabSize());

        setLine(position.line, new StringBuilder(line).insert(position.column, tab).toString());
        moveCursor(position.line, position.column + EDITOR.getTabSize());
    }

    private void moveLineUp() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        Position cursorPos = this.getCursorPos();

        if (cursorPos.line > 0) {
            String currentLine = this.getCurrentLine();
            String upLine = this.getLine(this.getCursorPos().line - 1);

            setLine(getCursorPos().line - 1, currentLine);
            setLine(getCursorPos().line, upLine);
            moveCursor(getCursorPos().line - 1, getCursorPos().column);
        }
    }

    private void moveLineDown() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        Position cursorPos = this.getCursorPos();

        if (cursorPos.line < getLineCount() - 1) {
            String currentLine = this.getCurrentLine();
            String upLine = this.getLine(this.getCursorPos().line + 1);

            setLine(getCursorPos().line + 1, currentLine);
            setLine(getCursorPos().line, upLine);
            moveCursor(getCursorPos().line + 1, getCursorPos().column);
        }
    }

    private void duplicateLine() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());

        Position cursorPos = getCursorPos();
        addLine(cursorPos.line, getLine(cursorPos.line));
        moveCursor(cursorPos.line+1, cursorPos.column);
    }

    private void commentLine() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        String currentLine = getCurrentLine();
        String text;
        if (currentLine.contains("//")) {
            text = getCurrentLine().replaceFirst("//", "");
        } else {
            text = "//"+getCurrentLine();
        }

        setCurrentLine(text);
        moveCursor(getCursorPos().line, getCurrentLine().length());
    }

    private void paste() {
        historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
        String clipboard = ImGui.getClipboardText();
        if (clipboard == null || clipboard.isEmpty()) return;

        if (!EDITOR.hasSelection()) {
            insert(clipboard);
        } else {
            Selection selection = getSelection();
            if (selection.isSingleLine()) {
                String line = getLine(selection.start.line);

                setLine(selection.start.line, new StringBuilder(line).replace(selection.start.column, selection.end.column, clipboard).toString());
            } else {
                deleteSelectionMulti();
                insert(clipboard);
            }
            moveCursor(selection.start.line, getCursorPos().column+clipboard.length());
            clearSelection();
        }
    }

    private void cut() {
        if (EDITOR.hasSelection()) {
            this.cutSelection();
        } else {
            ImGui.setClipboardText(this.getCurrentLine());
            this.removeCurrentLine();
            this.moveCursor(this.getCursorPos().line, 0);
        }
    }

    private void moveScrollUp() {
        ImGui.setScrollY(Math.max(0, ImGui.getScrollY()-15f));
    }

    private void moveScrollDown() {
        ImGui.setScrollY(Math.min(ImGui.getScrollMaxY(), ImGui.getScrollY()+15f));
    }

    private void undo() {
        HistoryManager.History undo = this.historyManager.undo();

        if (undo != null) {
            Position cursorPos = undo.cursorPos;

            this.selection = undo.selection;
            setLines(undo.lines);
            moveCursor(cursorPos.line, cursorPos.column);
        }
    }

    private void redo() {
        HistoryManager.History redo = this.historyManager.redo();

        if (redo != null) {
            Position cursorPos = redo.cursorPos;

            this.selection = redo.selection;
            setLines(redo.lines);
            moveCursor(cursorPos.line, cursorPos.column);
        }
    }

    /// /////////////////////////////////////////

    ImString search = new ImString(150);
    int numberSearchWord = 0;
    boolean isSearchRegex = false;
    final List<SearchMatch> searchMatches = new ArrayList<>();
    int currentMatchIndex = -1;
    ImString replace = new ImString(150);

    private void renderSearch() {
        float height = this.isReplace ? ImGui.calcTextSize("A").y*3 + ImGui.getStyle().getItemSpacingY() * 2 : ImGui.calcTextSize("A").y*2;

        CubeImGui.beginChild("searchAndReplace", 0, height, true, ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoScrollbar, () -> {
            float initialPosY = ImGui.getCursorPosY();
            ImGui.image(Icons.SEARCH.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());

            ImGui.sameLine();

            ImGui.pushItemWidth(ImGui.getFontSize() + 150);
            ImGui.inputTextWithHint("##Search", "Search", this.search);
            ImGui.popItemWidth();

            ImGui.sameLine();


            if (ImGui.radioButton("Regex", this.isSearchRegex)) {
                this.isSearchRegex = !this.isSearchRegex;
            }

            ImGui.sameLine();

            ImGui.text("|");

            ImGui.sameLine();
            ImGui.text(this.currentMatchIndex + 1 + "/" + this.numberSearchWord);

            ImGui.sameLine();

            ImGui.text("|");

            ImGui.sameLine();

            if (ImGui.button("\ue5d8", ImGui.getFontSize(), ImGui.getFontSize())) {
                this.prevMatch();
            }

            ImGui.sameLine();

            if (ImGui.button("\ue5db", ImGui.getFontSize(), ImGui.getFontSize())) {
                this.nextMatch();
            }

            if (this.isReplace) {
                this.renderReplace();
            }

            float rightEdge = ImGui.getWindowWidth() - ImGui.getFontSize() - ImGui.getStyle().getWindowPaddingX() - ImGui.getStyle().getItemSpacingX();

            ImGui.setCursorPosX(rightEdge);
            ImGui.setCursorPosY(initialPosY);

            CubeImGui.imageButton(Icons.MINUS, "Close", ImGui.getFontSize(), ImGui.getFontSize(), () -> {
                this.isSearch = false;
                this.isReplace = false;
                this.search.clear();
            });
        });
    }

    private void renderReplace() {
        ImGui.image(Icons.SEARCH.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());
        ImGui.sameLine();

        ImGui.pushItemWidth(ImGui.getFontSize() + 150);
        ImGui.inputTextWithHint("##Replace", "Replace", this.replace);
        ImGui.popItemWidth();

        ImGui.sameLine();

        CubeImGui.button("Replace", () -> {
            historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
            String replacedCode = StringUtils.replaceFirst(EDITOR.getText(), search.get(), replace.get(), isSearchRegex);
            EDITOR.setText(replacedCode.substring(0, replacedCode.length() - 1));
        });

        ImGui.sameLine();

        CubeImGui.button("Replace All", () -> {
            historyManager.addHistory(getAllLines(), getCursorPos(), getSelection());
            String replacedCode = StringUtils.replaceAll(EDITOR.getText(), search.get(), replace.get(), isSearchRegex);
            EDITOR.setText(replacedCode.substring(0, replacedCode.length() - 1));
        });
    }

    public void nextMatch() {
        if (!searchMatches.isEmpty()) {
            currentMatchIndex = (currentMatchIndex + 1) % searchMatches.size();
            scrollToMatch();
        }
    }

    public void prevMatch() {
        if (!searchMatches.isEmpty()) {
            currentMatchIndex = (currentMatchIndex - 1 + searchMatches.size()) % searchMatches.size();
            scrollToMatch();
        }
    }

    private void scrollToMatch() {
        if (currentMatchIndex >= 0 && currentMatchIndex < searchMatches.size()) {
            SearchMatch m = searchMatches.get(currentMatchIndex);
            moveCursor(m.line, m.startPos);
        }
    }

    private void highlightSearchMatches() {
        // Проверка на пустой поиск
        if (search == null || search.get().isEmpty()) {
            clearSearchState();
            return;
        }

        final String term = search.get();
        if (term.isEmpty()) return;

        final int normalColor = ImGui.getColorU32(0, 0.8f, 1, 0.5f);
        final int currentColor = ImGui.getColorU32(1, 0.5f, 0, 0.7f);
        final int borderColor = ImGui.getColorU32(1, 1, 1, 0.5f);
        final float lineHeight = ImGui.getTextLineHeight();
        final ImDrawList drawList = ImGui.getWindowDrawList();

        searchMatches.clear();
        int matchesFound = 0;

        Pattern pattern = null;
        if (isSearchRegex) {
            try {
                pattern = Pattern.compile(term);
            } catch (PatternSyntaxException e) {
                return;
            }
        }

        for (int lineIdx = 0; lineIdx < getLineCount(); lineIdx++) {
            String line = getLine(lineIdx);
            if (line == null) continue;

            if (isSearchRegex && pattern != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    int startIdx = matcher.start();
                    int endIdx = matcher.end();

                    ImVec2 start = getScreenPos(lineIdx, startIdx);
                    ImVec2 end = getScreenPos(lineIdx, endIdx);

                    if (start != null && end != null) {
                        searchMatches.add(new SearchMatch(lineIdx, startIdx, endIdx));

                        boolean isCurrent = matchesFound == currentMatchIndex;
                        drawList.addRectFilled(start.x, start.y, end.x, end.y + lineHeight,
                                isCurrent ? currentColor : normalColor);
                        drawList.addRect(start.x, start.y, end.x, end.y + lineHeight, borderColor);

                        matchesFound++;
                    }
                }
            } else {
                for (int pos = 0; (pos = line.indexOf(term, pos)) != -1; pos += term.length()) {
                    ImVec2 start = getScreenPos(lineIdx, pos);
                    ImVec2 end = getScreenPos(lineIdx, pos + term.length());

                    if (start != null && end != null) {
                        searchMatches.add(new SearchMatch(lineIdx, pos, pos + term.length()));

                        boolean isCurrent = matchesFound == currentMatchIndex;
                        drawList.addRectFilled(start.x, start.y, end.x, end.y + lineHeight,
                                isCurrent ? currentColor : normalColor);
                        drawList.addRect(start.x, start.y, end.x, end.y + lineHeight, borderColor);

                        matchesFound++;
                    }
                }
            }
        }

        this.numberSearchWord = matchesFound;
    }

    private void clearSearchState() {
        searchMatches.clear();
        currentMatchIndex = -1;
        this.numberSearchWord = 0;
    }

    /// /////////////////////////////////////////

    private void replaceSelectionWithNewline() {
        if (!selection.isValid(getLineCount())) return;

        if (selection.isSingleLine()) {
            String line = getLine(selection.start().line);
            setLine(selection.start().line, line.substring(0, selection.start().column));
            addLine(selection.start().line + 1, line.substring(selection.end().column));
        } else {
            String first = getLine(selection.start().line);
            String last = getLine(selection.end().line);

            setLine(selection.start().line, first.substring(0, selection.start().column));

            for (int i = selection.end().line; i >= selection.start().line; i--) {
                if (i != selection.start().line) removeLine(i);
            }

            if (!last.substring(selection.end().column).isEmpty()) {
                addLine(selection.start().line + 1, last.substring(selection.end().column));
            }
        }

        moveCursor(selection.start().line + 1, 0);
        clearSelection();
    }

    private void newline() {
        Position cursor = getCursorPos();
        String line = getLine(cursor.line);

        setLine(cursor.line, line.substring(0, cursor.column));
        addLine(cursor.line + 1, line.substring(cursor.column));

        moveCursor(cursor.line + 1, 0);
    }

    private void insert(char c) {
        insert(Character.toString(c));
    }

    public void insert(String text) {
        Position position = getCursorPos();
        String line = getLine(position.line);

        setLine(position.line, new StringBuilder(line).insert(position.column, text).toString());
        moveCursor(position.line, position.column + text.length());
    }

    private void replaceSelection(char c) {
        if (!selection.isValid(getLineCount())) return;

        if (selection.isSingleLine()) {
            replaceSelectionLine(c);
        } else {
            replaceSelectionMulti(c);
        }

        moveCursor(selection.start().line, selection.start().column + 1);
        clearSelection();
    }

    private void deleteChar() {
        Position position = getCursorPos();

        if (position.column > 0) {
            String line = getLine(position.line);
            setLine(position.line, line.substring(0, position.column - 1) + line.substring(position.column));
            moveCursor(position.line, position.column - 1);
        } else if (position.line > 0) {
            mergeLines(position.line);
        }
    }

    private void replaceSelectionLine(char c) {
        String line = getLine(selection.start().line);
        setLine(selection.start().line,
                line.substring(0, selection.start().column) + c + line.substring(selection.end().column));
    }

    private void replaceSelectionMulti(char c) {
        String first = getLine(selection.start().line);
        String last = getLine(selection.end().line);

        setLine(selection.start().line,
                first.substring(0, selection.start().column) + c + last.substring(selection.end().column));

        for (int i = selection.end().line; i > selection.start().line; i--) {
            removeLine(i);
        }
    }

    private void cutSelection() {
        if (selection.isSingleLine()) {
            cutSelectionLine();
        } else {
            cutSelectionMulti();
        }

        moveCursor(selection.start().line, selection.start().column);
        clearSelection();
    }

    private void cutSelectionLine() {
        String line = getLine(selection.start().line);
        setLine(selection.start().line,
                line.substring(0, selection.start().column) + line.substring(selection.end().column));

        ImGui.setClipboardText(line.substring(selection.start().column, selection.end().column));
    }

    private void cutSelectionMulti() {
        StringBuilder cutText = new StringBuilder();
        String first = getLine(selection.start().line);
        String last = getLine(selection.end().line);

        cutText.append(first.substring(selection.start().column));

        for (int i = selection.start().line + 1; i < selection.end().line; i++) {
            cutText.append("\n").append(getLine(i));
        }

        cutText.append("\n").append(last.substring(0, selection.end().column));

        setLine(selection.start().line,
                first.substring(0, selection.start().column) + last.substring(selection.end().column));

        for (int i = selection.end().line; i > selection.start().line; i--) {
            removeLine(i);
        }

        ImGui.setClipboardText(cutText.toString());
    }

    private void deleteSelection() {
        if (selection.isSingleLine()) {
            deleteSelectionLine();
        } else {
            deleteSelectionMulti();
        }

        moveCursor(selection.start().line, selection.start().column);
        clearSelection();
    }

    private void deleteSelectionLine() {
        String line = getLine(selection.start().line);
        setLine(selection.start().line,
                line.substring(0, selection.start().column) + line.substring(selection.end().column));
    }

    private void deleteSelectionMulti() {
        String first = getLine(selection.start().line);
        String last = getLine(selection.end().line);

        setLine(selection.start().line,
                first.substring(0, selection.start().column) + last.substring(selection.end().column));

        for (int i = selection.end().line; i > selection.start().line; i--) {
            removeLine(i);
        }
    }

    private void mergeLines(int line) {
        List<String> lines = getAllLines();
        String prev = lines.get(line - 1);
        String curr = lines.get(line);

        lines.set(line - 1, prev + curr);
        lines.remove(line);

        setLines(lines);
        moveCursor(line - 1, prev.length());
    }

    private Selection getSelection() {
        if (!EDITOR.hasSelection() || EDITOR.getSelectedText().isEmpty()) {
            return Selection.EMPTY;
        }

        String selectedText = EDITOR.getSelectedText();
        String fullText = EDITOR.getText();
        Position cursorPos = this.getCursorPos(); // Текущая позиция курсора

        // Находим линейный индекс курсора
        int cursorOffset = positionToOffset(cursorPos);

        // Ищем выделенный текст в окрестности курсора (не по всему тексту!)
        int searchRadius = selectedText.length() * 2; // Ищем в пределах 2-х длин выделения
        int searchStart = Math.max(0, cursorOffset - searchRadius);
        int searchEnd = Math.min(fullText.length(), cursorOffset + searchRadius);

        // Находим ВСЕ вхождения выделенного текста в этой зоне
        List<Integer> matches = new ArrayList<>();
        int idx = fullText.indexOf(selectedText, searchStart);
        while (idx != -1 && idx < searchEnd) {
            matches.add(idx);
            idx = fullText.indexOf(selectedText, idx + 1);
        }

        // Если нашли ровно одно вхождение — берём его
        if (matches.size() == 1) {
            int startIdx = matches.get(0);
            int endIdx = startIdx + selectedText.length();
            return new Selection(findPos(startIdx), findPos(endIdx));
        }

        // Если несколько вхождений — выбираем ближайшее к курсору
        if (!matches.isEmpty()) {
            int bestMatch = matches.get(0);
            int minDist = Math.abs(matches.get(0) - cursorOffset);

            for (int i = 1; i < matches.size(); i++) {
                int dist = Math.abs(matches.get(i) - cursorOffset);
                if (dist < minDist) {
                    minDist = dist;
                    bestMatch = matches.get(i);
                }
            }

            int startIdx = bestMatch;
            int endIdx = startIdx + selectedText.length();
            return new Selection(findPos(startIdx), findPos(endIdx));
        }

        // Если не нашли — возвращаем пустое выделение
        return Selection.EMPTY;
    }

    /**
     * Конвертирует линейный индекс в позицию (строка, колонка).
     */
    private Position findPos(int offset) {
        String text = EDITOR.getText();
        int line = 0;
        int col = 0;

        for (int i = 0; i < offset && i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                line++;
                col = 0;
            } else {
                col++;
            }
        }

        return new Position(line, col);
    }

    /**
     * Конвертирует позицию (строка, колонка) в линейный индекс.
     */
    private int positionToOffset(Position pos) {
        String text = EDITOR.getText();
        int currentLine = 0;
        int currentCol = 0;
        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            if (currentLine == pos.line && currentCol == pos.column) {
                return offset;
            }

            char c = text.charAt(i);
            if (c == '\n') {
                currentLine++;
                currentCol = 0;
            } else {
                currentCol++;
            }
            offset++;
        }

        return offset;
    }

    private void clearSelection() {
        EDITOR.setSelectionStart(0, 0);
        EDITOR.setSelectionEnd(0, 0);
    }

    public void moveCursor(int line, int col) {
        EDITOR.setCursorPosition(line, col);
    }

    public List<String> getAllLines() {
        return new ArrayList<>(List.of(EDITOR.getTextLines()));
    }

    public void addLine(int line, String text) {
        List<String> lines = getAllLines();
        lines.add(line, text);
        setLines(lines);
    }

    public void setLines(List<String> lines) {
        EDITOR.setTextLines(lines.toArray(new String[0]));
    }

    public String getLine(int line) {
        return EDITOR.getTextLines()[line];
    }

    public String getCurrentLine() {
        return EDITOR.getCurrentLineText();
    }

    public void setLine(int line, String text) {
        List<String> lines = getAllLines();
        lines.set(line, text);
        setLines(lines);
    }

    public void setCurrentLine(String text) {
        List<String> lines = getAllLines();
        lines.set(getCursorPos().line, text);
        setLines(lines);
    }

    public void removeLine(int line) {
        List<String> lines = getAllLines();
        lines.remove(line);
        setLines(lines);
    }

    public void removeCurrentLine() {
        List<String> lines = getAllLines();
        lines.remove(this.getCursorPos().line);
        setLines(lines);
    }

    public int getLineCount() {
        return EDITOR.getTextLines().length;
    }

    public ImVec2 getCursorScreenPos() {
        return getScreenPos(getCursorPos().line, getCursorPos().column);
    }

    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public ImVec2 getScreenPos(int line, int column) {
        String tab = "";
        for (int i = 0; i < EDITOR.getTabSize(); i++) {
            tab += " ";
        }
        float widthLeftBar = ImGui.calcTextSize(""+getLineCount()).x +
                ImGui.getFontSize() +
                ImGui.getStyle().getItemSpacingX() - ImGui.getScrollX();
        float x = this.x +
                widthLeftBar +
                ImGui.calcTextSize(getLine(line).replaceAll("\t", tab).substring(0, column)).x;

        float y = this.y + ImGui.calcTextSize("A").y * line - ImGui.getScrollY();

        return new ImVec2(x, y);
    }

    public record Position(int line, int column) {
        public static final Position INVALID = new Position(-1, -1);
    }

    public record Selection(Position start, Position end) {
        public static final Selection EMPTY = new Selection(Position.INVALID, Position.INVALID);

        public boolean isValid(int lineCount) {
            return start.line >= 0 && start.line < lineCount &&
                    end.line >= 0 && end.line < lineCount;
        }

        public boolean isSingleLine() {
            return start.line == end.line;
        }
    }

    public Position getCursorPos() {
        return new Position(
                EDITOR.getCursorPositionLine(),
                EDITOR.getCursorPositionColumn()
        );
    }

    public static class Background {
        int color;

        public Background(int color) {
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void render(float x, float y, float width, float height) {
            ImGui.getWindowDrawList().addRectFilled(
                    x,
                    y,
                    x + width,
                    y + height,
                    this.color,
                    ImGui.getStyle().getWindowRounding()
            );
        }
    }

    public static class Debug {
        public void render(float x, float y) {
            ImGui.getWindowDrawList().addRectFilled(x, y, x + ImGui.calcTextSize("A").x, y + ImGui.calcTextSize("A").y, ImGui.getColorU32(1, 1, 1, 0.5f));
        }
    }

    public record SearchMatch(int line, int startPos, int endPos) {
    }

    public void onClose() {
        focused = false;
    }
}