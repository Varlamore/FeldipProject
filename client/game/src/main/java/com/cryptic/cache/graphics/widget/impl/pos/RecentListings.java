package com.cryptic.cache.graphics.widget.impl.pos;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.*;

public class RecentListings extends InterfaceBuilder{
    public RecentListings() {
        super(81600);
    }

    @Override
    public void build() {

        //Overview background
        addSprite(nextInterface(), 1017);
        child(12, 25);

        addTitleText(nextInterface(), "Player Owned Shop");
        child(270, 34);

        //Small close button
        closeButton(nextInterface(), 24, 25, true);
        child(479, 33);

        final String[] CATEGORY_NAMES = new String[] {"Overview", "Buy an Item", "Sell an Item", "Trade History", "Recent Listing"};
        final int[] CATEGORY_ICONS = new int[] {1006, 1015, 990, 1016, 1016};

        int yPos = 0;
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
            addConfigButton(nextInterface(), 71050, 1003, 1002, 128, 29, "Show " + CATEGORY_NAMES[i], i, 5, 1406);
            child(21, 63 + yPos);
            yPos += 30;
        }
        int spritePosY = 0;
        for (int categoryIcon : CATEGORY_ICONS) {
            addSprite(nextInterface(), categoryIcon);
            child(30, 66 + spritePosY);
            spritePosY += 30;
        }
        int textPosY = 0;
        for (String categoryName : CATEGORY_NAMES) {
            addText(nextInterface(), categoryName, fonts, 2, DEFAULT_TEXT_COLOR, false, true);
            child(52, 70 + textPosY);
            textPosY += 30;
        }

        Widget container = addTabInterface(nextInterface());
        child(163, 71);
        container.width = 311;
        container.height = 229;
        container.scrollMax = 760;
        int childIds = 0;
        container.totalChildren(140);

        int lighterBackingY = 0;
        int offerSlotTextY = 0;
        int darkerBackingY = 0;
        int itemContainerY = 0;
        int sellerTextY = 0;
        int sellerNameY = 0;
        int priceTitleY = 0;
        int priceTextY = 0;
        for (int j = 0; j < 10; j++) {
            addSprite(nextInterface(), 988);
            container.child(childIds++, lastInterface(), 0, lighterBackingY);
            lighterBackingY += 76;

            addSprite(nextInterface(), 989);
            container.child(childIds++, lastInterface(), 0, 38 + darkerBackingY);
            darkerBackingY += 76;
        }
        for (int j = 0; j < 20; j++) {

            addItem(nextInterface(), true);
            container.child(childIds++, lastInterface(), 1, itemContainerY);
            itemContainerY += 38;

            addText(nextInterface(), "Iron Full Helm", fonts, 0, DEFAULT_TEXT_COLOR, false, true);
            container.child(childIds++, lastInterface(), 45, 13 + offerSlotTextY);
            offerSlotTextY += 38;

            addText(nextInterface(), "Seller", fonts, 0, YELLOW_COLOR, true, true);
            container.child(childIds++, lastInterface(), 190, 6 + sellerTextY);
            sellerTextY += 38;

            addText(nextInterface(), "Clive", fonts, 0, LIGHT_GREY, true, true);
            container.child(childIds++, lastInterface(), 190, 20 + sellerNameY);
            sellerNameY += 38;

            addText(nextInterface(), "Price", fonts, 0, YELLOW_COLOR, true, true);
            container.child(childIds++, lastInterface(), 270, 6 + priceTitleY);
            priceTitleY += 38;

            addText(nextInterface(), "250M | 25k(ea)", fonts, 0, GREEN_COLOR, true, true);
            container.child(childIds++, lastInterface(), 270, 20 + priceTextY);
            priceTextY += 38;
        }
    }
}
