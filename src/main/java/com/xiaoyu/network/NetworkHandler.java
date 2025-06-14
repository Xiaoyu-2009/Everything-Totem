package com.xiaoyu.network;

import com.xiaoyu.Everything_totem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.item.ItemStack;

/**
 * 服务端网络处理类
 */
public class NetworkHandler {
    public static final Identifier TOTEM_ANIMATION_PACKET_ID = 
            new Identifier(Everything_totem.MOD_ID, "totem_animation");
    
    public static void init() {}

    public static void sendToPlayer(ItemStack itemStack, ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeItemStack(itemStack);
        ServerPlayNetworking.send(player, TOTEM_ANIMATION_PACKET_ID, buf);
    }
} 