package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;

import com.cryptic.utility.ItemIdentifiers;

public class AppleTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.APPLE_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 8;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5986, 9); // 9 Sweetcorn
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
        if (stage == 0)
            return -1;
        return 20 + stage; // Zenyte 21 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 26 + stage; // Zenyte 27 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 33; // Confirmed Zenyte/Client
    }
}
