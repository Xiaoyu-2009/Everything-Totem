package com.xiaoyu.everything_totem;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

@Mod(EverythingTotem.MOD_ID)
public class EverythingTotem {
    public static final String MOD_ID = "everything_totem";
    
    @SuppressWarnings("removal")
    public EverythingTotem() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.BUILDER.build());
        FMLJavaModLoadingContext.get().getModEventBus().register(Config.class);
    }
}