package com.cubecode.client.imgui.fonts;

import com.cubecode.CubeCode;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class CubeFont {
    private Font font;
    private final String filePath;

    public CubeFont(String filePath, float size) {
        this.filePath = filePath;
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File(filePath)).deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e) {
            CubeCode.LOGGER.error("not create Font: " + e.getMessage());
        }
    }

    public Font getFont() {
        return font;
    }

    public String getFilePath() {
        return filePath;
    }
}
