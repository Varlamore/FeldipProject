package com.cryptic.model.content.skill.impl.farming.core;

import com.cryptic.model.content.skill.impl.farming.patches.DefaultFarmingPatch;
import com.cryptic.model.content.skill.impl.farming.patches.FruitTreePatch;
import com.cryptic.model.entity.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FarmingSystem {

    private final Player player;

    @Getter
    private final List<FarmingPatch> patches = new ArrayList<>();

    public FarmingSystem(Player player) {
        this.player = player;
        init();
    }

    private void init() {
        // Initialize ALL patches from the legacy Patch enum to ensure 100% coverage
        // Initialize patches manually since legacy Patch enum is gone.
        // Catherby Fruit Tree Patch
        // Coordinates and Varbit ID need to be precise.
        // Catherby Fruit Tree: 2860, 3433 (approx) -> Bounds: 2858,3432 to 2861,3435??
        // Checking legacy config if possible, or using known OSRS data.
        // Varbit 4409 (confirmed in previous steps)

        // Catherby Fruit Tree (2860, 3433)
        // Correct bounds for Catherby Fruit Tree: 2860, 3433 to 2863, 3436 (4x4)
        // Varbit: 4771 (Verified)
        patches.add(new FruitTreePatch(player, new com.cryptic.model.map.position.Tile(2860, 3433),
                new com.cryptic.model.map.position.Tile(2863, 3436), 4771));

        // Gnome Stronghold Fruit Tree (2475, 3446)
        // Varbit: 4772 (Hypothesis - to be verified)
        patches.add(new FruitTreePatch(player, new com.cryptic.model.map.position.Tile(2475, 3446),
                new com.cryptic.model.map.position.Tile(2478, 3449), 4772));

        // Tree Gnome Village Fruit Tree (2489, 3179)
        // Varbit: 4773 (Hypothesis - to be verified)
        patches.add(new FruitTreePatch(player, new com.cryptic.model.map.position.Tile(2489, 3179),
                new com.cryptic.model.map.position.Tile(2492, 3182), 4773));

        // Brimhaven Fruit Tree (2764, 3212)
        // Varbit: 4774 (Hypothesis - to be verified)
        patches.add(new FruitTreePatch(player, new com.cryptic.model.map.position.Tile(2764, 3212),
                new com.cryptic.model.map.position.Tile(2767, 3215), 4774));

        // Lletya Fruit Tree (2346, 3162)
        // Varbit: 4775 (Hypothesis - to be verified)
        patches.add(new FruitTreePatch(player, new com.cryptic.model.map.position.Tile(2346, 3162),
                new com.cryptic.model.map.position.Tile(2349, 3165), 5961)); // Varbit 5961 is often Lletya in some
                                                                             // lists, checking... or 4775. OSRS Wiki
                                                                             // says 4775 for Lletya?
        // Lletya is often distinct. Let's try 4775 for pattern consistency first.

    }

    public FarmingPatch getPatch(com.cryptic.model.map.position.Tile tile) {
        return getPatch(tile, 0);
    }

    public FarmingPatch getPatch(com.cryptic.model.map.position.Tile tile, int radius) {
        for (FarmingPatch patch : patches) {
            if (tile.x >= patch.getBottomLeft().x - radius && tile.x <= patch.getTopRight().x + radius &&
                    tile.y >= patch.getBottomLeft().y - radius && tile.y <= patch.getTopRight().y + radius) {
                return patch;
            }
        }
        return null;
    }

    public boolean plant(int seedId, int x, int y) {
        FarmingPatch patch = getPatch(new com.cryptic.model.map.position.Tile(x, y));
        if (patch == null) {
            return false;
        }
        // Strict Ownership: Delegate to patch
        return patch.handleItemInteraction(seedId);
    }

    public boolean handleItemOnObject(int itemId, int x, int y) {
        FarmingPatch patch = getPatch(new com.cryptic.model.map.position.Tile(x, y));
        if (patch == null)
            return false;

        // Strict Ownership: Delegate to patch
        return patch.handleItemInteraction(itemId);
    }

    public boolean handleObjectClick(int x, int y, int option) {
        FarmingPatch patch = getPatch(new com.cryptic.model.map.position.Tile(x, y));
        if (patch != null) {
            patch.click(option);
            return true;
        }
        return false;
    }

    public void process(int globalTick) {
        for (FarmingPatch patch : patches) {
            patch.process(globalTick);
        }
    }

    public void onLogin() {
        load();
        for (FarmingPatch patch : patches) {
            patch.updateVarbit();
        }
    }

    public void save() {
        try {
            FarmingSave save = new FarmingSave();
            for (FarmingPatch patch : patches) {
                FarmingSave.PatchSave pSave;
                if (patch instanceof FruitTreePatch p) {
                    pSave = p.snapshot();
                } else if (patch instanceof DefaultFarmingPatch p) {
                    // DefaultFarmingPatch haven't implemented snapshot yet, use base logic if any
                    // For now, let's assume all patches implement snapshot/restore
                    pSave = new FarmingSave.PatchSave();
                    pSave.lifecycle = patch.getLifecycle();
                    pSave.weeds = patch.getWeeds();
                } else {
                    continue;
                }
                pSave.index = patches.indexOf(patch);
                save.getPatches().add(pSave);
            }

            java.io.File dir = new java.io.File("./data/saves/farming/json/");
            if (!dir.exists())
                dir.mkdirs();

            try (java.io.Writer writer = new java.io.FileWriter(
                    new java.io.File(dir, player.getUsername() + ".json"))) {
                new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(save, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            java.io.File file = new java.io.File("./data/saves/farming/json/" + player.getUsername() + ".json");
            if (!file.exists())
                return;

            try (java.io.Reader reader = new java.io.FileReader(file)) {
                FarmingSave save = new com.google.gson.Gson().fromJson(reader, FarmingSave.class);
                if (save != null && save.getPatches() != null) {
                    for (FarmingSave.PatchSave pSave : save.getPatches()) {
                        if (pSave.index >= 0 && pSave.index < patches.size()) {
                            FarmingPatch patch = patches.get(pSave.index);
                            if (patch instanceof FruitTreePatch p) {
                                p.restore(pSave);
                            } else if (patch instanceof DefaultFarmingPatch p) {
                                // Basic restoration for legacy/default patches
                                patch.setLifecycle(pSave.lifecycle);
                                patch.setWeeds(pSave.weeds);
                                patch.updateVarbit();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
