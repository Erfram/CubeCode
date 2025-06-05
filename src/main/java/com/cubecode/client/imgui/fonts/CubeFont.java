package com.cubecode.client.imgui.fonts;

import com.cubecode.CubeCode;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import imgui.ImFont;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CubeFont {
    String id;
    String path;

    List<ImFont> scaleFonts;

    public CubeFont(String id, String path) {
        this.id = id;
        this.path = path;
        this.scaleFonts = new ArrayList<>();
    }

    public CubeFont load(ImFontAtlas fontAtlas) {
        try (InputStream is = ImGuiLoader.class.getClassLoader().getResourceAsStream(this.path)) {
            ImFontConfig fontConfig = new ImFontConfig();

            fontConfig.setRasterizerMultiply(1);
            fontConfig.setOversampleH(3);
            fontConfig.setOversampleV(3);
            fontConfig.setPixelSnapH(true);

            //TODO: В нормальном виде сделать добавление ренжей
            short[] ranges = ImGui.getIO().getFonts().getGlyphRangesCyrillic();
            short[] newArray = new short[ranges.length + 2];
            System.arraycopy(ranges, 0, newArray, 0, ranges.length);
            newArray[ranges.length] = 8592;
            newArray[ranges.length + 1] = 8703;

            fontConfig.setGlyphRanges(newArray);

            byte[] bytes = is.readAllBytes();
            this.addScaleFont(fontAtlas, bytes, 12, fontConfig);
            this.addScaleFont(fontAtlas, bytes, 16, fontConfig);
            this.addScaleFont(fontAtlas, bytes, 22, fontConfig);
            this.addScaleFont(fontAtlas, bytes, 32, fontConfig);
        } catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }

        return this;
    }

    private void addScaleFont(ImFontAtlas fontAtlas, byte[] bytes, int pixels, ImFontConfig imFontConfig) {
        this.scaleFonts.add(fontAtlas.addFontFromMemoryTTF(bytes, pixels, imFontConfig));
    }

    public ImFont getScaleFont() {
        Integer value = MinecraftClient.getInstance().options.getGuiScale().getValue();
        if (value == 0)
            value = 1;
        value--;
        return this.scaleFonts.get(value);
    }
}
