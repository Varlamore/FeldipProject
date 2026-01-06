package com.cryptic.model.content;

import com.cryptic.Client;
import com.cryptic.cache.config.VariableBits;

/**
 * Manages OSRS-style varbits on the client.
 * Provides helper methods for reading patch states and debugging changes.
 */
public class VarbitManager {

    private static final int[] BIT_MASKS = new int[32];

    static {
        for (int i = 0, j = 2; i < 32; i++) {
            BIT_MASKS[i] = j - 1;
            j += j;
        }
    }

    /**
     * Retrieves the value of a varbit.
     * 
     * @param id The varbit ID.
     * @return The current value.
     */
    public static int getVarbit(int id) {
        VariableBits var2 = VariableBits.lookup(id);
        if (var2 == null)
            return 0;

        int baseVar = var2.baseVar;
        int startBit = var2.startBit;
        int endBit = var2.endBit;
        int mask = BIT_MASKS[endBit - startBit];

        return Client.instance.settings[baseVar] >> startBit & mask;
    }

    /**
     * Hook called whenever a Varp (config) is updated.
     * 
     * @param varpId   The Varp ID.
     * @param newValue The new value of the Varp.
     */
    public static void onVarpUpdate(int varpId, int newValue) {
        // Farming varbits are primarily in Varp 529.
        // Check major farming varbits (Falador/Catherby) + Fruit Trees (4770+)
        for (int id = 4405; id <= 4800; id++) {
            VariableBits var2 = VariableBits.lookup(id);
            if (var2 != null && var2.baseVar == varpId) {
                int val = getVarbit(id);
                // System.out.println("[Farming] Varbit " + id + " state: " + val);
                if (id == 4774 || (id >= 4771 && id <= 4775)) {
                    System.out.println("[FarmingClient] Fruit Tree Update - Varbit " + id + " -> " + val);
                }
            }
        }
    }

    public static void dump() {
        System.out.println("--- Farming Varbit Dump ---");
        for (int id = 4405; id <= 4800; id++) {
            VariableBits var2 = VariableBits.lookup(id);
            if (var2 != null) {
                int val = getVarbit(id);
                if (val > 0)
                    System.out.println("Varbit " + id + ": " + val);
            }
        }
        System.out.println("---------------------------");
    }

    /**
     * Debugging helper to print varbit changes.
     */
    public static void onVarbitUpdate(int id, int oldValue, int newValue) {
        if (id >= 4400 && id <= 4500) {
            System.out.println("[Farming] Varbit " + id + " state change: " + oldValue + " -> " + newValue);
        }
    }
}
