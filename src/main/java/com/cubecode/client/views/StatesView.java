package com.cubecode.client.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

public class StatesView extends View {
    NbtCompound nbt;
    List<String> playerNames;

    String currentState = "Server^";

    public StatesView(NbtCompound nbt, List<String> playerNames) {
        this.nbt = nbt;
        this.playerNames = playerNames;
    }

    @Override
    public void init() {
        int viewWidth = 320;
        float posX = (windowWidth - viewWidth) * 0.5f;
        int viewHeight = 240;
        float posY = (windowHeight - viewHeight) * 0.5f;
        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public String getName() {
        return "States##"+this.getUniqueID();
    }

    @Override
    public void render() {
        Window.create()
            .title(getName())
            .callback(() -> {
                ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize(this.currentState).x) / 2);
                ImGui.textColored(255,207,72, 255, this.currentState.replaceAll("\\^", ""));
                if (ImGui.imageButton(Icons.PLUS.getGlId(), 16, 16)) {
                    ImVec2 center = ImGui.getMainViewport().getCenter();
                    ImGui.setNextWindowPos(center.x, center.y, ImGuiCond.Always, 0.5f, 0.5f);
                    ImGui.openPopup("lox");
                }

                if (ImGui.beginPopupModal("lox", new ImBoolean(true), ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize)) {
                    CubeImGui.selectable("Server", this.currentState.equals("Server^"), Icons.SERVER, ImGuiSelectableFlags.None, () -> {
                        this.currentState = "Server^";
                    });

                    this.playerNames.forEach(playerName -> {
                        if (ImGui.selectable(playerName, this.currentState.equals(playerName))) {
                            this.currentState = playerName;
                        }
                    });

                    ImGui.endPopup();
                }

                ImGui.separator();
            })
            .render(this);
    }
}
