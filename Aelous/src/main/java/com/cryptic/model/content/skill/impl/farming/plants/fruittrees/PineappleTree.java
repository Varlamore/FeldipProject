package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class PineappleTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.PINEAPPLE_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 21;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5982, 10); // 10 Watermelons
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public int getLevelRequired() {
        return 51;
    }

    @Override
    public double getPlantingXp() {
        return 57.0;
    }

    @Override
    public double getCheckHealthXp() {
        return 4605.7;
    }

    @Override
    public double getHarvestXp() {
        return 21.5;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.PINEAPPLE;
    }

    @Override
    public int getStartingVarbitValue() {
        return 136; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (142) used as "Empty/Check Health" hack.
        return 142;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 143; // Zenyte "Product Stage 1" (143) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 148 + stage; // Zenyte 149 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 154 + stage; // Zenyte 155 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 161; // Zenyte Stump
    }
}
