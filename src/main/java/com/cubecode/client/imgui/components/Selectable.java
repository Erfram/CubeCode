package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.Component;

public class Selectable implements Component {
    private final String label;
    private final String[] items;
    private final int selectedItem;
    private final Runnable callback;
    private final String id;

    public Selectable(String label, String[] items, int selectedItem, Runnable callback, String id) {
        this.label = label;
        this.items = items;
        this.selectedItem = selectedItem;
        this.callback = callback;
        this.id = id;
    }

    @Override
    public void render(View view) {

    }

    public static class Builder {
        private String label = "";
        private String[] items = new String[0];
        private int selectedItem = -1;
        private Runnable callback = () -> {};
        private String id = "";

        public Builder label() {
            this.label = label;
            return this;
        }

        public Builder items(String[] items) {
            this.items = items;
            return this;
        }

        public Builder selectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
            return this;
        }

        public Builder callback(Runnable callback) {
            this.callback = callback;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        private Selectable build() {
            return new Selectable(this.label, this.items, this.selectedItem, this.callback, this.id);
        }
    }
}
