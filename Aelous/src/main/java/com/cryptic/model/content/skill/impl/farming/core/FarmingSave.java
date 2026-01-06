package com.cryptic.model.content.skill.impl.farming.core;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * Handles persistence for the farming system.
 */
public class FarmingSave {

    @Getter
    @Setter
    public java.util.List<PatchSave> patches = new java.util.ArrayList<>();

    @Getter
    @Setter
    public static class PatchSave implements Serializable {
        public int index; // Legacy support or list positioning
        public PatchType patchType;
        public PatchLifecycle lifecycle;
        public int weeds;
        public long nextGrowthTick;
        public PlantSave plantSave;
    }

    @Getter
    @Setter
    public static class PlantSave implements Serializable {
        public int seedId;
        public int growthStage;
        public boolean diseased;
        public boolean dead;
        public boolean watered;
        public FarmingPatch.CompostState compost;
        public boolean protectedByGardener;
        public int fruitCount;
        public boolean healthChecked;
        public int cyclesAccumulated;
    }
}
