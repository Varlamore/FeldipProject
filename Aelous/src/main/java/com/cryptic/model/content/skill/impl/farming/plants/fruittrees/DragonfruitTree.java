package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class DragonfruitTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.DRAGONFRUIT_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 70;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5974, 15); // 15 Coconuts
    }

    @Override
    public int getLevelRequired() {
        return 81;
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public double getPlantingXp() {
        return 140.0;
    }

    @Override
    public double getCheckHealthXp() {
        return 17335.0;
    }

    @Override
    public double getHarvestXp() {
        return 70.0;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.DRAGONFRUIT;
    }

    @Override
    public int getStartingVarbitValue() {
        return 227; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (233) used as "Empty/Check Health" hack.
        return 233;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 234; // Zenyte "Product Stage 1" (234) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 239 + stage; // Zenyte 240 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 245 + stage; // Zenyte 246 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 252; // Zenyte Stump
    }
}
