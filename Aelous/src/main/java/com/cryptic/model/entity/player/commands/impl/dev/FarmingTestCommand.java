package com.cryptic.model.entity.player.commands.impl.dev;

import com.cryptic.model.content.skill.impl.farming.core.FarmingPatch;
import com.cryptic.model.content.skill.impl.farming.patches.FruitTreePatch;
import com.cryptic.model.entity.player.Player;
import com.cryptic.model.entity.player.commands.Command;

public class FarmingTestCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 2) {
            player.message("Usage: ::farmtest [grow|disease|cure|compost|regen|protect|setup]");
            return;
        }

        String action = parts[1].toLowerCase();

        if (action.equals("setup")) {
            // Teleport to Catherby
            player.teleport(new com.cryptic.model.map.position.Tile(2809, 3463, 0));

            // Give Tools
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(952, 1)); // Spade
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5341, 1)); // Rake
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5343, 1)); // Seed Dibber
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5331, 1)); // Watering Can
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(6036, 10)); // Plant Cure

            // Give Compost
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(6032, 5)); // Compost
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(6034, 5)); // Supercompost
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(21483, 5)); // Ultracompost

            // Give Saplings
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5496, 1)); // Apple
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5497, 1)); // Banana
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5498, 1)); // Orange
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5499, 1)); // Curry
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5500, 1)); // Pineapple
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5501, 1)); // Papaya
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(5502, 1)); // Palm
            player.inventory().addOrDrop(new com.cryptic.model.items.Item(22869, 1)); // Dragonfruit

            player.message("Teleported to Catherby and spawned farming kit.");
            return;
        }

        // Get the patch under the player
        FarmingPatch patch = player.getFarmingSystem().getPatch(player.tile(), 1);
        if (patch == null) {
            player.message("No farming patch found under you.");
            return;
        }

        if (!(patch instanceof FruitTreePatch)) {
            player.message("This command currently only supports Fruit Tree patches.");
            return;
        }

        FruitTreePatch fruitPatch = (FruitTreePatch) patch;

        switch (action) {
            case "grow":
                if (fruitPatch.getPlant() != null) {
                    fruitPatch.getPlant().incrementGrowthStage();

                    // Check if we've reached full growth
                    if (fruitPatch.getPlant().getGrowthStage() >= fruitPatch.getPlant().getType().getGrowthStages()) {
                        try {
                            java.lang.reflect.Field f = com.cryptic.model.content.skill.impl.farming.core.FarmingPatch.class
                                    .getDeclaredField("lifecycle");
                            f.setAccessible(true);
                            f.set(fruitPatch,
                                    com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.FULLY_GROWN);
                            fruitPatch.getPlant().setFruitCount(6);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    fruitPatch.updateVarbit();
                    player.message("Plant advanced to stage: " + fruitPatch.getPlant().getGrowthStage() +
                            " | Lifecycle: " + fruitPatch.getLifecycle());
                } else {
                    player.message("No plant to grow.");
                }
                break;

            case "disease":
                if (fruitPatch.getPlant().isProtectedByGardener()) {
                    player.message("This plant is protected by a gardener and cannot be diseased.");
                    return;
                }

                // Force disease
                try {
                    java.lang.reflect.Field f = com.cryptic.model.content.skill.impl.farming.core.FarmingPatch.class
                            .getDeclaredField("lifecycle");
                    f.setAccessible(true);
                    f.set(fruitPatch, com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.DISEASED);
                    fruitPatch.updateVarbit();
                    player.message("Patch forced to DISEASED state.");
                } catch (Exception e) {
                    player.message("Error forcing disease: " + e.getMessage());
                    e.printStackTrace();
                }
                break;

            case "force_disease":
                // Force disease ignoring protection
                try {
                    java.lang.reflect.Field f = com.cryptic.model.content.skill.impl.farming.core.FarmingPatch.class
                            .getDeclaredField("lifecycle");
                    f.setAccessible(true);
                    f.set(fruitPatch, com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.DISEASED);
                    fruitPatch.updateVarbit();
                    player.message("Patch FORCED to DISEASED state (Protection bypassed).");
                } catch (Exception e) {
                    player.message("Error forcing disease: " + e.getMessage());
                    e.printStackTrace();
                }
                break;

            case "cure":
                fruitPatch.cure();
                break;

            case "treat":
            case "compost":
                if (parts.length < 3) {
                    player.message("Usage: ::farmtest compost [1=Normal, 2=Super, 3=Ultra]");
                    return;
                }
                int type = Integer.parseInt(parts[2]);
                fruitPatch.treat(type);
                break;

            case "protect":
                fruitPatch.protect();
                player.message("Patch is now protected.");
                break;

            case "die":
                if (fruitPatch
                        .getLifecycle() != com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.DISEASED
                        && fruitPatch
                                .getLifecycle() != com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.GROWING) {
                    player.message("Can only kill growing or diseased plants.");
                    return;
                }
                try {
                    java.lang.reflect.Field f = com.cryptic.model.content.skill.impl.farming.core.FarmingPatch.class
                            .getDeclaredField("lifecycle");
                    f.setAccessible(true);
                    f.set(fruitPatch, com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.DEAD);
                    fruitPatch.updateVarbit();
                    player.message("Patch forced to DEAD state.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "debug_disease":
                player.message("Checking Protection Logic:");
                player.message("Is Protected By Gardener: " + fruitPatch.getPlant().isProtectedByGardener());
                boolean isDiseased = fruitPatch
                        .getLifecycle() == com.cryptic.model.content.skill.impl.farming.core.PatchLifecycle.DISEASED;
                player.message("Is Currently Diseased: " + isDiseased);

                if (fruitPatch.getPlant().isProtectedByGardener()) {
                    player.message("Result: Immune to disease.");
                } else {
                    player.message("Result: Susceptible to disease roll.");
                }
                break;

            case "regen":
                fruitPatch.regen();
                break;

            case "reset":
                fruitPatch.clear();
                player.message("Patch cleared.");
                break;

            default:
                player.message("Unknown action: " + action);
                break;
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdministrator(player);
    }
}
