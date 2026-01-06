package com.cryptic.cache.config;

import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.table.EvictingDualNodeHashTable;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;
import net.runelite.rs.api.RSVarpDefinition;

/**
 * Varps are used for inteface configuration ids and their functions, out of the current 725 config ids, only 9 or so of them are used.
 *
 */
public final class VariablePlayer extends DualNode implements RSVarpDefinition {

    static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);

    public int type = 0;

    public static VariablePlayer lookup(int id) {
        VariablePlayer varp = (VariablePlayer)VariablePlayer.cached.get((long)id);
        if (varp == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.VARPLAYER, id);
            varp = new VariablePlayer();
            if (data != null) {
                varp.decode(new Buffer(data));
            }

            cached.put(varp, id);
        }
        return varp;
    }

    void decode(Buffer buffer) {
        while(true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }

            this.decodeNext(buffer, opcode);
        }
    }


    void decodeNext(Buffer buffer, int opcode) {
        if (opcode == 5) {
            this.type = buffer.readUnsignedShort();
        }

    }

    @Override
    public int getType() {
        return type;
    }

    public static void clear() {
        cached.clear();
    }


}
