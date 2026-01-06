package com.cryptic.cache.def;

import com.cryptic.Client;
import com.cryptic.ClientConstants;
import com.cryptic.cache.graphics.SimpleImage;
import com.cryptic.collection.EvictingDualNodeHashTable;
import com.cryptic.collection.node.DualNode;
import com.cryptic.collection.table.IterableNodeHashTable;
import com.cryptic.draw.Rasterizer2D;
import com.cryptic.draw.Rasterizer3D;
import com.cryptic.entity.model.Mesh;
import com.cryptic.entity.model.Model;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.util.Js5ConfigType;

import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSItemComposition;
import net.runelite.rs.api.RSIterableNodeHashTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static net.runelite.api.Constants.CLIENT_DEFAULT_ZOOM;

public final class ItemDefinition extends DualNode implements RSItemComposition {

    public static com.cryptic.collection.table.EvictingDualNodeHashTable cached = new com.cryptic.collection.table.EvictingDualNodeHashTable(64);

    public static ItemDefinition get(int id) {
        ItemDefinition itemDef = (ItemDefinition) ItemDefinition.cached.get(id);
        if (itemDef == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.ITEM, id);
            itemDef = new ItemDefinition();
            itemDef.set_defaults();
            itemDef.id = id;
            if (data != null) {
                itemDef.decodeValues(new Buffer(data));
            }

            itemDef.post();

            if (id == 12791) {
                itemDef.inventoryActions = new String[]{"Open", null, null, "Empty", "Drop"};
            }

            if (id == 12972 || id == 12863 || id == 12865 || id == 12867 || id == 12869 || id == 12871 || id == 12873 || id == 12875 || id == 12877 || id == 12879 || id == 12881 || id == 12883 || id == 13000 || id == 13012 || id == 13024 || id == 13161
                    || id == 23110 || id == 23113 || id == 23116 || id == 23119 || id == 12728 || id == 12730 || id == 12732 || id == 12738 || id == 12736 || id == 12734) {
                itemDef.inventoryActions = new String[]{"Open", null, null, null, "Drop"};
            }

            if (id == 28767) {
                itemDef.inventoryActions = new String[]{"Open-Bank", null, null, null, null};
            }

            if (id == 32002) {
                itemDef.name = "Starter Sword";
                itemDef.inventoryModel = 63004;
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, null};
                itemDef.zoom2d = 2650;
                itemDef.yan2d = 1715;
                itemDef.xan2d = 1643;
                itemDef.zan2d = 465;
                itemDef.xOffset2d = -9;
                itemDef.yOffset2d = 60;
                itemDef.maleModel = 63004;
                itemDef.femaleModel = 63004;
                itemDef.contrast = 40;
                itemDef.ambient = 30;
            }

            if (id == 32000) {
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, null};
                itemDef.maleModel = 63002;
                itemDef.femaleModel = 63002;
            }

            if (id == 32001) {
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, null};
                itemDef.maleModel = 63003;
                itemDef.femaleModel = 63003;
            }

            if (id == 30010) {
                ItemDefinition copy = ItemDefinition.get(12422);
                itemDef.name = "Seismic Wand";
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.inventoryModel = 63011;
                itemDef.maleModel = 63010;
                itemDef.yOffset2d = copy.yOffset2d;
                itemDef.xOffset2d = copy.xOffset2d;
                itemDef.xan2d = copy.xan2d;
                itemDef.yan2d = copy.yan2d;
                itemDef.zan2d = copy.zan2d;
                itemDef.zoom2d = copy.zoom2d;
            }

            if (id == 30011) {
                ItemDefinition copy = ItemDefinition.get(4657);
                itemDef.name = "Luck of the Dwarves";
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.inventoryModel = 63012;
                itemDef.yOffset2d = copy.yOffset2d + 30;
                itemDef.xOffset2d = copy.xOffset2d - 7;
                itemDef.xan2d = copy.xan2d - 50;
                itemDef.yan2d = copy.yan2d + 525;
                itemDef.zan2d = copy.zan2d + 15;
                itemDef.zoom2d = copy.zoom2d - 120;
                itemDef.contrast = 95;
                itemDef.ambient = 75;
            }

            if (id == 298) {
                itemDef.name = "Wilderness Key";
            }

            if (id == 30012) {
                ItemDefinition copy = ItemDefinition.get(1692);
                itemDef.name = "Amulet of Souls";
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.inventoryModel = 63009;
                itemDef.yOffset2d = copy.yOffset2d;
                itemDef.xOffset2d = copy.xOffset2d;
                itemDef.xan2d = copy.xan2d;
                itemDef.yan2d = copy.yan2d;
                itemDef.zan2d = copy.zan2d;
                itemDef.zoom2d = copy.zoom2d;
                itemDef.contrast = copy.contrast;
                itemDef.ambient = copy.ambient;
            }

            if (id == 30000) {
                itemDef.name = "Completionist cape";
                itemDef.inventoryActions = new String[]{null, "Wear", "Features", null, "Drop"};
                itemDef.inventoryModel = 70003;
                itemDef.maleModel = 70004;
                itemDef.yOffset2d = 5;
                itemDef.xOffset2d = -4;
                itemDef.xan2d = 461;
                itemDef.yan2d = 1018;
                itemDef.zan2d = 9;
                itemDef.zoom2d = 2113;
            }

            if (id == 30276) {
                ItemDefinition copy = ItemDefinition.get(9807);
                itemDef.name = "Completionist cape";
                itemDef.inventoryActions = new String[]{null, "Wear", null, null, "Drop"};
                itemDef.inventoryModel = 63106;
                itemDef.maleModel = 63103;
                itemDef.femaleModel = 63103;
                itemDef.yOffset2d = copy.yOffset2d;
                itemDef.xOffset2d = copy.xOffset2d;
                itemDef.xan2d = copy.xan2d;
                itemDef.yan2d = copy.yan2d;
                itemDef.zan2d = copy.zan2d;
                itemDef.zoom2d = copy.zoom2d;
                itemDef.contrast = copy.contrast;
                itemDef.ambient = copy.ambient;
            }

            if (itemDef.notedTemplate != -1) {
                itemDef.updateNote();
            }

            if (itemDef.notedId != -1) {
                //itemDef.genBought(lookup(itemDef.notedId), lookup(itemDef.unnotedId));
            }

            if (itemDef.placeholderTemplate != -1) {
                //itemDef.genPlaceholder(lookup(itemDef.placeholderTemplate), lookup(itemDef.placeholder));
            }

            cached.put(itemDef, id);
        }
        return itemDef;
    }

    private void decodeValues(Buffer stream) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0)
                return;
            if (opcode == 1) {
                inventoryModel = stream.readUnsignedShort();
            } else if (opcode == 2) {
                name = stream.readStringCp1252NullTerminated();
            } else if (opcode == 4) {
                zoom2d = stream.readUnsignedShort();
            } else if (opcode == 5) {
                xan2d = stream.readUnsignedShort();
            } else if (opcode == 6) {
                yan2d = stream.readUnsignedShort();
            } else if (opcode == 7) {
                xOffset2d = stream.readUnsignedShort();
                if (xOffset2d > 32767) {
                    xOffset2d -= 65536;
                }
            } else if (opcode == 8) {
                yOffset2d = stream.readUnsignedShort();
                if (yOffset2d > 32767) {
                    yOffset2d -= 65536;
                }
            } else if (opcode == 9) {
                unknown1 = stream.readStringCp1252NullTerminated();
            } else if (opcode == 11) {
                stackable = true;
            } else if (opcode == 12) {
                cost = stream.readInt();
            } else if (opcode == 13) {
                wearPos1 = stream.readByte();
            } else if (opcode == 14) {
                wearPos2 = stream.readByte();
            } else if (opcode == 16) {
                members = true;
            } else if (opcode == 23) {
                maleModel = stream.readUnsignedShort();
                maleOffset = stream.readUnsignedByte();
            } else if (opcode == 24) {
                maleModel1 = stream.readUnsignedShort();
            } else if (opcode == 25) {
                femaleModel = stream.readUnsignedShort();
                femaleOffset = stream.readUnsignedByte();
            } else if (opcode == 26) {
                femaleModel1 = stream.readUnsignedShort();
            } else if (opcode == 27) {
                wearPos3 = stream.readByte();
            } else if (opcode >= 30 && opcode < 35) {
                groundActions[opcode - 30] = stream.readStringCp1252NullTerminated();
                if (groundActions[opcode - 30].equalsIgnoreCase("Hidden")) {
                    groundActions[opcode - 30] = null;
                }
            } else if (opcode >= 35 && opcode < 40) {
                inventoryActions[opcode - 35] = stream.readStringCp1252NullTerminated();
            } else if (opcode == 40) {
                int var5 = stream.readUnsignedByte();
                recolorFrom = new short[var5];
                recolorTo = new short[var5];

                for (int var4 = 0; var4 < var5; ++var4) {
                    recolorFrom[var4] = (short) stream.readUnsignedShort();
                    recolorTo[var4] = (short) stream.readUnsignedShort();
                }

            } else if (opcode == 41) {
                int var5 = stream.readUnsignedByte();
                retextureFrom = new short[var5];
                retextureTo = new short[var5];

                for (int var4 = 0; var4 < var5; ++var4) {
                    retextureFrom[var4] = (short) stream.readUnsignedShort();
                    retextureTo[var4] = (short) stream.readUnsignedShort();
                }

            } else if (opcode == 42) {
                shiftClickIndex = stream.readByte();
            } else if (opcode == 65) {
                isTradable = true;
            } else if (opcode == 75) {
                weight = stream.readShort();
            } else if (opcode == 78) {
                maleModel2 = stream.readUnsignedShort();
            } else if (opcode == 79) {
                femaleModel2 = stream.readUnsignedShort();
            } else if (opcode == 90) {
                maleHeadModel = stream.readUnsignedShort();
            } else if (opcode == 91) {
                femaleHeadModel = stream.readUnsignedShort();
            } else if (opcode == 92) {
                maleHeadModel2 = stream.readUnsignedShort();
            } else if (opcode == 93) {
                femaleHeadModel2 = stream.readUnsignedShort();
            } else if (opcode == 94) {
                category = stream.readUnsignedShort();
            } else if (opcode == 95) {
                zan2d = stream.readUnsignedShort();
            } else if (opcode == 97) {
                notedID = stream.readUnsignedShort();
            } else if (opcode == 98) {
                notedTemplate = stream.readUnsignedShort();
            } else if (opcode >= 100 && opcode < 110) {
                if (countobj == null) {
                    countobj = new int[10];
                    countco = new int[10];
                }

                countobj[opcode - 100] = stream.readUnsignedShort();
                countco[opcode - 100] = stream.readUnsignedShort();
            } else if (opcode == 110) {
                resizeX = stream.readUnsignedShort();
            } else if (opcode == 111) {
                resizeY = stream.readUnsignedShort();
            } else if (opcode == 112) {
                resizeZ = stream.readUnsignedShort();
            } else if (opcode == 113) {
                ambient = stream.readByte();
            } else if (opcode == 114) {
                contrast = stream.readByte();
            } else if (opcode == 115) {
                team = stream.readUnsignedByte();
            } else if (opcode == 139) {
                unnotedId = stream.readUnsignedShort();
            } else if (opcode == 140) {
                notedId = stream.readUnsignedShort();
            } else if (opcode == 148) {
                placeholder = stream.readUnsignedShort();
            } else if (opcode == 149) {
                placeholderTemplate = stream.readUnsignedShort();
            } else if (opcode == 249) {
                this.params = Buffer.readStringIntParameters(stream, this.params);
            } else {
                System.err.printf("Error unrecognised {Items} opcode: %d%n%n", opcode);
            }
        }
    }

    public static void copyInventory(ItemDefinition itemDef, int id) {
        ItemDefinition copy = ItemDefinition.get(id);
        itemDef.inventoryModel = copy.inventoryModel;
        itemDef.zoom2d = copy.zoom2d;
        itemDef.xan2d = copy.xan2d;
        itemDef.yan2d = copy.yan2d;
        itemDef.zan2d = copy.zan2d;
        itemDef.resizeX = copy.resizeX;
        itemDef.resizeY = copy.resizeY;
        itemDef.resizeZ = copy.resizeZ;
        itemDef.xOffset2d = copy.xOffset2d;
        itemDef.yOffset2d = copy.yOffset2d;
        itemDef.inventoryActions = copy.inventoryActions;
        itemDef.cost = copy.cost;
        itemDef.stackable = copy.stackable;
    }

    public static void copyEquipment(ItemDefinition itemDef, int id) {
        ItemDefinition copy = ItemDefinition.get(id);
        itemDef.maleModel = copy.maleModel;
        itemDef.maleModel1 = copy.maleModel1;
        itemDef.femaleModel = copy.femaleModel;
        itemDef.femaleModel1 = copy.femaleModel1;
        itemDef.maleOffset = copy.maleOffset;
        itemDef.femaleOffset = copy.femaleOffset;
    }

    public static void printStatement(final String text) {
        System.out.println(text + ";");
    }

    public static void printDefinitions(final ItemDefinition definition) {
        printStatement("definition.name = \"" + definition.name + "\"");
        printStatement("definition.model_zoom = " + definition.zoom2d);
        printStatement("definition.rotation_y = " + definition.xan2d);
        printStatement("definition.rotation_x = " + definition.yan2d);
        printStatement("definition.translate_x = " + definition.xOffset2d);
        printStatement("definition.translate_y = " + definition.yOffset2d);
        printStatement("definition.inventoryModel = " + definition.inventoryModel);
        printStatement("definition.male_equip_main = " + definition.maleModel);
        printStatement("definition.female_equip_main = " + definition.femaleModel);
        printStatement("definition.recolorFrom = " + Arrays.toString(definition.recolorFrom));
        printStatement("definition.recolorTo = " + Arrays.toString(definition.recolorTo));
    }

    public static void dump() {
        File f = new File(System.getProperty("user.home") + "/Desktop/items.txt");
        try {
            f.createNewFile();
            BufferedWriter bf = new BufferedWriter(new FileWriter(f));
            for (int id = 0; id < Js5List.getConfigSize(Js5ConfigType.ITEM); id++) {
                ItemDefinition definition = ItemDefinition.get(id);

                bf.write("case " + id + ":");
                bf.write(System.getProperty("line.separator"));
                if (definition.name == null || definition.name.equals("null") ||
                        definition.name.isEmpty()) continue;

                bf.write("definition[id].name = " + definition.name + ";");
                bf.write(System.getProperty("line.separator"));
                if (definition.inventoryModel != 0) {
                    bf.write("definition[id].inventoryModel = " + definition.inventoryModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.recolorFrom != null) {
                    bf.write("definition[id].recolorFrom = new int[] "
                            + Arrays.toString(definition.recolorFrom).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.recolorTo != null) {
                    bf.write("definition[id].recolorTo = new int[] "
                            + Arrays.toString(definition.recolorTo).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.retextureFrom != null) {
                    bf.write("definition[id].retextureFrom = new int[] "
                            + Arrays.toString(definition.retextureFrom).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.retextureTo != null) {
                    bf.write("definition[id].retextureTo = new int[] "
                            + Arrays.toString(definition.retextureTo).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.zoom2d != 2000) {
                    bf.write("definition[id].model_zoom = " + definition.zoom2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.xan2d != 0) {
                    bf.write("definition[id].rotation_y = " + definition.xan2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.yan2d != 0) {
                    bf.write("definition[id].rotation_x = " + definition.yan2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.zan2d != 0) {
                    bf.write("definition[id].rotation_z = " + definition.zan2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.xOffset2d != -1) {
                    bf.write("definition[id].translate_x = " + definition.xOffset2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.yOffset2d != -1) {
                    bf.write("definition[id].translate_y = " + definition.yOffset2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                bf.write("definition[id].stackable = " + definition.stackable + ";");
                bf.write(System.getProperty("line.separator"));
                if (definition.groundActions != null) {
                    bf.write("definition[id].scene_actions = new int[] "
                            + Arrays.toString(definition.groundActions).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.inventoryActions != null) {
                    bf.write("definition[id].widget_actions = new int[] "
                            + Arrays.toString(definition.inventoryActions).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleModel != -1) {
                    bf.write("definition[id].male_equip_main = " + definition.maleModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleModel1 != -1) {
                    bf.write("definition[id].male_equip_attachment = " + definition.maleModel1 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleOffset != 0) {
                    bf.write("definition[id].male_equip_translate_y = " + definition.maleOffset + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleModel != -1) {
                    bf.write("definition[id].female_equip_main = " + definition.femaleModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleModel1 != -1) {
                    bf.write("definition[id].female_equip_attachment = " + definition.femaleModel1 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleOffset != 0) {
                    bf.write("definition[id].female_equip_translate_y = " + definition.femaleOffset + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleModel2 != -1) {
                    bf.write("definition[id].male_equip_emblem = " + definition.maleModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleModel2 != -1) {
                    bf.write("definition[id].female_equip_emblem = " + definition.femaleModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleHeadModel != -1) {
                    bf.write("definition[id].male_dialogue_head = " + definition.maleHeadModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleHeadModel2 != -1) {
                    bf.write("definition[id].male_dialogue_headgear = " + definition.maleHeadModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleHeadModel != -1) {
                    bf.write("definition[id].female_dialogue_head = " + definition.femaleHeadModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleHeadModel2 != -1) {
                    bf.write("definition[id].female_dialogue_headgear = " + definition.femaleHeadModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.countobj != null) {
                    bf.write("definition[id].stack_variant_id = new int[] "
                            + Arrays.toString(definition.countobj).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.countco != null) {
                    bf.write("definition[id].stack_variant_size = new int[] "
                            + Arrays.toString(definition.countco).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.notedID != -1) {
                    bf.write("definition[id].note = " + definition.notedID + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.notedTemplate != -1) {
                    bf.write("definition[id].model_scale_xy = " + definition.notedTemplate + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.resizeX != 128) {
                    bf.write("definition[id].model_scale_x = " + definition.resizeX + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.resizeY != 128) {
                    bf.write("definition[id].model_scale_y = " + definition.resizeY + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.resizeZ != 128) {
                    bf.write("definition[id].model_scale_z = " + definition.resizeZ + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.ambient != 0) {
                    bf.write("definition[id].ambient = " + definition.ambient + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.contrast != 0) {
                    bf.write("definition[id].contrast = " + definition.contrast + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                bf.write("break;");
                bf.write(System.getProperty("line.separator"));
                bf.write(System.getProperty("line.separator"));
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void set_defaults() {
        name = "null";
        zoom2d = 2000;
        xan2d = 0;
        yan2d = 0;
        zan2d = 0;
        xOffset2d = 0;
        yOffset2d = 0;
        stackable = false;
        cost = 1;
        members = false;
        groundActions = new String[]{null, null, "Take", null, null};
        inventoryActions = new String[]{null, null, null, null, "Drop"};
        shiftClickIndex = -2;
        maleModel = -1;
        maleModel1 = -1;
        maleOffset = 0;
        femaleModel = -1;
        femaleModel1 = -1;
        femaleOffset = 0;
        maleModel2 = -1;
        femaleModel2 = -1;
        maleHeadModel = -1;
        maleHeadModel2 = -1;
        femaleHeadModel = -1;
        femaleHeadModel2 = -1;
        notedID = -1;
        notedTemplate = -1;
        resizeX = 128;
        resizeY = 128;
        resizeZ = 128;
        ambient = 0;
        contrast = 0;
        team = 0;
        isTradable = false;
        unnotedId = -1;
        notedId = -1;
        placeholder = -1;
        placeholderTemplate = -1;
        inventoryModel = 0;
        countobj = null;
        countco = null;
        animate_inv_sprite = false;
        modelCustomColor = 0;
        modelCustomColor2 = 0;
        modelCustomColor3 = 0;
        modelCustomColor4 = 0;
        modelSetColor = 0;
        recolorFrom = null;
        recolorTo = null;
    }

    private void updateNote() {
        ItemDefinition noted = get(notedTemplate);
        inventoryModel = noted.inventoryModel;
        zoom2d = noted.zoom2d;
        xan2d = noted.xan2d;
        yan2d = noted.yan2d;
        zan2d = noted.zan2d;
        xOffset2d = noted.xOffset2d;
        yOffset2d = noted.yOffset2d;
        recolorFrom = noted.recolorFrom;
        recolorTo = noted.recolorTo;
        retextureFrom = noted.retextureFrom;
        retextureTo = noted.retextureTo;
        ItemDefinition unnotedItem = get(notedID);
        name = unnotedItem.name;
        cost = unnotedItem.cost;
        stackable = true;

        String consonant_or_vowel_lead = "a";
        if (!ClientConstants.OSRS_DATA) {
            char character = unnotedItem.name.charAt(0);
            if (character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U')
                consonant_or_vowel_lead = "an";
        } else {
            String character = unnotedItem.name;
            if (character.equals("A") || character.equals("E") || character.equals("I") || character
                    .equals("O") || character.equals("U"))
                consonant_or_vowel_lead = "an";
        }
        description = ("Swap this note at any bank for " + consonant_or_vowel_lead + " " + unnotedItem.name + ".");
    }

    public Model getModel(int stack_size) {
        if (this.countobj != null && stack_size > 1) {
            int var2 = -1;

            for (int var3 = 0; var3 < 10; ++var3) {
                if (stack_size >= this.countco[var3] && this.countco[var3] != 0) {
                    var2 = this.countobj[var3];
                }
            }

            if (var2 != -1) {
                return get(var2).getModel(1);
            }
        }

        Model var5 = (Model) models.get((long) this.id);
        if (var5 != null) {
            return var5;
        } else {
            Mesh var6 = Mesh.getModel(this.inventoryModel);
            if (var6 == null) {
                return null;
            } else {
                if (this.resizeX != 128 || this.resizeY != 128 || this.resizeZ != 128) {
                    var6.resize(this.resizeX, this.resizeY, this.resizeZ);
                }

                int var4;
                if (this.recolorFrom != null) {
                    for (var4 = 0; var4 < this.recolorFrom.length; ++var4) {
                        var6.recolor(this.recolorFrom[var4], this.recolorTo[var4]);
                    }
                }

                if (this.retextureFrom != null) {
                    for (var4 = 0; var4 < this.retextureFrom.length; ++var4) {
                        var6.retexture(this.retextureFrom[var4], this.retextureTo[var4]);
                    }
                }

                var5 = var6.toModel(this.ambient + 64, this.contrast + 768, -50, -10, -50);
                var5.singleTile = true;

                models.put(var5, (long) this.id);
                return var5;
            }
        }
    }


    public Mesh get_widget_model(int stack_size) {
        if (countobj != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int index = 0; index < 10; index++) {
                if (stack_size >= countco[index] && countco[index] != 0)
                    stack_item_id = countobj[index];
            }
            if (stack_item_id != -1)
                return get(stack_item_id).get_widget_model(1);

        }
        Mesh widget_model = Mesh.getModel(inventoryModel);
        if (widget_model == null)
            return null;
        if (recolorFrom != null) {
            for (int index = 0; index < recolorFrom.length; index++) {
                widget_model.recolor(recolorFrom[index], recolorTo[index]);
            }
        }
        if (retextureFrom != null) {
            for (int index = 0; index < retextureFrom.length; index++) {
                widget_model.retexture(retextureFrom[index], retextureTo[index]);
            }
        }


        return widget_model;
    }

    public Mesh get_equipped_model(int gender) {
        int main = maleModel;
        int attatchment = maleModel1;
        int emblem = maleModel2;
        if (gender == 1) {
            main = femaleModel;
            attatchment = femaleModel1;
            emblem = femaleModel2;
        }
        if (main == -1)
            return null;

        Mesh equipped_model = Mesh.getModel(main);
        if (equipped_model == null) {
            return null;
        }
        if (attatchment != -1) {
            if (emblem != -1) {
                Mesh attachment_model = Mesh.getModel(attatchment);
                Mesh emblem_model = Mesh.getModel(emblem);
                Mesh[] list = {
                        equipped_model, attachment_model, emblem_model
                };
                equipped_model = new Mesh(list, 3);
            } else {
                Mesh attachment_model = Mesh.getModel(attatchment);
                Mesh[] list = {
                        equipped_model, attachment_model
                };
                equipped_model = new Mesh(list, 2);
            }
        }
        if (gender == 0 && maleOffset != 0)
            equipped_model.changeOffset(0, maleOffset, 0);

        if (gender == 1 && femaleOffset != 0)
            equipped_model.changeOffset(0, femaleOffset, 0);

        if (recolorFrom != null) {
            for (int index = 0; index < recolorFrom.length; index++) {
                equipped_model.recolor(recolorFrom[index], recolorTo[index]);
            }
        }
        if (retextureFrom != null) {
            for (int index = 0; index < retextureFrom.length; index++) {
                equipped_model.retexture(retextureFrom[index], retextureTo[index]);
            }
        }

        return equipped_model;
    }

    private void post() {
        if (stackable) {
            weight = 0;
        }
    }

    public boolean equipped_model_cached(int gender) {
        int main = maleModel;
        int attachment = maleModel1;
        int emblem = maleModel2;
        if (gender == 1) {
            main = femaleModel;
            attachment = femaleModel1;
            emblem = femaleModel2;
        }
        if (main == -1)
            return true;

        boolean cached = Js5List.models.tryLoadFile(main);

        if (attachment != -1 && !Js5List.models.tryLoadFile(attachment))
            cached = false;

        if (emblem != -1 && !Js5List.models.tryLoadFile(emblem))
            cached = false;

        return cached;
    }

    public Mesh get_equipped_dialogue_model(int gender) {
        int head_model = maleHeadModel;
        int equipped_headgear = maleHeadModel2;
        if (gender == 1) {
            head_model = femaleHeadModel;
            equipped_headgear = femaleHeadModel2;
        }
        if (head_model == -1)
            return null;

        Mesh dialogue_model = Mesh.getModel(head_model);
        if (equipped_headgear != -1) {
            Mesh headgear = Mesh.getModel(equipped_headgear);
            Mesh[] list = {
                    dialogue_model, headgear
            };
            dialogue_model = new Mesh(list, 2);
        }
        if (recolorFrom != null) {
            for (int index = 0; index < recolorFrom.length; index++) {
                if (dialogue_model != null) {
                    dialogue_model.recolor(recolorFrom[index], recolorTo[index]);
                }
            }

        }
        if (retextureFrom != null) {
            for (int index = 0; index < retextureFrom.length; index++) {
                if (dialogue_model != null) {
                    dialogue_model.retexture(retextureFrom[index], retextureTo[index]);
                }
            }
        }
        return dialogue_model;
    }

    public boolean dialogue_model_cached(int gender) {
        int head_model = maleHeadModel;
        int equipped_headgear = maleHeadModel2;
        if (gender == 1) {
            head_model = femaleHeadModel;
            equipped_headgear = femaleHeadModel2;
        }
        if (head_model == -1)
            return true;

        boolean cached = Js5List.models.tryLoadFile(head_model);

        if (equipped_headgear != -1 && !Js5List.models.tryLoadFile(equipped_headgear))
            cached = false;

        return cached;
    }

    public static void release() {
        models.clear();
        sprites.clear();
        cached.clear();
    }

    public static SimpleImage getSprite(int var0, int var1, int var2, int var3, int var4, boolean var5) {
        if (var1 == -1) {
            var4 = 0;
        } else if (var4 == 2 && var1 != 1) {
            var4 = 1;
        }

        long var6 = ((long) var2 << 38) + (long) var0 + ((long) var1 << 16) + ((long) var4 << 40) + ((long) var3 << 42);
        SimpleImage var8;
        if (!var5) {
            var8 = (SimpleImage) cached.get(var6);
            if (var8 != null) {
                return var8;
            }
        }

        ItemDefinition var9 = ItemDefinition.get(var0);
        if (var1 > 1 && var9.countobj != null) {
            int var10 = -1;

            for (int var11 = 0; var11 < 10; ++var11) {
                if (var1 >= var9.countco[var11] && var9.countco[var11] != 0) {
                    var10 = var9.countobj[var11];
                }
            }

            if (var10 != -1) {
                var9 = ItemDefinition.get(var10);
            }
        }

        Model var20 = var9.getModel(1);
        if (var20 == null) {
            return null;
        } else {
            SimpleImage var21 = null;
            if (var9.notedTemplate != -1) {
                var21 = getSprite(var9.notedID, 10, 1, 0, 0, true);
                if (var21 == null) {
                    return null;
                }
            } else if (var9.notedId != -1) {
                var21 = getSprite(var9.unnotedId, var1, var2, var3, 0, false);
                if (var21 == null) {
                    return null;
                }
            } else if (var9.placeholderTemplate != -1) {
                var21 = getSprite(var9.placeholder, var1, 0, 0, 0, false);
                if (var21 == null) {
                    return null;
                }
            }

            int[] var12 = Rasterizer2D.pixels;
            int var13 = Rasterizer2D.width;
            int var14 = Rasterizer2D.height;
            float[] var15 = Rasterizer2D.depth;
            int[] var16 = new int[4];
            Rasterizer2D.getClipArray(var16);
            var8 = new SimpleImage(36, 32);
            Rasterizer3D.initDrawingArea(var8.pixels, 36, 32, (float[]) null);
            Rasterizer2D.clear();
            Rasterizer3D.setupRasterizerClip();
            Rasterizer3D.drawImage(16, 16);
            Rasterizer3D.clips.rasterGouraudLowRes = false;
            var20.renderonGpu = false;
            if (var9.placeholderTemplate != -1) {
                var21.drawAdvancedSprite(0, 0);
            }

            int var17 = var9.zoom2d;
            if (var5) {
                var17 = (int) (1.5D * (double) var17);
            } else if (var2 == 2) {
                var17 = (int) ((double) var17 * 1.04D);
            }

            int var18 = var17 * Rasterizer3D.SINE[var9.xan2d] >> 16;
            int var19 = var17 * Rasterizer3D.COSINE[var9.xan2d] >> 16;
            var20.calculateBoundsCylinder();
            var20.renderModel(var9.yan2d, var9.zan2d, var9.xan2d, var9.xOffset2d, var20.model_height / 2 + var18 + var9.yOffset2d, var19 + var9.yOffset2d);
            if (var9.notedId != -1) {
                var21.drawAdvancedSprite(0, 0);
            }

            if (var2 >= 1) {
                var8.outline(1);
            }

            if (var2 >= 2) {
                var8.outline(16777215);
            }

            if (var3 != 0) {
                var8.shadow(var3);
            }

            Rasterizer3D.initDrawingArea(var8.pixels, 36, 32, (float[]) null);
            if (var9.notedTemplate != -1) {
                var21.drawAdvancedSprite(0, 0);
            }

            if (var4 == 1 || var4 == 2 && var9.stackable) {
                Client.smallFont.draw(method633(var1), 0, 9, 16776960, 1);
            }

            if (!var5) {
                sprites.put(var8, var6);
            }

            Rasterizer3D.initDrawingArea(var12, var13, var14, var15);
            Rasterizer2D.setClipArray(var16);
            Rasterizer3D.setupRasterizerClip();
            Rasterizer3D.clips.rasterGouraudLowRes = true;
            var20.renderonGpu = true;
            return var8;
        }
    }

    public static SimpleImage getSprite(int itemId, int stackSize, int outlineColor) {
        int zoom = Client.instance.get3dZoom();
        Client.instance.set3dZoom(CLIENT_DEFAULT_ZOOM);
        try {
            if (outlineColor == 0) {
                SimpleImage sprite = (SimpleImage) sprites.get(itemId);
                if (sprite != null && sprite.max_height != stackSize) {

                    sprite.unlink();
                    sprite = null;
                }
                if (sprite != null)
                    return sprite;
            }
            ItemDefinition itemDef = get(itemId);
            if (itemDef.countobj == null)
                stackSize = -1;
            if (stackSize > 1) {
                int stack_item_id = -1;
                for (int j1 = 0; j1 < 10; j1++)
                    if (stackSize >= itemDef.countco[j1] && itemDef.countco[j1] != 0)
                        stack_item_id = itemDef.countobj[j1];

                if (stack_item_id != -1)
                    itemDef = get(stack_item_id);
            }
            Model model = itemDef.getModel(1);
            if (model == null)
                return null;
            SimpleImage sprite = null;
            if (itemDef.notedTemplate != -1) {
                sprite = getSprite(itemDef.notedID, 10, -1);
                if (sprite == null) {
                    return null;
                }
            } else if (itemDef.notedId != -1) {
                sprite = getSprite(itemDef.unnotedId, 10, -1);
                if (sprite == null) {
                    return null;
                }
            } else if (itemDef.placeholderTemplate != -1) {
                sprite = getSprite(itemDef.placeholder, 10, -1);
                if (sprite == null) {
                    return null;
                }
            }

            int[] pixels = Rasterizer2D.pixels;
            int width = Rasterizer2D.width;
            int height = Rasterizer2D.height;
            float[] depth = Rasterizer2D.depth;
            int[] arrayClip = new int[4];
            Rasterizer2D.getClipArray(arrayClip);
            SimpleImage enabledSprite = new SimpleImage(32, 32);
            Rasterizer3D.initDrawingArea(enabledSprite.pixels, 32, 32, (float[]) null);
            Rasterizer2D.clear();
            Rasterizer3D.setupRasterizerClip();
            Rasterizer3D.drawImage(16, 16);
            Rasterizer3D.clips.rasterGouraudLowRes = false;
            model.renderonGpu = false;
            if (itemDef.placeholderTemplate != -1) {
                int old_w = sprite.max_width;
                int old_h = sprite.max_height;
                sprite.max_width = 32;
                sprite.max_height = 32;
                sprite.drawSprite(0, 0);
                sprite.max_width = old_w;
                sprite.max_height = old_h;
            }

            int k3 = itemDef.zoom2d;
            if (outlineColor == -1)
                k3 = (int) ((double) k3 * 1.5D);
            if (outlineColor > 0)
                k3 = (int) ((double) k3 * 1.04D);

            int l3 = Rasterizer3D.SINE[itemDef.xan2d] * k3 >> 16;
            int i4 = Rasterizer3D.COSINE[itemDef.xan2d] * k3 >> 16;

            model.calculateBoundsCylinder();
            model.renderModel(itemDef.yan2d, itemDef.zan2d, itemDef.xan2d, itemDef.xOffset2d, l3 + model.model_height / 2 + itemDef.yOffset2d, i4 + itemDef.yOffset2d);

            if (itemDef.notedId != -1) {
                enabledSprite.drawAdvancedSprite(0, 0);
            }

            enabledSprite.outline(1);
            if (outlineColor > 0) {
                enabledSprite.outline(16777215);
            }
            if (outlineColor == 0) {
                enabledSprite.shadow(3153952);
            }

            Rasterizer3D.initDrawingArea(enabledSprite.pixels, 32, 32, (float[]) null);

            if (itemDef.notedTemplate != -1) {
                int old_w = sprite.max_width;
                int old_h = sprite.max_height;
                sprite.max_width = 32;
                sprite.max_height = 32;
                sprite.drawSprite(0, 0);
                sprite.max_width = old_w;
                sprite.max_height = old_h;
            }

            if (outlineColor == 0) {
                sprites.put(enabledSprite, itemId);
            }


            Rasterizer3D.initDrawingArea(pixels, width, height, depth);
            Rasterizer2D.setClipArray(arrayClip);
            Rasterizer3D.setupRasterizerClip();
            Rasterizer3D.clips.rasterGouraudLowRes = true;
            model.renderonGpu = true;
            return enabledSprite;
        } finally {
            Client.instance.set3dZoom(zoom);
        }
    }

    static String method633(int var0) {
        if (var0 < 100000) {
            return "<col=ffff00>" + var0 + "</col>";
        } else {
            return var0 < 10000000 ? "<col=ffffff>" + var0 / 1000 + "K" + "</col>" : "<col=00ff80>" + var0 / 1000000 + "M" + "</col>";
        }
    }

    public ItemDefinition() {
        id = -1;
    }


    public static EvictingDualNodeHashTable models = new EvictingDualNodeHashTable(50);
    public static EvictingDualNodeHashTable sprites = new EvictingDualNodeHashTable(200);

    public int cost;
    public int id;
    public int team;
    public int zoom2d;
    public int yan2d;
    public int xan2d;

    public int weight;
    public int wearPos1;
    public int wearPos2;
    public int wearPos3;
    public int zan2d;
    public int inventoryModel;
    public int maleModel;
    public int maleModel1;
    public int maleModel2;

    public int femaleModel;
    public int femaleModel1;
    public int femaleModel2;

    public int maleHeadModel;
    private int maleHeadModel2;
    public int maleOffset;

    public int femaleHeadModel;
    private int femaleHeadModel2;
    public int femaleOffset;

    public int xOffset2d;
    public int yOffset2d;
    private int resizeX;
    private int resizeY;
    private int resizeZ;
    public int notedTemplate;
    public int notedID;
    public int ambient;
    public int contrast;
    public int[] countobj;
    public int[] countco;
    public short[] recolorFrom;
    public short[] recolorTo;
    public short[] retextureFrom;
    public short[] retextureTo;

    public String[] inventoryActions;
    public String[] groundActions;
    public String name;
    public String description;
    public static boolean members;
    public boolean stackable;
    public boolean animateInventory;

    public boolean animate_inv_sprite;

    public boolean isTradable;
    public int category;
    public int unnotedId;
    public int notedId;
    public int shiftClickIndex;
    public int placeholder;
    public String unknown1;
    public int placeholderTemplate;

    IterableNodeHashTable params;

    //Custom coloring
    public int modelCustomColor = 0;
    public int modelCustomColor2 = 0;
    public int modelCustomColor3 = 0;
    public int modelCustomColor4 = 0;
    public int modelSetColor = 0;

    public static int setInventoryModel(final int id) {
        final ItemDefinition definition = get(id);
        return definition.inventoryModel;
    }

    public static String setItemName(final int id) {
        final ItemDefinition definition = get(id);
        return definition.name;
    }

    public static int setMaleEquipmentId(final int id) {
        final ItemDefinition definition = get(id);
        return definition.maleModel;
    }

    public static int setFemaleEquipmentId(final int id) {
        final ItemDefinition definition = get(id);
        return definition.femaleModel;
    }

    public static int setModelZoom(final int id) {
        final ItemDefinition definition = get(id);
        return definition.zoom2d;
    }

    public static int setRotationX(final int id) {
        final ItemDefinition definition = get(id);
        return definition.yan2d;
    }

    public static int setRotationY(final int id) {
        final ItemDefinition definition = get(id);
        return definition.xan2d;
    }

    public static int setTranslateX(final int id) {
        final ItemDefinition definition = get(id);
        return definition.xOffset2d;
    }

    public static int setTranslateY(final int id) {
        final ItemDefinition definition = get(id);
        return definition.yOffset2d;
    }

    @Override
    public int getHaPrice() {
        int price = getPrice();
        return (int) ((float) price * 0.6f);
    }

    @Override
    public boolean isStackable() {
        return stackable;
    }

    @Override
    public void setShiftClickActionIndex(int shiftClickActionIndex) {

    }

    @Override
    public void resetShiftClickActionIndex() {

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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getNote() {
        return notedTemplate;
    }

    @Override
    public int getLinkedNoteId() {
        return notedID;
    }

    @Override
    public int getPlaceholderId() {
        return 0;
    }

    @Override
    public int getPlaceholderTemplateId() {
        return placeholderTemplate;
    }

    @Override
    public int getPrice() {
        return this.cost;
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return this.isTradable;
    }

    @Override
    public void setTradeable(boolean yes) {
        this.isTradable = yes;
    }

    @Override
    public int getIsStackable() {
        return 0;
    }

    @Override
    public int getMaleModel() {
        return this.maleModel;
    }

    @Override
    public String[] getInventoryActions() {
        return this.inventoryActions;
    }

    @Override
    public String[] getGroundActions() {
        return this.groundActions;
    }

    @Override
    public int getShiftClickActionIndex() {
        return shiftClickIndex;
    }

    @Override
    public int getInventoryModel() {
        return inventoryModel;
    }

    @Override
    public short[] getColorToReplaceWith() {
        return recolorTo;
    }

    @Override
    public short[] getTextureToReplaceWith() {
        return retextureTo;
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
}

