package com.cubecode.client.imgui.basic;

import com.cubecode.CubeCodeClient;
import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

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

        ImGui.pushFont(MAIN_FONT);
        renderViews();
        ImGui.popFont();

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
