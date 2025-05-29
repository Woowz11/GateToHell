package net.minecraft.client.renderer;

import com.gatetohell.Curses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PanoramaRenderer {
    public static final ResourceLocation PANORAMA_OVERLAY = ResourceLocation.withDefaultNamespace("textures/gui/title/background/panorama_overlay.png");
    private final Minecraft minecraft;
    private final CubeMap cubeMap;
    private float spin;

    public PanoramaRenderer(CubeMap p_110002_) {
        this.cubeMap = p_110002_;
        this.minecraft = Minecraft.getInstance();
    }

    public void render(GuiGraphics gg, int w, int h, float p_110004_, float p_110005_) {
        if (!Curses.ClearPanorama) {
            float f = this.minecraft.getDeltaTracker().getRealtimeDeltaTicks();
            float f1 = (float) ((double) f * this.minecraft.options.panoramaSpeed().get());
            this.spin = wrap(this.spin + f1 * 0.1F, 360.0F);
            gg.flush();
            this.cubeMap.render(this.minecraft, 10.0F, -this.spin, p_110004_);
            gg.flush();
            gg.blit(RenderType::guiTextured, PANORAMA_OVERLAY, 0, 0, 0.0F, 0.0F, w, h, 16, 128, 16, 128, ARGB.white(p_110004_));
        }else{
            gg.flush();
            gg.fill(0, 0, w, h, ARGB.color(0, 0, 0, 128));
        }
    }

    private static float wrap(float p_249058_, float p_249548_) {
        return p_249058_ > p_249548_ ? p_249058_ - p_249548_ : p_249058_;
    }
}