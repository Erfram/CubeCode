package com.cubecode.client.imgui;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CubeImGui {
    public static void textMutable(MutableText mutableText) {
        for (int i = 0; i < mutableText.withoutStyle().size(); i++) {
            Text sibling = mutableText.withoutStyle().get(i);
            Style style = sibling.getStyle();
            TextColor textColor = style.getColor();
            String content = sibling.getString();

            if (textColor != null) {
                int color = textColor.getRgb();
                float r = ((color >> 16) & 0xFF) / 255.0f;
                float g = ((color >> 8) & 0xFF) / 255.0f;
                float b = (color & 0xFF) / 255.0f;
                float a = 1.0f;

                ImGui.textColored(r, g, b, a, content);
            } else {
                ImGui.text(content);
            }

            if (i < mutableText.withoutStyle().size() - 1) {
                ImGui.sameLine(0, 0);
            }
        }
    }

    public static void begin(View view, String title, Consumer<Boolean> beginAction) {
        String titleKey = title + view.getUniqueID();

        view.putVariable(titleKey, new ImBoolean(true));
        ImBoolean close = view.getVariable(titleKey);

        if (ImGui.begin(title, close)) {
            beginAction.accept(close.get());
        }

        ImGui.end();
    }

    public static void begin(View view, String title, int windowFlags, Consumer<Boolean> beginAction) {
        view.putVariable(title + view.getUniqueID(), new ImBoolean(true));
        ImBoolean close = view.getVariable(title + view.getUniqueID());

        if (ImGui.begin(title, close, windowFlags)) {
            beginAction.accept(close.get());
        }

        ImGui.end();
    }

    public static void frame(Runnable frameAction) {
        ImGui.newFrame();
        frameAction.run();
        ImGui.endFrame();
    }

    public static int frameCount() {
        return ImGui.getFrameCount();
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

    public static void menu(View view, String window, Consumer<Boolean> menuAction) {
        view.putVariable(window + view.getUniqueID(), true);
        boolean enabled = view.getVariable(window + view.getUniqueID());
        if (ImGui.beginMenu(window, enabled)) {
            menuAction.accept(enabled);
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

    /**
     * Displays a button.
     * @param title the button text
     */
    public static void button(String title, Runnable buttonAction) {
        if (ImGui.button(title)) {
            buttonAction.run();
        }
    }

    /**
     *
     * @param title the button text
     * @param width  the button width
     * @param height the button height
     */
    public static void button(String title, float width, float height, Runnable buttonAction) {
        if (ImGui.button(title, width, height)) {
            buttonAction.run();
        }
    }

    /**
     * Displays a button with FramePadding=(0,0) to easily embed within text.
     * @param title the button text
     */
    public static void smallButton(String title, Runnable smallButtonAction) {
        if (ImGui.smallButton(title)) {
            smallButtonAction.run();
        }
    }

    /**
     * A flexible button behavior without the visuals. Frequently useful to build custom behaviors using the public api.
     * @param id the button ID
     * @param width the button width
     * @param height the button height
     */
    public static void invisibleButton(String id, float width, float height, Runnable invisibleButtonAction) {
        if (ImGui.invisibleButton(id, width, height)) {
            invisibleButtonAction.run();
        }
    }

    /**
     * A flexible button behavior without the visuals. Frequently useful to build custom behaviors using the public api.
     * @param id the button ID
     * @param width the button width
     * @param height the button height
     */
    public static void invisibleButton(String id, float width, float height, int buttonFlags, Runnable invisibleButtonAction) {
        if (ImGui.invisibleButton(id, width, height, buttonFlags)) {
            invisibleButtonAction.run();
        }
    }

    /**
     * Displays a square button with an arrow shape.
     * @param id the button ID
     * @param dir the arrow direction {@link imgui.flag.ImGuiDir}
     * @param arrowButtonAction
     */
    public static void arrowButton(String id, int dir, Runnable arrowButtonAction) {
        if (ImGui.arrowButton(id, dir)) {
            arrowButtonAction.run();
        }
    }

    /**
     * Displays an image button.
     * @param textureId the image texture
     * @param width the image width
     * @param height the image height
     */
    public static void imageButton(int textureId, float width, float height, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, width, height)) {
            imageButtonAction.run();
        }
    }

    /**
     * Displays an image button.
     * @param textureId the image texture
     * @param width the image width
     * @param height the image height
     */
    public static void imageButton(int textureId, float width, float height, float uvX, float uvY, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, width, height, uvX, uvY)) {
            imageButtonAction.run();
        }
    }

    /**
     * Displays an image button.
     * @param textureId the image texture
     * @param width the image width
     * @param height the image height
     */
    public static void imageButton(int textureId, float width, float height, float uvX, float uvY, float uv1X, float uv1Y, int framePadding, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, width, height, uvX, uvY, uv1X, uv1Y, framePadding)) {
            imageButtonAction.run();
        }
    }

    /**
     * Displays an image button.
     * @param textureId the image texture
     * @param width the image width
     * @param height the image height
     */
    public static void imageButton(
            int textureId,
            float width, float height,
            float uvX, float uvY, float uv1X, float uv1Y,
            int framePadding, float bgColorR, float bgColorG, float bgColorB, float bgColorA, Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, width, height, uvX, uvY, uv1X, uv1Y, framePadding, bgColorR, bgColorG, bgColorB, bgColorA)) {
            imageButtonAction.run();
        }
    }

    /**
     * Displays an image button.
     * @param textureId the image texture
     * @param width the image width
     * @param height the image height
     */
    public static void imageButton(
            int textureId,
            float width, float height,
            float uvX, float uvY, float uv1X, float uv1Y,
            int framePadding, float bgColorR, float bgColorG, float bgColorB, float bgColorA,
            float tintR, float tintG, float tintB, float tintA,
            Runnable imageButtonAction) {
        if (ImGui.imageButton(textureId, width, height, uvX, uvY, uv1X, uv1Y, framePadding, bgColorR, bgColorG, bgColorB, bgColorA, tintR, tintG, tintB, tintA)) {
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

    /**
     * Creates one or several radio buttons.
     * @param label the radio button text
     */
    public static void radioButton(String label, boolean active, Runnable radioButtonAction) {
        if (ImGui.radioButton(label, active)) {
            radioButtonAction.run();
        }
    }

    /**
     * Creates one or several radio buttons.
     * @param label the radio button text
     */
    public static void radioButton(String label, int value, int button, Runnable radioButtonAction) {
        if (ImGui.radioButton(label, new ImInt(value), button)) {
            radioButtonAction.run();
        }
    }

    public static void beginChild(String id, Runnable beginChildAction) {
        if (ImGui.beginChild(id)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    public static void beginChild(String id, float width, float height, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    public static void beginChild(String id, float width, float height, boolean border, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    public static void beginChild(String id, float width, float height, boolean border, int imGuiWindowFlags, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border, imGuiWindowFlags)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    public static void beginChild(int id, Runnable beginChildAction) {
        if (ImGui.beginChild(id)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    public static void beginChild(int id, float width, float height, boolean border, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    public static void beginChild(int id, float width, float height, boolean border, int imGuiWindowFlags, Runnable beginChildAction) {
        if (ImGui.beginChild(id, width, height, border, imGuiWindowFlags)) {
            beginChildAction.run();
        }
        ImGui.endChild();
    }

    /**
     * Displays a checkbox.
     * @param label the checkbox text
     * @param active the checkbox status, true=checked, false=unchecked
     */
    public static void checkbox(String label, boolean active, Runnable checkboxAction) {
        if (ImGui.checkbox(label, active)) {
            checkboxAction.run();
        }
    }

    /**
     * Displays a progress bar.
     *
     * @param fraction the progress, between 0 and 1
     */
    public static void progressBar(float fraction) {
        ImGui.progressBar(fraction);
    }

    /**
     * Displays a progress bar.
     *
     * @param fraction the progress, between 0 and 1
     * @param sizeArgX the anchor x (or the progress bar width)
     * @param sizeArgY the anchor y (or the progress bar height)
     */
    public static void progressBar(float fraction, float sizeArgX, float sizeArgY) {
        ImGui.progressBar(fraction, sizeArgX, sizeArgY);
    }

    /**
     * Displays a progress bar.
     *
     * @param fraction the progress, between 0 and 1
     * @param sizeArgX the anchor x (or the progress bar width)
     * @param sizeArgY the anchor y (or the progress bar height)
     * @param overlay an overlaying string
     */
    public static void progressBar(float fraction, float sizeArgX, float sizeArgY, String overlay) {
        ImGui.progressBar(fraction, sizeArgX, sizeArgY, overlay);
    }

    /**
     * UI element representing a selectable drop-down list.
     */
    public static void combo(View view, String label, int currentItem, String[] items, Consumer<Integer> comboAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentItem));
        view.putVariable("items_" + label + view.getUniqueID(), items);
        ImInt variable = view.getVariable(label + view.getUniqueID());
        String[] list = view.getVariable("items_" + label + view.getUniqueID());
        if (ImGui.combo(label, variable, list)) {
            comboAction.accept(variable.get());
        }
    }

    /**
     * UI element representing a selectable drop-down list.
     */
    public static void combo(View view, String label, int currentItem, String[] items, int popupMaxHeightInItems, Consumer<Integer> comboAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentItem));
        ImInt variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.combo(label, variable, items, popupMaxHeightInItems)) {
            comboAction.accept(variable.get());
            ImGui.endCombo();
        }
    }

    /**
     * UI element representing a selectable drop-down list.
     */
    public static void combo(View view, String label, int currentItem, String itemsSeparatedByZeros, Consumer<Integer> comboAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentItem));
        ImInt variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.combo(label, variable, itemsSeparatedByZeros)) {
            comboAction.accept(variable.get());
            ImGui.endCombo();
        }
    }

    /**
     * UI element representing a selectable drop-down list.
     */
    public static void combo(View view, String label, int currentItem, String itemsSeparatedByZeros, int popupMaxHeightInItems, Consumer<Integer> comboAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentItem));
        ImInt variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.combo(label, variable, itemsSeparatedByZeros, popupMaxHeightInItems)) {
            comboAction.accept(variable.get());
            ImGui.endCombo();
        }
    }

    /**
     * Displays a float drag slider.
     *
     * type 1-4.
     * 1 - Imgui.dragFloat
     * 2 - Imgui.dragFloat2
     * 3 - Imgui.dragFloat3
     * 4 - Imgui.dragFloat4
     */
    public static <T> void dragFloat(View view, int type, String label, Consumer<Float> dragFloatAction) {
        view.putVariable(label + view.getUniqueID(), new float[type]);
        float[] variable = view.getVariable(label + view.getUniqueID());
        if (
                type == 1 ? ImGui.dragFloat(label, variable) :
                type == 2 ? ImGui.dragFloat2(label, variable) :
                type == 3 ? ImGui.dragFloat3(label, variable) :
                type == 4 && ImGui.dragFloat4(label, variable)) {
            dragFloatAction.accept(variable[0]);
        }
    }

    /**
     * Displays a float drag slider.
     *
     * type 1-4.
     * 1 - Imgui.dragFloat
     * 2 - Imgui.dragFloat2
     * 3 - Imgui.dragFloat3
     * 4 - Imgui.dragFloat4
     */
    public static void dragFloat(View view, int type, String label, float speed, Consumer<Float> dragFloatAction) {
        view.putVariable(label + view.getUniqueID(), new float[type]);
        float[] variable = view.getVariable(label + view.getUniqueID());
        if (
                type == 1 ? ImGui.dragFloat(label, variable, speed) :
                type == 2 ? ImGui.dragFloat2(label, variable, speed) :
                type == 3 ? ImGui.dragFloat3(label, variable, speed) :
                type == 4 && ImGui.dragFloat4(label, variable, speed)) {
            dragFloatAction.accept(variable[0]);
        }
    }

    /**
     * Displays a float drag slider.
     *
     * type 1-4.
     * 1 - Imgui.dragFloat
     * 2 - Imgui.dragFloat2
     * 3 - Imgui.dragFloat3
     * 4 - Imgui.dragFloat4
     */
    public static void dragFloat(View view, int type, String label, float currenValue, float speed, float min, float max, Consumer<Float> dragFloatAction) {
        float[] value = new float[type];
        value[0] = currenValue;
        view.putVariable(label + view.getUniqueID(), value);
        float[] variable = view.getVariable(label + view.getUniqueID());
        if (
                type == 1 ? ImGui.dragFloat(label, variable, speed, min, max) :
                        type == 2 ? ImGui.dragFloat2(label, variable, speed, min, max) :
                                type == 3 ? ImGui.dragFloat3(label, variable, speed, min, max) :
                                        type == 4 && ImGui.dragFloat4(label, variable, speed, min, max)) {
            dragFloatAction.accept(variable[type]);
        }
    }

    /**
     * Displays a float drag slider.
     *
     * type 1-4.
     * 1 - Imgui.dragFloat
     * 2 - Imgui.dragFloat2
     * 3 - Imgui.dragFloat3
     * 4 - Imgui.dragFloat4
     */
    public static void dragFloat(View view, int type, String label, float speed, float min, float max, Consumer<Float> dragFloatAction) {
        view.putVariable(label + view.getUniqueID(), new float[type]);
        float[] variable = view.getVariable(label + view.getUniqueID());
        if (
                type == 1 ? ImGui.dragFloat(label, variable, speed, min, max) :
                type == 2 ? ImGui.dragFloat2(label, variable, speed, min, max) :
                type == 3 ? ImGui.dragFloat3(label, variable, speed, min, max) :
                type == 4 && ImGui.dragFloat4(label, variable, speed, min, max)) {
            dragFloatAction.accept(variable[type]);
        }
    }

    /**
     * Displays a float drag slider.
     *
     * type 1-4.
     * 1 - Imgui.dragFloat
     * 2 - Imgui.dragFloat2
     * 3 - Imgui.dragFloat3
     * 4 - Imgui.dragFloat4
     */
    public static void dragFloat(View view, int type, String label, float speed, float min, float max, String format, Consumer<Float> dragFloatAction) {
        view.putVariable(label + view.getUniqueID(), new float[type]);
        float[] variable = view.getVariable(label + view.getUniqueID());
        if (
                type == 1 ? ImGui.dragFloat(label, variable, speed, min, max, format) :
                type == 2 ? ImGui.dragFloat2(label, variable, speed, min, max, format) :
                type == 3 ? ImGui.dragFloat3(label, variable, speed, min, max, format) :
                type == 4 && ImGui.dragFloat4(label, variable, speed, min, max, format)) {
            dragFloatAction.accept(variable[0]);
        }
    }

    /**
     * Displays a float drag slider.
     *
     * type 1-4.
     * 1 - Imgui.dragFloat
     * 2 - Imgui.dragFloat2
     * 3 - Imgui.dragFloat3
     * 4 - Imgui.dragFloat4
     */
    public static void dragFloat(View view, int type, String label, float speed, float min, float max, String format, int imGuiSliderFlags, Consumer<Float> dragFloatAction) {
        view.putVariable(label + view.getUniqueID(), new float[type]);
        float[] variable = view.getVariable(label + view.getUniqueID());
        if (
                type == 1 ? ImGui.dragFloat(label, variable, speed, min, max, format, imGuiSliderFlags) :
                type == 2 ? ImGui.dragFloat2(label, variable, speed, min, max, format, imGuiSliderFlags) :
                type == 3 ? ImGui.dragFloat3(label, variable, speed, min, max, format, imGuiSliderFlags) :
                type == 4 && ImGui.dragFloat4(label, variable, speed, min, max, format, imGuiSliderFlags)) {
            dragFloatAction.accept(variable[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float initialDrag1, float initialDrag2, BiConsumer<Float, Float> dragFloatRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        float[] oneDragValue = {initialDrag1};
        float[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        float[] fMin = view.getVariable(oneKey);
        float[] fMax = view.getVariable(twoKey);

        if (ImGui.dragFloatRange2(label, fMin, fMax)) {
            dragFloatRange2Action.accept(fMin[0], fMax[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, BiConsumer<Float, Float> dragFloatRange2Action) {
        dragFloatRange(view, label, 0, 0, dragFloatRange2Action);
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float initialDrag1, float initialDrag2, float speed, BiConsumer<Float, Float> dragFloatRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        float[] oneDragValue = {initialDrag1};
        float[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        float[] fMin = view.getVariable(oneKey);
        float[] fMax = view.getVariable(twoKey);

        if (ImGui.dragFloatRange2(label, fMin, fMax, speed)) {
            dragFloatRange2Action.accept(fMin[0], fMax[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float speed, BiConsumer<Float, Float> dragFloatRange2Action) {
        dragFloatRange(view, label, 0, 0, speed, dragFloatRange2Action);
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float initialDrag1, float initialDrag2, float speed, float minDrag, BiConsumer<Float, Float> dragFloatRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        float[] oneDragValue = {initialDrag1};
        float[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        float[] fMin = view.getVariable(oneKey);
        float[] fMax = view.getVariable(twoKey);

        if (ImGui.dragFloatRange2(label, fMin, fMax, speed, minDrag)) {
            dragFloatRange2Action.accept(fMin[0], fMax[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float initialDrag1, float initialDrag2, float speed, float minDrag, float maxDrag, BiConsumer<Float, Float> dragFloatRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        float[] oneDragValue = {initialDrag1};
        float[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        float[] fMin = view.getVariable(oneKey);
        float[] fMax = view.getVariable(twoKey);

        if (ImGui.dragFloatRange2(label, fMin, fMax, speed, minDrag, maxDrag)) {
            dragFloatRange2Action.accept(fMin[0], fMax[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float initialDrag1, float initialDrag2, float speed, float minDrag, float maxDrag, String format, BiConsumer<Float, Float> dragFloatRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        float[] oneDragValue = {initialDrag1};
        float[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        float[] fMin = view.getVariable(oneKey);
        float[] fMax = view.getVariable(twoKey);

        if (ImGui.dragFloatRange2(label, fMin, fMax, speed, minDrag, maxDrag, format)) {
            dragFloatRange2Action.accept(fMin[0], fMax[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float speed, float minDrag, float maxDrag, String format, BiConsumer<Float, Float> dragFloatRange2Action) {
        dragFloatRange(view, label, 0, 0, speed, minDrag, maxDrag, format, dragFloatRange2Action);
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float initialDrag1, float initialDrag2, float speed, float minDrag, float maxDrag, String format, String formatMax, BiConsumer<Float, Float> dragFloatRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        float[] oneDragValue = {initialDrag1};
        float[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        float[] fMin = view.getVariable(oneKey);
        float[] fMax = view.getVariable(twoKey);

        if (ImGui.dragFloatRange2(label, fMin, fMax, speed, minDrag, maxDrag, format, formatMax)) {
            dragFloatRange2Action.accept(fMin[0], fMax[0]);
        }
    }

    /**
     * Displays 2 float drag range sliders, side by side.
     *
     * ImGui - dragFloatRange2
     */
    public static void dragFloatRange(View view, String label, float speed, float minDrag, float maxDrag, String format, String formatMax, BiConsumer<Float, Float> dragFloatRange2Action) {
        dragFloatRange(view, label, 0, 0, speed, minDrag, maxDrag, format, formatMax, dragFloatRange2Action);
    }

    /**
     * Displays an int drag slider.
     */
    public static void dragInt(View view, int type, String label, Consumer<Integer> dragIntAction) {
        String labelkey = label + view.getUniqueID();
        view.putVariable(labelkey, new int[1]);
        
        int[] variable = view.getVariable(labelkey);
        if (
                type == 1 ? ImGui.dragInt(label, variable) :
                type == 2 ? ImGui.dragInt2(label, variable) :
                type == 3 ? ImGui.dragInt3(label, variable) :
                type == 4 && ImGui.dragInt4(label, variable)
        ) {
            dragIntAction.accept(variable[0]);
        }
    }

    /**
     * Displays an int drag slider.
     */
    public static void dragInt(View view, int type, String label, float speed, Consumer<Integer> dragIntAction) {
        String labelkey = label + view.getUniqueID();
        view.putVariable(labelkey, new int[1]);

        int[] variable = view.getVariable(labelkey);
        if (
                type == 1 ? ImGui.dragInt(label, variable, speed) :
                type == 2 ? ImGui.dragInt2(label, variable, speed) :
                type == 3 ? ImGui.dragInt3(label, variable, speed) :
                type == 4 && ImGui.dragInt4(label, variable, speed)
        ) {
            dragIntAction.accept(variable[0]);
        }
    }

    /**
     * Displays an int drag slider.
     */
    public static void dragInt(View view, int type, String label, float speed, float min, Consumer<Integer> dragIntAction) {
        String labelkey = label + view.getUniqueID();
        view.putVariable(labelkey, new int[1]);

        int[] variable = view.getVariable(labelkey);
        if (
                type == 1 ? ImGui.dragInt(label, variable, speed, min) :
                type == 2 ? ImGui.dragInt2(label, variable, speed, min) :
                type == 3 ? ImGui.dragInt3(label, variable, speed, min) :
                type == 4 && ImGui.dragInt4(label, variable, speed, min)
        ) {
            dragIntAction.accept(variable[0]);
        }
    }

    /**
     * Displays an int drag slider.
     */
    public static void dragInt(View view, int type, String label, float speed, float min, float max, Consumer<Integer> dragIntAction) {
        String labelkey = label + view.getUniqueID();
        view.putVariable(labelkey, new int[type]);

        int[] variable = view.getVariable(labelkey);
        if (
                type == 1 ? ImGui.dragInt(label, variable, speed, min, max) :
                type == 2 ? ImGui.dragInt2(label, variable, speed, min, max) :
                type == 3 ? ImGui.dragInt3(label, variable, speed, min, max) :
                type == 4 && ImGui.dragInt4(label, variable, speed, min, max)
        ) {
            dragIntAction.accept(variable[0]);
        }
    }

    /**
     * Displays an int drag slider.
     */
    public static void dragInt(View view, int type, String label, float speed, float min, float max, String format, Consumer<Integer> dragIntAction) {
        String labelkey = label + view.getUniqueID();
        view.putVariable(labelkey, new int[1]);

        int[] variable = view.getVariable(labelkey);
        if (
                type == 1 ? ImGui.dragInt(label, variable, speed, min, max, format) :
                type == 2 ? ImGui.dragInt2(label, variable, speed, min, max, format) :
                type == 3 ? ImGui.dragInt3(label, variable, speed, min, max, format) :
                type == 4 && ImGui.dragInt4(label, variable, speed, min, max, format)
        ) {
            dragIntAction.accept(variable[0]);
        }
    }

    public static void image(int textureID, float width, float height) {
        ImGui.image(textureID, width, height);
    }

    public static void image(int textureID, float width, float height, float uv0X, float uv0Y) {
        ImGui.image(textureID, width, height, uv0X, uv0Y);
    }

    public static void image(int textureID, float width, float height, float uv0X, float uv0Y, float uv1X, float uv1Y) {
        ImGui.image(textureID, width, height, uv0X, uv0Y, uv1X, uv1Y);
    }

    public static void image(int textureID, float width, float height, float uv0X, float uv0Y, float uv1X, float uv1Y, float tintColorR, float tintColorG, float tintColorB, float tintColorA) {
        ImGui.image(textureID, width, height, uv0X, uv0Y, uv1X, uv1Y, tintColorR, tintColorG, tintColorB, tintColorA);
    }

    public static void image(int textureID, float width, float height, float uv0X, float uv0Y, float uv1X, float uv1Y, float tintColorR, float tintColorG, float tintColorB, float tintColorA, float borderR, float borderG, float borderB, float borderA) {
        ImGui.image(textureID, width, height, uv0X, uv0Y, uv1X, uv1Y, tintColorR, tintColorG, tintColorB, tintColorA, borderR, borderG, borderB, borderA);
    }

    /**
     * Draw a small circle + keep the cursor on the same line. advance cursor x position by GetTreeNodeToLabelSpacing(), same distance that TreeNode() uses
     */
    public static void bullet() {
        ImGui.bullet();
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int initialDrag1, int initialDrag2, BiConsumer<Integer, Integer> dragIntRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        int[] oneDragValue = {initialDrag1};
        int[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        int[] iMin = view.getVariable(oneKey);
        int[] iMax = view.getVariable(twoKey);

        if (ImGui.dragIntRange2(label, iMin, iMax)) {
            dragIntRange2Action.accept(iMin[0], iMax[0]);
        }
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, BiConsumer<Integer, Integer> dragIntRange2Action) {
        dragIntRange(view, label, 0, 0, dragIntRange2Action);
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int initialDrag1, int initialDrag2, int speed, BiConsumer<Integer, Integer> dragIntRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        int[] oneDragValue = {initialDrag1};
        int[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        int[] iMin = view.getVariable(oneKey);
        int[] iMax = view.getVariable(twoKey);

        if (ImGui.dragIntRange2(label, iMin, iMax, speed)) {
            dragIntRange2Action.accept(iMin[0], iMax[0]);
        }
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int speed, BiConsumer<Integer, Integer> dragIntRange2Action) {
        dragIntRange(view, label, 0, 0, speed, dragIntRange2Action);
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int initialDrag1, int initialDrag2, int speed, int minDrag, BiConsumer<Integer, Integer> dragIntRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        int[] oneDragValue = {initialDrag1};
        int[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        int[] iMin = view.getVariable(oneKey);
        int[] iMax = view.getVariable(twoKey);

        if (ImGui.dragIntRange2(label, iMin, iMax, speed, minDrag)) {
            dragIntRange2Action.accept(iMin[0], iMax[0]);
        }
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int initialDrag1, int initialDrag2, int speed, int minDrag, int maxDrag, BiConsumer<Integer, Integer> dragIntRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        int[] oneDragValue = {initialDrag1};
        int[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        int[] iMin = view.getVariable(oneKey);
        int[] iMax = view.getVariable(twoKey);

        if (ImGui.dragIntRange2(label, iMin, iMax, speed, minDrag, maxDrag)) {
            dragIntRange2Action.accept(iMin[0], iMax[0]);
        }
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int initialDrag1, int initialDrag2, int speed, int minDrag, int maxDrag, String format, BiConsumer<Integer, Integer> dragIntRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        int[] oneDragValue = {initialDrag1};
        int[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        int[] iMin = view.getVariable(oneKey);
        int[] iMax = view.getVariable(twoKey);

        if (ImGui.dragIntRange2(label, iMin, iMax, speed, minDrag, maxDrag, format)) {
            dragIntRange2Action.accept(iMin[0], iMax[0]);
        }
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int speed, int minDrag, int maxDrag, String format, BiConsumer<Integer, Integer> dragIntRange2Action) {
        dragIntRange(view, label, 0, 0, speed, minDrag, maxDrag, format, dragIntRange2Action);
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int initialDrag1, int initialDrag2, int speed, int minDrag, int maxDrag, String format, String formatMax, BiConsumer<Integer, Integer> dragIntRange2Action) {
        String oneKey = "one_" + view.getUniqueID();
        String twoKey = "two_" + view.getUniqueID();

        int[] oneDragValue = {initialDrag1};
        int[] twoDragValue = {initialDrag2};

        view.putVariable(oneKey, oneDragValue);
        view.putVariable(twoKey, twoDragValue);

        int[] iMin = view.getVariable(oneKey);
        int[] iMax = view.getVariable(twoKey);

        if (ImGui.dragIntRange2(label, iMin, iMax, speed, minDrag, maxDrag, format, formatMax)) {
            dragIntRange2Action.accept(iMin[0], iMax[0]);
        }
    }

    /**
     * Displays 2 int drag range sliders, side by side.
     *
     * ImGui - dragIntRange2
     */
    public static void dragIntRange(View view, String label, int speed, int minDrag, int maxDrag, String format, String formatMax, BiConsumer<Integer, Integer> dragIntRange2Action) {
        dragIntRange(view, label, 0, 0, speed, minDrag, maxDrag, format, formatMax, dragIntRange2Action);
    }

    public static void dragScalar(View view, String label, int currentValue, int dataType, int speed, Consumer<Integer> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentValue));

        ImInt variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, int currentValue, int dataType, int speed, int min, Consumer<Integer> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentValue));

        ImInt variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed, min)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, int currentValue, int dataType, int speed, int min, int max, Consumer<Integer> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImInt(currentValue));

        ImInt variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed, min, max)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, float currentValue, int dataType, float speed, Consumer<Float> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImFloat(currentValue));

        ImFloat variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, float currentValue, int dataType, float speed, float min, Consumer<Float> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImFloat(currentValue));

        ImFloat variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed, min)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, Float currentValue, int dataType, float speed, float min, float max, Consumer<Float> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImFloat(currentValue));

        ImFloat variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed, min, max)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, float currentValue, int dataType, float speed, float min, float max, String format, Consumer<Float> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImFloat(currentValue));

        ImFloat variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed, min, max, format)) {
            dragScalarAction.accept(variable.get());
        }
    }

    public static void dragScalar(View view, String label, float currentValue, int dataType, float speed, float min, float max, String format, int imGuiSliderFlags, Consumer<Float> dragScalarAction) {
        view.putVariable(label + view.getUniqueID(), new ImFloat(currentValue));

        ImFloat variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.dragScalar(label, dataType, variable, speed, min, max, format, imGuiSliderFlags)) {
            dragScalarAction.accept(variable.get());
        }
    }

    /**
     * maxLength - 30
     */
    public static ImString inputText(View view, String label, Consumer<String> inputTextAction) {
        view.putVariable(label + view.getUniqueID(), new ImString(30));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static ImString inputText(View view, String label, Consumer<String> inputTextAction, int imGuiInputTextFlags) {
        view.putVariable(label + view.getUniqueID(), new ImString(30));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable, imGuiInputTextFlags)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static ImString inputText(View view, String label, int maxLength, Consumer<String> inputTextAction) {
        view.putVariable(label + view.getUniqueID(), new ImString(maxLength));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static ImString inputText(View view, String label, String text, Consumer<String> inputTextAction) {
        view.putVariable(label + view.getUniqueID(), new ImString(text));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static ImString inputText(View view, String label, int maxLength, String text, Consumer<String> inputTextAction) {
        view.putVariable(label + view.getUniqueID(), new ImString(text, maxLength));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static ImString inputText(View view, String label, String text, Consumer<String> inputTextAction, int imGuiInputTextFlags) {
        view.putVariable(label + view.getUniqueID(), new ImString(text));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable, imGuiInputTextFlags)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static ImString inputText(View view, String label, String text, int maxLength, Consumer<String> inputTextAction, int imGuiInputTextFlags) {
        view.putVariable(label + view.getUniqueID(), new ImString(text, maxLength));

        ImString variable = view.getVariable(label + view.getUniqueID());
        if (ImGui.inputText(label, variable, imGuiInputTextFlags)) {
            inputTextAction.accept(variable.get());
        }

        return variable;
    }

    public static void withItemWidth(float width, Runnable withItemWidthAction) {
        ImGui.pushItemWidth(width);
        try {
            withItemWidthAction.run();
        } finally {
            ImGui.popItemWidth();
        }
    }

    /**
     * available variables:
     * "width_" + uniqueId
     * "height_" + uniqueId
     */
    public static void manageDocking(View view) {
        boolean isDocked = ImGui.isWindowDocked();

        view.putVariable(view.getUniqueID().toString(), false);
        view.putVariable("width_"+view.getUniqueID().toString(), 0F);
        view.putVariable("height_"+view.getUniqueID().toString(), 0F);

        boolean wasDocked = view.getVariable(view.getUniqueID().toString());

        float undockedWidth = view.getVariable("width_"+view.getUniqueID().toString());
        float undockedHeight = view.getVariable("height_"+view.getUniqueID().toString());

        if (wasDocked && !isDocked) {
            ImGui.setWindowSize(undockedWidth, undockedHeight);
        }

        if (!isDocked) {
            view.setVariable("width_"+view.getUniqueID().toString(), ImGui.getWindowWidth());
            view.setVariable("height_"+view.getUniqueID().toString(), ImGui.getWindowHeight());
        }

        view.setVariable(view.getUniqueID().toString(), isDocked);
    }
}