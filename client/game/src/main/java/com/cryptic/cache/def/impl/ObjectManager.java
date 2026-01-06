package com.cryptic.cache.def.impl;

import com.cryptic.cache.def.ObjectDefinition;

import static com.cryptic.ClientConstants.*;

public class ObjectManager {

    public static void get(int id) {
        ObjectDefinition definition = ObjectDefinition.get(id);

        if (id == 27095) {
            definition.name = "Portal";
        }

        if (id == 32629) {
            definition.actions = new String[]{"Loot", null, null, null, null};
        }

        if (id == 29241) {
            definition.actions = new String[]{"Drink", null, null, null, null};
        }

        if (id == 2341 || id == 2342 || id == 17977 || id == 26741) {
            definition.actions = new String[]{null, null, null, null, null};
        }

        if (id == 6437) {
            definition.name = "Tom Riddle’s gravestone";
            definition.actions = new String[]{"Reward", null, null, null, null};
        }

        if (id == 33456) {
            definition.name = "Portkey";
            definition.actions = new String[]{"Apparate", null, null, null, null};
        }

        if (id == 13503) {
            definition.actions = new String[]{"Leave", null, null, null, null};
        }

        if (id == 31858) {
            definition.actions = new String[]{ "Switch-Moderns", "Switch-Ancients", "Switch-Lunars", null, null};
        }

        if (id == 31621) {
            definition.name = "50s";
        }

        if (id == 31622) {
            definition.name = "Member cave";
        }

        if (id == 31618) {
            definition.name = "gdz";
        }

        if (id == 2515) {
            definition.actions = new String[]{"Travel", null, null, null, null};
        }

        if (id == 10060 || id == 7127 || id == 31626 || id == 4652 || id == 4653) {
            definition.actions = new String[]{null, null, null, null, null};
        }

        if (id == 562 || id == 3192) {
            definition.actions = new String[]{"Live scoreboard", "Todays top pkers", null, null, null};
        }

        if (id == 6552) {
            definition.actions = new String[]{"Change spellbook", null, null, null, null};
        }

        if (id == 29165) {
            definition.name = "Pile Of Coins";
            definition.actions[0] = null;
            definition.actions[1] = null;
            definition.actions[2] = null;
            definition.actions[3] = null;
            definition.actions[4] = null;
        }

        if(id == 33020) {
            definition.name = "Forging table";
            definition.actions = new String[] {"Forge", null, null, null, null};
        }

        if (id == 8878) {
            definition.name = "Item dispenser";
            definition.actions = new String[]{"Dispense", "Exchange coins", null, null, null};
        }

        if (id == 637) {
            definition.name = "Item cart";
            definition.actions = new String[]{"Check cart", "Item list", "Clear cart", null, null};
        }

        if (id == 13291) {
            definition.actions = new String[]{"Open", null, null, null, null};
        }

        if (id == 23709) {
            definition.actions[0] = "Use";
        }

        if (id == 2156) {
            definition.name = "World Boss Portal";
        }

        if (id == 27780) {
            definition.name = "Scoreboard";
        }

        if (id == 14986) {
            definition.name = "Key Chest";

            ObjectDefinition deadmanChest = ObjectDefinition.get(27269);

            definition.modelIds = deadmanChest.modelIds;
            definition.recolorFrom = deadmanChest.recolorFrom;
            definition.actions = deadmanChest.actions;
            definition.recolorTo = deadmanChest.recolorTo;
        }

        if (id == 2654) {
            definition.name = HALLOWEEN ? "Blood fountain" : WINTER ? "Snow fountain" : "Fountain";
            definition.sizeX = 3;
            definition.sizeY = 3;
            definition.actions[0] = HALLOWEEN || WINTER ? "Rewards" : null;
            definition.actions[1] = null;
            if (HALLOWEEN) {
                definition.recolorFrom = new short[]{10266, 10270, 10279, 10275, 10283, (short) 33325, (short) 33222};
                definition.recolorTo = new short[]{10266, 10270, 10279, 10275, 10283, 926, 926};
            } else if (WINTER) {
                definition.recolorFrom = new short[]{10266, 10270, 10279, 10275, 10283, (short) 33325, (short) 33222};
                definition.recolorTo = new short[]{10266, 10270, 10279, 10275, 10283, 127, 126};
            }
        }

        if (id == 1276 || id == 1278 || id == 1279) { //christmas prep
            if (CHRISTMAS) {
                definition.modelIds = new int[]{20491, 20492, 20493, 20494};
                definition.animationId = 5058;
                definition.ambient = 30;
                definition.contrast = 30;
            }
        }
    }
}
