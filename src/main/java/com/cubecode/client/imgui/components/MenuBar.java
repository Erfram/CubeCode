package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.AbstractBuilder;
import com.cubecode.client.imgui.components.basic.CommonProperties;
import com.cubecode.client.imgui.components.basic.Component;
import com.cubecode.utils.CubeCodeException;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class MenuBar implements Component {
    private final CommonProperties commonProperties;
    private final List<Menu> menus;
    private final Runnable callback;
    private final String id;

    public static Builder builder() {
        return new Builder();
    }

    public MenuBar(List<Menu> menus, Runnable callback, String id, CommonProperties commonProperties) {
        this.menus = menus;
        this.callback = callback;
        this.id = id;
        this.commonProperties = commonProperties;
    }

    @Override
    public void render(View view) {
        if (ImGui.beginMenuBar()) {
            this.menus.forEach(menu -> menu.render(view));
            this.callback.run();
            ImGui.endMenuBar();
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private final List<Menu> menus = new ArrayList<>();
        private String id = "default";
        private Runnable callback = () -> {};

        public Builder menu(Menu... menu) {
            this.menus.addAll(List.of(menu));
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder callback(Runnable callback) {
            this.callback = callback;
            return this;
        }

        public MenuBar build() {
            if (this.menus.contains(null)) {
                try {
                    throw new CubeCodeException("there is null in the menu list, remove it");
                } catch (CubeCodeException e) {
                    throw new RuntimeException(e);
                }
            }

            return new MenuBar(this.menus, this.callback, this.id, this.commonProperties);
        }
    }

    public static class Menu implements Component {
        private final CommonProperties commonProperties;
        private final String label;
        private final boolean enabled;
        private List<Item> items = new ArrayList<>();
        private final Runnable callback;
        private final String id;

        public static Builder builder() {
            return new Builder();
        }

        public Menu(String label, boolean enabled, List<Item> items, Runnable callback, String id, CommonProperties commonProperties) {
            this.label = label;
            this.enabled = enabled;
            this.items = items;
            this.callback = callback;
            this.id = id;
            this.commonProperties = commonProperties;
        }

        @Override
        public void render(View view) {
            view.putVariable(this.id, this.enabled);

            boolean enabled = view.getVariable(this.id);
            if (ImGui.beginMenu(this.label, enabled)) {
                this.callback.run();
                this.items.forEach(item -> item.render(view));
                ImGui.endMenu();
            }
        }

        public static class Builder extends AbstractBuilder<Builder> {
            private String label = "menu";
            private boolean enabled = true;
            private final List<Item> items = new ArrayList<>();
            private Runnable callback = () -> {};
            private String id = "default";

            public Builder label(String label) {
                this.label = label;
                return this;
            }

            public Builder enabled(boolean enabled) {
                this.enabled = enabled;
                return this;
            }

            public Builder item(Item... items) {
                this.items.addAll(List.of(items));
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

            public Menu build() {
                if (this.items.contains(null)) {
                    try {
                        throw new CubeCodeException("there is null in the menu list, remove it");
                    } catch (CubeCodeException e) {
                        throw new RuntimeException(e);
                    }
                }


                return new Menu(this.label, this.enabled, this.items, this.callback, this.id, this.commonProperties);
            }
        }
    }

    public static class Item implements Component {
        private final CommonProperties commonProperties;
        private final String label;
        private final String shortcut;
        private final boolean selected;
        private final boolean enabled;
        private final Runnable callback;

        public static Builder builder() {
            return new Builder();
        }

        public Item(String label, String shortcut, boolean selected, boolean enabled, Runnable callback, CommonProperties commonProperties) {
            this.label = label;
            this.shortcut = shortcut;
            this.selected = selected;
            this.enabled = enabled;
            this.callback = callback;
            this.commonProperties = commonProperties;
        }

        @Override
        public void render(View view) {
            if (ImGui.menuItem(this.label, this.shortcut, this.selected, this.enabled)) {
                this.callback.run();
            }
        }

        public static class Builder extends AbstractBuilder<Builder> {
            private String label = "item";
            private String shortcut = "shortcut";
            private boolean selected = true;
            private boolean enabled = true;
            private Runnable callback = () -> {};

            public Builder label(String label) {
                this.label = label;
                return this;
            }

            public Builder shortcut(String shortcut) {
                this.shortcut = shortcut;
                return this;
            }

            public Builder selected(boolean selected) {
                this.selected = selected;
                return this;
            }

            public Builder enabled(boolean enabled) {
                this.enabled = enabled;
                return this;
            }

            public Builder callback(Runnable callback) {
                this.callback = callback;
                return this;
            }

            public Item build() {
                return new Item(this.label, this.shortcut, this.selected, this.enabled, this.callback, this.commonProperties);
            }
        }
    }
}