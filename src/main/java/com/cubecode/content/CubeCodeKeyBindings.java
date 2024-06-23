package com.cubecode.content;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import com.cubecode.client.imgui.screens.DashboardScreen;

import java.util.Arrays;

public final class CubeCodeKeyBindings {

    private static final String CUBECODE_CATEGORY = "CubeCode";
    private static final KeyBinding DASHBOARD = new KeyBinding("Dashboard", GLFW.GLFW_KEY_EQUAL, CUBECODE_CATEGORY);

    public static void init() {
        CubeCodeKeyBindings.registerKeyBindings(DASHBOARD);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (DASHBOARD.wasPressed()) {
                client.setScreen(new DashboardScreen());
            }
        });
    }

    private static void registerKeyBindings(KeyBinding... keyBindings) {
        Arrays.stream(keyBindings).forEach(KeyBindingHelper::registerKeyBinding);
    }

}
