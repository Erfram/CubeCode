package com.cubecode.client.scripts.code.ui;

import com.cubecode.client.scripts.code.ui.components.*;
import com.cubecode.client.scripts.code.ui.components.draw.DrawComponent;

public interface CubeCodeUIBuilder {
    TextComponent text(String text);
    IconComponent icon(String iconId);
    ImageComponent image(String iconPath);
    CheckboxComponent checkbox(String id, boolean active);
    ButtonComponent button(String label);
    RadioButtonComponent radioButton(String id, boolean active);
    ArrowButtonComponent arrowButton(String id, int dir);
    InputTextComponent inputText(String label, String text, int maxLength);
    SliderComponent slider(String label, float value, float degreesMin, float degreesMax);
    ChildComponent child(String strId);
    DrawComponent draw();
}
