package com.cryptic.cache.def.impl.headicon;

import com.cryptic.cache.def.NpcDefinition;

public class HeadIconLoader {

    int[] archiveOffset = new int[8];
    short[] indexOffset = new short[8];

    public HeadIconLoader(NpcDefinition npc) {
        int index2 = 0;
        if (flag(npc)) {
            index2 = npc.getHeadIconSpriteIndex().length;
            System.arraycopy(npc.getHeadIconArchiveIds(), 0, this.archiveOffset, 0, index2);
            System.arraycopy(npc.getHeadIconSpriteIndex(), 0, this.indexOffset, 0, index2);
        }
        for(int index1 = index2; index1 < 8; ++index1) {
            this.archiveOffset[index1] = -1;
            this.indexOffset[index1] = -1;
        }
    }

    public boolean flag(NpcDefinition definition) {
        return definition.headIconArchiveIds != null && definition.headIconSpriteIndex != null;
    }

    public int[] getArchiveOffset() {
        return this.archiveOffset;
    }

    public short[] getIndexOffset() {
        return this.indexOffset;
    }

    public void method2472(int var1, int var2, short var3) {
        this.archiveOffset[var1] = var2;
        this.indexOffset[var1] = var3;
    }

    public void method2469(int[] var1, short[] var2) {
        this.archiveOffset = var1;
        this.indexOffset = var2;
    }

}
