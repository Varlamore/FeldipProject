package com.cryptic.cache.graphics.widget.impl.tob;

import com.cryptic.Client;
import com.cryptic.cache.graphics.widget.InterfaceBuilder;
import com.cryptic.cache.graphics.widget.Widget;

import java.util.concurrent.atomic.AtomicInteger;

public class NightmareHealth extends InterfaceBuilder {
    public NightmareHealth() {
        super(75000);
    }

    @Override
    public void build() {

        drawPixels(nextInterface(), 135, 72, 0, 0, 200);
        child(14, 8);

        addColorBoxNonTransparent(nextInterface(), 0x534a40, 133, 70);
        child(15, 9);

        addText(nextInterface(), "North West", fonts, 0, 0xFF9800, true, true);
        child(48, 15);
        drawProgressBar(nextInterface(), 50, 12, 100, 0xffa500, 0xFFFF00);
        child(22, 27);

        addText(nextInterface(), "South West", fonts, 0, 0xFF9800, true, true);
        child(47, 45);
        drawProgressBar(nextInterface(), 50, 12, 80, 0xffa500, 0xFFFF00);
        child(22, 57);

        addText(nextInterface(), "North East", fonts, 0, 0xFF9800, true, true);
        child(114, 15);
        drawProgressBar(nextInterface(), 50, 12, 50, 0xffa500, 0xFFFF00);
        child(90, 27);

        addText(nextInterface(), "South East", fonts, 0, 0xFF9800, true, true);
        child(113, 45);
        drawProgressBar(nextInterface(), 50, 12, 20, 0xffa500, 0xFFFF00);
        child(90, 57);
    }
    static AtomicInteger alpha = new AtomicInteger(150);
    private static boolean countingUp = true;
    public static void draw() {
        if (Client.instance.openWalkableInterface != 75000) {
            return;
        }

        if (Widget.cache[75004].currentPercent == 100) {

            if (countingUp) {

                alpha.getAndIncrement();

                if (alpha.get() == 255) {
                    countingUp = false;
                }

            } else {

                alpha.getAndDecrement();

                if (alpha.get() == 150) {
                    countingUp = true;
                }

            }

            Widget.cache[75004].alpha = alpha.get();
        }

    }

}
