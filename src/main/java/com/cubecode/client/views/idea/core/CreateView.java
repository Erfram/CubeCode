package com.cubecode.client.views.idea.core;

import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Button;
import com.cubecode.client.imgui.components.InputText;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.idea.utils.Extension;
import com.cubecode.client.views.idea.utils.node.*;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.CreateFolderC2SPacket;
import com.cubecode.network.packets.server.CreateScriptC2SPacket;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CreateView extends View {
    private final int viewWidth = 180;
    private final int viewHeight = 75;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    private NodeType type;
    private FolderNode folderNode;

    public CreateView(FolderNode folderNode, NodeType type) {
        this.type = type;
        this.folderNode = folderNode;
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public String getName() {
        return String.format("##Create" + "##%s", uniqueID);
    }

    @Override
    public void render() {
        ImGui.pushStyleColor(ImGuiCol.Border, 255, 255, 255, 255);
        Window.create()
                .flags(ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)
                .title(getName())
            .draw(
                InputText.builder()
                    .rxy(0.175f, 0.2f)
                    .id("name")
                    .build(),
                Button.builder()
                        .rxy(0.7f, 0.6f)
                        .title(Text.translatable("imgui.cubecode.windows.codeEditor.file.create.button.create.title").getString())
                        .callback(() -> {
                            String name = ((ImString) this.getVariable("name")).get();

                            if (this.type == NodeType.SCRIPT) {
                                name = name.endsWith(".js") ? name : name + ".js";
                            }

                            for (CubeCodeIDEAView view : ImGuiLoader.getViews(CubeCodeIDEAView.class)) {
                                if (folderNode != null) {
                                    if (NodeUtils.hasNodeByPath(view.nodes, folderNode.getPath() + "/" + name))
                                        return;
                                } else {
                                    if (NodeUtils.hasNodeByPath(view.nodes, "/" + name))
                                        return;
                                }

                                if (this.type == NodeType.FOLDER) {
                                    FolderNode node = new FolderNode(name);

                                    if (folderNode != null) {
                                        FolderNode findNode = (FolderNode) NodeUtils.findNodeByPath(view.nodes, folderNode.getPath());

                                        findNode.addChild(node);
                                    } else {
                                        view.nodes.add(node);
                                    }

                                    view.sortNodes();

                                } else {
                                    ScriptNode scriptNode = new ScriptNode(
                                            new Script(name, ProjectManager.DEFAULT_SCRIPT),
                                            Extension.JAVASCRIPT
                                    );

                                    if (folderNode != null) {
                                        FolderNode findNode = (FolderNode) NodeUtils.findNodeByPath(view.nodes, folderNode.getPath());

                                        findNode.addChild(scriptNode);
                                    } else {
                                        view.nodes.add(scriptNode);
                                    }

                                    view.sortNodes();
                                }
                            }

                            ImGuiLoader.removeView(this);

                            String path = folderNode == null ? "" : folderNode.getPath();

                            Dispatcher.sendToServer(this.type == NodeType.FOLDER ? new CreateFolderC2SPacket(name, path) : new CreateScriptC2SPacket(name, path));
                        })
                        .build()
            )
            .callback(() -> {
                if (!ImGui.isWindowFocused()) {
                    ImGuiLoader.removeView(this);
                }
            })
            .render(this);
        ImGui.popStyleColor();
    }
}
