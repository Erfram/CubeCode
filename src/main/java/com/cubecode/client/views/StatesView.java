package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import dev.latvian.mods.rhino.mod.util.NbtType;
import imgui.ImGui;
import imgui.flag.*;
import imgui.type.*;
import net.minecraft.nbt.*;

import java.util.List;
import java.util.Set;

public class StatesView extends View {
    NbtCompound nbt;
    List<String> playerNames;

    String currentState = "Server^";

    public StatesView(NbtCompound nbt, List<String> playerNames) {
        this.nbt = nbt;
        this.playerNames = playerNames;
    }

    @Override
    public String getName() {
        return "States##"+this.getUniqueID();
    }

    @Override
    public void render() {
        Window.create()
            .title(getName())
            .callback(() -> {
                ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
                ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 0.0f);

                if (ImGui.beginCombo("##target", this.currentState.replace("^", ""))) {
                    if (ImGui.selectable("Server", this.currentState.equals("Server^"))) {
                        this.currentState = "Server^";
                    }

                    this.playerNames.forEach(playerName -> {
                        if (ImGui.selectable(playerName, this.currentState.equals(playerName))) {
                            this.currentState = playerName;
                        }
                    });

                    ImGui.endCombo();
                }

                ImGui.popStyleVar(2);
                ImGui.separator();

                NbtCompound statesCompound = this.nbt.getCompound(this.currentState);
                Set.of(statesCompound.getKeys().toArray(String[]::new)).forEach(stateKey -> {
                    NbtElement stateValue = statesCompound.get(stateKey);

                    this.putVariable(stateKey, new ImString(stateKey, 9999));
                    String valueKey = this.getValueKey(stateKey);
                    ImString newStateKey = this.getVariable(stateKey);
                    boolean contains = !stateKey.equals(newStateKey.get()) && statesCompound.getKeys().contains(newStateKey.get());

                    ImGui.pushItemWidth(150);
                    ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
                    ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 0.0f);

                    if (contains) {
                        ImGui.pushStyleColor(ImGuiCol.Text, 255, 0, 0, 255);
                    }

                    if (!ImGui.inputText("##" + stateKey, newStateKey) && ImGui.isItemDeactivatedAfterEdit()) {
                        if (!statesCompound.getKeys().contains(newStateKey.get())) {
                            this.removeVariable(stateKey);
                            this.removeVariable(this.getValueKey(stateKey));
                            statesCompound.remove(stateKey);
                            statesCompound.put(newStateKey.get(), stateValue);
                            valueKey = this.getValueKey(newStateKey.get());
                        }
                        else if (!stateKey.equals(newStateKey.get())) {
                            newStateKey.set(stateKey);
                        }
                    }

                    if (contains) {
                        ImGui.popStyleColor();
                    }

                    ImGui.popStyleVar(2);
                    ImGui.popItemWidth();
                    ImGui.sameLine();

                    ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1.0f);
                    ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 0.0f);

                    byte stateType = stateValue.getType();
                    if (stateType == NbtElement.INT_TYPE) {
                        this.putVariable(valueKey, new ImInt(((NbtInt) stateValue).intValue()));
                        ImInt value = this.getVariable(valueKey);

                        if (!ImGui.dragScalar("##" + valueKey, ImGuiDataType.S32, value, 1f) && ImGui.isItemDeactivatedAfterEdit()) {
                            statesCompound.putInt(newStateKey.get(), value.get());
                        }
                    }
                    else if (stateType == NbtElement.DOUBLE_TYPE) {
                        this.putVariable(valueKey, new ImDouble(((NbtDouble) stateValue).doubleValue()));
                        ImDouble value = this.getVariable(valueKey);

                        if (!ImGui.dragScalar("##" + valueKey, ImGuiDataType.Double, value, 0.1f) && ImGui.isItemDeactivatedAfterEdit()) {
                            statesCompound.putDouble(newStateKey.get(), value.doubleValue());
                        }
                    }
                    else if (stateType == NbtElement.FLOAT_TYPE) {
                        this.putVariable(valueKey, new ImFloat(((NbtFloat) stateValue).floatValue()));
                        ImFloat value = this.getVariable(valueKey);

                        if (!ImGui.dragScalar("##" + valueKey, ImGuiDataType.Float, value, 0.1f) && ImGui.isItemDeactivatedAfterEdit()) {
                            statesCompound.putFloat(newStateKey.get(), value.floatValue());
                        }
                    }
                    else if (stateType == NbtElement.STRING_TYPE) {
                        this.putVariable(valueKey, new ImString(stateValue.asString(), 9999));
                        ImString value = this.getVariable(valueKey);

                        if (!ImGui.inputText("##" + valueKey, value) && ImGui.isItemDeactivatedAfterEdit()) {
                            statesCompound.putString(newStateKey.get(), value.get());
                        }
                    }

                    ImGui.popStyleVar(2);
                });
            })
            .render(this);
    }

    private String getValueKey(String stateKey) {
        return stateKey + "_value";
    }
}
