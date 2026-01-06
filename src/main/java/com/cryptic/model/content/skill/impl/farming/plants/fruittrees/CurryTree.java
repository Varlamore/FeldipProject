package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class CurryTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.CURRY_SAPLING;
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
        return 104;
    }

    @Override
    public int getCheckHealthVarbitValue() {
        return 123;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 124;
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 111 + stage;
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 117 + stage;
    }

    @Override
    public int getStumpVarbitValue() {
        return 110;
    }
}
