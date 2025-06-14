package com.xiaoyu.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import com.xiaoyu.Everything_totem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Everything_totem.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    
    /**
     * 客户端触发图腾动画
     */
    public static void triggerTotemAnimation(ItemStack item) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        
        if (player != null) {
            minecraft.gameRenderer.displayItemActivation(item);
        }
    }
} 