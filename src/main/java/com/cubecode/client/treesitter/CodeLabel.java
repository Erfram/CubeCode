package com.cubecode.client.treesitter;

import org.treesitter.TSPoint;

public record CodeLabel(String text, int color, TSPoint point, String type) {}
