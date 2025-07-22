package com.cubecode.client.imgui.views.ide;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.project.nodes.FileNode;
import com.cubecode.api.project.nodes.FolderNode;
import com.cubecode.api.project.nodes.IDENode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.api.project.scripts.ScriptExecutor;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.Icons;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.IDEUpdatePacket;
import com.cubecode.network.packets.server.FileCreateC2SPacket;
import com.cubecode.network.packets.server.ScriptRunC2SPacket;
import com.cubecode.network.packets.server.ScriptSaveC2SPacket;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.*;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.List;

public class IDEView extends View {
    private final TextEditor codeEditor = new TextEditor();

    private List<IDENode> nodes;

    private IDENode preSelectedNode;
    private IDENode selectedNode;

    private Boolean isRenderFolderExplorer = true;
    private boolean isFocusedCodeEditor = false;

    private List<Tool> tools;

    public IDEView(List<IDENode> nodes) {
        this.nodes = nodes;
        this.tools = new ArrayList<>();
        this.sortNodes();
    }

    @Override
    protected void init() {
        super.init();

        this.codeEditor.setText(ScriptExecutor.DEFAULT_SCRIPT);
        this.tools.add(Tool.create("project", true, this.isRenderFolderExplorer, Icons.FOLDER, () -> {
            this.isRenderFolderExplorer = !this.isRenderFolderExplorer;
        }));
    }

    @Override
    public String getName() {
        return "IDE##"+this.getUUID();
    }

    @Override
    public void render() {
        Window.create()
            .title(this.getName())
            .onExit(this::saveScript)
            .flags(ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)
            .callback((cig) -> {
                float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();

                this.renderTitleBar(cig);

                Float splitter = this.getVariable("splitter");

                this.renderTool(cig);

                ImGui.sameLine();

                if (this.isRenderFolderExplorer) {
                    if (splitter != null) {
                        this.renderFileExplorer(cig, availableWidth * splitter);
                    }

                    ImGui.sameLine();
                    cig.splitter("splitter", availableWidth, ImGui.getItemRectMaxY());

                    ImGui.sameLine();
                }

                this.renderCode(cig);

                this.renderPopups(cig);

                this.handleMouse();
            })
            .render(this);
    }

    private void handleMouse() {
        if (this.isFocusedCodeEditor) {
            if (ImGui.isMouseClicked(ImGuiMouseButton.Right)) {

            }
        }
    }

    private void renderTitleBar(CubeImGui cig) {
        CubeImGui.child("title_bar", 0, ImGui.getTextLineHeight() + 2 * ImGui.getStyle().getFramePaddingY() + 2 * ImGui.getStyle().getWindowPaddingY(), true, () -> {
            cig.imageButton(Icons.START, this::runScript);
        });
    }

    private void runScript() {
        if (this.selectedNode != null) {
            this.saveScript();
            Dispatcher.sendToServer(new ScriptRunC2SPacket(this.selectedNode.getPath()));
        }
    }

    private void renderTool(CubeImGui cig) {
        CubeImGui.child("tool", cig.getPixels() + 2 * ImGui.getStyle().getItemSpacingX(), 0, true, () -> {
            ImDrawList drawList = ImGui.getWindowDrawList();

            for (Tool tool : this.tools) {
                cig.image(tool.icon, tool.runnable);

                if (ImGui.isItemHovered()) {
                    drawList.addRect(
                            ImGui.getItemRectMinX() - 3, ImGui.getItemRectMinY() - 3f,
                            ImGui.getItemRectMaxX() + 3, ImGui.getItemRectMaxY() + 3f,
                            ImGui.getColorU32(1, 1, 1, 1)
                    );
                }

                if (tool.variable) {
                    drawList.addRect(
                            ImGui.getItemRectMinX() - 3, ImGui.getItemRectMinY() - 3f,
                            ImGui.getItemRectMaxX() + 3, ImGui.getItemRectMaxY() + 3f,
                            ImGui.getColorU32(1, 1, 1, 1)
                    );
                    drawList.addRectFilled(
                            ImGui.getItemRectMinX() - 3, ImGui.getItemRectMinY() - 3f,
                            ImGui.getItemRectMaxX() + 3, ImGui.getItemRectMaxY() + 3f,
                            ImGui.getColorU32(0f, 0.2f, 0.5f, 0.5f)
                    );
                }
            }

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            cig.image(Icons.FOLDER, () -> {

            });
        });
    }

    private void renderFileExplorer(CubeImGui cig, float width) {
        CubeImGui.child("file_explorer", width, 0, true, () -> {
            boolean treeProject = ImGui.treeNodeEx("##project", ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen);

            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                this.preSelectedNode = null;
                cig.openPopup("file_context_menu");
            }

            ImGui.sameLine(0, 6);
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

        ImGui.sameLine(0, 5);
        CubeImGui._image(Icons.FOLDER);
        ImGui.sameLine(0, 6);
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

        ImGui.sameLine(0, 6);
        ImGui.text(node.getName());

        if (selectableScript) {
            this.selectNode(node);
        }
    }

    private void renderScriptNode(ScriptNode node) {
        boolean isSelected = node.getPath().equals(this.preSelectedNode == null ? null : this.preSelectedNode.getPath());

        boolean selectableScript = ImGui.selectable("##"+node.getName(), isSelected, ImGuiSelectableFlags.AllowDoubleClick);

        ImGui.sameLine(0, 0);
        CubeImGui._image(Icons.JS);
        ImGui.sameLine(0, 6);
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

                this.isFocusedCodeEditor = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);
            }
        });
    }

    private void saveScript() {
        if (this.selectedNode instanceof ScriptNode scriptNode) {
            scriptNode.getScript().setCode(this.codeEditor.getText().replaceAll("\\n+$", ""));
            Dispatcher.sendToServer(new ScriptSaveC2SPacket(scriptNode));
        }
    }

    private void renderPopups(CubeImGui cig) {
        this.renderFileContextMenu(cig);
        this.renderModals(cig);

        cig.initPopups();
    }

    private void renderModals(CubeImGui cig) {
        this.renderCreateFolder(cig);
    }

    private void renderFileContextMenu(CubeImGui cig) {
        cig.popup("file_context_menu", () -> {
            CubeImGui.menu("Создать", () -> {
                CubeImGui.menuItem("Папка", () -> {
                    cig.openPopup("Создание##create_folder");
                });

                ImGui.separator();

                CubeImGui.menuItem("Скрипт", () -> {
                    cig.openPopup("Создание##create_folder");
                });
            });
        });
    }

    private void renderCreateFolder(CubeImGui cig) {
        ImGui.setNextWindowPos(
                this.window.getWidth() / 2f,
                this.window.getHeight() / 2f,
                ImGuiCond.Always,
                0.5f, 0.5f
        );

        cig.modal("Создание##create_folder",
            ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.AlwaysAutoResize,
            () -> {
                ImString folderName = cig.inputTextWithHint("##Create_folder", "create_folder", "Название", (str) -> {
                });

                ImGui.separator();

                if (ImGui.button("Создать")) {
                    String path = folderName.get();
                    if (this.preSelectedNode != null) {
                        path += this.preSelectedNode.getPath().substring(1) + "/" + path;
                    }

                    Dispatcher.sendToServer(new FileCreateC2SPacket(path, true));
                    Dispatcher.sendToServer(new IDEUpdatePacket());
                    ImGui.closeCurrentPopup();
                }

                ImGui.sameLine();

                if (ImGui.button("Отмена")) {
                    ImGui.closeCurrentPopup();
                }
            }
        );
    }

    public void sortNodes() {
        this.nodes.stream()
                .filter(node -> node.getType() == IDENode.Type.FOLDER)
                .forEach(node -> this.sortChildrenNode(((FolderNode)node).getChildren()));

        this.nodes.sort((node1, node2) -> {
            boolean isDir1 = node1.getType() == IDENode.Type.FOLDER;
            boolean isDir2 = node2.getType() == IDENode.Type.FOLDER;

            if (isDir1 == isDir2) {
                return node1.getName().compareToIgnoreCase(node2.getName());
            }

            return isDir1 ? -1 : 1;
        });
    }

    private void sortChildrenNode(List<IDENode> ideaNodes) {
        if (ideaNodes.isEmpty()) {
            return;
        }

        ideaNodes.stream()
                .filter(node -> node.getType() == IDENode.Type.FOLDER)
                .forEach(node -> this.sortChildrenNode(((FolderNode)node).getChildren()));

        ideaNodes.sort((node1, node2) -> {
            boolean isDir1 = node1.getType() == IDENode.Type.FOLDER;
            boolean isDir2 = node2.getType() == IDENode.Type.FOLDER;

            if (isDir1 == isDir2) {
                return node1.getName().compareToIgnoreCase(node2.getName());
            }

            return isDir1 ? -1 : 1;
        });
    }

    public void update(List<IDENode> nodes) {
        this.nodes = nodes;
        this.sortNodes();
    }

    private static class Tool {
        String id;
        boolean isPM;
        Boolean variable;
        Icons icon;
        Runnable runnable;

        public Tool(String id, boolean isPM, Boolean variable, Icons icon, Runnable runnable) {
            this.id = id;
            this.isPM = isPM;
            this.variable = variable;
            this.icon = icon;
            this.runnable = runnable;
        }

        public static Tool create(String id, boolean isPM, Boolean variable, Icons icon, Runnable runnable) {
            return new Tool(id, isPM, variable, icon, runnable);
        }
    }
}
