package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class PalmTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.PALM_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 41;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5972, 15); // 15 Papayas
    }

    @Override
    public int getLevelRequired() {
        return 68;
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public double getPlantingXp() {
        return 110.5;
    }

    @Override
    public double getCheckHealthXp() {
        return 10150.1;
    }

    @Override
    public double getHarvestXp() {
        return 41.5;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.COCONUT;
    }

    @Override
    public int getStartingVarbitValue() {
        return 200; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (206) used as "Empty/Check Health" hack.
        return 206;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 207; // Zenyte "Product Stage 1" (207) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 212 + stage; // Zenyte 213 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 218 + stage; // Zenyte 219 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 225; // Zenyte Stump
    }
}
