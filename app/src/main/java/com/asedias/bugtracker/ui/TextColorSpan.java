package com.asedias.bugtracker.ui;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

/**
 * Created by Рома on 11.07.2017.
 */

public class TextColorSpan extends CharacterStyle {

    private int color;

    public TextColorSpan(int color) {
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(this.color);
    }
}
