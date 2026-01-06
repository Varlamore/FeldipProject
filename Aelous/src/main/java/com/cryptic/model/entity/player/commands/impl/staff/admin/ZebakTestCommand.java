package com.cryptic.model.entity.player.commands.impl.staff.admin;

import com.cryptic.GameServer;
import com.cryptic.cache.definitions.AnimationDefinition;
import com.cryptic.cache.definitions.AnimationSkeletonSet;
import com.cryptic.cache.definitions.identifiers.NpcIdentifiers;
import com.cryptic.model.World;
import com.cryptic.model.entity.npc.NPC;
import com.cryptic.model.entity.player.Player;
import com.cryptic.model.entity.player.commands.Command;
import com.cryptic.model.map.position.Tile;
import com.cryptic.core.task.Task;
import com.cryptic.core.task.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class ZebakTestCommand implements Command {

    private static NPC lastZebak = null;
    private static Task cycleTask = null;

    static {
        System.out.println("[ZEBAK] ZebakTestCommand class loaded - v2.0 with argument parsing fix");
    }

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.message("DEBUG CMD: '" + command + "' Parts info: " + (parts == null ? "null" : parts.length));
        
        // Manual split if parts is too small or command contains the full string
        if (parts.length == 1 && command.contains(" ")) {
            parts = command.split(" ");
        }

        if (command.startsWith("zebak_lair")) {
            // Region 15698 (61, 82) -> Base (3904, 5248)
            // Local (32, 12) -> (3936, 5260)
            player.teleport(new Tile(3936, 5260, 0));
            player.message("Teleported to Zebak's Lair (Region 15698).");
        } else if (command.startsWith("zebak_info")) {
            com.cryptic.cache.definitions.NpcDefinition def = com.cryptic.cache.definitions.NpcDefinition
                    .get(NpcIdentifiers.ZEBAK);
            if (def != null) {
                player.message("Def found. Name: " + def.name + " ID: " + def.id);
                player.message("Stand: " + def.standingAnimation + " Walk: " + def.walkingAnimation);

                // Debug Stand Anim
                int animId = def.standingAnimation;
                if (animId != -1) {
                    try {
                        byte[] data = GameServer.store().getIndex(2).getContainer(12).getFileData(animId, true, true);
                        if (data == null) {
                            player.message(
                                    "CACHE FAIL: Stand Anim " + animId + " has NO DATA in cache Index 2 Container 12.");
                        } else {
                            player.message("Cache OK: Stand Anim " + animId + " data len: " + data.length);
                            AnimationDefinition animDef = new AnimationDefinition(animId, data);
                            if (animDef.skeletonSets == null) {
                                player.message("SKELETON FAIL: Anim " + animId + " has NULL skeletonSets.");
                            } else {
                                player.message("SkeletonSets: " + java.util.Arrays.toString(animDef.skeletonSets));
                                if (animDef.skeletonSets.length > 0) {
                                    int set = animDef.skeletonSets[0] >> 16;
                                    player.message("Set ID: " + set);
                                    try {
                                        AnimationSkeletonSet skelSet = AnimationSkeletonSet.get(GameServer.store(),
                                                set);
                                        if (skelSet.loadedSkins == null || skelSet.loadedSkins.isEmpty()) {
                                            player.message("SKIN FAIL: Set " + set + " has no skins.");
                                        } else {
                                            player.message(
                                                    "Skin OK: Set " + set + " Skins: " + skelSet.loadedSkins.keySet());
                                        }
                                    } catch (Exception e) {
                                        player.message("Set Load Error: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        player.message("Debug Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                player.message("Could not find definition for Zebak.");
            }
        } else if (command.startsWith("zebak_scan")) {
            player.message("Starting Deep Scan for Zebak animations (may freeze server)...");

            new Thread(() -> {
                try {
                    com.cryptic.cache.definitions.NpcDefinition def = com.cryptic.cache.definitions.NpcDefinition
                            .get(NpcIdentifiers.ZEBAK);
                    if (def == null) {
                        player.message("Zebak Definition not found.");
                        return;
                    }

                    // Collect all potential reference animations
                    List<Integer> potentials = new ArrayList<>();
                    if (def.standingAnimation > 0)
                        potentials.add(def.standingAnimation);
                    if (def.walkingAnimation > 0)
                        potentials.add(def.walkingAnimation);
                    if (def.runAnimation > 0)
                        potentials.add(def.runAnimation);
                    if (def.rotate180Animation > 0)
                        potentials.add(def.rotate180Animation);
                    if (def.rotate90LeftAnimation > 0)
                        potentials.add(def.rotate90LeftAnimation);
                    if (def.rotate90RightAnimation > 0)
                        potentials.add(def.rotate90RightAnimation);

                    if (potentials.isEmpty()) {
                        player.message("No reference animations found in NPC definition.");
                        return;
                    }

                    AnimationDefinition validDef = null;
                    int finalSet = -1;
                    int finalSkin = -1;

                    // Try to find a valid skeleton in any of the potential animations
                    for (int refId : potentials) {
                        try {
                            byte[] data = GameServer.store().getIndex(2).getContainer(12).getFileData(refId, true,
                                    true);
                            if (data == null)
                                continue;

                            AnimationDefinition animDef = new AnimationDefinition(refId, data);
                            if (animDef.skeletonSets != null && animDef.skeletonSets.length > 0) {
                                int set = animDef.skeletonSets[0] >> 16;
                                AnimationSkeletonSet skeletonSet = AnimationSkeletonSet.get(GameServer.store(), set);
                                if (skeletonSet.loadedSkins != null && !skeletonSet.loadedSkins.isEmpty()) {
                                    validDef = animDef;
                                    finalSet = set;
                                    finalSkin = skeletonSet.loadedSkins.keySet().iterator().next();
                                    player.message("Found valid reference: Anim " + refId + " -> Set " + finalSet
                                            + " -> Skin " + finalSkin);
                                    break; // Found one!
                                }
                            }
                        } catch (Exception e) {
                            // Continue searching
                        }
                    }

                    if (validDef == null) {
                        player.message("Could not find a valid skeleton in any of Zebak's animations: " + potentials);
                        return;
                    }

                    List<Integer> found = new ArrayList<>();
                    for (int i = 0; i < 30000; i++) {
                        byte[] data = GameServer.store().getIndex(2).getContainer(12).getFileData(i, true, true);
                        if (data == null)
                            continue;

                        AnimationDefinition a = new AnimationDefinition(i, data);
                        if (a.skeletonSets == null || a.skeletonSets.length == 0)
                            continue;

                        int skel = a.skeletonSets[0] >> 16;
                        try {
                            AnimationSkeletonSet sett = AnimationSkeletonSet.get(GameServer.store(), skel);
                            if (sett.loadedSkins.containsKey(finalSkin)) {
                                found.add(i);
                            }
                        } catch (Exception e) {
                            // Ignore invalid skeletons
                        }
                    }

                    player.message("Scan Complete. Found " + found.size() + " animations.");
                    System.out.println("Zebak Animations: " + found);

                    // Send chunks to player to avoid overflow
                    StringBuilder sb = new StringBuilder();
                    for (Integer id : found) {
                        if (sb.length() > 50) {
                            player.message("Anims: " + sb.toString());
                            sb.setLength(0);
                        }
                        sb.append(id).append(", ");
                    }
                    if (sb.length() > 0) {
                        player.message("Anims: " + sb.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    player.message("Scan failed: " + e.getMessage());
                }
            }).start();
        } else if (command.startsWith("spawn_zebak")) {
            NPC npc = NPC.of(NpcIdentifiers.ZEBAK, player.tile().copy());
            World.getWorld().registerNpc(npc);
            lastZebak = npc;
            player.message("Spawned Zebak. ID: " + NpcIdentifiers.ZEBAK);
        } else if (command.startsWith("zebak_anim")) {
            player.message("DEBUG: zebak_anim command received");
            player.message("DEBUG: lastZebak = " + (lastZebak == null ? "NULL" : "exists"));

            if (lastZebak == null || lastZebak.dead() || !lastZebak.isRegistered()) {
                player.message("No active Zebak found. Spawn one first with ::spawn_zebak");
                return;
            }

            player.message("DEBUG: Zebak is valid, checking parts length: " + parts.length);

            if (parts.length < 2) {
                player.message("Usage: ::zebak_anim <id>");
                player.message("Example: ::zebak_anim 9620");
                return;
            }

            try {
                int animId = Integer.parseInt(parts[1]);
                player.message("DEBUG: Attempting to play animation " + animId + " on Zebak");
                lastZebak.animate(animId);
                player.message("Animation " + animId + " sent to Zebak!");
            } catch (NumberFormatException e) {
                player.message("ERROR: Invalid animation ID. Must be a number.");
            } catch (Exception e) {
                player.message("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (command.startsWith("zebak_cycle")) {
            if (lastZebak == null || lastZebak.dead() || !lastZebak.isRegistered()) {
                player.message("No active Zebak found. Spawn one first.");
                return;
            }
            if (cycleTask != null && cycleTask.isRunning()) {
                cycleTask.stop();
            }
            int startAnim = 0;
            if (parts.length > 1) {
                startAnim = Integer.parseInt(parts[1]);
            }

            final int initialAnim = startAnim;

            cycleTask = new Task("ZebakCycle", 1) { // Changed to 1 tick (0.6s) for faster cycling
                int animId = initialAnim;

                @Override
                public void execute() {
                    if (lastZebak == null || lastZebak.dead() || !lastZebak.isRegistered()) {
                        stop();
                        return;
                    }
                    player.message("Zebak Cycle: " + animId);
                    lastZebak.animate(animId);
                    animId++;
                }
            };
            TaskManager.submit(cycleTask);
            player.message("Started animation cycle from " + startAnim);
        } else if (command.equals("extract_data")) {
            com.cryptic.utility.StaticDataExtractor.runExtraction();
            player.message("Static Data Extraction Complete. Saved to data/npc_dump.json");
        } else if (command.startsWith("zebak_stop")) {
            if (cycleTask != null) {
                cycleTask.stop();
                player.message("Stopped animation cycle.");
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
