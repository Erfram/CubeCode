package com.cubecode.client.imgui.themes;

import com.cubecode.client.imgui.basic.Theme;
import com.cubecode.client.treesitter.HighLight;
import com.cubecode.utils.ColorUtils;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;
import org.treesitter.TSNode;
import org.treesitter.TSPoint;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CodeTheme implements Theme {

    public HashMap<String, String> colors;

    public HashMap<String, ArrayList<String>> categories;

    public HashMap<String, Integer> highlightsMap;

    public CodeTheme() {

    }


    @Override
    public void preRender() {

    }

    @Override
    public void postRender() {

    }

    @Override
    @Nullable
    public HighLight getCodeHighlight(TSNode node) {
        String type = node.getGrammarType();

        boolean containsKey = false;
        String color = "";

        Set<Map.Entry<String, ArrayList<String>>> categories = this.getCategories().entrySet();

        for (Map.Entry<String, ArrayList<String>> category : categories) {
            if (category.getValue().contains(type)) {
                containsKey = true;
                color = this.getColors().get(category.getKey());
                break;
            }
        }

        if (!containsKey) {
            return null;
        }

        TSPoint point = node.getStartPoint();
        return new HighLight(
                node.getStartByte(),
                node.getEndByte(),
                point,
                ColorUtils.parseStringARGB(color),
                type
        );
    }

    @Override
    public HashMap<String, String> getColors() {
        return this.colors;
    }

    @Override
    public HashMap<String, ArrayList<String>> getCategories() {
        return this.categories;
    }

    @Override
    public int getColor(String colorName) {
        return ColorUtils.parseStringARGB(this.colors.get(colorName));
    }

    public static CodeTheme parseTheme(InputStream fileInputStream) {
        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(fileInputStream), CodeTheme.class);
    }
}
