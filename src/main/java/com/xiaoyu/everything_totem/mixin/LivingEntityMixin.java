package com.xiaoyu.everything_totem.mixin;

import com.xiaoyu.everything_totem.*;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    public boolean redirectTotemCheck(ItemStack itemStack, Item item) {
        return Util.canItemActAsTotem(itemStack);
    }
    
    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    public ItemStack redirectGetItemInHand(LivingEntity livingEntity, InteractionHand interactionHand) {
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            ItemStack totem = Util.findTotem(player);
            if (!totem.isEmpty()) {
                return totem;
            }
        }
        
        return livingEntity.getItemInHand(interactionHand);
    }
    
    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
        )
    )
    public void redirectShrink(ItemStack itemStack, int count) {
        if (Util.shouldConsumeItem(itemStack)) {
            itemStack.shrink(count);
        }
    }
}