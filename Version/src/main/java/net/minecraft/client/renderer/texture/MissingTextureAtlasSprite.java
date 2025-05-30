package net.minecraft.client.renderer.texture;

import com.gatetohell.Enums;
import com.gatetohell.Helper;
import com.gatetohell.Initializing;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.ARGB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MissingTextureAtlasSprite {
    public static final int MISSING_IMAGE = 16;
    private static final String MISSING_TEXTURE_NAME = "missingno";
    private static final ResourceLocation MISSING_TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace(MISSING_TEXTURE_NAME);

    private static NativeImage Generated = null;
    public static NativeImage generateMissingImage(int x, int y) {
        if(Generated != null){ return Generated; }

        NativeImage nativeimage = new NativeImage(x, y, false);

        if(Initializing.MissingoType == Enums.MissingoType.None || Initializing.MissingoType == Enums.MissingoType.Red) {
            int Color1 = ARGB.color(0, 0, 0);
            int Color2 = ARGB.color(255, 0, (Initializing.MissingoType == Enums.MissingoType.Red ? 0 : 255));

            for (int j = 0; j < y; j++) {
                for (int k = 0; k < x; k++) {
                    if (j < y / 2 ^ k < x / 2) {
                        nativeimage.setPixel(k, j, Color1);
                    } else {
                        nativeimage.setPixel(k, j, Color2);
                    }
                }
            }
        }else{
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < x; k++) {
                    int Color = ARGB.color(255,0,255);
                    switch (Initializing.MissingoType){
                        case White -> Color = ARGB.color(255,255,255);
                        case Black -> Color = ARGB.color(0,0,0);
                        case Noise -> {
                            int Rand = Helper.RandomI(0,255);
                            Color = ARGB.color(Rand,Rand,Rand);
                        }
                        case ColorNoise -> Color = ARGB.color(Helper.RandomI(0,255), Helper.RandomI(0,255), Helper.RandomI(0,255));
                    }
                    nativeimage.setPixel(k, j, Color);
                }
            }
        }

        if(Initializing.MissingoType != Enums.MissingoType.Noise && Initializing.MissingoType != Enums.MissingoType.ColorNoise) {
            Generated = nativeimage;
        }

        return nativeimage;
    }

    public static NativeImage generateMissingImage() {
        return generateMissingImage(MISSING_IMAGE, MISSING_IMAGE);
    }

    public static SpriteContents create() {
        NativeImage nativeimage = generateMissingImage();
        return new SpriteContents(MISSING_TEXTURE_LOCATION, new FrameSize(MISSING_IMAGE, MISSING_IMAGE), nativeimage, ResourceMetadata.EMPTY);
    }

    public static ResourceLocation getLocation() {
        return MISSING_TEXTURE_LOCATION;
    }
}