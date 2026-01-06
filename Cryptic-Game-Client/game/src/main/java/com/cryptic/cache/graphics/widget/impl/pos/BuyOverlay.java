package com.cryptic.cache.graphics.widget.impl.pos;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;

import static com.cryptic.cache.graphics.widget.ColourConstants.*;

public class BuyOverlay extends InterfaceBuilder {
    public BuyOverlay() {
        super(81375);
    }

    @Override
    public void build() {
        addTransparentSprite(nextInterface(), 986, 90);
        child(12, 25);

        addSprite(nextInterface(), 987);
        child(173, 80);

        closeButton(nextInterface(), 24, 25, true);
        child(312, 90);

        hoverButton(nextInterface(), 1026, 985, "Purchase Item", 0, LIGHT_GREY, "Purchase This Item", true);
        child(183, 223);

        hoverButton(nextInterface(), 998, 1001, "-1", 0, 0, "", false);
        child(237, 135);

        hoverButton(nextInterface(), 999, 1000, "+1", 0, 0, "", false);
        child(306, 135);

        addText(nextInterface(), "1", fonts, 0, LIGHT_GREY, true, true);
        child(280, 138);

        addItem(nextInterface(), false);
        child(189, 125);

        addText(nextInterface(), "Iron Full Helm", fonts, 0, DEFAULT_TEXT_COLOR, false, true);
        child(190, 177);

        addText(nextInterface(), "Price: 1m", fonts, 0, YELLOW_COLOR, false, true);
        child(190, 189);

        addText(nextInterface(), "Total Cost: 1m", fonts, 0, YELLOW_COLOR, false, true);
        child(190, 201);
    }
}
