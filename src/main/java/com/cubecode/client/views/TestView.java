package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.scripts.code.ui.components.Component;

import java.util.*;

public class TestView extends View {
    public LinkedList<Runnable> runnables = new LinkedList<>();

    public List<Component> components = new ArrayList<>();

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render() {
        this.runnables.forEach(Runnable::run);
    }

    @Override
    public void onClose() {

    }
}
