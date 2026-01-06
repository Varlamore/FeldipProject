package com.cryptic.cache.graphics;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;

import com.cryptic.cache.Archive;
import com.cryptic.cache.def.graphics.SpriteData;
import com.cryptic.collection.table.EvictingDualNodeHashTable;
import com.cryptic.draw.Rasterizer2D;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;
import com.cryptic.sign.SignLink;
import net.runelite.rs.api.RSSpritePixels;

public final class SimpleImage extends Rasterizer2D implements RSSpritePixels {

    private Image image;

    public Image getImage() {
        return image;
    }

    public static boolean field1463 = false;


    public int[] pixels;
    public int width;
    public int height;
    public int x_offset;
    public int y_offset;
    public int max_width;
    public int max_height;

    public SimpleImage(int width, int height) {
        this.pixels = new int[width * height];
        this.width = max_width = width;
        this.height = max_height = height;
        x_offset = y_offset = 0;
    }

    public SimpleImage setPixels(int size) {
        this.pixels[0] = 1;
        return this;
    }

    public void outline(int color) {

        int[] raster = new int[width * height];
        int start = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int outline = pixels[start];
                if (outline == 0) {
                    if (x > 0 && pixels[start - 1] != 0) {
                        outline = color;
                    } else if (y > 0 && pixels[start - width] != 0) {
                        outline = color;
                    } else if (x < width - 1 && pixels[start + 1] != 0) {
                        outline = color;
                    } else if (y < height - 1 && pixels[start + width] != 0) {
                        outline = color;
                    }
                }
                raster[start++] = outline;
            }
        }
        pixels = raster;
    }

    public SimpleImage(Archive archive, String name, int id) {
        Buffer buffer = new Buffer(archive.get(name + ".dat"));
        Buffer data = new Buffer(archive.get("index.dat"));
        data.pos = buffer.readUnsignedShort();
        max_width = data.readUnsignedShort();
        max_height = data.readUnsignedShort();
        int length = data.readUnsignedByte();
        int[] pixels = new int[length];
        for (int index = 0; index < length - 1; index++) {
            pixels[index + 1] = data.readTriByte();
            if (pixels[index + 1] == 0) {
                pixels[index + 1] = 1;
            }
        }

        for (int index = 0; index < id; index++) {
            data.pos += 2;
            buffer.pos += data.readUnsignedShort() * data.readUnsignedShort();
            data.pos++;
        }

        x_offset = data.readUnsignedByte();
        y_offset = data.readUnsignedByte();
        this.width = data.readUnsignedShort();
        this.height = data.readUnsignedShort(); // wheres this jazz fgrom its the textuyre stuff ignore  it
        int opcode = data.readUnsignedByte();
        int size = this.width * this.height;
        this.pixels = new int[size];
        if (opcode == 0) {
            for (int pixel = 0; pixel < size; pixel++) {
                this.pixels[pixel] = pixels[buffer.readUnsignedByte()];
            }
            set_transparent_pixels(255, 0, 255);
            return;
        }
        if (opcode == 1) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    this.pixels[x + y * this.width] = pixels[buffer.readUnsignedByte()];
                }
            }
        }
        set_transparent_pixels(255, 0, 255);
    }

    public SimpleImage(Image image) {
        ImageIcon imageicon = new ImageIcon(image);
        imageicon.getIconHeight();
        imageicon.getIconWidth();
        try {
            this.width = imageicon.getIconWidth();
            this.height = imageicon.getIconHeight();
            max_width = this.width;
            max_height = this.height;
            x_offset = 0;
            y_offset = 0;
            this.pixels = new int[this.width * this.height];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, this.width, this.height, this.pixels, 0, this.width);
            pixelgrabber.grabPixels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SimpleImage(byte[] spriteData, float width, int height) {
        try {
            image = Toolkit.getDefaultToolkit().createImage(spriteData);
            ImageIcon sprite = new ImageIcon(image);
            this.width = (int) width;
            this.height = height;
            max_width = this.width;
            max_height = this.height;
            x_offset = 0;
            y_offset = 0;
            this.pixels = new int[this.width * this.height];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, this.width, this.height, this.pixels, 0, this.width);
            pixelgrabber.grabPixels();
            image = null;
            set_transparent_pixels(255, 0, 255);
        } catch (Exception _ex) {
            System.err.println(_ex);
        }
    }

    public SimpleImage(String img) {
        try {
            image =  Toolkit.getDefaultToolkit().getImage(SignLink.findCacheDir() + "Sprites/" + img + ".png");
            ImageIcon sprite = new ImageIcon(image);
            this.width = sprite.getIconWidth();
            this.height = sprite.getIconHeight();
            max_width = this.width;
            max_height = this.height;
            x_offset = 0;
            y_offset = 0;
            this.pixels = new int[this.width * this.height];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, this.width, this.height, this.pixels, 0, this.width);
            pixelgrabber.grabPixels();
            image = null;
            set_transparent_pixels(255, 0, 255);
        } catch (Exception _ex) {
            System.err.println(_ex);
        }
    }

    public void drawAdvancedTransparentSprite(int x, int y, int opacity) {
        int alpha =  (int) (opacity * 2.56D);
        if (alpha > 256 || alpha < 0) {
            alpha = 256;
        }
        x += x_offset;
        y += y_offset;
        int dst_pos = x + y * Rasterizer2D.width;
        int src_pos = 0;
        int height = this.height;
        int width = this.width;
        int dst_width = Rasterizer2D.width - width;
        int dst_height = 0;
        if (y < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - y;
            height -= k2;
            y = Rasterizer2D.yClipStart;
            src_pos += k2 * width;
            dst_pos += k2 * Rasterizer2D.width;
        }
        if (y + height > yClipEnd)
            height -= (y + height) - yClipEnd;
        if (x < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - x;
            width -= l2;
            x = Rasterizer2D.xClipStart;
            src_pos += l2;
            dst_pos += l2;
            dst_height += l2;
            dst_width += l2;
        }
        if (x + width > Rasterizer2D.xClipEnd) {
            int i3 = (x + width) - Rasterizer2D.xClipEnd;
            width -= i3;
            dst_height += i3;
            dst_width += i3;
        }
        if (!(width <= 0 || height <= 0)) {
            render_transparent(src_pos, width, Rasterizer2D.pixels, this.pixels, dst_height, height, dst_width, alpha, dst_pos);
        }
    }

    public void draw_transparent(int x, int y, int alpha) {
        x += x_offset;
        y += y_offset;
        int dst_pos = x + y * Rasterizer2D.width;
        int src_pos = 0;
        int height = this.height;
        int width = this.width;
        int dst_width = Rasterizer2D.width - width;
        int dst_height = 0;
        if (y < Rasterizer2D.yClipStart) {
            int size = Rasterizer2D.yClipStart - y;
            height -= size;
            y = Rasterizer2D.yClipStart;
            src_pos += size * width;
            dst_pos += size * Rasterizer2D.width;
        }
        if (y + height > Rasterizer2D.yClipEnd)
            height -= (y + height) - Rasterizer2D.yClipEnd;
        
        if (x < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - x;
            width -= l2;
            x = Rasterizer2D.xClipStart;
            src_pos += l2;
            dst_pos += l2;
            dst_height += l2;
            dst_width += l2;
        }
        if (x + width > Rasterizer2D.xClipEnd) {
            int i3 = (x + width) - Rasterizer2D.xClipEnd;
            width -= i3;
            dst_height += i3;
            dst_width += i3;
        }
        if (!(width <= 0 || height <= 0)) {
            render_transparent(src_pos, width, Rasterizer2D.pixels, this.pixels, dst_height, height, dst_width, alpha, dst_pos);
        }
    }

    public void set_transparent_pixels(int r, int g, int b) {
        for (int index = 0; index < this.pixels.length; index++) {
            if (((this.pixels[index] >> 16) & 255) == r && ((this.pixels[index] >> 8) & 255) == g && (this.pixels[index] & 255) == b) {
                this.pixels[index] = 0;
            }
        }
    }

    public void init() {
        Rasterizer2D.init(this.width, this.height, this.pixels, depth);
    }

    public void blend(int red, int green, int blue) {
        for (int index = 0; index < this.pixels.length; index++) {
            int color = this.pixels[index];
            if (color != 0) {
                int r = color >> 16 & 0xff;
                r += red;
                if (r < 1)
                    r = 1;
                else if (r > 255)
                    r = 255;

                int g = color >> 8 & 0xff;
                g += green;
                if (g < 1)
                    g = 1;
                else if (g > 255)
                    g = 255;

                int b = color & 0xff;
                b += blue;
                if (b < 1)
                    b = 1;
                else if (b > 255)
                    b = 255;

                this.pixels[index] = (r << 16) + (g << 8) + b;
            }
        }
    }

    public SimpleImage trim() {
        int[] pixels = new int[max_width * max_height];
        for (int y = 0; y < this.height; y++) {
            System.arraycopy(this.pixels, y * this.width, pixels, y + y_offset * max_width + x_offset, this.width);
        }
        this.pixels = pixels;
        this.width = max_width;
        this.height = max_height;
        x_offset = 0;
        y_offset = 0;
        return this;
    }

    public void draw_inverse(int x, int y) {
        x += x_offset;
        y += y_offset;
        int dst_pos = x + y * Rasterizer2D.width;
        int src_pos = 0;
        int height = this.height;
        int width = this.width;
        int dst_width = Rasterizer2D.width - width;
        int src_width = 0;
        if (y < Rasterizer2D.yClipStart) {
            int size = Rasterizer2D.yClipStart - y;
            height -= size;
            y = Rasterizer2D.yClipStart;
            src_pos += size * width;
            dst_pos += size * Rasterizer2D.width;
        }
        if (y + height > Rasterizer2D.yClipEnd)
            height -= (y + height) - Rasterizer2D.yClipEnd;

        if (x < Rasterizer2D.xClipStart) {
            int size = Rasterizer2D.xClipStart - x;
            width -= size;
            x = Rasterizer2D.xClipStart;
            src_pos += size;
            dst_pos += size;
            src_width += size;
            dst_width += size;
        }
        if (x + width > Rasterizer2D.xClipEnd) {
            int size = (x + width) - Rasterizer2D.xClipEnd;
            width -= size;
            src_width += size;
            dst_width += size;
        }
        if (width <= 0 || height <= 0) {
            //
        } else {
            copy(dst_pos, width, height, src_width, src_pos, dst_width, this.pixels, Rasterizer2D.pixels);
        }
    }

    private void copy(int dst_pos, int width, int height, int src_width, int src_pos, int dst_width, int src[], int pixels[]) {
        int length = -(width >> 2);
        width = -(width & 3);
        for (int column = -height; column < 0; column++) {
            for (int row = length; row < 0; row++) {
                drawAlpha(pixels,dst_pos++,src[src_pos++],255);
                drawAlpha(pixels,dst_pos++,src[src_pos++],255);
                drawAlpha(pixels,dst_pos++,src[src_pos++],255);
                drawAlpha(pixels,dst_pos++,src[src_pos++],255);
            }

            for (int step = width; step < 0; step++)
                drawAlpha(pixels,dst_pos++,src[src_pos++],255);

            dst_pos += dst_width;
            src_pos += src_width;
        }
    }

    public void drawSprite1(int i, int j) {
          drawSprite1(i, j, 128);
    }

    public void drawSprite1(int i, int j, int k, boolean overrideCanvas) {
        i += x_offset;
        j += y_offset;
        int i1 = i + j * Rasterizer2D.width;
        int j1 = 0;
        int k1 = this.height;
        int l1 = this.width;
        int i2 = Rasterizer2D.width - l1;
        int j2 = 0;
        if (!(overrideCanvas && j > 0) && j < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - j;
            k1 -= k2;
            j = Rasterizer2D.yClipStart;
            j1 += k2 * l1;
            i1 += k2 * Rasterizer2D.width;
        }
        if (j + k1 > Rasterizer2D.yClipEnd)
            k1 -= (j + k1) - Rasterizer2D.yClipEnd;
        if (!overrideCanvas && i < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - i;
            l1 -= l2;
            i = Rasterizer2D.xClipStart;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Rasterizer2D.xClipEnd) {
            int i3 = (i + l1) - Rasterizer2D.xClipEnd;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            render_transparent(j1, l1, Rasterizer2D.pixels, this.pixels, j2, k1, i2, k, i1);
        }
    }

    public void drawShadedSpriteWithoutBounds(int i, int j, int k, boolean overrideCanvas) {
        i += x_offset;
        j += y_offset;
        int i1 = i + j * Rasterizer2D.width;
        int j1 = 0;
        int k1 = this.height;
        int l1 = this.width;
        int i2 = Rasterizer2D.width - l1;
        int j2 = 0;
        if (!(overrideCanvas && j > 0) && j < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - j;
            k1 -= k2;
            j = Rasterizer2D.yClipStart;
            j1 += k2 * l1;
            i1 += k2 * Rasterizer2D.width;
        }
        if (j + k1 > Rasterizer2D.yClipEnd)
            k1 -= (j + k1) - Rasterizer2D.yClipEnd;
        if (!overrideCanvas && i < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - i;
            l1 -= l2;
            i = Rasterizer2D.xClipStart;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Rasterizer2D.xClipEnd) {
            int i3 = (i + l1) - Rasterizer2D.xClipEnd;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            renderShadedARGBPixelsWithoutBounds(j1, l1, Rasterizer2D.pixels, this.pixels, j2, k1, i2, k, i1);
        }
        drawTransparentSpriteWithoutBounds(i, j, k, overrideCanvas);
    }

    private void renderShadedARGBPixelsWithoutBounds(int i, int j, int ai[], int ai1[], int l, int i1, int j1, int k1, int l1) {
        int k;// was parameter
        int j2 = 256 - k1;
        for (int k2 = -i1; k2 < 0; k2++) {
            for (int l2 = -j; l2 < 0; l2++) {
                k = ai1[i++];
                if (k != 0) {
                    int i3 = ai[l1];
                    ai[l1++] = 0x000000 >> 8;
                } else {
                    l1++;
                }
            }
            l1 += j1;
            i += l;
        }
    }

    public void drawTransparentSpriteWithoutBounds(int i, int j, int k, boolean overrideCanvas) {
        i += x_offset;
        j += y_offset;
        int i1 = i + j * Rasterizer2D.width;
        int j1 = 0;
        int k1 = this.height;
        int l1 = this.width;
        int i2 = Rasterizer2D.width - l1;
        int j2 = 0;
        if (!(overrideCanvas && j > 0) && j < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - j;
            k1 -= k2;
            j = Rasterizer2D.yClipStart;
            j1 += k2 * l1;
            i1 += k2 * Rasterizer2D.width;
        }
        if (j + k1 > Rasterizer2D.yClipEnd)
            k1 -= (j + k1) - Rasterizer2D.yClipEnd;
        if (!overrideCanvas && i < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - i;
            l1 -= l2;
            i = Rasterizer2D.xClipStart;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Rasterizer2D.xClipEnd) {
            int i3 = (i + l1) - Rasterizer2D.xClipEnd;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            render_transparent(j1, l1, Rasterizer2D.pixels, this.pixels, j2, k1, i2, k, i1);
        }
    }

    public void drawSpriteWithOutline(int i, int k, int color, boolean overrideCanvas) {
        int tempWidth = this.width + 2;
        int tempHeight = this.height + 2;
        int[] tempArray = new int[tempWidth * tempHeight];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.pixels[x + y * this.width] != 0)
                    tempArray[(x + 1) + (y + 1) * tempWidth] = this.pixels[x + y * this.width];
            }
        }
        for (int x = 0; x < tempWidth; x++) {
            for (int y = 0; y < tempHeight; y++) {
                if (tempArray[(x) + (y) * tempWidth] == 0) {
                    if (x < tempWidth - 1 && tempArray[(x + 1) + ((y) * tempWidth)] != 0
                        && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray,(x) + (y) * tempWidth,color,255);
                    }
                    if (x != 0 && tempArray[(x - 1) + ((y) * tempWidth)] != 0
                        && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray,(x) + (y) * tempWidth,color,255);
                    }
                    if (y < tempHeight - 1 && tempArray[(x) + ((y + 1) * tempWidth)] != 0
                        && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray,(x) + (y) * tempWidth,color,255);
                    }
                    if (y != 0 && tempArray[(x) + ((y - 1) * tempWidth)] != 0
                        && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray,(x) + (y) * tempWidth,color,255);
                    }
                }
            }
        }
        i--;
        k--;
        i += x_offset;
        k += y_offset;
        int l = i + k * Rasterizer2D.width;
        int i1 = 0;
        int j1 = tempHeight;
        int k1 = tempWidth;
        int l1 = Rasterizer2D.width - k1;
        int i2 = 0;
        if (!(overrideCanvas && k > 0) && k < Rasterizer2D.yClipStart) {
            int j2 = Rasterizer2D.yClipStart - k;
            j1 -= j2;
            k = Rasterizer2D.yClipStart;
            i1 += j2 * k1;
            l += j2 * Rasterizer2D.width;
        }
        if (k + j1 > Rasterizer2D.yClipEnd) {
            j1 -= (k + j1) - Rasterizer2D.yClipEnd;
        }
        if (!overrideCanvas && i < Rasterizer2D.xClipStart) {
            int k2 = Rasterizer2D.xClipStart - i;
            k1 -= k2;
            i = Rasterizer2D.xClipStart;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (!overrideCanvas && i + k1 > Rasterizer2D.xClipEnd) {
            int l2 = (i + k1) - Rasterizer2D.xClipEnd;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (!(k1 <= 0 || j1 <= 0)) {
            render(Rasterizer2D.pixels, tempArray, i1, l, k1, j1, l1, i2);
        }
    }

    public void draw_highlighted(int x, int y, int color) {
        int highlight_width = this.width + 2;
        int highlight_height = this.height + 2;
        int[] pixels = new int[highlight_width * highlight_height];
        for (int _x = 0; _x < this.width; _x++) {
            for (int _y = 0; _y < this.height; _y++) {
                if (this.pixels[_x + _y * this.width] != 0)
                    pixels[(_x + 1) + (_y + 1) * highlight_width] = this.pixels[_x + _y * this.width];
            }
        }
        for (int x_ = 0; x_ < highlight_width; x_++) {
            for (int y_ = 0; y_ < highlight_height; y_++) {
                if (pixels[(x_) + (y_) * highlight_width] == 0) {
                    if (x_ < highlight_width - 1 && pixels[(x_ + 1) + ((y_) * highlight_width)] != 0
                        && pixels[(x_ + 1) + ((y_) * highlight_width)] != 0xffffff) {
                        drawAlpha(pixels,(x_) + (y_) * highlight_width,color,255);
                    }
                    if (x_ != 0 && pixels[(x_ - 1) + ((y_) * highlight_width)] != 0
                        && pixels[(x_ - 1) + ((y_) * highlight_width)] != 0xffffff) {
                        drawAlpha(pixels,(x_) + (y_) * highlight_width,color,255);
                    }
                    if (y_ < highlight_height - 1 && pixels[(x_) + ((y_ + 1) * highlight_width)] != 0
                        && pixels[(x_) + ((y_ + 1) * highlight_width)] != 0xffffff) {
                        drawAlpha(pixels,(x_) + (y_) * highlight_width,color,255);
                    }
                    if (y_ != 0 && pixels[(x_) + ((y_ - 1) * highlight_width)] != 0
                        && pixels[(x_) + ((y_ - 1) * highlight_width)] != 0xffffff) {
                        drawAlpha(pixels,(x_) + (y_) * highlight_width,color,255);
                    }
                }
            }
        }
        x--;
        y--;
        x += this.x_offset;
        y += this.y_offset;
        int dst_pos = x + y * Rasterizer2D.width;
        int src_pos = 0;
        int height = highlight_height;
        int width = highlight_width;
        int dst_width = Rasterizer2D.width - width;
        int src_width = 0;
        if (y < Rasterizer2D.yClipStart) {
            int size = Rasterizer2D.yClipStart - y;
            height -= size;
            y = Rasterizer2D.yClipStart;
            src_pos += size * width;
            dst_pos += size * Rasterizer2D.width;
        }
        if (y + height > Rasterizer2D.yClipEnd) {
            height -= (y + height) - Rasterizer2D.yClipEnd;
        }
        if (x < Rasterizer2D.xClipStart) {
            int size = Rasterizer2D.xClipStart - x;
            width -= size;
            x = Rasterizer2D.xClipStart;
            src_pos += size;
            dst_pos += size;
            src_width += size;
            dst_width += size;
        }
        if (x + width > Rasterizer2D.xClipEnd) {
            int size = (x + width) - Rasterizer2D.xClipEnd;
            width -= size;
            src_width += size;
            dst_width += size;
        }
        if (!(width <= 0 || height <= 0)) {
            render(Rasterizer2D.pixels, pixels, src_pos, dst_pos, width, height, dst_width, src_width);
        }
    }

    public void drawSprite1(int i, int j, int k) {
        i += x_offset;
        j += y_offset;
        int i1 = i + j * Rasterizer2D.width;
        int j1 = 0;
        int k1 = this.height;
        int l1 = this.width;
        int i2 = Rasterizer2D.width - l1;
        int j2 = 0;
        if (j < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - j;
            k1 -= k2;
            j = Rasterizer2D.yClipStart;
            j1 += k2 * l1;
            i1 += k2 * Rasterizer2D.width;
        }
        if (j + k1 > Rasterizer2D.yClipEnd)
            k1 -= (j + k1) - Rasterizer2D.yClipEnd;
        if (i < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - i;
            l1 -= l2;
            i = Rasterizer2D.xClipStart;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > Rasterizer2D.xClipEnd) {
            int i3 = (i + l1) - Rasterizer2D.xClipEnd;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            render_transparent(j1, l1, Rasterizer2D.pixels, this.pixels, j2, k1, i2, k, i1);
        }
    }

    public SimpleImage setBounds(int x, int y) {
        this.height = max_height = x;
        this.width = max_width = y;
        return this;
    }


    public SimpleImage drawSprite(int x, int y) {
        x += x_offset;
        y += y_offset;
        int rasterClip = x + y * Rasterizer2D.width;
        int imageClip = 0;
        int height = this.height;
        int width = this.width;
        int rasterOffset = Rasterizer2D.width - width;
        int imageOffset = 0;
        if (y < Rasterizer2D.yClipStart)
        {
            int dy = Rasterizer2D.yClipStart - y;
            height -= dy;
            y = Rasterizer2D.yClipStart;
            imageClip += dy * width;
            rasterClip += dy * Rasterizer2D.width;
        }
        if (y + height > Rasterizer2D.yClipEnd)
            height -= (y + height) - Rasterizer2D.yClipEnd;
        if (x < Rasterizer2D.xClipStart)
        {
            int dx = Rasterizer2D.xClipStart - x;
            width -= dx;
            x = Rasterizer2D.xClipStart;
            imageClip += dx;
            rasterClip += dx;
            imageOffset += dx;
            rasterOffset += dx;
        }
        if (x + width > Rasterizer2D.xClipEnd)
        {
            int dx = (x + width) - Rasterizer2D.xClipEnd;
            width -= dx;
            imageOffset += dx;
            rasterOffset += dx;
        }
        if (!(width <= 0 || height <= 0))
        {
            render(Rasterizer2D.pixels, this.pixels, imageClip, rasterClip, width, height, rasterOffset, imageOffset);
        }
        return this;
    }

    public void drawSprite(int i, int k, int color) {
        int tempWidth = width + 2;
        int tempHeight = height + 2;
        int[] tempArray = new int[tempWidth * tempHeight];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (pixels[x + y * width] != 0)
                    drawAlpha(tempArray,(x + 1) + (y + 1) * tempWidth,pixels[x + y * width],255);
            }
        }
        for (int x = 0; x < tempWidth; x++) {
            for (int y = 0; y < tempHeight; y++) {
                if (tempArray[(x) + (y) * tempWidth] == 0) {
                    if (x < tempWidth - 1 && tempArray[(x + 1) + ((y) * tempWidth)] > 0 && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray, (x) + (y) * tempWidth, color, 255);
                    }
                    if (x > 0 && tempArray[(x - 1) + ((y) * tempWidth)] > 0 && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray, (x) + (y) * tempWidth, color, 255);
                    }
                    if (y < tempHeight - 1 && tempArray[(x) + ((y + 1) * tempWidth)] > 0 && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray, (x) + (y) * tempWidth, color, 255);
                    }
                    if (y > 0 && tempArray[(x) + ((y - 1) * tempWidth)] > 0 && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
                        drawAlpha(tempArray, (x) + (y) * tempWidth, color, 255);
                    }
                }
            }
        }
        i--;
        k--;
        i += x_offset;
        k += y_offset;
        int l = i + k * Rasterizer2D.width;
        int i1 = 0;
        int j1 = tempHeight;
        int k1 = tempWidth;
        int l1 = Rasterizer2D.width - k1;
        int i2 = 0;
        if (k < Rasterizer2D.yClipStart) {
            int j2 = Rasterizer2D.yClipStart - k;
            j1 -= j2;
            k = Rasterizer2D.yClipStart;
            i1 += j2 * k1;
            l += j2 * Rasterizer2D.width;
        }
        if (k + j1 > Rasterizer2D.yClipEnd) {
            j1 -= (k + j1) - Rasterizer2D.yClipEnd;
        }
        if (i < Rasterizer2D.xClipStart) {
            int k2 = Rasterizer2D.xClipStart - i;
            k1 -= k2;
            i = Rasterizer2D.xClipStart;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (i + k1 > Rasterizer2D.xClipEnd) {
            int l2 = (i + k1) - Rasterizer2D.xClipEnd;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (!(k1 <= 0 || j1 <= 0)) {
            render(Rasterizer2D.pixels, tempArray, i1, l, k1, j1, l1, i2);
        }
    }

    private void render(int pixels[], int src[], int src_pos, int dst_pos, int width, int height, int dst_width, int src_width) {
        int index;// was parameter
        int length = -(width >> 2);
        width = -(width & 3);
        for (int column = -height; column < 0; column++) {
            for (int row = length; row < 0; row++) {
                index = src[src_pos++];
                if (index != 0 && index != -1) {
                    drawAlpha(pixels,dst_pos++,index,255);
                } else {
                    dst_pos++;
                }
                index = src[src_pos++];
                if (index != 0 && index != -1) {
                    drawAlpha(pixels,dst_pos++,index,255);
                } else {
                    dst_pos++;
                }
                index = src[src_pos++];
                if (index != 0 && index != -1) {
                    drawAlpha(pixels,dst_pos++,index,255);
                } else {
                    dst_pos++;
                }
                index = src[src_pos++];
                if (index != 0 && index != -1) {
                    drawAlpha(pixels,dst_pos++,index,255);
                } else {
                    dst_pos++;
                }
            }

            for (int step = width; step < 0; step++) {
                index = src[src_pos++];
                if (index != 0 && index != -1) {
                    drawAlpha(pixels,dst_pos++,index,255);
                } else {
                    dst_pos++;
                }
            }
            dst_pos += dst_width;
            src_pos += src_width;
        }
    }

    private void render_transparent(int src_pos, int width, int pixels[], int raster[], int src_width, int height, int dst_width, int alpha, int dst_pos) {
        int color;// was parameter
        int opacity = 256 - alpha;
        for (int column = -height; column < 0; column++) {
            for (int row = -width; row < 0; row++) {
                color = raster[src_pos++];
                if (color != 0) {
                    int src = pixels[dst_pos];
                    drawAlpha(pixels,dst_pos++,((color & 0xff00ff) * alpha + (src & 0xff00ff) * opacity & 0xff00ff00) + ((color & 0xff00) * alpha + (src & 0xff00) * opacity & 0xff0000) >> 8,alpha);
                } else {
                    dst_pos++;
                }
            }

            dst_pos += dst_width;
            src_pos += src_width;
        }
    }

    public void rotate_raster(int height, int rotation, int[] dst_width, int hinge_size, int[]  raster_height, int centerY, int y, int x, int width, int centerX) {
        try {
            int location_x = -width / 2;
            int location_y = -height / 2;
            int sin = (int) (Math.sin((double) rotation / 326.11000000000001D) * 65536D);
            int cos = (int) (Math.cos((double) rotation / 326.11000000000001D) * 65536D);
            sin = sin * hinge_size >> 8;
            cos = cos * hinge_size >> 8;
            int rot_x = (centerX << 16) + (location_y * sin + location_x * cos);
            int rot_b = (centerY << 16) + (location_y * cos - location_x * sin);
            int dst_pos = x + y * Rasterizer2D.width;
            for (y = 0; y < height; y++) {
                int step = raster_height[y];
                int index = dst_pos + step;
                int a = rot_x + cos * step;
                int b = rot_b - sin * step;
                for (x = -dst_width[y]; x < 0; x++) {
                    int top = this.pixels[(a >> 16) + (b >> 16) * this.width];
                    int right = this.pixels[((a >> 16) + 1) + (b >> 16) * this.width];
                    int left = this.pixels[(a >> 16) + ((b >> 16) + 1) * this.width];
                    int bottom = this.pixels[((a >> 16) + 1) + ((b >> 16) + 1) * this.width];

                    int u1 = (a >> 8) - ((a >> 16) << 8);
                    int v1 = (b >> 8) - ((b >> 16) << 8);
                    int u2 = (((a >> 16) + 1) << 8) - (a >> 8);
                    int v2 = (((b >> 16) + 1) << 8) - (b >> 8);

                    int top_a = u2 * v2;
                    int right_a = u1 * v2;
                    int left_a = u2 * v1;
                    int bottom_a = u1 * v1;

                    int red = (top >> 16 & 0xff) * top_a + (right >> 16 & 0xff) * right_a + (left >> 16 & 0xff) * left_a + (bottom >> 16 & 0xff) * bottom_a & 0xff0000;
                    int green = (top >> 8 & 0xff) * top_a + (right >> 8 & 0xff) * right_a + (left >> 8 & 0xff) * left_a + (bottom >> 8 & 0xff) * bottom_a >> 8 & 0xff00;
                    int blue = (top & 0xff) * top_a + (right & 0xff) * right_a + (left & 0xff) * left_a + (bottom & 0xff) * bottom_a >> 16;

                    drawAlpha(Rasterizer2D.pixels,index++,red | green | blue,255);
                    a += cos;
                    b -= sin;
                }
                rot_x += sin;
                rot_b += cos;
                dst_pos += Rasterizer2D.width;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public SimpleImage(byte[] data) {
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(data);
            ImageIcon sprite = new ImageIcon(image);
            this.width = sprite.getIconWidth();
            this.height = sprite.getIconHeight();
            max_width = this.width;
            max_height = this.height;
            x_offset = 0;
            y_offset = 0;
            this.pixels = new int[this.width * this.height];
            PixelGrabber grab = new PixelGrabber(image, 0, 0, this.width, this.height, this.pixels, 0, this.width);
            grab.grabPixels();
            image = null;
            set_transparent_pixels(255, 0, 255);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public SimpleImage(int width, int height, int offsetX, int offsetY, int[] pixels) {
        this.width = width;
        this.height = height;
        this.x_offset = offsetX;
        this.y_offset = offsetY;
        this.pixels = pixels;

        Color color = Color.MAGENTA;
        set_transparent_pixels(color.getRed(), color.getGreen(), color.getBlue());
    }


    public SimpleImage(int width, int height, int offsetX, int offsetY, byte[] data) {
        try {
            Image image = Toolkit.getDefaultToolkit().createImage(data);
            this.width = width;
            this.height = height;
            this.max_width = width;
            this.max_height = height;
            this.x_offset = offsetX;
            this.y_offset = offsetY;

            pixels = new int[width * height];
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            pixelgrabber.grabPixels();

            Color color = Color.MAGENTA;
            set_transparent_pixels(color.getRed(), color.getGreen(), color.getBlue());
        } catch(Exception _ex) {
            _ex.printStackTrace();
            System.out.println("Error loading image.");
        }
    }

    public static Image create(byte spriteData[]) {
        return Toolkit.getDefaultToolkit().createImage(spriteData);
    }

    public void drawAdvancedSprite(int xPos, int yPos) {
        drawAdvancedSprite(xPos, yPos, 256);
    }

    public SimpleImage drawAdvancedSprite(int xPos, int yPos, int alpha) {
        int alphaValue = alpha;
        xPos += x_offset;
        yPos += y_offset;
        int i1 = xPos + yPos * Rasterizer2D.width;
        int j1 = 0;
        int spriteHeight = this.height;
        int spriteWidth = this.width;
        int i2 = Rasterizer2D.width - spriteWidth;
        int j2 = 0;
        if (yPos < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - yPos;
            spriteHeight -= k2;
            yPos = Rasterizer2D.yClipStart;
            j1 += k2 * spriteWidth;
            i1 += k2 * Rasterizer2D.width;
        }
        if (yPos + spriteHeight > Rasterizer2D.yClipEnd)
            spriteHeight -= (yPos + spriteHeight) - Rasterizer2D.yClipEnd;
            if (xPos < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - xPos;
            spriteWidth -= l2;
            xPos = Rasterizer2D.xClipStart;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (xPos + spriteWidth > Rasterizer2D.xClipEnd) {
            int i3 = (xPos + spriteWidth) - Rasterizer2D.xClipEnd;
            spriteWidth -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(spriteWidth <= 0 || spriteHeight <= 0)) {
            renderARGBPixels(spriteWidth, spriteHeight, this.pixels, Rasterizer2D.pixels, i1, alphaValue, j1, j2, i2);
        }
        return this;
    }

    public void drawShadedSprite(int xPos, int yPos, int shade) {
        int shadeValue = shade;
        xPos += x_offset;
        yPos += y_offset;
        int i1 = xPos + yPos * Rasterizer2D.width;
        int j1 = 0;
        int spriteHeight = this.height;
        int spriteWidth = this.width;
        int i2 = Rasterizer2D.width - spriteWidth;
        int j2 = 0;
        if (yPos < Rasterizer2D.yClipStart) {
            int k2 = Rasterizer2D.yClipStart - yPos;
            spriteHeight -= k2;
            yPos = Rasterizer2D.yClipStart;
            j1 += k2 * spriteWidth;
            i1 += k2 * Rasterizer2D.width;
        }
        if (yPos + spriteHeight > Rasterizer2D.yClipEnd)
            spriteHeight -= (yPos + spriteHeight) - Rasterizer2D.yClipEnd;
        if (xPos < Rasterizer2D.xClipStart) {
            int l2 = Rasterizer2D.xClipStart - xPos;
            spriteWidth -= l2;
            xPos = Rasterizer2D.xClipStart;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (xPos + spriteWidth > Rasterizer2D.xClipEnd) {
            int i3 = (xPos + spriteWidth) - Rasterizer2D.xClipEnd;
            spriteWidth -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(spriteWidth <= 0 || spriteHeight <= 0)) {
            renderShadedARGBPixels(spriteWidth, spriteHeight, this.pixels, Rasterizer2D.pixels, i1, shadeValue, j1, j2,
                i2);
        }
        drawAdvancedSprite(xPos, yPos, shade);
    }

    private void renderShadedARGBPixels(int spriteWidth, int spriteHeight, int spritePixels[], int renderAreaPixels[],
                                        int pixel, int alphaValue, int i, int l, int j1) {
        int pixelColor;
        int alphaLevel;
        int alpha = alphaValue;
        for (int height = -spriteHeight; height < 0; height++) {
            for (int width = -spriteWidth; width < 0; width++) {
                alphaValue = ((this.pixels[i] >> 24) & (alpha - 1));
                alphaLevel = 256 - alphaValue;
                if (alphaLevel > 256) {
                    alphaValue = 0;
                }
                if (alpha == 0) {
                    alphaLevel = 256;
                    alphaValue = 0;
                }
                pixelColor = spritePixels[i++];
                if (pixelColor != 0) {
                    int pixelValue = renderAreaPixels[pixel];
                    drawAlpha(renderAreaPixels,pixel++,0x000000 >> 8,255);
                } else {
                    pixel++;
                }
            }
            pixel += j1;
            i += l;
        }
    }

    private void renderARGBPixels(int spriteWidth, int spriteHeight, int spritePixels[], int renderAreaPixels[], int pixel, int alphaValue, int i, int l, int j1) {
        int pixelColor;
        int alphaLevel;
        int alpha = alphaValue;
        for (int height = -spriteHeight; height < 0; height++) {
            for (int width = -spriteWidth; width < 0; width++) {
                alphaValue = ((this.pixels[i] >> 24) & (alpha - 1));
                alphaLevel = 256 - alphaValue;
                if (alphaLevel > 256) {
                    alphaValue = 0;
                }
                if (alpha == 0) {
                    alphaLevel = 256;
                    alphaValue = 0;
                }
                pixelColor = spritePixels[i++];
                if (pixelColor != 0) {
                    int pixelValue = renderAreaPixels[pixel];
                    drawAlpha(renderAreaPixels,pixel++,((pixelColor & 0xff00ff) * alphaValue + (pixelValue & 0xff00ff) * alphaLevel & 0xff00ff00) + ((pixelColor & 0xff00) * alphaValue + (pixelValue & 0xff00) * alphaLevel & 0xff0000) >> 8,alphaValue);
                } else {
                    pixel++;
                }
            }
            pixel += j1;
            i += l;
        }
    }

    public void highlight(int color) {
        int[] pixels = new int[this.width * this.height];
        int index = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int src = this.pixels[index];
                if (src == 0) {
                    if (x > 0 && this.pixels[index - 1] != 0) {
                        src = color;
                    } else if (y > 0 && this.pixels[index - this.width] != 0) {
                        src = color;
                    } else if (x < this.width - 1 && this.pixels[index + 1] != 0) {
                        src = color;
                    } else if (y < this.height - 1 && this.pixels[index + this.width] != 0) {
                        src = color;
                    }
                }
                drawAlpha(pixels,index++,src,255);
            }
        }
        this.pixels = pixels;
    }

    public void shadow(int color) {
        for (int y = this.height - 1; y > 0; y--) {
            int pos = y * this.width;
            for (int x = this.width - 1; x > 0; x--) {
                if (this.pixels[x + pos] == 0 && this.pixels[x + pos - 1 - this.width] != 0) {
                    drawAlpha(pixels,x + pos,color,255);
                }
            }
        }
    }

    public Image convertToImage() {

        // Convert to buffered image
        BufferedImage bufferedimage = new BufferedImage(this.width, this.height, 1);
        bufferedimage.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);

        // Filter to ensure transparency preserved
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = Color.BLACK.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        // Create image
        ImageProducer ip = new FilteredImageSource(bufferedimage.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    int identifier;
    String name;

    public int getId() {
        return identifier;
    }

    public void setId(int id) {
        this.identifier = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getMyPixels() {
        return pixels;
    }

    public void setMyPixels(int[] myPixels) {
        this.pixels = myPixels;
    }

    public int getMyWidth() {
        return width;
    }

    public void setMyWidth(int myWidth) {
        this.width = myWidth;
    }

    public int getMyHeight() {
        return height;
    }

    public void setMyHeight(int myHeight) {
        this.height = myHeight;
    }

    public int getDrawOffsetX() {
        return x_offset;
    }

    public void setDrawOffsetX(int drawOffsetX) {
        this.x_offset = drawOffsetX;
    }

    public int getDrawOffsetY() {
        return y_offset;
    }

    public void setDrawOffsetY(int drawOffsetY) {
        this.y_offset = drawOffsetY;
    }

    @Override
    public void drawAt(int x, int y) {
        drawSprite(x, y);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getMaxWidth() {
        return max_width;
    }

    @Override
    public int getMaxHeight() {
        return max_height;
    }

    @Override
    public int getOffsetX() {
        return x_offset;
    }

    @Override
    public int getOffsetY() {
        return y_offset;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.max_width = maxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.max_height = maxHeight;
    }

    @Override
    public void setOffsetX(int offsetX) {
        x_offset = offsetX;
    }

    @Override
    public void setOffsetY(int offsetY) {
        y_offset = offsetY;
    }

    @Override
    public int[] getPixels() {
        return pixels;
    }

    @Override
    public void setRaster() {
        init();
    }

    @Override
    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(this.width, this.height, 2);
        toBufferedImage(image);
        return image;
    }

    @Override
    public void toBufferedImage(BufferedImage img) throws IllegalArgumentException {
        int width = getWidth();
        int height = getHeight();
        int[] pixels = getPixels();
        int[] palette = new int[pixels.length];
        for (int pixel = 0; pixel < pixels.length; pixel++) {
            if (pixels[pixel] != 0) {
                palette[pixel] = pixels[pixel] | 0xFF000000;
            }
        }
        img.setRGB(0, 0, width, height, palette, 0, width);
    }

    @Override
    public BufferedImage toBufferedOutline(Color color)
    {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        toBufferedOutline(img, color.getRGB());
        return img;
    }


    @Override
    public void toBufferedOutline(BufferedImage img, int color)
    {
        int width = getWidth();
        int height = getHeight();

        if (img.getWidth() != width || img.getHeight() != height)
        {
            throw new IllegalArgumentException("Image bounds do not match Sprite");
        }

        int[] pixels = getPixels();
        int[] newPixels = new int[width * height];
        int pixelIndex = 0;

        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < width; ++x)
            {
                int pixel = pixels[pixelIndex];
                if (pixel == 16777215 || pixel == 0)
                {
                    // W
                    if (x > 0 && pixels[pixelIndex - 1] != 0)
                    {
                        pixel = color;
                    }
                    // N
                    else if (y > 0 && pixels[pixelIndex - width] != 0)
                    {
                        pixel = color;
                    }
                    // E
                    else if (x < width - 1 && pixels[pixelIndex + 1] != 0)
                    {
                        pixel = color;
                    }
                    // S
                    else if (y < height - 1 && pixels[pixelIndex + width] != 0)
                    {
                        pixel = color;
                    }
                    newPixels[pixelIndex] = pixel;
                }

                pixelIndex++;
            }
        }

        img.setRGB(0, 0, width, height, newPixels, 0, width);
    }

    public static final SimpleImage EMPTY_SPRITE = new SimpleImage();

    SimpleImage(){}

    public static EvictingDualNodeHashTable cachedSprites = new EvictingDualNodeHashTable(256);

    public static SimpleImage getSprite(int id, int index) {

        SimpleImage sprite = (SimpleImage) cachedSprites.get(id);
        if (sprite != null) {
            return sprite;
        }

        byte[] spriteData = Js5List.sprites.takeFile(id, index);
        boolean decoded;
        if (spriteData == null) {
            decoded = false;
        } else {
            SpriteData.decode(spriteData);
            decoded = true;
        }
        if (!decoded) {
            return SimpleImage.EMPTY_SPRITE;
        } else {
            SimpleImage image = generateImage();
            cachedSprites.put(image, id);
            return image;
        }
    }

    public static SimpleImage[] getSprites(int var1, int index) {
        byte[] spriteData = Js5List.sprites.takeFile(var1, index);
        boolean decoded;
        if (spriteData == null) {
            decoded = false;
        } else {
            SpriteData.decode(spriteData);
            decoded = true;
        }

        return !decoded ? null : generateImages();
    }

    static SimpleImage generateImage() {
        SimpleImage sprite = new SimpleImage();
        sprite.max_width = SpriteData.spriteWidth;
        sprite.max_height = SpriteData.spriteHeight;
        sprite.x_offset = SpriteData.xOffsets[0];
        sprite.y_offset = SpriteData.yOffsets[0];
        sprite.width = SpriteData.spriteWidths[0];
        sprite.height = SpriteData.spriteHeights[0];
        int totalPixels = sprite.width * sprite.height;
        byte[] pixels = SpriteData.pixels[0];
        sprite.pixels = new int[totalPixels];

        for(int currentPixel = 0; currentPixel < totalPixels; ++currentPixel) {
            sprite.pixels[currentPixel] = SpriteData.spritePalette[pixels[currentPixel] & 255];
        }

        SpriteData.xOffsets = null;
        SpriteData.yOffsets = null;
        SpriteData.spriteWidths = null;
        SpriteData.spriteHeights = null;
        SpriteData.spritePalette = null;
        SpriteData.pixels = null;
        return sprite;
    }

    static SimpleImage[] generateImages() {
        SimpleImage[] sprites = new SimpleImage[SpriteData.spriteCount];

        for(int currentImage = 0; currentImage < SpriteData.spriteCount; ++currentImage) {
            SimpleImage var2 = sprites[currentImage] = new SimpleImage();
            var2.max_width = SpriteData.spriteWidth;
            var2.max_height = SpriteData.spriteHeight;
            var2.x_offset = SpriteData.xOffsets[currentImage];
            var2.y_offset = SpriteData.yOffsets[currentImage];
            var2.width = SpriteData.spriteWidths[currentImage];
            var2.height = SpriteData.spriteHeights[currentImage];
            int totalPixels = var2.width * var2.height;
            byte[] pixels = SpriteData.pixels[currentImage];
            var2.pixels = new int[totalPixels];

            for(int currentPixel = 0; currentPixel < totalPixels; ++currentPixel) {
                var2.pixels[currentPixel] = SpriteData.spritePalette[pixels[currentPixel] & 255];
            }
        }

        SpriteData.xOffsets = null;
        SpriteData.yOffsets = null;
        SpriteData.spriteWidths = null;
        SpriteData.spriteHeights = null;
        SpriteData.spritePalette = null;
        SpriteData.pixels = null;
        return sprites;
    }


}
