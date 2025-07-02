package com.cubecode.client.imgui.textEditor;

import java.util.ArrayList;
import java.util.List;

public class SnippetManager {
    public final List<Snippet> snippets = new ArrayList<>();

    public SnippetManager() {
        snippets.add(new Snippet("info", "CubeCode.info(\"CubeCode\", $END$)"));
        snippets.add(new Snippet("error", "CubeCode.info(\"CubeCode\", $END$)"));
        snippets.add(new Snippet("warning", "CubeCode.info(\"CubeCode\", $END$)"));
        snippets.add(new Snippet("debug", "CubeCode.info(\"CubeCode\", $END$)"));
        snippets.add(new Snippet("ssend", "c.server.send($END$)"));
        snippets.add(new Snippet("psend", "c.player.send($END$)"));
        snippets.add(new Snippet("ui", "const ui = CubeCode.createUI()$END$"));
        snippets.add(new Snippet("cbs", "const blockState = CubeCode.createBlockState($END$)"));
        snippets.add(new Snippet("cbsm", "const blockState = CubeCode.createBlockState(\"minecraft:$END$\")"));
        snippets.add(new Snippet("cc", "const compound = CubeCode.createCompound($END$)"));
        snippets.add(new Snippet("cco", "const compound = CubeCode.createCompound(\"{$END$}\")"));
        snippets.add(new Snippet("for", "for (int i = 0; i < $END$; i++) {}"));
        snippets.add(new Snippet("trct", "try {\n    \n} catch {\n    \n}"));
    }

    public void addSnippet(Snippet snippet) {
        snippets.add(snippet);
    }

    public void removeSnippet(String name) {
        snippets.forEach(snippet -> {
            if (name.equals(snippet.name)) {
                snippets.remove(snippet);
            }
        });
    }

    public Snippet getSnippet(String name) {
        Snippet snippet = null;

        for (Snippet sp : snippets) {
            if (sp.getName().equals(name)) {
                snippet = sp;
            }
        }

        return snippet;
    }

    public List<String> getMatchingSnippets(String startSnippet) {
        List<String> snippets = new ArrayList<>();

        for (String key : getNames()) if (key.startsWith(startSnippet)) {
            snippets.add(key);
        }

        return snippets;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        snippets.forEach(snippet -> {
            names.add(snippet.name);
        });

        return names;
    }

    public static class Snippet {
        String name;
        String code;

        public Snippet(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
