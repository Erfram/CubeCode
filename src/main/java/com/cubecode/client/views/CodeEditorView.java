package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.treesitter.CodeLabel;
import com.cubecode.client.treesitter.TreeSitterParser;
import imgui.*;
import imgui.flag.*;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HKL;
import com.sun.jna.platform.win32.WinUser;

import java.util.*;


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

    private static final Map<Character, Character> shiftMap = new HashMap<>();

    static {
        shiftMap.put('`', '~');
        shiftMap.put('1', '!');
        shiftMap.put('2', '@');
        shiftMap.put('3', '#');
        shiftMap.put('4', '$');
        shiftMap.put('5', '%');
        shiftMap.put('6', '^');
        shiftMap.put('7', '&');
        shiftMap.put('8', '*');
        shiftMap.put('9', '(');
        shiftMap.put('0', ')');
        shiftMap.put('-', '_');
        shiftMap.put('=', '+');
        shiftMap.put('[', '{');
        shiftMap.put(']', '}');
        shiftMap.put('\\', '|');
        shiftMap.put(';', ':');
        shiftMap.put('\'', '"');
        shiftMap.put(',', '<');
        shiftMap.put('.', '>');
        shiftMap.put('/', '?');
    }

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
        Window.create()
                .title(getName())
                .flags(ImGuiWindowFlags.HorizontalScrollbar)
                .callback(() -> {
                    ImVec2 windowSize = ImGui.getWindowSize();

                    ImGui.pushStyleColor(ImGuiCol.ChildBg, this.getTheme().getColor("background"));
                    ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
                    ImGui.beginChild(getName(), windowSize.x - 15, windowSize.y - 35, true, ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.AlwaysHorizontalScrollbar | ImGuiWindowFlags.NoMove);
                    drawCode();
                    handleKeyboardInputs();
                    handleCursor();
                    ImGui.endChild();
                    ImGui.popStyleVar();
                    ImGui.popStyleColor();
        }).render(this);
    }

    @Override
    public void handleKeyReleased(int keyCode, int scanCode, int modifiers) {
        try {
            if (InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString().length() == 1) {
                char enteredChar = InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString().charAt(0);

                boolean isShiftPressed = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

                boolean isBig = isShiftPressed;

                if (isWinCapsLock()) {
                    isBig = !isBig;
                }

                if (!isBig) {
                    enteredChar = Character.toLowerCase(enteredChar);
                }

                enterCharacter(enteredChar, isShiftPressed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isWinCapsLock() {
        byte[] keyboardState = new byte[256];
        boolean success = User32.INSTANCE.GetKeyboardState(keyboardState);

        boolean capsLockOn = false;

        if (success) {
            capsLockOn = (keyboardState[0x14] & 1) != 0;
        }

        return capsLockOn;
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
            } else if (!alt && ImGui.isKeyPressed(ImGui.getKeyIndex(ImGuiKey.Backspace))) {
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
            drawList.addText(x + offsetX, y + offsetY, this.getTheme().getColor("defaultCode"), line);

            offsetY += fontSize;
        }

        offsetY = 0;

        for (String line : lines) {
            if (String.valueOf(row).length() != numberOfDigits) {
                drawList.addText(x, y + offsetY, this.getTheme().getColor("currentLineEdge"), " ".repeat(numberOfDigits + 1) + row);
            } else {
                drawList.addText(x, y + offsetY, this.getTheme().getColor("currentLineEdge"), String.valueOf(row));
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
            drawList.addText(x + offsetX + cursor.x, y + cursor.y, this.getTheme().getColor("cursor"), "|");
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
        linesHighlights = TreeSitterParser.parse(lines, this.getTheme());
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

    private void enterCharacter(char character, boolean shift) {
        if (cursorText != null) {
            try {
                int lineIndex = (int) cursorText.y;
                int columnIndex = (int) cursorText.x;

                if (character == '\n') {
                    String currentLine = lines.get(lineIndex);
                    String newLine = currentLine.substring(columnIndex);
                    lines.set(lineIndex, currentLine.substring(0, columnIndex));
                    lines.add(lineIndex + 1, newLine);
                    cursorText = new ImVec2(0, lineIndex + 1);
                } else {
                    String line = lines.get(lineIndex);
                    StringBuilder builder = new StringBuilder(line);

                    if (shift) {
                        character = shiftMap.get(character) != null ? shiftMap.get(character) : character;
                    }

                    builder.insert(columnIndex, character);
                    lines.set(lineIndex, builder.toString());
                    cursorText = new ImVec2(columnIndex + 1, lineIndex);
                }

                updateCursorPosition();
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
        if (cursorText != null) {
            try {
                int lineIndex = (int) cursorText.y;
                String line = lines.get(lineIndex);

                if ((int) cursorText.x < line.length()) {
                    StringBuilder builder = new StringBuilder(line);
                    builder.deleteCharAt((int) cursorText.x);
                    lines.set(lineIndex, builder.toString());

                    updateCursorPosition();
                    handleEditorChanged();
                } else if (lineIndex < lines.size() - 1) {
                    String nextLine = lines.get(lineIndex + 1);
                    lines.set(lineIndex, line + nextLine);
                    lines.remove(lineIndex + 1);
                    updateCursorPosition();
                    handleEditorChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void backspace() {
        if (cursorText != null) {
            try {
                int lineIndex = (int) cursorText.y;
                String line = lines.get(lineIndex);

                if ((int) cursorText.x > 0) {
                    StringBuilder builder = new StringBuilder(line);
                    builder.deleteCharAt((int) cursorText.x - 1);
                    lines.set(lineIndex, builder.toString());

                    cursorText.x -= 1;
                    updateCursorPosition();
                    handleEditorChanged();
                } else if (lineIndex > 0) {
                    String previousLine = lines.get(lineIndex - 1);
                    cursorText.x = previousLine.length();
                    cursorText.y -= 1;
                    lines.set(lineIndex - 1, previousLine + line);
                    lines.remove(lineIndex);
                    updateCursorPosition();
                    handleEditorChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void paste() {

    }

    private void copy() {

    }

    private void moveTop(boolean shift) {

    }

    private void moveRight(int amount, boolean shift, boolean ctrl) {
        if (cursorText != null) {
            int lineIndex = (int) cursorText.y;
            int columnIndex = (int) cursorText.x;

            while (amount > 0 && lineIndex < lines.size()) {
                if (ctrl) {
                    String line = lines.get(lineIndex);
                    if (columnIndex == line.length()) {
                        // Переход на следующую строку
                        if (lineIndex < lines.size() - 1) {
                            lineIndex++;
                            columnIndex = 0;
                        } else {
                            break;
                        }
                    } else {
                        // Пропускаем текущее слово
                        while (columnIndex < line.length() && Character.isLetterOrDigit(line.charAt(columnIndex))) {
                            columnIndex++;
                        }
                        // Пропускаем пробелы и знаки препинания
                        while (columnIndex < line.length() && !Character.isLetterOrDigit(line.charAt(columnIndex))) {
                            columnIndex++;
                        }
                        amount--;
                    }
                } else {
                    String line = lines.get(lineIndex);
                    if (columnIndex == line.length()) {
                        // Переход на следующую строку
                        if (lineIndex < lines.size() - 1) {
                            lineIndex++;
                            columnIndex = 0;
                        } else {
                            break;
                        }
                    } else {
                        columnIndex++;
                    }
                    amount--;
                }
            }

            cursorText = new ImVec2(columnIndex, lineIndex);
            updateCursorPosition();
        }
    }

    private void moveLeft(int amount, boolean shift, boolean ctrl) {
        if (cursorText != null) {
            int lineIndex = (int) cursorText.y;
            int columnIndex = (int) cursorText.x;

            if (ctrl) {
                // Перемещение по словам
                while (amount > 0 && (lineIndex > 0 || columnIndex > 0)) {
                    if (columnIndex == 0) {
                        // Переход на предыдущую строку
                        lineIndex--;
                        columnIndex = lines.get(lineIndex).length();
                    } else {
                        String line = lines.get(lineIndex);
                        // Пропускаем пробелы и знаки препинания слева
                        while (columnIndex > 0 && !Character.isLetterOrDigit(line.charAt(columnIndex - 1))) {
                            columnIndex--;
                        }
                        // Двигаемся до начала слова или строки
                        while (columnIndex > 0 && Character.isLetterOrDigit(line.charAt(columnIndex - 1))) {
                            columnIndex--;
                        }
                        amount--;
                    }
                }
            } else {
                // Обычное перемещение
                while (amount > 0 && (lineIndex > 0 || columnIndex > 0)) {
                    if (columnIndex == 0) {
                        // Переход на предыдущую строку
                        lineIndex--;
                        columnIndex = lines.get(lineIndex).length();
                    } else {
                        columnIndex--;
                    }
                    amount--;
                }
            }

            cursorText = new ImVec2(columnIndex, lineIndex);
            updateCursorPosition();
        }
    }

    private void moveDown(int amount, boolean shift) {
        if (cursorText != null) {
            int lineIndex = (int) cursorText.y;
            int columnIndex = (int) cursorText.x;

            lineIndex = Math.min(lineIndex + amount, lines.size() - 1);
            String newLine = lines.get(lineIndex);
            columnIndex = Math.min(columnIndex, newLine.length());

            cursorText = new ImVec2(columnIndex, lineIndex);
            updateCursorPosition();
        }
    }

    private void moveUp(int amount, boolean shift) {
        if (cursorText != null) {
            int lineIndex = (int) cursorText.y;
            int columnIndex = (int) cursorText.x;

            lineIndex = Math.max(lineIndex - amount, 0);
            String newLine = lines.get(lineIndex);
            columnIndex = Math.min(columnIndex, newLine.length());

            cursorText = new ImVec2(columnIndex, lineIndex);
            updateCursorPosition();
        }
    }

    private void redo() {

    }

    private void undo() {

    }

    private void updateCursorPosition() {
        if (cursorText != null) {
            int lineIndex = (int) cursorText.y;
            int columnIndex = (int) cursorText.x;

            if (lineIndex >= 0 && lineIndex < lines.size()) {
                String line = lines.get(lineIndex);
                if (columnIndex > line.length()) {
                    columnIndex = line.length();
                }
                float xPos = ImGui.calcTextSize(line.substring(0, columnIndex)).x;
                float yPos = lineIndex * getFontSize();

                cursor = new ImVec2(xPos, yPos);
            }
        }
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