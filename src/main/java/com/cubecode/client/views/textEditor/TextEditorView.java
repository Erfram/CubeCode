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
    private final TextEditor codeEditor = new TextEditor();
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
        codeEditor.setLanguageDefinition(JavaScriptDefinition.build());
        codeEditor.setShowWhitespaces(false);
        codeEditor.setTabSize(4);
        codeEditor.setPalette(JavaScriptDefinition.buildPallet());

        Dispatcher.sendToServer(new UpdateScriptsC2SPacket(true));

        if (selectedScript != -1) {
            codeEditor.setText(scripts.get(selectedScript).code);
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
                            codeEditor.render(Text.translatable("imgui.cubecode.windows.codeEditor.name").getString());

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
                codeEditor.setText(scripts.get(selectedScript).code);
                ScopeView view = ImGuiLoader.getView(ScopeView.class);
                if (view != null) {
                    view.updateScope();
                }
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
                String code = codeEditor.getText().substring(0, codeEditor.getText().length() - 1);
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

        if (ImGui.beginMenu(Text.translatable("imgui.cubecode.windows.codeEditor.tools.title").getString())) {
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.documentation.title").getString())) {
                ImGuiLoader.pushView(new DocumentationView());
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.scope.title").getString())) {
                ImGuiLoader.pushView(new ScopeView());
            }

            ImGui.endMenu();
        }
    }

    private void renderEditMenu() {
        if (ImGui.beginMenu(Text.translatable("imgui.cubecode.windows.codeEditor.edit.title").getString())) {
            final boolean readOnly = codeEditor.isReadOnly();
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.readOnlyMode.title").getString(), "", readOnly)) {
                codeEditor.setReadOnly(!readOnly);
            }

            ImGui.separator();

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.undo.title").getString(), "ALT-Backspace", !readOnly && codeEditor.canUndo())) {
                codeEditor.undo(1);
            }

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.redo.title").getString(), "Ctrl-Y", !readOnly && codeEditor.canRedo())) {
                codeEditor.redo(1);
            }

            ImGui.separator();

            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.copy.title").getString(), "Ctrl-C", codeEditor.hasSelection())) {
                codeEditor.copy();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.cut.title").getString(), "Ctrl-X", !readOnly && codeEditor.hasSelection())) {
                codeEditor.cut();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.delete.title").getString(), "Del", !readOnly && codeEditor.hasSelection())) {
                codeEditor.delete();
            }
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.edit.paste.title").getString(), "Ctrl-V", !readOnly && ImGui.getClipboardText() != null)) {
                codeEditor.paste();
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
                codeEditor.setText("");
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
            String script = codeEditor.getText().substring(0, codeEditor.getText().length() - 1);

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

    public TextEditor getCodeEditor() {
        return this.codeEditor;
    }

    public static int getSelectedScript() {
        return selectedScript;
    }
}