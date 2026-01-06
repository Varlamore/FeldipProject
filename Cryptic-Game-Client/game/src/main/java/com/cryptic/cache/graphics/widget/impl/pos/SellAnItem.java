package com.cryptic.cache.graphics.widget.impl.pos;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.*;

public class SellAnItem extends InterfaceBuilder {
    public SellAnItem() {
        super(81800);
    }

    @Override
    public void build() {

        //Overview background
        addSprite(nextInterface(), 992);
        child(12, 25);

        addTitleText(nextInterface(), "Player Owned Shop");
        child(270, 34);

        //Small close button
        closeButton(nextInterface(), 24, 25, true);
        child(479, 33);

        final String[] CATEGORY_NAMES = new String[] {
                "Overview", "Buy an Item", "Sell an Item", "Trade History", "Recent Listing"
        };
        final int[] CATEGORY_ICONS = new int[] {
                1006, 1015, 990, 1016, 1016
        };

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

        addItem(nextInterface(), false);
        child(163, 70);

        addText(nextInterface(), "Iron Full Helm", fonts, 0, DEFAULT_TEXT_COLOR, false, true);
        child(205, 84);

        addText(nextInterface(), "Current Sales", fonts, 0, YELLOW_COLOR, true, true);
        child(410, 77);
        addText(nextInterface(), "204", fonts, 0, LIGHT_GREY, true, true);
        child(410, 89);

        addText(nextInterface(), "Avg. Sell time", fonts, 0, YELLOW_COLOR, true, true);
        child(216, 110);
        addText(nextInterface(), "6H", fonts, 0, LIGHT_GREY, true, true);
        child(310, 110);

        addText(nextInterface(), "Market price", fonts, 0, YELLOW_COLOR, true, true);
        child(380, 110);
        addText(nextInterface(), "349M", fonts, 0, GREEN_COLOR, true, true);
        child(465, 110);

        addText(nextInterface(), "Quantity", fonts, 0, YELLOW_COLOR, true, true);
        child(236, 134);
        addText(nextInterface(), "1", fonts, 0, LIGHT_GREY, true, true);
        child(235, 150);

        addText(nextInterface(), "Price per item", fonts, 0, YELLOW_COLOR, true, true);
        child(400, 134);
        addText(nextInterface(), "250,398,345", fonts, 0, GREEN_COLOR, true, true);
        child(400, 150);

        hoverButton(nextInterface(), 998, 1001, "-1", 0, 0, "", false);
        child(167, 147);

        hoverButton(nextInterface(), 999, 1000, "+1", 0, 0, "", false);
        child(285, 147);

        hoverButton(nextInterface(), 998, 1001, "-1", 0, 0, "", false);
        child(318, 147);

        hoverButton(nextInterface(), 999, 1000, "+1", 0, 0, "", false);
        child(468, 147);

        hoverButton(nextInterface(), 995, 996, "+1", 0, LIGHT_GREY, "+1", true);
        child(167, 165);
        hoverButton(nextInterface(), 995, 996, "+10", 0, LIGHT_GREY, "+10", true);
        child(201, 165);
        hoverButton(nextInterface(), 995, 996, "+100", 0, LIGHT_GREY, "+100", true);
        child(235, 165);
        hoverButton(nextInterface(), 995, 996, "...", 0, LIGHT_GREY, "...", true);
        child(269, 165);

        hoverButton(nextInterface(), 995, 996, "-5%", 0, LIGHT_GREY, "-5%", true);
        child(317, 165);
        hoverButton(nextInterface(), 995, 996, "", 0, LIGHT_GREY, "", false);
        child(351, 165);
        hoverButton(nextInterface(), 995, 996, "+...", 0, LIGHT_GREY, "...", true);
        child(385, 165);
        hoverButton(nextInterface(), 995, 996, "+5%", 0, LIGHT_GREY, "+5%", true);
        child(419, 165);
        hoverButton(nextInterface(), 995, 996, "Confirm", 0, LIGHT_GREY, "", false);
        child(453, 165);

        addSprite(nextInterface(), 993);
        child(361, 171);

        addSprite(nextInterface(), 994);
        child(461, 171);

        Widget container = addTabInterface(nextInterface());
        child(163, 202);
        container.width = 311;
        container.height = 97;
        container.scrollMax = 188;
        int childIds = 0;
        container.totalChildren(42);

        int lighterBackingY = 0;
        int offerSlotTextY = 0;
        int darkerBackingY = 0;
        int itemContainerY = 0;
        int sellerTextY = 0;
        int sellerNameY = 0;
        int priceTitleY = 0;
        int priceTextY = 0;
        for (int j = 0; j < 3; j++) {
            //hoverButton(nextInterface(), 988, 988, "Offer", 0, 0, "", false);
            addSprite(nextInterface(), 988);
            container.child(childIds++, lastInterface(), 0, lighterBackingY);
            lighterBackingY += 76;

            addSprite(nextInterface(), 989);
            //hoverButton(nextInterface(), 989, 989, "Offer", 0, 0, "", false);
            container.child(childIds++, lastInterface(), 0, 38 + darkerBackingY);
            darkerBackingY += 76;
        }
        for (int j = 0; j < 6; j++) {

            addItem(nextInterface(), false);
            container.child(childIds++, lastInterface(), 5, 10 + itemContainerY);
            itemContainerY += 38;

            addText(nextInterface(), "Iron Full Helm", fonts, 1, DEFAULT_TEXT_COLOR, false, true);
            container.child(childIds++, lastInterface(), 45, 11 + offerSlotTextY);
            offerSlotTextY += 38;

            addText(nextInterface(), "Seller", fonts, 1, YELLOW_COLOR, true, true);
            container.child(childIds++, lastInterface(), 190, 5 + sellerTextY);
            sellerTextY += 38;

            addText(nextInterface(), "Clive", fonts, 1, LIGHT_GREY, true, true);
            container.child(childIds++, lastInterface(), 190, 19 + sellerNameY);
            sellerNameY += 38;

            addText(nextInterface(), "Price", fonts, 1, YELLOW_COLOR, true, true);
            container.child(childIds++, lastInterface(), 265, 5 + priceTitleY);
            priceTitleY += 38;

            addText(nextInterface(), "250M", fonts, 1, GREEN_COLOR, true, true);
            container.child(childIds++, lastInterface(), 265, 19 + priceTextY);
            priceTextY += 38;
        }

    }
}
