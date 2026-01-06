package com.cryptic.model.content.skill.impl.farming.core;

/**
 * Constants for the 1:1 OSRS Farming System.
 */
public class FarmingConstants {

    /**
     * A "Farming Tick" or "Cycle" in OSRS occurs every 5 minutes.
     * 5 minutes * 60 seconds * 1000 ms / 600 ms per game tick = 500 game ticks.
     */
    public static final int FARMING_TICK_INTERVAL = 500;

    /**
     * Apple Trees grow every 160 minutes (32 cycles).
     * 160 minutes / 5 minutes per cycle = 32 cycles.
     */
    public static final int FRUIT_TREE_CYCLES = 32;

    /**
     * Interaction distances
     */
    public static final int INTERACTION_DISTANCE = 3;

    /**
     * Varp used to sync fruit tree state metadata to client.
     * Bits: 0-1 (Patch 0), 2-3 (Patch 1), 4-5 (Patch 2), 6-7 (Patch 3).
     * Value 0: Normal, 1: Unchecked, 2: Empty.
     */
    public static final int FRUIT_TREE_STATE_VARP = 1100;
}
