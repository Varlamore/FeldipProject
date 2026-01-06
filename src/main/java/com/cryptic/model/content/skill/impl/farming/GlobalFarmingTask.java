package com.cryptic.model.content.skill.impl.farming;

import com.cryptic.core.task.Task;
import com.cryptic.model.World;
import com.cryptic.model.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles the global farming growth ticks (segments).
 * OSRS uses 5-minute segments for this cycle.
 * 
 * @author Antigravity
 */
public class GlobalFarmingTask extends Task {

    private static final Logger logger = LogManager.getLogger(GlobalFarmingTask.class);

    // OSRS Global Farming Tick is approx 5 minutes (500 game ticks)
    private static final int TICK_INTERVAL = 500;

    private static int globalFarmingTick = 0;

    public GlobalFarmingTask() {
        super("GlobalFarmingTask", TICK_INTERVAL, false);
    }

    @Override
    protected void execute() {
        globalFarmingTick++;
        logger.info("[Farming] Global growth tick #{} occurred.", globalFarmingTick);

        // Process all online players
        for (Player player : World.getWorld().getPlayers()) {
            if (player == null || !player.isRegistered())
                continue;
            try {
                // Each player's farming state "catches up" or processes the global tick
                player.getFarmingSystem().process(globalFarmingTick);

            } catch (Exception e) {
                logger.error("Error processing farming tick for player: " + player.getUsername(), e);
            }
        }
    }

    public static int getGlobalFarmingTick() {
        return globalFarmingTick;
    }

    public static void setGlobalFarmingTick(int tick) {
        globalFarmingTick = tick;
    }
}
