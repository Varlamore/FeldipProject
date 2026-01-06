package com.cryptic.model.content.skill.impl.farming.core;

import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the state of a specific crop currently growing in a patch.
 * Decouples the crop's lifecycle from the physical patch.
 */
@Getter
@Setter
public class Plant {

    private final PlantType type;
    private int growthStage;
    private boolean diseased;
    private boolean dead;
    private boolean watered;
    private FarmingPatch.CompostState compost = FarmingPatch.CompostState.NONE;
    private boolean protectedByGardener;
    private int fruitCount;
    private boolean healthChecked;

    /**
     * How many 5-minute cycles have passed in the current growth stage.
     */
    private int cyclesAccumulated;

    public Plant(PlantType type) {
        this.type = type;
        this.growthStage = 0;
        this.diseased = false;
        this.dead = false;
        this.watered = false;
        this.fruitCount = 0;
        this.healthChecked = false;
        this.cyclesAccumulated = 0;
    }

    public boolean isMature() {
        return type != null && growthStage >= type.getGrowthStages();
    }

    public String getName() {
        return type == null ? "Unknown" : type.getClass().getSimpleName();
    }

    public FarmingSave.PlantSave snapshot() {
        FarmingSave.PlantSave save = new FarmingSave.PlantSave();
        save.seedId = type != null ? type.getSeedId() : -1;
        save.growthStage = growthStage;
        save.diseased = diseased;
        save.dead = dead;
        save.watered = watered;
        save.compost = compost;
        save.protectedByGardener = protectedByGardener;
        save.fruitCount = fruitCount;
        save.healthChecked = healthChecked;
        save.cyclesAccumulated = cyclesAccumulated;
        return save;
    }

    public void restore(FarmingSave.PlantSave save) {
        this.growthStage = save.growthStage;
        this.diseased = save.diseased;
        this.dead = save.dead;
        this.watered = save.watered;
        this.compost = save.compost;
        this.protectedByGardener = save.protectedByGardener;
        this.fruitCount = save.fruitCount;
        this.healthChecked = save.healthChecked;
        this.cyclesAccumulated = save.cyclesAccumulated;
    }
}
