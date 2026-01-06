package com.cryptic.entity;

import com.cryptic.Client;
import com.cryptic.audio.StaticSound;
import com.cryptic.cache.def.anim.SequenceDefinition;
import com.cryptic.cache.def.anim.SpotAnimation;
import com.cryptic.collection.iterable.IterableNodeDeque;
import com.cryptic.collection.iterable.IterableNodeHashTableIterator;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.entity.model.Model;
import com.cryptic.io.Buffer;
import com.google.common.collect.ImmutableSet;
import net.runelite.api.*;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.rs.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Set;

public class Entity extends Renderable implements RSActor {

    public int index = -1;
    public final int[] pathX;
    public final int[] pathY;
    public int targetIndex;
    public int step_tracker;
    public int rotation;
    public int turnRightSequence = -1;

    public IterableNodeHashTable spotAnimations;
    public int runSequence;
    public String spokenText;
    public int defaultHeight;
    public int[] damage_cycle;
    public int turn_direction;
    public int idleSequence;
    public int turnLeftSequence;
    public int textColour;
    public final int[] primary_hitmark_damage;
    public final int[] primary_hitmark_id;
    public int movementSequence;
    public int movementFrame;
    public int movementFrameCycle;


    public int pathLength;
    public int sequence;
    public int sequenceFrame;
    public int sequenceFrameCycle;
    public int sequenceDelay;
    public int currentAnimationLoops = 0;

    public int textEffect;
    public int game_tick_status;

    public int current_hitpoints;
    public int maximum_hitpoints;
    public int message_cycle;
    public int last_update_tick;
    public int faceX;
    public byte recolorHue;
    public byte recolourSaturation;
    public byte recolourLuminance;
    public byte recolourAmount;
    public int faceY;
    public int size; //occupied_tiles is also known as size.
    public boolean isWalking;
    public int anim_delay;
    public int initialX;
    public int destinationX;
    public int initialY;
    public int destinationY;
    public int initiate_movement;
    public int cease_movement;
    public int direction;
    public int x;
    public int y;
    public int current_rotation;
    public final boolean[] waypoint_movespeed;
    public int walkSequence;
    public int walkBackSequence;
    public int walkLeftSequence;
    public int walkRightSequence;

    public int recolourStartCycle;
    public int recolourEndCycle;

    int graphicsCount;
    public final int[] secondary_hitmark_id;
    public final int[] secondary_hitmark_damage;
    public int tinted;

    public void clearSpotAnimations() {
        IterableNodeHashTableIterator spotAnim = new IterableNodeHashTableIterator(this.spotAnimations);

        for (ActorSpotAnim currentSpotAnimation = (ActorSpotAnim)spotAnim.method2390(); currentSpotAnimation != null; currentSpotAnimation = (ActorSpotAnim)spotAnim.next()) {
            currentSpotAnimation.remove();
        }

        this.graphicsCount = 0;
    }


    Model createSpotAnimModel(Model model) {
        if (this.graphicsCount == 0) {
            return model;
        } else {
            IterableNodeHashTableIterator iterator = new IterableNodeHashTableIterator(this.spotAnimations);
            int totalVertices = model.verticesCount;
            int totalIndices = model.indicesCount;
            int someField = model.field2153;
            byte var6 = model.field2152;

            for (ActorSpotAnim spotAnim = (ActorSpotAnim)iterator.method2390(); spotAnim != null; spotAnim = (ActorSpotAnim)iterator.next()) {
                if (spotAnim.spotAnimationFrame != -1) {
                    Model animModel = SpotAnimation.lookup(spotAnim.spotAnimation).createModel();
                    if (animModel != null) {
                        totalVertices += animModel.verticesCount;
                        totalIndices += animModel.indicesCount;
                        someField += animModel.field2153;
                    }
                }
            }

            Model resultModel = new Model(totalVertices, totalIndices, someField, var6);
            resultModel.method1342(model);

            for (ActorSpotAnim spotAnim = (ActorSpotAnim)iterator.method2390(); spotAnim != null; spotAnim = (ActorSpotAnim)iterator.next()) {
                if (spotAnim.spotAnimationFrame != -1) {
                    Model animModel = SpotAnimation.lookup(spotAnim.spotAnimation).getModel(spotAnim.spotAnimationFrame);
                    if (animModel != null) {
                        animModel.offsetBy(0, -spotAnim.spotAnimationHeight, 0);
                        resultModel.method1342(animModel);
                    }
                }
            }

            return resultModel;
        }
    }

    public Entity() {
        pathX = new int[10];
        pathY = new int[10];
        targetIndex = -1;
        rotation = 32;
        runSequence = -1;
        defaultHeight = 200;
        idleSequence = -1;
        turnLeftSequence = -1;
        primary_hitmark_damage = new int[4];
        primary_hitmark_id = new int[4];
        damage_cycle = new int[4];
        secondary_hitmark_id = new int[4];
        secondary_hitmark_damage = new int[4];
        tinted = 0;
        movementSequence = -1;
        sequence = -1;
        game_tick_status = -1000;
        message_cycle = 100;
        size = 1;
        isWalking = false;
        waypoint_movespeed = new boolean[10];
        walkSequence = -1;
        spotAnimations = new IterableNodeHashTable(4);
        walkBackSequence = -1;
        walkLeftSequence = -1;
        walkRightSequence = -1;
        recolourStartCycle = -1;
    }

    public HealthBar getHealthBar() {
        return HealthBar.DEFAULT;
    }

    public int getHealthDimension() {
        return HealthBar.DIM_30;
    }

    public final void setPos(int x, int y, boolean flag) {
        if (sequence != -1 && SequenceDefinition.get(sequence).idleStyle == 1)
            sequence = -1;

        if (!flag) {
            int dx = x - pathX[0];
            int dy = y - pathY[0];
            if (dx >= -8 && dx <= 8 && dy >= -8 && dy <= 8) {
                if (pathLength < 9)
                    pathLength++;
                for (int i1 = pathLength; i1 > 0; i1--) {
                    pathX[i1] = pathX[i1 - 1];
                    pathY[i1] = pathY[i1 - 1];
                    waypoint_movespeed[i1] = waypoint_movespeed[i1 - 1];
                }

                pathX[0] = x;
                pathY[0] = y;
                waypoint_movespeed[0] = false;
                return;
            }
        }
        pathLength = 0;
        anim_delay = 0;
        step_tracker = 0;
        pathX[0] = x;
        pathY[0] = y;
        this.x = pathX[0] * 128 + size * 64;
        this.y = pathY[0] * 128 + size * 64;
    }

    public final void resetPath() {
        pathLength = 0;
        anim_delay = 0;
    }

    public final void updateHitData(int mark, int damage, int tick) {
        for (int hitPtr = 0; hitPtr < 4; hitPtr++)
            if (this.damage_cycle[hitPtr] <= Client.tick) {
                this.primary_hitmark_damage[hitPtr] = damage;
                this.primary_hitmark_id[hitPtr] = mark;
                this.damage_cycle[hitPtr] = tick + 70;
                return;
            }
    }

    public void refreshEntityPosition() {
        int remaining = initiate_movement - Client.tick;
        int tempX = initialX * 128 + size * 64;
        int tempY = initialY * 128 + size * 64;
        x += (tempX - x) / remaining;
        y += (tempY - y) / remaining;

        step_tracker = 0;

        if (direction == 0) {
            turn_direction = 1024;
        }

        if (direction == 1) {
            turn_direction = 1536;
        }

        if (direction == 2) {
            turn_direction = 0;
        }

        if (direction == 3) {
            turn_direction = 512;
        }
    }

    public void refreshEntityFaceDirection() {
        if (cease_movement == Client.tick || sequence == -1
                || sequenceDelay != 0
                || sequenceFrameCycle + 1 > SequenceDefinition.get(sequence).delays[sequenceFrame]) {
            int remaining = cease_movement - initiate_movement;
            int elapsed = Client.tick - initiate_movement;
            int initialX = this.initialX * 128 + size * 64;
            int initialY = this.initialY * 128 + size * 64;
            int endX = destinationX * 128 + size * 64;
            int endY = destinationY * 128 + size * 64;
            x = (initialX * (remaining - elapsed) + endX * elapsed) / remaining;
            y = (initialY * (remaining - elapsed) + endY * elapsed) / remaining;
            //System.out.println("end at "+initialX+" "+initialY+"  "+endX+" "+endY+" "+world_x+","+world_y);
        }

        step_tracker = 0;

        if (direction == 0) {
            turn_direction = 1024;
        }

        if (direction == 1) {
            turn_direction = 1536;
        }

        if (direction == 2) {
            turn_direction = 0;
        }

        if (direction == 3) {
            turn_direction = 512;
        }

        current_rotation = turn_direction;
    }

    public void getDegreesToTurn() {
        movementSequence = idleSequence;

        if (pathLength == 0) {
            step_tracker = 0;
            return;
        }

        if (sequence > SequenceDefinition.length()) {
            sequence = -1;
            return;
        }

        if (sequence != -1 && sequenceDelay == 0) {
            SequenceDefinition seq = SequenceDefinition.get(sequence);
            if (anim_delay > 0 && seq.moveStyle == 0) {
                step_tracker++;
                return;
            }
            if (anim_delay <= 0 && seq.idleStyle == 0) {
                step_tracker++;
                return;
            }
        }
        int tempX = x;
        int tempY = y;
        int nextX = pathX[pathLength - 1] * 128 + size * 64;
        int nextY = pathY[pathLength - 1] * 128 + size * 64;
        if (nextX - tempX > 256 || nextX - tempX < -256 || nextY - tempY > 256 || nextY - tempY < -256) {
            x = nextX;
            y = nextY;
            return;
        }
        if (tempX < nextX) {
            if (tempY < nextY) {
                turn_direction = 1280;
            } else if (tempY > nextY) {
                turn_direction = 1792;
            } else {
                turn_direction = 1536;
            }
        } else if (tempX > nextX) {
            if (tempY < nextY) {
                turn_direction = 768;
            } else if (tempY > nextY) {
                turn_direction = 256;
            } else {
                turn_direction = 512;
            }
        } else if (tempY < nextY) {
            turn_direction = 1024;
        } else {
            turn_direction = 0;
        }

        int rotation = turn_direction - current_rotation & 0x7ff;

        if (rotation > 1024) {
            rotation -= 2048;
        }

        int animation = walkBackSequence;

        if (rotation >= -256 && rotation <= 256) {
            animation = walkSequence;
        } else if (rotation >= 256 && rotation < 768) {
            animation = walkRightSequence;
        } else if (rotation >= -768 && rotation <= -256) {
            animation = walkLeftSequence;
        }

        if (animation == -1) {
            animation = walkSequence;
        }

        movementSequence = animation;

        int positionDelta = 4;

        boolean smoothTurn = true;

        if (this instanceof Npc) {
            smoothTurn = ((Npc) this).definition.smoothWalk;
        }

        if (smoothTurn) {
            if (current_rotation != turn_direction && targetIndex == -1 && rotation != 0) {
                positionDelta = 2;
            }

            if (pathLength > 2) {
                positionDelta = 6;
            }

            if (pathLength > 3) {
                positionDelta = 8;
            }

            if (step_tracker > 0 && pathLength > 1) {
                positionDelta = 8;
                step_tracker--;
            }
        } else {
            if (pathLength > 1) {
                positionDelta = 6;
            }

            if (pathLength > 2) {
                positionDelta = 8;
            }

            if (step_tracker > 0 && pathLength > 1) {
                positionDelta = 8;
                --step_tracker;
            }
        }

        if (waypoint_movespeed[pathLength - 1]) {
            positionDelta <<= 1;
        }

        if (positionDelta >= 8 && movementSequence == walkSequence && runSequence != -1) {
            movementSequence = runSequence;
        }

        if (tempX < nextX) {
            x += positionDelta;
            if (x > nextX) {
                x = nextX;
            }
        } else if (tempX > nextX) {
            x -= positionDelta;
            if (x < nextX) {
                x = nextX;
            }
        }
        if (tempY < nextY) {
            y += positionDelta;
            if (y > nextY) {
                y = nextY;
            }
        } else if (tempY > nextY) {
            y -= positionDelta;

            if (y < nextY) {
                y = nextY;
            }
        }
        if (x == nextX && y == nextY) {
            pathLength--;

            if (anim_delay > 0) {
                anim_delay--;
            }
        }
    }

    public final void moveInDir(boolean run, int direction) {
        int x = pathX[0];
        int y = pathY[0];
        if (direction == 0) {
            x--;
            y++;
        }
        if (direction == 1)
            y++;
        if (direction == 2) {
            x++;
            y++;
        }
        if (direction == 3)
            x--;
        if (direction == 4)
            x++;
        if (direction == 5) {
            x--;
            y--;
        }
        if (direction == 6)
            y--;
        if (direction == 7) {
            x++;
            y--;
        }
        if (sequence != -1 && SequenceDefinition.get(sequence).idleStyle == 1)
            sequence = -1;
        if (pathLength < 9)
            pathLength++;
        for (int l = pathLength; l > 0; l--) {
            pathX[l] = pathX[l - 1];
            pathY[l] = pathY[l - 1];
            waypoint_movespeed[l] = waypoint_movespeed[l - 1];
        }
        pathX[0] = x;
        pathY[0] = y;
        waypoint_movespeed[0] = run;
    }

    public boolean visible() {
        return false;
    }

    public void readSpotAnimation(Buffer buffer) {
        int size = buffer.readNegUByte();

        for (int index = 0; index < size; ++index) {
            int pos = buffer.readNegUByte();
            int id = buffer.readLEUShort();
            int info = buffer.readInt();

            updateSpotAnimation(pos, id, info >> 16, info & 65535);
        }
    }

    public void updateSpotAnimation(int id, int height, int cycle, int frame) {
        int updatedFrame = frame + Client.tick;

        // Remove existing spot animation
        ActorSpotAnim existingSpotAnim = (ActorSpotAnim) this.spotAnimations.get((long) id);
        if (existingSpotAnim != null) {
            existingSpotAnim.remove();
            --this.graphicsCount;
        }

        // Check if height is valid and add new spot animation
        if (height != 65535 && height != -1) {
            byte flag = frame > 0 ? (byte) -1 : 0;
            this.spotAnimations.put(new ActorSpotAnim(height, cycle, updatedFrame, flag), (long) id);
            ++this.graphicsCount;
        }
    }

    public int secondaryanimReplaycount;

    public void updateAnimation() {
        isWalking = false;
        if (movementSequence != -1) {
            SequenceDefinition var2 = SequenceDefinition.get(movementSequence);
            if (var2 != null) {
                if (!var2.isCachedModelIdSet() && var2.primaryFrameIds != null) {
                    ++movementFrameCycle;
                    if (movementFrame < var2.primaryFrameIds.length && movementFrameCycle > var2.delays[movementFrame]) {
                        movementFrameCycle = 1;
                        ++movementFrame;
                        playAnimationSound(var2, movementFrame, x, y);
                    }

                    if (movementFrame >= var2.primaryFrameIds.length) {
                        if (var2.frameCount > 0) {
                            movementFrame -= var2.frameCount;
                            if (var2.replay) {
                                ++secondaryanimReplaycount;
                            }

                            if (movementFrame < 0 || movementFrame >= var2.primaryFrameIds.length || var2.replay && secondaryanimReplaycount >= var2.loopCount) {
                                movementFrameCycle = 0;
                                movementFrame = 0;
                                secondaryanimReplaycount = 0;
                            }
                        } else {
                            movementFrameCycle = 0;
                            movementFrame = 0;
                        }

                        playAnimationSound(var2, movementFrame, x, y);
                    }
                } else if (var2.isCachedModelIdSet()) {
                    ++movementFrame;
                    int var3 = var2.getSkeletalLength();
                    if (movementFrame < var3) {
                        playSkeletalSounds(var2, movementFrame, x, y);
                    } else {
                        if (var2.frameCount > 0) {
                            movementFrame -= var2.frameCount;
                            if (var2.replay) {
                                ++secondaryanimReplaycount;
                            }

                            if (movementFrame < 0 || movementFrame >= var3 || var2.replay && secondaryanimReplaycount >= var2.loopCount) {
                                movementFrame = 0;
                                movementFrameCycle = 0;
                                secondaryanimReplaycount = 0;
                            }
                        } else {
                            movementFrameCycle = 0;
                            movementFrame = 0;
                        }

                        playSkeletalSounds(var2, movementFrame, x, y);
                    }
                } else {
                    movementSequence = -1;
                }
            } else {
                movementSequence = -1;
            }
        }

        IterableNodeHashTableIterator var17 = new IterableNodeHashTableIterator(spotAnimations);

        for (ActorSpotAnim var14 = (ActorSpotAnim)var17.method2390(); var14 != null; var14 = (ActorSpotAnim)var17.next()) {
            if (var14.spotAnimation != -1 && Client.tick >= var14.cycle) {
                int var4 = SpotAnimation.lookup(var14.spotAnimation).sequence;
                if (var4 == -1) {
                    var14.remove();
                    --graphicsCount;
                } else {
                    var14.spotAnimationFrame = Math.max(var14.spotAnimationFrame, 0);
                    SequenceDefinition var15 = SequenceDefinition.get(var4);
                    if (var15.primaryFrameIds != null && !var15.isCachedModelIdSet()) {
                        ++var14.spotAnimationFrameCycle;
                        if (var14.spotAnimationFrame < var15.primaryFrameIds.length && var14.spotAnimationFrameCycle > var15.delays[var14.spotAnimationFrame]) {
                            var14.spotAnimationFrameCycle = 1;
                            ++var14.spotAnimationFrame;
                            playAnimationSound(var15, var14.spotAnimationFrame, x, y);
                        }

                        if (var14.spotAnimationFrame >= var15.primaryFrameIds.length) {
                            var14.remove();
                            --graphicsCount;
                        }
                    } else if (var15.isCachedModelIdSet()) {
                        ++var14.spotAnimationFrame;
                        int var12 = var15.getSkeletalLength();
                        if (var14.spotAnimationFrame < var12) {
                            playSkeletalSounds(var15, var14.spotAnimationFrame, x, y);
                        } else {
                            var14.remove();
                            --graphicsCount;
                        }
                    } else {
                        var14.remove();
                        --graphicsCount;
                    }
                }
            }
        }

        if (sequence != -1 && sequenceDelay <= 1) {
            SequenceDefinition sequenceDefinition_2 = SequenceDefinition.get(sequence);
            if (sequenceDefinition_2.moveStyle == 1 && anim_delay > 0 && initiate_movement  <= Client.tick && cease_movement < Client.tick) {
                sequenceDelay = 1;
                return;
            }
        }

        if (sequence != -1 && sequenceDelay == 0) {
            SequenceDefinition var2 = SequenceDefinition.get(sequence);
            if (var2 == null) {
                sequence = -1;
            } else if (!var2.isCachedModelIdSet() && var2.primaryFrameIds != null) {
                ++sequenceFrameCycle;
                if (sequenceFrame < var2.primaryFrameIds.length && sequenceFrameCycle > var2.delays[sequenceFrame]) {
                    sequenceFrameCycle = 1;
                    ++sequenceFrame;
                    playAnimationSound(var2, sequenceFrame, x, y);
                }

                if (sequenceFrame >= var2.primaryFrameIds.length) {
                    sequenceFrame -= var2.frameCount;
                    ++currentAnimationLoops;
                    if (currentAnimationLoops >= var2.loopCount) {
                        sequence = -1;
                    } else if (sequenceFrame >= 0 && sequenceFrame < var2.primaryFrameIds.length) {
                        playAnimationSound(var2, sequenceFrame, x, y);
                    } else {
                        sequence = -1;
                    }
                }

                isWalking = var2.stretches;
            } else if (var2.isCachedModelIdSet()) {
                ++sequenceFrame;
                int var3 = var2.getSkeletalLength();
                if (sequenceFrame < var3) {
                    playSkeletalSounds(var2, sequenceFrame, x, y);
                } else {
                    sequenceFrame -= var2.frameCount;
                    ++sequenceFrame;
                    if (sequenceFrame >= var2.loopCount) {
                        sequence = -1;
                    } else if (sequenceFrame >= 0 && sequenceFrame < var3) {
                        playSkeletalSounds(var2, sequenceFrame, x, y);
                    } else {
                        sequence = -1;
                    }
                }
            } else {
                sequence = -1;
            }
        }

        if (sequenceDelay > 0)
            sequenceDelay--;
    }

    void playAnimationSound(SequenceDefinition var0, int var1, int var2, int var3) {
        StaticSound.playAnimationSound(var0,var1,var2,var3,this);
    }

    void playSkeletalSounds(SequenceDefinition var0, int var1, int var2, int var3) {
        StaticSound.playSkeletalSounds(var0,var1,var2,var3,this);
    }

    @Override
    public int getCombatLevel() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isInteracting()
    {
        return getRSInteracting() != -1;
    }


    @Override
    public int getHealthRatio()
    {
        return -1;
    }
    @Override
    public int getHealthScale()
    {
        return -1;
    }

    @Override
    public WorldPoint getWorldLocation()
    {
        return WorldPoint.fromLocal(Client.instance,
                this.getPathX()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
                this.getPathY()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
                Client.instance.getPlane());
    }


    @Override
    public LocalPoint getLocalLocation()
    {
        return new LocalPoint(getX(), getY());
    }

    @Override
    public void setIdleRotateLeft(int animationID) {
        turnLeftSequence = animationID;
    }

    @Override
    public void setIdleRotateRight(int animationID) {
        turnRightSequence = animationID;
    }

    @Override
    public void setWalkAnimation(int animationID) {
        walkLeftSequence = animationID;
    }

    @Override
    public void setWalkRotateLeft(int animationID) {
        walkLeftSequence = animationID;
    }

    @Override
    public void setWalkRotateRight(int animationID) {
        walkRightSequence = animationID;
    }

    @Override
    public void setWalkRotate180(int animationID) {
        walkBackSequence = animationID;
    }

    @Override
    public void setRunAnimation(int animationID) {
        runSequence = animationID;
    }


    @Override
    public Polygon getCanvasTilePoly()
    {
        return Perspective.getCanvasTilePoly(Client.instance, getLocalLocation());
    }

    @Override
    public net.runelite.api.Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset)
    {
        return Perspective.getCanvasTextLocation(Client.instance, graphics, getLocalLocation(), text, zOffset);
    }

    @Override
    public net.runelite.api.Point getCanvasImageLocation(BufferedImage image, int zOffset)
    {
        return Perspective.getCanvasImageLocation(Client.instance, getLocalLocation(), image, zOffset);
    }

    @Override
    public net.runelite.api.Point getCanvasSpriteLocation(SpritePixels spritePixels, int zOffset)
    {
        return Perspective.getCanvasSpriteLocation(Client.instance, getLocalLocation(), spritePixels, zOffset);
    }
    @Override
    public Point getMinimapLocation()
    {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }

    @Override
    public int getRSInteracting() {
        return targetIndex;
    }

    @Override
    public String getOverheadText() {
        return spokenText;
    }

    @Override
    public void setOverheadText(String overheadText) {
        this.spokenText = overheadText;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int[] getPathX() {
        return pathX;
    }

    @Override
    public int[] getPathY() {
        return pathY;
    }

    @Override
    public int getRSAnimation() {
        return sequence;
    }

    @Override
    public void setAnimation(int animation) {
        sequence = animation;
    }

    @Override
    public int getAnimationFrame() {
        return sequence;
    }

    @Override
    public int getActionFrame() {
        return sequenceFrame;
    }

    @Override
    public void setAnimationFrame(int frame) {
        sequenceFrame = frame;
    }

    @Override
    public void setActionFrame(int frame) {
        sequenceFrameCycle = frame;
    }

    @Override
    public int getActionFrameCycle() {
        return sequenceFrameCycle;
    }

    @Override
    public IterableNodeHashTable getSpotAnims() {
        return spotAnimations;
    }

    @Override
    public ActorSpotAnim newActorSpotAnim(int id, int height, int delay, int frame) {
        return new ActorSpotAnim(id,height,delay,frame);
    }

    @Override
    public int getGraphicsCount() {
        return graphicsCount;
    }

    @Override
    public void setGraphicsCount(int count) {
        graphicsCount = count;
    }

    @Override
    public int getIdlePoseAnimation() {
        return idleSequence;
    }

    @Override
    public void setIdlePoseAnimation(int animation) {
        idleSequence = animation;
    }

    @Override
    public int getPoseAnimation() {
        return movementSequence;
    }

    @Override
    public void setPoseAnimation(int animation) {
        movementSequence = animation;
    }

    @Override
    public int getPoseAnimationFrame() {
        return movementFrame;
    }

    @Override
    public void setPoseAnimationFrame(int frame) {
        movementFrame = frame;
    }

    @Override
    public int getPoseFrame() {
        return movementFrame;
    }

    @Override
    public void setPoseFrame(int frame) {
        movementFrame = frame;
    }

    @Override
    public int getPoseFrameCycle() {
        return movementFrameCycle;
    }

    @Override
    public int getLogicalHeight() {
        return defaultHeight;
    }

    @Override
    public Shape getConvexHull() {
        RSModel model = getModel();
        if (model == null)
        {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());

        return model.getConvexHull(getX(), getY(), getOrientation(), tileHeight);
    }

    @Override
    public int getOrientation() {
        return current_rotation;
    }

    @Override
    public int getCurrentOrientation() {
        return rotation;
    }

    @Override
    public IterableNodeDeque getHealthBars() {
        return null;
    }

    @Override
    public int[] getHitsplatValues() {
        return null;
    }

    @Override
    public int[] getHitsplatTypes() {
        return null;
    }

    @Override
    public int[] getHitsplatCycles() {
        return null;
    }

    @Override
    public int getIdleRotateLeft() {
        return turnLeftSequence;
    }

    @Override
    public int getIdleRotateRight() {
        return turnRightSequence;
    }

    @Override
    public int getWalkAnimation() {
        return turnLeftSequence;
    }

    @Override
    public int getWalkRotate180() {
        return walkBackSequence;
    }

    @Override
    public int getWalkRotateLeft() {
        return walkLeftSequence;
    }

    @Override
    public int getWalkRotateRight() {
        return walkRightSequence;
    }

    @Override
    public int getRunAnimation() {
        return runSequence;
    }

    @Override
    public int getPathLength() {
        return pathLength;
    }

    @Override
    public int getOverheadCycle() {
        return 0;
    }

    @Override
    public void setOverheadCycle(int cycle) {

    }

    @Override
    public net.runelite.api.Actor getInteracting()
    {
        try
        {
            int index = getRSInteracting();
            if (index == -1 || index == 65535 || index == 16777215)
            {
                return null;
            }

            int var2 = 65536;
            if (index < var2)
            {
                NPC[] npcs = Client.instance.getCachedNPCs();
                return npcs[index];
            }

            index -= var2;
            Player[] players = Client.instance.getCachedPlayers();
            return players[index];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            Client.instance.getLogger().error("", e);
            return null;
        }
    }

    public void animationChanged(int idx)
    {
        AnimationChanged animationChange = new AnimationChanged();
        animationChange.setActor(this);
        Client.instance.getCallbacks().post(animationChange);
    }

    public void onGraphicCleared()
    {
        GraphicChanged graphicChanged = new GraphicChanged();
        graphicChanged.setActor(this);
        Client.instance.getCallbacks().post(graphicChanged);
    }

    public void onGraphicChanged(int idx, int graphicID, int graphicHeight, int graphicStartCycle)
    {
        GraphicChanged graphicChanged = new GraphicChanged();
        graphicChanged.setActor(this);
        Client.instance.getCallbacks().post(graphicChanged);
    }

    public void createSpotAnim(int id, int spotAnimId, int height, int delay)
    {
        IterableHashTable<ActorSpotAnim> spotAnims = this.getSpotAnims();
        ActorSpotAnim actorSpotAnim = (ActorSpotAnim) spotAnims.get((long) id);
        if (actorSpotAnim != null)
        {
            actorSpotAnim.unlink();
            this.setGraphicsCount(getGraphicsCount() - 1);
        }

        if (spotAnimId != -1)
        {
            byte frame = 0;
            if (delay > 0)
            {
                frame = -1;
            }

            spotAnims.put(newActorSpotAnim(spotAnimId, height, Client.instance.getGameCycle() + delay, frame), (long) id);
            this.setGraphicsCount(getGraphicsCount() + 1);
        }
    }

    public boolean hasSpotAnim(int spotAnimId)
    {
        Iterator<ActorSpotAnim> iter = this.getSpotAnims().iterator();
        while (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            if (actorSpotAnim.getId() == spotAnimId)
            {
                return true;
            }
        }
        return false;
    }

    public void removeSpotAnim(int id)
    {
        ActorSpotAnim actorSpotAnim = (ActorSpotAnim) this.getSpotAnims().get(id);
        if (actorSpotAnim != null)
        {
            actorSpotAnim.unlink();
            this.setGraphicsCount(getGraphicsCount() - 1);
        }
    }

    public void clearSpotAnims()
    {
        this.getSpotAnims().clear();
        this.setGraphicsCount(0);
    }

    public void interactingChanged(int idx)
    {
        InteractingChanged interactingChanged = new InteractingChanged(this, getInteracting());
        Client.instance.getCallbacks().post(interactingChanged);
    }

    public void overheadTextChanged(int idx)
    {
        String overheadText = getOverheadText();
        if (overheadText != null)
        {
            OverheadTextChanged overheadTextChanged = new OverheadTextChanged(this, overheadText);
            Client.instance.getCallbacks().post(overheadTextChanged);
        }
    }

    private boolean dead;
    private static final Set<Integer> combatInfoFilter = ImmutableSet.of(0, 2, 16, 17, 18, 19, 20, 21, 22);

    @Override
    public boolean isDead()
    {
        return dead;
    }

    @Override
    public void setDead(boolean dead)
    {
        this.dead = dead;
    }

    public void setCombatInfo(int combatInfoId, int gameCycle, int var3, int var4, int healthRatio, int health)
    {
        final HealthBarUpdated event = new HealthBarUpdated(this, healthRatio);
        Client.instance.getCallbacks().post(event);

        if (healthRatio == 0)
        {
            if (isDead())
            {
                return;
            }

            if (!combatInfoFilter.contains(combatInfoId))
            {
                return;
            }

            setDead(true);

            final ActorDeath actorDeath = new ActorDeath(this);
            Client.instance.getCallbacks().post(actorDeath);
        }
        else if (healthRatio > 0)
        {
            if (this instanceof RSNPC && ((RSNPC) this).getId() == NpcID.CORPOREAL_BEAST && isDead())
            {
                return;
            }

            setDead(false);
        }
    }


    public void applyActorHitsplat(int type, int value, int var3, int var4, int gameCycle, int duration)
    {

    }


    @Override
    public boolean isMoving()
    {
        return getPathLength() > 0;
    }

    @Override
    public boolean isAnimating() {
        return RSActor.super.isAnimating();
    }

    @Override
    public int getGraphic()
    {
        Iterator iter = this.getSpotAnims().iterator();
        if (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            return actorSpotAnim.getId();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public void setGraphic(int id)
    {
        if (id == -1)
        {
            this.getSpotAnims().clear();
            this.setGraphicsCount(0);
        }
        else
        {
            Iterator iter = this.getSpotAnims().iterator();
            if (iter.hasNext())
            {
                ActorSpotAnim var3 = (ActorSpotAnim) iter.next();
                var3.setId(id);
            }
            else
            {
                RSActorSpotAnim actorSpotAnim = this.newActorSpotAnim(id, 0, 0, 0);
                this.getSpotAnims().put(actorSpotAnim, 0L);
                this.setGraphicsCount(getGraphicsCount() + 1);
            }
        }
    }

    @Override
    public int getGraphicHeight()
    {
        Iterator iter = this.getSpotAnims().iterator();
        if (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            return actorSpotAnim.getHeight();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void setGraphicHeight(int height)
    {
        Iterator iter = this.getSpotAnims().iterator();
        if (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            actorSpotAnim.setHeight(height);
        }
    }

    @Override
    public int getSpotAnimFrame()
    {
        Iterator iter = this.getSpotAnims().iterator();
        if (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            return actorSpotAnim.getFrame();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void setSpotAnimFrame(int id)
    {
        Iterator iter = this.getSpotAnims().iterator();
        if (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            actorSpotAnim.setFrame(id);
        }
    }

    @Override
    public int getSpotAnimationFrameCycle()
    {
        Iterator iter = this.getSpotAnims().iterator();
        if (iter.hasNext())
        {
            ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
            return actorSpotAnim.getCycle();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getAnimation()
    {
        int animation = getRSAnimation();
        switch (animation)
        {
			/*case 7592:
			case 7593:
			case 7949:
			case 7950:
			case 7951:
			case 7952:
			case 7957:
			case 7960:
			case 8059:
			case 8123:
			case 8124:
			case 8125:
			case 8126:
			case 8127:
			case 8234:
			case 8235:
			case 8236:
			case 8237:
			case 8238:
			case 8241:
			case 8242:
			case 8243:
			case 8244:
			case 8245:
			case 8248:
			case 8249:
			case 8250:
			case 8251:
			case 8252:
			case 8255:
			case 8256:
			case 8257:
			case 8258:
				return -1;*/
            default:
                return animation;
        }
    }

    public WorldArea getWorldArea()
    {
        int size = 1;
        if (this instanceof NPC)
        {
            NPCComposition composition = ((NPC) this).getComposition();
            if (composition != null && composition.getConfigs() != null)
            {
                composition = composition.transform();
            }
            if (composition != null)
            {
                size = composition.getSize();
            }
        }

        return new WorldArea(this.getWorldLocation(), size, size);
    }


}
