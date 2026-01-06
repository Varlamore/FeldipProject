package com.cryptic.cache.def.anim;

import com.cryptic.cache.def.anim.skeleton.SkeletalAnimBase;
import com.cryptic.collection.node.Node;
import com.cryptic.io.Buffer;
import lombok.Getter;
import net.runelite.rs.api.RSSkeleton;

@Getter
public final class Skeleton extends Node implements RSSkeleton {
    private SkeletalAnimBase skeletalBase;
    int id;

    public int count;
    public int[] transformTypes;
    public int[][] labels;

    public Skeleton(int id, byte[] payload) {
        this.id = id;
        Buffer data = new Buffer(payload);

        count = data.readUnsignedByte();
        transformTypes = new int[count];
        labels = new int[count][];

        for(int index = 0; index < count; index++) {
            transformTypes[index] = data.readUnsignedByte();
        }

        for(int index = 0; index < count; index++) {
            labels[index] = new int[data.readUnsignedByte()];
        }

        for(int j = 0; j < count; j++) {
            for (int l = 0; l < labels[j].length; l++) {
                labels[j][l] = data.readUnsignedByte();
            }
        }

        if (data.pos < data.payload.length) {
            int size = data.readUShort();
            if (size > 0) {
                this.skeletalBase = new SkeletalAnimBase(data, size);
            }
        }

    }
    public int transformsCount() {
        return this.count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int[] getTypes() {
        return transformTypes;
    }

    @Override
    public int[][] getList() {
        return labels;
    }
}
