package com.cubecode.client.imgui.views.ide;

import com.cubecode.CubeCode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.api.project.scripts.Script;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.CodeRunC2SPacket;
import com.cubecode.network.packets.server.ScriptRunC2SPacket;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;

public class IDEView extends View {
    private final TextEditor codeEditor = new TextEditor();

    @Override
    protected void init() {
        super.init();

        this.codeEditor.setText("function server(c) {\n" +
                "\t\n" +
                "}");
    }

    @Override
    public String getName() {
        return "IDE##"+this.getUUID();
    }

    @Override
    public void render() {
        Window.create()
            .title(this.getName())
            .callback((cig) -> {
                if (ImGui.button("START")) {
                    Dispatcher.sendToServer(new CodeRunC2SPacket(this.codeEditor.getText()));
                }

                renderCode();
            })
            .render(this);
    }

    private void renderCode() {
        codeEditor.render(CubeCode.MOD_ID);
    }
}
