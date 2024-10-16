package com.cubecode.client.views.idea;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.idea.utils.JavaScriptDefinition;
import com.cubecode.utils.Documentation;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.*;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.*;

public class DocumentationView extends View {
    private final int viewWidth = 700;
    private final int viewHeight = 400;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    private final TextEditor CODE_EDITOR = new TextEditor();

    private final Map<String, Documentation.Chapter> docs = Documentation.parseDocs();

    private static final Map<String, String> apiIcons = new HashMap<>();

    static {
        apiIcons.put("ScriptPlayer", "player");
        apiIcons.put("ScriptEntity", "entity");
        apiIcons.put("ScriptItem", "item");
        apiIcons.put("ScriptItemStack", "item_stack");
        apiIcons.put("ScriptWorld", "world");
        apiIcons.put("ScriptInventory", "inventory");
        apiIcons.put("ScriptNbtCompound", "nbt_compound");
        apiIcons.put("ScriptNbtList", "nbt_list");
        apiIcons.put("ScriptRayTrace", "ray_trace");
        apiIcons.put("ScriptVector", "vector");
        apiIcons.put("ScriptServer", "server");
        apiIcons.put("ScriptBlockState", "block");
        apiIcons.put("ScriptBlockEntity", "block_entity");
        apiIcons.put("ScriptEvent", "flag");
        apiIcons.put("ScriptFactory", "factory");
        apiIcons.put("CubeCodeStates", "state");
    }

    private int selectedClass = -1;

    private String className = "name";
    private Documentation.Chapter classChapter = null;

    public DocumentationView() {
        super();
    }

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.windows.codeEditor.documentation.title").getString()+"##%s", uniqueID);
    }

    @Override
    public void init() {
        float x = (windowWidth - viewWidth) * 0.5f;
        float y = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(x, y);
        ImGui.setNextWindowSize(viewWidth, viewHeight);

        CODE_EDITOR.setLanguageDefinition(JavaScriptDefinition.build());
        CODE_EDITOR.setShowWhitespaces(false);
        CODE_EDITOR.setTabSize(4);
        CODE_EDITOR.setPalette(JavaScriptDefinition.buildPallet());
    }

    @Override
    public void render() {
        Window.create()
                .title(getName())
                .flags(ImGuiWindowFlags.HorizontalScrollbar)
                .callback(() -> {
                    CubeImGui.beginChild("Left Pane", ImGui.calcTextSize(docs.keySet().stream()
                            .max(Comparator.comparingInt(String::length))
                            .orElse("")).x + 35, 0, true, this::renderClasses);
                    ImGui.sameLine();
                    CubeImGui.beginChild("CubeCode IDEA", 0, 0, false, this::renderContent);
                })
                .render(this);
    }

    public void renderClasses() {
        TreeMap<String, Documentation.Chapter> sortedMap = new TreeMap<>(docs);
        for (int i = 0; i < sortedMap.size(); i++) {
            if (ImGui.selectable("##"+sortedMap.keySet().stream().toList().get(i), selectedClass == i, ImGuiSelectableFlags.None)) {
                selectedClass = i;
                className = sortedMap.keySet().stream().toList().get(i);
                classChapter = sortedMap.values().stream().toList().get(i);

                this.setVariable("search_methods", null);
                this.setVariable("##Search" + this.uniqueID, new ImString("", 30));
            }

            ImGui.sameLine(0, 0);
            Icons icon = Icons.valueOf(apiIcons.get(sortedMap.keySet().stream().toList().get(i)).toUpperCase());

            ImGui.image(icon.getGlId(), 16, 16);

            ImGui.sameLine(0, 4);
            ImGui.text(sortedMap.keySet().stream().toList().get(i));
        }
    }

    public void renderContent() {
        if (selectedClass != -1) {
            renderTitle();
        }
    }

    List<Documentation.Method> methods;

    public void renderTitle() {
        ImGui.textColored(255, 207, 64, 255, className);

        ImGui.separator();

        ImGui.text(classChapter.description);

        ImGui.spacing();

        if (!classChapter.script.isEmpty()) {
            CubeImGui.beginChild("Code Editor", 0, 150, true, () -> {
                CODE_EDITOR.render("CodeEditor");
                CODE_EDITOR.setText(classChapter.script);
            });
        }

        this.methods = this.getVariable("search_methods") == null ? classChapter.methods : this.getVariable("search_methods");
        ImGui.image(Icons.SEARCH.getGlId(), 16, 16);
        ImGui.sameLine();
        CubeImGui.inputText(this, "##Search", (searchMethod) -> {
            if (!searchMethod.isEmpty()) {
                this.setVariable("search_methods", classChapter.methods.stream().filter((method -> {
                    String[] methodNameSeparator = method.name.split("(?=[A-Z])");

                    for (String word : methodNameSeparator) {
                        if (word.toLowerCase().startsWith(searchMethod.toLowerCase())) {
                            return true;
                        }
                    }

                    return method.name.toLowerCase().contains(searchMethod.toLowerCase());
                })).toList());
            } else {
                this.setVariable("search_methods", classChapter.methods);
            }
        });

        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0f, 0f, 0f, 0.2f);
        CubeImGui.beginChild("Methods", 0, 0, true, this::renderMethods);
        ImGui.popStyleColor();
    }

    public void renderMethods() {
        for (int i = 0; i < this.methods.size(); i++) {
            if (i % 2 == 0) {
                ImGui.pushStyleColor(ImGuiCol.ChildBg, 1, 1, 1, 0.01f);
            }

            Documentation.Method method = this.methods.get(i);
            CubeImGui.beginChild("Return Types " + i, this.getMaxWidthReturnType(), 50, false, () -> {
                this.renderReturnType(method.returnType);
            });

            ImGui.sameLine();

            MutableText parsingMethod = this.parseMethod(method.name, method.arguments);

            CubeImGui.beginChild("Method" + i, 0, 50, false, () -> {
                this.renderMethod(parsingMethod, method.name, method.description, method.script);
            });

            if (i % 2 == 0) {
                ImGui.popStyleColor();
            }
        }
    }

    public void renderReturnType(String returnType) {
        ImGui.textColored(1, 0.9f, 0, 1, returnType);
    }

    public void renderMethod(MutableText method, String name, String description, String script) {
        CubeImGui.textMutable(method);
        

        if (!script.isEmpty()) {
            String id = "##"+name + "_" + description + "_" + script;

            ImGui.sameLine();

            if (ImGui.imageButton(Icons.INFO.getGlId(), 16, 16)) {
                ImGuiLoader.pushView(new MethodEditorView(name, script, id));
            }
        }

        ImGui.textColored(0.5f, 0.5f, 0.5f, 1f, description);
    }

    private float getMaxWidthReturnType() {
        float maxLength = 0;

        for (Documentation.Method method : this.methods) {
            if (maxLength < ImGui.calcTextSize(method.returnType).x) {
                maxLength = ImGui.calcTextSize(method.returnType).x;
            }
        }
        return maxLength;
    }

    private int rgbaToInt(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                (b & 0xFF);
    }

    private MutableText parseMethod(String methodName, List<Documentation.Argemunt> arguments) {
        short[] textRgba = CubeCodeClient.themeManager.currentTheme.text;
        MutableText method = Text.literal(methodName + "(").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgbaToInt(textRgba[0], textRgba[1], textRgba[2], textRgba[3]))));

        for (int t = 0; t < arguments.size(); t++) {
            Documentation.Argemunt argument = arguments.get(t);
            method.append(Text.literal(argument.type + " ").formatted(Formatting.GOLD));
            method.append(Text.literal(argument.name).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgbaToInt(textRgba[0], textRgba[1], textRgba[2], textRgba[3])))));
            if (t < arguments.size() - 1) {
                method.append(Text.literal(", ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgbaToInt(textRgba[0], textRgba[1], textRgba[2], textRgba[3])))));
            }
        }

        method.append(Text.literal(")").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgbaToInt(textRgba[0], textRgba[1], textRgba[2], textRgba[3])))));

        return method;
    }

    private static class MethodEditorView extends View {
        private final TextEditor TEXT_EDITOR = new TextEditor();

        private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
        private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

        private final String name;
        private final String script;
        private final String id;

        public MethodEditorView(String name, String script, String id) {
            this.name = name;
            this.script = script;
            this.id = id;
        }

        @Override
        public void init() {
            float width = this.getSize()[0] + 50;
            float height = this.getSize()[1] + 55;
            ImGui.setNextWindowPos(
                    (windowWidth - width) * 0.5f,
                    (windowHeight - height) * 0.5f
            );
            ImGui.setNextWindowSize(width, height);

            TEXT_EDITOR.setLanguageDefinition(JavaScriptDefinition.build());
            TEXT_EDITOR.setShowWhitespaces(false);
            TEXT_EDITOR.setTabSize(4);
            TEXT_EDITOR.setPalette(JavaScriptDefinition.buildPallet());
        }

        @Override
        public String getName() {
            return String.format(name + "##%s", uniqueID);
        }

        @Override
        public void render() {
            Window.create()
                    .title(getName())
                    .callback(() -> {
                        TEXT_EDITOR.setText(script);
                        TEXT_EDITOR.setReadOnly(true);
                        TEXT_EDITOR.render(id);
                    })
                    .render(this);
        }

        private float[] getSize() {
            String[] lines = script.split("\n");

            float width = 0;
            float height = lines.length * ImGui.calcTextSize("C").y;

            for (String line : lines) {
                if (width < ImGui.calcTextSize(line).x) {
                    width = ImGui.calcTextSize(line).x;
                }
            }

            float[] size = new float[2];

            size[0] = width;
            size[1] = height;

            return size;
        }
    }
}
