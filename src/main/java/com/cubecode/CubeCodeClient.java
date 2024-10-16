package com.cubecode;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.fonts.FontManager;
import com.cubecode.client.imgui.themes.ThemeManager;
import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.Dispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static FontManager fontManager;
    public static ThemeManager themeManager;

    @Override
    public void onInitializeClient() {
        Dispatcher.register();
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();
        CubeCodeConfig.loadConfig();

        fontManager = new FontManager();
        themeManager = new ThemeManager();
        themeManager.loadThemes();
    }
}