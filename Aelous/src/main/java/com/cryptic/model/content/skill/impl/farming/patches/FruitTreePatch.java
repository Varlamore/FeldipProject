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

    // Tracks the timestamp (ms) of the last fruit growth/regeneration for real-time
    // 45m timer
    private long lastGrowthTime;

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

        // Compost
        if (itemId == 6032) { // Normal
            treat(1);
            player.inventory().remove(new com.cryptic.model.items.Item(6032, 1));
            player.inventory().add(new com.cryptic.model.items.Item(1925, 1)); // Bucket
            player.animate(2283);
            player.getPacketSender().sendSoundEffect(2435, 1, 0); // Sound
            return true;
        }
        if (itemId == 6034) { // Super
            treat(2);
            player.inventory().remove(new com.cryptic.model.items.Item(6034, 1));
            player.inventory().add(new com.cryptic.model.items.Item(1925, 1));
            player.animate(2283);
            player.getPacketSender().sendSoundEffect(2435, 1, 0); // Sound
            return true;
        }
        if (itemId == 21483) { // Ultra
            treat(3);
            player.inventory().remove(new com.cryptic.model.items.Item(21483, 1));
            player.inventory().add(new com.cryptic.model.items.Item(1925, 1));
            player.animate(2283);
            player.getPacketSender().sendSoundEffect(2435, 1, 0); // Sound
            return true;
        }

        // Cure
        if (itemId == 6036) { // Plant Cure
            cure();
            player.inventory().remove(new com.cryptic.model.items.Item(6036, 1));
            player.inventory().add(new com.cryptic.model.items.Item(229, 1)); // Vial
            player.animate(2288);
            player.getPacketSender().sendSoundEffect(2438, 1, 0); // Sound
            return true;
        }

        // Pruning with Secateurs
        if (itemId == ItemIdentifiers.SECATEURS || itemId == ItemIdentifiers.MAGIC_SECATEURS) {
            prune(itemId == ItemIdentifiers.MAGIC_SECATEURS);
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

    public void protect() {
        if (plant != null) {
            plant.setProtectedByGardener(true);
            player.message("You protect the fruit tree.");
        }
    }

    public void treat(int type) {
        player.putAttrib(com.cryptic.model.entity.attributes.AttributeKey.COMPOST_STATE, type);
        String name = type == 1 ? "compost" : (type == 2 ? "supercompost" : "ultracompost");
        player.message("You treat the fruit tree with " + name + ".");
    }

    public void cure() {
        if (plant == null)
            return;

        if (lifecycle == PatchLifecycle.DISEASED) {
            player.message("You cure the fruit tree.");
            player.animate(2288);
            player.getPacketSender().sendSoundEffect(2438, 1, 0);

            plant.setDiseased(false);
            lifecycle = PatchLifecycle.GROWING;
            updateVarbit();
        } else {
            player.message("This plant doesn't need curing.");
        }
    }

    private void prune(boolean magicSecateurs) {
        if (plant == null) {
            player.message("There is nothing to prune here.");
            return;
        }

        if (lifecycle == PatchLifecycle.DEAD) {
            player.message("This tree is dead. You need to clear it with a spade.");
            return;
        }

        if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            player.message("Fully grown fruit trees cannot become diseased.");
            return;
        }

        if (lifecycle != PatchLifecycle.DISEASED) {
            player.message("This tree doesn't need pruning.");
            return;
        }

        // OSRS: 75% success rate with regular secateurs
        // Magic secateurs could have 100% success rate or other bonuses
        double successRate = magicSecateurs ? 1.0 : 0.75;

        player.animate(2275); // Pruning animation
        player.lock();
        player.getPacketSender().sendSoundEffect(2440, 1, 0); // Pruning sound

        com.cryptic.utility.chainedwork.Chain.bound(player).runFn(2, () -> {
            if (Math.random() < successRate) {
                player.message("You successfully prune the diseased leaves.");
                plant.setDiseased(false);
                lifecycle = PatchLifecycle.GROWING;
                updateVarbit();
            } else {
                player.message("There are still diseased leaves left on the tree.");
            }
            player.unlock();
        });
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
        player.getPacketSender().sendSoundEffect(1470, 0, 0); // Tree Planting Sound
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
        // Weed Growth Logic
        if (lifecycle == PatchLifecycle.EMPTY || lifecycle == PatchLifecycle.WEEDS) {
            if (weeds < 3) { // 3 = Full Weeds
                if (nextGrowthTick <= 0) {
                    // 5 minutes per weed stage (Approx 500 ticks? OSRS is 5 mins)
                    // Using 100 ticks for testing speed, or 500 for realism.
                    // 1 tick = 0.6s. 5 mins = 300s = 500 ticks.
                    nextGrowthTick = currentGlobalTick + 500;
                }
                if (currentGlobalTick >= nextGrowthTick) {
                    weeds++;
                    lifecycle = PatchLifecycle.WEEDS;
                    nextGrowthTick = currentGlobalTick + 500;
                    updateVarbit();
                    // System.out.println("Weeds grew! Count: " + weeds);
                }
            }
            return;
        }

        if (lifecycle == PatchLifecycle.DEAD || lifecycle == PatchLifecycle.FULLY_GROWN
                || lifecycle == PatchLifecycle.STUMP) {
            return;
        }

        if (plant == null)
            return;

        // Growth/Disease Cycle
        long cycleTime = plant.getType().getCyclesPerStage() * 60 * 1000L; // CyclesPerStage is now minutes, convert to
                                                                           // milliseconds

        if (System.currentTimeMillis() - lastGrowthTime > cycleTime) {

            // 1. Disease Check
            // Condition: Not already diseased, not protected, and current stage has a valid
            // diseased form (!= -1)
            int currentStage = plant.getGrowthStage();
            int diseasedVarbit = plant.getType().getDiseasedVarbitValue(currentStage);

            if (lifecycle != PatchLifecycle.DISEASED && !plant.isProtectedByGardener() && diseasedVarbit != -1) {
                int diseaseChance = plant.getType().getDiseaseChance();
                int compostState = player.getAttribOr(com.cryptic.model.entity.attributes.AttributeKey.COMPOST_STATE,
                        0);

                // Compost Reduction
                if (compostState == 1)
                    diseaseChance = (int) (diseaseChance * 0.5); // Normal
                else if (compostState == 2)
                    diseaseChance = (int) (diseaseChance * 0.2); // Super
                else if (compostState == 3)
                    diseaseChance = (int) (diseaseChance * 0.1); // Ultra

                // Roll
                if (com.cryptic.utility.Utils.random(127) <= diseaseChance) {
                    lifecycle = PatchLifecycle.DISEASED;
                    updateVarbit();
                    // Do NOT update lastGrowthTime, so it can die next cycle if not cured?
                    // Or does it wait another full cycle to die?
                    // OSRS: "If a diseased plant is not cured by the next growth cycle, it dies."
                    // So we reset time to wait for next cycle.
                    lastGrowthTime = System.currentTimeMillis();
                    return;
                }
            }

            // 2. Death Check
            if (lifecycle == PatchLifecycle.DISEASED) {
                lifecycle = PatchLifecycle.DEAD;
                updateVarbit();
                return;
            }

            // 3. Growth
            plant.incrementGrowthStage();
            if (plant.getGrowthStage() >= plant.getType().getGrowthStages()) {
                lifecycle = PatchLifecycle.FULLY_GROWN;
                // Init fruit count to 6 on first growth
                plant.setFruitCount(6);
            }
            lastGrowthTime = System.currentTimeMillis();
            updateVarbit();
        }

        // Fruit Regeneration
        // OSRS: 45 minutes per fruit.
        // We use lastGrowthTime for regen tracking if fully grown
        if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            if (plant.getFruitCount() < 6 && plant.isHealthChecked()) {
                long regenTime = 45 * 60 * 1000L; // 45 Minutes

                // If we switched from Growing -> FullyGrown, lastGrowthTime was set.
                // So we can reuse lastGrowthTime as the "last regen time".

                if (System.currentTimeMillis() - lastGrowthTime > regenTime) {
                    plant.setFruitCount(plant.getFruitCount() + 1);
                    lastGrowthTime = System.currentTimeMillis();
                    updateVarbit();
                    // player.message("A fruit has regenerated."); // Debug
                }
            }
        }
    }

    public void regen() {
        if (plant != null && lifecycle == PatchLifecycle.FULLY_GROWN && plant.getFruitCount() < 6) {
            plant.setFruitCount(plant.getFruitCount() + 1);
            lastGrowthTime = System.currentTimeMillis();
            updateVarbit();
            player.message("You force regenerate a fruit.");
        }
    }

    @Override
    public void click(int option) {
        if (!canInteract())
            return;

        // 1. Weeds / Empty Check
        if (lifecycle == PatchLifecycle.WEEDS) {
            if (option == 1)
                rake();
            else if (option == 2)
                player.message("This patch needs raking.");
            return;
        }

        if (lifecycle == PatchLifecycle.EMPTY) {
            if (option == 1)
                player.message("This fruit tree patch is empty.");
            else if (option == 2)
                player.message("You can planting a sapling here.");
            return;
        }

        // 2. Dead Checks
        if (lifecycle == PatchLifecycle.DEAD || (plant != null && plant.isDead())) {
            if (option == 1) {
                player.message("The tree is dead. You need to clear it with a spade.");
            }
            return;
        }

        // 3. Growing State
        if (lifecycle == PatchLifecycle.GROWING) {
            if (option == 1) { // Guide/Inspect
                player.message("The tree is currently growing.");
            }
            return;
        }

        // 4. Fully Grown Interactions
        if (lifecycle == PatchLifecycle.FULLY_GROWN || (plant != null && plant.isMature())) {
            // Case A: Unchecked Health
            if (!plant.isHealthChecked()) {
                if (option == 1) { // "Check-health"
                    player.message("You examine the tree...");
                    player.animate(2282); // Examination animation (optional, reusing pick for now)

                    // Delay for effect
                    com.cryptic.utility.chainedwork.Chain.bound(player).runFn(2, () -> {
                        player.getSkills().addXp(Skills.FARMING, plant.getType().getCheckHealthXp());
                        plant.setHealthChecked(true);
                        plant.setFruitCount(6); // Max fruit
                        player.message("You find it in perfect health.");
                        updateVarbit();
                    });
                    return;
                }
                return;
            }

            // Case B: Harvest (Fruit Available)
            if (plant.getFruitCount() > 0) {
                if (option == 1) { // "Pick-fruit"
                    if (player.inventory().isFull()) {
                        player.message("You don't have enough inventory space to pick the fruit.");
                        return;
                    }

                    player.message("You begin to pick the fruit.");
                    player.animate(2282);
                    player.lock();

                    // Auto-pick / Repeating Task
                    com.cryptic.utility.chainedwork.Chain.bound(player).repeatingTask(3, t -> {
                        if (plant == null || plant.getFruitCount() <= 0) {
                            player.unlock();
                            player.animate(-1);
                            t.stop();
                            return;
                        }

                        // Check inventory BEFORE action
                        if (player.inventory().isFull()) {
                            player.message("Your inventory is full.");
                            player.unlock();
                            player.animate(-1);
                            t.stop();
                            return;
                        }

                        player.getPacketSender().sendSoundEffect(2437, 0, 0); // Pick Fruit Sound
                        player.inventory().add(plant.getType().getHarvestItemId(), 1);
                        player.message("You pick a fruit from the tree.");
                        plant.setFruitCount(plant.getFruitCount() - 1);
                        player.getSkills().addXp(Skills.FARMING, plant.getType().getHarvestXp());
                        updateVarbit();

                        if (plant.getFruitCount() > 0) {
                            if (!player.inventory().isFull()) {
                                player.animate(2282); // Continue animating
                            }
                        } else {
                            player.message("The tree is now empty.");
                            player.unlock();
                            player.animate(-1);
                            t.stop();
                        }
                    });
                }
                return;
            }

            // Case C: Empty (0 Fruit)
            if (plant.getFruitCount() <= 0) {
                if (option == 1) { // "Pick Fruit" (Empty)
                    player.getDialogueManager().start(new com.cryptic.model.inter.dialogue.Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(com.cryptic.model.inter.dialogue.DialogueType.OPTION,
                                    "The fruit is currently regenerating.", "Cut it down", "Don't cut it down");
                        }

                        @Override
                        protected void select(int option) {
                            if (option == 1) {
                                chop();
                                stop();
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    });
                } else if (option == 2) {
                    player.message("The tree is currently empty. It will regrow fruit later.");
                } else if (option == 3) {
                    chop();
                }
            } else {
                if (option == 3) { // "Chop down"
                    chop();
                }
            }
        }
    }

    private void chop() {
        if (!player.inventory().contains(ItemIdentifiers.DRAGON_AXE) &&
                !player.inventory().contains(ItemIdentifiers.RUNE_AXE) &&
                !player.inventory().contains(ItemIdentifiers.ADAMANT_AXE) &&
                !player.inventory().contains(ItemIdentifiers.MITHRIL_AXE) &&
                !player.inventory().contains(ItemIdentifiers.BLACK_AXE) &&
                !player.inventory().contains(ItemIdentifiers.STEEL_AXE) &&
                !player.inventory().contains(ItemIdentifiers.IRON_AXE) &&
                !player.inventory().contains(ItemIdentifiers.BRONZE_AXE) &&
                !player.getEquipment().contains(ItemIdentifiers.DRAGON_AXE)) {
            player.message("You need an axe to chop down this tree.");
            return;
        }

        player.animate(879); // Chop animation
        player.getPacketSender().sendSoundEffect(2735, 0, 0); // Woodcutting Sound (Generic Tree)
        player.message("You chop down the tree.");

        // Single swing for now as per user request (or short delay)
        com.cryptic.utility.chainedwork.Chain.bound(player).runFn(2, () -> {
            lifecycle = PatchLifecycle.STUMP;
            updateVarbit();
            player.animate(-1); // Stop animating
        });
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

        // Start animation immediately
        player.animate(2273);
        player.getPacketSender().sendSoundEffect(2442, 0, 0); // Raking Sound

        com.cryptic.utility.chainedwork.Chain.bound(player).repeatingTask(3, task -> {
            if (weeds > 0) {
                weeds--;
                player.inventory().add(ItemIdentifiers.WEEDS, 1);
                player.getSkills().addXp(Skills.FARMING, 4);
                updateVarbit();

                if (weeds > 0) {
                    player.animate(2273); // Continue animating only if work remains
                    player.getPacketSender().sendSoundEffect(2442, 0, 0); // Loop sound
                } else {
                    lifecycle = PatchLifecycle.EMPTY;
                    player.unlock();
                    player.message("You clear the patch completely.");
                    task.stop();
                    updateVarbit();
                }
            } else {
                // Fallback catch
                task.stop();
                player.unlock();
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

        if (plant == null) {
            return 3;
        }

        // Priority 1: Check Health (Fully Grown, Unchecked)
        if (lifecycle == PatchLifecycle.FULLY_GROWN && !plant.isHealthChecked()) {
            return plant.getType().getCheckHealthVarbitValue(); // 14 for Apple
        }

        // Priority 2: Stump
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

        // Priority 5: Harvest/Fruit Check (Only if healthy and mature AND checked)
        if (lifecycle == PatchLifecycle.FULLY_GROWN && plant.isHealthChecked()) {
            if (plant.getFruitCount() > 0) {
                // 1 Fruit = 15, 6 Fruit = 20
                int count = Math.min(6, Math.max(1, plant.getFruitCount()));
                return plant.getType().getFruitStartingVarbitValue() + (count - 1);
            } else {
                // 0 Fruit (Harvested state).
                // Returning 14 causes "Check Health" option.
                // We use Varp 1100 to handle the menu, so we CAN return 14 for correct visuals
                // now!
                int val = plant.getType().getCheckHealthVarbitValue();
                // System.out.println("DEBUG: FruitTreePatch zero fruit. returning " + val + "
                // for "
                // + plant.getType().getClass().getSimpleName());
                return val;
            }
        }

        // Priority 6: Growing
        if (lifecycle == PatchLifecycle.GROWING) {
            return plant.getType().getStartingVarbitValue() + plant.getGrowthStage();
        }

        return 3;
    }

    private void updateStateVarp() {
        if (player == null)
            return;

        int state = 0;
        if (lifecycle == PatchLifecycle.FULLY_GROWN) {
            if (!plant.isHealthChecked()) {
                state = 2; // Unchecked -> "Check-health"
            } else if (plant.getFruitCount() == 0) {
                state = 1; // Empty -> "Chop down"
            }
        }

        // Update Varp 1100
        var key = com.cryptic.model.entity.attributes.AttributeKey.FRUIT_TREE_STATE;
        int currentVal = player.getAttribOr(key, 0);
        int shift = (this.varbit % 16) * 2;
        int mask = ~(3 << shift); // Clear bits
        int newState = (state & 3) << shift;

        int finalVal = (currentVal & mask) | newState;
        if (currentVal != finalVal) {
            player.putAttrib(key, finalVal);
            player.getPacketSender().sendConfig(FarmingConstants.FRUIT_TREE_STATE_VARP, finalVal);
        }
    }

    @Override
    public void updateVarbit() {
        super.updateVarbit();
        updateStateVarp();
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
