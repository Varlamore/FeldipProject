package com.cryptic.model.content.skill.impl.farming.patches;

import com.cryptic.model.content.skill.impl.farming.core.*;
import com.cryptic.model.entity.player.Player;
import com.cryptic.model.map.position.Tile;
import com.cryptic.utility.ItemIdentifiers;
import com.cryptic.model.entity.player.Skills;

/**
 * A generic farming patch for Allotments, Flowers, and Herbs.
 * Implements standard OSRS 1:1 raking and growth logic.
 */
public class DefaultFarmingPatch extends FarmingPatch {

    public DefaultFarmingPatch(Player player, Tile bottomLeft, Tile topRight, int varbit) {
        super(player, bottomLeft, topRight, varbit, PatchType.ALLOTMENT);
    }

    @Override
    public void process(int globalTick) {
        // Standard crop growth cycles would be handled here
    }

    @Override
    public boolean handleItemInteraction(int itemId) {
        if (!canInteract())
            return false;

        if (itemId == ItemIdentifiers.RAKE) {
            rake();
            return true;
        }

        if (itemId == ItemIdentifiers.SPADE) {
            clear();
            return true;
        }

        return false;
    }

    @Override
    public void click(int option) {
        if (!canInteract())
            return;

        if (lifecycle == PatchLifecycle.WEEDS) {
            if (option == 1 || option == 2)
                rake();
            return;
        }

        if (lifecycle == PatchLifecycle.EMPTY) {
            player.message("This patch is currently empty.");
            return;
        }
    }

    @Override
    public void rake() {
        if (!player.inventory().contains(ItemIdentifiers.RAKE)) {
            player.message("You need a rake to clear this patch.");
            return;
        }

        if (lifecycle != PatchLifecycle.WEEDS)
            return;

        player.lock();
        com.cryptic.utility.chainedwork.Chain.bound(player).repeatingTask(1, task -> {
            if (weeds > 0) {
                player.animate(2273);
                weeds--;
                player.inventory().add(ItemIdentifiers.WEEDS, 1);
                player.getSkills().addXp(Skills.FARMING, 4);
                updateVarbit();
            } else {
                lifecycle = PatchLifecycle.EMPTY;
                player.unlock();
                player.message("You clear the patch completely.");
                task.stop();
                updateVarbit();
            }
        });
    }

    @Override
    public void clear() {
        if (lifecycle == PatchLifecycle.GROWING || lifecycle == PatchLifecycle.DEAD) {
            player.animate(830);
            player.message("You dig up the patch.");
            plant = null;
            lifecycle = PatchLifecycle.EMPTY;
            updateVarbit();
        }
    }

    @Override
    public int getVarbitValue() {
        if (lifecycle == PatchLifecycle.WEEDS) {
            return 3 - weeds; // 0=Full weeds, 3=Empty
        }
        if (lifecycle == PatchLifecycle.EMPTY)
            return 3;

        // Implementation for plant stages would go here
        return plant != null ? plant.getGrowthStage() : 3;
    }
}
