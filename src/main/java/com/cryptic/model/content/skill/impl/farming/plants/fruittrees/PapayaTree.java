package com.cryptic.model.content.skill.impl.farming.plants.fruittrees;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;

public class PapayaTree implements PlantType {

    @Override
    public int getSeedId() {
        return ItemIdentifiers.PAPAYA_SAPLING;
    }

    @Override
    public int getLevelRequired() {
        return 57;
    }

    @Override
    public int getGrowthStages() {
        return 6;
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
        return 168;
    }

    @Override
    public int getCheckHealthVarbitValue() {
        return 187;
    }

    @Override
    public int getFruitStartingVarbitValue() {
        return 188;
    }

    @Override
    public int getDiseasedVarbitValue(int stage) {
        return 175 + stage;
    }

    @Override
    public int getDeadVarbitValue(int stage) {
        return 181 + stage;
    }

    @Override
    public int getStumpVarbitValue() {
        return 174;
    }
}
