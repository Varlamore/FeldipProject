package com.cryptic.entity;

import com.cryptic.Client;
import com.cryptic.cache.def.*;
import com.cryptic.collection.EvictingDualNodeHashTable;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.entity.model.IdentityKit;
import com.cryptic.entity.model.Mesh;
import com.cryptic.cache.def.anim.SequenceDefinition;
import com.cryptic.entity.model.Model;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.kit.KitType;
import net.runelite.rs.api.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import static net.runelite.api.SkullIcon.*;

public final class Player extends Entity implements RSPlayer {

    public Model get_rotated_model() {
        if (!Client.instance.isInterpolatePlayerAnimations() || this.getPoseAnimation() == 244)
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
                ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
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
                ActorSpotAnim actorSpotAnim = (ActorSpotAnim) iter.next();
                int frame = actorSpotAnim.getFrame();
                if (frame != -1)
                {
                    actorSpotAnim.setFrame(frame & '\uFFFF');
                }
            }
        }
    }

    public Model getModelVanilla() {

        if (!this.visible) {
            return null;
        } else {
            SequenceDefinition sequenceDefinition = super.sequence != -1 && super.sequenceDelay == 0 ? SequenceDefinition.get(super.sequence) : null;
            SequenceDefinition walkSequenceDefinition = super.movementSequence == -1 || this.isUnanimated || super.movementSequence == super.idleSequence && sequenceDefinition != null ? null : SequenceDefinition.get(super.movementSequence);
            Model animatedModel = get_animated_model(sequenceDefinition, super.sequenceFrame, walkSequenceDefinition, super.movementFrame);
            if (animatedModel == null) {
                return null;
            } else {
                animatedModel.calculateBoundsCylinder();
                super.defaultHeight = animatedModel.model_height;
                int indicesCount = animatedModel.indicesCount;
                Model model;
                Model[] models;
                if (!this.isUnanimated) {
                    animatedModel = this.createSpotAnimModel(animatedModel);
                }

                if (!this.isUnanimated && this.playerModel != null) {
                    if (Client.tick >= this.animationCycleEnd) {
                        this.playerModel = null;
                    }

                    if (Client.tick >= this.animationCycleStart && Client.tick < this.animationCycleEnd) {
                        model = this.playerModel;
                        model.offsetBy(this.field1117 * 4096 - super.x, this.tileHeight2 - this.tileHeight, this.field1123 * 4096 - super.y);
                        if (super.turn_direction == 512) {
                            model.rotateY90Ccw();
                            model.rotateY90Ccw();
                            model.rotateY90Ccw();
                        } else if (super.turn_direction == 1024) {
                            model.rotateY90Ccw();
                            model.rotateY90Ccw();
                        } else if (super.turn_direction == 1536) {
                            model.rotateY90Ccw();
                        }

                        models = new Model[]{animatedModel, model};
                        animatedModel = new Model(models, 2);
                        if (super.turn_direction == 512) {
                            model.rotateY90Ccw();
                        } else if (super.turn_direction == 1024) {
                            model.rotateY90Ccw();
                            model.rotateY90Ccw();
                        } else if (super.turn_direction == 1536) {
                            model.rotateY90Ccw();
                            model.rotateY90Ccw();
                            model.rotateY90Ccw();
                        }

                        model.offsetBy(super.x - this.field1117 * 4096, this.tileHeight - this.tileHeight2, super.y - this.field1123 * 4096);
                    }
                }

                animatedModel.singleTile = true;
                if (super.recolourAmount != 0 && Client.tick >= super.recolourStartCycle && Client.tick < super.recolourEndCycle) {
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

    public void update(Buffer buffer) {
        buffer.pos = 0;
        title = buffer.readString();
        titleColor = buffer.readString();
        gender = buffer.readUnsignedByte();
        overhead_icon = buffer.readUnsignedByte();
        skull_icon = buffer.readUnsignedByte();
        hint_arrow_icon = buffer.readUnsignedByte();
        desc = null;
        team_id = 0;
        for (int bodyPart = 0; bodyPart < 12; bodyPart++) {
            int reset = buffer.readUnsignedByte();
            if (reset == 0) {
                player_appearance[bodyPart] = 0;
            } else {

                int id = buffer.readUnsignedByte();

                this.player_appearance[bodyPart] = (reset << 8) + id;

                if (bodyPart == 0 && this.player_appearance[0] == 65535) {
                    desc = NpcDefinition.get(buffer.readUnsignedShort());
                    break;
                }

                if (this.player_appearance[bodyPart] >= 512 && this.player_appearance[bodyPart] - 512 < Js5List.getConfigSize(Js5ConfigType.ITEM)) {
                    int team_cape = ItemDefinition.get(this.player_appearance[bodyPart] - 512).team;
                    if (team_cape != 0) {
                        this.team_id = team_cape;
                    }
                }
            }
        }

        for (int index = 0; index < 5; index++) {
            int color = buffer.readUnsignedByte();
            if (color < 0 || color >= Client.APPEARANCE_COLORS[index].length) {
                color = 0;
            }
            appearanceColors[index] = color;
        }

        super.idleSequence = buffer.readUnsignedShort();
        if (super.idleSequence == 65535) {
            super.idleSequence = -1;
        }

        super.turnLeftSequence = buffer.readUnsignedShort();
        if (super.turnLeftSequence == 65535) {
            super.turnLeftSequence = -1;
        }

        super.turnRightSequence = super.turnLeftSequence;
        super.walkSequence = buffer.readUnsignedShort();
        if (super.walkSequence == 65535) {
            super.walkSequence = -1;
        }

        super.walkBackSequence = buffer.readUnsignedShort();
        if (super.walkBackSequence == 65535) {
            super.walkBackSequence = -1;
        }

        super.walkLeftSequence = buffer.readUnsignedShort();
        if (super.walkLeftSequence == 65535) {
            super.walkLeftSequence = -1;
        }

        super.walkRightSequence = buffer.readUnsignedShort();
        if (super.walkRightSequence == 65535) {
            super.walkRightSequence = -1;
        }

        super.runSequence = buffer.readUnsignedShort();
        if (super.runSequence == 65535) {
            super.runSequence = -1;
        }

        username = buffer.readString();
        combat_level = buffer.readUnsignedByte();
        rights = buffer.readUnsignedByte();
        donatorRights = buffer.readUnsignedByte();
        ironmanRights = buffer.readUnsignedByte();

        visible = true;
        appearance_offset = 0L;

        for (int index = 0; index < 12; index++) {
            appearance_offset <<= 4;
            if (player_appearance[index] >= 256) {
                appearance_offset += player_appearance[index] - 256;
            }
        }

        if (player_appearance[0] >= 256) {
            appearance_offset += player_appearance[0] - 256 >> 4;
        }

        if (player_appearance[1] >= 256) {
            appearance_offset += player_appearance[1] - 256 >> 8;
        }

        for (int index = 0; index < 5; index++) {
            appearance_offset <<= 3;
            appearance_offset += appearanceColors[index];
        }

        appearance_offset <<= 1;
        appearance_offset += gender;

    }

    public Model get_animated_model() {
        SequenceDefinition sequenceDefinition = super.sequence != -1 && super.sequenceDelay == 0 ? SequenceDefinition.get(super.sequence) : null;
        SequenceDefinition walkSequenceDefinition = (super.movementSequence == -1 || (super.movementSequence == super.idleSequence && sequenceDefinition != null)) ? null : SequenceDefinition.get(super.movementSequence);
        return get_animated_model(sequenceDefinition, super.sequenceFrame ,walkSequenceDefinition,super.movementFrame);
    }


    public Model get_animated_model(SequenceDefinition var1, int var2, SequenceDefinition var3, int var4) {

        if (desc != null) {
            Model model = desc.get_animated_model(var1, var2, var3, var4);
            return model;
        }

        long uid = appearance_offset;


        int[] equipment = this.player_appearance;
        if (var1 != null && (var1.leftHandItem >= 0 || var1.rightHandItem >= 0)) {
            equipment = new int[12];

            for(int i = 0; i < 12; ++i) {
                equipment[i] = this.player_appearance[i];
            }

            if (var1.leftHandItem >= 0) {
                uid += (long)(var1.leftHandItem - this.player_appearance[5] << 40);
                equipment[5] = var1.leftHandItem;
            }

            if (var1.rightHandItem >= 0) {
                uid += (long)(var1.rightHandItem - this.player_appearance[3] << 48);
                equipment[3] = var1.rightHandItem;
            }
        }
        Model model_1 = (Model) model_cache.get(uid);
        if (model_1 == null) {
            boolean modelsInvalid = false;
            for (int wearpos = 0; wearpos < 12; wearpos++) {
                int wear = equipment[wearpos];
                if (wear >= 0x100 && wear < 512 && !IdentityKit.lookup(wear - 0x100).body_cached()) {
                    modelsInvalid = true;
                }
                if (wear >= 0x200 && !ItemDefinition.get(wear - 0x200).equipped_model_cached(gender)) {
                    modelsInvalid = true;
                }
            }

            if (modelsInvalid) {
                if (key != -1L)
                    model_1 = (Model) model_cache.get(key);
                if (model_1 == null)
                    return null;
            }
        }
        if (model_1 == null) {
            Mesh kitMeshes[] = new Mesh[12];
            int worn = 0;
            for (int slot = 0; slot < 12; slot++) {
                int part = equipment[slot];
                if (part >= 256 && part < 512) {
                    Mesh model_3 = IdentityKit.lookup(part - 256).get_body();
                    if (model_3 != null)
                        kitMeshes[worn++] = model_3;
                }
                if (part >= 512) {
                    ItemDefinition definition = ItemDefinition.get(part - 0x200);
                    Mesh itemModel = definition.get_equipped_model(this.gender);
                    if (itemModel != null) {
                        kitMeshes[worn++] = itemModel;
                    }
                }
            }


            Mesh var20 = new Mesh(kitMeshes, worn);

            for (int index = 0; index < 5; index++) {
                if (appearanceColors[index] != 0) {
                    var20.recolor((short) Client.APPEARANCE_COLORS[index][0], (short) Client.APPEARANCE_COLORS[index][appearanceColors[index]]);
                    if (index == 1)
                        var20.recolor((short) Client.SHIRT_SECONDARY_COLORS[0], (short) Client.SHIRT_SECONDARY_COLORS[appearanceColors[index]]);

                }
            }


            model_1 = var20.toModel(64, 850, -30, -50, -30);
            model_cache.put(model_1, uid);
            key = uid;
        }

        Model animatedModel;
        if (var1 == null && var3 == null) {
            animatedModel = model_1.toSharedSequenceModel(true);
        } else if (var1 != null && var3 != null) {
            animatedModel = var1.applyTransformations(model_1, var2, var3, var4);
        } else if (var1 != null) {
            animatedModel = var1.transformActorModel(model_1, var2);
        } else {
            animatedModel = var3.transformActorModel(model_1, var4);
        }

        return animatedModel;

    }




    public Mesh get_dialogue_model() {
        if (!visible) {
            return null;
        }

        if (desc != null) {
            return desc.get_dialogue_model();
        }

        boolean cached = false;

        for (int index = 0; index < 12; index++) {
            int appearanceId = player_appearance[index];

            if (appearanceId >= 256 && appearanceId < 512 && !IdentityKit.lookup(appearanceId - 256).headLoaded()) {
                cached = true;
            }

            if (appearanceId >= 512 && !ItemDefinition.get(appearanceId - 512).dialogue_model_cached(gender)) {
                cached = true;
            }
        }

        if (cached) {
            return null;
        }

        Mesh headModels[] = new Mesh[12];

        int headModelsOffset  = 0;

        for (int modelIndex  = 0; modelIndex  < 12; modelIndex ++) {
            int appearanceId  = player_appearance[modelIndex ];

            if (appearanceId  >= 256 && appearanceId  < 512) {

                Mesh subModel  = IdentityKit.lookup(appearanceId  - 256).get_head();

                if (subModel  != null) {
                    headModels[headModelsOffset ++] = subModel;
                }

            }
            if (appearanceId  >= 512) {
                Mesh subModel  = ItemDefinition.get(appearanceId  - 512).get_equipped_dialogue_model(gender);

                if (subModel  != null) {
                    headModels[headModelsOffset ++] = subModel;
                }

            }
        }

        Mesh headModel = new Mesh(headModels,headModelsOffset);

        for (int index = 0; index < 5; index++)
            if (appearanceColors[index] != 0) {
                headModel.recolor((short) Client.APPEARANCE_COLORS[index][0], (short) Client.APPEARANCE_COLORS[index][appearanceColors[index]]);
                if (index == 1)
                    headModel.recolor((short) Client.SHIRT_SECONDARY_COLORS[0], (short) Client.SHIRT_SECONDARY_COLORS[appearanceColors[index]]);
            }



        return headModel;
    }


    public boolean visible() {
        return true;
    }

    public Player() {
        key = -1L;
        isUnanimated = false;
        appearanceColors = new int[5];
        visible = false;
        player_appearance = new int[12];
    }

    public int rights, donatorRights, ironmanRights;
    private long key;
    public NpcDefinition desc;
    public boolean isUnanimated;
    public final int[] appearanceColors;
    public int team_id;
    private int gender;
    public String username;
    public static EvictingDualNodeHashTable model_cache = new EvictingDualNodeHashTable(260);
    public int combat_level;
    public int overhead_icon;
    public int skull_icon;
    public int hint_arrow_icon;
    public int animationCycleStart;
    public int animationCycleEnd;
    public int tileHeight;
    public boolean visible;
    public int field1117;
    public int tileHeight2;
    public int field1123;
    public Model playerModel;
    public final int[] player_appearance;
    private long appearance_offset;
    public int minX;
    public int minY;
    public int maxX;
    public int maxY;
    public int skill_level;
    public String title = "";
    public String titleColor = "";

    /**
     * Gets the players title
     *
     * @return title
     */
    public String getTitle(boolean rightClick) {
        if (title.length() > 0) {
            if (rightClick) {
                return titleColor + title + " <col=ffffff>";
            } else {
                return titleColor + title + " <col=0>";
            }
        } else {
            return "";
        }
    }

    @Override
    public Polygon[] getPolygons()
    {
        RSModel model = getModel();

        if (model == null)
        {
            return null;
        }

        int[] x2d = new int[model.getVerticesCount()];
        int[] y2d = new int[model.getVerticesCount()];

        int localX = getX();
        int localY = getY();

        final int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(localX, localY), Client.instance.getPlane());

        Perspective.modelToCanvas(Client.instance, model.getVerticesCount(), localX, localY, tileHeight, getOrientation(), model.getVerticesX(), model.getVerticesZ(), model.getVerticesY(), x2d, y2d);
        ArrayList polys = new ArrayList(model.getFaceCount());

        int[] trianglesX = model.getFaceIndices1();
        int[] trianglesY = model.getFaceIndices2();
        int[] trianglesZ = model.getFaceIndices3();

        for (int triangle = 0; triangle < model.getFaceCount(); ++triangle)
        {
            int[] xx =
                    {
                            x2d[trianglesX[triangle]], x2d[trianglesY[triangle]], x2d[trianglesZ[triangle]]
                    };

            int[] yy =
                    {
                            y2d[trianglesX[triangle]], y2d[trianglesY[triangle]], y2d[trianglesZ[triangle]]
                    };

            polys.add(new Polygon(xx, yy, 3));
        }

        return (Polygon[]) polys.toArray(new Polygon[0]);
    }


    public Shape getConvexHull()
    {
        RSModel model = getModel();
        if (model == null)
        {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());

        return model.getConvexHull(getX(), getY(), getOrientation(), tileHeight);
    }

    @Override
    public HeadIcon getOverheadIcon()
    {
        return getHeadIcon(getRsOverheadIcon());
    }

    public boolean isVisible() {
        return visible;
    }

    private HeadIcon getHeadIcon(int overheadIcon)
    {
        if (overheadIcon == -1)
        {
            return null;
        }

        return HeadIcon.values()[overheadIcon];
    }

    public SkullIcon getSkullIcon()
    {
        return skullFromInt(getRsSkullIcon());
    }

    private SkullIcon skullFromInt(int skull)
    {
        switch (skull)
        {
            case 0:
                return SKULL;
            case 1:
                return SKULL_FIGHT_PIT;
            case 8:
                return DEAD_MAN_FIVE;
            case 9:
                return DEAD_MAN_FOUR;
            case 10:
                return DEAD_MAN_THREE;
            case 11:
                return DEAD_MAN_TWO;
            case 12:
                return DEAD_MAN_ONE;
            default:
                return null;
        }
    }


    @Override
    public RSUsername getRsName() {
        return new RSUsername() {
            @Override
            public String getName() {
                return username;
            }

            @Override
            public int compareTo(Object o) {
                return 0;
            }
        };
    }

    public String getName()
    {
        final RSUsername rsName = getRsName();

        if (rsName == null)
        {
            return null;
        }

        String name = rsName.getName();

        if (name == null)
        {
            return null;
        }

        return name.replace('\u00A0', ' ');
    }

    @Override
    public int getId() {
        return index;
    }

    @Override
    public int getPlayerId() {
        return 0;
    }

    @Override
    public RSPlayerComposition getPlayerComposition() {
        return new RSPlayerComposition() {
            @Override
            public boolean isFemale() {
                return gender == 1;
            }

            @Override
            public int[] getColors() {
                return appearanceColors;
            }

            @Override
            public int[] getEquipmentIds() {
                return player_appearance;
            }

            @Override
            public int getEquipmentId(KitType type) {
                return player_appearance[type.getIndex()];
            }

            @Override
            public void setTransformedNpcId(int id) {

            }

            @Override
            public int getKitId(KitType type) {
                return player_appearance[type.getIndex()];
            }

            @Override
            public long getHash() {
                return appearance_offset;
            }

            @Override
            public void setHash() {

            }
        };
    }

    @Override
    public int getTotalLevel() {
        return Client.instance.getTotalLevel();
    }

    @Override
    public int getTeam() {
        return team_id;
    }

    @Override
    public boolean isFriendsChatMember() {
        return false;
    }

    @Override
    public boolean isClanMember() {
        return false;
    }

    @Override
    public boolean isFriend() {
        return false;
    }

    @Override
    public boolean isFriended()
    {
        return false;
    }

    @Override
    public int getRsOverheadIcon() {
        return 0;
    }

    @Override
    public int getRsSkullIcon() {
        return 0;
    }

    @Override
    public int getRSSkillLevel() {
        return skill_level;
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }


    public int getIndex() {
        for (int i = 0; i < Client.instance.getCachedPlayers().length; i++)
        {
            RSPlayer player = Client.instance.getCachedPlayers()[i];
            if (player != null && player.equals(this))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean isIdle() {
        return (getIdlePoseAnimation() == getPoseAnimation() && getAnimation() == -1)
                && (getInteracting() == null || getInteracting().isDead());
    }

}
