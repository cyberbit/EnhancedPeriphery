package dev.cyberbit.enhancedperiphery;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(EnhancedPeriphery.MODID)
public class EnhancedPeriphery {
    public static final String MODID = "enhancedperiphery";

    public EnhancedPeriphery() {
        Registration.register();

        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}