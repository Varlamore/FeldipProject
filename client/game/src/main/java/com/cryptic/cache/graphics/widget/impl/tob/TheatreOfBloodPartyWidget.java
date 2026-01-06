package com.cryptic.cache.graphics.widget.impl.tob;

import com.cryptic.cache.graphics.widget.ColourConstants;
import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.DEFAULT_TEXT_COLOR;
import static com.cryptic.cache.graphics.widget.ColourConstants.RED_COLOR;

public class TheatreOfBloodPartyWidget extends InterfaceBuilder {
    public TheatreOfBloodPartyWidget() {
        super(76000);
    }

    @Override
    public void build() {

        addBackgroundImage(nextInterface(), 375, 230, true);
        child(75, 55);

        addTitleText(nextInterface(), "Theatre Of Blood");
        System.out.println("party name id: " + getCurrentInterfaceId());
        child(270, 65);

        closeButton(nextInterface(), 107, 108, true);
        System.out.println("close id: " + getCurrentInterfaceId());
        child(423, 62);

        hoverButton(nextInterface(), 1856, 1857, "Create Party", 1, DEFAULT_TEXT_COLOR, "Create", true);
        System.out.println("create id: " + getCurrentInterfaceId());
        child(97, 248);

        hoverButton(nextInterface(), 1856, 1857, "Disband Party", 1, RED_COLOR, "Disband", true);
        System.out.println("disband id: " + getCurrentInterfaceId());
        child(227, 248);

        addText(nextInterface(), "Refresh", fonts, 1, DEFAULT_TEXT_COLOR, true, true);
        System.out.println("refresh id: " + getCurrentInterfaceId());
        child(379, 255);
        hoverButton(nextInterface(), 2097, 2098, "Refresh", 1, DEFAULT_TEXT_COLOR, "", true);
        System.out.println("refresh 2 id: " + getCurrentInterfaceId());
        child(409, 252);

        int[] spriteId = {431, 777, 780, 786, 792, 783, 778, 789};
        int[] spriteXPadding = {27, 25, 28, 32, 32, 30, 28, 26};
        int x = 0;
        for (int i = 0; i < spriteId.length; i++) {//Draw stat sprites
            addSprite(nextInterface(), spriteId[i]);
            int yPos = i == 0 ? 92 : i == 1 ? 94 : 95;
            child((i == 0 ? 187 : 197) + x, yPos);
            x += spriteXPadding[i];
        }

        int xPadding = 0;
        for(int j = 0; j < 2; j++) {//Pixels being drawn down the sides of the player info
            drawPixels(nextInterface(), 1, 121, 0x000000, 0x000000, 0);
            child(92 + xPadding, 124);
            xPadding += 341;
        }

        int yPadding = 0;
        for(int j = 0; j < 3; j++) {//Pixels being drawn every other player
            Widget.addRectangle(nextInterface(), 342, 24, 0x000000, 0, false);
            child(92, 124 + yPadding);
            Widget.addRectangle(nextInterface(), 340, 22, 0x645747, 0, true);
            child(93, 125 + yPadding);
            yPadding += 49;
        }

        int y = 0;
        for (int k = 0; k < 5; k++) {//Draw player names
            addClickableText(nextInterface(), "Player Name", "Kick", fonts, 0, ColourConstants.WHITE_COLOR, true, true, 500);
            System.out.println("name id: " + getCurrentInterfaceId());
            //addText(nextInterface(), "Player Name", fonts, 0, ColourConstants.WHITE_COLOR, true, false);
            child(-112, 130 + y);

            int textX = 0;
            for (int i = 0; i < 8; i++) {//Stat texts
                addText(nextInterface(), "99", fonts, 0, ColourConstants.WHITE_COLOR, true, true);
                child(202 + textX, 130 + y);
                textX += 30;
            }

            y += 25;
        }

    }
}
