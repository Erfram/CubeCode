package com.cubecode.client.views.textEditor;

import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.NetworkingPackets;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class TextEditorView extends View {
    private final TextEditor CODE_EDITOR = new TextEditor();
    private final ImBoolean CLOSE = new ImBoolean(true);

    private final int viewWidth = 700;
    private final int viewHeight = 400;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    @Override
    public String getName() {
        return String.format("Code Editor##%s", uniqueID);
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
        //CODE_EDITOR.setPalette(JavaScriptDefinition.buildPallet());
        //CODE_EDITOR.setLanguageDefinition(JavaScriptDefinition.build());
        CODE_EDITOR.setLanguageDefinition(JavaScriptDefinition.build());
        CODE_EDITOR.setShowWhitespaces(false);
        CODE_EDITOR.setTabSize(4);
        CODE_EDITOR.setPalette(JavaScriptDefinition.buildPallet());

        ClientPlayNetworking.send(NetworkingPackets.UPDATE_SCRIPTS_C2S_PACKET, PacketByteBufs.empty());
    }

    @Override
    public void render() {
        Window.create()
                .title(getName())
                .flags(ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.MenuBar)
                .callback(() -> {
                    CubeImGui.manageDocking(this);
                    if (!CLOSE.get()) {
                        ImGuiLoader.removeView(this);
                    }

                    renderMenuBar();

                    CubeImGui.beginChild("Left Pane", 200, 0, true, this::renderList);

                    ImGui.sameLine();

                    CubeImGui.beginChild("CubeCode IDEA", 0, 0, true, () -> {
                        CODE_EDITOR.render("CodeEditor");
                    });
                })
                .render(this);
    }


    public static List<String> scriptsName = new ArrayList<>();
    public static List<Script> scripts = new ArrayList<>();
    private int selectedScript = 0;

    private void renderList() {
        ImGui.text("Scripts");
        ImGui.separator();

        for (int i = 0; i < scriptsName.size(); i++) {
            if (ImGui.selectable(scriptsName.get(i), selectedScript == i)) {
                String textToSave = CODE_EDITOR.getText();

                CODE_EDITOR.setText(scripts.get(selectedScript).code);
            }
        }
    }

    private void renderMenuBar() {
        if (ImGui.beginMenuBar()) {
            renderFileMenu();
            renderEditMenu();
            ImGui.endMenuBar();
        }
    }

    private void renderFileMenu() {
        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Run")) {
                String code = CODE_EDITOR.getText();

                ClientPlayNetworking.send(NetworkingPackets.RUN_SCRIPT_C2S_PACKET, PacketByteBufs.create().writeString(code));
            }

            if (ImGui.menuItem("Save")) {
                String script = CODE_EDITOR.getText();

                PacketByteBuf buf = PacketByteBufs.create();

                String scriptName = scriptsName.get(selectedScript);

                buf.writeString(scriptName);
                buf.writeString(script);

                ClientPlayNetworking.send(NetworkingPackets.SAVE_SCRIPT_C2S_PACKET, buf);
            }
            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Documentation")) {
            if (ImGui.menuItem("Open")) {
                ImGuiLoader.pushView(new DocumentationView());
            }

            ImGui.endMenu();
        }
    }

    private void renderEditMenu() {
        if (ImGui.beginMenu("Edit")) {
            final boolean readOnly = CODE_EDITOR.isReadOnly();
            if (ImGui.menuItem("Read-only mode", "", readOnly)) {
                CODE_EDITOR.setReadOnly(!readOnly);
            }

            ImGui.separator();

            if (ImGui.menuItem("Undo", "ALT-Backspace", !readOnly && CODE_EDITOR.canUndo())) {
                CODE_EDITOR.undo(1);
            }

            if (ImGui.menuItem("Redo", "Ctrl-Y", !readOnly && CODE_EDITOR.canRedo())) {
                CODE_EDITOR.redo(1);
            }

            ImGui.separator();

            if (ImGui.menuItem("Copy", "Ctrl-C", CODE_EDITOR.hasSelection())) {
                CODE_EDITOR.copy();
            }
            if (ImGui.menuItem("Cut", "Ctrl-X", !readOnly && CODE_EDITOR.hasSelection())) {
                CODE_EDITOR.cut();
            }
            if (ImGui.menuItem("Delete", "Del", !readOnly && CODE_EDITOR.hasSelection())) {
                CODE_EDITOR.delete();
            }
            if (ImGui.menuItem("Paste", "Ctrl-V", !readOnly && ImGui.getClipboardText() != null)) {
                CODE_EDITOR.paste();
            }

            ImGui.endMenu();
        }
    }
}