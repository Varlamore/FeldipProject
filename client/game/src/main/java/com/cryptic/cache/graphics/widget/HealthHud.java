package com.cryptic.cache.graphics.widget;

import com.cryptic.Client;

/**
 * @author Sharky
 * @Since May 23, 2023
 */
public class HealthHud extends InterfaceBuilder {

    public static final int WIDGET_ID = 19_000;

    public static final int VARP_TYPE = 1312;

    public static final int VARP_HEALTH = 1313;

    public static int HEADER_ID = 19003;

    public static int PROGRESS_WIDGET_ID;

    public HealthHud() {
        super(WIDGET_ID);
    }

    @Override
    public void build() {
        int x = 128 + 20;
        int y = 25;
        int width = 250 - 40;
        int height = 46;
        addBox(nextInterface(), width, height, 1, 0, 0, 250);
        child(x, y);
        Widget.addRectangle(nextInterface(), width - 1, height - 3, 0x3d3327, 0, true);
        child(x + 1, y + 1);
        Widget.addText(nextInterface(), 0, 0xff9933, true, "?");
        child(width / 2 + x, y + 5);

        Widget progress = Widget.addProgressBar(nextInterface(), width - 5, 20, 2221, 10400);
        progress.text_type = Client.smallFont;
        progress.fillColor = 0x00FFFF;
        child(x + 6, y + 20);

        PROGRESS_WIDGET_ID = progress.id;
    }

    public enum HudType {
        REGULAR(0x00ff00, 0xff0000, 255),
        ORANGE_SHIELD(0xff8a00, 0, 150),
        CYAN_SHIELD(0x00FFFF, 0, 150);


        public int getMainColor() {
            return mainColor;
        }

        public int getBackColor() {
            return backColor;
        }

        public int getBackAlpha() {
            return backAlpha;
        }

        private final int mainColor;
        private final int backColor;
        private final int backAlpha;

        HudType(int mainColor, int backColor, int backAlpha) {
            this.mainColor = mainColor;
            this.backColor = backColor;
            this.backAlpha = backAlpha;
        }
    }

    public static void onVarpChange(int id, int value) {
        if (id == VARP_HEALTH) {
            int currentHealth = value & 0xFFFF;
            int maxHealth = value >> 16 & 0xFFFF;

            Widget progress = Widget.cache[PROGRESS_WIDGET_ID];
            progress.defaultText = currentHealth + "/" + maxHealth;
        } else if (id == VARP_TYPE) {
            setHudType(HudType.values()[value]);
        }
    }

    public static void setHudType(HudType type) {
        Widget progress = Widget.cache[PROGRESS_WIDGET_ID];
        progress.fillColor = type.getMainColor();
        progress.progressBackColor = type.getBackColor();
        progress.progressBackAlpha = type.getBackAlpha();
    }

    public static HudType getHudType() {
        return Widget.cache[PROGRESS_WIDGET_ID].hudType;
    }

}
