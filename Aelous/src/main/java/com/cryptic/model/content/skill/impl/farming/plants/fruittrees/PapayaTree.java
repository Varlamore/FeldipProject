package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class PapayaTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.PAPAYA_SAPLING;
    }

    @Override
    public int getRegenTicks() {
        return 16;
    }

    @Override
    public int getDiseaseChance() {
        return 27;
    }

    @Override
    public com.cryptic.model.items.Item getPayment() {
        return new com.cryptic.model.items.Item(2114, 10); // 10 Pineapples
    }

    @Override
    public int getGrowthStages() {
        return 6;
    }

    @Override
    public int getLevelRequired() {
        return 57;
    }

    @Override
    public double getPlantingXp() {
        return 72.0;
    }

    @Override
    public double getCheckHealthXp() {
        return 6146.4;
    }

    @Override
    public double getHarvestXp() {
        return 27.0;
    }

    @Override
    public int getHarvestItemId() {
        return ItemIdentifiers.PAPAYA_FRUIT;
    }

    @Override
    public int getStartingVarbitValue() {
        return 163; // Zenyte Growth Start
    }

    @Override
    public int getCheckHealthVarbitValue() {
        // Zenyte "Product Stage 0" (169) used as "Empty/Check Health" hack.
        return 169;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 170; // Zenyte "Product Stage 1" (170) = 1 Fruit.
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 175 + stage; // Zenyte 176 at stage 1
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        if (stage == 0)
            return -1;
        return 181 + stage; // Zenyte 182 at stage 1
    }

    @Override
    public int getStumpVarbitValue() {
        return 188; // Zenyte Stump
    }
}
