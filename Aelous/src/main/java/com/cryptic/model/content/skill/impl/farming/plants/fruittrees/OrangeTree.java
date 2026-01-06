package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class OrangeTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.ORANGE_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 13;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(5406, 3); // 3 Strawberries
    }

    @Override
    public int getLevelRequired() {
        return 39;
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public double getPlantingXp() {
        return 35.5;
    }

    @Override
    public double getCheckHealthXp() {
        return 2470.2;
    }

    @Override
    public double getHarvestXp() {
        return 13.5;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.ORANGE;
    }

    @Override
    public int getStartingVarbitValue() {
        return 72; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (78) used as "Empty/Check Health" hack.
        return 78;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 79; // Zenyte "Product Stage 1" (79) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 84 + stage; // Zenyte 85 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 90 + stage; // Zenyte 91 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 97; // Zenyte Stump
    }
}
