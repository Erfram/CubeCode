package com.cubecode.client.views.ide;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.ide.utils.ScriptDefinition;
import com.cubecode.utils.Documentation;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.*;
import imgui.type.ImString;
import net.fabricmc.api.EnvType;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class DocumentationView extends View {
    final Map<EnvType, List<Documentation.Chapter>> docs;
    Map<EnvType, List<Documentation.Chapter>> usedDocs;

    TextEditor codeEditor = new TextEditor();

    Documentation.Chapter selectedChapter = null;

    Map<String, Icons> docsIcons = new HashMap<>();

    boolean isHideDescriptionChapter = false;

    public DocumentationView(Map<EnvType, List<Documentation.Chapter>> docs) {
        this.docs = Collections.unmodifiableMap(docs);
        this.usedDocs = new HashMap<>(docs);

        this.docsIcons.put("ScriptPlayer", Icons.PLAYER);
        this.docsIcons.put("ScriptEntity", Icons.ENTITY);
        this.docsIcons.put("ScriptItem", Icons.ITEM);
        this.docsIcons.put("ScriptItemStack", Icons.ITEM_STACK);
        this.docsIcons.put("ScriptWorld", Icons.WORLD);
        this.docsIcons.put("ScriptInventory", Icons.INVENTORY);
        this.docsIcons.put("ScriptNbtCompound", Icons.NBT_COMPOUND);
        this.docsIcons.put("ScriptNbtList", Icons.NBT_LIST);
        this.docsIcons.put("ScriptRayTrace", Icons.RAY_TRACE);
        this.docsIcons.put("ScriptVector", Icons.VECTOR);
        this.docsIcons.put("ScriptServer", Icons.SERVER);
        this.docsIcons.put("ScriptBlockState", Icons.BLOCK);
        this.docsIcons.put("ScriptBlockEntity", Icons.BLOCK_ENTITY);
        this.docsIcons.put("ScriptEvent", Icons.FLAG);
        this.docsIcons.put("ScriptFactory", Icons.FACTORY);
        this.docsIcons.put("CubeCodeStates", Icons.STATE);

        this.docsIcons.put("ClientScriptPlayer", Icons.PLAYER);
        this.docsIcons.put("ClientScriptEntity", Icons.ENTITY);
    }

    @Override
    public void init() {
        super.init();

        codeEditor.setLanguageDefinition(ScriptDefinition.javaScript());
        codeEditor.setPalette(ScriptDefinition.getJavaScriptPalette());
        codeEditor.setShowWhitespaces(false);
    }

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.windows.CubeCodeIDE.documentation").getString() + "##%s", uniqueID);
    }

    @Override
    public void render() {
        Window.create()
                .title(this.getName())
                .flags(ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)
                .callback(() -> {
                    float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();

                    float variableSplitter = this.getVariable("splitter") == null ? 100f : this.getVariable("splitter");

                    float widthSplitter = availableWidth * variableSplitter;

                    CubeImGui.beginChild("docs", widthSplitter, 0f, true, () -> {
                        this.renderSearchChapter();
                        ImGui.separator();
                        ImGui.spacing();
                        this.renderTreeDocs();
                    });
                    ImGui.sameLine();
                    CubeImGui.verticalSplitter(this, "splitter", 4, availableWidth, ImGui.getItemRectMaxY(), 0.3f, 0.1f, 0.9f);
                    ImGui.sameLine();
                    CubeImGui.beginChild("chapter", 0, 0, true, this::renderChapter);
                })
                .render(this);
    }

    private void renderSearchChapter() {
        ImGui.image(Icons.SEARCH.getGlId(), 16, 16);

        ImGui.sameLine();

        ImGui.pushItemWidth(ImGui.getWindowContentRegionMaxX() - ImGui.getStyle().getWindowPaddingX() - ImGui.getStyle().getItemSpacingX() - 16);

        CubeImGui.inputText(this, "##SearchChapter", findChapter -> {
            this.usedDocs = new HashMap<>(this.docs);
            for (EnvType envType : this.usedDocs.keySet()) {
                List<Documentation.Chapter> chapters = this.usedDocs.get(envType);

                Stream<Documentation.Chapter> findChapters = chapters.stream().filter(chapter -> {
                    String[] chapterNameSeparator = chapter.name.split("(?=[A-Z])");

                    for (String word : chapterNameSeparator) {
                        if (word.toLowerCase().startsWith(findChapter.toLowerCase())) {
                            return true;
                        }
                    }

                    return chapter.name.toLowerCase().contains(findChapter.toLowerCase());
                });

                try {
                    this.usedDocs.put(envType, findChapters.toList());
                } catch (UnsupportedOperationException ignored) {
                }
            }
        });
        ImGui.popItemWidth();
    }

    private void renderTreeDocs() {
        CubeImGui.treeNode("Server", Icons.SERVER, ImGuiTreeNodeFlags.SpanAvailWidth, () -> {
            List<Documentation.Chapter> chapters = this.usedDocs.get(EnvType.SERVER);

            chapters.forEach(this::renderTreeChapter);
        });

        CubeImGui.treeNode("Client", Icons.CLIENT, ImGuiTreeNodeFlags.SpanAvailWidth, () -> {
            List<Documentation.Chapter> chapters = this.usedDocs.get(EnvType.CLIENT);

            chapters.forEach(this::renderTreeChapter);
        });
    }

    private void renderTreeChapter(Documentation.Chapter chapter) {
        Icons icon = docsIcons.get(chapter.name);

        if (icon == null) {
            icon = Icons.EMPTY;
        }

        CubeImGui.selectable(chapter.name, this.selectedChapter != null && this.selectedChapter.equals(chapter), icon, ImGuiSelectableFlags.None, () -> this.changeChapter(chapter));
    }

    private void changeChapter(Documentation.Chapter chapter) {
        this.selectedChapter = chapter;
    }

    private void renderChapter() {
        if (this.selectedChapter == null)
            return;

        ImVec2 chapterChildSize = ImGui.getContentRegionAvail();

        ImGui.setCursorPosX(chapterChildSize.x / 2 - ImGui.calcTextSize(this.selectedChapter.name).x / 2 + 10);
        ImGui.textColored(255, 207, 64, 255, this.selectedChapter.name);

        ImGui.spacing();

        ImGui.separator();

        ImGui.spacing();

        if (!this.isHideDescriptionChapter) {
            ImGui.textWrapped(this.selectedChapter.description);
        } else {
            ImGui.pushTextWrapPos();
            ImGui.textColored(41, 49, 51, 255, "...");
            ImGui.popTextWrapPos();
        }

        if (ImGui.isItemClicked(ImGuiMouseButton.Left)) {
            this.isHideDescriptionChapter = !this.isHideDescriptionChapter;
        }

        ImGui.spacing();

        ImGui.separator();

        this.renderCodeEditor(this.selectedChapter.name, this.selectedChapter.script);

        CubeImGui.beginChild("Methods", 0, 0, false, this::renderMethods);
    }

    private void renderMethods() {
        ImGui.image(Icons.SEARCH.getGlId(), 16, 16);

        ImGui.sameLine();

        ImGui.pushItemWidth(ImGui.getWindowContentRegionMaxX() - ImGui.getStyle().getWindowPaddingX() - ImGui.getStyle().getItemSpacingX() - 16);

        this.putVariable("##SearchMethod", new ImString(999));
        ImString searchMethod = this.getVariable("##SearchMethod");

        ImGui.inputText("##SearchMethod", searchMethod);

        List<Documentation.Method> methods = this.selectedChapter.methods.stream().filter(method -> {
            String[] chapterNameSeparator = method.name.split("(?=[A-Z])");

            for (String word : chapterNameSeparator) {
                if (word.toLowerCase().startsWith(searchMethod.get().toLowerCase())) {
                    return true;
                }
            }

            return method.name.toLowerCase().contains(searchMethod.get().toLowerCase());
        }).toList();

        ImGui.popItemWidth();

        float methodHeight = ImGui.calcTextSize("A").y * 3.2f;
        for (int i = 0; i < methods.size(); i++) {
            Documentation.Method method = methods.get(i);
            CubeImGui.beginChild("##"+method.name + "_" + i, 0, methodHeight, true, ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse, () -> this.renderMethod(method));
        }
    }

    private void renderMethod(Documentation.Method method) {
        //TODO Переделать ширину
        float methodHeight = ImGui.calcTextSize("A").y * 3;
        CubeImGui.beginChild(method.returnType, 200, methodHeight, false, () -> this.renderReturnType(method.returnType));

        ImGui.sameLine();

        CubeImGui.beginChild("core_" + method.name, 0, methodHeight, false, ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse, () -> this.renderCoreMethod(method));
    }

    private void renderReturnType(String returnType) {
        ImGui.textColored(ImGui.getColorU32(255,165,0, 255), returnType);

        this.goToChapter(returnType);
    }

    private void renderCoreMethod(Documentation.Method method) {
        ImGui.text(method.name + "(");

        for (int i = 0; i < method.arguments.size(); i++) {
            Documentation.Argument argument = method.arguments.get(i);
            ImGui.sameLine(0, 0);
            ImGui.textColored(Color.orange.getRGB(), argument.type);

            this.goToChapter(argument.type);

            if (i != method.arguments.size() - 1) {
                ImGui.sameLine(0, 0);
                ImGui.text(", ");
            }
        }

        ImGui.sameLine(0, 0);
        ImGui.text(")");

        ImGui.textColored(Color.GRAY.getRGB(), method.description);

        if (ImGui.isItemHovered()) {
            this.renderDescriptionTooltip(method.description, method.script);
        }
    }

    private void renderDescriptionTooltip(String description, String script) {
        ImGui.beginTooltip();

        float width = ImGui.getIO().getDisplaySizeX() - ImGui.getMousePosX();

        ImVec2 scriptSize = ImGui.calcTextSize(script);

        float codeEditorWidth = (float) (scriptSize.x + ImGui.getStyle().getItemSpacingX() + ImGui.getStyle().getWindowPaddingX() + 16 * Math.ceil(script.split("\n").length / 10f));

        ImGui.pushTextWrapPos(script.isEmpty() ? width : Math.min(width, codeEditorWidth));

        ImGui.text(description);
        if (!script.isEmpty()) {
            ImGui.separator();
            this.renderCodeEditor("description", script, Math.min(width, codeEditorWidth));
        }

        ImGui.popTextWrapPos();

        ImGui.endTooltip();
    }

    private void renderCodeEditor(String name, String code, float width) {
        if (!code.isEmpty()) {
            ImVec2 codeSize = ImGui.calcTextSize(code);

            this.codeEditor.setCursorPosition(999, 999);
            this.codeEditor.setText(code);

            CubeImGui.beginChild(name, width, codeSize.y + ImGui.getFrameHeightWithSpacing(), false, () -> this.codeEditor.render(name));
        }
    }

    private void renderCodeEditor(String name, String code) {
        this.renderCodeEditor(name, code, 0);
    }

    private void goToChapter(String chapterName) {
        int index = chapterName.indexOf("<");
        chapterName = chapterName.substring(0, index == -1 ? chapterName.length() : index);

        if (this.docsIcons.containsKey(chapterName)) {
            Documentation.Chapter chapter = this.findChapter(chapterName);
            if (this.selectedChapter.name.equals(chapterName))
                return;

            if (ImGui.isItemClicked(ImGuiMouseButton.Middle)) {
                this.selectedChapter = chapter;
                return;
            }

            if (!ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
                return;

            if (ImGui.isItemHovered()) {
                ImVec2 textRectMin = ImGui.getItemRectMin();
                ImVec2 textRectMax = ImGui.getItemRectMax();
                ImGui.getWindowDrawList().addLine(textRectMin.x, textRectMax.y + 1, textRectMax.x, textRectMax.y + 1, ImGui.getColorU32(255, 255, 255, 255));

                ImGui.setMouseCursor(ImGuiMouseCursor.Hand);
            }

            if (ImGui.isItemClicked(ImGuiMouseButton.Left) && chapter != null) {
                this.selectedChapter = chapter;
            }
        }
    }

    private Documentation.Chapter findChapter(String chapterName) {
        for (List<Documentation.Chapter> chapters : this.docs.values()) {
            for (Documentation.Chapter chapter : chapters) {
                if (chapter.name.equals(chapterName)) {
                    return chapter;
                }
            }
        }

        return null;
    }
}
