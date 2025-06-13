package com.xiaoyu.divinecreation.network;

import java.util.function.Supplier;
import com.xiaoyu.divinecreation.client.TotemAnimationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class TotemEffectMessage {
    private final int playerId;
    private final ItemStack itemStack;

    public TotemEffectMessage(int playerId, ItemStack itemStack) {
        this.playerId = playerId;
        this.itemStack = itemStack.copy();
    }

    public static void encode(TotemEffectMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.playerId);
        buffer.writeItem(message.itemStack);
    }

    public static TotemEffectMessage decode(FriendlyByteBuf buffer) {
        return new TotemEffectMessage(buffer.readInt(), buffer.readItem());
    }

    public static void handle(TotemEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient(message));
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(TotemEffectMessage message) {
        Minecraft minecraft = Minecraft.getInstance();

        // 启动图腾动画
        TotemAnimationHandler.startAnimation(message.itemStack);
    }
}