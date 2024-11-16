package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.views.TestView;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;

import java.util.Collections;

public class WindowComponent extends AbstractComponent {
    TestView view;
    String title;
    int flags;
    Runnable onClick;
    Runnable onClose;

    public WindowComponent(TestView view, String title, Runnable callback) {
        this.view = view;
        this.title = title;
        this.flags = 0;
        this.onClick = callback;
        this.onClose = () -> MinecraftClient.getInstance().player.closeScreen();
        this.windowPosFlags = ImGuiCond.Always;
    }

    public WindowComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    public WindowComponent onClose(Runnable onClose) {
        this.onClose = onClose;
        return this;
    }

    public WindowComponent noDecoration() {
        this.flags = this.flags | ImGuiWindowFlags.NoDecoration;
        return this;
    }

    public WindowComponent noMove() {
        this.flags = this.flags | ImGuiWindowFlags.NoMove;
        return this;
    }

    public WindowComponent alwaysAutoResize() {
        this.flags = this.flags | ImGuiWindowFlags.AlwaysAutoResize;
        return this;
    }

    public WindowComponent noDocking() {
        this.flags = this.flags | ImGuiWindowFlags.NoDocking;
        return this;
    }

    public WindowComponent appearingPosition() {
        this.windowPosFlags = this.windowPosFlags | ImGuiCond.Appearing;
        return this;
    }

    public WindowComponent noFocusOnAppearing() {
        this.flags = this.flags | ImGuiWindowFlags.NoFocusOnAppearing;
        return this;
    }

    public WindowComponent noNavFocus() {
        this.flags = this.flags | ImGuiWindowFlags.NoNavFocus;
        return this;
    }

    public WindowComponent noBringToFrontOnFocus() {
        this.flags = this.flags | ImGuiWindowFlags.NoBringToFrontOnFocus;
        return this;
    }

    public WindowComponent noResize() {
        this.flags = this.flags | ImGuiWindowFlags.NoResize;
        return this;
    }

    @Override
    public void render() {
        this.view.runnables.add(() -> {
            this.view.putVariable(this.title + view.getUniqueID(), new ImBoolean(true));
            ImBoolean close = view.getVariable(this.title + this.view.getUniqueID());

            this.pushTheme();
            this.pushSize();
            this.pushPosition();
            if (ImGui.begin(this.title, close, this.flags)) {
                if (!close.get()) {
                    this.onClose.run();
                    ImGuiLoader.removeView(this.view);
                } else {
                    CubeImGui.manageDocking(this.view);

                    this.onClick.run();
                }

                this.view.components.forEach(component -> {
                    component.pushTheme();
                    component.pushPosition();
                    component.pushSize();

                    component.render();

                    component.popSize();
                    component.popPosition();
                    component.popTheme();
                });
            }

            ImGui.end();
            this.popPosition();
            this.popSize();
            this.popTheme();
            this.view.components = Collections.unmodifiableList(this.view.components);
        });
    }
}