package com.cryptic.model.content.skill.impl.farming.patches;

import com.cryptic.model.content.skill.impl.farming.core.*;
import com.cryptic.model.content.skill.impl.farming.plants.PlantType;
import com.cryptic.model.entity.player.Player;
import com.cryptic.model.map.position.Tile;
import com.cryptic.utility.Utils;
import com.cryptic.utility.ItemIdentifiers;
import com.cryptic.model.entity.player.Skills;

public class FruitTreePatch extends FarmingPatch {

    public FruitTreePatch(Player player, Tile bottomLeft, Tile topRight, int varbit) {
        super(player, bottomLeft, topRight, varbit, PatchType.FRUIT_TREE);
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

        // Planting Logic
        FarmingPlant plantDef = FarmingPlant.forSeed(itemId);
        if (plantDef != null) {
            attemptPlant(plantDef);
            return true;
        }

        return false;
    }

    private void attemptPlant(FarmingPlant plantDef) {
        if (lifecycle != PatchLifecycle.EMPTY) {
            player.message("You need to clear the patch before planting.");
            return;
        }

        if (!player.inventory().contains(ItemIdentifiers.SPADE)) {
            player.message("You need a spade to plant the sapling.");
            return;
        }

        if (player.getSkills().level(Skills.FARMING) < plantDef.getPlantType().getLevelRequired()) {
            player.message(
                    "You need a Farming level of " + plantDef.getPlantType().getLevelRequired() + " to plant this.");
            return;
        }

        player.animate(2272);
        // Correct face coordinate for center of 4x4 patch: (bottomLeft * 2) + size
        int faceX = bottomLeft.x * 2 + 4;
        int faceY = bottomLeft.y * 2 + 4;
        player.setPositionToFace(new Tile(faceX, faceY));
        player.inventory().remove(plantDef.getSeedId(), 1);
        player.message("You plant the sapling in the fruit tree patch.");

        // Initialize the plant
        this.plant = new Plant(plantDef.getPlantType());
        // Force growth stage 0 and healthy state
        this.plant.setGrowthStage(0);
        this.plant.setDiseased(false);
        this.plant.setDead(false);

        this.lifecycle = PatchLifecycle.GROWING;
        this.nextGrowthTick = com.cryptic.model.content.skill.impl.farming.GlobalFarmingTask.getGlobalFarmingTick()
                + plant.getType().getCyclesPerStage();

        // Final update to send the NEW varbit (8)
        updateVarbit();
    }

    @Override
    public void process(int currentGlobalTick) {
        if (plant == null || lifecycle == PatchLifecycle.WEEDS || lifecycle == PatchLifecycle.EMPTY
                || lifecycle == PatchLifecycle.DEAD || lifecycle == PatchLifecycle.STUMP) {
            return;
        }
        if (lifecycle == PatchLifecycle.GROWING) {
            plant.setCyclesAccumulated(plant.getCyclesAccumulated() + 1);
            if (plant.getCyclesAccumulated() >= plant.getType().getCyclesPerStage()) {
                plant.setCyclesAccumulated(0);

                if (plant.isDiseased()) {
                    // Previously diseased -> Died
                    plant.setDiseased(false);
                    plant.setDead(true);
                    lifecycle = PatchLifecycle.DEAD;
                    player.message("A fruit tree you planted has died.");
                } else {
                    // Normalize growth logic
                    boolean getsDiseased = !plant.isProtectedByGardener() && rollDisease();
                    if (getsDiseased) {
                        plant.setDiseased(true);
                    } else {
                        plant.setGrowthStage(plant.getGrowthStage() + 1);
                        if (plant.isMature()) {
                            lifecycle = PatchLifecycle.FULLY_GROWN;
                            plant.setFruitCount(6);
                            // Initialize regen timer to now + regen time so it doesn't regen immediately
                            nextGrowthTick = currentGlobalTick + plant.getType().getRegenTicks();
                            System.out.println("Tree matured. Fruit set to 6. Next regen at: " + nextGrowthTick);
                            player.message("A fruit tree you planted has finished growing!");
                        }
                    }
                }
                updateVarbit();
            }
        } else if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            // Fruit regeneration
            if (plant.getFruitCount() < 6 && plant.isHealthChecked()) {
                if (nextGrowthTick <= 0) {
                    nextGrowthTick = currentGlobalTick + plant.getType().getRegenTicks();
                }

                // Fix for "Quick Respawn": If timer is way behind, catch up without granting
                // fruit
                if (nextGrowthTick < currentGlobalTick - plant.getType().getRegenTicks()) {
                    System.out
                            .println("Timer lag detected. Catching up " + nextGrowthTick + " -> " + currentGlobalTick);
                    nextGrowthTick = currentGlobalTick;
                }

                if (currentGlobalTick >= nextGrowthTick) {
                    plant.setFruitCount(plant.getFruitCount() + 1);
                    nextGrowthTick = currentGlobalTick + plant.getType().getRegenTicks();
                    updateVarbit();
                    System.out.println("Regenerated 1 fruit. Count: " + plant.getFruitCount());
                }
            }
        } else if (lifecycle == PatchLifecycle.DISEASED) {

        }
    }

    private boolean rollDisease() {
        int chance = 64; // Base OSRS
        if (plant != null) {
            switch (plant.getCompost()) {
                case COMPOST -> chance *= 2;
                case SUPERCOMPOST -> chance *= 5;
                case ULTRACOMPOST -> chance *= 10;
            }
        }
        return Utils.random(chance) == 0;
    }

    @Override
    public void click(int option) {
        if (!canInteract())
            return;

        // 1. Weeds / Empty Check
        if (lifecycle == PatchLifecycle.WEEDS) {
            if (option == 1 || option == 2)
                rake();
            return;
        }

        if (lifecycle == PatchLifecycle.EMPTY) {
            player.message("This fruit tree patch is empty.");
            return;
        }

        // 2. Dead Checks
        if (lifecycle == PatchLifecycle.DEAD || (plant != null && plant.isDead())) {
            player.message("The tree is dead. You need to clear it with a spade.");
            return;
        }

        // 3. Growing State
        if (lifecycle == PatchLifecycle.GROWING) {
            player.message("The tree is still growing.");
            return;
        }

        // 4. Fully Grown / Harvest / Health Check
        if (lifecycle == PatchLifecycle.FULLY_GROWN || (plant != null && plant.isMature())) {
            if (!plant.isHealthChecked()) {
                // Tree is unchecked - client shows "Check-health" in slot 0 (option 1)
                if (option == 1) {
                    player.getSkills().addXp(Skills.FARMING, plant.getType().getCheckHealthXp());
                    plant.setHealthChecked(true);
                    plant.setFruitCount(6); // Initialize 6 fruit (OSRS standard)
                    player.message("You examine the tree and find it in perfect health.");
                    updateVarbit();
                    return;
                }
                player.message("The tree needs to be checked for health.");
                return;
            }

            // Harvest Logic

            if (option == 1) { // Harvest / Pick-fruit
                if (plant.getFruitCount() > 0) {
                    // Pick Fruit Logic
                    if (player.inventory().isFull()) {
                        player.message("You don't have enough inventory space to pick the fruit.");
                        return;
                    }
                    player.animate(2282);
                    com.cryptic.utility.chainedwork.Chain.bound(player).repeatingTask(3, t -> {
                        if (plant == null || plant.getFruitCount() <= 0) {
                            t.stop();
                            return;
                        }
                        if (player.inventory().isFull()) {
                            player.message("Your inventory is full.");
                            player.animate(-1);
                            t.stop();
                            return;
                        }
                        player.animate(2282);
                        player.inventory().add(plant.getType().getHarvestItemId(), 1);
                        player.message("You pick a fruit from the tree.");
                        plant.setFruitCount(plant.getFruitCount() - 1);
                        player.getSkills().addXp(Skills.FARMING, plant.getType().getHarvestXp());
                        updateVarbit();

                        if (plant.getFruitCount() <= 0) {
                            player.message("The tree is now empty.");
                            player.animate(-1);
                            t.stop();
                        }
                    });
                } else {
                    player.message("The tree has no fruit right now.");
                }
            }
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
        int faceX = bottomLeft.x * 2 + 4;
        int faceY = bottomLeft.y * 2 + 4;
        player.setPositionToFace(new Tile(faceX, faceY));
        com.cryptic.utility.chainedwork.Chain.bound(player).repeatingTask(3, task -> {
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
        if (lifecycle == PatchLifecycle.STUMP || lifecycle == PatchLifecycle.DEAD
                || lifecycle == PatchLifecycle.GROWING) {
            int faceX = bottomLeft.x * 2 + 4;
            int faceY = bottomLeft.y * 2 + 4;
            player.setPositionToFace(new Tile(faceX, faceY));
            player.animate(830);
            player.message("You dig up the " + (lifecycle == PatchLifecycle.STUMP ? "stump" : "plant") + ".");
            plant = null;
            lifecycle = PatchLifecycle.EMPTY;
            updateVarbit();
        } else if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            player.message("You should check the health or pick the fruit first.");
        } else if (lifecycle == PatchLifecycle.EMPTY) {
            player.message("This patch is already empty.");
        } else if (lifecycle == PatchLifecycle.WEEDS) {
            player.message("You need to rake the weeds first.");
        }
    }

    @Override
    public int getVarbitValue() {
        if (lifecycle == PatchLifecycle.WEEDS) {
            return 3 - weeds; // 0=Full weeds, 3=Empty
        }
        if (lifecycle == PatchLifecycle.EMPTY)
            return 3;

        if (plant == null) { // Should not happen if lifecycle is not WEEDS or EMPTY
            // System.out.println("getVarbit: Returning default EMPTY due to null plant.");
            // // Debug print removed
            return 3; // Default to empty if plant is null unexpectedly
        }

        // Priority 1: Check Health (Fully Grown)
        if (lifecycle == PatchLifecycle.FULLY_GROWN && !plant.isHealthChecked()) {
            return plant.getType().getCheckHealthVarbitValue();
        }

        // Priority 2: Stump (Not Used for Fruit Trees)
        if (lifecycle == PatchLifecycle.STUMP) {
            return plant.getType().getStumpVarbitValue();
        }

        // Priority 3: Dead
        if (lifecycle == PatchLifecycle.DEAD) {
            return plant.getType().getDeadVarbitValue(plant.getGrowthStage());
        }

        // Priority 4: Diseased
        if (lifecycle == PatchLifecycle.DISEASED) {
            return plant.getType().getDiseasedVarbitValue(plant.getGrowthStage());
        }

        // Priority 5: Harvest/Fruit Check (Only if healthy and mature)
        if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            if (plant.getFruitCount() > 0) {
                // Ensure we don't go out of bounds (1-6 fruits)
                int count = Math.min(6, Math.max(1, plant.getFruitCount()));

                // For Apple: Base 15. 1 fruit = 15.
                return plant.getType().getFruitStartingVarbitValue() + (count - 1);
            } else {
                // 0 Fruit (Harvested state).
                // Use Base + 6 (ID 14 for Apple).
                return plant.getType().getStartingVarbitValue() + 6;
            }
        }

        // Priority 6: Growing
        if (lifecycle == PatchLifecycle.GROWING) {
            return plant.getType().getStartingVarbitValue() + plant.getGrowthStage();
        }

        return 3;
    }

    // Persistence helpers
    public FarmingSave.PatchSave snapshot() {
        FarmingSave.PatchSave save = new FarmingSave.PatchSave();
        save.patchType = this.type;
        save.lifecycle = this.lifecycle;
        save.weeds = this.weeds;
        save.nextGrowthTick = this.nextGrowthTick;
        if (this.plant != null) {
            save.plantSave = this.plant.snapshot();
        }
        return save;
    }

    public void restore(FarmingSave.PatchSave save) {
        this.lifecycle = save.lifecycle;
        this.weeds = save.weeds;
        this.nextGrowthTick = save.nextGrowthTick;
        if (save.plantSave != null) {
            FarmingPlant plantDef = FarmingPlant.forSeed(save.plantSave.seedId);
            if (plantDef != null) {
                this.plant = new Plant(plantDef.getPlantType());
                this.plant.restore(save.plantSave);
            }
        } else {
            this.plant = null;
        }

        // OSRS: Empty patches should have weeds (stage 3)
        if (lifecycle == PatchLifecycle.EMPTY && weeds == 0 && plant == null) {
            // Check if we should actually be in WEEDS state
            // If it was raked, weeds=0 and lifecycle=EMPTY. That's fine.
            // But if it's a fresh patch or loading old save, it might need weeds.
        }

        updateVarbit();
    }

    @Override
    public void forceGrow() {
        if (plant == null || lifecycle == PatchLifecycle.WEEDS || lifecycle == PatchLifecycle.EMPTY
                || lifecycle == PatchLifecycle.STUMP || lifecycle == PatchLifecycle.DEAD) {
            player.message("This patch cannot grow in its current state.");
            return;
        }

        if (lifecycle == PatchLifecycle.GROWING) {
            // Force accumulated cycles to limit to trigger growth
            plant.setCyclesAccumulated(plant.getType().getCyclesPerStage());
            int tick = com.cryptic.model.content.skill.impl.farming.GlobalFarmingTask.getGlobalFarmingTick();
            process(tick);
            player.message("Force grown! Stage: " + plant.getGrowthStage() + " Diseased: " + plant.isDiseased());
        } else if (lifecycle == PatchLifecycle.FULLY_GROWN && !plant.isHealthChecked()) {
            // Just regen fruit if health checking logic allows, or do nothing.
            // Fruit regen requires global tick check.
            nextGrowthTick = 0;
            process(com.cryptic.model.content.skill.impl.farming.GlobalFarmingTask.getGlobalFarmingTick());
            player.message("Forced fruit regen check.");
        } else if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            nextGrowthTick = 0;
            process(com.cryptic.model.content.skill.impl.farming.GlobalFarmingTask.getGlobalFarmingTick());
            player.message("Forced fruit regen check (Health already checked).");
        }
    }
}
