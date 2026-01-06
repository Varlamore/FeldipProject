package com.cryptic.scene.region;

import com.cryptic.cache.graphics.textures.TextureProvider;
import com.cryptic.util.EnumExtension;

public class DynamicRegion implements EnumExtension {
    public static final DynamicRegion SHA256 = new DynamicRegion(0, 0);
    public static TextureProvider textureProvider;
    public static int field120;
    public final int field119;
    public final int id;

    DynamicRegion(int arg0, int arg1) {
        this.id = arg0;
        this.field119 = arg1;
    }

 /*   public static final void rebuild_region(boolean dynamic, Buffer buffer) {
        Client.instance.requestMapReconstruct = dynamic;

        if (Client.dynamic_region) {
            boolean reload = buffer.get_unsignedbyte_neg() == 1;
            int chunk_x = buffer.get_unsignedshort();
            int chunk_y = buffer.get_unsignedshort_le();
            int size = buffer.get_unsignedshort();
            buffer.set_bit_mode();


            for(int var7 = 0; var7 < 4; ++var7) {
                for(int var8 = 0; var8 < 13; ++var8) {
                    for(int var9 = 0; var9 < 13; ++var9) {
                        int var10 = buffer.read_bits(1);
                        if (var10 == 1) {
                            Client.instanceChunkTemplates[var7][var8][var9] = buffer.readBits(26);
                        } else {
                            Client.instanceChunkTemplates[var7][var8][var9] = -1;
                        }
                    }
                }
            }

            buffer.set_byte_mode();
            KeyHandler.xteaKeys = new int[size][4];

            for(int var7 = 0; var7 < size; ++var7) {
                for(int var8 = 0; var8 < 4; ++var8) {
                    KeyHandler.xteaKeys[var7][var8] = buffer.readUnsignedInt();
                }
            }

            Client.instance.objectIndices = new int[size];
            Client.instance.terrainIndices = new int[size];
            Client.instance.mapCoordinates = new int[size];
            Client.instance.terrainData = new byte[size][];
            Client.instance.objectData = new byte[size][];
            size = 0;

            for(int var7 = 0; var7 < 4; ++var7) {
                for(int var8 = 0; var8 < 13; ++var8) {
                    for(int var9 = 0; var9 < 13; ++var9) {
                        int var10 = Client.instance.instanceChunkTemplates[var7][var8][var9];
                        if (var10 != -1) {
                            int var11 = var10 >> 14 & 1023;
                            int var12 = var10 >> 3 & 2047;
                            int var13 = var12 / 8 + (var11 / 8 << 8);

                            int var14;
                            for(var14 = 0; var14 < size; ++var14) {
                                if (Client.instance.mapCoordinates[var14] == var13) {
                                    var13 = -1;
                                    break;
                                }
                            }

                            if (-1 != var13) {
                                Client.instance.mapCoordinates[size] = var13;
                                var14 = var13 >> 8 & 255;
                                int var15 = var13 & 255;
                               int terrainMapId = Client.instance.terrainIndices[size] = Client.instance.resourceProvider.getMapIdForRegions(0, var14, var15);
                                if (terrainMapId != -1)
                                    Client.instance.resourceProvider.provide(3, terrainMapId);
                                int objectMapId = Client.instance.objectIndices[size] = Client.instance.resourceProvider.getMapIdForRegions(1, var14, var15);
                                if (objectMapId != -1)
                                    Client.instance.resourceProvider.provide(3, objectMapId);
                                ++size;
                            }
                        }
                    }
                }
            }

            CollisionMap.constructRegionOnLoad(chunk_x, chunk_y, !reload);
        } else {
            int chunk_x = buffer.get_unsignedshort_add();
            int chunk_y = buffer.get_unsignedshort_le();
            int regions = buffer.get_unsignedshort();
            KeyHandler.xteaKeys = new int[regions][4];

            for(int r = 0; r < regions; ++r) {
                for(int x = 0; x < 4; ++x) {
                    KeyHandler.xteaKeys[r][x] = buffer.readUnsignedInt();
                }
            }

            Client.instance.objectIndices = new int[regions];
            Client.instance.terrainIndices = new int[regions];
            Client.instance.mapCoordinates = new int[regions];
            Client.instance.terrainData = new byte[regions][];
            Client.instance.objectData = new byte[regions][];
            regions = 0;

            for(int var6 = (chunk_x - 6) / 8; var6 <= (6 + chunk_x) / 8; ++var6) {
                for(int var7 = (chunk_y - 6) / 8; var7 <= (6 + chunk_y) / 8; ++var7) {
                    int var8 = var7 + (var6 << 8);
                    Client.instance.mapCoordinates[regions] = var8;
                    int terrainMapId = Client.instance.terrainIndices[regions] = Client.instance.resourceProvider.getMapIdForRegions(0, var6, var7);
                    if (terrainMapId != -1)
                        Client.instance.resourceProvider.provide(3, terrainMapId);
                    int objectMapId = Client.instance.objectIndices[regions] = Client.instance.resourceProvider.getMapIdForRegions(1, var6, var7);
                    if (objectMapId != -1)
                        Client.instance.resourceProvider.provide(3, objectMapId);
                    ++regions;
                }
            }

            CollisionMap.constructRegionOnLoad(chunk_x, chunk_y, true);
        }
    }*/

    @Override
    public int rsOrdinal() {
        return id;
    }
}
