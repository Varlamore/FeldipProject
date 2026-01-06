package com.cryptic.cache.graphics.textures;

public interface TextureLoader {

    int[] getTexturePixels(int var1);

    int getAverageTextureRGB(int var1);

    boolean isTransparent(int var1);

    boolean isLowDetail(int var1);

}
