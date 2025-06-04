package com.cubecode.client.imgui.views.ide;

import com.cubecode.CubeCode;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import imgui.extension.texteditor.TextEditor;

public class IDEView extends View {
    private final TextEditor codeEditor = new TextEditor();

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render() {
        Window.create()
            .callback(() -> {
                renderCode();
            })
            .render(this);
    }

    private void renderCode() {
        codeEditor.render(CubeCode.MOD_ID);
    }
}
