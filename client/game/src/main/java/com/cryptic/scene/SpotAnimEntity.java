package com.cryptic.scene;

import com.cryptic.Client;
import com.cryptic.cache.def.anim.SequenceDefinition;
import com.cryptic.cache.def.anim.SpotAnimation;
import com.cryptic.entity.Renderable;
import com.cryptic.entity.model.Model;
import net.runelite.api.Animation;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.rs.api.RSGraphicsObject;
import net.runelite.rs.api.RSSequenceDefinition;

import javax.annotation.Nullable;


public final class SpotAnimEntity extends Renderable implements RSGraphicsObject {

    public final int z;
    public final int x;
    public final int y;
    public final int height;
    public final int cycleStart;
    public boolean expired;

    private int frame;

    private int id;
    public int frameCycle;

    SequenceDefinition graphics;

    public SpotAnimEntity(int z, int cycleStart, int offset, int id, int height, int y, int x) {
        this.frame = 0;
        this.frameCycle = 0;
        this.z = z;
        this.x = x;
        this.y = y;
        this.id = id;
        this.height = height;
        this.cycleStart = cycleStart + offset;
        expired = false;
        int sequence = SpotAnimation.lookup(id).sequence;
        if (sequence != -1) {
            this.expired = false;
            this.graphics = SequenceDefinition.get(sequence);
        } else {
            this.expired = true;
        }
        final GraphicsObjectCreated event = new GraphicsObjectCreated(this);
        Client.instance.getCallbacks().post(event);
    }

    public Model get_rotated_model() {
        SpotAnimation var1 = SpotAnimation.lookup(this.id);
        Model var2;
        if (!this.expired) {
            var2 = var1.getModel(this.frame);
        } else {
            var2 = var1.getModel(-1);
        }

        return var2 == null ? null : var2;
    }

    public void step(int cycle) {
        if (!this.expired) {
            this.frameCycle += cycle;
            if (!this.graphics.isCachedModelIdSet()) {
                while (this.frameCycle > this.graphics.delays[this.frame]) {
                    this.frameCycle -= this.graphics.delays[this.frame];
                    ++this.frame;
                    if (this.frame >= this.graphics.primaryFrameIds.length) {
                        this.expired = true;
                        break;
                    }
                }
            } else {
                this.frame += cycle;
                if (this.frame >= this.graphics.getSkeletalLength()) {
                    this.expired = true;
                }
            }

        }

    }

    @Override
    public LocalPoint getLocation() {
        return null;
    }

    @Nullable
    @Override
    public Animation getAnimation() {
        return null;
    }

    @Override
    public int getAnimationFrame() {
        return 0;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int id) {

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getStartCycle() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public int getFrame() {
        return 0;
    }

    @Override
    public void setFrame(int frame) {

    }

    @Override
    public int getFrameCycle() {
        return 0;
    }

    @Override
    public void setFrameCycle(int frameCycle) {

    }

    @Override
    public void setFinished(boolean finished) {

    }

    @Override
    public void setLevel(int level) {

    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void setY(int y) {

    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public RSSequenceDefinition getSequenceDefinition() {
        return null;
    }

    @Override
    public void setSequenceDefinition(RSSequenceDefinition sequenceDefinition) {

    }
}
