package com.cryptic.utility;

import com.cryptic.cache.definitions.NpcDefinition;
import com.cryptic.cache.definitions.identifiers.NpcIdentifiers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Static Data Extraction and Classification Engine.
 * Extracts NPC and Animation relationship data directly from the cache.
 */
public class StaticDataExtractor {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void dump(int startId, int endId, String fileName) {
        List<JsonObject> results = new ArrayList<>();

        for (int i = startId; i <= endId; i++) {
            try {
                NpcDefinition def = NpcDefinition.get(i);
                if (def == null || (def.name == null && def.models == null))
                    continue;

                JsonObject npc = new JsonObject();
                npc.addProperty("npc_id", i);
                npc.addProperty("name", def.name == null ? "UNKNOWN" : def.name);
                npc.addProperty("size", def.size);

                if (def.models != null) {
                    npc.add("model_ids", GSON.toJsonTree(def.models));
                }

                // Basic Animations
                npc.addProperty("stand_anim", def.standingAnimation);
                npc.addProperty("walk_anim", def.walkingAnimation);
                npc.addProperty("run_anim", def.runAnimation);
                npc.addProperty("turn_left_anim", def.turnLeftSequence);
                npc.addProperty("turn_right_anim", def.turnRightSequence);
                npc.addProperty("rotate180_anim", def.rotate180Animation);
                npc.addProperty("rotate90_left_anim", def.rotate90LeftAnimation);
                npc.addProperty("rotate90_right_anim", def.rotate90RightAnimation);

                // Specialized Animations from Params
                if (def.params != null) {
                    Map<Integer, Object> params = def.params;
                    npc.addProperty("attack_anim", extractParam(params, 582));
                    npc.addProperty("block_anim", extractParam(params, 583));
                    npc.addProperty("death_anim", extractParam(params, 584));
                } else {
                    npc.addProperty("attack_anim", -1);
                    npc.addProperty("block_anim", -1);
                    npc.addProperty("death_anim", -1);
                }

                results.add(npc);
            } catch (Exception e) {
                // Skip errors
            }
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            GSON.toJson(results, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int extractParam(Map<Integer, Object> params, int key) {
        Object val = params.get(key);
        if (val instanceof Integer)
            return (Integer) val;
        if (val instanceof String) {
            try {
                return Integer.parseInt((String) val);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    public static void runExtraction() {
        System.out.println("Acknowledging: data/filestore/main_file_cache.dat2");
        System.out.println("Acknowledging: data/filestore/main_file_cache.idx2 (NPCs)");
        System.out.println("Acknowledging: data/filestore/main_file_cache.idx12 (Sequences)");

        System.out.println("Starting Extractions...");
        dump(0, 16000, "data/npc_dump.json");
        System.out.println("Extraction Complete: data/npc_dump.json");
    }
}
