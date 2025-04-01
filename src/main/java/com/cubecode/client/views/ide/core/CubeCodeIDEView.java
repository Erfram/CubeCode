package com.cubecode.client.views.ide.core;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.ClientProperties;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.ide.DocumentationView;
import com.cubecode.client.views.ide.utils.ScriptDefinition;
import com.cubecode.client.views.ide.utils.ToolItem;
import com.cubecode.client.views.ide.utils.node.*;
import com.cubecode.client.views.ide.ScopeView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.SynchronizedClientScriptsPacket;
import com.cubecode.network.packets.server.*;
import com.cubecode.utils.*;
import com.google.gson.JsonObject;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CubeCodeIDEView extends View {
    private final TextEditor codeEditor = new TextEditor();

    public final CopyOnWriteArrayList<IdeaNode> nodes;

    private final Comparator<IdeaNode> comparator = (node1, node2) -> {
        boolean isDir1 = node1.getType() == NodeType.FOLDER;
        boolean isDir2 = node2.getType() == NodeType.FOLDER;

        if (isDir1 == isDir2) {
            return node1.getName().compareToIgnoreCase(node2.getName());
        }

        return isDir1 ? -1 : 1;
    };

    public IdeaNode preSelectedNode;
    public IdeaNode selectedNode;

    private IdeaNode saveNode;

    private ToolItem selectedToolItem = ToolItem.PROJECT;

    private boolean isIDEFocused = false;

    private boolean isDeletePopupModalRender = false;
    private boolean isSelectLibraryPopupModalRender = false;

    Map<Character, Character> pairs = new HashMap<>();

    public CubeCodeIDEView(CopyOnWriteArrayList<IdeaNode> nodes) {
        this.nodes = nodes;

        this.sortNodes();

        this.pairs.put('(', ')');
        this.pairs.put('[', ']');
        this.pairs.put('{', '}');
    }

    public void sortNodes() {
        this.nodes.stream()
                .filter(node -> node.getType() == NodeType.FOLDER)
                .forEach(node -> this.sortChildrenNode(((FolderNode)node).getChildren()));

        this.nodes.sort(this.comparator);
    }

    private void sortChildrenNode(List<IdeaNode> ideaNodes) {
        if (ideaNodes.isEmpty()) {
            return;
        }

        ideaNodes.stream()
                .filter(node -> node.getType() == NodeType.FOLDER)
                .forEach(node -> this.sortChildrenNode(((FolderNode)node).getChildren()));

        ideaNodes.sort(this.comparator);
    }

    @Override
    public void init() {
        super.init();

        this.codeEditor.setPalette(this.codeEditor.getDarkPalette());
    }

    @Override
    public String getName() {
        return "CubeCode IDE##"+this.getUniqueID();
    }

    @Override
    public void onClose() {
        this.saveContentScript();
    }

    @Override
    public void render() {
        Window.create()
                .flags(ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoScrollWithMouse)
                .title(getName())
                .onExit(this::onClose)
                .callback(() -> {
                    ImVec2 windowSize = ImGui.getWindowSize();
                    float windowPaddingX = ImGui.getStyle().getWindowPaddingX();
                    float availableWidth = windowSize.x - windowPaddingX;

                    this.renderMenuBar();

                    CubeImGui.beginChild("left_bar", 24, 0, false, this::renderLeftBar);

                    if (this.selectedToolItem == ToolItem.PROJECT) {
                        ImGui.sameLine();

                        Float variable = this.getVariable("vertical_splitter");

                        if (variable != null) {
                            CubeImGui.beginChild("file_manager", availableWidth * variable, 0, true, this::renderFileManager);
                        }

                        ImGui.sameLine();

                        CubeImGui.verticalSplitter(this, "vertical_splitter", availableWidth, ImGui.getItemRectMaxY());
                    }

                    ImGui.sameLine();

                    CubeImGui.beginChild("edit", 0, 0, true, this::renderEdit);

                    this.manageKeybinding();
                    this.manageMouse();
                    this.renderSelectLibraryPopupModal();
                    this.renderDeletePopupModal();
                })
                .render(this);
    }

    public void renderMenuBar() {
        if (ImGui.beginMenuBar()) {
            renderScope();

            this.renderDocumentation();

            if (this.selectedNode != null) {
                this.renderAddLibraryButton();
                this.renderRunScriptButton();
            }

            ImGui.endMenuBar();
        }
    }

    public void renderScope() {
        CubeImGui.imageButton(Icons.SERVER, Text.translatable("imgui.cubecode.windows.CubeCodeIDE.scope").getString(), 16, 16, () -> {
            if (this.selectedNode != null && this.selectedNode.getType() == NodeType.SCRIPT) {
                ImGuiLoader.pushView(new ScopeView(((ScriptNode)this.selectedNode).getScript()));
            }
        });
    }

    public void renderDocumentation() {
        CubeImGui.imageButton(Icons.BOOK, Text.translatable("imgui.cubecode.windows.CubeCodeIDE.documentation").getString(), 16, 16, () -> {
            ImGuiLoader.pushView(new DocumentationView(Documentation.parseDocs()));
        });
    }

    private void renderAddLibraryButton() {
        ImGui.setCursorPosX(ImGui.getWindowSize().x - 64);

        CubeImGui.imageButton(
                Icons.NBT_LIST,
                Text.translatable("Add Library").getString(),
                16, 16,
                this::renderSelectLibrary
        );
    }

    private void renderSelectLibrary() {
        this.isSelectLibraryPopupModalRender = true;
    }

    private void renderRunScriptButton() {
        ImGui.setCursorPosX(ImGui.getWindowSize().x - 32);

        CubeImGui.imageButton(
                Icons.START,
                Text.translatable("imgui.cubecode.windows.CubeCodeIDE.run_script").getString(),
                16, 16,
                this::actionRunScript
        );
    }

    public void renderLeftBar() {
        CubeImGui.imageButton(Icons.FOLDER, "Project", 16, 16, () -> {
            this.selectedToolItem = this.selectedToolItem == ToolItem.PROJECT ? ToolItem.UNKNOWN : ToolItem.PROJECT;
        });

        if (this.selectedToolItem == ToolItem.PROJECT) {
            ImGui.getWindowDrawList().addRect(
                    ImGui.getItemRectMin().x, ImGui.getItemRectMin().y,
                    ImGui.getItemRectMax().x, ImGui.getItemRectMax().y,
                    ImGui.colorConvertFloat4ToU32(255, 255, 255, 255)
            );
        }

        CubeImGui.imageButton(Icons.NBT_LIST, "Logger", 16, 16, () -> {
            ImGuiLoader.pushView(new LoggerView());
        });
    }

    public void renderFileManager() {
        boolean treeProject = ImGui.treeNodeEx("##project", ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen);

        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            this.preSelectedNode = null;
            ImGui.openPopup("file_context_menu");
        }

        ImGui.sameLine(0, 9);
        ImGui.image(Icons.MODULE.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text("project");

        if (treeProject) {
            for (IdeaNode node : this.nodes) {
                switch (node.getType()) {
                    case FOLDER -> renderFolder((FolderNode) node);
                    case SCRIPT -> renderScript((ScriptNode) node);
                }
            }

            runContextMenu();

            ImGui.treePop();
        }
    }

    private void renderFolder(FolderNode folderNode) {
        int flag = folderNode.getChildren().isEmpty() ? ImGuiTreeNodeFlags.Leaf : ImGuiTreeNodeFlags.None;
        boolean treeFolder = ImGui.treeNodeEx("##"+folderNode.getName(), flag | ImGuiTreeNodeFlags.SpanAvailWidth);

        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            this.preSelectedNode = folderNode;
            ImGui.openPopup("file_context_menu");
        }

        ImGui.sameLine(0, 9);
        ImGui.image(Icons.FOLDER.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(folderNode.getName());

        if (treeFolder) {
            for (IdeaNode child : folderNode.getChildren()) {
                switch (child.getType()) {
                    case FOLDER -> renderFolder((FolderNode) child);
                    case SCRIPT -> renderScript((ScriptNode) child);
                }
            }

            runContextMenu();

            ImGui.treePop();
        }
    }

    private void renderScript(ScriptNode scriptNode) {
        boolean isSelected = scriptNode.getPath().equals(this.preSelectedNode == null ? null : this.preSelectedNode.getPath());
        boolean selectableScript = ImGui.selectable("##"+scriptNode.getName(), isSelected, ImGuiSelectableFlags.AllowDoubleClick);

        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            this.preSelectedNode = scriptNode;
            ImGui.openPopup("file_context_menu");
        }
        Icons icon;
        if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            icon = scriptNode.getScript().getSide() == ScriptType.SERVER ? Icons.WORLD : Icons.CLIENT;
        } else {
            icon = Icons.JS;
        }

        ImGui.sameLine(0, 25);

        ImGui.image(icon.getGlId(), 16, 16);

        ImGui.sameLine(0, 4);
        ImGui.text(scriptNode.getName());

        if (selectableScript) {
            this.preSelectedNode = scriptNode;

            if (ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                this.saveContentScript();

                this.codeEditor.setLanguageDefinition(ScriptDefinition.javaScript());

                this.codeEditor.setText(scriptNode.getScript().getCode());
                this.codeEditor.setSelection(0, 0, 0,0,0);

                this.selectedNode = scriptNode;
            }
        }
    }

    private void manageKeybinding() {
        if (this.isIDEFocused) {
            if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || ImGui.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_D)) {
                    String currentLine = this.codeEditor.getCurrentLineText();

                    List<String> textLines = new ArrayList<>(List.of(this.codeEditor.getTextLines()));

                    textLines.add(this.codeEditor.getCursorPositionLine(), currentLine);

                    this.codeEditor.setText(String.join("\n", textLines));

                    this.codeEditor.setCursorPosition(this.codeEditor.getCursorPositionLine()+1, this.codeEditor.getCursorPositionColumn());
                }
            }
        } else {
            if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || ImGui.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL)) {
                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_X)) {
                    this.actionCut();
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_C)) {
                    this.actionSave();
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_V)) {
                    this.actionPaste();
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_R)) {
                    this.actionRename();
                }
            }

            if (ImGui.isKeyPressed(GLFW.GLFW_KEY_DELETE)) {
                this.actionDelete();
            }
        }

        if (ImGui.isKeyPressed(GLFW.GLFW_KEY_F6)) {
            this.actionRunScript();
        }
    }

    private void manageMouse() {
        if (ImGui.beginPopup("Context Menu IDE", ImGuiWindowFlags.AlwaysAutoResize)) {
            Map<Documentation.Chapter, String> methodVariations = Documentation.findMethodVariations(Documentation.parseDocs, this.codeEditor.getSelectedText());
            if (!methodVariations.isEmpty()) {
                methodVariations.forEach((chapter, method) -> CubeImGui.buttonAndImage(Icons.BOOK, chapter.name + " " + method, () -> {
                    DocumentationView documentationView = new DocumentationView(Documentation.parseDocs);
                    documentationView.selectedChapter = chapter;

                    ImGuiLoader.pushView(documentationView);

                    ImGui.closeCurrentPopup();
                }));

                ImGui.separator();
            }

            CubeImGui.buttonAndImage(Icons.BLOCK, "Вставить позицию блока", () -> {
                HitResult raycast = MinecraftClient.getInstance().player.raycast(4, 0, false);

                if (raycast.getType() == HitResult.Type.BLOCK) {
                    this.codeEditor.insertText(((BlockHitResult)raycast).getBlockPos().toShortString());
                }


                ImGui.closeCurrentPopup();
            });

            CubeImGui.buttonAndImage(Icons.VECTOR, "Вставить позицию игрока", () -> {
                this.codeEditor.insertText(MinecraftClient.getInstance().player.getBlockPos().toShortString());
                ImGui.closeCurrentPopup();
            });

            CubeImGui.buttonAndImage(Icons.PLAYER, "Вставить поворот игрока", () -> {
                this.codeEditor.insertText(new ScriptVector(
                        MinecraftClient.getInstance().player.getPitch(),
                        MinecraftClient.getInstance().player.getYaw(),
                        MinecraftClient.getInstance().player.getHeadYaw()
                ).toBlockPos().toShortString());
                ImGui.closeCurrentPopup();
            });

            ImGui.endPopup();
        }

        if (this.isIDEFocused) {
            if (ImGui.isMouseReleased(ImGuiMouseButton.Right)) {
                ImGui.openPopup("Context Menu IDE");
                Documentation.parseDocs();
            }
        }
    }

    private void renderDeletePopupModal() {
        if (this.isDeletePopupModalRender) {
            ImGui.pushStyleColor(ImGuiCol.Border, ColorUtils.rgbaToImguiColor(255, 255, 255, 255));
            ImGui.openPopup("confirm_delete");

            ImGui.pushStyleColor(ImGuiCol.ModalWindowDimBg, ImGui.getColorU32(0.0f, 0.0f, 0.0f, 0.5f)); // Устанавливаем цвет фона
            if (ImGui.beginPopupModal("confirm_delete", ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize)) {
                ImGui.textColored(255, 207, 72, 255, "Удалить " + this.preSelectedNode.getName() + "?");

                ImGui.separator();

                if (ImGui.button("Да")) {
                    this.isDeletePopupModalRender = false;
                    this.deleteFile();
                }

                ImGui.sameLine();

                if (ImGui.button("Нет")) {
                    this.isDeletePopupModalRender = false;
                    ImGui.closeCurrentPopup();
                }

                ImGui.endPopup();
            }
            ImGui.popStyleColor();
            ImGui.popStyleColor();
        }
    }

    private void renderSelectLibraryPopupModal() {
        if (this.isSelectLibraryPopupModalRender) {
            ImGui.openPopup("confirm_select_library");

            if (ImGui.beginPopupModal("confirm_select_library", ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize)) {
                String lengthPath = "";
                for (IdeaNode node : NodeUtils.getAllNodes(this.nodes)) {
                    if (lengthPath.length() <= node.getPath().length()) {
                        lengthPath = node.getPath();
                    }
                }

                for (ScriptNode node : NodeUtils.getAllNodes(this.nodes)) {
                    ImVec2 cursorPos = ImGui.getCursorPos();
                    String scriptName = node.getPath().substring(1);
                    Script serverScript = node.getScript();

                    if (!scriptName.equals(((ScriptNode)this.selectedNode).getScript().getName())) {
                        if (ImGui.selectable(scriptName)) {
                            if (serverScript.hasLibraryScript(scriptName)) {
                                CubeCodeClient.projectManager.removeLibraryScript(serverScript.getName(), scriptName);
                                Dispatcher.sendToServer(new RemoveLibraryC2SPacket(((ScriptNode)this.selectedNode), scriptName));
                            } else {
                                CubeCodeClient.projectManager.addLibraryScript(serverScript.getName(), scriptName);
                                Dispatcher.sendToServer(new AddLibraryC2SPacket(((ScriptNode)this.selectedNode), scriptName));
                            }

                            if (((ScriptNode)this.selectedNode).getScript().hasLibraryScript(scriptName)) {
                                ((ScriptNode)this.selectedNode).getScript().removeLibraryScript(scriptName);
                            } else {
                                ((ScriptNode)this.selectedNode).getScript().addLibraryScript(scriptName);
                            }

                            this.isSelectLibraryPopupModalRender = false;
                            ImGui.closeCurrentPopup();
                        }

                        if (serverScript.hasLibraryScript(scriptName)) {
                            ImGui.setCursorPos(cursorPos.x + ImGui.calcTextSize(lengthPath).x, cursorPos.y);

                            ImGui.image(Icons.PLUS.getGlId(), 16, 16);
                        }
                    }
                };

                if (ImGui.isAnyMouseDown()) {
                    if (ImGui.getMousePosX() < ImGui.getItemRectMinX() || ImGui.getMousePosX() > ImGui.getItemRectMaxX()) {
                        if (ImGui.getMousePosY() < ImGui.getItemRectMinY() || ImGui.getMousePosY() > ImGui.getItemRectMaxY()) {
                            this.isSelectLibraryPopupModalRender = false;
                        }
                    }
                }

                ImGui.endPopup();
            }
        }
    }

    private void saveContentScript() {
        if (this.selectedNode != null) {
            for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
                ScriptNode scriptNode = (ScriptNode) NodeUtils.findNodeByPath(view.nodes, this.selectedNode.getPath());

                if (scriptNode != null) {
                    scriptNode.setScript(new Script(scriptNode.getScript().getName(), this.codeEditor.getText().replaceAll("\\n+$", ""), scriptNode.getScript().getSide(), scriptNode.getScript().getLibraries()));
                }
            }

            Dispatcher.sendToServer(new SaveScriptC2SPacket((ScriptNode) this.selectedNode));

            List<Script> clientScripts = new ArrayList<>();

            for (IdeaNode node : this.nodes) {
                if (node.getType() == NodeType.SCRIPT) {
                    if (((ScriptNode)node).getScript().getSide() == ScriptType.CLIENT) {
                        clientScripts.add(((ScriptNode) node).getScript());
                    }
                } else {
                    this.scanFolder((FolderNode) node, clientScripts);
                }
            }

            List<Script> scripts = new ArrayList<>();

            for (Script script : clientScripts) {
                scripts.add(new Script(script.getName(), script.getCode(), ScriptType.CLIENT, script.getLibraries()));
            }

            CubeCodeClient.projectManager.setScripts(scripts);

            Dispatcher.sendToServer(new SynchronizedClientScriptsPacket(clientScripts));
        }
    }

    private void scanFolder(FolderNode folderNode, List<Script> serverScripts) {
        folderNode.getChildren().forEach(node -> {
            if (node.getType() == NodeType.SCRIPT) {
                if (((ScriptNode)node).getScript().getSide() == ScriptType.CLIENT) {
                    serverScripts.add(((ScriptNode) node).getScript());
                }
            } else {
                this.scanFolder((FolderNode) node, serverScripts);
            }
        });
    }

    private void runContextMenu() {
        if (ImGui.beginPopup("file_context_menu")) {
            if (this.preSelectedNode == null || this.preSelectedNode.getType() != NodeType.SCRIPT) {
                CubeImGui.menu(Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.create").getString(), Icons.HAMMER, 16, 16, () -> {
                    CubeImGui.menuItem(Text.translatable(
                            "imgui.cubecode.windows.CubeCodeIDE.context_menu.create.folder").getString(), Icons.FOLDER,
                            16, 16,
                            this::actionCreateFolder
                    );

                    ImGui.separator();

                    CubeImGui.menuItem(
                            Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.create.script").getString(), Icons.JS,
                            16, 16,
                            this::actionCreateScript
                    );

                    ImGui.endMenu();
                });
            }

            if (this.preSelectedNode == null) {
                ImGui.separator();
            }

            if (this.preSelectedNode == null) {
                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.paste"), Icons.PASTE,
                        16, 16,
                        128, 128, 128, 255,
                        "Ctrl + V",
                        this::actionModulePaste
                );
            }

            //SCRIPT
            if (this.preSelectedNode != null) {
                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.cut"), Icons.CUT,
                        16, 16,
                        128, 128, 128, 255,
                        "Ctrl + X",
                        this::actionCut
                );

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.copy"), Icons.COPY,
                        16, 16,
                        128, 128, 128, 255,
                        "Ctrl + C",
                        this::actionSave
                );

                if (this.preSelectedNode != null && this.preSelectedNode.getType() != NodeType.SCRIPT) {
                    CubeImGui.menuItemAndTooltip(
                            Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.paste"), Icons.PASTE,
                            16, 16,
                            128, 128, 128, 255,
                            "Ctrl + V",
                            this::actionPaste
                    );
                }

                ImGui.separator();

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.delete"), Icons.DELETE,
                        16, 16,
                        128, 128, 128, 255,
                        "Delete",
                        this::actionDelete
                );

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.rename"), Icons.EDIT,
                        16, 16,
                        128, 128, 128, 255,
                        "Ctrl + R",
                        this::actionRename
                );

                ImGui.separator();

                if (this.preSelectedNode != null && this.preSelectedNode.getType() == NodeType.SCRIPT) {
                    CubeImGui.menuItemAndTooltip(
                            Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.changeType"), Icons.RESET,
                            16, 16,
                            128, 128, 128, 255,
                            "",
                            this::actionChangeSide
                    );
                }

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.openInExplorer"), Icons.SEARCH,
                        16, 16,
                        128, 128, 128, 255,
                        "",
                        this::actionOpenExplorer
                );
            }

            ImGui.endPopup();
        }
    }

    private void actionCreateFolder() {
        ImGuiLoader.pushView(new CreateView((FolderNode) this.preSelectedNode, NodeType.FOLDER));
    }

    private void actionCreateScript() {
        ImGuiLoader.pushView(new CreateView((FolderNode) this.preSelectedNode, NodeType.SCRIPT));
    }

    private void actionModulePaste() {
        if (this.saveNode != null) {
            String path = "/" + this.saveNode.getName();
            for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
                if (!NodeUtils.hasNodeByPathIgnoreCase(view.nodes, path)) {
                    view.nodes.add(this.saveNode);

                    view.sortNodes();
                }
            }

            if (!NodeUtils.hasNodeByPathIgnoreCase(this.nodes, path)) {
                Dispatcher.sendToServer(new InsertElementC2SPacket(this.saveNode, ""));
            }
        }
    }

    private void actionPaste() {
        if (this.preSelectedNode == null)
            return;

        if (this.preSelectedNode.getType() == NodeType.SCRIPT)
            return;

        FolderNode folderNode = (FolderNode) this.preSelectedNode;

        if (this.saveNode != null) {
            String path = folderNode.getPath() + "/" + this.saveNode.getName();
            boolean isSendPacket = false;
            for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
                if (!NodeUtils.hasNodeByPathIgnoreCase(view.nodes, path)) {
                    IdeaNode node = this.saveNode.copy();

                    node.setPath(path);

                    folderNode.addChild(node);

                    view.sortNodes();

                    isSendPacket = true;
                } else {
                    isSendPacket = false;
                }
            }

            if (isSendPacket) {
                Dispatcher.sendToServer(new InsertElementC2SPacket(this.saveNode, folderNode.getPath()));
            }
        }
    }

    private void actionCut() {
        if (this.preSelectedNode != null) {
            this.saveNode = this.preSelectedNode;

            this.deleteFile();
        }
    }

    private void actionSave() {
        this.saveNode = this.preSelectedNode;
    }

    private void actionDelete() {
        this.isDeletePopupModalRender = true;
    }

    private void actionRename() {
        ImGuiLoader.pushView(new RenameView(this.preSelectedNode));
    }

    private void actionChangeSide() {
        ScriptType side = ((ScriptNode) this.preSelectedNode).getScript().getSide() == ScriptType.SERVER ? ScriptType.CLIENT : ScriptType.SERVER;
        String path = this.preSelectedNode.getPath();
        for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
            ScriptNode nodeByPath = (ScriptNode) NodeUtils.findNodeByPath(view.nodes, path);

            if (nodeByPath != null) {
                nodeByPath.getScript().setSide(side);
            }
        }

        CubeCodeClient.projectManager.setScriptSide(((ScriptNode)this.preSelectedNode).getScript().getName(), side);

        Dispatcher.sendToServer(new ChangeSideScriptC2SPacket((ScriptNode) this.preSelectedNode, side));
    }

    private void actionOpenExplorer() {
        File file = new File(CubeCode.cubeCodeDirectory, "project" + this.preSelectedNode.getPath().substring(0, this.preSelectedNode.getPath().lastIndexOf("/") + 1));

        if (file.exists()) {
            Util.getOperatingSystem().open(file);
        }
    }

    private void actionRunScript() {
        if (this.selectedNode != null) {
            if (this.selectedNode.getType() == NodeType.SCRIPT) {
                this.saveContentScript();
                ScriptNode scriptNode = (ScriptNode) this.selectedNode;
                if (scriptNode.getScript().getSide() == ScriptType.SERVER) {
                    Dispatcher.sendToServer(new RunScriptC2SPacket(scriptNode.getScript()));
                } else {
                    ClientProperties properties = ClientProperties.create(
                            scriptNode.getScript().getName(),
                            "client",
                            MinecraftClient.getInstance().player,
                            null,
                            MinecraftClient.getInstance().world);

                    try {
                        Script script = CubeCodeClient.projectManager.getScript(scriptNode.getScript().getName());
                        if (script != null) {
                            script.run(script.getName(), properties);
                        }
                    } catch (CubeCodeException e) {
                        MinecraftClient.getInstance().player.sendMessage(Text.of("§c"+e.getMessage()));
                    }
                }
            }
        }
    }

    private void deleteFile() {
        if (this.preSelectedNode != null) {
            String path = this.preSelectedNode.getPath();
            String parent = path.substring(0, path.lastIndexOf("/"));

            Dispatcher.sendToServer(new DeleteElementC2SPacket(path, this.preSelectedNode.getType()));

            for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
                IdeaNode nodeByPath = NodeUtils.findNodeByPath(view.nodes, parent);

                if (nodeByPath != null) {
                    ((FolderNode)nodeByPath).removeChild(this.preSelectedNode);
                } else {
                    view.nodes.remove(this.preSelectedNode);
                }

                if (view.preSelectedNode == view.selectedNode) {
                    view.selectedNode = null;
                }

                view.preSelectedNode = null;
            }
        }
    }

    private void renderEdit() {
        if (this.selectedNode == null)
            return;

        this.codeEditor.setLanguageDefinition(ScriptDefinition.javaScript());
        this.codeEditor.setPalette(ScriptDefinition.getJavaScriptPalette());

        this.codeEditor.setShowWhitespaces(CubeCodeConfig.getIdeaSettingsConfig().showWhitespaces);
        this.codeEditor.setReadOnly(CubeCodeConfig.getIdeaSettingsConfig().readOnly);
        this.codeEditor.setTabSize(CubeCodeConfig.getIdeaSettingsConfig().tabSize);

        this.codeEditor.render("IDEA");

        this.isIDEFocused = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);
    }

    @Override
    public JsonObject serializeData() {
        JsonObject jsonObject = new JsonObject();

        if (this.selectedNode != null) {
            jsonObject.addProperty("selectedNode", this.selectedNode.getPath());
        }

        return jsonObject;
    }

    @Override
    public void deserializeData(JsonObject jsonObject) {
        if (jsonObject.has("selectedNode")) {
            this.selectedNode = NodeUtils.findNodeByPath(this.nodes, jsonObject.get("selectedNode").getAsString());
        }
    }
}