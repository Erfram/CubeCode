package com.cubecode.client.imgui;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.basic.View;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.ImVec2;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiMouseCursor;
import imgui.type.ImString;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CubeImGui {
    View view;

    private final Map<String, Integer> pendingPopups = new HashMap<>();
    private final Map<String, Consumer<CubeImGui>> popups = new HashMap<>();

    public CubeImGui(View view) {
        this.view = view;
    }

    public void addPopup(String name, Consumer<CubeImGui> popup) {
        this.popups.put(name, popup);
    }

    public void initPopups() {
        for (Consumer<CubeImGui> popup : this.popups.values()) {
            popup.accept(this);
        }
    }

    public void openPopup(String name) {
        pendingPopups.merge(name, 1, Integer::sum);
    }

    public void popup(String name, Runnable render) {
        if (pendingPopups.containsKey(name)) {
            ImGui.openPopup(name);
            pendingPopups.remove(name);
        }

        if (ImGui.beginPopup(name)) {
            render.run();
            ImGui.endPopup();
        }
    }

    public void modal(String name, Runnable render) {
        this.modal(name, 0, render);
    }

    public void modal(String name, int imguiWindowFlags, Runnable render) {
        if (pendingPopups.containsKey(name)) {
            ImGui.openPopup(name);
            pendingPopups.remove(name);
        }

        if (ImGui.beginPopupModal(name, imguiWindowFlags)) {
            render.run();
            ImGui.endPopup();
        }
    }

    public static void mainMenuBar(Runnable runnable) {
        ImGui.beginMainMenuBar();
        runnable.run();
        ImGui.endMainMenuBar();
    }

    public static void menu(String label, Runnable runnable) {
        if (ImGui.beginMenu(label)) {
            runnable.run();
            ImGui.endMenu();
        }
    }

    public static void menuItem(String label, Runnable runnable) {
        if (ImGui.menuItem(label)) {
            runnable.run();
        }
    }

    public static void child(String id, Runnable runnable) {
        if (ImGui.beginChild(id)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public static void child(String id, float width, float height, Runnable runnable) {
        if (ImGui.beginChild(id, width, height)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public static void child(String id, float width, float height, boolean border, Runnable runnable) {
        if (ImGui.beginChild(id, width, height, border)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public static void child(String id, float width, float height, boolean border, int imguiChildFlags, Runnable runnable) {
        if (ImGui.beginChild(id, width, height, border, imguiChildFlags)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public void dragFloat(String label, float defaultValue) {
        this.view.putVariable(label, new float[]{defaultValue});
        ImGui.dragFloat(label, this.view.getVariable(label), 0.1f, 0.0f, 1.0f, "%.2f");
    }

    public void dragInt(String label, int defaultValue) {
        this.view.putVariable(label, new int[]{defaultValue});
        ImGui.dragInt(label, this.view.getVariable(label), 1, 1, 4);
    }

    public void sliderInt(String label, int defaultValue) {
        this.view.putVariable(label, new int[]{defaultValue});
        ImGui.sliderInt(label, this.view.getVariable(label), 1, 4);
    }

    public void sliderInt(String label, int defaultValue, Consumer<Integer> callback) {
        this.view.putVariable(label, new int[]{defaultValue});
        int[] variable = this.view.getVariable(label);
        if (ImGui.sliderInt(label, variable, 1, 4)) {
            callback.accept(variable[0]);
        }
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * cig.splitter("splitter", 4, availableWidth, ImGui.getItemRectMaxY(), 0.3f, 0.1f, 0.9f)
     * }</pre>
     */
    public void splitter(String splitterId, float splitterWidth, float availableWidth, float availableHeight, float defaultRatio, float minSplitRatio, float maxSplitRatio) {
        this.view.putVariable(splitterId, defaultRatio);

        ImGui.invisibleButton(splitterId, splitterWidth, availableHeight);

        ImGui.getWindowDrawList().addRect(
                ImGui.getItemRectMin().x, ImGui.getItemRectMin().y,
                ImGui.getItemRectMax().x, availableHeight,
                ImGui.colorConvertFloat4ToU32(1, 1, 1, 1)
        );

        if (ImGui.isItemActive()) {
            float mouseDeltaX = ImGui.getIO().getMouseDeltaX();
            float newSplitRatio = ((float) this.view.getVariable(splitterId)) + mouseDeltaX / availableWidth;

            if (newSplitRatio >= minSplitRatio && newSplitRatio <= maxSplitRatio) {
                this.view.setVariable(splitterId, newSplitRatio);
            }
        }

        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.ResizeEW);
        }
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * cig.splitter("splitter", 4, availableWidth, ImGui.getItemRectMaxY())
     * }</pre>
     */
    public void splitter(String splitterId, float splitterWidth, float availableWidth, float availableHeight) {
        this.splitter(splitterId, splitterWidth, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * cig.splitter("splitter", availableWidth, ImGui.getItemRectMaxY())
     * }</pre>
     */
    public void splitter(String splitterId, float availableWidth, float availableHeight) {
        this.splitter(splitterId, 4, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    /**
     * maxLength - 30
     */
    public ImString inputText(String label, String id, Consumer<ImString> inputTextAction) {
        view.putVariable(id, new ImString(30));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputTextWithHint(String label, String id, String hint, Consumer<ImString> inputTextAction) {
        view.putVariable(id, new ImString(30));

        ImString variable = view.getVariable(id);

        if (ImGui.inputTextWithHint(label, hint, variable, 0)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputText(String label, String id, Consumer<ImString> inputTextAction, int imGuiInputTextFlags) {
        view.putVariable(id, new ImString(30));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable, imGuiInputTextFlags)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputText(String label, String id, int maxLength, Consumer<ImString> inputTextAction) {
        view.putVariable(id, new ImString(maxLength));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputTextWithHint(String label, String id, String hint, int maxLength, Consumer<ImString> inputTextAction) {
        view.putVariable(id, new ImString(maxLength));

        ImString variable = view.getVariable(id);
        if (ImGui.inputTextWithHint(label, hint, variable)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputText(String label, String id, String text, Consumer<ImString> inputTextAction) {
        view.putVariable(id, new ImString(text));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputText(String label, String id, int maxLength, String text, Consumer<ImString> inputTextAction) {
        view.putVariable(id, new ImString(text, maxLength));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputText(String label, String id, String text, Consumer<ImString> inputTextAction, int imGuiInputTextFlags) {
        view.putVariable(id, new ImString(text));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable, imGuiInputTextFlags)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public ImString inputText(String label, String id, String text, int maxLength, Consumer<ImString> inputTextAction, int imGuiInputTextFlags) {
        view.putVariable(id, new ImString(text, maxLength));

        ImString variable = view.getVariable(id);
        if (ImGui.inputText(label, variable, imGuiInputTextFlags)) {
            inputTextAction.accept(variable);
        }

        return variable;
    }

    public void image(Icons icon, float width, float height) {
        this.image(icon, width, height, () -> {});
    }

    public void image(Icons icon, float width, float height, Runnable onClick) {
        ImGui.image(icon.getGlId(), width, height);

        if (ImGui.isItemClicked()) {
            onClick.run();
        }
    }

    public void image(Icons icon) {
        this.image(icon, this.getPixels(), this.getPixels());
    }

    public void image(Icons icon, Runnable onClick) {
        this.image(icon, this.getPixels(), this.getPixels(), onClick);
    }

    public void imageButton(Icons icon, float width, float height, Runnable onClick) {
        if (ImGui.imageButton(icon.getGlId(), width, height)) {
            onClick.run();
        }
    }

    public void imageButton(Icons icon, Runnable onClick) {
        if (ImGui.imageButton(icon.getGlId(), this.getPixels(), this.getPixels())) {
            onClick.run();
        }
    }

    public static void _image(Icons icon, float width, float height) {
        ImGui.image(icon.getGlId(), width, height);
    }

    public static void _image(Icons icon) {
        ImGui.image(icon.getGlId(), _getPixels(), _getPixels());
    }

    public static void _imageButton(Icons icon, float width, float height, Runnable onClick) {
        if (ImGui.imageButton(icon.getGlId(), width, height)) {
            onClick.run();
        }
    }

    public static void _imageButton(Icons icon, Runnable onClick) {
        if (ImGui.imageButton(icon.getGlId(), _getPixels(), _getPixels())) {
            onClick.run();
        }
    }

    public static void separatorText(String text) {
        separatorText(text, 0.5f);
    }

    public static void separatorText(String text, float ratio) {
        ratio = Math.max(0.0f, Math.min(1.0f, ratio));

        float fullWidth = ImGui.getContentRegionAvailX();
        float textWidth = ImGui.calcTextSize(text).x;
        float spacing = 8.0f;

        float availableWidth = fullWidth - textWidth - spacing * 2.0f;
        if (availableWidth < 0) availableWidth = 0;

        float leftWidth = availableWidth * ratio;
        float rightWidth = availableWidth * (1.0f - ratio);

        float cursorPosX = ImGui.getCursorScreenPosX();
        float cursorPosY = ImGui.getCursorScreenPosY() + ImGui.getTextLineHeight() / 2.0f;

        ImDrawList drawList = ImGui.getWindowDrawList();

        drawList.addLine(cursorPosX, cursorPosY, cursorPosX + leftWidth, cursorPosY, ImGui.getColorU32(ImGuiCol.Separator));

        float textPosX = cursorPosX + leftWidth + spacing;
        ImGui.setCursorScreenPos(textPosX, ImGui.getCursorScreenPosY());
        ImGui.text(text);

        float afterTextX = textPosX + textWidth + spacing;
        drawList.addLine(afterTextX, cursorPosY, afterTextX + rightWidth, cursorPosY, ImGui.getColorU32(ImGuiCol.Separator));

        ImGui.setCursorPosY(ImGui.getCursorPosY() + 5.0f);
    }

    public int getPixels() {
        Integer value = CubeCodeClient.getConfig().getViewScale();
        if (value == 0)
            value = 1;

        int pixels = 12;

        if (value == 2) {
            pixels = 16;
        } else if (value == 3) {
            pixels = 20;
        } else if (value == 4) {
            pixels = 24;
        }

        return pixels;
    }

    public static int _getPixels() {
        Integer value = CubeCodeClient.getConfig().getViewScale();
        if (value == 0)
            value = 1;

        int pixels = 12;

        if (value == 2) {
            pixels = 16;
        } else if (value == 3) {
            pixels = 20;
        } else if (value == 4) {
            pixels = 24;
        }

        return pixels;
    }
}
