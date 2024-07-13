package com.cubecode.client.imgui.basic;

import com.cubecode.client.treesitter.HighLight;
import org.treesitter.TSNode;

import java.util.ArrayList;
import java.util.HashMap;

public interface Theme {

    /**
     * Pre initialization {@link imgui.ImGui} styles
     */
    void preRender();

    /**
     * Post initialization {@link imgui.ImGui} styles
     */
    void postRender();
    
    HighLight getCodeHighlight(TSNode treeSitterNode);

    HashMap<String, String> getColors();

    HashMap<String, ArrayList<String>> getCategories();

    int getColor(String colorName);
}
