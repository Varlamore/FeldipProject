package com.cryptic.cache.graphics.widget.impl.questtabs;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.DEFAULT_TEXT_COLOR;

public class ModernInformationTab extends InterfaceBuilder {
    public ModernInformationTab() {
        super(80050);
    }

    @Override
    public void build() {

        int x = 0;
        int y = 3;

        addConfigButton(nextInterface(), 80000, 2125, 2124, 34, 21, "Character summary", 0, 5, 1306);
        child(40 + x, y);
        addConfigButton(nextInterface(), 80000, 2127, 2126, 34, 21, "Quests", 1, 5, 1306);
        child(80 + x, y);
        addConfigButton(nextInterface(), 80000, 2129, 2128, 34, 21, "Achievements", 2, 5, 1306);
        child(120 + x, y);

        drawPixels(nextInterface(), 192, 1, 0x544b40, 0x544b40, 100);
        child(0, 23);

        addTitleText(nextInterface(), "Information");
        child(94 + x, 25 + y);

        addSprite(nextInterface(), 2116);
        child(3, y + 45);

        hoverButton(nextInterface(), 1030, 1031, "Open Presets", 0, 0, "", false);
        child(6, 230);

        Widget container = addTabInterface(nextInterface());
        child(8, 55);
        container.width = 157;
        container.height = 148;
        container.scrollMax = 500;
        int childIds = 0;
        int yPads = 0;
        container.totalChildren(100);

        for (int j = 0; j < 100; j++) {
            addClickableText(nextInterface(), "Some information can go here", "Select", fonts, 0, DEFAULT_TEXT_COLOR, false, true, 130);
            container.child(childIds++, lastInterface(), 5, 6 + yPads);
            yPads += 17;
        }


    }
}
