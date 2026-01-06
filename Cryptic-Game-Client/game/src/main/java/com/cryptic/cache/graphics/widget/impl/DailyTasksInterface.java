package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.DEFAULT_TEXT_COLOR;
import static com.cryptic.cache.graphics.widget.ColourConstants.WHITE_COLOR;

public class DailyTasksInterface extends InterfaceBuilder {
    public DailyTasksInterface() {
        super(80750);
    }

    @Override
    public void build() {

        int offX = 6;

        addBackgroundImage(nextInterface(), 485, 306, true);
        child(15, 15);

        addVerticalDivider(nextInterface(), 245);
        child(200, 70);

        addHorizontalDivider(nextInterface(), 185);
        child(20, 70);

        addTitleText(nextInterface(), "Daily Tasks");
        child(250, 25);

        //Small close button
        closeButton(nextInterface(), 24, 25, true);
        child(475, 25);

        //Bottom right reward points
        addText(nextInterface(), 0, DEFAULT_TEXT_COLOR, true, "Reward points: 250");
        System.out.println("Reward point text = " + lastInterface());
        child(75, 27);

        addText(nextInterface(), 2, WHITE_COLOR, true, "Challenges:");
        child(105, 51);

        //Border surrounding information
        addHorizontalDivider(nextInterface(), 180);
        child(20, 220);
        addWrappingText(nextInterface(), "You have 24 hours to complete your tasks which will reset at midnight. Each task completed will reward you with experience and task points to spend as you wish.", fonts, 0, DEFAULT_TEXT_COLOR, true, true, 175);
        child(25, 245);

        drawPixels(nextInterface(), 270, 95, 0x494034, 0, 100);
        child(216, 70);

        addText(nextInterface(), 2, WHITE_COLOR, true, "Objective:");
        child(350, 80);

        addText(nextInterface(), 2, DEFAULT_TEXT_COLOR, true, "Fetch Yew logs: 25");
        System.out.println("Objective name text = " + lastInterface());
        child(350, 100);

        drawProgressBar(nextInterface(),230, 30,50,0,0);
        child(235, 120);
        //Progress bar text
        addText(nextInterface(), 1, WHITE_COLOR, true, "0/25");
        System.out.println("Progress bar text = " + lastInterface());
        child(355, 128);

        addText(nextInterface(), 2, WHITE_COLOR, true, "Rewards:");
        child(350, 170);

        addSprite(nextInterface(), 934);
        System.out.println("Sprite ID for category = " + lastInterface());
        child(305, 197);
        addText(nextInterface(), 0, WHITE_COLOR, true, "+10k XP");
        child(315, 225);
        int x = 0;
        for(int i = 0; i < 3; i++) {
            addToItemGroup(nextInterface(), 3, 3, 0, 0, false, false, new String[] {"", ""});
            child(340 + x, 200);
            x+= 35;
        }

        addHorizontalDivider(nextInterface(), 288);
        child(207, 245);

        addText(nextInterface(), 2, DEFAULT_TEXT_COLOR, true, "5m Coins");
        child(275 + offX, 257);
        hoverButton(nextInterface(), 1018, 1019, "Re-roll Tasks", 1, DEFAULT_TEXT_COLOR, "Re-roll Tasks", true);
        child(215 + offX, 276);

        addText(nextInterface(), 2, DEFAULT_TEXT_COLOR, true, "10m Coins");
        child(415 + offX, 257);
        hoverButton(nextInterface(), 1018, 1019, "Extend Task", 1, DEFAULT_TEXT_COLOR, "Extend Tasks", true);
        child(355 + offX, 276);

        Widget scroll = addTabInterface(nextInterface());
        child(22, 77);
        scroll.width = 162;
        scroll.height = 142;
        scroll.scrollMax = 215;
        int childid = 0;
        scroll.totalChildren(12);

        // Progress bars
        int barY = 36;
        for (int index = 0; index < 6; index++) {
            //select button
            addConfigButton(nextInterface(), scroll.parent, 1023, 1022, 171, 35, "Select Challenge", index, 5, 1109);
            scroll.child(childid++, lastInterface(), 0, (index * barY));
            //Name of task
            addText(nextInterface(), 1, DEFAULT_TEXT_COLOR, true, "Fetch 25 Yew logs");
            scroll.child(childid++, lastInterface(),80, 10 + (index * barY));
        }

    }

}
