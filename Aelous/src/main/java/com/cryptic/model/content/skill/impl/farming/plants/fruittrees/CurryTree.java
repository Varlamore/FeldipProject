package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class CurryTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.CURRY_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 15;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5416, 5); // 5 Bananas
    }

    @Override
    public int getLevelRequired() {
        return 42;
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public double getPlantingXp() {
        return 40.0;
    }

    @Override
    public double getCheckHealthXp() {
        return 2906.9;
    }

    @Override
    public double getHarvestXp() {
        return 15.0;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.CURRY_LEAF;
    }

    @Override
    public int getStartingVarbitValue() {
        return 99; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (105) used as "Empty/Check Health" hack.
        return 105;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 106; // Zenyte "Product Stage 1" (106) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 111 + stage; // Zenyte 112 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 117 + stage; // Zenyte 118 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 124; // Zenyte Stump
    }
}
