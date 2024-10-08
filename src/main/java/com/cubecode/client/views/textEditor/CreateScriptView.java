package com.cubecode.client.views.textEditor;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Button;
import com.cubecode.client.imgui.components.InputText;
import com.cubecode.client.imgui.components.Text;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.CreateScriptC2SPacket;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;

public class CreateScriptView extends View {
    private final int viewWidth = 200;
    private final int viewHeight = 120;
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
    public String getName() {
        return String.format(net.minecraft.text.Text.translatable("imgui.cubecode.windows.codeEditor.file.create.name").getString() + "##%s", uniqueID);
    }

    @Override
    public void render() {
        Window.create()
                .flags(ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDocking)
                .title(getName())
            .draw(
                Text.builder()
                    .title(net.minecraft.text.Text.translatable("imgui.cubecode.windows.codeEditor.file.create.text.name").getString())
                    .rxy(0.5f, 0.3f)
                    .build(),
                InputText.builder()
                    .rxy(0.2f, 0.5f)
                    .id("scriptName")
                    .build(),
                Button.builder()
                        .rxy(0.8f, 0.75f)
                        .title(net.minecraft.text.Text.translatable("imgui.cubecode.windows.codeEditor.file.create.button.create.title").getString())
                        .callback(() -> Dispatcher.sendToServer(new CreateScriptC2SPacket(
                                ((ImString)this.getVariable("scriptName")).get()
                            )
                        ))
                        .build()
            )
            .render(this);
    }
}
