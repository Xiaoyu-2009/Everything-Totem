package com.xiaoyu.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.xiaoyu.Everything_totem;

/**
 * 客户端初始化和事件处理
 */
@Environment(EnvType.CLIENT)
public class ClientEventHandler implements ClientModInitializer {
    private static final Identifier TOTEM_ANIMATION_PACKET_ID = new Identifier(Everything_totem.MOD_ID,
            "totem_animation");

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                TOTEM_ANIMATION_PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    ItemStack itemStack = buf.readItemStack();
                    client.execute(() -> {
                        triggerTotemAnimation(itemStack);
                    });
                });
    }

    /**
     * 客户端触发图腾动画
     */
    private static void triggerTotemAnimation(ItemStack item) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        if (minecraft.player != null) {
            minecraft.gameRenderer.showFloatingItem(item);
        }
    }
}