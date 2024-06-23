package com.cubecode.client.editor;

import java.util.ArrayList;

public class CodeEditorTester {


    public static void main(String[] args) throws Exception {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("var Player = World.getPlayer();");
        lines.add("");
        lines.add("function example() {");
        lines.add("    Player.send('Hello World!');");
        lines.add("}");
        lines.add("// comment code");
        lines.add("/* comment */");

        ArrayList<CodeLabel> parse = TreeSitterParser.parse(lines);
        for (CodeLabel codeLabel : parse) {
            System.out.println("[" + codeLabel.text() + "] | " + codeLabel.color() + " | " + codeLabel.type());
        }

    }


}
