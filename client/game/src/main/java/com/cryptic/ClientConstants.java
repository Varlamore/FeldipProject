package com.cryptic;

import com.cryptic.sign.SignLink;

import java.io.File;

/**
 * The main configuration for the Client
 *
 * @author Seven
 */
public final class ClientConstants {

    public static int soundEffectVolume = 127;
    public static int areaSoundEffectVolume = 127;
    public static int musicVolume = 127;
    public static boolean disableLoginScreenMusic = true;

    /**
     * Toggles zBuffering
     */
    public static boolean ZBUFF = false;

    public static boolean mouseCam = false;

    /*
     * This determines if we are going to connect to the live server or local
     * server.
     */
    public static final boolean isLive = false;
    public static final String SERVICES_IP = isLive ? "15.204.229.237" : "127.0.0.1";
    public static final String SERVER_ADDRESS = isLive ? "15.204.229.237" : "localhost";
    public static int SERVER_PORT = 43597;
    public static int SERVICES_PORT = 43594;

    /**
     * Used to repack indexes Index 1 = Models Index 2 = Animations Index 3 =
     * Sounds/Music Index 4 = Maps
     */
    public static boolean repackIndexOne = false, repackIndexTwo = false, repackIndexThree = false,
            repackIndexFour = false;

    public static boolean HALLOWEEN = false;
    public static boolean WINTER = false;
    public static boolean CHRISTMAS = false;

    public static final String CLIENT_VERSION = "6";
    public static final int OSRS_DATA_VERSION = 217;
    public static final int OSRS_DATA_SUB_VERSION = 2;

    public static final boolean DEBUG_MODE = true;

    public static final boolean DEBUG_INTERFACES = true;

    public static final boolean PVP_GAME_MODE_ENABLED = false;

    /**
     * Used for change world button on login screen
     */
    public static boolean CAN_SWITCH_WORLD = false;

    /**
     * Used for toggle music button on login screen
     */
    public static boolean CAN_SWITCH_MUSIC = true;

    /**
     * Used to toggle music for login screen at client level,
     * overriding the user's choice.
     */
    public static boolean LOGIN_MUSIC_ENABLED = true;

    /**
     * To enable restarting the login song on toggling the login song,
     * set this to true.
     */
    public static boolean ENABLE_RESTARTING_LOGIN_SONG = true;

    public static final String SPRITE_FILE_NAME = "main_file_sprites";

    // Set this to true to check for unused interface ids, this slows down client
    // loading.
    public static final boolean CHECK_UNUSED_INTERFACES = false;

    // Set this to true to check for duplicate interfaces ids, this might slow down
    // client loading.
    public static final boolean CHECK_DUPLICATE_INTERFACES_IDS = true;

    // Set this to true to display the verbose client load time (the loading time at
    // each step).
    public static final boolean DISPLAY_CLIENT_LOAD_TIME_VERBOSE = true;

    // Set this to true to display the simple client load time (the total time it
    // takes to load the client).
    public static final boolean DISPLAY_CLIENT_LOAD_TIME = true;

    public static final boolean RASTERIZER3D_LOW_MEMORY = false; // This was true.

    public static final boolean MAPREGION_LOW_MEMORY = false; // This was true.

    public static final boolean SCENEGRAPH_LOW_MEMORY = false; // This was true.

    public static final boolean CLIENT_LOW_MEMORY = false; // This was false.

    public static final boolean OBJECT_DEFINITION_LOW_MEMORY = false;

    public final static int YELLOW = 0xffff01;

    public final static int BLUE = 0x005aff;

    public final static int GOLD = 0xffc600;

    public final static int SKILL_TAB_YELLOW = 0xf2eb5e;

    public final static int WHITE = 0xffffff;

    public final static int ORANGE = 0xff981f;

    public final static int BLACK = 0x000000;

    public final static int PALE_ORANGE = 0xc8aa64;

    public final static int RED = 0xfe3200;

    public final static int BURGUNDY = 0x800000;

    public final static int DARK_BLUE = 0x000080;

    public final static int GREEN = 0x09FF00;

    public final static int PALE_GREEN = 0x46b556;

    // We could always add the version here for the production cache folder if we
    // wanted to.
    public static final String CACHE_NAME = ".cryptic/";
    public static final String DATA_NAME = ".cryptic-data/";

    public static final File CACHE_DIR = new File(System.getProperty("user.home"), ".cryptic");

    public static boolean JAGCACHED_ENABLED = false;

    /**
     * Toggles a security feature called RSA to prevent packet sniffers
     */
    public static final boolean ENABLE_RSA = true;

    /**
     * A string which indicates the Client's name.
     */
    public static final String CLIENT_NAME = "Cryptic";

    /**
     * Dumps map region images when new regions are loaded.
     */
    public static boolean DUMP_MAP_REGIONS = false;

    /**
     * A constant which is used for easy repacking data
     */
    public static boolean LOAD_OSRS_DATA_FROM_CACHE_DIR = true;

    /**
     * Displays debug messages on loginscreen and in-game
     */
    public static boolean CLIENT_DATA = false;

    public static boolean FORCE_OVERLAY_ABOVE_WIDGETS = false;

    public static boolean SHIFT_CLICK_TELEPORT = true;

    public static boolean RIGHT_CLICK_AUTOCAST = false;

    /**
     * npcBits can be changed to what your server's bits are set to.
     */
    public static final int NPC_BITS = 14;
    public static final int SHOW_MINIMAP = 0;
    public static final boolean OSRS_DATA = true;
    public final static int NO_EFFECT = 0;
    public final static int WAVE = 1;
    public final static int WAVE_2 = 2;
    public final static int SHAKE = 3;
    public final static int SCROLL = 4;
    public final static int SLIDE = 5;

}
