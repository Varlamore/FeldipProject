package com.cryptic.cache.graphics.widget.impl;

import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import static com.cryptic.cache.graphics.widget.ColourConstants.LIGHT_GREY;

public class ModernPresetManager extends InterfaceBuilder {
    public ModernPresetManager() {
        super(73230);
    }

    @Override
    public void build() {

        addSprite(nextInterface(), 2090);
        child(40, 15);

        addTitleText(nextInterface(), "Preset Manager");
        child(270, 25);

        closeButton(nextInterface(), 2062, 2063, false);
        child(453, 23);

        hoverButton(nextInterface(), 2094, 2093, "Edit Preset", 0, LIGHT_GREY, "Edit This Preset", 24, 17, true);
        child(190, 267);

        hoverButton(nextInterface(), 2096, 2095, "Load Preset", 0, LIGHT_GREY, "Load This Preset", 24, 17, true);
        child(190, 297);

        addSprite(nextInterface(), 2088);
        child(49, 254);

        addText(nextInterface(), "Spellbook", fonts, 0, LIGHT_GREY, true, true);
        child(130, 265);

        addText(nextInterface(), "Regular", fonts, 0, LIGHT_GREY, true, true);
        child(130, 300);

        itemGroup(nextInterface(), 4, 8, (int) (7.8 * 0.6875), 5);
        System.out.println("Item group id for inventory slots = " + getCurrentInterfaceId());
        child(319, 57);

        /**
         * Equipment Interface
         */
        int[] backingXPositions = { 227, 185, 227, 185, 227, 268, 227, 185, 227, 268, 268 };
        int[] backingYPositions = { 54, 95, 95, 136, 136, 136, 177, 218, 218, 218, 95};
        for (int i = 0; i < backingXPositions.length; i++) {
            addSprite(nextInterface(), 1860);
            child(backingXPositions[i], backingYPositions[i]);
        }

        int[] itemXPositions = {234, 191, 234, 191, 234, 275, 227, 234, 234, 191, 234, 275, 275, 275};
        int[] itemYPositions = {57,   98, 98,  139, 139, 138, 177, 180, 221, 221, 221, 221, 221, 98};

        for (int i = 0; i < itemXPositions.length; i++) {
            addItemOnInterface(nextInterface(), 73230, null, 5, 6, 7, 4);
            child(itemXPositions[i], itemYPositions[i]);
        }

        //Main preset container
        Widget container = addInterface(nextInterface());
        child(54, 56);
        container.width = 113;
        container.height = 92;
        container.scrollMax = 155;
        container.totalChildren(16);
        int childId = 0;
        int yPadding = 0;
        int y = 0;

        for(int j = 0; j < 4; j++) {//Pixels being drawn every other player
            Widget.addRectangle(nextInterface(), 114, 18, 0x645747, 100, true);
            container.child(childId++, lastInterface(), 0, 2 + yPadding);
            yPadding += 19;
            Widget.addRectangle(nextInterface(), 114, 18, 0x645747, 0, false);
            container.child(childId++, lastInterface(), 0, 2 + yPadding);
            yPadding += 19;
        }

        for (int k = 0; k < 8; k++) {
            addClickableText(nextInterface(), "Main - Melee", "Select", fonts, 0, LIGHT_GREY, false, true, 113);
            container.child(childId++, lastInterface(), 5, 6 + y);
            y += 19;
        }

        //Custom preset container
        Widget customPresetContainer = addInterface(nextInterface());
        child(54, 158);
        customPresetContainer.width = 113;
        customPresetContainer.height = 92;
        customPresetContainer.scrollMax = 155;
        customPresetContainer.totalChildren(16);
        int childIds = 0;
        int yPaddings = 0;
        int yPads = 0;

        for(int j = 0; j < 4; j++) {//Pixels being drawn every other player
            Widget.addRectangle(nextInterface(), 114, 18, 0x645747, 100, true);
            customPresetContainer.child(childIds++, lastInterface(), 0, 2 + yPaddings);
            yPaddings += 19;
            Widget.addRectangle(nextInterface(), 114, 18, 0x645747, 0, false);
            customPresetContainer.child(childIds++, lastInterface(), 0, 2 + yPaddings);
            yPaddings += 19;
        }

        for (int k = 0; k < 8; k++) {
            addClickableText(nextInterface(), "Click to create", "Create", fonts, 0, LIGHT_GREY, false, true, 113);
            customPresetContainer.child(childIds++, lastInterface(), 5, 6 + yPads);
            yPads += 19;
            System.out.println(getCurrentInterfaceId());
        }

    }


}
