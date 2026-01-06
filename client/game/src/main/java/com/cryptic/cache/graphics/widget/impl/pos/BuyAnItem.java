package com.cryptic.cache.graphics.widget.impl.pos;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import java.text.DecimalFormatSymbols;

import static com.cryptic.cache.graphics.widget.ColourConstants.*;

public class BuyAnItem extends InterfaceBuilder {
    public BuyAnItem() {
        super(81250);
    }

    @Override
    public void build() {

        //Overview background
        addSprite(nextInterface(), 991);
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

        final String[] PLAYER_DATA = new String[] {"Open Offers", "Item Volume"};
        int textPosX = 0;
        for (String playerData : PLAYER_DATA) {
            addText(nextInterface(), playerData, fonts, 0, DEFAULT_TEXT_COLOR, false, true);
            child(193 + textPosX, 79);
            textPosX += 113;
        }

        addText(nextInterface(), "2344", fonts, 0, LIGHT_GREY, false, true);
        System.out.println("open offers text: " + lastInterface());
        child(170, 102);

        addText(nextInterface(), "127k", fonts, 0, WHITE_COLOR, true, true);
        System.out.println("volume text: " + lastInterface());
        child(333, 102);

        addInputField(nextInterface(), 25, LIGHT_GREY, "Search player", 85, 18, false, true, "[A-Za-z0-9 ]");
        child(403, 67);

        addInputField(nextInterface(), 25, LIGHT_GREY, "Search item", 85, 18, false, true, "[A-Za-z0-9 ]");
        child(403, 97);

        hoverButton(nextInterface(), 997, 1004, "Clear", 0, 0, "", false);
        child(482, 72);

        hoverButton(nextInterface(), 997, 1004, "Clear", 0, 0, "", false);
        child(482, 102);

        Widget container = addTabInterface(nextInterface());
        child(163, 126);
        container.width = 311;
        container.height = 174;
        container.scrollMax = 381;
        int childIds = 0;
        container.totalChildren(70);

        int lighterBackingY = 0;
        int offerSlotTextY = 0;
        int darkerBackingY = 0;
        int itemContainerY = 0;
        int sellerTextY = 0;
        int sellerNameY = 0;
        int priceTitleY = 0;
        int priceTextY = 0;
        for (int j = 0; j < 5; j++) {
            hoverButton(nextInterface(), 988, 988, "Offer", 0, 0, "", false);
            //addSprite(nextInterface(), 988);
            container.child(childIds++, lastInterface(), 0, lighterBackingY);
            lighterBackingY += 76;

            //addSprite(nextInterface(), 989);
            hoverButton(nextInterface(), 989, 989, "Offer", 0, 0, "", false);
            container.child(childIds++, lastInterface(), 0, 38 + darkerBackingY);
            darkerBackingY += 76;
        }

        int xOff = 10;
        for (int j = 0; j < 10; j++) {

            addItem(nextInterface(), false);
            container.child(childIds++, lastInterface(), 1, itemContainerY);
            itemContainerY += 38;

            addText(nextInterface(), "Iron Full Helm", fonts, 1, DEFAULT_TEXT_COLOR, false, true);
            container.child(childIds++, lastInterface(), 45, 11 + offerSlotTextY);
            offerSlotTextY += 38;

            addText(nextInterface(), "Seller", fonts, 1, YELLOW_COLOR, true, true);
            container.child(childIds++, lastInterface(), 190 + xOff, 5 + sellerTextY);
            sellerTextY += 38;

            addText(nextInterface(), "Clive", fonts, 1, LIGHT_GREY, true, true);
            container.child(childIds++, lastInterface(), 190 + xOff, 19 + sellerNameY);
            sellerNameY += 38;

            addText(nextInterface(), "Price", fonts, 1, YELLOW_COLOR, true, true);
            container.child(childIds++, lastInterface(), 250 + xOff, 5 + priceTitleY);
            priceTitleY += 38;

            addText(nextInterface(), "250M", fonts, 1, GREEN_COLOR, true, true);
            container.child(childIds++, lastInterface(), 250 + xOff, 19 + priceTextY);
            priceTextY += 38;
        }

    }
}
