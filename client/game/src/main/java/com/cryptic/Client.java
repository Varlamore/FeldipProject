package com.cryptic;

import com.cryptic.audio.ObjectSound;
import com.cryptic.audio.StaticSound;
import com.cryptic.cache.def.FloorOverlayDefinition;
import com.cryptic.cache.def.FloorUnderlayDefinition;
import com.cryptic.cache.def.anim.SpotAnimation;
import com.cryptic.cache.def.graphics.SpriteData;
import com.cryptic.cache.graphics.*;
import com.cryptic.cache.graphics.font.*;
import com.cryptic.cache.graphics.widget.impl.tob.NightmareHealth;
import com.cryptic.camera.Camera;
import com.cryptic.camera.CameraUtils;
import com.cryptic.camera.impl.FollowCameraAdvanced;
import com.cryptic.camera.impl.FollowCameraSimple;
import com.cryptic.camera.impl.StaticCamera;
import com.cryptic.collection.node.NodeDeque;
import com.cryptic.engine.keys.KeyEventProcessorImpl;
import com.cryptic.js5.Js5ArchiveIndex;
import com.cryptic.js5.Js5List;
import com.cryptic.js5.Js5System;
import com.cryptic.js5.disk.ArchiveDisk;
import com.cryptic.js5.disk.ArchiveDiskActionHandler;
import com.cryptic.js5.net.JagexNetThread;
import com.cryptic.js5.util.ArchiveLoader;
import com.cryptic.js5.util.Js5ConfigType;
import com.cryptic.net.tcp.packet.Packet;
import com.cryptic.net.tcp.packet.PacketBit;
import com.cryptic.net.tcp.TcpConnectionMessage;
import com.cryptic.net.tcp.buffer.ServerConnection;
import com.cryptic.net.tcp.pow.ProofOfWorkRequester;
import com.cryptic.net.tcp.secure.SecureRandomFuture;
import com.google.common.primitives.Doubles;
import com.cryptic.cache.Archive;
import com.cryptic.cache.config.VariableBits;
import com.cryptic.cache.config.VariablePlayer;
import com.cryptic.collection.node.Node;
import com.cryptic.cache.def.AreaDefinition;
import com.cryptic.cache.def.ItemDefinition;
import com.cryptic.cache.def.ObjectDefinition;
import com.cryptic.cache.def.NpcDefinition;
import com.cryptic.cache.graphics.dropdown.DropdownMenu;
import com.cryptic.cache.graphics.fading_screen.BlackFadingScreen;
import com.cryptic.cache.graphics.fading_screen.FadingScreen;
import com.cryptic.cache.graphics.textures.TextureProvider;
import com.cryptic.cache.graphics.widget.*;
import com.cryptic.cache.graphics.widget.impl.*;
import com.cryptic.cache.graphics.widget.impl.teleport.NewTeleportInterface;
import com.cryptic.cache.graphics.widget.impl.teleport.TeleportInfo;
import com.cryptic.cache.graphics.widget.option_menu.OptionMenu;
import com.cryptic.cache.graphics.widget.option_menu.OptionMenuInterface;
import com.cryptic.draw.AbstractRasterProvider;
import com.cryptic.draw.Rasterizer2D;
import com.cryptic.draw.rasterizer.Clips;
import com.cryptic.engine.GameEngine;
import com.cryptic.engine.impl.MouseWheelHandler;
import com.cryptic.entity.*;
import com.cryptic.entity.HealthBar;
import com.cryptic.entity.Item;
import com.cryptic.entity.Player;
import com.cryptic.entity.Renderable;
import com.cryptic.entity.model.Mesh;
import com.cryptic.entity.model.IdentityKit;
import com.cryptic.entity.model.Model;
import com.cryptic.instruction.InstructionArgs;
import com.cryptic.instruction.InstructionProcessor;
import com.cryptic.io.Buffer;
import com.cryptic.io.PacketSender;
import com.cryptic.model.EffectTimer;
import com.cryptic.model.content.*;
import com.cryptic.model.content.account.AccountManager;
import com.cryptic.model.content.prayer.Save;
import com.cryptic.model.content.VarbitManager;
import com.cryptic.model.content.prayer.PrayerSystem;
import com.cryptic.model.content.hoverMenu.HoverMenuManager;
import com.cryptic.model.settings.Settings;
import com.cryptic.net.tcp.buffer.BufferedConnection;
import com.cryptic.net.tcp.isaac.IsaacCipher;
import com.cryptic.net.ServerToClientPackets;
import com.cryptic.engine.impl.MouseHandler;
import com.cryptic.engine.impl.KeyHandler;
import com.cryptic.scene.*;
import com.cryptic.scene.Projectile;
import com.cryptic.scene.object.GroundDecoration;
import com.cryptic.scene.object.SpawnedObject;
import com.cryptic.scene.object.Wall;
import com.cryptic.scene.object.WallDecoration;
import com.cryptic.sign.SignLink;
import com.cryptic.cache.def.anim.SequenceDefinition;

import com.cryptic.util.*;
import com.cryptic.util.status.ChatCrown;
import com.google.common.primitives.Ints;
import jagex.PacketBuffer;
import jagex.class0;
import jagex.class7;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.api.widgets.WidgetID;
import net.runelite.rs.api.RSClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import com.cryptic.draw.Rasterizer3D;

import java.util.Deque;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cryptic.cache.graphics.widget.Widget.OPTION_MENU;
import static com.cryptic.net.ServerToClientPackets.SEND_AREA_SOUND;
import static com.cryptic.net.ServerToClientPackets.UPDATE_RECENT_TELEPORT;
import static com.cryptic.scene.SceneGraph.pitchRelaxEnabled;
import static com.cryptic.util.Utils.longForName;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.rs.api.*;
import org.slf4j.Logger;

@Slf4j
public class Client extends GameEngine implements RSClient {

    public static AbstractRasterProvider rasterProvider;

    private final HashSet<Widget> parallelWidgetList = new HashSet<>();
    public boolean noclip;

    private void drawParallelWidgets() {
        if (this.parallelWidgetList.size() == 0)
            return;
        for (Widget widget : this.parallelWidgetList) {
            if (widget != null) {
                int xPosition = 512 / 2 - widget.width / 2;
                int yPosition = 334 / 2 - widget.height / 2;
                if (widget.id == HealthHud.WIDGET_ID) {
                    xPosition = !isResized() ? 0 : getViewportWidth() / 2 - 358;
                    yPosition = 0;
                }
                drawInterface(widget, xPosition, yPosition, 0);
            }
        }
    }

    public static int currentPageId;

    /**
     * The current skill being practised.
     */
    private int currentSkill = -1;

    /**
     * The player's total exp
     */
    public long totalExperience;

    public static boolean xpLocked;

    public static boolean renderself;

    public WorldPoint getWorldPoint() {
        int x = next_region_start + (local_player.x - 6 >> 7);
        int y = next_region_end + (local_player.y - 6 >> 7);
        return new WorldPoint(x, y, plane);
    }

    private String depositBoxOptionFirst = "1";

    public static int npcPetId = -1;

    private AccountManager accountManager;

    private final StatusBars bars = new StatusBars();

    /**
     * Speed of camera rotation.
     */
    public static String cameraSpeed = "SLOW";

    public static String osName = "";
    public String tooltip;
    private final ArrayList<IncomingHit> expectedHit;

    public static SimpleImage[] fadingScreenImages = new SimpleImage[8];
    private FadingScreen fadingScreen;
    public boolean darkenEnabled;
    public int darkenOpacity;

    static boolean debug_packet_info = false; // This is for debugging packet info
    public boolean isDisplayed = true;
    public Announcement broadcast;
    public static String broadcastText;
    String selectedMsg = "";

    public void changeColour(int id, int colour) {
        int i19 = colour >> 10 & 0x1f;
        int i22 = colour >> 5 & 0x1f;
        int l24 = colour & 0x1f;
        Widget.cache[id].textColour = (i19 << 19) + (i22 << 11) + (l24 << 3);
    }

    public int getMyPrivilege() {
        return myPrivilege;
    }

    public enum RuneType {
        AIR(556),
        WATER(555),
        EARTH(557),
        FIRE(554),
        MIND(558),
        CHAOS(562),
        DEATH(560),
        BLOOD(565),
        COSMIC(564),
        NATURE(561),
        LAW(563),
        BODY(559),
        SOUL(566),
        ASTRAL(9075),
        MIST(324),
        MUD(4698),
        DUST(4696),
        LAVA(4699),
        STEAM(4694),
        SMOKE(4697),
        WRATH(21880);

        private final int id;

        RuneType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        /**
         * @param rune
         * @return
         */
        public static RuneType forId(int rune) {
            for (RuneType type : RuneType.values()) {
                if (type.getId() == rune) {
                    return type;
                }
            }
            return null;
        }
    }

    private static final int[][] runePouch = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 }, { -1, -1 } };
    private static final int[][] vengeance = new int[][] { { 560, 999999999 }, { 557, 999999999 },
            { 9075, 999999999 } };
    private static final int[][] iceSack = new int[][] { { 555, 999999999 }, { 560, 999999999 }, { 565, 999999999 } };
    private static final int[][] bindSack = new int[][] { { 555, 999999999 }, { 557, 999999999 }, { 561, 999999999 } };
    private static final int[][] snareSack = new int[][] { { 555, 999999999 }, { 557, 999999999 }, { 561, 999999999 } };
    private static final int[][] entangleSack = new int[][] { { 555, 999999999 }, { 557, 999999999 },
            { 561, 999999999 } };
    private static final int[][] teleportSack = new int[][] { { 563, 999999999 }, { 562, 999999999 },
            { 560, 999999999 } };

    private static void handleRunePouch(String text, int frame) {
        if (frame != 49999)
            return;
        // System.out.println("RP decoding : " + text);
        if (!(text.startsWith("#") && text.endsWith("$"))) {
            return;
        }
        text = text.replace("#", "");
        text = text.replace("$", "");
        String[] runes = text.split("-");
        for (int index = 0; index < runes.length; index++) {
            String[] args = runes[index].split(":");
            // We only want the id and amount if it's not empty, otherwise it will cause a
            // stacktrace trying to parse it.
            if (!args[0].equals("") && !args[1].equals("")) {
                int id = Integer.parseInt(args[0]);
                int amt = Integer.parseInt(args[1]);
                if (id < 0 || amt < 0) {
                    return;
                }
                runePouch[index][0] = id;
                runePouch[index][1] = amt;
            } else {
                // System.out.println("Empty rune pouch rune slot");
                return;
            }
        }
    }

    public enum ScreenMode {
        FIXED,
        RESIZABLE,
        FULLSCREEN
    }

    private int widgetId = 0;
    public List<TradeOpacity> tradeSlot = new ArrayList<>();
    private final SimpleImage[] skill_sprites = new SimpleImage[SkillConstants.SKILL_COUNT];

    public void resetsidebars_teleportinterface() {
        for (int i = 0; i < NewTeleportInterface.CATEGORY_NAMES.length; i++) {
            Widget.cache[88005 + i].disabledSprite = SpriteCache.get(2064);

        }
    }

    public void openjustfavorites() {
        resetsidebars_teleportinterface();
        Widget.cache[88005].enabledSprite = SpriteCache.get(2065);
        Widget.cache[88005].disabledSprite = SpriteCache.get(2065);
    }

    public void stopMidi() {

    }

    private void saveMidi(boolean flag, byte[] abyte0) {

    }

    private void adjustVolume(boolean updateMidi, int volume) {

    }

    // Timers
    public List<EffectTimer> effects_list = new ArrayList<>();

    public void addEffectTimer(EffectTimer et) {

        // Check if exists.. If so, update delay.
        for (EffectTimer timer : effects_list) {
            if (timer.getSprite() == et.getSprite()) {
                timer.setSeconds(et.getSecondsTimer().secondsRemaining());
                return;
            }
        }

        effects_list.add(et);
    }

    public void drawEffectTimers() {
        int offsetY = (18 * totalMessages) + (broadcastActive() ? 18 : 0);
        if (rebootTimer != 0) {
            offsetY += 18;
        }
        int yDraw = canvasHeight - 190 - offsetY;
        int xDraw = 1;
        for (int index = 0; index < 500; index++) {
            if (chatMessages[index] != null) {
                ChatMessage msg = chatMessages[index];
                String message = msg.getMessage();
                boolean broadcast = message.contains("<link");
                if (broadcast) {
                    yDraw += 15;
                }
            }
        }
        Iterator<EffectTimer> iterator = effects_list.iterator();
        int count = 0; // Counter to keep track of the number of items processed in the current row
        boolean stackRows = effects_list.size() >= 4; // Determine whether to stack rows

        while (iterator.hasNext()) {
            EffectTimer timer = iterator.next();
            if (timer.getSecondsTimer().finished()) {
                iterator.remove();
                continue;
            }

            SimpleImage sprite = SpriteCache.get(timer.getSprite());

            if (sprite != null) {
                int row = 0; // Default to the first row
                int col = count % 2; // Calculate the column number (0 or 1)

                if (stackRows) {
                    row = count / 2; // Calculate the row number (0-based) if stacking rows
                }

                int xDrawPosition = (xDraw + col * 80) - 8; // Adjust xDraw based on column
                int yDrawPosition = yDraw - row * 25; // Adjust yDraw based on row

                sprite.drawAdvancedSprite(xDrawPosition + 12, yDrawPosition);
                smallFont.draw(calculateInMinutes(timer.getSecondsTimer().secondsRemaining()),
                        xDrawPosition + 40, yDrawPosition + 13, 0xFF8C00, -1);

                count++; // Increment the count for the current row

                if (stackRows && count >= 2) {
                    count = 0; // Reset the count for the next row
                    yDraw -= 25; // Move to the next row
                }
            }
        }
        tradeSlot.removeIf(TradeOpacity::cycle);
    }

    public static void teleport(int x, int z, int level) {
        String text = "clienttele " + x + " " + z + " " + level;
        packetSender.sendCommand(text);
    }

    private String calculateInMinutes(int paramInt) {
        int i = (int) Math.floor(paramInt / 60);
        int j = paramInt - i * 60;
        String str1 = String.valueOf(i);
        String str2 = String.valueOf(j);
        if (j < 10) {
            str2 = "0" + str2;
        }
        if (i < 10) {
            str1 = "0" + str1;
        }
        return str1 + ":" + str2;
    }

    /**
     * Draws information about our current target during combat.
     */

    private final SecondsTimer lobbyTimer = new SecondsTimer();

    private void drawLobbyTimer() {
        if (!lobbyTimer.finished()) {
            // System.out.println("sending timer: "+lobbyTimer.secondsRemaining());
            // Convert to milliseconds
            int timeInMilliseconds = lobbyTimer.secondsRemaining() * 1000;

            // Get minutes and seconds
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds);
            timeInMilliseconds -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds);

            String timeAsString = seconds > 9 ? "0" + minutes + ":" + seconds : minutes + ":0" + seconds;

            instance.sendString(timeAsString, 57102);
        }
    }

    public static void setZoom(int zoomValue) {
        int value = Ints.constrainToRange(zoomValue, 166, 900);
        Client.field625 = value;
        if (Client.field625 <= 0) {
            Client.field625 = 256;
        }

        Client.field626 = value - 51;
        if (Client.field626 <= 0) {
            Client.field626 = 205;
        }
    }

    // Removes the chat box
    public static boolean showChatComponents = true;

    // When set to false all tab interfaces are hidden
    public static boolean showTabComponents = true;

    private final NumberFormat format = NumberFormat.getInstance(Locale.US);

    int frameValueW = 765, frameValueH = 503;

    public void frameMode(boolean resizable) {

        setResized(resizable);

        Bounds bounds = getFrameContentBounds();
        canvasWidth = !isResized() ? 765 : bounds.highX;
        canvasHeight = !isResized() ? 503 : bounds.highY;

        setMaxCanvasSize(canvasWidth, canvasHeight);
        ResizeableChanged event = new ResizeableChanged();
        event.setResized(resizable);
        callbacks.post(event);

        showChatComponents = !isResized() || showChatComponents;
        showTabComponents = !isResized() || showTabComponents;
    }

    private final Stopwatch frameDelay = new Stopwatch();
    private final Stopwatch loggedInWatch = new Stopwatch();

    /**
     * Cuts a string into more than one line if it exceeds the specified max width.
     *
     * @param font
     * @param string
     * @param maxWidth
     * @return
     */
    public static String[] splitString(AdvancedFont font, String prefix, String string, int maxWidth, boolean ranked) {
        maxWidth -= font.getTextWidth(prefix) + (ranked ? 14 : 0);
        if (font.getTextWidth(prefix + string) + (ranked ? 14 : 0) <= maxWidth) {
            return new String[] { string };
        }
        String line = "";
        String[] cut = new String[2];
        boolean split = false;
        char[] characters = string.toCharArray();
        int space = -1;
        for (int index = 0; index < characters.length; index++) {
            char c = characters[index];
            line += c;
            if (c == ' ') {
                space = index;
            }
            if (!split) {
                if (font.getTextWidth(line) + 10 > maxWidth) {
                    if (space != -1 && characters[index - 1] != ' ') {
                        cut[0] = line.substring(0, space);
                        line = line.substring(space);
                    } else {
                        cut[0] = line;
                        line = "";
                    }
                    split = true;
                }
            }
        }
        if (line.length() > 0) {
            cut[1] = line;
        }
        return cut;
    }

    public boolean getMousePositions() {
        if (mouseInRegion(canvasWidth - (canvasWidth <= 1000 ? 240 : 420),
                canvasHeight - (canvasWidth <= 1000 ? 90 : 37), canvasWidth, canvasHeight)) {
            return false;
        }
        if (showChatComponents) {
            if (settings[ConfigUtility.TRANSPARENT_CHAT_BOX_ID] == 1 && resized) {
                if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 494 && MouseHandler.mouseY > canvasHeight - 175
                        && MouseHandler.mouseY < canvasHeight) {
                    return true;
                } else {
                    if (MouseHandler.mouseX > 494 && MouseHandler.mouseX < 515
                            && MouseHandler.mouseY > canvasHeight - 175
                            && MouseHandler.mouseY < canvasHeight) {
                        return false;
                    }
                }
            } else if (settings[ConfigUtility.TRANSPARENT_CHAT_BOX_ID] == 0) {
                if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 519 && MouseHandler.mouseY > canvasHeight - 175
                        && MouseHandler.mouseY < canvasHeight) {
                    return false;
                }
            }
        }
        if (mouseInRegion(canvasWidth - 216, 0, canvasWidth, 172)) {
            return false;
        }
        if (settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
            if (MouseHandler.mouseX > 0 && MouseHandler.mouseY > 0 && MouseHandler.mouseY < canvasWidth
                    && MouseHandler.mouseY < canvasHeight) {
                return MouseHandler.mouseX < canvasWidth - 242 || MouseHandler.mouseY < canvasHeight - 335;
            }
            return false;
        }
        if (showTabComponents) {
            if (canvasWidth > 1000) {
                return (MouseHandler.mouseX < canvasWidth - 420 || MouseHandler.mouseX > canvasWidth
                        || MouseHandler.mouseY < canvasHeight - 37 || MouseHandler.mouseY > canvasHeight)
                        && (MouseHandler.mouseX <= canvasWidth - 225 || MouseHandler.mouseX >= canvasWidth
                                || MouseHandler.mouseY <= canvasHeight - 37 - 274
                                || MouseHandler.mouseY >= canvasHeight);
            } else {
                return (MouseHandler.mouseX < canvasWidth - 210 || MouseHandler.mouseX > canvasWidth
                        || MouseHandler.mouseY < canvasHeight - 74 || MouseHandler.mouseY > canvasHeight)
                        && (MouseHandler.mouseX <= canvasWidth - 225 || MouseHandler.mouseX >= canvasWidth
                                || MouseHandler.mouseY <= canvasHeight - 74 - 274
                                || MouseHandler.mouseY >= canvasHeight);
            }
        }
        return true;
    }

    public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
        return MouseHandler.mouseX >= x1 && MouseHandler.mouseX <= x2 && MouseHandler.mouseY >= y1
                && MouseHandler.mouseY <= y2;
    }

    public boolean mouseMapPosition() {
        return MouseHandler.mouseX < canvasWidth - 21 || MouseHandler.mouseX > canvasWidth || MouseHandler.mouseY < 0
                || MouseHandler.mouseY > 21;
    }

    private void drawLoadingMessages(int lines, String first, String second) {
        int width = regularFont.getTextWidth(lines == 1 ? first : second);
        int height = second == null ? 25 : 38;

        Rasterizer2D.draw_filled_rect(1, 1, width + 6, height, 0);
        Rasterizer2D.draw_filled_rect(1, 1, width + 6, 1, 0xffffff);
        Rasterizer2D.draw_filled_rect(1, 1, 1, height, 0xffffff);
        Rasterizer2D.draw_filled_rect(1, height, width + 6, 1, 0xffffff);
        Rasterizer2D.draw_filled_rect(width + 6, 1, 1, height, 0xffffff);

        regularFont.drawCenteredString("<col=ffffff>" + first, width / 2 + 5, 18);
        if (second != null) {
            regularFont.drawCenteredString("<col=ffffff>" + second, width / 2 + 5, 31);
        }
    }

    private static final long serialVersionUID = 5707517957054703648L;

    private static String intToKOrMilLongName(int i) {
        String s = String.valueOf(i);
        for (int k = s.length() - 3; k > 0; k -= 3)
            s = s.substring(0, k) + "," + s.substring(k);
        if (s.length() > 8)
            s = "<col=475154>" + s.substring(0, s.length() - 8) + " million <col=ffffff>(" + s + ")";
        else if (s.length() > 4)
            s = "<col=65535>" + s.substring(0, s.length() - 4) + "K <col=ffffff>(" + s + ")";
        return " " + s;
    }

    public final String formatCoins(int coins) {
        if (coins >= 0 && coins < 10000)
            return String.valueOf(coins);
        if (coins >= 10000 && coins < 10000000)
            return coins / 1000 + "K";
        if (coins >= 10000000 && coins < 999999999)
            return coins / 1000000 + "M";
        if (coins >= 999999999)
            return "*";
        else
            return "?";
    }

    public static final byte[] ReadFile(String fileName) {
        try {
            byte[] abyte0;
            File file = new File(fileName);
            int i = (int) file.length();
            abyte0 = new byte[i];
            DataInputStream datainputstream = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(fileName)));
            datainputstream.readFully(abyte0, 0, i);
            datainputstream.close();
            return abyte0;
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
            return null;
        }
    }

    private void drawInputField(Widget child, int interfaceX, int interfaceY, int x, int y, int width, int height) {
        int clickX = MouseHandler.saveClickX, clickY = MouseHandler.saveClickY;
        if (!resized) {
            if (clickX >= 512 && clickY >= 169) {
                clickX -= 512;
                clickY -= 169;
            }
        }
        for (int row = 0; row < width; row += 12) {
            if (row + 12 > width) {
                row -= 12 - (width - row);
            }
            // Rasterizer2D.fillRectangle(x + row, y, 12, 12, 0x363227, 0);
            for (int collumn = 0; collumn < height; collumn += 12) {
                if (collumn + 12 > height) {
                    collumn -= 12 - (height - collumn);
                }
                // Rasterizer2D.fillRectangle(x + row, y + collumn, 12, 12, 0x363227, 0);
            }
        }
        for (int top = 0; top < width; top += 8) {
            if (top + 8 > width) {
                top -= 8 - (width - top);
            }
            // Rasterizer2D.draw_horizontal_line(x + top, y, 8, 0);
            // Rasterizer2D.draw_horizontal_line(x + top, y + height - 1, 8, 0);
        }
        for (int bottom = 0; bottom < height; bottom += 8) {
            if (bottom + 8 > height) {
                bottom -= 8 - (height - bottom);
            }
            // Rasterizer2D.draw_vertical_line(x, y + bottom, 8, 0);
            // Rasterizer2D.draw_vertical_line(x + width - 1, y + bottom, 8, 0);
        }
        String message = child.defaultText;

        if (!child.invisible) {
            if (smallFont.getTextWidth(message) > child.width - 10) {
                message = message.substring(message.length() - (child.width / 10) - 1);
            }
            if (child.displayAsterisks) {
                smallFont.draw(
                        StringUtils.toAsterisks(message)
                                + (((!child.isInFocus ? 0 : 1) & (tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""),
                        (x + 4), (y + (height / 2) + 6), child.textColour, true);
            } else {
                smallFont.draw(message + (((!child.isInFocus ? 0 : 1) & (tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""),
                        (x + 4), (y + (height / 2) + 6), child.textColour, true);
            }
            if (clickX >= x && clickX <= x + child.width && clickY >= y && clickY <= y + child.height) {
                if (!child.isInFocus && getInputFieldFocusOwner() != child) {
                    if ((MouseHandler.instance.clickMode2 == 1 && !isMenuOpen)) {
                        Widget.currentInputFieldId = child.id;
                        setInputFieldFocusOwner(child);
                        if (child.defaultText != null && child.defaultText.equals(child.defaultInputFieldText)) {
                            child.defaultText = "";
                        }
                        if (child.defaultText == null) {
                            child.defaultText = "";
                        }
                    }
                }
            }
        }
    }

    public void setInputFieldFocusOwner(Widget owner) {
        for (Widget rsi : Widget.cache)
            if (rsi != null)
                rsi.isInFocus = rsi == owner;
    }

    public Widget getInputFieldFocusOwner() {
        for (Widget rsi : Widget.cache)
            if (rsi != null)
                if (rsi.isInFocus)
                    return rsi;
        return null;
    }

    public boolean isInputFieldInFocus() {
        for (Widget rsi : Widget.cache)
            if (rsi != null)
                if (rsi.type == 16 && rsi.isInFocus)
                    return true;
        return false;
    }

    public void resetInputFieldFocus() {
        for (Widget rsi : Widget.cache)
            if (rsi != null)
                rsi.isInFocus = false;
        Widget.currentInputFieldId = -1;
    }

    private boolean menuHasAddFriend(int j) {
        if (j < 0)
            return false;
        int k = menuActionTypes[j];
        if (k >= 2000)
            k -= 2000;
        return k == 337;
    }

    private void clearHistory(int chatType) {

        // Stops the opening of the tab
        MouseHandler.saveClickX = 0;
        MouseHandler.saveClickY = 0;

        // Go through each message, compare its type..
        outerLoop: for (int i = 0; i < chatMessages.length; i++) {
            if (chatMessages[i] == null)
                continue;
            if (chatMessages[i].getType() == chatType) {

                // Don't clear this message if it was sent from another staff member.
                if (!chatMessages[i].getName().equalsIgnoreCase(local_player.username)) {
                    for (ChatCrown c : chatMessages[i].getCrowns()) {
                        if (c.isStaff()) {
                            continue outerLoop;
                        }
                    }
                }

                chatMessages[i] = null;
            }
        }
    }

    private final int[] modeNamesX = { 26, 86, 150, 212, 286, 349, 427 },
            modeNamesY = { 158, 158, 153, 153, 153, 153, 158 }, channelButtonsX = { 5, 71, 137, 203, 269, 335, 404 };

    private final String[] modeNames = { "All", "Game", "Public", "Private", "Clan", "Trade", "Report Abuse" };

    public void drawChannelButtons() {
        int yOffset = !isResized() ? 338 : canvasHeight - 165;
        SpriteCache.get(49).drawSprite(0, 143 + yOffset);
        String[] text = { "On", "Friends", "Off", "Hide" };
        int[] textColor = { 65280, 0xffff00, 0xff0000, 65535 };
        switch (cButtonCPos) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                SpriteCache.get(16)
                        .drawSprite(channelButtonsX[cButtonCPos], 143 + yOffset);
                break;
        }
        if (cButtonHPos == cButtonCPos) {
            switch (cButtonHPos) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    SpriteCache.get(17)
                            .drawSprite(channelButtonsX[cButtonHPos], 143 + yOffset);
                    break;
            }
        } else {
            switch (cButtonHPos) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    SpriteCache.get(15)
                            .drawSprite(channelButtonsX[cButtonHPos], 143 + yOffset);
                    break;
                case 6:
                    SpriteCache.get(18)
                            .drawSprite(channelButtonsX[cButtonHPos], 143 + yOffset);
                    break;
            }
        }
        int[] modes = { set_public_channel, privateChatMode, clanChatMode, tradeMode };
        for (int i = 0; i < modeNamesX.length; i++) {
            smallFont.draw(modeNames[i], modeNamesX[i], modeNamesY[i] + yOffset, 0xffffff, -1);
        }
        for (int index = 0; index < 4; index++) {
            if (ChannelText.getText(index) == null) {
                return;
            }
            switch (text[modes[index]]) {
                case "On":
                    smallFont.draw(text[modes[index]], ChannelText.getText(index).onX, 164 + yOffset,
                            textColor[modes[index]], -1);
                    break;
                case "Friends":
                    smallFont.draw(text[modes[index]], ChannelText.getText(index).friendsX, 164 + yOffset,
                            textColor[modes[index]], -1);
                    break;
                case "Off":
                    smallFont.draw(text[modes[index]], ChannelText.getText(index).offX, 164 + yOffset,
                            textColor[modes[index]], -1);
                    break;
                case "Hide":
                    smallFont.draw(text[modes[index]], ChannelText.getText(index).hideX, 164 + yOffset,
                            textColor[modes[index]], -1);
                    break;
            }
        }
    }

    private enum ChannelText {
        PUBLIC(0, 159, 148, 156, 154),
        PRIVATE(1, 225, 213, 223, -1),
        CLAN(2, 291, 280, 289, -1),
        TRADE(3, 357, 345, 355, -1);

        public int index;
        public int onX, friendsX, offX, hideX;

        ChannelText(int index, int onX, int friendsX, int offX, int hideX) {
            this.index = index;
            this.onX = onX;
            this.friendsX = friendsX;
            this.offX = offX;
            this.hideX = hideX;
        }

        public static ChannelText getText(int index) {
            for (ChannelText channelText : ChannelText.values()) {
                if (channelText.index == index) {
                    return channelText;
                }
            }
            return null;
        }
    }

    private boolean chatStateCheck() {
        return messagePromptRaised || inputDialogState != 0 || clickToContinueString != null || backDialogueId != -1
                || dialogueId != -1;
    }

    private String enter_amount_title = "Enter amount:";
    private String enter_name_title = "Enter name:";
    private String enter_amount_title2 = "";

    private void drawChatArea() {
        boolean fixed = !resized;
        boolean transparent_chat_box = settings[ConfigUtility.TRANSPARENT_CHAT_BOX_ID] == 1;

        int yOffset = !isResized() ? 338 : canvasHeight - 165;

        if (chatStateCheck()) {
            SpriteCache.get(20).drawSprite(0, yOffset);
        }

        if (showChatComponents) {
            if (settings[ConfigUtility.TRANSPARENT_CHAT_BOX_ID] == 1 && !chatStateCheck() && !fixed) {
                Rasterizer2D.draw_horizontal_line(7, 7 + yOffset, 506, 0x575757);
                Rasterizer2D.drawTransparentGradientBox(7, 7 + yOffset, 506, 135, 0, 0xFFFFFF, 20);
            } else {
                SpriteCache.get(20).drawSprite(0, yOffset);
            }
        }

        drawChannelButtons();
        // Only check for the input field if there is an active interface for better
        // performance.
        if (widget_overlay_id != -1 && this.isInputFieldInFocus()) {
            inputString = "[Click chat box to enable]";
        } else {
            // In case the user clicks the minimap, reset the input string.
            if (inputString.equals("[Click chat box to enable]")) {
                inputString = "";
            }
        }

        // In case the user clicks the chatbox, reset the input string.
        if (MouseHandler.saveClickX >= 0 && MouseHandler.saveClickX <= 522
                && MouseHandler.saveClickY >= (fixed ? 343 : canvasHeight - 165)
                && MouseHandler.saveClickY <= (fixed ? 484 : canvasHeight - 27)) {
            if (!this.isInputFieldInFocus() && inputString.equals("[Click chat box to enable]")) {
                inputString = "";
                this.resetInputFieldFocus();
            }
        }

        if (messagePromptRaised) {
            boldFont.drawCentered(inputMessage, 259, 60 + yOffset, 0, -1);
            boldFont.drawCenteredString(promptInput + "*", 259, 80 + yOffset, 128, -1);
        } else if (inputDialogState == 3 || inputDialogState == 4) {
            boldFont.drawCenteredString(enter_amount_title, 259, yOffset + 40, 0, -1);
            boldFont.drawCenteredString(enter_amount_title2, 259, yOffset + 55, 0, -1);
            boldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
        } else if (inputDialogState == 1) {
            boldFont.drawCenteredString(enter_amount_title, 259, yOffset + 60, 0, -1);
            boldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
        } else if (inputDialogState == 2) {
            boldFont.drawCenteredString(enter_name_title, 259, 60 + yOffset, 0, -1);
            boldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
        } else if (clickToContinueString != null) {
            boldFont.drawCenteredString(clickToContinueString, 259, 60 + yOffset, 0, -1);
            boldFont.drawCenteredString("Click to continue", 259, 80 + yOffset, 128, -1);
        } else if (backDialogueId != -1) {
            try {
                drawInterface(Widget.cache[backDialogueId], 20, 20 + yOffset, 0);
            } catch (Exception e) {
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
        } else if (dialogueId != -1) {
            try {
                drawInterface(Widget.cache[dialogueId], 20, 20 + yOffset, 0);
            } catch (Exception e) {
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
        } else if (showChatComponents) {
            int playerChatRows = -3;
            int totalMessages = 0;
            int shadow = (transparent_chat_box && !fixed) ? 0 : -1;
            Rasterizer2D.setDrawingArea(0, 7 + yOffset, 497, 122 + yOffset);
            for (int k = 0; k < 500; k++) {
                if (chatMessages[k] != null) {
                    ChatMessage msg = chatMessages[k];
                    String title = msg.getTitle() == null ? "" : msg.getTitle();
                    int type = msg.getType();
                    String name = title + msg.getName();
                    String message = msg.getMessage();
                    List<ChatCrown> crowns = msg.getCrowns();
                    boolean broadcast = message.contains("<link");

                    if (settings[ConfigUtility.TRANSPARENT_CHAT_BOX_ID] == 1 && !fixed) {
                        message = message.replace("<col=0>", "<col=FFFFFF>")
                                .replace("<col=255>", "<col=9090ff>")
                                .replace("<col=800000>", "<col=ef5050>")
                                .replace("<col=18626b>", "<col=3aa6b3>")
                                .replace("<col=7e5221>", "<col=b1722b>")
                                .replace("<col=7a5c66>", "<col=986f7d>");
                    }
                    int y = (70 - playerChatRows * 14) + chatScrollAmount + 6;
                    if (type == 0) {
                        int xPos = 10;
                        if (broadcast) {
                            SpriteCache.get(856).drawSprite(xPos + 62, y - 12 + yOffset);
                            xPos += 14;
                        }
                        if (chatTypeView == 0 || chatTypeView == 6) {
                            if (!broadcast) {
                                if (message.contains("[Global]")) {
                                    xPos += 48;
                                    for (ChatCrown c : crowns) {
                                        SimpleImage sprite = SpriteCache.get(c.getSpriteId());
                                        if (sprite != null) {
                                            sprite.drawAdvancedSprite(xPos + 1, y - 12 + yOffset);
                                            xPos += sprite.width + 2;
                                        }
                                    }
                                }
                                if (y > 0 && y < 210) {
                                    regularFont.draw(message, 11, y + yOffset,
                                            transparent_chat_box && !fixed ? 0xFFFFFF : 0, shadow);
                                }
                            } else if (broadcast) {
                                if (y > 0 && y < 210) {
                                    regularFont.draw("<col=004f00>Broadcast:", 10, y + yOffset - 1,
                                            transparent_chat_box && !fixed ? 0xFFFFFF : 0, shadow);
                                    regularFont.draw(message, 85, y + yOffset - 1,
                                            transparent_chat_box && !fixed ? 0xFFFFFF : 0, shadow);
                                }
                            }
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if ((type == 1 || type == 2)
                            && (set_public_channel == 0 || set_public_channel == 1 && check_username(name))) {
                        if (chatTypeView == 1 || chatTypeView == 0) {
                            int xPos = 11;

                            for (ChatCrown c : crowns) {
                                SimpleImage sprite = SpriteCache.get(c.getSpriteId());
                                if (sprite != null) {
                                    sprite.drawAdvancedSprite(xPos + 1, y - 12 + yOffset);
                                    xPos += sprite.width + 2;
                                }
                            }

                            if (y > 0 && y < 210) {
                                regularFont.draw(name + ":", xPos, y + yOffset,
                                        (transparent_chat_box && !fixed) ? 0xFFFFFF : 0, shadow);
                                xPos += regularFont.getTextWidth(name + ": ");
                                regularFont.draw(message, xPos, y + yOffset,
                                        (transparent_chat_box && !fixed) ? 0x7FA9FF : 255, shadow);
                            }
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if ((type == 3 || type == 7) && (splitPrivateChat == 0 || chatTypeView == 2)
                            && (type == 7 || privateChatMode == 0
                                    || privateChatMode == 1 && check_username(msg.getName()))) {
                        if (chatTypeView == 2 || chatTypeView == 0) {
                            boolean onIgnore = false;
                            if (msg.getName() != null) {
                                for (int count = 0; count < ignoreCount; count++) {
                                    if (ignoreListAsLongs[count] != longForName(msg.getName())) {
                                        continue;
                                    }
                                    onIgnore = true;
                                    break;
                                }
                            }
                            if (y > 0 && y < 210 && !onIgnore) {
                                int x = 11;
                                /** In chatbox **/
                                regularFont.draw("From ", x, y + yOffset,
                                        transparent_chat_box && !fixed ? 0xFFFFFF : 0, shadow);
                                x += regularFont.getTextWidth("From ");

                                for (ChatCrown c : crowns) {
                                    SimpleImage sprite = SpriteCache.get(c.getSpriteId());
                                    if (sprite != null) {
                                        sprite.drawAdvancedSprite(x + 1, y - 12 + yOffset);
                                        x += sprite.width + 2;
                                    }
                                }

                                regularFont.draw(name + ":", x, y + yOffset,
                                        (transparent_chat_box && !fixed) ? 0xFFFFFF : 0, shadow);
                                x += regularFont.getTextWidth(name + ": ");
                                regularFont.draw(message, x, y + yOffset, 0x800000, shadow);
                            }
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if (type == 4 && (tradeMode == 0 || tradeMode == 1 && check_username(name))) {
                        if (chatTypeView == 3 || chatTypeView == 0) {
                            regularFont.draw(name + " " + message, 11, y + yOffset,
                                    transparent_chat_box && !fixed ? 0xdf20ff : 0x800080, shadow);
                            playerChatRows++;
                        }
                    }

                    if (type == 5 && splitPrivateChat == 0 && privateChatMode < 2) {
                        if (chatTypeView == 2 || chatTypeView == 0) {
                            regularFont.draw(name + " " + message, 8, y + yOffset, 0x800080, shadow);
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if (type == 6 && (splitPrivateChat == 0 || chatTypeView == 2) && privateChatMode < 2) {
                        if (chatTypeView == 2 || chatTypeView == 0) {
                            if (y > 0 && y < 210) {
                                regularFont.draw("To " + msg.getName() + ":", 11, y + yOffset,
                                        (transparent_chat_box && !fixed) ? 0xFFFFFF : 0, shadow);
                                regularFont.draw(capitalizeFirstChar(message),
                                        15 + regularFont.getTextWidth("To :" + msg.getName()), y + yOffset, 0x800000,
                                        shadow);
                            }
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if (type == 8 && (tradeMode == 0 || tradeMode == 1 && check_username(name))) {
                        if (chatTypeView == 3 || chatTypeView == 0) {
                            if (y > 0 && y < 210) {
                                regularFont.draw(name + " " + message, 11, y + yOffset, 0x7e3200, shadow);
                            }
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if (type == 11 && (clanChatMode == 0 || (clanChatMode == 1 && check_username(msg.getName())))) {
                        if (chatTypeView == 11 || chatTypeView == 0) {
                            boolean onIgnore = false;
                            if (msg.getName() != null) {
                                for (int count = 0; count < ignoreCount; count++) {
                                    if (ignoreListAsLongs[count] != longForName(msg.getName())) {
                                        continue;
                                    }
                                    onIgnore = true;
                                    break;
                                }
                            }
                            if (!onIgnore) {
                                regularFont.draw(
                                        transparent_chat_box && !fixed ? message.replace("<col=9090ff>", "<col=9070ff>")
                                                : message,
                                        10, y + yOffset, 0x7e3200, shadow);
                                totalMessages++;
                                playerChatRows++;
                            }
                        }
                    }
                    if (type == 12) {
                        if (chatTypeView == 5 || chatTypeView == 0) {
                            boolean onIgnore = false;
                            if (msg.getName() != null) {
                                for (int count = 0; count < ignoreCount; count++) {
                                    if (ignoreListAsLongs[count] != longForName(msg.getName())) {
                                        continue;
                                    }
                                    onIgnore = true;
                                    break;
                                }
                            }
                            if (!onIgnore) {
                                regularFont.draw(message, 10, y + yOffset, 0x7e3200, shadow);
                                totalMessages++;
                                playerChatRows++;
                            }
                        }
                    }

                    if (type == 13 && chatTypeView == 12) {
                        if (y > 0 && y < 210) {
                            regularFont.draw(name + " " + message, 8, y + yOffset, 0x7e3200, shadow);
                        }
                        totalMessages++;
                        playerChatRows++;
                    }

                    if (type == 16) {
                        if (chatTypeView == 11 || chatTypeView == 0) {
                            regularFont.draw(message, 10, y + yOffset, 0x7e3200, shadow);
                            totalMessages++;
                            playerChatRows++;
                        }
                    }

                    if (type == 20) {
                        if (chatTypeView == 3 || chatTypeView == 0) {
                            int offsetX = 0;
                            if (this.broadcast != null && this.broadcast.hasUrl()) {
                                /*
                                 * SimpleImage sprite = SpriteCache.get(856);
                                 * if (sprite != null) {
                                 * offsetX = sprite.width + 1;
                                 * sprite.drawAdvancedSprite(11, y + yOffset - 10);
                                 * }
                                 */
                            }

                            /*
                             * adv_font_regular.draw("Broadcast:", 11 + offsetX, y + yOffset,
                             * transparent_chat_box && !fixed ? 0x16630f : 0x16630f, shadow);
                             * adv_font_regular.draw(name + "" + capitalizeFirstChar(message), 75 + offsetX,
                             * y + yOffset, transparent_chat_box && !fixed ? 0 : 0, shadow);
                             * totalMessages++;
                             * playerChatRows++;
                             */
                        }
                    }

                    if (type == 40) {
                        if (chatTypeView == 5 || chatTypeView == 0) {
                            boolean onIgnore = false;
                            if (msg.getName() != null) {
                                for (int count = 0; count < ignoreCount; count++) {
                                    if (ignoreListAsLongs[count] != longForName(msg.getName())) {
                                        continue;
                                    }
                                    onIgnore = true;
                                    break;
                                }
                            }
                            if (!onIgnore) {
                                regularFont.draw(name + " " + message, 10, y + yOffset, 0x00a7e0, shadow);
                                totalMessages++;
                                playerChatRows++;
                            }
                        }
                    }
                    if (type == 41) {
                        if (chatTypeView == 5 || chatTypeView == 0) {
                            boolean onIgnore = false;
                            if (msg.getName() != null) {
                                for (int count = 0; count < ignoreCount; count++) {
                                    if (ignoreListAsLongs[count] != longForName(msg.getName())) {
                                        continue;
                                    }
                                    onIgnore = true;
                                    break;
                                }
                            }
                            if (!onIgnore) {
                                regularFont.draw(name + " " + message, 10, y + yOffset, 0x00a7e0, shadow);
                                totalMessages++;
                                playerChatRows++;
                            }
                        }
                    }
                }
            }
            Rasterizer2D.set_default_size();
            chatScrollHeight = totalMessages * 14 + 7 + 5;
            if (chatScrollHeight < 111) {
                chatScrollHeight = 111;
            }

            drawScrollbar(114, chatScrollHeight - chatScrollAmount - 113, 7 + yOffset,
                    transparent_chat_box && !fixed ? 490 : 496, chatScrollHeight, transparent_chat_box && !fixed);
            String name;

            if (local_player != null && local_player.username != null) {
                name = local_player.getTitle(false) + StringUtils.formatText(capitalize(myUsername));
            } else {
                name = StringUtils.formatText(capitalize(myUsername));
            }

            Rasterizer2D.setDrawingArea(0, yOffset, 519, 142 + yOffset);
            int xOffset = 11;

            for (ChatCrown c : ChatCrown.get(myPrivilege, donatorPrivilege, ironmanPrivilege)) {
                SimpleImage sprite = SpriteCache.get(c.getSpriteId());
                if (sprite != null) {
                    sprite.drawAdvancedSprite(xOffset + 1, 121 + yOffset);
                    xOffset += sprite.width + 2;
                }
            }

            /**
             * Name in chatbox
             */
            regularFont.draw(name + ":", xOffset, 133 + yOffset, (transparent_chat_box && !fixed) ? 0xFFFFFF : 0,
                    shadow);
            regularFont.draw(inputString + "*", xOffset + regularFont.getTextWidth(name + ": "), 133 + yOffset,
                    (transparent_chat_box && !fixed) ? 0x7FA9FF : 255, shadow);

            Rasterizer2D.drawHorizontalLine(transparent_chat_box && !fixed ? 4 : 7, 121 + yOffset,
                    transparent_chat_box && !fixed ? 509 : 505, transparent_chat_box && !fixed ? 0xFFFFFF : 0x807660,
                    transparent_chat_box && !fixed ? 120 : 255);
            Rasterizer2D.set_default_size();
        }

    }

    public static String capitalize(String name) {
        if (name == null)
            return name;
        for (int length = 0; length < name.length(); length++) {
            if (length == 0) {
                name = String.format("%s%s", Character.toUpperCase(name.charAt(0)), name.substring(1));
            }
            if (!Character.isLetterOrDigit(name.charAt(length))) {
                if (length + 1 < name.length()) {
                    name = String.format("%s%s%s", name.subSequence(0, length + 1),
                            Character.toUpperCase(name.charAt(length + 1)), name.substring(length + 2));
                }
            }
        }
        return name;
    }

    /**
     * Initializes the client for startup
     */
    public void init() {
        try {
            nodeID = 10;
            setHighMem();
            isMembers = true;
            SignLink.init(22);

            instance = this;
            startThread(765, 503, 206, 1);
            setMaxCanvasSize(765, 503);
            osName = System.getProperty("os.name");
        } catch (Exception exception) {
            exception.printStackTrace();
            addReportToServer(exception.getMessage());
        }
    }

    public Socket openSocket(int port) throws IOException {
        return new Socket(InetAddress.getByName(serverAddress), port);
        // I believe this is required for IPv6 support, but maybe not.
        // return new Socket(serverAddress, port);
    }

    private void processMenuClick() {
        if (activeInterfaceType != 0)
            return;
        int click = MouseHandler.keypressedEventIndex;
        if (widget_highlighted == 1 && MouseHandler.saveClickX >= 516 && MouseHandler.saveClickY >= 160
                && MouseHandler.saveClickX <= 765
                && MouseHandler.saveClickY <= 205)
            click = 0;
        if (isMenuOpen) {
            if (click != 1) {
                int k = MouseHandler.mouseX;
                int j1 = MouseHandler.mouseY;

                if (k < menuX - 10 || k > menuX + menuWidth + 10 || j1 < menuY - 10
                        || j1 > menuY + menuHeight + 10) {
                    isMenuOpen = false;
                    if (menuScreenArea == 1) {
                    }
                    if (menuScreenArea == 2)
                        update_chat_producer = true;
                }
            }
            if (click == 1) {
                int l = menuX;
                int k1 = menuY;
                int i2 = menuWidth;
                int k2 = MouseHandler.saveClickX;
                int l2 = MouseHandler.saveClickY;

                int i3 = -1;
                for (int j3 = 0; j3 < menuActionRow; j3++) {
                    int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
                    if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
                        i3 = j3;
                }
                if (i3 != -1)
                    processMenuActions(i3);
                isMenuOpen = false;
                if (menuScreenArea == 1) {
                }
                if (menuScreenArea == 2) {
                    update_chat_producer = true;
                }
            }
        } else {
            if (click == 1 && menuActionRow > 0) {
                int menuId = menuActionTypes[menuActionRow - 1];
                if (menuId == 632 || menuId == 78 || menuId == 867 || menuId == 431 || menuId == 53 || menuId == 74
                        || menuId == 454 || menuId == 539
                        || menuId == 493 || menuId == 847 || menuId == 447 || menuId == 1125 || menuId == 968) {
                    int l1 = firstMenuAction[menuActionRow - 1];
                    int j2 = secondMenuAction[menuActionRow - 1];
                    Widget class9 = Widget.cache[j2];
                    if (class9.allowSwapItems || class9.replaceItems) {
                        aBoolean1242 = false;
                        draggingCycles = 0;
                        focusedDragWidget = j2;
                        dragFromSlot = l1;
                        activeInterfaceType = 2;
                        mouseDragX = MouseHandler.saveClickX;
                        mouseDragY = MouseHandler.saveClickY;
                        if (Widget.cache[j2].parent == widget_overlay_id)
                            activeInterfaceType = 1;
                        if (Widget.cache[j2].parent == backDialogueId)
                            activeInterfaceType = 3;
                        return;
                    }
                }
            }
            if (click == 1 && (useOneMouseButton == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2)
                click = 2;
            if (click == 1 && menuActionRow > 0)
                processMenuActions(menuActionRow - 1);
            if (click == 2 && menuActionRow > 0)
                determineMenuSize(MouseHandler.saveClickX, MouseHandler.saveClickY);
            processMainScreenClick();
            processTabClick();
            processChatModeClick();
            minimapHovers();
        }
    }

    public static Region currentMapRegion;

    public final void loadRegion() {
        if (!floorMaps.equals("") || !objectMaps.equals("")) {
            floorMaps = new StringBuilder();
            objectMaps = new StringBuilder();
        }
        Client.mapsLoaded = 0;
        boolean var1 = true;

        int var2;
        for (var2 = 0; var2 < regionLandArchives.length; var2++) {
            floorMaps.append("  ").append(regionLandIds[var2]);
            objectMaps.append("  ").append(regionLocIds[var2]);
            if (regionLandArchives[var2] == null && regionLandIds[var2] != -1) {
                regionLandArchives[var2] = Js5List.maps.getFile(regionLandIds[var2], 0);
                if (regionLandArchives[var2] == null) {
                    var1 = false;
                    ++Client.mapsLoaded;
                }
            }
            if (regionMapArchives[var2] == null && regionLocIds[var2] != -1) {
                try {
                    regionMapArchives[var2] = Js5List.maps.getFile(regionLocIds[var2], 0);
                    if (regionMapArchives[var2] == null) {
                        var1 = false;
                        ++Client.mapsLoaded;
                    }
                } catch (Throwable e) {
                    log.info("Missing xteas for region: {}", regions[var2]);
                }
            }
        }

        if (!var1) {
            Client.loadingType = 1;
        } else {
            Client.objectsLoaded = 0;
            var1 = true;

            int xChunk;
            int yChunk;
            for (var2 = 0; var2 < regionLandArchives.length; ++var2) {
                byte[] var3 = regionMapArchives[var2];
                if (var3 != null) {
                    xChunk = 64 * (regions[var2] >> 8) - next_region_start;
                    yChunk = 64 * (regions[var2] & 255) - next_region_end;
                    if (isInInstance) {
                        xChunk = 10;
                        yChunk = 10;
                    }
                    var1 &= Region.method787(var3, xChunk, yChunk);
                }
            }

            if (!var1) {
                Client.loadingType = 2;
            } else {
                if (0 != Client.loadingType) {
                    drawLoadingMessage("Loading - please wait." + "<br>" + " (" + 100 + "%" + ")");
                }

                setGameState(GameState.LOADING);
                StaticSound.playPcmPlayers();
                lastKnownPlane = -1;
                incompleteAnimables.clear();
                StaticSound.resetSoundCount();
                projectiles.clear();
                scene.clear();
                release();
                System.gc();

                for (var2 = 0; var2 < 4; ++var2) {
                    collisionMaps[var2].init();
                }

                int zChunk;
                for (var2 = 0; var2 < 4; ++var2) {
                    for (zChunk = 0; zChunk < 104; ++zChunk) {
                        for (xChunk = 0; xChunk < 104; ++xChunk) {
                            tileFlags[var2][zChunk][xChunk] = 0;
                        }
                    }
                }
                StaticSound.playPcmPlayers();
                Region objectManager = new Region(tileFlags, tileHeights);
                ObjectSound.clearObjectSounds();
                currentMapRegion = objectManager;
                var2 = regionLandArchives.length;
                packetSender.sendEmptyPacket();
                int tileBits;
                if (!isInInstance) {
                    byte[] var6;
                    for (zChunk = 0; zChunk < var2; ++zChunk) {
                        xChunk = 64 * (regions[zChunk] >> 8) - next_region_start;
                        yChunk = 64 * (regions[zChunk] & 255) - next_region_end;
                        var6 = regionLandArchives[zChunk];
                        if (null != var6) {
                            StaticSound.playPcmPlayers();
                            objectManager.decode_map_terrain(
                                    var6, xChunk,
                                    yChunk,
                                    region_x * 8 - 48, region_y * 8 - 48,
                                    collisionMaps);
                        }
                    }

                    for (zChunk = 0; zChunk < var2; ++zChunk) {
                        xChunk = (regions[zChunk] >> 8) * 64 - next_region_start;
                        yChunk = (regions[zChunk] & 255) * 64 - next_region_end;
                        var6 = regionLandArchives[zChunk];
                        if (null == var6 && region_y < 800) {
                            StaticSound.playPcmPlayers();
                            objectManager.method736(xChunk, yChunk, 64, 64);
                        }
                    }

                    packetSender.sendEmptyPacket();

                    for (zChunk = 0; zChunk < var2; ++zChunk) {
                        byte[] var15 = regionMapArchives[zChunk];
                        if (var15 != null) {
                            yChunk = 64 * (regions[zChunk] >> 8) - next_region_start;
                            tileBits = 64 * (regions[zChunk] & 255) - next_region_end;
                            StaticSound.playPcmPlayers();
                            objectManager.decode_map_locations(yChunk, collisionMaps, tileBits, scene, var15);
                        }
                    }
                }

                int level;
                int rotation;
                int worldX;
                if (isInInstance) {
                    int worldY;
                    int region;
                    int var12;
                    for (zChunk = 0; zChunk < 4; ++zChunk) {
                        StaticSound.playPcmPlayers();
                        for (xChunk = 0; xChunk < 13; ++xChunk) {
                            for (yChunk = 0; yChunk < 13; ++yChunk) {
                                boolean var18 = false;
                                level = constructRegionData[zChunk][xChunk][yChunk];
                                if (level != -1) {
                                    rotation = level >> 24 & 3;
                                    worldX = level >> 1 & 3;
                                    worldY = level >> 14 & 1023;
                                    region = level >> 3 & 2047;
                                    var12 = (worldY / 8 << 8) + region / 8;

                                    for (int var13 = 0; var13 < regions.length; ++var13) {
                                        if (regions[var13] == var12 && null != regionLandArchives[var13]) {
                                            objectManager.method3968(
                                                    regionLandArchives[var13],
                                                    zChunk,
                                                    8 * xChunk,
                                                    8 * yChunk,
                                                    rotation, 8 * (worldY & 7), 8 * (region & 7),
                                                    worldX,
                                                    collisionMaps);
                                            var18 = true;
                                            break;
                                        }
                                    }
                                }

                                if (!var18) {
                                    TextureProvider.method1307(zChunk, xChunk * 8, 8 * yChunk);
                                }
                            }
                        }
                    }

                    for (zChunk = 0; zChunk < 13; ++zChunk) {
                        for (xChunk = 0; xChunk < 13; ++xChunk) {
                            yChunk = constructRegionData[0][zChunk][xChunk];
                            if (yChunk == -1) {
                                objectManager.method736(zChunk * 8, 8 * xChunk, 8, 8);
                            }
                        }
                    }

                    packetSender.sendEmptyPacket();

                    for (zChunk = 0; zChunk < 4; ++zChunk) {
                        StaticSound.playPcmPlayers();
                        for (xChunk = 0; xChunk < 13; ++xChunk) {
                            for (yChunk = 0; yChunk < 13; ++yChunk) {
                                tileBits = constructRegionData[zChunk][xChunk][yChunk];
                                if (tileBits != -1) {
                                    level = tileBits >> 24 & 3;
                                    rotation = tileBits >> 1 & 3;
                                    worldX = tileBits >> 14 & 1023;
                                    worldY = tileBits >> 3 & 2047;
                                    region = worldY / 8 + (worldX / 8 << 8);

                                    for (var12 = 0; var12 < regions.length; ++var12) {
                                        if (regions[var12] == region && null != regionMapArchives[var12]) {
                                            objectManager.method1668(
                                                    regionMapArchives[var12],
                                                    zChunk,
                                                    xChunk * 8, 8 * yChunk,
                                                    level,
                                                    (worldX & 7) * 8, (worldY & 7) * 8,
                                                    rotation,
                                                    scene, collisionMaps);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                packetSender.sendEmptyPacket();
                StaticSound.playPcmPlayers();
                objectManager.method6042(scene, collisionMaps);
                rasterProvider.init();
                packetSender.sendEmptyPacket();

                zChunk = Region.min_plane;
                if (zChunk > plane) {
                    zChunk = plane;
                }

                if (zChunk < plane - 1) {
                    zChunk = plane - 1;
                }

                if (low_detail) {
                    scene.init(Region.min_plane);
                } else {
                    scene.init(0);
                }

                for (xChunk = 0; xChunk < 104; ++xChunk) {
                    for (yChunk = 0; yChunk < 104; ++yChunk) {
                        spawn_scene_item(xChunk, yChunk);
                    }
                }

                StaticSound.playPcmPlayers();
                anInt1051++;
                if (anInt1051 > 98) {
                    anInt1051 = 0;
                }

                clearObjectSpawnRequests();
                ObjectDefinition.cachedModelData.clear();
                packetSender.sendRegionChange();

                if (!isInInstance) {
                    xChunk = (region_x - 6) / 8;
                    yChunk = (region_x + 6) / 8;
                    tileBits = (region_y - 6) / 8;
                    level = (region_y + 6) / 8;

                    for (rotation = xChunk - 1; rotation <= 1 + yChunk; ++rotation) {
                        for (worldX = tileBits - 1; worldX <= level + 1; ++worldX) {
                            if (rotation < xChunk || rotation > yChunk || worldX < tileBits || worldX > level) {
                                Js5List.maps.loadRegionFromName("m" + rotation + "_" + worldX);
                                Js5List.maps.loadRegionFromName("l" + rotation + "_" + worldX);
                            }
                        }
                    }
                }

                if (plane != lastKnownPlane) {
                    lastKnownPlane = plane;
                    renderMapScene(plane);
                }
                setGameState(GameState.LOGGED_IN);
                StaticSound.playPcmPlayers();
            }
        }
    }

    public static AbstractMap.SimpleEntry<Integer, Integer> getNextInteger(ArrayList<Integer> values) {
        ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> frequencies = new ArrayList<>();
        int maxIndex = 0;
        main: for (int i = 0; i < values.size(); ++i) {
            int value = values.get(i);
            for (int j = 0; j < frequencies.size(); ++j) {
                if (frequencies.get(j).getKey() == value) {
                    frequencies.get(j).setValue(frequencies.get(j).getValue() + 1);
                    if (frequencies.get(maxIndex).getValue() < frequencies.get(j)
                            .getValue()) {
                        maxIndex = j;
                    }
                    continue main;
                }
            }
            frequencies.add(new AbstractMap.SimpleEntry<Integer, Integer>(value, 1));
        }
        return frequencies.get(maxIndex);
    }

    private void release() {
        ObjectDefinition.cachedModelData.clear();
        ObjectDefinition.objectsCached.clear();
        ObjectDefinition.modelsCached.clear();
        NpcDefinition.modelCache.clear();
        ItemDefinition.release();
        Player.model_cache.clear();
        SpotAnimation.cached.clear();

        SequenceDefinition.cached.clear();
        VariableBits.cached.clear();
        IdentityKit.cached.clear();
        FloorUnderlayDefinition.floorCache.clear();
        FloorOverlayDefinition.floorCache.clear();
        VariablePlayer.clear();
        ((TextureProvider) Rasterizer3D.clips.textureLoader).clear();
        Js5List.models.clearFiles();
        Js5List.soundEffects.clearFiles();
        Js5List.textures.clearFiles();
        Js5List.musicTracks.clearFiles();
        Js5List.musicJingles.clearFiles();
        Js5List.sprites.clearFiles();
        Js5List.binary.clearFiles();
        Js5List.dbtableindex.clearFiles();
        Js5List.animations.clearFiles();
        Js5List.skeletons.clearFiles();
        Js5List.clientScript.clearFiles();
        Js5List.maps.clearFiles();
    }

    private void renderMapScene2(int plane) {
        int[] pixels = minimapImage.pixels;
        int length = pixels.length;
        int pixel;
        for (pixel = 0; pixel < length; pixel++) {
            pixels[pixel] = 0;
        }

        for (int y = 1; y < 103; y++) {
            int drawIndex = 2048 * (103 - pixel) + 24628;
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0)
                    scene.draw_minimap_tile(pixels, drawIndex, plane, x, y);
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0)
                    scene.draw_minimap_tile(pixels, drawIndex, plane + 1, x, y);
                drawIndex += 4;
            }
        }

        int wallColor = (238 + (int) (Math.random() * 20.0D) - 10 << 16)
                + (238 + (int) (Math.random() * 20.0D) - 10 << 8) + (238 + (int) (Math.random() * 20.0D) - 10);
        int interactable = 238 + (int) (Math.random() * 20.0D) - 10 << 16;
        minimapImage.init();
        for (int y = 1; y < 103; ++y) {
            for (int x = 1; x < 103; ++x) {
                if ((tileFlags[plane][x][y] & 24) == 0) {
                    drawMapScenes(plane, x, y, wallColor, interactable);
                }

                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0) {
                    drawMapScenes(plane + 1, x, y, wallColor, interactable);
                }
            }
        }
        rasterProvider.init();
        objectIconCount = 0;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                long id = scene.getFloorDecorationTag(plane, x, y);
                int function = ObjectDefinition.get(ObjectKeyUtil.getObjectId(id)).mapIconId;

                if (function >= 0 && AreaDefinition.lookup(function).field1940) {
                    minimapHint[objectIconCount] = AreaDefinition.lookup(function).getIconSprite();
                    minimapHintX[objectIconCount] = x;
                    minimapHintY[objectIconCount] = y;
                    objectIconCount++;
                }

            }
        }
        /*
         * for (int x = 0; x < 104; x++) {
         * for (int y = 0; y < 104; y++) {
         * long id = scene.get_ground_decor_uid(plane, x, y);
         * if (id != 0L) {
         * int function = ObjectDefinition.get(ObjectKeyUtil.getObjectId(id)).mapIconId;
         * if (function >= -1) {
         * int sprite = AreaDefinition.lookup(function).spriteId;
         * if(sprite != -1) {
         * int viewportX = x;
         * int viewportY = y;
         * minimapHint[objectIconCount] = AreaDefinition.getImage(sprite);
         * minimapHintX[objectIconCount] = viewportX;
         * minimapHintY[objectIconCount] = viewportY;
         * objectIconCount++;
         * }
         * }
         * }
         * }
         * }
         */

        if (ClientConstants.DUMP_MAP_REGIONS) {

            File directory = new File("MapImageDumps/");
            if (!directory.exists()) {
                directory.mkdir();
            }
            BufferedImage bufferedimage = new BufferedImage(minimapImage.width, minimapImage.height, 1);
            bufferedimage.setRGB(0, 0, minimapImage.width, minimapImage.height, minimapImage.pixels, 0,
                    minimapImage.width);
            Graphics2D graphics2d = bufferedimage.createGraphics();
            graphics2d.dispose();
            try {
                File file1 = new File("MapImageDumps/" + (directory.listFiles().length + 1) + ".png");
                ImageIO.write(bufferedimage, "png", file1);
            } catch (Exception e) {
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
        }
    }

    public final void renderMapScene(int arg0) {
        int[] var2 = minimapImage.pixels;
        int var3 = var2.length;

        int var4;
        for (var4 = 0; var4 < var3; ++var4) {
            var2[var4] = 0;
        }

        int var5;
        int var6;
        for (var4 = 1; var4 < 103; ++var4) {
            var5 = 2048 * (103 - var4) + 24628;

            for (var6 = 1; var6 < 103; ++var6) {
                if (0 == (tileFlags[arg0][var6][var4] & 24)) {
                    scene.draw_minimap_tile(var2, var5, arg0, var6, var4);
                }

                if (arg0 < 3 && 0 != (tileFlags[1 + arg0][var6][var4] & 8)) {
                    scene.draw_minimap_tile(var2, var5, arg0 + 1, var6, var4);
                }

                var5 += 4;
            }
        }

        var4 = (238 + (int) (Math.random() * 20.0D) - 10 << 16) + (238 + (int) (Math.random() * 20.0D) - 10 << 8)
                + (238 + (int) (Math.random() * 20.0D) - 10);
        var5 = 238 + (int) (Math.random() * 20.0D) - 10 << 16;
        minimapImage.init();

        int var7;
        for (var6 = 1; var6 < 103; ++var6) {
            for (var7 = 1; var7 < 103; ++var7) {
                if ((tileFlags[arg0][var7][var6] & 24) == 0) {
                    drawMapScenes(arg0, var7, var6, var4, var5);
                }

                if (arg0 < 3 && 0 != (tileFlags[1 + arg0][var7][var6] & 8)) {
                    drawMapScenes(1 + arg0, var7, var6, var4, var5);
                }
            }
        }
        rasterProvider.init();
        objectIconCount = 0;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                long id = scene.getFloorDecorationTag(plane, x, y);
                int function = ObjectDefinition.get(ObjectKeyUtil.getObjectId(id)).mapIconId;

                if (function >= 0 && AreaDefinition.lookup(function).field1940) {
                    minimapHint[objectIconCount] = AreaDefinition.lookup(function).getIconSprite();
                    minimapHintX[objectIconCount] = x;
                    minimapHintY[objectIconCount] = y;
                    objectIconCount++;
                }

            }
        }
    }

    public static int getVar(int arg0) {
        return VarbitManager.getVarbit(arg0);
    }

    private int interactingWithEntityId;

    public int getInteractingWithEntityId() {
        return interactingWithEntityId;
    }

    public void setInteractingWithEntityId(int interactingWithEntityId) {
        this.interactingWithEntityId = interactingWithEntityId;
    }

    private void spawn_scene_item(int x, int y) {
        NodeDeque list = scene_items[plane][x][y];
        if (list == null) {
            scene.remove_ground_item(plane, x, y);
            return;
        }
        long value_priority = -99999999L;
        Object first = null;
        for (Item item = (Item) list.first(); item != null; item = (Item) list.next()) {
            ItemDefinition itemDef = ItemDefinition.get(item.id);
            long value = itemDef.cost;
            if (itemDef.stackable) {
                value *= item.quantity + (long) 1;
            }
            if (value > value_priority) {
                value_priority = value;
                first = item;
            }
        }
        list.addFirst(((Node) (first)));
        Object second = null;
        Object third = null;
        for (Item node = (Item) list.first(); node != null; node = (Item) list.next()) {
            if (node.id != ((Item) (first)).id && second == null)
                second = node;

            if (node.id != ((Item) (first)).id && node.id != ((Item) (second)).id && third == null)
                third = node;

        }
        long key = ObjectKeyUtil.calculateTag(x, y, 3, false, 0);

        scene.add_ground_item(x, key, ((Renderable) (second)), get_tile_pos(plane, y * 128 + 64, x * 128 + 64),
                ((Renderable) (third)), ((Renderable) (first)), plane, y);
    }

    private void buildInterfaceMenu(int x, Widget widget, int cursor_x, int y, int cursor_y, int scrollPosition) {
        if (widget == null || widget.type != 0 || widget.children == null || widget.invisible || widget.drawingDisabled)
            return;
        if (cursor_x < x || cursor_y < y || cursor_x >= x + widget.width || cursor_y >= y + widget.height)
            return;
        if (widget.id == 53729) {
            System.err.println("here.. found 1");
        }
        int size = widget.children.length;
        // System.out.println("Build interface menu called at: " + MouseHandler.mouseX +
        // " | " + MouseHandler.mouseY);
        for (int index = 0; index < size; index++) {
            int childX = widget.child_x[index] + x;
            int childY = (widget.child_y[index] + y) - scrollPosition;
            Widget child = Widget.cache[widget.children[index]];
            if (child == null) {
                continue;
            }

            if (child.invisible) {
                continue;
            }

            if (child.drawingDisabled) {
                continue;
            }

            HoverMenuManager.reset();
            childX += child.x;
            childY += child.y;
            checkFilters(child, childX, childY);
            checkHoverWithText(child, childX, childY);
            if ((child.hoverType >= 0 || child.defaultHoverColor != 0) && cursor_x >= childX && cursor_y >= childY
                    && cursor_x < childX + child.width && cursor_y < childY + child.height) {
                if (child.hoverType >= 0) {
                    frameFocusedInterface = child.hoverType;
                    if (child.hoverType == 17) {
                    }
                } else {
                    frameFocusedInterface = child.id;
                }
            }

            if (child.type == 8 && cursor_x >= childX && cursor_y >= childY && cursor_x < childX + child.width
                    && cursor_y < childY + child.height) {
                anInt1315 = child.id;
            }

            if (cursor_x >= childX && cursor_y >= childY && cursor_x < childX + child.width
                    && cursor_y < childY + child.height) {
                child.toggled = true;
            } else {
                child.toggled = false;
            }

            if (child.type == Widget.TYPE_CONTAINER && !child.invisible) {
                buildInterfaceMenu(childX, child, cursor_x, childY, cursor_y, child.scrollPosition);
                if (child.scrollMax > child.height)
                    handleScroll(childX + child.width, child.height, cursor_x, cursor_y, child, childY,
                            child.scrollMax);
            } else {

                /*
                 * if(child.id >= 25301) {
                 * System.err.println("Mouse x: " + cursor_x + " | Mouse y: " + cursor_y +
                 * " | Child x: " + childX + " | child y: " + childY + " | Child width: " +
                 * child.width + " | Child height: " + child.height);
                 * System.err.println("Expected: " + (cursor_x >= childX) + " | " + (cursor_y >=
                 * childY) + " | " + (cursor_x < childX + child.width) + " | " + (cursor_y <
                 * childY + child.height));
                 * System.err.
                 * println("To check: cursor_x >= childX cursor_x >= childX && cursor_y >= childY + && cursor_x < childX + child.width && cursor_y < childY + child.height"
                 * );
                 * 
                 * System.out.println("Mouse y: " + cursor_y + " | Child y: " + childY);
                 * }
                 */
                if (child.optionType == Widget.OPTION_OK && cursor_x >= childX && cursor_y >= childY
                        && cursor_x < childX + child.width
                        && cursor_y < childY + child.height) {
                    // System.err.println("Checking for: " + child.id);
                    boolean flag = false;
                    if (child.contentType != 0) {
                        flag = buildFriendsListMenu(child);
                    }
                    if (child.tooltip == null || child.tooltip.length() == 0) {
                        flag = true;
                    }

                    if (!flag) {
                        menuActionText[menuActionRow] = (myPrivilege >= 2 && myPrivilege <= 4
                                && ClientConstants.DEBUG_MODE) ? child.tooltip + " " + child.id : child.tooltip;
                        menuActionTypes[menuActionRow] = 315;
                        secondMenuAction[menuActionRow] = child.id;
                        menuActionRow++;
                    }
                }
                if (child.optionType == Widget.OPTION_USABLE && widget_highlighted == 0 && cursor_x >= childX
                        && cursor_y >= childY && cursor_x < childX + child.width && cursor_y < childY + child.height) {
                    String s = child.selectedActionName;
                    if (s.contains(" "))
                        s = s.substring(0, s.indexOf(" "));

                    if ((myPrivilege >= 2 && myPrivilege <= 4 && ClientConstants.DEBUG_MODE)) {
                        menuActionText[menuActionRow] = s + " <col=65280>" + child.spellName + " " + child.id;
                    } else {
                        menuActionText[menuActionRow] = s + " <col=65280>" + child.spellName;
                    }
                    menuActionTypes[menuActionRow] = USE_SPELL;
                    secondMenuAction[menuActionRow] = child.id;
                    menuActionRow++;
                }
                if (child.optionType == Widget.OPTION_CLOSE && cursor_x >= childX && cursor_y >= childY
                        && cursor_x < childX + child.width && cursor_y < childY + child.height) {
                    HoverMenuManager.reset();
                    menuActionText[menuActionRow] = "Close";
                    menuActionTypes[menuActionRow] = 200;
                    secondMenuAction[menuActionRow] = child.id;
                    menuActionRow++;
                }
                if (child.optionType == Widget.OPTION_TOGGLE_SETTING && cursor_x >= childX && cursor_y >= childY
                        && cursor_x < childX + child.width && cursor_y < childY + child.height) {
                    HoverMenuManager.reset();
                    menuActionText[menuActionRow] = (myPrivilege >= 2 && myPrivilege <= 4 && ClientConstants.DEBUG_MODE)
                            ? child.tooltip + " <col=65280>(" + child.id + ")"
                            : child.tooltip;
                    menuActionTypes[menuActionRow] = 169;
                    secondMenuAction[menuActionRow] = child.id;
                    menuActionRow++;
                }
                if (child.optionType == Widget.OPTION_RESET_SETTING && cursor_x >= childX && cursor_y >= childY
                        && cursor_x < childX + child.width && cursor_y < childY + child.height) {
                    boolean flag = child.tooltip == null || child.tooltip.length() == 0;
                    if (!flag) {
                        if (child.id == 433) {
                            if (widget.id == 24899) {
                                child.tooltip = "Short fuse";
                            } else if (widget.id == 22899) {
                                child.tooltip = "Scorch";
                            } else {
                                child.tooltip = "Pound";
                            }
                        } else if (child.id == 432) {
                            if (widget.id == 24899) {
                                child.tooltip = "Medium fuse";
                            } else if (widget.id == 22899) {
                                child.tooltip = "Flare";
                            } else {
                                child.tooltip = "Pummel";
                            }
                        } else if (child.id == 431) {
                            if (widget.id == 24899) {
                                child.tooltip = "Long fuse";
                            } else if (widget.id == 22899) {
                                child.tooltip = "Blaze";
                            } else {
                                child.tooltip = "Block";
                            }
                        }
                        HoverMenuManager.reset();
                        menuActionText[menuActionRow] = (myPrivilege >= 2 && myPrivilege <= 4
                                && ClientConstants.DEBUG_MODE)
                                        ? child.tooltip + " <col=FF9040>(" + child.id + ")"
                                        : child.tooltip;
                        menuActionTypes[menuActionRow] = 646;
                        secondMenuAction[menuActionRow] = child.id;
                        menuActionRow++;
                    }
                }

                if (child.optionType == Widget.OPTION_CONTINUE && !continuedDialogue && cursor_x >= childX
                        && cursor_y >= childY && cursor_x < childX + child.width && cursor_y < childY + child.height) {
                    HoverMenuManager.reset();
                    menuActionText[menuActionRow] = child.tooltip;
                    menuActionTypes[menuActionRow] = 679;
                    secondMenuAction[menuActionRow] = child.id;
                    menuActionRow++;
                }

                if (child.optionType == OPTION_MENU) {
                    if (child instanceof OptionMenuInterface) {
                        OptionMenuInterface optionInter = (OptionMenuInterface) child;

                        if (cursor_x >= childX && cursor_y >= childY + scrollPosition && cursor_x < childX + child.width
                                && cursor_y < childY + scrollPosition + child.height) {

                            if (!optionInter.getOptionMenus().isEmpty()) {
                                int drawX = childX;
                                int drawY = childY;
                                final int boxWidth = optionInter.width - 4;
                                final int boxHeight = 40;

                                for (OptionMenu menu : optionInter.getOptionMenus()) {
                                    if (menu == null) {
                                        continue;
                                    }
                                    final boolean isHighlighted = MouseHandler.mouseX >= drawX
                                            && MouseHandler.mouseX <= drawX + boxWidth && MouseHandler.mouseY >= drawY
                                            && MouseHandler.mouseY <= drawY + boxHeight;
                                    menu.setHighlighted(isHighlighted);
                                    if (isHighlighted) {

                                        // Teleport menu only
                                        if (optionInter.id == 72156) {

                                            final int addFavoriteXPosition = drawX + boxWidth
                                                    - optionInter.getSprites()[0].width - 4;
                                            final int addFavoriteYPosition = drawY + 3;
                                            final boolean withinFavoritePosition = MouseHandler.mouseX >= addFavoriteXPosition
                                                    && MouseHandler.mouseX <= addFavoriteXPosition
                                                            + optionInter.getSprites()[0].width
                                                    && MouseHandler.mouseY >= addFavoriteYPosition
                                                    && MouseHandler.mouseY <= addFavoriteYPosition
                                                            + optionInter.getSprites()[0].height;

                                            if (withinFavoritePosition) {
                                                menuActionText[menuActionRow] = "Add Favorite: " + menu.getOptionName();
                                                menuActionTypes[menuActionRow] = 72000;
                                                secondMenuAction[menuActionRow] = child.id;
                                                firstMenuAction[menuActionRow] = menu.getIdentifier();
                                                selectedMenuActions[menuActionRow] = 1;
                                                menuActionRow++;
                                            } else {
                                                menuActionText[menuActionRow] = menu.getOptionName();
                                                menuActionTypes[menuActionRow] = 72000;
                                                secondMenuAction[menuActionRow] = child.id;
                                                firstMenuAction[menuActionRow] = menu.getIdentifier();
                                                selectedMenuActions[menuActionRow] = 0;
                                                menuActionRow++;
                                            }

                                        } else if (optionInter.id == 72206) {
                                            final int addFavoriteXPosition = drawX + boxWidth
                                                    - optionInter.getSprites()[0].width - 4;
                                            final int addFavoriteYPosition = drawY + 3;

                                            final int moveUpXPosition = drawX + boxWidth
                                                    - optionInter.getSprites()[1].width - 6;
                                            final int moveUpYPosition = drawY + 17;

                                            final int moveDownXPosition = drawX + boxWidth
                                                    - optionInter.getSprites()[2].width - 6;
                                            final int moveDownYPosition = drawY + 28;

                                            final boolean withinFavoritePosition = MouseHandler.mouseX >= addFavoriteXPosition
                                                    && MouseHandler.mouseX <= addFavoriteXPosition
                                                            + optionInter.getSprites()[0].width
                                                    && MouseHandler.mouseY >= addFavoriteYPosition
                                                    && MouseHandler.mouseY <= addFavoriteYPosition
                                                            + optionInter.getSprites()[0].height;
                                            final boolean withinMoveUpPosition = MouseHandler.mouseX >= moveUpXPosition
                                                    && MouseHandler.mouseX <= moveUpXPosition
                                                            + optionInter.getSprites()[1].width
                                                    && MouseHandler.mouseY >= moveUpYPosition
                                                    && MouseHandler.mouseY <= moveUpYPosition
                                                            + optionInter.getSprites()[1].height;
                                            final boolean withinMoveDownPosition = MouseHandler.mouseX >= moveDownXPosition
                                                    && MouseHandler.mouseX <= moveDownXPosition
                                                            + optionInter.getSprites()[2].width
                                                    && MouseHandler.mouseY >= moveDownYPosition
                                                    && MouseHandler.mouseY <= moveDownYPosition
                                                            + optionInter.getSprites()[2].height;

                                            if (withinFavoritePosition) {
                                                menuActionText[menuActionRow] = "Remove Favorite: "
                                                        + menu.getOptionName();
                                                menuActionTypes[menuActionRow] = 72000;
                                                secondMenuAction[menuActionRow] = child.id;
                                                firstMenuAction[menuActionRow] = menu.getIdentifier();
                                                selectedMenuActions[menuActionRow] = 1;
                                                menuActionRow++;
                                            } else if (withinMoveUpPosition) {
                                                menuActionText[menuActionRow] = "Move Up: " + menu.getOptionName();
                                                menuActionTypes[menuActionRow] = 72000;
                                                secondMenuAction[menuActionRow] = child.id;
                                                firstMenuAction[menuActionRow] = menu.getIdentifier();
                                                selectedMenuActions[menuActionRow] = 2;
                                                menuActionRow++;
                                            } else if (withinMoveDownPosition) {
                                                menuActionText[menuActionRow] = "Move Down: " + menu.getOptionName();
                                                menuActionTypes[menuActionRow] = 72000;
                                                secondMenuAction[menuActionRow] = child.id;
                                                firstMenuAction[menuActionRow] = menu.getIdentifier();
                                                selectedMenuActions[menuActionRow] = 3;
                                                menuActionRow++;
                                            } else {
                                                menuActionText[menuActionRow] = menu.getOptionName();
                                                menuActionTypes[menuActionRow] = 72000;
                                                secondMenuAction[menuActionRow] = child.id;
                                                firstMenuAction[menuActionRow] = menu.getIdentifier();
                                                selectedMenuActions[menuActionRow] = 0;
                                                menuActionRow++;
                                            }
                                        } else {
                                            menuActionText[menuActionRow] = menu.getOptionName();
                                            menuActionTypes[menuActionRow] = 72000;
                                            secondMenuAction[menuActionRow] = child.id;
                                            firstMenuAction[menuActionRow] = menu.getIdentifier();
                                            selectedMenuActions[menuActionRow] = 0;
                                            menuActionRow++;
                                        }
                                        break;
                                    }
                                    drawY += 42;
                                }
                            }
                        }
                    }
                }

                if (child.optionType == Widget.OPTION_DROPDOWN) {

                    boolean flag = false;
                    child.hovered = false;
                    child.dropdownHover = -1;

                    if (child.dropdown.isOpen()) {

                        // Inverted keybinds dropdown
                        if (child.type == Widget.TYPE_KEYBINDS_DROPDOWN && child.inverted && cursor_x >= childX
                                && cursor_x < childX + (child.dropdown.getWidth() - 16)
                                && cursor_y >= childY - child.dropdown.getHeight() - 10 && cursor_y < childY) {

                            int yy = cursor_y - (childY - child.dropdown.getHeight());

                            if (cursor_x > childX + (child.dropdown.getWidth() / 2)) {
                                child.dropdownHover = ((yy / 15) * 2) + 1;
                            } else {
                                child.dropdownHover = (yy / 15) * 2;
                            }
                            flag = true;
                        } else if (!child.inverted && cursor_x >= childX
                                && cursor_x < childX + (child.dropdown.getWidth() - 16) && cursor_y >= childY + 19
                                && cursor_y < childY + 19 + child.dropdown.getHeight()) {

                            int yy = cursor_y - (childY + 19);

                            if (child.type == Widget.TYPE_KEYBINDS_DROPDOWN && child.dropdown.doesSplit()) {
                                if (cursor_x > childX + (child.dropdown.getWidth() / 2)) {
                                    child.dropdownHover = ((yy / 15) * 2) + 1;
                                } else {
                                    child.dropdownHover = (yy / 15) * 2;
                                }
                            } else {
                                child.dropdownHover = yy / 14; // Regular dropdown hover
                            }
                            flag = true;
                        }
                        if (flag) {
                            if (menuActionRow != 1) {
                                menuActionRow = 1;
                            }
                            HoverMenuManager.reset();
                            menuActionText[menuActionRow] = "Select";
                            menuActionTypes[menuActionRow] = 770;
                            secondMenuAction[menuActionRow] = child.id;
                            firstMenuAction[menuActionRow] = child.dropdownHover;
                            selectedMenuActions[menuActionRow] = widget.id;
                            menuActionRow++;
                        }
                    }
                    if (cursor_x >= childX && cursor_y >= childY && cursor_x < childX + child.dropdown.getWidth()
                            && cursor_y < childY + 24 && menuActionRow == 1) {
                        child.hovered = true;
                        HoverMenuManager.reset();
                        menuActionText[menuActionRow] = child.dropdown.isOpen() ? "Hide" : "Show";
                        menuActionTypes[menuActionRow] = 769;
                        secondMenuAction[menuActionRow] = child.id;
                        selectedMenuActions[menuActionRow] = widget.id;
                        menuActionRow++;
                    }
                }

                if (cursor_x >= childX && cursor_y >= childY
                        && cursor_x < childX + child.width + (child.type == 4 ? 100 : child.width)
                        && cursor_y < childY + child.height) {

                    if (!child.invisible) {
                        if (child.actions != null && !child.drawingDisabled) {
                            if (!(child.contentType == 206 && interfaceIsSelected(child))) {
                                if ((child.type == 4 && child.defaultText.length() > 0) || child.type == 5
                                        || !child.invisible) {

                                    boolean drawOptions = true;

                                    // HARDCODE CLICKABLE TEXT HERE
                                    if (child.parent == 37128) { // Clan chat interface, dont show options for guests
                                        drawOptions = showClanOptions;
                                    }

                                    if (drawOptions) {
                                        for (int actionId = child.actions.length - 1; actionId >= 0; actionId--) {
                                            if (child.actions[actionId] != null) {
                                                String action = child.actions[actionId]
                                                        + (child.type == 4 ? " <col=ffb000>" + child.defaultText : "");
                                                try {
                                                    if (action.contains("img")) {
                                                        action = action.replaceAll("<col=ffb000>", "");
                                                        int prefix = action.indexOf("<img=");
                                                        int suffix = action.indexOf(">");
                                                        // System.out.println("Action is: " + action);
                                                        action = action.replaceAll(action.substring(prefix + 3, suffix),
                                                                "");
                                                        action = action.replaceAll("</img>", "");
                                                        action = action.replaceAll("<img=>", "");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    addReportToServer(e.getMessage());
                                                }
                                                HoverMenuManager.reset();
                                                menuActionText[menuActionRow] = action;
                                                menuActionTypes[menuActionRow] = 647;
                                                firstMenuAction[menuActionRow] = actionId;
                                                secondMenuAction[menuActionRow] = child.id;
                                                menuActionRow++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (child.type == Widget.TYPE_INVENTORY && !child.invisible && !child.drawingDisabled) {
                    int itemSlot = 0;
                    int tabAm = tabAmounts[0];
                    int tabSlot = 0;
                    int heightShift = 0;

                    int newSlot = 0;
                    if (child.contentType == 206 && settings[211] != 0) {
                        for (int tab = 0; tab < tabAmounts.length; tab++) {
                            if (tab == settings[211]) {
                                break;
                            }
                            newSlot += tabAmounts[tab];
                        }
                        itemSlot = newSlot;
                    }

                    int results = -1;
                    Widget rsi = this.getInputFieldFocusOwner();
                    boolean searchBank = searchingBank && !promptInput.isEmpty() && child.id != 5064;
                    boolean searchShops = false;
                    if (rsi != null && searchingShops) {
                        searchShops = searchingShops && !rsi.defaultText.isEmpty() && child.id != 3823;
                    }
                    heightLoop: for (int l2 = 0; l2 < child.height; l2++) {
                        for (int i3 = 0; i3 < child.width; i3++) {
                            if (itemSlot >= child.inventoryItemId.length) {
                                continue;
                            }
                            if (rsi != null) {
                                if (searchShops && child.inventoryItemId[itemSlot] > 0) {
                                    ItemDefinition definition = ItemDefinition.get(child.inventoryItemId[itemSlot] - 1);
                                    if (definition == null || definition.name == null) {
                                        itemSlot++;
                                        continue;
                                    }
                                    if (!definition.name.toLowerCase().contains(rsi.defaultText.toLowerCase())) {
                                        itemSlot++;
                                        continue;
                                    }
                                    results++;
                                }
                            }
                            if (searchBank && child.inventoryItemId[itemSlot] > 0) {
                                ItemDefinition definition = ItemDefinition.get(child.inventoryItemId[itemSlot] - 1);
                                if (definition == null || definition.name == null) {
                                    itemSlot++;
                                    continue;
                                }
                                if (!definition.name.toLowerCase().contains(promptInput.toLowerCase())) {
                                    itemSlot++;
                                    continue;
                                }
                                results++;
                            }
                            if (child.contentType == 206 && !searchBank) {
                                if (settings[211] == 0) {
                                    if (itemSlot >= tabAm) {
                                        if (tabSlot + 1 < tabAmounts.length) {
                                            tabAm += tabAmounts[++tabSlot];
                                            if (tabSlot > 0 && tabAmounts[tabSlot - 1] % child.width == 0) {
                                                l2--;
                                            }
                                            heightShift += 8;
                                        }
                                        break;
                                    }
                                } else if (settings[211] <= 9) {
                                    if (itemSlot >= tabAmounts[settings[211]] + newSlot) {
                                        break heightLoop;
                                    }
                                }
                            }

                            int j3 = childX + (searchBank || searchShops ? (results % child.width) : i3)
                                    * (32 + child.inventoryMarginX);
                            int k3 = (childY + (searchBank || searchShops ? (results / child.width) : l2)
                                    * (32 + child.inventoryMarginY)) + heightShift;

                            if (itemSlot < 20) {
                                j3 += child.inventoryOffsetX[itemSlot];
                                k3 += child.inventoryOffsetY[itemSlot];
                            }

                            if (cursor_x >= j3 && cursor_y >= k3 && cursor_x < j3 + 32 && cursor_y < k3 + 32) {
                                mouseInvInterfaceIndex = itemSlot;
                                lastActiveInvInterface = child.id;
                                int item = child.inventoryItemId[itemSlot];
                                if (item > 0) {
                                    ItemDefinition itemDef = ItemDefinition.get(item - 1);
                                    if (item_highlighted == 1 && child.hasActions) {
                                        if (child.id != interfaceitemSelectionTypeIn
                                                || itemSlot != selectedItemIdSlot) {
                                            menuActionText[menuActionRow] = "Use " + selectedItemName
                                                    + " with <col=FF9040>" + itemDef.name;
                                            menuActionTypes[menuActionRow] = 870;
                                            selectedMenuActions[menuActionRow] = itemDef.id;
                                            firstMenuAction[menuActionRow] = itemSlot;
                                            secondMenuAction[menuActionRow] = child.id;
                                            menuActionRow++;
                                        }
                                    } else if (widget_highlighted == 1 && child.hasActions) {
                                        if ((selectedTargetMask & 0x10) == 16) {
                                            menuActionText[menuActionRow] = selected_target_id + " <col=FF9040>"
                                                    + itemDef.name;
                                            menuActionTypes[menuActionRow] = 543;
                                            selectedMenuActions[menuActionRow] = itemDef.id;
                                            firstMenuAction[menuActionRow] = itemSlot;
                                            secondMenuAction[menuActionRow] = child.id;
                                            menuActionRow++;
                                        }
                                    } else {
                                        if (child.hasActions) {
                                            for (int l3 = 4; l3 >= 3; l3--)
                                                if (itemDef.inventoryActions != null
                                                        && itemDef.inventoryActions[l3] != null) {
                                                    menuActionText[menuActionRow] = itemDef.inventoryActions[l3]
                                                            + " <col=FF9040>" + itemDef.name;
                                                    if (l3 == 3)
                                                        menuActionTypes[menuActionRow] = 493;
                                                    if (l3 == 4)
                                                        menuActionTypes[menuActionRow] = 847;
                                                    selectedMenuActions[menuActionRow] = itemDef.id;
                                                    firstMenuAction[menuActionRow] = itemSlot;
                                                    secondMenuAction[menuActionRow] = child.id;
                                                    menuActionRow++;
                                                } else if (l3 == 4) {
                                                    menuActionText[menuActionRow] = "Drop <col=FF9040>" + itemDef.name;
                                                    menuActionTypes[menuActionRow] = 847;
                                                    selectedMenuActions[menuActionRow] = itemDef.id;
                                                    firstMenuAction[menuActionRow] = itemSlot;
                                                    secondMenuAction[menuActionRow] = child.id;
                                                    menuActionRow++;
                                                }
                                        }
                                        if (child.usableItems && settings[ConfigUtility.SHIFT_CLICK_ID] == 1
                                                && isShiftPressed) {
                                            menuActionText[menuActionRow] = "Drop <col=FF9040>" + itemDef.name;
                                            menuActionTypes[menuActionRow] = 847;
                                            selectedMenuActions[menuActionRow] = itemDef.id;
                                            firstMenuAction[menuActionRow] = itemSlot;
                                            secondMenuAction[menuActionRow] = child.id;
                                            menuActionRow++;
                                        } else if (child.usableItems) {
                                            menuActionText[menuActionRow] = "Use <col=FF9040>" + itemDef.name;
                                            menuActionTypes[menuActionRow] = 447;
                                            selectedMenuActions[menuActionRow] = itemDef.id;
                                            firstMenuAction[menuActionRow] = itemSlot;
                                            secondMenuAction[menuActionRow] = child.id;
                                            menuActionRow++;
                                        }
                                        if (child.hasActions && itemDef.inventoryActions != null) {
                                            for (int i4 = 2; i4 >= 0; i4--)
                                                if (itemDef.inventoryActions[i4] != null) {
                                                    menuActionText[menuActionRow] = itemDef.inventoryActions[i4]
                                                            + " <col=FF9040>" + itemDef.name;
                                                    if (itemDef.inventoryActions[i4].contains("Wield")
                                                            || itemDef.inventoryActions[i4].contains("Wear")
                                                            || itemDef.inventoryActions[i4].contains("Value")
                                                            || itemDef.inventoryActions[i4].contains("Examine")) {
                                                        HoverMenuManager.showMenu = true;
                                                        HoverMenuManager.hintName = itemDef.name;
                                                        HoverMenuManager.hintId = itemDef.id;
                                                    } else {
                                                        HoverMenuManager.reset();
                                                    }
                                                    if (HoverMenuManager.showMenu && HoverMenuManager.drawType() == 1
                                                            && widget.parent != 3213) {
                                                        HoverMenuManager.drawHintMenu();
                                                    }
                                                    if (i4 == 0)
                                                        menuActionTypes[menuActionRow] = 74;
                                                    if (i4 == 1)
                                                        menuActionTypes[menuActionRow] = 454;
                                                    if (i4 == 2)
                                                        menuActionTypes[menuActionRow] = 539;
                                                    selectedMenuActions[menuActionRow] = itemDef.id;
                                                    firstMenuAction[menuActionRow] = itemSlot;
                                                    secondMenuAction[menuActionRow] = child.id;
                                                    menuActionRow++;
                                                }
                                            if (settings[ConfigUtility.SHIFT_CLICK_ID] == 1 && isShiftPressed) {
                                                menuActionText[menuActionRow] = "Drop <col=FF9040>" + itemDef.name;
                                                menuActionTypes[menuActionRow] = 847;
                                                selectedMenuActions[menuActionRow] = itemDef.id;
                                                firstMenuAction[menuActionRow] = itemSlot;
                                                secondMenuAction[menuActionRow] = child.id;
                                                menuActionRow++;
                                            }
                                        }
                                        int amount = 0;
                                        if (itemSlot != -1) {
                                            amount = child.inventoryAmounts[itemSlot];
                                        }
                                        if (child.actions != null) {
                                            int length = 6;

                                            boolean lootingBag = itemDef.id == ItemIdentifiers.LOOTING_BAG
                                                    || itemDef.id == ItemIdentifiers.LOOTING_BAG_22586;
                                            if (child.id == 5064 && lootingBag) {
                                                child.actions = new String[] { "Check", null, null, null, null, null,
                                                        null };
                                            } else if (child.id == 5064) {
                                                if (child.parent == 5063 && widget_overlay_id == 34400) {
                                                    child.actions = new String[] { null, null, null, null, null, null,
                                                            null };
                                                } else {
                                                    child.actions = new String[] { "Store 1", "Store 5", "Store 10",
                                                            "Store All", "Store X", null, null };
                                                }
                                            }
                                            if (child.parent == 34417) {
                                                switch (depositBoxOptionFirst) {
                                                    case "1":
                                                        child.actions = new String[] { "Store 1", "Store 5", "Store 10",
                                                                "Store All", "Store X", null, null };
                                                        break;
                                                    case "5":
                                                        child.actions = new String[] { "Store 5", "Store 1", "Store 10",
                                                                "Store All", "Store X", null, null };
                                                        break;
                                                    case "10":
                                                        child.actions = new String[] { "Store 10", "Store 1", "Store 5",
                                                                "Store All", "Store X", null, null };
                                                        break;
                                                    case "X":
                                                        child.actions = new String[] { "Store X", "Store 1", "Store 5",
                                                                "Store 10", "Store All", null, null };
                                                        break;
                                                    case "All":
                                                        child.actions = new String[] { "Store All", "Store 1",
                                                                "Store 5", "Store 10", "Store X", null, null };
                                                        break;
                                                }
                                            }
                                            if (child.parent == 5382) {
                                                if (amount != 0) {
                                                    child.actions = new String[] { "Withdraw-1", "Withdraw-5",
                                                            "Withdraw-10", "Withdraw-All", "Withdraw-X", null,
                                                            "Withdraw-All but one" };
                                                    if (modifiableXValue > 0) {
                                                        child.actions[5] = "Withdraw-" + modifiableXValue;
                                                    } else {
                                                        child.actions[5] = null;
                                                    }
                                                    boolean placeholder = Widget.cache[26101].active;
                                                    if (!placeholder) {
                                                        String[] newActions = new String[child.actions.length + 1];
                                                        for (int action = 0; action < newActions.length; action++) {
                                                            if (action == child.actions.length) {
                                                                newActions[action] = "Place holder";
                                                                continue;
                                                            }
                                                            newActions[action] = child.actions[action];
                                                        }
                                                        child.actions = newActions;
                                                        length = 7;
                                                    }
                                                } else {
                                                    menuActionsRow("Release <col=ff9040>" + itemDef.name, 1, 968, 2);
                                                }
                                            }

                                            if (amount != 0) {
                                                for (int type = length; type >= 0; type--) {
                                                    if (type > child.actions.length - 1)
                                                        continue;
                                                    if (type < child.actions.length && child.actions[type] != null) {
                                                        String action = child.actions[type];

                                                        // HARDCODING OF MENU ACTIONS
                                                        menuActionText[menuActionRow] = action + " <col=FF9040>"
                                                                + itemDef.name;
                                                        if (type == 0)
                                                            menuActionTypes[menuActionRow] = 632;
                                                        if (type == 1)
                                                            menuActionTypes[menuActionRow] = 78;
                                                        if (type == 2)
                                                            menuActionTypes[menuActionRow] = 867;
                                                        if (type == 3)
                                                            menuActionTypes[menuActionRow] = 431;
                                                        if (type == 4)
                                                            menuActionTypes[menuActionRow] = 53;
                                                        if (child.parent == 5382) {
                                                            if (child.actions[type] == null) {
                                                                if (type == 5)
                                                                    menuActionTypes[menuActionRow] = 291;
                                                            } else {
                                                                if (type == 5)
                                                                    menuActionTypes[menuActionRow] = 300;
                                                                if (type == 6)
                                                                    menuActionTypes[menuActionRow] = 291;
                                                            }
                                                        }
                                                        if (type == 7)
                                                            menuActionTypes[menuActionRow] = 968;

                                                        selectedMenuActions[menuActionRow] = itemDef.id;
                                                        firstMenuAction[menuActionRow] = itemSlot;
                                                        secondMenuAction[menuActionRow] = child.id;
                                                        menuActionRow++;
                                                    }
                                                }
                                            }
                                        }

                                        if (HoverMenuManager.shouldDraw(itemDef.id)) {
                                            HoverMenuManager.showMenu = true;
                                            HoverMenuManager.hintName = itemDef.name;
                                            HoverMenuManager.hintId = itemDef.id;
                                        }

                                        if (HoverMenuManager.showMenu && HoverMenuManager.drawType() == 1
                                                && widget.parent != 3213) {
                                            HoverMenuManager.drawHintMenu();
                                        }

                                        if (amount != 0 || widget_overlay_id != 41000) { // No examine on
                                            // Slayer Rewards
                                            // interface
                                            // Code commented out by Suic, this is wrong.
                                            // if (!child.displayExamine)
                                            // return;

                                            // If statement added by Suic
                                            if (child.displayExamine) {
                                                // If statement body moved into if statement by Suic
                                                menuActionText[menuActionRow] = (myPrivilege >= 2 && myPrivilege <= 4
                                                        && ClientConstants.DEBUG_MODE)
                                                                ? "Examine <col=FF9040>" + itemDef.name
                                                                        + " <col=65280>(<col=FFFFFF>"
                                                                        + (child.inventoryItemId[itemSlot] - 1)
                                                                        + "<col=65280>) int: " + child.id
                                                                : "Examine <col=FF9040>" + itemDef.name;
                                                menuActionTypes[menuActionRow] = 1125;
                                                selectedMenuActions[menuActionRow] = itemDef.id;
                                                firstMenuAction[menuActionRow] = itemSlot;
                                                secondMenuAction[menuActionRow] = child.id;
                                                menuActionRow++;
                                            }
                                        }
                                    }
                                }
                            }
                            itemSlot++;
                        }
                    }
                }
            }
        }
    }

    private void menuActionsRow(String action, int index, int actionId, int row) {
        if (isMenuOpen)
            return;
        menuActionText[index] = action;
        menuActionTypes[index] = actionId;
        menuActionRow = row;
    }

    private void checkHoverWithText(Widget childInterface, int i2, int j2) {
        if (!childInterface.clickable) {
            return;
        }
        if ((MouseHandler.mouseX < childInterface.width || MouseHandler.mouseY < childInterface.height
                || MouseHandler.mouseX > +childInterface.width
                || MouseHandler.mouseY > +childInterface.height)) {
            Widget.cache[childInterface.id + 1].textColour = 0xff981f;
        }
        if (MouseHandler.mouseX >= i2 && MouseHandler.mouseY >= j2 && MouseHandler.mouseX < i2 + childInterface.width
                && MouseHandler.mouseY < j2 + childInterface.height) {
            Widget.cache[childInterface.id + 1].textColour = 0xffffff;
        }

    }

    private boolean withinRange(int id, int min, int max) {
        return id >= min && id <= max;
    }

    private void checkFilters(Widget childInterface, int i2, int j2) {
        if (!childInterface.isRadioButton) {
            return;
        }
        if ((MouseHandler.mouseX < childInterface.width || MouseHandler.mouseY < childInterface.height
                || MouseHandler.mouseX > +childInterface.width
                || MouseHandler.mouseY > +childInterface.height)
                && childInterface.disabledSprite == SpriteCache.get(167)) {
            childInterface.disabledSprite = SpriteCache.get(166);
            childInterface.enabledSprite = SpriteCache.get(166);
        }
        if (MouseHandler.mouseX >= i2 && MouseHandler.mouseY >= j2 && MouseHandler.mouseX < i2 + childInterface.width
                && MouseHandler.mouseY < j2 + childInterface.height
                && childInterface.disabledSprite == SpriteCache.get(166)) {
            childInterface.disabledSprite = SpriteCache.get(167);
            childInterface.enabledSprite = SpriteCache.get(167);
        }
        if ((MouseHandler.mouseX < childInterface.width || MouseHandler.mouseY < childInterface.height
                || MouseHandler.mouseX > +childInterface.width
                || MouseHandler.mouseY > +childInterface.height)
                && childInterface.disabledSprite == SpriteCache.get(170)) {
            childInterface.disabledSprite = SpriteCache.get(169);
            childInterface.enabledSprite = SpriteCache.get(169);
        }
        if (MouseHandler.mouseX >= i2 && MouseHandler.mouseY >= j2 && MouseHandler.mouseX < i2 + childInterface.width
                && MouseHandler.mouseY < j2 + childInterface.height
                && childInterface.disabledSprite == SpriteCache.get(169)) {
            childInterface.disabledSprite = SpriteCache.get(170);
            childInterface.enabledSprite = SpriteCache.get(170);
        }
        if ((MouseHandler.mouseX < childInterface.width || MouseHandler.mouseY < childInterface.height
                || MouseHandler.mouseX > +childInterface.width
                || MouseHandler.mouseY > +childInterface.height)
                && childInterface.disabledSprite == SpriteCache.get(172)) {
            childInterface.disabledSprite = SpriteCache.get(171);
            childInterface.enabledSprite = SpriteCache.get(171);
        }
        if (MouseHandler.mouseX >= i2 && MouseHandler.mouseY >= j2 && MouseHandler.mouseX < i2 + childInterface.width
                && MouseHandler.mouseY < j2 + childInterface.height
                && childInterface.disabledSprite == SpriteCache.get(171)) {
            childInterface.disabledSprite = SpriteCache.get(172);
            childInterface.enabledSprite = SpriteCache.get(172);
        }
    }

    public void drawTransparentScrollBar(int x, int y, int height, int maxScroll, int pos) {
        SpriteCache.get(29).drawAdvancedSprite(x, y, 120);
        SpriteCache.get(30).drawAdvancedSprite(x, y + height - 16, 120);
        Rasterizer2D.drawTransparentVerticalLine(x, y + 16, height - 32, 0xffffff, 64);
        Rasterizer2D.drawTransparentVerticalLine(x + 15, y + 16, height - 32, 0xffffff, 64);
        int barHeight = (height - 32) * height / maxScroll;
        if (barHeight < 10) {
            barHeight = 10;
        }
        int barPos = 0;
        if (maxScroll != height) {
            barPos = (height - 32 - barHeight) * pos / (maxScroll - height);
        }
        Rasterizer2D.drawTransparentBoxOutline(x, y + 16 + barPos, 16,
                5 + y + 16 + barPos + barHeight - 5 - (y + 16 + barPos), 0xffffff, 32);
    }

    public void drawScrollbar(int height, int pos, int y, int x, int maxScroll, boolean transparent) {
        if (transparent) {
            drawTransparentScrollBar(x, y, height, maxScroll, pos);
        } else {
            scrollBar1.drawSprite(x, y);
            scrollBar2.drawSprite(x, (y + height) - 16);
            Rasterizer2D.draw_filled_rect(x, y + 16, 16, height - 32, 0x000001);
            Rasterizer2D.draw_filled_rect(x, y + 16, 15, height - 32, 0x3d3426);
            Rasterizer2D.draw_filled_rect(x, y + 16, 13, height - 32, 0x342d21);
            Rasterizer2D.draw_filled_rect(x, y + 16, 11, height - 32, 0x2e281d);
            Rasterizer2D.draw_filled_rect(x, y + 16, 10, height - 32, 0x29241b);
            Rasterizer2D.draw_filled_rect(x, y + 16, 9, height - 32, 0x252019);
            Rasterizer2D.draw_filled_rect(x, y + 16, 1, height - 32, 0x000001);
            if (maxScroll == 0)
                maxScroll = 1;
            int k1 = ((height - 32) * height) / maxScroll;
            if (k1 < 8) {
                k1 = 8;
            }
            int l1 = ((height - 32 - k1) * pos) / (maxScroll - height);
            Rasterizer2D.draw_filled_rect(x, y + 16 + l1, 16, k1, barFillColor);
            Rasterizer2D.draw_vertical_line(x, y + 16 + l1, k1, 0x000001);
            Rasterizer2D.draw_vertical_line(x + 1, y + 16 + l1, k1, 0x817051);
            Rasterizer2D.draw_vertical_line(x + 2, y + 16 + l1, k1, 0x73654a);
            Rasterizer2D.draw_vertical_line(x + 3, y + 16 + l1, k1, 0x6a5c43);
            Rasterizer2D.draw_vertical_line(x + 4, y + 16 + l1, k1, 0x6a5c43);
            Rasterizer2D.draw_vertical_line(x + 5, y + 16 + l1, k1, 0x655841);
            Rasterizer2D.draw_vertical_line(x + 6, y + 16 + l1, k1, 0x655841);
            Rasterizer2D.draw_vertical_line(x + 7, y + 16 + l1, k1, 0x61553e);
            Rasterizer2D.draw_vertical_line(x + 8, y + 16 + l1, k1, 0x61553e);
            Rasterizer2D.draw_vertical_line(x + 9, y + 16 + l1, k1, 0x5d513c);
            Rasterizer2D.draw_vertical_line(x + 10, y + 16 + l1, k1, 0x5d513c);
            Rasterizer2D.draw_vertical_line(x + 11, y + 16 + l1, k1, 0x594e3a);
            Rasterizer2D.draw_vertical_line(x + 12, y + 16 + l1, k1, 0x594e3a);
            Rasterizer2D.draw_vertical_line(x + 13, y + 16 + l1, k1, 0x514635);
            Rasterizer2D.draw_vertical_line(x + 14, y + 16 + l1, k1, 0x4b4131);
            Rasterizer2D.draw_horizontal_line(x, y + 16 + l1, 15, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 15, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 14, 0x655841);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 13, 0x6a5c43);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 11, 0x6d5f48);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 10, 0x73654a);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 7, 0x76684b);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 5, 0x7b6a4d);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 4, 0x7e6e50);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 3, 0x817051);
            Rasterizer2D.draw_horizontal_line(x, y + 17 + l1, 2, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 15, 0x564b38);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 14, 0x5d513c);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 11, 0x625640);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 10, 0x655841);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 7, 0x6a5c43);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 5, 0x6e6046);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 4, 0x716247);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 3, 0x7b6a4d);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 2, 0x817051);
            Rasterizer2D.draw_horizontal_line(x, y + 18 + l1, 1, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 15, 0x514635);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 14, 0x564b38);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 11, 0x5d513c);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 9, 0x61553e);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 7, 0x655841);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 5, 0x6a5c43);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 4, 0x6e6046);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 3, 0x73654a);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 2, 0x817051);
            Rasterizer2D.draw_horizontal_line(x, y + 19 + l1, 1, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 15, 0x4b4131);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 14, 0x544936);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 13, 0x594e3a);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 10, 0x5d513c);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 8, 0x61553e);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 6, 0x655841);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 4, 0x6a5c43);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 3, 0x73654a);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 2, 0x817051);
            Rasterizer2D.draw_horizontal_line(x, y + 20 + l1, 1, 0x000001);
            Rasterizer2D.draw_vertical_line(x + 15, y + 16 + l1, k1, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 15 + l1 + k1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 15, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 14, 0x3f372a);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 10, 0x443c2d);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 9, 0x483e2f);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 7, 0x4a402f);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 4, 0x4b4131);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 3, 0x564b38);
            Rasterizer2D.draw_horizontal_line(x, y + 14 + l1 + k1, 2, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 15, 0x443c2d);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 11, 0x4b4131);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 9, 0x514635);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 7, 0x544936);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 6, 0x564b38);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 4, 0x594e3a);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 3, 0x625640);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 2, 0x6a5c43);
            Rasterizer2D.draw_horizontal_line(x, y + 13 + l1 + k1, 1, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 15, 0x443c2d);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 14, 0x4b4131);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 12, 0x544936);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 11, 0x564b38);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 10, 0x594e3a);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 7, 0x5d513c);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 4, 0x61553e);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 3, 0x6e6046);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 2, 0x7b6a4d);
            Rasterizer2D.draw_horizontal_line(x, y + 12 + l1 + k1, 1, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 16, 0x000001);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 15, 0x4b4131);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 14, 0x514635);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 13, 0x564b38);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 11, 0x594e3a);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 9, 0x5d513c);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 7, 0x61553e);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 5, 0x655841);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 4, 0x6a5c43);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 3, 0x73654a);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 2, 0x7b6a4d);
            Rasterizer2D.draw_horizontal_line(x, y + 11 + l1 + k1, 1, 0x000001);
        }
    }

    private void updateNPCs(Buffer stream, int i) {
        removedMobCount = 0;
        mobsAwaitingUpdateCount = 0;
        method139(stream);
        updateNPCMovement(i, stream);
        npcUpdateMask(stream);
        for (int k = 0; k < removedMobCount; k++) {
            int l = removedMobs[k];
            if (npcs[l].last_update_tick != tick) {
                callbacks.post(new NpcDespawned(npcs[l]));
                npcs[l].definition = null;
                npcs[l] = null;
            }
        }

        if (stream.pos != i) {
            addReportToServer("NPC updating broke (stream position mismatch), this is very bad.");
            addReportToServer(
                    "Make sure to check buffer received datatypes in client match buffer sent datatypes from server.");
            SignLink.reporterror(myUsername + " size mismatch in getnpcpos - pos:" + stream.pos + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < npcs_in_region; i1++)
            if (npcs[local_npcs[i1]] == null) {
                addReportToServer("NPC updating broke, this is really bad.");
                SignLink.reporterror(myUsername + " null entry in npc list - pos:" + i1 + " size:" + npcs_in_region);
                throw new RuntimeException("eek");
            }

    }

    private int cButtonHPos;
    private int cButtonCPos;
    private int setChannel;

    public void processChatModeClick() {

        final int yOffset = !resized ? 0 : canvasHeight - 503;
        if (MouseHandler.mouseX >= 5 && MouseHandler.mouseX <= 61 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 0;
            update_chat_producer = true;
        } else if (MouseHandler.mouseX >= 71 && MouseHandler.mouseX <= 127 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 1;
            update_chat_producer = true;
        } else if (MouseHandler.mouseX >= 137 && MouseHandler.mouseX <= 193 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 2;
            update_chat_producer = true;
        } else if (MouseHandler.mouseX >= 203 && MouseHandler.mouseX <= 259 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 3;
            update_chat_producer = true;
        } else if (MouseHandler.mouseX >= 269 && MouseHandler.mouseX <= 325 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 4;
            update_chat_producer = true;
        } else if (MouseHandler.mouseX >= 335 && MouseHandler.mouseX <= 391 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 5;
            update_chat_producer = true;
        } else if (MouseHandler.mouseX >= 404 && MouseHandler.mouseX <= 515 && MouseHandler.mouseY >= yOffset + 482
                && MouseHandler.mouseY <= yOffset + 503) {
            cButtonHPos = 6;
            update_chat_producer = true;
        } else {
            cButtonHPos = -1;
            update_chat_producer = true;
        }
        if (MouseHandler.keypressedEventIndex == 1) {
            if (MouseHandler.saveClickX >= 5 && MouseHandler.saveClickX <= 61
                    && MouseHandler.saveClickY >= yOffset + 482
                    && MouseHandler.saveClickY <= yOffset + 505) {
                if (resized) {
                    if (setChannel != 0) {
                        cButtonCPos = 0;
                        chatTypeView = 0;
                        setChannel = 0;
                    } else {
                        showChatComponents = !showChatComponents;
                    }
                } else {
                    cButtonCPos = 0;
                    chatTypeView = 0;
                    setChannel = 0;
                }
            } else if (MouseHandler.saveClickX >= 71 && MouseHandler.saveClickX <= 127
                    && MouseHandler.saveClickY >= yOffset + 482
                    && MouseHandler.saveClickY <= yOffset + 505) {
                if (resized) {
                    if (setChannel != 1) {
                        cButtonCPos = 1;
                        chatTypeView = 5;
                        setChannel = 1;
                    } else {
                        showChatComponents = !showChatComponents;
                    }
                } else {
                    cButtonCPos = 1;
                    chatTypeView = 5;
                    setChannel = 1;
                }
            } else if (MouseHandler.saveClickX >= 137 && MouseHandler.saveClickX <= 193
                    && MouseHandler.saveClickY >= yOffset + 482
                    && MouseHandler.saveClickY <= yOffset + 505) {
                if (resized) {
                    if (setChannel != 2) {
                        cButtonCPos = 2;
                        chatTypeView = 1;
                        setChannel = 2;
                    } else {
                        showChatComponents = !showChatComponents;
                    }
                } else {
                    cButtonCPos = 2;
                    chatTypeView = 1;
                    setChannel = 2;
                }
            } else if (MouseHandler.saveClickX >= 203 && MouseHandler.saveClickX <= 259
                    && MouseHandler.saveClickY >= yOffset + 482
                    && MouseHandler.saveClickY <= yOffset + 505) {
                if (resized) {
                    if (setChannel != 3) {
                        cButtonCPos = 3;
                        chatTypeView = 2;
                        setChannel = 3;
                    } else {
                        showChatComponents = !showChatComponents;
                    }
                } else {
                    cButtonCPos = 3;
                    chatTypeView = 2;
                    setChannel = 3;
                }
            } else if (MouseHandler.saveClickX >= 269 && MouseHandler.saveClickX <= 325
                    && MouseHandler.saveClickY >= yOffset + 482
                    && MouseHandler.saveClickY <= yOffset + 505) {
                if (resized) {
                    if (setChannel != 4) {
                        cButtonCPos = 4;
                        chatTypeView = 11;
                        setChannel = 4;
                    } else {
                        showChatComponents = !showChatComponents;
                    }
                } else {
                    cButtonCPos = 4;
                    chatTypeView = 11;
                    setChannel = 4;
                }
            } else if (MouseHandler.saveClickX >= 335 && MouseHandler.saveClickX <= 391
                    && MouseHandler.saveClickY >= yOffset + 482
                    && MouseHandler.saveClickY <= yOffset + 505) {
                if (resized) {
                    if (setChannel != 5) {
                        cButtonCPos = 5;
                        chatTypeView = 3;
                        setChannel = 5;
                    } else {
                        showChatComponents = !showChatComponents;
                    }
                } else {
                    cButtonCPos = 5;
                    chatTypeView = 3;
                    setChannel = 5;
                }
            } else if (MouseHandler.saveClickX >= 404 && MouseHandler.saveClickX <= 515
                    && MouseHandler.saveClickY >= yOffset + 482 && MouseHandler.saveClickY <= yOffset + 505) {
                if (widget_overlay_id == -1) {
                    clearTopInterfaces();
                    reportAbuseInput = "";
                    canMute = false;
                    for (int i = 0; i < Widget.cache.length; i++) {
                        if (Widget.cache[i] == null || Widget.cache[i].contentType != 600) {
                            continue;
                        }
                        reportAbuseInterfaceID = widget_overlay_id = Widget.cache[i].parent;
                        break;
                    }
                } else {
                    sendMessage("Please close the interface you have open before using this.", 0, "");
                }
            }
        }
    }

    public boolean updateVarp(final int varpId) {
        ObjectSound.update();
        var varp = VariablePlayer.lookup(varpId);
        int varpType = varp.type;
        int state = settings[varpId];
        ObjectSound.update();
        if (varpType == 0 && varpId == 166) {

            if (state == 1) {
                Settings.adjustBrightness(0.9D);
                setting.save();
            }

            if (state == 2) {
                Settings.adjustBrightness(0.8D);
                setting.save();
            }

            if (state == 3) {
                Settings.adjustBrightness(0.7D);
                setting.save();
            }

            if (state == 4) {
                Settings.adjustBrightness(0.6D);
                setting.save();
            }

            update_producers = true;
        }

        if (varpType == 0 && varpId == 168) {
            if (state == 4) {
                StaticSound.updateMusicVolume(127);
                setting.save();
            }
            if (state == 3) {
                StaticSound.updateMusicVolume(96);
                setting.save();
            }
            if (state == 2) {
                StaticSound.updateMusicVolume(64);
                setting.save();
            }
            if (state == 1) {
                StaticSound.updateMusicVolume(32);
                setting.save();
            }
            if (state == 0) {
                StaticSound.updateMusicVolume(0);
                setting.save();
            }
        }

        if (varpType == 0 && varpId == 169) {
            if (state == 4) {
                StaticSound.updateSoundEffectVolume(127);
                setting.save();
            }
            if (state == 3) {
                StaticSound.updateSoundEffectVolume(96);
                setting.save();
            }
            if (state == 2) {
                StaticSound.updateSoundEffectVolume(64);
                setting.save();
            }
            if (state == 1) {
                StaticSound.updateSoundEffectVolume(32);
                setting.save();
            }
            if (state == 0) {
                StaticSound.updateSoundEffectVolume(0);
                setting.save();
            }
        }

        if (varpType == 0 && varpId == 780) {
            if (state == 4) {
                StaticSound.updateAreaVolume(127);
                setting.save();
            }
            if (state == 3) {
                StaticSound.updateAreaVolume(96);
                setting.save();
            }
            if (state == 2) {
                StaticSound.updateAreaVolume(64);
                setting.save();
            }
            if (state == 1) {
                StaticSound.updateAreaVolume(32);
                setting.save();
            }
            if (state == 0) {
                StaticSound.updateAreaVolume(0);
                setting.save();
            }
        }

        if (varpType == 5) {
            useOneMouseButton = state;
        }

        if (varpType == 6) {
            showSpokenEffects = state;
        }

        if (varpType == 8) {
            Client.instance.totalMessages = 0;
            splitPrivateChat = state;
            update_chat_producer = true;
        }

        if (varpType == 9) {
            anInt913 = state;
        }

        if (varpType == 19) {
            if (state == -1) {
                interactingWithEntityId = -1;
            } else {
                interactingWithEntityId = state & 2047;
            }
        }

        return true;
    }

    public void updateEntities() {
        try {
            int total_messages = 0;

            for (int entity_index = -1; entity_index < players_in_region + npcs_in_region; entity_index++) {
                final Entity entity;

                if (entity_index == -1) {
                    entity = local_player;
                } else if (entity_index < players_in_region) {
                    entity = players[local_players[entity_index]];
                } else {
                    entity = npcs[local_npcs[entity_index - players_in_region]];
                }

                if (entity == null || !entity.visible() || !shouldDraw(entity, true)) {
                    continue;
                }

                if (entity instanceof Npc) {
                    NpcDefinition entityDef = ((Npc) entity).definition;
                    if (entityDef.transforms != null) {
                        entityDef = entityDef.get_configs();
                    }
                    if (entityDef == null) {
                        continue;
                    }
                }

                if (entity_index < players_in_region) {
                    int offset = 30;
                    final Player player = (Player) entity;
                    get_entity_scene_pos(entity, entity.defaultHeight + 15);
                    if (scene_draw_x > -1) {
                        if (entity.game_tick_status > tick) {
                            if (setting.toggle_overhead_hp) {
                                offset = 5;
                                smallFont.drawCenteredString(
                                        "<trans=220>" + entity.current_hitpoints + "/" + entity.maximum_hitpoints,
                                        scene_draw_x, scene_draw_y - offset, calcHitpointColor(entity), 1);
                                offset += (setting.toggle_overhead_names ? 12 : 35);
                            }
                        }

                        if (setting.toggle_overhead_names) {
                            int color = 0xffffff;
                            if (!setting.toggle_overhead_hp || entity.game_tick_status < tick)
                                offset = 5;

                            if (player != local_player)
                                color = 0x27c497;

                            smallFont.drawCenteredString(player.username, scene_draw_x, scene_draw_y - offset, color,
                                    1);
                            offset += 35;
                        }
                    }
                    if (player.overhead_icon >= 0) {
                        if (scene_draw_x > -1) {
                            if (player.skull_icon < 5) {
                                skullIcons[player.skull_icon].drawSprite(scene_draw_x - 12, scene_draw_y - offset);
                                offset += 25;
                            }
                            if (player.overhead_icon < 18) {
                                headIcons[player.overhead_icon].drawSprite(scene_draw_x - 12, scene_draw_y - offset);
                                offset += 25;
                            }
                        }
                    }
                    if (entity_index >= 0 && hintIconDrawType == 10
                            && hintIconPlayerId == local_players[entity_index]) {
                        if (scene_draw_x > -1) {
                            offset += 13;
                            headIconsHint[player.hint_arrow_icon].drawSprite(scene_draw_x - 12, scene_draw_y - offset);
                        }
                    }
                } else {
                    int offset = 30;
                    Npc npc = ((Npc) entity);
                    NpcDefinition def = npc.definition;
                    get_entity_scene_pos(entity, entity.defaultHeight + 15);
                    if (scene_draw_x > -1) {
                        if (npc.game_tick_status > tick) {
                            if (setting.toggle_npc_overhead_hp && def.combatLevel > 0) {
                                offset = 5;
                                smallFont.drawCenteredString(
                                        "<trans=220>" + npc.current_hitpoints + "/" + npc.maximum_hitpoints,
                                        scene_draw_x, scene_draw_y - offset, calcHitpointColor(npc), 1);
                                offset += (setting.toggle_npc_overhead_names ? 12 : 35);
                            }
                        }
                        if (setting.toggle_npc_overhead_names) {
                            if (!setting.toggle_npc_overhead_hp || def.combatLevel == 0 || npc.game_tick_status < tick)
                                offset = 5;

                            smallFont.drawCenteredString(
                                    "<trans=140>" + (def.combatLevel == 0 ? def.name
                                            : def.name + get_level_diff(local_player.combat_level, def.combatLevel)
                                                    + " (level-" + def.combatLevel + ")"),
                                    scene_draw_x, scene_draw_y - offset, 0xffff33, 1);
                            offset += 35;
                        }
                    }
                    if (npc.getHeadIcon() >= 0 && npc.getHeadIcon() < headIcons.length) {
                        if (scene_draw_x > -1)
                            headIcons[npc.getHeadIcon()].drawSprite(scene_draw_x - 12, scene_draw_y - offset);
                        offset += 25;
                    }
                    if (hintIconDrawType == 1 && hintIconNpcId == local_npcs[entity_index - players_in_region]
                            && tick % 20 < 10) {
                        if (scene_draw_x > -1) {
                            headIconsHint[0].drawSprite(scene_draw_x - 12, scene_draw_y - 28);
                        }
                    }
                }
                if (entity.spokenText != null
                        && (entity_index >= players_in_region || set_public_channel == 0 || set_public_channel == 3
                                || set_public_channel == 1 && check_username(((Player) entity).username))) {
                    get_entity_scene_pos(entity, entity.defaultHeight);
                    if (scene_draw_x > -1 && total_messages < spokenMaxCount) {
                        scene_text_center_x[total_messages] = boldFont.getTextWidth(entity.spokenText) / 2;
                        scene_text_height[total_messages] = boldFont.ascent;
                        scene_text_x[total_messages] = scene_draw_x;
                        scene_text_y[total_messages] = scene_draw_y;
                        scene_text_color[total_messages] = entity.textColour;
                        scene_text_effect[total_messages] = entity.textEffect;
                        scene_text_update_cycle[total_messages] = entity.message_cycle;
                        spokenMessage[total_messages++] = entity.spokenText;
                        // System.out.println("colorCode: "+entity.textColour+" vs effectCode:
                        // "+entity.textEffect);
                        if (showSpokenEffects == 0 && entity.textEffect >= 1 && entity.textEffect <= 3) {
                            scene_text_height[total_messages] += 10;
                            scene_text_y[total_messages] += 5;
                        }

                        if (showSpokenEffects == 0 && entity.textEffect == 4) {
                            scene_text_center_x[total_messages] = 60;
                        }

                        if (showSpokenEffects == 0 && entity.textEffect == 5) {
                            scene_text_height[total_messages] += 5;
                        }
                    }
                }
                // hitmarks
                for (int hit_index = 0; hit_index < 4; hit_index++) {
                    if (entity.damage_cycle[hit_index] > tick) {
                        get_entity_scene_pos(entity, entity.defaultHeight / 2);
                        if (scene_draw_x > -1) {
                            if (hit_index == 1) {// top
                                scene_draw_y -= 20;
                            }
                            if (hit_index == 2) {// left
                                scene_draw_x -= 15;
                                scene_draw_y -= 10;
                            }

                            if (hit_index == 3) {// right
                                scene_draw_x += 15;
                                scene_draw_y -= 10;
                            }

                            hitMarks[entity.primary_hitmark_id[hit_index]].drawSprite(scene_draw_x - 12,
                                    scene_draw_y - 12);
                            smallFont.drawCenteredString(String.valueOf(entity.primary_hitmark_damage[hit_index]),
                                    scene_draw_x, scene_draw_y + 4, 0xffffff, 1);
                        }
                    }
                }

                boolean combatDummy = false;
                if (entity instanceof Npc) {
                    final Npc npc = (Npc) entity;
                    if (npc.definition.id == 2668 || npc.definition.id == 7413) {
                        combatDummy = true;
                    }
                }

                if (entity.game_tick_status > tick && !combatDummy) {
                    try {
                        get_entity_scene_pos(entity, entity.defaultHeight + 15);
                        if (scene_draw_x > -1) {
                            int current = (entity.current_hitpoints * 30) / entity.maximum_hitpoints;

                            int size = entity.getHealthDimension();
                            HealthBar healthBar = entity.getHealthBar();

                            if (size == HealthBar.DIM_30 && !(entity instanceof Player)
                                    && entity.maximum_hitpoints >= 5000) {
                                size = HealthBar.DIM_120;
                            }

                            if (entity instanceof Npc) {
                                Npc n = (Npc) entity;

                                if (getRegionId() == 11601 && n.definition.name.equals("Nex")
                                        && HealthHud.getHudType() == HealthHud.HudType.REGULAR) {
                                    size = HealthBar.DIM_120;
                                    healthBar = HealthBar.DEFAULT;
                                }

                                if (n.definition.id == 10572) {
                                    size = HealthBar.DIM_100;
                                    healthBar = HealthBar.CYAN;
                                } else if (n.definition.id == 10574) {
                                    size = HealthBar.DIM_100;
                                    healthBar = HealthBar.DEFAULT;
                                } else if (n.definition.id == 10575) {
                                    size = HealthBar.DIM_100;
                                    healthBar = HealthBar.ORANGE;
                                } else if (n.definition.id == 10814 || n.definition.id == 10815
                                        || n.definition.id == 10816 || n.definition.id == 10817) {
                                    size = HealthBar.DIM_100;
                                    healthBar = HealthBar.DEFAULT;
                                } else if (n.definition.id == 8359) {
                                    size = HealthBar.DIM_100;
                                    healthBar = HealthBar.DEFAULT;
                                } else if (n.definition.id == 9425) {
                                    size = HealthBar.DIM_100;
                                    healthBar = HealthBar.CYAN;
                                } else if (n.definition.id == 9444 || n.definition.id == 9445 || n.definition.id == 9441
                                        || n.definition.id == 9442 || n.definition.id == 9435 || n.definition.id == 9436
                                        || n.definition.id == 9439 || n.definition.id == 9438) {
                                    size = HealthBar.DIM_50;
                                    healthBar = HealthBar.YELLOW;
                                }
                            }

                            current = (entity.current_hitpoints * size) / entity.maximum_hitpoints;
                            Rasterizer2D.drawPixels(5, scene_draw_y - 3, scene_draw_x - size / 2,
                                    healthBar.getMainColor(), current);
                            Rasterizer2D.drawPixels(5, scene_draw_y - 3, (scene_draw_x - size / 2) + current,
                                    healthBar.getBackColor(), size - current);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int message_index = 0; message_index < total_messages; message_index++) {
                int raster_x = scene_text_x[message_index];
                int raster_y = scene_text_y[message_index];
                int center_x = scene_text_center_x[message_index];
                int message_height = scene_text_height[message_index];
                boolean update = true;
                while (update) {
                    update = false;
                    for (int index = 0; index < message_index; index++) {
                        if (raster_y + 2 > scene_text_y[index] - scene_text_height[index]
                                && raster_y - message_height < scene_text_y[index] + 2
                                && raster_x - center_x < scene_text_x[index] + scene_text_center_x[index]
                                && raster_x + center_x > scene_text_x[index] - scene_text_center_x[index]
                                && scene_text_y[index] - scene_text_height[index] < raster_y) {
                            raster_y = scene_text_y[index] - scene_text_height[index];
                            update = true;
                        }
                    }
                }
                scene_draw_x = scene_text_x[message_index];
                scene_draw_y = scene_text_y[message_index] = raster_y;
                String message = spokenMessage[message_index];
                // System.out.println("showSpokenEffects: "+showSpokenEffects);
                if (showSpokenEffects == 0) {
                    int color = 0xffff00;
                    if (scene_text_color[message_index] < 6) // send_color
                        color = SPOKEN_PALETTE[scene_text_color[message_index]];
                    if (scene_text_color[message_index] == 6) // flash1
                        color = viewportDrawCount % 20 >= 10 ? 0xffff00 : 0xff0000;
                    if (scene_text_color[message_index] == 7) // flash2
                        color = viewportDrawCount % 20 >= 10 ? 65535 : 255;
                    if (scene_text_color[message_index] == 8) // flash3
                        color = viewportDrawCount % 20 >= 10 ? 0x80ff80 : 45056;
                    if (scene_text_color[message_index] == 9) { // glow1
                        int timer = 150 - scene_text_update_cycle[message_index];
                        if (timer < 50)
                            color = 0xff0000 + 1280 * timer;
                        else if (timer < 100)
                            color = 0xffff00 - 0x50000 * (timer - 50);
                        else if (timer < 150)
                            color = 65280 + 5 * (timer - 100);
                    }
                    if (scene_text_color[message_index] == 10) { // glow2
                        int cycle = 150 - scene_text_update_cycle[message_index];
                        if (cycle < 50)
                            color = 0xff0000 + 5 * cycle;
                        else if (cycle < 100)
                            color = 0xff00ff - 0x50000 * (cycle - 50);
                        else if (cycle < 150)
                            color = (255 + 0x50000 * (cycle - 100)) - 5 * (cycle - 100);
                    }
                    if (scene_text_color[message_index] == 11) {// glow3
                        int cycle = 150 - scene_text_update_cycle[message_index];// next change
                        if (cycle < 50)
                            color = 0xffffff - 0x50005 * cycle;
                        else if (cycle < 100)
                            color = 65280 + 0x50005 * (cycle - 50);
                        else if (cycle < 150)
                            color = 0xffffff - 0x50000 * (cycle - 100);
                    }
                    if (scene_text_effect[message_index] == ClientConstants.NO_EFFECT) {
                        boldFont.drawCenteredString(message, scene_draw_x, scene_draw_y, color, true);
                    }
                    if (scene_text_effect[message_index] == ClientConstants.WAVE) {
                        boldFont.drawCenteredWave(message, scene_draw_x, scene_draw_y, color, 0, viewportDrawCount);
                    }
                    if (scene_text_effect[message_index] == ClientConstants.WAVE_2) {
                        boldFont.drawCenteredWaveWithShaking(message, scene_draw_x, scene_draw_y, color, 0,
                                viewportDrawCount);
                    }
                    if (scene_text_effect[message_index] == ClientConstants.SHAKE) {
                        boldFont.drawCenteredShakeWithVariance(message, scene_draw_x, scene_draw_y, color, 0,
                                viewportDrawCount,
                                150 - scene_text_update_cycle[message_index]);
                    }
                    if (scene_text_effect[message_index] == ClientConstants.SCROLL) {
                        int width = boldFont.getTextWidth(message);
                        int horizontal_offset = ((150 - scene_text_update_cycle[message_index]) * (width + 100)) / 150;
                        Rasterizer2D.setDrawingArea(scene_draw_x - 50, 0, scene_draw_x + 50, 334);
                        boldFont.draw(message, (scene_draw_x + 50) - horizontal_offset, scene_draw_y, color, true);
                        Rasterizer2D.set_default_size();
                    }
                    if (scene_text_effect[message_index] == ClientConstants.SLIDE) {
                        int delay = 150 - scene_text_update_cycle[message_index];
                        int vertical_offset = 0;
                        if (delay < 25)
                            vertical_offset = delay - 25;
                        else if (delay > 125)
                            vertical_offset = delay - 125;

                        Rasterizer2D.setDrawingArea(0, scene_draw_y - boldFont.ascent - 1, 512,
                                scene_draw_y + 5);
                        boldFont.drawCenteredString(message, scene_draw_x, scene_draw_y + 1 + vertical_offset, color,
                                true);
                        Rasterizer2D.set_default_size();
                    }
                } else {
                    boldFont.drawCenteredString(message, scene_draw_x, scene_draw_y + 1, 0xffff00, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
    }

    /**
     * All the tab identifications
     * <p>
     * ATTACK_TAB = 0 SKILL_TAB = 1 QUEST_TAB = 2 INVENTORY_TAB = 3 EQUIPMENT_TAB =
     * 4 PRAYER_TAB = 5 MAGIC_TAB = 6 CLAN_TAB = 7 FRIENDS_TAB = 8 IGNORE_TAB = 9
     * LOGOUT_TAB = 10 OPTIONS_TAB = 11 EMOTE_TAB = 12 MUSIC_TAB = 13
     */
    private final int[] fixed_side_icon = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 },
            fixed_tab_id = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 },
            fixed_side_icon_x = { 17, 49, 83, 114, 146, 180, 214, 16, 49, 85, 116, 148, 184, 217 },
            fixed_side_icon_y = { 9, 7, 6, 5, 2, 3, 7, 303, 306, 306, 302, 305, 303, 304, 303 };

    private final int[] resizable_side_icon = { 0, 1, 2, 3, 4, 5, 6, -1, 8, 9, 7, 11, 12, 13 },
            resizable_side_icon_x = { 219, 189, 156, 126, 93, 62, 30, 219, 189, 156, 124, 92, 59, 28 },
            resizable_side_icon_y = { 66, 67, 67, 69, 71, 70, 67, 32, 31, 31, 32, 31, 33, 32, 33 };

    private final int[] fullscreen_resizable_side_icon = { 0, 1, 2, 3, 4, 5, 6, -1, 8, 9, 7, 11, 12, 13 },
            fullscreen_resizable_side_icon_x = { 50, 80, 114, 143, 176, 208, 240, 242, 273, 306, 338, 370, 404, 433 },
            fullscreen_resizable_side_icon_y = { 30, 31, 30, 32, 35, 34, 30, 31, 31, 31, 32, 31, 32, 32, 32 };
    private int questTabId;
    public boolean isViewingQuests = true;
    public int spellbook = 0;

    /**
     * This method draws the icons on the tab interfaces
     */
    private void drawSideIcons() {

        final int x = !isResized() ? 516 : canvasWidth - 247;
        final int y = !isResized() ? 168 : canvasHeight - 336;

        if (!resized || resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
            for (int tab = 0; tab < fixed_tab_id.length; tab++) {
                if (tabInterfaceIDs[fixed_tab_id[tab]] != -1) {
                    if (fixed_side_icon[tab] != -1) {
                        SimpleImage sprite = sideIcons[fixed_side_icon[tab]];
                        // Replace music tab icon
                        if (tab == 13) {
                            SpriteCache.get(1912).drawAdvancedSprite(fixed_side_icon_x[tab] + x,
                                    fixed_side_icon_y[tab] + y);
                            continue;
                        }
                        if (tab == 2) {// Edit quest tab icons for fixed here:
                            if (questTabId == 0) {
                                sprite.drawSprite(fixed_side_icon_x[tab] + x + 1, fixed_side_icon_y[tab] + y + 1);
                                continue;
                            } else if (questTabId == 1) {
                                SpriteCache.get(456).drawSprite(fixed_side_icon_x[tab] + x - 3,
                                        fixed_side_icon_y[tab] + y - 5);
                                continue;
                            } else if (questTabId == 2) {
                                SpriteCache.get(848).drawSprite(fixed_side_icon_x[tab] + x - 3,
                                        fixed_side_icon_y[tab] + y - 5);
                                continue;
                            }
                        } else if (tab == 6) {
                            if (spellbook == 0) {
                                sprite.drawSprite(fixed_side_icon_x[tab] + x, fixed_side_icon_y[tab] + y);
                                continue;
                            } else if (spellbook == 1) {
                                SpriteCache.get(772).drawSprite(fixed_side_icon_x[tab] + x, fixed_side_icon_y[tab] + y);
                                continue;
                            } else if (spellbook == 2) {
                                SpriteCache.get(773).drawSprite(fixed_side_icon_x[tab] + x, fixed_side_icon_y[tab] + y);
                                continue;
                            } else if (spellbook == 3) {
                                SpriteCache.get(2088).drawSprite(fixed_side_icon_x[tab] + x - 4,
                                        fixed_side_icon_y[tab] + y - 6);
                                continue;
                            }
                        }
                        sprite.drawSprite(fixed_side_icon_x[tab] + x, fixed_side_icon_y[tab] + y);
                    }
                }
            }
        }

        // Resizeable
        if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1 && canvasWidth < 1000) {
            for (int tab = 0; tab < fixed_tab_id.length; tab++) {
                if (tabInterfaceIDs[fixed_tab_id[tab]] != -1) {
                    if (resizable_side_icon[tab] != -1) {
                        SimpleImage sprite = sideIcons[resizable_side_icon[tab]];
                        if (tab == 13) {
                            SpriteCache.get(1912).drawAdvancedSprite(canvasWidth - resizable_side_icon_x[tab],
                                    canvasHeight - resizable_side_icon_y[tab]);
                            continue;
                        }
                        if (tab == 2) {// Edit quest tab icons for resizable here:
                            if (questTabId == 0) {
                                sprite.drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                        canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            } else if (questTabId == 1) {
                                SpriteCache.get(456).drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                        canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            } else if (questTabId == 2) {
                                SpriteCache.get(848).drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                        canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            }
                        } else if (tab == 6) {
                            if (spellbook == 0) {
                                sprite.drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                        canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            } else if (spellbook == 1) {
                                SpriteCache.get(772)
                                        .drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                                canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            } else if (spellbook == 2) {
                                SpriteCache.get(773)
                                        .drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                                canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            } else if (spellbook == 3) {
                                SpriteCache.get(2088)
                                        .drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                                canvasHeight - resizable_side_icon_y[tab]);
                                continue;
                            }
                        } else {
                            sprite.drawSprite(canvasWidth - resizable_side_icon_x[tab],
                                    canvasHeight - resizable_side_icon_y[tab]);
                        }
                    }
                }
            }
        }

        // Fullscreen resizable
        if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1
                && canvasWidth >= 1000) {
            for (int tab = 0; tab < fixed_tab_id.length; tab++) {
                if (tabInterfaceIDs[fixed_tab_id[tab]] != -1) {
                    if (fullscreen_resizable_side_icon[tab] != -1) {
                        SimpleImage sprite = sideIcons[fullscreen_resizable_side_icon[tab]];
                        if (tab == 13) {
                            SpriteCache.get(1912).drawAdvancedSprite(
                                    canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                    canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                            continue;
                        }
                        if (tab == 2) {
                            if (questTabId == 0) {
                                sprite.drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                        canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            } else if (questTabId == 1) {
                                SpriteCache.get(456)
                                        .drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                                canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            } else if (questTabId == 2) {
                                SpriteCache.get(848)
                                        .drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                                canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            }
                        } else if (tab == 6) {
                            if (spellbook == 0) {
                                sprite.drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                        canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            } else if (spellbook == 1) {
                                SpriteCache.get(772)
                                        .drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                                canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            } else if (spellbook == 2) {
                                SpriteCache.get(773)
                                        .drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                                canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            } else if (spellbook == 3) {
                                SpriteCache.get(2088)
                                        .drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                                canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                                continue;
                            }
                        }
                        sprite.drawSprite(canvasWidth - 461 + fullscreen_resizable_side_icon_x[tab],
                                canvasHeight - fullscreen_resizable_side_icon_y[tab]);
                    }
                }
            }
        }
    }

    private final int[] fixed_red_stones_id = { 35, 39, 39, 39, 39, 39, 36, 37, 39, 39, 39, 39, 39, 38 },
            fixed_red_stones_x = { 6, 44, 77, 110, 143, 176, 209, 6, 44, 77, 110, 143, 176, 209 },
            fixed_red_stones_y = { 0, 0, 0, 0, 0, 0, 0, 298, 298, 298, 298, 298, 298, 298 };

    private final int[] resizable_red_stones_x = { 226, 194, 162, 130, 99, 65, 34, 219, 195, 161, 130, 98, 65, 33 },
            resizable_red_stones_y = { 73, 73, 73, 73, 73, 73, 73, -1, 37, 37, 37, 37, 37, 37, 37 };

    private final int[] fullscreen_resizable_red_stones_x = { 417, 385, 353, 321, 289, 256, 224, 129, 193, 161, 130, 98,
            65, 33 };

    private void drawRedStones() {

        final int x = !isResized() ? 516 : canvasWidth - 247;
        final int y = !isResized() ? 168 : canvasHeight - 336;
        // Fixed game mode
        if (!resized
                || resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
            if (tabInterfaceIDs[sidebarId] != -1 && sidebarId != 15) {
                SpriteCache.get(fixed_red_stones_id[sidebarId])
                        .drawSprite(fixed_red_stones_x[sidebarId] + x,
                                fixed_red_stones_y[sidebarId] + y);
            }
        }

        // Resizable game mode
        if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1
                && canvasWidth < 1000) {
            if (tabInterfaceIDs[sidebarId] != -1 && sidebarId != 10 && showTabComponents) {
                if (sidebarId == 7) {
                    SpriteCache.get(39).drawSprite(canvasWidth - 130, canvasHeight - 37);
                }
                SpriteCache.get(39)
                        .drawSprite(canvasWidth - resizable_red_stones_x[sidebarId],
                                canvasHeight - resizable_red_stones_y[sidebarId]);
            }
        }

        // Fullscreen resizable game mode
        if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1
                && canvasWidth >= 1000) {
            if (tabInterfaceIDs[sidebarId] != -1 && sidebarId != 10 && showTabComponents) {
                SpriteCache.get(39)
                        .drawSprite(canvasWidth - fullscreen_resizable_red_stones_x[sidebarId],
                                canvasHeight - 37);
            }
        }
    }

    public String getNameForTab(int tab) {
        switch (tab) {
            case 0:
                return "Combat";
            case 1:
                return "Stats";
            case 2:
                return "Spawn tab";
            case 3:
                return "Inventory";
            case 4:
                return "Equipment";
            case 5:
                return "Prayer";
            case 6:
                return "Magic";
            case 7:
                return "Clan chat";
            case 8:
                return "Friends";
            case 9:
                return "Ignores";
            case 10:
                return "Logout";
            case 11:
                return "Settings";
            case 12:
                return "Emotes";
            case 13:
                return "PvP";
        }
        return "";
    }

    private void drawTabArea() {
        final int xOffset = !isResized() ? 516 : canvasWidth - 241;
        final int yOffset = !isResized() ? 168 : canvasHeight - 336;

        if (!resized) {
            SpriteCache.get(21).drawSprite(xOffset, yOffset);
        } else if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
            Rasterizer2D.draw_filled_rect(canvasWidth - 217, canvasHeight - 304, 195, 270, 0x3E3529,
                    settings[ConfigUtility.TRANSPARENT_SIDE_PANEL_ID] == 1 ? 120 : 255);
            SpriteCache.get(47).drawSprite(xOffset, yOffset);
        } else {
            if (canvasWidth >= 1000) {
                if (showTabComponents) {
                    Rasterizer2D.draw_filled_rect(canvasWidth - 197, canvasHeight - 304, 197, 265, 0x3E3529,
                            settings[ConfigUtility.TRANSPARENT_SIDE_PANEL_ID] == 1 ? 80 : 255);
                    SpriteCache.get(50).drawSprite(canvasWidth - 204, canvasHeight - 311);
                }
                for (int x = canvasWidth - 417, y = canvasHeight - 37, index = 0; x <= canvasWidth - 30
                        && index < 13; x += 32, index++) {
                    SpriteCache.get(46).drawSprite(x, y);
                }
            } else if (canvasWidth < 1000) {
                if (showTabComponents) {
                    Rasterizer2D.draw_filled_rect(canvasWidth - 197, canvasHeight - 341, 195, 265, 0x3E3529,
                            settings[ConfigUtility.TRANSPARENT_SIDE_PANEL_ID] == 1 ? 80 : 255);
                    SpriteCache.get(50).drawSprite(canvasWidth - 204, canvasHeight - 348);
                }
                for (int x = canvasWidth - 226, y = canvasHeight - 73, index = 0; x <= canvasWidth - 32
                        && index < 7; x += 32, index++) {
                    SpriteCache.get(46).drawSprite(x, y);
                }
                for (int x = canvasWidth - 226, y = canvasHeight - 37, index = 0; x <= canvasWidth - 32
                        && index < 7; x += 32, index++) {
                    SpriteCache.get(46).drawSprite(x, y);
                }
            }
        }
        if (overlayInterfaceId == -1) {
            drawRedStones();
            drawSideIcons();
        }
        if (showTabComponents) {
            int x = !resized ? 31 + xOffset : canvasWidth - 215;
            int y = !resized ? 37 + yOffset : canvasHeight - 299;
            if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1) {
                x = canvasWidth - 197;
                y = canvasWidth >= 1000 ? canvasHeight - 303 : canvasHeight - 340;
            }
            try {
                if (overlayInterfaceId != -1) {
                    drawInterface(Widget.cache[overlayInterfaceId], x, y, 0);
                } else if (tabInterfaceIDs[sidebarId] != -1) {
                    drawInterface(Widget.cache[tabInterfaceIDs[sidebarId]], x, y, 0);
                    if (sidebarId == 5 && prayerGrabbed != null) {
                        Widget.cache[prayerGrabbed.spriteId].enabledSprite.draw_transparent(MouseHandler.mouseX - 12,
                                MouseHandler.mouseY - 12, 100);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                addReportToServer(ex.getMessage());
            }
        }

        // bars.drawStatusBars(xOffset, yOffset);

    }

    private void processMobChatText() {
        for (int i = -1; i < players_in_region; i++) {
            int j;
            if (i == -1)
                j = LOCAL_PLAYER_INDEX;
            else
                j = local_players[i];
            Player player = players[j];
            if (player != null && player.message_cycle > 0) {
                player.message_cycle--;
                if (player.message_cycle == 0)
                    player.spokenText = null;
            }
        }
        for (int k = 0; k < npcs_in_region; k++) {
            int l = local_npcs[k];
            Npc npc = npcs[l];
            if (npc != null && npc.message_cycle > 0) {
                npc.message_cycle--;
                if (npc.message_cycle == 0)
                    npc.spokenText = null;
            }
        }
    }

    private static final int MENU_BORDER_OUTER_2010 = 0x6D6A5B;
    private static final int MENU_BORDER_INNER_2010 = 0x524A3D;
    private static final int MENU_PADDING_2010 = 0x2B2622;
    private static final int MENU_BACKGROUND_2010 = 0x2B271C;
    private static final int MENU_TEXT_2010 = 0xC6B895;
    private static final int MENU_HEADER_GRADIENT_TOP_2010 = 0x322E22;
    private static final int MENU_HEADER_GRADIENT_BOTTOM_2010 = 0x090A04;
    private static final int ORIGINAL_BG = 0x5D5447;

    @Override
    public void draw2010Menu(int alpha) {
        int x = getMenuX();
        int y = getMenuY();
        int w = getMenuWidth();
        int h = getMenuHeight();

        // Outside border
        rasterizerDrawHorizontalLineAlpha(x + 2, y, w - 4, MENU_BORDER_OUTER_2010, alpha);
        rasterizerDrawHorizontalLineAlpha(x + 2, y + h - 1, w - 4, MENU_BORDER_OUTER_2010, alpha);
        rasterizerDrawVerticalLineAlpha(x, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);
        rasterizerDrawVerticalLineAlpha(x + w - 1, y + 2, h - 4, MENU_BORDER_OUTER_2010, alpha);

        // Padding
        rasterizerDrawRectangleAlpha(x + 1, y + 5, w - 2, h - 6, MENU_PADDING_2010, alpha);
        rasterizerDrawHorizontalLineAlpha(x + 1, y + 17, w - 2, MENU_PADDING_2010, alpha);
        rasterizerDrawCircleAlpha(x + 2, y + h - 3, 0, MENU_PADDING_2010, alpha);
        rasterizerDrawCircleAlpha(x + w - 3, y + h - 3, 0, MENU_PADDING_2010, alpha);

        // Header
        rasterizerDrawGradientAlpha(x + 2, y + 1, w - 4, 16, MENU_HEADER_GRADIENT_TOP_2010,
                MENU_HEADER_GRADIENT_BOTTOM_2010, alpha, alpha);
        rasterizerFillRectangleAlpha(x + 1, y + 1, 2, 4, MENU_PADDING_2010, alpha);
        rasterizerFillRectangleAlpha(x + w - 3, y + 1, 2, 4, MENU_PADDING_2010, alpha);

        // Inside border
        rasterizerDrawHorizontalLineAlpha(x + 2, y + 18, w - 4, MENU_BORDER_INNER_2010, alpha);
        rasterizerDrawHorizontalLineAlpha(x + 3, y + h - 3, w - 6, MENU_BORDER_INNER_2010, alpha);
        rasterizerDrawVerticalLineAlpha(x + 2, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);
        rasterizerDrawVerticalLineAlpha(x + w - 3, y + 18, h - 21, MENU_BORDER_INNER_2010, alpha);

        // Options background
        rasterizerFillRectangleAlpha(x + 3, y + 19, w - 6, h - 22, MENU_BACKGROUND_2010, alpha);

        // Corner insets
        rasterizerDrawCircleAlpha(x + 1, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
        rasterizerDrawCircleAlpha(x + w - 2, y + 1, 0, MENU_BORDER_OUTER_2010, alpha);
        rasterizerDrawCircleAlpha(x + 1, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);
        rasterizerDrawCircleAlpha(x + w - 2, y + h - 2, 0, MENU_BORDER_OUTER_2010, alpha);

        boldFont.draw("Choose Option", x + 3, y + 14, MENU_TEXT_2010, -1);

        int mouseX = getMouseX();
        int mouseY = getMouseY();

        int count = getMenuOptionCount();

        for (int i = 0; i < count; i++) {
            int rowY = y + (count - 1 - i) * 15 + 31;

            boldFont.draw(menuActionText[i].toString(), x + 3, rowY, MENU_TEXT_2010, -1);

            // Highlight current option
            if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3) {
                rasterizerFillRectangleAlpha(x + 3, rowY - 12, w - 6, 15, 0xffffff, 80);
            }
        }
    }

    public void addFriend(String added) {
        try {
            if (added == null)
                return;
            if (friendsCount >= 100 && member != 1) {
                sendMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            /// YOU HAVE TO KEEP THIS LOGIC OTHERWISE CLIENT WILL DESYNC
            if (friendsCount >= 200) {
                sendMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }

            for (int i = 0; i < friendsCount; i++)
                if (friendsList[i].equalsIgnoreCase(added)) {
                    sendMessage(added + " is already on your friend list", 0, "");
                    return;
                }
            for (int j = 0; j < ignoreCount; j++)
                if (ignoreList[j].equalsIgnoreCase(added)) {
                    sendMessage("Please remove " + added + " from your ignore list first", 0, "");
                    return;
                }
            if (!added.equalsIgnoreCase(local_player.username)) {
                friendsList[friendsCount] = added;
                friendsListAsLongs[friendsCount] = StringUtils.encodeBase37(added);
                friendsNodeIDs[friendsCount] = 0;
                friendsCount++;
                packetSender.sendFriendAddition(added);
            }
            return;
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("15283, " + (byte) 68 + ", " + added + ", " + runtimeexception);
        }
        throw new RuntimeException();
    }

    public int get_tile_pos(int z, int y, int x) {
        int var3 = x >> 7;
        int var4 = y >> 7;
        if (var3 >= 0 && var4 >= 0 && var3 <= 103 && var4 <= 103) {
            int var5 = z;
            if (z < 3 && (tileFlags[1][var3][var4] & 2) == 2) {
                var5 = z + 1;
            }

            int var6 = x & 127;
            int var7 = y & 127;
            int var8 = (128 - var6) * tileHeights[var5][var3][var4] + tileHeights[var5][var3 + 1][var4] * var6 >> 7;
            int var9 = var6 * tileHeights[var5][var3 + 1][var4 + 1]
                    + tileHeights[var5][var3][var4 + 1] * (128 - var6) >> 7;
            return var8 * (128 - var7) + var9 * var7 >> 7;
        } else {
            return 0;
        }
    }

    private static String set_k_or_m(int value) {// can set <col tags infront of the value
        if (value < 0x186a0)
            return String.valueOf(value);

        if (value < 0x989680)
            return value / 1000 + "K";
        else
            return value / 0xf4240 + "M";

    }

    private void logout() {
        parallelWidgetList.clear();
        logoutTime = System.currentTimeMillis();
        expectedHit.clear();
        frameMode(false);
        try {
            if (SERVER_SOCKET != null)
                SERVER_SOCKET.close();
            SERVER_SOCKET = null;
        } catch (Exception _ex) {
            _ex.printStackTrace();
            addReportToServer(_ex.getMessage());
        }
        ArchiveDiskActionHandler.waitForPendingArchiveDiskActions();
        firstLoginMessage = secondLoginMessage = "";
        effects_list.clear();
        scene.reset_interactive_obj();
        Rasterizer2D.Rasterizer2D_clear();
        StaticSound.logout();
        smallFont.drawCenteredString("", 0, 0, 0, 0);
        setInteractingWithEntityId(0);
        SERVER_SOCKET = null;
        loggedIn = false;
        setGameState(GameState.LOGIN_SCREEN);
        loginStage = 0;
        if (fadingScreen != null) {
            fadingScreen.stop();
        }
        release();
        scene.clear();
        for (int i = 0; i < 4; i++) {
            collisionMaps[i].init();
        }
        Arrays.fill(chatMessages, null);
        stopMidi();
        interactingWithEntityId = -1;
        renderself = true;
        clearTextClicked();
        frameValueW = 765;
        frameValueH = 503;
        this.staminaActive = 0;
        frameMode(false);
        resetInputFieldFocus();
        setting.save();
        resetSplitPrivateChatMessages();
        System.gc();
        setGameState(GameState.LOGIN_SCREEN);
    }

    private void resetCharacterCreation() {
        updateCharacterCreation = true;
        for (int j = 0; j < 7; j++) {
            characterClothing[j] = -1;
            for (int k = 0; k < Js5List.getConfigSize(Js5ConfigType.IDENTKIT); k++) {
                if (IdentityKit.lookup(k).validStyle
                        || IdentityKit.lookup(k).bodyPartId != j + (characterGender ? 0 : 7))
                    continue;
                characterClothing[j] = k;
                break;
            }
        }
    }

    private void updateNPCMovement(int i, Buffer stream) {
        while (stream.bitPosition + 21 < i * 8) {
            int k = stream.readBits(16);
            if (k == 65535) {
                break;
            }
            if (npcs[k] == null)
                npcs[k] = new Npc();
            Npc npc = npcs[k];
            local_npcs[npcs_in_region++] = k;
            npc.last_update_tick = tick;
            int z = stream.readBits(5);
            if (z > 15)
                z -= 32;
            int x = stream.readBits(5);
            if (x > 15)
                x -= 32;
            int teleport = stream.readBits(1);
            npc.definition = NpcDefinition.get(stream.readBits(ClientConstants.NPC_BITS));
            int updateRequired = stream.readBits(1);
            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = k;
            }
            npc.size = npc.definition.size;
            npc.rotation = npc.definition.getRotationSpeed();
            if (npc.rotation == 0) {
                npc.current_rotation = 0;
            }
            npc.walkSequence = npc.definition.getWalkingAnimation();
            npc.walkBackSequence = npc.definition.getRotate180Animation();
            npc.walkLeftSequence = npc.definition.getRotate90LeftAnimation();
            npc.walkRightSequence = npc.definition.getRotate90RightAnimation();
            npc.idleSequence = npc.definition.getStandingAnimation();
            npc.setPos(local_player.pathX[0] + x, local_player.pathY[0] + z, teleport == 1);

            int updateFacing = stream.readBits(1);
            if (updateFacing == 1) {
                int faceX = stream.readBits(14);
                int faceY = stream.readBits(14);
                npc.faceX = faceX;
                npc.faceY = faceY;
            }

            callbacks.post(new NpcSpawned(npc));
        }
        stream.disableBitAccess();
    }

    static long processGameTime, xpro;

    protected static final void frameDrawHistory() {
        clock.mark();

        int var0;
        for (var0 = 0; var0 < 32; ++var0) {
            GameEngine.graphicsTickTimes[var0] = 0L;
        }

        for (var0 = 0; var0 < 32; ++var0) {
            GameEngine.clientTickTimes[var0] = 0L;
        }

        gameCyclesToDo = 0;
    }

    public static int mouseWheelRotation;

    public void doCycle() {
        tick++;
        getCallbacks().tick();

        Js5System.doCycleJs5();
        ArchiveDiskActionHandler.processArchiveDiskActions();
        StaticSound.pulse();
        keyManager.prepareForNextCycle();
        keyHandler.processKeyEvents();
        if (mouseWheel != null) {
            mouseWheelRotation = mouseWheel.useRotation();
        }
        if (gameState == 0) {
            load();
            frameDrawHistory();
        } else if (gameState == 5) {
            load();
            frameDrawHistory();
        } else if (gameState != 10 && gameState != 11) {
            if (gameState == 20) {
                doCycleLoggedOut();
            } else if (gameState == 50) {
                doCycleLoggedOut();
            } else if (gameState == 25) {
                loadRegion();
            }
        } else {
            doCycleLoggedOut();
        }

        if (gameState == 30) {
            doCycleLoggedIn();
        } else if (gameState == 40 || gameState == 45) {
            doCycleLoggedOut();
        }

        if (debug_packet_info && readpkts > 0) {
            addReportToServer(Client.xpro++ + " processGame took " + (System.currentTimeMillis() - processGameTime));
        }
        processGameTime = System.currentTimeMillis();
        readpkts = 0;
    }

    private boolean promptUserForInput(Widget widget) {
        int contentType = widget.contentType;
        if (friendServerStatus == 2) {
            if (contentType == 201) {
                update_chat_producer = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                interfaceInputAction = 1;
                inputMessage = "Enter name of friend to add to list";
            }
            if (contentType == 202) {
                update_chat_producer = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                interfaceInputAction = 2;
                inputMessage = "Enter name of friend to delete from list";
            }
        }
        if (contentType == 205) {
            logoutTimer = 250;
            return true;
        }
        if (contentType == 501) {
            update_chat_producer = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            interfaceInputAction = 4;
            inputMessage = "Enter name of player to add to list";
        }
        if (contentType == 502) {
            update_chat_producer = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            interfaceInputAction = 5;
            inputMessage = "Enter name of player to delete from list";
        }
        if (contentType == 550) {
            update_chat_producer = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            interfaceInputAction = 6;
            inputMessage = "Enter the name of the chat you wish to join";
        }

        if (contentType >= 300 && contentType <= 313) {
            int k = (contentType - 300) / 2;
            int j1 = contentType & 1;
            int i2 = characterClothing[k];
            if (i2 != -1) {
                do {
                    if (j1 == 0 && --i2 < 0)
                        i2 = Js5List.getConfigSize(Js5ConfigType.IDENTKIT) - 1;
                    if (j1 == 1 && ++i2 >= Js5List.getConfigSize(Js5ConfigType.IDENTKIT))
                        i2 = 0;
                } while (IdentityKit.lookup(i2).validStyle
                        || IdentityKit.lookup(i2).bodyPartId != k + (characterGender ? 0 : 7));
                characterClothing[k] = i2;
                updateCharacterCreation = true;
            }
        }
        if (contentType >= 314 && contentType <= 323) {
            int l = (contentType - 314) / 2;
            int k1 = contentType & 1;
            int j2 = characterDesignColours[l];
            if (k1 == 0 && --j2 < 0)
                j2 = APPEARANCE_COLORS[l].length - 1;
            if (k1 == 1 && ++j2 >= APPEARANCE_COLORS[l].length)
                j2 = 0;
            characterDesignColours[l] = j2;
            updateCharacterCreation = true;
        }
        if (contentType == 324 && !characterGender) {
            characterGender = true;
            resetCharacterCreation();
        }
        if (contentType == 325 && characterGender) {
            characterGender = false;
            resetCharacterCreation();
        }
        if (contentType == 326) {
            // appearance change
            packetSender.sendAppearanceChange(characterGender, characterClothing, characterDesignColours);
            return true;
        }

        if (contentType == 613) {
            canMute = !canMute;
        }

        if (contentType >= 601 && contentType <= 612) {
            clearTopInterfaces();
            if (reportAbuseInput.length() > 0) {
                /*
                 * outgoing.writeOpcode(ClientToServerPackets.REPORT_PLAYER);
                 * outgoing.writeLong(StringUtils.encodeBase37(reportAbuseInput));
                 * outgoing.writeByte(contentType - 601); outgoing.writeByte(canMute ? 1 : 0);
                 */
            }
        }
        return false;
    }

    private void parsePlayerSynchronizationMask(Buffer stream) {
        for (int count = 0; count < mobsAwaitingUpdateCount; count++) {
            int index = mobsAwaitingUpdate[count];
            Player player = players[index];

            int mask = stream.readUnsignedByte();

            if ((mask & 0x40) != 0) {
                mask += stream.readUnsignedByte() << 8;
            }

            appendPlayerUpdateMask(mask, index, stream, player);
        }
    }

    final void drawMapScenes(int arg0, int arg1, int arg2, int arg3, int arg4) {
        long var6 = scene.getBoundaryObjectTag(arg0, arg1, arg2);
        int var8;
        int var9;
        int var10;
        int var11;
        int var13;
        int var14;
        if (0L != var6) {
            var8 = scene.getObjectFlags(arg0, arg1, arg2, var6);
            var9 = var8 >> 6 & 3;
            var10 = var8 & 31;
            var11 = arg3;
            if (ViewportMouse.method5519(var6)) {
                var11 = arg4;
            }

            int[] var12 = minimapImage.pixels;
            var13 = 24624 + arg1 * 4 + 2048 * (103 - arg2);
            var14 = ViewportMouse.Entity_unpackID(var6);
            ObjectDefinition var15 = ObjectDefinition.get(var14);
            if (var15.mapSceneId != -1) {
                IndexedImage var16 = mapSceneSprites[var15.mapSceneId];
                if (var16 != null) {
                    int var17 = (var15.sizeX * 4 - var16.width) / 2;
                    int var18 = (var15.sizeY * 4 - var16.height) / 2;
                    var16.draw(48 + arg1 * 4 + var17, var18 + 48 + 4 * (104 - arg2 - var15.sizeY));
                }
            } else {
                if (0 == var10 || 2 == var10) {
                    if (var9 == 0) {
                        var12[var13] = var11;
                        var12[var13 + 512] = var11;
                        var12[1024 + var13] = var11;
                        var12[var13 + 1536] = var11;
                    } else if (var9 == 1) {
                        var12[var13] = var11;
                        var12[1 + var13] = var11;
                        var12[2 + var13] = var11;
                        var12[3 + var13] = var11;
                    } else if (var9 == 2) {
                        var12[3 + var13] = var11;
                        var12[var13 + 3 + 512] = var11;
                        var12[var13 + 3 + 1024] = var11;
                        var12[1536 + var13 + 3] = var11;
                    } else if (3 == var9) {
                        var12[1536 + var13] = var11;
                        var12[1 + var13 + 1536] = var11;
                        var12[2 + 1536 + var13] = var11;
                        var12[1536 + var13 + 3] = var11;
                    }
                }

                if (3 == var10) {
                    if (var9 == 0) {
                        var12[var13] = var11;
                    } else if (1 == var9) {
                        var12[var13 + 3] = var11;
                    } else if (2 == var9) {
                        var12[3 + var13 + 1536] = var11;
                    } else if (3 == var9) {
                        var12[1536 + var13] = var11;
                    }
                }

                if (var10 == 2) {
                    if (3 == var9) {
                        var12[var13] = var11;
                        var12[512 + var13] = var11;
                        var12[1024 + var13] = var11;
                        var12[1536 + var13] = var11;
                    } else if (var9 == 0) {
                        var12[var13] = var11;
                        var12[var13 + 1] = var11;
                        var12[var13 + 2] = var11;
                        var12[3 + var13] = var11;
                    } else if (1 == var9) {
                        var12[var13 + 3] = var11;
                        var12[512 + 3 + var13] = var11;
                        var12[3 + var13 + 1024] = var11;
                        var12[3 + var13 + 1536] = var11;
                    } else if (2 == var9) {
                        var12[var13 + 1536] = var11;
                        var12[1 + 1536 + var13] = var11;
                        var12[2 + 1536 + var13] = var11;
                        var12[3 + var13 + 1536] = var11;
                    }
                }
            }
        }

        var6 = scene.getGameObjectTag(arg0, arg1, arg2);
        if (0L != var6) {
            var8 = scene.getObjectFlags(arg0, arg1, arg2, var6);
            var9 = var8 >> 6 & 3;
            var10 = var8 & 31;
            var11 = ViewportMouse.Entity_unpackID(var6);
            ObjectDefinition var25 = ObjectDefinition.get(var11);
            int var20;
            if (var25.mapSceneId != -1) {
                IndexedImage var19 = mapSceneSprites[var25.mapSceneId];
                if (var19 != null) {
                    var14 = (var25.sizeX * 4 - var19.width) / 2;
                    var20 = (var25.sizeY * 4 - var19.height) / 2;
                    var19.draw(48 + arg1 * 4 + var14, var20 + (104 - arg2 - var25.sizeY) * 4 + 48);
                }
            } else if (9 == var10) {
                var13 = 15658734;
                if (ViewportMouse.method5519(var6)) {
                    var13 = 15597568;
                }

                int[] var21 = minimapImage.pixels;
                var20 = 2048 * (103 - arg2) + arg1 * 4 + 24624;
                if (0 != var9 && 2 != var9) {
                    var21[var20] = var13;
                    var21[1 + var20 + 512] = var13;
                    var21[1024 + var20 + 2] = var13;
                    var21[3 + 1536 + var20] = var13;
                } else {
                    var21[1536 + var20] = var13;
                    var21[1 + var20 + 1024] = var13;
                    var21[512 + var20 + 2] = var13;
                    var21[var20 + 3] = var13;
                }
            }
        }

        var6 = scene.getFloorDecorationTag(arg0, arg1, arg2);
        if (0L != var6) {
            var8 = ViewportMouse.Entity_unpackID(var6);
            ObjectDefinition var22 = ObjectDefinition.get(var8);
            if (var22.mapSceneId != -1) {
                IndexedImage var23 = mapSceneSprites[var22.mapSceneId];
                if (var23 != null) {
                    var11 = (var22.sizeX * 4 - var23.width) / 2;
                    int var24 = (var22.sizeY * 4 - var23.height) / 2;
                    var23.draw(arg1 * 4 + 48 + var11, var24 + (104 - arg2 - var22.sizeY) * 4 + 48);
                }
            }
        }
    }

    private void loadTitleScreen() {
        usernameHover = SpriteCache.get(1852);
        passwordHover = SpriteCache.get(1852);
        loginHover = SpriteCache.get(1854);
        draw_loadup(10, "Loading...");
        /*
         * titleBoxIndexedImage = new IndexedImage(titleArchive, "titlebox", 0);
         * titleButtonIndexedImage = new IndexedImage(titleArchive, "titlebutton", 0);
         *
         * titleIndexedImages = new IndexedImage[12]; int icon = 0; try { icon =
         * Integer.parseInt(getParameter("fl_icon")); } catch (Exception ex) {
         *
         * } if (icon == 0) { for (int index = 0; index < 12; index++) {
         * titleIndexedImages[index] = new IndexedImage(titleArchive, "runes", index); }
         *
         * } else { for (int index = 0; index < 12; index++) { titleIndexedImages[index]
         * = new IndexedImage(titleArchive, "runes", 12 + (index & 3)); }
         *
         * } flameLeftSprite = new Sprite(128, 265); flameRightSprite = new Sprite(128,
         * 265);
         *
         * System.arraycopy(flameLeftBackground.canvasRaster, 0,
         * flameLeftSprite.myPixels, 0, 33920);
         *
         * System.arraycopy(flameRightBackground.canvasRaster, 0,
         * flameRightSprite.myPixels, 0, 33920);
         *
         * anIntArray851 = new int[256];
         *
         * for (int k1 = 0; k1 < 64; k1++) anIntArray851[k1] = k1 * 0x40000;
         *
         * for (int l1 = 0; l1 < 64; l1++) anIntArray851[l1 + 64] = 0xff0000 + 1024 *
         * l1;
         *
         * for (int i2 = 0; i2 < 64; i2++) anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;
         *
         * for (int j2 = 0; j2 < 64; j2++) anIntArray851[j2 + 192] = 0xffffff;
         *
         * anIntArray852 = new int[256]; for (int k2 = 0; k2 < 64; k2++)
         * anIntArray852[k2] = k2 * 1024;
         *
         * for (int l2 = 0; l2 < 64; l2++) anIntArray852[l2 + 64] = 65280 + 4 * l2;
         *
         * for (int i3 = 0; i3 < 64; i3++) anIntArray852[i3 + 128] = 65535 + 0x40000 *
         * i3;
         *
         * for (int j3 = 0; j3 < 64; j3++) anIntArray852[j3 + 192] = 0xffffff;
         *
         * anIntArray853 = new int[256]; for (int k3 = 0; k3 < 64; k3++)
         * anIntArray853[k3] = k3 * 4;
         *
         * for (int l3 = 0; l3 < 64; l3++) anIntArray853[l3 + 64] = 255 + 0x40000 * l3;
         *
         * for (int i4 = 0; i4 < 64; i4++) anIntArray853[i4 + 128] = 0xff00ff + 1024 *
         * i4;
         *
         * for (int j4 = 0; j4 < 64; j4++) anIntArray853[j4 + 192] = 0xffffff;
         *
         * anIntArray850 = new int[256]; anIntArray1190 = new int[32768]; anIntArray1191
         * = new int[32768]; randomizeBackground(null); anIntArray828 = new int[32768];
         * anIntArray829 = new int[32768]; drawLoadingText(10,
         * "Connecting to fileserver"); if (!aBoolean831) { drawFlames = true;
         * aBoolean831 = true; startRunnable(this, 2); }
         */
    }

    private static void setHighMem() {
        SceneGraph.low_detail = false;
        low_detail = false;
        Region.low_detail = false;
        ObjectDefinition.isLowDetail = false;
    }

    // Let's not lazy initialize the singleton, we should initialize it inline to
    // guarantee thread safety.
    public static Client instance;

    public static void main(String[] args) {
        try {
            nodeID = 10;
            setHighMem();
            isMembers = true;
            // instance.createClientFrame(canvasWidth, canvasHeight);
            osName = System.getProperty("os.name");
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
    }

    private void render_projectiles() {
        for (Projectile projectile = (Projectile) projectiles
                .first(); projectile != null; projectile = (Projectile) projectiles.next())
            if (projectile.plane != plane || tick > projectile.cycleEnd)
                projectile.remove();
            else if (tick >= projectile.cycleStart) {
                if (projectile.targetIndex > 0) {
                    Npc npc = npcs[projectile.targetIndex - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0
                            && npc.y < 13312)
                        projectile.setDestination(npc.x, npc.y,
                                get_tile_pos(projectile.plane, npc.y, npc.x) - projectile.endHeight, tick);
                }
                if (projectile.targetIndex < 0) {
                    int index = -projectile.targetIndex;
                    Player player;
                    if (index == localPlayerIndex)
                        player = local_player;
                    else
                        player = players[index];
                    if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0
                            && player.y < 13312)
                        projectile.setDestination(player.x, player.y,
                                get_tile_pos(projectile.plane, player.y, player.x) - projectile.endHeight, tick);
                }
                projectile.travel(animation_step);

                scene.add_entity(plane, projectile.yaw, (int) projectile.z, -1, (int) projectile.y,
                        60, (int) projectile.x, projectile, false);
            }

    }

    /*
     * public AppletContext getAppletContext() { if (SignLink.mainapp != null)
     * return SignLink.mainapp.getAppletContext(); //else // return
     * super.getAppletContext(); }
     */

    private void drawLogo() {

    }

    public void toImage(SimpleImage image, String name) {
        File directory = new File(SignLink.findCacheDir() + "rsimg/dump1/");
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (image == null) {
            // System.out.println("Image was null :/");
            return;
        }
        BufferedImage bi = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);
        Image img = makeColorTransparent(bi, new Color(0, 0, 0));
        BufferedImage trans = imageToBufferedImage(img);
        try {
            File out = new File(SignLink.findCacheDir() + "rsimg/dump1/" + name + ".png");
            ImageIO.write(trans, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
    }

    /**
     * Turns an Image into a BufferedImage.
     *
     * @param image
     * @return
     */
    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * Makes the specified color transparent in a buffered image.
     *
     * @param im
     * @param color
     * @return
     */
    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        RGBImageFilter filter = new RGBImageFilter() {
            public final int markerRGB = color.getRGB() | 0xFF000000;

            public int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public Widget interesecting() {
        if (openWalkableInterface < 1) {
            return null;
        }
        Widget rsi = Widget.cache[openWalkableInterface];
        if (rsi.childToIntersect == 0) {
            // System.out.println("Is 0 so continue.");
            return null;
        }

        Widget ri = Widget.cache[rsi.childToIntersect];

        if (mouseInRegion(ri.positionX, ri.positionY, ri.positionX + (ri.width),
                ri.positionY + (ri.height))) {
            // System.out.println("Intersecting");
            return ri;
        }

        return null;

    }

    private void calcFlamesPosition() {
        char c = '\u0100';
        for (int j = 10; j < 117; j++) {
            int k = (int) (Math.random() * 100D);
            if (k < 50)
                anIntArray828[j + (c - 2 << 7)] = 255;
        }
        for (int l = 0; l < 100; l++) {
            int i1 = (int) (Math.random() * 124D) + 2;
            int k1 = (int) (Math.random() * 128D) + 128;
            int k2 = i1 + (k1 << 7);
            anIntArray828[k2] = 192;
        }

        for (int j1 = 1; j1 < c - 1; j1++) {
            for (int l1 = 1; l1 < 127; l1++) {
                int l2 = l1 + (j1 << 7);
                anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128]
                        + anIntArray828[l2 + 128]) / 4;
            }

        }

        anInt1275 += 128;
        if (anInt1275 > anIntArray1190.length) {
            anInt1275 -= anIntArray1190.length;
            int i2 = (int) (Math.random() * 12D);
            randomizeBackground(titleIndexedImages[i2]);
        }
        for (int j2 = 1; j2 < c - 1; j2++) {
            for (int i3 = 1; i3 < 127; i3++) {
                int k3 = i3 + (j2 << 7);
                int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
                if (i4 < 0)
                    i4 = 0;
                anIntArray828[k3] = i4;
            }

        }

        System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

        anIntArray969[c - 1] = (int) (Math.sin((double) tick / 14D) * 16D
                + Math.sin((double) tick / 15D) * 14D + Math.sin((double) tick / 16D) * 12D);
        if (anInt1040 > 0)
            anInt1040 -= 4;
        if (anInt1041 > 0)
            anInt1041 -= 4;
        if (anInt1040 == 0 && anInt1041 == 0) {
            int l3 = (int) (Math.random() * 2000D);
            if (l3 == 0)
                anInt1040 = 1024;
            if (l3 == 1)
                anInt1041 = 1024;
        }
    }

    private final int TEXT_CHILD_OFFSET = 15;

    private void resetAnimation(int i) {
        Widget class9 = Widget.cache[i];
        if (class9 == null || class9.children == null) {
            return;
        }
        for (int j = 0; j < class9.children.length; j++) {
            if (class9.children[j] == -1)
                break;
            Widget class9_1 = Widget.cache[class9.children[j]];
            if (class9_1.type == 1)
                resetAnimation(class9_1.id);
            class9_1.currentFrame = 0;
            class9_1.lastFrameTime = 0;
        }
    }

    private void drawHeadIcon() {
        if (hintIconDrawType != 2)
            return;
        get_scene_pos((hintIconX - next_region_start << 7) + hintIconLocationArrowRelX, hintIconLocationArrowHeight * 2,
                (hintIconY - next_region_end << 7) + hintIconLocationArrowRelY);
        if (scene_draw_x > -1 && tick % 20 < 10) {
            headIconsHint[0].drawSprite(scene_draw_x - 12, scene_draw_y - 28);
        }
    }

    public void addCancelMenuEntry() {
        menuActionRow = 0;
        isMenuOpen = false;
        menuActionText[0] = "Cancel";
        menuActionText[0] = "";
        menuActionTypes[0] = 1006;
        // menuShiftClick[0] = false;
        menuActionRow = 1;
    }

    static long lastPackets;
    static long currentPacketTime;
    static int readpkts = 0;

    private void doCycleLoggedIn() { // mainGameProcessor

        boolean isFixed = !resized;

        if (rebootTimer > 1) {
            rebootTimer--;
        }

        if (logoutTimer > 0) {
            logoutTimer--;
        }

        long packetsReadTime = System.currentTimeMillis();

        int packetsPerTick = 100;
        for (int j = 0; j < packetsPerTick; j++) {
            if (!readPacket()) {
                break;
            }
            readpkts++;
        }

        if (debug_packet_info) {
            long lastRead = System.currentTimeMillis() - lastPackets;
            long itTook = System.currentTimeMillis() - packetsReadTime;
            addReportToServer(
                    "read " + readpkts + " packets last read was " + lastRead + " ms ago. it took " + itTook + " ms ");
        }

        currentPacketTime = System.currentTimeMillis();

        if (currentPacketTime - packetsReadTime > 20 && loggedIn
                && loggedInWatch.hasElapsed(10_000, TimeUnit.MILLISECONDS)) {
            addReportToServer("It took longer than 20 ms to read packets");
        }
        lastPackets = System.currentTimeMillis();

        if (!loggedIn) {
            return;
        }

        if (MouseHandler.keypressedEventIndex != 0) {
            long delta = (MouseHandler.lastPressed - lastKeyboardEventTime) / 50L;
            if (delta > 16777215L) {
                delta = 16777215L;
            }
            lastKeyboardEventTime = MouseHandler.lastPressed;
            int k2 = MouseHandler.saveClickY;
            if (k2 < 0)
                k2 = 0;
            else if (k2 > 502)
                k2 = 502;
            int k3 = MouseHandler.saveClickX;
            if (k3 < 0)
                k3 = 0;
            else if (k3 > 764)
                k3 = 764;
            int k4 = k2 * 765 + k3;
            int j5 = 0;
            if (MouseHandler.keypressedEventIndex == 2)
                j5 = 1;
            int l5 = (int) delta;
            /*
             * outgoing.writeOpcode(ClientToServerPackets.MOUSE_CLICK);
             * outgoing.writeInt((l5 << 20) + (j5 << 19) + k4);
             */
        }

        if (super.canvas.hasFocus() && !hadFocus) {
            hadFocus = true;
        }

        if (!super.canvas.hasFocus() && hadFocus) {
            hadFocus = false;
        }

        if (gameState != 30) {
            return;
        }

        processPendingSpawns();

        timeoutCounter++;

        if (timeoutCounter > 750) {
            addReportToServer("Connection timed out at counter " + timeoutCounter + " ("
                    + (int) ((timeoutCounter / 30) * 0.6) + " secs), dropping client");
            try {
                regularFont.drawCenteredString("Connection lost.", 119, 18, 0xffffff, true);
                addReportToServer("Dropping client, not a normal logout.");
                processNetworkError();
            } catch (Exception e) {
                addReportToServer("There was an error dropping the client:");
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
        }

        updatePlayerInstances();
        forceNPCUpdateBlock();

        processMobChatText();
        processLagReports();
        StaticSound.method4532();
        animation_step++;
        getCallbacks().post(ClientTick.INSTANCE);
        if (crossType != 0) {
            crossIndex += 20;
            if (crossIndex >= 400)
                crossType = 0;
        }
        if (atInventoryInterfaceType != 0) {
            item_container_cycle++;
            if (item_container_cycle >= 15) {
                if (atInventoryInterfaceType == 2) {
                    update_tab_producer = true;
                }
                if (atInventoryInterfaceType == 3)
                    update_chat_producer = true;

                atInventoryInterfaceType = 0;
            }
            // System.out.println("" + atInventoryInterfaceType);
        }
        if (activeInterfaceType != 0) {
            draggingCycles++;
            if (MouseHandler.mouseX > mouseDragX + 5 || MouseHandler.mouseX < mouseDragX - 5
                    || MouseHandler.mouseY > mouseDragY + 5
                    || MouseHandler.mouseY < mouseDragY - 5)
                aBoolean1242 = true;// on an item bounds?

            if (MouseHandler.instance.clickMode2 == 0) {
                if (activeInterfaceType == 2) {
                    update_tab_producer = true;
                }
                if (activeInterfaceType == 3) {
                    update_chat_producer = true;
                }
                activeInterfaceType = 0;

                if (aBoolean1242 && draggingCycles >= setting.drag_item_value) {// dragging start?
                    lastActiveInvInterface = -1;
                    processRightClick();
                    if (focusedDragWidget == 5382) {
                        Point southWest, northEast;

                        if (isFixed) {
                            southWest = new Point(56, 81);
                            northEast = new Point(101, 41);
                        } else {
                            int xOffset = (canvasWidth - 237 - Widget.cache[5292].width) / 2;
                            int yOffset = 36 + ((canvasHeight - 503) / 2);
                            southWest = new Point(xOffset + 76, yOffset + 62);
                            northEast = new Point(xOffset + 117, yOffset + 22);
                        }

                        int[] slots = new int[10];

                        for (int i = 0; i < slots.length; i++) {
                            slots[i] = (40 * i) + southWest.getX();
                        }

                        for (int i = 0; i < slots.length; i++) {
                            if ((MouseHandler.mouseX >= slots[i]) && (MouseHandler.mouseX <= (slots[i] + 41))
                                    && (MouseHandler.mouseY >= northEast.getY())
                                    && (MouseHandler.mouseY <= southWest.getY())) {
                                packetSender.sendItemContainerSlotSwap(focusedDragWidget, 2, dragFromSlot, i);
                                return;
                            }
                        }

                        slots = null;
                    }

                    if (lastActiveInvInterface == focusedDragWidget && mouseInvInterfaceIndex != dragFromSlot) {
                        Widget widget = Widget.cache[focusedDragWidget];
                        int j1 = 0;
                        if (settings[304] == 1 && widget.contentType == 206) {
                            j1 = 1;
                        }
                        if (widget.inventoryItemId[mouseInvInterfaceIndex] <= 0)
                            j1 = 0;
                        if (widget.replaceItems) {
                            int l2 = dragFromSlot;
                            int l3 = mouseInvInterfaceIndex;
                            widget.inventoryItemId[l3] = widget.inventoryItemId[l2];
                            widget.inventoryAmounts[l3] = widget.inventoryAmounts[l2];
                            widget.inventoryItemId[l2] = -1;
                            widget.inventoryAmounts[l2] = 0;
                        } else if (j1 == 1) {
                            int fromTab = 0;
                            int toTab = 0;
                            if (widget.contentType == 206) {
                                for (int tab = 0, totalSlots = 0; tab < 10; tab++) {
                                    if (dragFromSlot <= totalSlots + tabAmounts[tab] - 1
                                            && dragFromSlot >= totalSlots) {
                                        fromTab = tab;
                                    }
                                    if (mouseInvInterfaceIndex <= totalSlots + tabAmounts[tab] - 1
                                            && mouseInvInterfaceIndex >= totalSlots) {
                                        toTab = tab;
                                    }
                                    totalSlots += tabAmounts[tab];
                                }
                            }
                            if (fromTab == toTab || widget.contentType != 206) {
                                int i3 = dragFromSlot;
                                for (int i4 = mouseInvInterfaceIndex; i3 != i4;)
                                    if (i3 > i4) {
                                        widget.swapInventoryItems(i3, i3 - 1);
                                        i3--;
                                    } else if (i3 < i4) {
                                        widget.swapInventoryItems(i3, i3 + 1);
                                        i3++;
                                    }
                            }
                        } else if (j1 == 0) {
                            widget.swapInventoryItems(dragFromSlot, mouseInvInterfaceIndex);
                        }
                        packetSender.sendItemContainerSlotSwap(focusedDragWidget, j1, dragFromSlot,
                                mouseInvInterfaceIndex);
                    }
                } else if ((useOneMouseButton == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2)
                    determineMenuSize(MouseHandler.saveClickX, MouseHandler.saveClickY);
                else if (menuActionRow > 0)
                    processMenuActions(menuActionRow - 1);
                item_container_cycle = 10;
                MouseHandler.keypressedEventIndex = 0;
            }
        }

        if (SceneGraph.clickedTileX != -1) {
            int k = SceneGraph.clickedTileX;
            int k1 = SceneGraph.clickedTileY;
            if ((myPrivilege >= 2) && isShiftPressed && ClientConstants.SHIFT_CLICK_TELEPORT) {
                teleport(SceneGraph.clickedTileX + next_region_start, SceneGraph.clickedTileY + next_region_end, plane);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 1;
                crossIndex = 0;
                SceneGraph.clickedTileX = -1;
            } else {
                boolean flag = walk(0, 0, 0, 0, local_player.pathY[0], 0, 0, k1, local_player.pathX[0], true, k);
                SceneGraph.clickedTileX = -1;
                if (flag) {
                    crossX = MouseHandler.saveClickX;
                    crossY = MouseHandler.saveClickY;
                    crossType = 1;
                    crossIndex = 0;
                }
            }
        }
        if (SceneGraph.tracedMarkTileX != -1) {
            scene.markTile(SceneGraph.tracedMarkTileX, SceneGraph.tracedMarkTileY, plane);
            SceneGraph.tracedMarkTileX = -1;
            SceneGraph.tracedMarkTileY = -1;
        }
        if (MouseHandler.keypressedEventIndex == 1 && clickToContinueString != null) {
            clickToContinueString = null;
            update_chat_producer = true;
            MouseHandler.keypressedEventIndex = 0;
        }
        processMenuClick();
        if (MouseHandler.instance.clickMode2 == 1 || MouseHandler.keypressedEventIndex == 1)
            anInt1213++;
        if (chatTooltipSupportId != 0 || tabTooltipSupportId != 0 || gameTooltipSupportId != 0) {
            if (anInt1501 < tooltipDelay) {
                anInt1501++;
                if (anInt1501 == tooltipDelay) {
                    if (chatTooltipSupportId != 0) {
                        update_chat_producer = true;
                    }
                    if (tabTooltipSupportId != 0) {
                        update_tab_producer = true;
                    }
                }
            }
        } else if (anInt1501 > 0) {
            anInt1501--;
        }
        if (gameState == GameState.LOGGED_IN.getState()) {
            Camera.updateCamera();
            if (oculusOrbState != 1 || keyManager.lastTypedCharacter <= 0) {
                manageTextInputs();
            }
        }

        ++MouseHandler.idleCycles;
        ++KeyHandler.idleCycles;

        if (MouseHandler.idleCycles++ > 15000 && KeyHandler.idleCycles > 15000) {
            logoutTimer = 250;
            MouseHandler.idleCycles = 14500;
            packetSender.sendPlayerInactive();
        }
        // TODO overhead text cycles here
        if (pingPacketCounter++ > 65) {
            packetSender.sendEmptyPacket();
        }

        try {
            if (SERVER_SOCKET != null && packetSender.getBuffer().pos > 0) {
                SERVER_SOCKET.queueBytes(packetSender.getBuffer().pos, packetSender.getBuffer().payload);
                packetSender.getBuffer().resetPosition();
                pingPacketCounter = 0;
            }
        } catch (IOException _ex) {
            try {
                addReportToServer("Dropping client, not a normal logout. 2");
                processNetworkError();
            } catch (Exception e) {
                addReportToServer("There was an error dropping the client: dropClient()");
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
            _ex.printStackTrace();
            addReportToServer(_ex.getMessage());
        } catch (Exception exception) {
            logout();
            addReportToServer("There was an error sending logout():");
            exception.printStackTrace();
            addReportToServer(exception.getMessage());
        }
    }

    private void processLagReports() {
        if (reports.isEmpty()) {
            return;
        }
        for (int i = 0; i < reports.size(); i++) {
            if (reports.isEmpty())
                break;
            String text = reports.pop();
            if (text == null)
                return;
            packetSender.sendClientReport(text);
        }
        reports.clear();
    }

    private void clearObjectSpawnRequests() {
        SpawnedObject spawnedObject = (SpawnedObject) spawns.reverseGetFirst();
        for (; spawnedObject != null; spawnedObject = (SpawnedObject) spawns.reverseGetNext())
            if (spawnedObject.getLongetivity == -1) {
                spawnedObject.delay = 0;
                handleTemporaryObjects(spawnedObject);
            } else {
                spawnedObject.remove();
            }

    }

    public void draw_loadup(int percent, String string) {
        loadingPercent = percent;
        loadingText = string;
    }

    private int getPixelAmt(int current, int pixels) {
        return (int) (pixels * .01 * current);
    }

    private void handleScroll(int childWidth, int childHeight, int xPos, int cursor_y, Widget child, int to,
            int scrollMax) {

        int anInt992;
        if (aBoolean972)
            anInt992 = 32;
        else
            anInt992 = 0;
        aBoolean972 = false;
        if (xPos >= childWidth && xPos < childWidth + 16 && cursor_y >= to && cursor_y < to + 16) {
            child.scrollPosition -= anInt1213 * 4;
        } else if (xPos >= childWidth && xPos < childWidth + 16 && cursor_y >= (to + childHeight) - 16
                && cursor_y < to + childHeight) {
            child.scrollPosition += anInt1213 * 4;
        } else if (xPos >= childWidth - anInt992 && xPos < childWidth + 16 + anInt992 && cursor_y >= to + 16
                && cursor_y < (to + childHeight) - 16 && anInt1213 > 0) {
            int l1 = ((childHeight - 32) * childHeight) / scrollMax;
            if (l1 < 8)
                l1 = 8;
            int i2 = cursor_y - to - 16 - l1 / 2;
            int j2 = childHeight - 32 - l1;
            if (j2 != 0) {
                child.scrollPosition = ((scrollMax - childHeight) * i2) / j2;
            }
            aBoolean972 = true;
        }
    }

    private boolean clickObject(long object, int y, int x) {
        int objectFlag = scene.getObjectFlags(plane, x, y, object);
        int object_type = ObjectKeyUtil.getObjectType(objectFlag);
        int orientation = ObjectKeyUtil.getObjectOrientation(objectFlag);
        if (object_type == 10 || object_type == 11 || object_type == 22) {
            ObjectDefinition class46 = ObjectDefinition.get(ObjectKeyUtil.getObjectId(object));// check
            int w;
            int h;
            if (orientation == 0 || orientation == 2) {
                w = class46.sizeX;
                h = class46.sizeY;
            } else {
                w = class46.sizeY;
                h = class46.sizeX;
            }
            int k2 = class46.orientation;
            if (orientation != 0)
                k2 = (k2 << orientation & 0xf) + (k2 >> 4 - orientation);
            walk(2, 0, h, 0, local_player.pathY[0], w, k2, y, local_player.pathX[0], false, x);
        } else {
            walk(2, orientation, 0, orientation + 1, local_player.pathY[0], 0, 0, y, local_player.pathX[0], false, x);
        }
        crossX = MouseHandler.saveClickX;
        crossY = MouseHandler.saveClickY;
        crossType = 2;
        crossIndex = 0;
        return true;
    }

    private void processNetworkError() {
        setGameState(GameState.CONNECTION_LOST);
        addReportToServer("Client dropped");
        regularFont.drawCenteredString("Please wait - attempting to reestablish.", 116, 34, 0xffffff, true);
        packetSender.sendDisconnectByPacket(true);
        if (logoutTimer > 0) {
            try {
                logout();
            } catch (Exception e) {
                addReportToServer("There was an error resetting logout: ");
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
            return;
        }
        Rasterizer2D.draw_rect_outline(2, 2, 229, 39, 0xffffff); // white box around
        Rasterizer2D.draw_filled_rect(3, 3, 227, 37, 0); // black fill
        minimapState = 2;
        travel_destination_x = 0;
        BufferedConnection rsSocket = SERVER_SOCKET;
        loggedIn = false;
        loginFailures = 0;
        login(myUsername, myPassword, true);
        if (!loggedIn)
            logout();
        try {
            rsSocket.close();
        } catch (Exception _ex) {
            _ex.printStackTrace();
            addReportToServer(_ex.getMessage());
        }
    }

    public void set_camera_north() {
        boolean oldToggle = unlockedFps;
        setUnlockedFps(false);
        cameraX = 0;
        cameraY = 0;
        cameraRotation = 0;
        camAngleY = 0;
        minimapZoom = 0;
        map_rotation = 0;
        setUnlockedFps(oldToggle);
    }

    public void launchURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, url);
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime()
                            .exec(new String[] { "which", browsers[count] })
                            .waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[] { browser, url });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
    }

    // Menu actions
    final int USE_SPELL = 626;

    private void processMenuActions(int id) {
        if (id < 0) {
            return;
        }

        if (inputDialogState != 0) {
            inputDialogState = 0;
            update_chat_producer = true;
        }

        int first_menu_action = firstMenuAction[id];
        int second_menu_action = secondMenuAction[id];
        int action = menuActionTypes[id];
        long local_player_index = selectedMenuActions[id];
        // System.out.println("menu action " + first_menu_action + ", " +
        // second_menu_action + ", " + action + ", " + local_player_index);

        if (action == 72000) {
            // Option menu actionId.
            packetSender.writeOptionMenuPacket(second_menu_action, first_menu_action, (int) local_player_index);
        }

        // 317 BELOW
        if (action >= 2000) {
            action -= 2000;
        }

        if (action == 291) {
            packetSender.withdrawAllButOneAction(first_menu_action, second_menu_action, (int) local_player_index);
        }

        if (action == 300) {
            packetSender.withdrawModifiableX(first_menu_action, second_menu_action, (int) local_player_index,
                    modifiableXValue);
        }

        // World map orb
        if (action == 850) {
            packetSender.sendButtonClick(156);
            return;
        }

        if (action == 851) { // Spec orb
            packetSender.sendButtonClick(155);
            return;
        }

        resetsidebars_teleportinterface();
        for (int i = 0; i < NewTeleportInterface.CATEGORY_NAMES.length; i++) {

            if (second_menu_action == 88005 + i) {
                Widget.cache[88005 + i].enabledSprite = SpriteCache.get(2065);
                Widget.cache[88005 + i].disabledSprite = SpriteCache.get(2065);
            }
        }

        if (second_menu_action >= 29055 && second_menu_action <= 29061) {
            if (second_menu_action == 29055) {//
                TeleportWidget.handleTeleportTab(0);
            }
            if (second_menu_action == 29056) {//
                TeleportWidget.handleTeleportTab(1);
            }
            if (second_menu_action == 29057 || second_menu_action == 30106 || second_menu_action == 13061
                    || second_menu_action == 1174) {// PK tps
                TeleportWidget.handleTeleportTab(2);
            }

            if (second_menu_action == 29058 || second_menu_action == 30075 || second_menu_action == 13045
                    || second_menu_action == 1167) {// Pvm tps
                TeleportWidget.handleTeleportTab(3);
            }

            if (second_menu_action == 29059 || second_menu_action == 30083 || second_menu_action == 13053
                    || second_menu_action == 1170) {// Boss tps
                TeleportWidget.handleTeleportTab(4);
            }

            if (second_menu_action == 29060) {
                TeleportWidget.handleTeleportTab(5);
            }

            if (second_menu_action == 29061) {
                TeleportWidget.handleTeleportTab(6);
            }
        }

        // System.err.println(Widget.cache[y]);

        // System.out.println("Action: " + y);
        if (action == 1895) {
            // System.out.println("lol hi");
            resetSplitPrivateChatMessages();
        }

        if (action == 3000) {
            // System.out.println("L was: " + action);
            // System.out.println("Link was: " + broadcast.getLink());
            Utils.launchURL(broadcast.getLink());
        }

        if (action == 3555) {
            // System.out.println("L was here: " + action);
            // System.out.println("Link was: " + broadcast.getLink());
            Utils.launchURL(broadcast.getLink());
        }
        if (action == 3001) {
            // System.out.println("Dismissed");
            broadcast.dismiss();
        }

        // click logout tab
        if (action == 700) {
            if (tabInterfaceIDs[10] != -1) {
                if (sidebarId == 10) {
                    showTabComponents = !showTabComponents;
                } else {
                    showTabComponents = true;
                }
                sidebarId = 10;
                update_tab_producer = true;
            }
        }

        if (action == 769) {
            Widget d = Widget.cache[second_menu_action];
            Widget p = Widget.cache[(int) local_player_index];
            if (!d.dropdown.isOpen()) {
                if (p.dropdownOpen != null) {
                    p.dropdownOpen.dropdown.setOpen(false);
                }
                p.dropdownOpen = d;
            } else {
                p.dropdownOpen = null;
            }
            d.dropdown.setOpen(!d.dropdown.isOpen());
        } else if (action == 770) {
            Widget d = Widget.cache[second_menu_action];
            Widget p = Widget.cache[(int) local_player_index];
            if (first_menu_action >= d.dropdown.getOptions().length)
                return;
            d.dropdown.setSelected(d.dropdown.getOptions()[first_menu_action]);
            d.dropdown.setOpen(false);
            d.dropdown.getDrop().selectOption(first_menu_action, d);
            p.dropdownOpen = null;
        }

        // reset compass to north
        if (action == 696) {
            set_camera_north();
        }
        // System.out.println("Clicked button " + action);
        // button clicks
        switch (action) {
            case 1500:
            case 1501:
            case 1506:
            case 1507:
            case 1510:
            case 1511:
            case 1512:
            case 1315:
            case 1316:
            case 1317:
            case 1318:
            case 1319:
            case 1320:
            case 1321:
            case 879:
            case 475:
            case 476:
            case 1050:
            case 268:
            case 258:
                // button click
                packetSender.sendButtonClick(action);
                break;
        }

        // click autocast
        if (action == 104) {
            Widget widget = Widget.cache[second_menu_action];
            packetSender.sendButtonClick(widget.id);
            /*
             * spellId = widget.id; if (!autocast) { autocast = true; autoCastId =
             * widget.id; sendPacket(new ClickButton(widget.id)); } else if (autoCastId ==
             * widget.id) { autocast = false; autoCastId = 0; sendPacket(new
             * ClickButton(widget.id)); } else if (autoCastId != widget.id) { autocast =
             * true; autoCastId = widget.id; sendPacket(new ClickButton(widget.id)); }
             */
        }

        // item on npc
        if (action == 582) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                packetSender.sendUseItemOnNPC(useItem, (int) local_player_index, selectedItemIdSlot,
                        interfaceitemSelectionTypeIn);
            }
        }

        // picking up ground item
        if (action == 234) {
            boolean flag1 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag1)
                flag1 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // pickup ground item
            packetSender.sendPickupItem(second_menu_action + next_region_end, (int) local_player_index,
                    first_menu_action + next_region_start);
        }

        // using item on object
        if (action == 62 && clickObject(local_player_index, second_menu_action, first_menu_action)) {
            packetSender.sendUseItemOnObject(interfaceitemSelectionTypeIn,
                    ObjectKeyUtil.getObjectId(local_player_index), second_menu_action + next_region_end,
                    selectedItemIdSlot, first_menu_action + next_region_start, useItem);
        }

        // using item on ground item
        if (action == 511) {
            boolean flag2 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag2)
                flag2 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // item on ground item
            packetSender.sendUseItemOnGroundItem(interfaceitemSelectionTypeIn, useItem, (int) local_player_index,
                    second_menu_action + next_region_end,
                    selectedItemIdSlot, first_menu_action + next_region_start);
        }

        // item option 1
        if (action == 74) {
            packetSender.sendItemOption1(second_menu_action, (int) local_player_index, first_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id) {
                atInventoryInterfaceType = 1;
            }
            if (Widget.cache[second_menu_action].parent == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }

        if (first_menu_action == 23004) {
            Client.update_chat_producer = false;
            this.messagePromptRaised = false;
            this.promptInput = "";
        }

        if (action == 315) {
            int buttonId = second_menu_action;
            Widget widget = Widget.cache[buttonId];
            // log.info("click {}", buttonId);

            switch (second_menu_action) {
                /*
                 * case tset: // exmaple, not used
                 * if (widget.clickable) {
                 * Widget widget2 = Widget.cache[test];
                 * if (widget2 != null)
                 * parallelWidgetList.add(widget2);
                 * }
                 * break;
                 */
            }
            boolean flag8 = true;
            if (widget.type == Widget.TYPE_CONFIG || widget.id == 26101 || widget.id == 26102 || widget.id == 73155) { // Placeholder
                                                                                                                       // (or
                widget.active = !widget.active;
            } else if (widget.type == Widget.TYPE_CONFIG_HOVER) {
                Widget.handleConfigHover(widget);
            } else if (widget.type == Widget.TYPE_CONFIG_BUTTON_HOVERED_SPRITE_OUTLINE) {
                Widget.handleConfigSpriteHover(widget);
            }

            if (widget.id == 74003) {
                Widget.cache[74000].children[13] = 74150;
            }

            // bank search chat
            if (widget.id == 26102) {
                if (widget.active) {
                    searchingBank = true;
                    update_chat_producer = true;
                    inputDialogState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    interfaceInputAction = 1;
                    inputMessage = "Enter an item to search for";
                } else {
                    Widget.cache[26102].active = false;
                    searchingBank = false;
                    update_chat_producer = true;
                    inputDialogState = 0;
                    messagePromptRaised = false;
                    promptInput = "";
                    interfaceInputAction = 1;
                    inputMessage = "";
                }
            }

            // System.out.println("Button: "+button);
            if (widget.contentType > 0) {
                flag8 = promptUserForInput(widget);
            }

            if (setting.click(this, buttonId)) {
                return;
            }

            if (setting.settingButtons(buttonId)) {
                return;
            }

            if (flag8) {
                OptionTabWidget.optionTabButtons(buttonId);
                OSRSQuestTabWidget.settings(second_menu_action);// quest tab switching

                /** Faster spec bars toggle **/
                // Handle radio buttons
                switch (buttonId) {

                    case 12697: // Drag Setting.
                        enter_amount_title = "Please enter your desired Drag Setting <col=A10081>(5 is OSRS):";
                        enter_amount_title2 = "This setting goes hand in hand with switching, choose wisely and test!";
                        messagePromptRaised = false;
                        inputDialogState = 3;
                        amountOrNameInput = "";
                        update_chat_producer = true;
                        break;

                    case Keybinding.RESTORE_DEFAULT:
                        Keybinding.restoreDefault();
                        Keybinding.updateInterface();
                        sendMessage("Default keys loaded.", 0, "");
                        setting.save();
                        break;
                    case 29138:
                    case 29038:
                    case 29063:
                    case 29113:
                    case 29163:
                    case 29188:
                    case 29213:
                    case 29238:
                    case 30007:
                    case 48023:
                    case 33033:
                    case 30108:
                    case 7473:
                    case 7562:
                    case 7487:
                    case 7788:
                    case 8481:
                    case 7612:
                    case 7587:
                    case 7662:
                    case 7462:
                    case 7548:
                    case 7687:
                    case 7537:
                    case 7623:
                    case 12322:
                    case 7637:
                    case 12311:
                    case 155:
                        packetSender.sendSpecialAttackToggle(buttonId);
                        break;
                    default:
                        if (Widget.radioButtons.contains(buttonId)) {
                            checkRadioOptions(buttonId);
                        }
                        if (Widget.cache[buttonId].clickable && Widget.cache[buttonId].serverCheck) {
                            packetSender.sendConfirm(2, 0);
                            widgetId = buttonId;
                            Widget.cache[buttonId].disabledSprite = SpriteCache
                                    .get(Widget.cache[buttonId].clickSprite2); // change this to add a variable stored
                                                                               // in
                            // clickable buttons that can store the
                            // sprite ids
                            Widget.cache[buttonId].enabledSprite = SpriteCache.get(Widget.cache[buttonId].clickSprite2);
                        }
                        if (Widget.cache[buttonId].clickable && !Widget.cache[buttonId].serverCheck) {
                            if (Widget.cache[buttonId].disabledSprite == SpriteCache
                                    .get(Widget.cache[buttonId].clickSprite1)) {
                                Widget.cache[buttonId].disabledSprite = SpriteCache
                                        .get(Widget.cache[buttonId].clickSprite2);
                                Widget.cache[buttonId].enabledSprite = SpriteCache
                                        .get(Widget.cache[buttonId].clickSprite2);
                                break;
                            }
                            if (Widget.cache[buttonId].disabledSprite == SpriteCache
                                    .get(Widget.cache[buttonId].clickSprite1)) {
                                Widget.cache[buttonId].disabledSprite = SpriteCache
                                        .get(Widget.cache[buttonId].clickSprite2);
                                Widget.cache[buttonId].enabledSprite = SpriteCache
                                        .get(Widget.cache[buttonId].clickSprite2);
                                break;
                            }
                        }
                        packetSender.sendButtonClick(buttonId);
                }
            }
        }

        // player option
        if (action == 561) {
            Player player = players[(int) local_player_index];
            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1188 += (int) local_player_index;
                if (anInt1188 >= 90) {
                    // (anti-cheat)
                    // outgoing.writeOpcode(136);
                    anInt1188 = 0;
                }
                packetSender.sendPlayerOption1((int) local_player_index);
            }
        }

        // npc option 1
        if (action == 20) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // npc action 1
                // System.out.println("npc option 1");
                packetSender.sendNPCOption1((int) local_player_index);
                if (this.isInputFieldInFocus()) {
                    this.resetInputFieldFocus();
                    inputString = "";
                }
            }
        }

        // player option 2
        if (action == 779) {// attack player
            Player player = players[(int) local_player_index];
            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // player option 2
                packetSender.sendAttackPlayer((int) local_player_index);
            }
        }

        if (action == 517) {
            int x = second_menu_action - 4;
            int y = first_menu_action - 4;
            scene.requestMarkTile(x, y);
            System.out.println("x " + x + "y " + y);
        }

        // clicking tiles hello\
        if (action == 519) {
            // System.out.println("Clicked here.");
            if (!isMenuOpen) {
                // System.out.println("Clicked at: " + (MouseHandler.saveClickY - 4) + " | " +
                // (MouseHandler.saveClickX - 4));
                scene.register_click(MouseHandler.saveClickY - 4, MouseHandler.saveClickX - 4, Client.instance.plane);
            } else {
                scene.register_click(second_menu_action - 4, first_menu_action - 4, Client.instance.plane);
                // System.out.println("Clicked at else: " + (MouseHandler.saveClickY - 4) + " |
                // " + (slot - 4));
            }
        }

        // object option 4
        if (action == 1062) {
            clickObject(local_player_index, second_menu_action, first_menu_action);
            // object option 4
            packetSender.sendObjectOption4(first_menu_action + next_region_start,
                    ObjectKeyUtil.getObjectId(local_player_index), second_menu_action + next_region_end);
        }

        // continue dialogue
        if (action == 679 && !continuedDialogue) {
            packetSender.sendNextDialogue(second_menu_action);
            continuedDialogue = true;
        }

        // Pressed button, this used to be 647 but is now 648 for presets text and 649
        // for spawning text because we add choice to the action in buildInterfaceMenu.
        if (action == 647 || action == 648) {
            // Key bindings?
            if (widget_overlay_id == 53000) {
                for (int i = 0; i < 14; i++) {
                    if (second_menu_action == 53048 + (i * 3)) {
                        int key = KeyEvent.VK_F1 + first_menu_action;
                        if (key > KeyEvent.VK_F12) {
                            key = KeyEvent.VK_ESCAPE;
                        }
                        Keybinding.bind(i, key);
                        return;
                    }
                }
            }
            packetSender.sendButtonAction(second_menu_action, first_menu_action);
        }

        // using bank all option of the bank interface
        if (action == 431) {
            packetSender.sendItemContainerOption4(first_menu_action, second_menu_action, (int) local_player_index);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id) {
                atInventoryInterfaceType = 1;
            }
            if (Widget.cache[second_menu_action].parent == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 449) {
            String url = selectedMsg.substring(18, selectedMsg.lastIndexOf(">"));
            Utils.launchURL(url);
        }
        if (action == 337 || action == 42 || action == 792 || action == 322 || action == 338) {
            String string = menuActionText[id];
            int indexOf = string.indexOf(">");
            if (indexOf != -1) {
                String addedName = string.substring(indexOf + 1);
                // System.out.println("Username string is: " + string);
                if (!StringUtils.VALID_NAME.matcher(addedName).matches())
                    return;
                addedName = StringUtils.capitalizeIf(addedName);
                // long usernameHash =
                // StringUtils.encodeBase37(string.substring(indexOf).trim());
                if (action == 337)
                    addFriend(addedName);
                if (action == 42)
                    addIgnore(addedName);
                if (action == 792)
                    removeFriend(addedName);
                if (action == 322)
                    removeIgnore(addedName);
                if (action == 338)
                    openPrivateChatMessageInput(addedName);
            }
        }
        // using the bank x option on the bank interface
        if (action == 53) {
            packetSender.sendItemContainerOption5(second_menu_action, first_menu_action, (int) local_player_index);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        if (action == 539) {
            packetSender.sendItemOption3((int) local_player_index, first_menu_action, second_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id) {
                atInventoryInterfaceType = 1;
            }
            if (Widget.cache[second_menu_action].parent == backDialogueId) {
                atInventoryInterfaceType = 3;
            }
        }
        if (action == 484 || action == 6 || action == 525 || action == 526) {
            try {
                String string = menuActionText[id];
                int indexOf = string.indexOf(">");
                if (indexOf != -1) {
                    // System.out.println("Username string is actually: " + string);
                    if (string.contains("Accept trade") || string.contains("Accept challenge")
                            || string.contains("Accept gamble") || string.contains("Accept invite")) {
                        string = string.substring(indexOf + 1).trim();
                    } else {
                        string = string.substring(indexOf + 5).trim();
                    }
                    String username = StringUtils
                            .formatText(StringUtils.decodeBase37(StringUtils.encodeBase37(string)));
                    boolean flag9 = false;
                    for (int count = 0; count < players_in_region; count++) {
                        Player player = players[local_players[count]];
                        if (player == null || player.username == null || !player.username.equalsIgnoreCase(username)) {
                            continue;
                        }
                        walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0],
                                local_player.pathX[0], false, player.pathX[0]);

                        // accepting trade
                        if (action == 484) {
                            packetSender.sendTradePlayer(local_players[count]);
                        }

                        // accepting gamble
                        if (action == 525) {
                            packetSender.sendGambleRequest(local_players[count]);
                        }

                        // accepting group invite
                        if (action == 526) {
                            packetSender.sendGroupInvite(local_players[count]);
                        }

                        // accepting a challenge
                        if (action == 6) {
                            anInt1188 += (int) local_player_index;
                            if (anInt1188 >= 90) {
                                // (anti-cheat)
                                // outgoing.writeOpcode(136);
                                anInt1188 = 0;
                            }
                            packetSender.sendChatboxDuel(local_players[count]);
                        }
                        flag9 = true;
                        break;
                    }

                    if (!flag9)
                        sendMessage("Unable to find " + username, 0, "");
                }
            } catch (Exception e) {
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
        }

        // Using an item on another item
        if (action == 870) {
            packetSender.sendUseItemOnItem(first_menu_action, selectedItemIdSlot, (int) local_player_index,
                    interfaceitemSelectionTypeIn, useItem, second_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Using the drop option of an item
        if (action == 847) {
            packetSender.sendDropItem((int) local_player_index, second_menu_action, first_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }
        // useable spells
        if (action == USE_SPELL) {
            Widget widget = Widget.cache[second_menu_action];
            widget_highlighted = 1;
            spellId = widget.id;
            anInt1137 = second_menu_action;
            selectedTargetMask = widget.selectedTargetMask;
            item_highlighted = 0;
            String tooltip = widget.spellName;
            if (tooltip.contains("<br>")) {
                tooltip = tooltip.replaceAll("<br>", " ");
            }
            if (widget.selectedActionName.toLowerCase().contains("cast on")) {
                widget.selectedActionName = widget.selectedActionName.replaceAll(" on", "");
            }
            selected_target_id = widget.selectedActionName + " <col=65280>" + tooltip + "</col> -> ";
            if (selectedTargetMask == 16) {
                sidebarId = 3;
                update_tab_producer = true;
            }
            return;
        }

        // Using the bank 5 option on a bank widget
        if (action == 78) {
            packetSender.sendItemContainerOption2(second_menu_action, (int) local_player_index, first_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // player option 2
        if (action == 27) {
            Player player = players[(int) local_player_index];
            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt986 += (int) local_player_index;
                if (anInt986 >= 54) {
                    // (anti-cheat)
                    // outgoing.writeOpcode(189);
                    // outgoing.writeByte(234);
                    anInt986 = 0;
                }
                packetSender.sendFollowPlayer((int) local_player_index);
            }
        }

        // Used for lighting logs
        if (action == 213) {
            boolean flag3 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag3)
                flag3 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // light item
            /*
             * outgoing.writeOpcode(79); outgoing.writeLEShort(button + regionBaseY);
             * outgoing.writeShort(clicked); outgoing.writeShortA(first + regionBaseX);
             */
        }

        // Using the unequip option on the equipment tab interface
        if (action == 632) {
            packetSender.sendItemContainerOption1(second_menu_action, first_menu_action, (int) local_player_index);
            if (widget_overlay_id == 52000) {
                tradeModified(second_menu_action, first_menu_action);
            }
            // System.out.println(button + " " + first + " " + clicked);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
            if (searchingShops) {
                Widget.cache[73155].active = false;
                searchingShops = false;
                update_chat_producer = true;
                inputDialogState = 0;
                messagePromptRaised = false;
                interfaceInputAction = 1;
                inputMessage = "";
                Widget rsi = this.getInputFieldFocusOwner();
                if (rsi == null) {
                    return;
                }
                rsi.defaultText = "";
                packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id, rsi.defaultText);
                inputString = "";
                promptInput = "";
            }
            if (this.isInputFieldInFocus()) {
                this.resetInputFieldFocus();
                inputString = "";
            }
        }

        if (action == 1004) {
            if (tabInterfaceIDs[10] != -1) {
                sidebarId = 10;
                update_tab_producer = true;
            }
        }
        if (action == 1003) {
            clanChatMode = 2;
            update_chat_producer = true;
        }
        if (action == 1002) {
            clanChatMode = 1;
            update_chat_producer = true;
        }
        if (action == 1001) {
            clanChatMode = 0;
            update_chat_producer = true;
        }
        if (action == 1000) {
            cButtonCPos = 4;
            chatTypeView = 11;
            update_chat_producer = true;
        }

        if (action == 999) {
            cButtonCPos = 0;
            chatTypeView = 0;
            update_chat_producer = true;
        }
        if (action == 998) {
            cButtonCPos = 1;
            chatTypeView = 5;
            update_chat_producer = true;
        }

        // public chat "hide" option
        if (action == 997) {
            set_public_channel = 3;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // public chat "off" option
        if (action == 996) {
            set_public_channel = 2;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // public chat "friends" option
        if (action == 995) {
            set_public_channel = 1;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // public chat "on" option
        if (action == 994) {
            set_public_channel = 0;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // public chat main click
        if (action == 993) {
            cButtonCPos = 2;
            chatTypeView = 1;
            update_chat_producer = true;
        }

        // private chat "off" option
        // private chat "off" option
        if (action == 992) {
            privateChatMode = 2;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // private chat "friends" option
        if (action == 991) {
            privateChatMode = 1;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // private chat "on" option
        if (action == 990) {
            privateChatMode = 0;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // private chat main click
        if (action == 989) {
            cButtonCPos = 3;
            chatTypeView = 2;
            update_chat_producer = true;
        }

        // trade message privacy option "off" option
        if (action == 987) {
            tradeMode = 2;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // trade message privacy option "friends" option
        if (action == 986) {
            tradeMode = 1;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // trade message privacy option "on" option
        if (action == 985) {
            tradeMode = 0;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // trade message privacy option "off" option
        if (action == 987) {
            tradeMode = 2;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // trade message privacy option "friends" option
        if (action == 986) {
            tradeMode = 1;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // trade message privacy option "on" option
        if (action == 985) {
            tradeMode = 0;
            update_chat_producer = true;

            packetSender.sendChatConfigurations(set_public_channel, privateChatMode, tradeMode, clanChatMode);
        }

        // trade message privacy option main click
        if (action == 984) {
            cButtonCPos = 5;
            chatTypeView = 3;
            update_chat_producer = true;
        }

        if (action == 980) {
            cButtonCPos = 6;
            chatTypeView = 4;
            update_chat_producer = true;
        }

        // Using 3rd option of an item
        if (action == 493) {
            // item option 3
            packetSender.sendItemOption2(second_menu_action, first_menu_action, (int) local_player_index);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // clicking some sort of tile
        if (action == 652) {
            boolean flag4 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag4)
                flag4 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // unknown (non-anti bot)
            /*
             * outgoing.writeOpcode(156); outgoing.writeShortA(first + regionBaseX);
             * outgoing.writeLEShort(button + regionBaseY); outgoing.writeLEShortA(clicked);
             */
        }

        // Using a spell on a ground item
        if (action == 94) {
            boolean flag5 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag5)
                flag5 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            packetSender.sendUseMagicOnGroundItem(second_menu_action + next_region_end, (int) local_player_index,
                    first_menu_action + next_region_start, anInt1137);
        }
        System.out.println("action: " + action);

        if (action == 646) {
            // button click

            packetSender.sendButtonClick(second_menu_action);

            Widget widget = Widget.cache[second_menu_action];
            if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                int settingId = widget.valueIndexArray[0][1];
                if (settings[settingId] != widget.requiredValues[0]) {
                    if (widget.updateConfig) {
                        settings[settingId] = widget.requiredValues[0];
                        // System.out.println("setting: " + settings[id] + " value=" + id);
                        updateVarp(settingId);
                    }
                }
            }

            if (searchingShops) {
                Widget.cache[73155].active = false;
                searchingShops = false;
                update_chat_producer = true;
                inputDialogState = 0;
                messagePromptRaised = false;
                interfaceInputAction = 1;
                inputMessage = "";
                Widget rsi = this.getInputFieldFocusOwner();
                if (rsi == null) {
                    return;
                }
                rsi.defaultText = "";
                packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id, rsi.defaultText);
                inputString = "";
                promptInput = "";
            }
        }

        // Using the 2nd option of an npc
        if (action == 225) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1226 += (int) local_player_index;
                if (anInt1226 >= 85) {
                    // (anti-cheat)
                    // outgoing.writeOpcode(230);
                    // outgoing.writeByte(239);
                    anInt1226 = 0;
                }
                // System.out.println("npc option 2");
                packetSender.sendNPCOption2((int) local_player_index);
            }
        }

        // Using the 3rd option of an npc
        if (action == 965) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1134++;
                if (anInt1134 >= 96) {
                    // (anti-cheat)
                    // outgoing.writeOpcode(152);
                    // outgoing.writeByte(88);
                    anInt1134 = 0;
                }
                packetSender.sendNPCOption3((int) local_player_index);
            }
        }

        // Using a spell on an npc
        if (action == 413) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                packetSender.sendUseMagicOnNPC((int) local_player_index, anInt1137);
            }
        }

        // close open interfaces
        if (action == 200) {
            clearTopInterfaces();
        }

        // Clicking "Examine" option on an npc
        // TODO: warning by ken, why check the same ID twice?
        if (action == 1025 || action == 1025) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                NpcDefinition entityDef = npc.definition;
                if (entityDef.transforms != null)
                    entityDef = entityDef.get_configs();
                if (entityDef != null) {
                    packetSender.sendExamineNPC(entityDef.id);
                }
            }
        }

        if (action == 900) {
            clickObject(local_player_index, second_menu_action, first_menu_action);
            // object option 2
            packetSender.sendObjectOption2(ObjectKeyUtil.getObjectId(local_player_index),
                    second_menu_action + next_region_end, first_menu_action + next_region_start);
        }

        // Using the "Attack" option on a npc
        if (action == 412) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                packetSender.sendAttackNPC((int) local_player_index);
            }
        }

        // Using spells on a player
        if (action == 365) {
            Player player = players[(int) local_player_index];
            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                // spells on plr
                packetSender.sendUseMagicOnPlayer((int) local_player_index, anInt1137);
            }
        }

        if (action == 729) {
            Player player = players[(int) local_player_index];
            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                packetSender.sendTradePlayer((int) local_player_index);
            }
        }

        if (action == 577) {// trade player
            Player player = players[(int) local_player_index];
            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                packetSender.sendTradePlayer((int) local_player_index);
            }
        }

        // Using a spell on an item
        if (action == 956 && clickObject(local_player_index, second_menu_action, first_menu_action)) {
            // magic on item
            // sendPacket(new MagicOnItem(first + regionBaseX, anInt1137, button +
            // regionBaseY, clicked >> 14 & 0x7fff));
        }

        // Some walking action (packet 23)
        if (action == 567) {
            boolean flag6 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag6)
                flag6 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // anti-cheat)
            /*
             * outgoing.writeOpcode(23); outgoing.writeLEShort(button + regionBaseY);
             * outgoing.writeLEShort(clicked); outgoing.writeLEShort(first + regionBaseX);
             */
        }
        if (action == 968) {// custom place holder packet
            packetSender.sendItemContainerOption1(968, first_menu_action, (int) local_player_index);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
            if (this.isInputFieldInFocus()) {
                this.resetInputFieldFocus();
                inputString = "";
            }
        }

        // Using the bank 10 option on the bank interface
        if (action == 867) {

            if (((int) local_player_index & 3) == 0) {
                anInt1175++;
            }

            if (anInt1175 >= 59) {
                // (anti-cheat)
                // outgoing.writeOpcode(200);
                // outgoing.writeShort(25501);
                anInt1175 = 0;
            }
            packetSender.sendItemContainerOption3(second_menu_action, (int) local_player_index, first_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Using a spell on an inventory item
        if (action == 543) {
            // magic on item
            packetSender.sendUseMagicOnItem(first_menu_action, (int) local_player_index, second_menu_action, anInt1137);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        // Clicking report abuse button
        if (action == 606) {
            String s2 = menuActionText[id];
            int j2 = s2.indexOf(">");
            if (j2 != -1)
                if (widget_overlay_id == -1) {
                    clearTopInterfaces();
                    reportAbuseInput = s2.substring(j2 + 5).trim();
                    canMute = false;
                    for (int index = 0; index < Widget.cache.length; index++) {
                        if (Widget.cache[index] == null || Widget.cache[index].contentType != 600)
                            continue;
                        reportAbuseInterfaceID = widget_overlay_id = Widget.cache[index].parent;
                        break;
                    }

                } else {
                    sendMessage("Please close the interface you have open before using this.", 0, "");
                }
        }

        // Using an inventory item on a player
        if (action == 491) {
            Player player = players[(int) local_player_index];

            if (player != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, player.pathY[0], local_player.pathX[0],
                        false, player.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;
                packetSender.sendUseItemOnPlayer(interfaceitemSelectionTypeIn, (int) local_player_index, useItem,
                        selectedItemIdSlot);
            }
        }

        // reply to private message
        if (action == 639) {
            String text = menuActionText[id];

            int indexOf = text.indexOf(">");

            if (indexOf != -1) {
                indexOf++; // skip ">" at the end
                // System.out.println("Text: " + text);
                // System.out.println("IndexOf " + indexOf);
                // System.out.println("Text substr: " + text.substring(indexOf).trim());
                long usernameHash = StringUtils.encodeBase37(text.substring(indexOf)
                        .trim());
                int resultIndex = -1;
                for (int friendIndex = 0; friendIndex < friendsCount; friendIndex++) {
                    if (friendsList[friendIndex].equalsIgnoreCase(text.substring(indexOf)
                            .trim())) {
                        resultIndex = friendIndex;
                        break;
                    }
                }

                if (resultIndex != -1 && friendsNodeIDs[resultIndex] > 0) {
                    update_chat_producer = true;
                    inputDialogState = 0;
                    messagePromptRaised = true;
                    promptInput = "";
                    interfaceInputAction = 3;
                    // aLong953 = friendsListAsLongs[resultIndex];
                    selectedSocialListName = friendsList[resultIndex];
                    inputMessage = "Enter a message to send to " + selectedSocialListName;
                }
            }
        }

        // Using the equip option of an item in the inventory
        if (action == 454) {
            // equip item
            packetSender.sendEquipItem((int) local_player_index, first_menu_action, second_menu_action);
            item_container_cycle = 0;
            atInventoryInterface = second_menu_action;
            atInventoryIndex = first_menu_action;
            atInventoryInterfaceType = 2;
            if (Widget.cache[second_menu_action].parent == widget_overlay_id)
                atInventoryInterfaceType = 1;
            if (Widget.cache[second_menu_action].parent == backDialogueId)
                atInventoryInterfaceType = 3;
        }

        if (action == 474) {

            if (!counterOn) {
                final int size = ExpCounter.GAINS.size();
                if (size > 1) {
                    ExpCounter.GAINS.subList(0, size - 2).clear();
                }
            }
            counterOn = !counterOn;

            WildernessWidget.unpack(new AdvancedFont[] { smallFont, regularFont, boldFont, fancyFont });

        }

        if (action == 646) {
            switch (second_menu_action) {

                case 80001:
                case 80051:
                case 80201:
                    setSidebarInterface(2, 80000);
                    Client.instance.toggleConfig(1306, 0);
                    break;
                case 80002:
                case 80052:
                case 80202:
                    setSidebarInterface(2, 80050);
                    Client.instance.toggleConfig(1307, 1);
                    break;
                case 80003:
                case 80053:
                case 80203:
                    setSidebarInterface(2, 80200);
                    Client.instance.toggleConfig(1308, 1);
                    break;

                case 81054:
                case 81254:
                case 81804:
                case 81404:
                case 81604:
                    widget_overlay_id = 81050;
                    Client.instance.toggleConfig(1406, 0);
                    break;
                case 81055:
                case 81255:
                case 81805:
                case 81405:
                case 81605:
                    widget_overlay_id = 81250;
                    Client.instance.toggleConfig(1406, 1);
                    break;
                case 81056:
                case 81256:
                case 81806:
                case 871406:
                case 871606:
                    widget_overlay_id = 81800;
                    Client.instance.toggleConfig(1406, 2);
                    break;
                case 81057:
                case 81257:
                case 81807:
                case 81407:
                case 81607:
                    widget_overlay_id = 81400;
                    Client.instance.toggleConfig(1406, 3);
                    break;
                case 81058:
                case 81258:
                case 81808:
                case 81408:
                case 81608:
                    widget_overlay_id = 81600;
                    Client.instance.toggleConfig(1406, 4);
                    break;

                case 73001:
                case 73051:
                case 73101:
                    setSidebarInterface(9, 73000);
                    Client.instance.toggleConfig(1357, 0);
                    break;
                case 73002:
                case 73052:
                case 73102:
                    setSidebarInterface(9, 73050);
                    Client.instance.toggleConfig(1357, 1);
                    break;
                case 73003:
                case 73053:
                case 73103:
                    setSidebarInterface(9, 73100);
                    Client.instance.toggleConfig(1357, 2);
                    break;

            }
        }

        // Npc option 4
        if (action == 478) {
            Npc npc = npcs[(int) local_player_index];
            if (npc != null) {
                walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, npc.pathY[0], local_player.pathX[0], false,
                        npc.pathX[0]);
                crossX = MouseHandler.saveClickX;
                crossY = MouseHandler.saveClickY;
                crossType = 2;
                crossIndex = 0;

                if (((int) local_player_index & 3) == 0) {
                    anInt1155++;
                }

                if (anInt1155 >= 53) {
                    // outgoing.writeOpcode(85);
                    // outgoing.writeByte(66);
                    anInt1155 = 0;
                }

                packetSender.sendNPCOption4((int) local_player_index);
            }
        }

        if (action == 1107) {
            if (searchingShops) {
                Widget.cache[73155].active = false;
                searchingShops = false;
                update_chat_producer = true;
                inputDialogState = 0;
                messagePromptRaised = false;
                interfaceInputAction = 1;
                inputMessage = "";
                Widget rsi = this.getInputFieldFocusOwner();
                if (rsi == null) {
                    return;
                }
                rsi.defaultText = "";
                packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id, rsi.defaultText);
                inputString = "";
                promptInput = "";
            }
        }

        // Object option 3
        if (action == 113) {
            clickObject(local_player_index, second_menu_action, first_menu_action);
            // object option 3
            packetSender.sendObjectOption3(first_menu_action + next_region_start, second_menu_action + next_region_end,
                    ObjectKeyUtil.getObjectId(local_player_index));
        }

        // Object option 4
        if (action == 872) {
            clickObject(local_player_index, second_menu_action, first_menu_action);
            packetSender.sendObjectOption4(first_menu_action + next_region_start,
                    ObjectKeyUtil.getObjectId(local_player_index), second_menu_action + next_region_end);
        }

        // Object option 1
        if (action == 502) {
            clickObject(local_player_index, second_menu_action, first_menu_action);
            packetSender.sendObjectOption1(first_menu_action + next_region_start,
                    ObjectKeyUtil.getObjectId(local_player_index), second_menu_action + next_region_end);
        }

        if (action == 169) {
            if (isCtrlPressed && setting.moving_prayers) {
                PrayerSystem.InterfaceData grabbed = PrayerSystem.InterfaceData.searchByButton(second_menu_action);
                if (grabbed != null) {
                    prayerGrabbed = grabbed;
                    return;
                }
            }
            packetSender.sendButtonClick(second_menu_action);

            if (second_menu_action != 19158 && second_menu_action != Keybinding.ESCAPE_CONFIG) { // Run button, server
                                                                                                 // handles config
                Widget widget = Widget.cache[second_menu_action];

                if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                    int setting = widget.valueIndexArray[0][1];
                    settings[setting] = 1 - settings[setting];
                    updateVarp(setting);
                }
            }
        }
        if (action == 447) {
            item_highlighted = 1;
            selectedItemIdSlot = first_menu_action;
            interfaceitemSelectionTypeIn = second_menu_action;
            useItem = (int) local_player_index;
            selectedItemName = ItemDefinition.get((int) local_player_index).name;
            widget_highlighted = 0;
            return;
        }

        if (action == 1226) {
            int objectId = ObjectKeyUtil.getObjectId(local_player_index);
            ObjectDefinition definition = ObjectDefinition.get(objectId);
            packetSender.sendObjectExamine(definition.id);
        }

        // Click First Option Ground Item
        if (action == 244) {
            boolean flag7 = walk(2, 0, 0, 0, local_player.pathY[0], 0, 0, second_menu_action, local_player.pathX[0],
                    false,
                    first_menu_action);
            if (!flag7)
                flag7 = walk(2, 0, 1, 0, local_player.pathY[0], 1, 0, second_menu_action, local_player.pathX[0], false,
                        first_menu_action);
            crossX = MouseHandler.saveClickX;
            crossY = MouseHandler.saveClickY;
            crossType = 2;
            crossIndex = 0;
            packetSender.sendGroundItemOption1(second_menu_action + next_region_end, (int) local_player_index,
                    first_menu_action + next_region_start);
        }

        if (action == 1448 || action == 1125) {
            ItemDefinition definition = ItemDefinition.get((int) local_player_index);
            if (definition != null) {
                packetSender.sendExamineItem((int) local_player_index, second_menu_action);
            }
        }

        item_highlighted = 0;
        widget_highlighted = 0;

    }

    private void tradeModified(int container, int slot) {
        if (container != 52015) {
            return;
        }
        Widget tradeWidget = Widget.cache[52017 + slot];
        tradeSlot.add(new TradeOpacity(tradeWidget, slot, 1));
        Widget.cache[52013].drawingDisabled = false;
        packetSender.sendWidgetChange(slot);
        packetSender.sendConfirm(4, 1);
    }

    public void run() {
        super.run();
    }

    private void createMenu() {
        // System.out.println("I am walkable interface id ok: " +
        // openWalkableInterface);
        if (widget_overlay_id == 16244) {
            return;
        }

        if (item_highlighted == 0 && widget_highlighted == 0) {
            menuActionText[menuActionRow] = "Walk here";
            menuActionTypes[menuActionRow] = 519;
            firstMenuAction[menuActionRow] = MouseHandler.mouseX;
            secondMenuAction[menuActionRow] = MouseHandler.mouseY;
            menuActionRow++;

            if (!isMenuOpen) {
                callbacks.post(new MenuEntryAdded(menuActionText[menuActionRow - 1], "",
                        menuActionTypes[menuActionRow - 1], selectedMenuActions[menuActionRow - 1],
                        firstMenuAction[menuActionRow - 1], secondMenuAction[menuActionRow - 1]));
            }
        }

        // System.out.println("open interface: " + openWalkableInterface);
        // long var5 = -1L;
        long previous = -1L;

        try {
            for (int cached = 0; cached < ViewportMouse.method1116(); cached++) {
                long current = ViewportMouse.entityTags[cached];
                int x = ViewportMouse.method7552(ViewportMouse.entityTags[cached]);
                int y = ViewportMouse.method134(cached);
                long var19 = ViewportMouse.entityTags[cached];
                int opcode = (int) (var19 >>> 14 & 3L);
                int uid = ViewportMouse.Entity_unpackID(ViewportMouse.entityTags[cached]);
                if (current == previous) {
                    continue;
                }
                previous = current;
                if (opcode == 2 && scene.getObjectFlags(plane, x, y, previous) >= 0) {
                    ObjectDefinition def = ObjectDefinition.get(uid);
                    if (def.transforms != null)
                        def = def.transform();

                    if (def == null)
                        continue;
                    if (item_highlighted == 1) {
                        menuActionText[menuActionRow] = "Use " + selectedItemName + " with <col=00FFFF>" + def.name;
                        menuActionTypes[menuActionRow] = 62;
                        selectedMenuActions[menuActionRow] = previous;
                        firstMenuAction[menuActionRow] = x;
                        secondMenuAction[menuActionRow] = y;
                        menuActionRow++;
                    } else if (widget_highlighted == 1) {
                        if ((selectedTargetMask & 4) == 4) {
                            menuActionText[menuActionRow] = selected_target_id + " <col=00FFFF>" + def.name;
                            menuActionTypes[menuActionRow] = 956;
                            selectedMenuActions[menuActionRow] = previous;
                            firstMenuAction[menuActionRow] = x;
                            secondMenuAction[menuActionRow] = y;
                            menuActionRow++;
                        }
                    } else {
                        if (def.actions != null) {
                            // Get dynamic actions based on varbit state (e.g., fruit tree filtering)
                            String[] dynamicActions = def.getDynamicActions(uid);

                            for (int type = 4; type >= 0; type--)
                                if (dynamicActions != null && dynamicActions[type] != null) {
                                    String actionText = dynamicActions[type];

                                    menuActionText[menuActionRow] = actionText + " <col=00FFFF>" + def.name;

                                    if (type == 0)
                                        menuActionTypes[menuActionRow] = 502;
                                    if (type == 1)
                                        menuActionTypes[menuActionRow] = 900;
                                    if (type == 2)
                                        menuActionTypes[menuActionRow] = 113;
                                    if (type == 3)
                                        menuActionTypes[menuActionRow] = 872;
                                    if (type == 4)
                                        menuActionTypes[menuActionRow] = 1062;
                                    selectedMenuActions[menuActionRow] = previous;
                                    firstMenuAction[menuActionRow] = x;
                                    secondMenuAction[menuActionRow] = y;
                                    menuActionRow++;
                                }

                        }
                        menuActionText[menuActionRow] = (myPrivilege >= 2 && myPrivilege <= 4
                                && ClientConstants.DEBUG_MODE)
                                        ? "Examine <col=00FFFF>" + def.name + " <col=65280>(<col=FFFFFF>" + uid
                                                + "<col=65280>) (<col=FFFFFF>" + (x + next_region_start) + ","
                                                + (y + next_region_end)
                                                + "<col=65280>)"
                                        : "Examine <col=65535>" + def.name;
                        menuActionTypes[menuActionRow] = 1226;
                        selectedMenuActions[menuActionRow] = previous;
                        firstMenuAction[menuActionRow] = x;
                        secondMenuAction[menuActionRow] = y;
                        menuActionRow++;

                        callbacks.post(new MenuEntryAdded(menuActionText[menuActionRow - 1], def.name,
                                menuActionTypes[menuActionRow - 1], selectedMenuActions[menuActionRow - 1],
                                firstMenuAction[menuActionRow - 1], secondMenuAction[menuActionRow - 1]));
                    }
                }

                if (opcode == 1) {
                    Npc npc = npcs[uid];

                    try {
                        if (npc.definition.size == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
                            for (int j2 = 0; j2 < npcs_in_region; j2++) {
                                Npc npc2 = npcs[local_npcs[j2]];
                                if (npc2 != null && npc2 != npc && npc2.definition.size == 1
                                        && npc2.x == npc.x && npc2.y == npc.y) {
                                    if (npc2.showActions()) {
                                        buildAtNPCMenu(npc2.definition, local_npcs[j2], y, x);
                                    }
                                }
                            }
                            for (int l2 = 0; l2 < players_in_region; l2++) {
                                Player player = players[local_players[l2]];
                                if (player != null && player.x == npc.x && player.y == npc.y)
                                    buildAtPlayerMenu(x, local_players[l2], player, y);
                            }
                        }
                        if (npc.showActions()) {
                            buildAtNPCMenu(npc.definition, uid, y, x);
                        }
                    } catch (Exception e) {
                        log.error("error", e);
                    }
                }
                if (opcode == 0) {
                    try {
                        Player playerOnTop = players[uid];// The player ontop.
                        if ((playerOnTop.x & 0x7f) == 64 && (playerOnTop.y & 0x7f) == 64) {
                            for (int k2 = 0; k2 < npcs_in_region; k2++) {
                                Npc npc = npcs[local_npcs[k2]];
                                try {
                                    if (npc != null && npc.definition.size == 1 && npc.x == playerOnTop.x
                                            && npc.y == playerOnTop.y) {
                                        buildAtNPCMenu(npc.definition, local_npcs[k2], y, x);
                                    }
                                } catch (Exception e) {
                                    log.error("error", e);
                                }
                            }

                            for (int i3 = 0; i3 < players_in_region; i3++) {
                                Player loop = players[local_players[i3]];
                                if (loop != null && loop != playerOnTop && loop.x == playerOnTop.x
                                        && loop.y == playerOnTop.y) {
                                    buildAtPlayerMenu(x, local_players[i3], loop, y);
                                }
                            }
                        }

                        // if (uid != interactingWithEntityId) {
                        buildAtPlayerMenu(x, uid, playerOnTop, y);
                        // }
                    } catch (Exception e) {
                        log.error("error", e);
                    }
                }
                if (opcode == 3) {
                    NodeDeque groundItem = scene_items[plane][x][y];
                    if (groundItem != null) {
                        List<Item> gitemsOnTile = new ArrayList<>();
                        for (Item item = (Item) groundItem.first(); item != null; item = (Item) groundItem.next()) {
                            gitemsOnTile.add(item);
                        }
                        gitemsOnTile = gitemsOnTile.stream()
                                .sorted(Comparator.comparingInt(o -> o.getDefinition().cost))
                                .collect(Collectors.toList());
                        for (Item item : gitemsOnTile) {
                            ItemDefinition itemDef = ItemDefinition.get(item.id);
                            if (item_highlighted == 1) {
                                menuActionText[menuActionRow] = "Use " + selectedItemName + " with <col=FF9040>"
                                        + itemDef.name;
                                menuActionTypes[menuActionRow] = 511;
                                selectedMenuActions[menuActionRow] = item.id;
                                firstMenuAction[menuActionRow] = x;
                                secondMenuAction[menuActionRow] = y;
                                menuActionRow++;
                            } else if (widget_highlighted == 1) {
                                if ((selectedTargetMask & 1) == 1) {
                                    menuActionText[menuActionRow] = selected_target_id + " <col=FF9040>" + itemDef.name;
                                    menuActionTypes[menuActionRow] = 94;
                                    selectedMenuActions[menuActionRow] = item.id;
                                    firstMenuAction[menuActionRow] = x;
                                    secondMenuAction[menuActionRow] = y;
                                    menuActionRow++;
                                }
                            } else {
                                for (int j3 = 4; j3 >= 0; j3--)
                                    if (itemDef.groundActions != null && itemDef.groundActions[j3] != null) {
                                        menuActionText[menuActionRow] = itemDef.groundActions[j3] + " <col=FF9040>"
                                                + itemDef.name;
                                        if (j3 == 0)
                                            menuActionTypes[menuActionRow] = 652;
                                        if (j3 == 1)
                                            menuActionTypes[menuActionRow] = 567;
                                        if (j3 == 2)
                                            menuActionTypes[menuActionRow] = 234;
                                        if (j3 == 3)
                                            menuActionTypes[menuActionRow] = 244;
                                        if (j3 == 4)
                                            menuActionTypes[menuActionRow] = 213;
                                        selectedMenuActions[menuActionRow] = item.id;
                                        firstMenuAction[menuActionRow] = x;
                                        secondMenuAction[menuActionRow] = y;
                                        menuActionRow++;
                                    } else if (j3 == 2) {
                                        menuActionText[menuActionRow] = "Take <col=FF9040>" + itemDef.name;
                                        menuActionTypes[menuActionRow] = 234;
                                        selectedMenuActions[menuActionRow] = item.id;
                                        firstMenuAction[menuActionRow] = x;
                                        secondMenuAction[menuActionRow] = y;
                                        menuActionRow++;
                                    }
                            }
                            menuActionText[menuActionRow] = (myPrivilege >= 2 && myPrivilege <= 4
                                    && ClientConstants.DEBUG_MODE)
                                            ? "Examine <col=FF9040>" + itemDef.name + " <col=65280> (<col=FFFFFF>"
                                                    + item.id
                                                    + "<col=65280>)"
                                            : "Examine <col=FF9040>" + itemDef.name;
                            menuActionTypes[menuActionRow] = 1448;
                            selectedMenuActions[menuActionRow] = item.id;
                            firstMenuAction[menuActionRow] = x;
                            secondMenuAction[menuActionRow] = y;
                            menuActionRow++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    public boolean exitRequested = false;
    public int dropdownInversionFlag;

    float spinSpeed = 1;
    private boolean startSpin = false;
    public static JagexNetThread jagexNetThread = new JagexNetThread();
    public static Huffman huffman;

    public void clear() {
        exitRequested = true;
        SignLink.reporterror = false;
        try {
            if (SERVER_SOCKET != null) {
                SERVER_SOCKET.close();
                SERVER_SOCKET = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
        ArchiveDiskActionHandler.waitForPendingArchiveDiskActions();
        try {
            SignLink.cacheData.close();

            for (int var3 = 0; var3 < SignLink.archiveCount; ++var3) {
                SignLink.cacheIndexes[var3].close();
            }

            SignLink.cacheMasterIndex.close();
            SignLink.uid.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        SERVER_SOCKET = null;
        stopMidi();
        chatBuffer = null;
        packetSender = null;
        incoming = null;
        regions = null;
        regionLandArchives = null;
        regionMapArchives = null;
        regionLandIds = null;
        regionLocIds = null;
        tileHeights = null;
        tileFlags = null;
        scene = null;
        collisionMaps = null;
        waypoints = null;
        travel_distances = null;
        walking_queue_x = null;
        walking_queue_y = null;
        backgroundFix = null;
        mapBack = null;
        sideIcons = null;
        compass = null;
        hitMarks = null;
        headIcons = null;
        skullIcons = null;
        headIconsHint = null;
        autoBackgroundSprites = null;
        crosses = null;

        mapSceneSprites = null;
        tile_cycle_map = null;
        players = null;
        local_players = null;
        mobsAwaitingUpdate = null;
        playerSynchronizationBuffers = null;
        removedMobs = null;
        npcs = null;
        local_npcs = null;
        scene_items = null;
        spawns = null;
        projectiles = null;
        incompleteAnimables = null;
        firstMenuAction = null;
        secondMenuAction = null;
        menuActionTypes = null;
        selectedMenuActions = null;
        menuActionText = null;
        settings = null;
        minimapHintX = null;
        minimapHintY = null;
        minimapHint = null;
        minimapImage = null;
        friendsList = null;
        ignoreList = null;
        friendsListAsLongs = null;
        friendsNodeIDs = null;
        multiOverlay = null;
        nullLoader();
        ObjectDefinition.release();
        NpcDefinition.clear();
        ItemDefinition.release();
        ((TextureProvider) Rasterizer3D.clips.textureLoader).clear();
        Widget.cache = null;
        SpotAnimation.cached = null;
        jagexNetThread.closeSocketStream();
        Player.model_cache = null;
        SceneGraph.release();
        System.gc();
    }

    public boolean tradingPostOpen() {
        return widget_overlay_id == 66_000;
    }

    final List<Integer> OPTION_DIALOGUE_IDS = Arrays.asList(2459, 2469, 2480, 2492);

    public int texA, texB, texC;
    public boolean debugTextures;

    private void manageTextInputs() {
        do {
            while (true) {
                label1776: do {
                    while (true) {
                        while (keyManager.hasNextKey()) {

                            int key = keyManager.lastTypedCharacter;
                            if (key == -1 || key == 96)
                                break;
                            /**
                             * @author Suic Continue a dialogue with space bar (spacebar)
                             */
                            if (key == 32 && !OPTION_DIALOGUE_IDS.contains(backDialogueId)) { // p much assures that it
                                                                                              // only sends data
                                // when needed
                                // System.out.println("Skipped dialogue");
                                packetSender.sendNextDialogue(4899);
                                continuedDialogue = true;
                            }

                            /**
                             * Author @Suic
                             */

                            // all the checks assure that it only sends the 3rd option for three option
                            // dialogue(for example) if a 3 option dialogue is open
                            // and if a 2 option dialogue is open, and "1" is pressed it only sends a click
                            // for that, not all of em. (eg a 3 option dialogue) since they all have the
                            // first option
                            // p much what i mean is that, it only sends data to the server for the click
                            // when necessary, and only the data thats needed.
                            // 1234 1 2 3 4
                            if (key == 49) {

                                switch (backDialogueId) {
                                    case 2459:
                                        packetSender.sendButtonClick(2461);
                                        break;
                                    case 2469:
                                        packetSender.sendButtonClick(2471);
                                        break;
                                    case 2480:
                                        packetSender.sendButtonClick(2482);
                                        break;
                                    case 2492:
                                        packetSender.sendButtonClick(2494);
                                        break;
                                }

                            } else if (key == 50) {
                                switch (backDialogueId) {
                                    case 2459:
                                        packetSender.sendButtonClick(2462);
                                        break;
                                    case 2469:
                                        packetSender.sendButtonClick(2472);
                                        break;
                                    case 2480:
                                        packetSender.sendButtonClick(2483);
                                        break;
                                    case 2492:
                                        packetSender.sendButtonClick(2495);
                                        break;
                                }
                            } else if (key == 51) {
                                switch (backDialogueId) {
                                    case 2459:
                                        packetSender.sendButtonClick(2464);
                                        break;
                                    case 2469:
                                        packetSender.sendButtonClick(2473);
                                        break;
                                    case 2480:
                                        packetSender.sendButtonClick(2484);
                                        break;
                                    case 2492:
                                        packetSender.sendButtonClick(2496);
                                        break;
                                }
                            } else if (key == 52) {
                                switch (backDialogueId) {
                                    case 2480:
                                        packetSender.sendButtonClick(2485);
                                        break;
                                    case 2492:
                                        packetSender.sendButtonClick(2497);
                                        break;
                                }
                            } else if (key == 53 && backDialogueId != 4882) {
                                if (backDialogueId == 2492) {
                                    packetSender.sendButtonClick(2498);
                                }
                                continuedDialogue = true;
                            }

                            if (widget_overlay_id != -1 && widget_overlay_id == reportAbuseInterfaceID) {
                                if (key == 8 && reportAbuseInput.length() > 0)
                                    reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
                                if ((key >= 97 && key <= 122 || key >= 65 && key <= 90 || key >= 48 && key <= 57
                                        || key == 32)
                                        && reportAbuseInput.length() < 12)
                                    reportAbuseInput += (char) key;
                            } else if (messagePromptRaised) {
                                if (key >= 32 && key <= 122 && promptInput.length() < 80) {
                                    promptInput += (char) key;
                                    update_chat_producer = true;
                                }
                                if (key == 8 && promptInput.length() > 0) {
                                    promptInput = promptInput.substring(0, promptInput.length() - 1);
                                    update_chat_producer = true;
                                }
                                if (key == 9) {
                                    tabToReplyPm();
                                } else if (key == 13 || key == 10) {
                                    messagePromptRaised = false;
                                    update_chat_producer = true;
                                    privateChatUserListPtr = 0;

                                    if (searchingBank) {
                                        Widget.cache[26102].active = false;
                                        searchingBank = false;
                                        promptInput = "";
                                    } else {
                                        String enteredName = promptInput;

                                        if (interfaceInputAction != 3 && !StringUtils.VALID_NAME.matcher(enteredName)
                                                .matches())
                                            return;
                                        enteredName = StringUtils.capitalizeIf(enteredName);
                                        if (interfaceInputAction == 1) {
                                            addFriend(enteredName);
                                        }
                                        if (interfaceInputAction == 2 && friendsCount > 0) {
                                            removeFriend(enteredName);
                                        }

                                        if (interfaceInputAction == 3 && promptInput.length() > 0) {
                                            // private message
                                            packetSender.sendPrivateMessage(selectedSocialListName, promptInput);
                                            promptInput = ChatMessageCodec.processText(promptInput);
                                            privateChatUserListPtr = 0;

                                            sendMessage(enteredName, 6, selectedSocialListName);
                                            if (privateChatMode == 2) {
                                                privateChatMode = 1;
                                                // privacy option
                                                packetSender.sendChatConfigurations(set_public_channel, privateChatMode,
                                                        tradeMode, clanChatMode);
                                            }
                                        }
                                        if (interfaceInputAction == 4 && ignoreCount < 100) {
                                            addIgnore(enteredName);
                                        }
                                        if (interfaceInputAction == 5 && ignoreCount > 0) {
                                            removeIgnore(enteredName);
                                        }
                                    }
                                }
                            } else if (inputDialogState == 3 || inputDialogState == 4) { // Only Allow Numbers
                                if (key >= 48 && key <= 57 && amountOrNameInput.length() < 2) {
                                    amountOrNameInput += (char) key;
                                    update_chat_producer = true;
                                }

                                if (key == 8 && amountOrNameInput.length() > 0) {
                                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                                    update_chat_producer = true;
                                }

                                if (key == 13 || key == 10) {
                                    if (amountOrNameInput.length() > 0) {
                                        int amount = 0;

                                        try {
                                            amount = Integer.parseInt(amountOrNameInput);

                                            // overflow concious code
                                            if (amount < 0) {
                                                amount = 0;
                                            } else if (amount > Integer.MAX_VALUE) {
                                                amount = Integer.MAX_VALUE;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            addReportToServer(e.getMessage());
                                        }

                                        if (amount > 0) {
                                            packetSender.sendEnteredAmount(amount, (byte) inputDialogState);
                                        }

                                        if (inputDialogState == 3) {
                                            if (amount >= 0 && amount < 51) {
                                                setting.drag_item_value = amount;
                                                Widget.cache[12697].defaultText = "Drag setting: <col=ffffff>" + amount
                                                        + (amount == 5 ? " (OSRS)"
                                                                : amount == 10 ? " (Pre-EOC)" : " (Custom)");
                                            } else {
                                                sendMessage("Please only enter a number between 0 - 50!", 0,
                                                        amountOrNameInput);
                                            }
                                        }

                                        if (inputDialogState == 4) {
                                            if (amount >= 4 && amount < 13) {
                                                packetSender.sendEnteredAmount(amount, (byte) inputDialogState);
                                            } else {
                                                sendMessage("Please only enter a number between 4 - 12!", 0,
                                                        amountOrNameInput);
                                            }
                                        }
                                    }

                                    inputDialogState = 0;
                                    update_chat_producer = true;
                                }
                            } else if (inputDialogState == 1) {

                                if (amountOrNameInput.length() < 10) {
                                    String test = amountOrNameInput + (char) key;
                                    boolean valid = test.matches("[0-9]+[kmbKMB]");
                                    boolean noLettersBefore = test.matches("(?<![A-Za-z0-9.])[0-9.]+");
                                    if (valid || noLettersBefore) {
                                        amountOrNameInput += (char) key;
                                        update_chat_producer = true;
                                    }
                                }
                                if (key == 8 && amountOrNameInput.length() > 0) {
                                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                                    update_chat_producer = true;
                                }
                                if (key == 13 || key == 10) {
                                    if (amountOrNameInput.length() > 0) {
                                        // System.out.println("enter "+widget_overlay_id);
                                        int length = amountOrNameInput.length();
                                        char lastChar = amountOrNameInput.charAt(length - 1);

                                        if (lastChar == 'k' || lastChar == 'K') {
                                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000";
                                        } else if (lastChar == 'm' || lastChar == 'M') {
                                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000";
                                        } else if (lastChar == 'b' || lastChar == 'B') {
                                            amountOrNameInput = amountOrNameInput.substring(0, length - 1)
                                                    + "000000000";
                                        }

                                        long amount = 0;
                                        boolean insideTp = tradingPostOpen();
                                        long maxAmt = insideTp ? Long.MAX_VALUE : Integer.MAX_VALUE;

                                        try {
                                            amount = Long.parseLong(amountOrNameInput);

                                            // overflow concious code
                                            if (amount < 0) {
                                                amount = 0;
                                            } else if (amount > maxAmt) {
                                                amount = maxAmt;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            addReportToServer(e.getMessage());
                                        }

                                        if (amount > 0) {
                                            packetSender.sendEnteredAmount(insideTp ? amount : (int) amount, (byte) -1);
                                            if (widget_overlay_id == 26000) {
                                                modifiableXValue = (int) amount;
                                            }
                                        }
                                    }
                                    inputDialogState = 0;
                                    update_chat_producer = true;
                                }
                            } else if (inputDialogState == 2) {
                                // System.out.println("Hello lol");
                                int limit = 100;
                                if (key >= 32 && key <= 122 && amountOrNameInput.length() < limit) {
                                    amountOrNameInput += (char) key;
                                    update_chat_producer = true;
                                }
                                if (key == 8 && amountOrNameInput.length() > 0) {
                                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                                    update_chat_producer = true;
                                }
                                if (key == 13 || key == 10) {
                                    if (amountOrNameInput.length() > 0) {
                                        packetSender.sendEnteredSyntax(amountOrNameInput);
                                        inputDialogState = 0;
                                        update_chat_producer = true;
                                    }
                                }
                                // System.out.println("KEY: " + key + ", inputString: " + amountOrNameInput);
                            } else if (backDialogueId == -1) {
                                /*
                                 * log.info("key {}", key);
                                 * 
                                 * if (this.isInputFieldInFocus() && key == 8) { // backspace on Trading Post
                                 * Widget rsi = this.getInputFieldFocusOwner();
                                 * if (rsi != null) {
                                 * if (rsi.id == 81274 || rsi.id == 81273) {
                                 * packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id,
                                 * rsi.defaultText);
                                 * break;
                                 * }
                                 * }
                                 * }
                                 */
                                if (this.isInputFieldInFocus() && key >= 32 && key <= 122) {
                                    Widget rsi = this.getInputFieldFocusOwner();
                                    if (rsi == null) {
                                        return;
                                    }

                                    if (Widget.currentInputFieldId == 73155) {
                                        Widget.cache[73155].active = true;
                                        searchingShops = true;
                                        update_chat_producer = true;
                                        messagePromptRaised = false;
                                        inputMessage = "";
                                        inputDialogState = 0;
                                        interfaceInputAction = 1;
                                    }

                                    if (rsi.defaultText.length() < rsi.characterLimit) {
                                        if (rsi.inputRegex.length() > 0) {
                                            Pattern regex = Pattern.compile(rsi.inputRegex);
                                            Matcher match = regex.matcher(Character.toString(((char) key)));
                                            if (match.matches()) {
                                                rsi.defaultText += (char) key;
                                                update_chat_producer = true;
                                            }
                                        } else {
                                            rsi.defaultText += (char) key;
                                            update_chat_producer = true;
                                        }
                                    }
                                    if (rsi.updatesEveryInput && rsi.defaultText.length() > 0 && key != 10
                                            && key != 13) {
                                        if (Utils.hasInvalidChars(rsi.defaultText)) {
                                            sendMessage("You cannot use invalid characters.", 0, "");
                                            rsi.defaultText = "";
                                            return;
                                        }
                                        packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id,
                                                rsi.defaultText);
                                        inputString = "";
                                        promptInput = "";
                                        break;
                                    } else if ((key == 10 || key == 13) && !rsi.updatesEveryInput) {
                                        if (Utils.hasInvalidChars(rsi.defaultText)) {
                                            sendMessage("You cannot use invalid characters.", 0, "");
                                            rsi.defaultText = "";
                                            return;
                                        }
                                        packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id,
                                                rsi.defaultText);
                                        inputString = "";
                                        promptInput = "";
                                        break;
                                    }
                                } else if (this.isInputFieldInFocus() && key == key) {
                                    Widget rsi = this.getInputFieldFocusOwner();
                                    if (rsi == null) {
                                        return;
                                    }
                                    if (key == 8 && rsi.defaultText.length() > 0) {
                                        rsi.defaultText = rsi.defaultText.substring(0, rsi.defaultText.length() - 1);
                                        update_chat_producer = true;
                                    }
                                }
                                if (key >= 32 && key <= 122 && inputString.length() < 80) {
                                    inputString += (char) key;
                                    update_chat_producer = true;
                                }
                                if (key == 8 && inputString.length() > 0) {
                                    inputString = inputString.substring(0, inputString.length() - 1);
                                    update_chat_producer = true;
                                }

                                if (key == 9) {
                                    tabToReplyPm();
                                }

                                // Remove the ability for players to do crowns..
                                if (inputString.contains("<img=")) {
                                    // System.err.println("Removed crown");
                                    inputString = inputString.replaceAll("<img=", "");
                                }
                                // Remove the ability for players to do double color messages..
                                if (inputString.contains("@@")) {
                                    inputString = inputString.replaceAll("@@", "");
                                }
                                // Remove the ability for players to do colour messages..
                                if (inputString.contains("<col")) {
                                    inputString = inputString.replaceAll("<col", "");
                                }
                                // Remove the ability for players to do transparent messages..
                                if (inputString.contains("<trans")) {
                                    inputString = inputString.replaceAll("<trans", "");
                                }
                                // Remove the ability for players to do shaded messages..
                                if (inputString.contains("<shad")) {
                                    inputString = inputString.replaceAll("<shad", "");
                                }
                                // Remove the ability for players to do global messages..
                                if (inputString.contains("[Global]")) {
                                    inputString = inputString.replaceAll("Global", "");
                                }
                                // Remove the ability for players to do global messages..
                                if (inputString.contains("[global]")) {
                                    inputString = inputString.replaceAll("global", "");
                                }
                                if ((key == 13 || key == 10) && inputString.length() > 0) {
                                    cmds();
                                    if (inputString.startsWith("/")) {
                                        inputString = "::" + inputString;
                                    }

                                    if (inputString.startsWith("::")) {
                                        packetSender.sendCommand(inputString.substring(2));
                                    } else {
                                        String text = inputString.toLowerCase();
                                        int colorCode = 0;
                                        if (text.startsWith("yellow:")) {
                                            colorCode = 0;
                                            inputString = inputString.substring(7);
                                        } else if (text.startsWith("red:")) {
                                            colorCode = 1;
                                            inputString = inputString.substring(4);
                                        } else if (text.startsWith("green:")) {
                                            colorCode = 2;
                                            inputString = inputString.substring(6);
                                        } else if (text.startsWith("cyan:")) {
                                            colorCode = 3;
                                            inputString = inputString.substring(5);
                                        } else if (text.startsWith("purple:")) {
                                            colorCode = 4;
                                            inputString = inputString.substring(7);
                                        } else if (text.startsWith("white:")) {
                                            colorCode = 5;
                                            inputString = inputString.substring(6);
                                        } else if (text.startsWith("flash1:")) {
                                            colorCode = 6;
                                            inputString = inputString.substring(7);
                                        } else if (text.startsWith("flash2:")) {
                                            colorCode = 7;
                                            inputString = inputString.substring(7);
                                        } else if (text.startsWith("flash3:")) {
                                            colorCode = 8;
                                            inputString = inputString.substring(7);
                                        } else if (text.startsWith("glow1:")) {
                                            colorCode = 9;
                                            inputString = inputString.substring(6);
                                        } else if (text.startsWith("glow2:")) {
                                            colorCode = 10;
                                            inputString = inputString.substring(6);
                                        } else if (text.startsWith("glow3:")) {
                                            colorCode = 11;
                                            inputString = inputString.substring(6);
                                        }
                                        text = inputString.toLowerCase();
                                        int effectCode = 0;
                                        if (text.startsWith("wave:")) {
                                            effectCode = 1;
                                            inputString = inputString.substring(5);
                                        } else if (text.startsWith("wave2:")) {
                                            effectCode = 2;
                                            inputString = inputString.substring(6);
                                        } else if (text.startsWith("shake:")) {
                                            effectCode = 3;
                                            inputString = inputString.substring(6);
                                        } else if (text.startsWith("scroll:")) {
                                            effectCode = 4;
                                            inputString = inputString.substring(7);
                                        } else if (text.startsWith("slide:")) {
                                            effectCode = 5;
                                            inputString = inputString.substring(6);
                                        }

                                        if (chatDelay.elapsed() < 599) {
                                            return;
                                        }

                                        // chat
                                        packetSender.sendChatMessage(colorCode, effectCode, inputString);
                                        inputString = ChatMessageCodec.processText(inputString);

                                        local_player.spokenText = inputString;
                                        local_player.textColour = colorCode;
                                        local_player.textEffect = effectCode;
                                        local_player.message_cycle = 150;

                                        List<ChatCrown> crowns = ChatCrown.get(myPrivilege, donatorPrivilege,
                                                ironmanPrivilege);
                                        StringBuilder crownPrefix = new StringBuilder();
                                        for (ChatCrown c : crowns) {
                                            crownPrefix.append(c.getIdentifier());
                                        }

                                        sendMessage(local_player.spokenText, 2, crownPrefix + local_player.username,
                                                local_player.getTitle(false));

                                        if (set_public_channel == 2) {
                                            set_public_channel = 3;
                                            packetSender.sendChatConfigurations(set_public_channel, privateChatMode,
                                                    tradeMode, clanChatMode);
                                        }

                                        chatDelay.reset();
                                    }
                                    inputString = "";
                                    update_chat_producer = true;
                                }
                            }
                        }
                        return;
                    }
                } while (true);
            }
        } while (true);

    }

    private void cmds() {
        if (inputString.equals("::vzo"))
            System.out.println(ObjectDefinition.get(32737));
        boolean isDeveloper = myPrivilege == 5;
        if (inputString.equals("::noclip")) {
            Client.instance.noclip = !Client.instance.noclip;
        }
        if (inputString.equals("::fps")) {
            setting.draw_fps = !setting.draw_fps;
        }
        if (inputString.startsWith("::zbuf")) {
            ClientConstants.ZBUFF = !ClientConstants.ZBUFF;
            enableDepth(ClientConstants.ZBUFF);
            Rasterizer3D.toggleZBuffering(ClientConstants.ZBUFF);
        }
        if (inputString.equals("::renderself")) {
            renderself = !renderself;
        }
        /* Client debug commands not actually used in the game */
        if (isDeveloper) {

            if (inputString.equals("::lagtest")) {
                addReportToServer("testy1!");
                addReportToServer("testy2!");
                addReportToServer("testy3!");
            }
            if (inputString.equals("::dc")) {
                processNetworkError();
            }
            if (inputString.equals("::lagtest2")) {
                String[] data = new String[] { String.valueOf(timeoutCounter), String.valueOf(opcode),
                        String.valueOf(lastOpcode), String.valueOf(secondLastOpcode), String.valueOf(thirdLastOpcode) };
                addReportToServer(Arrays.toString(data));
            }

            if (inputString.startsWith("::debugt")) {
                debugTextures = !debugTextures;
            }

            if (debugTextures) {
                if (inputString.startsWith("::abc")) {
                    int a = Integer.parseInt(inputString.split(" ")[1]);
                    int b = Integer.parseInt(inputString.split(" ")[2]);
                    int c = Integer.parseInt(inputString.split(" ")[3]);
                    texA = a;
                    texB = b;
                    texC = c;
                    sendMessage("New mapping = @red@" + texA + " | " + texB + " | " + texC, 0, "");
                }

                if (inputString.startsWith("::a")) {
                    texA = Integer.parseInt(inputString.split(" ")[1]);
                    sendMessage("New mapping for a = @red@" + texA, 0, "");
                }

                if (inputString.startsWith("::b")) {
                    texB = Integer.parseInt(inputString.split(" ")[1]);
                    sendMessage("New mapping for b = @red@" + texB, 0, "");
                }

                if (inputString.startsWith("::c")) {
                    texC = Integer.parseInt(inputString.split(" ")[1]);
                    sendMessage("New mapping for c = @red@" + texC, 0, "");
                }
            }

            if (inputString.startsWith("::colors")) {

            }
            if (inputString.equalsIgnoreCase("::getcolors")) {

            }
            // System.out.println(inputString);
            if (inputString.equalsIgnoreCase("::resetpm")) {
                resetSplitPrivateChatMessages();
                // System.out.println("Reset");
            }

            if (inputString.startsWith("::getcolor") && !inputString.equals("::getcolors")) {

            }
            if (inputString.equalsIgnoreCase("::ri")
                    || inputString
                            .equalsIgnoreCase("::reloadinterfaces")) {
                AdvancedFont[] fonts = { smallFont, regularFont, boldFont, fancyFont };
                Widget.load(interface_archive, fonts, mediaStreamLoader);
            }
            if (inputString.equalsIgnoreCase("::debugobj")) {
                int objectToDebug = 34895;
                ObjectDefinition data = ObjectDefinition.get(objectToDebug);
                for (int i = 0; i < data.modelIds.length; i++) {
                    System.out.println("objectDef.modelIds[" + i + "] = " + data.modelIds[i] + ";");
                }
            }
            if (inputString.equalsIgnoreCase("::debugobj2")) {
                int objectToDebug = 46702;
                ObjectDefinition data = ObjectDefinition.get(objectToDebug);
                for (int i = 0; i < data.transformVarbit; i++) {
                    System.out.println("objectDef.varbit[" + i + "] = " + data.transformVarbit + ";");
                }
            }
            if (inputString.equalsIgnoreCase("::chatcomp")) {
                if (resized) {
                    showChatComponents = !showChatComponents;
                    sendMessage("showing chat components is now "
                            + (showChatComponents ? "enabled" : "disabled") + ".", 0, "");
                }
            }
            if (inputString.equalsIgnoreCase("::showtab")) {
                if (resized) {
                    showTabComponents = !showTabComponents;
                    sendMessage("showing tab components is now "
                            + (showTabComponents ? "enabled" : "disabled") + ".", 0, "");
                }
            }
            if (inputString.equalsIgnoreCase("::fixed")) {
                frameMode(false);
            }
            if (inputString.equalsIgnoreCase("::resize")) {
                frameMode(true);
            }
            if (inputString.equalsIgnoreCase("::sendtestpacket")) {
                // packetSender.sendTestPacket(0);
            }
            if (inputString.equalsIgnoreCase("::renderdistance")) {
                sendMessage("Render distance is: " + SceneGraph.render_distance, 0, "");
            }
        }
        if (inputString.equalsIgnoreCase("::lastpacketsread")) {
            Client.debug_packet_info = !Client.debug_packet_info;
        }

        if (inputString.equalsIgnoreCase("::data")) {
            ClientConstants.CLIENT_DATA = !ClientConstants.CLIENT_DATA;
        }
        if (inputString.equalsIgnoreCase("::data1")) {
            ClientConstants.FORCE_OVERLAY_ABOVE_WIDGETS = !ClientConstants.FORCE_OVERLAY_ABOVE_WIDGETS;
        }
        if (inputString.equalsIgnoreCase("::dumpitemsprites")) {
            // We need to run this dumpitemsprites command twice, likely to get the images
            // into the cache.
            System.out.println("Dumping item images.");
            CacheUtils.dumpItemImages(32510, 32540);
            // CacheUtils.dumpItemImages( 0, ItemDefinition.length-1);
            System.out.println("Dumped item images.");
        }
        if (inputString.equalsIgnoreCase("::farmstatus")) {
            VarbitManager.dump();
            sendMessage("Farming varbits dumped to console.", 0, "");
        }
        if (inputString.equalsIgnoreCase("::findpatch")) {
            System.out.println("Searching for dynamic Fruit Tree Patches...");
            for (int i = 0; i < 80000; i++) {
                try {
                    ObjectDefinition def = ObjectDefinition.get(i);
                    if (def != null && def.name != null && def.name.toLowerCase().contains("fruit tree patch")) {
                        if (def.transforms != null || def.transformVarbit != -1 || def.transformVarp != -1) {
                            System.out.println("DYNAMIC Patch: ID=" + i + " Name=" + def.name + " Vb:"
                                    + def.transformVarbit + " Vp:" + def.transformVarp);
                            if (def.transforms != null) {
                                System.out.println("  Transforms: " + java.util.Arrays.toString(def.transforms));
                            }
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
            System.out.println("Search complete.");
            sendMessage("Search complete. Check console.", 0, "");
        }
        if (inputString.equalsIgnoreCase("::obj")) {
            int x = local_player.x;
            int y = local_player.y;
            int z = plane;
            com.cryptic.scene.SceneGraph sceneGraph = scene;
            if (sceneGraph != null) {
                try {
                    int tileX = x >> 7;
                    int tileY = y >> 7;
                    System.out
                            .println("Inspecting Objects at " + x + ", " + y + " (Tile: " + tileX + "," + tileY + ")");

                    // Check InteractiveObject
                    com.cryptic.entity.InteractiveObject go = sceneGraph.get_interactive_object(tileX, tileY, z);
                    if (go != null) {
                        long tag = go.uid;
                        int objId = com.cryptic.ViewportMouse.Entity_unpackID(tag);
                        ObjectDefinition def = ObjectDefinition.get(objId);
                        if (def != null) {
                            System.out.println("InteractiveObject: ID=" + objId + " name=" + def.name + " Vb:"
                                    + def.transformVarbit + " Vp:" + def.transformVarp);
                            sendMessage("Obj: " + objId + " Vb:" + def.transformVarbit + " Vp:" + def.transformVarp, 0,
                                    "");
                        }
                    } else {
                        // System.out.println("No InteractiveObject found.");
                    }

                    // Check Wall
                    com.cryptic.scene.object.Wall wall = sceneGraph.get_wall(z, tileX, tileY);
                    if (wall != null) {
                        long tag = wall.uid;
                        int objId = com.cryptic.ViewportMouse.Entity_unpackID(tag);
                        ObjectDefinition def = ObjectDefinition.get(objId);
                        if (def != null) {
                            System.out.println("Wall: ID=" + objId + " name=" + def.name + " Vb:" + def.transformVarbit
                                    + " Vp:" + def.transformVarp);
                            sendMessage("Wall: " + objId + " Vb:" + def.transformVarbit + " Vp:" + def.transformVarp, 0,
                                    "");
                        }
                    }

                    // Check WallDecoration
                    com.cryptic.scene.object.WallDecoration wallDecor = sceneGraph.get_wall_decor(tileX, tileY, z);
                    if (wallDecor != null) {
                        long tag = wallDecor.uid;
                        int objId = com.cryptic.ViewportMouse.Entity_unpackID(tag);
                        ObjectDefinition def = ObjectDefinition.get(objId);
                        if (def != null) {
                            System.out.println("WallDecor: ID=" + objId + " name=" + def.name + " Vb:"
                                    + def.transformVarbit + " Vp:" + def.transformVarp);
                            sendMessage("WD: " + objId + " Vb:" + def.transformVarbit + " Vp:" + def.transformVarp, 0,
                                    "");
                        }
                    }

                    // Check GroundDecoration
                    com.cryptic.scene.object.GroundDecoration groundDecor = sceneGraph.get_ground_decor(tileY, tileX,
                            z);
                    if (groundDecor != null) {
                        long tag = groundDecor.uid;
                        int objId = com.cryptic.ViewportMouse.Entity_unpackID(tag);
                        ObjectDefinition def = ObjectDefinition.get(objId);
                        if (def != null) {
                            System.out.println("GroundDecor: ID=" + objId + " name=" + def.name + " Vb:"
                                    + def.transformVarbit + " Vp:" + def.transformVarp);
                            sendMessage("GD: " + objId + " Vb:" + def.transformVarbit + " Vp:" + def.transformVarp, 0,
                                    "");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage("Error inspecting object.", 0, "");
                }
            }
        }

    }

    final Path colorPath = Paths.get("./colordump.txt");

    private void writeColors(int id, Set<Short> colors) {
        int[] colorArray = colors.stream().mapToInt(Short::shortValue).toArray();
        try {
            String str = "Id = " + id + " | Colors = " + Arrays.toString(colorArray) + System.lineSeparator()
                    + "-----------" + System.lineSeparator();
            Files.write(colorPath, str.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
    }

    private void buildPublicChat(int chatIndex) {
        int index = 0;
        for (int message = 0; message < 500; message++) {

            if (chatMessages[message] == null) {
                continue;
            }

            if (chatTypeView != 1) {
                continue;
            }

            int chatType = chatMessages[message].getType();
            String name = chatMessages[message].getName();
            int scrollAmount = (70 - index * 14 + 42) + chatScrollAmount + 4 + 5;
            if (scrollAmount < -23)
                break;
            if ((chatType == 1 || chatType == 2)
                    && (chatType == 1 || set_public_channel == 0 || set_public_channel == 1 && !check_username(name))) {
                if (chatIndex > scrollAmount - 14 && chatIndex <= scrollAmount
                        && !name.equalsIgnoreCase(local_player.username)) {
                    if (myPrivilege >= 1) {
                        menuActionText[menuActionRow] = "Report abuse <col=FFFFFF>" + name;
                        menuActionTypes[menuActionRow] = 606;
                        menuActionRow++;
                    }
                    menuActionText[menuActionRow] = "Add ignore <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Add friend <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 337;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Reply to <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 338;
                    menuActionRow++;
                }
                index++;
            }
        }
    }

    private void buildFriendChat(int chatIndex) {
        if (chatTypeView != 2) {
            return;
        }
        int index = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null)
                continue;
            if (chatTypeView != 2)
                continue;
            int chatType = chatMessages[i1].getType();
            String name = chatMessages[i1].getName();
            int scrollAmount = (70 - index * 14 + 42) + chatScrollAmount + 4 + 5;
            if (scrollAmount < -23)
                break;
            if ((chatType == 5 || chatType == 6) && (splitPrivateChat == 0 || chatTypeView == 2)
                    && (chatType == 6 || privateChatMode == 0 || privateChatMode == 1 && check_username(name)))
                index++;
            if ((chatType == 3 || chatType == 7) && (splitPrivateChat == 0 || chatTypeView == 2)
                    && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && check_username(name))) {
                if (chatIndex > scrollAmount - 14 && chatIndex <= scrollAmount) {
                    if (myPrivilege >= 1) {
                        menuActionText[menuActionRow] = "Report abuse <col=FFFFFF>" + name;
                        menuActionTypes[menuActionRow] = 606;
                        menuActionRow++;
                    }
                    menuActionText[menuActionRow] = "Add ignore <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Add friend <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 337;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Reply to <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 338;
                    menuActionRow++;
                }
                index++;
            }
        }
    }

    private void buildDuelorTrade(int chatIndex) {
        int index = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null)
                continue;
            if (chatTypeView != 3 && chatTypeView != 4)
                continue;
            int chatType = chatMessages[i1].getType();
            String name = chatMessages[i1].getName();
            int scrollAmount = (70 - index * 14 + 42) + chatScrollAmount + 4 + 5;
            if (scrollAmount < -23)
                break;
            if (chatTypeView == 3 && chatType == 4 && (tradeMode == 0 || tradeMode == 1 && check_username(name))) {
                if (chatIndex > scrollAmount - 14 && chatIndex <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept trade <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 484;
                    menuActionRow++;
                }
                index++;
            }
            if (chatTypeView == 4 && chatType == 8 && (tradeMode == 0 || tradeMode == 1 && check_username(name))) {
                if (chatIndex > scrollAmount - 14 && chatIndex <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept challenge <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 6;
                    menuActionRow++;
                }
                index++;
            }
            if (chatTypeView == 3 && chatType == 40 && (tradeMode == 0 || tradeMode == 1 && check_username(name))) {
                if (chatIndex > scrollAmount - 14 && chatIndex <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept gamble <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 525;
                    menuActionRow++;
                }
                index++;
            }
            if (chatTypeView == 3 && chatType == 41 && (tradeMode == 0 || tradeMode == 1 && check_username(name))) {
                if (chatIndex > scrollAmount - 14 && chatIndex <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept invite <col=FFFFFF>" + name;
                    menuActionTypes[menuActionRow] = 526;
                    menuActionRow++;
                }
                index++;
            }
        }
    }

    private void buildChatAreaMenu(int j) {
        if (inputDialogState == 3) {
            return;
        }

        int index = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null)
                continue;
            int chatType = chatMessages[i1].getType();
            int scrollAmount = (70 - index * 14 + 42) + chatScrollAmount + 4 + 5;
            if (scrollAmount < -23)
                break;
            String chatName = chatMessages[i1].getName();
            String message = chatMessages[i1].getMessage();
            if (chatTypeView == 1) {
                buildPublicChat(j);
                break;
            }
            if (chatTypeView == 2) {
                buildFriendChat(j);
                break;
            }
            if (chatTypeView == 3 || chatTypeView == 4) {
                buildDuelorTrade(j);
                break;
            }
            if (chatTypeView == 5) {
                break;
            }
            if (chatName == null) {
                continue;
            }
            if (chatType == 0)
                index++;
            for (String s : menuActionText) {
                if (s != null) {
                    if (!s.contains("link") && message.contains("<col=3030ff>")) {
                        message = message.substring(12);
                    }
                }
            }
            if (j > scrollAmount - 14 && j <= scrollAmount && message.contains("<link")) {
                message = "<col=3030ff>" + message;
                menuActionText[menuActionRow] = "Visit link";
                menuActionTypes[menuActionRow] = 449;
                selectedMsg = message;
                menuActionRow++;
            }
            if (j < scrollAmount - 14 && j > scrollAmount && message.contains("<link")) {
                message = message.substring(12);
                message = "<col=0>" + message;
            }
            if ((chatType == 1 || chatType == 2)
                    && (chatType == 1 || set_public_channel == 0
                            || set_public_channel == 1 && check_username(chatName))) {
                if (j > scrollAmount - 14 && j <= scrollAmount && !chatName.equalsIgnoreCase(local_player.username)) {
                    if (myPrivilege >= 1) {
                        menuActionText[menuActionRow] = "Report abuse <col=FFFFFF>" + chatName;
                        menuActionTypes[menuActionRow] = 606;
                        menuActionRow++;
                    }
                    menuActionText[menuActionRow] = "Add ignore <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Add friend <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 337;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Reply to <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 338;
                    menuActionRow++;
                }
                index++;
            }
            if ((chatType == 3 || chatType == 7) && splitPrivateChat == 0
                    && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && check_username(chatName))) {
                if (j > scrollAmount - 14 && j <= scrollAmount) {
                    if (myPrivilege >= 1) {
                        menuActionText[menuActionRow] = "Report abuse <col=FFFFFF>" + chatName;
                        menuActionTypes[menuActionRow] = 606;
                        menuActionRow++;
                    }
                    menuActionText[menuActionRow] = "Add ignore <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Add friend <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 337;
                    menuActionRow++;
                    menuActionText[menuActionRow] = "Reply to <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 338;
                    menuActionRow++;
                }
                index++;
            }
            if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && check_username(chatName))) {
                if (j > scrollAmount - 14 && j <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept trade <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 484;
                    menuActionRow++;
                }
                index++;
            }
            if ((chatType == 5 || chatType == 6) && splitPrivateChat == 0 && privateChatMode < 2)
                index++;
            if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && check_username(chatName))) {
                if (j > scrollAmount - 14 && j <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept challenge <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 6;
                    menuActionRow++;
                }
                index++;
            }
            if (chatType == 40 && (tradeMode == 0 || tradeMode == 1 && check_username(chatName))) {
                if (j > scrollAmount - 14 && j <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept gamble <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 525;
                    menuActionRow++;
                }
                index++;
            }

            if (chatType == 41 && (tradeMode == 0 || tradeMode == 1 && check_username(chatName))) {
                if (j > scrollAmount - 14 && j <= scrollAmount) {
                    menuActionText[menuActionRow] = "Accept invite <col=FFFFFF>" + chatName;
                    menuActionTypes[menuActionRow] = 526;
                    menuActionRow++;
                }
                index++;
            }

            if (chatType == 20) {
                if (j > scrollAmount - 14 && j <= scrollAmount) {
                    if (broadcast != null && broadcast.hasUrl()) {
                        menuActionText[menuActionRow] = "Open Link";
                        menuActionTypes[menuActionRow] = 5555;
                        menuActionRow++;
                    }
                }
                index++;
            }
        }
    }

    public String setSkillTooltip(int skillLevel) {
        String totalExperience = "";
        String[] getToolTipText = new String[4];
        String setToolTipText = "";
        int maxLevel = 99;

        if (realSkillLevels[skillLevel] > maxLevel) {
            if (skillLevel != 24) {
                realSkillLevels[skillLevel] = 99;
            } else if (realSkillLevels[skillLevel] > 120) {
                realSkillLevels[skillLevel] = 120;
            }
        }

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        int[] getSkillId = { 0, 0, 2, 1, 4, 5, 6, 20, 22, 3, 16, 15, 17, 12, 9, 18, 21, 14, 14, 13, 10, 7, 11, 8, 19,
                24 };
        int totalXP = skillExperience[0] + skillExperience[1] + skillExperience[2] + skillExperience[3]
                + skillExperience[4] + skillExperience[5]
                + skillExperience[6] + skillExperience[7] + skillExperience[8] + skillExperience[9]
                + skillExperience[10] + skillExperience[11]
                + skillExperience[12] + skillExperience[13] + skillExperience[14] + skillExperience[15]
                + skillExperience[16] + skillExperience[17]
                + skillExperience[18] + skillExperience[19] + skillExperience[20] + skillExperience[21]
                + skillExperience[22];
        totalExperience = numberFormat.format(totalXP);
        if (!SkillConstants.SKILL_NAMES_SKILLSTAB[skillLevel].equals("-1")) {
            if (realSkillLevels[getSkillId[skillLevel]] >= 99) {
                getToolTipText[0] = SkillConstants.SKILL_NAMES_SKILLSTAB[skillLevel] + " XP: "
                        + numberFormat.format(skillExperience[getSkillId[skillLevel]]) + "<br>";
                setToolTipText = getToolTipText[0];
            } else {
                getToolTipText[0] = SkillConstants.SKILL_NAMES_SKILLSTAB[skillLevel] + " XP: "
                        + numberFormat.format(skillExperience[getSkillId[skillLevel]]) + "<br>";
                getToolTipText[1] = "Next level at: "
                        + (numberFormat.format(getXPForLevel(realSkillLevels[getSkillId[skillLevel]] + 1))) + "<br>";
                getToolTipText[2] = "Remaining XP: " + numberFormat.format(
                        getXPForLevel(realSkillLevels[getSkillId[skillLevel]] + 1)
                                - skillExperience[getSkillId[skillLevel]])
                        + "<br>";
                getToolTipText[3] = "";
                setToolTipText = getToolTipText[0] + getToolTipText[1] + getToolTipText[2];
            }
        } else {
            setToolTipText = "Total XP: " + totalExperience;
        }
        return setToolTipText;
    }

    /**
     * interface_handle_auto_content
     */
    private void handle_widget_support(Widget child) {
        int support_opcode = child.contentType;

        if (child.invisible)
            return;

        if ((support_opcode >= 205) && (support_opcode <= (205 + 25))) {
            support_opcode -= 205;
            child.defaultText = setSkillTooltip(support_opcode);
            return;
        }

        if (support_opcode == 831) {
            child.defaultText = setSkillTooltip(0);
            return;
        }
        if (support_opcode >= 1 && support_opcode <= 100 || support_opcode >= 701 && support_opcode <= 800) {
            if (support_opcode == 1 && friendServerStatus == 0) {
                child.defaultText = "Loading friend list";
                child.optionType = 0;
                return;
            }
            if (support_opcode == 1 && friendServerStatus == 1) {
                child.defaultText = "Connecting to friendserver";
                child.optionType = 0;
                return;
            }
            if (support_opcode == 2 && friendServerStatus != 2) {
                child.defaultText = "Please wait...";
                child.optionType = 0;
                return;
            }
            int k = friendsCount;
            if (friendServerStatus != 2)
                k = 0;
            if (support_opcode > 700)
                support_opcode -= 601;
            else
                support_opcode--;
            if (support_opcode >= k) {
                child.defaultText = "";
                child.optionType = 0;
                return;
            } else {
                child.defaultText = friendsList[support_opcode];
                child.optionType = 1;
                return;
            }
        }
        if (support_opcode >= 101 && support_opcode <= 200 || support_opcode >= 801 && support_opcode <= 900) {
            int l = friendsCount;
            if (friendServerStatus != 2)
                l = 0;
            if (support_opcode > 800)
                support_opcode -= 701;
            else
                support_opcode -= 101;
            if (support_opcode >= l) {
                child.defaultText = "";
                child.optionType = 0;
                return;
            }
            if (friendsNodeIDs[support_opcode] == 0)
                child.defaultText = "<col=FF0000>Offline";
            else if (friendsNodeIDs[support_opcode] == nodeID)
                child.defaultText = "<col=00FF00>Online";
            else
                child.defaultText = "<col=FF0000>Offline";
            child.optionType = 1;
            return;
        }

        if (support_opcode == 203) {
            int i1 = friendsCount;
            if (friendServerStatus != 2)
                i1 = 0;
            child.scrollMax = i1 * 15 + 20;
            if (child.scrollMax <= child.height)
                child.scrollMax = child.height + 1;
            return;
        }
        if (support_opcode >= 401 && support_opcode <= 500) {
            if ((support_opcode -= 401) == 0 && friendServerStatus == 0) {
                child.defaultText = "Loading ignore list";
                child.optionType = 0;
                return;
            }
            if (support_opcode == 1 && friendServerStatus == 0) {
                child.defaultText = "Please wait...";
                child.optionType = 0;
                return;
            }
            int j1 = ignoreCount;
            if (friendServerStatus == 0)
                j1 = 0;
            if (support_opcode >= j1) {
                child.defaultText = "";
                child.optionType = 0;
                return;
            } else {
                child.defaultText = ignoreList[support_opcode];
                child.optionType = 1;
                return;
            }
        }
        if (support_opcode == 503) {
            child.scrollMax = ignoreCount * 15 + 20;
            if (child.scrollMax <= child.height)
                child.scrollMax = child.height + 1;
            return;
        }
        if (support_opcode == 327) {
            child.modelAngleX = 150;
            child.modelAngleY = (int) (Math.sin((double) tick / 40D) * 256D) & 0x7ff;
            if (updateCharacterCreation) {
                for (int k1 = 0; k1 < 7; k1++) {
                    int l1 = characterClothing[k1];
                    if (l1 >= 0 && !IdentityKit.lookup(l1).body_cached())
                        return;
                }

                updateCharacterCreation = false;
                Mesh aclass30_sub2_sub4_sub6s[] = new Mesh[7];
                int i2 = 0;
                for (int j2 = 0; j2 < 7; j2++) {
                    int k2 = characterClothing[j2];
                    if (k2 >= 0)
                        aclass30_sub2_sub4_sub6s[i2++] = IdentityKit.lookup(k2).get_body();
                }

                Mesh model = new Mesh(aclass30_sub2_sub4_sub6s, i2);
                for (int l2 = 0; l2 < 5; l2++) {
                    if (characterDesignColours[l2] != 0) {

                        if (this.characterDesignColours[l2] < Client.PLAYER_BODY_RECOLOURS[l2].length) {
                            model.recolor(Client.field3532[l2],
                                    Client.PLAYER_BODY_RECOLOURS[l2][this.characterDesignColours[l2]]);
                        }

                        if (l2 == 1) {
                            if (this.characterDesignColours[l2] < Client.field3535[l2].length) {
                                model.recolor(Client.field3534[l2],
                                        Client.field3535[l2][this.characterDesignColours[l2]]);
                            }
                        }

                    }
                }

                Model finalModel = model.toModel(64, 850, -30, -50, -30);

                SequenceDefinition.get(local_player.idleSequence).transformWidgetModel(finalModel, 0);
                child.defaultMediaType = 5;
                child.defaultMedia = 0;
                child.set_model(aBoolean994, finalModel);
            }
            return;
        }
        if (support_opcode == 328) {
            Widget rsInterface = child;
            int verticleTilt = 150;
            int animationSpeed = (int) (Math.sin((double) tick / 40D) * 256D) & 0x7ff;
            rsInterface.modelAngleX = verticleTilt;
            rsInterface.modelAngleY = animationSpeed;
            if (updateCharacterCreation) {

                Model characterDisplay = local_player.get_animated_model();

                int staticFrame = local_player.idleSequence;

                SequenceDefinition.get(staticFrame).transformWidgetModel(characterDisplay, 0);
                // characterDisplay.light(64, 850, -30, -50, -30, true);
                rsInterface.defaultMediaType = 5;
                rsInterface.defaultMedia = 0;
                child.set_model(aBoolean994, characterDisplay);
            }
            return;
        }

        if (support_opcode == 1430 && child.scrollMax > 5) {
            if (child.pauseTicks > 0) {
                child.pauseTicks--;
                return;
            }
            Widget parent = Widget.cache[child.parent];
            if (child.scrollPosition == -child.scrollMax) {
                child.endReached = true;
                child.pauseTicks = 20;
            }
            if (child.endReached) {
                if (child.scrollPosition == 0) {
                    child.endReached = false;
                    child.pauseTicks = 20;
                }
                child.scrollPosition++;
            } else {
                child.scrollPosition--;
            }
            parent.child_x[0] = child.scrollPosition;
        }
        if (support_opcode == 324) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = child.enabledSprite;
                aClass30_Sub2_Sub1_Sub1_932 = child.disabledSprite;
            }
            if (characterGender) {
                child.enabledSprite = aClass30_Sub2_Sub1_Sub1_932;
                return;
            } else {
                child.enabledSprite = aClass30_Sub2_Sub1_Sub1_931;
                return;
            }
        }
        if (support_opcode == 325) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = child.enabledSprite;
                aClass30_Sub2_Sub1_Sub1_932 = child.disabledSprite;
            }
            if (characterGender) {
                child.enabledSprite = aClass30_Sub2_Sub1_Sub1_931;
                return;
            } else {
                child.enabledSprite = aClass30_Sub2_Sub1_Sub1_932;
                return;
            }
        }
        if (support_opcode == 600) {
            child.defaultText = reportAbuseInput;
            if (tick % 20 < 10) {
                child.defaultText += "|";
                return;
            } else {
                child.defaultText += " ";
                return;
            }
        }
        if (support_opcode == 613)
            if (myPrivilege >= 1) {
                if (canMute) {
                    child.textColour = 0xff0000;
                    child.defaultText = "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    child.textColour = 0xffffff;
                    child.defaultText = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                child.defaultText = "";
            }
        if (support_opcode == 650 || support_opcode == 655)
            if (anInt1193 != 0) {
                String s;
                if (daysSinceLastLogin == 0)
                    s = "earlier today";
                else if (daysSinceLastLogin == 1)
                    s = "yesterday";
                else
                    s = daysSinceLastLogin + " days ago";
                child.defaultText = "You last logged in " + s + " from: " + SignLink.dns;
            } else {
                child.defaultText = "";
            }
        if (support_opcode == 651) {
            if (unreadMessages == 0) {
                child.defaultText = "0 unread messages";
                child.textColour = 0xffff00;
            }
            if (unreadMessages == 1) {
                child.defaultText = "1 unread defaultText";
                child.textColour = 65280;
            }
            if (unreadMessages > 1) {
                child.defaultText = unreadMessages + " unread messages";
                child.textColour = 65280;
            }
        }
        if (support_opcode == 652)
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1)
                    child.defaultText = "<col=ffff00>This is a non-members world: <col=FFFFFF>Since you are a member we";
                else
                    child.defaultText = "";
            } else if (daysSinceRecovChange == 200) {
                child.defaultText = "You have not yet set any password recovery questions.";
            } else {
                String s1;
                if (daysSinceRecovChange == 0)
                    s1 = "Earlier today";
                else if (daysSinceRecovChange == 1)
                    s1 = "Yesterday";
                else
                    s1 = daysSinceRecovChange + " days ago";
                child.defaultText = s1 + " you changed your recovery questions";
            }
        if (support_opcode == 653)
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1)
                    child.defaultText = "<col=FFFFFF>recommend you use a members world instead. You may use";
                else
                    child.defaultText = "";
            } else if (daysSinceRecovChange == 200)
                child.defaultText = "We strongly recommend you do so now to secure your account.";
            else
                child.defaultText = "If you do not remember making this change then cancel it immediately";
        if (support_opcode == 654) {
            if (daysSinceRecovChange == 201)
                if (membersInt == 1) {
                    child.defaultText = "<col=FFFFFF>this world but member benefits are unavailable whilst here.";
                    return;
                } else {
                    child.defaultText = "";
                    return;
                }
            if (daysSinceRecovChange == 200) {
                child.defaultText = "Do this from the 'account management' area on our front webpage";
                return;
            }
            child.defaultText = "Do this from the 'account management' area on our front webpage";
        }
    }

    private int update_offset = 0;

    private Constraint createPMConstraint() {
        if (update_offset == 0) {
            return null;
        }
        return new Constraint(5, 110, 321 - (update_offset * 10), 334); // TODO: endX should be determined by the width
                                                                        // of that specific msg

    }

    public int totalMessages = 0;

    private void drawSplitPrivateChat() {
        if (splitPrivateChat == 0) {
            return;
        }
        update_offset = 0;
        if (rebootTimer != 0) {
            update_offset = 1;
        }
        totalMessages = 0;
        // Increase from 100 to full list size.
        for (int message_index = 0; message_index < 100; message_index++) {
            if (splitPrivateChatMessages.get(message_index) != null) {
                int messageType = splitPrivateChatMessages.get(message_index).getType();
                String name = StringUtils.capitalize(splitPrivateChatMessages.get(message_index)
                        .getName());
                List<ChatCrown> crowns = splitPrivateChatMessages.get(message_index)
                        .getCrowns();
                if ((messageType == 3 || messageType == 7)
                        && (privateChatMode == 0 || privateChatMode == 1 && check_username(name))) {
                    boolean onIgnore = false;
                    if (name != null) {
                        for (int index = 0; index < ignoreCount; index++) {
                            if (ignoreListAsLongs[index] != longForName(name)) {
                                continue;
                            }
                            onIgnore = true;
                            break;
                        }
                        if (onIgnore)
                            break;
                    }
                    int y = this.broadcast != null && isDisplayed ? 309 - update_offset * 13 : 329 - update_offset * 13;
                    if (resized) {
                        y = canvasHeight - 170 - update_offset * 13;
                        if (broadcast != null && isDisplayed) {
                            y -= 20;
                        }
                        if (cButtonCPos == -1)
                            y += 135;
                    }
                    int x = 4;
                    /** pm above inter **/
                    regularFont.draw("From", x, y, 65535, true);
                    x += regularFont.getTextWidth("From ");
                    for (ChatCrown c : crowns) {
                        SimpleImage sprite = SpriteCache.get(c.getSpriteId());
                        if (sprite != null) {
                            sprite.drawAdvancedSprite(x, y - 12);
                            x += sprite.width + 2;
                        }
                    }

                    totalMessages++;
                    regularFont.draw(name + ": " + splitPrivateChatMessages.get(message_index)
                            .getMessage(), x, y, 65535, true);

                    if (++update_offset >= 5) {
                        return;
                    }
                }
                if (messageType == 5 && privateChatMode < 2) {
                    int y = this.broadcast != null && isDisplayed ? 309 - update_offset * 13 : 329 - update_offset * 13;
                    if (resized) {
                        y = canvasHeight - 170 - update_offset * 13;
                        if (broadcast != null && isDisplayed) {
                            y -= 20;
                        }
                    }

                    regularFont.draw(splitPrivateChatMessages.get(message_index)
                            .getMessage(), 4, y, 65535, true);
                    totalMessages++;

                    if (++update_offset >= 5) {
                        return;
                    }
                }
                if (messageType == 6 && privateChatMode < 2) {
                    int y = this.broadcast != null && isDisplayed ? 309 - update_offset * 13 : 329 - update_offset * 13;
                    if (resized) {
                        y = canvasHeight - 170 - update_offset * 13;
                        if (broadcast != null && isDisplayed) {
                            y -= 20;
                        }
                    }
                    totalMessages++;
                    regularFont
                            .draw("To " + name + ": " + capitalizeJustFirst(splitPrivateChatMessages.get(message_index)
                                    .getMessage()), 4, y, 65535, 0);
                    if (++update_offset >= 5) {
                        return;
                    }
                }
            }
        }
    }

    public static String capitalizeJustFirst(String str) {
        str = str.toLowerCase();
        if (str.length() > 1) {
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        } else {
            return str.toUpperCase();
        }
        return str;
    }

    private final ChatMessage[] chatMessages;
    private final List<ChatMessage> splitPrivateChatMessages;

    private void resetSplitPrivateChatMessages() {
        for (int index = 0; index < 500; index++) {
            splitPrivateChatMessages.set(index, null);
        }
    }

    public void sendMessage(String message, int type, String name) {
        sendMessage(message, type, name, null);
    }

    public void sendMessage(String message, int type, String name, String title, List<ChatCrown> crowns) {
        if (name == null || name.length() == 0) {
            name = "";
        }
        saveMessageDetails(message, type, name, title, crowns);
    }

    public void sendMessage(String message, int type, String name, String title) {
        if (name == null || name.length() == 0) {
            name = "";
            // return;
        }
        List<ChatCrown> crowns = new ArrayList<>();
        for (ChatCrown c : ChatCrown.values()) {
            boolean exists = false;
            if (message.contains(c.getIdentifier())) {
                message = message.replaceAll(c.getIdentifier(), "");
                exists = true;
            }
            if (name.contains(c.getIdentifier())) {
                name = name.replaceAll(c.getIdentifier(), "");
                exists = true;
            }
            if (exists) {
                // System.out.println("Crown exists!");
                if (!crowns.contains(c)) {
                    crowns.add(c);
                }
            }
        }

        saveMessageDetails(message, type, name, title, crowns);
    }

    private void saveMessageDetails(String message, int type, String name, String title, List<ChatCrown> crowns) {
        if (type == 0 && dialogueId != -1) {
            clickToContinueString = message;
            MouseHandler.keypressedEventIndex = 0;
        }

        if (backDialogueId == -1) {
            update_chat_producer = true;
        }

        // Create new chat message
        ChatMessage chatMessage = new ChatMessage(message, name, title, type, rights, crowns);

        for (int index = 499; index > 0; index--) {
            chatMessages[index] = chatMessages[index - 1];
            splitPrivateChatMessages.set(index, splitPrivateChatMessages.get(index - 1));
        }

        // Insert new message
        chatMessages[0] = chatMessage;
        splitPrivateChatMessages.set(0, chatMessage);
    }

    public static void setTab(int id) {
        sidebarId = id;
        update_tab_producer = true;
    }

    public boolean mouseInCircle(int x, int y, int radius) {
        return Math.pow(MouseHandler.mouseX - (x + radius), 2)
                + Math.pow(MouseHandler.mouseY - (y + radius), 2) <= radius * radius;
    }

    private void minimapHovers() {
        final boolean fixed = !resized;

        int xOffset = fixed ? 516 : canvasWidth - 216;

        prayHover = fixed
                ? prayHover = MouseHandler.mouseX >= 517 && MouseHandler.mouseX <= 572
                        && MouseHandler.mouseY >= (setting.draw_special_orb ? 79 : 94)
                        && MouseHandler.mouseY < (setting.draw_special_orb ? 111 : 118)
                : MouseHandler.mouseX >= canvasWidth - 210 && MouseHandler.mouseX <= canvasWidth - 157
                        && MouseHandler.mouseY >= (setting.draw_special_orb ? 90 : 95)
                        && MouseHandler.mouseY < (setting.draw_special_orb ? 119 : 124);

        // expCounterHover = fixed ? MouseHandler.mouseX >= 517 && MouseHandler.mouseX
        // <= 530 && MouseHandler.mouseY >= 22 && MouseHandler.mouseY <= 41 :
        // //resize
        // MouseHandler.mouseX >= window_width - 215 && MouseHandler.mouseX <=
        // window_width - 200 && MouseHandler.mouseY >= 21 && MouseHandler.mouseY <= 40;

        expCounterHover = mouseInCircle(xOffset + (fixed ? 0 : 4), 20,
                SpriteCache.get(counterOn ? 594 : 23).width / 2 + 1);

        runHover = fixed
                ? runHover = MouseHandler.mouseX >= (setting.draw_special_orb ? 528 : 542)
                        && MouseHandler.mouseX <= (setting.draw_special_orb ? 582 : 594)
                        && MouseHandler.mouseY >= (setting.draw_special_orb ? 111 : 129)
                        && MouseHandler.mouseY < (setting.draw_special_orb ? 143 : 157)
                : MouseHandler.mouseX >= canvasWidth - (setting.draw_special_orb ? 196 : 186)
                        && MouseHandler.mouseX <= canvasWidth - (setting.draw_special_orb ? 142 : 132)
                        && MouseHandler.mouseY >= (setting.draw_special_orb ? 123 : 132)
                        && MouseHandler.mouseY < (setting.draw_special_orb ? 150 : 159);
    }

    private final int[] tabClickX = { 38, 33, 33, 33, 33, 33, 38, 38, 33, 33, 33, 33, 33, 38 },
            tabClickStart = { 522, 560, 593, 625, 659, 692, 724, 522, 560, 593, 625, 659, 692, 724 },
            tabClickY = { 169, 169, 169, 169, 169, 169, 169, 466, 466, 466, 466, 466, 466, 466 };

    private void processTabClick() {
        if (MouseHandler.keypressedEventIndex == 1) {
            resetInputFieldFocus();
            if (!resized
                    || resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
                int xOffset = !resized ? 0 : canvasWidth - 765;
                int yOffset = !resized ? 0 : canvasHeight - 503;
                for (int i = 0; i < tabClickX.length; i++) {
                    if (MouseHandler.mouseX >= tabClickStart[i] + xOffset
                            && MouseHandler.mouseX <= tabClickStart[i] + tabClickX[i] + xOffset
                            && MouseHandler.mouseY >= tabClickY[i] + yOffset
                            && MouseHandler.mouseY < tabClickY[i] + 37 + yOffset
                            && tabInterfaceIDs[i] != -1) {
                        sidebarId = i;
                        update_tab_producer = true;
                        break;
                    }
                }
            } else if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1
                    && canvasWidth < 1000) {
                if (MouseHandler.saveClickX >= canvasWidth - 226 && MouseHandler.saveClickX <= canvasWidth - 195
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[0] != -1) {
                    if (sidebarId == 0) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 0;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 194 && MouseHandler.saveClickX <= canvasWidth - 163
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[1] != -1) {
                    if (sidebarId == 1) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 1;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 162 && MouseHandler.saveClickX <= canvasWidth - 131
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[2] != -1) {
                    if (sidebarId == 2) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 2;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 129 && MouseHandler.saveClickX <= canvasWidth - 98
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[3] != -1) {
                    if (sidebarId == 3) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 3;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 97 && MouseHandler.saveClickX <= canvasWidth - 66
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[4] != -1) {
                    if (sidebarId == 4) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 4;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 65 && MouseHandler.saveClickX <= canvasWidth - 34
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[5] != -1) {
                    if (sidebarId == 5) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 5;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 33 && MouseHandler.saveClickX <= canvasWidth
                        && MouseHandler.saveClickY >= canvasHeight - 72 && MouseHandler.saveClickY < canvasHeight - 40
                        && tabInterfaceIDs[6] != -1) {
                    if (sidebarId == 6) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 6;
                    update_tab_producer = true;

                }

                if (MouseHandler.saveClickX >= canvasWidth - 194 && MouseHandler.saveClickX <= canvasWidth - 163
                        && MouseHandler.saveClickY >= canvasHeight - 37 && MouseHandler.saveClickY < canvasHeight
                        && tabInterfaceIDs[8] != -1) {
                    if (sidebarId == 8) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 8;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 162 && MouseHandler.saveClickX <= canvasWidth - 131
                        && MouseHandler.saveClickY >= canvasHeight - 37 && MouseHandler.saveClickY < canvasHeight
                        && tabInterfaceIDs[9] != -1) {
                    if (sidebarId == 9) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 9;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 129 && MouseHandler.saveClickX <= canvasWidth - 98
                        && MouseHandler.saveClickY >= canvasHeight - 37 && MouseHandler.saveClickY < canvasHeight
                        && tabInterfaceIDs[10] != -1) {
                    if (sidebarId == 7) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 7;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 97 && MouseHandler.saveClickX <= canvasWidth - 66
                        && MouseHandler.saveClickY >= canvasHeight - 37 && MouseHandler.saveClickY < canvasHeight
                        && tabInterfaceIDs[11] != -1) {
                    if (sidebarId == 11) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 11;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 65 && MouseHandler.saveClickX <= canvasWidth - 34
                        && MouseHandler.saveClickY >= canvasHeight - 37 && MouseHandler.saveClickY < canvasHeight
                        && tabInterfaceIDs[12] != -1) {
                    if (sidebarId == 12) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 12;
                    update_tab_producer = true;

                }
                if (MouseHandler.saveClickX >= canvasWidth - 33 && MouseHandler.saveClickX <= canvasWidth
                        && MouseHandler.saveClickY >= canvasHeight - 37 && MouseHandler.saveClickY < canvasHeight
                        && tabInterfaceIDs[13] != -1) {
                    if (sidebarId == 13) {
                        showTabComponents = !showTabComponents;
                    } else {
                        showTabComponents = true;
                    }
                    sidebarId = 13;
                    update_tab_producer = true;

                }
            } else if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1
                    && canvasWidth >= 1000) {
                if (MouseHandler.mouseY >= canvasHeight - 37 && MouseHandler.mouseY <= canvasHeight) {
                    if (MouseHandler.mouseX >= canvasWidth - 417 && MouseHandler.mouseX <= canvasWidth - 386) {
                        if (sidebarId == 0) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 0;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 385 && MouseHandler.mouseX <= canvasWidth - 354) {
                        if (sidebarId == 1) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 1;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 353 && MouseHandler.mouseX <= canvasWidth - 322) {
                        if (sidebarId == 2) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 2;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 321 && MouseHandler.mouseX <= canvasWidth - 290) {
                        if (sidebarId == 3) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 3;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 289 && MouseHandler.mouseX <= canvasWidth - 258) {
                        if (sidebarId == 4) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 4;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 257 && MouseHandler.mouseX <= canvasWidth - 226) {
                        if (sidebarId == 5) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 5;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 225 && MouseHandler.mouseX <= canvasWidth - 194) {
                        if (sidebarId == 6) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 6;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 193 && MouseHandler.mouseX <= canvasWidth - 163) {
                        if (sidebarId == 8) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 8;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 162 && MouseHandler.mouseX <= canvasWidth - 131) {
                        if (sidebarId == 9) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 9;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 130 && MouseHandler.mouseX <= canvasWidth - 99) {
                        if (sidebarId == 7) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 7;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 98 && MouseHandler.mouseX <= canvasWidth - 67) {
                        if (sidebarId == 11) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 11;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 66 && MouseHandler.mouseX <= canvasWidth - 45) {
                        if (sidebarId == 12) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 12;
                        update_tab_producer = true;
                    }
                    if (MouseHandler.mouseX >= canvasWidth - 31 && MouseHandler.mouseX <= canvasWidth) {
                        if (sidebarId == 13) {
                            showTabComponents = !showTabComponents;
                        } else {
                            showTabComponents = true;
                        }
                        sidebarId = 13;
                        update_tab_producer = true;
                    }
                }
            }
        }
    }

    private void refreshMinimap(SimpleImage sprite, int j, int k) {
        int l = k * k + j * j;
        if (l > 4225 && l < 0x15f90) {
            int i1 = camAngleY + map_rotation & 0x7ff;
            int j1 = Model.SINE[i1];
            int k1 = Model.COSINE[i1];
            j1 = (j1 * 256) / (minimapZoom + 256);
            k1 = (k1 * 256) / (minimapZoom + 256);
        } else {
            markMinimap(sprite, k, j);
        }
    }

    public void rightClickChatButtons() {
        if (MouseHandler.mouseY >= canvasHeight - 22 && MouseHandler.mouseY <= canvasHeight) {
            if (MouseHandler.mouseX >= 5 && MouseHandler.mouseX <= 61) {
                menuActionText[1] = "View All";
                menuActionTypes[1] = 999;
                menuActionRow = 2;
            } else if (MouseHandler.mouseX >= 71 && MouseHandler.mouseX <= 127) {
                menuActionText[1] = "View Game";
                menuActionTypes[1] = 998;
                menuActionRow = 2;
            } else if (MouseHandler.mouseX >= 137 && MouseHandler.mouseX <= 193) {
                menuActionText[1] = "Hide public";
                menuActionTypes[1] = 997;
                menuActionText[2] = "Off public";
                menuActionTypes[2] = 996;
                menuActionText[3] = "Friends public";
                menuActionTypes[3] = 995;
                menuActionText[4] = "On public";
                menuActionTypes[4] = 994;
                menuActionText[5] = "View public";
                menuActionTypes[5] = 993;
                menuActionRow = 6;
            } else if (MouseHandler.mouseX >= 203 && MouseHandler.mouseX <= 259) {
                menuActionText[1] = "Off private";
                menuActionTypes[1] = 992;
                menuActionText[2] = "Friends private";
                menuActionTypes[2] = 991;
                menuActionText[3] = "On private";
                menuActionTypes[3] = 990;
                menuActionText[4] = "View private";
                menuActionTypes[4] = 989;
                menuActionRow = 5;
                menuActionText[5] = "Clear private";
                menuActionTypes[5] = 1895;
                menuActionRow = 6;
            } else if (MouseHandler.mouseX >= 269 && MouseHandler.mouseX <= 325) {
                menuActionText[1] = "Off clan chat";
                menuActionTypes[1] = 1003;
                menuActionText[2] = "Friends clan chat";
                menuActionTypes[2] = 1002;
                menuActionText[3] = "On clan chat";
                menuActionTypes[3] = 1001;
                menuActionText[4] = "View clan chat";
                menuActionTypes[4] = 1000;
                menuActionRow = 5;
            } else if (MouseHandler.mouseX >= 335 && MouseHandler.mouseX <= 391) {
                menuActionText[1] = "Off trade";
                menuActionTypes[1] = 987;
                menuActionText[2] = "Friends trade";
                menuActionTypes[2] = 986;
                menuActionText[3] = "On trade";
                menuActionTypes[3] = 985;
                menuActionText[4] = "View trade";
                menuActionTypes[4] = 984;
                menuActionRow = 5;
            } else if (MouseHandler.mouseX >= 404 && MouseHandler.mouseX <= 515) {
                menuActionText[1] = "Report Abuse";
                menuActionTypes[1] = 606;
                menuActionRow = 2;
            }
        }
    }

    private boolean interfaceOpen() {
        return widget_overlay_id > 0;
    }

    public void processRightClick() {
        if (activeInterfaceType != 0) {
            return;
        }
        menuActionText[0] = "Cancel";
        menuActionTypes[0] = 1107;
        menuActionRow = 1;
        if (showChatComponents) {
            buildSplitPrivateChatMenu();
        }

        frameFocusedInterface = 0;
        anInt1315 = 0;
        if (!resized) {
            if (MouseHandler.mouseX > 4 && MouseHandler.mouseY > 4 && MouseHandler.mouseX < 516
                    && MouseHandler.mouseY < 338) {
                if (widget_overlay_id != -1) {
                    buildInterfaceMenu(4, Widget.cache[widget_overlay_id], MouseHandler.mouseX, 4, MouseHandler.mouseY,
                            0);
                    // When this condition is true, the screen menu is never created, therefore
                    // "walk here" and other things aren't created, which will prevent the player
                    // from clicking on the screen. We commented this out for "walk here" to work.
                    // It might change how "walk here" works in right click menus but it should be
                    // fine.
                } else {
                    createMenu();
                }
            }

        } else if (resized) {
            if (getMousePositions()) {
                if (MouseHandler.mouseX > (canvasWidth / 2) - 356 && MouseHandler.mouseY > (canvasHeight / 2) - 230
                        && MouseHandler.mouseX < ((canvasWidth / 2) + 356)
                        && MouseHandler.mouseY < (canvasHeight / 2) + 230 && widget_overlay_id != -1) {
                    buildInterfaceMenu((canvasWidth / 2) - 356, Widget.cache[widget_overlay_id], MouseHandler.mouseX,
                            (canvasHeight / 2) - 230, MouseHandler.mouseY, 0);
                    // When this condition is true, the screen menu is never created, therefore
                    // "walk here" and other things aren't created, which will prevent the player
                    // from clicking on the screen. We commented this out for "walk here" to work.
                    // It might change how "walk here" works in right click menus but it should be
                    // fine.
                } else {
                    createMenu();
                }
            }
        }

        if (frameFocusedInterface != focusedViewportWidget) {
            focusedViewportWidget = frameFocusedInterface;
        }
        if (anInt1315 != gameTooltipSupportId) {
            gameTooltipSupportId = anInt1315;
        }
        frameFocusedInterface = 0;
        anInt1315 = 0;

        if (!resized || settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
            final int yOffset = !resized ? 0 : canvasHeight - 503;
            final int xOffset = !resized ? 0 : canvasWidth - 765;
            if (MouseHandler.mouseX > 548 + xOffset && MouseHandler.mouseX < 740 + xOffset
                    && MouseHandler.mouseY > 207 + yOffset
                    && MouseHandler.mouseY < 468 + yOffset) {
                if (overlayInterfaceId != -1) {
                    buildInterfaceMenu(548 + xOffset, Widget.cache[overlayInterfaceId], MouseHandler.mouseX,
                            207 + yOffset, MouseHandler.mouseY, 0);
                } else if (tabInterfaceIDs[sidebarId] != -1) {
                    buildInterfaceMenu(548 + xOffset, Widget.cache[tabInterfaceIDs[sidebarId]], MouseHandler.mouseX,
                            207 + yOffset, MouseHandler.mouseY, 0);
                }
            }
        } else if (settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1) {
            final int yOffset = canvasWidth >= 1000 ? 37 : 74;
            if (MouseHandler.mouseX > canvasWidth - 197 && MouseHandler.mouseY > canvasHeight - yOffset - 267
                    && MouseHandler.mouseX < canvasWidth - 7 && MouseHandler.mouseY < canvasHeight - yOffset - 7
                    && showTabComponents) {
                if (overlayInterfaceId != -1) {
                    buildInterfaceMenu(canvasWidth - 197, Widget.cache[overlayInterfaceId], MouseHandler.mouseX,
                            canvasHeight - yOffset - 267, MouseHandler.mouseY, 0);
                } else if (tabInterfaceIDs[sidebarId] != -1) {
                    buildInterfaceMenu(canvasWidth - 197, Widget.cache[tabInterfaceIDs[sidebarId]],
                            MouseHandler.mouseX, canvasHeight - yOffset - 267, MouseHandler.mouseY, 0);
                }
            }
        }
        if (frameFocusedInterface != focusedSidebarWidget) {
            update_tab_producer = true;
            focusedSidebarWidget = frameFocusedInterface;
        }
        if (anInt1315 != tabTooltipSupportId) {
            update_tab_producer = true;
            tabTooltipSupportId = anInt1315;
        }

        // System.out.println(update_offset);

        // System.out.println(splitPrivateChatMessages.get(17).getMessage());
        /*
         * Constraint constraint = createPMConstraint();
         * if (constraint != null) {
         * if (MouseHandler.mouseX > constraint.getStartX() && MouseHandler.mouseY >
         * (!resized ? constraint.getStartY() : window_height - 187 - (update_offset *
         * 10)) && MouseHandler.mouseX < constraint.getEndX() && MouseHandler.mouseY <
         * (!resized ? constraint.getEndY() : window_height - 167)) {
         * if(interfaceOpen()) {
         * return;
         * }
         * menuActionRow = 1;
         * menuActionText[menuActionRow] = "Walk here";
         * menuActionTypes[menuActionRow] = 519;
         * firstMenuAction[menuActionRow] = MouseHandler.mouseX;
         * secondMenuAction[menuActionRow] = MouseHandler.mouseY;
         * menuActionRow++;
         * menuActionText[menuActionRow] = "Clear Private Messages";
         * menuActionTypes[menuActionRow] = 1895;
         * menuActionRow++;
         * }
         * }
         */

        if (broadcast != null) {
            broadcast.isHovered(MouseHandler.mouseX, MouseHandler.mouseY);
        }
        frameFocusedInterface = 0;
        anInt1315 = 0;

        if (MouseHandler.mouseX > 0 && MouseHandler.mouseY > (!resized ? 338 : canvasHeight - 165)
                && MouseHandler.mouseX < 490 && MouseHandler.mouseY < (!resized ? 463 : canvasHeight - 40)) {
            if (backDialogueId != -1) {
                buildInterfaceMenu(20, Widget.cache[backDialogueId], MouseHandler.mouseX,
                        (!resized ? 358 : canvasHeight - 145), MouseHandler.mouseY, 0);
            } else if (MouseHandler.mouseY < (!resized ? 463 : canvasHeight - 40) && MouseHandler.mouseX < 490) {
                buildChatAreaMenu(MouseHandler.mouseY - (!resized ? 338 : canvasHeight - 165));
            }
        }
        if (backDialogueId != -1 && frameFocusedInterface != focusedChatWidget) {
            update_chat_producer = true;
            focusedChatWidget = frameFocusedInterface;
        }
        if (backDialogueId != -1 && anInt1315 != chatTooltipSupportId) {
            update_chat_producer = true;
            chatTooltipSupportId = anInt1315;
        }
        if (MouseHandler.mouseX > 4 && MouseHandler.mouseY > 480 && MouseHandler.mouseX < 516
                && MouseHandler.mouseY < canvasHeight) {
            rightClickChatButtons();
        }
        processMinimapActions();
        boolean flag = false;
        while (!flag) {
            flag = true;
            for (int j = 0; j < menuActionRow - 1; j++) {
                if (menuActionTypes[j] < 1000 && menuActionTypes[j + 1] > 1000) {
                    String s = menuActionText[j];
                    menuActionText[j] = menuActionText[j + 1];
                    menuActionText[j + 1] = s;
                    int k = menuActionTypes[j];
                    menuActionTypes[j] = menuActionTypes[j + 1];
                    menuActionTypes[j + 1] = k;
                    k = firstMenuAction[j];
                    firstMenuAction[j] = firstMenuAction[j + 1];
                    firstMenuAction[j + 1] = k;
                    k = secondMenuAction[j];
                    secondMenuAction[j] = secondMenuAction[j + 1];
                    secondMenuAction[j + 1] = k;
                    long k2 = selectedMenuActions[j];
                    selectedMenuActions[j] = selectedMenuActions[j + 1];
                    selectedMenuActions[j + 1] = k2;
                    flag = false;
                }
            }
        }
    }

    private int method83(int i, int j, int k) {
        int l = 256 - k;
        return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00)
                + ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
    }

    private byte[] getMACAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] hwaddress = network.getHardwareAddress();
            return hwaddress == null ? new byte[0] : hwaddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String macAddress;
    public static SecureRandom secure_random;
    static SecureRandomFuture secure_future;

    /**
     * The login method for the 317 protocol.
     *
     * @param name         The name of the user trying to login.
     * @param password     The password of the user trying to login.
     * @param reconnecting The flag for the user indicating to attempt to reconnect.
     */
    public void login(String name, String password, boolean reconnecting) {
        setting.save();
        SignLink.setError(name);
        PacketBit secure_packet = SERVER_CONNECTION.buffer;
        try {
            if (name.length() < 1) {
                firstLoginMessage = "";
                secondLoginMessage = "Your username is too short.";
                return;
            }
            if (password.length() < 3) {
                firstLoginMessage = "";
                secondLoginMessage = "Your password is too short.";
                return;
            }
            if (!reconnecting) {
                firstLoginMessage = "";
                secondLoginMessage = "Connecting to server...";
                drawLoginScreen();
                setGameState(GameState.LOGGING_IN);
            }

            setGameState(GameState.LOGGING_IN);

            addReportToServer("Client loginStage: " + loginStage);

            if (secure_random == null && (secure_future.isDone())) {
                secure_random = secure_future.get();
                secure_future.shutdown();
                secure_future = null;
            }

            SERVER_SOCKET = new BufferedConnection(openSocket(ClientConstants.SERVER_PORT));
            SERVER_CONNECTION.set_socket(SERVER_SOCKET);
            TcpConnectionMessage tcpConnectionMessage;
            SERVER_CONNECTION.clear_queue();

            tcpConnectionMessage = TcpConnectionMessage.get_empty();
            tcpConnectionMessage.packet.put_byte(14);
            SERVER_CONNECTION.addNode(tcpConnectionMessage);
            SERVER_CONNECTION.flush();

            secure_packet.pos = 0;
            int response = SERVER_SOCKET.read();

            secure_packet.pos = 0;
            SERVER_SOCKET.flushInputStream(secure_packet.array, 8);

            secure_packet.pos = 0;
            serverSeed = secure_packet.get_long();
            System.out.println("serverSeed=" + serverSeed);

            secure_packet.pos = 0;
            SERVER_SOCKET.flushInputStream(secure_packet.array, 2);
            int powSize = secure_packet.get_unsignedshort();
            System.out.println("powSize=" + powSize);

            // watch how long it gonna take client to solve it XD

            PacketBuffer powBuffer = new PacketBuffer(40000);
            powBuffer.offset = 0;
            SERVER_SOCKET.read(powBuffer.array, powBuffer.offset, powSize);

            System.out.println("read " + powSize + " bytes...");

            int powType = powBuffer.readUnsignedByte();
            System.out.println("powType=" + powType);

            class7 powTask;
            switch (powType) {
                case 0:
                    class0 var32 = new class0();
                    powTask = new class7(powBuffer, var32);

                    // wait until the proof of work task is done
                    while (!powTask.method44()) {
                        Thread.yield();
                        // Thread.sleep(20);
                        // System.out.println("waiting for pow...");
                    }

                    jagex.Buffer powTaskResponse = powTask.method46();
                    powTask.method45(); // cleanup

                    tcpConnectionMessage = TcpConnectionMessage.get_empty();
                    tcpConnectionMessage.packet.put_short(powTaskResponse.offset);
                    tcpConnectionMessage.packet.put_bytes(powTaskResponse.array, 0, powTaskResponse.offset);
                    System.out.println("pow size: " + tcpConnectionMessage.packet.pos);
                    SERVER_CONNECTION.addNode(tcpConnectionMessage);
                    SERVER_CONNECTION.flush();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid proof of work type: " + powType);
            }
            SERVER_CONNECTION.buffer.pos = 0;
            SERVER_CONNECTION.clear_queue();
            Buffer rsa = new Buffer(500);
            int[] clientKeys = new int[] { secure_random.nextInt(), secure_random.nextInt(), secure_random.nextInt(),
                    secure_random.nextInt() };
            rsa.pos = 0;
            rsa.writeByte(10);
            rsa.writeInt(clientKeys[0]);
            rsa.writeInt(clientKeys[1]);
            rsa.writeInt(clientKeys[2]);
            rsa.writeInt(clientKeys[3]);
            rsa.writeLong(serverSeed);
            rsa.writeString(ClientConstants.CLIENT_VERSION);
            rsa.writeString(name);
            rsa.writeString(password);
            rsa.writeString(macAddress);
            rsa.writeString("");
            rsa.encryptRSAContent();
            TcpConnectionMessage login = TcpConnectionMessage.get_empty();
            login.packet.pos = 0;
            login.packet.put_byte(reconnecting ? 18 : 16);
            login.packet.put_byte(rsa.pos + 2);
            login.packet.put_byte(255);
            login.packet.put_byte(low_detail ? 1 : 0);
            login.packet.put_bytes(rsa.payload, 0, rsa.pos);
            SERVER_CONNECTION.addNode(login);
            SERVER_CONNECTION.flush();
            SERVER_CONNECTION.isaacCipher = new IsaacCipher(clientKeys);
            int[] serverKeys = new int[4];
            for (int index = 0; index < 4; index++)
                serverKeys[index] += 50 + clientKeys[index];
            encryption = new IsaacCipher(serverKeys);
            secure_packet.set_seed(serverKeys);
            response = SERVER_SOCKET.read();
            addReportToServer("SuccesFull Login: " + loginStage);

            int copy = response;

            if (response == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                login(name, password, reconnecting);
                return;
            }

            if (response == 2) {
                accountHash = longForName(myUsername) + longForName(myPassword);
                int rights = myPrivilege;
                rights = SERVER_SOCKET.read();
                lastKeyboardEventTime = 0L;
                recentIncomingPrivateChatUserList = new String[10];
                setGameState(GameState.LOGGING_IN);
                hadFocus = true;
                loggedIn = true;
                loggedInWatch.reset();
                Widget.cache[12697].defaultText = "Drag setting: <col=ffffff>" + setting.drag_item_value
                        + (setting.drag_item_value == 5 ? " (OSRS)"
                                : setting.drag_item_value == 10 ? " (Pre-EOC)" : " (Custom)");
                packetSender = new PacketSender(SERVER_CONNECTION.isaacCipher);
                incoming.pos = 0;
                opcode = -1;
                lastOpcode = -1;
                secondLastOpcode = -1;
                thirdLastOpcode = -1;
                currentSkill = -1; // loading shit here
                packetSize = 0;
                timeoutCounter = 0;
                rebootTimer = 0;
                logoutTimer = 0;
                hintIconDrawType = 0;
                totalExperience = 0L;
                menuActionRow = 0;
                isMenuOpen = false;
                MouseHandler.idleCycles = 0;
                KeyHandler.idleCycles = 0;
                for (int index = 0; index < 100; index++)
                    chatMessages[index] = null;
                item_highlighted = 0;
                widget_highlighted = 0;

                set_camera_north();
                minimapState = ClientConstants.SHOW_MINIMAP;
                lastKnownPlane = -1;
                travel_destination_x = 0;
                travel_destination_y = 0;
                players_in_region = 0;
                npcs_in_region = 0;
                for (int index = 0; index < maxPlayers; index++) {
                    players[index] = null;
                    playerSynchronizationBuffers[index] = null;
                }
                for (int index = 0; index < maxNpcs; index++) {
                    npcs[index] = null;
                }
                local_player = players[LOCAL_PLAYER_INDEX] = new Player();
                local_player.index = LOCAL_PLAYER_INDEX;
                projectiles.clear();
                incompleteAnimables.clear();
                clearRegionalSpawns();
                fullscreenInterfaceID = -1;
                friendServerStatus = 0;
                friendsCount = 0;
                dialogueId = -1;
                backDialogueId = -1;
                widget_overlay_id = -1;
                overlayInterfaceId = -1;
                openWalkableInterface = -1;
                continuedDialogue = false;
                sidebarId = 3;
                inputDialogState = 0;
                isMenuOpen = false;
                messagePromptRaised = false;
                clickToContinueString = null;
                multicombat = 0;
                flashingSidebarId = -1;
                characterGender = true;
                resetCharacterCreation();
                for (int index = 0; index < 5; index++)
                    characterDesignColours[index] = 0;
                for (int index = 0; index < 5; index++) {
                    playerOptions[index] = null;
                    playerOptionsHighPriority[index] = false;
                }
                anInt1175 = 0;
                anInt1134 = 0;
                anInt986 = 0;
                current_walking_queue_length = 0;
                anInt924 = 0;
                anInt1188 = 0;
                anInt1155 = 0;
                anInt1226 = 0;
                regenHealthStart = System.currentTimeMillis();
                regenSpecStart = System.currentTimeMillis();
                loginTime = System.currentTimeMillis();
                darkenEnabled = false;
                darkenOpacity = 0;
                this.stopMidi();
                Rasterizer2D.Rasterizer2D_clear();
                jagexNetThread.writePacket(false);
                drawChatArea();
                drawMinimap();
                drawTabArea();
                setGameState(GameState.LOGGED_IN);
                return;
            }

            if (response == 97) {
                firstLoginMessage = "That username is invalid.";
                secondLoginMessage = "Please try another username.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }

            if (response == 98) {
                firstLoginMessage = "The server is currently under maintenance.";
                secondLoginMessage = "Please try again later.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }

            if (response == 28) {
                firstLoginMessage = "Username or password contains illegal";
                secondLoginMessage = "characters. Try other combinations.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 30) {
                // firstLoginMessage = "Old client usage detected.";
                // secondLoginMessage = "Please download the latest one.";
                firstLoginMessage = "An updated client is available. Please restart the";
                secondLoginMessage = "launcher";
                // Utils.launchURL(ClientConstants.DOWNLOAD_URL);
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 3) {
                firstLoginMessage = "";
                secondLoginMessage = "Invalid username or password.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 4) {
                firstLoginMessage = "Your account has been banned.";
                secondLoginMessage = "Please check the website for more details.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 22) {
                firstLoginMessage = "Your computer has been banned.";
                secondLoginMessage = "";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 27) {
                firstLoginMessage = "Your host-address has been banned.";
                secondLoginMessage = "";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 5) {
                firstLoginMessage = "Your account is already logged in.";
                secondLoginMessage = "Try again in 60 seconds...";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 6) {
                firstLoginMessage = ClientConstants.CLIENT_NAME + " is being updated.";
                secondLoginMessage = "Try again in 60 seconds...";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 7) {
                firstLoginMessage = "The world is currently full.";
                secondLoginMessage = "";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 8) {
                firstLoginMessage = "Unable to connect.";
                secondLoginMessage = "Login server offline.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 9) {
                firstLoginMessage = "Login limit exceeded.";
                secondLoginMessage = "Too many connections from your address.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 10) {
                firstLoginMessage = "Unable to connect. Bad session id.";
                secondLoginMessage = "Try again in 60 secs...";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 11) {
                secondLoginMessage = "Login server rejected session.";
                secondLoginMessage = "Try again in 60 secs...";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 12) {
                firstLoginMessage = "You need a members account to login to this world.";
                secondLoginMessage = "Please subscribe, or use a different world.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 13) {
                firstLoginMessage = "Could not complete login.";
                secondLoginMessage = "Please check the Discord server.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 14) {
                firstLoginMessage = "The server is being updated.";
                secondLoginMessage = "Please wait 1 minute and try again.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 15) {
                loggedIn = true;
                incoming.pos = 0;
                opcode = -1;
                lastOpcode = -1;
                secondLastOpcode = -1;
                thirdLastOpcode = -1;
                packetSize = 0;
                timeoutCounter = 0;
                rebootTimer = 0;
                menuActionRow = 0;
                isMenuOpen = false;
                loadingStartTime = System.currentTimeMillis();
                setGameState(GameState.LOGGED_IN);
                return;
            }
            if (response == 16) {
                firstLoginMessage = "Login attempts exceeded.";
                secondLoginMessage = "Please wait 1 minute and try again.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 17) {
                firstLoginMessage = "You are standing in a members-only area.";
                secondLoginMessage = "To play on this world move to a free area first";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 20) {
                firstLoginMessage = "Invalid loginserver requested";
                secondLoginMessage = "Please try using a different world.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == 21) {
                for (int k1 = SERVER_SOCKET.read(); k1 >= 0; k1--) {
                    firstLoginMessage = "You have only just left another world";
                    secondLoginMessage = "Your profile will be transferred in: " + k1 + " seconds";
                    drawLoginScreen(true);
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                login(name, password, reconnecting);
                return;
            }
            if (response == 22) {
                firstLoginMessage = "Your computer has been UUID banned.";
                secondLoginMessage = "Please appeal on the forums.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
            if (response == -1) {
                if (copy == 0) {
                    if (loginFailures < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        loginFailures++;
                        login(name, password, reconnecting);
                        return;
                    } else {
                        firstLoginMessage = "No response from loginserver";
                        secondLoginMessage = "Please wait 1 minute and try again.";
                        return;
                    }
                } else {
                    firstLoginMessage = "No response from server";
                    secondLoginMessage = "Please try using a different world.";
                    return;
                }
            } else {
                firstLoginMessage = "Unexpected server response";
                secondLoginMessage = "Please try using a different world.";
                setGameState(GameState.LOGIN_SCREEN);
                return;
            }
        } catch (IOException _ex) {
            _ex.printStackTrace();
            firstLoginMessage = "";
            setGameState(GameState.LOGIN_SCREEN);
        } catch (Exception e) {
            addReportToServer("Error while generating uid. Skipping step.");
            e.printStackTrace();
            addReportToServer(e.getMessage());
            setGameState(GameState.LOGIN_SCREEN);
        }
        // addReportToServer("Cannot connect to server, IP is " +
        // ClientConstants.SERVER_ADDRESS + " with port " +
        // ClientConstants.SERVER_PORT);
        secondLoginMessage = "Error connecting to server.";
        setGameState(GameState.LOGIN_SCREEN);
    }

    private void clearRegionalSpawns() {
        for (int plane = 0; plane < 4; plane++) {
            for (int x = 0; x < 104; x++) {
                for (int y = 0; y < 104; y++) {
                    scene_items[plane][x][y] = null;
                }
            }
        }
        if (spawns == null) {
            spawns = new NodeDeque();
        }
        for (SpawnedObject object = (SpawnedObject) spawns
                .first(); object != null; object = (SpawnedObject) spawns.next())
            object.getLongetivity = 0;
    }

    static class Tile {
        public int x, y, level;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public int distance(Tile other, Tile from) {
        int deltaX = other.x - from.x, deltaY = other.y - from.y;
        double dis = Math.sqrt(Math.pow(deltaX, 2D) + Math.pow(deltaY, 2D));
        if (dis > 1.0 && dis < 2)
            return 2;
        return (int) dis;
    }

    private boolean walk(int opcode, int obstruction_orientation, int obstruction_height, int obstruction_type,
            int local_y_path, int obstruction_width, int orientation_mask, int path_to_y_position, int local_x_path,
            boolean minimap_click, int path_to_x_position) {
        // logging.log(Level.INFO, String.format("Walking distance %s from %s,%s to
        // %s,%s,%s%n", distance(new Tile(local_x_path, local_y_path), new
        // Tile(path_to_x_position, path_to_y_position)), local_x_path, local_y_path,
        // path_to_x_position, path_to_y_position, obstruction_height));

        // System.out.printf("click %s %s, %s %s, %s %s, %s%n", SceneGraph.clickedTileX,
        // SceneGraph.clickedTileY, path_to_x_position, path_to_y_position,
        // next_region_start, next_region_end, plane);
        if (/* Client.singleton.getMyPrivilege() == 5 && */ Client.isShiftPressed && minimap_click
                && ClientConstants.SHIFT_CLICK_TELEPORT) { // skip pathfinding
            Client.teleport(next_region_start + path_to_x_position, next_region_end + path_to_y_position, plane & 3);
            return false;
        }
        // teleport(next_region_start + i2, next_region_end + j2, plane);
        try {
            byte region_x = 104;
            byte region_y = 104;
            for (int x = 0; x < region_x; x++) {
                for (int y = 0; y < region_y; y++) {
                    waypoints[x][y] = 0;
                    travel_distances[x][y] = 0x5f5e0ff;// 99999999
                }
            }
            int x = local_x_path;
            int y = local_y_path;
            waypoints[local_x_path][local_y_path] = 99;
            travel_distances[local_x_path][local_y_path] = 0;
            int next_pos = 0;
            int current_pos = 0;
            walking_queue_x[next_pos] = local_x_path;
            walking_queue_y[next_pos++] = local_y_path;
            boolean reached = false;
            int path_length = walking_queue_x.length;
            int[][] adjacencies = Client.instance.noclip ? new int[104][104] : collisionMaps[plane].adjacencies;

            while (current_pos != next_pos) {
                x = walking_queue_x[current_pos];
                y = walking_queue_y[current_pos];
                current_pos = (current_pos + 1) % path_length;
                if (x == path_to_x_position && y == path_to_y_position) {
                    reached = true;
                    break;
                }
                if (obstruction_type != 0) {
                    if ((obstruction_type < 5 || obstruction_type == 10)
                            && collisionMaps[plane].obstruction_wall(path_to_x_position, x, y,
                                    obstruction_orientation, obstruction_type - 1, path_to_y_position)) {
                        reached = true;
                        break;
                    }
                    if (obstruction_type < 10 && collisionMaps[plane].obstruction_decor(path_to_x_position,
                            path_to_y_position, y, obstruction_type - 1, obstruction_orientation, x)) {
                        reached = true;
                        break;
                    }
                }
                if (obstruction_width != 0 && obstruction_height != 0
                        && collisionMaps[plane].obstruction(path_to_y_position, path_to_x_position, x,
                                obstruction_height, orientation_mask, obstruction_width, y)) {
                    reached = true;
                    break;
                }
                int updated_distance = travel_distances[x][y] + 1;
                if (x > 0 && waypoints[x - 1][y] == 0
                        && (adjacencies[x - 1][y] & 0x1280108) == 0) {
                    walking_queue_x[next_pos] = x - 1;
                    walking_queue_y[next_pos] = y;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x - 1][y] = 2;
                    travel_distances[x - 1][y] = updated_distance;
                }
                if (x < region_x - 1 && waypoints[x + 1][y] == 0 && (adjacencies[x + 1][y] & 0x1280180) == 0) {
                    walking_queue_x[next_pos] = x + 1;
                    walking_queue_y[next_pos] = y;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x + 1][y] = 8;
                    travel_distances[x + 1][y] = updated_distance;
                }
                if (y > 0 && waypoints[x][y - 1] == 0 && (adjacencies[x][y - 1] & 0x1280102) == 0) {
                    walking_queue_x[next_pos] = x;
                    walking_queue_y[next_pos] = y - 1;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x][y - 1] = 1;
                    travel_distances[x][y - 1] = updated_distance;
                }
                if (y < region_y - 1 && waypoints[x][y + 1] == 0 && (adjacencies[x][y + 1] & 0x1280120) == 0) {
                    walking_queue_x[next_pos] = x;
                    walking_queue_y[next_pos] = y + 1;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x][y + 1] = 4;
                    travel_distances[x][y + 1] = updated_distance;
                }
                if (x > 0 && y > 0 && waypoints[x - 1][y - 1] == 0
                        && (adjacencies[x - 1][y - 1] & 0x128010e) == 0
                        && (adjacencies[x - 1][y] & 0x1280108) == 0
                        && (adjacencies[x][y - 1] & 0x1280102) == 0) {
                    walking_queue_x[next_pos] = x - 1;
                    walking_queue_y[next_pos] = y - 1;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x - 1][y - 1] = 3;
                    travel_distances[x - 1][y - 1] = updated_distance;
                }
                if (x < region_x - 1 && y > 0 && waypoints[x + 1][y - 1] == 0
                        && (adjacencies[x + 1][y - 1] & 0x1280183) == 0
                        && (adjacencies[x + 1][y] & 0x1280180) == 0
                        && (adjacencies[x][y - 1] & 0x1280102) == 0) {
                    walking_queue_x[next_pos] = x + 1;
                    walking_queue_y[next_pos] = y - 1;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x + 1][y - 1] = 9;
                    travel_distances[x + 1][y - 1] = updated_distance;
                }
                if (x > 0 && y < region_y - 1 && waypoints[x - 1][y + 1] == 0
                        && (adjacencies[x - 1][y + 1] & 0x1280138) == 0
                        && (adjacencies[x - 1][y] & 0x1280108) == 0
                        && (adjacencies[x][y + 1] & 0x1280120) == 0) {
                    walking_queue_x[next_pos] = x - 1;
                    walking_queue_y[next_pos] = y + 1;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x - 1][y + 1] = 6;
                    travel_distances[x - 1][y + 1] = updated_distance;
                }
                if (x < region_x - 1 && y < region_y - 1 && waypoints[x + 1][y + 1] == 0
                        && (adjacencies[x + 1][y + 1] & 0x12801e0) == 0
                        && (adjacencies[x + 1][y] & 0x1280180) == 0
                        && (adjacencies[x][y + 1] & 0x1280120) == 0) {
                    walking_queue_x[next_pos] = x + 1;
                    walking_queue_y[next_pos] = y + 1;
                    next_pos = (next_pos + 1) % path_length;
                    waypoints[x + 1][y + 1] = 12;
                    travel_distances[x + 1][y + 1] = updated_distance;
                }
            }
            destination_mask = 0;
            if (!reached) {
                if (minimap_click) { // minimap has clipping checks
                    int steps = 100;
                    for (int deviation_offset = 1; deviation_offset < 2; deviation_offset++) {
                        for (int deviation_x = path_to_x_position - deviation_offset; deviation_x <= path_to_x_position
                                + deviation_offset; deviation_x++) {
                            for (int deviation_y = path_to_y_position
                                    - deviation_offset; deviation_y <= path_to_y_position
                                            + deviation_offset; deviation_y++) {
                                if (deviation_x >= 0 && deviation_y >= 0 && deviation_x < 104 && deviation_y < 104
                                        && travel_distances[deviation_x][deviation_y] < steps) {
                                    steps = travel_distances[deviation_x][deviation_y];
                                    x = deviation_x;
                                    y = deviation_y;
                                    destination_mask = 1;
                                    reached = true;
                                }
                            }
                        }
                        if (reached)
                            break;
                    }
                }
                if (!reached) {
                    // dont send walk packet when pathfinding fails
                    return false;
                }
            }
            current_pos = 0;
            walking_queue_x[current_pos] = x;
            walking_queue_y[current_pos++] = y;
            int skip;
            for (int waypoint = skip = waypoints[x][y]; x != local_x_path
                    || y != local_y_path; waypoint = waypoints[x][y]) {
                if (waypoint != skip) {
                    skip = waypoint;
                    walking_queue_x[current_pos] = x;
                    walking_queue_y[current_pos++] = y;
                }
                if ((waypoint & 2) != 0)
                    x++;
                else if ((waypoint & 8) != 0)
                    x--;
                if ((waypoint & 1) != 0)
                    y++;
                else if ((waypoint & 4) != 0)
                    y--;
            }
            if (current_pos > 0) {
                int max_path = current_pos;
                if (max_path > 25)
                    max_path = 25;
                current_pos--;
                int walking_x = walking_queue_x[current_pos];
                int walking_y = walking_queue_y[current_pos];
                current_walking_queue_length += max_path;
                if (current_walking_queue_length >= 92) {
                    /*
                     * Anti-cheatValidates, walking. Not used. OUTPUT_BUFFER.createFrame(36);
                     * OUTPUT_BUFFER.writeDWord(0);
                     */
                    current_walking_queue_length = 0;
                }
                if (opcode == 0) {
                    packetSender.getBuffer().writeOpcode(164);
                    packetSender.getBuffer().writeByte(max_path + max_path + 4);
                } else if (opcode == 1) {
                    packetSender.getBuffer().writeOpcode(248);
                    packetSender.getBuffer().writeByte(max_path + max_path + 4);
                } else if (opcode == 2) {
                    packetSender.getBuffer().writeOpcode(98);
                    packetSender.getBuffer().writeByte(max_path + max_path + 4);
                }
                packetSender.getBuffer().writeByte(plane);
                packetSender.getBuffer().writeLEShortA(walking_x + next_region_start);
                travel_destination_x = walking_queue_x[0];
                travel_destination_y = walking_queue_y[0];
                for (int step = 1; step < max_path; step++) {
                    current_pos--;
                    packetSender.getBuffer()
                            .writeByte(walking_queue_x[current_pos] - walking_x);
                    packetSender.getBuffer()
                            .writeByte(walking_queue_y[current_pos] - walking_y);
                }
                packetSender.getBuffer().writeLEShort(walking_y + next_region_end);
                packetSender.getBuffer()
                        .writeNegatedByte(!isShiftPressed ? 0 : 1); // shift click teleporting
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
        return opcode != 1;
    }

    private void npcUpdateMask(Buffer stream) {
        for (int j = 0; j < mobsAwaitingUpdateCount; j++) {
            int k = mobsAwaitingUpdate[j];
            Npc npc = npcs[k];
            int mask = stream.readUnsignedByte();
            if ((mask & 0x10) != 0) {
                int i1 = stream.get_unsignedshort_le();
                if (i1 == 65535)
                    i1 = -1;
                int i2 = stream.readUnsignedByte();
                if (i1 == npc.sequence && i1 != -1) {
                    int l2 = SequenceDefinition.get(i1).delayType;
                    if (l2 == 1) {
                        npc.sequenceFrame = 0;
                        npc.sequenceFrameCycle = 0;
                        npc.sequenceDelay = i2;
                        npc.currentAnimationLoops = 0;
                    }
                    if (l2 == 2)
                        npc.currentAnimationLoops = 0;
                } else if (i1 == -1 || npc.sequence == -1
                        || SequenceDefinition.get(i1).forcedPriority >= SequenceDefinition
                                .get(npc.sequence).forcedPriority) {
                    npc.sequence = i1;
                    npc.sequenceFrame = 0;
                    npc.sequenceFrameCycle = 0;
                    npc.sequenceDelay = i2;
                    npc.currentAnimationLoops = 0;
                    npc.anim_delay = npc.pathLength;
                }
            }
            if ((mask & 0x80) != 0) {
                npc.readSpotAnimation(stream);
            }
            if ((mask & 8) != 0) {
                int count = stream.readUnsignedByte();
                for (int i = 0; i < count; i++) {
                    int damage = stream.readShort();
                    int type = stream.readUnsignedByte();
                    int hp = stream.readShort();
                    int maxHp = stream.readShort();
                    npc.updateHitData(type, damage, tick);
                    npc.game_tick_status = tick + 300;
                    npc.current_hitpoints = hp;
                    npc.maximum_hitpoints = maxHp;
                }
            }
            if ((mask & 0x20) != 0) {
                npc.targetIndex = stream.readUnsignedShort();
                if (npc.targetIndex == 65535)
                    npc.targetIndex = -1;
            }
            if ((mask & 1) != 0) {
                npc.spokenText = stream.readString();
                npc.message_cycle = 100;
            }
            if ((mask & 0x40) != 0) {
                npc.recolourStartCycle = tick + stream.readShort();
                npc.recolourEndCycle = tick + stream.readShort();
                npc.recolorHue = (byte) stream.readUnsignedByte();
                npc.recolourSaturation = (byte) stream.readUnsignedByte();
                npc.recolourLuminance = (byte) stream.readUnsignedByte();
                npc.recolourAmount = (byte) stream.readUnsignedByte();
            }
            /*
             * if ((mask & 0x160) != 0) {
             * int initialX = stream.readUByteS();
             * int initialY = stream.readUByteS();
             * int destinationX = stream.readUByteS();
             * int destinationY = stream.readUByteS();
             * int startForceMovement = stream.readLEUShortA() + game_tick;
             * int endForceMovement = stream.readUShortA() + game_tick;
             * int animation = stream.readLEUShortA();
             * int direction = stream.readUByteS();
             * 
             * npc.initialX = initialX;
             * npc.initialY = initialY;
             * npc.destinationX = destinationX;
             * npc.destinationY = destinationY;
             * npc.initiate_movement = startForceMovement;
             * npc.cease_movement = endForceMovement;
             * npc.direction = direction;
             * 
             * if (animation >= 0) {
             * npc.primarySeqID = animation;
             * npc.primarySeqFrame = 0;
             * npc.primarySeqCycle = 0;
             * npc.animation_loops = 0;
             * npc.remaining_steps = npc.waypoint_index;
             * }
             * npc.resetPath();
             * }
             */
            if ((mask & 0x2) != 0) {// Transform
                npc.definition = NpcDefinition.get(stream.get_unsignedshort_le());
                npc.size = npc.definition.size;
                npc.rotation = npc.definition.rotationSpeed;
                npc.walkSequence = npc.definition.walkingAnimation;
                npc.walkBackSequence = npc.definition.rotate180Animation;
                npc.walkLeftSequence = npc.definition.rotate90LeftAnimation;
                npc.walkRightSequence = npc.definition.rotate90RightAnimation;
                npc.idleSequence = npc.definition.standingAnimation;
            }
            if ((mask & 4) != 0) {
                npc.faceX = stream.get_unsignedshort_le();
                npc.faceY = stream.get_unsignedshort_le();
            }
        }
    }

    private void buildAtNPCMenu(NpcDefinition npcDefinition, int npcIndex, int j, int npcArrayIndex) {
        if (widget_overlay_id == 16244) {
            return;
        }
        if (menuActionRow >= 400)
            return;
        if (npcDefinition.getTransforms() != null) {
            npcDefinition = npcDefinition.get_configs();
        }

        if (npcDefinition == null) {
            return;
        }

        String name = npcDefinition.getName();

        if (npcDefinition.getCombatLevel() != 0)
            name = name + get_level_diff(local_player.combat_level, npcDefinition.getCombatLevel()) + " (level-"
                    + npcDefinition.getCombatLevel() + ")";

        if (item_highlighted == 1) {
            menuActionText[menuActionRow] = "Use " + selectedItemName + " with <col=ffff00>" + name;
            menuActionTypes[menuActionRow] = 582;
            selectedMenuActions[menuActionRow] = npcIndex;
            firstMenuAction[menuActionRow] = npcArrayIndex;
            secondMenuAction[menuActionRow] = j;
            menuActionRow++;
            return;
        }

        if (widget_highlighted == 1) {
            if ((selectedTargetMask & 2) == 2) {
                // Stop the player from being able to use a spell on his own pet.
                if (npcPetId == npcArrayIndex) {
                    return;
                }

                menuActionText[menuActionRow] = selected_target_id + " <col=ffff00>" + name;
                menuActionTypes[menuActionRow] = 413;
                selectedMenuActions[menuActionRow] = npcIndex;
                firstMenuAction[menuActionRow] = npcArrayIndex;
                secondMenuAction[menuActionRow] = j;
                menuActionRow++;
            }
        } else {
            boolean isPet = npcDefinition.isPet();
            if (npcDefinition.getActions() != null) {
                for (int l = 4; l >= 0; l--) {
                    // Is not an attack option.
                    if (npcDefinition.getActions()[l] != null
                            && !npcDefinition.getActions()[l].equalsIgnoreCase("attack")) {
                        if (npcDefinition.getActions()[l].contains("Pick-up")) {
                            isPet = true;
                        }
                        char c = '\0';
                        if (isPet && setting.shift_pet_options) {
                            c = '\u07D0';
                        }
                        menuActionText[menuActionRow] = npcDefinition.actions[l] + " <col=ffff00>" + name;
                        if (l == 0)
                            menuActionTypes[menuActionRow] = 20 + c;
                        if (l == 1)
                            menuActionTypes[menuActionRow] = 412 + c;
                        if (l == 2)
                            menuActionTypes[menuActionRow] = 225 + c;
                        if (l == 3)
                            menuActionTypes[menuActionRow] = 965 + c;
                        if (l == 4)
                            menuActionTypes[menuActionRow] = 478 + c;
                        selectedMenuActions[menuActionRow] = npcIndex;
                        firstMenuAction[menuActionRow] = npcArrayIndex;
                        secondMenuAction[menuActionRow] = j;
                        menuActionRow++;
                    }
                }
            }
            if (npcDefinition.getActions() != null) {
                for (int i1 = 4; i1 >= 0; i1--) {
                    if (npcDefinition.getActions()[i1] != null
                            && npcDefinition.getActions()[i1].equalsIgnoreCase("attack")) {
                        char c = '\0';
                        if (setting.npc_attack_priority == 0) {
                            if (npcDefinition.getCombatLevel() > local_player.combat_level)
                                c = '\u07D0';
                        } else if (setting.npc_attack_priority == 1) {
                            c = '\u07D0';
                        } else if (setting.npc_attack_priority == 3) {
                            continue;
                        }
                        menuActionText[menuActionRow] = npcDefinition.actions[i1] + " <col=ffff00>" + name;
                        if (i1 == 0)
                            menuActionTypes[menuActionRow] = 20 + c;
                        if (i1 == 1)
                            menuActionTypes[menuActionRow] = 412 + c;
                        if (i1 == 2)
                            menuActionTypes[menuActionRow] = 225 + c;
                        if (i1 == 3)
                            menuActionTypes[menuActionRow] = 965 + c;
                        if (i1 == 4)
                            menuActionTypes[menuActionRow] = 478 + c;
                        selectedMenuActions[menuActionRow] = npcIndex;
                        firstMenuAction[menuActionRow] = npcArrayIndex;
                        secondMenuAction[menuActionRow] = j;
                        menuActionRow++;
                    }
                }
            }
            if (ClientConstants.DEBUG_MODE) {
                menuActionText[menuActionRow] = "Examine <col=ffff00>" + name + ", " + npcDefinition.getId();
            } else {
                menuActionText[menuActionRow] = "Examine <col=ffff00>" + name;
            }
            menuActionTypes[menuActionRow] = 1025;
            selectedMenuActions[menuActionRow] = npcIndex;
            firstMenuAction[menuActionRow] = npcArrayIndex;
            secondMenuAction[menuActionRow] = j;
            menuActionRow++;
        }
    }

    public static String colorStartTag(int arg0) {
        return "<col=" + Integer.toHexString(arg0) + ">";
    }

    private void buildAtPlayerMenu(int i, int j, Player player, int k) {
        if (widget_overlay_id == 16244) {
            return;
        }
        if (menuActionRow >= 400)
            return;
        if (player != local_player) {
            String s;
            List<ChatCrown> crowns = ChatCrown.get(player.rights, player.donatorRights, player.ironmanRights);
            StringBuilder crownPrefix = new StringBuilder();
            for (ChatCrown c : crowns) {
                crownPrefix.append("<img=").append(c.getSpriteId()).append(">");
            }

            if (player.skill_level == 0)
                s = player.getTitle(true) + crownPrefix + player.username
                        + get_level_diff(local_player.combat_level, player.combat_level) + " (level-"
                        + player.combat_level + ")";
            else
                s = player.getTitle(true) + crownPrefix + player.username + " (skill-" + player.skill_level + ")";
            if (item_highlighted == 1) {
                menuActionText[menuActionRow] = "Use " + selectedItemName + " with <col=FFFFFF>" + s;
                menuActionTypes[menuActionRow] = 491;
                selectedMenuActions[menuActionRow] = j;
                firstMenuAction[menuActionRow] = i;
                secondMenuAction[menuActionRow] = k;
                menuActionRow++;
            } else if (widget_highlighted == 1) {
                if ((selectedTargetMask & 8) == 8) {
                    menuActionText[menuActionRow] = selected_target_id + " <col=FFFFFF>" + s;
                    menuActionTypes[menuActionRow] = 365;
                    selectedMenuActions[menuActionRow] = j;
                    firstMenuAction[menuActionRow] = i;
                    secondMenuAction[menuActionRow] = k;
                    menuActionRow++;
                }
            } else {
                for (int type = 4; type >= 0; type--) {
                    if (playerOptions[type] != null) {
                        menuActionText[menuActionRow] = playerOptions[type] + " <col=FFFFFF>" + s;
                        char c = '\0';
                        if (playerOptions[type].equalsIgnoreCase("attack")) {

                            if (setting.player_attack_priority == 0) {
                                if (player.combat_level > local_player.combat_level)
                                    c = '\u07D0';
                            } else if (setting.player_attack_priority == 1) {
                                c = '\u07D0';
                            } else if (setting.player_attack_priority == 3) {
                                continue;
                            }

                            boolean clanMember = false;

                            for (String clan : clanList) {
                                if (clan == null) {
                                    continue;
                                }
                                if (!clan.equalsIgnoreCase(player.username)) {
                                    continue;
                                }
                                clanMember = true;
                                break;
                            }

                            if (local_player.team_id != 0 && player.team_id != 0)
                                if (local_player.team_id == player.team_id) {
                                    c = '\u07D0';
                                } else {
                                    c = '\0';
                                }

                            if (clanMember) {
                                c = '\u07D0';
                            }
                        } else if (playerOptionsHighPriority[type])
                            c = '\u07D0';
                        if (type == 0) {
                            menuActionTypes[menuActionRow] = 561 + c;
                        }
                        if (type == 1) {
                            menuActionTypes[menuActionRow] = 779 + c;
                        }
                        if (type == 2) {
                            menuActionTypes[menuActionRow] = 27 + c;
                        }
                        if (type == 3) {
                            menuActionTypes[menuActionRow] = 577 + c;
                        }
                        if (type == 4) {
                            menuActionTypes[menuActionRow] = 729 + c;
                        }
                        selectedMenuActions[menuActionRow] = j;
                        firstMenuAction[menuActionRow] = i;
                        secondMenuAction[menuActionRow] = k;
                        menuActionRow++;
                    }
                }
            }
            for (int row = 0; row < menuActionRow; row++) {
                if (menuActionTypes[row] == 519) {
                    menuActionText[row] = "Walk here <col=FFFFFF>" + s;
                    return;
                }
            }
        }
    }

    private void handleTemporaryObjects(SpawnedObject spawnedObject) {
        long id = 0L;
        int key = -1;
        int type = 0;
        int orientation = 0;
        if (spawnedObject.group == 0)
            id = scene.getBoundaryObjectTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
        if (spawnedObject.group == 1)
            id = scene.getWallDecorationTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
        if (spawnedObject.group == 2)
            id = scene.getGameObjectTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
        if (spawnedObject.group == 3)
            id = scene.getFloorDecorationTag(spawnedObject.plane, spawnedObject.x, spawnedObject.y);
        if (id != 0L) {
            int flags = scene.getObjectFlags(spawnedObject.plane, spawnedObject.x, spawnedObject.y, id);
            key = ObjectKeyUtil.getObjectId(id);
            type = ObjectKeyUtil.getObjectType(flags);
            orientation = ObjectKeyUtil.getObjectOrientation(flags);
        }
        spawnedObject.getPreviousId = key;
        spawnedObject.previousType = type;
        spawnedObject.previousOrientation = orientation;
    }

    public static int[] removeDuplicates(int[] arr) {
        int end = arr.length;

        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (arr[i] == arr[j]) {
                    arr[j] = arr[end - 1];
                    end--;
                    j--;
                }
            }
        }

        int[] whitelist = new int[end];
        System.arraycopy(arr, 0, whitelist, 0, end);
        return whitelist;
    }

    public volatile Archive config_archive = null;

    private void setStartupActions() {
        try {
            long starting_up_start_time = System.currentTimeMillis();

            // playermodel_palette_recol_src = PlayerPalette.body_parts_recol_src;
            // playermodel_palette_recol_dst = PlayerPalette.body_parts_recol_dst;
            // playermodel_palette_recol_unk_src = PlayerPalette.field3621;
            // playermodel_palette_recol_unk_dst = PlayerPalette.field3623;
            try {
                HoverMenuManager.init();
            } catch (Exception e) {
                e.printStackTrace();
            }

            drawLogo();
            loadTitleScreen();

            long starting_up_time_elapsed = System.currentTimeMillis() - starting_up_start_time;
            if (ClientConstants.DISPLAY_CLIENT_LOAD_TIME_VERBOSE) {
                System.out.println("It took " + starting_up_time_elapsed + " ms to start up the client.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Archive mediaStreamLoader;
    public Archive interface_archive;

    private Archive loadArchive(String name) throws IOException {
        int id = 0;
        if (name == "media.dat") {
            id = 1;
        } else if (name == "interface.dat") {
        }
        Archive streamLoader = new Archive(Js5List.configs.takeFile(Js5ConfigType.CUSTOM_DATA, id));
        return streamLoader;
    }

    private void unpackingMedia() {
        try {
            System.out.println("DEBUG: unpackingMedia started");
            long unpacking_media_start_time = System.currentTimeMillis();
            drawLoadingText(70, "Loading sprites");
            this.mediaStreamLoader = loadArchive("media.dat");
            System.out.println("DEBUG: mediaStreamLoader loaded: " + (this.mediaStreamLoader != null));

            backgroundFix = SpriteCache.get(1847);
            System.out.println("DEBUG: backgroundFix loaded: "
                    + (backgroundFix != null ? backgroundFix.width + "x" + backgroundFix.height : "null"));

            accountManager = new AccountManager(this, SpriteCache.get(1850));
            accountManager.loadAccounts();
            saveButton = SpriteCache.get(1851);

            for (int imageId = 82, index = 0; index < SkillConstants.SKILL_COUNT; imageId++, index++) {
                skill_sprites[index] = SpriteCache.get(imageId);
            }

            for (int index = 0; index < fadingScreenImages.length; index++) {
                fadingScreenImages[index] = new SimpleImage("fadingscreen/" + (index + 1));
            }

            try {
                for (int index = 0; index < hitMarks.length; index++) {
                    hitMarks[index] = new SimpleImage(mediaStreamLoader, "hitmarks", index);
                }
                System.out.println("Loaded " + hitMarks.length + " hitmarks loading OSRS version "
                        + ClientConstants.OSRS_DATA_VERSION + " and SUB version "
                        + ClientConstants.OSRS_DATA_SUB_VERSION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Sprites from the media cache
            multiOverlay = new SimpleImage(mediaStreamLoader, "overlay_multiway", 0);
            mapBack = new IndexedImage(mediaStreamLoader, "mapback", 0);
            compass = new SimpleImage(mediaStreamLoader, "compass", 0);
            leftFrame = new SimpleImage(mediaStreamLoader, "screenframe", 0);
            topFrame = new SimpleImage(mediaStreamLoader, "screenframe", 1);

            scrollBar1 = new SimpleImage(mediaStreamLoader, "scrollbar", 0);
            scrollBar2 = new SimpleImage(mediaStreamLoader, "scrollbar", 1);

            top508 = SpriteCache.get(759 + 54);
            bottom508 = SpriteCache.get(759 + 55);

            try {
                for (int index = 0; index <= 14; index++) {
                    sideIcons[index] = new SimpleImage(mediaStreamLoader, "sideicons", index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int index = 0; index < 6; index++) {
                    headIconsHint[index] = new SimpleImage(mediaStreamLoader, "headicons_hint", index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int index = 0; index < 18; index++) {
                    headIcons[index] = new SimpleImage(mediaStreamLoader, "headicons_prayer", index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int index = 0; index < 7; index++) {
                    skullIcons[index] = new SimpleImage(mediaStreamLoader, "headicons_pk", index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                int i = 0;
                autoBackgroundSprites[i++] = new SimpleImage(mediaStreamLoader, "tradebacking", 0);
                for (int j = 0; j < 4; j++)
                    autoBackgroundSprites[i++] = new SimpleImage(mediaStreamLoader, "steelborder", j);
                for (int j = 0; j < 2; j++)
                    autoBackgroundSprites[i++] = new SimpleImage(mediaStreamLoader, "steelborder2", j);
                for (int j = 2; j < 4; j++)
                    autoBackgroundSprites[i++] = new SimpleImage(mediaStreamLoader, "miscgraphics", j);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int index = 0; index < 8; index++) {
                    crosses[index] = new SimpleImage(mediaStreamLoader, "cross", index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            long unpacking_media_time_elapsed = System.currentTimeMillis() - unpacking_media_start_time;
            if (ClientConstants.DISPLAY_CLIENT_LOAD_TIME_VERBOSE) {
                System.out.println("It took " + unpacking_media_time_elapsed + " ms to unpack the media.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpackInterfaces() {
        try {

            drawLoadingText(95, "Loading Interfaces");

            long unpacking_interfaces_start_time = System.currentTimeMillis();

            interface_archive = loadArchive("interface.dat");

            AdvancedFont[] fonts = { smallFont, regularFont, boldFont, fancyFont };
            Widget.load(interface_archive, fonts, mediaStreamLoader);
            fixed = Widget.cache[OptionTabWidget.FIXED_MODE];
            resizable = Widget.cache[OptionTabWidget.RESIZABLE_MODE];

            long unpacking_interfaces_time_elapsed = System.currentTimeMillis() - unpacking_interfaces_start_time;
            if (ClientConstants.DISPLAY_CLIENT_LOAD_TIME_VERBOSE) {
                System.out.println("It took " + unpacking_interfaces_time_elapsed + " ms to unpack the interfaces.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareGameEngine() {
        long preparing_game_engine_start_time = System.currentTimeMillis();

        for (int j6 = 0; j6 < 33; j6++) {
            int k6 = 999;
            int i7 = 0;
            for (int k7 = 0; k7 < 34; k7++) {
                if (mapBack.palettePixels[k7 + j6 * mapBack.subWidth] == 0) {
                    if (k6 == 999)
                        k6 = k7;
                    continue;
                }
                if (k6 == 999)
                    continue;
                i7 = k7;
                break;
            }
            anIntArray968[j6] = k6;
            anIntArray1057[j6] = i7 - k6;
        }
        for (int l6 = 1; l6 < 153; l6++) {
            int j7 = 999;
            int l7 = 0;
            for (int j8 = 24; j8 < 177; j8++) {
                if (mapBack.palettePixels[j8 + l6 * mapBack.subWidth] == 0 && (j8 > 34 || l6 > 34)) {
                    if (j7 == 999) {
                        j7 = j8;
                    }
                    continue;
                }
                if (j7 == 999) {
                    continue;
                }
                l7 = j8;
                break;
            }
            minimapLeft[l6 - 1] = j7 - 24;
            minimapLineWidth[l6 - 1] = l7 - j7;
        }

        // mouseDetection = new MouseDetection(this);
        // startRunnable(mouseDetection, 10);
        NpcDefinition.clientInstance = this;
        setting.load();
        setting.update();
        setting.toggleVarbits();
        OptionTabWidget.updateSettings();
        Keybinding.updateInterface();
        SnowFlake.createSnow();
        Save.load();

        try {
            macAddress = new HardwareAddress(InetAddress.getLocalHost()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // resourceProvider.writeAll();

        SpriteCache.cached.clear();

        long game_engine_elapsed = System.currentTimeMillis() - preparing_game_engine_start_time;
        if (ClientConstants.DISPLAY_CLIENT_LOAD_TIME_VERBOSE) {
            System.out.println("It took " + game_engine_elapsed + " ms, to prepare the game engine.");
        }
    }

    public static KeyEventProcessorImpl keyManager;
    public static MouseWheelHandler mouseWheel;

    protected void setUp() {
        setUpKeyboard();
        mouseWheel = this.mouseWheel();
        keyHandler.assignProcessor(keyManager, 0);
        setUpClipboard();
        SignLink.masterDisk = new ArchiveDisk(255, SignLink.cacheData, SignLink.cacheMasterIndex, 500000);
    }

    private static GraphicsDefaults spriteIds;
    public static FontLoader fontLoader;

    static int titleLoadingStage;
    public int loadingPercent;
    public String loadingText;

    public void drawLoadingText(int loadingPercent, String loadingText) {
        this.loadingPercent = loadingPercent;
        this.loadingText = loadingText;
    }

    private int currrentRev = 210;

    void load() {
        int loadingProgress;
        if (Client.titleLoadingStage == 0) {
            System.out.println(
                    "Loading " + ClientConstants.CLIENT_NAME + " on port " + ClientConstants.SERVER_PORT + ".");
            frameMode(false);

            scene = new SceneGraph(tileHeights);

            for (int index = 0; index < 4; index++) {
                collisionMaps[index] = new CollisionMap(104, 104);
            }

            minimapImage = new SimpleImage(512, 512);

            drawLoadingText(5, "Starting game engine...");
            Client.titleLoadingStage = 20;
        } else if (Client.titleLoadingStage == 20) {
            drawLoadingText(10, "Prepared visibility map");
            Client.titleLoadingStage = 30;
        } else if (Client.titleLoadingStage == 30) {
            Js5List.animations = Js5System.createJs5(Js5ArchiveIndex.ANIMATIONS, false, true, true, false);
            Js5List.skeletons = Js5System.createJs5(Js5ArchiveIndex.SKELETONS, false, true, true, false);
            Js5List.configs = Js5System.createJs5(Js5ArchiveIndex.CONFIGS, true, false, true, false);
            Js5List.interfaces = Js5System.createJs5(Js5ArchiveIndex.INTERFACES, false, true, true, false);
            Js5List.soundEffects = Js5System.createJs5(Js5ArchiveIndex.SOUNDEFFECTS, false, true, true, false);
            Js5List.maps = Js5System.createJs5(Js5ArchiveIndex.MAPS, true, true, true, false);
            Js5List.musicTracks = Js5System.createJs5(Js5ArchiveIndex.MUSIC_TRACKS, true, true, true, false);
            Js5List.models = Js5System.createJs5(Js5ArchiveIndex.MODELS, false, true, true, false);
            Js5List.sprites = Js5System.createJs5(Js5ArchiveIndex.SPRITES, false, true, true, false); // here it creates
                                                                                                      // the socket to
                                                                                                      // read from the
                                                                                                      // disk
            Js5List.textures = Js5System.createJs5(Js5ArchiveIndex.TEXTURES, false, true, true, false);
            Js5List.binary = Js5System.createJs5(Js5ArchiveIndex.BINARY, false, true, true, false);
            Js5List.musicJingles = Js5System.createJs5(Js5ArchiveIndex.MUSIC_JINGLES, false, true, true, false);
            Js5List.clientScript = Js5System.createJs5(Js5ArchiveIndex.CLIENTSCRIPT, false, true, true, false);
            Js5List.fonts = Js5System.createJs5(Js5ArchiveIndex.FONTS, true, false, true, false);
            Js5List.musicSamples = Js5System.createJs5(Js5ArchiveIndex.MUSIC_SAMPLES, false, true, true, false);
            Js5List.musicPatches = Js5System.createJs5(Js5ArchiveIndex.MUSIC_PATCHES, false, true, true, false);
            Js5List.archive17 = Js5System.createJs5(Js5ArchiveIndex.ARCHIVE_17, true, true, true, false);
            Js5List.worldmapGeography = Js5System.createJs5(Js5ArchiveIndex.WORLDMAP_GEOGRAPHY, false, true, true,
                    false);
            Js5List.worldmap = Js5System.createJs5(Js5ArchiveIndex.WORLDMAP, false, true, true, false);
            Js5List.worldmapGround = Js5System.createJs5(Js5ArchiveIndex.WORLDMAP_GROUND, false, true, true, false);
            Js5List.dbtableindex = Js5System.createJs5(Js5ArchiveIndex.DBTABLEINDEX, false, true, true, true);
            drawLoadingText(20, "Connecting to update server");
            titleLoadingStage = 40;

        } else if (Client.titleLoadingStage == 40) {
            byte var24 = 0;
            loadingProgress = var24 + Js5List.animations.loadPercent() * 4 / 100;
            loadingProgress += Js5List.sprites.loadPercent() * 4 / 100;
            loadingProgress += Js5List.configs.loadPercent() * 2 / 100;
            loadingProgress += Js5List.interfaces.loadPercent() * 2 / 100;
            loadingProgress += Js5List.soundEffects.loadPercent() * 6 / 100;
            loadingProgress += Js5List.maps.loadPercent() * 4 / 100;
            loadingProgress += Js5List.musicTracks.loadPercent() * 2 / 100;
            loadingProgress += Js5List.models.loadPercent() * 55 / 100;
            loadingProgress += Js5List.sprites.loadPercent() * 2 / 100;
            loadingProgress += Js5List.textures.loadPercent() * 2 / 100;
            loadingProgress += Js5List.binary.loadPercent() * 2 / 100;
            loadingProgress += Js5List.musicJingles.loadPercent() * 2 / 100;
            loadingProgress += Js5List.clientScript.loadPercent() * 2 / 100;
            loadingProgress += Js5List.musicSamples.loadPercent() * 2 / 100;
            loadingProgress += Js5List.musicPatches.loadPercent() * 2 / 100;
            loadingProgress += Js5List.archive17.isLoading() && Js5List.archive17.isFullyLoaded() ? 1 : 0;
            loadingProgress += Js5List.worldmapGeography.loadPercent() / 100;
            loadingProgress += Js5List.worldmap.loadPercent() / 100;
            loadingProgress += Js5List.worldmapGround.loadPercent() / 100;
            loadingProgress += Js5List.dbtableindex.loadPercent() / 100;
            loadingProgress += Js5List.skeletons.loadPercent() / 100;
            loadingProgress += Js5List.fonts.loadPercent() / 100;

            if (loadingProgress != 100) {
                if (loadingProgress != 0) {
                    drawLoadingText(30, "Checking for updates - " + loadingProgress + "%");
                }
            } else {
                Js5System.init(Js5List.animations, "Animations");
                Js5System.init(Js5List.skeletons, "Skeletons");
                Js5System.init(Js5List.soundEffects, "Sound FX");
                Js5System.init(Js5List.maps, "Maps");
                Js5System.init(Js5List.musicTracks, "Music Tracks");
                Js5System.init(Js5List.models, "Models");
                Js5System.init(Js5List.sprites, "Sprites");
                Js5System.init(Js5List.textures, "Textures");
                Js5System.init(Js5List.musicJingles, "Music Jingles");
                Js5System.init(Js5List.musicSamples, "Music Samples");
                Js5System.init(Js5List.musicPatches, "Music Patches");
                Js5System.init(Js5List.worldmap, "World Map");
                Js5System.init(Js5List.worldmapGeography, "World Map Geography");
                Js5System.init(Js5List.worldmapGround, "World Map Ground");

                spriteIds = new GraphicsDefaults();
                spriteIds.decode(Js5List.archive17);
                drawLoadingText(30, "Loaded update list");
                Client.titleLoadingStage = 45;
            }
        } else if (Client.titleLoadingStage == 45) {
            StaticSound.setup(!Client.low_detail, Js5List.soundEffects, Js5List.musicTracks, Js5List.musicJingles,
                    Js5List.musicSamples, Js5List.musicPatches);
            drawLoadingText(35, "Prepared sound engine");
            Client.titleLoadingStage = 50;
            fontLoader = new FontLoader();
        } else if (Client.titleLoadingStage == 50) {
            loadingProgress = FontInfo.initialFonts().length;
            fontLoader.createMap();
            if (fontLoader.fonts.size() != loadingProgress) {
                drawLoadingText(40, "Loading fonts - " + fontLoader.fonts.size() * 100 / loadingProgress + "%");
            } else {
                smallFont = fontLoader.fonts.get(FontInfo.SMALL_FONT);
                regularFont = fontLoader.fonts.get(FontInfo.REGULAR_FONT);
                boldFont = fontLoader.fonts.get(FontInfo.BOLD_FONT);
                fancyFont = fontLoader.fonts.get(FontInfo.FANCY_SMALL);
                fancyFontMedium = fontLoader.fonts.get(FontInfo.FANCY_MEDIUM);
                fancyFontLarge = fontLoader.fonts.get(FontInfo.FANCY_CAPS_LARGE);
                drawLoadingText(40, "Loading fonts");
                Client.titleLoadingStage = 60;
            }
        } else {
            int var3;
            if (Client.titleLoadingStage == 60) {
                drawLoadingText(50, "Loaded title screen");
                setGameState(5);
                setStartupActions();
                Client.titleLoadingStage = 70;
            } else if (Client.titleLoadingStage == 70) {
                if (!Js5List.configs.isFullyLoaded()) {
                    drawLoadingText(60, "Loading config - " + Js5List.configs.loadPercent() + "%");
                } else if (!Js5List.configs.isFullyLoaded()) {
                    drawLoadingText(60, "Loading config - " + (80 + Js5List.clientScript.loadPercent() / 6) + "%");
                } else {
                    Js5List.initConfigSizes();
                    ItemDefinition.members = isMembers;
                    NpcDefinition.init(currrentRev <= 209, spriteIds.headIconArchive);

                    if (Js5List.configs.isFullyLoaded()) {
                        AreaDefinition.definitions = new AreaDefinition[Js5List.getConfigSize(Js5ConfigType.AREA)];

                        for (int count = 0; count < Js5List.getConfigSize(Js5ConfigType.AREA); ++count) {
                            byte[] fileData = Js5List.configs.takeFile(Js5ConfigType.AREA, count);
                            AreaDefinition.definitions[count] = new AreaDefinition(count);
                            if (fileData != null) {
                                AreaDefinition.definitions[count].decode(new Buffer(fileData));
                                AreaDefinition.definitions[count].init();
                            }
                        }

                    }

                    drawLoadingText(60, "Loaded config");
                    Client.titleLoadingStage = 80;
                }
            } else if (Client.titleLoadingStage == 80) {
                loadingProgress = 0;

                ++loadingProgress;
                ++loadingProgress;
                ++loadingProgress;
                ++loadingProgress;
                ++loadingProgress;
                ++loadingProgress;
                if (mapSceneSprites == null) {
                    mapSceneSprites = IndexedImage.loadImages(spriteIds.mapScenes, 0);
                } else {
                    ++loadingProgress;
                }

                ++loadingProgress;
                ++loadingProgress;
                if (mapMarkerSprites == null) {
                    mapMarkerSprites = SimpleImage.getSprites(spriteIds.field4591, 0);
                } else {
                    ++loadingProgress;
                }
                ++loadingProgress;

                if (mapDotSprites == null) {
                    mapDotSprites = SimpleImage.getSprites(spriteIds.field4590, 0);
                } else {
                    ++loadingProgress;
                }

                ++loadingProgress;

                ++loadingProgress;

                ++loadingProgress;

                unpackingMedia();

                if (loadingProgress < 14) {
                    drawLoadingText(70, "Loading sprites - " + loadingProgress * 100 / 13 + "%");
                } else {
                    int var1 = (int) (Math.random() * 21.0) - 10;
                    int var2 = (int) (Math.random() * 21.0) - 10;
                    var3 = (int) (Math.random() * 21.0) - 10;
                    int var4 = (int) (Math.random() * 41.0) - 20;
                    mapSceneSprites[0].offsetColor(var1 + var4, var2 + var4, var4 + var3);
                    drawLoadingText(70, "Loading sprites");
                    Client.titleLoadingStage = 90;
                }
            } else if (Client.titleLoadingStage == 90) {
                if (!Js5List.textures.isFullyLoaded()) {
                    drawLoadingText(90, "Loading textures - 0%");
                } else {
                    TextureProvider textureProvider = new TextureProvider(Js5List.textures, Js5List.sprites, 20,
                            0.80000000000000004D, low_detail ? 64 : 128);
                    Rasterizer3D.setTextureLoader(textureProvider);
                    Rasterizer3D.setBrightness(0.80000000000000004D);
                    textureProvider.getLoadedPercentage();
                    drawLoadingText(100, "Loading textures");
                    Client.titleLoadingStage = 95;
                }
            } else if (Client.titleLoadingStage == 95) {
                if (!Js5List.maps.isFullyLoaded()) {
                    drawLoadingText(90, "Loading maps - " + Js5List.maps.percentage() + "%");
                } else {
                    drawLoadingText(90, "Loading maps");
                    Client.titleLoadingStage = 100;

                }
            } else if (Client.titleLoadingStage == 100) {
                drawLoadingText(90, "Loading textures");
                Client.titleLoadingStage = 110;
            } else if (Client.titleLoadingStage == 110) {
                drawLoadingText(92, "Loading input handler");
                Client.titleLoadingStage = 120;
            } else if (Client.titleLoadingStage == 120) {
                if (!Js5List.binary.tryLoadFileByNames("huffman", "")) {
                    drawLoadingText(94, "Loading wordpack - " + 0 + "%");
                } else {
                    Huffman var22 = new Huffman(Js5List.binary.takeFileByNames("huffman", ""));
                    huffman = var22;
                    drawLoadingText(94, "Loaded wordpack");
                    Client.titleLoadingStage = 130;
                }

                drawLoadingText(94, "Loaded wordpack");
                Client.titleLoadingStage = 130;
            } else if (Client.titleLoadingStage == 130) {
                drawLoadingText(98, "Loaded interfaces");
                unpackInterfaces();
                Client.titleLoadingStage = 140;
            } else if (Client.titleLoadingStage == 140) {
                drawLoadingText(99, "Loaded world map");
                Client.titleLoadingStage = 150;
            } else if (Client.titleLoadingStage == 150) {
                try {
                    setGameState(GameState.LOGIN_SCREEN);
                    isLoading = false;
                    long clientLoadStart = System.currentTimeMillis();
                    prepareGameEngine();
                    long clientLoadEnd = System.currentTimeMillis();
                    long clientLoadDifference = clientLoadEnd - clientLoadStart;
                    if (ClientConstants.DISPLAY_CLIENT_LOAD_TIME) {
                        System.out.println("It took " + clientLoadDifference + " ms to load the client.");
                    }
                    Client.titleLoadingStage = 210;
                } catch (Exception exception) {
                    exception.printStackTrace();
                    addReportToServer(exception.getMessage());
                }
            }
        }

    }

    private Widget fixed;
    private Widget resizable;

    private final void adjustWhenOnResizeable() {
        fixed.disabledSprite = SpriteCache.get(619);
        fixed.enabledAltSprite = SpriteCache.get(595);
        fixed.enabledSprite = SpriteCache.get(596);
        fixed.disabledAltSprite = SpriteCache.get(595);

        resizable.disabledSprite = SpriteCache.get(618);
        resizable.enabledSprite = SpriteCache.get(618);
        resizable.enabledAltSprite = SpriteCache.get(597);
        resizable.disabledAltSprite = SpriteCache.get(598);

        fixed.active = false;
        resizable.active = true;
    }

    private final void adjustWhenOnFixed() {
        fixed.disabledSprite = SpriteCache.get(595);
        fixed.enabledAltSprite = SpriteCache.get(619);
        fixed.enabledSprite = SpriteCache.get(595);
        fixed.disabledAltSprite = SpriteCache.get(596);

        resizable.disabledSprite = SpriteCache.get(597);
        resizable.enabledSprite = SpriteCache.get(598);
        resizable.enabledAltSprite = SpriteCache.get(618);
        resizable.disabledAltSprite = SpriteCache.get(618);

        fixed.active = true;
        resizable.active = false;

    }

    private void updatePlayerList(Buffer stream, int packetSize) {
        while (stream.bitPosition + 10 < packetSize * 8) {
            int index = stream.readBits(11);
            if (index == 2047) {
                break;
            }
            if (players[index] == null) {
                players[index] = new Player();
                if (playerSynchronizationBuffers[index] != null) {
                    players[index].update(playerSynchronizationBuffers[index]);
                    // players[index].drawPlayer(playerSynchronizationBuffers[index]);
                }
            }
            local_players[players_in_region++] = index;
            Player player = players[index];
            player.last_update_tick = tick;

            int update = stream.readBits(1);

            if (update == 1)
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;

            int discardWalkingQueue = stream.readBits(1);

            int y = stream.readBits(5);

            if (y > 15) {
                y -= 32;
            }

            int x = stream.readBits(5);

            if (x > 15) {
                x -= 32;
            }

            player.setPos(local_player.pathX[0] + x, local_player.pathY[0] + y, discardWalkingQueue == 1);
        }
        stream.disableBitAccess();
    }

    public boolean circle_clip(int x, int y, int click_x, int click_y, int radius) {
        return java.lang.Math.pow((x + radius - click_x), 2)
                + java.lang.Math.pow((y + radius - click_y), 2) < java.lang.Math.pow(radius, 2);
    }

    private void processMainScreenClick() {
        if (widget_overlay_id == 16244) {
            // TODO: fix this welcome screen for resize
            if (MouseHandler.keypressedEventIndex == 1 && MouseHandler.saveClickX >= 267
                    && MouseHandler.saveClickX <= 500 && MouseHandler.saveClickY >= 300
                    && MouseHandler.saveClickY <= 393) {
                clearTopInterfaces();
            }
            return;
        }
        if (minimapState != ClientConstants.SHOW_MINIMAP) {
            return;
        }

        if (MouseHandler.keypressedEventIndex == 1) {
            resetInputFieldFocus();
            int screen_click_x = MouseHandler.saveClickX - 25 - 547;
            int screen_click_y = MouseHandler.saveClickY - 5 - 3;
            if (resized) {
                screen_click_x = MouseHandler.saveClickX - (canvasWidth - 182 + 24);
                screen_click_y = MouseHandler.saveClickY - 8;
            }
            if (circle_clip(0, 0, screen_click_x, screen_click_y, 76) && mouseMapPosition() && !runHover) {
                screen_click_x -= 73;
                screen_click_y -= 75;
                int k = camAngleY + map_rotation & 0x7ff;
                int i1 = Rasterizer3D.SINE[k];
                int j1 = Rasterizer3D.COSINE[k];
                i1 = i1 * (minimapZoom + 256) >> 8;
                j1 = j1 * (minimapZoom + 256) >> 8;
                int k1 = screen_click_y * i1 + screen_click_x * j1 >> 11;
                int l1 = screen_click_y * j1 - screen_click_x * i1 >> 11;
                int i2 = local_player.x + k1 >> 7;
                int j2 = local_player.y - l1 >> 7;
                if ((myPrivilege == 2) && isShiftPressed && ClientConstants.SHIFT_CLICK_TELEPORT) {
                    teleport(next_region_start + i2, next_region_end + j2, plane);
                } else {
                    boolean accessible = walk(1, 0, 0, 0, local_player.pathY[0], 0, 0, j2,
                            local_player.pathX[0], true, i2);
                    if (accessible) {

                        /*
                         * outgoing.writeByte(i); outgoing.writeByte(j);
                         * outgoing.writeShort(cameraHorizontal); outgoing.writeByte(57);
                         * outgoing.writeByte(minimapRotation); outgoing.writeByte(minimapZoom);
                         * outgoing.writeByte(89); outgoing.writeShort(localPlayer.x);
                         * outgoing.writeShort(localPlayer.y); outgoing.writeByte(anInt1264);
                         * outgoing.writeByte(63);
                         */
                    }
                }
            }
            anInt1117++;
            if (anInt1117 > 1151) {
                anInt1117 = 0;
                // anti-cheat
                /*
                 * outgoing.writeOpcode(246); outgoing.writeByte(0); int bufPos =
                 * outgoing.currentPosition;
                 *
                 * if ((int) (Math.random() * 2D) == 0) { outgoing.writeByte(101); }
                 *
                 * outgoing.writeByte(197); outgoing.writeShort((int) (Math.random() * 65536D));
                 * outgoing.writeByte((int) (Math.random() * 256D)); outgoing.writeByte(67);
                 * outgoing.writeShort(14214);
                 *
                 * if ((int) (Math.random() * 2D) == 0) { outgoing.writeShort(29487); }
                 *
                 * outgoing.writeShort((int) (Math.random() * 65536D));
                 *
                 * if ((int) (Math.random() * 2D) == 0) { outgoing.writeByte(220); }
                 *
                 * outgoing.writeByte(180); outgoing.writeBytes(outgoing.currentPosition -
                 * bufPos);
                 */
            }
        }
    }

    private String interfaceIntToString(int j) {
        if (j < 0x3b9ac9ff)
            return format.format(j);
        else
            return "*";
    }

    private void showErrorScreen() {

    }

    private void forceNPCUpdateBlock() {
        for (int j = 0; j < npcs_in_region; j++) {
            int k = local_npcs[j];
            Npc npc = npcs[k];
            if (npc != null)
                entityUpdateBlock(npc);
        }
    }

    private void entityUpdateBlock(Entity entity) {
        if (entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
            entity.sequence = -1;
            entity.initiate_movement = 0;
            entity.cease_movement = 0;
            entity.clearSpotAnimations();
            entity.x = entity.pathX[0] * 128 + entity.size * 64;
            entity.y = entity.pathY[0] * 128 + entity.size * 64;
            entity.resetPath();
        }
        if (entity == local_player
                && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
            entity.sequence = -1;
            entity.initiate_movement = 0;
            entity.cease_movement = 0;
            entity.clearSpotAnimations();
            entity.x = entity.pathX[0] * 128 + entity.size * 64;
            entity.y = entity.pathY[0] * 128 + entity.size * 64;
            entity.resetPath();
        }
        if (entity.initiate_movement > tick) {
            entity.refreshEntityPosition();
        } else if (entity.cease_movement >= tick) {
            entity.refreshEntityFaceDirection();
        } else {
            entity.getDegreesToTurn();
        }
        appendFocusDestination(entity);
        entity.updateAnimation();
    }

    private void appendFocusDestination(Entity entity) {
        if (entity.rotation == 0)
            return;
        if (entity.targetIndex != -1 && entity.targetIndex < 32768
                && entity.targetIndex < npcs.length) {
            Npc npc = npcs[entity.targetIndex];
            if (npc != null) {
                int i1 = entity.x - npc.x;
                int k1 = entity.y - npc.y;
                if (i1 != 0 || k1 != 0)
                    entity.turn_direction = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
            }
        }
        if (entity.targetIndex >= 32768) {
            int j = entity.targetIndex - 32768;
            if (j == localPlayerIndex) {
                j = LOCAL_PLAYER_INDEX;
            }
            Player player = players[j];
            if (player != null) {
                int l1 = entity.x - player.x;
                int i2 = entity.y - player.y;
                if (l1 != 0 || i2 != 0) {
                    entity.turn_direction = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if ((entity.faceX != 0 || entity.faceY != 0) && (entity.pathLength == 0 || entity.step_tracker > 0)) {
            int k = entity.x - (entity.faceX - next_region_start - next_region_start) * 64;
            int j1 = entity.y - (entity.faceY - next_region_end - next_region_end) * 64;
            if (k != 0 || j1 != 0)
                entity.turn_direction = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
            entity.faceX = 0;
            entity.faceY = 0;
        }
        int l = entity.turn_direction - entity.current_rotation & 0x7ff;
        if (l != 0) {
            if (l < entity.rotation || l > 2048 - entity.rotation)
                entity.current_rotation = entity.turn_direction;
            else if (l > 1024)
                entity.current_rotation -= entity.rotation;
            else
                entity.current_rotation += entity.rotation;
            entity.current_rotation &= 0x7ff;
            if (entity.movementSequence == entity.turnLeftSequence
                    && entity.current_rotation != entity.turn_direction) {
                if (entity.walkSequence != -1) {
                    entity.movementSequence = entity.walkSequence;
                    return;
                }
                entity.movementSequence = entity.walkSequence;
            }
        }
    }

    private void drawGameScreen() { // ight go ahead
        if (fullscreenInterfaceID != -1) {
            if (gameState == GameState.LOGGED_IN.getState()) {
                try {
                    processWidgetAnimations(animation_step, fullscreenInterfaceID);
                    if (widget_overlay_id != -1) {
                        processWidgetAnimations(animation_step, widget_overlay_id);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    addReportToServer(ex.getMessage());
                }
                animation_step = 0;
                getCallbacks().post(ClientTick.INSTANCE);
                resetAllImageProducers();

                Rasterizer2D.Rasterizer2D_clear();
                update_producers = true;
                if (widget_overlay_id != -1) {
                    Widget rsInterface_1 = Widget.cache[widget_overlay_id];
                    if (rsInterface_1.width == 512 && rsInterface_1.height == 334 && rsInterface_1.type == 0) {
                        rsInterface_1.width = 765;
                        rsInterface_1.height = 503;
                    }
                    try {
                        drawInterface(rsInterface_1, 0, 8, 0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        addReportToServer(ex.getMessage());
                    }
                }
                Widget rsInterface = Widget.cache[fullscreenInterfaceID];
                if (rsInterface.width == 512 && rsInterface.height == 334 && rsInterface.type == 0) {
                    rsInterface.width = 765;
                    rsInterface.height = 503;
                }
                try {
                    drawInterface(rsInterface, 0, 8, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    addReportToServer(ex.getMessage());
                }
                if (!isMenuOpen) {
                    processRightClick();
                    drawTooltip();
                }
            }
            drawCount++;

            return;
        } else {
            if (drawCount != 0) {

            }
        }
        // System.out.println("Canvas size " + super.fullGameScreen.canvasWidth + " by "
        // + super.fullGameScreen.canvasHeight);
        if (update_producers) {
            update_producers = false;

            update_chat_producer = true;
            update_tab_producer = true;
        }
        if (overlayInterfaceId != -1) {
            try {
                processWidgetAnimations(animation_step, overlayInterfaceId);
            } catch (Exception ex) {
                ex.printStackTrace();
                addReportToServer(ex.getMessage());
            }
        }

        if (backDialogueId == -1) {
            aClass9_1059.scrollPosition = chatScrollHeight - chatScrollAmount - 110;
            if (MouseHandler.mouseX >= 496 && MouseHandler.mouseX <= 511
                    && MouseHandler.mouseY > (!resized ? 345 : canvasHeight - 158))
                handleScroll(494, 110, MouseHandler.mouseX,
                        MouseHandler.mouseY - (!resized ? 345 : canvasHeight - 158), aClass9_1059, 0,
                        chatScrollHeight);
            int i = chatScrollHeight - 110 - aClass9_1059.scrollPosition;
            if (i < 0) {
                i = 0;
            }
            if (i > chatScrollHeight - 110) {
                i = chatScrollHeight - 110;
            }
            if (chatScrollAmount != i) {
                chatScrollAmount = i;
                update_chat_producer = true;
            }
        }
        if (backDialogueId != -1) {
            boolean flag2 = false;

            try {
                flag2 = processWidgetAnimations(animation_step, backDialogueId);
            } catch (Exception ex) {
                ex.printStackTrace();
                addReportToServer(ex.getMessage());
            }

            if (flag2) {
                update_chat_producer = true;
            }
        }
        if (atInventoryInterfaceType == 3)
            update_chat_producer = true;
        if (activeInterfaceType == 3)
            update_chat_producer = true;
        if (clickToContinueString != null)
            update_chat_producer = true;
        if (isMenuOpen && menuScreenArea == 2)
            update_chat_producer = true;
        if (update_chat_producer) {

            update_chat_producer = false;
        }
        if (gameState == GameState.LOGGED_IN.getState()) {
            int offsetX = isResized() ? 0 : 4;
            int offsetY = isResized() ? 0 : 4;
            drawEntities(offsetX, offsetY, !isResized() ? 512 : canvasWidth, !isResized() ? 334 : canvasHeight);
        }
        if (flashingSidebarId != -1)
            update_tab_producer = true;
        if (update_tab_producer) {
            if (flashingSidebarId != -1 && flashingSidebarId == sidebarId) {
                flashingSidebarId = -1;
                // flashing sidebar
                /*
                 * outgoing.writeOpcode(120); outgoing.writeByte(tabId);
                 */
            }
            update_tab_producer = false;
        }

        ObjectSound.updateObjectSounds(plane, local_player.x, local_player.y, animation_step); // L:
        animation_step = 0;
        getCallbacks().post(ClientTick.INSTANCE);

    }

    private boolean buildFriendsListMenu(Widget widget) {
        int type = widget.contentType;
        if (type >= 1 && type <= 200 || type >= 701 && type <= 900) {
            if (type >= 801)
                type -= 701;
            else if (type >= 701)
                type -= 601;
            else if (type >= 101)
                type -= 101;
            else
                type--;
            menuActionText[menuActionRow] = "Remove <col=FFFFFF>" + friendsList[type];
            menuActionTypes[menuActionRow] = 792;
            menuActionRow++;
            menuActionText[menuActionRow] = "Message <col=FFFFFF>" + friendsList[type];
            menuActionTypes[menuActionRow] = 639;
            menuActionRow++;
            return true;
        }
        if (type >= 401 && type <= 500) {
            menuActionText[menuActionRow] = "Remove <col=FFFFFF>" + widget.defaultText;
            menuActionTypes[menuActionRow] = 322;
            menuActionRow++;
            return true;
        }

        if (type == 902) {
            menuActionText[menuActionRow] = "Choose " + widget.defaultText;
            menuActionTypes[menuActionRow] = 169;
            menuActionRow++;
            return true;
        } else {
            return false;
        }
    }

    public final void render_stationary_graphics() {
        for (SpotAnimEntity spotAnimationEntity = (SpotAnimEntity) incompleteAnimables
                .last(); null != spotAnimationEntity; spotAnimationEntity = (SpotAnimEntity) incompleteAnimables
                        .previous()) {
            if (plane == spotAnimationEntity.z && !spotAnimationEntity.expired) {
                if (tick >= spotAnimationEntity.cycleStart) {
                    spotAnimationEntity.step(animation_step);
                    if (spotAnimationEntity.expired) {
                        spotAnimationEntity.unlink();
                    } else {
                        scene.add_entity(spotAnimationEntity.z, 0, spotAnimationEntity.height, -1,
                                spotAnimationEntity.y, 60, spotAnimationEntity.x,
                                spotAnimationEntity, false);
                    }
                }
            } else {
                spotAnimationEntity.unlink();
            }
        }
    }

    public void drawBlackBox(int xPos, int yPos) {
        Rasterizer2D.draw_filled_rect(xPos - 2, yPos - 1, 1, 71, 0x726451);
        Rasterizer2D.draw_filled_rect(xPos + 174, yPos, 1, 69, 0x726451);
        Rasterizer2D.draw_filled_rect(xPos - 2, yPos - 2, 178, 1, 0x726451);
        Rasterizer2D.draw_filled_rect(xPos, yPos + 68, 174, 1, 0x726451);
        Rasterizer2D.draw_filled_rect(xPos - 1, yPos - 1, 1, 71, 0x2E2B23);
        Rasterizer2D.draw_filled_rect(xPos + 175, yPos - 1, 1, 71, 0x2E2B23);
        Rasterizer2D.draw_filled_rect(xPos, yPos - 1, 175, 1, 0x2E2B23);
        Rasterizer2D.draw_filled_rect(xPos, yPos + 69, 175, 1, 0x2E2B23);
        Rasterizer2D.draw_filled_rect(xPos, yPos, 174, 68, 0, 220);
    }

    private SimpleImage top508;
    private SimpleImage bottom508;

    public void draw508Scrollbar(int height, int pos, int y, int x, int maxScroll,
            boolean transparent) {
        if (transparent) {
            drawTransparentScrollBar(x, y, height, maxScroll, pos);
        } else {

            top508.drawSprite(x, y);
            bottom508.drawSprite(x, (y + height) - 16);
            Rasterizer2D.draw_filled_rect(x, y + 16, 16, height - 32, 0x746241);
            Rasterizer2D.draw_filled_rect(x, y + 16, 15, height - 32, 0x77603e);
            Rasterizer2D.draw_filled_rect(x, y + 16, 14, height - 32, 0x77603e);
            Rasterizer2D.draw_filled_rect(x, y + 16, 13, height - 32, 0x95784a);
            Rasterizer2D.draw_filled_rect(x, y + 16, 12, height - 32, 0x997c52);
            Rasterizer2D.draw_filled_rect(x, y + 16, 11, height - 32, 0x9e8155);
            Rasterizer2D.draw_filled_rect(x, y + 16, 10, height - 32, 0xa48558);
            Rasterizer2D.draw_filled_rect(x, y + 16, 8, height - 32, 0xaa8b5c);
            Rasterizer2D.draw_filled_rect(x, y + 16, 6, height - 32, 0xb09060);
            Rasterizer2D.draw_filled_rect(x, y + 16, 3, height - 32, 0x866c44);
            Rasterizer2D.draw_filled_rect(x, y + 16, 1, height - 32, 0x7c6945);

            int k1 = ((height - 32) * height) / maxScroll;
            if (k1 < 8) {
                k1 = 8;
            }
            int l1 = ((height - 32 - k1) * pos) / (maxScroll - height);
            int l2 = ((height - 32 - k1) * pos) / (maxScroll - height) + 6;
            Rasterizer2D.draw_vertical_line(x + 1, y + 16 + l1, k1, 0x5c492d);
            Rasterizer2D.draw_vertical_line(x + 14, y + 16 + l1, k1, 0x5c492d);
            Rasterizer2D.draw_horizontal_line(x + 1, y + 16 + l1, 14, 0x5c492d);
            Rasterizer2D.draw_horizontal_line(x + 1, y + 15 + l1 + k1, 14, 0x5c492d);
            Rasterizer2D.draw_horizontal_line(x + 4, y + 18 + l1, 8, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 4, y + 13 + l1 + k1, 8, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 3, y + 19 + l1, 2, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 11, y + 19 + l1, 2, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 3, y + 12 + l1 + k1, 2, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 11, y + 12 + l1 + k1, 2, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 3, y + 14 + l1 + k1, 11, 0x866c44);
            Rasterizer2D.draw_horizontal_line(x + 3, y + 17 + l1, 11, 0x866c44);
            Rasterizer2D.draw_vertical_line(x + 13, y + 12 + l2, k1 - 4, 0x866c44);
            Rasterizer2D.draw_vertical_line(x + 3, y + 13 + l2, k1 - 6, 0x664f2b);
            Rasterizer2D.draw_vertical_line(x + 12, y + 13 + l2, k1 - 6, 0x664f2b);
            Rasterizer2D.draw_horizontal_line(x + 2, y + 18 + l1, 2, 0x866c44);
            Rasterizer2D.draw_horizontal_line(x + 2, y + 13 + l1 + k1, 2, 0x866c44);
            Rasterizer2D.draw_horizontal_line(x + 12, y + 18 + l1, 1, 0x866c44);
            Rasterizer2D.draw_horizontal_line(x + 12, y + 13 + l1 + k1, 1, 0x866c44);
        }
    }

    private int interfaceDrawY;

    private void drawProgressBar(int xPos, int yPos, int width, int height, int currentPercent, int firstColor,
            int secondColor, int strokeWidth) {
        Rasterizer2D.draw_filled_rect(xPos, yPos, width, height, firstColor, 30);
        Rasterizer2D.draw_filled_rect(xPos, yPos, (int) (width * (currentPercent / 100.0f)), height, secondColor, 200);
        Rasterizer2D.drawStroke(xPos - strokeWidth, yPos, width + strokeWidth, height, 0x000000, strokeWidth);
    }

    private static final int NORMAL_SUB_SPELLBOOK = 938;
    private static final int ANCIENT_SUB_SPELLBOOK = 838;
    private static final int LUNAR_SPELLBOOK = 29999;

    private boolean isMagicBook() {
        return tabInterfaceIDs[sidebarId] != NORMAL_SUB_SPELLBOOK && tabInterfaceIDs[sidebarId] != ANCIENT_SUB_SPELLBOOK
                && tabInterfaceIDs[sidebarId] != LUNAR_SPELLBOOK && tabInterfaceIDs[sidebarId] != 839;
    }

    private void drawInterface(Widget widget, int x, int y, int scroll_y) {
        if (widget == null)
            return;

        if (widget.type != 0 || widget.children == null)
            return;

        if (isMagicBook() && widget.invisible)
            return;
        if (widget.invisible && focusedViewportWidget != widget.id && focusedSidebarWidget != widget.id
                && focusedChatWidget != widget.id || widget.drawingDisabled) {
            return;
        }

        drawSpecialAttack(widget);

        if (widget.parent == 193) {
            widget.width = 765;
            widget.height = 503;
            x = 455;
            y = 285;
        }

        int clipLeft = Rasterizer2D.xClipStart;
        int clipTop = Rasterizer2D.yClipStart;
        int clipRight = Rasterizer2D.xClipEnd;
        int clipBottom = Rasterizer2D.yClipEnd;
        Rasterizer2D.setDrawingArea(x, y, x + widget.width, y + widget.height);
        Rasterizer3D.setupRasterizerClip();
        int childCount = widget.children.length;
        for (int childId = 0; childId < childCount; childId++) {
            int child_x_in_bounds = widget.child_x[childId] + x;
            int child_y_in_bounds = (widget.child_y[childId] + y) - scroll_y;
            Widget child = Widget.cache[widget.children[childId]];
            if (child == null) {
                continue;
            }
            if (child.drawingDisabled || (isMagicBook() && child.invisible) && focusedViewportWidget != child.id
                    && focusedSidebarWidget != child.id) {
                continue;
            }
            child_x_in_bounds += child.x;
            child_y_in_bounds += child.y;
            if (child.defaultText != null) {
                // System.out.println("Child defaultText is " + child.defaultText);
            }
            if (child.contentType > 0)
                handle_widget_support(child);

            if (child.type == 287 && !child.invisible) {
                drawProgressBar(child_x_in_bounds, child_y_in_bounds, child.width, child.height, child.currentPercent,
                        child.backingColour != 0 ? child.backingColour : 0xFF0000,
                        child.barColour != 0 ? child.barColour : 0x008000, 1);
            }

            if (child.type == Widget.TYPE_CONTAINER) {
                int scrollMax = child.scrollMax;
                if (child.scrollPosition > scrollMax - child.height) {
                    child.scrollPosition = scrollMax - child.height;
                }
                if (child.scrollPosition < 0) {
                    child.scrollPosition = 0;
                }
                drawInterface(child, child_x_in_bounds, child_y_in_bounds, child.scrollPosition);
                if (scrollMax > child.height) {
                    if (child.id == 36350 || child.newScroller) {
                        draw508Scrollbar(child.height, child.scrollPosition, child_y_in_bounds,
                                child_x_in_bounds + child.width, child.scrollMax, false);
                    } else {
                        drawScrollbar(child.height, child.scrollPosition, child_y_in_bounds,
                                child_x_in_bounds + child.width, child.scrollMax, false);
                    }
                }
            } else if (child.type != 1)
                if (child.type == Widget.TYPE_INVENTORY) {
                    // sendMessage("draw "+child+" "+child.type+" "+child.id, 0, "");
                    boolean isFixed = !resized;
                    int slot = 0;
                    int newSlot = 0;
                    int tabAm = 0;
                    int tabSlot = -1;
                    int hh = 2;
                    int results = -1;
                    Widget rsi = this.getInputFieldFocusOwner();
                    boolean search = searchingBank && !promptInput.isEmpty() && child.id != 5064; // exclude inventory
                                                                                                  // searching
                    boolean searchShops = false;
                    if (rsi != null && searchingShops) {
                        searchShops = searchingShops && !rsi.defaultText.isEmpty() && child.id != 3823;
                    }

                    if (child.contentType == 206) {
                        int tabHeight = 0;
                        for (int i = 0; i < tabAmounts.length; i++) {
                            if (tabSlot + 1 < tabAmounts.length && tabAmounts[tabSlot + 1] > 0) {
                                tabAm += tabAmounts[++tabSlot];
                                tabHeight += (tabAmounts[tabSlot] / child.width)
                                        + (tabAmounts[tabSlot] % child.width == 0 ? 0 : 1);

                                if (tabSlot + 1 < tabAmounts.length && tabAmounts[tabSlot + 1] > 0 && settings[211] == 0
                                        && !search
                                        || tabSlot + 1 < tabAmounts.length && tabAmounts[tabSlot + 1] > 0
                                                && settings[211] == 0 && !searchShops) {
                                    Rasterizer2D.draw_horizontal_line(child_x_in_bounds,
                                            (child_y_in_bounds + tabHeight * (32 + child.inventoryMarginY) + hh) - 1,
                                            ((32 + child.inventoryMarginX) * child.width) - 10, 0x3F3528);
                                    Rasterizer2D.draw_horizontal_line(child_x_in_bounds,
                                            (child_y_in_bounds + tabHeight * (32 + child.inventoryMarginY) + hh),
                                            ((32 + child.inventoryMarginX) * child.width) - 10, 0x3F3528);
                                }
                                hh += 8;
                            }

                            if (i > 0) {
                                int itemSlot = tabAm - tabAmounts[i];
                                if (itemSlot == 816) {
                                    itemSlot--;
                                }
                                int xOffset = (canvasWidth - 237 - Widget.cache[26000].width) / 2;
                                int yOffset = 36 + ((canvasHeight - 503) / 2);
                                int x2 = xOffset + 77;
                                int y2 = yOffset + 25;
                                try {
                                    int item = Widget.cache[5382].inventoryItemId[itemSlot];
                                    if (tabAmounts[i] > 0 && item > 0) {
                                        SimpleImage icon = null;
                                        int amount = child.inventoryAmounts[itemSlot];

                                        if (settings[750] == 1) {
                                            icon = ItemDefinition.getSprite(item - 1, amount, 0);
                                        } else if (settings[751] == 1) {
                                            icon = SpriteCache.get(219 + i);
                                        } else if (settings[752] == 1) {
                                            icon = SpriteCache.get(210 + i);
                                        }

                                        if (icon != null) {
                                            if (settings[750] == 1 && amount == 0) {
                                                icon.drawSprite1((isFixed ? 59 : x2 + 3) + 40 * i,
                                                        (isFixed ? 41 : y2 + 2), 110, true);
                                            } else {
                                                icon.drawSprite1((isFixed ? 59 : x2 + 3) + 40 * i,
                                                        (isFixed ? 41 : y2 + 2), 255, true);
                                            }
                                        }

                                        Widget.cache[26031 + i * 4].y = 0;
                                        Widget.cache[26032 + i * 4].y = 0;
                                        Widget.cache[26032 + i * 4].tooltip = "View tab <col=ff7000>" + i;
                                        Widget.cache[26032 + i * 4].enabledSprite = SpriteCache.get(110);
                                    } else if (tabAmounts[i - 1] <= 0) {
                                        Widget.cache[26031 + i * 4].y = -500;
                                        if (i > 1) {
                                            Widget.cache[26032 + i * 4].y = -500;
                                        } else {
                                            SpriteCache.get(210)
                                                    .drawSprite1((isFixed ? 59 : x2) + 40 * i,
                                                            (isFixed ? 41 : y2), 255, true);
                                        }
                                        Widget.cache[26032 + i * 4].tooltip = "New tab";
                                    } else {
                                        Widget.cache[26031 + i * 4].y = -500;
                                        Widget.cache[26032 + i * 4].y = 0;
                                        Widget.cache[26032 + i * 4].tooltip = "New tab";
                                        Widget.cache[26032 + i * 4].enabledSprite = SpriteCache.get(110);
                                        SpriteCache.get(210)
                                                .drawSprite1((isFixed ? 59 : x2) + 40 * i,
                                                        (isFixed ? 41 : y2), 255, true);
                                    }
                                } catch (Exception e) {
                                    addReportToServer("Bank tab icon error: tab [" + i + "], amount [" + tabAm
                                            + "], tabAmount [" + tabAmounts[i] + "], itemSlot [" + itemSlot + "]");
                                    e.printStackTrace();
                                    addReportToServer(e.getMessage());
                                }
                            }
                        }

                        Rasterizer2D.yClipEnd += 3;

                        tabAm = tabAmounts[0];
                        tabSlot = 0;
                        hh = 0;

                        newSlot = 0;
                        int tabH = 0;
                        if (settings[211] != 0) {
                            for (int i = 0; i < tabAmounts.length; i++) {
                                if (i == settings[211]) {
                                    tabH = (int) Math.ceil(tabAmounts[i] / 9.0);
                                    break;
                                }
                                newSlot += tabAmounts[i];
                            }
                            slot = newSlot;
                            Widget.cache[5385].scrollMax = tabH * 45; // This used to be 42, but it's probably better as
                                                                      // a multiple of 9 so is now 45.
                        } else {
                            int totalTabs = 0;
                            for (int i = 0; i < tabAmounts.length; i++) {
                                if (tabAmounts[i] > 0) {
                                    totalTabs = i;
                                    tabH += (int) Math.ceil(tabAmounts[i] / 9.0); // This used to be 42, but it's
                                                                                  // probably better as a multiple of 9
                                                                                  // so is now 45.
                                }
                            }

                            Widget.cache[5385].scrollMax = tabH * 45 + (totalTabs * 10);
                        }
                    }

                    int dragX = 0, dragY = 0;
                    SimpleImage draggedItem = null;

                    heightLoop: for (int height = 0; height < child.height; height++) {
                        for (int width = 0; width < child.width; width++) {
                            if (child.contentType == 206 && !search) {
                                if (settings[211] == 0) {
                                    if (slot == tabAm) {
                                        if (tabSlot + 1 < tabAmounts.length) {
                                            tabAm += tabAmounts[++tabSlot];
                                            if (tabSlot > 0 && tabAmounts[tabSlot - 1] % child.width == 0) {
                                                height--;
                                            }
                                            hh += 8;
                                        }
                                        break;
                                    }
                                } else if (settings[211] <= 9) {
                                    if (slot >= tabAmounts[settings[211]] + newSlot) {
                                        break heightLoop;
                                    }
                                }
                            }
                            if (slot >= child.inventoryItemId.length) {
                                continue;
                            }
                            if (rsi != null && searchingShops) {
                                if (searchShops && child.inventoryItemId[slot] > 0) {
                                    final ItemDefinition definition = ItemDefinition
                                            .get(child.inventoryItemId[slot] - 1);
                                    if (definition == null || definition.name == null
                                            || !definition.name.toLowerCase().contains(rsi.defaultText.toLowerCase())) {
                                        slot++;
                                        continue;
                                    }
                                }
                                results++;
                            }
                            if (search && child.inventoryItemId[slot] > 0) {
                                final ItemDefinition definition = ItemDefinition.get(child.inventoryItemId[slot] - 1);
                                if (definition == null || definition.name == null
                                        || !definition.name.toLowerCase().contains(promptInput.toLowerCase())) {
                                    slot++;
                                    continue;
                                }
                                results++;
                            }

                            int w = child_x_in_bounds + (search || searchShops ? (results % child.width) : width)
                                    * (32 + child.inventoryMarginX);
                            int h = (child_y_in_bounds + ((search || searchShops ? (results / child.width) : height))
                                    * (32 + child.inventoryMarginY)) + hh;

                            if (slot < 20) {
                                w += child.inventoryOffsetX[slot];
                                h += child.inventoryOffsetY[slot];
                            }

                            if (slot < child.inventoryItemId.length && child.inventoryItemId[slot] > 0) {
                                int x2 = 0;
                                int y2 = 0;
                                int itemId = child.inventoryItemId[slot] - 1;
                                // System.out.println("itemId: "+itemId);
                                if (w > Rasterizer2D.xClipStart - 32 && w < Rasterizer2D.xClipEnd
                                        && h > Rasterizer2D.yClipStart - 32 && h < Rasterizer2D.yClipEnd
                                        || activeInterfaceType != 0 && dragFromSlot == slot) {
                                    int color = 0;
                                    if (item_highlighted == 1 && selectedItemIdSlot == slot
                                            && interfaceitemSelectionTypeIn == child.id) {
                                        color = 0xFFFFFF;
                                    }
                                    SimpleImage itemSprite = ItemDefinition.getSprite(itemId,
                                            child.inventoryAmounts[slot], color);
                                    // Draw item sprites for NPC drops interface.
                                    if (child.id == 15100) {
                                        itemSprite = ItemDefinition.getSprite(itemId, child.inventoryAmounts[slot],
                                                color, 24, 24, false);
                                    }

                                    if (itemSprite != null) {
                                        if (activeInterfaceType != 0 && dragFromSlot == slot
                                                && focusedDragWidget == child.id) {
                                            draggedItem = itemSprite;
                                            x2 = MouseHandler.mouseX - mouseDragX;
                                            y2 = MouseHandler.mouseY - mouseDragY;
                                            if (x2 < 5 && x2 > -5)
                                                x2 = 0;
                                            if (y2 < 5 && y2 > -5)
                                                y2 = 0;
                                            if (draggingCycles < setting.drag_item_value) {
                                                x2 = 0;
                                                y2 = 0;
                                            }
                                            dragX = w + x2;
                                            if (dragX < Rasterizer2D.xClipStart) {
                                                dragX = Rasterizer2D.xClipStart - (x2);
                                                if (x2 < Rasterizer2D.xClipStart)
                                                    dragX = Rasterizer2D.xClipStart;
                                            }
                                            if (dragX > Rasterizer2D.xClipEnd - 32) {
                                                dragX = Rasterizer2D.xClipEnd - 32;
                                            }

                                            dragY = h + y2;
                                            if (dragY < Rasterizer2D.yClipStart && widget.scrollMax == 0) {
                                                dragY = Rasterizer2D.yClipStart - (y2);
                                                if (y2 < Rasterizer2D.yClipStart)
                                                    dragY = Rasterizer2D.yClipStart;
                                            }
                                            if (dragY > Rasterizer2D.yClipEnd - 32)
                                                dragY = Rasterizer2D.yClipEnd - 32;

                                            if (h + y2 < Rasterizer2D.yClipStart && widget.scrollPosition > 0) {
                                                int scrollValue = (animation_step * (Rasterizer2D.yClipStart - h - y2))
                                                        / 3;
                                                if (scrollValue > animation_step * 10)
                                                    scrollValue = animation_step * 10;
                                                if (scrollValue > widget.scrollPosition)
                                                    scrollValue = widget.scrollPosition;
                                                widget.scrollPosition -= scrollValue;
                                                mouseDragY += scrollValue;
                                            }
                                            if (h + y2 + 32 > Rasterizer2D.yClipEnd
                                                    && widget.scrollPosition < widget.scrollMax - widget.height) {
                                                int scrollValue = (animation_step
                                                        * ((h + y2 + 32) - Rasterizer2D.yClipEnd)) / 3;
                                                if (scrollValue > animation_step * 10) {
                                                    scrollValue = animation_step * 10;
                                                }
                                                if (scrollValue > widget.scrollMax - widget.height
                                                        - widget.scrollPosition) {
                                                    scrollValue = widget.scrollMax - widget.height
                                                            - widget.scrollPosition;
                                                }
                                                widget.scrollPosition += scrollValue;
                                                mouseDragY -= scrollValue;
                                            }
                                        } else if (atInventoryInterfaceType != 0 && atInventoryIndex == slot
                                                && atInventoryInterface == child.id) {
                                            itemSprite.drawSprite1(w, h);
                                        } else {
                                            if (child.alpha > 0) {
                                                if (child.id == 61026) {
                                                    if (child.inventoryAmounts[slot] == 0) {
                                                        itemSprite.draw_transparent(w, h, 90);
                                                    } else {
                                                        itemSprite.drawSprite(w, h);
                                                    }
                                                } else if (child.id == 73318) {
                                                    if (child.inventoryAmounts[slot] == 0) {
                                                        itemSprite.draw_transparent(w, h, 90);
                                                    } else {
                                                        itemSprite.drawSprite(w, h);
                                                    }
                                                } else {
                                                    itemSprite.draw_transparent(w, h, 180);
                                                }
                                            } else {
                                                RuneType runeType = RuneType.forId(itemId);
                                                int amount = child.inventoryAmounts[slot];
                                                if (widget_overlay_id == 34400 && child.parent == 5063
                                                        && child.id == 5064) {
                                                    itemSprite.draw_transparent(w, h, 110);
                                                } else if (child.parent == 5382 && amount == 0) {
                                                    itemSprite.draw_transparent(w, h, 110);
                                                } else if (child.parent == 3824 && amount == 0) {
                                                    itemSprite.draw_transparent(w, h, 110);
                                                } else if (widget_overlay_id == 48700 && runeType == null) {
                                                    itemSprite.draw_transparent(w, h, 110);
                                                } else if (child.parent == 26806
                                                        && (itemId == ItemIdentifiers.LOOTING_BAG
                                                                || itemId == ItemIdentifiers.LOOTING_BAG_22586)) {
                                                    itemSprite.draw_transparent(w, h, 110);
                                                } else {
                                                    itemSprite.drawSprite(w, h);
                                                }
                                            }
                                        }

                                        int amount = child.inventoryAmounts[slot];

                                        if (itemSprite.max_width == 33 || amount != 1) {
                                            final boolean showAmount = child.displayAmount;
                                            boolean flag = true;

                                            if (flag) {
                                                if (showAmount) {
                                                    // item container drawing
                                                    if (child.parent == 5382 && amount == 0) {
                                                        smallFont.draw("0", w + 1 + x2, h + 10 + y2, 0xFFE100, -1);
                                                    } else if (child.displayAmount) {
                                                        if (amount >= 1500000000 && child.drawInfinity) {
                                                            SpriteCache.get(105)
                                                                    .drawSprite(w, h);
                                                        } else {
                                                            if (amount >= -1 && amount < 1 && child.parent != 54301) {
                                                                smallFont.draw(set_k_or_m(amount), w + x2,
                                                                        h + 9 + y2, 0xa9a417, 0x201c17, 255);
                                                            }
                                                            if (amount >= 1 && amount < 100000) {
                                                                smallFont.draw(set_k_or_m(amount), w + x2,
                                                                        h + 9 + y2, 0xFFFF00, 0x000000, 255);
                                                            }
                                                            if (amount >= 100000 && amount < 10000000) {
                                                                smallFont.draw(set_k_or_m(amount), w + x2,
                                                                        h + 9 + y2, 0xFFFFFF, 0x000000, 255);
                                                            }
                                                            if (amount >= 10000000) {
                                                                smallFont.draw(set_k_or_m(amount), w + x2,
                                                                        h + 9 + y2, 0x00FF80, 0x000000, 255);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (child.sprites != null && slot < 20) {
                                SimpleImage childSprite = child.sprites[slot];
                                if (childSprite != null) {
                                    childSprite.drawSprite(w, h);
                                }
                            }
                            slot++;
                        }
                    }
                    if (draggedItem != null) {
                        draggedItem.drawSprite1(dragX, dragY, 200 + (int) (50 * Math.sin(tick / 10.0)),
                                child.contentType == 206);
                    }
                } else if (child.type == Widget.TYPE_RECTANGLE) {
                    boolean hover = focusedChatWidget == child.id || focusedSidebarWidget == child.id
                            || focusedViewportWidget == child.id;
                    int colour;
                    if (interfaceIsSelected(child)) {
                        colour = child.secondaryColor;
                        if (hover && child.secondaryHoverColor != 0)
                            colour = child.secondaryHoverColor;
                    } else {
                        colour = child.textColour;
                        if (hover && child.defaultHoverColor != 0)
                            colour = child.defaultHoverColor;
                    }
                    if (child.opacity == 0) {
                        if (child.filled)
                            Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width,
                                    child.height, colour);
                        else
                            Rasterizer2D.draw_rect_outline(child_x_in_bounds, child_y_in_bounds, child.width,
                                    child.height, colour);
                    } else if (child.filled)
                        Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                                colour,
                                255 - (child.opacity & 0xff));
                    else
                        Rasterizer2D.drawTransparentBoxOutline(child_x_in_bounds, child_y_in_bounds, child.width,
                                child.height, colour,
                                255 - (child.opacity & 0xff));
                } else if (child.type == Widget.TYPE_TEXT || child.type == Widget.TYPE_WRAPPING_TEXT) {
                    AdvancedFont font = child.text_type;

                    String text;

                    if (child.type == Widget.TYPE_WRAPPING_TEXT) {
                        text = Widget.getWrappedText(child.text_type, child.defaultText, child.width);
                    } else {
                        text = child.defaultText;
                    }

                    // if (child.id == 14945) {
                    // System.out.println("Font: " + font.anInt4142 + " | text: " + text);
                    // }

                    if (text == null) {
                        continue;
                    }
                    // text = text + " " + child.id;
                    if (child.parent == 2400) {
                        // System.out.println("(Widget) Text is " + text);
                        // System.out.println("font.ascent = " + font.ascent);
                        // System.out.println("child.height = " + child.height);
                        // System.out.println("child_y_in_bounds = " + child_y_in_bounds);
                        // text = text.replaceAll("\\R", "");
                        // text = text.replaceAll("\\n\\n\\n\\n", "");
                        // text = text.replaceAll("\n\n\n\n", "");
                        // text = text.replaceAll("\\n", "");
                        // text = text.replaceAll("\n", "");
                    }
                    if (child.id == 8147) {
                        // System.out.println("8147 text: " + child.defaultText);
                        // System.out.println("8147 font: " + child.text_type + " | X: " + childX + " |
                        // Y: " + childY);
                        // System.out.println("8147 center: " + child.centerText);

                    }

                    if (child.id == 8157) {
                        // System.out.println("8157 text: " + child.defaultText);
                        // System.out.println("8157 font: " + child.text_type + " | X: " + childX + " |
                        // Y: " + childY);
                        // System.out.println("8157 center: " + child.centerText);

                    }

                    SimpleImage sprite;
                    if (child.textIsClicked) {
                        sprite = child.textSpriteClicked;
                        sprite.drawSprite(child_x_in_bounds - child.textClickedX,
                                child_y_in_bounds - child.textClickedY, 0xffffff);
                    }

                    boolean flag1 = focusedChatWidget == child.id || focusedSidebarWidget == child.id
                            || focusedViewportWidget == child.id;
                    int colour;
                    if (interfaceIsSelected(child)) {
                        colour = child.secondaryColor;
                        if (flag1 && child.secondaryHoverColor != 0)
                            colour = child.secondaryHoverColor;
                        if (child.secondaryText.length() > 0)
                            text = child.secondaryText;
                    } else {
                        colour = child.textColour;
                        if (flag1 && child.defaultHoverColor != 0)
                            colour = child.defaultHoverColor;
                    }
                    if (child.optionType == Widget.OPTION_CONTINUE && continuedDialogue) {
                        text = "Please wait...";
                        if (child.id == 6209) { // Level up clicking continue by Suic
                            if (backDialogueId != -1) {
                                backDialogueId = -1;
                                update_chat_producer = true;
                            }
                        }
                        colour = child.textColour;
                    }

                    if ((backDialogueId != -1 || dialogueId != -1
                            || child.defaultText.contains("Click here to continue"))
                            && (widget.id == backDialogueId || widget.id == dialogueId)) {
                        if (colour == 0xffff00) {
                            colour = 255;
                        }
                        if (colour == 49152) {
                            colour = 0xffffff;
                        }
                    }

                    if ((child.parent == 1151) || (child.parent == 12855) || (child.parent == 24794)) {
                        switch (colour) {
                            case 16773120:
                                colour = 0xFE981F;
                                break;
                            case 7040819:
                                colour = 0xAF6A1A;
                                break;
                        }
                    }

                    int image = -1;

                    for (int drawY = child_y_in_bounds + font.ascent; text
                            .length() > 0; drawY += font.ascent) {
                        // Commented out this way of drawing it, it broke interface 2400. Who knows why
                        // it was here.
                        // drawY += (child.height > font.ascent ? child.height : font.ascent)) {// can
                        // set the offset for <br> by child.height +
                        // font.getHeight() + offset
                        if (image != -1) {

                            // CLAN CHAT LIST = 37128
                            if (child.parent == 37128) {
                                SpriteCache.get(image)
                                        .drawAdvancedSprite(child_x_in_bounds,
                                                drawY - SpriteCache.get(image).height - 1);
                                child_x_in_bounds += SpriteCache.get(image).width + 3;
                            } else {
                                SpriteCache.get(image)
                                        .drawAdvancedSprite(child_x_in_bounds,
                                                drawY - SpriteCache.get(image).height + 3);
                                child_x_in_bounds += SpriteCache.get(image).width + 4;
                            }
                        }

                        if (text.indexOf("%") != -1) {
                            do {
                                int index = text.indexOf("%1");
                                if (index == -1)
                                    break;
                                if (child.id < 4000 || child.id > 5000 && child.id != 13921 && child.id != 13922
                                        && child.id != 12171 && child.id != 12172) {
                                    text = text.substring(0, index) + formatCoins(executeScript(child, 0))
                                            + text.substring(index + 2);

                                } else {
                                    text = text.substring(0, index) + interfaceIntToString(executeScript(child, 0))
                                            + text.substring(index + 2);
                                }
                            } while (true);
                            do {
                                int index = text.indexOf("%2");
                                if (index == -1) {
                                    break;
                                }
                                text = text.substring(0, index) + interfaceIntToString(executeScript(child, 1))
                                        + text.substring(index + 2);
                            } while (true);
                            do {
                                int index = text.indexOf("%3");
                                if (index == -1) {
                                    break;
                                }

                                text = text.substring(0, index) + interfaceIntToString(executeScript(child, 2))
                                        + text.substring(index + 2);
                            } while (true);
                            do {
                                int index = text.indexOf("%4");
                                if (index == -1) {
                                    break;
                                }
                                text = text.substring(0, index) + interfaceIntToString(executeScript(child, 3))
                                        + text.substring(index + 2);
                            } while (true);
                            do {
                                int index = text.indexOf("%5");
                                if (index == -1) {
                                    break;
                                }

                                text = text.substring(0, index) + interfaceIntToString(executeScript(child, 4))
                                        + text.substring(index + 2);
                            } while (true);
                        }

                        // System.out.println("(Widget) Text is now " + text);
                        // For interfaces like 2400, it's not actually a newline character,
                        // It's literally stored in the cache as a backslash and an n.
                        int break1 = text.indexOf("\\n");
                        int break2 = text.indexOf("<br>");
                        String widgetText;
                        if (break1 != -1 || break2 != -1) {
                            widgetText = text.substring(0, (break2 != -1 ? break2 : break1));
                            text = text.substring((break2 != -1 ? break2 + 4 : break1 + 2));// was +2, changed it to +3,
                                                                                            // then back to +2 for
                                                                                            // interface 2400 since
                                                                                            // "\\n".length() is
                                                                                            // actually 2
                            // System.out.println(widgetText + "_" + text);//Debug string breaks
                        } else {
                            widgetText = text;
                            text = "";// empty the string field
                        }
                        if (child.centerText) {
                            font.drawCenteredString(
                                    widgetText.equals("Block") && child.id == 439
                                            && WeaponInterfacesWidget.weaponId == 13263 ? "Smash" : widgetText,
                                    child_x_in_bounds + (child.width / 2) + (widgetText.contains("(Off") ? 1 : 0),
                                    drawY, colour,
                                    child.textShadow ? 0 : -1);
                        } else if (child.rightText) {
                            font.draw(widgetText, child_x_in_bounds - font.getTextWidth(widgetText), drawY, colour,
                                    child.textShadow ? 0 : -1);
                        } else {
                            font.draw(widgetText, child_x_in_bounds, drawY, colour, child.textShadow ? 0 : -1);
                        }
                    }
                } else if (child.type == Widget.TYPE_CONFIG_BUTTON_HOVERED_SPRITE_OUTLINE) {
                    boolean flag = false;

                    if (child.toggled) {
                        child.enabledSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                        child.spriteWithOutline.draw_highlighted(child_x_in_bounds + child.hoveredOutlineSpriteXOffset,
                                child_y_in_bounds + child.hoveredOutlineSpriteYOffset, 0xffffff);
                        flag = true;
                    } else {
                        child.disabledSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                        child.spriteWithOutline.drawSprite(child_x_in_bounds + child.hoveredOutlineSpriteXOffset,
                                child_y_in_bounds + child.hoveredOutlineSpriteYOffset);
                    }
                    // Draw text
                    if (child.defaultText == null) {
                        continue;
                    }
                    if (child.centerText) {
                        child.text_type.drawCenteredString(child.defaultText, child_x_in_bounds + child.msgX,
                                child_y_in_bounds + child.msgY,
                                flag ? child.defaultHoverColor : child.textColour, 0);
                    } else {
                        child.text_type.draw(child.defaultText, child_x_in_bounds + 5, child_y_in_bounds + child.msgY,
                                flag ? child.defaultHoverColor : child.textColour, 0);
                    }
                } else if (child.type == Widget.TYPE_SPRITE) {
                    if (prayerGrabbed != null && prayerGrabbed.spriteId == child.id) {
                        continue;
                    }

                    // Added by suic for testing, comment this out because it breaks the logout
                    // button
                    // but don't remove it.
                    /*
                     * if (child.enabledSprite == null || child.disabledSprite == null) {
                     * continue;
                     * }
                     */
                    SimpleImage sprite;

                    if (child.spriteXOffset != 0) {
                        child_x_in_bounds += child.spriteXOffset;
                    }

                    if (child.spriteYOffset != 0) {
                        child_y_in_bounds += child.spriteYOffset;
                    }

                    if (interfaceIsSelected(child)) {
                        sprite = child.disabledSprite;
                    } else {
                        sprite = child.enabledSprite;
                    }

                    if (child.parent == 1764) {
                        ItemDefinition item = ItemDefinition.get(WeaponInterfacesWidget.weaponId);
                        if (item.name.contains("ross") || item.name.contains("'bow")) {
                            if (child.id == 1773) {
                                sprite = SpriteCache.get(825);
                            }
                            if (child.id == 1774) {
                                sprite = SpriteCache.get(823);
                            }
                            if (child.id == 1775) {
                                sprite = SpriteCache.get(824);
                            }
                        }
                    }

                    if (widget_highlighted == 1 && child.id == spellId && spellId != 0 && sprite != null) {
                        sprite.drawSpriteWithOutline(child_x_in_bounds, child_y_in_bounds, 0xffffff, true);
                    } else {
                        if (sprite != null) {

                            boolean drawTransparent = child.drawsTransparent;
                            boolean highDetail = child.hightDetail;

                            // Check if parent draws as transparent..
                            if (!drawTransparent && child.parent > 0 && Widget.cache[child.parent] != null) {
                                drawTransparent = Widget.cache[child.parent].drawsTransparent;
                            }

                            if (drawTransparent) {
                                sprite.draw_transparent(child_x_in_bounds, child_y_in_bounds, child.transparency);
                            } else if (!highDetail) {
                                sprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                            } else {
                                sprite.drawAdvancedSprite(child_x_in_bounds, child_y_in_bounds);
                            }
                        }
                    }
                } else if (child.type == 279) {

                    // System.out.println("Child type is 279 for child id: " + child.id);

                    int startX = child_x_in_bounds;
                    int startY = child_y_in_bounds; // might use later
                    int shiftX = child.inventoryMarginX;
                    int shiftY = child.inventoryMarginY;
                    int[] spritesToDraw = child.spritesToDraw;
                    int splitIndex = child.spritesPerRow;

                    for (int i = 0; i < spritesToDraw.length; i++) {
                        SimpleImage sprite = SpriteCache.get(spritesToDraw[i]);
                        if (i % splitIndex == 0 && i != 0) {
                            child_x_in_bounds = startX;
                            child_y_in_bounds += shiftY;
                        } else {
                            if (i != 0) {
                                child_x_in_bounds += shiftX;
                            }
                        }
                        // System.out.println("Drawing sprite at: " + child_x_in_bounds + " | " +
                        // child_y_in_bounds);
                        sprite.drawSprite(child_x_in_bounds, child_y_in_bounds);

                    }

                } else if (child.type == Widget.TYPE_SPELL_SPRITE) {
                    SimpleImage sprite;

                    if (child.spriteXOffset != 0) {
                        child_x_in_bounds += child.spriteXOffset;
                    }

                    if (child.spriteYOffset != 0) {
                        child_y_in_bounds += child.spriteYOffset;
                    }

                    if (interfaceIsSelected(child)) {
                        sprite = child.disabledSprite;
                    } else {
                        sprite = child.enabledSprite;
                    }

                    if (widget_highlighted == 1 && child.id == spellId && spellId != 0 && sprite != null) {
                        sprite.draw_highlighted(child_x_in_bounds, child_y_in_bounds, 0xffffff);
                    } else {
                        if (sprite != null) {

                            boolean drawTransparent = child.drawsTransparent;

                            // Check if parent draws as transparent..
                            if (!drawTransparent && child.parent > 0 && Widget.cache[child.parent] != null) {
                                drawTransparent = Widget.cache[child.parent].drawsTransparent;
                            }

                            if (drawTransparent) {
                                sprite.draw_transparent(child_x_in_bounds, child_y_in_bounds, child.transparency);
                            } else {
                                // if (autoCastId != 65535) {
                                // TODO: check if this != 1 check breaks anything since it fixes trident
                                // autocast book sprites.
                                if (autoCastId != 65535 && autoCastId != 1) {
                                    Widget spellSprite = Widget.cache[autoCastId];
                                    sprite = spellSprite.disabledSprite;
                                    // System.out.println("autocastid " + autoCastId);
                                    // Since we re-assign sprite here, we may need to
                                    if (sprite != null) {
                                        sprite.drawSprite(child_x_in_bounds + 2, child_y_in_bounds + 2);
                                    } else {
                                        // System.err.println("Spell sprite seems to be null.");
                                    }
                                } else {
                                    sprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                                }
                            }
                        }
                    }
                } else if (child.type == Widget.TYPE_MODEL) {
                    Rasterizer3D.drawImage(child.width / 2 + child_x_in_bounds, child.height / 2 + child_y_in_bounds);
                    int sine = Rasterizer3D.SINE[child.modelAngleX] * child.modelZoom >> 16;
                    int cosine = Rasterizer3D.COSINE[child.modelAngleX] * child.modelZoom >> 16;

                    boolean selected = interfaceIsSelected(child);
                    int emoteAnimation;
                    if (selected)
                        emoteAnimation = child.secondaryAnimationId;
                    else
                        emoteAnimation = child.defaultAnimationId;

                    Model model;
                    try {
                        if (emoteAnimation == -1) {
                            model = child.getAnimatedModel(null, -1, selected);
                        } else {
                            SequenceDefinition sequenceDefinition = SequenceDefinition.get(emoteAnimation);
                            model = child.getAnimatedModel(
                                    sequenceDefinition,
                                    child.currentFrame,
                                    selected);
                        }

                        if (model != null) {
                            model.calculateBoundsCylinder();
                            model.renderonGpu = false;
                            model.renderModel(child.modelAngleY, 0, child.modelAngleX, 0, sine, cosine);
                            model.renderonGpu = true;
                        }
                    } catch (Exception e) {
                    }

                    Rasterizer3D.clips.method1358();
                } else if (child.type == Widget.TYPE_ITEM_LIST) {
                    AdvancedFont font = child.text_type;
                    int slot = 0;
                    for (int row = 0; row < child.height; row++) {
                        for (int column = 0; column < child.width; column++) {
                            if (child.inventoryItemId[slot] > 0) {
                                ItemDefinition item = ItemDefinition.get(child.inventoryItemId[slot] - 1);
                                bars.setConsume(StatusBars.Restore.get(item.id));
                                String name = item.name;
                                if (item.stackable || child.inventoryAmounts[slot] != 1)
                                    name = name + " x" + intToKOrMilLongName(child.inventoryAmounts[slot]);
                                int spriteX = child_x_in_bounds + column * (115 + child.inventoryMarginX);
                                int spriteY = child_y_in_bounds + row * (12 + child.inventoryMarginY);
                                if (child.centerText)
                                    font.drawCenteredString(name, spriteX + child.width / 2, spriteY, child.textColour,
                                            child.textShadow);
                                else
                                    font.draw(name, spriteX, spriteY, child.textColour, child.textShadow);
                            }
                            slot++;
                        }
                    }
                } else if (child.type == Widget.TYPE_OTHER
                        && (chatTooltipSupportId == child.id || tabTooltipSupportId == child.id
                                || gameTooltipSupportId == child.id)
                        && anInt1501 == tooltipDelay && !isMenuOpen && prayerGrabbed == null) {
                    AdvancedFont font = regularFont;
                    String text = child.defaultText;
                    String tooltip;
                    int break_index_old;
                    int break_index_new;
                    int box_width = 0;
                    int box_height = 0;

                    if (child.parent == 3917) {
                        return;
                    }

                    if (child.hoverXOffset != 0) {
                        child_x_in_bounds += child.hoverXOffset;
                    }

                    if (child.hoverYOffset != 0) {
                        child_y_in_bounds += child.hoverYOffset;
                    }

                    // calculate tooltip box size
                    for (text = widget_tooltip_text_script(text, child); text.length() > 0; box_height = box_height
                            + font.ascent + 1) {
                        break_index_old = text.indexOf("\\n");
                        break_index_new = text.indexOf("<br>");
                        if (break_index_old != -1 || break_index_new != -1) {
                            tooltip = text.substring(0, (break_index_new != -1 ? break_index_new : break_index_old));
                            text = text.substring((break_index_new != -1 ? break_index_new + 4 : break_index_old + 2));
                        } else {
                            tooltip = text;
                            text = "";
                        }
                        int text_width = font.getTextWidth(tooltip);
                        if (text_width > box_width) {
                            box_width = text_width;
                        }
                    }

                    box_width += 6;
                    box_height += 7;
                    int x_pos = (child_x_in_bounds + child.width) - 5 - box_width;
                    int y_pos = child_y_in_bounds + child.height + 5;
                    if (x_pos < child_x_in_bounds + 5) {
                        x_pos = child_x_in_bounds + 5;
                    }
                    if (x_pos + box_width > x + widget.width) {
                        x_pos = (x + widget.width) - box_width;
                    }
                    if (y_pos + box_height > y + widget.height) {
                        y_pos = (child_y_in_bounds - box_height);
                    }

                    // Skill tab
                    if (child.parent >= 14918 && child.parent <= 14941) {
                        if (child.inventoryHover) {
                            if (x_pos + box_width > canvasWidth - 26) {
                                x_pos = canvasWidth - (box_width) - 26;
                            }
                            if (y_pos + box_height > canvasHeight - 38
                                    && y_pos + box_height < canvasHeight - 18) {
                                y_pos = canvasHeight - (box_height) - 108;
                            } else if (y_pos + box_height >= canvasHeight - 18) {
                                y_pos = canvasHeight - (box_height) - (box_height == 46 ? 76 : 75);
                            }
                            if (x_pos == canvasWidth - 146) {
                                x_pos = canvasWidth - 147;
                            }
                        }
                    }

                    Rasterizer2D.draw_filled_rect(x_pos, y_pos, box_width, box_height, 0xFFFFA0);
                    Rasterizer2D.draw_rect_outline(x_pos, y_pos, box_width, box_height, 0);

                    text = child.defaultText;
                    int text_y = y_pos + font.ascent + 2;
                    for (text = widget_tooltip_text_script(text, child); text.length() > 0; text_y = text_y
                            + font.ascent + 1) {
                        break_index_old = text.indexOf("\\n");
                        break_index_new = text.indexOf("<br>");
                        if (break_index_old != -1 || break_index_new != -1) {
                            tooltip = text.substring(0, break_index_new != -1 ? break_index_new : break_index_old);
                            text = text.substring((break_index_new != -1 ? break_index_new + 4 : break_index_old + 2));
                        } else {
                            tooltip = text;
                            text = "";
                        }

                        font.draw(tooltip, x_pos + 3, text_y, 0, -1);
                    }
                } else if (child.type == Widget.HOVER_BUTTONS_NEW) {
                    if (child.toggled) {
                        child.enabledSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                    } else {
                        child.disabledSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                    }
                    if (child.centerText)
                        child.text_type.drawCenteredString(child.defaultText, child_x_in_bounds + child.msgX,
                                child_y_in_bounds + child.msgY, child.textColour, 0x000000);
                    else
                        child.text_type.draw(child.defaultText, child_x_in_bounds + 5 + child.msgX,
                                child_y_in_bounds + child.msgY, child.textColour, 0x00000);
                } else if (child.type == Widget.TYPE_HOVER || child.type == Widget.TYPE_CONFIG_HOVER) {
                    // Draw sprite
                    boolean flag = false;

                    if (child.toggled) {
                        child.enabledSprite.drawAdvancedSprite(child_x_in_bounds, child_y_in_bounds,
                                child.spriteOpacity);
                        flag = true;
                    } else {
                        child.disabledSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                    }

                    // Draw text
                    if (child.defaultText == null) {
                        continue;
                    }

                    if (child.centerText) {
                        if (child.text_type != null && child.defaultText != null)
                            child.text_type.drawCenteredString(child.defaultText, child_x_in_bounds + child.msgX,
                                    child_y_in_bounds + child.msgY, flag ? child.defaultHoverColor : child.textColour,
                                    0);
                    } else {
                        if (child.text_type != null && child.defaultText != null)
                            child.text_type.draw(child.defaultText, child_x_in_bounds + 5,
                                    child_y_in_bounds + child.msgY, flag ? child.defaultHoverColor : child.textColour,
                                    0);
                    }
                } else if (child.type == Widget.TYPE_CONFIG) {
                    SimpleImage sprite = child.active ? child.disabledSprite : child.enabledSprite;
                    sprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                } else if (child.type == Widget.TYPE_SLIDER) {
                    Slider slider = child.slider;
                    if (slider != null) {
                        slider.draw(child_x_in_bounds, child_y_in_bounds);
                    }
                } else if (child.type == Widget.TYPE_DROPDOWN) {
                    DropdownMenu d = child.dropdown;

                    int bgColour = child.dropdownColours[2];
                    int fontColour = 0xfe971e;
                    int downArrow = 609;

                    if (child.hovered || d.isOpen()) {
                        downArrow = 610;
                        fontColour = 0xffb83f;
                        bgColour = child.dropdownColours[3];
                    }

                    Rasterizer2D.drawPixels(20, child_y_in_bounds, child_x_in_bounds, child.dropdownColours[0],
                            d.getWidth());
                    Rasterizer2D.drawPixels(18, child_y_in_bounds + 1, child_x_in_bounds + 1, child.dropdownColours[1],
                            d.getWidth() - 2);
                    Rasterizer2D.drawPixels(16, child_y_in_bounds + 2, child_x_in_bounds + 2, bgColour,
                            d.getWidth() - 4);

                    int xOffset = child.centerText ? 3 : 16;
                    if (widget.id == 41900) {
                        regularFont.drawCenteredString(d.getSelected(),
                                child_x_in_bounds + (d.getWidth() - xOffset) / 2,
                                child_y_in_bounds + 14, fontColour, 0);
                    } else {
                        smallFont.drawCenteredString(d.getSelected(), child_x_in_bounds + (d.getWidth() - xOffset) / 2,
                                child_y_in_bounds + 14, fontColour, 0);
                    }

                    if (d.isOpen()) {
                        // Up arrow
                        SpriteCache.get(608)
                                .drawSprite(child_x_in_bounds + d.getWidth() - 18, child_y_in_bounds + 2);

                        Rasterizer2D.drawPixels(d.getHeight(), child_y_in_bounds + 19, child_x_in_bounds,
                                child.dropdownColours[0],
                                d.getWidth());
                        Rasterizer2D.drawPixels(d.getHeight() - 2, child_y_in_bounds + 20, child_x_in_bounds + 1,
                                child.dropdownColours[1],
                                d.getWidth() - 2);
                        Rasterizer2D.drawPixels(d.getHeight() - 4, child_y_in_bounds + 21, child_x_in_bounds + 2,
                                child.dropdownColours[3],
                                d.getWidth() - 4);

                        int yy = 2;
                        for (int i = 0; i < d.getOptions().length; i++) {
                            if (child.dropdownHover == i) {
                                Rasterizer2D.drawPixels(13, child_y_in_bounds + 19 + yy, child_x_in_bounds + 2,
                                        child.dropdownColours[4],
                                        d.getWidth() - 4);
                                if (widget.id == 41900) {
                                    regularFont.drawCenteredString(d.getOptions()[i],
                                            child_x_in_bounds + (d.getWidth() - xOffset) / 2,
                                            child_y_in_bounds + 29 + yy, 0xffb83f, 0);
                                } else {
                                    smallFont.drawCenteredString(d.getOptions()[i],
                                            child_x_in_bounds + (d.getWidth() - xOffset) / 2,
                                            child_y_in_bounds + 29 + yy, 0xffb83f, 0);
                                }

                            } else {
                                Rasterizer2D.drawPixels(13, child_y_in_bounds + 19 + yy, child_x_in_bounds + 2,
                                        child.dropdownColours[3],
                                        d.getWidth() - 4);
                                if (widget.id == 41900) {
                                    regularFont.drawCenteredString(d.getOptions()[i],
                                            child_x_in_bounds + (d.getWidth() - xOffset) / 2,
                                            child_y_in_bounds + 29 + yy, 0xfe971e, 0);
                                } else {
                                    smallFont.drawCenteredString(d.getOptions()[i],
                                            child_x_in_bounds + (d.getWidth() - xOffset) / 2,
                                            child_y_in_bounds + 29 + yy, 0xfe971e, 0);
                                }
                            }
                            yy += 14;
                        }
                        drawScrollbar(d.getHeight() - 4, child.scrollPosition, child_y_in_bounds + 21,
                                child_x_in_bounds + d.getWidth() - 18,
                                d.getHeight() - 5, false);

                    } else {
                        SpriteCache.get(downArrow)
                                .drawSprite(child_x_in_bounds + d.getWidth() - 18, child_y_in_bounds + 2);
                    }
                } else if (child.type == Widget.TYPE_KEYBINDS_DROPDOWN) {

                    DropdownMenu d = child.dropdown;

                    // If dropdown inverted, don't draw following 2 menus
                    if (dropdownInversionFlag > 0) {
                        dropdownInversionFlag--;
                        continue;
                    }

                    Rasterizer2D.drawPixels(18, child_y_in_bounds + 1, child_x_in_bounds + 1, 0x544834,
                            d.getWidth() - 2);
                    Rasterizer2D.drawPixels(16, child_y_in_bounds + 2, child_x_in_bounds + 2, 0x2e281d,
                            d.getWidth() - 4);
                    regularFont.draw(d.getSelected(), child_x_in_bounds + 7, child_y_in_bounds + 15, 0xff8a1f, 0);
                    SpriteCache.get(449)
                            .drawSprite(child_x_in_bounds + d.getWidth() - 18, child_y_in_bounds + 2);

                    if (d.isOpen()) {

                        Widget.cache[child.id - 1].active = true; // Alter stone colour

                        int yPos = child_y_in_bounds + 18;

                        // Dropdown inversion for lower stones
                        if (child.inverted) {
                            yPos = child_y_in_bounds - d.getHeight() - 10;
                            dropdownInversionFlag = 2;
                        }

                        Rasterizer2D.drawPixels(d.getHeight() + 17, yPos, child_x_in_bounds + 1, 0x544834,
                                d.getWidth() - 2);
                        Rasterizer2D.drawPixels(d.getHeight() + 15, yPos + 1, child_x_in_bounds + 2, 0x2e281d,
                                d.getWidth() - 4);

                        int yy = 2;
                        int xx = 0;
                        int bb = d.getWidth() / 2;

                        for (int i = 0; i < d.getOptions().length; i++) {

                            int fontColour = 0xff981f;
                            if (child.dropdownHover == i) {
                                fontColour = 0xffffff;
                            }

                            if (xx == 0) {
                                regularFont.draw(d.getOptions()[i], child_x_in_bounds + 5, yPos + 14 + yy, fontColour,
                                        0x2e281d);
                                xx = 1;

                            } else {
                                regularFont.draw(d.getOptions()[i], child_x_in_bounds + 5 + bb, yPos + 14 + yy,
                                        fontColour,
                                        0x2e281d);
                                xx = 0;
                                yy += 15;
                            }
                        }
                    } else {
                        Widget.cache[child.id - 1].active = false;
                    }
                } else if (child.type == Widget.TYPE_ADJUSTABLE_CONFIG) {

                    int totalWidth = child.width;
                    int spriteWidth = child.disabledSprite.width;
                    int totalHeight = child.height;
                    int spriteHeight = child.disabledSprite.height;
                    SimpleImage behindSprite = child.active ? child.enabledAltSprite : child.disabledAltSprite;

                    if (child.toggled) {
                        behindSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                        child.disabledSprite.drawAdvancedSprite(child_x_in_bounds + (totalWidth / 2) - spriteWidth / 2,
                                child_y_in_bounds + (totalHeight / 2) - spriteHeight / 2, child.spriteOpacity);
                    } else {
                        behindSprite.drawSprite(child_x_in_bounds, child_y_in_bounds);
                        child.disabledSprite.drawSprite(child_x_in_bounds + (totalWidth / 2) - spriteWidth / 2,
                                child_y_in_bounds + (totalHeight / 2) - spriteHeight / 2);
                    }
                } else if (child.type == Widget.TYPE_BOX) {
                    // Draw outline
                    Rasterizer2D.draw_filled_rect(child_x_in_bounds - 2, child_y_in_bounds - 2, child.width + 4,
                            child.height + 4, 0x0e0e0c);
                    Rasterizer2D.draw_filled_rect(child_x_in_bounds - 1, child_y_in_bounds - 1, child.width + 2,
                            child.height + 2, 0x474745);
                    // Draw base box
                    if (child.toggled) {
                        Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                                child.secondaryHoverColor);
                    } else {
                        Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                                child.defaultHoverColor);
                    }
                } else if (child.type == Widget.CLICKABLE_SPRITES) {
                    if (child.backgroundSprites.length > 1) {
                        if (child.enabledSprite != null) {
                            child.enabledSprite.drawAdvancedSprite(child_x_in_bounds, child_y_in_bounds);
                        }
                    }
                } else if (child.type == Widget.TYPE_INPUT_FIELD) {
                    drawInputField(child, x, y, child_x_in_bounds, child_y_in_bounds, child.width, child.height);
                } else if (child.type == Widget.DARKEN) {
                    if (child.id != 66010) {
                        Rasterizer2D.setDrawingArea(0, 0, canvasWidth, canvasHeight);
                    }
                    Rasterizer2D.fillRectangle(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                            child.fillColor, child.opacity);
                    Rasterizer2D.setDrawingArea(x, y, x + child.width, y + widget.height);
                } else if (child.type == Widget.OUTLINE) {
                    int color = child.parent == 34006 || child.parent == 34025 ? 0x494034 : 0x383023;
                    Rasterizer2D.draw_rect_outline(child_x_in_bounds - 1, child_y_in_bounds - 1, child.width + 2,
                            child.height + 2, 0x383023);
                    Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                            child.color, child.transparency);
                } else if (child.type == Widget.LINE) {
                    Rasterizer2D.draw_horizontal_line(child_x_in_bounds, child_y_in_bounds, child.width, child.color);
                } else if (child.type == Widget.COLOR) {
                    Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                            child.color,
                            child.transparency);
                } else if (child.type == Widget.DRAW_LINE) {
                    if (child instanceof DrawLine) {
                        DrawLine inter = (DrawLine) child;
                        if (inter.getLineType() == DrawLine.LineType.HORIZONTAL) {
                            Rasterizer2D.drawTransparentHorizontalLine(child_x_in_bounds, child_y_in_bounds,
                                    child.width, child.textColour, child.opacity2);
                        } else if (inter.getLineType() == DrawLine.LineType.VERTICAL) {
                            int localHeight = child.width;
                            Rasterizer2D.drawTransparentVerticalLine(child_x_in_bounds, child_y_in_bounds, localHeight,
                                    child.textColour, child.opacity2);
                        }
                    }
                } else if (child.type == Widget.TYPE_DRAW_PIXELS) {
                    Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                            child.fillColor, child.opacity);
                    Rasterizer2D.draw_rect_outline(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                            child.borderColor);
                } else if (child.type == OPTION_MENU && child instanceof OptionMenuInterface) {
                    final OptionMenuInterface inter = (OptionMenuInterface) child;
                    if (!inter.getOptionMenus().isEmpty()) {
                        int drawX = child_x_in_bounds + 2;
                        int drawY = child_y_in_bounds + 2;
                        final int boxWidth = inter.width - 4;
                        final int boxHeight = 40;
                        for (OptionMenu menu : inter.getOptionMenus()) {
                            if (menu == null) {
                                continue;
                            }
                            Rasterizer2D.drawPixels(boxHeight, drawY, drawX, menu.isHighlighted() ? 0x695B36 : 0x3B3629,
                                    boxWidth);
                            Rasterizer2D.fillPixels(drawX, boxWidth, boxHeight, 0, drawY);

                            regularFont.draw(menu.getOptionName(), drawX + 5, drawY + 17, 0xFF981F, -1);
                            smallFont.draw(menu.getOptionTooltip(), drawX + 5, drawY + 33, 0xFFA945, -1);

                            // Teleport menu only
                            if (inter.id == 72156) {
                                inter.getSprites()[0].drawSprite(drawX + boxWidth - inter.getSprites()[0].width - 4,
                                        drawY + 3);
                            } else if (inter.id == 72206) {
                                inter.getSprites()[0].drawSprite(drawX + boxWidth - inter.getSprites()[0].width - 4,
                                        drawY + 3);
                                inter.getSprites()[1].drawSprite(drawX + boxWidth - inter.getSprites()[1].width - 6,
                                        drawY + 17);
                                inter.getSprites()[2].drawSprite(drawX + boxWidth - inter.getSprites()[2].width - 6,
                                        drawY + 28);
                            }
                            menu.setHighlighted(false);
                            drawY += 42;
                        }
                    }
                    if (inter.defaultText.length() > 0) {
                        regularFont.drawCenteredString(inter.defaultText, 5 + child_x_in_bounds + inter.width / 2,
                                child_y_in_bounds + inter.height / 2, 0xFF981F, -1);
                    }
                } else if (child.type == Widget.DRAW_BOX) {
                    Rasterizer2D.drawBox(child_x_in_bounds, child_y_in_bounds, child.width, child.height,
                            child.borderWidth, child.borderColor, child.secondaryColor, child.transparency);

                    if (child.filled) {
                        Rasterizer2D.fillRectangle(child.fillColor, child_y_in_bounds + child.borderWidth,
                                child.width - child.borderWidth * 2 + 1, child.height - child.borderWidth * 2 - 1,
                                child.customOpacity, child_x_in_bounds + child.borderWidth);
                    }
                } else if (child.type == Widget.PROGRESS_BAR) {
                    try {
                        String progress = Widget.cache[child.id].defaultText;

                        if (!progress.contains("/")) {
                            continue;
                        }

                        int current = Integer.parseInt(progress.substring(0, progress.indexOf("/")));

                        int maximum = Integer.parseInt(progress.substring(progress.indexOf("/") + 1));
                        if (current > maximum) {
                            current = maximum;
                        }

                        int width = child.width;
                        int height = child.height;
                        int color = getProgressBarColor(widget.id, child.id,
                                (int) ((double) current / (double) maximum * 100));

                        Rasterizer2D.drawRectangle(child_x_in_bounds - 1, child_y_in_bounds - 1, child.width - 5,
                                child.height + 2, 0, 250);
                        Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds, width - 7, height,
                                child.progressBackColor, child.progressBackAlpha);
                        Rasterizer2D.draw_filled_rect(child_x_in_bounds, child_y_in_bounds,
                                (int) ((double) current / maximum * width - 7), height, color, 200);

                        if (child.drawProgressText) {
                            AdvancedFont font = child.text_type == null ? smallFont : child.text_type;
                            font.drawCenteredString(current + " / " + maximum, child_x_in_bounds + (width - 8) / 2,
                                    child_y_in_bounds + height / 2 + 5, 0xFFFFFF, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
        Rasterizer2D.setDrawingArea(clipLeft, clipTop, clipRight, clipBottom);
        if (!isResized()) {
            callbacks.drawInterface(WidgetID.FIXED_VIEWPORT_GROUP_ID, Collections.emptyList());
        } else {
            callbacks.drawInterface(WidgetID.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX_GROUP_ID, Collections.emptyList());
        }
    }

    public static int getProgressBarColor(int mainId, int childId, int percent) {
        if (childId == HealthHud.PROGRESS_WIDGET_ID) {
            return Widget.cache[childId].fillColor;
        }

        if (percent <= 15) {
            return 0xFF0000;
        }

        if (percent <= 45) {
            return 0xffa500;
        }

        if (percent <= 65) {
            return 0xffa500;
        }

        if (percent <= 75) {
            return 0xffa500;
        }

        if (percent <= 99) {
            return 0xFFFF00;
        }

        return 0x00FF00;
    }

    public int brightness;

    public int getSpriteBrightness() {
        brightness = 4;
        if (brightness == 4) {
            return 255;
        } else if (brightness == 3) {
            return 225;
        } else if (brightness == 2) {
            return 195;
        } else
            return 182;
    }

    public static String[] addLinebreaks(String input, double maxCharInLine) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            while (word.length() > maxCharInLine) {
                output.append(word.substring(0, (int) (maxCharInLine) - lineLen) + "\n");
                word = word.substring((int) (maxCharInLine) - lineLen);
                lineLen = 0;
            }
            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");
            lineLen += word.length() + 1;
        }
        return output.toString().split("\n");
    }

    public static int tooltipDelay = 25;

    public void drawSpecialAttack(Widget widget) {
        boolean fixed = !resized;
        final int xOffset = !isResized() ? 516 : canvasWidth - 241;
        final int yOffset = !isResized() ? 168 : canvasHeight - 336;
        /** Blue special attack bar **/
        for (int i = 0; i < SpecialAttackBars.values().length; i++) {
            if (widget.id == SpecialAttackBars.values()[i].readInterfaceId()
                    && WeaponInterfacesWidget.weaponId == SpecialAttackBars.values()[i].getItemId()) {

                if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1) {
                    int x = fixed ? 51 + xOffset : canvasWidth - 178;
                    int y = fixed ? 242 + yOffset : canvasWidth < 1000 ? canvasHeight - 135 : canvasHeight - 97;
                    SpriteCache.get(2158).drawSprite(x, y);
                    SimpleImage bar = new SimpleImage(
                            SpriteCache.metaData(specialAttack >= SpecialAttackBars.values()[i]
                                    .getSpecialAmount() ? 2159 : 2160).pixels,
                            (142 / 100.0f) * specialAttack, 11);

                    int x2 = fixed ? 55 + xOffset : canvasWidth - 174;
                    int y2 = fixed ? 249 + yOffset : canvasWidth < 1000 ? canvasHeight - 128 : canvasHeight - 90;
                    bar.drawShadedSprite(x2, y2, getSpriteBrightness());
                } else {
                    SpriteCache.get(2158).drawSprite(fixed ? 51 + xOffset : canvasWidth - 194,
                            fixed ? 242 + yOffset : canvasHeight - 94);
                    SimpleImage bar = new SimpleImage(
                            SpriteCache.metaData(specialAttack >= SpecialAttackBars.values()[i]
                                    .getSpecialAmount() ? 2159 : 2160).pixels,
                            (142 / 100.0f) * specialAttack, 11);
                    bar.drawShadedSprite(fixed ? 55 + xOffset : canvasWidth - 190,
                            fixed ? 249 + yOffset : canvasHeight - 87, getSpriteBrightness());
                }
            }
        }
        boolean specialAttackBarHover = MouseHandler.mouseX >= (fixed ? 569 : canvasWidth - 192)
                && MouseHandler.mouseX <= (fixed ? 718 : canvasWidth - 50)
                && MouseHandler.mouseY >= (fixed ? 413 : canvasHeight - 89)
                && MouseHandler.mouseY <= (fixed ? 439 : canvasHeight - 67);
        /** Tooltips **/
        if (anInt1501 < tooltipDelay && specialAttackBarHover) {
            anInt1501++;
        } else {
            int boxLength;
            int boxHeight;
            for (int i = 0; i < SpecialAttackBars.values().length; i++) {
                if (widget.id == SpecialAttackBars.values()[i].readInterfaceId()
                        && WeaponInterfacesWidget.weaponId == SpecialAttackBars.values()[i].getItemId()
                        && specialAttackBarHover) {
                    String tooltip = SpecialAttackBars.values()[i].getTooltip() + " ("
                            + (int) (SpecialAttackBars.values()[i].getSpecialAmount()) + "%)";
                    if (WeaponInterfacesWidget.weaponId == 11235 || WeaponInterfacesWidget.weaponId == 12768
                            || WeaponInterfacesWidget.weaponId == 12767 || WeaponInterfacesWidget.weaponId == 12766
                            || WeaponInterfacesWidget.weaponId == 12765) {
                        if (WeaponInterfacesWidget.ammoId == 11212 || WeaponInterfacesWidget.ammoId == 11227
                                || WeaponInterfacesWidget.ammoId == 11228 || WeaponInterfacesWidget.ammoId == 11229) {
                            tooltip = "Descent of Dragons: Deal a double attack with dragon arrows that inflicts up to 50% more damage (minimum damage of 8 per hit).";
                        }
                    }
                    String[] tooltipArray = addLinebreaks(tooltip,
                            SpecialAttackBars.values()[i].getItemId() == 1215
                                    || SpecialAttackBars.values()[i].getItemId() == 1231
                                    || SpecialAttackBars.values()[i].getItemId() == 5680
                                    || SpecialAttackBars.values()[i].getItemId() == 5698 ? 25 : 26);
                    boxLength = regularFont.getTextWidth(tooltipArray[0]);
                    for (int lengthLoop = 0; lengthLoop < tooltipArray.length; lengthLoop++) {
                        if (regularFont.getTextWidth(tooltipArray[lengthLoop]) > boxLength) {
                            boxLength = regularFont.getTextWidth(tooltipArray[lengthLoop]);
                        }
                    }
                    boxHeight = 7 + (tooltipArray.length * 12);
                    Rasterizer2D.draw_filled_rect(fixed ? 55 : canvasWidth - 190,
                            fixed ? 237 - boxHeight : (canvasHeight - boxHeight) - 100, boxLength + 4, boxHeight,
                            0xFFFFA0);
                    Rasterizer2D.draw_rect_outline(fixed ? 55 : canvasWidth - 190,
                            fixed ? 237 - boxHeight : (canvasHeight - boxHeight) - 100, boxLength + 4, boxHeight, 0);
                    for (int tooltipSplit = 0; tooltipSplit < tooltipArray.length; tooltipSplit++) {
                        regularFont
                                .draw(tooltipArray[tooltipSplit], fixed ? 57 : canvasWidth - 188,
                                        fixed ? (250 - boxHeight) + (12 * tooltipSplit)
                                                : (canvasHeight - boxHeight) + (12 * tooltipSplit) - 87,
                                        0x000000, 0xFFFFA0);
                    }
                }
            }
        }
    }

    // Bank vars
    private final int[] tabAmounts = new int[] { 350, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    int modifiableXValue;

    private void randomizeBackground(IndexedImage background) {
        int j = 256;
        for (int k = 0; k < anIntArray1190.length; k++)
            anIntArray1190[k] = 0;

        for (int l = 0; l < 5000; l++) {
            int i1 = (int) (Math.random() * 128D * (double) j);
            anIntArray1190[i1] = (int) (Math.random() * 256D);
        }
        for (int j1 = 0; j1 < 20; j1++) {
            for (int k1 = 1; k1 < j - 1; k1++) {
                for (int i2 = 1; i2 < 127; i2++) {
                    int k2 = i2 + (k1 << 7);
                    anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128]
                            + anIntArray1190[k2 + 128]) / 4;
                }

            }
            int[] ai = anIntArray1190;
            anIntArray1190 = anIntArray1191;
            anIntArray1191 = ai;
        }
        if (background != null) {
            int l1 = 0;
            for (int j2 = 0; j2 < background.height; j2++) {
                for (int l2 = 0; l2 < background.width; l2++)
                    if (background.palettePixels[l1++] != 0) {
                        int i3 = l2 + 16 + background.xOffset;
                        int j3 = j2 + 16 + background.yOffset;
                        int k3 = i3 + (j3 << 7);
                        anIntArray1190[k3] = 0;
                    }
            }
        }
    }

    private void appendPlayerUpdateMask(int mask, int index, Buffer buffer, Player player) {
        if ((mask & 0x400) != 0) {
            int initialX = buffer.readUByteS();
            int initialY = buffer.readUByteS();
            int destinationX = buffer.readUByteS();
            int destinationY = buffer.readUByteS();
            int startForceMovement = buffer.readLEUShortA() + tick;
            int endForceMovement = buffer.readUShortA() + tick;
            int animation = buffer.readLEUShortA();
            int direction = buffer.readUByteS();

            player.initialX = initialX;
            player.initialY = initialY;
            player.destinationX = destinationX;
            player.destinationY = destinationY;
            player.initiate_movement = startForceMovement;
            player.cease_movement = endForceMovement;
            player.direction = direction;

            if (animation >= 0) {
                player.sequence = animation;
                player.sequenceFrame = 0;
                player.sequenceFrameCycle = 0;
                player.currentAnimationLoops = 0;
                player.anim_delay = player.pathLength;
            }

            player.resetPath();
        }
        if ((mask & 0x100) != 0) {
            player.readSpotAnimation(buffer);
        }
        if ((mask & 8) != 0) {
            int animation = buffer.readLEUShort();
            if (animation == 65535)
                animation = -1;
            int delay = buffer.readNegUByte();

            if (animation == player.sequence && animation != -1) {
                int replayMode = SequenceDefinition.get(animation).delayType;
                if (replayMode == 1) {
                    player.sequenceFrame = 0;
                    player.sequenceFrameCycle = 0;
                    player.sequenceDelay = delay;
                    player.currentAnimationLoops = 0;
                }
                if (replayMode == 2)
                    player.currentAnimationLoops = 0;
            } else if (animation == -1 || player.sequence == -1
                    || SequenceDefinition.get(animation).forcedPriority >= SequenceDefinition
                            .get(player.sequence).forcedPriority) {
                player.sequence = animation;
                player.sequenceFrame = 0;
                player.sequenceFrameCycle = 0;
                player.sequenceDelay = delay;
                player.currentAnimationLoops = 0;
                player.anim_delay = player.pathLength;
            }
        }
        if ((mask & 4) != 0) {
            player.spokenText = buffer.readString();
            // System.out.println("mask &4 != 0 text: " + player.spokenText);
            if (player.spokenText.charAt(0) == '~') {
                player.spokenText = player.spokenText.substring(1);
                sendMessage(player.spokenText, 2, player.username, player.getTitle(false));
            } else if (player == local_player)
                sendMessage(player.spokenText, 2, player.username, player.getTitle(false));
            player.textColour = 0;
            player.textEffect = 0;
            player.message_cycle = 150;
        }
        if ((mask & 0x80) != 0) {
            int textColorAndEffect = buffer.readLEUShort();
            int privilege = buffer.readUnsignedByte();
            int donatorPrivilege = buffer.readUnsignedByte();
            int ironPrivilege = buffer.readUnsignedByte();
            int j3 = buffer.readNegUByte(); // chat text size
            int k3 = buffer.pos; // chat text
            if (player.username != null && player.visible) {
                long name = StringUtils.encodeBase37(player.username);
                boolean ignored = false;
                if (privilege <= 1) {
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignoreListAsLongs[count] != name)
                            continue;
                        ignored = true;
                        break;
                    }

                }
                if (!ignored && onTutorialIsland == 0)
                    try {

                        chatBuffer.pos = 0;
                        buffer.readReverseData(chatBuffer.payload, j3, 0);
                        chatBuffer.pos = 0;
                        String text = ChatMessageCodec.decode(j3, chatBuffer);
                        AbstractFont.escapeBrackets(WordPack.method2630(WordPack.decodeHuffman(chatBuffer)));
                        player.spokenText = text;
                        player.textColour = textColorAndEffect >> 8;
                        player.rights = privilege;
                        player.donatorRights = donatorPrivilege;
                        player.ironmanRights = ironPrivilege;
                        player.textEffect = textColorAndEffect & 0xff;
                        player.message_cycle = 150;

                        List<ChatCrown> crowns = ChatCrown.get(player.rights, player.donatorRights,
                                player.ironmanRights);
                        StringBuilder crownPrefix = new StringBuilder();
                        for (ChatCrown c : crowns) {
                            crownPrefix.append(c.getIdentifier());
                        }

                        sendMessage(text, 1, crownPrefix + player.username, player.getTitle(false));

                    } catch (Exception exception) {
                        exception.printStackTrace();
                        addReportToServer(exception.getMessage());
                    }
            }
            buffer.pos = k3 + j3;
        }
        if ((mask & 1) != 0) {
            player.targetIndex = buffer.readLEUShort();
            if (player.targetIndex == 65535)
                player.targetIndex = -1;
        }
        if ((mask & 0x10) != 0) {
            int length = buffer.readNegUByte();
            byte[] data = new byte[length];
            Buffer appearanceBuffer = new Buffer(data);
            buffer.readBytes(length, 0, data);
            playerSynchronizationBuffers[index] = appearanceBuffer;
            player.update(appearanceBuffer);
        }
        if ((mask & 2) != 0) {
            player.faceX = buffer.readLEUShortA();
            player.faceY = buffer.readLEUShort();
        }
        if ((mask & 0x20) != 0) {
            int count = buffer.readUnsignedByte();
            for (int i = 0; i < count; i++) {
                int damage = buffer.readShort();
                int type = buffer.readUnsignedByte();
                int hp = buffer.readShort();
                int maxHp = buffer.readShort();
                player.updateHitData(type, damage, tick);
                player.game_tick_status = tick + 300;
                player.current_hitpoints = hp;
                player.maximum_hitpoints = maxHp;
            }
        }
        if ((mask & 0x200) != 0) {
            player.recolourStartCycle = tick + buffer.readShort();
            player.recolourEndCycle = tick + buffer.readShort();
            player.recolorHue = (byte) buffer.readUnsignedByte();
            player.recolourSaturation = (byte) buffer.readUnsignedByte();
            player.recolourLuminance = (byte) buffer.readUnsignedByte();
            player.recolourAmount = (byte) buffer.readUnsignedByte();
        }
    }

    public int gameState = 0;

    public void drawLoadingMessage(String messages) {
        int width = 0;
        for (String message : messages.split("<br>")) {
            int size = regularFont.getTextWidth(message);
            if (width <= regularFont.getTextWidth(message)) {
                width = size;
            }
        }

        int offset = isResized() ? 3 : 6;

        int height = (12 * messages.split("<br>").length) + 4;

        Rasterizer2D.draw_filled_rect(offset, offset, width + 16, height + 6, 0x000000);
        Rasterizer2D.draw_rectangle_outline(offset, offset, width + 16, height + 6, 0xFFFFFF);

        int offsetY = 0;
        for (String message : messages.split("<br>")) {
            regularFont.drawCenteredString(message, offset + (width + 16) / 2, offset + 15 + offsetY, 0xffffff, true);
            offsetY += 12;
        }

    }

    @Override
    public void draw(boolean redraw) {
        if (rsAlreadyLoaded || loadingError || genericLoadingError) {
            showErrorScreen();
            return;
        }
        StaticSound.draw();
        callbacks.frame();
        updateCamera();

        if (gameState == 0) {
            this.drawInitial(loadingPercent, loadingText, false);
        } else if (gameState == 5) {
            drawLoginScreen();
        } else if (gameState != 10 && gameState != 11) {
            if (gameState == 20) {
                drawLoginScreen();
            } else if (gameState == 50) {
                drawLoginScreen();
            } else if (gameState == 25) {
                int percentage;
                if (loadingType == 1) {
                    if (mapsLoaded > totalMaps) {
                        totalMaps = mapsLoaded;
                    }

                    percentage = (totalMaps * 50 - mapsLoaded * 50) / totalMaps;
                    drawLoadingMessage("Loading - please wait." + "<br>" + " (" + percentage + "%" + ")");
                } else if (loadingType == 2) {
                    if (objectsLoaded > totalObjects) {
                        totalObjects = objectsLoaded;
                    }

                    percentage = (totalObjects * 50 - objectsLoaded * 50) / totalObjects + 50;
                    drawLoadingMessage("Loading - please wait." + "<br>" + " (" + percentage + "%" + ")");
                } else {
                    drawLoadingMessage("Loading - please wait.");
                }
            } else if (gameState == 30) {
                drawGameScreen();
            } else if (gameState == 40) {
                drawLoadingMessage("Connection lost" + "<br>" + "Please wait - attempting to reestablish");
            } else if (gameState == 45) {
                drawLoadingMessage("Please wait...");
            }
        } else {
            drawLoginScreen();
        }

        if (gameState > 0) {
            rasterProvider.drawFull(0, 0);
        }

        anInt1213 = 0;
    }

    @Override
    protected void cleanUpForQuit() {
        if (StaticSound.pcmPlayer0 != null) {
            StaticSound.pcmPlayer0.shutdown();
        }
        if (StaticSound.pcmPlayer1 != null) {
            StaticSound.pcmPlayer1.shutdown();
        }
    }

    @Override
    protected void vmethod1099() {

    }

    private boolean check_username(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < friendsCount; i++)
            if (s.equalsIgnoreCase(friendsList[i]))
                return true;
        return s.equalsIgnoreCase(local_player.username);
    }

    private static String get_level_diff(int local, int entity) {
        int dif = local - entity;
        if (dif < -9)
            return "<col=ff0000>";

        if (dif < -6)
            return "<col=ff3000>";

        if (dif < -3)
            return "<col=ff7000>";

        if (dif < 0)
            return "<col=ffb000>";

        if (dif > 9)
            return "<col=00FF00>";

        if (dif > 6)
            return "<col=40ff00>";

        if (dif > 3)
            return "<col=80ff00>";

        if (dif > 0)
            return "<col=c0ff00>";
        else
            return "<col=ffff00>";

    }

    private StringBuilder objectMaps = new StringBuilder();
    private StringBuilder floorMaps = new StringBuilder();
    Runtime runtime = Runtime.getRuntime();
    int clientMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);

    public String entityFeedName;
    public int entityFeedHP;
    public int entityFeedMaxHP;
    public int entityFeedHP2;
    public int entityAlpha;
    private int entityTick;

    public void pushFeed(String entityName, int HP, int maxHP) {
        entityFeedHP2 = entityFeedHP <= 0 ? entityFeedMaxHP : entityFeedHP;
        entityFeedName = entityName;
        entityFeedHP = HP;
        entityFeedMaxHP = maxHP;
        entityAlpha = 255;
        entityTick = entityName.isEmpty() ? 0 : 600;
    }

    private void displayEntityFeed() {
        if (entityFeedName == null)
            return;
        if (entityFeedHP == 0)
            return;
        if (entityTick-- <= 0)
            return;

        double percentage = entityFeedHP / (double) entityFeedMaxHP;
        double percentage2 = (entityFeedHP2 - entityFeedHP) / (double) entityFeedMaxHP;
        int width = (int) (135 * percentage);

        if (width > 132)
            width = 132;

        int xOff = 3;
        int yOff = 25;

        // background
        Rasterizer2D.fillRectangle(xOff, yOff, 141, 50, 0x4c433d, 155);
        Rasterizer2D.drawRectangle(xOff, yOff, 141, 50, 0x332f2d, 255);

        // name
        smallFont.drawCenteredString(entityFeedName, xOff + 69, yOff + 23, 0xFDFDFD, 0);

        // Hp fill
        Rasterizer2D.fillRectangle(xOff + 7, yOff + 32, width - 4, 12, 0x66b754, 130);
        Rasterizer2D.fillRectangle(xOff + 7, yOff + 32, width - 4, 12, 0x66b754, 130);

        // Hp empty
        Rasterizer2D.fillRectangle(xOff + 4 + width, yOff + 32, 135 - width - 4, 12, 0xc43636, 130);

        if (entityAlpha > 0) {
            entityAlpha -= 5;
            Rasterizer2D.fillRectangle(xOff + 4 + width, yOff + 32, (int) (135 * percentage2) - 4, 12, 0xFFDB00,
                    (int) (130 * entityAlpha / 255.0));
        }

        Rasterizer2D.drawRectangle(xOff + 7, yOff + 32, 128, 12, 0x332f2d, 130);

        // HP text
        smallFont.drawCenteredString(NumberFormat.getInstance(Locale.US)
                .format(entityFeedHP) + " / "
                + NumberFormat.getInstance(Locale.US)
                        .format(entityFeedMaxHP),
                xOff + 72, yOff + 44, 0xFDFDFD, 0);
    }

    private void displayHits() {
        ArrayList<IncomingHit> temp = new ArrayList<>(expectedHit);
        for (int index = 0; index < temp.size(); index++) {
            if (index >= expectedHit.size())
                continue;

            final int addPos = 1;
            final IncomingHit hit = expectedHit.get(index);
            final int damage = hit.damage;
            // System.out.println("damage "+damage);
            final int color = (damage > 0) ? 16711680 : 65535;
            int opacity = 255 - hit.pos * 2;
            if (opacity < 0) {
                opacity = 0;
                expectedHit.remove(hit);
            }

            hit.incrementPos(addPos);
            int yOffset = ((index + 1) * 20) + hit.pos;
            SpriteCache.get((damage > 0) ? 345 : 346)
                    .draw_transparent(448 + ((damage > 0) ? 0 : 3), 52 + yOffset, opacity);
            boldFont.draw(Integer.toString(damage), 488, 68 + yOffset, color, (damage > 0) ? 3407872 : 100, opacity);
        }
    }

    public static boolean counterOn = false;

    private void draw3dScreen() {
        try {
            boolean fixed = !resized;

            if (counterOn) {
                ExpCounter.drawExperienceCounter();
            }

            if (showChatComponents) {
                drawSplitPrivateChat();
            }

            if (fadingScreen != null) {
                fadingScreen.draw();
            }

            NightmareHealth.draw();

            if (broadcast != null && isDisplayed) {
                broadcast.process();
            }

            // Effect timers
            if (setting.draw_timers) {
                drawEffectTimers();
            }

            if (setting.draw_health_overlay) {
                displayEntityFeed();
            }

            if (startSpin) {
                startSpinner();
            }

            if (crossType == 1) {
                int offSet = fixed ? 4 : 0;
                crosses[crossIndex / 100].drawSprite(crossX - 8 - offSet, crossY - 8 - offSet);
                anInt1142++;
                if (anInt1142 > 67) {
                    anInt1142 = 0;
                    // sendPacket(new ClearMinimapFlag()); //Not server-sided, flag is only handled
                    // in the client
                }
            }
            if (crossType == 2) {
                int offSet = fixed ? 4 : 0;
                crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - offSet, crossY - 8 - offSet);
            }

            if (openWalkableInterface != -1) {
                try {
                    processWidgetAnimations(animation_step, openWalkableInterface);
                    Widget rsinterface = Widget.cache[openWalkableInterface];
                    if (openWalkableInterface == 21100 && lobbyTimer.secondsRemaining() > 0) {
                        drawLobbyTimer();
                    }
                    if (fixed) {
                        drawInterface(rsinterface, 0, 0, 0);
                    } else {
                        int x = canvasWidth - 215;
                        x -= rsinterface.width;
                        int min_y = Integer.MAX_VALUE;
                        for (int i = 0; i < rsinterface.children.length; i++) {
                            min_y = Math.min(min_y, rsinterface.child_y[i]);
                        }

                        // barrows kc interface
                        if (openWalkableInterface == 4535 && resized) {
                            drawInterface(rsinterface, (canvasHeight > 685) ? x + 100 : x - 20, 30, 0);
                        } else if (openWalkableInterface == 23300 && resized) {
                            drawInterface(rsinterface, 16, 0, 0);
                        } else {
                            drawInterface(rsinterface, x, -min_y + 10, 0);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    addReportToServer(ex.getMessage());
                }
            }

            drawParallelWidgets();

            if (widget_overlay_id != -1) {
                try {
                    processWidgetAnimations(animation_step, widget_overlay_id);
                    int w = 512, h = 334;
                    int x = !resized ? 0 : (canvasWidth / 2) - 256;
                    int y = !resized ? 0 : (canvasHeight / 2) - 167;
                    int count = settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1 ? 3 : 4;
                    if (resized) {
                        for (int i = 0; i < count; i++) {
                            if (x + w > (canvasWidth - 225)) {
                                x = x - 30;
                                if (x < 0) {
                                    x = 0;
                                }
                            }
                            if (y + h > (canvasHeight - 182)) {
                                y = y - 30;
                                if (y < 0) {
                                    y = 0;
                                }
                            }
                        }
                    }
                    // barrows kc interface
                    if (widget_overlay_id == 4335) {
                        drawInterface(Widget.cache[widget_overlay_id], 0, 0, 0);
                    }

                    drawInterface(Widget.cache[widget_overlay_id],
                            !resized ? 0 : (canvasWidth / 2) - 356,
                            !resized ? 0 : (canvasHeight / 2) - 230, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // System.out.println("widget overlay id: " + widget_overlay_id);

            if (!isMenuOpen) {
                processRightClick();
                drawTooltip();
            }

            // Multi sign
            if (multicombat == 1) {
                int y = openWalkableInterface == 196 ? canvasHeight - 250 : canvasHeight - 200;
                multiOverlay.drawSprite(471, y);
            }

            if (broadcastText != null && !broadcastText.isEmpty()) {
                regularFont.draw(broadcastText, 0, rebootTimer != 0 ? 316 : 329, 0xffff00, 0);
            }

            if ((widget_overlay_id == -1 || ClientConstants.FORCE_OVERLAY_ABOVE_WIDGETS)
                    && (setting.draw_fps || ClientConstants.CLIENT_DATA)) {
                int x = canvasWidth - 750;
                int y = 20;
                if (setting.draw_fps) {
                    int rgb = 0xFFFF00;
                    if (super.fps < 15) {
                        rgb = 0xff0000;
                    }
                    smallFont.draw("Fps:  " + super.fps, x, y, rgb, true);
                    y += 15;
                    // Update memory display every 30 ticks.
                    if (tick % 30 == 0) {
                        clientMemory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
                    }

                    int memoryColour;
                    if (clientMemory > 350_000 && clientMemory < 450_000) {
                        memoryColour = 0xff8624;
                    } else if (clientMemory > 450_000) {
                        memoryColour = 0xff0000;
                    } else {
                        memoryColour = 0xffff00;
                    }
                    smallFont.draw("Memory: " + NumberFormat.getInstance()
                            .format(clientMemory) + "k", x, y, memoryColour, 40);
                }

                if (ClientConstants.CLIENT_DATA) {
                    int playerX = next_region_start + (local_player.x - 6 >> 7);
                    int playerY = next_region_end + (local_player.y - 6 >> 7);
                    smallFont.draw("Coords: " + playerX + ", " + playerY, x, y + 15, 0xffff00, 40);
                    smallFont.draw("Resolution: " + canvasWidth + "x" + canvasHeight, x, y + 30, 0xffff00, 40);
                    smallFont.draw("Build: " + ClientConstants.CLIENT_VERSION, x, y + 45, 0xffff00, 40);
                    smallFont.draw("Mouse X: " + MouseHandler.mouseX, x, y + 75, 0xffff00, 40);
                    smallFont.draw("Mouse Y: " + MouseHandler.mouseY, x, y + 90, 0xffff00, 40);
                    smallFont.draw("Frame Width: " + canvasWidth + ", Frame Height: " + canvasHeight, x, y + 105,
                            0xffff00, 40);
                    smallFont.draw("Object Maps: " + objectMaps, x, y + 120, 0xffff00, 40);
                    smallFont.draw("Floor Maps: " + floorMaps, x, y + 135, 0xffff00, 40);
                }
            }

            if (rebootTimer != 0) {
                int seconds = rebootTimer / 50;
                int minutes = seconds / 60;
                int yOffset = !resized ? 0 : canvasHeight - 498;
                if (this.broadcast != null && isDisplayed) {
                    yOffset -= 20;
                }
                seconds %= 60;
                regularFont.draw("System update in: " + minutes + ":" + (seconds < 10 ? "0" : "") + seconds, 4,
                        329 + yOffset, 0xffff00, false);
                anInt849++;
                if (anInt849 > 75) {
                    anInt849 = 0;
                    // unknown (system updating)
                    // outgoing.writeOpcode(148);
                }
            }
            drawScreenBox();
        } catch (Exception e) {
            e.printStackTrace();
            addReportToServer(e.getMessage());
        }
    }

    public void drawScreenBox() {
        if (darkenEnabled) {
            Rasterizer2D.drawAlphaBox(0, 0, canvasWidth, canvasHeight, 0, darkenOpacity);
        }
    }

    public int getRegionId() {
        int localX = region_x / 8;
        int localY = region_y / 8;
        return (localX << 8) + localY;
    }

    private void addIgnore(String name) {
        try {
            if (name == null)
                return;

            if (ignoreCount >= 100) {
                sendMessage("Your ignore list is full. Max of 100 hit", 0, "");
                return;
            }
            /// YOU HAVE TO KEEP THIS LOGIC OTHERWISE CLIENT WILL DESYNC
            for (int j = 0; j < ignoreCount; j++)
                if (ignoreList[j].equalsIgnoreCase(name)) {
                    sendMessage(name + " is already on your ignore list", 0, "");
                    return;
                }
            for (int k = 0; k < friendsCount; k++)
                if (friendsList[k].equalsIgnoreCase(name)) {
                    sendMessage("Please remove " + name + " from your friend list first", 0, "");
                    return;
                }

            ignoreList[ignoreCount] = name;
            ignoreListAsLongs[ignoreCount++] = StringUtils.encodeBase37(name);
            packetSender.sendIgnoreAddition(name);
            return;
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("45688, " + name + ", " + 4 + ", " + runtimeexception);
        }
        throw new RuntimeException();
    }

    private void updatePlayerInstances() {
        for (int index = -1; index < players_in_region; index++) {

            int playerIndex;

            if (index == -1) {
                playerIndex = LOCAL_PLAYER_INDEX;
            } else {
                playerIndex = local_players[index];
            }

            Player player = players[playerIndex];

            if (player != null) {
                entityUpdateBlock(player);
            }
        }
    }

    private void checkRadioOptions(int second) {
        for (int ids : Widget.radioButtons) {
            if (Widget.cache[ids].radioID == Widget.cache[second].radioID && ids != second) {
                Widget.cache[ids].disabledSprite = SpriteCache.get(166);
                Widget.cache[ids].enabledSprite = SpriteCache.get(166);
            }
        }
        if (Widget.cache[second].disabledSprite == SpriteCache.get(167)) {
            Widget.cache[second].disabledSprite = SpriteCache.get(169);
            Widget.cache[second].enabledSprite = SpriteCache.get(169);
            return;
        }
        if (Widget.cache[second].disabledSprite == SpriteCache.get(170)) {
            Widget.cache[second].disabledSprite = SpriteCache.get(171);
            Widget.cache[second].enabledSprite = SpriteCache.get(171);
            return;
        }
        if (Widget.cache[second].disabledSprite == SpriteCache.get(172)) {
            Widget.cache[second].disabledSprite = SpriteCache.get(169);
            Widget.cache[second].enabledSprite = SpriteCache.get(169);
        }
    }

    private void processPendingSpawns() {
        for (SpawnedObject spawnedObject = (SpawnedObject) spawns
                .first(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
                        .next()) {
            if (spawnedObject.getLongetivity > 0)
                spawnedObject.getLongetivity--;
            if (spawnedObject.getLongetivity == 0) {
                if (spawnedObject.getPreviousId < 0
                        || Region.cached(spawnedObject.getPreviousId, spawnedObject.previousType)) {
                    currentMapRegion.addPendingSpawnToScene(spawnedObject.plane, spawnedObject.group, spawnedObject.x,
                            spawnedObject.y, spawnedObject.getPreviousId, spawnedObject.previousOrientation,
                            spawnedObject.previousType);

                    spawnedObject.remove();
                }
            } else {
                if (spawnedObject.delay > 0)
                    spawnedObject.delay--;
                if (spawnedObject.delay == 0 && spawnedObject.x >= 1 && spawnedObject.y >= 1
                        && spawnedObject.x <= 102 && spawnedObject.y <= 102
                        && (spawnedObject.id < 0 || Region.cached(spawnedObject.id, spawnedObject.type))) {

                    currentMapRegion.addPendingSpawnToScene(spawnedObject.plane, spawnedObject.group, spawnedObject.x,
                            spawnedObject.y, spawnedObject.id, spawnedObject.orientation, spawnedObject.type);

                    spawnedObject.delay = -1;
                    if (spawnedObject.id == spawnedObject.getPreviousId && spawnedObject.getPreviousId == -1)
                        spawnedObject.remove();
                    else if (spawnedObject.id == spawnedObject.getPreviousId
                            && spawnedObject.orientation == spawnedObject.previousOrientation
                            && spawnedObject.type == spawnedObject.previousType)
                        spawnedObject.remove();
                }
            }
        }

    }

    private final void determineMenuSize(int saveX, int saveY) {
        final MenuOpened event = new MenuOpened();
        event.setMenuEntries(getMenuEntries());
        getCallbacks().post(event);

        if (event.isModified()) {
            setMenuEntries(event.getMenuEntries());
        }

        int boxLength = boldFont.getTextWidth("Choose Option");

        int boxWidth;
        int xPosition;
        for (int row = 0; row < menuActionRow; ++row) {
            int actionLength = boldFont.stringWidthNoFormatting(menuActionText[row].toString());
            if (menuActionText[row].contains("<img")) {
                for (String s : org.apache.commons.lang3.StringUtils.substringsBetween(menuActionText[row], "<img=",
                        ">")) {
                    actionLength += SpriteCache.get(Integer.parseInt(s)).getMyWidth();
                }
            }
            if (actionLength > boxLength) {
                boxLength = actionLength;
            }
        }

        boxLength += 8;
        boxWidth = menuActionRow * 15 + 22;
        xPosition = saveX - boxLength / 2;
        if (boxLength + xPosition > canvasWidth) {
            xPosition = canvasWidth - boxLength;
        }

        if (xPosition < 0) {
            xPosition = 0;
        }

        int yPosition = saveY;
        if (boxWidth + saveY > canvasHeight) {
            yPosition = canvasHeight - boxWidth;
        }

        if (yPosition < 0) {
            yPosition = 0;
        }

        menuX = xPosition;
        menuY = yPosition;
        menuWidth = boxLength;
        menuHeight = menuActionRow * 15 + 22;

        isMenuOpen = true;
    }

    private boolean isMouseWithin(int minX, int maxX, int minY, int maxY) {
        return MouseHandler.mouseX >= minX && MouseHandler.mouseX <= maxX && MouseHandler.mouseY >= minY
                && MouseHandler.mouseY <= maxY;
    }

    private void updateLocalPlayerMovement(Buffer stream) {
        stream.initBitAccess();

        int update = stream.readBits(1);

        if (update == 0) {
            return;
        }

        int type = stream.readBits(2);
        if (type == 0) {
            mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = LOCAL_PLAYER_INDEX;
            return;
        }
        if (type == 1) {
            int direction = stream.readBits(3);
            local_player.moveInDir(false, direction);
            int updateRequired = stream.readBits(1);

            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = LOCAL_PLAYER_INDEX;
            }
            return;
        }
        if (type == 2) {
            int firstDirection = stream.readBits(3);
            local_player.moveInDir(true, firstDirection);

            int secondDirection = stream.readBits(3);
            local_player.moveInDir(true, secondDirection);

            int updateRequired = stream.readBits(1);

            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = LOCAL_PLAYER_INDEX;
            }
            return;
        }
        if (type == 3) {
            plane = stream.readBits(2);

            // Fix for height changes
            if (lastKnownPlane != plane) {
                setGameState(GameState.LOADING);
            }

            lastKnownPlane = plane;

            int teleport = stream.readBits(1);
            int updateRequired = stream.readBits(1);

            if (updateRequired == 1) {
                mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = LOCAL_PLAYER_INDEX;
            }

            int y = stream.readBits(7);
            int x = stream.readBits(7);

            local_player.setPos(x, y, teleport == 1);
        }
    }

    private void nullLoader() {
        update_flame_components = false;
        while (drawingFlames) {
            update_flame_components = false;
            try {
                Thread.sleep(50L);
            } catch (Exception _ex) {
            }
        }
        titleBoxIndexedImage = null;
        titleButtonIndexedImage = null;
        titleIndexedImages = null;
        anIntArray850 = null;
        anIntArray851 = null;
        anIntArray852 = null;
        anIntArray853 = null;
        anIntArray1190 = null;
        anIntArray1191 = null;
        anIntArray828 = null;
        anIntArray829 = null;
        flameLeftSprite = null;
        flameRightSprite = null;
    }

    private boolean processWidgetAnimations(int tick, int interfaceId) throws Exception {
        boolean redrawRequired = false;
        Widget widget = Widget.cache[interfaceId];

        if (widget == null || widget.children == null) {
            return false;
        }

        for (int element : widget.children) {
            if (element == -1) {
                break;
            }

            Widget child = Widget.cache[element];

            if (child.type == Widget.TYPE_MODEL_LIST) {
                redrawRequired |= processWidgetAnimations(tick, child.id);
            }

            if (child.type == 6 && (child.defaultAnimationId != -1 || child.secondaryAnimationId != -1)) {
                boolean updated = interfaceIsSelected(child);

                int animationId = updated ? child.secondaryAnimationId : child.defaultAnimationId;

                if (animationId != -1) {
                    SequenceDefinition animation = SequenceDefinition.get(animationId);
                    for (child.lastFrameTime += tick; child.lastFrameTime > animation.duration(child.currentFrame);) {
                        child.lastFrameTime -= animation.duration(child.currentFrame) + 1;
                        child.currentFrame++;
                        if (child.currentFrame >= animation.frameCount) {
                            child.currentFrame -= animation.frameCount;
                            if (child.currentFrame < 0 || child.currentFrame >= animation.frameCount)
                                child.currentFrame = 0;
                        }
                        redrawRequired = true;
                    }

                }
            }
        }

        return redrawRequired;
    }

    public void removeFriend(String name) {
        if (name == null)
            return;
        packetSender.sendFriendDeletion(name);
        // Fallback incase a name exists client-sided but not in server.
        for (int i = 0; i < friendsCount; i++) {
            if (!friendsList[i].equalsIgnoreCase(name)) {
                continue;
            }

            friendsCount--;
            for (int n = i; n < friendsCount; n++) {
                friendsList[n] = friendsList[n + 1];
                friendsNodeIDs[n] = friendsNodeIDs[n + 1];
                friendsListAsLongs[n] = friendsListAsLongs[n + 1];
            }
            break;
        }
    }

    private void removeIgnore(String name) {
        if (name == null)
            return;
        packetSender.sendIgnoreDeletion(name);
        for (int index = 0; index < ignoreCount; index++) {
            if (ignoreList[index].equalsIgnoreCase(name)) {
                ignoreCount--;
                System.arraycopy(ignoreList, index + 1, ignoreList, index, ignoreCount - index);
                break;
            }
        }
    }

    public String widget_tooltip_text_script(String text, Widget child) {
        if (text.indexOf("%") != -1) {
            int script_id;
            for (script_id = 1; script_id <= 5; script_id++) {
                while (true) {
                    int index = text.indexOf("%" + script_id);
                    if (index == -1) {
                        break;
                    }
                    text = text.substring(0, index) + interfaceIntToString(executeScript(child, script_id - 1))
                            + text.substring(index + 2);
                }
            }
        }
        return text;
    }

    public int int_to_percent;

    public final int calcHitpointColor(Entity entity) {
        int int_to_percent = (int) (((double) entity.current_hitpoints / (double) entity.maximum_hitpoints) * 100D);
        return int_to_percent >= 75 ? 65280
                : int_to_percent >= 50 ? 16776960
                        : int_to_percent >= 25 ? 16557575
                                : 16059661;
    }

    private int executeScript(Widget widget, int id) {
        if (widget.valueIndexArray == null || id >= widget.valueIndexArray.length)
            return -2;
        boolean inWild = (VarbitManager.getVarbit(5963) == 1);
        try {
            int[] script = widget.valueIndexArray[id];
            int accumulator = 0;
            int counter = 0;
            int operator = 0;
            do {
                int instruction = script[counter++];
                int value = 0;
                byte next = 0;

                if (instruction == 0) {
                    return accumulator;
                }

                if (instruction == 1) {
                    value = boostedSkillLevels[script[counter++]];
                }

                if (instruction == 2) {
                    value = realSkillLevels[script[counter++]];
                }

                if (instruction == 3) {
                    value = skillExperience[script[counter++]];
                }

                if (instruction == 4) {
                    Widget other = Widget.cache[script[counter++]];
                    int item = script[counter++];
                    // System.out.println("inv scan");
                    if (item >= 0 && item < Js5List.getConfigSize(Js5ConfigType.ITEM)
                            && (!ItemDefinition.get(item).members || isMembers)) {
                        for (int slot = 0; slot < other.inventoryItemId.length; slot++) {
                            if (other.inventoryItemId[slot] == item + 1) {
                                value += other.inventoryAmounts[slot];
                            }

                            if (inWild) {
                                // Blighted ancient ice sack
                                if (other.inventoryItemId[slot] == 24607 + 1) {
                                    if ((iceSack[0][0] + 1) == (item + 1)) {
                                        value += iceSack[0][1];
                                    }
                                    if ((iceSack[1][0] + 1) == (item + 1)) {
                                        value += iceSack[1][1];
                                    }
                                    if ((iceSack[2][0] + 1) == (item + 1)) {
                                        value += iceSack[2][1];
                                    }
                                }

                                // Blighted bind sack
                                if (other.inventoryItemId[slot] == 24609 + 1) {
                                    if ((bindSack[0][0] + 1) == (item + 1)) {
                                        value += bindSack[0][1];
                                    }
                                    if ((bindSack[1][0] + 1) == (item + 1)) {
                                        value += bindSack[1][1];
                                    }
                                    if ((bindSack[2][0] + 1) == (item + 1)) {
                                        value += bindSack[2][1];
                                    }
                                }

                                // Blighted snare sack
                                if (other.inventoryItemId[slot] == 24611 + 1) {
                                    if ((snareSack[0][0] + 1) == (item + 1)) {
                                        value += snareSack[0][1];
                                    }
                                    if ((snareSack[1][0] + 1) == (item + 1)) {
                                        value += snareSack[1][1];
                                    }
                                    if ((snareSack[2][0] + 1) == (item + 1)) {
                                        value += snareSack[2][1];
                                    }
                                }

                                // Blighted entangle sack
                                if (other.inventoryItemId[slot] == 24613 + 1) {
                                    if ((entangleSack[0][0] + 1) == (item + 1)) {
                                        value += entangleSack[0][1];
                                    }
                                    if ((entangleSack[1][0] + 1) == (item + 1)) {
                                        value += entangleSack[1][1];
                                    }
                                    if ((entangleSack[2][0] + 1) == (item + 1)) {
                                        value += entangleSack[2][1];
                                    }
                                }

                                // Blighted teleport sack
                                if (other.inventoryItemId[slot] == 24615 + 1) {
                                    if ((teleportSack[0][0] + 1) == (item + 1)) {
                                        value += teleportSack[0][1];
                                    }
                                    if ((teleportSack[1][0] + 1) == (item + 1)) {
                                        value += teleportSack[1][1];
                                    }
                                    if ((teleportSack[2][0] + 1) == (item + 1)) {
                                        value += teleportSack[2][1];
                                    }
                                }

                                // Blighted vengeance sack
                                if (other.inventoryItemId[slot] == 24621 + 1) {
                                    if ((vengeance[0][0] + 1) == (item + 1)) {
                                        value += vengeance[0][1];
                                    }
                                    if ((vengeance[1][0] + 1) == (item + 1)) {
                                        value += vengeance[1][1];
                                    }
                                    if ((vengeance[2][0] + 1) == (item + 1)) {
                                        value += vengeance[2][1];
                                    }
                                }
                            }
                        }

                        if ((runePouch[0][0] + 1) == (item + 1)) { // match ID of rune which the spell uses with the one
                            // in RP
                            value += runePouch[0][1];
                        }
                        if ((runePouch[1][0] + 1) == (item + 1)) {
                            value += runePouch[1][1];
                        }
                        if ((runePouch[2][0] + 1) == (item + 1)) {
                            value += runePouch[2][1];
                        }
                        if ((runePouch[3][0] + 1) == (item + 1)) {
                            value += runePouch[3][1];
                        }
                    }
                }
                if (instruction == 5) {
                    value = settings[script[counter++]];
                }

                if (instruction == 6) {
                    value = SKILL_EXPERIENCE[realSkillLevels[script[counter++]] - 1];
                }

                if (instruction == 7) {
                    value = (settings[script[counter++]] * 100) / 46875;
                }

                if (instruction == 8) {
                    value = local_player.combat_level;
                }

                if (instruction == 9) {
                    for (int skill = 0; skill < SkillConstants.SKILL_COUNT; skill++)
                        if (SkillConstants.ENABLED_SKILLS[skill])
                            value += realSkillLevels[skill];
                }

                if (instruction == 10) {
                    Widget other = Widget.cache[script[counter++]];
                    int item = script[counter++] + 1;
                    if (item >= 0 && item < Js5List.getConfigSize(Js5ConfigType.ITEM) && isMembers) {
                        for (int stored = 0; stored < other.inventoryItemId.length; stored++) {
                            if (other.inventoryItemId[stored] != item)
                                continue;
                            value = 0x3b9ac9ff;
                            break;
                        }

                    }
                }

                if (instruction == 11) {
                    value = runEnergy;
                }

                if (instruction == 12) {
                    value = weight;
                }

                if (instruction == 13) {
                    int bool = settings[script[counter++]];
                    int shift = script[counter++];
                    value = (bool & 1 << shift) == 0 ? 0 : 1;
                }

                if (instruction == 14) {
                    value = VarbitManager.getVarbit(script[counter++]);
                }

                if (instruction == 15) {
                    next = 1;
                }

                if (instruction == 16) {
                    next = 2;
                }

                if (instruction == 17) {
                    next = 3;
                }

                if (instruction == 18) {
                    value = (local_player.x >> 7) + next_region_start;
                }

                if (instruction == 19) {
                    value = (local_player.y >> 7) + next_region_end;
                }

                if (instruction == 20) {
                    value = script[counter++];
                }

                if (instruction == 21) {
                    value = tabAmounts[script[counter++]];
                }

                if (instruction == 22) {
                    Widget class9_1 = Widget.cache[script[counter++]];
                    int initAmount = class9_1.inventoryItemId.length;
                    for (int j3 = 0; j3 < class9_1.inventoryItemId.length; j3++) {
                        if (class9_1.inventoryItemId[j3] <= 0) {
                            initAmount--;
                        }
                    }
                    value += initAmount;
                }

                if (next == 0) {

                    if (operator == 0) {
                        accumulator += value;
                    }

                    if (operator == 1) {
                        accumulator -= value;
                    }

                    if (operator == 2 && value != 0) {
                        accumulator /= value;
                    }

                    if (operator == 3) {
                        accumulator *= value;
                    }
                    operator = 0;
                } else {
                    operator = next;
                }
            } while (true);
        } catch (Exception _ex) {
            _ex.printStackTrace();
            addReportToServer(_ex.getMessage());
            return -1;
        }
    }

    private void drawTooltip() {
        if (widget_overlay_id == 16244) {
            return;
        }

        if (menuActionRow < 2 && item_highlighted == 0 && widget_highlighted == 0) {
            return;
        }

        if (item_highlighted == 1 && menuActionRow < 2) {
            tooltip = "Use " + selectedItemName + " with...";
        } else if (widget_highlighted == 1 && menuActionRow < 2) {
            tooltip = selected_target_id + "...";
        } else {
            tooltip = menuActionText[menuActionRow - 1];
        }

        if (menuActionRow > 2) {
            tooltip = tooltip + "<col=ffffff> / " + (menuActionRow - 2) + " more options";
        }

        if (tooltip.contains("<br>")) {
            tooltip = tooltip.replaceAll("<br>", " ");
        }

        boldFont.draw(tooltip, 4, 15, 0xffffff, 0);
    }

    public static SimpleImage[] mapMarkerSprites;

    public static final int DOT_ITEM = 0;
    public static final int DOT_NPC = 1;
    public static final int DOT_PLAYER = 2;
    public static final int DOT_FRIEND = 3;
    public static final int DOT_TEAM = 4;
    public static final int DOT_FRIENDSCHAT = 5;
    public static final int DOT_CLAN = 6;

    private void markMinimap(SimpleImage sprite, int x, int y) {
        if (sprite == null) {
            return;
        }
        int angle = camAngleY + map_rotation & 0x7ff;
        int l = x * x + y * y;
        if (l > 6400) {
            return;
        }
        int xOffset = !isResized() ? 516 : 0;
        int sineAngle = Model.SINE[angle];
        int cosineAngle = Model.COSINE[angle];
        sineAngle = (sineAngle * 256) / (minimapZoom + 256);
        cosineAngle = (cosineAngle * 256) / (minimapZoom + 256);
        int spriteOffsetX = y * sineAngle + x * cosineAngle >> 16;
        int spriteOffsetY = y * cosineAngle - x * sineAngle >> 16;
        if (!resized) {
            sprite.drawAt(((94 + spriteOffsetX) - sprite.width / 2) + 4 + 30 + xOffset,
                    83 - spriteOffsetY - sprite.height / 2 - 4 + 5);
        } else {
            int spriteCenterX = (Client.canvasWidth - 94) + xOffset; // X-coordinate of the center point of rotation
            int adjustedX = spriteCenterX + spriteOffsetX - (sprite.width / 2) + 9;
            sprite.drawAt(adjustedX, 83 - spriteOffsetY - (sprite.height / 2) - 1);
        }
    }

    private void drawMinimap() {
        int xOffset = !isResized() ? 516 : 0;
        if (minimapState == 2) {
            if (!resized) {
                // ken comment, let's fix barrows minimap and any other occurrences of minimap
                // state 2. Simply clear the 2D rasterizer to set the minimap to black.
                SpriteCache.get(19).drawSprite(xOffset, 0);
            } else {
                SpriteCache.get(44).drawSprite(canvasWidth - 181, 0);
                SpriteCache.get(45).drawSprite(canvasWidth - 158, 7);
            }

            if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1) {
                if (MouseHandler.mouseX >= canvasWidth - 26 && MouseHandler.mouseX <= canvasWidth - 1
                        && MouseHandler.mouseY >= 2
                        && MouseHandler.mouseY <= 24 || sidebarId == 15) {
                    SpriteCache.get(27).drawSprite(canvasWidth - 25, 2);
                } else {
                    SpriteCache.get(27).drawAdvancedSprite(canvasWidth - 25, 2, 165);
                }
            }
            if (settings[ConfigUtility.DATA_ORBS_ID] == 1) {
                loadAllOrbs(!resized ? +xOffset : canvasWidth - 217);
            }

            compass.rotate_raster(33, camAngleY, anIntArray1057, 256, anIntArray968, (!resized ? 25 : 24),
                    4, (!resized ? 29 + xOffset : canvasWidth - 176), 33, 25);

            return;
        }
        int angle = camAngleY + map_rotation & 0x7ff;
        int centreX = 48 + local_player.x / 32;
        int centreY = 464 - local_player.y / 32;
        minimapImage.rotate_raster(151, angle, minimapLineWidth, 256 + minimapZoom, minimapLeft, centreY,
                (!resized ? 9 : 7), (!resized ? 54 + xOffset : canvasWidth - 158), 146,
                centreX);

        for (int icon = 0; icon < objectIconCount; icon++) {
            int mapX = (minimapHintX[icon] * 4 + 2) - local_player.x / 32;
            int mapY = (minimapHintY[icon] * 4 + 2) - local_player.y / 32;
            markMinimap(minimapHint[icon], mapX, mapY);
        }
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                NodeDeque class19 = scene_items[plane][x][y];
                if (class19 != null) {
                    int mapX = (x * 4 + 2) - local_player.x / 32;
                    int mapY = (y * 4 + 2) - local_player.y / 32;
                    markMinimap(mapDotSprites[DOT_ITEM], mapX, mapY);
                }
            }
        }
        for (int n = 0; n < npcs_in_region; n++) {
            Npc npc = npcs[local_npcs[n]];
            if (npc != null && npc.visible()) {
                NpcDefinition entityDef = npc.definition;
                if (entityDef.getTransforms() != null) {
                    entityDef = entityDef.get_configs();
                }
                if (entityDef != null && entityDef.isMinimapVisible && entityDef.isInteractable) {
                    int mapX = npc.x / 32 - local_player.x / 32;
                    int mapY = npc.y / 32 - local_player.y / 32;
                    markMinimap(mapDotSprites[DOT_NPC], mapX, mapY);
                }
            }
        }
        for (int p = 0; p < players_in_region; p++) {
            Player player = players[local_players[p]];
            if (player != null && player.visible()) {
                int mapX = player.x / 32 - local_player.x / 32;
                int mapY = player.y / 32 - local_player.y / 32;
                boolean friend = false;
                boolean clanMember = false;

                for (String s : clanList) {
                    if (s == null) {
                        continue;
                    }
                    if (!s.equalsIgnoreCase(player.username)) {
                        continue;
                    }
                    clanMember = true;
                    break;
                }

                // System.out.println(clanMember+" list: "+
                // Arrays.toString(Arrays.stream(clanList).toArray()));

                long nameHash = StringUtils.encodeBase37(player.username);
                for (int f = 0; f < friendsCount; f++) {
                    if (nameHash != friendsListAsLongs[f] || friendsNodeIDs[f] == 0) {
                        continue;
                    }
                    friend = true;
                    break;
                }
                boolean team = local_player.team_id != 0 && player.team_id != 0
                        && local_player.team_id == player.team_id;
                if (clanMember) {
                    markMinimap(mapDotSprites[DOT_FRIENDSCHAT], mapX, mapY);
                } else if (friend) {
                    markMinimap(mapDotSprites[DOT_FRIEND], mapX, mapY);
                } else if (team) {
                    markMinimap(mapDotSprites[DOT_TEAM], mapX, mapY);
                } else {
                    markMinimap(mapDotSprites[DOT_PLAYER], mapX, mapY);
                }
            }
        }
        if (hintIconDrawType != 0 && tick % 20 < 10) {
            if (hintIconDrawType == 1 && hintIconNpcId >= 0 && hintIconNpcId < npcs.length) {
                Npc npc = npcs[hintIconNpcId];
                if (npc != null) {
                    int mapX = npc.x / 32 - local_player.x / 32;
                    int mapY = npc.y / 32 - local_player.y / 32;
                    refreshMinimap(mapMarkerSprites[0], mapY, mapX);
                }
            }
            if (hintIconDrawType == 2) {
                int mapX = ((hintIconX - next_region_start) * 4 + 2) - local_player.x / 32;
                int mapY = ((hintIconY - next_region_end) * 4 + 2) - local_player.y / 32;
                refreshMinimap(mapMarkerSprites[0], mapY, mapX);
            }
            if (hintIconDrawType == 10 && hintIconPlayerId >= 0 && hintIconPlayerId < players.length) {
                Player player = players[hintIconPlayerId];
                if (player != null) {
                    int mapX = player.x / 32 - local_player.x / 32;
                    int mapY = player.y / 32 - local_player.y / 32;
                    refreshMinimap(mapMarkerSprites[0], mapY, mapX);
                }
            }
        }
        if (travel_destination_x != 0) {
            int mapX = (travel_destination_x * 4 + 2) - local_player.x / 32;
            int mapY = (travel_destination_y * 4 + 2) - local_player.y / 32;
            markMinimap(mapMarkerSprites[0], mapX, mapY);
        }
        Rasterizer2D.draw_filled_rect((!resized ? 127 + xOffset : canvasWidth - 88),
                (!resized ? 83 : 80), 3, 3, 0xffffff);
        if (!resized) {
            SpriteCache.get(19).drawSprite(+xOffset, 0);
        } else {
            SpriteCache.get(44).drawSprite(canvasWidth - 181, 0);
        }
        compass.rotate_raster(33, camAngleY, anIntArray1057, 256, anIntArray968, (!resized ? 25 : 24), 4,
                (!resized ? 29 + xOffset : canvasWidth - 176), 33, 25);

        if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1) {
            if (MouseHandler.mouseX >= canvasWidth - 26 && MouseHandler.mouseX <= canvasWidth - 1
                    && MouseHandler.mouseY >= 2
                    && MouseHandler.mouseY <= 24 || sidebarId == 10) {
                SpriteCache.get(27).drawSprite(canvasWidth - 25, 2);
            } else {
                SpriteCache.get(27).drawAdvancedSprite(canvasWidth - 25, 2, 165);
            }
        }
        if (settings[ConfigUtility.DATA_ORBS_ID] == 1) {
            loadAllOrbs(!resized ? xOffset : canvasWidth - 217);
        }

    }

    private final float REGEN_HEALTH_TIME = 60000.0f;
    private final float REGEN_SPEC_TIME = 30000.0f;
    private long regenHealthStart = 0;
    private long regenSpecStart = 0;
    private long loginTime = 0;
    private long logoutTime = 0;
    private int lastHp = 0;
    private int lastSpec = 0;
    public int specialAttack = 0;

    private String runEnergyString;

    public void setRunText(String text) {
        runEnergyString = text;
    }

    public int getRunState() {
        int runType = 0;
        return runType;
    }

    private void loadSpecialOrb(int xOffset) {
        boolean fixed = !resized;
        int yOff = setting.draw_special_orb ? fixed ? -10 : 1 : 0;
        int xOff = setting.draw_special_orb ? fixed ? 138 : 113 : 0;
        SimpleImage image = SpriteCache.get(7);
        SimpleImage fill = SpriteCache.get(768);
        SimpleImage sword = SpriteCache.get(770);
        double percent = specialAttack / (double) 100;
        image.drawSprite((fixed ? 170 - xOff : 159 - xOff) + xOffset, fixed ? 122 - yOff : 147 - yOff);
        fill.drawShadedSprite((fixed ? 197 - xOff : 186 - xOff) + xOffset, fixed ? 126 - yOff : 151 - yOff, 233);
        SpriteCache.get(14).height = (int) (26 * (1 - percent));
        SpriteCache.get(14)
                .drawSprite((fixed ? 197 - xOff : 186 - xOff) + xOffset, fixed ? 126 - yOff : 151 - yOff);
        sword.drawSprite((fixed ? 202 - xOff : 191 - xOff) + xOffset, fixed ? 131 - yOff : 156 - yOff);
        smallFont.drawCenteredString(String.valueOf(specialAttack), (fixed ? 185 - xOff : 173 - xOff) + xOffset,
                fixed ? 148 - yOff : 172 - yOff, getOrbTextColor((int) (percent * 100)), true);

    }

    public int poisonType = 0;

    private void loadHpOrb(int xOffset) {
        final boolean fixed = !resized;
        int yOff = fixed ? 0 : -5;
        int xOff = fixed ? 0 : -6;
        SimpleImage bg = SpriteCache.get(7);
        SimpleImage fg = null;
        if (poisonType == 0) {
            fg = SpriteCache.get(0);
        }
        if (poisonType == 1) {
            fg = SpriteCache.get(803);
        }
        if (poisonType == 2) {
            fg = SpriteCache.get(804);
        }
        bg.drawSprite(xOffset - xOff, 41 - yOff);
        if (poisonType > 0) {
            fg.drawSprite(27 + xOffset - xOff, 45 - yOff);
        } else {
            fg.drawShadedSprite(27 + xOffset - xOff, 45 - yOff, 233);
        }
        int level = Integer.parseInt(Widget.cache[4016].defaultText.replaceAll("%", ""));
        int max = Integer.parseInt(Widget.cache[4017].defaultText.replaceAll("%", ""));
        double percent = level / (double) max;
        SpriteCache.get(14).height = (int) (26 * (1 - percent));
        SpriteCache.get(14).drawSprite(27 + xOffset - xOff, 45 - yOff);
        SpriteCache.get(9).drawSprite(32 + xOffset - xOff, 51 - yOff);
        smallFont.drawCenteredString(String.valueOf(level), 15 + xOffset - xOff, 67 - yOff,
                getOrbTextColor((int) (percent * 100)), true);

        if (percent < 1) {
            if (regenHealthStart > 0) {
                float difference = (int) (System.currentTimeMillis() - regenHealthStart);
                float angle = (difference / REGEN_HEALTH_TIME) * 360.0f;
                if (setting.draw_orb_arc) {
                    Rasterizer2D.draw_arc(24 + xOffset - xOff, 42 - yOff, 28, 28, 2, 90, -(int) angle, 0xff0000, 210, 0,
                            false);
                }
                if (angle > 358.0f && level != lastHp)
                    regenHealthStart = System.currentTimeMillis();
                lastHp = level;
            }
        }
    }

    private void loadPrayerOrb(int xOffset) {
        int yOff = setting.draw_special_orb ? !resized ? 10 : 2 : !resized ? 0 : -5;
        int xOff = setting.draw_special_orb ? !resized ? 0 : -7
                : !resized ? -1 : -7;
        SimpleImage bg = SpriteCache.get(prayHover ? 8 : 7);
        SimpleImage fg = SpriteCache.get(759 + (prayClicked ? 42 : 41));
        bg.drawSprite(xOffset - xOff, 85 - yOff);
        fg.drawShadedSprite(27 + xOffset - xOff, 89 - yOff, 233);
        int level = Integer.parseInt(Widget.cache[4012].defaultText.replaceAll("%", ""));
        int max = Integer.parseInt(Widget.cache[4013].defaultText.replaceAll("%", ""));
        double percent = level / (double) max;
        SpriteCache.get(14).height = (int) (26 * (1 - percent));
        SpriteCache.get(14).drawSprite(27 + xOffset - xOff, 89 - yOff);
        if (prayClicked) {
            SpriteCache.get(802).drawSprite(30 + xOffset - xOff, 92 - yOff);
        } else {
            SpriteCache.get(10).drawSprite(30 + xOffset - xOff, 92 - yOff);
        }
        smallFont.drawCenteredString(String.valueOf(level), 15 + xOffset - xOff, 111 - yOff,
                getOrbTextColor((int) (percent * 100)), true);
    }

    private int staminaActive;

    private void loadRunOrb(int xOffset) {
        int yOff = setting.draw_special_orb ? !resized ? 15 : 5 : !resized ? 1 : -4;
        int xMinus = setting.draw_special_orb ? !resized ? 14 : 5 : !resized ? -1 : -6;
        SimpleImage bg = SpriteCache.get(runHover ? 8 : 7);
        SimpleImage fg = SpriteCache.get(settings[ConfigUtility.RUN_ORB_ID] == 1 ? 4 : 3);
        bg.drawSprite(24 + xOffset - xMinus, 122 - yOff);
        fg.drawShadedSprite(51 + xOffset - xMinus, 126 - yOff, 233);
        int level = runEnergy;
        double percent = level / (double) 100;
        boolean staminaActive = this.staminaActive == 1;
        SpriteCache.get(14).height = (int) (26 * (1 - percent));
        SpriteCache.get(14).drawSprite(51 + xOffset - xMinus, 126 - yOff);
        SpriteCache.get(staminaActive ? 1035 : settings[ConfigUtility.RUN_ORB_ID] == 1 ? 12 : 11)
                .drawSprite(staminaActive ? 51 + xOffset - xMinus : 56 + xOffset - xMinus,
                        staminaActive ? 125 - yOff : 129 - yOff);
        smallFont.drawCenteredString(runEnergyString, 39 + xOffset - xMinus, 148 - yOff,
                getOrbTextColor((int) (percent * 100)), true);
    }

    private void loadAllOrbs(int xOffset) {
        loadHpOrb(xOffset);
        loadPrayerOrb(xOffset);
        loadRunOrb(xOffset);

        if (setting.draw_special_orb) {
            loadSpecialOrb(xOffset);
        }

        if (expCounterHover) {
            SpriteCache.get(counterOn ? 1944 : 1943)
                    .drawSprite(!resized ? xOffset : canvasWidth - 212, 20);
        } else {
            SpriteCache.get(counterOn ? 1944 : 1943).drawSprite(!resized ? xOffset : canvasWidth - 211, 20);
        }

        Rasterizer2D.draw_filled_rect(xOffset, 0, 1, 200, 0x332B16, 250);
    }

    private void get_entity_scene_pos(Entity entity, int height) {
        get_scene_pos(entity.x, height, entity.y);
    }

    private void get_scene_pos(int x, int vertical_offset, int y) {
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            scene_draw_x = -1;
            scene_draw_y = -1;
            return;
        }
        int z = get_tile_pos(plane, y, x) - vertical_offset;
        x -= cameraX;
        z -= cameraY;
        y -= cameraZ;

        int sin_y = Rasterizer3D.SINE[cameraPitch];
        int cos_y = Rasterizer3D.COSINE[cameraPitch];

        int sin_x = Rasterizer3D.SINE[cameraYaw];
        int cos_x = Rasterizer3D.COSINE[cameraYaw];

        int a_x = y * sin_x + x * cos_x >> 16;
        int b_x = y * cos_x - x * sin_x >> 16;

        int a_y = z * cos_y - b_x * sin_y >> 16;
        int b_y = z * sin_y + b_x * cos_y >> 16;

        if (b_y >= 50) {
            scene_draw_x = a_x * Clips.get3dZoom() / b_y + (Client.viewportWidth / 2) + viewportOffsetX;
            scene_draw_y = a_y * Clips.get3dZoom() / b_y + (Client.viewportHeight / 2);
        } else {
            this.scene_draw_x = -1;
            this.scene_draw_y = -1;
        }
    }

    private void buildSplitPrivateChatMenu() {
        if (splitPrivateChat == 0)
            return;
        int message = 0;
        if (rebootTimer != 0)
            message = 1;
        if (broadcastActive())
            message += 1;
        for (int index = 0; index < 100; index++)
            if (chatMessages[index] != null) {
                int type = chatMessages[index].getType();
                String name = chatMessages[index].getName();
                if ((type == 3 || type == 7)
                        && (type == 7 || privateChatMode == 0 || privateChatMode == 1 && check_username(name))) {
                    int y = broadcastActive() ? 309 - update_offset * 13 : 329 - message * 13;
                    if (resized) {
                        y = canvasHeight - 170 - message * 13;
                        if (broadcastActive()) {
                            y -= 20;
                        }
                    }
                    if (MouseHandler.mouseX > 4 && MouseHandler.mouseY - 4 > y - 10
                            && MouseHandler.mouseY - 4 <= y + 3) {
                        int width = regularFont.getTextWidth("From:  " + name + chatMessages[index]) + 25;
                        if (width > 450)
                            width = 450;
                        if (MouseHandler.mouseX < 4 + width) {
                            if (interfaceOpen()) {
                                return;
                            }
                            menuActionText[menuActionRow] = "Add ignore <col=FFFFFF>" + name;
                            menuActionTypes[menuActionRow] = 2042;
                            menuActionRow++;
                            menuActionText[menuActionRow] = "Add friend <col=FFFFFF>" + name;
                            menuActionTypes[menuActionRow] = 2337;
                            menuActionRow++;
                            menuActionText[menuActionRow] = "Reply to <col=FFFFFF>" + name;
                            menuActionTypes[menuActionRow] = 338;
                            menuActionRow++;
                        }
                    }
                    if (++message >= 5)
                        return;
                }
                if ((type == 5 || type == 6) && privateChatMode < 2 && ++message >= 5)
                    return;
            }
    }

    private void requestSpawnObject(int longetivity, int id, int orientation, int group,
            int y, int type, int plane, int x, int delay) {
        SpawnedObject object = null;
        for (SpawnedObject node = (SpawnedObject) spawns.first(); node != null; node = (SpawnedObject) spawns.next()) {
            if (node.plane != plane || node.x != x || node.y != y || node.group != group)
                continue;
            object = node;
            break;
        }

        if (object == null) {
            object = new SpawnedObject();
            object.plane = plane;
            object.group = group;
            object.x = x;
            object.y = y;
            handleTemporaryObjects(object);
            spawns.insertBack(object);
        }
        object.id = id;
        object.type = type;
        object.orientation = orientation;
        object.delay = delay;
        object.getLongetivity = longetivity;
    }

    private boolean interfaceIsSelected(Widget widget) {
        if (widget.valueCompareType == null)
            return false;
        for (int i = 0; i < widget.valueCompareType.length; i++) {
            int j = executeScript(widget, i);
            int k = widget.requiredValues[i];
            if (widget.valueCompareType[i] == 2) {
                if (j >= k)
                    return false;
            } else if (widget.valueCompareType[i] == 3) {
                if (j <= k)
                    return false;
            } else if (widget.valueCompareType[i] == 4) {
                if (j == k)
                    return false;
            } else if (j != k)
                return false;
        }

        return true;
    }

    private void updateOtherPlayerMovement(Buffer stream) {
        int count = stream.readBits(8);

        if (count < players_in_region) {
            for (int index = count; index < players_in_region; index++) {
                removedMobs[removedMobCount++] = local_players[index];
            }
        }
        if (count > players_in_region) {
            SignLink.reporterror(myUsername + " Too many players");
            throw new RuntimeException("eek");
        }
        players_in_region = 0;
        for (int globalIndex = 0; globalIndex < count; globalIndex++) {
            int index = local_players[globalIndex];
            Player player = players[index];
            player.index = index;
            int updateRequired = stream.readBits(1);

            if (updateRequired == 0) {
                local_players[players_in_region++] = index;
                player.last_update_tick = tick;
            } else {
                int movementType = stream.readBits(2);
                if (movementType == 0) {
                    local_players[players_in_region++] = index;
                    player.last_update_tick = tick;
                    mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;
                } else if (movementType == 1) {
                    local_players[players_in_region++] = index;
                    player.last_update_tick = tick;

                    int direction = stream.readBits(3);

                    player.moveInDir(false, direction);

                    int update = stream.readBits(1);

                    if (update == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;
                    }
                } else if (movementType == 2) {
                    local_players[players_in_region++] = index;
                    player.last_update_tick = tick;

                    int firstDirection = stream.readBits(3);
                    player.moveInDir(true, firstDirection);

                    int secondDirection = stream.readBits(3);
                    player.moveInDir(true, secondDirection);

                    int update = stream.readBits(1);
                    if (update == 1) {
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = index;
                    }
                } else if (movementType == 3) {
                    removedMobs[removedMobCount++] = index;
                }
            }
        }
    }

    private SimpleImage loginHover;
    private SimpleImage usernameHover;
    private SimpleImage passwordHover;
    public SimpleImage backgroundFix;
    private SimpleImage saveButton;
    public boolean musicHover;

    public static boolean adjustText = false;

    private void drawLoginScreen() {
        // System.out.println("Drawing login screen");
        int centerX = GameEngine.canvasWidth / 2;
        int centerY = GameEngine.canvasHeight / 2;

        boolean isLoading = Client.instance.gameState < 10;

        if (isLoading) {
            int barWidth = 304;

            int x = centerX - (barWidth / 2);
            int y = centerY - (34 / 2);

            Rasterizer2D.drawBox(x, y, barWidth, 34, 0x8C1111);
            Rasterizer2D.drawBox(x + 1, y + 1, 302, 32, 0x000000);
            Rasterizer2D.drawBox(x + 2, y + 2, getPixelAmt(loadingPercent, 300), 30, 0x8C1111);

            Client.boldFont.drawCenteredString(loadingText, (x + 1) + (302 / 2), y + 21, 0xFFFFFF, 0);
            Client.boldFont.drawCenteredString(ClientConstants.CLIENT_NAME + " is loading - please wait...",
                    (x) + (barWidth / 2), y - 14, 0xFFFFFF, 0);
        } else {
            backgroundFix.drawAdvancedSprite(0, 0);

            musicHover = (ClientConstants.CAN_SWITCH_MUSIC && mouseInRegion(726, 465, 764, 500));

            char c = '\u0168';
            if (loginStage == 0) {
                int i = 100;
                // adv_font_regular.draw(loadingMessage, i, c / 190, 0x75a9a9, true);
                if ((MouseHandler.mouseX >= 240) && (MouseHandler.mouseX <= 530) && (MouseHandler.mouseY >= 196)
                        && (MouseHandler.mouseY <= 228)) {
                    this.usernameHover.drawAdvancedSprite(240, 198);
                } else if ((MouseHandler.mouseX >= 240) && (MouseHandler.mouseX <= 530) && (MouseHandler.mouseY >= 249)
                        && (MouseHandler.mouseY <= 279)) {
                    this.passwordHover.drawAdvancedSprite(240, 249);
                }

                boldFont.drawCenteredString(firstLoginMessage, 385, adjustText ? 445 : 385, 16777215, true);
                boldFont.drawCenteredString(secondLoginMessage, 385, adjustText ? 465 : 400, 16777215, true);
                boldFont.draw(myUsername
                        + (((this.loginScreenCursorPos == 0 ? 1 : 0) & (tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""), 250,
                        219, 16777215, true);
                boldFont.draw(StringUtils.passwordAsterisks(myPassword)
                        + (((this.loginScreenCursorPos == 1 ? 1 : 0) & (tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""), 250,
                        272, 16777215, true);
                if ((MouseHandler.mouseX >= 303) && (MouseHandler.mouseX <= 465) && (MouseHandler.mouseY >= 295)
                        && (MouseHandler.mouseY <= 327)) {
                    this.loginHover.drawAdvancedSprite(299, 297);
                }
                if ((MouseHandler.mouseX >= 322) && (MouseHandler.mouseX <= 446) && (MouseHandler.mouseY >= 329)
                        && (MouseHandler.mouseY <= 347)) {
                    this.saveButton.drawAdvancedSprite(321, 328);
                }
            }

            if (ClientConstants.DEBUG_MODE) {
                regularFont.draw("cursor_x: " + MouseHandler.mouseX, 10, 20, 0xFFFFFF, 0);
                regularFont.draw("cursor_y: " + MouseHandler.mouseY, 10, 40, 0xFFFFFF, 0);
            }

            if (ClientConstants.CAN_SWITCH_MUSIC) {
                if (!ClientConstants.disableLoginScreenMusic && !low_detail) {
                    // Toggle Music
                    // playSong(ClientConstants.WINTER ? SoundConstants.XMAS_THEME :
                    // ClientConstants.HALLOWEEN ? SoundConstants.HWEEN_THEME :
                    // SoundConstants.SCAPE_RUNE);
                    SpriteCache.get(58).drawSprite(726, 464);
                } else {
                    SpriteCache.get(59).drawSprite(726, 464);
                    // stopMidi();
                }
            }

            accountManager.processAccountDrawing();
        }
    }

    private void drawLoginScreen(boolean flag) {
        drawLoginScreen();
    }

    public void raiseWelcomeScreen() {
        update_producers = true;
    }

    private void parseRegionPackets(Buffer stream, int packetType) {
        if (packetType == ServerToClientPackets.SEND_ALTER_GROUND_ITEM_COUNT) {
            int offset = stream.readUnsignedByte();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int itemId = stream.readUnsignedShort();
            int oldItemCount = stream.readUnsignedShort();
            int newItemCount = stream.readUnsignedShort();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                NodeDeque groundItemsDeque = scene_items[plane][xLoc][yLoc];
                if (groundItemsDeque != null) {
                    for (Item groundItem = (Item) groundItemsDeque
                            .first(); groundItem != null; groundItem = (Item) groundItemsDeque.next()) {
                        if (groundItem.id != (itemId & 0x7fff) || groundItem.quantity != oldItemCount)
                            continue;
                        groundItem.quantity = newItemCount;
                        net.runelite.api.Tile[][][] tiles = Client.instance.getScene().getTiles();
                        var tile = tiles[plane][xLoc][yLoc];
                        if (tile != null)
                            callbacks.post(new ItemSpawned(tile, groundItem));
                        break;
                    }

                    spawn_scene_item(xLoc, yLoc);
                }
            }
            return;
        }
        if (packetType == SEND_AREA_SOUND) {
            int id = stream.get_unsignedshort_le();
            int delays = stream.readUnsignedByteAdd();
            int hash = stream.readUnsignedByteAdd();
            int distance = hash >> 4 & 15;
            int loops = hash & 7;
            int offset = stream.readUnsignedByteAdd();
            int x = localX + (offset >> 4 & 7);
            int y = (offset & 7) + localY;
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                int radius = distance + 1;
                if (local_player.pathX[0] >= x - radius && local_player.pathX[0] <= radius + x
                        && local_player.pathY[0] >= y - radius && local_player.pathY[0] <= y + radius
                        && ClientConstants.areaSoundEffectVolume != 0 && loops > 0
                        && StaticSound.soundEffectCount < 50) {
                    StaticSound.soundEffectIds[StaticSound.soundEffectCount] = id;
                    StaticSound.queuedSoundEffectLoops[StaticSound.soundEffectCount] = loops;
                    StaticSound.queuedSoundEffectDelays[StaticSound.soundEffectCount] = delays;
                    StaticSound.soundEffects[StaticSound.soundEffectCount] = null;
                    StaticSound.soundLocations[StaticSound.soundEffectCount] = distance + (y << 8) + (x << 16);
                    ++StaticSound.soundEffectCount;
                }
            }
            return;
        }
        if (packetType == 215) {
            int i1 = stream.readUShortA();
            int l3 = stream.readUByteS();
            int k6 = localX + (l3 >> 4 & 7);
            int j9 = localY + (l3 & 7);
            int i12 = stream.readUShortA();
            int j14 = stream.readUnsignedShort();
            if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != localPlayerIndex) {
                Item item = new Item();
                item.id = i1;
                item.quantity = j14;
                if (scene_items[plane][k6][j9] == null)
                    scene_items[plane][k6][j9] = new NodeDeque();
                scene_items[plane][k6][j9].insertBack(item);
                net.runelite.api.Tile[][][] tiles = Client.instance.getScene().getTiles();
                var tile = tiles[plane][k6][j9];
                if (tile != null)
                    callbacks.post(new ItemSpawned(tile, item));
                spawn_scene_item(k6, j9);
            }
            return;
        }
        if (packetType == ServerToClientPackets.SEND_REMOVE_GROUND_ITEM) {
            int offset = stream.readUByteA();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int itemId = stream.readUnsignedShort();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                NodeDeque groundItemsDeque = scene_items[plane][xLoc][yLoc];
                if (groundItemsDeque != null) {
                    for (Item item = (Item) groundItemsDeque.first(); item != null; item = (Item) groundItemsDeque
                            .next()) {
                        if (item.id != (itemId & 0x7fff))
                            continue;
                        item.remove();
                        net.runelite.api.Tile[][][] tiles = Client.instance.getScene().getTiles();
                        var tile = tiles[plane][xLoc][yLoc];
                        if (tile != null)
                            callbacks.post(new ItemDespawned(tile, item));
                        break;
                    }

                    if (groundItemsDeque.first() == null)
                        scene_items[plane][xLoc][yLoc] = null;

                    spawn_scene_item(xLoc, yLoc);
                }
            }
            return;
        }
        if (packetType == ServerToClientPackets.ANIMATE_OBJECT) {
            int offset = stream.readUByteS();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int objectTypeFace = stream.readUByteS();
            int objectType = objectTypeFace >> 2;
            int objectFace = objectTypeFace & 3;
            int objectGenre = objectGroups[objectType];
            int animId = stream.readUShortA();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 103 && yLoc < 103) {
                if (objectGenre == 0) {// WallObject
                    Wall wallObjectObject = scene.get_wall(plane, xLoc, yLoc);
                    if (wallObjectObject != null) {
                        int objectId = ObjectKeyUtil.getObjectId(wallObjectObject.uid);
                        if (objectType == 2) {
                            wallObjectObject.wall = new SceneObject(objectId, 4 + objectFace, 2, plane, xLoc, yLoc,
                                    animId, false, wallObjectObject.wall);
                            wallObjectObject.corner = new SceneObject(objectId, objectFace + 1 & 3, 2, plane, xLoc,
                                    yLoc, animId, false, wallObjectObject.corner);
                        } else {
                            wallObjectObject.wall = new SceneObject(objectId, objectFace, objectType, plane, xLoc, yLoc,
                                    animId, false, wallObjectObject.wall);
                        }
                    }
                }
                if (objectGenre == 1) { // WallDecoration
                    WallDecoration wallDecoration = scene.get_wall_decor(xLoc, yLoc, plane);
                    if (wallDecoration != null)
                        wallDecoration.node = new SceneObject(ObjectKeyUtil.getObjectId(wallDecoration.uid), 0, 4,
                                plane, xLoc, yLoc, animId, false, wallDecoration.node);
                }
                if (objectGenre == 2) { // TiledObject
                    InteractiveObject tiledObject = scene.get_interactive_object(xLoc, yLoc, plane);
                    if (objectType == 11)
                        objectType = 10;
                    if (tiledObject != null)
                        tiledObject.renderable = new SceneObject(ObjectKeyUtil.getObjectId(tiledObject.uid), objectFace,
                                objectType, plane, xLoc, yLoc, animId, false, tiledObject.renderable);
                }
                if (objectGenre == 3) { // GroundDecoration
                    GroundDecoration groundDecoration = scene.get_ground_decor(yLoc, xLoc, plane);
                    if (groundDecoration != null)
                        groundDecoration.node = new SceneObject(ObjectKeyUtil.getObjectId(groundDecoration.uid),
                                objectFace, 22, plane, xLoc, yLoc, animId, false, groundDecoration.node);
                }
            }
            return;
        }
        if (packetType == ServerToClientPackets.TRANSFORM_PLAYER_TO_OBJECT) {
            int offset = stream.readUByteS();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int playerIndex = stream.readUnsignedShort();
            byte byte0GreaterXLoc = stream.readByteS();
            int startDelay = stream.readLEUShort();
            byte byte1GreaterYLoc = stream.readNegByte();
            int stopDelay = stream.readUnsignedShort();
            int objectTypeFace = stream.readUByteS();
            int objectType = objectTypeFace >> 2;
            int objectFace = objectTypeFace & 3;
            int objectGenre = objectGroups[objectType];
            byte byte2LesserXLoc = stream.readSignedByte();
            int objectId = stream.readUnsignedShort();
            byte byte3LesserYLoc = stream.readNegByte();
            Player player;
            if (playerIndex == localPlayerIndex)
                player = local_player;
            else
                player = players[playerIndex];
            if (player != null) {
                ObjectDefinition objectDefinition = ObjectDefinition.get(objectId);
                int sizeX;
                int sizeY;
                if (objectFace != 1 && objectFace != 3) {
                    sizeX = objectDefinition.sizeX;
                    sizeY = objectDefinition.sizeY;
                } else {
                    sizeX = objectDefinition.sizeY;
                    sizeY = objectDefinition.sizeX;
                }

                int left = xLoc + (sizeX >> 1);
                int right = xLoc + (sizeX + 1 >> 1);
                int top = yLoc + (sizeY >> 1);
                int bottom = yLoc + (sizeY + 1 >> 1);
                int[][] heights = tileHeights[plane];
                int mean = heights[left][bottom] + heights[right][top] + heights[left][top]
                        + heights[right][bottom] >> 2;
                int middleX = (xLoc << 7) + (sizeX << 6);
                int middleY = (yLoc << 7) + (sizeY << 6);

                Model model = objectDefinition.getModel(objectType, objectFace, heights, middleX, mean, middleY);
                if (model != null) {
                    requestSpawnObject(stopDelay + 1, -1, 0, objectGenre, yLoc, 0, plane, xLoc, startDelay + 1);
                    player.animationCycleStart = startDelay + tick;
                    player.animationCycleEnd = stopDelay + tick;
                    player.playerModel = model;
                    int playerSizeX = objectDefinition.sizeX;
                    int playerSizeY = objectDefinition.sizeY;
                    if (objectFace == 1 || objectFace == 3) {
                        playerSizeX = objectDefinition.sizeY;
                        playerSizeY = objectDefinition.sizeX;
                    }
                    player.field1117 = xLoc * 128 + playerSizeX * 64;
                    player.field1123 = yLoc * 128 + playerSizeY * 64;
                    player.tileHeight2 = get_tile_pos(plane, player.field1123, player.field1117);
                    if (byte2LesserXLoc > byte0GreaterXLoc) {
                        byte tmp = byte2LesserXLoc;
                        byte2LesserXLoc = byte0GreaterXLoc;
                        byte0GreaterXLoc = tmp;
                    }
                    if (byte3LesserYLoc > byte1GreaterYLoc) {
                        byte tmp = byte3LesserYLoc;
                        byte3LesserYLoc = byte1GreaterYLoc;
                        byte1GreaterYLoc = tmp;
                    }
                    player.minX = xLoc + byte2LesserXLoc;
                    player.maxX = xLoc + byte0GreaterXLoc;
                    player.minY = yLoc + byte3LesserYLoc;
                    player.maxY = yLoc + byte1GreaterYLoc;
                }
            }
        }
        if (packetType == ServerToClientPackets.SEND_OBJECT) {
            int offset = stream.readUByteA();
            int x = localX + (offset >> 4 & 7);
            int y = localY + (offset & 7);
            int id = stream.readLEUShort();
            int objectTypeFace = stream.readUByteS();
            int type = objectTypeFace >> 2;
            int orientation = objectTypeFace & 3;
            int group = objectGroups[type];
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                requestSpawnObject(-1, id, orientation, group, y, type, plane, x, 0);
            }
            return;
        }

        if (packetType == ServerToClientPackets.SEND_GFX) {
            int offset = stream.readUnsignedByte();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            int gfxId = stream.readUnsignedShort();
            int gfxHeight = stream.readUnsignedByte();
            int gfxDelay = stream.readUnsignedShort();
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                xLoc = xLoc * 128 + 64;
                yLoc = yLoc * 128 + 64;
                SpotAnimEntity loneGfx = new SpotAnimEntity(plane, tick, gfxDelay, gfxId,
                        get_tile_pos(plane, yLoc, xLoc) - gfxHeight, yLoc, xLoc);
                incompleteAnimables.insertBack(loneGfx);
            }
            return;
        }

        if (packetType == ServerToClientPackets.SEND_GROUND_ITEM) {
            int itemId = stream.readLEUShortA();
            int itemCount = stream.readInt();
            int offset = stream.readShort();
            int xLoc = localX + (offset >> 4 & 7);
            int yLoc = localY + (offset & 7);
            if (xLoc >= 0 && yLoc >= 0 && xLoc < 104 && yLoc < 104) {
                Item groundItem = new Item();
                groundItem.id = itemId;
                groundItem.quantity = itemCount;
                if (scene_items[plane][xLoc][yLoc] == null)
                    scene_items[plane][xLoc][yLoc] = new NodeDeque();
                scene_items[plane][xLoc][yLoc].insertBack(groundItem);
                net.runelite.api.Tile[][][] tiles = Client.instance.getScene().getTiles();
                var tile = tiles[plane][xLoc][yLoc];
                if (tile != null)
                    callbacks.post(new ItemSpawned(tile, groundItem));
                spawn_scene_item(xLoc, yLoc);
            }
            return;
        }
        if (packetType == ServerToClientPackets.SEND_REMOVE_OBJECT) {
            int objectTypeFace = stream.readNegUByte();
            int type = objectTypeFace >> 2;
            int orientation = objectTypeFace & 3;
            int group = objectGroups[type];
            int offset = stream.readUnsignedByte();
            int x = localX + (offset >> 4 & 7);
            int y = localY + (offset & 7);
            // System.out.println("despawn " + objectTypeFace + " " + type + " " +
            // orientation+" from "+localX+" to "+x+","+y);
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                requestSpawnObject(-1, -1, orientation, group, y, type, plane, x, 0);

                /*
                 * Item groundItem = new Item();
                 * groundItem.id = 227;
                 * groundItem.quantity = 1;
                 * if (scene_items[plane][x][y] == null)
                 * scene_items[plane][x][y] = new LinkedList();
                 * scene_items[plane][x][y].insertBack(groundItem);
                 * spawn_scene_item(x, y);
                 */
            }
            return;
        }
        if (packetType == ServerToClientPackets.SEND_PROJECTILE) {
            int offset = stream.readUnsignedByte();
            int sourceX = localX + (offset >> 4 & 7);
            int sourceY = localY + (offset & 0x7);
            int destX = sourceX + stream.readSignedByte();
            int destY = sourceY + stream.readSignedByte();
            int target = stream.readShort();
            int gfxMoving = stream.readUnsignedShort();
            int startHeight = stream.readUnsignedByte() * 4;
            int endHeight = stream.readUnsignedByte() * 4;
            int startDelay = stream.readUnsignedShort();
            int speed = stream.readUnsignedShort();
            int initialSlope = stream.readUnsignedByte();
            int frontOffset = stream.readUnsignedByte();
            if (sourceX >= 0 && sourceY >= 0 && sourceX < 104 && sourceY < 104 && destX >= 0 && destY >= 0
                    && destX < 104 && destY < 104
                    && gfxMoving != 65535) {
                sourceX = sourceX * 128 + 64;
                sourceY = sourceY * 128 + 64;
                destX = destX * 128 + 64;
                destY = destY * 128 + 64;
                Projectile projectile = new Projectile(initialSlope, endHeight, startDelay + tick,
                        speed + tick, frontOffset, plane, get_tile_pos(plane, sourceY, sourceX) - startHeight, sourceY,
                        sourceX,
                        target, gfxMoving);
                projectile.setDestination(destX, destY, get_tile_pos(plane, destY, destX) - endHeight,
                        startDelay + tick);
                projectiles.insertBack(projectile);
            }
        }
    }

    private void method139(Buffer stream) {
        stream.initBitAccess();
        int k = stream.readBits(8);
        if (k < npcs_in_region) {
            for (int l = k; l < npcs_in_region; l++)
                removedMobs[removedMobCount++] = local_npcs[l];

        }
        if (k > npcs_in_region) {
            SignLink.reporterror(myUsername + " Too many npcs");
            throw new RuntimeException("eek");
        }
        npcs_in_region = 0;
        for (int i1 = 0; i1 < k; i1++) {
            int j1 = local_npcs[i1];
            Npc npc = npcs[j1];
            npc.index = j1;
            int k1 = stream.readBits(1);
            if (k1 == 0) { // no move change
                local_npcs[npcs_in_region++] = j1;
                npc.last_update_tick = tick;
            } else {
                int l1 = stream.readBits(2);
                if (l1 == 0) {
                    local_npcs[npcs_in_region++] = j1;
                    npc.last_update_tick = tick; // generic update (npc has at least one update flag)
                    mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j1;
                } else if (l1 == 1) {
                    local_npcs[npcs_in_region++] = j1;
                    npc.last_update_tick = tick;
                    int i2 = stream.readBits(3);
                    npc.moveInDir(false, i2);
                    int k2 = stream.readBits(1);
                    if (k2 == 1)
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j1;
                } else if (l1 == 2) {
                    local_npcs[npcs_in_region++] = j1;
                    npc.last_update_tick = tick;
                    int j2 = stream.readBits(3);
                    npc.moveInDir(true, j2);
                    int l2 = stream.readBits(3);
                    npc.moveInDir(true, l2);
                    int i3 = stream.readBits(1);
                    if (i3 == 1)
                        mobsAwaitingUpdate[mobsAwaitingUpdateCount++] = j1;
                } else if (l1 == 3)
                    removedMobs[removedMobCount++] = j1;
            }
        }
    }

    public final SecondsTimer loginTimer = new SecondsTimer();

    private void doCycleLoggedOut() {
        boolean isLoading = Client.instance.gameState < 10;
        if (isLoading) {
            return;
        }
        try {
            accountManager.processAccountInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.loginStage == 0 || this.loginStage == 2) {
            if (MouseHandler.keypressedEventIndex == 1) {
                if ((MouseHandler.mouseX >= 267) && (MouseHandler.mouseX <= 508) && (MouseHandler.mouseY >= 177)
                        && (MouseHandler.mouseY <= 232)) {
                    loginScreenCursorPos = 0;
                } else if ((MouseHandler.mouseX >= 267) && (MouseHandler.mouseX <= 508) && (MouseHandler.mouseY >= 235)
                        && (MouseHandler.mouseY <= 290)) {
                    loginScreenCursorPos = 1;
                } else if ((MouseHandler.mouseX >= 305) && (MouseHandler.mouseX <= 461) && (MouseHandler.mouseY >= 290)
                        && (MouseHandler.mouseY <= 329)) {
                    if ((myUsername.length() > 0) && (myPassword.length() > 0)) {
                        if (loginTimer.finished()) {
                            login(myUsername, myPassword, false);
                            loginTimer.start(2);
                        }
                    } else {
                        this.loginScreenCursorPos = 0;
                        this.firstLoginMessage = "Username & Password";
                        this.secondLoginMessage = "Must be more than 1 character";
                    }
                    if (loggedIn) {
                        return;
                    }
                } else if ((MouseHandler.mouseX >= 337) && (MouseHandler.mouseX <= 432) && (MouseHandler.mouseY >= 328)
                        && (MouseHandler.mouseY <= 348)) {
                    try {
                        accountManager.addAccount(myUsername, myPassword);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (musicHover) {
                    setting.music = !setting.music;
                    setting.save();
                    StaticSound.LoginButton();
                }
            }
            do {
                while (true) {
                    label1776: do {
                        while (true) {
                            while (keyManager.hasNextKey()) {

                                int l1 = keyManager.lastTypedCharacter;
                                if (l1 == -1)
                                    break;
                                boolean flag1 = false;
                                for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
                                    if (l1 != validUserPassChars.charAt(i2))
                                        continue;
                                    flag1 = true;
                                    break;
                                }

                                if (loginScreenCursorPos == 0) {
                                    if (l1 == 8 && myUsername.length() > 0)
                                        myUsername = myUsername.substring(0, myUsername.length() - 1);
                                    if (l1 == 9 || l1 == 10 || l1 == 13)
                                        loginScreenCursorPos = 1;
                                    if (flag1)
                                        myUsername += (char) l1;
                                    if (myUsername.length() > 12)
                                        myUsername = myUsername.substring(0, 12);
                                } else if (loginScreenCursorPos == 1) {
                                    if (l1 == 8 && myPassword.length() > 0)
                                        myPassword = myPassword.substring(0, myPassword.length() - 1);
                                    if (l1 == 9 || l1 == 10 || l1 == 13) {
                                        if (loginTimer.finished()) {
                                            login(myUsername, myPassword, false);
                                            loginTimer.start(2);
                                        }
                                    }
                                    if (flag1) {
                                        myPassword += (char) l1;
                                    }
                                    if (myPassword.length() > 20) {
                                        myPassword = myPassword.substring(0, 20);
                                    }
                                }
                            }
                            return;
                        }
                    } while (true);
                }
            } while (true);
        }
        if (loginStage == 3) {
            int k = canvasWidth / 2;
            int j1 = canvasHeight / 2 + 50;
            j1 += 20;
            if (MouseHandler.keypressedEventIndex == 1 && MouseHandler.saveClickX >= k - 75
                    && MouseHandler.saveClickX <= k + 75 && MouseHandler.saveClickY >= j1 - 20
                    && MouseHandler.saveClickY <= j1 + 20)
                loginStage = 0;
        }
    }

    private void updatePlayers(int packetSize, Buffer stream) {
        removedMobCount = 0;
        mobsAwaitingUpdateCount = 0;
        updateLocalPlayerMovement(stream);
        updateOtherPlayerMovement(stream);
        updatePlayerList(stream, packetSize);
        parsePlayerSynchronizationMask(stream);
        for (int count = 0; count < removedMobCount; count++) {
            int index = removedMobs[count];

            if (players[index].last_update_tick != tick) {
                players[index] = null;
            }
        }

        if (stream.pos != packetSize) {
            addReportToServer("Player updating broke, this is very bad.");
            addReportToServer(
                    "Make sure to check buffer received datatypes in client match buffer sent datatypes from server.");
            SignLink.reporterror("Error packet size mismatch in getplayer pos:" + stream.pos + " psize:" + packetSize);
            throw new RuntimeException("eek");
        }
        for (int count = 0; count < players_in_region; count++) {
            if (players[local_players[count]] == null) {
                addReportToServer("Player updating broke, this is really bad.");
                SignLink.reporterror(
                        myUsername + " null entry in pl list - pos:" + count + " size:" + players_in_region);
                throw new RuntimeException("eek");
            }
        }
    }

    static int method8016(int var0, int var1) {
        int var2 = var1 - 334;
        if (var2 < 0) {
            var2 = 0;
        } else if (var2 > 100) {
            var2 = 100;
        }

        int var3 = (Client.zoomWidth - Client.zoomHeight) * var2 / 100 + Client.zoomHeight;
        return var0 * var3 / 256;
    }

    public final void method446(int var0, int var1, int var2, int x, int y, int height, int var6) {
        height = method8016(height, var6);
        int diffX = 2048 - x & 2047;
        int diffY = 2048 - y & 2047;
        int xOffset = 0;
        int yOffset = 0;
        int z = height;
        int var12;
        int var13;
        int var14;

        if (diffX != 0) {
            var12 = Rasterizer3D.SINE[diffX];
            var13 = Rasterizer3D.COSINE[diffX];
            var14 = var13 * yOffset - var12 * height >> 16;
            z = var12 * yOffset + var13 * height >> 16;
            yOffset = var14;
        }

        if (diffY != 0) {
            var12 = Rasterizer3D.SINE[diffY];
            var13 = Rasterizer3D.COSINE[diffY];
            var14 = var12 * z + var13 * xOffset >> 16;
            z = z * var13 - xOffset * var12 >> 16;
            xOffset = var14;
        }

        cameraX = var0 - xOffset;
        cameraZ = var1 - yOffset;
        cameraY = var2 - z;
        cameraPitch = y;
        cameraYaw = x;
    }

    private void set_camera_pos(int depth, int tilt_curve, int x, int z, int pan_curve, int y, int viewportHeight) {
        depth = method8016(depth, viewportHeight);
        int tilt_diff = 2048 - tilt_curve & 0x7ff;
        int pan_diff = 2048 - pan_curve & 0x7ff;
        int x_offset = 0;
        int z_offset = 0;
        int y_offset = depth;
        if (tilt_diff != 0) {
            int sin = Model.SINE[tilt_diff];
            int cos = Model.COSINE[tilt_diff];
            int pos = z_offset * cos - y_offset * sin >> 16;
            y_offset = z_offset * sin + y_offset * cos >> 16;
            z_offset = pos;
        }
        if (pan_diff != 0) {
            int sin = Model.SINE[pan_diff];
            int cos = Model.COSINE[pan_diff];
            int pos = y_offset * sin + x_offset * cos >> 16;
            y_offset = y_offset * cos - x_offset * sin >> 16;
            x_offset = pos;
        }
        cameraX = x - x_offset;
        cameraY = z - z_offset;
        cameraZ = y - y_offset;
        cameraPitch = tilt_curve;
        cameraYaw = pan_curve;
    }

    private boolean cheapHaxPacket(int id, String text) {
        if (id == 99900) { // @shadowrs
            String[] parts = text.split(":");
            int specialCheck = Integer.parseInt(parts[1]),
                    specialBar = Integer.parseInt(parts[2]),
                    specialAmount = Integer.parseInt(parts[3]);
            for (int i = 0; i < 10; i++) {
                moveWidget(specialAmount >= specialCheck ? 500 : 0, 0, --specialBar);
                specialCheck--;
            }
            return true;
        }
        if (id == 52260 && text.equals("--clearall--")) {
            for (int i = 0; i < 28; i++) {
                sendString("", 52260 + i);
                sendString("", 52290 + i);
            }
            return true;
        }
        return false;
    }

    void moveWidget(int horizontalOffset, int verticalOffset, int id) {
        Widget widget = Widget.cache[id];
        widget.x = horizontalOffset;
        widget.y = verticalOffset;
    }

    public void updateString(String text, int id) {
        if (Widget.cache[id] != null) {
            Widget.cache[id].defaultText = text;
            Widget.cache[id].scrollPosition = 0;
        }
    }

    public void toggleConfig(int configId, int value) {
        anIntArray1045[configId] = value;
        if (settings[configId] != value) {
            settings[configId] = value;
            updateVarp(configId);
        }
    }

    public static final HashMap<Integer, OldState> kek1 = new HashMap();

    public static class OldState {
        public int x, y;
        public AdvancedFont font;
    }

    public void setSidebarInterface(int sidebarID, int interfaceID) {
        tabInterfaceIDs[sidebarID] = interfaceID;
        update_tab_producer = true;
    }

    /**
     * Sends a string
     */
    public void sendString(String text, int interfaceId) {

        if (Widget.cache[interfaceId] == null) {
            return;
        }
        if (widget_overlay_id == 52250) { // trade screen 2
            int loop = 0;
            // reset back to original positons before changing to reset prev trade
            // adjustments
            for (int i = 0; i < 28; i++) {
                OldState oldState = kek1.get(interfaceId + i);
                if (oldState != null) {
                    Widget.cache[interfaceId + i].x = oldState.x;
                    Widget.cache[interfaceId + i].y = oldState.y;
                    Widget.cache[interfaceId + i].text_type = oldState.font;
                }
            }
            // String[] text2 = text.split("\\\\n");
            String[] text2 = text.split("<br>");
            for (String texts : text2) {
                Widget.cache[interfaceId + loop].defaultText = texts;
                loop++;

                // loop 12 i guess is the threshold for items displayed on this interface
                // other loops will be username, accept buton, pkp string etc
                if (loop > 12) {
                    // System.out.println("beo wtf: "+Arrays.toString(text2));
                    // System.out.printf("client txt %s from %s%n", texts, loop);

                    for (int i = 0; i < 28; i++) {
                        Widget sub = Widget.cache[interfaceId + i];
                        OldState oldState = kek1.get(interfaceId + i);
                        if (oldState == null) {
                            // cache the original before changing
                            oldState = new OldState();
                            oldState.x = sub.x;
                            oldState.y = sub.y;
                            oldState.font = sub.text_type;
                            kek1.put(interfaceId + i, oldState);
                        }
                        // half the currently displayed amount of items: 28 -> 14 or 20 -> 10
                        // to get 2 columns
                        if (i <= text2.length / 2) {
                            // column 1 render
                            sub.x -= 55;
                        }
                        if (i > text2.length / 2) {
                            int base2ndColumnId = text2.length / 2;
                            sub.y -= (i + 1) * 13; // put the text at the top firstly
                            int additionals = (i - base2ndColumnId) * 13;
                            // column 1 render
                            sub.x += 55;
                            sub.y += additionals;
                            // System.out.printf("base %s this id:%s add:%s remove: %s%n", base2ndColumnId,
                            // i, additionals, sub.y, base2ndColumnId * 13);
                        }
                        sub.text_type = regularFont;
                    }
                    // only adjust the columns, dont keep adjusting for every loop after the first
                    // item
                    break;
                }
            }
            return;
        }
        Widget.cache[interfaceId].defaultText = text;
    }

    public boolean handleConfirmationPacket(int state, int value) {
        switch (state) {
            case 0:
                return true;
            case 1:
                switch (value) {
                }
                return true;
            case 2:
                switch (value) {
                    case 0:
                        Widget.cache[widgetId].disabledSprite = SpriteCache.get(Widget.cache[widgetId].clickSprite1);
                        Widget.cache[widgetId].enabledSprite = SpriteCache.get(Widget.cache[widgetId].clickSprite1);
                        return true;
                    case 1:
                        return true;

                }
                return true;

            case 4:
                if (value == 1) {
                    return true;
                }
                return true;
        }
        return false;
    }

    public void sendButtonClick(int button, int toggle, int type) {
        Widget widget = Widget.cache[button];
        // case reset setting widget
        switch (type) {
            case 135:
                boolean flag8 = true;
                if (widget.contentType > 0) {
                    flag8 = promptUserForInput(widget);
                }
                if (flag8) {
                    packetSender.sendButtonClick(button);
                }
                break;
            case 646:
                packetSender.sendButtonClick(button);
                if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                    if (settings[toggle] != widget.requiredValues[0]) {
                        settings[toggle] = widget.requiredValues[0];
                        updateVarp(toggle);
                    }
                }
                break;
            case 169:
                packetSender.sendButtonClick(button);
                if (widget.valueIndexArray != null && widget.valueIndexArray[0][0] == 5) {
                    settings[toggle] = 1 - settings[toggle];
                    updateVarp(toggle);
                }
                break;
            default:
                System.out.println("button: " + button + " - toggle: " + toggle + " - type: " + type);
        }
    }

    /**
     * Sets button configurations on interfaces.
     */
    public void sendConfiguration(int id, int value) {
        // System.out.println("id: " + id + " vs value: " + value);
        if (id < anIntArray1045.length) {
            anIntArray1045[id] = value;
            if (settings[id] != value) {
                settings[id] = value;
                updateVarp(id);
                if (dialogueId != -1)
                    update_chat_producer = true;
            }
        }
    }

    /**
     * Clears the screen of all open interfaces.
     */
    public void clearScreen() {
        if (overlayInterfaceId != -1) {
            overlayInterfaceId = -1;
            update_tab_producer = true;
        }
        if (backDialogueId != -1) {
            backDialogueId = -1;
            update_chat_producer = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            update_chat_producer = true;
        }
        widget_overlay_id = -1;
        continuedDialogue = false;
    }

    /**
     * Displays an interface over the sidebar area.
     */
    public void inventoryOverlay(int interfaceId, int sideInterfaceId) {
        if (backDialogueId != -1) {
            backDialogueId = -1;
            update_chat_producer = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            update_chat_producer = true;
        }
        widget_overlay_id = interfaceId;
        overlayInterfaceId = sideInterfaceId;
        update_tab_producer = true;
        continuedDialogue = false;
    }

    private boolean readPacket() {
        if (SERVER_SOCKET == null)
            return false;
        try {
            int available = SERVER_SOCKET.available();
            if (available == 0)
                return false;

            // Read opcode...
            if (opcode == -1) {
                SERVER_SOCKET.flushInputStream(incoming.payload, 1);
                opcode = incoming.payload[0] & 0xff;
                if (encryption != null)
                    opcode = (opcode - encryption.value()) & 0xff;

                packetSize = ServerToClientPackets.PACKET_SIZES[opcode];
                available--;
            }

            // The packet size -3 means the packet shouldn't be sent by the server, did we
            // forget to add the size in the client ServerToClientPackets class?
            if (packetSize == -3) {
                addReportToServer("The packet opcode " + opcode + " is not whitelisted!");
                return false;
            }

            // Read size
            if (packetSize == -1)
                if (available > 0) {
                    SERVER_SOCKET.flushInputStream(incoming.payload, 1);
                    packetSize = incoming.payload[0] & 0xff;
                    available--;
                } else {
                    return false;
                }

            if (packetSize == -2)
                if (available > 1) {
                    SERVER_SOCKET.flushInputStream(incoming.payload, 2);
                    incoming.pos = 0;
                    packetSize = incoming.readUnsignedShort();
                    available -= 2;
                } else {
                    return false;
                }

            if (available < packetSize) {
                // data still in progress being sent over internet. wait until all expected
                // bytes are available before moving onto parsing.
                return false;
            }

            incoming.pos = 0;

            if (timeoutCounter > 50) {
                String[] data = new String[] { String.valueOf(timeoutCounter), String.valueOf(opcode),
                        String.valueOf(lastOpcode), String.valueOf(secondLastOpcode), String.valueOf(thirdLastOpcode) };
                // final String txt = "timeoutCounter above threshold " + timeoutCounter + ",
                // packet: opcode: " + opcode + ", lastOpcode: " + lastOpcode + ",
                // secondLastOpcode: " + secondLastOpcode + ", thirdLastOpcode: " +
                // thirdLastOpcode;
                // addReportToServer(txt);
                // addReportToServer(Arrays.toString(data));
            }

            try {
                SERVER_SOCKET.flushInputStream(incoming.payload, packetSize);
            } catch (IOException e) {
                processNetworkError();
            }

            timeoutCounter = 0;
            thirdLastOpcode = secondLastOpcode;
            secondLastOpcode = lastOpcode;
            lastOpcode = opcode;

            if (debug_packet_info) {
                // addReportToServer("> " + opcode);
            }

            // Size is 1 (packet number is 6)
            if (opcode == ServerToClientPackets.HEALTH_ORB) {
                try {
                    poisonType = incoming.readNegUByte();
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer(e.getMessage());
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_TOTAL_EXP) {
                totalExperience = incoming.readLong();
                opcode = -1;
                return true;
            }

            // Size is 5 (packet number is 7)
            if (opcode == ServerToClientPackets.ADD_CLICKABLE_SPRITES) {
                int componentId = 0;
                try {
                    componentId = incoming.readInt();
                    byte spriteIndex = incoming.readSignedByte();
                    Widget component = Widget.cache[componentId];
                    if (component != null) {
                        if (component.backgroundSprites != null
                                && spriteIndex <= component.backgroundSprites.length - 1) {
                            SimpleImage sprite = component.backgroundSprites[spriteIndex];
                            if (sprite != null) {
                                component.enabledSprite = component.backgroundSprites[spriteIndex];
                            }
                        }
                    }
                    opcode = -1;
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer("Packet 7 error caused by interface: " + componentId);
                    addReportToServer(e.getMessage());
                }
                return true;
            }

            // Size is 6 opcode 128
            if (opcode == ServerToClientPackets.SET_FRAME_MODE) {
                int width = incoming.readLEUShortA();
                int height = incoming.readInt();
                // frameMode(width, height);
                opcode = -1;
                return true;
            }

            if (opcode == SEND_AREA_SOUND) {
                int id = incoming.get_unsignedshort_le();
                int delays = incoming.readUnsignedByteAdd();
                int hash = incoming.readUnsignedByteAdd();
                int distance = hash >> 4 & 15;
                int loops = hash & 7;
                int offset = incoming.readUnsignedByteAdd();
                int x = localX + (offset >> 4 & 7);
                int y = (offset & 7) + localY;
                if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                    int radius = distance + 1;
                    if (local_player.pathX[0] >= x - radius && local_player.pathX[0] <= radius + x
                            && local_player.pathY[0] >= y - radius && local_player.pathY[0] <= y + radius
                            && ClientConstants.areaSoundEffectVolume != 0 && loops > 0
                            && StaticSound.soundEffectCount < 50) {
                        StaticSound.soundEffectIds[StaticSound.soundEffectCount] = id;
                        StaticSound.queuedSoundEffectLoops[StaticSound.soundEffectCount] = loops;
                        StaticSound.queuedSoundEffectDelays[StaticSound.soundEffectCount] = delays;
                        StaticSound.soundEffects[StaticSound.soundEffectCount] = null;
                        StaticSound.soundLocations[StaticSound.soundEffectCount] = distance + (y << 8) + (x << 16);
                        ++StaticSound.soundEffectCount;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_EXP_DROP) {
                try {
                    int skill = incoming.readUnsignedByte();
                    int experience = incoming.readInt();
                    boolean increment = incoming.readUnsignedByte() == 1;
                    xpLocked = incoming.readByteS() == 1;
                    ExpCounter.addXP(skill, experience, increment);
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer(e.getMessage());
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.RECEIVE_WIDGET_SLOT) {
                int slot = incoming.readUnsignedByte();
                Widget tradeWidget = Widget.cache[52046 + slot];
                tradeSlot.add(new TradeOpacity(tradeWidget, slot, 2));
                Widget.cache[52014].drawingDisabled = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_SCROLLBAR_HEIGHT) {
                int interface_ = incoming.readInt();
                int scrollMax = incoming.readShort();
                Widget w = Widget.cache[interface_];
                if (w != null) {
                    w.scrollMax = scrollMax;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.INTERFACE_SCROLL_RESET) {
                int interface_ = incoming.readInt();
                Widget w = Widget.cache[interface_];
                if (w != null) {
                    w.scrollPosition = 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_WEAPON) {
                int weaponId = incoming.readUnsignedShort();
                int ammoId = incoming.readUnsignedShort();
                WeaponInterfacesWidget.weaponId = weaponId;
                WeaponInterfacesWidget.ammoId = ammoId;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_SPRITE_CHANGE) {
                int interfaceID = incoming.readInt();
                int spriteID = incoming.readShort();

                Widget.cache[interfaceID].enabledSprite = SpriteCache.get(spriteID);

                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.UPDATE_PLAYER_RIGHTS) {
                myPrivilege = incoming.readUnsignedByte();
                donatorPrivilege = incoming.readUnsignedByte();
                ironmanPrivilege = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.PLAYER_UPDATING) {
                updatePlayers(packetSize, incoming);
                loadingMap = false;
                opcode = -1;
                if (debug_packet_info) {
                    addReportToServer("last gpi " + (System.currentTimeMillis() - LAST_GPI) + " ms ago.. call count "
                            + Client.tick + " (+" + (tick - lastcallcount) + ")");
                }
                lastcallcount = tick;
                LAST_GPI = System.currentTimeMillis();
                xpro = 0;
                return true;
            }

            if (opcode == ServerToClientPackets.SHOW_CLANCHAT_OPTIONS) {
                showClanOptions = incoming.readUnsignedByte() == 1;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.ENTITY_FEED) {
                try {
                    pushFeed(incoming.readString(), incoming.readUnsignedShort(), incoming.readUnsignedShort());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.OPEN_WELCOME_SCREEN) {
                daysSinceRecovChange = incoming.readNegUByte();
                unreadMessages = incoming.readUShortA();
                membersInt = incoming.readUnsignedByte();
                anInt1193 = incoming.readIMEInt();
                daysSinceLastLogin = incoming.readUnsignedShort();
                if (anInt1193 != 0 && widget_overlay_id == -1) {
                    SignLink.dnslookup(StringUtils.decodeIp(anInt1193));
                    clearTopInterfaces();
                    char character = '\u028A';
                    if (daysSinceRecovChange != 201 || membersInt == 1)
                        character = '\u028F';
                    reportAbuseInput = "";
                    canMute = false;
                    for (int interfaceId = 0; interfaceId < Widget.cache.length; interfaceId++) {
                        if (Widget.cache[interfaceId] == null
                                || Widget.cache[interfaceId].contentType != character)
                            continue;
                        widget_overlay_id = Widget.cache[interfaceId].parent;

                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == 178) {
                clearRegionalSpawns();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.DELETE_GROUND_ITEM) {
                localX = incoming.readNegUByte();
                localY = incoming.readUByteS();
                for (int x = localX; x < localX + 8; x++) {
                    for (int y = localY; y < localY + 8; y++)
                        if (scene_items[plane][x][y] != null) {
                            scene_items[plane][x][y] = null;
                            spawn_scene_item(x, y);
                        }
                }

                for (SpawnedObject object = (SpawnedObject) spawns
                        .reverseGetFirst(); object != null; object = (SpawnedObject) spawns.reverseGetNext())
                    if (object.x >= localX && object.x < localX + 8 && object.y >= localY && object.y < localY + 8
                            && object.plane == plane)
                        object.getLongetivity = 0;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SHOW_PLAYER_HEAD_ON_INTERFACE) {
                int playerHeadModelId = incoming.readLEUShortA();
                Widget.cache[playerHeadModelId].defaultMediaType = 3;
                if (local_player.desc == null)
                    Widget.cache[playerHeadModelId].defaultMedia = (local_player.appearanceColors[0] << 25)
                            + (local_player.appearanceColors[4] << 20) + (local_player.player_appearance[0] << 15)
                            + (local_player.player_appearance[8] << 10) + (local_player.player_appearance[11] << 5)
                            + local_player.player_appearance[1];
                else
                    Widget.cache[playerHeadModelId].defaultMedia = (int) (0x12345678L
                            + local_player.desc.interfaceType);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLAN_CHAT) {
                try {
                    name = incoming.readString();
                    defaultText = incoming.readString();
                    clanname = incoming.readString();
                    rights = incoming.readUnsignedShort();
                    // System.out.println("name is " + defaultText);
                    sendMessage(Character.toUpperCase(defaultText.charAt(0)) + defaultText.substring(1), 16, name);
                    // sendMessage(new ChatMessage(clanname, name,
                    // Character.toUpperCase(defaultText.charAt(0)) + defaultText.substring(1),
                    // 16));
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer(e.getMessage());
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_RESET) {
                Camera.isCameraUpdating = false;
                isCameraLocked = false;
                Camera.followCameraMode = false;
                Camera.staticCameraMode = false;
                Camera.staticCameraX = 0;
                Camera.staticCameraY = 0;
                Camera.staticCameraAltitudeOffset = 0;
                Camera.fixedCamera = false;
                Camera.cameraPitchStep = 0;
                Camera.cameraYawSpeed = 0;
                Camera.cameraInterpolationSpeed = 0;
                Camera.cameraMinimumStep = 0;
                Camera.cameraTargetX = 0;
                Camera.cameraTargetY = 0;
                Camera.cameraAltitudeOffset = 0;
                Camera.camera = null;
                Camera.cameraPitch = null;
                Camera.cameraYaw = null;

                for (int var20 = 0; var20 < 5; ++var20) {
                    quakeDirectionActive[var20] = false;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLEAN_ITEMS_OF_INTERFACE) {
                int id = incoming.readUnsignedShort();
                Widget widget = Widget.cache[id];
                for (int slot = 0; slot < widget.inventoryItemId.length; slot++) {
                    widget.inventoryItemId[slot] = -1;
                    widget.inventoryItemId[slot] = 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_SPIN) {
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.followCameraMode = false;
                Camera.cameraTargetX = incoming.readUnsignedByte() * 16384;
                Camera.cameraTargetY = incoming.readUnsignedByte() * 16384;
                Camera.cameraAltitudeOffset = incoming.readUShort();
                Camera.cameraMinimumStep = incoming.readUnsignedByte();
                Camera.cameraInterpolationSpeed = incoming.readUnsignedByte();
                if (Camera.cameraInterpolationSpeed >= 100) {
                    cameraX = Camera.cameraTargetX * 128 + 64;
                    cameraZ = Camera.cameraTargetY * 128 + 64;
                    cameraY = get_tile_pos(plane, cameraZ, cameraX) - Camera.cameraAltitudeOffset;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_UNKOWN) {
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.followCameraMode = true;
                Camera.cameraTargetX = incoming.readUnsignedByte() * 16384;
                Camera.cameraTargetY = incoming.readUnsignedByte() * 16384;
                int heightOffset = incoming.readUnsignedShort();
                int targetValue = incoming.readUnsignedShort();
                Camera.fixedCamera = incoming.readBoolean();
                int interpolationParameter = incoming.readUnsignedByte();
                int targetX = Camera.cameraTargetX * 128 + 64;
                int targetY = Camera.cameraTargetY * 128 + 64;

                int cameraY;
                int height;
                if (Camera.fixedCamera) {
                    cameraY = this.cameraY;
                    height = get_tile_pos(plane, targetY, targetX) - heightOffset;
                } else {
                    cameraY = get_tile_pos(plane, cameraZ, cameraX) - this.cameraY;
                    height = heightOffset;
                }

                Camera.camera = new FollowCameraSimple(cameraX, cameraZ, cameraY, targetX, targetY, height, targetValue,
                        interpolationParameter);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_UNKOWN1) {
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.followCameraMode = true;
                Camera.cameraTargetX = incoming.readUnsignedByte() * 16384;
                Camera.cameraTargetY = incoming.readUnsignedByte() * 16384;
                int elevation = incoming.readUnsignedShort();
                int offsetX = incoming.readUnsignedByte() * 128 + 64;
                int offsetY = incoming.readUnsignedByte() * 128 + 64;
                int otherParameter = incoming.readUnsignedShort();
                Camera.fixedCamera = incoming.readBoolean();
                int additionalParameter = incoming.readUnsignedByte();
                int targetX = Camera.cameraTargetX * 128 + 64;
                int targetY = Camera.cameraTargetY * 128 + 64;
                int cameraHeight;
                int verticalDisplacement;
                if (Camera.fixedCamera) {
                    cameraHeight = cameraY;
                    verticalDisplacement = get_tile_pos(plane, targetY, targetX) - elevation;
                } else {
                    cameraHeight = get_tile_pos(plane, cameraZ, cameraX) - cameraY;
                    verticalDisplacement = elevation;
                }

                Camera.camera = new FollowCameraAdvanced(cameraX, cameraZ, cameraHeight, targetX, targetY,
                        verticalDisplacement, offsetX, offsetY, otherParameter, additionalParameter);

                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_UNKOWN3) {
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.followCameraMode = false;
                Camera.cameraTargetX = incoming.readUnsignedByte() * 16384;
                Camera.cameraTargetY = incoming.readUnsignedByte() * 16384;
                Camera.cameraAltitudeOffset = incoming.readUnsignedShort();
                Camera.cameraMinimumStep = incoming.readUnsignedByte();
                Camera.cameraInterpolationSpeed = incoming.readUnsignedByte();
                if (Camera.cameraInterpolationSpeed >= 100) {
                    cameraX = Camera.cameraTargetX * 128 + 64;
                    cameraZ = Camera.cameraTargetY * 128 + 64;
                    cameraY = get_tile_pos(plane, cameraZ, cameraX) - Camera.cameraAltitudeOffset;
                }

                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_UNKOWN4) {
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.staticCameraMode = true;
                Camera.staticCameraX = incoming.readUnsignedByte() * 128;
                Camera.staticCameraY = incoming.readUnsignedByte() * 128;
                Camera.staticCameraAltitudeOffset = incoming.readUnsignedShort();
                int var20 = incoming.readUnsignedShort();
                int var5 = incoming.readUnsignedByte();
                int var22 = Camera.staticCameraX * 16384 + 64;
                int var7 = Camera.staticCameraY * 16384 + 64;
                int var8 = get_tile_pos(plane, var7, var22) - Camera.staticCameraAltitudeOffset;
                int var24 = var22 - cameraX;
                int var10 = var8 - cameraY;
                int var60 = var7 - cameraZ;
                double var91 = Math.sqrt((double) (var60 * var60 + var24 * var24));
                int var69 = CameraUtils
                        .clampValue((int) (Math.atan2((double) var10, var91) * 325.9490051269531D) & 2047);
                int var15 = CameraUtils.adjustCameraYaw(
                        (int) (Math.atan2((double) var24, (double) var60) * -325.9490051269531D) & 2047);
                Camera.cameraPitch = new StaticCamera(cameraPitch, var69, var20, var5);
                Camera.cameraYaw = new StaticCamera(cameraYaw, var15, var20, var5);

                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CAMERA_UNKOWN5) {
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.staticCameraMode = true;
                int var20 = incoming.readShort();
                int var5 = incoming.readShort();
                int var22 = CameraUtils.clampValue(var5 + cameraPitch & 2027);
                int var7 = var20 + cameraYaw;
                int var8 = incoming.readUnsignedShort();
                int var24 = incoming.readUnsignedByte();
                Camera.cameraPitch = new StaticCamera(cameraPitch, var22, var8, var24);
                Camera.cameraYaw = new StaticCamera(cameraYaw, var7, var8, var24);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_SKILL) {
                int skill = incoming.readUnsignedByte();
                int xp = incoming.readMEInt();
                int level = incoming.readUnsignedByte();
                boostedSkillLevels[skill] = level;
                skillExperience[skill] = xp;
                realSkillLevels[skill] = 1;

                for (int skillId = 0; skillId < 98; ++skillId) {
                    if (xp >= SKILL_EXPERIENCE[skillId]) {
                        realSkillLevels[skill] = skillId + 2;
                    }
                }

                postSkillChangedEvent(skill);
                ExperienceChanged event = new ExperienceChanged();
                Skill[] skills = Skill.values();
                if (skill < skills.length - 1) {
                    Skill s = skills[skill];
                    event.setSkill(s);
                    getCallbacks().post(event);
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CHANGE_WIDGET_TOOLTIP_TEXT) {
                int widget_id = incoming.readShort();
                String text = incoming.readString();

                Widget.cache[widget_id].tooltip = text;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CHANGE_WIDGET_TEXT) {
                int widget_id = incoming.readShort();
                String text = incoming.readString();

                if (Widget.cache[widget_id].defaultText != null)
                    Widget.cache[widget_id].defaultText = text;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.HIT_PREDICTOR) {
                int hit = incoming.readUnsignedShort();
                if (hit > -1) {
                    expectedHit.add(new IncomingHit(hit));
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.STAMINA) {
                staminaActive = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_SIDE_TAB) {
                int id = incoming.readInt();
                int tab = incoming.readUByteA();
                // System.out.println("id "+id+" vs side tab "+tab);
                if (id == 65535)
                    id = -1;
                tabInterfaceIDs[tab] = id;
                update_tab_producer = true;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.PLAY_SONG) {
                int id = incoming.readShort();
                StaticSound.playSong(id);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.PLAY_JINGLE) {
                incoming.readUnsignedByte();
                int soundId = incoming.readUShort();

                StaticSound.playJingle(soundId, 0);

                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.PLAY_SOUND_EFFECT) {
                int soundId = incoming.readUShort();
                int rep = incoming.readUnsignedByte();
                int delay = incoming.readUShort();

                StaticSound.queueSoundEffect(soundId, rep, delay);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.LOGOUT) {
                logout();
                opcode = -1;
                return false;
            }

            if (opcode == ServerToClientPackets.MOVE_COMPONENT) {
                int x = incoming.readShort();
                int y = incoming.readShort();
                int id = incoming.readInt();
                // System.out.println("x " + x + " y " + y + " id " + id);
                Widget widget = Widget.cache[id];
                widget.x = x;
                widget.y = y;
                opcode = -1;
                return true;
            }

            if (opcode == UPDATE_RECENT_TELEPORT) {
                try {
                    boolean forFavorites = incoming.readSignedByte() == 1;
                    boolean remove = incoming.readSignedByte() == 1;
                    int spriteId = incoming.readShort();
                    String name = incoming.readString();
                    if (forFavorites) {
                        if (remove) {
                            TeleportWidget.removeFavoriteTeleport(spriteId);
                            opcode = -1;
                            return true;
                        }
                        if (spriteId == 5) {
                            TeleportWidget.createNewList(true);
                            opcode = -1;
                            return true;
                        }
                        TeleportWidget.getFavoriteTeleports()
                                .add(new TeleportInfo(spriteId, name));
                        opcode = -1;
                        return true;
                    } else {
                        if (remove) {
                            TeleportWidget.createNewList(false);
                            opcode = -1;
                            return true;
                        }
                        TeleportWidget.getRecentTeleports()
                                .add(new TeleportInfo(spriteId, name));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MAP_REGION
                    || opcode == ServerToClientPackets.SEND_REGION_MAP_REGION) {
                setGameState(GameState.LOADING);
                clearRegionalSpawns();
                int regionX = region_x;
                int regionY = region_y;
                if (opcode == ServerToClientPackets.SEND_MAP_REGION) {
                    regionX = incoming.readUShortA();
                    regionY = incoming.readUnsignedShort();
                } else if (opcode == ServerToClientPackets.SEND_REGION_MAP_REGION) {
                    regionY = incoming.readUShortA();
                    incoming.initBitAccess();
                    for (int z = 0; z < 4; z++) {
                        for (int x = 0; x < 13; x++) {
                            for (int y = 0; y < 13; y++) {
                                int visible = incoming.readBits(1);
                                if (visible == 1) {
                                    constructRegionData[z][x][y] = incoming.readBits(26);
                                } else {
                                    constructRegionData[z][x][y] = -1;
                                }
                            }
                        }
                    }
                    incoming.disableBitAccess();
                    regionX = incoming.readUnsignedShort();
                    isInInstance = true;
                }

                if (opcode != ServerToClientPackets.SEND_REGION_MAP_REGION && region_x == regionX
                        && region_y == regionY) { // rebuild region packet
                    opcode = -1;
                    return true;
                }

                region_x = regionX;
                region_y = regionY;
                next_region_start = (region_x - 6) * 8;
                next_region_end = (region_y - 6) * 8;
                inTutorialIsland = (region_x / 8 == 48 || region_x / 8 == 49) && region_y / 8 == 48;
                if (region_x / 8 == 48 && region_y / 8 == 148)
                    inTutorialIsland = true;

                loadingStartTime = System.currentTimeMillis();

                if (opcode == 73) { // construct non instance region packet
                    int regionCount = 0;
                    for (int x = (region_x - 6) / 8; x <= (region_x + 6) / 8; x++) {
                        for (int y = (region_y - 6) / 8; y <= (region_y + 6) / 8; y++)
                            regionCount++;
                    }
                    regionLandArchives = new byte[regionCount][];
                    regionMapArchives = new byte[regionCount][];
                    regions = new int[regionCount];
                    regionLandIds = new int[regionCount];
                    regionLocIds = new int[regionCount];
                    regionCount = 0;

                    for (int x = (region_x - 6) / 8; x <= (6 + region_x) / 8; ++x) {
                        for (int y = (region_y - 6) / 8; y <= (6 + region_y) / 8; ++y) {
                            int id = y + (x << 8);
                            regions[regionCount] = id;
                            regionLandIds[regionCount] = Js5List.maps.getGroupId("m" + x + "_" + y);
                            regionLocIds[regionCount] = Js5List.maps.getGroupId("l" + x + "_" + y);
                            ++regionCount;
                        }
                    }
                    // construct region on load
                }
                if (opcode == 241) {
                    int totalLegitChunks = 0;
                    int[] totalChunks = new int[676];
                    for (int z = 0; z < 4; z++) {
                        for (int x = 0; x < 13; x++) {
                            for (int y = 0; y < 13; y++) {
                                int tileBits = constructRegionData[z][x][y];
                                if (tileBits != -1) {
                                    int xCoord = tileBits >> 14 & 0x3ff;
                                    int yCoord = tileBits >> 3 & 0x7ff;
                                    int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
                                    for (int idx = 0; idx < totalLegitChunks; idx++) {
                                        if (totalChunks[idx] != mapRegion)
                                            continue;
                                        mapRegion = -1;

                                    }
                                    if (mapRegion != -1) {
                                        totalChunks[totalLegitChunks++] = mapRegion;
                                    }
                                }
                            }
                        }
                    }
                    regionLandArchives = new byte[totalLegitChunks][];
                    regionMapArchives = new byte[totalLegitChunks][];
                    regions = new int[totalLegitChunks];
                    regionLandIds = new int[totalLegitChunks];
                    regionLocIds = new int[totalLegitChunks];
                    for (int idx = 0; idx < totalLegitChunks; idx++) {
                        int region = regions[idx] = totalChunks[idx];
                        int constructedRegionX = region >> 8 & 0xff;
                        int constructedRegionY = region & 0xff;
                        regionLandIds[totalLegitChunks] = Js5List.maps
                                .getGroupId("m" + constructedRegionX + "_" + constructedRegionY);
                        regionLocIds[totalLegitChunks] = Js5List.maps
                                .getGroupId("l" + constructedRegionX + "_" + constructedRegionY);
                    }
                }
                int dx = next_region_start - previousAbsoluteX;
                int dy = next_region_end - previousAbsoluteY;
                previousAbsoluteX = next_region_start;
                previousAbsoluteY = next_region_end;
                for (int index = 0; index < maxNpcs; index++) {
                    Npc npc = npcs[index];
                    if (npc != null) {
                        for (int point = 0; point < 10; point++) {
                            npc.pathX[point] -= dx;
                            npc.pathY[point] -= dy;
                        }
                        npc.x -= dx * 128;
                        npc.y -= dy * 128;
                    }
                }
                for (int index = 0; index < maxPlayers; index++) {
                    Player player = players[index];
                    if (player != null) {
                        for (int point = 0; point < 10; point++) {
                            player.pathX[point] -= dx;
                            player.pathY[point] -= dy;
                        }
                        player.x -= dx * 128;
                        player.y -= dy * 128;
                    }
                }
                loadingMap = true;
                byte startX = 0;
                byte endX = 104;
                byte stepX = 1;
                if (dx < 0) {
                    startX = 103;
                    endX = -1;
                    stepX = -1;
                }
                byte startY = 0;
                byte endY = 104;
                byte stepY = 1;

                if (dy < 0) {
                    startY = 103;
                    endY = -1;
                    stepY = -1;
                }
                for (int x = startX; x != endX; x += stepX) {
                    for (int y = startY; y != endY; y += stepY) {
                        int shiftedX = x + dx;
                        int shiftedY = y + dy;
                        for (int plane = 0; plane < 4; plane++)
                            if (shiftedX >= 0 && shiftedY >= 0 && shiftedX < 104 && shiftedY < 104) {
                                scene_items[plane][x][y] = scene_items[plane][shiftedX][shiftedY];
                            } else {
                                scene_items[plane][x][y] = null;
                            }
                    }
                }
                for (SpawnedObject object = (SpawnedObject) spawns
                        .first(); object != null; object = (SpawnedObject) spawns.next()) {
                    object.x -= dx;
                    object.y -= dy;
                    if (object.x < 0 || object.y < 0 || object.x >= 104 || object.y >= 104)
                        object.remove();
                }
                if (travel_destination_x != 0) {
                    travel_destination_x -= dx;
                    travel_destination_y -= dy;
                }
                StaticSound.resetSoundCount();
                isCameraLocked = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_WALKABLE_INTERFACE) {
                int interfaceId = incoming.readInt();
                if (interfaceId >= 0)
                    resetAnimation(interfaceId);
                openWalkableInterface = interfaceId;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MINIMAP_STATE) {
                minimapState = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SHOW_NPC_HEAD_ON_INTERFACE) {
                int npcId = incoming.readLEUShortA();
                int interfaceId = incoming.readLEUShortA();
                Widget.cache[interfaceId].defaultMediaType = 2;
                Widget.cache[interfaceId].defaultMedia = npcId;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SYSTEM_UPDATE) {
                rebootTimer = incoming.readLEUShort() * 30;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MULTIPLE_MAP_PACKETS) {
                localY = incoming.readUnsignedByte();
                localX = incoming.readNegUByte();
                while (incoming.pos < packetSize) {
                    int k3 = incoming.readUnsignedByte();
                    parseRegionPackets(incoming, k3);
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_EARTHQUAKE) {
                int quakeDirection = incoming.readUnsignedByte();
                int quakeMagnitude = incoming.readUnsignedByte();
                int quakeAmplitude = incoming.readUnsignedByte();
                int fourPiOverPeriod = incoming.readUnsignedByte();
                quakeDirectionActive[quakeDirection] = true;
                this.quakeMagnitude[quakeDirection] = quakeMagnitude;
                this.quakeAmplitude[quakeDirection] = quakeAmplitude;
                this.fourPiOverPeriod[quakeDirection] = fourPiOverPeriod;
                cameraUpdateCounters[quakeDirection] = 0;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_AUTOCAST_ID) {
                int auto = incoming.readUnsignedShort();
                if (auto == -1) {
                    autocast = false;
                    autoCastId = 0;
                } else {
                    autocast = true;
                    autoCastId = auto;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.MYSTERY_BOX_SPIN) {
                spinSpeed = 1;
                Widget.cache[71101].x = 0;
                Widget.cache[71200].x = 0;
                startSpin = true;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.BLACK_FADING_SCREEN) {
                String text = incoming.readString();
                byte state = incoming.readSignedByte();
                byte seconds = incoming.readSignedByte();
                int drawingWidth = !isResized() ? 519 : Client.canvasWidth;
                int drawingHeight = !isResized() ? 338 : Client.canvasHeight;
                fadingScreen = new BlackFadingScreen(boldFont, text, state, seconds, 0, 0, drawingWidth, drawingHeight,
                        100);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_PLAYER_OPTION) {
                int slot = incoming.readNegUByte();
                int lowPriority = incoming.readUByteA();
                String message = incoming.readString();
                if (slot >= 1 && slot <= 5) {
                    if (message.equalsIgnoreCase("null"))
                        message = null;
                    playerOptions[slot - 1] = message;
                    playerOptionsHighPriority[slot - 1] = lowPriority == 0;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLEAR_MINIMAP_FLAG) {
                travel_destination_x = 0;
                opcode = -1;
                return true;
            }

            /**
             * Packet 79: Used to set scrollPosition and scrollMax on an interface.
             */
            if (opcode == ServerToClientPackets.SCROLL_POSITION) {
                int scrollChildId = incoming.readUnsignedShort();
                int scrollPosition = incoming.readUnsignedShort();
                int scrollMax = incoming.readUnsignedShort();
                Widget widget = Widget.cache[scrollChildId];
                if (widget != null && widget.type == 0) {
                    if (scrollPosition < 0)
                        scrollPosition = 0;
                    if (scrollPosition > widget.scrollMax - widget.height)
                        scrollPosition = widget.scrollMax - widget.height;
                    widget.scrollPosition = scrollPosition;
                }
                if (scrollMax == 0 || scrollMax < widget.height) {
                    widget.scrollMax = widget.height + 1;
                } else {
                    widget.scrollMax = scrollMax;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.ENABLE_NOCLIP) {
                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 1; x < 103; x++) {
                        for (int y = 1; y < 103; y++) {
                            collisionMaps[plane].adjacencies[x][y] = 0;
                        }
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_URL) {
                String url = incoming.readString();
                Utils.launchURL(url);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.RECEIVE_BROADCAST) {
                String message = incoming.readString();
                broadcastText = message;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_CLAN_CHAT_MESSAGE) {
                int type = incoming.readUnsignedByte();
                String name = incoming.readString();
                String message = incoming.readString();
                // System.out.println("clan chat msg: " + message);
                sendMessage(message, type, name);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MESSAGE) {
                String message = incoming.readString();

                if (message.startsWith("dismissbroadcast##")) {
                    if (broadcast != null) {
                        broadcast.dismiss();
                    }
                    opcode = -1;
                    return true;
                }
                if (message.startsWith("osrsbroadcast##")) {
                    String[] args = message.split("##");
                    String[] args1 = message.split("%%");
                    // System.out.println("args 1: " + Arrays.toString(args1));
                    // System.out.println("args 1 size: " + args1[1].length());
                    int linkIndex = args[1].indexOf(args1[1]);
                    String link = args[1].substring(linkIndex);
                    String text = args[1].substring(0, linkIndex - 2);
                    if (link.equalsIgnoreCase("no_link")) {
                        broadcast = new Announcement(text);
                    } else {
                        broadcast = new Announcement(text, link);
                    }
                    sendMessage(text, 20, "");
                    isDisplayed = true;
                    opcode = -1;
                    return true;
                }

                if (message.startsWith("depositbox")) {
                    String[] args = message.split(" ");
                    depositBoxOptionFirst = args[1];
                    opcode = -1;
                    return true;
                }

                if (message.endsWith(":spin:")) {
                    startSpin = true;
                    opcode = -1;
                    return true;
                }

                if (message.endsWith(":clearspin:")) {
                    startSpin = false;
                    opcode = -1;
                    return true;
                }

                if (message.startsWith("npcpetid")) {
                    String[] args = message.split(":");
                    Client.npcPetId = Integer.parseInt(args[1]);
                    opcode = -1;
                    return true;
                }

                if (message.startsWith("prioritizetarget")) {
                    String[] args = message.split(":");
                    Client.instance.setInteractingWithEntityId(Integer.parseInt(args[1]));
                    opcode = -1;
                    return true;
                }

                if (message.endsWith(":groupinvite:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignoreListAsLongs[count] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0) {
                        if (message.endsWith(":groupinvite:")) {
                            sendMessage("wishes to invite you to their ironman group.", 41, name);
                        }
                    }
                } else if (message.endsWith(":tradereq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int index = 0; index < ignoreCount; index++) {
                        if (ignoreListAsLongs[index] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0)
                        sendMessage("wishes to trade with you.", 4, name);
                } else if (message.endsWith("#url#")) {
                    String link = message.substring(0, message.indexOf("#"));
                    sendMessage("Join us at: ", 9, link);
                } else if (message.endsWith(":duelreqddswhiponly:") || message.endsWith(":duelreqwhiponly:")
                        || message.endsWith(":duelreqnormal:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignoreListAsLongs[count] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0) {
                        if (message.endsWith(":duelreqddswhiponly:")) {
                            sendMessage("wishes to whip and dds duel with you.", 8, name);
                        } else if (message.endsWith(":duelreqwhiponly:")) {
                            sendMessage("wishes to whip only duel with you.", 8, name);
                        } else {
                            sendMessage("wishes to duel with you.", 8, name);
                        }
                    }
                } else if (message.endsWith(":gamblereq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int count = 0; count < ignoreCount; count++) {
                        if (ignoreListAsLongs[count] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0) {
                        if (message.endsWith(":gamblereq:")) {
                            sendMessage("wishes to gamble with you.", 40, name);
                        }
                    }
                } else if (message.endsWith(":chalreq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long encodedName = StringUtils.encodeBase37(name);
                    boolean ignored = false;
                    for (int index = 0; index < ignoreCount; index++) {
                        if (ignoreListAsLongs[index] != encodedName)
                            continue;
                        ignored = true;

                    }
                    if (!ignored && onTutorialIsland == 0) {
                        String msg = message.substring(message.indexOf(":") + 1, message.length() - 9);
                        sendMessage(msg, 8, name);
                        // System.out.println("msg is: " + msg);
                    }
                } else {
                    // System.out.println("message is: " + message);
                    sendMessage(message, 0, "");
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.INTER_CLICKABLE) {
                int id = incoming.readInt();
                int clickable = incoming.readUnsignedByte();
                Widget widget = Widget.cache[id];
                if (widget != null) {
                    widget.clickable = clickable == 1;
                    // log.info("{} = {}", id, clickable);
                }
                opcode = -1;
                return true;
            }
            if (opcode == ServerToClientPackets.STOP_ALL_ANIMATIONS) {
                for (int index = 0; index < players.length; index++) {
                    if (players[index] != null)
                        players[index].sequence = -1;
                }
                for (int index = 0; index < npcs.length; index++) {
                    if (npcs[index] != null)
                        npcs[index].sequence = -1;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.ADD_FRIEND) {
                String friendName = incoming.readNewString();
                int world = incoming.readUnsignedByte();
                // add friend i guess is used for updating online/offline too.
                for (int playerIndex = 0; playerIndex < friendsCount; playerIndex++) {
                    String currentFriend = friendsList[playerIndex];
                    if (currentFriend == null)
                        continue;
                    if (!friendName.equalsIgnoreCase(currentFriend))
                        continue;
                    if (friendsNodeIDs[playerIndex] != world) {
                        friendsNodeIDs[playerIndex] = world;
                        if (world >= 2) {
                            sendMessage(friendName + " has logged in.", 5, "");
                        }
                        if (world <= 1) {
                            sendMessage(friendName + " has logged out.", 5, "");
                        }
                    }
                    friendName = null; // don't add the name to friends list, its already here
                    break;
                }
                if (friendName != null && friendsCount < 200) {
                    friendsListAsLongs[friendsCount] = StringUtils.encodeBase37(friendName);
                    friendsList[friendsCount] = friendName;
                    friendsNodeIDs[friendsCount] = world;
                    friendsCount++;
                }
                for (boolean stopSorting = false; !stopSorting;) {
                    stopSorting = true;
                    for (int friendIndex = 0; friendIndex < friendsCount - 1; friendIndex++)
                        if (friendsNodeIDs[friendIndex] != nodeID && friendsNodeIDs[friendIndex + 1] == nodeID
                                || friendsNodeIDs[friendIndex] == 0 && friendsNodeIDs[friendIndex + 1] != 0) {
                            int tempFriendNodeId = friendsNodeIDs[friendIndex];
                            friendsNodeIDs[friendIndex] = friendsNodeIDs[friendIndex + 1];
                            friendsNodeIDs[friendIndex + 1] = tempFriendNodeId;
                            String tempFriendName = friendsList[friendIndex];
                            friendsList[friendIndex] = friendsList[friendIndex + 1];
                            friendsList[friendIndex + 1] = tempFriendName;
                            long tempFriendLong = friendsListAsLongs[friendIndex];
                            friendsListAsLongs[friendIndex] = friendsListAsLongs[friendIndex + 1];
                            friendsListAsLongs[friendIndex + 1] = tempFriendLong;
                            stopSorting = false;
                        }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.REMOVE_FRIEND) {
                String nameHash = incoming.readString();

                for (int i = 0; i < friendsCount; i++) {
                    if (!friendsList[i].equalsIgnoreCase(nameHash)) {
                        continue;
                    }

                    friendsCount--;
                    for (int n = i; n < friendsCount; n++) {
                        friendsList[n] = friendsList[n + 1];
                        friendsNodeIDs[n] = friendsNodeIDs[n + 1];
                        friendsListAsLongs[n] = friendsListAsLongs[n + 1];
                    }
                    break;
                }

                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.ADD_IGNORE) {
                String encodedName = incoming.readString();
                boolean exists = false;
                // THIS IS DIFFERENT FROM 317 WHY THE FUCK? SOMEONE TRYING TO BE CLEVER AND
                // "OPTIMIZE"
                // DIDNT GO WELL - SHADOWRS jan 12 2021
                // old packet OVERWROTE ignores and this just ADDS - so remember to check if
                // theyre already here!!
                for (String ignoredName : ignoreList) {
                    if (ignoredName != null)
                        if (ignoredName.equalsIgnoreCase(encodedName)) { // already there bro
                            exists = true;
                            break;
                        }
                }
                if (!exists && ignoreCount < 200) {
                    ignoreList[ignoreCount] = encodedName;
                    ignoreCount++;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.REMOVE_IGNORE) {
                String nameHash = incoming.readString();
                int before = ignoreCount;
                for (int index = 0; index < ignoreCount; index++) {
                    if (ignoreList[index].equalsIgnoreCase(nameHash)) {
                        ignoreCount--;
                        System.arraycopy(ignoreListAsLongs, index + 1, ignoreListAsLongs, index, ignoreCount - index);
                        break;
                    }
                }
                if (ignoreCount == before) {
                    addReportToServer("unable to find " + nameHash);
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_TOGGLE_QUICK_PRAYERS) {
                prayClicked = incoming.readUnsignedByte() == 1;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_RUN_ENERGY) {
                runEnergy = incoming.readUnsignedByte();
                runEnergyString = "" + runEnergy;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_TOGGLE_RUN) {
                settings[ConfigUtility.RUN_ORB_ID] = settings[ConfigUtility.RUN_ID] = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_EXIT) {
                System.exit(1);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_HINT_ICON) {
                // the first byte, which indicates the type of mob
                hintIconDrawType = incoming.readUnsignedByte();
                if (hintIconDrawType == 1) // NPC Hint Arrow
                    // the world index or slot of the npc in the server (which is also the same for
                    // the client (should))
                    hintIconNpcId = incoming.readUnsignedShort();
                if (hintIconDrawType >= 2 && hintIconDrawType <= 6) { // Location Hint Arrow
                    if (hintIconDrawType == 2) { // Center
                        hintIconLocationArrowRelX = 64;
                        hintIconLocationArrowRelY = 64;
                    }
                    if (hintIconDrawType == 3) { // West side
                        hintIconLocationArrowRelX = 0;
                        hintIconLocationArrowRelY = 64;
                    }
                    if (hintIconDrawType == 4) { // East side
                        hintIconLocationArrowRelX = 128;
                        hintIconLocationArrowRelY = 64;
                    }
                    if (hintIconDrawType == 5) { // South side
                        hintIconLocationArrowRelX = 64;
                        hintIconLocationArrowRelY = 0;
                    }
                    if (hintIconDrawType == 6) { // North side
                        hintIconLocationArrowRelX = 64;
                        hintIconLocationArrowRelY = 128;
                    }
                    hintIconDrawType = 2;
                    // x offset
                    hintIconX = incoming.readUnsignedShort();

                    // y offset
                    hintIconY = incoming.readUnsignedShort();

                    // z offset
                    hintIconLocationArrowHeight = incoming.readUnsignedByte();
                }
                if (hintIconDrawType == 10) // Player Hint Arrow
                    hintIconPlayerId = incoming.readUnsignedShort();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_DUO_INTERFACE) {
                int mainInterfaceId = incoming.readInt();
                int sidebarOverlayInterfaceId = incoming.readInt();
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    update_chat_producer = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    update_chat_producer = true;
                }
                widget_overlay_id = mainInterfaceId;
                overlayInterfaceId = sidebarOverlayInterfaceId;
                update_tab_producer = true;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_SIDE_TAB_DISABLED_TABS) {
                int tabInterface = incoming.readUnsignedShort();
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    update_chat_producer = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    update_chat_producer = true;
                }
                overlayInterfaceId = tabInterface;
                update_tab_producer = true;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == 79) {
                int id = incoming.readLEUShort();
                int scrollPosition = incoming.readUShortA();
                Widget widget = Widget.cache[id];
                if (widget != null && widget.type == 0) {
                    if (scrollPosition < 0)
                        scrollPosition = 0;
                    if (scrollPosition > widget.scrollMax - widget.height)
                        scrollPosition = widget.scrollMax - widget.height;
                    widget.scrollPosition = scrollPosition;
                }
                opcode = -1;
                return true;
            }

            if (opcode == 68) {
                for (int k5 = 0; k5 < settings.length; k5++)
                    if (settings[k5] != anIntArray1045[k5]) {
                        settings[k5] = anIntArray1045[k5];
                        updateVarp(k5);
                    }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_RECEIVED_PRIVATE_MESSAGE) {
                String sender = incoming.readString();
                int messageId = incoming.readInt();
                int rights = incoming.readUnsignedByte();
                int memberRights = incoming.readUnsignedByte();
                int ironManRights = incoming.readUnsignedByte();
                boolean ignoreRequest = false;
                // System.out.printf("%s %s %s %s%n", sender, rights, memberRights,
                // ironManRights);

                if (rights <= 1) {
                    for (int index = 0; index < ignoreCount; index++) {
                        if (!ignoreList[index].equalsIgnoreCase(sender))
                            continue;
                        ignoreRequest = true;

                    }
                }
                if (!ignoreRequest && onTutorialIsland == 0)
                    try {
                        privateMessageIds[privateMessageCount] = messageId;
                        privateMessageCount = (privateMessageCount + 1) % 100;
                        String message = ChatMessageCodec.decode(packetSize - incoming.pos, incoming);
                        List<ChatCrown> crowns = ChatCrown.get(rights, memberRights, ironManRights);
                        sendMessage(message, 3, sender, "", crowns);
                        addToPrivateChatHistory(sender);
                    } catch (Exception ex) {
                        SignLink.reporterror("cde1");
                        ex.printStackTrace();
                        addReportToServer(ex.getMessage());
                    }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_REGION) {
                localY = incoming.readNegUByte();
                localX = incoming.readNegUByte();
                opcode = -1;
                return true;
            }

            if (opcode == 24) {
                flashingSidebarId = incoming.readUByteS();
                if (flashingSidebarId == sidebarId) {
                    if (flashingSidebarId == 3)
                        sidebarId = 1;
                    else
                        sidebarId = 3;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLIENT_SCRIPT) {
                final int instruction = incoming.readUnsignedShort();
                final char[] types = incoming.readString().toCharArray();
                final int[] intArgs = new int[incoming.readUnsignedShort()];
                final String[] stringArgs = new String[incoming.readUnsignedShort()];
                int intArgIndex = 0;
                int stringArgIndex = 0;
                for (char t : types) {
                    if (t == 's')
                        stringArgs[stringArgIndex++] = incoming.readString();
                    else
                        intArgs[intArgIndex++] = incoming.readInt();
                }

                InstructionArgs args = InstructionArgs.createFrom(intArgs, stringArgs);
                InstructionProcessor.invoke(instruction, args);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_ITEM_TO_INTERFACE) {
                int widget = incoming.readLEUShort();
                int scale = incoming.readUnsignedShort();
                int item = incoming.readUnsignedShort();
                if (item == 65535) {
                    Widget.cache[widget].defaultMediaType = 0;
                } else {
                    ItemDefinition definition = ItemDefinition.get(item);
                    var parent = Widget.cache[widget];
                    parent.defaultMediaType = 4;
                    parent.defaultMedia = item;
                    parent.modelAngleX = definition.xan2d;
                    parent.modelAngleY = definition.yan2d;
                    parent.height = definition.zan2d;
                    parent.inventoryMarginX = definition.xOffset2d;
                    parent.inventoryMarginY = definition.yOffset2d;
                    parent.modelZoom = definition.zoom2d * 32 / scale;
                    System.out.println("zoom: " + definition.zoom2d);
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CONFIRM) {
                int state = incoming.readUnsignedShort();
                int value = incoming.readUnsignedShort();
                handleConfirmationPacket(state, value);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MULTIPLE_WALKABLE_INTERFACE) {
                int interfaceId = incoming.readInt();
                int add = incoming.readUnsignedByte();
                Widget widget = Widget.cache[interfaceId];
                if (widget != null) {
                    if (add == 1) {
                        if (!parallelWidgetList.contains(widget)) {
                            parallelWidgetList.add(widget);
                        }
                    } else {
                        parallelWidgetList.remove(widget);
                        parallelWidgetList.removeIf(w -> w.id == interfaceId);
                    }
                }
                // log.info("whats left {}",
                // java.util.Arrays.toString(parallelWidgetList.stream().map(e ->
                // e.id+"").toArray()));
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_DEFENSIVE_AUTOCAST_STATE) {
                int state = incoming.readUnsignedShort();
                Widget.cache[24111].active = state != 0;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.UPDATE_TAB) {
                int value = incoming.readUnsignedShort();
                int id = incoming.readUnsignedShort();
                if (id == 0) {
                    spellbook = value;
                } else if (id == 1) {
                    questTabId = value;
                }
                opcode = -1;
                return true;
            }
            if (opcode == ServerToClientPackets.DARKEN_SCREEN) {
                int enabled = incoming.readUnsignedByte();
                darkenEnabled = enabled == 1;
                darkenOpacity = incoming.readInt();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SHOW_HIDE_INTERFACE_CONTAINER) {
                boolean hide = incoming.readUnsignedByte() == 1;
                int id = incoming.readInt();
                if (id < 1 || id >= Widget.cache.length) {
                    opcode = -1;
                    System.err.println("rejected id " + id);
                    return true;
                }
                Widget w = Widget.cache[id];

                if (w == null) {
                    opcode = -1;
                    System.err.println("error hiding component.. id doesn't exist to hide.");
                    return true;
                }

                w.invisible = hide;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.WIDGET_ACTIVE) {
                boolean active = incoming.readUnsignedByte() == 1;
                int id = incoming.readUnsignedShort();
                Widget.cache[id].active = active;
                // System.out.println("Widget active: "+active+" child: "+id);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_SOLO_NON_WALKABLE_SIDEBAR_INTERFACE) {
                int id = incoming.readLEUShort();
                resetAnimation(id);
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    update_chat_producer = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    update_chat_producer = true;
                }
                overlayInterfaceId = id;
                update_tab_producer = true;
                widget_overlay_id = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SPECIAL_ATTACK_OPCODE) {
                specialAttack = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MULTIPLE_STRINGS) {
                int amount = incoming.readUnsignedByte();

                for (int index = 0; index < amount; index++) {
                    String string = incoming.readString();
                    int id = incoming.readInt();

                    handleRunePouch(string, id);

                    if (string.startsWith("www.") || string.startsWith("http")) {
                        launchURL(string);
                        opcode = -1;
                        return true;
                    }
                    sendString(string, id);
                    if (id >= 33821 && id <= 33921) {
                        clanList[id - 33821] = Utils.replaceIcons(string);
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_INTERFACE_TEXT) {
                try {
                    String text = incoming.readString();
                    int id = incoming.readInt();
                    if (cheapHaxPacket(id, text)) {
                        opcode = -1;
                        return true;
                    }
                    handleRunePouch(text, id);
                    sendString(text, id);
                    if (id >= 33821 && id <= 33921) {
                        clanList[id - 33821] = Utils.replaceIcons(text);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer(e.getMessage());
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.UPDATE_CHAT_MODES) {
                set_public_channel = incoming.readUnsignedByte();
                privateChatMode = incoming.readUnsignedByte();
                tradeMode = incoming.readUnsignedByte();
                update_chat_producer = true;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_PLAYER_WEIGHT) {
                weight = incoming.readShort();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.TOURNY_LOBBY_TIMER) {
                lobbyTimer.start(incoming.readShort());
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MODEL_TO_INTERFACE) {
                int id = incoming.readLEUShortA();
                int model = incoming.readUnsignedShort();
                Widget.cache[id].defaultMediaType = 1;
                Widget.cache[id].defaultMedia = model;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_CHANGE_INTERFACE_COLOUR) {
                int intId = incoming.readShort();
                int color = incoming.readInt();
                if (Widget.cache[intId] != null) {
                    Widget.cache[intId].textColour = color;
                }
                opcode = -1;
                return true;
            }

            /*
             * Packet 53: Setting large amount of items on interfaces in order of receiving,
             * no index is being used to determine the position.
             */
            if (opcode == ServerToClientPackets.SEND_UPDATE_ITEMS) {
                int frameId = -1;
                try {
                    frameId = incoming.readInt();
                    int totalItems = incoming.readUnsignedShort();
                    Widget container = Widget.cache[frameId];
                    for (int index = 0; index < totalItems; index++) {
                        int itemAmount = incoming.readIMEInt();
                        int itemId = incoming.readLEUShortA();
                        container.inventoryItemId[index] = itemId;
                        container.inventoryAmounts[index] = itemAmount;
                    }

                    for (int index = totalItems; index < container.inventoryItemId.length; index++) {
                        container.inventoryItemId[index] = 0;
                        container.inventoryAmounts[index] = 0;
                    }

                    if (container.contentType == 206) {
                        for (int tab = 0; tab < 10; tab++) {
                            int itemAmount = incoming.readSignedByte() << 8 | incoming.readUnsignedShort();
                            tabAmounts[tab] = itemAmount;
                        }
                    }
                    // sendMessage("updated items "+Arrays.toString(container.inventoryItemId), 0,
                    // "");
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer("Container error caused by interface: " + frameId);
                    addReportToServer(e.getMessage());
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_BANKTABS) {
                for (int tab = 0; tab < 10; tab++) {
                    int itemAmount = incoming.readSignedByte() << 8 | incoming.readUnsignedShort();
                    tabAmounts[tab] = itemAmount;
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_EFFECT_TIMER) {
                try {

                    int timer = incoming.readShort();
                    int sprite = incoming.readShort();

                    addEffectTimer(new EffectTimer(timer, sprite));
                } catch (Exception e) {
                    e.printStackTrace();
                    addReportToServer(e.getMessage());
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.PROGRESS_BAR) {
                try {
                    int id = incoming.readInt();
                    Widget.cache[id].currentPercent = incoming.readUnsignedShort();

                    switch (id) {
                        case 75004:
                        case 75006:
                        case 75008:
                        case 75010:
                            if (Widget.cache[id].currentPercent >= 100) {
                                Widget.cache[id].barColour = 0x615f47;
                            } else {
                                Widget.cache[id].barColour = 0xFFFF00;
                            }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLEAR_CLICKED_TEXT) {
                clearTextClicked();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_CLICKED_TEXT) {
                boolean state = incoming.readUnsignedByte() == 1;
                int id = incoming.readInt();
                setTextClicked(id, state);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_MODEL_INTERFACE_ZOOM) {
                int scale = incoming.readUShortA();
                int id = incoming.readUnsignedShort();
                int pitch = incoming.readUnsignedShort();
                int roll = incoming.readLEUShortA();
                Widget.cache[id].modelAngleX = pitch;
                Widget.cache[id].modelAngleY = roll;
                Widget.cache[id].modelZoom = scale;
                opcode = -1;
                return true;
            }

            /**
             * Packet 223:
             *
             * Used to set OptionMenuInterfaces.
             */
            if (opcode == ServerToClientPackets.SET_OPTION_MENU) {
                final int childId = incoming.readInt();
                final int optionAmount = incoming.readUnsignedByte();
                final Collection<OptionMenu> optionMenus = new ArrayList<OptionMenu>();
                if (optionAmount > 0) {
                    for (int index = 0; index < optionAmount; index++) {
                        final int identifier = incoming.readUnsignedByte();
                        final String optionName = incoming.readString();
                        final String optionTooltip = incoming.readString();
                        optionMenus.add(new OptionMenu(identifier, optionName, optionTooltip));
                    }
                }
                final Widget optionInterface = Widget.cache[childId];
                if (optionInterface instanceof OptionMenuInterface) {
                    final OptionMenuInterface inter_1 = (OptionMenuInterface) optionInterface;
                    if (optionAmount > 0) {
                        inter_1.setOptionMenus(optionMenus);
                    } else {
                        inter_1.getOptionMenus().clear();
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SET_FRIENDSERVER_STATUS) {
                friendServerStatus = incoming.readUnsignedByte();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.MOVE_CAMERA) { // Gradually turn camera to spatial point.
                isCameraLocked = true;
                Camera.isCameraUpdating = false;
                Camera.staticCameraMode = false;
                Camera.staticCameraX = incoming.readUnsignedByte();
                Camera.staticCameraY = incoming.readUnsignedByte();
                Camera.staticCameraAltitudeOffset = incoming.readUShort();
                Camera.cameraPitchStep = incoming.readUnsignedByte();
                Camera.cameraYawSpeed = incoming.readUnsignedByte();
                if (Camera.cameraYawSpeed >= 100) {
                    int var20 = Camera.staticCameraX * 16384 + 64;
                    int var5 = Camera.staticCameraY * 16384 + 64;
                    int var22 = get_tile_pos(plane, var5, var20) - Camera.staticCameraAltitudeOffset;
                    int var7 = var20 - cameraX;
                    int var8 = var22 - cameraY;
                    int var24 = var5 - cameraZ;
                    int var10 = (int) Math.sqrt((double) (var24 * var24 + var7 * var7));
                    cameraPitch = (int) (Math.atan2((double) var8, (double) var10) * 325.9490051269531D) & 2047;
                    cameraYaw = (int) (Math.atan2((double) var7, (double) var24) * -325.9490051269531D) & 2047;
                    if (cameraPitch < 128) {
                        cameraPitch = 128;
                    }

                    if (cameraPitch > 383) {
                        cameraPitch = 383;
                    }
                    onCameraPitchChanged(0);
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_INITIALIZE_PACKET) {
                member = incoming.readUByteA();
                localPlayerIndex = incoming.readShort();
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.NPC_UPDATING) {
                updateNPCs(incoming, packetSize);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_ENTER_AMOUNT) {
                String title = incoming.readString();
                enter_amount_title = title;
                messagePromptRaised = false;
                inputDialogState = 1;
                amountOrNameInput = "";
                update_chat_producer = true;
                opcode = -1;
                int xxx = title.indexOf(":::");
                if (xxx != -1) {
                    String q = title.substring(0, xxx);
                    enter_amount_title = q;
                    amountOrNameInput = title.substring(xxx + 3);
                }
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_ENTER_NAME) { // Send Enter Name Dialogue (still allows numbers)
                String title = incoming.readString();
                enter_name_title = title;
                messagePromptRaised = false;
                inputDialogState = 2;
                amountOrNameInput = "";
                update_chat_producer = true;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_NON_WALKABLE_INTERFACE) {
                // ken comment, changed in server's PacketSender.java from short to int to allow
                // interfaces greater than 65000.
                // int interfaceId = incoming.readUShort();
                int interfaceId = incoming.readInt();
                resetAnimation(interfaceId);
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    update_tab_producer = true;
                }
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    update_chat_producer = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    update_chat_producer = true;
                }
                if (interfaceId == 16244) {
                    fullscreenInterfaceID = 16244;
                    widget_overlay_id = 16244;
                }
                if (interfaceId == 29050)
                    TeleportWidget.handleTeleportTab(0);
                widget_overlay_id = interfaceId;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_WALKABLE_CHATBOX_INTERFACE) {
                dialogueId = incoming.readLEShortA();
                update_chat_producer = true;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_CONFIG_INT) {
                int id = incoming.readLEUShort();
                int value = incoming.readMEInt();
                anIntArray1045[id] = value;
                if (settings[id] != value) {
                    settings[id] = value;
                    updateVarp(id);
                    if (dialogueId != -1)
                        update_chat_producer = true;
                }
                HealthHud.onVarpChange(id, value);
                VarbitManager.onVarpUpdate(id, value);
                VarbitChanged varbitChanged = new VarbitChanged();
                varbitChanged.setVarbitId(id);
                varbitChanged.setValue(value);
                getCallbacks().post(varbitChanged);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_CONFIG_BYTE) {
                int id = incoming.readLEUShort();
                byte value = incoming.readSignedByte();
                // System.out.println("p36 "+id+" "+value);
                if (id < anIntArray1045.length) {
                    anIntArray1045[id] = value;
                    if (settings[id] != value) {
                        settings[id] = value; // ok so this is set
                        Keybinding.onVarpUpdate(id, value);
                        VarbitManager.onVarpUpdate(id, value);
                        updateVarp(id);
                        if (dialogueId != -1)
                            update_chat_producer = true;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_MULTICOMBAT_ICON) {
                multicombat = incoming.readUnsignedByte(); // 1 is active
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_ANIMATE_INTERFACE) {
                int id = incoming.readUnsignedShort();
                int animation = incoming.readShort();
                Widget widget = Widget.cache[id];
                widget.defaultAnimationId = animation;
                widget.modelZoom = 796;// In OSRS the chat size is always 796!
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLOSE_INTERFACE) {
                if (overlayInterfaceId != -1) {
                    overlayInterfaceId = -1;
                    update_tab_producer = true;
                }
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    update_chat_producer = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    update_chat_producer = true;
                }
                if (searchingShops) {
                    Widget.cache[73155].active = false;
                    searchingShops = false;
                    update_chat_producer = true;
                    messagePromptRaised = false;
                    interfaceInputAction = 1;
                    inputMessage = "";
                    Widget rsi = this.getInputFieldFocusOwner();
                    if (rsi != null) {
                        rsi.defaultText = "";
                        packetSender.inputField(4 + rsi.defaultText.length() + 1, rsi.id, rsi.defaultText);
                        inputString = "";
                        promptInput = "";
                    }
                }
                if (this.isInputFieldInFocus()) {
                    this.resetInputFieldFocus();
                    inputString = "";
                }
                if (searchingBank) {
                    Widget.cache[26102].active = false;
                    searchingBank = false;
                    update_chat_producer = true;
                    inputDialogState = 0;
                    messagePromptRaised = false;
                    promptInput = "";
                    interfaceInputAction = 1;
                    inputMessage = "";
                }
                widget_overlay_id = -1;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.CLOSE_DIALOGUE_INTERFACE) {
                if (backDialogueId != -1) {
                    backDialogueId = -1;
                    update_chat_producer = true;
                }
                if (inputDialogState != 0) {
                    inputDialogState = 0;
                    update_chat_producer = true;
                }
                if (this.isInputFieldInFocus()) {
                    this.resetInputFieldFocus();
                    inputString = "";
                }

                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.UPDATE_SPECIFIC_ITEM) {
                int widgetId = incoming.readInt();
                Widget widget = Widget.cache[widgetId];
                while (incoming.pos < packetSize) {
                    int slot = incoming.readUSmart();
                    int itemId = incoming.readUnsignedShort();
                    int amount = incoming.readUnsignedByte();
                    if (amount == 255)
                        amount = incoming.readInt();
                    if (slot >= 0 && slot < widget.inventoryItemId.length) {
                        widget.inventoryItemId[slot] = itemId;
                        widget.inventoryAmounts[slot] = amount;
                    }
                }
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SEND_GFX || opcode == ServerToClientPackets.SEND_GROUND_ITEM
                    || opcode == ServerToClientPackets.SEND_ALTER_GROUND_ITEM_COUNT
                    || opcode == ServerToClientPackets.SEND_REMOVE_OBJECT || opcode == 105
                    || opcode == ServerToClientPackets.SEND_PROJECTILE
                    || opcode == ServerToClientPackets.TRANSFORM_PLAYER_TO_OBJECT
                    || opcode == ServerToClientPackets.SEND_OBJECT
                    || opcode == ServerToClientPackets.SEND_REMOVE_GROUND_ITEM
                    || opcode == ServerToClientPackets.ANIMATE_OBJECT || opcode == 215) {
                parseRegionPackets(incoming, opcode);
                opcode = -1;
                return true;
            }

            if (opcode == ServerToClientPackets.SWITCH_TAB) {
                sidebarId = incoming.readNegUByte();
                update_tab_producer = true;
                opcode = -1;
                return true;
            }
            if (opcode == ServerToClientPackets.SEND_CHATBOX_INTERFACE) {
                int id = incoming.readLEUShort();

                resetAnimation(id);
                backDialogueId = id;
                update_chat_producer = true;
                continuedDialogue = false;
                opcode = -1;
                return true;
            }

            SignLink.reporterror(
                    "T1 - " + opcode + "," + packetSize + " - " + secondLastOpcode + "," + thirdLastOpcode);
            logout();
        } catch (IOException _ex) {
            addReportToServer("There has been an exception reading packets, dropping client. dropClient()");
            try {
                addReportToServer("Dropping client, not a normal logout. 3");
                processNetworkError();
            } catch (Exception e) {
                addReportToServer("There was an error dropping the client: dropClient()");
                e.printStackTrace();
                addReportToServer(e.getMessage());
            }
            _ex.printStackTrace();
            addReportToServer(_ex.getMessage());
        } catch (Throwable exception) {
            StringBuilder s2 = new StringBuilder("T2 - " + opcode + "," + secondLastOpcode + "," + thirdLastOpcode
                    + " - " + packetSize + ","
                    + (next_region_start + local_player.pathX[0]) + "," + (next_region_end + local_player.pathY[0])
                    + " - ");
            for (int j15 = 0; j15 < packetSize && j15 < 50; j15++)
                s2.append(incoming.payload[j15]).append(",");
            SignLink.reporterror(s2.toString());
            exception.printStackTrace();
            addReportToServer(exception.getMessage());
            // logout();
        }
        opcode = -1;
        return true;
    }

    private void postSkillChangedEvent(int skill) {
        var values = Skill.values()[skill];
        StatChanged statChanged = new StatChanged(values, this.getSkillExperience(values),
                this.getRealSkillLevel(values), this.getBoostedSkillLevel(values));
        getCallbacks().post(statChanged);
    }

    static final Deque<String> reports = new java.util.LinkedList<>();

    public static void addReportToServer(String s) {
        if (reports != null) {
            // append newest to end
            reports.addLast(s);
        }
    }

    static int method1389(int var0) {
        return var0 * 3 + 600;
    }

    public static int field625 = 256;
    static int field626 = 205;
    static int field440 = 1;
    static int field630 = 32767;
    static short field488 = 1;
    static short field562 = 32767;

    static void setViewportShape(int var0, int var1, int var2, int var3, boolean var4) {
        if (var2 < 1) {
            var2 = 1;
        }

        if (var3 < 1) {
            var3 = 1;
        }

        int var5 = var3 - 334;
        int var6;
        if (var5 < 0) {
            var6 = Client.field625;
        } else if (var5 >= 100) {
            var6 = Client.field626;
        } else {
            var6 = (Client.field626 - Client.field625) * var5 / 100 + Client.field625;
        }

        int var7 = var3 * var6 * 512 / (var2 * 334);
        int var8;
        int var9;
        short var17;
        if (var7 < Client.field488) {
            var17 = Client.field488;
            var6 = var17 * var2 * 334 / (var3 * 512);
            if (var6 > Client.field630) {
                var6 = Client.field630;
                var8 = var3 * var6 * 512 / (var17 * 334);
                var9 = (var2 - var8) / 2;
                if (var4) {
                    Rasterizer2D.resetClip();
                    Rasterizer2D.drawItemBox(var0, var1, var9, var3, -16777216);
                    Rasterizer2D.drawItemBox(var0 + var2 - var9, var1, var9, var3, -16777216);
                }

                var0 += var9;
                var2 -= var9 * 2;
            }
        } else if (var7 > Client.field562) {
            var17 = Client.field562;
            var6 = var17 * var2 * 334 / (var3 * 512);
            if (var6 < Client.field440) {
                var6 = Client.field440;
                var8 = var17 * var2 * 334 / (var6 * 512);
                var9 = (var3 - var8) / 2;
                if (var4) {
                    Rasterizer2D.resetClip();
                    Rasterizer2D.drawItemBox(var0, var1, var2, var9, -16777216);
                    Rasterizer2D.drawItemBox(var0, var3 + var1 - var9, var2, var9, -16777216);
                }

                var1 += var9;
                var3 -= var9 * 2;
            }
        }

        viewportZoom = var3 * var6 / 334;
        if (var2 != Client.instance.getViewportWidth() || var3 != Client.instance.getViewportHeight()) {
            int[] var16 = new int[9];

            for (var9 = 0; var9 < var16.length; ++var9) {
                int var10 = var9 * 32 + 15 + 128;
                int var11 = method1389(var10);
                int var12 = Rasterizer3D.SINE[var10];
                int var14 = var3 - 334;
                if (var14 < 0) {
                    var14 = 0;
                } else if (var14 > 100) {
                    var14 = 100;
                }

                int var15 = (Client.zoomWidth - Client.zoomHeight) * var14 / 100 + Client.zoomHeight;
                int var13 = var11 * var15 / 256;
                var16[var9] = var13 * var12 >> 16;
            }

            SceneGraph.buildVisiblityMap(var16, 500, 800, var2 * 334 / var3, 334);
        }

        Client.viewportOffsetX = var0;
        Client.viewportOffsetY = var1;
        Client.viewportWidth = var2;
        Client.viewportHeight = var3;
    }

    private void showPrioritizedPlayers() {
        showPlayer(local_player, LOCAL_PLAYER_INDEX, true);

        // Draw the player we're interacting with
        // Interacting includes combat, following, etc.
        int interact = local_player.targetIndex - 32768;
        if (interact > 0) {
            Player player = players[interact];
            showPlayer(player, interact, false);
        }
    }

    private void showOtherPlayers() {
        for (int l = 0; l < players_in_region; l++) {
            Player player = players[local_players[l]];
            int index = local_players[l];

            // Don't draw interacting player as we've already drawn it on top
            int interact_index = (local_player.targetIndex - 32768);
            if (interact_index > 0 && index == interact_index) {
                continue;
            }

            if (!showPlayer(player, index, false)) {
            }
        }
    }

    private boolean showPlayer(Player player, int i1, boolean flag) {
        if (player == null || !player.visible()) {
            return false;
        }

        player.isUnanimated = (low_detail && players_in_region > 50 || players_in_region > 200) && !flag
                && player.movementSequence == player.idleSequence;
        int j1 = player.x >> 7;
        int k1 = player.y >> 7;
        if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) {
            return false;
        }
        long var4 = ObjectKeyUtil.calculateTag(0, 0, 0, false, i1);
        if (player.playerModel != null && tick >= player.animationCycleStart && tick < player.animationCycleEnd) {
            player.isUnanimated = false;
            player.last_update_tick = tick;
            player.tileHeight = get_tile_pos(plane, player.y, player.x);
            scene.add_transformed_entity(plane, player.y, player, player.current_rotation, player.maxY, player.x,
                    player.tileHeight, player.minX, player.maxX, i1, player.minY);
            return false;
        }
        if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
            if (tile_cycle_map[j1][k1] == viewportDrawCount) {
                return false;
            }
            tile_cycle_map[j1][k1] = viewportDrawCount;
        }
        player.last_update_tick = tick;
        player.tileHeight = get_tile_pos(plane, player.y, player.x);
        scene.add_entity(plane, player.current_rotation, player.tileHeight, var4, player.y, 60, player.x, player,
                player.isWalking);
        return true;
    }

    private void showPrioritizedNPCs() {
        addNpcsToScene(true);
    }

    private boolean prioritizedNpc(Npc npc) {

        // Check if it's being interacted with
        if (local_player.targetIndex != -1 &&
                local_player.targetIndex < 32768) {
            if (npc.index == local_player.targetIndex) {
                return true;
            }
        }

        return npc.definition != null && npc.definition.hasRenderPriority;

    }

    private void showOtherNpcs() {
        addNpcsToScene(false);
    }

    public final void addNpcsToScene(boolean arg0) {
        for (int idx = 0; idx < npcs_in_region; ++idx) {
            Npc var3 = npcs[local_npcs[idx]];
            var3.setIndex(idx);
            if (null != var3 && var3.visible() && var3.definition.isVisible() == arg0
                    && var3.definition.transformIsVisible()) {
                int var4 = var3.x >> 7;
                int var5 = var3.y >> 7;
                if (var4 >= 0 && var4 < 104 && var5 >= 0 && var5 < 104) {
                    if (1 == var3.definition.size && 64 == (var3.x & 127) && 64 == (var3.y & 127)) {
                        if (viewportDrawCount == tile_cycle_map[var4][var5]) {
                            continue;
                        }

                        tile_cycle_map[var4][var5] = viewportDrawCount;
                    }

                    long var6 = ObjectKeyUtil.calculateTag(0, 0, 1, !var3.definition.isInteractable, local_npcs[idx]);
                    var3.last_update_tick = tick;
                    scene.add_entity(plane, var3.current_rotation,
                            get_tile_pos(plane, var3.size * 64 - 64 + var3.y, var3.x + (var3.size * 64 - 64)), var6,
                            var3.y, 60 + (var3.size * 64 - 64), var3.x, var3, var3.isWalking);
                }
            }
        }
    }

    private void drawEntities(int viewportOffsetX, int viewportOffsetY, int width, int height) {
        viewportDrawCount++;
        if (local_player.x >> 7 == travel_destination_x && local_player.y >> 7 == travel_destination_y) {
            travel_destination_x = 0;
        }

        showPrioritizedPlayers();
        showOtherPlayers();

        showPrioritizedNPCs();
        showOtherNpcs();

        render_projectiles();
        render_stationary_graphics();

        int var37 = camAngleX;
        if (Camera.cameraAltitudeAdjustment / 256 > var37) {
            var37 = Camera.cameraAltitudeAdjustment / 256;
        }

        if (quakeDirectionActive[4] && quakeAmplitude[4] + 128 > var37) {
            var37 = quakeAmplitude[4] + 128;
        }

        int var5 = camAngleY & 2047;
        int var6 = oculusOrbFocalPointX;
        int var7 = Camera.oculusOrbAltitude;
        int var8 = oculusOrbFocalPointY;
        int var9 = var37 * 3 + 600;
        int var12 = viewportHeight - 334;
        if (var12 < 0) {
            var12 = 0;
        } else if (var12 > 100) {
            var12 = 100;
        }

        int var13 = (Client.zoomWidth - Client.zoomHeight) * var12 / 100 + Client.zoomHeight;
        int var11 = var13 * var9 / 256;
        var12 = 2048 - var37 & 2047;
        var13 = 2048 - var5 & 2047;
        int var14 = 0;
        int var15 = 0;
        int var16 = var11;
        int var17;
        int var18;
        int var19;
        if (var12 != 0) {
            var17 = Rasterizer3D.SINE[var12];
            var18 = Rasterizer3D.COSINE[var12];
            var19 = var18 * var15 - var17 * var11 >> 16;
            var16 = var18 * var11 + var15 * var17 >> 16;
            var15 = var19;
        }

        if (var13 != 0) {
            var17 = Rasterizer3D.SINE[var13];
            var18 = Rasterizer3D.COSINE[var13];
            var19 = var17 * var16 + var14 * var18 >> 16;
            var16 = var18 * var16 - var14 * var17 >> 16;
            var14 = var19;
        }

        int plane;
        if (isCameraLocked) {
            Camera.targetCameraX = var6 - var14;
            Camera.targetCameraY = var7 - var15;
            Camera.targetCameraZ = var8 - var16;
            Camera.targetCameraPitch = var37;
            Camera.targetCameraYaw = var5;
        } else {
            cameraX = var6 - var14;
            cameraY = var7 - var15;
            cameraZ = var8 - var16;
            cameraPitch = var37;
            cameraYaw = var5;
            onCameraPitchChanged(0);
        }

        if (!isCameraLocked) {
            plane = Camera.determineRoofHeight();
        } else {
            plane = Camera.calculateCameraPlane();
        }

        var12 = cameraX;
        var13 = cameraY;
        var14 = cameraZ;
        var15 = cameraPitch;
        var16 = cameraYaw;

        for (var17 = 0; var17 < 5; ++var17) {
            if (quakeDirectionActive[var17]) {
                var18 = (int) (Math.random() * (double) (quakeMagnitude[var17] * 2 + 1) - (double) quakeMagnitude[var17]
                        + Math.sin((double) cameraUpdateCounters[var17] * ((double) fourPiOverPeriod[var17] / 100.0D))
                                * (double) quakeAmplitude[var17]);
                if (var17 == 0) {
                    cameraX += var18;
                }

                if (var17 == 1) {
                    cameraY += var18;
                }

                if (var17 == 2) {
                    cameraZ += var18;
                }

                if (var17 == 3) {
                    cameraYaw = var18 + cameraYaw & 2047;
                }

                if (var17 == 4) {
                    cameraPitch += var18;
                    if (cameraPitch < 128) {
                        cameraPitch = 128;
                        onCameraPitchChanged(0);
                    }

                    if (cameraPitch > 383) {
                        cameraPitch = 383;
                        onCameraPitchChanged(0);
                    }
                }
            }
        }

        int viewportXOffset = getViewportXOffset();
        int viewportYOffset = getViewportYOffset();

        setViewportShape(viewportOffsetX, viewportOffsetY, width, height, true);

        Rasterizer2D.setClip(viewportXOffset, viewportYOffset, viewportXOffset + getViewportWidth(),
                getViewportHeight() + viewportYOffset);
        Rasterizer3D.setupRasterizerClip();
        Rasterizer2D.resetDepth();

        int mouseX = MouseHandler.mouseX;
        int mouseY = MouseHandler.mouseY;
        if (MouseHandler.lastButton != 0) {
            mouseX = MouseHandler.saveClickX;
            mouseY = MouseHandler.saveClickY;
        }

        if (mouseX >= viewportXOffset && mouseX < viewportXOffset + getViewportWidth() && mouseY >= viewportYOffset
                && mouseY < getViewportHeight() + viewportYOffset) {
            int mouseXLoc = mouseX - viewportXOffset;
            int mouseYLoc = mouseY - viewportYOffset;
            ViewportMouse.setClick(mouseXLoc, mouseYLoc);
        } else {
            ViewportMouse.method3167();
        }
        StaticSound.playPcmPlayers();
        Rasterizer2D.Rasterizer2D_clear();

        callbacks.post(BeforeRender.INSTANCE);
        Rasterizer3D.toggleZBuffering(ClientConstants.ZBUFF);
        Rasterizer3D.clips.viewportZoom = Client.viewportZoom;
        scene.render(cameraX, cameraZ, cameraYaw, cameraY, plane, cameraPitch);
        Rasterizer3D.toggleZBuffering(false);
        Rasterizer3D.clips.viewportZoom = Clips.get3dZoom();
        StaticSound.playPcmPlayers();
        scene.renderTileMarkers();
        rasterProvider.init();

        scene.reset_interactive_obj();
        updateEntities();
        drawHeadIcon();
        ((TextureProvider) Rasterizer3D.clips.textureLoader).animate(animation_step);
        draw3dScreen();

        if (!isResized()) {
            leftFrame.drawSprite(0, 4);
            topFrame.drawSprite(0, 0);
        }

        drawChatArea();
        drawMinimap();
        drawTabArea();
        viewportInterfaceCallback();

        if (isMenuOpen) {
            BeforeMenuRender event = new BeforeMenuRender();
            getCallbacks().post(event);
            if (!event.isConsumed()) {
                drawOriginalMenu(255);
            }
        }

        cameraX = var12;
        cameraY = var13;
        cameraZ = var14;
        cameraPitch = var15;
        cameraYaw = var16;

        if (Client.isLoading && jagexNetThread.method1968(true, false) == 0) {
            Client.isLoading = false;
        }

        if (Client.isLoading) {
            Rasterizer2D.fillRectangle(0, 0, canvasWidth, canvasHeight, 0);
            drawLoadingMessage("Loading - please wait.");
        }

    }

    public static boolean isLoading = true;

    public SimpleImage leftFrame;
    public SimpleImage topFrame;

    private void viewportInterfaceCallback() {
        if (!isResized()) {
            callbacks.drawInterface(WidgetID.FIXED_VIEWPORT_GROUP_ID, Collections.emptyList());
        } else {
            callbacks.drawInterface(WidgetID.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX_GROUP_ID, Collections.emptyList());
        }
    }

    private int privateChatUserListPtr;
    private String[] recentIncomingPrivateChatUserList;

    private void tabToReplyPm() {
        String user = recentIncomingPrivateChatUserList[privateChatUserListPtr];
        if (user == null) {
            sendMessage("You haven't received any messages to which you can reply.", 0, "");
            return;
        }

        openPrivateChatMessageInput(user);
        ++privateChatUserListPtr;
        if (privateChatUserListPtr == 10)
            privateChatUserListPtr = 0;
        boolean next = recentIncomingPrivateChatUserList[privateChatUserListPtr
                % recentIncomingPrivateChatUserList.length] != null;
        if (!next)
            privateChatUserListPtr = 0;
    }

    private void addToPrivateChatHistory(String user) {
        for (int i = recentIncomingPrivateChatUserList.length - 1; i > 0; i--) {
            boolean remove = recentIncomingPrivateChatUserList[i - 1] == user && i != 1;
            if (remove)
                continue;

            recentIncomingPrivateChatUserList[i] = recentIncomingPrivateChatUserList[i - 1];
        }

        recentIncomingPrivateChatUserList[0] = user;
    }

    private void openPrivateChatMessageInput(String user) {
        update_chat_producer = true;
        inputDialogState = 0;
        messagePromptRaised = true;
        promptInput = "";
        interfaceInputAction = 3;
        // aLong953 = userHash;
        this.selectedSocialListName = user;
        inputMessage = "Enter message to send to " + selectedSocialListName;
    }

    private void processMinimapActions() {
        if (widget_overlay_id == 16244) {
            return;
        }
        final boolean fixed = !resized;
        if (fixed
                ? MouseHandler.mouseX >= 542 && MouseHandler.mouseX <= 579 && MouseHandler.mouseY >= 2
                        && MouseHandler.mouseY <= 38
                : MouseHandler.mouseX >= canvasWidth - 180 && MouseHandler.mouseX <= canvasWidth - 139
                        && MouseHandler.mouseY >= 0
                        && MouseHandler.mouseY <= 40) {
            menuActionText[1] = "Face North";
            menuActionTypes[1] = 696;
            menuActionRow = 2;
        }
        if (resized && settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 1) {
            if (MouseHandler.mouseX >= canvasWidth - 26 && MouseHandler.mouseX <= canvasWidth - 1
                    && MouseHandler.mouseY >= 2
                    && MouseHandler.mouseY <= 24) {
                menuActionText[1] = "Logout";
                menuActionTypes[1] = 700;
                menuActionRow = 2;
            }
        }

        if (settings[ConfigUtility.DATA_ORBS_ID] == 1) {
            if (expCounterHover) {
                menuActionText[3] = counterOn ? "Hide @lre@XP Drops" : "Display @lre@XP Drops";
                menuActionTypes[3] = 474;
                menuActionText[2] = "Setup @lre@XP Drops";
                menuActionTypes[2] = 476;
                menuActionText[1] = "Reset @lre@XP Drops";
                menuActionTypes[1] = 475;
                menuActionRow = 4;
            }
            if (prayHover) {
                menuActionText[2] = prayClicked ? "Turn Quick Prayers off" : "Turn Quick Prayers on";
                menuActionTypes[2] = 1500;
                menuActionRow = 2;
                menuActionText[1] = "Setup Quick Prayers";
                menuActionTypes[1] = 1506;
                menuActionRow = 3;
            }
            if (runHover) {
                menuActionText[1] = "Toggle Run";
                menuActionTypes[1] = 1050;
                menuActionRow = 2;
            }
        }
    }

    /**
     * Gets the progress color for the xp bar
     *
     * @param percent
     * @return
     */
    public static int getProgressColor(int percent) {
        if (percent <= 15) {
            return 0x808080;
        }
        if (percent <= 45) {
            return 0x7f7f00;
        }
        if (percent <= 65) {
            return 0x999900;
        }
        if (percent <= 75) {
            return 0xb2b200;
        }
        if (percent <= 90) {
            return 0x007f00;
        }
        return 31744;
    }

    public static int getXPForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    private boolean runHover;
    private boolean prayHover;
    private boolean prayClicked;
    private boolean expCounterHover;
    private boolean bankHover;
    private boolean healHover;
    private boolean potionsHover;

    public int getOrbTextColor(int statusInt) {
        if (statusInt >= 75 && statusInt <= Integer.MAX_VALUE)
            return 0x00FF00;
        else if (statusInt >= 50 && statusInt <= 74)
            return 0xFFFF00;
        else if (statusInt >= 25 && statusInt <= 49)
            return 0xFF981F;
        else
            return 0xFF0000;
    }

    public void clearTopInterfaces() {
        // close interface
        packetSender.sendInterfaceClear();
        if (overlayInterfaceId != -1) {
            overlayInterfaceId = -1;
            continuedDialogue = false;
            update_tab_producer = true;
        }
        if (backDialogueId != -1) {
            backDialogueId = -1;
            update_chat_producer = true;
            continuedDialogue = false;
        }
        widget_overlay_id = -1;
        fullscreenInterfaceID = -1;
    }

    public void addObject(int x, int y, int objectId, int face, int type, int height) {
        int mX = region_x - 6;
        int mY = region_y - 6;
        int x2 = x - mX * 8;
        int y2 = y - mY * 8;
        int i15 = 40 >> 2;
        int l17 = objectGroups[i15];
        if (y2 > 0 && y2 < 103 && x2 > 0 && x2 < 103) {
            requestSpawnObject(-1, objectId, face, l17, y2, type, height, x2, 0);

        }
    }

    @SuppressWarnings("unused")
    private int currentTrackPlaying;

    public Settings setting;

    public Client() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            addReportToServer(t.toString());
            addReportToServer(e.getMessage());
        });
        addReportToServer("client init");
        ArchiveLoader.archiveLoaders = new ArrayList<>(10);
        ArchiveLoader.archiveLoadersDone = 0;
        expectedHit = new ArrayList<>();
        setting = new Settings();
        oculusOrbSlowedSpeed = 40;
        oculusOrbNormalSpeed = 20;
        chatMessages = new ChatMessage[500];
        SERVER_CONNECTION = new ServerConnection();
        splitPrivateChatMessages = new ArrayList<>(Collections.nCopies(500, null));
        packetSender = new PacketSender(null);
        chatBuffer = new Buffer(5000);
        secure_future = new SecureRandomFuture();
        // Buffer size 40k is supposedly what OSRS uses.
        incoming = new Buffer(new byte[40_000]);
        fullscreenInterfaceID = -1;
        chatRights = new int[500];
        chatTypeView = 0;
        keyManager = new KeyEventProcessorImpl();
        clanChatMode = 0;
        cButtonHPos = -1;
        mapsLoaded = 0;
        totalMaps = 1;
        objectsLoaded = 0;
        totalObjects = 1;
        loadingType = 0;
        currentTrackPlaying = -1;
        cButtonCPos = 0;
        // serverAddress = ClientConstants.SERVER_ADDRESS;
        travel_distances = new int[104][104];
        friendsNodeIDs = new int[200];
        scene_items = new NodeDeque[4][104][104];
        update_flame_components = false;
        npcs = new Npc[65536];
        local_npcs = new int[65536];
        removedMobs = new int[1000];
        soundEnabled = true;
        widget_overlay_id = -1;
        skillExperience = new int[SkillConstants.SKILL_COUNT];
        quakeMagnitude = new int[5];
        quakeDirectionActive = new boolean[5];
        drawFlames = false;
        reportAbuseInput = "";
        localPlayerIndex = -1;
        isMenuOpen = false;
        inputString = "";
        maxNpcs = 65536;
        maxPlayers = 2_048;
        LOCAL_PLAYER_INDEX = 2_047;
        players = new Player[maxPlayers];
        local_players = new int[maxPlayers];
        mobsAwaitingUpdate = new int[maxPlayers];
        playerSynchronizationBuffers = new Buffer[maxPlayers];
        anInt897 = 1;
        waypoints = new int[104][104];
        boostedSkillLevels = new int[SkillConstants.SKILL_COUNT];
        ignoreListAsLongs = new long[100];
        loadingError = false;
        fourPiOverPeriod = new int[5];
        tile_cycle_map = new int[104][104];
        sideIcons = new SimpleImage[15];
        hadFocus = true;
        friendsListAsLongs = new long[200];

        drawingFlames = false;
        scene_draw_x = -1;
        scene_draw_y = -1;
        anIntArray968 = new int[33];
        anIntArray969 = new int[256];
        camFollowHeight = 50;
        settings = new int[300_000];
        aBoolean972 = false;
        spokenMaxCount = 50;
        scene_text_x = new int[spokenMaxCount];
        scene_text_y = new int[spokenMaxCount];
        scene_text_height = new int[spokenMaxCount];
        scene_text_center_x = new int[spokenMaxCount];
        scene_text_color = new int[spokenMaxCount];
        scene_text_effect = new int[spokenMaxCount];
        scene_text_update_cycle = new int[spokenMaxCount];
        spokenMessage = new String[spokenMaxCount];
        lastKnownPlane = -1;
        hitMarks = new SimpleImage[37];
        characterDesignColours = new int[5];
        aBoolean994 = false;
        amountOrNameInput = "";
        projectiles = new NodeDeque();
        openWalkableInterface = -1;
        cameraUpdateCounters = new int[5];
        updateCharacterCreation = false;
        dialogueId = -1;
        realSkillLevels = new int[SkillConstants.SKILL_COUNT];
        anIntArray1045 = new int[4_000];
        characterGender = true;
        minimapLeft = new int[152];
        minimapLineWidth = new int[152];
        flashingSidebarId = -1;
        incompleteAnimables = new NodeDeque();
        anIntArray1057 = new int[33];
        aClass9_1059 = new Widget();
        barFillColor = 0x4d4233;
        characterClothing = new int[7];
        minimapHintX = new int[1_000];
        minimapHintY = new int[1_000];
        loadingMap = false;
        friendsList = new String[200];
        ignoreList = new String[100];
        firstMenuAction = new int[500];
        secondMenuAction = new int[500];
        menuActionTypes = new int[500];
        selectedMenuActions = new long[500];
        headIcons = new SimpleImage[20];
        skullIcons = new SimpleImage[20];
        headIconsHint = new SimpleImage[20];
        autoBackgroundSprites = new SimpleImage[20];
        update_tab_producer = false;
        inputMessage = "";
        playerOptions = new String[5];
        playerOptionsHighPriority = new boolean[5];
        constructRegionData = new int[4][13][13];
        anInt1132 = 2;
        minimapHint = new SimpleImage[1_000];
        inTutorialIsland = false;
        continuedDialogue = false;
        crosses = new SimpleImage[8];
        loggedIn = false;
        setGameState(GameState.STARTING);
        canMute = false;
        isInInstance = false;
        isCameraLocked = false;
        anInt1171 = 1;
        myUsername = "";
        myPassword = "";
        genericLoadingError = false;
        reportAbuseInterfaceID = -1;
        spawns = new NodeDeque();
        camAngleX = 128;
        overlayInterfaceId = -1;
        menuActionText = new String[500];
        quakeAmplitude = new int[5];
        tracks = new int[50];
        anInt1210 = 2;
        chatScrollHeight = 78;
        promptInput = "";
        sidebarId = 3;
        update_chat_producer = false;
        collisionMaps = new CollisionMap[4];
        privateMessageIds = new int[100];
        rsAlreadyLoaded = false;
        update_producers = false;
        messagePromptRaised = false;
        firstLoginMessage = "";
        secondLoginMessage = "";
        backDialogueId = -1;
        anInt1279 = 2;
        walking_queue_x = new int[4_000];
        walking_queue_y = new int[4_000];
    }

    @Override
    protected void resizeGame() {

    }

    public int rights;
    public String name;
    public String defaultText;
    public String clanname;
    private final int[] chatRights;
    public int chatTypeView;
    public int clanChatMode;
    private boolean autocast;
    public int autoCastId = 0;

    private int ignoreCount;
    private long loadingStartTime;
    private int[][] travel_distances;
    private int[] friendsNodeIDs;
    private NodeDeque[][][] scene_items;
    private int[] anIntArray828;
    private int[] anIntArray829;
    private volatile boolean update_flame_components;
    private int loginStage = 0;
    private Npc[] npcs;
    private int npcs_in_region;
    private int[] local_npcs;
    private int removedMobCount;
    private int[] removedMobs;
    private int lastOpcode;
    private int secondLastOpcode;
    private int thirdLastOpcode;
    public String clickToContinueString;
    public String prayerBook;
    private int privateChatMode;
    public boolean soundEnabled;
    private static int anInt849;
    private int[] anIntArray850;
    private int[] anIntArray851;
    private int[] anIntArray852;
    private int[] anIntArray853;
    private int hintIconDrawType;
    public static int widget_overlay_id; // The active interface (widget)
    private final Stopwatch chatDelay = new Stopwatch();
    public int cameraX;
    public int oculusOrbState;
    public int oculusOrbFocalPointX;
    public int oculusOrbFocalPointY;
    public static int oculusOrbSlowedSpeed;
    public static int oculusOrbNormalSpeed;
    public static int camFollowHeight;
    public int cameraY;
    public int cameraZ;
    public int cameraPitch;
    public int cameraYaw;
    static int viewportWidth = 0;
    static int viewportHeight = 0;
    static short zoomHeight = 256;
    static short zoomWidth = 320;
    static int viewportOffsetX = 0;
    static int viewportOffsetY = 0;
    private int myPrivilege, donatorPrivilege, ironmanPrivilege;
    public final int[] skillExperience;

    private final int[] quakeMagnitude;
    private final boolean[] quakeDirectionActive;
    private int weight;
    // private MouseDetection mouseDetection;
    private final boolean drawFlames;
    private String reportAbuseInput;
    public int localPlayerIndex;
    public boolean isMenuOpen;
    private int frameFocusedInterface;
    private String inputString;
    private final int maxPlayers;
    private final int maxNpcs;
    private final int LOCAL_PLAYER_INDEX;
    private Player[] players;
    private int players_in_region;
    private int[] local_players;
    private int mobsAwaitingUpdateCount;
    private int[] mobsAwaitingUpdate;
    private Buffer[] playerSynchronizationBuffers;
    private int cameraRotation;
    public int anInt897;
    private final String[] clanList = new String[100];
    private int friendsCount;
    private int friendServerStatus;
    private int[][] waypoints;
    private byte[] tmpTexture;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossType;
    // private int plane;
    public int plane;
    public final int[] boostedSkillLevels;
    private static int anInt924;
    private final long[] ignoreListAsLongs;
    private final boolean loadingError;
    private final int[] fourPiOverPeriod;
    private int[][] tile_cycle_map;
    private SimpleImage aClass30_Sub2_Sub1_Sub1_931;
    private SimpleImage aClass30_Sub2_Sub1_Sub1_932;
    private int hintIconPlayerId;
    private int hintIconX;
    private int hintIconY;
    private int hintIconLocationArrowHeight;
    private int hintIconLocationArrowRelX;
    private int hintIconLocationArrowRelY;
    private int animation_step;
    public SceneGraph scene;
    private SimpleImage[] sideIcons;
    private int menuScreenArea;
    private int menuX;
    private int menuY;
    private int menuWidth;
    private int menuHeight;
    private long aLong953;
    private String selectedSocialListName;
    private boolean hadFocus;
    private long[] friendsListAsLongs;

    private static int nodeID = 10;
    private static boolean isMembers = true;
    private static boolean low_detail = ClientConstants.CLIENT_LOW_MEMORY;
    private volatile boolean drawingFlames;
    private int scene_draw_x;
    private int scene_draw_y;
    private final int[] SPOKEN_PALETTE = { 0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff };
    private IndexedImage titleBoxIndexedImage;
    private IndexedImage titleButtonIndexedImage;
    private final int[] anIntArray968;
    private final int[] anIntArray969;

    public int[] settings;
    private boolean aBoolean972;
    private final int spokenMaxCount;
    private final int[] scene_text_x;
    private final int[] scene_text_y;
    private final int[] scene_text_height;
    private final int[] scene_text_center_x;
    private final int[] scene_text_color;
    private final int[] scene_text_effect;
    private final int[] scene_text_update_cycle;
    private final String[] spokenMessage;

    private int lastKnownPlane;
    private static int anInt986;
    private SimpleImage[] hitMarks;
    public int anInt988;
    private int draggingCycles;
    private final int[] characterDesignColours;
    private final boolean aBoolean994;

    private IsaacCipher encryption;
    private SimpleImage multiOverlay;
    public static final int[][] APPEARANCE_COLORS = {
            { 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193 },
            { 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239 },
            { 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003 },
            { 4626, 11146, 6439, 12, 4758, 10270 },
            { 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574, 15909, 32689, 130770, 947, 60359, 32433, 4960, 76770,
                    491770 }
    };

    public static final short[] field3532 = new short[] { 6798, 8741, 25238, 4626, 4550 };

    public static final short[][] PLAYER_BODY_RECOLOURS = new short[][] {
            { 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, -31839, 22433, 2983, -11343, 8, 5281, 10438, 3650, -27322,
                    -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },
            { 8741, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 25239,
                    8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },
            { 25238, 8742, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533,
                    8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },
            { 4626, 11146, 6439, 12, 4758, 10270 },
            { 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574, 17050, 0, 127, -31821, -17991 } };

    public static final short[] field3534 = new short[] { -10304, 9104, -1, -1, -1 };

    public static final short[][] field3535 = new short[][] {
            { 6554, 115, 10304, 28, 5702, 7756, 5681, 4510, -31835, 22437, 2859, -11339, 16, 5157, 10446, 3658, -27314,
                    -21965, 472, 580, 784, 21966, 28950, -15697, -14002 },
            { 9104, 10275, 7595, 3610, 7975, 8526, 918, -26734, 24466, 10145, -6882, 5027, 1457, 16565, -30545, 25486,
                    24, 5392, 10429, 3673, -27335, -21957, 192, 687, 412, 21821, 28835, -15460, -14019 },
            new short[0], new short[0], new short[0] };

    public String amountOrNameInput;
    private static int anInt1005;
    private int daysSinceLastLogin;

    private int packetSize;
    private int opcode;
    static long LAST_GPI;
    private int timeoutCounter;
    public int pingPacketCounter;
    private int logoutTimer;
    private NodeDeque projectiles;

    public int openWalkableInterface;
    private static final int[] SKILL_EXPERIENCE;
    private int minimapState;

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String input) {
        this.inputString = input;
    }

    private SimpleImage scrollBar1;
    private SimpleImage scrollBar2;
    private int focusedViewportWidget;
    public final int[] cameraUpdateCounters;
    private boolean updateCharacterCreation;

    // private int regionBaseX;
    // private int regionBaseY;
    public int next_region_start;
    public int next_region_end;
    private int previousAbsoluteX;
    private int previousAbsoluteY;
    private int loginFailures;
    private int focusedChatWidget;
    private int anInt1040;
    private int anInt1041;
    public int dialogueId;
    public final int[] realSkillLevels;
    private final int[] anIntArray1045;
    private int member;
    private boolean characterGender;
    private int focusedSidebarWidget;
    private String loading_bar_string;
    private static int anInt1051;
    private final int[] minimapLeft;

    private int flashingSidebarId;
    private int multicombat;
    private NodeDeque incompleteAnimables;
    private final int[] anIntArray1057;
    public final Widget aClass9_1059;
    private IndexedImage[] mapSceneSprites;

    private final int barFillColor;
    private int interfaceInputAction;
    private final int[] characterClothing;
    private int mouseInvInterfaceIndex;
    private int lastActiveInvInterface;

    public int region_x;
    public int region_y;
    private int objectIconCount;
    private int[] minimapHintX;
    private int[] minimapHintY;

    private int loading_bar_percent;
    private boolean loadingMap;
    private String[] friendsList;
    private String[] ignoreList;
    private Buffer incoming;
    private int focusedDragWidget;
    private int dragFromSlot;
    private int activeInterfaceType;
    private int mouseDragX;
    private int mouseDragY;
    public static int chatScrollAmount;
    public static int spellId = 0;
    public static int totalRead = 0;
    private int[] firstMenuAction;
    private int[] secondMenuAction;
    public int[] menuActionTypes;
    private long[] selectedMenuActions;
    private SimpleImage[] headIcons;
    private SimpleImage[] skullIcons;
    private SimpleImage[] headIconsHint;
    public static SimpleImage[] autoBackgroundSprites;
    private static int anInt1097;

    public static boolean update_tab_producer;
    private int rebootTimer;

    private static int anInt1117;
    private int membersInt;
    public String inputMessage;
    private SimpleImage compass;

    public static Player local_player;
    private final String[] playerOptions;
    private final boolean[] playerOptionsHighPriority;
    private final int[][][] constructRegionData;
    public static final int[] tabInterfaceIDs = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1 };

    public int anInt1132;
    public int menuActionRow;
    private static int anInt1134;
    private final int spellButtonUsed = -1;
    private int widget_highlighted;
    private int anInt1137;
    private int selectedTargetMask;
    private String selected_target_id;
    private SimpleImage[] minimapHint;
    private boolean inTutorialIsland;
    private static int anInt1142;
    public int runEnergy;
    public boolean continuedDialogue;
    private SimpleImage[] crosses;
    private IndexedImage[] titleIndexedImages;
    private int unreadMessages;
    private static int anInt1155;
    private static boolean fpsOn;
    public static boolean loggedIn;
    private boolean canMute;
    public boolean searchingBank;
    public boolean searchingShops;
    private boolean isInInstance;
    public boolean isCameraLocked;
    @Getter
    public static int tick;
    static long lastcallcount;
    private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    private int daysSinceRecovChange;
    private BufferedConnection SERVER_SOCKET;
    private ServerConnection SERVER_CONNECTION;
    ProofOfWorkRequester pow_request;
    Packet pow_response_buffer;
    public IsaacCipher cipher;
    public static PacketSender packetSender;
    private int privateMessageCount;
    private int minimapZoom;
    public int anInt1171;
    public String myUsername;
    public String myPassword;
    private boolean showClanOptions;
    private static int anInt1175;
    private final boolean genericLoadingError;
    private final int[] objectGroups = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
    private int reportAbuseInterfaceID;
    private NodeDeque spawns;
    private static int[] anIntArray1180;
    private static int[] anIntArray1181;
    private static int[] viewportOffsets;
    static byte[][] regionLandArchives;
    public int camAngleX;
    public int camAngleY;
    public int camAngleDY;
    public int camAngleDX;
    private static int anInt1188;
    public int overlayInterfaceId;
    private int[] anIntArray1190;
    private int[] anIntArray1191;
    public Buffer chatBuffer;
    private int anInt1193;
    private int splitPrivateChat;
    private IndexedImage mapBack;
    public String[] menuActionText;
    private SimpleImage flameLeftSprite;
    private SimpleImage flameRightSprite;
    private final int[] quakeAmplitude;
    public static final int[] SHIRT_SECONDARY_COLORS = { 9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145,
            58654, 5027, 1457, 16565, 34991, 25486 };
    private static boolean flagged;
    public final int[] tracks;
    private int map_rotation;
    public int anInt1210;
    public static int chatScrollHeight;
    private String promptInput;
    private int anInt1213;
    public int[][][] tileHeights = new int[4][105][105];
    private long serverSeed;
    public int loginScreenCursorPos;
    private long lastKeyboardEventTime;
    public static int sidebarId;
    private int hintIconNpcId;
    public static boolean update_chat_producer;
    public int inputDialogState;
    private static int anInt1226;

    private final int[] minimapLineWidth;
    public CollisionMap[] collisionMaps;
    public static int[] BIT_MASKS;

    private int[] regions;
    private int[] regionLandIds;
    private int[] regionLocIds;
    private int anInt1237;
    private int anInt1238;
    public final int anInt1239 = 100;
    private final int[] privateMessageIds;

    private boolean aBoolean1242;
    private int item_container_cycle;
    private int atInventoryInterface;
    private int atInventoryIndex;
    private int atInventoryInterfaceType;
    private byte[][] regionMapArchives;
    private int tradeMode;
    private int showSpokenEffects;

    private int onTutorialIsland;
    private final boolean rsAlreadyLoaded;
    private int useOneMouseButton;
    public int anInt1254;
    private boolean update_producers;
    public boolean messagePromptRaised;
    public byte[][][] tileFlags = new byte[4][104][104];

    private int travel_destination_x;
    private int travel_destination_y;
    private SimpleImage minimapImage;
    private int markerAngle;
    private long lastMarkerRotation;
    private int destination_mask;
    private int viewportDrawCount;
    public String firstLoginMessage;
    public String secondLoginMessage;
    private int localX;
    private int localY;
    public static AdvancedFont smallFont;
    public static AdvancedFont regularFont;
    public static AdvancedFont boldFont;
    public static AdvancedFont fancyFont;
    public AdvancedFont fancyFontMedium;
    public AdvancedFont fancyFontLarge;
    private int anInt1275;
    public int backDialogueId;

    public int anInt1279;
    private int[] walking_queue_x;
    private int[] walking_queue_y;
    private int item_highlighted;
    private int selectedItemIdSlot;
    private int interfaceitemSelectionTypeIn;
    private int useItem;
    private String selectedItemName;
    private int set_public_channel;
    private static int current_walking_queue_length;
    public static int anInt1290;
    public static final String serverAddress = ClientConstants.SERVER_ADDRESS;
    public int drawCount;
    public int fullscreenInterfaceID;
    public int tabTooltipSupportId;// 377
    public int gameTooltipSupportId;// 377
    public int anInt1315;// 377
    public int chatTooltipSupportId;// 377
    public static int anInt1501;// 377
    public static int[] fullScreenTextureArray;
    public static boolean isShiftPressed;
    public static boolean isCtrlPressed;

    public void resetAllImageProducers() {

        update_producers = true;
    }

    public void setMouseDragY(int mouseDragY) {
        this.mouseDragY = mouseDragY;
    }

    public int getMouseDragY() {
        return mouseDragY;
    }

    public void setActiveInterfaceType(int activeInterfaceType) {
        this.activeInterfaceType = activeInterfaceType;
    }

    public int getActiveInterfaceType() {
        return activeInterfaceType;
    }

    public void mouseWheelDragged(int pan, int tilt) {
        if (!MouseWheelHandler.mouseWheelDown) {
            return;
        }
        this.camAngleDY += pan * 3;
        this.camAngleDX += (tilt << 1);
    }

    // Also known as renderGroundItemNames
    private void render_item_pile_attatchments() {
        if (!setting.toggle_item_pile_names)
            return;

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                NodeDeque node = scene_items[plane][x][y];
                int offset = 12;
                if (node != null) {
                    for (Item item = (Item) node.last(); item != null; item = (Item) node.previous()) {
                        ItemDefinition itemDef = ItemDefinition.get(item.id);
                        get_scene_pos((x << 7) + 64, 64, (y << 7) + 64);
                        if (itemDef.cost < 0xC350 && item.quantity < 0x186A0) {
                            if (setting.filter_item_pile_names)
                                continue;
                        }
                        if (scene_draw_x > -1 && setting.toggle_item_pile_names) {
                            String color = "<trans=120><col=ffb000>";// Fallback
                            smallFont.drawCenteredString((itemDef.cost >= 0xC350 || item.quantity >= 0x186A0 ? color
                                    : "<trans=120>") + itemDef.name
                                    + (item.quantity > 1 ? "</col> (" + set_k_or_m(item.quantity) + "</col>)" : ""),
                                    scene_draw_x, scene_draw_y - offset, 0xffffff, 1);
                            offset += 12;
                        }
                    }
                }
            }
        }
    }

    /**
     * If toggled, render ground item names and lootbeams
     * Use {@link #render_item_pile_attatchments} instead
     */
    @Deprecated
    private void renderGroundItemNames() {
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                NodeDeque node = scene_items[plane][x][y];
                int offset = 12;
                if (node != null) {
                    for (Item item = (Item) node.last(); item != null; item = (Item) node.previous()) {
                        ItemDefinition item_definition = ItemDefinition.get(item.id);
                        final Item it = item;
                        get_scene_pos((x << 7) + 64, 64, (y << 7) + 64);
                        // Red if default value is >= 50k || amount >= 100k
                        smallFont.drawCenteredString(
                                (item_definition.cost >= 0xC350 || item.quantity >= 0x186A0 ? "<col=ff0000>"
                                        : "<trans=120>")
                                        + item_definition.name
                                        + (item.quantity > 1
                                                ? "</col> ("
                                                        + StringUtils
                                                                .insertCommasToNumber(String.valueOf(item.quantity))
                                                        + "</col>)"
                                                : ""),
                                scene_draw_x, scene_draw_y - offset, 0xffffff, 1);
                        offset += 12;
                    }
                }
            }
        }
    }

    // This actually reloads the floors.
    public static void toggleSnow(Archive configArchive) {
        instance.setGameState(GameState.LOADING);
    }

    public String date() {
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("h:mm:ss");
        return sd.format(date);
    }

    public static String capitalizeEachFirstLetter(String str) {

        String[] words = str.split(": ");

        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1)
                    .toLowerCase();
        }
        return String.join(": ", words);
    }

    public static String capitalizeFirstChar(final String character) {
        try {
            if (!character.equals("")) {
                return (character.substring(0, 1).toUpperCase() + character.substring(1)
                        .toLowerCase()).trim();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            addReportToServer(ex.getMessage());
        }
        return character;
    }

    float PercentCalc(long Number1, long number2) {
        float percentage;
        percentage = (Number1 * 100 / number2);
        return percentage;
    }

    public static String readableFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static long findSize(String path) {
        long totalSize = 0;
        ArrayList<String> directory = new ArrayList<String>();
        File file = new File(path);

        if (file.isDirectory()) {
            directory.add(file.getAbsolutePath());
            while (directory.size() > 0) {
                String folderPath = directory.get(0);
                directory.remove(0);
                File folder = new File(folderPath);
                File[] filesInFolder = folder.listFiles();
                int noOfFiles = filesInFolder.length;

                for (int i = 0; i < noOfFiles; i++) {
                    File f = filesInFolder[i];
                    if (f.isDirectory()) {
                        directory.add(f.getAbsolutePath());
                    } else {
                        totalSize += f.length();
                    }
                }
            }
        } else {
            totalSize = file.length();
        }
        return totalSize;
    }

    public static boolean inCircle(int circleX, int circleY, int clickX, int clickY,
            int radius) {
        return java.lang.Math.pow((circleX + radius - clickX), 2)
                + java.lang.Math.pow((circleY + radius - clickY), 2) < java.lang.Math.pow(radius, 2);
    }

    static {
        SKILL_EXPERIENCE = new int[99];
        int i = 0;
        for (int j = 0; j < 99; j++) {
            int l = j + 1;
            int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
            i += i1;
            SKILL_EXPERIENCE[j] = i / 4;
        }
        BIT_MASKS = new int[32];
        i = 2;
        for (int k = 0; k < 32; k++) {
            BIT_MASKS[k] = i - 1;
            i += i;
        }

    }

    /**
     * Used for storing all the clicked texts set to true. So when a new clicked
     * interface is changed to true, the ones stored here
     * can be reset.
     */
    public static ArrayList<String> textClickedList = new ArrayList<String>();

    public static void clearTextClicked() {
        for (int index = 0; index < textClickedList.size(); index++) {
            Widget.cache[Integer.parseInt(textClickedList.get(index))].textIsClicked = false;
        }
        textClickedList.clear();
    }

    /**
     * Have a specific interface text to have the clicked sprite.
     *
     * @param interfaceId
     * @param clicked
     */
    public static void setTextClicked(int interfaceId, boolean clicked) {
        // System.out.println("id: " + interfaceId + " text: " +
        // Widget.cache[interfaceId].defaultText + " clicked: " + clicked);
        if (clicked && Widget.cache[interfaceId].defaultText.isEmpty()) {
            return;
        }
        for (int index = 0; index < textClickedList.size(); index++) {
            Widget.cache[Integer.parseInt(textClickedList.get(index))].textIsClicked = false;
        }
        textClickedList.clear();
        Widget.cache[interfaceId].textIsClicked = clicked;
        if (clicked) {
            textClickedList.add(String.valueOf(interfaceId));
        }
    }

    public PrayerSystem.InterfaceData prayerGrabbed = null;

    public void releasePrayer() {
        if (!setting.moving_prayers) {
            return;
        }
        if (prayerGrabbed != null) {
            int yMod = 249;
            int posX = !resized ? MouseHandler.mouseX - 550 : MouseHandler.mouseX - (canvasWidth - 195);
            int posY = !resized ? MouseHandler.mouseY - 205 : MouseHandler.mouseY - (canvasHeight - yMod);
            if (resized) {
                yMod = 340;
                if (settings[ConfigUtility.SIDE_STONES_ARRANGEMENT_ID] == 0) {
                    posY = MouseHandler.mouseY - (canvasWidth >= 1000 ? canvasHeight - 303 : canvasHeight - yMod);
                } else {
                    posX = MouseHandler.mouseX - (canvasWidth - 215);
                    posY = MouseHandler.mouseY - (canvasHeight - yMod);

                }
            }
            PrayerSystem.release(prayerGrabbed, posX, posY);
            prayerGrabbed = null;
        }
    }

    public int[] teleportSprites = null;

    public String[] teleportNames = null;

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        Client.loggedIn = loggedIn;
    }

    private void startSpinner() {
        Widget w = Widget.cache[71101];
        Widget w2 = Widget.cache[71200];
        if (w.x >= -600) {
            w.x -= 25;
            w2.x -= 25;
        }
        if (w.x >= -1512 && w.x <= -601) {
            w.x -= (25 / spinSpeed);
            w2.x -= (25 / spinSpeed);
            spinSpeed = spinSpeed + 0.07f;
        }
        if (w.x >= -1600 && w.x < -1513) {
            w.x -= (25 / spinSpeed);
            w2.x -= (25 / spinSpeed);
            spinSpeed = spinSpeed + 2f;
        }
        if (w.x <= -1513) {
            startSpin = false;
        }
    }

    private boolean broadcastActive() {
        return broadcastText != null && !broadcastText.isEmpty() || (broadcast != null && isDisplayed);
    }

    /**
     * Runelite
     */
    public DrawCallbacks drawCallbacks;
    @javax.inject.Inject
    private Callbacks callbacks;

    private boolean gpu = false;

    @Override
    public Callbacks getCallbacks() {
        return callbacks;
    }

    @Override
    public DrawCallbacks getDrawCallbacks() {
        return drawCallbacks;
    }

    @Override
    public void setDrawCallbacks(DrawCallbacks drawCallbacks) {
        this.drawCallbacks = drawCallbacks;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public String getBuildID() {
        return "1";
    }

    @Override
    public List<net.runelite.api.Player> getPlayers() {
        return Arrays.asList(players);
    }

    @Override
    public List<NPC> getNpcs() {
        List<NPC> npcs = new ArrayList<NPC>(npcs_in_region);
        for (int i = 0; i < npcs_in_region; ++i) {
            npcs.add(this.npcs[local_npcs[i]]);
        }
        return npcs;
    }

    @Override
    public RSNPC[] getCachedNPCs() {
        return npcs;
    }

    @Override
    public RSPlayer[] getCachedPlayers() {
        return players;
    }

    @Override
    public int getLocalInteractingIndex() {
        return 0;
    }

    @Override
    public void setLocalInteractingIndex(int idx) {

    }

    @Override
    public RSNodeDeque getTilesDeque() {
        return null;
    }

    @Override
    public RSNodeDeque[][][] getGroundItemDeque() {
        return scene_items;
    }

    @Override
    public RSNodeDeque getProjectilesDeque() {
        return projectiles;
    }

    @Override
    public RSNodeDeque getGraphicsObjectDeque() {
        return null;
    }

    @Override
    public String getUsername() {
        return myUsername;
    }

    @Override
    public int getBoostedSkillLevel(Skill skill) {
        return boostedSkillLevels[skill.ordinal()];
    }

    @Override
    public int getRealSkillLevel(Skill skill) {
        return realSkillLevels[skill.ordinal()];
    }

    @Override
    public String getVirtualSkillLevelWidget() {
        return Widget.cache[10121].defaultText;
    }

    @Override
    public void setVirtualSkillLevel(String virtualSkillLevel) {
        Widget.cache[10121].defaultText = virtualSkillLevel;
    }

    @Override
    public int getTotalLevel() {
        return IntStream.of(realSkillLevels).sum();
    }

    @Override
    public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender) {
        return null;
    }

    @Override
    public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender,
            boolean postEvent) {
        return null;
    }

    @Override
    public GameState getGameState() {
        return GameState.of(getRSGameState());
    }

    static int loadingType;
    static int mapsLoaded;
    static int totalMaps;

    public static int objectsLoaded;

    static int totalObjects;

    @Override
    public int getRSGameState() {
        return gameState;
    }

    @Override
    public void setRSGameState(int state) {
        if (state != gameState) {
            if (gameState == 30) {
                // Updates Widget does not have on 317
            }

            if (gameState == 0) {
                // Resets fonts dont need for 317
            }

            if (state == 20 || state == 40 || state == 45 || state == 50) {
                loginStage = 0;
            }

            if (state != 20 && state != 40) {
                // Resets some Socket
            }

            jagexNetThread.writePacket(true);

            if (gameState == 25) {
                Client.loadingType = 0;
                Client.mapsLoaded = 0;
                Client.totalMaps = 1;
                Client.objectsLoaded = 0;
                Client.totalObjects = 1;
            }

            if (state != 5 && state != 10) { // L: 1270
                if (state == 20) { // L: 1278
                    setupLoginScreen();
                } else if (state == 11) { // L: 1282
                    setupLoginScreen();
                } else if (state == 50) { // L: 1285
                    setupLoginScreen();
                } else if (clearLoginScreen) { // L: 1290
                    StaticSound.clear();
                    clearLoginScreen = false;
                }
            } else {
                setupLoginScreen();
            }

            gameStateChanged(0);
            gameState = state;

        }
    }

    @Override
    public void setCheckClick(boolean checkClick) {
        scene.clicked = checkClick;
    }

    @Override
    public void setMouseCanvasHoverPositionX(int x) {
        MouseHandler.mouseX = x;
    }

    @Override
    public void setMouseCanvasHoverPositionY(int y) {
        MouseHandler.mouseY = y;
    }

    public void setGameState(GameState state) {
        // assert this.isClientThread() : "setGameState must be called on client
        // thread";
        setGameState(state.getState());
    }

    public void setGameState(int state) {
        // assert this.isClientThread() : "setGameState must be called on client
        // thread";
        setRSGameState(state);
    }

    public static boolean clearLoginScreen;

    public static void setupLoginScreen() {
        if (clearLoginScreen) { // L: 183

        } else {

            StaticSound.update();
            clearLoginScreen = true;
        }

    }

    public static void gameStateChanged(int idx) {
        GameState gameState = Client.instance.getGameState();
        Client.instance.getLogger().debug("Game state changed: {}", gameState);
        GameStateChanged gameStateChange = new GameStateChanged();
        gameStateChange.setGameState(gameState);
        Client.instance.getCallbacks().post(gameStateChange);

        if (gameState == GameState.LOGGED_IN) {
            if (Client.instance.getLocalPlayer() == null) {
                return;
            }

        } else if (gameState == GameState.LOGIN_SCREEN) {
            // Laod Varbits
        }
    }

    @Override
    public void stopNow() {
    }

    @Override
    public void setUsername(String name) {
        myUsername = name;
    }

    @Override
    public void setPassword(String password) {
        myPassword = password;
    }

    @Override
    public void setOtp(String otp) {

    }

    @Override
    public int getCurrentLoginField() {
        return 0;
    }

    @Override
    public int getLoginIndex() {
        return 0;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.NORMAL;
    }

    @Override
    public int getFPS() {
        return fps;
    }

    @Override
    public int getCameraX() {
        return this.cameraX;
    }

    @Override
    public int getCameraY() {
        return this.cameraZ;
    }

    @Override
    public int getCameraZ() {
        return this.cameraY;
    }

    @Override
    public int getCameraPitch() {
        return cameraPitch;
    }

    @Override
    public void setCameraPitch(int cameraPitch) {
        this.cameraPitch = cameraPitch;
    }

    @Override
    public int getCameraYaw() {
        return cameraYaw;
    }

    @Override
    public int getWorld() {
        return 1;
    }

    @Override
    public int getCanvasHeight() {
        return canvasHeight;
    }

    @Override
    public int getCanvasWidth() {
        return canvasWidth;
    }

    @Override
    public int getViewportHeight() {
        return viewportHeight;
    }

    @Override
    public int getViewportWidth() {
        return viewportWidth;

    }

    @Override
    public int getViewportXOffset() {
        return viewportOffsetX;
    }

    @Override
    public int getViewportYOffset() {
        return viewportOffsetY;
    }

    public static int viewportZoom;

    @Override
    public void setScale(int scale) {
        viewportZoom = scale;
    }

    @Override
    public int getScale() {
        return viewportZoom;
    }

    @Override
    public Point getMouseCanvasPosition() {
        return new Point(MouseHandler.mouseX, MouseHandler.mouseY);
    }

    @Override
    public int[][][] getTileHeights() {
        return tileHeights;
    }

    @Override
    public byte[][][] getTileSettings() {
        return tileFlags;
    }

    @Override
    public int getPlane() {
        return plane;
    }

    @Override
    public SceneGraph getScene() {
        return scene;
    }

    @Override
    public RSPlayer getLocalPlayer() {
        return local_player;
    }

    @Override
    public int getLocalPlayerIndex() {
        return local_player.index;
    }

    @Override
    public int getNpcIndexesCount() {
        return 0;
    }

    @Override
    public int[] getNpcIndices() {
        return new int[0];
    }

    @Override
    public ItemComposition getItemComposition(int id) {
        assert this.isClientThread() : "getItemComposition must be called on client thread";
        RSItemComposition def = getRSItemDefinition(id);
        return def;
    }

    @Override
    public ItemComposition getItemDefinition(int id) {
        return getItemComposition(id);
    }

    @Override
    public SpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable,
            boolean noted, int scale) {
        assert isClientThread() : "createItemSprite must be called on client thread";

        int zoom = get3dZoom();
        set3dZoom(scale);
        try {
            return createItemSprite(itemId, quantity, border, shadowColor, stackable, noted);
        } finally {
            set3dZoom(zoom);
        }
    }

    public RSSpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable,
            boolean noted) {
        assert isClientThread() : "createItemSprite must be called on client thread";
        return createRSItemSprite(itemId, quantity, border, shadowColor, stackable, noted);
    }

    @Override
    public RSSpritePixels[] getSprites(IndexDataBase source, int archiveId, int fileId) {
        RSAbstractArchive rsSource = (RSAbstractArchive) source;
        byte[] configData = rsSource.getConfigData(archiveId, fileId);
        if (configData == null) {
            return null;
        }

        decodeSprite(configData);

        int indexedSpriteCount = getIndexedSpriteCount();
        int maxWidth = getIndexedSpriteWidth();
        int maxHeight = getIndexedSpriteHeight();
        int[] offsetX = getIndexedSpriteOffsetXs();
        int[] offsetY = getIndexedSpriteOffsetYs();
        int[] widths = getIndexedSpriteWidths();
        int[] heights = getIndexedSpriteHeights();
        byte[][] spritePixelsArray = getSpritePixels();
        int[] indexedSpritePalette = getIndexedSpritePalette();

        RSSpritePixels[] array = new RSSpritePixels[indexedSpriteCount];

        for (int i = 0; i < indexedSpriteCount; ++i) {
            int width = widths[i];
            int height = heights[i];

            byte[] pixelArray = spritePixelsArray[i];
            int[] pixels = new int[width * height];

            RSSpritePixels spritePixels = createSpritePixels(pixels, width, height);
            spritePixels.setMaxHeight(maxHeight);
            spritePixels.setMaxWidth(maxWidth);
            spritePixels.setOffsetX(offsetX[i]);
            spritePixels.setOffsetY(offsetY[i]);

            for (int j = 0; j < width * height; ++j) {
                pixels[j] = indexedSpritePalette[pixelArray[j] & 0xff];
            }

            array[i] = spritePixels;
        }

        setIndexedSpriteOffsetXs(null);
        setIndexedSpriteOffsetYs(null);
        setIndexedSpriteWidths(null);
        setIndexedSpriteHeights(null);
        setIndexedSpritePalette(null);
        setSpritePixels(null);

        return array;
    }

    @Override
    public RSArchive getIndexSprites() {
        return Js5List.sprites;
    }

    @Override
    public RSArchive getIndexScripts() {
        return Js5List.clientScript;
    }

    @Override
    public RSArchive getIndexConfig() {
        return Js5List.configs;
    }

    @Override
    public RSArchive getMusicTracks() {
        return Js5List.musicTracks;
    }

    @Override
    public int getBaseX() {
        return next_region_start;
    }

    @Override
    public int getBaseY() {
        return next_region_end;
    }

    @Override
    public int getMouseCurrentButton() {
        return 0;
    }

    @Override
    public int getSelectedSceneTileX() {
        return SceneGraph.clickedTileX;
    }

    @Override
    public void setSelectedSceneTileX(int selectedSceneTileX) {
        scene.clickedTileX = selectedSceneTileX;
    }

    @Override
    public int getSelectedSceneTileY() {
        return SceneGraph.clickedTileY;
    }

    @Override
    public void setSelectedSceneTileY(int selectedSceneTileY) {
        scene.clickedTileY = selectedSceneTileY;
    }

    @Override
    public RSTile getSelectedSceneTile() {
        int tileX = SceneGraph.hoverX;
        int tileY = SceneGraph.hoverY;

        if (tileX == -1 || tileY == -1) {
            return null;
        }

        return getScene().getTiles()[getPlane()][tileX][tileY];

    }

    @Override
    public boolean isDraggingWidget() {
        return false;
    }

    @Override
    public RSWidget getDraggedWidget() {
        return null;
    }

    @Override
    public RSWidget getDraggedOnWidget() {
        return null;
    }

    @Override
    public void setDraggedOnWidget(net.runelite.api.widgets.Widget widget) {
    }

    @Override
    public RSWidget[][] getWidgets() {
        return new RSWidget[0][];
    }

    @Override
    public RSWidget[] getGroup(int groupId) {
        RSWidget[][] widgets = getWidgets();

        if (widgets == null || groupId < 0 || groupId >= widgets.length || widgets[groupId] == null) {
            return null;
        }

        return widgets[groupId];
    }

    @Override
    public int getTopLevelInterfaceId() {
        return 0;
    }

    @Override
    public RSWidget[] getWidgetRoots() {
        int topGroup = getTopLevelInterfaceId();
        if (topGroup == -1) {
            return new RSWidget[] {};
        }
        List<net.runelite.api.widgets.Widget> widgets = new ArrayList<net.runelite.api.widgets.Widget>();
        for (RSWidget widget : getWidgets()[topGroup]) {
            if (widget != null && widget.getRSParentId() == -1) {
                widgets.add(widget);
            }
        }
        return widgets.toArray(new RSWidget[widgets.size()]);
    }

    @Override
    public RSWidget getWidget(WidgetInfo widget) {
        int groupId = widget.getGroupId();
        int childId = widget.getChildId();

        return getWidget(groupId, childId);
    }

    @Override
    public RSWidget getWidget(int groupId, int childId) {
        RSWidget[][] widgets = getWidgets();

        if (widgets == null || widgets.length <= groupId) {
            return null;
        }

        RSWidget[] childWidgets = widgets[groupId];
        if (childWidgets == null || childWidgets.length <= childId) {
            return null;
        }

        return childWidgets[childId];
    }

    @Override
    public RSWidget getWidget(int packedID) {
        return getWidget(WidgetInfo.TO_GROUP(packedID), WidgetInfo.TO_CHILD(packedID));
    }

    @Override
    public int[] getWidgetPositionsX() {
        return null;
    }

    @Override
    public int[] getWidgetPositionsY() {
        return null;
    }

    @Override
    public boolean isMouseCam() {
        return false;
    }

    @Override
    public int getCamAngleDX() {
        return camAngleDX;
    }

    @Override
    public void setCamAngleDX(int angle) {
        camAngleDX = angle;
    }

    @Override
    public int getCamAngleDY() {
        return camAngleDY;
    }

    @Override
    public void setCamAngleDY(int angle) {
        camAngleDY = angle;
    }

    @Override
    public RSWidget createWidget() {
        return null;
    }

    @Override
    public void revalidateWidget(net.runelite.api.widgets.Widget w) {

    }

    @Override
    public void revalidateWidgetScroll(net.runelite.api.widgets.Widget[] group, net.runelite.api.widgets.Widget w,
            boolean postEvent) {

    }

    @Override
    public int getEntitiesAtMouseCount() {
        return ViewportMouse.entityCount;
    }

    @Override
    public void setEntitiesAtMouseCount(int i) {
        ViewportMouse.entityCount = i;
    }

    @Override
    public long[] getEntitiesAtMouse() {
        return ViewportMouse.entityTags;
    }

    @Override
    public int getViewportMouseX() {
        return ViewportMouse.viewportMouseX;
    }

    @Override
    public int getViewportMouseY() {
        return ViewportMouse.viewportMouseY;
    }

    @Override
    public int getEnergy() {
        return runEnergy;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String[] getPlayerOptions() {
        return playerOptions;
    }

    @Override
    public boolean[] getPlayerOptionsPriorities() {
        return playerOptionsHighPriority;
    }

    @Override
    public int[] getPlayerMenuTypes() {
        return null;
    }

    @Override
    public int getMouseX() {
        return MouseHandler.mouseX;
    }

    @Override
    public int getMouseY() {
        return MouseHandler.mouseY;
    }

    @Override
    public int getMouseX2() {
        return scene.clickScreenX;
    }

    @Override
    public int getMouseY2() {
        return scene.clickScreenY;
    }

    @Override
    public boolean containsBounds(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        return scene.inBounds(var0, var1, var2, var3, var4, var5, var6, var7);
    }

    @Override
    public boolean isCheckClick() {
        return SceneGraph.clicked;
    }

    @Override
    public RSWorld[] getWorldList() {
        return null;
    }

    @Override
    public MenuEntry createMenuEntry(int idx) {
        return null;
    }

    @Override
    public void addRSChatMessage(int type, String name, String message, String sender) {

    }

    @Override
    public RSObjectComposition getRSObjectComposition(int objectId) {
        return null;
    }

    @Override
    public RSNPCComposition getRSNpcComposition(int npcId) {
        return null;
    }

    @Override
    public MenuEntry createMenuEntry(String option, String target, int identifier, int opcode, int param1, int param2,
            boolean forceLeftClick) {
        return null;
    }

    @Override
    public MenuEntry[] getMenuEntries() {
        return null;
    }

    @Override
    public int getMenuOptionCount() {
        return menuActionRow;
    }

    @Override
    public void setMenuEntries(MenuEntry[] entries) {

    }

    @Override
    public void setMenuOptionCount(int count) {
        this.menuActionRow = count;
    }

    @Override
    public String[] getMenuOptions() {
        return menuActionText;
    }

    @Override
    public String[] getMenuTargets() {
        return new String[0];
    }

    @Override
    public int[] getMenuIdentifiers() {
        return new int[0];
    }

    @Override
    public int[] getMenuOpcodes() {
        return new int[0];
    }

    @Override
    public int[] getMenuArguments1() {
        return new int[0];
    }

    @Override
    public int[] getMenuArguments2() {
        return new int[0];
    }

    @Override
    public boolean[] getMenuForceLeftClick() {
        return new boolean[0];
    }

    @Override
    public boolean isMenuOpen() {
        return isMenuOpen;
    }

    @Override
    public int getMenuX() {
        return menuX;
    }

    @Override
    public int getMenuY() {
        return menuY;
    }

    @Override
    public int getMenuHeight() {
        return menuHeight;
    }

    @Override
    public int getMenuWidth() {
        return menuWidth;
    }

    @Override
    public net.runelite.rs.api.RSFont getFontBold12() {
        return null;
    }

    @Override
    public void rasterizerDrawHorizontalLine(int x, int y, int w, int rgb) {
        Rasterizer2D.drawHorizontalLine(x, y, w, rgb);
    }

    @Override
    public void rasterizerDrawHorizontalLineAlpha(int x, int y, int w, int rgb, int a) {
        Rasterizer2D.drawTransparentHorizontalLine(x, y, w, rgb, a);
    }

    @Override
    public void rasterizerDrawVerticalLine(int x, int y, int h, int rgb) {
        Rasterizer2D.draw_vertical_line(x, y, h, rgb);
    }

    @Override
    public void rasterizerDrawVerticalLineAlpha(int x, int y, int h, int rgb, int a) {
        Rasterizer2D.drawTransparentVerticalLine(x, y, h, rgb, a);
    }

    @Override
    public void rasterizerDrawGradient(int x, int y, int w, int h, int rgbTop, int rgbBottom) {
        Rasterizer2D.drawTransparentGradientBox(x, y, w, h, rgbTop, rgbBottom, 255);
    }

    @Override
    public void rasterizerDrawGradientAlpha(int x, int y, int w, int h, int rgbTop, int rgbBottom, int alphaTop,
            int alphaBottom) {
        Rasterizer2D.drawTransparentGradientBox(x, y, w, h, rgbTop, rgbBottom, 255);
    }

    @Override
    public void rasterizerFillRectangleAlpha(int x, int y, int w, int h, int rgb, int a) {
        Rasterizer2D.draw_filled_rect(x, y, w, h, rgb, a);
    }

    @Override
    public void rasterizerDrawRectangle(int x, int y, int w, int h, int rgb) {
        Rasterizer2D.draw_filled_rect(x, y, w, h, rgb);
    }

    @Override
    public void rasterizerDrawRectangleAlpha(int x, int y, int w, int h, int rgb, int a) {
        Rasterizer2D.drawTransparentBoxOutline(x, y, w, h, rgb, a);
    }

    @Override
    public void rasterizerDrawCircle(int x, int y, int r, int rgb) {

    }

    @Override
    public void rasterizerDrawCircleAlpha(int x, int y, int r, int rgb, int a) {
        Rasterizer2D.drawAlphaCircle(x, y, r, rgb, a);
    }

    @Override
    public RSEvictingDualNodeHashTable getHealthBarCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getHealthBarSpriteCache() {
        return null;
    }

    @Override
    public int getMapAngle() {
        return camAngleY;
    }

    @Override
    public void setCameraYawTarget(int cameraYawTarget) {
        camAngleY = cameraYawTarget;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    private boolean resized = false;

    @Override
    public boolean isResized() {
        return resized;
    }

    @Override
    public int getRevision() {
        return 1;
    }

    @Override
    public int[] getMapRegions() {
        return regions;
    }

    public int[] getRegions() {
        return regions;
    }

    @Override
    public int[][][] getInstanceTemplateChunks() {
        return constructRegionData;
    }

    @Override
    public int[][] getXteaKeys() {
        return null;
    }

    @Override
    public int getCycleCntr() {
        return animation_step;
    }

    @Override
    public void setChatCycle(int value) {
        this.local_player.message_cycle = value;
    }

    @Override
    public int[] getVarps() {
        return settings;
    }

    @Override
    public RSVarcs getVarcs() {
        return null;
    }

    @Override
    public Map<Integer, Object> getVarcMap() {
        return null;
    }

    @Override
    public int getVar(VarPlayer varPlayer) {
        return getVarps()[varPlayer.getId()];
    }

    @Override
    public int getVar(Varbits varbit) {
        return getVarps()[varbit.getId()];
    }

    @Override
    public int getVar(VarClientInt varClientInt) {
        return getVarps()[varClientInt.getIndex()];
    }

    @Override
    public String getVar(VarClientStr varClientStr) {
        return null;
    }

    @Override
    public int getVarbitValue(Varbits varbitId) {
        return VarbitManager.getVarbit(varbitId.getId());
    }

    @Override
    public int getVarcIntValue(int varcIntId) {
        return 0;
    }

    @Override
    public String getVarcStrValue(int varcStrId) {
        return null;
    }

    @Override
    public void setVar(VarClientStr varClientStr, String value) {
    }

    @Override
    public void setVar(VarClientInt varClientStr, int value) {
    }

    @Override
    public void setVarbit(Varbits varbit, int value) {
    }

    @Override
    public VarbitComposition getVarbit(int id) {
        return null;
    }

    @Override
    public int getVarbitValue(int[] varps, int varbitId) {
        return 0;
    }

    @Override
    public int getVarpValue(int[] varps, int varpId) {
        return 0;
    }

    @Override
    public int getVarpValue(int i) {
        return 0;
    }

    @Override
    public void setVarbitValue(int[] varps, int varbit, int value) {
    }

    @Override
    public void queueChangedVarp(int varp) {
    }

    @Override
    public RSNodeHashTable getWidgetFlags() {
        return null;
    }

    @Override
    public RSNodeHashTable getComponentTable() {
        return null;
    }

    @Override
    public RSGrandExchangeOffer[] getGrandExchangeOffers() {
        return null;
    }

    @Override
    public boolean isPrayerActive(Prayer prayer) {
        return false;
    }

    @Override
    public int getSkillExperience(Skill skill) {
        return skillExperience[skill.ordinal()];
    }

    @Override
    public long getOverallExperience() {
        return IntStream.of(skillExperience).sum();
    }

    @Override
    public void refreshChat() {
    }

    @Override
    public Map<Integer, ChatLineBuffer> getChatLineMap() {
        return null;
    }

    @Override
    public RSIterableNodeHashTable getMessages() {
        return null;
    }

    @Override
    public ObjectComposition getObjectDefinition(int objectId) {
        return ObjectDefinition.get(objectId);
    }

    @Override
    public NPCComposition getNpcDefinition(int npcId) {
        return NpcDefinition.get(npcId);
    }

    @Override
    public StructComposition getStructComposition(int structID) {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getStructCompositionCache() {
        return null;
    }

    @Override
    public RSWorldMapElement[] getMapElementConfigs() {
        return null;
    }

    @Override
    public RSSpritePixels[] getMapScene() {
        return null;
    }

    public SimpleImage[] mapDotSprites = null;

    @Override
    public RSSpritePixels[] getMapDots() {
        return mapDotSprites;
    }

    @Override
    public int getGameCycle() {
        return tick;
    }

    @Override
    public RSSpritePixels[] getMapIcons() {
        return null;
    }

    @Override
    public RSIndexedSprite[] getModIcons() {
        return null;
    }

    @Override
    public void setRSModIcons(RSIndexedSprite[] modIcons) {

    }

    @Override
    public void setModIcons(IndexedSprite[] modIcons) {

    }

    @Override
    public RSIndexedSprite createIndexedSprite() {
        return null;
    }

    @Override
    public RSSpritePixels createSpritePixels(int[] pixels, int width, int height) {
        return null;
    }

    @Override
    public int getDestinationX() {
        return travel_destination_x;
    }

    @Override
    public int getDestinationY() {
        return travel_destination_y;
    }

    @Override
    public RSSoundEffect[] getAudioEffects() {
        return new RSSoundEffect[0];
    }

    @Override
    public int[] getQueuedSoundEffectIDs() {
        return new int[0];
    }

    @Override
    public int[] getSoundLocations() {
        return new int[0];
    }

    @Override
    public int[] getQueuedSoundEffectLoops() {
        return new int[0];
    }

    @Override
    public int[] getQueuedSoundEffectDelays() {
        return new int[0];
    }

    @Override
    public int getQueuedSoundEffectCount() {
        return 0;
    }

    @Override
    public void setQueuedSoundEffectCount(int queuedSoundEffectCount) {

    }

    @Override
    public void queueSoundEffect(int id, int numLoops, int delay) {

    }

    @Override
    public LocalPoint getLocalDestinationLocation() {
        int sceneX = getDestinationX();
        int sceneY = getDestinationY();
        if (sceneX != 0 && sceneY != 0) {
            return LocalPoint.fromScene(sceneX, sceneY);
        }
        return null;
    }

    @Override
    public List<net.runelite.api.Projectile> getProjectiles() {
        List<net.runelite.api.Projectile> projectileList = new ArrayList<>();
        for (Projectile projectile = (Projectile) projectiles
                .reverseGetFirst(); projectile != null; projectile = (Projectile) projectiles.reverseGetNext()) {
            projectileList.add(projectile);
        }
        return projectileList;
    }

    @Override
    public List<GraphicsObject> getGraphicsObjects() {
        List<net.runelite.api.GraphicsObject> list = new ArrayList<>();
        for (GraphicsObject projectile = (GraphicsObject) incompleteAnimables
                .reverseGetFirst(); projectile != null; projectile = (GraphicsObject) incompleteAnimables
                        .reverseGetNext()) {
            list.add(projectile);
        }
        return list;
    }

    @Override
    public RuneLiteObject createRuneLiteObject() {
        return null;
    }

    @Override
    public net.runelite.api.Model loadModel(int id) {
        return null;
    }

    @Override
    public net.runelite.api.Model loadModel(int id, short[] colorToFind, short[] colorToReplace) {
        return null;
    }

    @Override
    public net.runelite.api.Animation loadAnimation(int id) {
        return null;
    }

    @Override
    public int getMusicVolume() {
        return 0;
    }

    @Override
    public void setMusicVolume(int volume) {
    }

    @Override
    public void playSoundEffect(int id) {

    }

    @Override
    public void playSoundEffect(int id, int x, int y, int range) {
    }

    @Override
    public void playSoundEffect(int id, int x, int y, int range, int delay) {
    }

    @Override
    public void playSoundEffect(int id, int volume) {

    }

    @Override
    public RSAbstractRasterProvider getBufferProvider() {
        return rasterProvider;
    }

    @Override
    public int getMouseIdleTicks() {
        return MouseHandler.idleCycles;
    }

    @Override
    public long getMouseLastPressedMillis() {
        return MouseHandler.lastPressed;
    }

    public long getClientMouseLastPressedMillis() {
        return MouseHandler.lastPressed;
    }

    public void setClientMouseLastPressedMillis(long mills) {
        MouseHandler.lastPressed = mills;
    }

    @Override
    public int getKeyboardIdleTicks() {
        return KeyHandler.idleCycles;
    }

    @Override
    public void changeMemoryMode(boolean lowMemory) {
        setLowMemory(lowMemory);
        setSceneLowMemory(lowMemory);
        setAudioHighMemory(true);
        setObjectDefinitionLowDetail(lowMemory);
        if (getGameState() == GameState.LOGGED_IN) {
            setGameState(1);
        }
    }

    public HashMap<Integer, ItemContainer> containers = new HashMap<Integer, ItemContainer>();

    @Override
    public ItemContainer getItemContainer(InventoryID inventory) {
        return containers.get(inventory.getId());
    }

    @Override
    public ItemContainer getItemContainer(int id) {
        return containers.get(id);
    }

    @Override
    public RSNodeHashTable getItemContainers() {
        return null;
    }

    @Override
    public RSItemComposition getRSItemDefinition(int itemId) {
        return ItemDefinition.get(itemId);
    }

    @Override
    public RSSpritePixels createRSItemSprite(int itemId, int quantity, int thickness, int borderColor, int stackable,
            boolean noted) {
        return ItemDefinition.getSprite(itemId, quantity, thickness, borderColor, stackable, noted);
    }

    @Override
    public void sendMenuAction(int n2, int n3, int n4, int n5, String string, String string2, int n6, int n7) {

    }

    @Override
    public void decodeSprite(byte[] data) {
        SpriteData.decode(data);
    }

    @Override
    public int getIndexedSpriteCount() {
        return SpriteData.spriteCount;
    }

    @Override
    public int getIndexedSpriteWidth() {
        return SpriteData.spriteWidth;
    }

    @Override
    public int getIndexedSpriteHeight() {
        return SpriteData.spriteHeight;
    }

    @Override
    public int[] getIndexedSpriteOffsetXs() {
        return SpriteData.xOffsets;
    }

    @Override
    public void setIndexedSpriteOffsetXs(int[] indexedSpriteOffsetXs) {
        SpriteData.xOffsets = indexedSpriteOffsetXs;
    }

    @Override
    public int[] getIndexedSpriteOffsetYs() {
        return SpriteData.yOffsets;
    }

    @Override
    public void setIndexedSpriteOffsetYs(int[] indexedSpriteOffsetYs) {
        SpriteData.yOffsets = indexedSpriteOffsetYs;
    }

    @Override
    public int[] getIndexedSpriteWidths() {
        return SpriteData.spriteWidths;
    }

    @Override
    public void setIndexedSpriteWidths(int[] indexedSpriteWidths) {
        SpriteData.spriteWidths = indexedSpriteWidths;
    }

    @Override
    public int[] getIndexedSpriteHeights() {
        return SpriteData.spriteHeights;
    }

    @Override
    public void setIndexedSpriteHeights(int[] indexedSpriteHeights) {
        SpriteData.spriteHeights = indexedSpriteHeights;
    }

    @Override
    public byte[][] getSpritePixels() {
        return SpriteData.pixels;
    }

    @Override
    public void setSpritePixels(byte[][] spritePixels) {
        SpriteData.pixels = spritePixels;
    }

    @Override
    public int[] getIndexedSpritePalette() {
        return SpriteData.spritePalette;
    }

    @Override
    public void setIndexedSpritePalette(int[] indexedSpritePalette) {
        SpriteData.spritePalette = indexedSpritePalette;
    }

    @Override
    public int getIntStackSize() {
        return 0;
    }

    @Override
    public void setIntStackSize(int stackSize) {
    }

    @Override
    public int[] getIntStack() {
        return null;
    }

    @Override
    public int getStringStackSize() {
        return 0;
    }

    @Override
    public void setStringStackSize(int stackSize) {
    }

    @Override
    public String[] getStringStack() {
        return null;
    }

    @Override
    public RSFriendSystem getFriendManager() {
        return null;
    }

    @Override
    public RSWidget getScriptActiveWidget() {
        return null;
    }

    @Override
    public RSWidget getScriptDotWidget() {
        return null;
    }

    @Override
    public RSScriptEvent createRSScriptEvent(Object... args) {
        return null;
    }

    @Override
    public void runScriptEvent(RSScriptEvent event) {

    }

    @Override
    public RSEvictingDualNodeHashTable getScriptCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getRSStructCompositionCache() {
        return null;
    }

    @Override
    public RSStructComposition getRSStructComposition(int id) {
        return null;
    }

    @Override
    public RSParamComposition getRSParamComposition(int id) {
        return null;
    }

    @Override
    public void setMouseLastPressedMillis(long time) {

    }

    @Override
    public int getRootWidgetCount() {
        return 0;
    }

    @Override
    public int getWidgetClickX() {
        return 0;
    }

    @Override
    public int getWidgetClickY() {
        return 0;
    }

    @Override
    public int getStaffModLevel() {
        return rights;
    }

    @Override
    public int getTradeChatMode() {
        return tradeMode;
    }

    @Override
    public int getPublicChatMode() {
        return set_public_channel;
    }

    @Override
    public int getClientType() {
        return 0;
    }

    @Override
    public boolean isOnMobile() {
        return false;
    }

    @Override
    public boolean hadFocus() {
        return false;
    }

    @Override
    public int getMouseCrossColor() {
        return 0;
    }

    @Override
    public void setMouseCrossColor(int color) {

    }

    @Override
    public int getLeftClickOpensMenu() {
        return 0;
    }

    @Override
    public boolean getShowMouseOverText() {
        return false;
    }

    @Override
    public void setShowMouseOverText(boolean showMouseOverText) {

    }

    @Override
    public int[] getDefaultRotations() {
        return new int[0];
    }

    @Override
    public boolean getShowLoadingMessages() {
        return false;
    }

    @Override
    public void setShowLoadingMessages(boolean showLoadingMessages) {

    }

    @Override
    public void setStopTimeMs(long time) {

    }

    @Override
    public void clearLoginScreen(boolean shouldClear) {

    }

    @Override
    public void setLeftTitleSprite(SpritePixels background) {

    }

    @Override
    public void setRightTitleSprite(SpritePixels background) {

    }

    @Override
    public RSBuffer newBuffer(byte[] bytes) {
        return null;
    }

    @Override
    public RSVarbitComposition newVarbitDefinition() {
        return null;
    }

    @Override
    public boolean[] getPressedKeys() {
        return null;
    }

    public boolean lowMemoryMusic = false;

    @Override
    public void setLowMemory(boolean lowMemory) {

    }

    @Override
    public void setSceneLowMemory(boolean lowMemory) {
        Region.low_detail = lowMemory;
        SceneGraph.low_detail = lowMemory;
        ((TextureProvider) Rasterizer3D.clips.textureLoader).setTextureSize(low_detail ? 64 : 128);

    }

    @Override
    public void setAudioHighMemory(boolean highMemory) {
        lowMemoryMusic = highMemory;
    }

    @Override
    public void setObjectDefinitionLowDetail(boolean lowDetail) {
        ObjectDefinition.isLowDetail = lowDetail;
    }

    @Override
    public boolean isFriended(String name, boolean mustBeLoggedIn) {
        return false;
    }

    @Override
    public RSFriendsChat getFriendsChatManager() {
        return null;
    }

    @Override
    public RSLoginType getLoginType() {
        return null;
    }

    @Override
    public RSUsername createName(String name, RSLoginType type) {
        return null;
    }

    @Override
    public int rs$getVarbit(int varbitId) {
        return 0;
    }

    @Override
    public RSEvictingDualNodeHashTable getVarbitCache() {
        return null;
    }

    @Override
    public FriendContainer getFriendContainer() {
        return null;
    }

    @Override
    public NameableContainer<Ignore> getIgnoreContainer() {
        return null;
    }

    @Override
    public RSClientPreferences getPreferences() {
        return null;
    }

    @Override
    public int getCameraPitchTarget() {
        return camAngleX;
    }

    @Override
    public void setCameraPitchTarget(int pitch) {
        camAngleX = pitch;
    }

    @Override
    public void setPitchSin(int v) {
        scene.pitchSineY = v;
    }

    @Override
    public void setPitchCos(int v) {
        scene.pitchCosineY = v;
    }

    @Override
    public void setYawSin(int v) {
        scene.yawSineX = v;
    }

    @Override
    public void setYawCos(int v) {
        scene.yawCosineX = v;
    }

    static int lastPitch = 128;
    static int lastPitchTarget = 128;

    @Override
    public void setCameraPitchRelaxerEnabled(boolean enabled) {

        if (pitchRelaxEnabled == enabled) {
            return;
        }
        pitchRelaxEnabled = enabled;
        if (!enabled) {
            int pitch = getCameraPitchTarget();
            if (pitch > STANDARD_PITCH_MAX) {
                setCameraPitchTarget(STANDARD_PITCH_MAX);
            }
        }

    }

    @Override
    public void setInvertYaw(boolean state) {
        invertYaw = state;
    }

    @Override
    public void setInvertPitch(boolean state) {
        invertPitch = state;
    }

    @Override
    public RSWorldMap getRenderOverview() {
        return null;
    }

    private static boolean stretchedEnabled;

    private static boolean stretchedFast;

    private static boolean stretchedIntegerScaling;

    private static boolean stretchedKeepAspectRatio;

    private static double scalingFactor;

    private static Dimension cachedStretchedDimensions;

    private static Dimension cachedRealDimensions;

    @Override
    public boolean isStretchedEnabled() {
        return stretchedEnabled;
    }

    @Override
    public void setStretchedEnabled(boolean state) {
        stretchedEnabled = state;
    }

    @Override
    public boolean isStretchedFast() {
        return stretchedFast;
    }

    @Override
    public void setStretchedFast(boolean state) {
        stretchedFast = state;
    }

    @Override
    public void setStretchedIntegerScaling(boolean state) {
        stretchedIntegerScaling = state;
    }

    @Override
    public void setStretchedKeepAspectRatio(boolean state) {
        stretchedKeepAspectRatio = state;
    }

    @Override
    public void setScalingFactor(int factor) {
        scalingFactor = 1 + (factor / 100D);
    }

    @Override
    public double getScalingFactor() {
        return scalingFactor;
    }

    @Override
    public Dimension getRealDimensions() {
        if (!isStretchedEnabled()) {
            return getCanvas().getSize();
        }

        if (cachedRealDimensions == null) {
            if (isResized()) {
                Container canvasParent = getCanvas().getParent();

                int parentWidth = canvasParent.getWidth();
                int parentHeight = canvasParent.getHeight();

                int newWidth = (int) (parentWidth / scalingFactor);
                int newHeight = (int) (parentHeight / scalingFactor);

                if (newWidth < Constants.GAME_FIXED_WIDTH || newHeight < Constants.GAME_FIXED_HEIGHT) {
                    double scalingFactorW = (double) parentWidth / Constants.GAME_FIXED_WIDTH;
                    double scalingFactorH = (double) parentHeight / Constants.GAME_FIXED_HEIGHT;
                    double scalingFactor = Math.min(scalingFactorW, scalingFactorH);

                    newWidth = (int) (parentWidth / scalingFactor);
                    newHeight = (int) (parentHeight / scalingFactor);
                }

                cachedRealDimensions = new Dimension(newWidth, newHeight);
            } else {
                cachedRealDimensions = Constants.GAME_FIXED_SIZE;
            }
        }

        return cachedRealDimensions;
    }

    @Override
    public Dimension getStretchedDimensions() {
        if (cachedStretchedDimensions == null) {
            Container canvasParent = getCanvas().getParent();

            int parentWidth = canvasParent.getWidth();
            int parentHeight = canvasParent.getHeight();

            Dimension realDimensions = getRealDimensions();

            if (stretchedKeepAspectRatio) {
                double aspectRatio = realDimensions.getWidth() / realDimensions.getHeight();

                int tempNewWidth = (int) (parentHeight * aspectRatio);

                if (tempNewWidth > parentWidth) {
                    parentHeight = (int) (parentWidth / aspectRatio);
                } else {
                    parentWidth = tempNewWidth;
                }
            }

            if (stretchedIntegerScaling) {
                if (parentWidth > realDimensions.width) {
                    parentWidth = parentWidth - (parentWidth % realDimensions.width);
                }
                if (parentHeight > realDimensions.height) {
                    parentHeight = parentHeight - (parentHeight % realDimensions.height);
                }
            }

            cachedStretchedDimensions = new Dimension(parentWidth, parentHeight);
        }

        return cachedStretchedDimensions;
    }

    @Override
    public void invalidateStretching(boolean resize) {
        cachedRealDimensions = null;
        cachedStretchedDimensions = null;

        if (resize && isResized()) {
            /*
             * Tells the game to run resizeCanvas the next frame.
             * 
             * This is useful when resizeCanvas wouldn't usually run,
             * for example when we've only changed the scaling factor
             * and we still want the game's canvas to resize
             * with regards to the new maximum bounds.
             */
            setResizeCanvasNextFrame(true);
        }
    }

    @Override
    public void changeWorld(World world) {

    }

    @Override
    public RSWorld createWorld() {
        return null;
    }

    @Override
    public void setAnimOffsetX(int animOffsetX) {

    }

    @Override
    public void setAnimOffsetY(int animOffsetY) {

    }

    @Override
    public void setAnimOffsetZ(int animOffsetZ) {

    }

    @Override
    public RSSpritePixels drawInstanceMap(int z) {
        return null;
    }

    @Override
    public void setMinimapReceivesClicks(boolean minimapReceivesClicks) {

    }

    @Override
    public void runScript(Object... args) {

    }

    @Override
    public ScriptEvent createScriptEvent(Object... args) {
        return null;
    }

    @Override
    public boolean hasHintArrow() {
        return false;
    }

    @Override
    public HintArrowType getHintArrowType() {
        return null;
    }

    @Override
    public void clearHintArrow() {

    }

    @Override
    public void setHintArrow(WorldPoint point) {

    }

    @Override
    public void setHintArrow(net.runelite.api.Player player) {

    }

    @Override
    public void setHintArrow(NPC npc) {

    }

    @Override
    public WorldPoint getHintArrowPoint() {
        return null;
    }

    @Override
    public net.runelite.api.Player getHintArrowPlayer() {
        return null;
    }

    @Override
    public NPC getHintArrowNpc() {
        return null;
    }

    public boolean interpolatePlayerAnimations;
    public boolean interpolateNpcAnimations;
    public boolean interpolateObjectAnimations;
    public boolean interpolateWidgetAnimations;
    public boolean interpolateGraphicAnimations;

    @Override
    public boolean isInterpolatePlayerAnimations() {
        return interpolatePlayerAnimations;
    }

    @Override
    public void setInterpolatePlayerAnimations(boolean interpolate) {
        interpolatePlayerAnimations = interpolate;
    }

    @Override
    public boolean isInterpolateNpcAnimations() {
        return interpolateNpcAnimations;
    }

    @Override
    public void setInterpolateNpcAnimations(boolean interpolate) {
        interpolateNpcAnimations = interpolate;
    }

    @Override
    public boolean isInterpolateObjectAnimations() {
        return interpolateObjectAnimations;
    }

    @Override
    public void setInterpolateObjectAnimations(boolean interpolate) {
        interpolateObjectAnimations = interpolate;
    }

    @Override
    public boolean isInterpolateWidgetAnimations() {
        return interpolateWidgetAnimations;
    }

    @Override
    public void setInterpolateWidgetAnimations(boolean interpolate) {
        interpolateWidgetAnimations = interpolate;
    }

    @Override
    public boolean isInInstancedRegion() {
        return false; // TODO:
    }

    @Override
    public int getItemPressedDuration() {
        return 0;
    }

    @Override
    public void setItemPressedDuration(int duration) {

    }

    @Override
    public int getFlags() {
        return 0;
    }

    public static boolean isHidingEntities;
    public static boolean hideOthers;
    public static boolean hideOthers2D;
    public static boolean hideFriends;
    public static boolean hideClanMates;
    public static boolean hideClanChatMembers;
    public static boolean hideIgnores;
    public static boolean hideLocalPlayer;
    public static boolean hideLocalPlayer2D;
    public static boolean hideNPCs;
    public static boolean hideNPCs2D;
    public static boolean hidePets;
    public static boolean hideAttackers;
    public static boolean hideProjectiles;
    public static boolean hideDeadNPCs;
    public static List<String> hideSpecificPlayers = new ArrayList<>();
    public static HashMap<String, Integer> hiddenNpcsName = new HashMap<>();
    public static List<Integer> hiddenNpcIndices = new ArrayList<>();

    @Override
    public void setIsHidingEntities(boolean state) {
        isHidingEntities = state;
    }

    @Override
    public void setOthersHidden(boolean state) {
        hideOthers = state;
    }

    @Override
    public void setOthersHidden2D(boolean state) {
        hideOthers2D = state;
    }

    @Override
    public void setFriendsHidden(boolean state) {
        hideFriends = state;
    }

    @Override
    public void setFriendsChatMembersHidden(boolean state) {
        hideClanMates = state;
    }

    @Override
    public void setIgnoresHidden(boolean state) {
        hideIgnores = state;
    }

    @Override
    public void setLocalPlayerHidden(boolean state) {
        hideLocalPlayer = state;
    }

    @Override
    public void setLocalPlayerHidden2D(boolean state) {
        hideLocalPlayer2D = state;
    }

    @Override
    public void setNPCsHidden(boolean state) {
        hideNPCs = state;
    }

    @Override
    public void setNPCsHidden2D(boolean state) {
        hideNPCs2D = state;
    }

    @Override
    public void setHideSpecificPlayers(List<String> players) {
        hideSpecificPlayers = players;
    }

    @Override
    public void setHiddenNpcIndices(List<Integer> npcIndices) {
        hiddenNpcIndices = new ArrayList<>(npcIndices);
    }

    @Override
    public List<Integer> getHiddenNpcIndices() {
        return new ArrayList<>(hiddenNpcIndices);
    }

    @Override
    public void setPetsHidden(boolean state) {
        hidePets = state;
    }

    @Override
    public void setAttackersHidden(boolean state) {
        hideAttackers = state;
    }

    @Override
    public void setProjectilesHidden(boolean state) {
        hideProjectiles = state;
    }

    @Override
    public void setDeadNPCsHidden(boolean state) {
        hideDeadNPCs = state;
    }

    @Override
    public void addHiddenNpcName(String npc) {
        npc = npc.toLowerCase();
        int i = hiddenNpcsName.getOrDefault(npc, 0);
        if (i == Integer.MAX_VALUE) {
            throw new RuntimeException(
                    "NPC name " + npc + " has been hidden Integer.MAX_VALUE times, is something wrong?");
        }

        hiddenNpcsName.put(npc, ++i);
    }

    @Override
    public void removeHiddenNpcName(String npc) {
        npc = npc.toLowerCase();
        int i = hiddenNpcsName.getOrDefault(npc, 0);
        if (i == 0) {
            return;
        }

        hiddenNpcsName.put(npc, --i);
    }

    @Override
    public void setBlacklistDeadNpcs(Set<Integer> blacklist) {

    }

    public boolean addEntityMarker(int x, int y, RSRenderable entity) {
        final boolean shouldDraw = shouldDraw(entity, false);
        return shouldDraw;
    }

    public boolean shouldDraw(Object entity, boolean drawingUI) {

        if (!isHidingEntities) {
            return true;
        }

        if (entity instanceof RSRenderable) {
            if (((RSRenderable) entity).isHidden()) {
                return false;
            }
        }

        if (entity instanceof RSPlayer) {
            RSPlayer player = (RSPlayer) entity;
            RSPlayer local = Client.instance.getLocalPlayer();
            if (player.getName() == null) {
                return true;
            }

            if (player == local) {
                return drawingUI ? !hideLocalPlayer2D : !hideLocalPlayer;
            }

            for (String name : hideSpecificPlayers) {
                if (name != null && !name.equals("")) {
                    if (player.getName() != null && player.getName().equalsIgnoreCase(name)) {
                        return false;
                    }
                }
            }

            if (hideAttackers && player.getInteracting() == local) {
                return false;
            }

            if (player.isFriend()) {
                return !hideFriends;
            }

            if (player.isFriendsChatMember()) {
                return !hideClanMates;
            }

            if (player.isClanMember()) {
                return !hideClanChatMembers;
            }

            // if (Client.instance.getFriendManager().isIgnored(player.getRsName()))
            // {
            // return !hideIgnores;
            // }//

            return drawingUI ? !hideOthers2D : !hideOthers;
        } else if (entity instanceof RSNPC) {
            RSNPC npc = (RSNPC) entity;

            if (hiddenNpcIndices.contains(npc.getIndex())) {
                return false;
            }

            for (Map.Entry<String, Integer> entry : hiddenNpcsName.entrySet()) {
                String name = entry.getKey();
                int count = entry.getValue();
                if (name != null && !name.equals("")) {
                    if (count > 0 && npc.getName() != null && npc.getName().equalsIgnoreCase(name)) {
                        return false;
                    }
                }
            }

            if (npc.isDead() && hideDeadNPCs) {
                return false;
            }

            if (npc.getComposition().isFollower() && npc.getIndex() != getFollowerIndex() && hidePets) {
                return false;
            }

            if (npc.getInteracting() == getLocalPlayer() && hideAttackers) {
                return false;
            }

            return drawingUI ? !hideNPCs2D : !hideNPCs;
        } else if (entity instanceof RSProjectile) {
            return !hideProjectiles;
        }

        return true;
    }

    private static boolean invertPitch;
    private static boolean invertYaw;

    @Override
    public RSCollisionMap[] getCollisionMaps() {
        return collisionMaps;
    }

    @Override
    public int getPlayerIndexesCount() {
        return 0;
    }

    @Override
    public int[] getPlayerIndices() {
        return new int[0];
    }

    @Override
    public int[] getBoostedSkillLevels() {
        return boostedSkillLevels;
    }

    @Override
    public int[] getRealSkillLevels() {
        return realSkillLevels;
    }

    @Override
    public int[] getSkillExperiences() {
        return skillExperience;
    }

    @Override
    public int[] getChangedSkills() {
        return new int[0];
    }

    @Override
    public int getChangedSkillsCount() {
        return 0;
    }

    @Override
    public void setChangedSkillsCount(int i) {

    }

    @Override
    public void queueChangedSkill(Skill skill) {
    }

    @Override
    public Map<Integer, SpritePixels> getSpriteOverrides() {
        return null;
    }

    @Override
    public Map<Integer, SpritePixels> getWidgetSpriteOverrides() {
        return null;
    }

    @Override
    public void setCompass(SpritePixels SpritePixels) {

    }

    @Override
    public RSEvictingDualNodeHashTable getWidgetSpriteCache() {
        return null;
    }

    @Override
    public int getTickCount() {
        return tick;
    }

    @Override
    public void setTickCount(int tickCount) {
        tick = tickCount;
    }

    @Override
    public void setInventoryDragDelay(int delay) {
        setting.drag_item_value = delay;
    }

    @Override
    public boolean isHdMinimapEnabled() {
        return SceneGraph.hdMinimapEnabled;
    }

    @Override
    public void setHdMinimapEnabled(boolean enabled) {
        SceneGraph.hdMinimapEnabled = enabled;
    }

    @Override
    public EnumSet<WorldType> getWorldType() {
        return EnumSet.of(WorldType.MEMBERS);
    }

    @Override
    public int getOculusOrbState() {
        return oculusOrbState;
    }

    @Override
    public void setOculusOrbState(int state) {
        oculusOrbState = state;
    }

    @Override
    public void setOculusOrbNormalSpeed(int speed) {
        oculusOrbNormalSpeed = speed;
    }

    @Override
    public int getOculusOrbFocalPointX() {
        return oculusOrbFocalPointX;
    }

    @Override
    public int getOculusOrbFocalPointY() {
        return oculusOrbFocalPointY;
    }

    @Override
    public void setOculusOrbFocalPointX(int xPos) {
        oculusOrbFocalPointX = xPos;
    }

    @Override
    public void setOculusOrbFocalPointY(int yPos) {
        oculusOrbFocalPointY = yPos;
    }

    @Override
    public RSTileItem getLastItemDespawn() {
        return null;
    }

    @Override
    public void setLastItemDespawn(RSTileItem lastItemDespawn) {

    }

    @Override
    public void openWorldHopper() {

    }

    @Override
    public void hopToWorld(World world) {

    }

    @Override
    public void setSkyboxColor(int skyboxColor) {
        scene.skyboxColor = skyboxColor;
    }

    @Override
    public int getSkyboxColor() {
        return scene.skyboxColor;
    }

    @Override
    public boolean isGpu() {
        return gpu;
    }

    @Override
    public void setGpu(boolean gpu) {
        this.gpu = gpu;
    }

    @Override
    public int get3dZoom() {
        return Rasterizer3D.clips.viewportZoom;
    }

    @Override
    public int set3dZoom(int zoom) {
        Rasterizer3D.clips.viewportZoom = zoom;
        return zoom;
    }

    @Override
    public int getCenterX() {
        return getViewportWidth() / 2;
    }

    @Override
    public int getCenterY() {
        return getViewportHeight() / 2;
    }

    @Override
    public int getCameraX2() {
        return SceneGraph.xCameraPos;
    }

    @Override
    public int getCameraY2() {
        return SceneGraph.zCameraPos;
    }

    @Override
    public int getCameraZ2() {
        return SceneGraph.yCameraPos;
    }

    @Override
    public RSTextureProvider getTextureProvider() {
        return ((TextureProvider) Rasterizer3D.clips.textureLoader);
    }

    @Override
    public int[][] getOccupiedTilesTick() {
        return new int[0][];
    }

    @Override
    public RSEvictingDualNodeHashTable getObjectDefinitionModelsCache() {
        return null;
    }

    @Override
    public int getCycle() {
        return scene.cycle;
    }

    @Override
    public void setCycle(int cycle) {
        scene.cycle = cycle;
    }

    @Override
    public boolean[][][][] getVisibilityMaps() {
        return scene.visibilityMap;
    }

    @Override
    public RSEvictingDualNodeHashTable getCachedModels2() {
        return null;
    }

    @Override
    public void setRenderArea(boolean[][] renderArea) {
        scene.renderArea = renderArea;
    }

    @Override
    public void setCameraX2(int cameraX2) {
        scene.xCameraPos = cameraX2;
    }

    @Override
    public void setCameraY2(int cameraY2) {
        scene.zCameraPos = cameraY2;
    }

    @Override
    public void setCameraZ2(int cameraZ2) {
        scene.yCameraPos = cameraZ2;
    }

    @Override
    public void setScreenCenterX(int screenCenterX) {
        scene.screenCenterX = screenCenterX;
    }

    @Override
    public void setScreenCenterZ(int screenCenterZ) {
        scene.screenCenterZ = screenCenterZ;
    }

    @Override
    public void setScenePlane(int scenePlane) {
        scene.currentRenderPlane = scenePlane;
    }

    @Override
    public void setMinTileX(int i) {
        scene.minTileX = i;
    }

    @Override
    public void setMinTileZ(int i) {
        scene.minTileZ = i;
    }

    @Override
    public void setMaxTileX(int i) {
        scene.maxTileX = i;
    }

    @Override
    public void setMaxTileZ(int i) {
        scene.maxTileZ = i;
    }

    @Override
    public int getTileUpdateCount() {
        return scene.tileUpdateCount;
    }

    @Override
    public void setTileUpdateCount(int tileUpdateCount) {
        scene.tileUpdateCount = tileUpdateCount;
    }

    @Override
    public boolean getViewportContainsMouse() {
        return false;
    }

    @Override
    public int getRasterizer3D_clipMidX2() {
        return Clips.getClipMidX2();
    }

    @Override
    public int getRasterizer3D_clipNegativeMidX() {
        return Rasterizer3D.clips.clipNegativeMidX;
    }

    @Override
    public int getRasterizer3D_clipNegativeMidY() {
        return Rasterizer3D.clips.clipNegativeMidY;
    }

    @Override
    public int getRasterizer3D_clipMidY2() {
        return Clips.getClipMidY2();
    }

    @Override
    public void checkClickbox(net.runelite.api.Model model, int orientation, int pitchSin, int pitchCos, int yawSin,
            int yawCos, int x, int y, int z, long hash) {

    }

    @Override
    public RSWidget getIf1DraggedWidget() {
        return null;
    }

    @Override
    public int getIf1DraggedItemIndex() {
        return 0;
    }

    @Override
    public void setSpellSelected(boolean selected) {

    }

    @Override
    public RSEnumComposition getRsEnum(int id) {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getItemCompositionCache() {
        return null;
    }

    @Override
    public RSSpritePixels[] getCrossSprites() {
        return null;
    }

    @Override
    public EnumComposition getEnum(int id) {
        return null;
    }

    @Override
    public int[] getGraphicsPixels() {
        return Rasterizer2D.pixels;
    }

    @Override
    public int getGraphicsPixelsWidth() {
        return Rasterizer2D.width;
    }

    @Override
    public int getGraphicsPixelsHeight() {
        return Rasterizer2D.height;
    }

    @Override
    public void rasterizerFillRectangle(int x, int y, int w, int h, int rgb) {
        Rasterizer2D.draw_filled_rect(x, y, w, h, rgb);
    }

    @Override
    public int getStartX() {
        return Rasterizer2D.xClipStart;
    }

    @Override
    public int getStartY() {
        return Rasterizer2D.yClipStart;
    }

    @Override
    public int getEndX() {
        return Rasterizer2D.xClipEnd;
    }

    @Override
    public int getEndY() {
        return Rasterizer2D.yClipEnd;
    }

    @Override
    public void drawOriginalMenu(int alpha) {
        int x = getMenuX();
        int y = getMenuY();
        int w = getMenuWidth();
        int h = getMenuHeight();
        rasterizerFillRectangleAlpha(x, y, w, h, ORIGINAL_BG, alpha);
        rasterizerDrawRectangleAlpha(x, y, w, h, ORIGINAL_BG, alpha);
        rasterizerFillRectangleAlpha(x + 1, y + 1, w - 2, 16, 0, alpha);
        rasterizerDrawRectangleAlpha(x + 1, y + 18, w - 2, h - 19, 0, alpha);
        boldFont.draw("Choose Option", x + 3, y + 14, ORIGINAL_BG, -1);

        int mouseX = getMouseX();
        int mouseY = getMouseY();

        int count = getMenuOptionCount();

        for (int i = 0; i < count; i++) {
            int rowY = y + (count - 1 - i) * 15 + 31;

            int highlight = 0xFFFFFF;
            if (mouseX > x && mouseX < w + x && mouseY > rowY - 13 && mouseY < rowY + 3) {
                highlight = 0xFFFF00;
            }

            boldFont.draw(menuActionText[i].toString(), x + 3, rowY, highlight, 0);
        }
    }

    @Override
    public void resetHealthBarCaches() {

    }

    @Override
    public boolean getRenderSelf() {
        return false;
    }

    @Override
    public void setRenderSelf(boolean enabled) {

    }

    @Override
    public void invokeMenuAction(String option, String target, int identifier, int opcode, int param0, int param1) {

    }

    @Override
    public RSMouseRecorder getMouseRecorder() {
        return null;
    }

    @Override
    public void setPrintMenuActions(boolean b) {

    }

    @Override
    public String getSelectedSpellName() {
        return null;
    }

    @Override
    public void setSelectedSpellName(String name) {

    }

    @Override
    public boolean getSpellSelected() {
        return false;
    }

    @Override
    public RSSoundEffect getTrack(RSAbstractArchive indexData, int id, int var0) {
        return null;
    }

    @Override
    public RSRawPcmStream createRawPcmStream(RSRawSound audioNode, int var0, int volume) {
        return null;
    }

    @Override
    public RSPcmStreamMixer getSoundEffectAudioQueue() {
        return null;
    }

    @Override
    public RSArchive getIndexCache4() {
        return Js5List.soundEffects;
    }

    @Override
    public RSDecimator getSoundEffectResampler() {
        return null;
    }

    @Override
    public void setMusicTrackVolume(int volume) {

    }

    @Override
    public void setViewportWalking(boolean viewportWalking) {

    }

    @Override
    public void playMusicTrack(int var0, RSAbstractArchive var1, int var2, int var3, int var4, boolean var5) {

    }

    @Override
    public RSMidiPcmStream getMidiPcmStream() {
        return null;
    }

    @Override
    public int getCurrentTrackGroupId() {
        return 0;
    }

    @Override
    public String getSelectedSpellActionName() {
        return null;
    }

    @Override
    public int getSelectedSpellFlags() {
        return 0;
    }

    @Override
    public void setHideFriendAttackOptions(boolean yes) {

    }

    @Override
    public void setHideFriendCastOptions(boolean yes) {

    }

    @Override
    public void setHideClanmateAttackOptions(boolean yes) {
    }

    @Override
    public void setHideClanmateCastOptions(boolean yes) {

    }

    @Override
    public void setUnhiddenCasts(Set<String> casts) {

    }

    @Override
    public void setModulus(BigInteger modulus) {

    }

    @Override
    public BigInteger getModulus() {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void setAllWidgetsAreOpTargetable(boolean value) {

    }

    @Override
    public void insertMenuItem(String action, String target, int opcode, int identifier, int argument1, int argument2,
            boolean forceLeftClick) {

    }

    @Override
    public void setSelectedItemID(int id) {

    }

    @Override
    public int getSelectedItemWidget() {
        return 0;
    }

    @Override
    public void setSelectedItemWidget(int widgetID) {

    }

    @Override
    public int getSelectedItemSlot() {
        return 0;
    }

    @Override
    public void setSelectedItemSlot(int idx) {

    }

    @Override
    public int getSelectedSpellWidget() {
        return 0;
    }

    @Override
    public int getSelectedSpellChildIndex() {
        return 0;
    }

    @Override
    public void setSelectedSpellWidget(int widgetID) {

    }

    @Override
    public void setSelectedSpellChildIndex(int index) {

    }

    @Override
    public void scaleSprite(int[] canvas, int[] pixels, int color, int pixelX, int pixelY, int canvasIdx,
            int canvasOffset, int newWidth, int newHeight, int pixelWidth, int pixelHeight, int oldWidth) {

    }

    @Override
    public void promptCredentials(boolean clearPass) {

    }

    @Override
    public RSVarpDefinition getVarpDefinition(int id) {
        return null;
    }

    @Override
    public RSTileItem newTileItem() {
        return null;
    }

    @Override
    public RSNodeDeque newNodeDeque() {
        return null;
    }

    @Override
    public void updateItemPile(int localX, int localY) {

    }

    @Override
    public void setHideDisconnect(boolean dontShow) {

    }

    @Override
    public void setTempMenuEntry(MenuEntry entry) {

    }

    @Override
    public void setShowMouseCross(boolean show) {

    }

    @Override
    public int getDraggedWidgetX() {
        return 0;
    }

    @Override
    public int getDraggedWidgetY() {
        return 0;
    }

    @Override
    public int[] getChangedSkillLevels() {
        return new int[0];
    }

    @Override
    public void setMouseIdleTicks(int cycles) {
        MouseHandler.idleCycles = cycles;
    }

    @Override
    public void setKeyboardIdleTicks(int cycles) {
        KeyHandler.idleCycles = cycles;
    }

    @Override
    public void setGeSearchResultCount(int count) {
    }

    @Override
    public void setGeSearchResultIds(short[] ids) {

    }

    @Override
    public void setGeSearchResultIndex(int index) {

    }

    @Override
    public void setComplianceValue(String key, boolean value) {

    }

    @Override
    public boolean getComplianceValue(String key) {
        return false;
    }

    @Override
    public boolean isMirrored() {
        return false;
    }

    @Override
    public void setMirrored(boolean isMirrored) {

    }

    @Override
    public boolean isComparingAppearance() {
        return false;
    }

    @Override
    public void setComparingAppearance(boolean comparingAppearance) {

    }

    @Override
    public void setLoginScreen(SpritePixels pixels) {

    }

    @Override
    public void setShouldRenderLoginScreenFire(boolean val) {

    }

    @Override
    public boolean shouldRenderLoginScreenFire() {
        return false;
    }

    @Override
    public boolean isKeyPressed(int keycode) {
        return false;
    }

    @Override
    public int getFollowerIndex() {
        return npcPetId;
    }

    @Override
    public int isItemSelected() {
        return 0;
    }

    @Override
    public String getSelectedItemName() {
        return null;
    }

    @Override
    public RSWidget getMessageContinueWidget() {
        return null;
    }

    @Override
    public void setMusicPlayerStatus(int var0) {

    }

    @Override
    public void setMusicTrackArchive(RSAbstractArchive var0) {

    }

    @Override
    public void setMusicTrackGroupId(int var0) {

    }

    @Override
    public void setMusicTrackFileId(int var0) {

    }

    @Override
    public void setMusicTrackBoolean(boolean var0) {

    }

    @Override
    public void setPcmSampleLength(int var0) {

    }

    @Override
    public int[] getChangedVarps() {
        return new int[0];
    }

    @Override
    public int getChangedVarpCount() {
        return 0;
    }

    @Override
    public void setChangedVarpCount(int changedVarpCount) {

    }

    @Override
    public void setOutdatedScript(String outdatedScript) {

    }

    @Override
    public List<String> getOutdatedScripts() {
        return null;
    }

    @Override
    public RSFrames getFrames(int frameId) {
        return null;
    }

    @Override
    public RSSpritePixels getMinimapSprite() {
        return minimapImage;
    }

    @Override
    public void setMinimapSprite(SpritePixels spritePixels) {

    }

    @Override
    public void drawObject(int z, int x, int y, int randomColor1, int randomColor2) {

    }

    @Override
    public RSScriptEvent createScriptEvent() {
        return null;
    }

    @Override
    public void runScript(RSScriptEvent ev, int ex, int var2) {

    }

    @Override
    public void setHintArrowTargetType(int value) {
        this.hintIconDrawType = value;
    }

    @Override
    public int getHintArrowTargetType() {
        return hintIconDrawType;
    }

    @Override
    public void setHintArrowX(int value) {
        this.hintIconX = value;
    }

    @Override
    public int getHintArrowX() {
        return this.hintIconX;
    }

    @Override
    public void setHintArrowY(int value) {
        this.hintIconY = value;
    }

    @Override
    public int getHintArrowY() {
        return this.hintIconY;
    }

    @Override
    public void setHintArrowOffsetX(int value) {
        this.hintIconX += value;
    }

    @Override
    public void setHintArrowOffsetY(int value) {
        this.hintIconY += value;
    }

    @Override
    public void setHintArrowNpcTargetIdx(int value) {
        this.hintIconNpcId = value;
    }

    @Override
    public int getHintArrowNpcTargetIdx() {
        return hintIconNpcId;
    }

    @Override
    public void setHintArrowPlayerTargetIdx(int value) {
        this.hintIconPlayerId = value;
    }

    @Override
    public int getHintArrowPlayerTargetIdx() {
        return hintIconPlayerId;
    }

    @Override
    public RSSequenceDefinition getSequenceDefinition(int id) {
        return null;
    }

    @Override
    public RSIntegerNode newIntegerNode(int contents) {
        return null;
    }

    @Override
    public RSObjectNode newObjectNode(Object contents) {
        return null;
    }

    @Override
    public RSIterableNodeHashTable newIterableNodeHashTable(int size) {
        return null;
    }

    @Override
    public RSVarbitComposition getVarbitComposition(int id) {
        return null;
    }

    @Override
    public RSAbstractArchive getSequenceDefinition_skeletonsArchive() {
        return null;
    }

    @Override
    public RSAbstractArchive getSequenceDefinition_archive() {
        return null;
    }

    @Override
    public RSAbstractArchive getSequenceDefinition_animationsArchive() {
        return null;
    }

    @Override
    public RSAbstractArchive getNpcDefinition_archive() {
        return null;
    }

    @Override
    public RSAbstractArchive getObjectDefinition_modelsArchive() {
        return null;
    }

    @Override
    public RSAbstractArchive getObjectDefinition_archive() {
        return null;
    }

    @Override
    public RSAbstractArchive getItemDefinition_archive() {
        return null;
    }

    @Override
    public RSAbstractArchive getKitDefinition_archive() {
        return null;
    }

    @Override
    public RSAbstractArchive getKitDefinition_modelsArchive() {
        return null;
    }

    @Override
    public RSAbstractArchive getSpotAnimationDefinition_archive() {
        return null;
    }

    @Override
    public RSAbstractArchive getSpotAnimationDefinition_modelArchive() {
        return null;
    }

    @Override
    public RSBuffer createBuffer(byte[] initialBytes) {
        return null;
    }

    @Override
    public RSSceneTilePaint createSceneTilePaint(int swColor, int seColor, int neColor, int nwColor, int texture,
            int rgb, boolean isFlat) {
        return null;
    }

    @Override
    public long[] getCrossWorldMessageIds() {
        return null;
    }

    @Override
    public int getCrossWorldMessageIdsIndex() {
        return 0;
    }

    @Override
    public RSClanChannel[] getCurrentClanChannels() {
        return new RSClanChannel[0];
    }

    @Override
    public RSClanSettings[] getCurrentClanSettingsAry() {
        return new RSClanSettings[0];
    }

    @Override
    public RSClanChannel getClanChannel() {
        return null;
    }

    @Override
    public RSClanChannel getGuestClanChannel() {
        return null;
    }

    @Override
    public RSClanSettings getClanSettings() {
        return null;
    }

    @Override
    public RSClanSettings getGuestClanSettings() {
        return null;
    }

    @Override
    public ClanRank getClanRankFromRs(int rank) {
        return null;
    }

    @Override
    public RSIterableNodeHashTable readStringIntParameters(RSBuffer buffer, RSIterableNodeHashTable table) {
        return null;
    }

    @Override
    public int getRndHue() {
        return 0;
    }

    @Override
    public short[][][] getTileUnderlays() {
        return scene.getUnderlayIds();
    }

    @Override
    public short[][][] getTileOverlays() {
        return scene.getOverlayIds();
    }

    @Override
    public byte[][][] getTileShapes() {
        return scene.getTileShapes();
    }

    @Override
    public RSSpotAnimationDefinition getSpotAnimationDefinition(int id) {
        return null;
    }

    @Override
    public RSModelData getModelData(RSAbstractArchive var0, int var1, int var2) {
        return null;
    }

    @Override
    public boolean isCameraLocked() {
        return false;
    }

    @Override
    public boolean getCameraPitchRelaxerEnabled() {
        return pitchRelaxEnabled;
    }

    public static boolean unlockedFps;
    public long delayNanoTime;
    public long lastNanoTime;
    public static double tmpCamAngleY;
    public static double tmpCamAngleX;

    @Override
    public boolean isUnlockedFps() {
        return unlockedFps;
    }

    @Override
    public long getUnlockedFpsTarget() {
        return delayNanoTime;
    }

    public void updateCamera() {
        if (unlockedFps) {
            long nanoTime = System.nanoTime();
            long diff = nanoTime - this.lastNanoTime;
            this.lastNanoTime = nanoTime;

            if (this.getGameState() == GameState.LOGGED_IN) {
                this.interpolateCamera(diff);
            }
        }
    }

    public static final int STANDARD_PITCH_MIN = 128;
    public static final int STANDARD_PITCH_MAX = 383;
    public static final int NEW_PITCH_MAX = 512;

    public void interpolateCamera(long var1) {
        double angleDX = diffToDangle(getCamAngleDY(), var1);
        double angleDY = diffToDangle(getCamAngleDX(), var1);

        tmpCamAngleY += angleDX / 2;
        tmpCamAngleX += angleDY / 2;
        tmpCamAngleX = Doubles.constrainToRange(tmpCamAngleX, Perspective.UNIT * STANDARD_PITCH_MIN,
                getCameraPitchRelaxerEnabled() ? Perspective.UNIT * NEW_PITCH_MAX
                        : Perspective.UNIT * STANDARD_PITCH_MAX);

        int yaw = toCameraPos(tmpCamAngleY);
        int pitch = toCameraPos(tmpCamAngleX);

        setCameraYawTarget(yaw);
        setCameraPitchTarget(pitch);
    }

    public static int toCameraPos(double var0) {
        return (int) (var0 / Perspective.UNIT) & 2047;
    }

    public static double diffToDangle(int var0, long var1) {
        double var2 = var0 * Perspective.UNIT;
        double var3 = (double) var1 / 2.0E7D;

        return var2 * var3;
    }

    @Override
    public void posToCameraAngle(int var0, int var1) {
        tmpCamAngleY = var0 * Perspective.UNIT;
        tmpCamAngleX = var1 * Perspective.UNIT;
    }

    public static void onCameraPitchTargetChanged(int idx) {
        int newPitch = instance.getCameraPitchTarget();
        int pitch = newPitch;
        if (pitchRelaxEnabled) {
            // This works because the vanilla camera movement code only moves %2
            if (lastPitchTarget > STANDARD_PITCH_MAX && newPitch == STANDARD_PITCH_MAX) {
                pitch = lastPitchTarget;
                if (pitch > NEW_PITCH_MAX) {
                    pitch = NEW_PITCH_MAX;
                }
                instance.setCameraPitchTarget(pitch);
            }
        }
        lastPitchTarget = pitch;
    }

    public void onCamAngleDXChange() {
        if (invertPitch && getMouseCurrentButton() == 4 && isMouseCam()) {
            setCamAngleDX(-getCamAngleDX());
        }
    }

    public void onCamAngleDYChange() {
        if (invertYaw && getMouseCurrentButton() == 4 && isMouseCam()) {
            setCamAngleDY(-getCamAngleDY());
        }
    }

    public static void onCameraPitchChanged(int idx) {
        int newPitch = instance.getCameraPitch();
        int pitch = newPitch;
        if (pitchRelaxEnabled) {
            // This works because the vanilla camera movement code only moves %2
            if (lastPitch > STANDARD_PITCH_MAX && newPitch == STANDARD_PITCH_MAX) {
                pitch = lastPitch;
                if (pitch > NEW_PITCH_MAX) {
                    pitch = NEW_PITCH_MAX;
                }
                instance.setCameraPitch(pitch);
            }
        }
        lastPitch = pitch;
    }

    @Override
    public RSClanChannel getClanChannel(int clanId) {
        return null;
    }

    @Override
    public RSClanSettings getClanSettings(int clanId) {
        return null;
    }

    @Override
    public void setUnlockedFps(boolean unlock) {
        unlockedFps = unlock;

        if (unlock) {
            posToCameraAngle(getMapAngle(), getCameraPitch());
        } else {
            delayNanoTime = 0L;
        }
    }

    @Override
    public void setUnlockedFpsTarget(int fps) {
        if (fps <= 0) {
            delayNanoTime = 0L;
        } else {
            delayNanoTime = 1000000000L / (long) fps;
        }
    }

    @Override
    public net.runelite.api.Deque<AmbientSoundEffect> getAmbientSoundEffects() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getEnumDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getFloorUnderlayDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getFloorOverlayDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getHitSplatDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getHitSplatDefinitionSpritesCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getHitSplatDefinitionDontsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getInvDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getItemDefinitionModelsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getItemDefinitionSpritesCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getKitDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getNpcDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getNpcDefinitionModelsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getObjectDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getObjectDefinitionModelDataCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getObjectDefinitionEntitiesCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getParamDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getPlayerAppearanceModelsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSequenceDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSequenceDefinitionFramesCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSequenceDefinitionModelsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSpotAnimationDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSpotAnimationDefinitionModlesCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getVarcIntCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getVarpDefinitionCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getModelsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getFontsCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSpriteMasksCache() {
        return null;
    }

    @Override
    public RSEvictingDualNodeHashTable getSpritesCache() {
        return null;
    }

    @Override
    public RSIterableNodeHashTable createIterableNodeHashTable(int size) {
        return null;
    }

    @Override
    public int getSceneMaxPlane() {
        return 0;
    }

    @Override
    public void setIdleTimeout(int id) {

    }

    @Override
    public int getIdleTimeout() {
        return 0;
    }

    boolean minimapZoomActive = false;

    @Override
    public void setMinimapZoom(boolean minimapZoom) {
        minimapZoomActive = minimapZoom;
    }

    @Override
    public double getMinimapZoom() {
        return minimapZoom;
    }

    @Override
    public boolean isMinimapZoom() {
        return minimapZoomActive;
    }

    @Override
    public void setMinimapZoom(double zoom) {
        minimapZoom = (int) zoom;
    }

    CinematicState cinematicState = CinematicState.UNKNOWN;

    @Override
    public CinematicState getCinematicState() {
        return cinematicState;
    }

    public static long accountHash = 0L;

    @Override
    public long getAccountHash() {
        return accountHash;
    }

    @Override
    public void setCinematicState(CinematicState gameState) {
        cinematicState = gameState;
        GameStateChanged event = new GameStateChanged();
        event.setGameState(GameState.UNKNOWN);
        if (gameState == CinematicState.ACTIVE) {
            event.setGameState(GameState.LOGGED_IN);
        } else if (gameState == CinematicState.UNKNOWN) {
            event.setGameState(GameState.LOGIN_SCREEN);
        } else if (gameState == CinematicState.LOADING) {
            event.setGameState(GameState.LOADING);
        }

        callbacks.post(event);
    }

}
