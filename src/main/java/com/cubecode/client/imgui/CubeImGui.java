package com.cubecode.client.imgui;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.gifs.Gif;
import com.cubecode.client.gifs.GifManager;
import com.cubecode.client.imgui.basic.ImGuiFrameBuffer;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.screens.DashboardScreen;
import com.cubecode.utils.Icons;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.Optional;
import java.util.function.Consumer;

import static imgui.ImGui.*;

public class CubeImGui {
    public static void pushItemHeight(float height) {
        float frameHeight = height * 0.65f; // Основная высота без паддингов

        ImVec2 itemInnerSpacing = new ImVec2(ImGui.getStyle().getItemInnerSpacing().x, height * 0.1f);
        ImVec2 itemSpacing = new ImVec2(ImGui.getStyle().getItemSpacing().x, height * 0.1f);


        ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, itemInnerSpacing.x, itemInnerSpacing.y);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, itemSpacing.x, itemSpacing.y);
    }

    public static void popItemHeight() {
        ImGui.popStyleVar(2);
    }

    public static void gif(String gifPath, float width, float height) {
        gif(gifPath, width, height, 0, () -> {});
    }

    public static void gif(String gifPath, float width, float height, long velocityTick) {
        gif(gifPath, width, height, velocityTick, () -> {});
    }

    public static void gif(String gifPath, float width, float height, long velocityTick, Runnable callback) {
        Gif targetGif = null;
        Optional<Gif> optionalGif = GifManager.getGif(gifPath);

        if (optionalGif.isPresent()) {
            targetGif = optionalGif.get();
            targetGif.velocity(velocityTick);
        } else {
            Optional<Gif> defaultOptionalGif = GifManager.getGif(GifManager.DEFAULT_GIF);
            if (defaultOptionalGif.isPresent()) {
                targetGif = defaultOptionalGif.get();
            }
        }

        if (targetGif != null && targetGif.isAvailable()) {
            ImGui.invisibleButton("##invisibleButton", width, height);

            if (ImGui.isItemHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Left)) {
                callback.run();
            }

            ImVec2 pos = ImGui.getItemRectMin();

            ImDrawList windowDrawList = ImGui.getWindowDrawList();
            windowDrawList.addImage(targetGif.getGlId(), pos.x, pos.y, pos.x + width, pos.y + height, 0f, targetGif.getCursor(), 1, targetGif.getCursor() + targetGif.getDifference());
        }

    }

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

    public static void mutableText(MutableText mutableText) {
        boolean isFirstElement = true;
        for (int i = 0; i < mutableText.withoutStyle().size(); i++) {
            Text sibling = mutableText.withoutStyle().get(i);
            Style style = sibling.getStyle();
            TextColor textColor = style.getColor();
            String content = sibling.getString();

            String[] lines = content.split("\n", -1);
            for (int j = 0; j < lines.length; j++) {
                if (!isFirstElement && j == 0) {
                    ImGui.sameLine(0, 0);
                }

                if (textColor != null) {
                    int color = textColor.getRgb();
                    float r = ((color >> 16) & 0xFF) / 255.0f;
                    float g = ((color >> 8) & 0xFF) / 255.0f;
                    float b = (color & 0xFF) / 255.0f;
                    float a = 1.0f;

                    ImGui.textColored(r, g, b, a, lines[j]);
                } else {
                    ImGui.text(lines[j]);
                }

                if (j < lines.length - 1 || content.endsWith("\n")) {
                    isFirstElement = true;
                } else {
                    isFirstElement = false;
                }
            }
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

    public static void menuItem(String id, Runnable menuItemAction) {
        if (ImGui.menuItem(id)) {
            menuItemAction.run();
        }
    }

    public static void menuItem(String id, Icons icon, float sizeX, float sizeY, Runnable menuItemAction) {
        ImGui.image(icon.getGlId(), sizeX, sizeY);

        ImGui.sameLine();
        if (ImGui.menuItem(id)) {
            menuItemAction.run();
        }
    }

    public static void menuItemAndTooltip(String id, int r, int g, int b, int a, String text, Runnable menuItemAction) {
        if (ImGui.menuItem(id)) {
            menuItemAction.run();
        }

        ImGui.sameLine();

        ImGui.alignTextToFramePadding();

        ImGui.sameLine();

        ImGui.textColored(r, g, b, a, text);
    }

    public static void menuItemAndTooltip(String id, Icons icon, float sizeX, float sizeY, int r, int g, int b, int a, String text, Runnable menuItemAction) {
        ImGui.image(icon.getGlId(), sizeX, sizeY);

        ImGui.sameLine();

        menuItemAndTooltip(id, r, g, b, a, text, menuItemAction);
    }

    public static void menuItemAndTooltip(Text id, Icons icon, float sizeX, float sizeY, int r, int g, int b, int a, String text, Runnable menuItemAction) {
        ImGui.image(icon.getGlId(), sizeX, sizeY);

        ImGui.sameLine();

        menuItemAndTooltip(id.getString(), r, g, b, a, text, menuItemAction);
    }

    public static void menu(String id, Icons icon, float sizeX, float sizeY, Runnable menuItemAction) {
        ImGui.image(icon.getGlId(), sizeX, sizeY);

        ImGui.sameLine();

        if (ImGui.beginMenu(id)) {
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

    /**
     * available variables:
     * "width_" + uniqueId
     * "height_" + uniqueId
     */
    public static void manageDocking(View view) {
        boolean isDocked = ImGui.isWindowDocked();
        String uniqueID = view.getUniqueID().toString();
        String widthKey = "width_" + uniqueID;
        String heightKey = "height_" + uniqueID;
        String dockedKey = "docked_" + uniqueID;

        // Получаем текущее состояние и размеры окна
        boolean wasDocked = view.getVariable(dockedKey) != null ? view.getVariable(dockedKey) : false;
        float undockedWidth = view.getVariable(widthKey) != null ? view.getVariable(widthKey) : 0F;
        float undockedHeight = view.getVariable(heightKey) != null ? view.getVariable(heightKey) : 0F;

        // Если окно было закреплено и теперь откреплено, восстановить размеры и установить позицию окна к курсору
        if (wasDocked && !isDocked) {
            ImGui.setWindowSize(undockedWidth, undockedHeight);
            ImGui.setWindowPos(ImGui.getMousePosX(), getMousePosY());
        }

        // Если окно не закреплено, сохранить его текущие размеры
        if (!isDocked) {
            view.setVariable(widthKey, getWindowWidth());
            view.setVariable(heightKey, ImGui.getWindowHeight());
        }

        // Обновить статус закрепления
        view.setVariable(dockedKey, isDocked);
    }


    public static void imageButton(Icons icon, String tooltip, float sizeX, float sizeY, Runnable imageButtonAction) {
        if (ImGui.imageButton(icon.getGlId(), sizeX, sizeY)) {
            imageButtonAction.run();
        }

        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(tooltip);
        }
    }

    public static void buttonAndImage(Icons icon, String text, Runnable runnable) {
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine();

        ImVec2 cursorPos = ImGui.getCursorPos();

        if (ImGui.button("##"+text, ImGui.calcTextSize(text).x, ImGui.calcTextSize(text).y)) {
            runnable.run();
        }

        ImGui.setCursorPos(cursorPos.x, cursorPos.y);

        int index = text.indexOf(" ") + 1;

        ImGui.textColored(255, 207, 64, 255, text.substring(0, index));

        ImGui.sameLine(0, 0);

        ImGui.text(text.substring(index));
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * CubeImGui.splitter(this, "splitter", 4, availableWidth, ImGui.getItemRectMaxY(), 0.3f, 0.1f, 0.9f)
     * }</pre>
     */
    public static void verticalSplitter(View view, String splitterId, float splitterWidth, float availableWidth, float availableHeight, float defaultRatio, float minSplitRatio, float maxSplitRatio) {
        view.putVariable(splitterId, defaultRatio);

        ImGui.invisibleButton(splitterId, splitterWidth, availableHeight);

        short[] border = CubeCodeClient.themeManager.currentTheme.border;

        ImGui.getWindowDrawList().addRect(
                ImGui.getItemRectMin().x, ImGui.getItemRectMin().y,
                ImGui.getItemRectMax().x, availableHeight,
                ImGui.colorConvertFloat4ToU32(border[0], border[1], border[2], border[3])
        );

        if (ImGui.isItemActive()) {
            float mouseDeltaX = ImGui.getIO().getMouseDeltaX();
            float newSplitRatio = ((float)view.getVariable(splitterId)) + mouseDeltaX / availableWidth;

            if (newSplitRatio >= minSplitRatio && newSplitRatio <= maxSplitRatio) {
                view.setVariable(splitterId, newSplitRatio);
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
     * CubeImGui.splitter(this, "splitter", 4, availableWidth, ImGui.getItemRectMaxY())
     * }</pre>
     */
    public static void verticalSplitter(View view, String splitterId, float splitterWidth, float availableWidth, float availableHeight) {
        verticalSplitter(view, splitterId, splitterWidth, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * CubeImGui.splitter(this, "splitter", availableWidth, ImGui.getItemRectMaxY())
     * }</pre>
     */
    public static void verticalSplitter(View view, String splitterId, float availableWidth, float availableHeight) {
        verticalSplitter(view, splitterId, 4, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    /**
     * Example:
     * <pre>{@code
     * float availableHeight = ImGui.getWindowSize().y - ImGui.getStyle().getWindowPaddingY();
     *
     * CubeImGui.beginChild("child", 0, availableHeight * ((float)this.getVariable("splitter")), true, () -> {});
     *
     * CubeImGui.horizontalSplitter(this, "splitter", 5, ImGui.getWindowWidth(), availableHeight, 0.3f, 0.1f, 0.9f);
     * }</pre>
     */
    public static void horizontalSplitter(View view, String splitterId, float splitterHeight, float availableWidth, float availableHeight, float defaultRatio, float minSplitRatio, float maxSplitRatio) {
        view.putVariable(splitterId, defaultRatio);
        float currentSplitRatio = view.getVariable(splitterId);

        ImGui.invisibleButton(splitterId, availableWidth, splitterHeight);

        ImDrawList drawList = ImGui.getWindowDrawList();
        ImVec2 minPos = ImGui.getItemRectMin();
        ImVec2 maxPos = ImGui.getItemRectMax();
        short[] borderColor = CubeCodeClient.themeManager.currentTheme.border;
        int borderColorU32 = ImGui.colorConvertFloat4ToU32(borderColor[0], borderColor[1], borderColor[2], borderColor[3]);
        drawList.addRect(
                minPos.x, minPos.y,
                availableWidth, maxPos.y,
                borderColorU32
        );


        if (ImGui.isItemActive()) {
            float mouseDeltaY = ImGui.getIO().getMouseDeltaY();
            float newSplitRatio = currentSplitRatio + mouseDeltaY / availableHeight;

            if (newSplitRatio >= minSplitRatio && newSplitRatio <= maxSplitRatio) {
                view.setVariable(splitterId, newSplitRatio);
            }
        }

        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.ResizeNS);
        }
    }

    public static void horizontalSplitter(View view, String splitterId, float splitterHeight, float availableWidth, float availableHeight, float defaultRatio) {
        horizontalSplitter(view, splitterId, splitterHeight, availableWidth, availableHeight, defaultRatio, 0.1f, 0.9f);
    }

    /**
     * Example:
     * <pre>{@code
     * float availableHeight = ImGui.getWindowSize().y - ImGui.getStyle().getWindowPaddingY();
     *
     * Float horizontalSplitter = this.getVariable("horizontal_splitter");
     *
     * if (horizontalSplitter != null) {
     *     CubeImGui.beginChild("a", 0, availableHeight * horizontalSplitter, true, () -> {
     *         ImGui.button("lox");
     *     });
     * }
     *
     * CubeImGui.horizontalSplitter(this, "horizontal_splitter", 5, ImGui.getItemRectMaxX(), availableHeight);
     *
     * CubeImGui.beginChild("b", 0, 0, true, () -> {
     *     ImGui.button("f");
     * });
     * }</pre>
     */
    public static void horizontalSplitter(View view, String splitterId, float splitterHeight, float availableWidth, float availableHeight) {
        horizontalSplitter(view, splitterId, splitterHeight, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    public static void horizontalSplitter(View view, String splitterId, float availableWidth, float availableHeight) {
        horizontalSplitter(view, splitterId, 4, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    public static void treeNodeEx(String treeId, String label, Icons icon, int imGuiTreeNodeFlags, Runnable treeNodeAction, Runnable rightClickAction) {
        boolean treeProject = ImGui.treeNodeEx("##"+treeId, imGuiTreeNodeFlags);

        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            rightClickAction.run();
        }

        ImGui.sameLine(0, 9);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (treeProject) {
            treeNodeAction.run();
            ImGui.treePop();
        }
    }

    public static void treeNodeEx(String treeId, String label, Icons icon, int imGuiTreeNodeFlags, Runnable treeNodeAction) {
        boolean treeProject = ImGui.treeNodeEx("##"+treeId, imGuiTreeNodeFlags);

        ImGui.sameLine(0, 9);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (treeProject) {
            treeNodeAction.run();
            ImGui.treePop();
        }
    }

    public static void treeNodeEx(String label, Icons icon, int imGuiTreeNodeFlags, Runnable treeNodeAction) {
        boolean treeProject = ImGui.treeNodeEx("##"+label, imGuiTreeNodeFlags);

        ImGui.sameLine(0, 9);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (treeProject) {
            treeNodeAction.run();
            ImGui.treePop();
        }
    }

    public static void selectable(String selectableId, String label, boolean isSelected, Icons icon, int imGuiSelectableFlags, Runnable rightClickAction, Runnable selectableAction) {
        boolean selectableScript = ImGui.selectable("##"+selectableId, isSelected, imGuiSelectableFlags);

        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            rightClickAction.run();
        }

        ImGui.sameLine(0, 25);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (selectableScript) {
            selectableAction.run();
        }
    }

    public static void selectable(String label, boolean isSelected, Icons icon, int imGuiSelectableFlags, Runnable rightClickAction, Runnable selectableAction) {
        boolean selectableScript = ImGui.selectable("##"+label, isSelected, imGuiSelectableFlags);

        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            rightClickAction.run();
        }

        ImGui.sameLine(0, 25);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (selectableScript) {
            selectableAction.run();
        }
    }

    public static void selectable(String label, boolean isSelected, Icons icon, int imGuiSelectableFlags, Runnable selectableAction) {
        boolean selectableScript = ImGui.selectable("##"+label, isSelected, imGuiSelectableFlags);

        ImGui.sameLine(0, 0);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (selectableScript) {
            selectableAction.run();
        }
    }

    public static void treeNode(String label, Icons icon, int imguiTreeNodeFlags, Runnable render) {
        boolean tree = ImGui.treeNodeEx("##"+label, imguiTreeNodeFlags);

        ImGui.sameLine(0, 4);
        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(label);

        if (tree) {
            render.run();
            ImGui.treePop();
        }
    }

    public static void renderEntityInInventoryRaw(int entityPosLeft, int entityPosTop, int size, int rotatePlayer,
                                                  float angleXComponent, float angleYComponent, LivingEntity Player) {
        MatrixStack posestack = RenderSystem.getModelViewStack();
        posestack.push();
        posestack.translate(entityPosLeft, entityPosTop, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack posestack1 = new MatrixStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float) size, (float) size, (float) size);
        Quaternionf quaternion = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternion1 = new Quaternionf();

        quaternion.mul(quaternion1);
        posestack1.multiply(quaternion);
        float f2 = 0;
        float f3 = 0;
        float f4 = 0;
        float f5 = 0;
        float f6 = 0;

        EntityRenderDispatcher entityrenderdispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.setRotation(quaternion1);
        entityrenderdispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate entityVertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(Player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1,
                    entityVertexConsumers, 15728880);
        });
        entityrenderdispatcher.setRenderShadows(true);
        posestack.pop();
        RenderSystem.applyModelViewMatrix();
    }

    public static final Framebuffer buffer = new Framebuffer(false) {};

    public static void renderCharacter(float x, float y, float scale) {
        MinecraftClient mc = MinecraftClient.getInstance();
        LivingEntity player = mc.player;
        if (player == null) return;

        // Сохраняем текущие состояния OpenGL
        int lastTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        int lastFBO = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);

        // Сохраняем состояние viewport
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

        Framebuffer mainBuffer = mc.getFramebuffer();

        if (buffer == null) {
            buffer.initFbo(mainBuffer.textureWidth, mainBuffer.textureHeight, false);
        }
        if (buffer.textureWidth != mainBuffer.textureWidth || buffer.textureHeight != mainBuffer.textureHeight) {
            buffer.resize(mainBuffer.textureWidth, mainBuffer.textureHeight, false);
        }

        buffer.clear(false);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, buffer.fbo);
        buffer.beginWrite(false);

        // Настройка освещения
        DiffuseLighting.method_34742();

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.translate(x, y, 100);
        matrixStack.scale(-scale, -scale, -scale);
        matrixStack.translate(0, player.getScaleFactor() / 2f, 0);

        Quaternionf quaternion = new Quaternionf().rotateZ((float) Math.PI);
        matrixStack.multiply(quaternion);

        // Сохраняем оригинальные повороты
        float bodyYaw = player.bodyYaw;
        float yaw = player.getYaw();
        float pitch = player.getPitch();
        float headYawO = player.prevHeadYaw;
        float headYaw = player.headYaw;

        // Обновляем повороты на основе положения мыши
        player.bodyYaw = 180.0f;
        player.setYaw(180.0f);
        player.setPitch(0.0f);
        player.headYaw = player.getYaw();
        player.prevHeadYaw = player.getYaw();

        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);

        VertexConsumerProvider.Immediate bufferSource = mc.getBufferBuilders().getEntityVertexConsumers();

        dispatcher.render(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack,
                bufferSource, LightmapTextureManager.MAX_LIGHT_COORDINATE);

        bufferSource.draw();
        dispatcher.setRenderShadows(true);

        // Восстанавливаем повороты
        player.bodyYaw = bodyYaw;
        player.setYaw(yaw);
        player.setPitch(pitch);
        player.prevHeadYaw = headYawO;
        player.headYaw = headYaw;

        matrixStack.pop();

        // Отключаем освещение
        DiffuseLighting.enableGuiDepthLighting();

        buffer.endWrite();

        // Восстанавливаем состояния OpenGL
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, lastFBO);
        GlStateManager._bindTexture(lastTexture);
        GL11.glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);

        // Очищаем состояния
        RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void item(ItemStack itemStack, int width, int height) {
        ImGuiFrameBuffer frameBuffer = ImGuiLoader.frameBuffer;


        ImGui.imageButton(frameBuffer.getTexture(), width, height);
    }

    public void splitter(boolean splitVertically, float thickness, float size0, float size1, float minSize0, float minSize1) {
        ImVec2 backupPos = ImGui.getCursorPos();
        if (splitVertically) {
            ImGui.setCursorPosY(backupPos.y + size0);
        } else {
            ImGui.setCursorPosX(backupPos.x + size0);
        }

        ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.6f, 0.6f, 0.6f, 0.1f);
        ImGui.button((splitVertically ? "" : "##Splitter"), !splitVertically ? thickness : -1.0f, splitVertically ? thickness : -1.0f);
        ImGui.popStyleColor(3);

        ImGui.setItemAllowOverlap();

        if (ImGui.isItemActive()) {
            float mouseDelta = splitVertically ? ImGui.getIO().getMouseDeltaY() : ImGui.getIO().getMouseDeltaX();

            // Minimum pane size
            if (mouseDelta < minSize0 - size0) {
                mouseDelta = minSize0 - size0;
            }
            if (mouseDelta > size1 - minSize1) {
                mouseDelta = size1 - minSize1;
            }

            // Apply resize
            size0 += mouseDelta;
            size1 -= mouseDelta;
        }

        ImGui.setCursorPos(backupPos.x, backupPos.y);
    }
}