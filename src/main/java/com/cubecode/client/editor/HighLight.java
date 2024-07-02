package com.cubecode.client.editor;

import org.treesitter.TSPoint;

public record HighLight(int start, int end, TSPoint point, int color, String type) {}
