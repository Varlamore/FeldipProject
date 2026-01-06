package com.cryptic.model.entity.player.commands.impl.dev;

import com.cryptic.model.entity.player.Player;
import com.cryptic.model.entity.player.commands.Command;
import com.cryptic.model.map.object.GameObject;
import com.cryptic.model.map.object.MapObjects;
import com.cryptic.model.map.position.Tile;
import java.util.Optional;

public class DebugObjectCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        Tile tile = player.tile();
        // Check 3x3 area
        for (int x = tile.getX() - 1; x <= tile.getX() + 1; x++) {
            for (int y = tile.getY() - 1; y <= tile.getY() + 1; y++) {
                // Get all objects at this tile
                for (GameObject obj : MapObjects.getAll(new Tile(x, y, tile.getZ()))) {
                    player.message(
                            "Object at " + x + "," + y + ": " + obj.getId() + " (" + obj.definition().name + ")");
                    // If it has a varbit, print it if possible (requires definition access)
                    if (obj.definition().varbit != -1) {
                        player.message("  -> Varbit: " + obj.definition().varbit + ", Varp: " + obj.definition().varp);
                    }
                }
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdministrator(player);
    }
}
