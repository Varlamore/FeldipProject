package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.DEFAULT_TEXT_COLOR;

public class ModernShopInterface extends InterfaceBuilder {
    public ModernShopInterface() {
        super(73150);
    }

    @Override
    public void build() {

        addSprite(nextInterface(), 2089);
        child(50, 5);

        addTitleText(nextInterface(), "Store");
        child(260, 15);

        closeButton(nextInterface(), 107, 108, true);
        child(440, 12);

        addText(nextInterface(), "Search:", fonts, 1, DEFAULT_TEXT_COLOR, true, true);
        child(100, 50);

        addInputField(nextInterface(), 25, DEFAULT_TEXT_COLOR, "*", 300, 25, false, true, "[A-Za-z0-9 ]");
        child(155, 43);

        final int[] CATEGORY_IDS = new int[] { 2100, 2099, 2101, 2102, 2103, 2105, 2104, 2106, 2107, 2108, 2109 };

        final String[] CATEGORY_NAMES = new String[] {
                "Weapons", "Armoury", "Ranged", "Magic", "Supplies", "General",
                "PK Points", "Voting", "Donator", "Skilling", "Cosmetics"
        };

        int yPos = 0;
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            addConfigButton(nextInterface(), 73150, 2110, 2112, 76, 20, "Open " +CATEGORY_NAMES[i], i, 5, 1206);
            child(66, 85 + yPos);
            yPos += 21;
        }

        int yPos2 = 0;
        for (int categoryId : CATEGORY_IDS) {
            addSprite(nextInterface(), categoryId);
            child((int) 69.6, 89 + yPos2);
            yPos2 += 21;
        }

        int yPos1 = 0;
        for (String categoryName : CATEGORY_NAMES) {
            addText(nextInterface(), categoryName, fonts, 0, DEFAULT_TEXT_COLOR, false, true);
            child(84, 90 + yPos1);
            yPos1 += 21;
        }

        Widget container = addInterface(nextInterface());
        child(155, 78);
        container.width = 285;
        container.height = 243;
        container.scrollMax = 500;
        container.totalChildren(1);
        addItemGroup(nextInterface(), 5, 20, 30, 15, "Value", "Buy-1", "Buy-5", "Buy-10", "Buy-X");
        container.child(0, lastInterface(), 0, 13);
    }
}
