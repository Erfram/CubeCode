package com.cubecode.client.imgui.basic;

import imgui.ImFont;
import imgui.ImFontAtlas;
import imgui.ImGui;
import imgui.ImGuiIO;
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
    private static final ConcurrentLinkedQueue<View> RENDER_STACK = new ConcurrentLinkedQueue<>();
    private static ImFont MAIN_FONT;

    public static void onGlfwInit(long handle) {
        ImGui.createContext();
        final ImGuiIO io = ImGui.getIO();

        loadFont(io);

        io.setIniFilename(null);
        IMGUI_GLFW.init(handle, true);
        IMGUI_GL3.init();
    }

    private static void loadFont(ImGuiIO io) {
        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.addFontDefault();

        try (InputStream inputStream = ImGuiLoader.class.getClassLoader().getResourceAsStream("imgui/fonts/default.ttf")) {
            byte[] bytes = inputStream.readAllBytes();
            MAIN_FONT = fontAtlas.addFontFromMemoryTTF(bytes, 16);
        } catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }
        fontAtlas.build();
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
}
