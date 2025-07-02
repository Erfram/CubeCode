package com.cubecode.client.views.ide.core;

import com.cubecode.client.imgui.textEditor.CubeTextEditor2;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;

public class CubeCodeIDEView2 extends View {
    CubeTextEditor2 textEditor = new CubeTextEditor2("IDE2");
    @Override
    protected void init() {
        super.init();
    }

    @Override
    public String getName() {
        return "CubeCode IDE2";
    }

    @Override
    protected void render() {
        Window.create()
            .callback(() -> {
                //textEditor.setDebugMode(true);
                textEditor.render();
//                textEditor.addContextItem(Icons.ACCEPT, "Послать нахуй", () -> {
//                    MinecraftClient.getInstance().player.sendMessage(Text.of("Пошёл нахуй"));
//                });
            })
            .render(this);
    }

    @Override
    public void onClose() {
        this.textEditor.onClose();
        super.onClose();
    }
}
