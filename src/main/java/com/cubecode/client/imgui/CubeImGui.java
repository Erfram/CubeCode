package com.cubecode.client.imgui;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.gifs.Gif;
import com.cubecode.client.gifs.GifManager;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.utils.Icons;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiMouseCursor;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CubeImGui {
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

    public static void imageButton(Icons icon, String tooltip, float sizeX, float sizeY, Runnable imageButtonAction) {
        if (ImGui.imageButton(icon.getGlId(), sizeX, sizeY)) {
            imageButtonAction.run();
        }

        if (ImGui.isItemHovered()) {
            ImGui.setTooltip(tooltip);
        }
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
}