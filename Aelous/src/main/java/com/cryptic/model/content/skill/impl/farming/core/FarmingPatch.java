package com.cryptic.model.content.skill.impl.farming.core;

import com.cryptic.model.entity.player.Player;
import com.cryptic.model.map.position.Tile;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a single physical farming patch in the world.
 * Refactored to be a robust base class for all OSRS patch types.
 */
@Getter
@Setter
public abstract class FarmingPatch {

    protected final Player player;
    protected final Tile bottomLeft;
    protected final Tile topRight;
    protected final int varbit;
    protected final PatchType type;

    public enum CompostState {
        NONE, COMPOST, SUPERCOMPOST, ULTRACOMPOST
    }

    // State Management
    protected PatchLifecycle lifecycle = PatchLifecycle.WEEDS;
    protected Plant plant;
    protected int weeds = 3; // OSRS standard: patches start with 3 weeds

    /**
     * The last global tick this patch was updated on.
     */
    protected long lastUpdateTick;
    protected long nextGrowthTick = 0;

    public FarmingPatch(Player player, Tile bottomLeft, Tile topRight, int varbit, PatchType type) {
        this.player = player;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.varbit = varbit;
        this.type = type;
        this.lastUpdateTick = com.cryptic.model.content.skill.impl.farming.GlobalFarmingTask.getGlobalFarmingTick();
    }

    /**
     * Process a growth increment.
     * 
     * @param currentGlobalTick The currently active global farming tick.
     */
    public abstract void process(int currentGlobalTick);

    /**
     * Handles clicking on the patch (Pick, Rake, Inspect, etc).
     */
    public abstract void click(int option);

    /**
     * Handles an item used on the patch (Seeds, Tools, Compost).
     * 
     * @return True if the interaction was handled.
     */
    public abstract boolean handleItemInteraction(int itemId);

    /**
     * Core raking logic (Common to almost all patches).
     */
    public abstract void rake();

    /**
     * Core digging/clearing logic.
     */
    public abstract void clear();

    public boolean isCleared() {
        return lifecycle == PatchLifecycle.EMPTY;
    }

    public void updateVarbit() {
        if (varbit != -1) {
            int value = getVarbitValue();
            player.getPacketSender().sendVarbit(varbit, value);
        }
    }

    /**
     * Returns the dynamic varbit value based on OSRS 1:1 state mapping.
     */
    public abstract int getVarbitValue();

    /**
     * Convenience method to check if the patch can be interacted with.
     */
    protected boolean canInteract() {
        return !player.getMovementQueue().isFollowing() && player.getMovementQueue().isAtDestination();
    }

    public void forceGrow() {
        player.message("This patch type does not support forced growth yet.");
    }
}
