package com.cubecode.client.imgui.basic;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.themes.CubeTheme;
import com.cubecode.client.imgui.themes.ThemeManager;
import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class ImGuiLoader {
    public static final ImGuiImplGlfw IMGUI_GLFW = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 IMGUI_GL3 = new ImGuiImplGl3();
    private static final ConcurrentLinkedQueue<View> RENDER_STACK = new ConcurrentLinkedQueue<>();
    private static ImFont MAIN_FONT;

    public static void onGlfwInit(long handle) {
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();

        CubeCodeClient.fontManager.loadFonts();

        MAIN_FONT = CubeCodeClient.fontManager.fonts.get("default");

        io.setIniFilename(null);

        IMGUI_GLFW.init(handle, true);
        IMGUI_GL3.init();
    }

    public static void onFrameRender() {
        IMGUI_GLFW.newFrame();
        ImGui.newFrame();

        CubeTheme cubeTheme = CubeCodeClient.themeManager.currentTheme;

        boolean isDefaultTheme = CubeCodeClient.themeManager.currentTheme.equals(CubeCodeClient.themeManager.getTheme("Default"));
        boolean isDefaultLightTheme = CubeCodeClient.themeManager.currentTheme.equals(CubeCodeClient.themeManager.getTheme("Default light"));
        boolean isDefaultDarkTheme = CubeCodeClient.themeManager.currentTheme.equals(CubeCodeClient.themeManager.getTheme("Default dark"));

        if (!isDefaultTheme) {
            if (isDefaultLightTheme) {
                ImGui.styleColorsLight();
            } else if (isDefaultDarkTheme) {
                ImGui.styleColorsDark();
            } else {
                ImGui.pushStyleVar(ImGuiStyleVar.Alpha, cubeTheme.alpha);
                ImGui.pushStyleVar(ImGuiStyleVar.DisabledAlpha, cubeTheme.disabledAlpha);
                ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, cubeTheme.windowPadding[0], cubeTheme.windowPadding[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, cubeTheme.windowRounding);
                ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, cubeTheme.windowBorderSize);
                ImGui.pushStyleVar(ImGuiStyleVar.WindowMinSize, cubeTheme.windowMinSize[0], cubeTheme.windowMinSize[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.WindowTitleAlign, cubeTheme.windowTitleAlign[0], cubeTheme.windowTitleAlign[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, cubeTheme.childRounding);
                ImGui.pushStyleVar(ImGuiStyleVar.ChildBorderSize, cubeTheme.childBorderSize);
                ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, cubeTheme.popupRounding);
                ImGui.pushStyleVar(ImGuiStyleVar.PopupBorderSize, cubeTheme.popupBorderSize);
                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, cubeTheme.framePadding[0], cubeTheme.framePadding[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, cubeTheme.frameRounding);
                ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, cubeTheme.frameBorderSize);
                ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, cubeTheme.itemSpacing[0], cubeTheme.itemSpacing[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, cubeTheme.itemInnerSpacing[0], cubeTheme.itemInnerSpacing[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.IndentSpacing, cubeTheme.indentSpacing);
                ImGui.pushStyleVar(ImGuiStyleVar.CellPadding, cubeTheme.cellPadding[0], cubeTheme.cellPadding[1]);
                ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarSize, cubeTheme.scrollbarSize);
                ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, cubeTheme.scrollbarRounding);
                ImGui.pushStyleVar(ImGuiStyleVar.GrabMinSize, cubeTheme.grabMinSize);
                ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, cubeTheme.grabRounding);
                ImGui.pushStyleVar(ImGuiStyleVar.TabRounding, cubeTheme.tabRounding);

                ImGui.pushStyleColor(ImGuiCol.Text, cubeTheme.text[0], cubeTheme.text[1], cubeTheme.text[2], cubeTheme.text[3]);
                ImGui.pushStyleColor(ImGuiCol.TextDisabled, cubeTheme.textDisabled[0], cubeTheme.textDisabled[1], cubeTheme.textDisabled[2], cubeTheme.textDisabled[3]);
                ImGui.pushStyleColor(ImGuiCol.WindowBg, cubeTheme.windowBg[0], cubeTheme.windowBg[1], cubeTheme.windowBg[2], cubeTheme.windowBg[3]);
                ImGui.pushStyleColor(ImGuiCol.ChildBg, cubeTheme.childBg[0], cubeTheme.childBg[1], cubeTheme.childBg[2], cubeTheme.childBg[3]);
                ImGui.pushStyleColor(ImGuiCol.PopupBg, cubeTheme.popupBg[0], cubeTheme.popupBg[1], cubeTheme.popupBg[2], cubeTheme.popupBg[3]);
                ImGui.pushStyleColor(ImGuiCol.Border, cubeTheme.border[0], cubeTheme.border[1], cubeTheme.border[2], cubeTheme.border[3]);
                ImGui.pushStyleColor(ImGuiCol.BorderShadow, cubeTheme.borderShadow[0], cubeTheme.borderShadow[1], cubeTheme.borderShadow[2], cubeTheme.borderShadow[3]);
                ImGui.pushStyleColor(ImGuiCol.FrameBg, cubeTheme.frameBg[0], cubeTheme.frameBg[1], cubeTheme.frameBg[2], cubeTheme.frameBg[3]);
                ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, cubeTheme.frameBgHovered[0], cubeTheme.frameBgHovered[1], cubeTheme.frameBgHovered[2], cubeTheme.frameBgHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.FrameBgActive, cubeTheme.frameBgActive[0], cubeTheme.frameBgActive[1], cubeTheme.frameBgActive[2], cubeTheme.frameBgActive[3]);
                ImGui.pushStyleColor(ImGuiCol.TitleBg, cubeTheme.titleBg[0], cubeTheme.titleBg[1], cubeTheme.titleBg[2], cubeTheme.titleBg[3]);
                ImGui.pushStyleColor(ImGuiCol.TitleBgActive, cubeTheme.titleBgActive[0], cubeTheme.titleBgActive[1], cubeTheme.titleBgActive[2], cubeTheme.titleBgActive[3]);
                ImGui.pushStyleColor(ImGuiCol.TitleBgCollapsed, cubeTheme.titleBgCollapsed[0], cubeTheme.titleBgCollapsed[1], cubeTheme.titleBgCollapsed[2], cubeTheme.titleBgCollapsed[3]);
                ImGui.pushStyleColor(ImGuiCol.MenuBarBg, cubeTheme.menuBarBg[0], cubeTheme.menuBarBg[1], cubeTheme.menuBarBg[2], cubeTheme.menuBarBg[3]);
                ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, cubeTheme.scrollbarBg[0], cubeTheme.scrollbarBg[1], cubeTheme.scrollbarBg[2], cubeTheme.scrollbarBg[3]);
                ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, cubeTheme.scrollbarGrab[0], cubeTheme.scrollbarGrab[1], cubeTheme.scrollbarGrab[2], cubeTheme.scrollbarGrab[3]);
                ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, cubeTheme.scrollbarGrabHovered[0], cubeTheme.scrollbarGrabHovered[1], cubeTheme.scrollbarGrabHovered[2], cubeTheme.scrollbarGrabHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, cubeTheme.scrollbarGrabActive[0], cubeTheme.scrollbarGrabActive[1], cubeTheme.scrollbarGrabActive[2], cubeTheme.scrollbarGrabActive[3]);
                ImGui.pushStyleColor(ImGuiCol.CheckMark, cubeTheme.checkMark[0], cubeTheme.checkMark[1], cubeTheme.checkMark[2], cubeTheme.checkMark[3]);
                ImGui.pushStyleColor(ImGuiCol.SliderGrab, cubeTheme.sliderGrab[0], cubeTheme.sliderGrab[1], cubeTheme.sliderGrab[2], cubeTheme.sliderGrab[3]);
                ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, cubeTheme.sliderGrabActive[0], cubeTheme.sliderGrabActive[1], cubeTheme.sliderGrabActive[2], cubeTheme.sliderGrabActive[3]);
                ImGui.pushStyleColor(ImGuiCol.Button, cubeTheme.button[0], cubeTheme.button[1], cubeTheme.button[2], cubeTheme.button[3]);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, cubeTheme.buttonHovered[0], cubeTheme.buttonHovered[1], cubeTheme.buttonHovered[2], cubeTheme.buttonHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, cubeTheme.buttonActive[0], cubeTheme.buttonActive[1], cubeTheme.buttonActive[2], cubeTheme.buttonActive[3]);
                ImGui.pushStyleColor(ImGuiCol.Header, cubeTheme.header[0], cubeTheme.header[1], cubeTheme.header[2], cubeTheme.header[3]);
                ImGui.pushStyleColor(ImGuiCol.HeaderHovered, cubeTheme.headerHovered[0], cubeTheme.headerHovered[1], cubeTheme.headerHovered[2], cubeTheme.headerHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.HeaderActive, cubeTheme.headerActive[0], cubeTheme.headerActive[1], cubeTheme.headerActive[2], cubeTheme.headerActive[3]);
                ImGui.pushStyleColor(ImGuiCol.Separator, cubeTheme.separator[0], cubeTheme.separator[1], cubeTheme.separator[2], cubeTheme.separator[3]);
                ImGui.pushStyleColor(ImGuiCol.SeparatorHovered, cubeTheme.separatorHovered[0], cubeTheme.separatorHovered[1], cubeTheme.separatorHovered[2], cubeTheme.separatorHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.SeparatorActive, cubeTheme.separatorActive[0], cubeTheme.separatorActive[1], cubeTheme.separatorActive[2], cubeTheme.separatorActive[3]);
                ImGui.pushStyleColor(ImGuiCol.ResizeGrip, cubeTheme.resizeGrip[0], cubeTheme.resizeGrip[1], cubeTheme.resizeGrip[2], cubeTheme.resizeGrip[3]);
                ImGui.pushStyleColor(ImGuiCol.ResizeGripHovered, cubeTheme.resizeGripHovered[0], cubeTheme.resizeGripHovered[1], cubeTheme.resizeGripHovered[2], cubeTheme.resizeGripHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.ResizeGripActive, cubeTheme.resizeGripActive[0], cubeTheme.resizeGripActive[1], cubeTheme.resizeGripActive[2], cubeTheme.resizeGripActive[3]);
                ImGui.pushStyleColor(ImGuiCol.Tab, cubeTheme.tab[0], cubeTheme.tab[1], cubeTheme.tab[2], cubeTheme.tab[3]);
                ImGui.pushStyleColor(ImGuiCol.TabHovered, cubeTheme.tabHovered[0], cubeTheme.tabHovered[1], cubeTheme.tabHovered[2], cubeTheme.tabHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.TabActive, cubeTheme.tabActive[0], cubeTheme.tabActive[1], cubeTheme.tabActive[2], cubeTheme.tabActive[3]);
                ImGui.pushStyleColor(ImGuiCol.TabUnfocused, cubeTheme.tabUnfocused[0], cubeTheme.tabUnfocused[1], cubeTheme.tabUnfocused[2], cubeTheme.tabUnfocused[3]);
                ImGui.pushStyleColor(ImGuiCol.TabUnfocusedActive, cubeTheme.tabUnfocusedActive[0], cubeTheme.tabUnfocusedActive[1], cubeTheme.tabUnfocusedActive[2], cubeTheme.tabUnfocusedActive[3]);
                ImGui.pushStyleColor(ImGuiCol.DockingPreview, cubeTheme.dockingPreview[0], cubeTheme.dockingPreview[1], cubeTheme.dockingPreview[2], cubeTheme.dockingPreview[3]);
                ImGui.pushStyleColor(ImGuiCol.DockingEmptyBg, cubeTheme.dockingEmptyBg[0], cubeTheme.dockingEmptyBg[1], cubeTheme.dockingEmptyBg[2], cubeTheme.dockingEmptyBg[3]);
                ImGui.pushStyleColor(ImGuiCol.PlotLines, cubeTheme.plotLines[0], cubeTheme.plotLines[1], cubeTheme.plotLines[2], cubeTheme.plotLines[3]);
                ImGui.pushStyleColor(ImGuiCol.PlotLinesHovered, cubeTheme.plotLinesHovered[0], cubeTheme.plotLinesHovered[1], cubeTheme.plotLinesHovered[2], cubeTheme.plotLinesHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.PlotHistogram, cubeTheme.plotHistogram[0], cubeTheme.plotHistogram[1], cubeTheme.plotHistogram[2], cubeTheme.plotHistogram[3]);
                ImGui.pushStyleColor(ImGuiCol.PlotHistogramHovered, cubeTheme.plotHistogramHovered[0], cubeTheme.plotHistogramHovered[1], cubeTheme.plotHistogramHovered[2], cubeTheme.plotHistogramHovered[3]);
                ImGui.pushStyleColor(ImGuiCol.TableHeaderBg, cubeTheme.tableHeaderBg[0], cubeTheme.tableHeaderBg[1], cubeTheme.tableHeaderBg[2], cubeTheme.tableHeaderBg[3]);
                ImGui.pushStyleColor(ImGuiCol.TableBorderStrong, cubeTheme.tableBorderStrong[0], cubeTheme.tableBorderStrong[1], cubeTheme.tableBorderStrong[2], cubeTheme.tableBorderStrong[3]);
                ImGui.pushStyleColor(ImGuiCol.TableBorderLight, cubeTheme.tableBorderLight[0], cubeTheme.tableBorderLight[1], cubeTheme.tableBorderLight[2], cubeTheme.tableBorderLight[3]);
                ImGui.pushStyleColor(ImGuiCol.TableRowBg, cubeTheme.tableRowBg[0], cubeTheme.tableRowBg[1], cubeTheme.tableRowBg[2], cubeTheme.tableRowBg[3]);
                ImGui.pushStyleColor(ImGuiCol.TableRowBgAlt, cubeTheme.tableRowBgAlt[0], cubeTheme.tableRowBgAlt[1], cubeTheme.tableRowBgAlt[2], cubeTheme.tableRowBgAlt[3]);
                ImGui.pushStyleColor(ImGuiCol.TextSelectedBg, cubeTheme.textSelectedBg[0], cubeTheme.textSelectedBg[1], cubeTheme.textSelectedBg[2], cubeTheme.textSelectedBg[3]);
                ImGui.pushStyleColor(ImGuiCol.DragDropTarget, cubeTheme.dragDropTarget[0], cubeTheme.dragDropTarget[1], cubeTheme.dragDropTarget[2], cubeTheme.dragDropTarget[3]);
                ImGui.pushStyleColor(ImGuiCol.NavHighlight, cubeTheme.navHighlight[0], cubeTheme.navHighlight[1], cubeTheme.navHighlight[2], cubeTheme.navHighlight[3]);
                ImGui.pushStyleColor(ImGuiCol.NavWindowingHighlight, cubeTheme.navWindowingHighlight[0], cubeTheme.navWindowingHighlight[1], cubeTheme.navWindowingHighlight[2], cubeTheme.navWindowingHighlight[3]);
                ImGui.pushStyleColor(ImGuiCol.NavWindowingDimBg, cubeTheme.navWindowingDimBg[0], cubeTheme.navWindowingDimBg[1], cubeTheme.navWindowingDimBg[2], cubeTheme.navWindowingDimBg[3]);
                ImGui.pushStyleColor(ImGuiCol.ModalWindowDimBg, cubeTheme.modalWindowDimBg[0], cubeTheme.modalWindowDimBg[1], cubeTheme.modalWindowDimBg[2], cubeTheme.modalWindowDimBg[3]);
            }
        } else {
            ImGui.styleColorsClassic();
        }

        ImGui.pushFont(CubeCodeClient.fontManager.fonts.get(CubeCodeClient.fontManager.currentFontName));

        renderViews();

        ImGui.popFont();

        if (!isDefaultTheme && !isDefaultLightTheme && !isDefaultDarkTheme) {
            ImGui.popStyleColor(55);
            ImGui.popStyleVar(23);
        }

        ImGui.render();

        endFrameRender();
    }

    private static void renderViews() {
        MinecraftClient client = MinecraftClient.getInstance();
        RENDER_STACK.forEach(view -> {
            client.getProfiler().push(String.format("Section [%s]", view.getName()));
            view.getTheme().preRender();
            view.loop();
            view.getTheme().postRender();
            client.getProfiler().pop();
        });
    }

    private static void endFrameRender() {
        IMGUI_GL3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public static ConcurrentLinkedQueue<View> getRenderStack() {
        return RENDER_STACK;
    }

    public static void pushView(View view) {
        RENDER_STACK.add(view);
    }

    public static void pushViews(View... views) {
        RENDER_STACK.addAll(Arrays.asList(views));
    }

    public static void removeView(View view) {
        RENDER_STACK.remove(view);
    }

    public static void removeViews(View... views) {
        RENDER_STACK.removeAll(Arrays.asList(views));
    }

    public static void clearViews() {
        RENDER_STACK.clear();
    }

    @Nullable
    public static <T extends View> T getView(Class<T> viewClass) {
        for (View view : RENDER_STACK) {
            if (viewClass.isInstance(view)) {
                return viewClass.cast(view);
            }
        }
        return null;
    }

    public static List<View> getViews() {
        return RENDER_STACK.stream().toList();
    }

    public static <T extends View> List<T> getViews(Class<T> viewClass) {
        return getViews().stream()
                .filter(viewClass::isInstance)
                .map(viewClass::cast)
                .collect(Collectors.toList());
    }

    public static boolean isOpenView(Class<? extends View> viewClass) {
        return ImGuiLoader.getRenderStack().stream()
                .anyMatch(viewClass::isInstance);
    }

    public static void handleKeyReleased(int keyCode, int scanCode, int modifiers) {
        RENDER_STACK.forEach(view -> {
            view.handleKeyReleased(keyCode, scanCode, modifiers);
        });
    }

    public static void handleKeyPressed(int keyCode, int scanCode, int modifiers) {
        RENDER_STACK.forEach(view -> {
            view.handleKeyPressed(keyCode, scanCode, modifiers);
        });
    }

    public static void handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        RENDER_STACK.forEach(view -> {
            view.handleScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
        });
    }

    public static void handleMouseClicked(double mouseX, double mouseY, int button) {
        RENDER_STACK.forEach(view -> {
            view.handleMouseClicked(mouseX, mouseY, button);
        });
    }

    public static void handleMouseReleased(double mouseX, double mouseY, int button) {
        RENDER_STACK.forEach(view -> {
            view.handleMouseReleased(mouseX, mouseY, button);
        });
    }

    public static void onClose() {
        RENDER_STACK.forEach(View::onClose);
    }
}
