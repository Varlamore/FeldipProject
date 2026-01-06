package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class PalmTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.PALM_SAPLING;
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
        return 200;
    }

    @Override
    public int getCheckHealthVarbitValue() {
        return 219;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 220;
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 207 + stage;
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 213 + stage;
    }

    @Override
    public int getStumpVarbitValue() {
        return 206;
    }
}
