package com.cryptic.cache.def;

import com.cryptic.Client;
import com.cryptic.ClientConstants;
import com.cryptic.cache.config.VariableBits;
import com.cryptic.cache.def.anim.SequenceDefinition;
import com.cryptic.collection.EvictingDualNodeHashTable;
import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.entity.Renderable;
import com.cryptic.entity.model.Mesh;
import com.cryptic.entity.model.Model;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSObjectComposition;

import java.util.Arrays;
import java.util.Objects;

import static com.cryptic.Client.BIT_MASKS;

public final class ObjectDefinition extends DualNode implements RSObjectComposition {

    static Mesh[] meshData;

    static {
        meshData = new Mesh[4];
    }

    void processOp(Buffer buffer, int opcode) {
        int var3;
        int var4;
        if (opcode == 1) {
            var3 = buffer.readUnsignedByte();
            if (var3 > 0) {
                if (this.modelIds != null && !isLowDetail) {
                    buffer.pos += var3 * 3;
                } else {
                    this.models = new int[var3];
                    this.modelIds = new int[var3];

                    for (var4 = 0; var4 < var3; ++var4) {
                        this.modelIds[var4] = buffer.readUShort();
                        this.models[var4] = buffer.readUnsignedByte();
                    }
                }
            }
        } else if (opcode == 2) {
            this.name = buffer.readStringCp1252NullTerminated();
        } else if (opcode == 5) {
            var3 = buffer.readUnsignedByte();
            if (var3 > 0) {
                if (this.modelIds != null && !isLowDetail) {
                    buffer.pos += var3 * 2;
                } else {
                    this.models = null;
                    this.modelIds = new int[var3];

                    for (var4 = 0; var4 < var3; ++var4) {
                        this.modelIds[var4] = buffer.readUShort();
                    }
                }
            }
        } else if (opcode == 14) {
            this.sizeX = buffer.readUnsignedByte();
        } else if (opcode == 15) {
            this.sizeY = buffer.readUnsignedByte();
        } else if (opcode == 17) {
            this.interactType = 0;
            this.boolean1 = false;
        } else if (opcode == 18) {
            this.boolean1 = false;
        } else if (opcode == 19) {
            int1 = buffer.readUnsignedByte();
        } else if (opcode == 21) {
            this.clipType = 0;
        } else if (opcode == 22) {
            this.nonFlatShading = true;
        } else if (opcode == 23) {
            this.modelClipped = true;
        } else if (opcode == 24) {
            this.animationId = buffer.readUShort();
            if (this.animationId == 65535) {
                this.animationId = -1;
            }
        } else if (opcode == 27) {
            this.interactType = 1;
        } else if (opcode == 28) {
            this.int2 = buffer.readUnsignedByte();
        } else if (opcode == 29) {
            this.ambient = buffer.readSignedByte();
        } else if (opcode == 39) {
            this.contrast = buffer.readSignedByte() * 25;
        } else if (opcode >= 30 && opcode < 35) {
            this.actions[opcode - 30] = buffer.readStringCp1252NullTerminated();
            if (this.actions[opcode - 30].equalsIgnoreCase("Hidden")) {
                this.actions[opcode - 30] = null;
            }
        } else if (opcode == 40) {
            var3 = buffer.readUnsignedByte();
            this.recolorFrom = new short[var3];
            this.recolorTo = new short[var3];

            for (var4 = 0; var4 < var3; ++var4) {
                this.recolorFrom[var4] = (short) buffer.readUShort();
                this.recolorTo[var4] = (short) buffer.readUShort();
            }
        } else if (opcode == 41) {
            var3 = buffer.readUnsignedByte();
            this.retextureFrom = new short[var3];
            this.retextureTo = new short[var3];

            for (var4 = 0; var4 < var3; ++var4) {
                this.retextureFrom[var4] = (short) buffer.readUShort();
                this.retextureTo[var4] = (short) buffer.readUShort();
            }
        } else if (opcode == 61) {
            buffer.readUShort();
        } else if (opcode == 62) {
            this.isRotated = true;
        } else if (opcode == 64) {
            this.clipped = false;
        } else if (opcode == 65) {
            this.modelSizeX = buffer.readUShort();
        } else if (opcode == 66) {
            this.modelHeight = buffer.readUShort();
        } else if (opcode == 67) {
            this.modelSizeY = buffer.readUShort();
        } else if (opcode == 68) {
            this.mapSceneId = buffer.readUShort();
        } else if (opcode == 69) {
            orientation = buffer.readUnsignedByte();
        } else if (opcode == 70) {
            this.offsetX = buffer.readShort();
        } else if (opcode == 71) {
            this.offsetHeight = buffer.readShort();
        } else if (opcode == 72) {
            this.offsetY = buffer.readShort();
        } else if (opcode == 73) {
            this.boolean2 = true;
        } else if (opcode == 74) {
            this.isSolid = true;
        } else if (opcode == 75) {
            this.int3 = buffer.readUnsignedByte();
        } else if (opcode != 77 && opcode != 92) {
            if (opcode == 78) {
                this.soundId = buffer.readUShort();
                this.soundRange = buffer.readUnsignedByte();
            } else if (opcode == 79) {
                this.soundMin = buffer.readUShort();
                this.soundMax = buffer.readUShort();
                this.soundRange = buffer.readUnsignedByte();

                var3 = buffer.readUnsignedByte();
                this.soundEffectIds = new int[var3];

                for (var4 = 0; var4 < var3; ++var4) {
                    this.soundEffectIds[var4] = buffer.readUShort();
                }
            } else if (opcode == 81) {
                this.clipType = buffer.readUnsignedByte() * 256;
            } else if (opcode == 82) {
                this.mapIconId = buffer.readUShort();
            } else if (opcode == 89) {
                this.boolean3 = false;
            } else if (opcode == 249) {
                this.params = Buffer.readStringIntParameters(buffer, this.params);
            }
        } else {
            this.transformVarbit = buffer.readUShort();
            if (this.transformVarbit == 65535) {
                this.transformVarbit = -1;
            }

            this.transformVarp = buffer.readUShort();
            if (this.transformVarp == 65535) {
                this.transformVarp = -1;
            }

            var3 = -1;
            if (opcode == 92) {
                var3 = buffer.readUShort();
                if (var3 == 65535) {
                    var3 = -1;
                }
            }

            var4 = buffer.readUnsignedByte();
            this.transforms = new int[var4 + 2];

            for (int var5 = 0; var5 <= var4; ++var5) {
                this.transforms[var5] = buffer.readUShort();
                if (this.transforms[var5] == 65535) {
                    this.transforms[var5] = -1;
                }
            }

            this.transforms[var4 + 1] = var3;
        }

    }

    public void post_decode() {
        if (int1 == -1) {
            int1 = 0;
            if (modelIds != null && (models == null || models[0] == 10)) {
                int1 = 1;
            }

            for (int index = 0; index < 5; index++) {
                if (this.actions[index] != null) {
                    this.int1 = 1;
                    break;
                }
            }
        }

        if (int3 == -1) {
            int3 = interactType != 0 ? 1 : 0;
        }
    }

    public static int getVarbit(int arg0) {
        VariableBits var2 = VariableBits.lookup(arg0);
        int var3 = var2.baseVar;
        int var4 = var2.startBit;
        int var5 = var2.endBit;
        int var6 = BIT_MASKS[var5 - var4];
        return Client.instance.settings[var3] >> var4 & var6;
    }

    public final ObjectDefinition transform() {
        int var2 = -1;
        if (this.transformVarbit != -1) {
            var2 = getVarbit(this.transformVarbit);
        } else if (this.transformVarp != -1) {
            var2 = Client.instance.settings[this.transformVarp];
        }

        int var3;
        if (var2 >= 0 && var2 < this.transforms.length - 1) {
            var3 = this.transforms[var2];
        } else {
            var3 = this.transforms[this.transforms.length - 1];
        }

        return var3 != -1 ? ObjectDefinition.get(var3) : null;
    }

    /**
     * Returns dynamically modified actions based on varbit state.
     * This is a scalable, data-driven approach for context-sensitive menu options.
     * Does NOT modify the cached definition, only returns a modified copy when
     * needed.
     * 
     * @param parentObjectId The parent object ID (before transformation)
     * @return The actions array (either original or dynamically modified)
     */
    public String[] getDynamicActions(int parentObjectId) {
        if (this.actions == null || this.actions.length == 0) {
            return this.actions;
        }

        ObjectDefinition parentDef = ObjectDefinition.get(parentObjectId);
        if (parentDef.transformVarbit == -1) {
            return this.actions;
        }

        // CRITICAL: Only apply to FRUIT TREE patches (varbits 4770-4776)
        int varbitId = parentDef.transformVarbit;
        boolean isFruitTree = (varbitId >= 4770 && varbitId <= 4776);

        if (!isFruitTree) {
            return this.actions;
        }

        // Clone actions to modify safely
        String[] dynamicActions = (this.actions != null) ? this.actions.clone() : new String[5];

        // Blanket Suppression: Remove "Chop down" or "Chop-down" from ANY slot
        // DISABLED: We need "Chop down" for fully grown fruit trees to clear them (Chop
        // -> Stump -> Dig).
        /*
         * if (this.actions != null) {
         * for (int i = 0; i < dynamicActions.length; i++) {
         * if (dynamicActions[i] != null &&
         * (dynamicActions[i].equalsIgnoreCase("Chop down")
         * || dynamicActions[i].equalsIgnoreCase("Chop-down"))) {
         * dynamicActions[i] = null; // Suppress it
         * }
         * }
         * }
         */

        // Get varbit value to check tree state
        int varbitVal = getVarbit(parentDef.transformVarbit);

        // Varbit 14 = UNCHECKED state -> INSERT "Check-health" in slot 0
        // BUT also used for EMPTY state -> Needs "Chop down"
        // Solution: Use Varp 1100 metadata to distinguish.
        // Bits: (VarbitID % 16) * 2.
        // Value 1 = Empty (Chop), 2 = Unchecked (Check-health).
        if (varbitVal == 14 || varbitVal == 41 || varbitVal == 78 || varbitVal == 105 || varbitVal == 142
                || varbitVal == 169 || varbitVal == 206 || varbitVal == 233) {
            int shift = (parentDef.transformVarbit % 16) * 2;
            int state = (com.cryptic.model.content.VarbitManager
                    .getVarp(ClientConstants.FRUIT_TREE_STATE_VARP) >> shift) & 3;

            if (state == 2) { // Unchecked
                dynamicActions[0] = "Check-health";
                dynamicActions[1] = "Inspect";
                dynamicActions[2] = null;
                dynamicActions[3] = "Guide";
                dynamicActions[4] = null;
            } else if (state == 1) { // Empty
                dynamicActions[0] = "Chop down";
                dynamicActions[1] = "Inspect";
                dynamicActions[2] = null;
                dynamicActions[3] = "Guide";
                dynamicActions[4] = null;
            } else {
                // Fallback
                dynamicActions[0] = "Check-health";
                dynamicActions[1] = "Inspect";
                dynamicActions[2] = "Chop down";
                dynamicActions[3] = "Guide";
                dynamicActions[4] = null;
            }
        }

        return dynamicActions;
    }

    void decode(Buffer buffer) {
        while (true) {
            int var2 = buffer.readUnsignedByte();
            if (var2 == 0) {
                return;
            }

            this.decodeNext(buffer, var2);
        }
    }

    public static ObjectDefinition get(int objectID) {
        ObjectDefinition definition = (ObjectDefinition) objectsCached.get((long) objectID);
        if (definition == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.OBJECT, objectID);
            definition = new ObjectDefinition();
            definition.set_defaults();
            definition.id = objectID;
            if (data != null) {
                definition.decode(new Buffer(data));
            }
            definition.post_decode();
            if (Objects.equals(definition.name, "fountain")) {
                System.out.println("x: " + definition.sizeX + " y: " + definition.sizeY);
            }
            if (objectID == 63001) {
                definition.orientation = 2;
            }
            if (objectID == 31625) {
                definition.name = "Well of Goodwill";
            }
            if (objectID == 31923) {
                definition.actions = new String[] { "Moderns", "Ancients", "Lunar", "Arceuus", null };
            }
            if (objectID == 35965) {
                definition.name = "Cryptic Teleport Platform";
                definition.actions = new String[] { "Teleport", null, null, null, null };
            }
            if (objectID == 16264) {
                definition.actions = new String[] { null, null, null, null, null };
            }
            if (objectID == 31924) {
                definition.actions = new String[] { "Enchant", null, null, null, null };
            }
            if (objectID == 27215) {
                definition.actions = new String[] { "Attack", null, null, null, null };
            }
            if (objectID == 630) {
                definition.actions = new String[] { "Steal-From", null, null, null, null };
            }
            if (objectID == 629) {
                definition.actions = new String[] { "Steal-From", null, null, null, null };
            }
            if (objectID == 628) {
                definition.actions = new String[] { "Steal-From", null, null, null, null };
            }
            if (objectID == 4728) {
                definition.actions = new String[] { "Steal-From", null, null, null, null };
            }
            if (objectID == 631) {
                definition.actions = new String[] { "Steal-From", null, null, null, null };
            }

            if (definition.isSolid) {
                definition.interactType = 0;
                definition.boolean1 = false;
            }

            objectsCached.put(definition, objectID);
        }
        return definition;
    }

    @Override
    public String toString() {
        return "ObjectDefinition{" +
                "id=" + id +
                ", sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                ", animationId=" + animationId +
                ", orientation=" + orientation +
                ", modelSizeX=" + modelSizeX +
                ", modelHeight=" + modelHeight +
                ", modelSizeY=" + modelSizeY +
                ", offsetX=" + offsetX +
                ", offsetHeight=" + offsetHeight +
                ", offsetY=" + offsetY +
                ", mapIconId=" + mapIconId +
                ", mapSceneId=" + mapSceneId +
                ", interact_state=" + int1 +
                ", decor_offset=" + int2 +
                ", merge_interact_state=" + int3 +
                ", varp=" + transformVarp +
                ", varbit=" + transformVarbit +
                ", ambientSoundId=" + ambientSoundId +
                ", anInt2083=" + soundRange +
                ", anInt2112=" + soundMin +
                ", anInt2113=" + soundMax +
                ", soundEffectIds=" + Arrays.toString(soundEffectIds) +
                ", contour_to_tile=" + clipType +
                ", ambientSoundID=" + soundId +
                ", randomAnimStart=" + boolean3 +
                ", params=" + params +
                ", modelIds=" + Arrays.toString(modelIds) +
                ", transforms=" + Arrays.toString(transforms) +
                ", models=" + Arrays.toString(models) +
                ", recolorFrom=" + Arrays.toString(recolorFrom) +
                ", recolorTo=" + Arrays.toString(recolorTo) +
                ", retextureFrom=" + Arrays.toString(retextureFrom) +
                ", retextureTo=" + Arrays.toString(retextureTo) +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", actions=" + Arrays.toString(actions) +
                ", contrast=" + contrast +
                ", ambient=" + ambient +
                ", rotated=" + isRotated +
                ", walkable=" + boolean1 +
                ", inverted=" + inverted +
                ", contour_to_tile=" + clipType +
                ", isInteractive=" + isInteractive +
                ", modelClipped=" + modelClipped +
                ", isSolid=" + isSolid +
                ", solid=" + solid +
                ", clipped=" + clipped +
                ", nonFlatShading=" + nonFlatShading +
                ", obstructs_ground=" + boolean2 +
                ", category=" + category +
                ", interactType=" + interactType +
                ", ambientSoundIds=" + Arrays.toString(ambientSoundIds) +
                '}';
    }

    public void set_defaults() {
        modelIds = null;
        models = null;
        name = null;
        description = null;
        recolorFrom = null;
        recolorTo = null;
        retextureTo = null;
        retextureFrom = null;
        sizeX = 1;
        sizeY = 1;
        boolean1 = true;
        int1 = -1;
        clipType = -1;
        nonFlatShading = false;
        modelClipped = false;
        boolean3 = true;
        animationId = -1;
        int2 = 16;
        interactType = 2;
        ambient = 0;
        contrast = 0;
        actions = new String[5];
        mapIconId = -1;
        mapSceneId = -1;
        isRotated = false;
        clipped = true;
        modelSizeX = 128;
        modelHeight = 128;
        modelSizeY = 128;
        orientation = 0;
        offsetX = 0;
        offsetHeight = 0;
        offsetY = 0;
        boolean2 = false;
        isSolid = false;
        int3 = -1;
        transformVarbit = -1;
        transformVarp = -1;
        transforms = null;
    }

    public final Model getModelDynamic(int var1, int var2, int[][] var3, int var4, int var5, int var6,
            SequenceDefinition var7, int var8) {
        long var9;
        if (this.models == null) {
            var9 = (long) (var2 + (this.id << 10));
        } else {
            var9 = (long) (var2 + (var1 << 3) + (this.id << 10));
        }

        Model var11 = (Model) modelsCached.get(var9);
        if (var11 == null) {
            Mesh var12 = this.getModelData(var1, var2);
            if (var12 == null) {
                return null;
            }

            var11 = var12.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
            modelsCached.put(var11, var9);
        }

        if (var7 == null && this.clipType * 65536 == -1) {
            return var11;
        } else {
            if (var7 != null) {
                var11 = var7.transformObjectModel(var11, var8, var2);
            } else {
                var11 = var11.toSharedSequenceModel(true);
            }

            if (this.clipType * 65536 >= 0) {
                var11 = var11.contourGround(var3, var4, var5, var6, false, this.clipType * 65536);
            }

            return var11;
        }
    }

    public Renderable getEntity(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
        long var7;
        if (this.models == null) {
            var7 = (long) (var2 + (this.id << 10));
        } else {
            var7 = (long) (var2 + (var1 << 3) + (this.id << 10));
        }

        Object var9 = (Renderable) cachedModelData.get(var7);
        if (var9 == null) {
            Mesh var10 = this.getModelData(var1, var2);
            if (var10 == null) {
                return null;
            }

            if (!this.nonFlatShading) {
                var9 = var10.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
            } else {
                var10.ambient = (short) (this.ambient + 64);
                var10.contrast = (short) (this.contrast + 768);
                var10.calculateVertexNormals();
                var9 = var10;
            }

            cachedModelData.put((DualNode) var9, var7);
        }

        if (this.nonFlatShading) {
            var9 = ((Mesh) var9).copyModelData();
        }

        if (this.clipType * 65536 >= 0) {
            if (var9 instanceof Model) {
                var9 = ((Model) var9).contourGround(var3, var4, var5, var6, true, this.clipType * 65536);
            } else if (var9 instanceof Mesh) {
                var9 = ((Mesh) var9).method4239(var3, var4, var5, var6, true, this.clipType * 65536);
            }
        }

        return (Renderable) var9;
    }

    public final Model getModel(int var1, int var2, int[][] var3, int var4, int var5, int var6) {
        long var7;
        if (this.models == null) {
            var7 = (long) (var2 + (this.id << 10));
        } else {
            var7 = (long) (var2 + (var1 << 3) + (this.id << 10));
        }

        Model var9 = (Model) cachedModelData.get(var7);
        if (var9 == null) {
            Mesh var10 = this.getModelData(var1, var2);
            if (var10 == null) {
                return null;
            }

            var9 = var10.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
            cachedModelData.put(var9, var7);
        }

        if (this.clipType * 65536 >= 0) {
            var9 = var9.contourGround(var3, var4, var5, var6, true, this.clipType * 65536);
        }

        return var9;
    }

    public boolean modelTypeCached(int var1) {
        if (this.models != null) {
            for (int var4 = 0; var4 < this.models.length; ++var4) {
                if (this.models[var4] == var1) {
                    return Js5List.models.tryLoadFile(this.modelIds[var4] & 65535, 0);
                }
            }

            return true;
        } else if (this.modelIds == null) {
            return true;
        } else if (var1 != 10) {
            return true;
        } else {
            boolean var2 = true;

            for (int var3 = 0; var3 < this.modelIds.length; ++var3) {
                var2 &= Js5List.models.tryLoadFile(this.modelIds[var3] & 65535, 0);
            }

            return var2;
        }
    }

    public final boolean needsModelFiles() {
        if (this.modelIds == null) {
            return true;
        } else {
            boolean var1 = true;

            for (int var2 = 0; var2 < this.modelIds.length; ++var2) {
                var1 &= Js5List.models.tryLoadFile(this.modelIds[var2] & 65535, 0);
            }

            return var1;
        }
    }

    public ObjectDefinition get_configs() {
        int setting_id = -1;
        if (transformVarbit != -1) {
            VariableBits bit = VariableBits.lookup(transformVarbit);
            int setting = bit.baseVar;
            int low = bit.startBit;
            int high = bit.endBit;
            int mask = Client.BIT_MASKS[high - low];
            setting_id = Client.instance.settings[setting] >> low & mask;
        } else if (transformVarp != -1)
            setting_id = Client.instance.settings[transformVarp];

        if (setting_id < 0 || setting_id >= transforms.length || transforms[setting_id] == -1)
            return null;
        else
            return get(transforms[setting_id]);
    }

    Mesh getModelData(int var1, int var2) {
        Mesh var3 = null;
        boolean var4;
        int var5;
        int var7;
        if (this.models == null) {
            if (var1 != 10) {
                return null;
            }

            if (this.modelIds == null) {
                return null;
            }

            var4 = this.isRotated;
            if (var1 == 2 && var2 > 3) {
                var4 = !var4;
            }

            var5 = this.modelIds.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                var7 = this.modelIds[var6];
                if (var4) {
                    var7 += 65536;
                }

                var3 = (Mesh) cachedModelData.get((long) var7);
                if (var3 == null) {
                    var3 = Mesh.getModel(var7 & 65535);
                    if (var3 == null) {
                        return null;
                    }

                    if (var4) {
                        var3.method4306();
                    }

                    cachedModelData.put(var3, (long) var7);
                }

                if (var5 > 1) {
                    meshData[var6] = var3;
                }
            }

            if (var5 > 1) {
                var3 = new Mesh(meshData, var5);
            }
        } else {
            int var9 = -1;

            for (var5 = 0; var5 < this.models.length; ++var5) {
                if (this.models[var5] == var1) {
                    var9 = var5;
                    break;
                }
            }

            if (var9 == -1) {
                return null;
            }

            var5 = this.modelIds[var9];
            boolean var10 = this.isRotated ^ var2 > 3;
            if (var10) {
                var5 += 65536;
            }

            var3 = (Mesh) cachedModelData.get((long) var5);
            if (var3 == null) {
                var3 = Mesh.getModel(var5 & 65535);
                if (var3 == null) {
                    return null;
                }

                if (var10) {
                    var3.method4306();
                }

                cachedModelData.put(var3, (long) var5);
            }
        }

        if (this.modelSizeX == 128 && this.modelHeight == 128 && this.modelSizeY == 128) {
            var4 = false;
        } else {
            var4 = true;
        }

        boolean var11;
        if (this.offsetX == 0 && this.offsetHeight == 0 && this.offsetY == 0) {
            var11 = false;
        } else {
            var11 = true;
        }

        Mesh var8 = new Mesh(var3, var2 == 0 && !var4 && !var11, this.recolorFrom == null, null == this.retextureFrom,
                true);
        if (var1 == 4 && var2 > 3) {
            var8.method4244(256);
            var8.changeOffset(45, 0, -45);
        }

        var2 &= 3;
        if (var2 == 1) {
            var8.method4281();
        } else if (var2 == 2) {
            var8.method4242();
        } else if (var2 == 3) {
            var8.method4243();
        }

        if (this.recolorFrom != null) {
            for (var7 = 0; var7 < this.recolorFrom.length; ++var7) {
                var8.recolor(this.recolorFrom[var7], this.recolorTo[var7]);
            }
        }

        if (this.retextureFrom != null) {
            for (var7 = 0; var7 < this.retextureFrom.length; ++var7) {
                var8.retexture(this.retextureFrom[var7], this.retextureTo[var7]);
            }
        }

        if (var4) {
            var8.resize(this.modelSizeX, this.modelHeight, this.modelSizeY);
        }

        if (var11) {
            var8.changeOffset(this.offsetX, this.offsetHeight, this.offsetY);
        }

        return var8;
    }

    public static void release() {
        cachedModelData = null;
        modelsCached = null;
    }

    public ObjectDefinition() {
        id = -1;
    }

    public static int length;

    public static boolean isLowDetail = ClientConstants.OBJECT_DEFINITION_LOW_MEMORY;
    public static final Model[] modelData = new Model[4];
    public static EvictingDualNodeHashTable objectsCached = new EvictingDualNodeHashTable(4096);
    public static EvictingDualNodeHashTable cachedModelData = new EvictingDualNodeHashTable(500);
    public static EvictingDualNodeHashTable modelsCached = new EvictingDualNodeHashTable(30);

    public int id;
    public int sizeX;
    public int sizeY;
    public int animationId;
    public int orientation;

    public int modelSizeX;
    public int modelHeight;
    public int modelSizeY;
    public int offsetX;
    public int offsetHeight;
    public int offsetY;
    public int mapIconId;
    public int mapSceneId;
    /**
     * opcode 19
     */
    public int int1; // opcode 19
    public int int2;
    public int int3;
    public int transformVarp;
    public int transformVarbit;
    public int ambientSoundId;
    public int soundRange = 0;
    public int soundMin = 0;
    public int soundMax = 0;
    public int[] soundEffectIds;

    public int soundId = -1;
    public boolean boolean3;
    public IterableNodeHashTable params = null;

    public int[] modelIds;
    public int[] transforms;
    public int[] models;

    public short[] recolorFrom;
    public short[] recolorTo;

    public short[] retextureFrom;
    public short[] retextureTo;
    static Mesh[] field2059 = new Mesh[4];

    public String name;
    public String description;
    public String[] actions;

    public int contrast;
    public int ambient;

    public boolean isRotated;
    public boolean boolean1;

    public boolean inverted;
    public int clipType;
    public boolean isInteractive;
    public boolean modelClipped;
    public boolean isSolid;
    /**
     * opcode 27
     */
    public boolean solid;
    public boolean clipped;
    public boolean nonFlatShading;
    public boolean boolean2;
    public int category;

    public int interactType;

    public int[] ambientSoundIds;

    @Override
    public int getAccessBitMask() {
        return 0;
    }

    @Override
    public int getIntValue(int paramID) {
        return 0;
    }

    @Override
    public void setValue(int paramID, int value) {

    }

    @Override
    public String getStringValue(int paramID) {
        return null;
    }

    @Override
    public void setValue(int paramID, String value) {

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getActions() {
        return actions;
    }

    @Override
    public int getMapSceneId() {
        return mapSceneId;
    }

    @Override
    public int getMapIconId() {
        return mapIconId;
    }

    @Override
    public int[] getImpostorIds() {
        return new int[0];
    }

    @Override
    public RSObjectComposition getImpostor() {
        return null;
    }

    @Override
    public RSIterableNodeHashTable getParams() {
        return null;
    }

    @Override
    public void setParams(IterableHashTable params) {

    }

    @Override
    public void setParams(RSIterableNodeHashTable params) {

    }

    @Override
    public void decodeNext(RSBuffer buffer, int opcode) {
        this.processOp((Buffer) buffer, opcode);
    }

    @Override
    public int[] getModelIds() {
        return modelIds;
    }

    @Override
    public void setModelIds(int[] modelIds) {
        this.modelIds = modelIds;
    }

    @Override
    public int[] getModels() {
        return models;
    }

    @Override
    public void setModels(int[] models) {
        this.models = models;
    }

    @Override
    public boolean getObjectDefinitionIsLowDetail() {
        return isLowDetail;
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    @Override
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    @Override
    public int getInteractType() {
        return interactType;
    }

    @Override
    public void setInteractType(int interactType) {
        this.interactType = interactType;
    }

    @Override
    public boolean getBoolean1() {
        return boolean1;
    }

    @Override
    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    @Override
    public int getInt1() {
        return int1;
    }

    @Override
    public void setInt1(int int1) {
        this.int1 = int1;
    }

    @Override
    public int getInt2() {
        return int2;
    }

    @Override
    public void setInt2(int int2) {
        this.int2 = int2;
    }

    @Override
    public int getClipType() {
        return clipType;
    }

    @Override
    public void setClipType(int clipType) {
        this.clipType = clipType;
    }

    @Override
    public boolean getNonFlatShading() {
        return nonFlatShading;
    }

    @Override
    public void setNonFlatShading(boolean nonFlatShading) {
        this.nonFlatShading = nonFlatShading;
    }

    @Override
    public void setModelClipped(boolean modelClipped) {
        this.modelClipped = modelClipped;
    }

    @Override
    public boolean getModelClipped() {
        return modelClipped;
    }

    @Override
    public int getAnimationId() {
        return animationId;
    }

    @Override
    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    @Override
    public int getAmbient() {
        return ambient;
    }

    @Override
    public void setAmbient(int ambient) {
        this.ambient = ambient;
    }

    @Override
    public int getContrast() {
        return contrast;
    }

    @Override
    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    @Override
    public short[] getRecolorFrom() {
        return recolorFrom;
    }

    @Override
    public void setRecolorFrom(short[] recolorFrom) {
        this.recolorFrom = recolorFrom;
    }

    @Override
    public short[] getRecolorTo() {
        return recolorTo;
    }

    @Override
    public void setRecolorTo(short[] recolorTo) {
        this.recolorTo = recolorTo;
    }

    @Override
    public short[] getRetextureFrom() {
        return retextureFrom;
    }

    @Override
    public void setRetextureFrom(short[] retextureFrom) {
        this.retextureFrom = retextureFrom;
    }

    @Override
    public short[] getRetextureTo() {
        return retextureTo;
    }

    @Override
    public void setRetextureTo(short[] retextureTo) {
        this.retextureTo = retextureTo;
    }

    @Override
    public void setIsRotated(boolean rotated) {
        this.isRotated = rotated;
    }

    @Override
    public boolean getIsRotated() {
        return isRotated;
    }

    @Override
    public void setClipped(boolean clipped) {
        this.clipped = clipped;
    }

    @Override
    public boolean getClipped() {
        return clipped;
    }

    @Override
    public void setMapSceneId(int mapSceneId) {
        this.mapSceneId = mapSceneId;
    }

    @Override
    public void setModelSizeX(int modelSizeX) {
        this.modelSizeX = modelSizeX;
    }

    @Override
    public int getModelSizeX() {
        return modelSizeX;
    }

    @Override
    public void setModelHeight(int modelHeight) {
        this.modelHeight = modelHeight;
    }

    @Override
    public void setModelSizeY(int modelSizeY) {
        this.modelSizeY = modelSizeY;
    }

    @Override
    public void setOffsetX(int modelSizeY) {
        this.offsetX = modelSizeY;
    }

    @Override
    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    @Override
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    @Override
    public void setInt3(int int3) {
        this.int3 = int3;
    }

    @Override
    public void setInt5(int int5) {
        this.soundMin = int5;
    }

    @Override
    public void setInt6(int int6) {
        this.soundMax = int6;
    }

    @Override
    public void setInt7(int int7) {
        this.soundRange = int7;
    }

    @Override
    public void setBoolean2(boolean boolean2) {
        this.boolean2 = boolean2;
    }

    @Override
    public void setIsSolid(boolean isSolid) {
        this.isSolid = isSolid;
    }

    @Override
    public void setAmbientSoundId(int ambientSoundId) {
        this.soundId = ambientSoundId;
    }

    @Override
    public void setSoundEffectIds(int[] soundEffectIds) {
        this.soundEffectIds = soundEffectIds;
    }

    @Override
    public int[] getSoundEffectIds() {
        return soundEffectIds;
    }

    @Override
    public void setMapIconId(int mapIconId) {
        this.mapIconId = mapIconId;
    }

    @Override
    public void setBoolean3(boolean boolean3) {
        this.boolean3 = boolean3;
    }

    @Override
    public void setTransformVarbit(int transformVarbit) {
        this.transformVarbit = transformVarbit;
    }

    @Override
    public int getTransformVarbit() {
        return transformVarbit;
    }

    @Override
    public void setTransformVarp(int transformVarp) {
        this.transformVarp = transformVarp;
    }

    @Override
    public int getTransformVarp() {
        return transformVarp;
    }

    @Override
    public void setTransforms(int[] transforms) {
        this.transforms = transforms;
    }

    @Override
    public int[] getTransforms() {
        return transforms;
    }

    public int sizeX() {
        return sizeX;
    }

    public int sizeY() {
        return sizeY;
    }

    public int soundId() {
        return soundId;
    }

    public int soundRange() {
        return soundRange;
    }

    public int soundMin() {
        return soundMin;
    }

    public int soundMax() {
        return soundMax;
    }

    public int[] soundIds() {
        return soundEffectIds;
    }

    public boolean hasSound() {
        if (this.transforms == null) {
            return this.soundId != -1 || this.soundEffectIds != null;
        } else {
            for (int transform : this.transforms) {
                if (transform != -1) {
                    ObjectDefinition var2 = get(transform);
                    if (var2.soundId != -1 || var2.soundEffectIds != null) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
