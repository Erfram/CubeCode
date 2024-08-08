package com.cubecode.client.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.views.settings.ScriptView;
import com.cubecode.client.views.settings.WindowView;
import com.cubecode.client.views.textEditor.TextEditorView;
import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;
import net.minecraft.text.Text;

public class DashboardView extends View {
    @Override
    public void render() {
        CubeImGui.mainMenuBar(() -> {
            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.windows.title").getString(), () -> {
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.title").getString(), () -> {
                    ImGuiLoader.pushView(new TextEditorView());
                });
            });

            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.settings.title").getString(), () -> {
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.settings.window.title").getString(), () -> ImGuiLoader.pushView(new WindowView()));
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.settings.script.title").getString(), () -> ImGuiLoader.pushView(new ScriptView()));
            });
        });

        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }
}
