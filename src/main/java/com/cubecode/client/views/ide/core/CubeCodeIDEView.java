package com.cubecode.client.views.ide.core;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.ClientProperties;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.ScriptExecutor;
import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.CubeTextEditor;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.ide.DocumentationView;
import com.cubecode.client.views.ide.utils.ScriptDefinition;
import com.cubecode.client.views.ide.utils.ToolItem;
import com.cubecode.client.views.ide.utils.node.*;
import com.cubecode.client.views.ide.ScopeView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.CreateScriptPacket;
import com.cubecode.network.packets.all.SynchronizedClientScriptsPacket;
import com.cubecode.network.packets.server.*;
import com.cubecode.utils.*;
import com.google.gson.JsonObject;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImString;
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
import java.util.regex.Pattern;

public class CubeCodeIDEView extends View {
    private final CubeTextEditor codeEditor = new CubeTextEditor("IDE");

    public CopyOnWriteArrayList<IdeaNode> nodes;

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

    private boolean isCreateNodeModalRender = false;
    private boolean isDeleteNodeModalRender = false;
    private boolean isSelectLibraryModalRender = false;
    private boolean isReplacementRender = false;
    private boolean isFinderRender = false;

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
        this.codeEditor.setColorizerEnable(true);
        this.codeEditor.setImGuiChildIgnored(true);
        this.codeEditor.setDebugMode(true);
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

                    CubeImGui.beginChild("left_bar", 1.5f * ImGui.getFontSize(), 0, false, this::renderLeftBar);

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

                    CubeImGui.beginChild("right_panel", 0, 0, true, () -> {
                        if (this.isReplacementRender || this.isFinderRender) {
                            this.renderFinder();
                        }

                        this.renderEdit();

                        //CubeImGui.beginChild("edit", 0, 0, false, ImGuiWindowFlags.NoMove | ImGuiWindowFlags.HorizontalScrollbar, this::renderEdit);
                    });

                    this.manageKeybinding();
                    this.renderSelectLibraryPopupModal();
                    this.renderDeletePopupModal();
                    this.renderCreateNodePopupModal();
                })
                .render(this);
    }

    public void renderMenuBar() {
        if (ImGui.beginMenuBar()) {
            this.renderScope();

            this.renderDocumentation();

            if (this.selectedNode != null) {
                ImGui.setCursorPosX(ImGui.getWindowWidth() - ImGui.getFontSize() * 2 - ImGui.getStyle().getItemSpacingX() * 4);
                ImGui.beginGroup();
                    this.renderAddLibraryButton();
                    ImGui.sameLine();
                    this.renderRunScriptButton();
                ImGui.endGroup();
            }

            ImGui.endMenuBar();
        }
    }

    public void renderScope() {
        float fontSize = ImGui.getFontSize();
        CubeImGui.imageButton(Icons.SERVER, Text.translatable("imgui.cubecode.windows.CubeCodeIDE.scope").getString(), fontSize, fontSize, () -> {
            if (this.selectedNode != null && this.selectedNode.getType() == NodeType.SCRIPT) {
                ImGuiLoader.pushView(new ScopeView(((ScriptNode)this.selectedNode).getScript()));
            }
        });
    }

    public void renderDocumentation() {
        float fontSize = ImGui.getFontSize();
        CubeImGui.imageButton(Icons.BOOK, Text.translatable("imgui.cubecode.windows.CubeCodeIDE.documentation").getString(), fontSize, fontSize, () -> {
            ImGuiLoader.pushView(new DocumentationView(Documentation.parseDocs()));
        });
    }

    private void renderAddLibraryButton() {
        float fontSize = ImGui.getFontSize();
        CubeImGui.imageButton(
                Icons.NBT_LIST,
                Text.translatable("Add Library").getString(),
                fontSize, fontSize,
                this::renderSelectLibrary
        );
    }

    private void renderSelectLibrary() {
        this.isSelectLibraryModalRender = true;
    }

    private void renderRunScriptButton() {
        float fontSize = ImGui.getFontSize();
        CubeImGui.imageButton(
                Icons.START,
                Text.translatable("imgui.cubecode.windows.CubeCodeIDE.run_script").getString(),
                fontSize, fontSize,
                this::actionRunScript
        );
    }

    public void renderLeftBar() {
        float fontSize = ImGui.getFontSize();
        CubeImGui.imageButton(Icons.FOLDER, "Project", fontSize, fontSize, () -> {
            this.selectedToolItem = this.selectedToolItem == ToolItem.PROJECT ? ToolItem.UNKNOWN : ToolItem.PROJECT;
        });

        if (this.selectedToolItem == ToolItem.PROJECT) {
            ImGui.getWindowDrawList().addRect(
                    ImGui.getItemRectMin().x, ImGui.getItemRectMin().y,
                    ImGui.getItemRectMax().x, ImGui.getItemRectMax().y,
                    ImGui.colorConvertFloat4ToU32(255, 255, 255, 255)
            );
        }

        CubeImGui.imageButton(Icons.NBT_LIST, "Logger", fontSize, fontSize, () -> {
            ImGuiLoader.pushView(new LoggerView());
        });
    }

    public void renderFileManager() {
        boolean treeProject = ImGui.treeNodeEx("##project", ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen);
        float fontSize = ImGui.getFontSize();
        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            this.preSelectedNode = null;
            ImGui.openPopup("file_context_menu");
        }

        ImGui.sameLine(0, 9);
        ImGui.image(Icons.MODULE.getGlId(), fontSize, fontSize);

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
        float fontSize = ImGui.getFontSize();
        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            this.preSelectedNode = folderNode;
            ImGui.openPopup("file_context_menu");
        }

        ImGui.sameLine(0, 9);
        ImGui.image(Icons.FOLDER.getGlId(), fontSize, fontSize);

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
        float fontSize = ImGui.getFontSize();
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

        ImGui.image(icon.getGlId(), fontSize, fontSize);

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
                    CubeTextEditor.CursorPosition cursorPosition = this.codeEditor.getCursorPosition();

                    List<String> textLines = new ArrayList<>(List.of(this.codeEditor.getTextLines()));

                    textLines.add(cursorPosition.line(), currentLine);

                    this.codeEditor.setText(String.join("\n", textLines));

                    this.codeEditor.setCursorPosition(cursorPosition.line() +1, cursorPosition.column());
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_R)) {
                    this.isReplacementRender = !this.isReplacementRender;
                    this.isFinderRender = false;
                }

                if (ImGui.isKeyPressed(GLFW.GLFW_KEY_F)) {
                    this.isFinderRender = !this.isFinderRender;
                    this.isReplacementRender = false;
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

        float mouseX = ImGui.getMousePosX();
        float mouseY = ImGui.getMousePosY();

        boolean clickedOutside = (mouseX >= this.codeEditor.x) &&
                (mouseX <= this.codeEditor.x + this.codeEditor.width) &&
                (mouseY >= this.codeEditor.y) &&
                (mouseY <= this.codeEditor.y + this.codeEditor.height);

        if (this.isIDEFocused) {
            if (ImGui.isMouseReleased(ImGuiMouseButton.Right) && clickedOutside) {
                ImGui.openPopup("Context Menu IDE");
                Documentation.parseDocs();
            }
        }
    }

    private void renderFinder() {
        float height = this.isReplacementRender ? ImGui.calcTextSize("A").y*3 + ImGui.getStyle().getItemSpacingY() * 2 : ImGui.calcTextSize("A").y*2;
        CubeImGui.beginChild("replacementAndFinder", 0, height, true, ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoScrollbar, () -> {
            float initialPosY = ImGui.getCursorPosY();
            ImGui.image(Icons.SEARCH.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());

            ImGui.sameLine();

            ImGui.pushItemWidth(ImGui.getFontSize() + 150);
                CubeImGui.inputTextWithHint(this, "##Search", "Search", (str) -> {});
            ImGui.popItemWidth();

            this.putVariable("numberSearchWord", 0);

            int numberSearchWord = this.getVariable("numberSearchWord");
            ImString search = this.getVariable("##Search" + this.getUniqueID());

            ImGui.sameLine();

            this.putVariable("isFinderRegex", false);

            if (ImGui.radioButton("Regex", this.getVariable("isFinderRegex"))) {
                this.setVariable("isFinderRegex", !(Boolean) this.getVariable("isFinderRegex"));
            }

            ImGui.sameLine();

            ImGui.text("|");

            ImGui.sameLine();
            ImGui.text(numberSearchWord+"/?");
            if (this.isReplacementRender) {
                this.renderReplacement(search);
            }

            float rightEdge = ImGui.getWindowWidth() - ImGui.getFontSize() - ImGui.getStyle().getWindowPaddingX() - ImGui.getStyle().getItemSpacingX();

            ImGui.setCursorPosX(rightEdge);
            ImGui.setCursorPosY(initialPosY);

            CubeImGui.imageButton(Icons.MINUS, "Close", ImGui.getFontSize(), ImGui.getFontSize(), () -> {
                this.isFinderRender = false;
                this.isReplacementRender = false;
                this.setVariable("##Search" + this.getUniqueID(), new ImString(30));
            });
        });
    }

    private void renderReplacement(ImString search) {
        ImGui.image(Icons.SEARCH.getGlId(), ImGui.getFontSize(), ImGui.getFontSize());
        ImGui.sameLine();

        ImGui.pushItemWidth(ImGui.getFontSize() + 150);
            CubeImGui.inputTextWithHint(this, "##Replace", "Replace", (str) -> {});
        ImGui.popItemWidth();

        ImGui.sameLine();

        ImString replace = this.getVariable("##Replace" + this.getUniqueID());
        CubeImGui.button("Replace", () -> {
            String replacedCode = StringUtils.replaceFirst(this.codeEditor.getText(), search.get(), replace.get(), this.getVariable("isFinderRegex"));
            this.codeEditor.setText(replacedCode.substring(0, replacedCode.length() - 1));
        });

        ImGui.sameLine();

        CubeImGui.button("Replace All", () -> {
            String replacedCode = StringUtils.replaceFirst(this.codeEditor.getText(), search.get(), replace.get(), this.getVariable("isFinderRegex"));
            this.codeEditor.setText(replacedCode.substring(0, replacedCode.length() - 1));
        });
    }

    private void renderCreateNodePopupModal() {
        if (this.isCreateNodeModalRender) {
            ImGui.pushStyleColor(ImGuiCol.Border, 255, 255, 255, 255);
            //ImGui.pushStyleColor(ImGuiCol.ModalWindowDimBg, ImGui.getColorU32(0.0f, 0.0f, 0.0f, 0.5f)); // цвет фона
            ImGui.setNextWindowPos(
                    ImGui.getIO().getDisplaySizeX() * 0.5f,
                    ImGui.getIO().getDisplaySizeY() * 0.5f,
                    ImGuiCond.Always,
                    0.5f,
                    0.5f
            );
            CubeImGui.modal(
                "Create " + ((NodeType)this.getVariable("node_type")).name().toLowerCase(),
                    ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize,
                () -> {
                    ImGui.text("Name");
                    ImGui.sameLine();
                    CubeImGui.inputText(this, "##script_name", 100, str -> {});

                    ImGui.separator();

                    ImGui.text("Side");

                    ImGui.sameLine();

                    this.putVariable("side", "Server");

                    if (ImGui.radioButton("Server", this.getVariable("side") == "Server")) {
                        this.setVariable("side", "Server");
                    }

                    ImGui.sameLine();

                    if (ImGui.radioButton("Client", this.getVariable("side") == "Client")) {
                        this.setVariable("side", "Client");
                    }
                    ImGui.spacing();

                    if (ImGui.button("Create")) {
                        this.isCreateNodeModalRender = false;
                        this.removeVariable("create_node_type");
                    }

                    ImGui.sameLine();

                    if (ImGui.button("Cancel")) {
                        this.isCreateNodeModalRender = false;
                        this.removeVariable("create_node_type");
                    }


                    if (ImGui.isKeyPressed(ImGuiKey.Enter)) {
                        this.isCreateNodeModalRender = false;
                        this.removeVariable("create_node_type");
                    }
                },
                () -> {
                    this.isCreateNodeModalRender = false;
                    this.removeVariable("create_node_type");
                }
            );

            ImGui.popStyleColor();
        }
    }

    private void createNode(NodeType type, FolderNode folderNode, ScriptType side) {
        String name = ((ImString) this.getVariable("##script_name" + this.getUniqueID())).get();

        if (name.isEmpty() || name.length() > 255 || !Pattern.compile("^[^<>:\"/\\\\|?*\\x00-\\x1F]*$").matcher(name).matches())
            return;

        if (type == NodeType.SCRIPT) {
            name = name.endsWith(".js") ? name : name + ".js";
        }

        for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
            if (folderNode != null) {
                if (NodeUtils.hasNodeByPathIgnoreCase(view.nodes, folderNode.getPath() + "/" + name))
                    return;
            } else {
                if (NodeUtils.hasNodeByPathIgnoreCase(view.nodes, "/" + name))
                    return;
            }

            if (type == NodeType.FOLDER) {
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
                        new Script(name,
                                side == ScriptType.SERVER ? ScriptExecutor.DEFAULT_SCRIPT : ScriptExecutor.DEFAULT_CLIENT_SCRIPT,
                                side
                        )
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

        String path = folderNode == null ? "/" : folderNode.getPath();

        Dispatcher.sendToServer(type == NodeType.FOLDER ? new CreateFolderC2SPacket(name, path) : new CreateScriptPacket(name, path, side));
    }

    private void renderDeletePopupModal() {
        if (this.isDeleteNodeModalRender) {
            ImGui.pushStyleColor(ImGuiCol.Border, ColorUtils.rgbaToImguiColor(255, 255, 255, 255));
            ImGui.pushStyleColor(ImGuiCol.ModalWindowDimBg, ImGui.getColorU32(0.0f, 0.0f, 0.0f, 0.5f));
                CubeImGui.modal("Delete " + this.preSelectedNode.getType().name().toLowerCase(), ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize,
                    () -> {
                        ImGui.textColored(255, 207, 72, 255, "Удалить " + this.preSelectedNode.getName() + "?");

                        ImGui.separator();

                        if (ImGui.button("Да")) {
                            this.isDeleteNodeModalRender = false;
                            this.deleteFile();
                        }

                        ImGui.sameLine();

                        if (ImGui.button("Нет")) {
                            this.isDeleteNodeModalRender = false;
                            ImGui.closeCurrentPopup();
                        }
                    },
                    () -> this.isDeleteNodeModalRender = false
                );
            ImGui.popStyleColor();
            ImGui.popStyleColor();
        }
    }

    private void renderSelectLibraryPopupModal() {
        if (this.isSelectLibraryModalRender) {
            CubeImGui.modal("select_library", ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.AlwaysAutoResize);
        }
    }

    private void saveContentScript() {
        if (this.selectedNode != null) {
            for (CubeCodeIDEView view : ImGuiLoader.getViews(CubeCodeIDEView.class)) {
                ScriptNode scriptNode = (ScriptNode) NodeUtils.findNodeByPath(view.nodes, this.selectedNode.getPath());
                if (scriptNode != null) {
                    scriptNode.getScript().setCode(this.codeEditor.getText().replaceAll("\\n+$", ""));
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
        float fontSize = ImGui.getFontSize();
        if (ImGui.beginPopup("file_context_menu")) {
            if (this.preSelectedNode == null || this.preSelectedNode.getType() != NodeType.SCRIPT) {
                CubeImGui.menu(Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.create").getString(), Icons.HAMMER, fontSize, fontSize, () -> {
                    CubeImGui.menuItem(Text.translatable(
                            "imgui.cubecode.windows.CubeCodeIDE.context_menu.create.folder").getString(), Icons.FOLDER,
                            fontSize, fontSize,
                            this::actionCreateFolder
                    );

                    ImGui.separator();

                    CubeImGui.menuItem(
                            Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.create.script").getString(), Icons.JS,
                            fontSize, fontSize,
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
                        fontSize, fontSize,
                        128, 128, 128, 255,
                        "Ctrl + V",
                        this::actionModulePaste
                );
            }

            //SCRIPT
            if (this.preSelectedNode != null) {
                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.cut"), Icons.CUT,
                        fontSize, fontSize,
                        128, 128, 128, 255,
                        "Ctrl + X",
                        this::actionCut
                );

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.copy"), Icons.COPY,
                        fontSize, fontSize,
                        128, 128, 128, 255,
                        "Ctrl + C",
                        this::actionSave
                );

                if (this.preSelectedNode != null && this.preSelectedNode.getType() != NodeType.SCRIPT) {
                    CubeImGui.menuItemAndTooltip(
                            Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.paste"), Icons.PASTE,
                            fontSize, fontSize,
                            128, 128, 128, 255,
                            "Ctrl + V",
                            this::actionPaste
                    );
                }

                ImGui.separator();

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.delete"), Icons.DELETE,
                        fontSize, fontSize,
                        128, 128, 128, 255,
                        "Delete",
                        this::actionDelete
                );

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.rename"), Icons.EDIT,
                        fontSize, fontSize,
                        128, 128, 128, 255,
                        "Ctrl + R",
                        this::actionRename
                );

                ImGui.separator();

                if (this.preSelectedNode != null && this.preSelectedNode.getType() == NodeType.SCRIPT) {
                    CubeImGui.menuItemAndTooltip(
                            Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.changeType"), Icons.RESET,
                            fontSize, fontSize,
                            128, 128, 128, 255,
                            "",
                            this::actionChangeSide
                    );
                }

                CubeImGui.menuItemAndTooltip(
                        Text.translatable("imgui.cubecode.windows.CubeCodeIDE.context_menu.openInExplorer"), Icons.SEARCH,
                        fontSize, fontSize,
                        128, 128, 128, 255,
                        "",
                        this::actionOpenExplorer
                );
            }

            ImGui.endPopup();
        }
    }

    private void actionCreateFolder() {
        this.isCreateNodeModalRender = true;
        this.setVariable("node_type", NodeType.FOLDER);
    }

    private void actionCreateScript() {
        this.isCreateNodeModalRender = true;
        this.setVariable("node_type", NodeType.SCRIPT);
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
        this.isDeleteNodeModalRender = true;
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
                        Script script = scriptNode.getScript();
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

        if (this.selectedNode instanceof ScriptNode) {
            Script script = ((ScriptNode) this.selectedNode).getScript();
            HashMap<Integer, String> errors = new HashMap<>();
            errors.put(script.getLastLaunchErrorLine(), script.getLastLaunchErrorMessage());
            this.codeEditor.setErrorMarkers(errors);
        }


        this.codeEditor.render();

        //this.renderAutocomplete(codeEditorScreenX, codeEditorScreenY);
        this.renderFinderWords();

        this.manageMouse();

        this.isIDEFocused = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);
    }

    private void autocomplete() {
        String textLine = this.codeEditor.getCurrentLineText();
        int column = this.codeEditor.getCursorPosition().column();
    }

    private void renderAutocomplete(float codeEditorScreenX, float codeEditorScreenY) {
        float x = codeEditorScreenX +
                ImGui.calcTextSize("1").x +
                ImGui.getFontSize() +
                ImGui.getStyle().getItemSpacingX() +
                ImGui.calcTextSize(this.codeEditor.getCurrentLineText().substring(0, this.codeEditor.getCursorPosition().column())).x;
        float y = codeEditorScreenY + ImGui.calcTextSize("A").y * this.codeEditor.getCursorPosition().line();

        ImGui.getWindowDrawList().addRectFilled(x, y, x + ImGui.calcTextSize("Б").x, y + ImGui.calcTextSize("A").y, ImGui.getColorU32(1f, 1f, 1f, 0.5f));
    }

    private void renderFinderWords() {
        ImString search = this.getVariable("##Search" + this.getUniqueID());
        if (search == null || search.get().isEmpty()) return;

        String[] textLines = this.codeEditor.getTextLines();
        if (textLines == null) return;

        float lineHeight = ImGui.calcTextSize("A").y;
        float widthLeftBar = ImGui.calcTextSize(String.valueOf(this.codeEditor.getTextLines().length)).x + ImGui.getFontSize() + ImGui.getStyle().getItemSpacingX();

        String searchTerm = search.get();
        int searchLength = searchTerm.length();
        int highlightColor = ImGui.getColorU32(0, 0.8f, 1, 0.5f);

        int numberSearchWord = 0;

        for (int lineNum = 0; lineNum < textLines.length; lineNum++) {
            String line = textLines[lineNum];
            int fromIndex = 0;

            while ((fromIndex = line.indexOf(searchTerm, fromIndex)) != -1) {
                float x = this.codeEditor.x + widthLeftBar + ImGui.calcTextSize(line.substring(0, fromIndex)).x;
                float y = this.codeEditor.y + lineHeight * lineNum;
                float width = ImGui.calcTextSize(searchTerm).x;

                numberSearchWord++;

                ImGui.getWindowDrawList().addRectFilled(
                        x, y,
                        x + width, y + lineHeight,
                        highlightColor
                );

                ImGui.getWindowDrawList().addRect(
                        x, y,
                        x + width, y + lineHeight,
                        ImGui.getColorU32(1, 1, 1, 0.5f)
                );

                fromIndex += searchLength;
            }
        }

        this.setVariable("numberSearchWord", numberSearchWord);
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