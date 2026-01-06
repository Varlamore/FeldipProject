package com.cryptic.entity;

import com.cryptic.Client;
import com.cryptic.cache.def.NpcDefinition;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.entity.model.Model;
import com.cryptic.cache.def.anim.SequenceDefinition;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.rs.api.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import static com.cryptic.Client.instance;
import static com.cryptic.Client.tick;

public final class Npc extends Entity implements RSNPC {

    public NpcDefinition definition;
    public int headIcon = -1;
    public int ownerIndex = -1;

    public boolean showActions() {
        if (ownerIndex == -1) {
            return true;
        }
        return (instance.localPlayerIndex == ownerIndex);
    }

    public int getHeadIcon() {
        if (headIcon == -1) {
            if (definition != null && definition.getHeadIconSpriteIndex() != null)
                return definition.getHeadIconSpriteIndex()[0];
            if (definition != null) {
                return definition.getDefaultHeadIconArchive();
            }
        }
        return headIcon;
    }


    public Model get_rotated_model() { //gradle slow as shit
        if (!instance.isInterpolateNpcAnimations()
                || this.getAnimation() == AnimationID.HELLHOUND_DEFENCE
                || this.getAnimation() == 8270
                || this.getAnimation() == 8271
                || this.getPoseAnimation() == 5583
                || this.getId() == NpcID.WYRM && this.getAnimation() == AnimationID.IDLE
                || this.getId() == NpcID.TREE_SPIRIT && this.getAnimation() == AnimationID.IDLE
                || this.getId() == NpcID.TREE_SPIRIT_6380 && this.getAnimation() == AnimationID.IDLE
                || this.getId() == NpcID.TREE_SPIRIT_HARD && this.getAnimation() == AnimationID.IDLE
        )
        {
            return getModelVanilla();
        }
        int actionFrame = getActionFrame();
        int poseFrame = getPoseFrame();
        int spotAnimFrame = getSpotAnimFrame();
        try
        {
            // combine the frames with the frame cycle so we can access this information in the sequence methods
            // without having to change method calls
            setActionFrame(Integer.MIN_VALUE | getActionFrameCycle() << 16 | actionFrame);
            setPoseFrame(Integer.MIN_VALUE | getPoseFrameCycle() << 16 | poseFrame);
            setSpotAnimFrame(Integer.MIN_VALUE | getSpotAnimationFrameCycle() << 16 | spotAnimFrame);
            Iterator iter = getSpotAnims().iterator();
            while (iter.hasNext())
            {
                net.runelite.api.ActorSpotAnim actorSpotAnim = (net.runelite.api.ActorSpotAnim) iter.next();
                int frame = actorSpotAnim.getFrame();
                if (frame != -1)
                {
                    actorSpotAnim.setFrame(Integer.MIN_VALUE | actorSpotAnim.getCycle() << 16 | frame);
                }
            }
            return getModelVanilla();
        }
        finally
        {
            // reset frames
            setActionFrame(actionFrame);
            setPoseFrame(poseFrame);
            setSpotAnimFrame(spotAnimFrame);
            Iterator iter = getSpotAnims().iterator();
            while (iter.hasNext())
            {
                net.runelite.api.ActorSpotAnim actorSpotAnim = (net.runelite.api.ActorSpotAnim) iter.next();
                int frame = actorSpotAnim.getFrame();
                if (frame != -1)
                {
                    actorSpotAnim.setFrame(frame & '\uFFFF');
                }
            }
        }
    }

    public Model getModelVanilla() {
        if (this.definition == null) {
            return null;
        } else {
            SequenceDefinition sequenceDefinition = super.sequence != -1 && super.sequenceDelay == 0 ? SequenceDefinition.get(super.sequence) : null;
            SequenceDefinition walkSequenceDefinition = super.movementSequence == -1 || super.idleSequence == super.movementSequence && sequenceDefinition != null ? null : SequenceDefinition.get(super.movementSequence);
            Model animatedModel = this.definition.get_animated_model(sequenceDefinition, super.sequenceFrame, walkSequenceDefinition, super.movementFrame);
            if (animatedModel == null) {
                return null;
            } else {
                animatedModel.calculateBoundsCylinder();
                super.defaultHeight = animatedModel.model_height;
                int indicesCount = animatedModel.indicesCount;
                animatedModel = createSpotAnimModel(animatedModel);

                if (this.definition.size == 1) {
                    animatedModel.singleTile = true;
                }



                if (super.recolourAmount != 0 && tick >= super.recolourStartCycle && tick < super.recolourEndCycle) {
                    animatedModel.overrideHue = super.recolorHue;
                    animatedModel.overrideSaturation = super.recolourSaturation;
                    animatedModel.overrideLuminance = super.recolourLuminance;
                    animatedModel.overrideAmount = super.recolourAmount;
                    animatedModel.field2196 = (short)indicesCount;
                } else {
                    animatedModel.overrideAmount = 0;
                }

                return animatedModel;
            }
        }
    }

    public boolean visible() {
        return definition != null;
    }

    @Override
    public int getId() {
        return definition.id;
    }

    @Override
    public NPCComposition getTransformedComposition() {
        return null;
    }

    @Override
    public void onDefinitionChanged(NPCComposition composition) {
        if (composition != null)
        {
            composition.setIndex(getIndex());
        }

        if (composition == null)
        {
            instance.getCallbacks().post(new NpcDespawned(this));
        }
        else if (this.getTransformedId() != -1)
        {
            RSNPCComposition oldComposition = getComposition();
            if (oldComposition == null)
            {
                return;
            }

            if (composition.getId() == oldComposition.getId())
            {
                return;
            }

            setDead(false);
            instance.getCallbacks().postDeferred(new NpcChanged(this, oldComposition));
        }
    }

    @Override
    public RSNPCComposition getComposition() {
        return definition;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int id) {
        index = id;
    }

    @Override
    public int getCombatLevel() {
        return definition.combatLevel;
    }

    @Override
    public String getName() {
        return definition.name;
    }

    public Polygon getCanvasTilePoly()
    {
        NPCComposition transformedComposition = this.getTransformedComposition();
        if (transformedComposition == null)
        {
            return null;
        }
        else
        {
            int size = transformedComposition.getSize();
            return Perspective.getCanvasTileAreaPoly(instance, this.getLocalLocation(), size);
        }
    }

    public int getTransformedLevel()
    {
        RSNPCComposition composition = getComposition();
        if (composition != null && composition.getConfigs() != null)
        {
            composition = composition.transform();
        }
        return composition == null ? -1 : composition.getCombatLevel();
    }

    public int getTransformedId()
    {
        RSNPCComposition composition = getComposition();
        if (composition != null && composition.getConfigs() != null)
        {
            composition = composition.transform();
        }
        return composition == null ? -1 : composition.getId();
    }

    public String getTransformedName()
    {
        RSNPCComposition composition = getComposition();
        if (composition != null && composition.getConfigs() != null)
        {
            composition = composition.transform();
        }
        return composition == null ? null : composition.getName().replace('\u00A0', ' ');
    }


    public Shape getConvexHull()
    {
        RSModel model = getModel();
        if (model == null)
        {
            return null;
        }

        int size = getComposition().getSize();
        LocalPoint tileHeightPoint = new LocalPoint(
                size * Perspective.LOCAL_HALF_TILE_SIZE - Perspective.LOCAL_HALF_TILE_SIZE + getX(),
                size * Perspective.LOCAL_HALF_TILE_SIZE - Perspective.LOCAL_HALF_TILE_SIZE + getY());

        int tileHeight = Perspective.getTileHeight(instance, tileHeightPoint, instance.getPlane());

        return model.getConvexHull(getX(), getY(), getOrientation(), tileHeight);
    }


}
