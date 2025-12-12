package com.xiaoyu.everything_totem.mixin.client;

import com.xiaoyu.everything_totem.*;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    
    @Redirect(
        method = "findTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean redirectFindTotemCheck(ItemStack itemStack, Item item) {
        return Util.canItemActAsTotem(itemStack);
    }
    
    @Redirect(
        method = "findTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack redirectGetItemInHand(Player player, InteractionHand interactionHand) {
        ItemStack totem = Util.findTotem(player);
        if (!totem.isEmpty()) {
            return totem;
        }
        
        return player.getItemInHand(interactionHand);
    }
}