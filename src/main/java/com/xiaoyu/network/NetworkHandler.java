package com.xiaoyu.network;

import com.xiaoyu.Everything_totem;
import com.xiaoyu.client.ClientEventHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Everything_totem.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, TotemAnimationPacket.class, 
                TotemAnimationPacket::encode, 
                TotemAnimationPacket::decode, 
                TotemAnimationPacket::handle);
    }

    public static void sendToPlayer(ItemStack itemStack, ServerPlayer player) {
        INSTANCE.sendTo(new TotemAnimationPacket(itemStack), 
                player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static class TotemAnimationPacket {
        private final ItemStack itemStack;

        public TotemAnimationPacket(ItemStack itemStack) {
            this.itemStack = itemStack.copy();
        }

        public static void encode(TotemAnimationPacket message, net.minecraft.network.FriendlyByteBuf buffer) {
            buffer.writeItem(message.itemStack);
        }

        public static TotemAnimationPacket decode(net.minecraft.network.FriendlyByteBuf buffer) {
            return new TotemAnimationPacket(buffer.readItem());
        }

        public static void handle(TotemAnimationPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    ClientEventHandler.triggerTotemAnimation(message.itemStack);
                });
            });
            context.setPacketHandled(true);
        }
    }
} 