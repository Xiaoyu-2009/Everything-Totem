package com.xiaoyu.divinecreation.network;

import java.util.function.Supplier;

import com.xiaoyu.divinecreation.client.ClientEventHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class TotemAnimationMessage {
    private final ItemStack itemStack;

    public TotemAnimationMessage(ItemStack itemStack) {
        this.itemStack = itemStack.copy();
    }

    public static void encode(TotemAnimationMessage message, FriendlyByteBuf buffer) {
        buffer.writeItem(message.itemStack);
    }

    public static TotemAnimationMessage decode(FriendlyByteBuf buffer) {
        return new TotemAnimationMessage(buffer.readItem());
    }

    public static void handle(TotemAnimationMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // 确保在客户端线程执行
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // 触发图腾动画
                ClientEventHandler.triggerTotemAnimation(message.itemStack);
            });
        });
        context.setPacketHandled(true);
    }
}