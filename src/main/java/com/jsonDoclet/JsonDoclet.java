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
        @SerializedName("chapters")
        private Map<String, ClassInfo> chapters = new HashMap<>();

        public void addClass(String className, ClassInfo classInfo) {
            chapters.put(className, classInfo);
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
    }

    static class ArgumentInfo {
        private String name;
        private String type;

        public ArgumentInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static void generateDocs(List<String> targetPackages, List<String> clientTargetPackages) {
        String outputPath = ImGuiLoader.class.getClassLoader().getResource("assets/cubecode/docs.json").getFile();
        System.out.println(outputPath);
        if (targetPackages == null || targetPackages.size() == 0) {
            throw new IllegalArgumentException("At least one package must be specified");
        }

        try {
            Documentation documentation = new Documentation();
            JavaParser javaParser = new JavaParser();

            List<Path> javaFiles = findJavaFiles(targetPackages);

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

                            if (method.getModifiers().size() == 1 && method.getModifiers().get(0).toString().equals("public ")) {
                                classInfo.addMethod(methodInfo);
                            }
                        });

                        documentation.addClass(classDecl.getNameAsString(), classInfo);
                    });
                } catch (IOException e) {
                    System.err.println("Error processing file: " + javaFile);
                    e.printStackTrace();
                }
            }

            // Записываем результат в JSON файл
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

        return targetPackages.stream().map(pkg -> projectRoot.resolve("src/main/java/" + pkg)).toList();
    }

    public static void main(String[] args) {

        generateDocs(List.of(
                    "com/cubecode/api/scripts/code/JavaScriptUtils.java",
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

                )
        );
    }
}