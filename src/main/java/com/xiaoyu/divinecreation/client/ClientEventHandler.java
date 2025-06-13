package com.xiaoyu.divinecreation.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "everything_totem", value = Dist.CLIENT)
public class ClientEventHandler {
    
    // 客户端触发图腾动画
    public static void triggerTotemAnimation(ItemStack item) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        
        if (player != null) {
            // 传入触发的物品动画
            minecraft.gameRenderer.displayItemActivation(item);
        }
    }
} 