package com.cryptic.cache.def.anim;

import com.cryptic.cache.def.anim.skeleton.SkeletalFrameHandler;
import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.table.EvictingDualNodeHashTable;
import com.cryptic.entity.model.Model;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SequenceDefinition extends DualNode {

    static boolean field2206 = false;


    public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);
    public static EvictingDualNodeHashTable cachedFrames = new EvictingDualNodeHashTable(100);
    public static EvictingDualNodeHashTable cachedModel = new EvictingDualNodeHashTable(100);

    public static int length() {
        return Js5List.getConfigSize(Js5ConfigType.SEQUENCE);
    }

    public static SequenceDefinition get(int id) {
        SequenceDefinition cached = (SequenceDefinition)SequenceDefinition.cached.get(id);
        if (cached == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.SEQUENCE, id);
            cached = new SequenceDefinition();
            if (data != null) {
                cached.decode(new Buffer(data));
                cached.id = id;
            }

            cached.postDecode();
            SequenceDefinition.cached.put(cached, id);
        }
        return cached;
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
        if (opcode == 1) {
            int frameCount = buffer.readUShort();
            delays = new int[frameCount];

            for(int index = 0; index < frameCount; ++index) {
                delays[index] = buffer.readUShort();
            }

            primaryFrameIds = new int[frameCount];

            for(int index = 0; index < frameCount; ++index) {
                primaryFrameIds[index] = buffer.readUShort();
            }

            for(int index = 0; index < frameCount; ++index) {
                primaryFrameIds[index] += buffer.readUShort() << 16;
            }

        } else if (opcode == 2) {
            this.frameCount = buffer.readUShort();
        } else if (opcode == 3) {
            int count = buffer.readUnsignedByte();
            this.masks = new int[count + 1];
            for(int index = 0; index < count; ++index) {
                this.masks[index] = buffer.readUnsignedByte();
            }
            masks[count] = 0x98967f;
        } else if (opcode == 4) {
            this.stretches = true;
        } else if (opcode == 5) {
            this.forcedPriority = buffer.readUnsignedByte();
        } else if (opcode == 6) {
            this.leftHandItem = buffer.readUShort();
        } else if (opcode == 7) {
            this.rightHandItem = buffer.readUShort();
        } else if (opcode == 8) {
            this.loopCount = buffer.readUnsignedByte();
            this.replay = true;
        } else if (opcode == 9) {
            this.moveStyle = buffer.readUnsignedByte();
        } else if (opcode == 10) {
            this.idleStyle = buffer.readUnsignedByte();
        } else if (opcode == 11) {
            this.delayType = buffer.readUnsignedByte();
        } else if (opcode == 12) {
            int count = buffer.readUnsignedByte();
            this.chatFrameIds = new int[count];

            for(int index = 0; index < count; ++index) {
                this.chatFrameIds[index] = buffer.readUShort();
            }

            for(int index = 0; index < count; ++index) {
                this.chatFrameIds[index] += buffer.readUShort() << 16;
            }
        } else if (opcode == 13) {
            int count = buffer.readUnsignedByte();
            this.soundEffects = new int[count];

            for(int index = 0; index < count; ++index) {
                this.soundEffects[index] = buffer.readMedium();
            }
        } else if (opcode == 14) {
            this.skeletalId = buffer.readInt();
        } else if (opcode == 15) {
            int count = buffer.readUShort();
            this.soundRelated = new HashMap<>();

            for(int index = 0; index < count; ++index) {
                int id = buffer.readUShort();
                int range = buffer.readMedium();
                this.soundRelated.put(id, range);
            }
        } else if (opcode == 16) {
            this.rangeBegin = buffer.readUShort();
            this.rangeEnd = buffer.readUShort();
        } else if (opcode == 17) {
            this.booleanMasks = new boolean[256];

            Arrays.fill(this.booleanMasks, false);

            int count = buffer.readUnsignedByte();

            for(int index = 0; index < count; ++index) {
                this.booleanMasks[buffer.readUnsignedByte()] = true;
            }
        }
    }


    public int duration(int i) {
        int j = isCachedModelIdSet() ? 1 : delays[i];
        if (j == 0)
            j = 1;
        return j;
    }


    public void postDecode() {
        if (moveStyle == -1) {
            if (masks == null && booleanMasks == null) {
                moveStyle = 0;
            } else {
                moveStyle = 2;
            }
        }

        if (idleStyle == -1) {
            if (masks == null && booleanMasks == null) {
                idleStyle = 0;
            } else {
                idleStyle = 2;
            }
        }
    }

    public int getFrameSoundeffect(int frameIndex) {
        if (soundEffects != null && frameIndex < soundEffects.length && soundEffects[frameIndex] != 0) {
            return soundEffects[frameIndex];
        } else {
            return -1;
        }
    }

    public int id;

    public int[] primaryFrameIds;
    private int[] secondaryFrameIds;
    public int[] delays;
    public int frameCount = -1;
    public int[] masks;
    private boolean[] booleanMasks;
    public boolean stretches;
    public int forcedPriority = 5;
    public boolean replay = false;
    public int leftHandItem = -1;
    public int rightHandItem = -1;
    public int loopCount = 99;
    public int moveStyle = -1;
    public int idleStyle = -1;
    public int delayType = 2;
    private int[] chatFrameIds;
    private int[] soundEffects;
    private int skeletalId = -1;
    private int rangeBegin = 0;
    private int rangeEnd = 0;

    public Map<Integer, Integer> soundRelated;

    public static void clear() {
        SequenceDefinition.cached.clear();
        SequenceDefinition.cachedFrames.clear();
        SequenceDefinition.cachedModel.clear();
    }

    public int getSkeletalLength() {
        return this.rangeEnd - this.rangeBegin;
    }

    public boolean isCachedModelIdSet() {
        return skeletalId >= 0;
    }

    public Model transformWidgetModel(Model model, int primaryIndex) {
        if(isCachedModelIdSet()) {
            return this.transformActorModel(model, primaryIndex);
        }
        int regularFrame = primaryFrameIds[primaryIndex];
        Frames regularFrameset = Frames.getFrames(regularFrame >> 16);
        int regularFrameindex = regularFrame & 0xffff;
        if (regularFrameset == null) {
            return model.toSharedSpotAnimationModel(true);
        }
        Frames frameSet = null;
        int fameID = 0;
        int ifFrameindex = 0;
        if (chatFrameIds != null && primaryIndex < chatFrameIds.length) {
            fameID = chatFrameIds[primaryIndex];
            frameSet = Frames.getFrames(fameID >> 16);
            ifFrameindex = fameID & 0xffff;
        }

        Model animatedModel;
        if (frameSet == null || fameID == 0xffff) {
            animatedModel = model.toSharedSpotAnimationModel(!regularFrameset.hasAlphaTransform(regularFrameindex));
            animatedModel.animate(regularFrameset, regularFrameindex);
        } else {
            animatedModel = model.toSharedSpotAnimationModel(!regularFrameset.hasAlphaTransform(regularFrameindex) & !frameSet.hasAlphaTransform(ifFrameindex));
            animatedModel.animate(regularFrameset, regularFrameindex);
            animatedModel.animate(frameSet, ifFrameindex);
        }
        return animatedModel;
    }

    public Model transformActorModel(Model originalModel, int frameIndex) {
        Model transformedModel;
        if (!this.isCachedModelIdSet()) {
            frameIndex = this.primaryFrameIds[frameIndex];
            Frames frameSet = Frames.getFrames(frameIndex >> 16);
            frameIndex &= 65535;
            if (frameSet == null) {
                return originalModel.toSharedSequenceModel(true);
            } else {
                transformedModel = originalModel.toSharedSequenceModel(!frameSet.hasAlphaTransform(frameIndex));
                transformedModel.animate(frameSet, frameIndex);
                return transformedModel ;
            }
        } else {
            SkeletalFrameHandler skeletalFrames = SkeletalFrameHandler.getFrames(this.skeletalId);
            if (skeletalFrames == null) {
                return originalModel.toSharedSequenceModel(true);
            } else {
                transformedModel = originalModel.toSharedSequenceModel(!skeletalFrames.method3187());
                transformedModel.applySkeletalTransform(skeletalFrames, frameIndex);
                return transformedModel;
            }
        }
    }


    private SkeletalFrameHandler getSkeletalFrameHandler() {
        return this.isCachedModelIdSet() ? SkeletalFrameHandler.getFrames(this.skeletalId) : null;
    }

    public Model applyTransformations(Model originalModel, int frameIndex, SequenceDefinition sequenceDef, int sequenceFrameIndex) {
        if (field2206 && !this.isCachedModelIdSet() && !sequenceDef.isCachedModelIdSet()) {
            return this.transformModelSequence(originalModel, frameIndex, sequenceDef, sequenceFrameIndex);
        } else {
            Model transformedModel = originalModel.toSharedSequenceModel(false);
            boolean sequenceCachedModelFlag = false;
            Frames primaryFrames = null;
            Skeleton skeleton = null;
            SkeletalFrameHandler skeletalFrameHandler;
            if (this.isCachedModelIdSet()) {
                skeletalFrameHandler = this.getSkeletalFrameHandler();
                if (skeletalFrameHandler == null) {
                    return transformedModel;
                }

                if (sequenceDef.isCachedModelIdSet() && this.booleanMasks == null) {
                    transformedModel.applySkeletalTransform(skeletalFrameHandler, frameIndex);
                    return transformedModel;
                }

                skeleton = skeletalFrameHandler.base;
                transformedModel.applyComplexTransform(skeleton, skeletalFrameHandler, frameIndex, this.booleanMasks, false, !sequenceDef.isCachedModelIdSet());
            } else {
                frameIndex = this.primaryFrameIds[frameIndex];
                primaryFrames = Frames.getFrames(frameIndex >> 16);
                frameIndex &= 65535;
                if (primaryFrames == null) {
                    return sequenceDef.transformActorModel(originalModel, sequenceFrameIndex);
                }

                if (!sequenceDef.isCachedModelIdSet() && (this.masks == null || sequenceFrameIndex == -1)) {
                    transformedModel.animate(primaryFrames, frameIndex);
                    return transformedModel;
                }

                if (this.masks == null || sequenceFrameIndex == -1) {
                    transformedModel.animate(primaryFrames, frameIndex);
                    return transformedModel;
                }

                sequenceCachedModelFlag = sequenceDef.isCachedModelIdSet();
                if (!sequenceCachedModelFlag) {
                    transformedModel.animate(primaryFrames, frameIndex, this.masks, false);
                }
            }

            if (sequenceDef.isCachedModelIdSet()) {
                skeletalFrameHandler = sequenceDef.getSkeletalFrameHandler();
                if (skeletalFrameHandler == null) {
                    return transformedModel;
                }

                if (skeleton == null) {
                    skeleton = skeletalFrameHandler.base;
                }

                transformedModel.applyComplexTransform(skeleton, skeletalFrameHandler, sequenceFrameIndex, this.booleanMasks, true, true);
            } else {
                sequenceFrameIndex = sequenceDef.primaryFrameIds[sequenceFrameIndex];
                Frames var10 = Frames.getFrames(sequenceFrameIndex >> 16);
                sequenceFrameIndex &= 65535;
                if (var10 == null) {
                    return this.transformActorModel(originalModel, frameIndex);
                }

                transformedModel.animate(var10, sequenceFrameIndex, this.masks, true);
            }

            if (sequenceCachedModelFlag && primaryFrames != null) {
                transformedModel.animate(primaryFrames, frameIndex, this.masks, false);
            }

            transformedModel.resetBounds();
            return transformedModel;
        }
    }

    Model transformModelSequence(Model originalModel, int frameIndex, SequenceDefinition sequence, int sequenceFrameIndex) {
        frameIndex = this.primaryFrameIds[frameIndex];
        Frames var5 = Frames.getFrames(frameIndex >> 16);
        frameIndex &= 65535;
        if (var5 == null) {
            return sequence.transformActorModel(originalModel, sequenceFrameIndex);
        } else {
            sequenceFrameIndex = sequence.primaryFrameIds[sequenceFrameIndex];
            Frames frameSet = Frames.getFrames(sequenceFrameIndex >> 16);
            sequenceFrameIndex &= 65535;
            Model transformedModel;
            if (frameSet == null) {
                transformedModel = originalModel.toSharedSequenceModel(!var5.hasAlphaTransform(frameIndex));
                transformedModel.animate(var5, frameIndex);
            } else {
                transformedModel = originalModel.toSharedSequenceModel(!var5.hasAlphaTransform(frameIndex) & !frameSet.hasAlphaTransform(sequenceFrameIndex));
                transformedModel.animate2(var5, frameIndex, frameSet, sequenceFrameIndex, this.masks);
            }
            return transformedModel;
        }
    }


    Model transformSpotAnimationModel(Model originalModel, int frameIndex) {
        Model transformedModel;
        if (!this.isCachedModelIdSet()) {
            frameIndex = this.primaryFrameIds[frameIndex];
            Frames frameSet = Frames.getFrames(frameIndex >> 16);
            frameIndex &= 65535;
            if (frameSet == null) {
                return originalModel.toSharedSpotAnimationModel(true);
            } else {
                transformedModel = originalModel.toSharedSpotAnimationModel(!frameSet.hasAlphaTransform(frameIndex));
                transformedModel.animate(frameSet, frameIndex);
                return transformedModel;
            }
        } else {
            SkeletalFrameHandler var3 = SkeletalFrameHandler.getFrames(this.skeletalId);
            if (var3 == null) {
                return originalModel.toSharedSpotAnimationModel(true);
            } else {
                transformedModel = originalModel.toSharedSpotAnimationModel(!var3.method3187());
                transformedModel.applySkeletalTransform(var3, frameIndex);
                return transformedModel;
            }
        }
    }

    public Model transformObjectModel(Model originalModel, int frameIndex, int rotationIndex) {
        if (!this.isCachedModelIdSet()) {
            frameIndex = this.primaryFrameIds[frameIndex];
            Frames frameSet = Frames.getFrames(frameIndex >> 16);
            frameIndex &= 65535;
            if (frameSet == null) {
                return originalModel.toSharedSequenceModel(true);
            } else {
                Model transformedModel = originalModel.toSharedSequenceModel(!frameSet.hasAlphaTransform(frameIndex));
                rotationIndex &= 3;
                if (rotationIndex == 1) {
                    transformedModel.rotateY270Ccw();
                } else if (rotationIndex == 2) {
                    transformedModel.rotateY180Ccw();
                } else if (rotationIndex == 3) {
                    transformedModel.rotateY90Ccw();
                }

                transformedModel.animate(frameSet, frameIndex);
                if (rotationIndex == 1) {
                    transformedModel.rotateY90Ccw();
                } else if (rotationIndex == 2) {
                    transformedModel.rotateY180Ccw();
                } else if (rotationIndex == 3) {
                    transformedModel.rotateY270Ccw();
                }

                return transformedModel;
            }
        } else {
            SkeletalFrameHandler skeletalFrameHandler = SkeletalFrameHandler.getFrames(this.skeletalId);

            if (skeletalFrameHandler == null) {
                return originalModel.toSharedSequenceModel(true);
            } else {
                Model transformedModel = originalModel.toSharedSequenceModel(!skeletalFrameHandler.method3187());
                rotationIndex &= 3;
                if (rotationIndex == 1) {
                    transformedModel.rotateY270Ccw();
                } else if (rotationIndex == 2) {
                    transformedModel.rotateY180Ccw();
                } else if (rotationIndex == 3) {
                    transformedModel.rotateY90Ccw();
                }

                transformedModel.applySkeletalTransform(skeletalFrameHandler, frameIndex);
                if (rotationIndex == 1) {
                    transformedModel.rotateY90Ccw();
                } else if (rotationIndex == 2) {
                    transformedModel.rotateY180Ccw();
                } else if (rotationIndex == 3) {
                    transformedModel.rotateY270Ccw();
                }

                return transformedModel;
            }
        }
    }

    public int[] getSoundEffects() {
        return soundEffects;
    }

    public Map getSkeletalSounds() {
        return soundRelated;
    }
}
