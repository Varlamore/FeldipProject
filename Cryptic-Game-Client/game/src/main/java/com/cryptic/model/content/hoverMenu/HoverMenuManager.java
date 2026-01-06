package com.cryptic.model.content.hoverMenu;

import com.cryptic.Client;
import com.cryptic.ClientConstants;
import com.cryptic.cache.def.ItemDefinition;
import com.cryptic.cache.graphics.SimpleImage;
import com.cryptic.draw.Rasterizer2D;
import com.cryptic.engine.impl.MouseHandler;

import java.util.Arrays;
import java.util.HashMap;

import static com.cryptic.util.CustomItemIdentifiers.*;
import static com.cryptic.util.ItemIdentifiers.*;

public class HoverMenuManager {

    public static final int TEXT_COLOUR = 0xFFFFFFF;

    public static HashMap<Integer, HoverMenu> menus = new HashMap<>();

    public static void init() {
        menus.put(MINING_GLOVES, new HoverMenu("@gre@+1%@whi@ bonus exp in the mining skill."));
        menus.put(EXPERT_MINING_GLOVES, new HoverMenu("@gre@+3%@whi@ bonus exp in the mining skill."));
        menus.put(SUPERIOR_MINING_GLOVES, new HoverMenu("@gre@+5%@whi@ bonus exp in the mining skill."));
        menus.put(RING_OF_WEALTH, new HoverMenu("@gre@+5%@whi@ drop rate bonus."));
        menus.put(RING_OF_WEALTH_I, new HoverMenu("@gre@+7.5%@whi@ drop rate bonus."));

        menus.put(DONATOR_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                GREEN_HALLOWEEN_MASK, _3RD_AGE_BOW, BLACK_SANTA_HAT, BLUE_PARTYHAT, ARCANE_SPIRIT_SHIELD, SPECTRAL_SPIRIT_SHIELD, FEROCIOUS_GLOVES, DRAGON_HUNTER_LANCE, ARMADYL_GODSWORD, DRAGON_WARHAMMER, ARMADYL_CROSSBOW, TOXIC_STAFF_OF_THE_DEAD, PRIMORDIAL_BOOTS, BANDOS_TASSETS, ARMADYL_CHESTPLATE
            )));

        menus.put(16451, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
                Arrays.asList(
                        INQUISITORS_MACE,
                        VESTAS_LONGSWORD,
                        STATIUSS_WARHAMMER,
                        TOXIC_STAFF_OF_THE_DEAD,
                        TOXIC_BLOWPIPE,
                        ARMADYL_GODSWORD,
                        DRAGON_CLAWS
                )));

        menus.put(25278, new HoverMenu("Increases melee, ranged and magic damage & accuracy by @gre@20%@whi@ against the undead."));

        menus.put(16452, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
                Arrays.asList(
                        JUSTICIAR_FACEGUARD,
                        JUSTICIAR_CHESTGUARD,
                        JUSTICIAR_LEGGUARDS,
                        INQUISITORS_GREAT_HELM,
                        INQUISITORS_HAUBERK,
                        INQUISITORS_PLATESKIRT,
                        DARK_INFINITY_HAT,
                        DARK_INFINITY_TOP,
                        DARK_INFINITY_BOTTOMS,
                        LIGHT_INFINITY_HAT,
                        LIGHT_INFINITY_TOP,
                        LIGHT_INFINITY_BOTTOMS
                )));

        menus.put(16454, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
                Arrays.asList(
                        ARCANE_SPIRIT_SHIELD,
                        AVERNIC_DEFENDER,
                        INFERNAL_CAPE,
                        FEROCIOUS_GLOVES,
                        HARMONISED_NIGHTMARE_STAFF,
                        VOLATILE_NIGHTMARE_STAFF,
                        SANGUINESTI_STAFF,
                        DEXTEROUS_PRAYER_SCROLL,
                        ARCANE_PRAYER_SCROLL,
                        ELDER_MAUL
                )));

        menus.put(MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
                Arrays.asList(

                        ARCANE_SPIRIT_SHIELD,
                        ELDER_MAUL,

                        //Rare
                        DRAGON_CLAWS,
                        ARMADYL_GODSWORD,
                        GHRAZI_RAPIER,
                        SPECTRAL_SPIRIT_SHIELD,
                        HEAVY_BALLISTA,
                        TOXIC_STAFF_OF_THE_DEAD,
                        SERPENTINE_HELM
                )));

        System.out.println(ClientConstants.CLIENT_NAME + " has loaded " + menus.size() + "x menu hovers.");
    }

    public static int drawType() {
        if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 500 && MouseHandler.mouseY > 0
            && MouseHandler.mouseY < 300) {
            return 1;
        }
        return 0;
    }

    public static boolean shouldDraw(int id) {
        return menus.get(id) != null;
    }

    public static boolean showMenu;

    public static String hintName;

    public static int hintId;

    public static int displayIndex;

    public static long displayDelay;

    public static int[] itemDisplay = new int[4];

    private static int lastDraw;

    public static void reset() {
        showMenu = false;
        hintId = -1;
        hintName = "";
    }

    public static boolean canDraw() {
		/*if (Client.singleton.menuActionRow < 2 && Client.singleton.itemSelected == 0
				&& Client.singleton.spellSelected == 0) {
			return false;
		}*/
        if (Client.instance.menuActionText[Client.instance.menuActionRow] != null) {
            if (Client.instance.menuActionText[Client.instance.menuActionRow].contains("Walk")) {
                return false;
            }
        }
        if (Client.instance.tooltip != null && (Client.instance.tooltip.contains("Walk") || Client.instance.tooltip.contains("World"))) {
            return false;
        }
        if (Client.instance.isMenuOpen) {
            return false;
        }
        if (hintId == -1) {
            return false;
        }
        return showMenu;
    }

    public static void drawHintMenu() {
        int cursor_x = MouseHandler.mouseX;
        int cursor_y = MouseHandler.mouseY;
        if (!canDraw()) {
            return;
        }

        if (Client.instance.isResized()) {
            if (MouseHandler.mouseY < Client.canvasHeight - 450
                && MouseHandler.mouseX < Client.canvasWidth - 200) {
                return;
            }
            cursor_x -= 100;
            cursor_y -= 50;
        }
        if (!Client.instance.isResized()) {
            if (MouseHandler.mouseY < 210 || MouseHandler.mouseX < 561) {
            } else {
                cursor_x -= 516;
                cursor_y -= 158;
            }
            if (MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
                cursor_x -= 60;

            }
            if (MouseHandler.mouseX > 685) {
                cursor_x -= 80;
            }
        }

        if (lastDraw != hintId) {
            lastDraw = hintId;
            itemDisplay = new int[4];
        }

        HoverMenu menu = menus.get(hintId);
        if (menu != null) {
            String[] text = split(menu.text).split("<br>");

            int height = (text.length * 12) + (menu.items != null ? 40 : 0);

            cursor_x += 15;
            int width = (16 + text[0].length() * 5) + (menu.items != null ? 30 : 0);
            if (!Client.instance.isResized()) {
                if (drawType() == 1) {
                    if (width + cursor_x > 500) {
                        cursor_x = 500 - width;
                    }
                } else {
                    if (width + cursor_x > 250) {
                        cursor_x = 245 - width;
                    }

                    if (height + cursor_y > 250) {
                        cursor_y = 250 - height;
                    }
                }
            }

            Rasterizer2D.draw_rect_outline(cursor_x, cursor_y + 5, width + text[0].length(), 26 + height, 0x383023);
            Rasterizer2D.draw_filled_rect(cursor_x, cursor_y + 6, width + text[0].length(), 24 + height, 0x534a40, 200);

            Client.smallFont.draw("<col=ff9040>" + hintName, cursor_x + 4, cursor_y + 19, TEXT_COLOUR, 1);
            int y = 0;

            for (String string : text) {
                Client.smallFont.draw(string, cursor_x + 4, cursor_y + 35 + y, TEXT_COLOUR, 1);
                y += 12;
            }

            if (menu.items != null) {
                int spriteX = 10;

                if (System.currentTimeMillis() - displayDelay > 300) {
                    displayDelay = System.currentTimeMillis();
                    displayIndex++;
                    if (displayIndex == menu.items.size()) {
                        displayIndex = 0;
                    }

                    if (menu.items.size() <= 4) {
                        for (int i = 0; i < menu.items.size(); i++) {
                            itemDisplay[i] = menu.items.get(i);
                        }
                    } else {
                        if (displayIndex >= menu.items.size() - 1) {
                            displayIndex = menu.items.size() - 1;
                        }
                        int next = menu.items.get(displayIndex);
                        if (itemDisplay.length - 1 >= 0)
                            System.arraycopy(itemDisplay, 1, itemDisplay, 0, itemDisplay.length - 1);
                        itemDisplay[3] = next;
                    }
                }

                for (int id : itemDisplay) {
                    if (id < 1) continue;
                    SimpleImage item = ItemDefinition.getSprite(id,1, 0);
                    if (item != null) {
                        item.drawSprite(cursor_x + spriteX, cursor_y + 35 + y);
                        spriteX += 40;
                    }
                }
            }
        }
    }

    private static String split(String text) {
        StringBuilder string = new StringBuilder();

        int size = 0;

        for (String s : text.split(" ")) {
            string.append(s).append(" ");
            size += s.length();
            if (size > 20) {
                string.append("<br>");
                size = 0;
            }
        }
        return string.toString();
    }

}
