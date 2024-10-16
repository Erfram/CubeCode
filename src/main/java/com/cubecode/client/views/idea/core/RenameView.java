package com.cubecode.client.views.idea.core;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Button;
import com.cubecode.client.imgui.components.InputText;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.idea.utils.node.*;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.RenameElementC2SPacket;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class RenameView extends View {
    private final int viewWidth = 180;
    private final int viewHeight = 75;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    private IdeaNode ideaNode;

    public RenameView(IdeaNode ideaNode) {
        this.ideaNode = ideaNode;
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
        return String.format("##Rename" + "##%s", uniqueID);
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
                                .text(ideaNode.getName())
                                .id("name")
                                .build(),
                        Button.builder()
                                .rxy(0.5f, 0.6f)
                                .title(Text.translatable("imgui.cubecode.windows.CubeCodeIDEA.createView.rename").getString())
                                .callback(() -> {
                                    String name = ((ImString) this.getVariable("name")).get();

                                    if (ideaNode.getType() == NodeType.SCRIPT) {
                                        name = name.endsWith(".js") ? name : name + ".js";
                                    }

                                    boolean isSendToServer = true;

                                    IdeaNode copyNode = ideaNode.copy();

                                    copyNode.setName(name);

                                    for (CubeCodeIDEAView view : ImGuiLoader.getViews(CubeCodeIDEAView.class)) {
                                        if (!NodeUtils.hasNodeByPath(view.nodes, copyNode.getPath())) {
                                            if (isSendToServer) {
                                                Dispatcher.sendToServer(new RenameElementC2SPacket(ideaNode.getPath(), name));
                                            }

                                            isSendToServer = false;

                                            IdeaNode nodeByPath = NodeUtils.findNodeByPath(view.nodes, ideaNode.getPath());

                                            nodeByPath.setName(name);

                                            view.sortNodes();
                                        }
                                    }
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
