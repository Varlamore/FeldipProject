package com.cryptic.cache.graphics.widget.impl;

import com.google.common.collect.Lists;
import com.cryptic.Client;
import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.cache.graphics.widget.Widget;
import com.cryptic.cache.graphics.widget.impl.teleport.TeleportInfo;

import java.util.List;

/**
 * This class represents the world's teleporting manager.
 *
 * @author Zerikoth | 27 okt. 2020 : 16:12
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>}
 *
 * Favorites/recent added by @author Ynneh
 */
public class TeleportWidget extends Widget {

    public static void unpack(AdvancedFont[] font) {
        teleportInterface(font);
    }

    public static final int STARTING_IMAGE_INDEX = 29095;

    public static final int ENDING_IMAGE_INDEX = 29135; // increment if needed

    /**
     * Support for recent/favorites
     */
    private static List<TeleportInfo> recent_teleports = Lists.newLinkedList(), favorite_teleports = Lists.newLinkedList();

    public static List<TeleportInfo> getRecentTeleports() {
        return recent_teleports;
    }

    public static List<TeleportInfo> getFavoriteTeleports() {
        return favorite_teleports;
    }

    public static void createNewList(boolean favs) {
        if (favs)
            favorite_teleports = Lists.newLinkedList();
        else
            recent_teleports = Lists.newLinkedList();
    }

    /**
     * Main
     * @param font
     */
    private static void teleportInterface(AdvancedFont[] font) {
        Widget main = addInterface(29050);
        addSpriteLoader(29051, 974);
        addHoverButton(29063, 981, 115, 24, "Settings", -1, 29064, 1);
        addHoveredButton(29064, 982, 115, 24, 29065);
        addSpriteLoader(29066, 990);
        closeButton(29175, 107, 108, false);
        main.totalChildren(21);
        main.child(0, 29051, 7, 7);
        main.child(8, 29090, 216 - 100, 62);
        main.child(9, 29063, 19, 281);
        main.child(10, 29064, 19, 281);
        main.child(11, 29066, 150, 273);
        main.child(19, 29078, 213, 17);
        main.child(20, 29175, 478, 13);

        final int[] CATEGORY_IDS = new int[] { 991, 985, 986, 987, 980, 989, 988 };
        final String[] CATEGORY_NAMES = new String[] { "Favourite", "Recent", "PvP", "PvM", "Bossing", "Minigames", "Other" };
        int childStart = 1;
        int yPos = 64;
        for (int i = 0; i < CATEGORY_IDS.length; i++) {
            addButton(29055 + i, CATEGORY_IDS[i], "Select");
            main.child(childStart, 29055 + i, 19, yPos);
            childStart++;
            yPos += 30;
        }
        int childStart1 = 12;
        int yPos1 = 70;
        for (int i = 0; i < CATEGORY_NAMES.length; i++) { // could of been done in the loop above, but i did it after i added plenty of other stuff(by mistake) so i would of had to re-order all the ids.
            addText(29070 + i, CATEGORY_NAMES[i], font, 1, 0xffffff, false, false);
            main.child(childStart1, 29070 + i, 50, yPos1);
            childStart1++;
            yPos1 += 30;
        }
        addText(29078, "World Teleports - ", font, 2, 0xff981f, false, true); //Favorite is the default tab.
        Widget scroll = addTabInterface(29090);
        scroll.width = 265 + 100;
        scroll.height = 208;
        scroll.scrollMax = 209;
    }

    public static int[][] sprite_ids = {
            {1001, 1000, 999, 998, 997, 995, 994, 993, 992, 1101},//pvp
            {1008, 1007, 1006, 1005, 1004, 1003, 1002, 1043, 1092, 1095, 1094, 1090, 1158, 1159, 1160, 1161, 1162, 1163, 1190, 1009, 1448, 1449, 1814, 1818, 1820},//pvm
            {1031, 1030, 1029, 1028, 1027, 1026, 1025, 1024, 1023, 1021, 1020, 1018, 1017, 1016, 1042, 1044, 1015, 1104, 1019, 1022, 1447, 1758, 1795, 1812, 1819},//bossing
            {1014, 1012, 1011, 1808, 1804},//minigames
            {1093, 1096, 1097, 1116, 1450, 1816, 1817, 1806, 1798, 1815, 1807, 1799, 1796, 1794, 1797, 1802, 1810, 1801, 1803, 1809, 1101, 1805},//other
     };

    public static String[][] location_names = {
            {"Bandit Camp", "Chaos Temple", "Demonic Ruins", "East Dragons", "  Graveyard", "   Magebank", "   Rev Caves", "    The Gate", "West Dragons", "  Black Chins"},//pvp
            {"        Cows", " Dagannoths", " Experiments", "  Lizardmen", "  Rock Crabs", "Skele Wyverns", "        Yaks", "Smoke Devils", " Slayer Tower", "Brimhaven Du", " Taverley Du", " Catacombs", "  Sand Crabs", "  Fire Giants", "Slayer Strong", "Rellekka Du", "  Dark Beast", "Kalphite Lair", "Ancient Cave", "Wyvern Cave", "Karuulm Du", "    Lithkren", "Lumbridge Sw", "Brine Rat Cav", "Mos Le'Harm"},//pvm
            {"  Callisto", "   Cerberus", "   Chaos Fan", "  Corp Beast", "  Crazy Arch", "  Dagg Kings", "Demon Gorillas", "         GWD", "         KBD", "     Kraken", "     Shaman", "     Thermo", "   Venenatis", "      Vet'ion", "      Scorpia", "   Chaos Ele", "      Zulrah", "   Vorkath", "   World boss", "          KQ", "Giant Mole", "Alchy Hydra", "Barrelchest", "Corrupted Ne", "Raids Area"},//bossing
            {"     Barrows", "    Fight Cave", "   Magebank", "Warriors Guild", "Pest Control"},//minigames
            {"Port Piscaril", "Gnome Agility", " Barb Agility", "Farming Area", "Catherby", "Karamja", "Lunar Isle", "Tzhaar City", "Edgevile", "Lumbridge", "Varrock", "Falador", "Camelot", "Ardougne", "Canifis", "Keldagrim", "Yanille", "Fishing Areas", "Mining Areas", "Woodcutting", "Hunter Areas", "Smithing Anvil"},//other
    };

    /**
     * This method handles the teleports for the teleportation interface.
     *
     * @param pageIndex The teleport category index.
     */
    public static void handleTeleportTab(int pageIndex) {
        Widget widget = Widget.cache[29090];

        Client.currentPageId = pageIndex;

        int spritePositionX = 0;

        int spritePositionY = 0;

        int offset = pageIndex - 2;

        switch (pageIndex) {
            case 0:
            case 1:
                createCustomTable(widget, spritePositionX, spritePositionY, pageIndex == 0);
                return;

            default:
                createTable(widget, spritePositionX, spritePositionY, sprite_ids[offset], location_names[offset]);
                break;
        }
        return;
    }

    private static void createCustomTable(Widget widget, int spritePositionX, int spritePositionY, boolean favorite) {
        List<TeleportInfo> info = favorite ? favorite_teleports : recent_teleports;
        int teleportSize = info.size();
        widget.totalChildren(teleportSize * 2);
        spritePositionX = 35;
        spritePositionY = 0;
        for (int teleportSpriteIndex = 0; teleportSpriteIndex < teleportSize; teleportSpriteIndex++) {
            if (teleportSpriteIndex % 4 == 0 && teleportSpriteIndex != 0) {
                spritePositionX = 35;
                spritePositionY += 68;
            } else {
                if (teleportSpriteIndex != 0) {
                    spritePositionX += 83;
                }
            }
            if (teleportSize > 0) {
                Widget.addButtonWithMenu(STARTING_IMAGE_INDEX + teleportSpriteIndex, info.get(teleportSpriteIndex).spriteId, favorite ? new String[] { "Teleport", null, "Remove from favorites", null, null } : new String[] { "Teleport", "Add to favorites", null, null, null });
                Widget.cache[STARTING_IMAGE_INDEX + teleportSpriteIndex].width = 40;
                Widget.cache[STARTING_IMAGE_INDEX + teleportSpriteIndex].height = 65;
                widget.child(teleportSpriteIndex, STARTING_IMAGE_INDEX + teleportSpriteIndex, spritePositionX, spritePositionY);
            }
        }
        int x1 = 38;
        int y1 = 50;
        AdvancedFont[] fonts = {Client.smallFont, Client.regularFont, Client.boldFont, Client.fancyFont};
        for (int teleportNameIndex = 0; teleportNameIndex < teleportSize; teleportNameIndex++) {
            if (teleportNameIndex % 4 == 0 && teleportNameIndex != 0) {
                x1 = 38;
                y1 += 68;
            } else {
                if (teleportNameIndex != 0) {
                    x1 += 83;
                }
            }
            Widget.addText(29195 + teleportNameIndex, info.get(teleportNameIndex).name, fonts, 1, 0xffffff, false, false);
            widget.child(teleportSize + teleportNameIndex, 29195 + teleportNameIndex, x1, y1);
        }
        if (info.size() > 12) {
            int offset = 69;
            int columns = spritePositionY;
            widget.scrollMax = columns + offset;
        } else {
            widget.scrollMax = widget.height + 1;
        }
    }

    /**
     * Creates teleport table for required teleports
     * @param widget
     * @param spritePositionX
     * @param spritePositionY
     * @param sprites
     * @param locations
     */
    private static void createTable(Widget widget, int spritePositionX, int spritePositionY, int[] sprites, String[] locations) {
        widget.totalChildren(sprites.length * 2);
        spritePositionX = 35;
        spritePositionY = 0;
        for (int i = 0; i < sprites.length; i++) {
            if (i % 4 == 0 && i != 0) {
                spritePositionX = 35;
                spritePositionY += 68;
            } else {
                if (i != 0) {
                    spritePositionX += 83;
                }
            }

            Widget.addButtonWithMenu(STARTING_IMAGE_INDEX + i, sprites[i], new String[]{"Teleport", "Add to favourites", null, null, null});
            Widget.cache[STARTING_IMAGE_INDEX + i].width = 40;
            Widget.cache[STARTING_IMAGE_INDEX + i].height = 65;
            widget.child(i, STARTING_IMAGE_INDEX + i, spritePositionX, spritePositionY);
        }

        int x1 = 38;
        int y1 = 50;
        AdvancedFont[] fonts = {Client.smallFont, Client.regularFont, Client.boldFont, Client.fancyFont};
        for (int i = 0; i < locations.length; i++) {
            if (i % 4 == 0 && i != 0) {
                x1 = 38;
                y1 += 68;
            } else {
                if (i != 0) {
                    x1 += 83;
                }
            }
            Widget.addText(29195 + i, String.valueOf(locations[i]), fonts, 1, 0xffffff, false, false);
            widget.child(sprites.length + i, 29195 + i, x1, y1);
        }
        if (sprites.length > 12) {
            int offset = 69;
            int columns = spritePositionY;
            widget.scrollMax = columns + offset;
        } else {
            widget.scrollMax = widget.height + 1;
        }
    }

    public static void removeFavoriteTeleport(int spriteId) {
        favorite_teleports.removeIf(f -> f.spriteId == spriteId);
        TeleportWidget.handleTeleportTab(0);
    }
}
