package com.cryptic.model.content.skill.impl.farming.plants;

import com.cryptic.model.content.skill.impl.farming.core.FarmingConstants;

public interface PlantType {

    int getSeedId();

    int getLevelRequired();

    /**
     * Total number of growth stages.
     * E.g. Apple Tree = 6 stages (Seedling ... Mature)
     */
    int getGrowthStages();

    /**
     * How many cycles (5 mins) per stage?
     * E.g. Apple Tree = 160 mins / 5 = 32 cycles.
     */
    default int getCyclesPerStage() {
        return FarmingConstants.FRUIT_TREE_CYCLES;
    }

    double getPlantingXp();

    double getCheckHealthXp();

    double getHarvestXp();

    int getHarvestItemId();

    /**
     * The starting varbit value for the first growth stage.
     */
    int getStartingVarbitValue();

    /**
     * Returns the varbit value for a diseased plant at the given growth stage.
     */
    int getDiseasedVarbitValue(int stage);

    /**
     * Returns the varbit value for a dead plant at the given growth stage.
     */
    int getDeadVarbitValue(int stage);

    /**
     * Returns the varbit value for the "Check Health" state (Mature but not
     * checked).
     */
    int getCheckHealthVarbitValue();

    /**
     * Returns the varbit value for the 6-fruit state.
     */
    int getFruitStartingVarbitValue();

    /**
     * Returns the varbit value for the stump/harvested tree state.
     */
    int getStumpVarbitValue();

    /**
     * Ticks required to regenerate fruit.
     * Default 16 ticks (80 mins) for most trees.
     */
    default int getRegenTicks() {
        return 16;
    }

    int getDiseaseChance();

    com.cryptic.model.items.Item getPayment();
}
