package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class OrangeTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.ORANGE_SAPLING;
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
        return 72;
    }

    @Override
    public int getCheckHealthVarbitValue() {
        return 91;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 92;
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 79 + stage;
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 85 + stage;
    }

    @Override
    public int getStumpVarbitValue() {
        return 78;
    }
}
