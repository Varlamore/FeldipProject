package com.cryptic.model.content;

import com.cryptic.Client;
import com.cryptic.draw.Rasterizer2D;
import com.cryptic.engine.impl.MouseHandler;

/**
 * Created by Evan2 on 17/07/2016.
 */
public class SnowFlake {

    private boolean touched;

    private boolean melting;

    private boolean moving;

    private int x;

    private int y;

    private int cycle;

    private int radius;

    private int alpha;

    private int lifespan;

    private SnowFlake(int x, int y, int radius) {
        this(x, y);
        this.radius = radius;
        this.alpha = 100 + random(100);
    }

    private SnowFlake(int x, int y) {
        this.x = x;
        this.y = y;
        this.moving = true;
        //this.lifespan = 250 + random(350);
        this.lifespan = (!Client.instance.isResized()) ? 250 + random(350) : 150 + random(250);
    }

    private void draw(int x, int y) {
        Rasterizer2D.drawFilledCircle(this.x + x - (this.radius / 2), this.y + y - (this.radius / 2), radius, 0xFFFFFF, this.alpha);
    }

    private void reset() {
        this.x = random((!Client.instance.isResized()) ? 765 : Client.canvasWidth);
        this.y = -10;
        this.cycle = 0;
        this.moving = true;
        this.touched = false;
        //this.lifespan = 250 + random(250);
        this.lifespan = (!Client.instance.isResized()) ? 250 + random(250) : 150 + random(250);
        this.radius = 4 + random(2);
        this.alpha = 100 + random(100);
    }

    private static int random(int range) {
        return (int) (Math.random() * (range + 1));
    }

    private void adjustX(int x) {
        this.x += x;
    }

    private void adjustY(int y) {
        this.y += y;
    }

    private int getX() {
        return this.x;
    }

    private int getY() {
        return this.y;
    }

    private int getCycle() {
        return this.cycle;
    }

    private void resetCycle() {
        this.cycle = 0;
    }

    private void cycle() {
        this.cycle++;
    }

    private int getRadius() {
        return this.radius;
    }

    private boolean isMoving() {
        return this.moving;
    }

    private void adjustAlpha(int alpha) {
        this.alpha += alpha;
        if (this.alpha < 0) {
            this.alpha = 0;
        }
        if (this.alpha > 256) {
            this.alpha = 256;
        }
    }

    private int getAlpha() {
        return this.alpha;
    }

    private boolean isMelted() {
        return this.alpha == 0 && !this.moving;
    }

    private int getLifespan() {
        return this.lifespan;
    }

    private boolean isMelting() {
        return this.melting;
    }

    private void setMelting(boolean melting) {
        this.melting = melting;
    }

    private boolean touched() {
        if (Client.inCircle(this.x - (this.radius * 2 - this.radius / 2), this.y - (this.radius * 2 - this.radius / 2), MouseHandler.mouseX, MouseHandler.mouseY, radius)) {
            return true;
        }
        return false;
    }

    private boolean wasTouched() {
        return this.touched;
    }

    private void setTouched(boolean touched) {
        this.touched = touched;
    }

    private static SnowFlake[] snowflakes;

    private static boolean snowing = Client.instance.setting.sky_snow;

    public static void createSnow() {
        snowflakes = new SnowFlake[30 + SnowFlake.random(20)];

        for (int index = 0; index < snowflakes.length; index++) {
            int x = SnowFlake.random((!Client.instance.isResized()) ? 765 : Client.canvasWidth);
            int y_offset = 100 + random(300) + index * 5;

            if (SnowFlake.random(100) < 25) {
                snowflakes[index] = new SnowFlake(x, -(11 + y_offset));
            }
            else {
                int radius = 4 + random(2);
                snowflakes[index] = new SnowFlake(x, -(radius + y_offset), radius);
            }
        }
        snowing = true;
    }

    public static void processSnowflakes() {
        int melted = 0;

        for (int index = 0; index < snowflakes.length; index++) {
            SnowFlake flake = snowflakes[index];

            if (flake.isMelted()) {
                melted++;
                continue;
            }
            else if (!snowing) {
                if (flake.getY() + (flake.getRadius()) < 0) {
                    melted++;
                    continue;
                }
                else {
                    flake.setMelting(true);
                    flake.setTouched(true);
                }
            }
            flake.cycle();
            if (!flake.isMelting() && flake.getX() >= 15 && flake.getX() <= 70 && flake.getY() >= 100 && flake.getY() <= 300) {
                flake.setMelting(true);
                flake.setTouched(true);
            }
            if (flake.touched()) {
                flake.setMelting(true);
                flake.setTouched(true);
            }
            if (flake.isMelting()) {
                if (flake.getCycle() >= flake.getLifespan() && flake.getAlpha() > 0 || flake.wasTouched()) {
                    flake.adjustAlpha(-4);
                }
            }
            if (flake.isMoving()) {
                int x_adjust = flake.getCycle() > 30 ? random(2) : 0;

                if (x_adjust > 0) {
                    flake.resetCycle();
                }

                int y_adjust = random(2);
                flake.adjustX(random(100) >= 50 ? -x_adjust : x_adjust);
                flake.adjustY(y_adjust);
                int height = flake.getRadius();

                //This doesn't account for fullscreen.
                /*if (flake.getY() >= Client.gamescreen_height + height) {
                    System.out.println("Resetting flake at " + flake.getY() + " gamescreenheight " + Client.gamescreen_height + " windowheight " + Client.window_height);
                    flake.reset();
                }*/

                //TODO: maybe we can improve this for full screen (really large y aka height value), it doesn't reset them fast enough so there is a "gap" in drawing the snowflakes. We could also try changing the lifespan, or speeding them up some other way.
                if ((Client.instance.isResized() && flake.getY() >= Client.instance.getViewportHeight() - random( Client.instance.getViewportHeight() / 3) + height) || flake.getY() >= Client.instance.getViewportHeight() + height) {
                    //System.out.println("Resetting flake at " +flake.getY() + " gamescreenheight " + Client.gamescreen_height + " windowheight " + Client.window_height);
                    flake.reset();
                }
            }
        }
        if (melted == snowflakes.length) {
            snowing = false;
        }
    }

    public static void drawSnowflakes(int x, int y) {
        if (snowflakes != null) {
            for (int index = 0; index < snowflakes.length; index++) {
                SnowFlake flake = snowflakes[index];
                if (flake.isMelted()) {
                    continue;
                }
                flake.draw(x, y);
            }
        }
    }
}
