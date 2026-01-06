package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class PineappleTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.PINEAPPLE_SAPLING;
    }

    @Override
    public int getLevelRequired() {
        return 51;
    }

    @Override
    public int getGrowthStages() {
        return 6;
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
        return 136;
    }

    @Override
    public int getCheckHealthVarbitValue() {
        return 155;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 156;
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 143 + stage;
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 149 + stage;
    }

    @Override
    public int getStumpVarbitValue() {
        return 142;
    }
}
