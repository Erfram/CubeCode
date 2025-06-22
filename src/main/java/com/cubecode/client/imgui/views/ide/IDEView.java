package com.cubecode.client.imgui.views.ide;

import com.cubecode.CubeCode;
import com.cubecode.api.project.nodes.FileNode;
import com.cubecode.api.project.nodes.FolderNode;
import com.cubecode.api.project.nodes.IDENode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.api.project.scripts.Script;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.ScriptRunC2SPacket;
import com.cubecode.network.packets.server.ScriptSaveC2SPacket;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class IDEView extends View {
    private final TextEditor codeEditor = new TextEditor();

    private List<IDENode> nodes;

    private IDENode preSelectedNode;
    private IDENode selectedNode;

    public IDEView(List<IDENode> nodes) {
        this.nodes = nodes;
    }

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
                float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
                this.renderTitleBar(cig);

                Float splitter = this.getVariable("splitter");

                if (splitter != null) {
                    this.renderFileExplorer(cig, availableWidth * splitter);
                }

                ImGui.sameLine();
                cig.splitter("splitter", availableWidth, ImGui.getItemRectMaxY());
                ImGui.sameLine();

                this.renderCode(cig);
            })
            .render(this);
    }

    private void renderTitleBar(CubeImGui cig) {
        CubeImGui.child("title_bar", 0, ImGui.getTextLineHeight() + 2 * ImGui.getStyle().getFramePaddingY() + 2 * ImGui.getStyle().getWindowPaddingY(), true, () -> {
            if (ImGui.button("START")) {
                this.runScript();
            }
        });
    }

    private void runScript() {
        this.saveScript();
        Dispatcher.sendToServer(new ScriptRunC2SPacket(this.selectedNode.getPath()));
    }

    private void renderFileExplorer(CubeImGui cig, float width) {
        CubeImGui.child("file_explorer", width, 0, true, () -> {
            boolean treeProject = ImGui.treeNodeEx("##project", ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen);
            ImGui.sameLine(0, 4);
            ImGui.text("project");
            if (treeProject) {
                for (IDENode node : this.nodes) {
                    switch (node.getType()) {
                        case FOLDER -> this.renderFolderNode((FolderNode) node);
                        case FILE -> this.renderFileNode((FileNode) node);
                        case SCRIPT -> this.renderScriptNode((ScriptNode) node);
                    }
                }

                ImGui.treePop();
            }
        });
    }

    private void renderFolderNode(FolderNode folderNode) {
        int flag = folderNode.getChildren().isEmpty() ? ImGuiTreeNodeFlags.Leaf : ImGuiTreeNodeFlags.None;
        boolean treeFolder = ImGui.treeNodeEx("##"+folderNode.getName(), flag | ImGuiTreeNodeFlags.SpanAvailWidth);

        ImGui.sameLine(0, 4);
        ImGui.text(folderNode.getName());

        if (treeFolder) {
            for (IDENode child : folderNode.getChildren()) {
                switch (child.getType()) {
                    case FOLDER -> renderFolderNode((FolderNode) child);
                    case SCRIPT -> renderScriptNode((ScriptNode) child);
                }
            }

            ImGui.treePop();
        }
    }

    private void renderFileNode(FileNode node) {
        boolean isSelected = node.getPath().equals(this.preSelectedNode == null ? null : this.preSelectedNode.getPath());
        boolean selectableScript = ImGui.selectable("##"+node.getName(), isSelected, ImGuiSelectableFlags.AllowDoubleClick);

        ImGui.sameLine(0, 4);
        ImGui.text(node.getName());

        if (selectableScript) {
            this.selectNode(node);
        }
    }

    private void renderScriptNode(ScriptNode node) {
        boolean isSelected = node.getPath().equals(this.preSelectedNode == null ? null : this.preSelectedNode.getPath());

        boolean selectableScript = ImGui.selectable("##"+node.getName(), isSelected, ImGuiSelectableFlags.AllowDoubleClick);

        ImGui.sameLine(0, 4);
        ImGui.text(node.getName().substring(0, node.getName().lastIndexOf('.')));

        if (selectableScript) {
            this.selectNode(node);
        }
    }

    private void selectNode(IDENode node) {
        if (node instanceof ScriptNode scriptNode) {
            this.preSelectedNode = node;

            if (ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                this.saveScript();

                this.codeEditor.setText(scriptNode.getScript().getCode());
                this.codeEditor.setSelection(0, 0, 0,0,0);

                this.selectedNode = node;
            }
        } else if (node instanceof FileNode fileNode) {
            this.preSelectedNode = fileNode;
        }
    }

    private void renderCode(CubeImGui cig) {
        CubeImGui.child("code", 0, 0, true, () -> {
            if (this.selectedNode != null) {
                this.codeEditor.render(CubeCode.MOD_ID);
            }
        });
    }

    private void saveScript() {
        if (this.selectedNode instanceof ScriptNode scriptNode) {
            scriptNode.getScript().setCode(this.codeEditor.getText().replaceAll("\\n+$", ""));
            Dispatcher.sendToServer(new ScriptSaveC2SPacket(scriptNode));
        }
    }
}
