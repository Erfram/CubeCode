package com.cubecode.client.views.textEditor;

import com.cubecode.api.scripts.Script;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.DeleteScriptC2SPacket;
import com.cubecode.network.packets.server.RunScriptC2SPacket;
import com.cubecode.network.packets.server.SaveScriptC2SPacket;
import com.cubecode.network.packets.server.UpdateScriptsC2SPacket;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.CopyOnWriteArrayList;

public class TextEditorView extends View {
    private final TextEditor CODE_EDITOR = new TextEditor();
    private final ImBoolean CLOSE = new ImBoolean(true);

    private final int viewWidth = 700;
    private final int viewHeight = 400;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    public static CopyOnWriteArrayList<Script> scripts = new CopyOnWriteArrayList<>();
    private static int selectedScript = -1;

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.windows.codeEditor.name").getString() + "##%s", uniqueID);
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

        Dispatcher.sendToServer(new UpdateScriptsC2SPacket(true));

        if (selectedScript != -1) {
            CODE_EDITOR.setText(scripts.get(selectedScript).code);
        }
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
                        if (selectedScript != -1) {
                            CODE_EDITOR.render(Text.translatable("imgui.cubecode.windows.codeEditor.name").getString());

                        }
                    });
                })
                .onExit(this::saveScript)
                .render(this);
    }

    private void renderList() {
        ImGui.text(Text.translatable("imgui.cubecode.windows.codeEditor.scripts.title").getString());
        ImGui.separator();

        for (int i = 0; i < scripts.size(); i++) {
            if (ImGui.selectable(scripts.get(i).name, selectedScript == i)) {
                saveScript();
                selectedScript = i;
                CODE_EDITOR.setText(scripts.get(selectedScript).code);
            }


            //Mouse Right CLick
            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                selectedScript = i;
                ImGui.openPopup("script_context_menu_" + i);
            }
        }

        runContextMenu();
    }

    private void renderMenuBar() {
        if (ImGui.beginMenuBar()) {
            renderFileMenu();
            renderEditMenu();
            ImGui.endMenuBar();
        }
    }

    private void renderFileMenu() {
        if (ImGui.beginMenu(Text.translatable("imgui.cubecode.windows.codeEditor.file.title").getString())) {
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.file.run.title").getString())) {
                String code = CODE_EDITOR.getText().substring(0, CODE_EDITOR.getText().length() - 1);
                Script script = scripts.get(selectedScript);
                script.code = code;
                Dispatcher.sendToServer(new RunScriptC2SPacket(script));
                saveScript();
            }

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.file.create.title").getString())) {
                ImGuiLoader.pushView(new CreateScriptView());
            }

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.file.save.title").getString())) {
                saveScript();
            }
            ImGui.endMenu();
        }

        if (ImGui.beginMenu(Text.translatable("imgui.cubecode.windows.codeEditor.documentation.title").getString())) {
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.documentation.open.title").getString())) {
                ImGuiLoader.pushView(new DocumentationView());
            }

            ImGui.endMenu();
        }
    }

    private void renderEditMenu() {
        if (ImGui.beginMenu(Text.translatable("imgui.cubecode.windows.codeEditor.edit.title").getString())) {
            final boolean readOnly = CODE_EDITOR.isReadOnly();
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.readOnlyMode.title").getString(), "", readOnly)) {
                CODE_EDITOR.setReadOnly(!readOnly);
            }

            ImGui.separator();

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.undo.title").getString(), "ALT-Backspace", !readOnly && CODE_EDITOR.canUndo())) {
                CODE_EDITOR.undo(1);
            }

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.redo.title").getString(), "Ctrl-Y", !readOnly && CODE_EDITOR.canRedo())) {
                CODE_EDITOR.redo(1);
            }

            ImGui.separator();

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.copy.title").getString(), "Ctrl-C", CODE_EDITOR.hasSelection())) {
                CODE_EDITOR.copy();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.cut.title").getString(), "Ctrl-X", !readOnly && CODE_EDITOR.hasSelection())) {
                CODE_EDITOR.cut();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.delete.title").getString(), "Del", !readOnly && CODE_EDITOR.hasSelection())) {
                CODE_EDITOR.delete();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.paste.title").getString(), "Ctrl-V", !readOnly && ImGui.getClipboardText() != null)) {
                CODE_EDITOR.paste();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.save.title").getString(), "Ctrl-S", !readOnly)) {
                saveScript();
            }

            ImGui.endMenu();
        }
    }

    public void runContextMenu() {
        if (ImGui.beginPopup("script_context_menu_" + selectedScript)) {
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.scripts.context.delete.title").getString())) {
                Dispatcher.sendToServer(new DeleteScriptC2SPacket(scripts.get(selectedScript).name));
                selectedScript = -1;
                CODE_EDITOR.setText("");
            }

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.scripts.context.rename.title").getString())) {
                ImGuiLoader.pushView(new RenameScriptView(scripts.get(selectedScript).name));
            }

            ImGui.endPopup();
        }
    }

    private void saveScript() {
        if (selectedScript != -1) {
            String scriptName = scripts.get(selectedScript).name;
            String script = CODE_EDITOR.getText().substring(0, CODE_EDITOR.getText().length() - 1);

            Dispatcher.sendToServer(new SaveScriptC2SPacket(new Script(scriptName, script)));
        }
    }

    @Override
    public void onClose() {
        saveScript();
    }

    @Override
    public void handleKeyPressed(int keyCode, int scanCode, int modifiers) {
        try {
            if (InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString().length() == 1) {
                char enteredChar = InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString().charAt(0);
                boolean isCtrlPressed = (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;

                if (isCtrlPressed && enteredChar == 'S') {
                    saveScript();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}