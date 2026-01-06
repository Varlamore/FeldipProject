package com.cryptic.model.entity.player.commands.impl.staff.admin;

import com.cryptic.GameServer;
import com.cryptic.cache.definitions.AnimationDefinition;
import com.cryptic.cache.definitions.AnimationSkeletonSet;
import com.cryptic.cache.definitions.NpcDefinition;
import com.cryptic.model.entity.player.Player;
import com.cryptic.model.entity.player.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class DumpAnimCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 2) {
            player.message("Usage: ::dumpanims <npc_id> [limit]");
            return;
        }

        int npcId;
        try {
            npcId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            player.message("Invalid NPC ID.");
            return;
        }

        final int limit = (parts.length > 2) ? Integer.parseInt(parts[2]) : 35000;

        player.message("Starting Deep Scan for NPC " + npcId + "...");

        new Thread(() -> {
            try {
                NpcDefinition def = NpcDefinition.get(npcId);
                if (def == null) {
                    player.message("NPC Definition " + npcId + " not found.");
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
                    player.message("NPC " + npcId + " has no reference animations.");
                    return;
                }

                AnimationDefinition validDef = null;
                int finalSet = -1;
                int finalSkin = -1;

                // Try to find a valid skeleton in any of the potential animations
                for (int refId : potentials) {
                    try {
                        byte[] data = GameServer.store().getIndex(2).getContainer(12).getFileData(refId, true, true);
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
                                player.message(
                                        "Ref found: Anim " + refId + " -> Set " + finalSet + " -> Skin " + finalSkin);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        // Continue searching
                    }
                }

                if (validDef == null) {
                    player.message("Could not find a valid skeleton in animations: " + potentials);
                    return;
                }

                List<Integer> found = new ArrayList<>();
                long start = System.currentTimeMillis();
                for (int i = 0; i < limit; i++) {
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

                player.message(
                        "Scan Complete (" + (System.currentTimeMillis() - start) + "ms). Found: " + found.size());
                System.out.println("NPC " + npcId + " Animations: " + found);

                StringBuilder sb = new StringBuilder();
                for (Integer id : found) {
                    if (sb.length() > 60) {
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
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
