package com.cubecode.client.views.idea;

import com.cubecode.api.scripts.Script;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.RequestScriptScopeC2SPacket;
import imgui.ImGui;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

import java.util.List;

public class ScopeView extends View {
    public static NbtCompound structure;
    public Script script;

    public ScopeView(Script script) {
        this.script = script;
    }

    @Override
    public void init() {
        super.init();
        updateScope();
    }

    public void updateScope() {
        Dispatcher.sendToServer(new RequestScriptScopeC2SPacket(this.script));
    }

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.windows.CubeCodeIDEA.scope").getString() + "##%s", uniqueID);
    }

    public void render() {
        Window.create()
                .title(getName())
                .callback(() -> {
                    this.renderTreeNodeElement("root", ScopeView.structure);
                })
                .render(this);
    }

    public void renderTreeNodeElement(String key, NbtCompound element) {
        if (element == null) return;
        if(ImGui.treeNodeEx(key)) {
            List<String> keys = element.getKeys().stream().sorted((str1, str2) -> str1.equals("keys") ? 1 : -1).toList();
            keys.forEach(k -> {
                NbtElement nbtElement = element.get(k);
                if (nbtElement instanceof NbtList) {
                    ((NbtList) nbtElement).forEach(s -> ImGui.text(s.asString()));
                } else if (nbtElement instanceof NbtCompound) {
                    renderTreeNodeElement(k, (NbtCompound) nbtElement);
                }
            });
            ImGui.treePop();
        }
    }

    public static void setStructure(NbtCompound structure) {
        ScopeView.structure = structure;
    }
}
