package com.cryptic.cache.def;

import com.cryptic.Client;
import com.cryptic.cache.config.VariableBits;
import com.cryptic.cache.def.anim.SequenceDefinition;
import com.cryptic.collection.EvictingDualNodeHashTable;
import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.entity.model.Mesh;
import com.cryptic.entity.model.Model;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;
import lombok.Data;
import lombok.ToString;
import net.runelite.api.HeadIcon;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSNPCComposition;


@ToString
@Data
public class NpcDefinition extends DualNode implements RSNPCComposition {
    public static void clear() {
        modelCache = null;
    }

    public NpcDefinition definition;

    public static Client clientInstance;
    public static EvictingDualNodeHashTable modelCache = new EvictingDualNodeHashTable(30);

    public int id;
    public String name = "null";
    public int size = 1;
    public int[] modelId;
    public int[] chatheadModels;
    public int standingAnimation = -1;
    public int idleRotateLeftAnimation = -1;
    public int idleRotateRightAnimation = -1;
    public int walkingAnimation = -1;
    public int rotate180Animation = -1;
    public int rotate90LeftAnimation = -1;

    public int rotate90RightAnimation = -1;
    public int headIconPrayer = -1;

    public long interfaceType;

    public boolean smoothWalk = true;
    public int runAnimation = -1;

    public static Client getClientInstance() {
        return clientInstance;
    }

    public static EvictingDualNodeHashTable getModelCache() {
        return modelCache;
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
    public int getSize() {
        return size;
    }

    @Override
    public int getRsOverheadIcon() {
        return 0;
    }

    public int[] getModelId() {
        return modelId;
    }

    public int[] getChatheadModels() {
        return chatheadModels;
    }

    public int getStandingAnimation() {
        return standingAnimation;
    }

    public int getWalkingAnimation() {
        return walkingAnimation;
    }

    public int getRotate180Animation() {
        return rotate180Animation;
    }

    public int getRotate90LeftAnimation() {
        return rotate90LeftAnimation;
    }

    public int getRotate90RightAnimation() {
        return rotate90RightAnimation;
    }

    public long getInterfaceType() {
        return interfaceType;
    }

    public boolean isSmoothWalk() {
        return smoothWalk;
    }

    public int getDefaultHeadIconArchive() {
        return defaultHeadIconArchive;
    }

    public boolean isRev210HeadIcons() {
        return rev210HeadIcons;
    }

    public short[] getRecolorToFind() {
        return recolorToFind;
    }

    public short[] getRecolorToReplace() {
        return recolorToReplace;
    }

    public short[] getRetextureToFind() {
        return retextureToFind;
    }

    public short[] getRetextureToReplace() {
        return retextureToReplace;
    }

    @Override
    public String[] getActions() {
        return actions;
    }

    @Override
    public boolean isMinimapVisible() {
        return isMinimapVisible;
    }

    @Override
    public int getCombatLevel() {
        return combatLevel;
    }

    public boolean isHasRenderPriority() {
        return hasRenderPriority;
    }

    public int getAmbient() {
        return ambient;
    }

    public int getContrast() {
        return contrast;
    }

    public int[] getHeadIconArchiveIds() {
        return headIconArchiveIds;
    }

    public short[] getHeadIconSpriteIndex() {
        return headIconSpriteIndex;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public int[] getTransforms() {
        return transforms;
    }

    public int getTransformVarbit() {
        return transformVarbit;
    }

    public boolean isPet() {
        return isPet;
    }

    public int runRotate180Animation = -1;
    public int runRotateLeftAnimation = -1;

    private static int defaultHeadIconArchive = -1;
    private boolean rev210HeadIcons = true;
    public int runRotateRightAnimation = -1;
    public int crawlAnimation = -1;
    public int crawlRotate180Animation = -1;
    public int crawlRotateLeftAnimation = -1;
    public int crawlRotateRightAnimation = -1;
    public short[] recolorToFind;
    public short[] recolorToReplace;
    public short[] retextureToFind;

    public boolean largeHpBar;
    public short[] retextureToReplace;
    public String[] actions = new String[5];
    public boolean isMinimapVisible = true;
    public int combatLevel = -1;
    public int widthScale = 128;
    public int heightScale = 128;
    public boolean hasRenderPriority;
    public int ambient;
    public int contrast;
    public int[] headIconArchiveIds;
    public short[] headIconSpriteIndex;
    public int rotationSpeed = 32;
    public int[] transforms;
    public int transformVarbit = -1;
    public int transformVarp = -1;
    public boolean isInteractable = true;
    public boolean rotationFlag = true;
    public boolean isPet;
    IterableNodeHashTable params;
    public int category;

    public static com.cryptic.collection.table.EvictingDualNodeHashTable npcsCached = new com.cryptic.collection.table.EvictingDualNodeHashTable(64);


    public static NpcDefinition get(int id) {
        NpcDefinition cachedNpc = (NpcDefinition)NpcDefinition.npcsCached.get(id);
        if (cachedNpc == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.NPC, id);
            cachedNpc = new NpcDefinition();
            cachedNpc.id = id;
            if (data != null) {
                cachedNpc.decode(new Buffer(data));
            }
            if (cachedNpc.name.toLowerCase().contains("wise old man")) {
                cachedNpc.setName("Donator Shop");
            }

            switch (id) {
                case 2884:
                    cachedNpc.setName("Ironman Supply Shop");
                    break;
                case 11434:
                    cachedNpc.setName("Ironman Armour Shop");
                    break;
                case 5365:
                    cachedNpc.setName("Vote Shop");
                    break;
            }
            npcsCached.put(cachedNpc, id);
        }
        return cachedNpc;
    }


    void decode(Buffer var1) {
        while(true) {
            int var2 = var1.readUnsignedByte();
            if (var2 == 0) {
                return;
            }

            this.decodeNext(var1, var2);
        }
    }

    static boolean clientRev;

    public static void init(boolean rev, int headIconArchive) {
        clientRev = rev;
        defaultHeadIconArchive = headIconArchive;
    }

    void decodeNext(Buffer buffer, int var2) {
        int index;
        int var4;
        if (var2 == 1) {
            index = buffer.readUnsignedByte();
            modelId = new int[index];

            for(var4 = 0; var4 < index; ++var4) {
                modelId[var4] = buffer.readUShort();
            }
        } else if (var2 == 2) {
            name = buffer.readStringCp1252NullTerminated();
        } else if (var2 == 12) {
            size = buffer.readUnsignedByte();
        } else if (var2 == 13) {
            standingAnimation = buffer.readUShort();
        } else if (var2 == 14) {
            walkingAnimation = buffer.readUShort();
        } else if (var2 == 15) {
            idleRotateLeftAnimation = buffer.readUShort();
        } else if (var2 == 16) {
            idleRotateRightAnimation = buffer.readUShort();
        } else if (var2 == 17) {
            walkingAnimation = buffer.readUShort();
            rotate180Animation = buffer.readUShort();
            rotate90LeftAnimation = buffer.readUShort();
            rotate90RightAnimation = buffer.readUShort();
        } else if (var2 == 18) {
            category = buffer.readUShort();
        } else if (var2 >= 30 && var2 < 35) {
            actions[var2 - 30] = buffer.readStringCp1252NullTerminated();
            if (actions[var2 - 30].equalsIgnoreCase("Hidden")) {
                actions[var2 - 30] = null;
            }
        } else if (var2 == 40) {
            index = buffer.readUnsignedByte();
            recolorToFind = new short[index];
            recolorToReplace = new short[index];

            for(var4 = 0; var4 < index; ++var4) {
                recolorToFind[var4] = (short)buffer.readUShort();
                recolorToReplace[var4] = (short)buffer.readUShort();
            }
        } else if (var2 == 41) {
            index = buffer.readUnsignedByte();
            retextureToFind = new short[index];
            retextureToReplace = new short[index];

            for(var4 = 0; var4 < index; ++var4) {
                retextureToFind[var4] = (short)buffer.readUShort();
                retextureToReplace[var4] = (short)buffer.readUShort();
            }
        } else if (var2 == 60) {
            index = buffer.readUnsignedByte();
            chatheadModels = new int[index];

            for(var4 = 0; var4 < index; ++var4) {
                chatheadModels[var4] = buffer.readUShort();
            }
        } else if (var2 == 93) {
            isMinimapVisible = false;
        } else if (var2 == 95) {
            combatLevel = buffer.readUShort();
        } else if (var2 == 97) {
            widthScale = buffer.readUShort();
        } else if (var2 == 98) {
            heightScale = buffer.readUShort();
        } else if (var2 == 99) {
            hasRenderPriority = true;
        } else if (var2 == 100) {
            ambient = buffer.readSignedByte();
        } else if (var2 == 101) {
            contrast = buffer.readSignedByte();
        } else {
            int var5;
            if (var2 == 102) {
                if (clientRev) {
                    headIconArchiveIds = new int[1];
                    headIconSpriteIndex = new short[1];
                    headIconArchiveIds[0] = defaultHeadIconArchive;
                    headIconSpriteIndex[0] = (short)buffer.readUShort();
                } else {
                    index = buffer.readUnsignedByte();
                    var4 = 0;

                    for(var5 = index; var5 != 0; var5 >>= 1) {
                        ++var4;
                    }

                    headIconArchiveIds = new int[var4];
                    headIconSpriteIndex = new short[var4];

                    for(int var6 = 0; var6 < var4; ++var6) {
                        if ((index & 1 << var6) == 0) {
                            headIconArchiveIds[var6] = -1;
                            headIconSpriteIndex[var6] = -1;
                        } else {
                            headIconArchiveIds[var6] = buffer.readNullableLargeSmart();
                            headIconSpriteIndex[var6] = (short)buffer.readShortSmartSub();
                        }
                    }
                }
            } else if (var2 == 103) {
                rotationSpeed = buffer.readUShort();
            } else if (var2 != 106 && var2 != 118) {
                if (var2 == 107) {
                    isInteractable = false;
                } else if (var2 == 109) {
                    smoothWalk = false;
                } else if (var2 == 111) {
                    isPet = true;
                } else if (var2 == 114) {
                    runAnimation = buffer.readUShort();
                } else if (var2 == 115) {
                    runAnimation = buffer.readUShort();
                    runRotate180Animation = buffer.readUShort();
                    runRotateLeftAnimation = buffer.readUShort();
                    runRotateRightAnimation = buffer.readUShort();
                } else if (var2 == 116) {
                    crawlAnimation = buffer.readUShort();
                } else if (var2 == 117) {
                    crawlAnimation = buffer.readUShort();
                    crawlRotate180Animation = buffer.readUShort();
                    crawlRotateLeftAnimation = buffer.readUShort();
                    crawlRotateRightAnimation = buffer.readUShort();
                } else if (var2 == 249) {
                    params = Buffer.readStringIntParameters(buffer, params);
                }
            } else {
                transformVarbit = buffer.readUShort();
                if (transformVarbit == 65535) {
                    transformVarbit = -1;
                }

                transformVarp = buffer.readUShort();
                if (transformVarp == 65535) {
                    transformVarp = -1;
                }

                index = -1;
                if (var2 == 118) {
                    index = buffer.readUShort();
                    if (index == 65535) {
                        index = -1;
                    }
                }

                var4 = buffer.readUnsignedByte();
                transforms = new int[var4 + 2];

                for(var5 = 0; var5 <= var4; ++var5) {
                    transforms[var5] = buffer.readUShort();
                    if (transforms[var5] == 65535) {
                        transforms[var5] = -1;
                    }
                }

                transforms[var4 + 1] = index;
            }
        }

    }

    public boolean transformIsVisible() {
        if (null == this.transforms) {
            return true;
        } else {
            int var2 = -1;
            if (this.transformVarbit != -1) {
                var2 = Client.getVar(this.transformVarbit);
            } else if (-1 != this.transformVarp) {
                var2 = clientInstance.settings[this.transformVarp];
            }

            if (var2 >= 0 && var2 < this.transforms.length) {
                return -1 != this.transforms[var2];
            } else {
                return this.transforms[this.transforms.length - 1] != -1;
            }
        }
    }

    public NpcDefinition get_configs() {
        try {
            int j = -1;
            if (getTransformVarbit() != -1) {
                VariableBits varBit = VariableBits.lookup(getTransformVarbit());
                int k = varBit.baseVar;
                int l = varBit.startBit;
                int i1 = varBit.endBit;
                int j1 = Client.BIT_MASKS[i1 - l];
                j = clientInstance.settings[k] >> l & j1;
            } else if (transformVarp != -1)
                j = clientInstance.settings[transformVarp];
            if (j < 0 || j >= getTransforms().length || getTransforms()[j] == -1)
                return null;
            else
                return get(getTransforms()[j]);
        } catch (Exception e) {
            System.err.println("There was an error getting configs for NPC " + id);
            e.printStackTrace();
        }
        //Ken comment: return null if we haven't returned already, this shouldn't be possible.
        return null;
    }

    public NpcDefinition get_transforms() {
        //Ken comment: Added try catch to get_transforms method to catch any config errors
        try {
            int j = -1;
            if (getTransformVarbit() != -1) {
                VariableBits varBit = VariableBits.lookup(getTransformVarbit());
                int k = varBit.baseVar;
                int l = varBit.startBit;
                int i1 = varBit.endBit;
                int j1 = Client.BIT_MASKS[i1 - l];
                j = clientInstance.settings[k] >> l & j1;
            } else if (transformVarp != -1)
                j = clientInstance.settings[transformVarp];
            if (j < 0 || j >= getTransforms().length || getTransforms()[j] == -1)
                return null;
            else
                return get(getTransforms()[j]);
        } catch (Exception e) {
            System.err.println("There was an error getting transforms for NPC " + id);
            e.printStackTrace();
        }
        //Ken comment: return null if we haven't returned already, this shouldn't be possible.
        return null;
    }

    public Mesh get_dialogue_model() {
        if (this.transforms != null) {
            NpcDefinition var2 = this.get_configs();
            return var2 == null ? null : var2.get_dialogue_model();
        } else {
            return this.getModelData(this.chatheadModels);
        }
    }

    Mesh getModelData(int[] models) {

        if (models == null) {
            return null;
        } else {
            boolean cached = false;

            for (int i : models) {
                if (i != -1 && !Js5List.models.tryLoadFile(i, 0)) {
                    cached = true;
                }
            }

            if (cached) {
                return null;
            } else {
                Mesh[] modelParts = new Mesh[models.length];

                for(int var6 = 0; var6 < models.length; ++var6) {
                    modelParts[var6] = Mesh.getModel(models[var6]);
                }

                Mesh model;
                if (modelParts.length == 1) {
                    model = modelParts[0];
                    if (model == null) {
                        model = new Mesh(modelParts, modelParts.length);
                    }
                } else {
                    model = new Mesh(modelParts, modelParts.length);
                }

                if (getRecolorToFind() != null) {
                    for (int k = 0; k < getRecolorToFind().length; k++)
                        model.recolor(getRecolorToFind()[k], getRecolorToReplace()[k]);
                }

                if (getRetextureToReplace() != null) {
                    for (int k1 = 0; k1 < getRetextureToReplace().length; k1++) {
                        model.recolor(getRetextureToReplace()[k1], getRetextureToFind()[k1]);
                    }
                }

                return model;
            }
        }
    }



    public final Model get_animated_model(SequenceDefinition var1, int var2, SequenceDefinition var3, int var4) {
        if (this.transforms != null) {
            NpcDefinition var10 = this.get_configs();
            return var10 == null ? null : var10.get_animated_model(var1, var2, var3, var4);
        } else {
            long var6 = (long)this.id;


            Model var8 = (Model)modelCache.get(var6);
            if (var8 == null) {
                Mesh var9 = this.getModelData(this.modelId);
                if (var9 == null) {
                    return null;
                }

                var8 = var9.toModel(this.ambient + 64, this.contrast * 5 + 850, -30, -50, -30);
                modelCache.put(var8, var6);
            }

            Model var11;
            if (var1 != null && var3 != null) {
                var11 = var1.applyTransformations(var8, var2, var3, var4);
            } else if (var1 != null) {
                var11 = var1.transformActorModel(var8, var2);
            } else if (var3 != null) {
                var11 = var3.transformActorModel(var8, var4);
            } else {
                var11 = var8.toSharedSequenceModel(true);
            }

            if (this.widthScale != 128 || this.heightScale != 128) {
                var11.scale(this.widthScale, this.heightScale, this.widthScale);
            }

            return var11;
        }
    }


    @Override
    public int[] getModels() {
        return modelId;
    }

    @Override
    public void setIndex(int npcIndex) {

    }

    @Override
    public boolean isFollower() {
        return isPet;
    }

    @Override
    public boolean isInteractible() {
        return isInteractable;
    }

    @Override
    public boolean isVisible() {
        return null != this.definition;
    }

    @Override
    public int[] getConfigs() {
        return new int[0];
    }

    @Override
    public RSNPCComposition transform() {
        return get_configs();
    }

    @Override
    public HeadIcon getOverheadIcon() {
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
}
