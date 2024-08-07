package com.cubecode.client.views.factory;

import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.*;
import com.cubecode.network.NetworkingPackets;
import com.cubecode.utils.PacketByteBufUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class BlockView extends View {
    @Override
    public String getName() {
        return String.format("Block##%s", uniqueID);
    }

    private final int viewWidth = 500;
    private final int viewHeight = 400;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public void render() {
        Window.create()
            .title(Text.translatable("imgui.cubecode.factory.block.title").getString())
            .draw(
                InputText.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.id").getString())
                        .w(120)
                        .id("id")
                        .build(),

                InputText.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.name").getString())
                        .w(200)
                        .id("name")
                        .build(),

                InputText.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.texture").getString())
                        .w(200)
                        .id("texture")
                        .build(),

                InputText.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.dropsLike").getString())
                        .w(200)
                        .id("dropsLike")
                        .build(),

                MenuBar.builder()
                        .menu(
                            MenuBar.Menu.builder()
                                .label("blocks")
                                .item(
                                    MenuBar.Item.builder()
                                            .label("lox")
                                            .shortcut("таблетка лох")
                                            .build()
                                ).build()
                        )
                        .build(),

                DragScalar.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.hardness").getString())
                        .defaultValue(0.5F)
                        .dataType(ImGuiDataType.Float)
                        .speed(0.01F)
                        .min(0F)
                        .max(10F)
                        .w(100)
                        .id("hardness")
                        .build(),

                DragScalar.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.resistance").getString())
                        .defaultValue(0.5F)
                        .dataType(ImGuiDataType.Float)
                        .speed(0.01F)
                        .min(0F)
                        .max(10F)
                        .w(100)
                        .id("resistance")
                        .build(),

                DragScalar.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.slipperiness").getString())
                        .defaultValue(0.6F)
                        .dataType(ImGuiDataType.Float)
                        .speed(0.01F)
                        .min(0F)
                        .max(10F)
                        .w(100)
                        .id("slipperiness")
                        .build(),

                DragScalar.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.velocityMultiplier").getString())
                        .defaultValue(1F)
                        .dataType(ImGuiDataType.Float)
                        .speed(0.01F)
                        .min(0F)
                        .max(10F)
                        .w(80)
                        .id("velocityMultiplier")
                        .build(),

                DragScalar.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.jumpVelocityMultiplier").getString())
                        .defaultValue(1F)
                        .dataType(ImGuiDataType.Float)
                        .speed(0.01F)
                        .min(0F)
                        .max(10F)
                        .w(80)
                        .id("jumpVelocityMultiplier")
                        .build(),

                DragScalar.builder()
                        .label(Text.translatable("imgui.cubecode.factory.block.luminance").getString())
                        .defaultValue(1)
                        .speed(1)
                        .min(0)
                        .max(15)
                        .w(80)
                        .id("luminance")
                        .build(),

                Button.builder()
                        .title(Text.translatable("imgui.cubecode.factory.block.add").getString())
                        .wh(180, 20)
                        .id("add")
                        .callback(() -> {
                            ImString id = this.getVariable("id");
                            ImString name = this.getVariable("name");
                            ImString texture = this.getVariable("texture");
                            ImString dropsLike = this.getVariable("dropsLike");
                            ImFloat hardness = this.getVariable("hardness");
                            ImFloat resistance = this.getVariable("resistance");
                            ImFloat slipperiness = this.getVariable("slipperiness");
                            ImFloat jumpVelocityMultiplier = this.getVariable("jumpVelocityMultiplier");
                            ImFloat velocityMultiplier = this.getVariable("velocityMultiplier");
                            ImInt luminance = this.getVariable("luminance");

                            BlockManager.DefaultBlock defaultBlock = new BlockManager.DefaultBlock();
                            defaultBlock.id = id.get();
                            defaultBlock.name = name.get();
                            defaultBlock.texture = texture.get();
                            defaultBlock.dropsLike = dropsLike.get();
                            defaultBlock.hardness = hardness.get();
                            defaultBlock.resistance = resistance.get();
                            defaultBlock.slipperiness = slipperiness.get();
                            defaultBlock.jumpVelocityMultiplier = jumpVelocityMultiplier.get();
                            defaultBlock.velocityMultiplier = velocityMultiplier.get();
                            defaultBlock.luminance = luminance.get();

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();

                            ClientPlayNetworking.send(NetworkingPackets.CREATE_ELEMENT_FILE_C2S_PACKET, PacketByteBufUtils.createElementFileByteBuf("/factory/content/block/"+id.get()+".json", gson.toJson(defaultBlock)));
                        })
                        .build(),

                Button.builder()
                        .title(Text.translatable("imgui.cubecode.factory.block.reset").getString())
                        .wh(200, 20)
                        .id("reset")
                        .callback(() -> {
                            this.setVariable("id", new ImString(30));
                            this.setVariable("name", new ImString(30));
                            this.setVariable("texture", new ImString(30));
                            this.setVariable("dropsLike", new ImString(30));
                            this.setVariable("hardness", new ImFloat(0.5F));
                            this.setVariable("resistance", new ImFloat(0.5F));
                            this.setVariable("slipperiness", new ImFloat(0.6F));
                            this.setVariable("jumpVelocityMultiplier", new ImFloat(1F));
                            this.setVariable("velocityMultiplier", new ImFloat(1F));
                            this.setVariable("luminance", new ImInt(1));
                        }).build()
        ).render(this);
    }
}