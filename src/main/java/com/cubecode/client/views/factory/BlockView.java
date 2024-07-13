package com.cubecode.client.views.factory;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class BlockView extends View {
    @Override
    public String getName() {
        return String.format("Block##%s", uniqueID);
    }

    @Override
    public void render() {
        CubeImGui.begin(this, Text.translatable("imgui.cubecode.factory.block.title").getString(), (closed) -> {
            if (!closed) {
                ImGuiLoader.removeView(this);
            }

            CubeImGui.manageDocking(this);

            CubeImGui.withItemWidth(120, () -> CubeImGui.inputText(this, Text.translatable("imgui.cubecode.factory.block.id").getString(), (str) -> {}));

            CubeImGui.withItemWidth(200, () -> {
                CubeImGui.inputText(this, Text.translatable("imgui.cubecode.factory.block.name").getString(), (str) -> {});
                CubeImGui.inputText(this, Text.translatable("imgui.cubecode.factory.block.texture").getString(), (str) -> {});
                CubeImGui.inputText(this, Text.translatable("imgui.cubecode.factory.block.dropsLike").getString(), (str) -> {});
            });

            CubeImGui.withItemWidth(100, () -> CubeImGui.dragScalar(this, Text.translatable("imgui.cubecode.factory.block.resistance").getString(), 0.5F, ImGuiDataType.Float, 0.01F, 0F, 10F, (drag) -> {}));
            CubeImGui.withItemWidth(100, () -> CubeImGui.dragScalar(this, Text.translatable("imgui.cubecode.factory.block.slipperiness").getString(), 0.6F, ImGuiDataType.Float, 0.01F, 0F, 3F, (drag) -> {}));
            CubeImGui.withItemWidth(80, () -> CubeImGui.dragScalar(this, Text.translatable("imgui.cubecode.factory.block.jumpVelocityMultiplier").getString(), 1.0F, ImGuiDataType.Float, 0.01F, 0F, 10F, (drag) -> {}));
            CubeImGui.withItemWidth(80, () -> CubeImGui.dragScalar(this, Text.translatable("imgui.cubecode.factory.block.velocityMultiplier").getString(), 1.0F, ImGuiDataType.Float, 0.01F, 0F, 10F, (drag) -> {}));

            CubeImGui.withItemWidth(80, () -> CubeImGui.dragScalar(this, Text.translatable("imgui.cubecode.factory.block.luminance").getString(), 1, ImGuiDataType.S8, 1, 0, 20, (drag) -> {}));

            CubeImGui.withItemWidth(40, () -> CubeImGui.button(Text.translatable("imgui.cubecode.factory.block.reset").getString(), () -> {
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.id").getString() + this.getUniqueID(), new ImString(30));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.name").getString() + this.getUniqueID(), new ImString(30));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.texture").getString() + this.getUniqueID(), new ImString(30));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.dropsLike").getString() + this.getUniqueID(), new ImString(30));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.resistance").getString() + this.getUniqueID(), new ImFloat(0.5F));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.slipperiness").getString() + this.getUniqueID(), new ImFloat(0.6F));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.jumpVelocityMultiplier").getString() + this.getUniqueID(), new ImFloat(1F));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.velocityMultiplier").getString() + this.getUniqueID(), new ImFloat(1F));
                this.setVariable(Text.translatable("imgui.cubecode.factory.block.luminance").getString() + this.getUniqueID(), new ImInt(1));
            }));
        });
    }

    @Override
    public void init() {
        int viewWidth = 500;
        int viewHeight = 400;

        int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
        int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

        //Центрирование по центру
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }
}