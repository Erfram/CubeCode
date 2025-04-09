package com.cubecode.client.views.ide.core;

import com.cubecode.api.scripts.Script;
import com.cubecode.network.packets.all.ScriptSaveC2SPacket;
import com.cubecode.scripting.ScriptExecutor;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.ide.utils.node.*;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.CreateScriptPacket;
import com.cubecode.utils.ScriptType;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;
import java.util.regex.Pattern;

public class CreateView extends View {
    private final int viewWidth = 180;
    private final int viewHeight = 110;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    private static final Pattern VALID_FILENAME_PATTERN = Pattern.compile("^[^<>:\"/\\\\|?*\\x00-\\x1F]*$");

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
        return String.format("Create File" + "##%s", uniqueID);
    }

    public ScriptType side = ScriptType.SERVER;

    @Override
    public void render() {
        ImGui.pushStyleColor(ImGuiCol.Border, 255, 255, 255, 255);
        Window.create()
                .flags(ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)
                .title(getName())
                .callback(() -> {
                    String idInputName = "##name"+this.getUniqueID();
                    this.putVariable(idInputName, new ImString(255));

                    if (!ImGui.isWindowFocused()) {
                        ImGuiLoader.removeView(this);
                    }

                    ImGui.setCursorPos(ImGui.getWindowWidth() * 0.175f, ImGui.getWindowHeight() * (this.type == NodeType.SCRIPT ? 0.25f : 0.5f));
                    ImGui.inputText("##name", this.getVariable(idInputName));

                    if (this.type == NodeType.SCRIPT) {
                        ImGui.separator();
                        ImGui.spacing();

                        if (ImGui.selectable("Server", this.side == ScriptType.SERVER)) {
                            this.side = ScriptType.SERVER;

                            if (ImGui.isMouseDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                                this.createScript();
                            }
                        }

                        if (ImGui.selectable("Client", this.side == ScriptType.CLIENT)) {
                            this.side = ScriptType.CLIENT;

                            if (ImGui.isMouseDoubleClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                                this.createScript();
                            }
                        }
                    }

                    if (ImGui.isKeyPressed(GLFW.GLFW_KEY_ENTER)) {
                        this.createScript();
                    }
                })
            .render(this);
        ImGui.popStyleColor();
    }

    private void createScript() {
        String uuid = UUID.randomUUID().toString();
        String name = ((ImString) this.getVariable("##name"+this.getUniqueID())).get();

        if (name.isEmpty() || name.length() > 255 || !VALID_FILENAME_PATTERN.matcher(name).matches())
            return;

        if (this.type == NodeType.SCRIPT) {
            name = name.endsWith(".script") ? name : name + ".script";
        }

        for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
            if (folderNode != null) {
                if (NodeUtils.hasNodeByPathIgnoreCase(view.nodes, folderNode.getPath() + "/" + name))
                    return;
            } else {
                if (NodeUtils.hasNodeByPathIgnoreCase(view.nodes, "/" + name))
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
                    new Script(uuid, name,
                        this.side == ScriptType.SERVER ? ScriptExecutor.DEFAULT_SCRIPT : ScriptExecutor.DEFAULT_CLIENT_SCRIPT,
                        this.side
                    )
                );
                Dispatcher.sendToServer(new ScriptSaveC2SPacket(scriptNode.getScript(), scriptNode.getPath()));

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

        String path = folderNode == null ? "/" : folderNode.getPath();

    }
}
