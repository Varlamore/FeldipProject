package com.cryptic.entity.model;

import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.table.EvictingDualNodeHashTable;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;

public final class IdentityKit extends DualNode {


    public int bodyPartId = -1;
    private int[] bodyModels;
    private short[] originalColors = new short[6];
    private short[] replacementColors = new short[6];
    public short[] retextureToFind;
    public short[] retextureToReplace;
    private final int[] headModels = { -1, -1, -1, -1, -1 };
    public boolean validStyle;


    public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);

    public static IdentityKit lookup(int id) {
        IdentityKit hasCached = (IdentityKit)IdentityKit.cached.get(id);
        if (hasCached == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.IDENTKIT, id);
            hasCached = new IdentityKit();
            if (data != null) {
                hasCached.load(new Buffer(data));
                hasCached.originalColors[0] = (short) 55232;
                hasCached.replacementColors[0] = 6798;
            }

            cached.put(hasCached, id);
        }
        return hasCached;
    }


    public void load(Buffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            }
            if (opcode == 1) {
                bodyPartId = buffer.readUnsignedByte();
            } else if (opcode == 2) {
                int length = buffer.readUnsignedByte();
                bodyModels = new int[length];

                for (int index = 0; index < length; ++index) {
                    bodyModels[index] = buffer.readUnsignedShort();
                }
            } else if (opcode == 3) {
                validStyle = true;
            } else if (opcode == 40) {
                int length = buffer.readUnsignedByte();
                originalColors = new short[length];
                replacementColors = new short[length];

                for (int index = 0; index < length; ++index) {
                    originalColors[index] = (short) buffer.readShort();
                    replacementColors[index] = (short) buffer.readShort();
                }
            } else if (opcode == 41) {
                int length = buffer.readUnsignedByte();
                retextureToFind = new short[length];
                retextureToReplace = new short[length];

                for (int index = 0; index < length; ++index) {
                    retextureToFind[index] = (short) buffer.readShort();
                    retextureToReplace[index] = (short) buffer.readShort();
                }
            } else if (opcode >= 60 && opcode < 70) {
                headModels[opcode - 60] = buffer.readUnsignedShort();
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        }
    }

    public boolean body_cached() {
        if (bodyModels == null) {
            return true;
        }
        boolean ready = true;
        for (int bodyModel : bodyModels) {
            if (!Js5List.models.tryLoadFile(bodyModel))
                ready = false;
        }
        return ready;
    }

    public Mesh get_body() {
        if (bodyModels == null) {
            return null;
        }

        Mesh[] models = new Mesh[bodyModels.length];

        for (int part = 0; part < bodyModels.length; part++) {
            models[part] = Mesh.getModel(bodyModels[part]);
        }

        Mesh model;
        if (models.length == 1) {
            model = models[0];
        } else {
            model = new Mesh(models,models.length);
        }

        if (model != null) {
            if (originalColors != null) {
                for (int index = 0; index < originalColors.length; index++) {
                    model.recolor(originalColors[index], replacementColors[index]);
                }
                if (retextureToFind != null) {
                    for (int index = 0; index < retextureToFind.length; index++) {
                        model.retexture(retextureToFind[index], retextureToReplace[index]);
                    }
                }
            }
        }

        return model;
    }

    public boolean headLoaded() {
        boolean ready = true;
        for (int part = 0; part < 5; part++) {
            if (headModels[part] != -1 && !Js5List.models.tryLoadFile(headModels[part])) {
                ready = false;
            }
        }
        return ready;
    }

    public Mesh get_head() {
        Mesh[] headModels = new Mesh[5];
        int modelIndex = 0;

        for(int index = 0; index < 5; ++index) {
            if (this.headModels[index] != -1) {
                headModels[modelIndex++] = Mesh.getModel(this.headModels[index]);
            }
        }

        Mesh headModeel = new Mesh(headModels,modelIndex);
        if (this.originalColors != null) {
            for(int index = 0; index < this.originalColors.length; ++index) {
                headModeel.recolor(this.originalColors[index], this.replacementColors[index]);
            }
        }

        if (this.retextureToFind != null) {
            for(int index = 0; index < this.retextureToFind.length; ++index) {
                headModeel.retexture(this.retextureToFind[index], this.retextureToReplace[index]);
            }
        }

        return headModeel;
    }

}
