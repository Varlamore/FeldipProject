package com.cryptic.model.content.skill.impl.farming.core;

import com.cryptic.model.content.skill.impl.farming.plants.fruittrees.*;
import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.utility.ItemIdentifiers;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all farmable plants in the new system.
 * Maps seed/sapling IDs to their PlantType definition.
 */
@Getter
public enum FarmingPlant {
    APPLE(ItemIdentifiers.APPLE_SAPLING, new AppleTree()),
    BANANA(ItemIdentifiers.BANANA_SAPLING, new BananaTree()),
    ORANGE(ItemIdentifiers.ORANGE_SAPLING, new OrangeTree()),
    CURRY(ItemIdentifiers.CURRY_SAPLING, new CurryTree()),
    PINEAPPLE(ItemIdentifiers.PINEAPPLE_SAPLING, new PineappleTree()),
    PAPAYA(ItemIdentifiers.PAPAYA_SAPLING, new PapayaTree()),
    PALM(ItemIdentifiers.PALM_SAPLING, new PalmTree()),
    DRAGONFRUIT(ItemIdentifiers.DRAGONFRUIT_SAPLING, new DragonfruitTree());

    private final int seedId;
    private final PlantType plantType;

    FarmingPlant(int seedId, PlantType plantType) {
        this.seedId = seedId;
        this.plantType = plantType;
    }

    private static final Map<Integer, FarmingPlant> BY_SEED = new HashMap<>();

    static {
        for (FarmingPlant plant : values()) {
            BY_SEED.put(plant.seedId, plant);
        }
    }

    public static FarmingPlant forSeed(int seedId) {
        return BY_SEED.get(seedId);
    }
}
