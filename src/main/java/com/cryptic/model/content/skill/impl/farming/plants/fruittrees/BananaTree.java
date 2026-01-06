package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class BananaTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.BANANA_SAPLING;
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
        return 40;
    }

    @Override
    public int getCheckHealthVarbitValue() {
        return 59;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 60;
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 47 + stage;
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 53 + stage;
    }

    @Override
    public int getStumpVarbitValue() {
        return 46;
    }
}
