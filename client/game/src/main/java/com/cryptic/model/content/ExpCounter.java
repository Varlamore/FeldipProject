package com.cryptic.model.content;

import com.cryptic.Client;
import com.cryptic.cache.graphics.SimpleImage;
import com.cryptic.cache.graphics.SpriteCache;
import com.cryptic.cache.graphics.font.AdvancedFont;

import java.text.NumberFormat;
import java.util.*;

/**
 * @author Patrick van Elderen | November, 15, 2020, 12:49
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ExpCounter {

    private static final int START_SPRITE = 82;
    private static final int START = 130;
    private static final int STOP = 35;
    private static final int MIDLINE = (START + STOP) / 2;
    public static int xpCounter;
    public static final ArrayList<ExpGain> GAINS = new ArrayList<>();
    private static ExpGain currentGain = null;

    public static void addXP(int skill, int xp, boolean increment) {
        if (skill == 99) {
            xpCounter = xp;
        } else {
            if (increment) {
                xpCounter += xp;
            }
            if (xp != 0) {
                if (currentGain != null && Math.abs(currentGain.getY() - START) <= getSize(Client.instance.setting.counter_size).ascent) {
                    currentGain.xp += xp;
                    currentGain.addSprite(skill);
                } else {
                    ExpGain gain = new ExpGain(skill, xp);
                    GAINS.add(gain);
                    currentGain = gain;
                }
            }
        }
    }

    public static void addXP(int skillID, int xp) {
            if (xp != 0) {
                GAINS.add(new ExpGain(skillID, xp));
            }
        }

    public static void drawExperienceCounter() {
        boolean isFixed = !Client.instance.isResized();

        int x = Client.canvasWidth - 80 - 255, y = 12;
        int boxWidth = Objects.requireNonNull(SpriteCache.get(521)).width;
        int skillWidth = Objects.requireNonNull(SpriteCache.get(81)).width;

        if (Client.instance.setting.counter_position == 0) {
            x = Client.canvasWidth - boxWidth - 255;
        } else if (Client.instance.setting.counter_position == 1) {
            x = (Client.canvasWidth - boxWidth - (isFixed ? 255 : 0)) / 2;
        } else if (Client.instance.setting.counter_position == 2) {
            x = 2;
        }

        Objects.requireNonNull(SpriteCache.get(521)).draw_transparent(x + 5, 5, 230);
        Objects.requireNonNull(SpriteCache.get(81)).drawSprite(x + 8, 8);

        if (xpCounter >= 0) {
            AdvancedFont text = getSize(Client.instance.setting.counter_size);
            int xPos = x + (skillWidth + boxWidth) / 2;
            String string = NumberFormat.getInstance().format(xpCounter);
            text.drawCenteredString(string, xPos + 1, 24 + Client.instance.setting.counter_size, Client.instance.setting.counter_color,true);
        }

        if (!GAINS.isEmpty()) {
            Iterator<ExpGain> gained = GAINS.iterator();

            while (gained.hasNext()) {
                ExpGain gain = gained.next();

                if (gain.getY() > STOP) {

                    if (gain.getY() >= MIDLINE) {
                        gain.increaseAlpha();
                    } else {
                        gain.decreaseAlpha();
                    }

                    gain.changeY();

                } else if (gain.getY() <= STOP) {
                    gained.remove();
                }

                boolean xpLocked = Client.xpLocked;
                if (gain.getY() > STOP) {
                    Queue<ExpSprite> temp = new PriorityQueue<>(gain.sprites);
                    int dx = 0;
                    while (!temp.isEmpty()) {
                        ExpSprite expSprite = temp.poll();
                        expSprite.sprite.drawSprite1(x + dx, (int) (y + gain.getY()), gain.getAlpha());
                        dx += expSprite.sprite.width + 1;
                    }
                    if (xpLocked) {
                        Objects.requireNonNull(SpriteCache.get(2006)).drawAdvancedSprite((x + dx + 2), (int) (y + gain.getY()), gain.getAlpha());
                    }
                    String drop = String.format("<trans=%s>%,d", gain.getAlpha(), gain.getXP());
                    getSize(Client.instance.setting.counter_size).draw(drop, x + dx + (xpLocked ? 22 : 0), (int) (gain.getY() + y) + 14, Client.instance.setting.counter_color, 0);
                }
            }
        }
    }


    private static AdvancedFont getSize(int size) {
        if (size == 0)
            return Client.smallFont;
        if (size == 2)
            return Client.boldFont;
        return Client.regularFont;
    }

    static class ExpSprite implements Comparable<ExpSprite> {
        private int skill;
        private SimpleImage sprite;

        ExpSprite(int skill, SimpleImage sprite) {
            this.skill = skill;
            this.sprite = sprite;
        }

        @Override
        public int compareTo(ExpSprite other) {
            return Integer.signum(other.skill - skill);
        }
    }

    static class ExpGain {
        private int xp;
        private float y;
        private double alpha = 0;
        private final Set<ExpSprite> sprites = new TreeSet<>();

        ExpGain(int skill, int xp) {
            this.xp = xp;
            this.y = START;
            addSprite(skill);
        }

        void addSprite(int skill) {
            for (ExpSprite sprite : sprites) {
                if (sprite.skill == skill) {
                    return;
                }
            }
            sprites.add(new ExpSprite(skill, SpriteCache.get(START_SPRITE + skill)));
        }

        void changeY() {
            y -= Client.instance.setting.counter_speed;
        }

        int getXP() {
            return xp;
        }

        public float getY() {
            return y;
        }

        public int getAlpha() {
            return (int) alpha;
        }

        void increaseAlpha() {
            alpha += alpha < 256 ? 30 : 0;
            alpha = Math.min(alpha, 255);
        }

        void decreaseAlpha() {
            alpha -= (alpha > 0 ? 30 : 0) * 0.10;
            alpha = Math.max(alpha, 0);
        }
    }
}
