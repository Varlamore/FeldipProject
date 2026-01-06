package com.cryptic.cache.graphics;


import com.cryptic.cache.Archive;
import com.cryptic.cache.def.graphics.SpriteData;
import com.cryptic.draw.Rasterizer2D;
import com.cryptic.io.Buffer;
import com.cryptic.js5.Js5List;

public final class IndexedImage extends Rasterizer2D {

    public int[] palette;
    public byte palettePixels[];
    public int subWidth;
    public int subHeight;
    public int xOffset;
    public int yOffset;
    public int width;
    public int height;


    public void setTransparency(int transRed, int transGreen, int transBlue) {
        for (int index = 0; index < palette.length; index++)
            if (((palette[index] >> 16) & 255) == transRed && ((palette[index] >> 8) & 255) == transGreen && (palette[index] & 255) == transBlue)
                palette[index] = 0;
    }

    public IndexedImage(Archive archive, String s, int i) {
        Buffer image = new Buffer(archive.get(s + ".dat"));
        Buffer meta = new Buffer(archive.get("index.dat"));
        meta.pos = image.readUnsignedShort();
        width = meta.readUnsignedShort();
        height = meta.readUnsignedShort();

        int colorLength = meta.readUnsignedByte();
        palette = new int[colorLength];

        for (int index = 0; index < colorLength - 1; index++) {
            palette[index + 1] = meta.readTriByte();
        }

        for (int l = 0; l < i; l++) {
            meta.pos += 2;
            image.pos += meta.readUnsignedShort() * meta.readUnsignedShort();
            meta.pos++;
        }
        xOffset = meta.readUnsignedByte();
        yOffset = meta.readUnsignedByte();
        subWidth = meta.readUnsignedShort();
        subHeight = meta.readUnsignedShort();
        int type = meta.readUnsignedByte();
        int pixels = subWidth * subHeight;
        palettePixels = new byte[pixels];

        if (type == 0) {
            for (int index = 0; index < pixels; index++) {
                palettePixels[index] = image.readSignedByte();
            }
        } else if (type == 1) {
            for (int x = 0; x < subWidth; x++) {
                for (int y = 0; y < subHeight; y++) {
                    palettePixels[x + y * subWidth] = image.readSignedByte();
                }
            }
        }

    }

    public void normalize() {
        if (subWidth != width || subHeight != height) { // L: 18
            byte[] pixels = new byte[width * height]; // L: 19
            int var2 = 0; // L: 20

            for (int var3 = 0; var3 < subHeight; ++var3) { // L: 21
                for (int var4 = 0; var4 < subWidth; ++var4) { // L: 22
                    pixels[var4 + (var3 + yOffset) * width + xOffset] = palettePixels[var2++]; // L: 23
                }
            }

            this.palettePixels = pixels; // L: 26
            subWidth = width; // L: 27
            subHeight = height; // L: 28
            xOffset = 0; // L: 29
            yOffset = 0; // L: 30
        }
    }
    public void resize() {
        if(subWidth == width && subHeight == height) {
            return;
        }

        byte raster[] = new byte[width * height];

        int i = 0;
        for(int y = 0; y < subHeight; y++) {
            for(int x = 0; x < subWidth; x++) {
                raster[x + xOffset + (y + yOffset) * width] = raster[i++];
            }
        }
        palettePixels = raster;
        subWidth = width;
        subHeight = height;
        xOffset = 0;
        yOffset = 0;
    }

    public void flipHorizontally() {
        byte raster[] = new byte[subWidth * subHeight];
        int pixel = 0;
        for (int y = 0; y < subHeight; y++) {
            for (int x = subWidth - 1; x >= 0; x--) {
                raster[pixel++] = raster[x + y * subWidth];
            }
        }
        palettePixels = raster;
        xOffset = width - subWidth - xOffset;
    }

    public void flipVertically() {
        byte raster[] = new byte[subWidth * subHeight];
        int pixel = 0;
        for (int y = subHeight - 1; y >= 0; y--) {
            for (int x = 0; x < subWidth; x++) {
                raster[pixel++] = raster[x + y * subWidth];
            }
        }
        palettePixels = raster;
        yOffset = height - subHeight - yOffset;
    }

    public void offsetColor(int redOffset, int greenOffset, int blueOffset) {
        for (int index = 0; index < palette.length; index++) {
            int red = palette[index] >> 16 & 0xff;
            red += redOffset;

            if (red < 0) {
                red = 0;
            } else if (red > 255) {
                red = 255;
            }

            int green = palette[index] >> 8 & 0xff;

            green += greenOffset;
            if (green < 0) {
                green = 0;
            } else if (green > 255) {
                green = 255;
            }

            int blue = palette[index] & 0xff;

            blue += blueOffset;
            if (blue < 0) {
                blue = 0;
            } else if (blue > 255) {
                blue = 255;
            }
            palette[index] = (red << 16) + (green << 8) + blue;
        }
    }

    public void draw(int x, int y) {
        x += xOffset;
        y += yOffset;
        int destOffset = x + y * Rasterizer2D.width;
        int sourceOffset = 0;
        int height = subHeight;
        int width = subWidth;
        int destStep = Rasterizer2D.width - width;
        int sourceStep = 0;

        if (y < Rasterizer2D.yClipStart) {
            int dy = Rasterizer2D.yClipStart - y;
            height -= dy;
            y = Rasterizer2D.yClipStart;
            sourceOffset += dy * width;
            destOffset += dy * Rasterizer2D.width;
        }

        if (y + height > Rasterizer2D.yClipEnd) {
            height -= (y + height) - Rasterizer2D.yClipEnd;
        }

        if (x < Rasterizer2D.xClipStart) {
            int k2 = Rasterizer2D.xClipStart - x;
            width -= k2;
            x = Rasterizer2D.xClipStart;
            sourceOffset += k2;
            destOffset += k2;
            sourceStep += k2;
            destStep += k2;
        }

        if (x + width > Rasterizer2D.xClipEnd) {
            int dx = (x + width) - Rasterizer2D.xClipEnd;
            width -= dx;
            sourceStep += dx;
            destStep += dx;
        }

        if (!(width <= 0 || height <= 0)) {
            draw(height, Rasterizer2D.pixels, palettePixels, destStep, destOffset, width, sourceOffset, palette, sourceStep);
        }

    }

    private void draw(int i, int raster[], byte image[], int destStep, int destIndex, int width, int sourceIndex, int ai1[], int sourceStep) {
        int minX = -(width >> 2);
        width = -(width & 3);
        for (int y = -i; y < 0; y++) {
            for (int x = minX; x < 0; x++) {

                byte pixel = image[sourceIndex++];

                if (pixel != 0) {
                    drawAlpha(raster,destIndex++,ai1[pixel & 0xff],255);
                } else {
                    destIndex++;
                }
                pixel = image[sourceIndex++];
                if (pixel != 0) {
                    drawAlpha(raster,destIndex++,ai1[pixel & 0xff],255);
                } else {
                    destIndex++;
                }
                pixel = image[sourceIndex++];
                if (pixel != 0) {
                    drawAlpha(raster,destIndex++,ai1[pixel & 0xff],255);
                } else {
                    destIndex++;
                }
                pixel = image[sourceIndex++];
                if (pixel != 0) {
                    drawAlpha(raster,destIndex++,ai1[pixel & 0xff],255);
                } else {
                    destIndex++;
                }
            }
            for (int x = width; x < 0; x++) {
                byte pixel = image[sourceIndex++];
                if (pixel != 0) {
                    drawAlpha(raster,destIndex++,ai1[pixel & 0xff],255);
                } else {
                    destIndex++;
                }
            }
            destIndex += destStep;
            sourceIndex += sourceStep;
        }
    }

    public static IndexedImage[] loadImages(int spriteID, int index) {
        byte[] spriteData = Js5List.sprites.takeFile(spriteID, index);
        boolean decoded;
        if (spriteData == null) {
            decoded = false;
        } else {
            SpriteData.decode(spriteData);
            decoded = true;
        }

        return !decoded ? null : generateImage();
    }

    public IndexedImage() {}

    static IndexedImage[] generateImage() {
        IndexedImage[] images = new IndexedImage[SpriteData.spriteCount];

        for(int currentImage = 0; currentImage < SpriteData.spriteCount; ++currentImage) {
            IndexedImage var2 = images[currentImage] = new IndexedImage();
            var2.width = SpriteData.spriteWidth;
            var2.height = SpriteData.spriteHeight;
            var2.xOffset = SpriteData.xOffsets[currentImage];
            var2.yOffset = SpriteData.yOffsets[currentImage];
            var2.subWidth = SpriteData.spriteWidths[currentImage];
            var2.subHeight = SpriteData.spriteHeights[currentImage];
            var2.palette = SpriteData.spritePalette;
            var2.palettePixels = SpriteData.pixels[currentImage];
        }

        SpriteData.xOffsets = null;
        SpriteData.yOffsets = null;
        SpriteData.spriteWidths = null;
        SpriteData.spriteHeights = null;
        SpriteData.spritePalette = null;
        SpriteData.pixels = null;
        return images;
    }

}