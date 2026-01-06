package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class BananaTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.BANANA_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 10;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5386, 4); // 4 Apples
    }

    @Override
    public int getLevelRequired() {
        return 33;
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public double getPlantingXp() {
        return 28.0;
    }

    @Override
    public double getCheckHealthXp() {
        return 1750.0;
    }

    @Override
    public double getHarvestXp() {
        return 10.5;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.BANANA;
    }

    @Override
    public int getStartingVarbitValue() {
        return 35; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (41) used as "Empty/Check Health" hack (like Apple
        // 14).
        return 41;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 42; // Zenyte "Product Stage 1" (42) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 47 + stage; // Zenyte 48 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 53 + stage; // Zenyte 54 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 60; // Zenyte Stump
    }
}
