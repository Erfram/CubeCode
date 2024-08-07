package com.cubecode.client.views.textEditor;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.MenuBar;
import com.cubecode.client.imgui.components.Separator;
import com.cubecode.client.imgui.components.Text;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.utils.Documentation;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import imgui.ImColor;
import imgui.ImGui;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentationView extends View {
    private final TextEditor CODE_EDITOR = new TextEditor();
    private final int viewWidth = 500;
    private final int viewHeight = 400;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    private final JsonObject chapters = parseDocs().getAsJsonObject("chapters");
    private final Map<String, List<Documentation.Method>> sortedMethods = new HashMap<>();

    private ImString text = new ImString("");

    public DocumentationView() {
        super();

        for (String chapterName : chapters.keySet()) {
            JsonArray methodsArray = chapters.getAsJsonObject(chapterName).getAsJsonArray("methods");
            List<Documentation.Method> methodList = new ArrayList<>();

            for (JsonElement jsonElement : methodsArray) {
                JsonObject methodObject = jsonElement.getAsJsonObject();

                List<Documentation.Argemunt> arguments = new ArrayList<>();

                methodObject.getAsJsonArray("arguments").forEach(element -> {
                    arguments.add(new Documentation.Argemunt(element.getAsJsonObject().get("name").getAsString(), element.getAsJsonObject().get("type").getAsString()));
                });

                methodList.add(new Documentation.Method(
                    methodObject.get("name").getAsString(),
                    methodObject.get("description").getAsString(),
                    methodObject.get("script").getAsString(),
                    arguments,
                    methodObject.get("returnType").getAsString()
                ));
            }

            methodList.sort(Comparator.comparing(method -> method.name));
            sortedMethods.put(chapterName, methodList);
        }
    }

    @Override
    public String getName() {
        return String.format("Documentation##%s", uniqueID);
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);

        CODE_EDITOR.setLanguageDefinition(JavaScriptDefinition.build());
        CODE_EDITOR.setShowWhitespaces(false);
        CODE_EDITOR.setTabSize(4);
        CODE_EDITOR.setPalette(JavaScriptDefinition.buildPallet());
    }

    private String methodName = "";
    private String methodDescription = "";
    private String methodScript = "";

    @Override
    public void render() {
        Window.create()
            .title(getName())
            .flags(ImGuiWindowFlags.HorizontalScrollbar | ImGuiWindowFlags.MenuBar)
            .draw(MenuBar.builder()
                .menu(MenuBar.Menu.builder()
                    .label("API")
                    .callback(() -> {
                        chapters.keySet().forEach((chapterName -> {
                            CubeImGui.menu(chapterName, () -> {
                                for (Documentation.Method method : sortedMethods.get(chapterName)) {
                                    String name = method.name;
                                    String description = method.description;
                                    String script = method.script;
                                    List<Documentation.Argemunt> argemunts = method.arguments;

                                    String arguments = argemunts.stream().map(argemunt -> argemunt.type).collect(Collectors.joining(", "));

                                    CubeImGui.menuItem(name+"("+ arguments +")", () -> {
                                        this.methodName = name+"("+ arguments +")";
                                        this.methodDescription = description;
                                        this.methodScript = script;
                                    });
                                }
                            });
                        }));
                    })
                    .build())
                .build(),
                Text.builder()
                        .title(this.methodName)
                        .id("name")
                        .build(),
                Separator.builder().build(),
                Text.builder()
                        .title(this.methodDescription)
                        .id("description")
                        .build(),
                Text.builder()
                        .callback(() -> {
                            CODE_EDITOR.render("CodeEditor");
                            CODE_EDITOR.setText(this.methodScript);
                        })
                        .rwh(1, 0.5f)
                        .build()
            ).render(this);
    }

    private JsonObject parseDocs() {
        try {
            InputStream inputStream = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(CubeCode.MOD_ID, "docs.json")).get().getInputStream();

            Gson gson = new Gson();

            return gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
