package com.cubecode.client.imgui;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CubeImGui {
    public static <T> void putVariable(View view, String name, T value) {
        view.getVariables().put(name, value);
    }

    public static void removeVariable(View view, String name) {
        view.getVariables().remove(name);
    }

    public static <T> T getVariable(View view, String name) {
        return (T) view.getVariables().get(name);
    }

    public static void begin(String title, Runnable beginAction) {
        if (ImGui.begin(title)) {
            beginAction.run();
            ImGui.end();
        }
    }

    public static void begin(String title, int windowFlags, Runnable beginAction) {
        if (ImGui.begin(title, windowFlags)) {
            beginAction.run();
            ImGui.end();
        }
    }

    public static void begin(String title, boolean flag, Runnable beginAction) {
        if (ImGui.begin(title, new ImBoolean(flag))) {
            beginAction.run();
            ImGui.end();
        }
    }

    public static void begin(String title, boolean flag, int windowFlags, Runnable beginAction) {
        if (ImGui.begin(title, new ImBoolean(flag), windowFlags)) {
            beginAction.run();
            ImGui.end();
        }
    }

    public static void dragDropSource(Runnable dragDropSourceAction) {
        if (ImGui.beginDragDropSource()) {
            dragDropSourceAction.run();
            ImGui.endDragDropSource();
        }
    }

    public static void dragDropSource(int dragDropFlags, Runnable dragDropSourceAction) {
        if (ImGui.beginDragDropSource(dragDropFlags)) {
            dragDropSourceAction.run();
            ImGui.endDragDropSource();
        }
    }

    public static void dragDropTarget(Runnable dragDropTargetAction) {
        if (ImGui.beginDragDropTarget()) {
            dragDropTargetAction.run();
            ImGui.endDragDropSource();
        }
    }

    public static void mainMenuBar(Runnable mainMenuBarAction) {
        if (ImGui.beginMainMenuBar()) {
            mainMenuBarAction.run();
            ImGui.endMainMenuBar();
        }
    }

    public static void menu(String window, Runnable menuAction) {
        if (ImGui.beginMenu(window)) {
            menuAction.run();
            ImGui.endMenu();
        }
    }

    public static void mouseClicked(int button, BiConsumer<Float, Float> mouseClickedAction) {
        if (ImGui.isMouseClicked(button)) {
            mouseClickedAction.accept(ImGui.getMousePos().x, ImGui.getMousePos().y);
        }
    }

    public static void popup(String id, Runnable popupAction) {
        if (ImGui.beginPopup(id)) {
            popupAction.run();
            ImGui.endPopup();
        }
    }

    public static void menuItem(String id, Runnable menuItemAction) {
        if (ImGui.menuItem(id)) {
            menuItemAction.run();
        }
    }

    public static void button(String title, Runnable buttonAction) {
        if (ImGui.button(title)) {
            buttonAction.run();
        }
    }

    public static void button(String title, float width, float height, Runnable buttonAction) {
        if (ImGui.button(title, width, height)) {
            buttonAction.run();
        }
    }

    public static void smallButton(String title, Runnable smallButtonAction) {
        if (ImGui.smallButton(title)) {
            smallButtonAction.run();
        }
    }

    public static void invisibleButton(String title, float width, float height, Runnable invisibleButtonAction) {
        if (ImGui.invisibleButton(title, width, height)) {
            invisibleButtonAction.run();
        }
    }

    public static void invisibleButton(String title, float width, float height, int buttonFlags, Runnable invisibleButtonAction) {
        if (ImGui.invisibleButton(title, width, height, buttonFlags)) {
            invisibleButtonAction.run();
        }
    }

    public static void arrowButton(String title, int dir, Runnable arrowButtonAction) {
        if (ImGui.arrowButton(title, dir)) {
            arrowButtonAction.run();
        }
    }

    public static void imageButton(int textureId, float sizeX, float sizeY, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, sizeX, sizeY)) {
            imageButtonAction.run();
        }
    }

    public static void imageButton(int textureId, float sizeX, float sizeY, float uvX, float uvY, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, sizeX, sizeY, uvX, uvY)) {
            imageButtonAction.run();
        }
    }

    public static void imageButton(int textureId, float sizeX, float sizeY, float uvX, float uvY, float uv1X, float uv1Y, int framePadding, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, sizeX, sizeY, uvX, uvY, uv1X, uv1Y, framePadding)) {
            imageButtonAction.run();
        }
    }

    public static void imageButton(
            int textureId,
            float sizeX, float sizeY,
            float uvX, float uvY, float uv1X, float uv1Y,
            int framePadding, float bgColorR, float bgColorG, float bgColorB, float bgColorA, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, sizeX, sizeY, uvX, uvY, uv1X, uv1Y, framePadding, bgColorR, bgColorG, bgColorB, bgColorA)) {
            imageButtonAction.run();
        }
    }

    public static void imageButton(
            int textureId,
            float sizeX, float sizeY,
            float uvX, float uvY, float uv1X, float uv1Y,
            int framePadding, float bgColorR, float bgColorG, float bgColorB, float bgColorA,
            float tintR, float tintG, float tintB, float tintA,
            Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, sizeX, sizeY, uvX, uvY, uv1X, uv1Y, framePadding, bgColorR, bgColorG, bgColorB, bgColorA, tintR, tintG, tintB, tintA)) {
            imageButtonAction.run();
        }
    }

    public static void display(String id, int fileDialogFlags, float minSizeX, float minSizeY, float maxSizeX, float maxSizeY, Runnable displayAction) {
        if (ImGuiFileDialog.display(id, fileDialogFlags, minSizeX, minSizeY, maxSizeX, maxSizeY)) {
            if (ImGuiFileDialog.isOk()) {
                displayAction.run();
            }
            ImGuiFileDialog.close();
        }
    }

    public static void keyPressed(int glfwKey, Runnable keyPressedAction) {
        if (ImGui.isKeyPressed(glfwKey)) {
            keyPressedAction.run();
        }
    }

    public static void keyPressed(Runnable keyPressedAction, int... glfwKeys) {
        for (int glfwKey : glfwKeys) {
            if (ImGui.isKeyPressed(glfwKey)) {
                keyPressedAction.run();
            }
        }
    }

    public static void radioButton(String label, boolean active, Runnable radioButtonAction) {
        if (ImGui.radioButton(label, active)) {
            radioButtonAction.run();
        }
    }

    public static void beginChild(String id, Runnable beginChildAction) {
        if (ImGui.beginChild(id)) {
            beginChildAction.run();
        }
    }

    public static void beginChild(String id, float width, float height, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height)) {
            beginChildAction.run();
        }
    }

    public static void beginChild(String id, float width, float height, boolean border, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border)) {
            beginChildAction.run();
        }
    }

    public static void beginChild(String id, float width, float height, boolean border, int imGuiWindowFlags, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border, imGuiWindowFlags)) {
            beginChildAction.run();
        }
    }

    public static void beginChild(int id, Runnable beginChildAction) {
        if (ImGui.beginChild(id)) {
            beginChildAction.run();
        }
    }

    public static void beginChild(int id, float width, float height, boolean border, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border)) {
            beginChildAction.run();
        }
    }

    public static void beginChild(int id, float width, float height, boolean border, int imGuiWindowFlags, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border, imGuiWindowFlags)) {
            beginChildAction.run();
        }
    }

    public static void checkbox(String label, boolean active, Runnable checkboxAction) {
        if (ImGui.checkbox(label, active)) {
            checkboxAction.run();
        }
    }

    public static void progressBar(float fraction) {
        ImGui.progressBar(fraction);
    }

    public static void progressBar(float fraction, float sizeArgX, float sizeArgY) {
        ImGui.progressBar(fraction, sizeArgX, sizeArgY);
    }

    public static void progressBar(float fraction, float sizeArgX, float sizeArgY, String overlay) {
        ImGui.progressBar(fraction, sizeArgX, sizeArgY, overlay);
    }

    public static void beginCombo(String label, String previewValue, int imGuiComboFlags, Runnable beginComboAction) {
        if (ImGui.beginCombo(label, previewValue, imGuiComboFlags)) {
            beginComboAction.run();
            ImGui.endCombo();
        }
    }

    public static void beginCombo(String label, String previewValue, Runnable beginComboAction) {
        if (ImGui.beginCombo(label, previewValue)) {
            beginComboAction.run();
            ImGui.endCombo();
        }
    }

    public static void combo(String label, int currentItem, String[] items, Runnable comboAction) {
        if (ImGui.combo(label, new ImInt(currentItem), items)) {
            comboAction.run();
            ImGui.endCombo();
        }
    }

    public static void combo(String label, int currentItem, String[] items, int popupMaxHeightInItems, Runnable comboAction) {
        if (ImGui.combo(label, new ImInt(currentItem), items, popupMaxHeightInItems)) {
            comboAction.run();
            ImGui.endCombo();
        }
    }

    public static void combo(String label, ImInt currentItem, String itemsSeparatedByZeros, Runnable comboAction) {
        if (ImGui.combo(label, new ImInt(currentItem), itemsSeparatedByZeros)) {
            comboAction.run();
            ImGui.endCombo();
        }
    }

    public static void combo(String label, ImInt currentItem, String itemsSeparatedByZeros, int popupMaxHeightInItems, Runnable comboAction) {
        if (ImGui.combo(label, new ImInt(currentItem), itemsSeparatedByZeros, popupMaxHeightInItems)) {
            comboAction.run();
            ImGui.endCombo();
        }
    }

    public static void dragFloat(View view, String label, Runnable dragFloatAction) {
        putVariable(view, label, new float[0]);
        if (ImGui.dragFloat(label, getVariable(view, label))) {
            dragFloatAction.run();
            ImGui.endCombo();
        }
    }

    public static void dragFloat(String label, float[] variable, Runnable dragFloatAction) {
        if (ImGui.dragFloat(label, variable)) {
            dragFloatAction.run();
            ImGui.endCombo();
        }
    }

    public static void dragFloat(String label, float[] v, float speed, Runnable dragFloatAction) {
        if (ImGui.dragFloat(label, v, speed)) {
            dragFloatAction.run();
            ImGui.endCombo();
        }
    }
}