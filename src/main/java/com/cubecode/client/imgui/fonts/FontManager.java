package com.cubecode.client.imgui.fonts;

import com.cubecode.CubeCodeClient;
import imgui.*;

import java.util.HashSet;
import java.util.Set;

public class FontManager {
    private final Set<CubeFont> fonts = new HashSet<>();

    public void loadFonts() {
        ImGuiIO io = ImGui.getIO();
        ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.clear();

        this.loadFont("default", "assets/cubecode/imgui/fonts/anonymous.ttf", fontAtlas);
    }

    public void loadFont(String id, String path, ImFontAtlas fontAtlas) {
        this.fonts.add(new CubeFont(id, path).load(fontAtlas));
    }

    public ImFont getFont(String id) {
        for (CubeFont font : this.fonts) {
            if (font.id.equals(id)) {
                return font.getScaleFont();
            }
        }
        return null;
    }

    public ImFont getDefaultFont() {
        return this.getFont("default");
    }
}
