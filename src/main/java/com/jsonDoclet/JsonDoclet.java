package com.jsonDoclet;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JsonDoclet {
    static class Documentation {
        @SerializedName("Server")
        private Map<String, ClassInfo> serverChapters = new HashMap<>();

        @SerializedName("Client")
        private Map<String, ClassInfo> clientChapters = new HashMap<>();

        public void addServerClass(String className, ClassInfo classInfo) {
            serverChapters.put(className, classInfo);
        }

        public void addClientClass(String className, ClassInfo classInfo) {
            clientChapters.put(className, classInfo);
        }
    }

    static class ClassInfo {
        private String description;
        private String script;
        private List<MethodInfo> methods = new ArrayList<>();

        public ClassInfo(String description, String script) {
            this.description = description;
            this.script = script;
        }

        public void addMethod(MethodInfo methodInfo) {
            methods.add(methodInfo);
        }
    }

    static class MethodInfo {
        private String name;
        private String description;
        private String script;
        private List<ArgumentInfo> arguments = new ArrayList<>();
        private String returnType;

        public MethodInfo(String name, String description, String script, String returnType) {
            this.name = name;
            this.description = description;
            this.script = script;
            this.returnType = returnType;
        }

        public void addArgument(ArgumentInfo argumentInfo) {
            arguments.add(argumentInfo);
        }

        public boolean hasDescription() {
            return description != null && !description.trim().isEmpty();
        }
    }

    static class ArgumentInfo {
        private String name;
        private String type;

        public ArgumentInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static void generateDocs(List<String> serverTargetPackages, List<String> clientTargetPackages) {
        String outputPath = ImGuiLoader.class.getClassLoader().getResource("assets/cubecode/docs.json").getFile();
        System.out.println(outputPath);
        if ((serverTargetPackages == null || serverTargetPackages.isEmpty()) &&
                (clientTargetPackages == null || clientTargetPackages.isEmpty())) {
            throw new IllegalArgumentException("At least one package must be specified for either server or client");
        }

        try {
            Documentation documentation = new Documentation();
            JavaParser javaParser = new JavaParser();

            // Process server packages
            if (serverTargetPackages != null && !serverTargetPackages.isEmpty()) {
                List<Path> serverJavaFiles = findJavaFiles(serverTargetPackages);
                processJavaFiles(javaParser, serverJavaFiles, documentation, true);
            }

            // Process client packages
            if (clientTargetPackages != null && !clientTargetPackages.isEmpty()) {
                List<Path> clientJavaFiles = findJavaFiles(clientTargetPackages);
                processJavaFiles(javaParser, clientJavaFiles, documentation, false);
            }

            // Write to JSON file
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            try (FileWriter writer = new FileWriter(outputPath, StandardCharsets.UTF_8)) {
                gson.toJson(documentation, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processJavaFiles(JavaParser javaParser, List<Path> javaFiles, Documentation documentation, boolean isServer) throws IOException {
        for (Path javaFile : javaFiles) {
            try {
                CompilationUnit cu = javaParser.parse(javaFile).getResult().orElse(null);
                if (cu == null) continue;

                cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDecl -> {
                    Optional<Javadoc> classJavadoc = classDecl.getJavadoc();
                    String classDescription = extractDescription(classJavadoc);
                    String classScript = extractScript(classJavadoc);

                    ClassInfo classInfo = new ClassInfo(classDescription, classScript);

                    classDecl.getMethods().forEach(method -> {
                        Optional<Javadoc> methodJavadoc = method.getJavadoc();
                        String methodDescription = extractDescription(methodJavadoc);

                        // Пропускаем метод, если у него нет описания
                        if (methodDescription.trim().isEmpty()) {
                            return; // continue для forEach
                        }

                        String methodScript = extractScript(methodJavadoc);
                        String returnType = method.getType().asString();

                        MethodInfo methodInfo = new MethodInfo(
                                method.getNameAsString(),
                                methodDescription,
                                methodScript,
                                returnType
                        );

                        for (Parameter param : method.getParameters()) {
                            String paramDescription = method.getJavadoc()
                                    .map(javadoc -> javadoc.getBlockTags().stream()
                                            .filter(tag -> tag.getTagName().equals("param"))
                                            .filter(tag -> tag.getName().isPresent())
                                            .filter(tag -> tag.getName().get().equals(param.getNameAsString()))
                                            .map(JavadocBlockTag::getContent)
                                            .map(Object::toString)
                                            .findFirst()
                                            .orElse(""))
                                    .orElse("");

                            ArgumentInfo argumentInfo = new ArgumentInfo(
                                    param.getNameAsString(),
                                    param.getType().asString()
                            );
                            methodInfo.addArgument(argumentInfo);
                        }

                        // Добавляем метод только если он публичный и имеет описание
                        if (method.getModifiers().size() == 1 &&
                                method.getModifiers().get(0).toString().equals("public ") &&
                                methodInfo.hasDescription()) {
                            classInfo.addMethod(methodInfo);
                        }
                    });

                    // Добавляем класс только если у него есть методы
                    if (!classInfo.methods.isEmpty()) {
                        if (isServer) {
                            documentation.addServerClass(classDecl.getNameAsString(), classInfo);
                        } else {
                            documentation.addClientClass(classDecl.getNameAsString(), classInfo);
                        }
                    }
                });
            } catch (IOException e) {
                System.err.println("Error processing file: " + javaFile);
                e.printStackTrace();
            }
        }
    }

    private static String extractDescription(Optional<Javadoc> javadoc) {
        if (!javadoc.isPresent()) {
            return "";
        }

        String description = javadoc.get().getDescription().toText();
        int preIndex = description.indexOf("<pre>");

        if (preIndex != -1) {
            description = description.substring(0, preIndex).trim();
        }

        return description;
    }

    private static String cleanScript(String script) {
        return script.replace("\r\n", "\n")
                .replace("\n", "\\n")
                .replace("\"", "\\\"");
    }

    private static String extractScript(Optional<Javadoc> javadoc) {
        if (!javadoc.isPresent()) {
            return "";
        }

        String description = javadoc.get().getDescription().toText();
        int startIndex = description.indexOf("<pre>{@code");
        int endIndex = description.indexOf("}</pre>");

        if (startIndex != -1 && endIndex != -1) {
            return description.substring(startIndex + 11, endIndex)
                    .trim()
                    .replace("\r\n", "\n")
                    .replaceAll("^\\s+", "");
        }

        return "";
    }

    private static List<Path> findJavaFiles(List<String> targetPackages) throws IOException {
        Path projectRoot = Paths.get("").toAbsolutePath();
        return targetPackages.stream()
                .map(pkg -> projectRoot.resolve("src/main/java/" + pkg))
                .toList();
    }

    public static void main(String[] args) {
        generateDocs(
                List.of(
                        "com/cubecode/api/scripts/code/JavaUtils.java",
                        "com/cubecode/api/scripts/code/ScriptEvent.java",
                        "com/cubecode/api/scripts/code/ScriptFactory.java",
                        "com/cubecode/api/scripts/code/ScriptRayTrace.java",
                        "com/cubecode/api/scripts/code/ScriptServer.java",
                        "com/cubecode/api/scripts/code/ScriptVector.java",
                        "com/cubecode/api/scripts/code/ScriptWorld.java",
                        "com/cubecode/api/scripts/code/blocks/ScriptBlockEntity.java",
                        "com/cubecode/api/scripts/code/blocks/ScriptBlockState.java",
                        "com/cubecode/api/scripts/code/cubecode/CubeCodeStates.java",
                        "com/cubecode/api/scripts/code/entities/ScriptEntity.java",
                        "com/cubecode/api/scripts/code/entities/ScriptPlayer.java",
                        "com/cubecode/api/scripts/code/items/ScriptInventory.java",
                        "com/cubecode/api/scripts/code/items/ScriptItem.java",
                        "com/cubecode/api/scripts/code/items/ScriptItemStack.java",
                        "com/cubecode/api/scripts/code/nbt/ScriptNbtCompound.java",
                        "com/cubecode/api/scripts/code/nbt/ScriptNbtList.java"
                ),
                List.of(
                        "com/cubecode/client/scripts/code/entities/ClientScriptEntity.java",
                        "com/cubecode/client/scripts/code/entities/ClientScriptPlayer.java",
                        "com/cubecode/client/scripts/code/ui/ClientCubeCodeUI.java",
                        "com/cubecode/client/scripts/code/ui/components/AbstractComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/ArrowButtonComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/ButtonComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/CheckboxComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/ChildComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/IconComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/InputTextComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/RadioButtonComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/SliderComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/TextComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/WindowComponent.java",
                        "com/cubecode/client/scripts/code/ui/components/draw/DrawComponent.java"
                )
        );
    }
}