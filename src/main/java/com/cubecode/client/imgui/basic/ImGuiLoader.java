package com.cubecode.client.imgui.basic;

import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraft.client.MinecraftClient;
import com.cubecode.CubeCode;

import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class ImGuiLoader {

    public static final ImGuiImplGlfw IMGUI_GLFW = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 IMGUI_GL3 = new ImGuiImplGl3();
    private static final ConcurrentLinkedQueue<View> RENDERSTACK = new ConcurrentLinkedQueue<>();
    private static ImFont MAIN_FONT;

    public static void onGlfwInit(long handle) {
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();

        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.addFontDefault();

        try (InputStream inputStream = ImGuiLoader.class.getClassLoader().getResourceAsStream("imgui/fonts/default.ttf")) {
            byte[] bytes = inputStream.readAllBytes();
            MAIN_FONT = fontAtlas.addFontFromMemoryTTF(bytes, 16);
        } catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }
        fontAtlas.build();

        io.setIniFilename(null);
        IMGUI_GLFW.init(handle, true);
        IMGUI_GL3.init();
    }

    public static void onFrameRender() {
        IMGUI_GLFW.newFrame();
        ImGui.newFrame();

        ImGui.pushFont(MAIN_FONT);
        RENDERSTACK.forEach(view -> {
            MinecraftClient.getInstance().getProfiler()
                    .push(String.format("Section [%s]", view.getName()));
            view.getTheme().preRender();
            view.loop();
            view.getTheme().postRender();
            MinecraftClient.getInstance().getProfiler().pop();
        });
        ImGui.popFont();

        ImGui.render();
        endFrameRender();
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
        return RENDERSTACK;
    }

    public static void pushView(View view) {
        RENDERSTACK.add(view);
    }

    public static void pushViews(View... views) {
        RENDERSTACK.addAll(Arrays.asList(views));
    }

    public static void removeView(View view) {
        RENDERSTACK.remove(view);
    }

    public static void removeViews(View... views) {
        RENDERSTACK.removeAll(Arrays.asList(views));
    }

    public static void clearViews() {
        RENDERSTACK.clear();
    }
}
