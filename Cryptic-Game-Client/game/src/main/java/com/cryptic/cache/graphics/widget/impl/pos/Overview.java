package com.cryptic.cache.graphics.widget.impl.pos;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.*;

public class Overview extends InterfaceBuilder {
    public Overview() {
        super(81050);
    }

    @Override
    public void build() {

        //Overview background
        addSprite(nextInterface(), 1005);
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

        hoverButton(nextInterface(), 1007, 1008, "Add-to coffer", 0, 0, "", false);
        child(245, 75);

        final String[] PLAYER_DATA = new String[] {"My coins", "My trades", "Global trades"};
        int textPosX = 0;
        for (String playerData : PLAYER_DATA) {
            addText(nextInterface(), playerData, fonts, 0, DEFAULT_TEXT_COLOR, false, true);
            child(185 + textPosX, 80);
            textPosX += 115;
        }

        final String[] DATA = new String[] {"123,634,213M", "312", "5433"};
        int dataPosX = 0;
        for (String data : DATA) {
            addText(nextInterface(), data, fonts, 0, LIGHT_GREY, false, true);
            System.out.println("data text Id's: " + lastInterface());
            child(167 + dataPosX, 102);
            dataPosX += 113;
        }

        int lightBackingY = 0;
        int spotFeatureY = 0;
        for(int i = 0; i < 3; i++) {
            addSprite(nextInterface(), 1009);
            child(385, 126 + lightBackingY);
            lightBackingY += 69;
            addClickableText(nextInterface(), "Click to purchase", "Purchase", fonts, 0, DEFAULT_TEXT_COLOR, true, true, 85);
            System.out.println("Click to purchase text: " + lastInterface());
            child(395, 131 + spotFeatureY);
            addClickableText(nextInterface(), "a featured spot", "Purchase", fonts, 0, DEFAULT_TEXT_COLOR, true, true, 85);
            System.out.println("a featured spot text: " + lastInterface());
            child(395, 141 + spotFeatureY);
            spotFeatureY += 72;
        }

        int darkBackingY = 0;
        int spotFeatureY2 = 0;
        for(int i = 0; i < 2; i++) {
            addSprite(nextInterface(), 1011);
            child(385, 161 + darkBackingY);
            darkBackingY += 69;
            addClickableText(nextInterface(), "Click to purchase", "Purchase", fonts, 0, DEFAULT_TEXT_COLOR, true, true, 85);
            System.out.println("Click to purchase text: " + lastInterface());
            child(395, 167 + spotFeatureY2);
            addClickableText(nextInterface(), "a featured spot", "Purchase", fonts, 0, DEFAULT_TEXT_COLOR, true, true, 85);
            System.out.println("a featured spot text: " + lastInterface());
            child(395, 177 + spotFeatureY2);
            spotFeatureY2 += 72;
        }


        Widget container = addTabInterface(nextInterface());
        child(163, 126);
        container.width = 198;
        container.height = 174;
        container.scrollMax = 381;
        int childIds = 0;
        container.totalChildren(20);

        int lighterBackingY = 0;
        int offerSlotTextY = 0;
        int darkerBackingY = 0;
        for (int j = 0; j < 5; j++) {
            addSprite(nextInterface(), 1010);
            container.child(childIds++, lastInterface(), 0, lighterBackingY);
            lighterBackingY += 76;

            addSprite(nextInterface(), 1012);
            container.child(childIds++, lastInterface(), 0, 38 + darkerBackingY);
            darkerBackingY += 76;
        }
        for (int j = 0; j < 10; j++) {
            addText(nextInterface(), "Available Offer Slot", fonts, 1, LIGHT_GREY, true, true);
            System.out.println("Available offer slot text: " + lastInterface());
            container.child(childIds++, lastInterface(), 100, 11 + offerSlotTextY);
            offerSlotTextY += 38;
        }

        Widget offersContainer = addTabInterface(nextInterface());
        child(163, 126);
        offersContainer.width = 198;
        offersContainer.height = 174;
        offersContainer.scrollMax = 381;
        int childId = 0;
        offersContainer.totalChildren(60);

        int lighterBackingsY = 0;
        int itemNameTextY = 0;
        int itemContainerY = 0;
        int darkerBackingsY = 0;
        int progressBarY = 0;
        int progressBarTextsY = 0;
        int offerListingButtonY = 0;
        for (int j = 0; j < 5; j++) {
            addSprite(nextInterface(), 988);
            offersContainer.child(childId++, lastInterface(), 0, lighterBackingsY);
            lighterBackingsY += 76;

            addSprite(nextInterface(), 989);
            offersContainer.child(childId++, lastInterface(), 0, 38 + darkerBackingsY);
            darkerBackingsY += 76;
        }

        int offsetY = 8;
        int offsetX = 3;
        for (int j = 0; j < 10; j++) {
            addItem(nextInterface(), false);
            offersContainer.child(childId++, lastInterface(), (int) (4.8 - offsetX), 8 + itemContainerY - offsetY);
            itemContainerY += 38;

            //item name
            addText(nextInterface(), "Iron Full Helm", fonts, 1, DEFAULT_TEXT_COLOR, false, true);
            offersContainer.child(childId++, lastInterface(), 45, 4 + itemNameTextY);
            itemNameTextY += 38;

            //Progress bars for achievement
            drawProgressBar(nextInterface(),145, 10,100,0,0);
            System.out.println("Progress bar begins at: " + lastInterface());
            offersContainer.child(childId++, lastInterface(),45, 23 + progressBarY);
            progressBarY += 38;

            addText(nextInterface(), "5/6 | 2k (ea)", fonts, 0, WHITE_COLOR, true, true);
            System.out.println("Progress text data : " + lastInterface());
            offersContainer.child(childId++, lastInterface(), 118, 23 + progressBarTextsY);
            progressBarTextsY += 38;

            hoverButton(nextInterface(), 997, 1004, "Remove listing", 0, 0, "", false);
            offersContainer.child(childId++, lastInterface(),180, 5 + offerListingButtonY);
            offerListingButtonY += 38;
        }


    }
}
