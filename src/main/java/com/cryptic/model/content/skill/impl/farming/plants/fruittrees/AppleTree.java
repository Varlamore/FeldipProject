package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;

import com.cryptic.utility.ItemIdentifiers;

public class AppleTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.APPLE_SAPLING;
    }

    @Override
    public int getLevelRequired() {
        return 27;
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public double getPlantingXp() {
        return 22.0;
    }

    @Override
    public double getCheckHealthXp() {
        return 1199.5;
    }

    @Override
    public double getHarvestXp() {
        return 8.5;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.COOKING_APPLE;
    }

    @Override
    public int getStartingVarbitValue() {
        return 8; // Confirmed Zenyte/Client
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // User confirmed: 14 is the reused model for Empty/Check Health.
        // Server state differentiates the action.
        return 14;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 15; // User confirmed: 15 = 1 Fruit ... 20 = 6 Fruits
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 21 + stage; // Confirmed Zenyte/Client: Starts at 21
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 27 + stage; // Confirmed Zenyte/Client: Starts at 27
    }

    @Override
    public int getStumpVarbitValue() {
        return 33; // Confirmed Zenyte/Client
    }
}
