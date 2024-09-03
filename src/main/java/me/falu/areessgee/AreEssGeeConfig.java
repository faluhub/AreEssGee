package me.falu.areessgee;

import org.mcsr.speedrunapi.config.SpeedrunConfigContainer;
import org.mcsr.speedrunapi.config.api.SpeedrunConfig;
import org.mcsr.speedrunapi.config.api.annotations.Config;
import org.mcsr.speedrunapi.config.api.annotations.InitializeOn;

@InitializeOn(InitializeOn.InitPoint.PRELAUNCH)
public class AreEssGeeConfig implements SpeedrunConfig {
    @Config.Category("position")
    @Config.Numbers.Whole.Bounds(min = 1, max = 10)
    public int randomOffset = 3;
    @Config.Category("position")
    @Config.Numbers.Whole.Bounds(min = 1, max = 6)
    public int randomBound = 4;
    @Config.Category("basalt")
    public String basaltReplacement = "nether_wastes";
    @Config.Category("basalt")
    @Config.Numbers.Whole.Bounds(min = 0, max = 200)
    public int basaltReplacementRange = 120;
    @Config.Category("basalt")
    public boolean basaltBastions = true;
    @Config.Category("luck")
    @Config.Numbers.Fractional.Bounds(min = 0.0F, max = 1.0F)
    public float flintRarity = 0.6F;
    @Config.Category("luck")
    @Config.Numbers.Fractional.Bounds(min = 0.0F, max = 1.0F)
    public float rodRarity = 0.4F;
    @Config.Category("luck")
    @Config.Numbers.Fractional.Bounds(min = 0.0F, max = 1.0F)
    public float buriedTreasureRarity = 0.07F;
    @Config.Category("luck")
    @Config.Numbers.Fractional.Bounds(min = 0.0F, max = 1.0F)
    public float oceanRavineRarity = 0.04F;
    @Config.Category("luck")
    @Config.Numbers.Fractional.Bounds(min = 0.0F, max = 1.0F)
    public float treasureGoldRarity = 0.2F;
    @Config.Category("luck")
    @Config.Numbers.Fractional.Bounds(min = 0.0F, max = 1.0F)
    public float portalEyeRarity = 0.9F;
    @Config.Category("luck")
    public boolean guaranteeEyeDrops = true;
    @Config.Category("misc")
    @Config.Numbers.Whole.Bounds(min = 1, max = 32)
    public int maxStrongholdDepth = 8;
    @Config.Category("misc")
    public boolean openNetherTerrain = true;
    @Config.Ignored
    public SpeedrunConfigContainer<?> container;

    {
        AreEssGee.CONFIG = this;
    }

    @Override
    public String modID() {
        return AreEssGee.MOD_CONTAINER.getMetadata().getId();
    }

    @Override
    public void finishInitialization(SpeedrunConfigContainer<?> container) {
        this.container = container;
    }
}
