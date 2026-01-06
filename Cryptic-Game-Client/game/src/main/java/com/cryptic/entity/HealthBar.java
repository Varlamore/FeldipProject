package com.cryptic.entity;

/**
 * An enumerated type that represents a health bar above an entity.
 * @author Sharky
 * @Since May 23, 2023
 */
public enum HealthBar {

    DEFAULT(65280, 0xff0000),

    /**
     * Zalcano.
     */
    ORANGE(0xe25505, 0x491c00),

    /**
     * Verzik, nightmare.
     */
    CYAN(0x46eae4, 0x032620),

    /**
     * Raid puzzle npcs.
     */
    BLUE_GREEN(0x00a9ff, 65280),

    /**
     * Nightmare totems.
     */
    YELLOW(0xdbd300, 0x2d2b00);


    public int getMainColor() {
        return mainColor;
    }

    public int getBackColor() {
        return backColor;
    }

    private int mainColor;

    private int backColor;

    HealthBar(int mainColor, int backColor) {
        this.mainColor = mainColor;
        this.backColor = backColor;
    }

    public static final int DIM_30 = 30;
    public static final int DIM_40 = 40;
    public static final int DIM_50 = 50;
    public static final int DIM_60 = 60;
    public static final int DIM_80 = 80;
    public static final int DIM_100 = 100;
    public static final int DIM_120 = 120;
    public static final int DIM_140 = 140;
    public static final int DIM_160 = 160;

}
