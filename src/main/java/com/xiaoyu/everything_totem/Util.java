package com.xiaoyu.everything_totem;

import net.minecraft.world.item.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.*;

public class Util {
    public static boolean canItemActAsTotem(ItemStack itemStack) {
        ItemCheckResult result = checkItemStatus(itemStack);
        return result.canTrigger;
    }
    
    public static boolean shouldConsumeItem(ItemStack itemStack) {
        ItemCheckResult result = checkItemStatus(itemStack);
        return result.shouldConsume;
    }
    
    private static ItemCheckResult checkItemStatus(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return new ItemCheckResult(false, false);
        }

        String itemString = getItemString(itemStack);
        if (itemString == null) {
            return new ItemCheckResult(false, false);
        }
        
        List<? extends String> nonConsumableItems = Config.nonConsumableItems.get();
        List<? extends String> consumableItems = Config.consumableItems.get();

        if (nonConsumableItems.isEmpty() && consumableItems.isEmpty()) {
            return new ItemCheckResult(true, Config.consumeItem.get());
        }

        if (!nonConsumableItems.isEmpty() && nonConsumableItems.contains(itemString)) {
            return new ItemCheckResult(true, false);
        }

        if (!consumableItems.isEmpty() && consumableItems.contains(itemString)) {
            return new ItemCheckResult(true, true);
        }

        return new ItemCheckResult(false, false);
    }
    
    private static String getItemString(ItemStack itemStack) {
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        if (itemKey == null) {
            return null;
        }
        return itemKey.toString();
    }

    private static class ItemCheckResult {
        final boolean canTrigger;
        final boolean shouldConsume;
        
        ItemCheckResult(boolean canTrigger, boolean shouldConsume) {
            this.canTrigger = canTrigger;
            this.shouldConsume = shouldConsume;
        }
    }
    
    public static ItemStack findTotemInHands(Player player) {
        ItemStack mainHandStack = ItemStack.EMPTY;
        ItemStack offHandStack = ItemStack.EMPTY;
        
        if (shouldCheckMainHand()) {
            mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (canItemActAsTotem(mainHandStack)) {
                return mainHandStack;
            }
        }
        
        if (shouldCheckOffHand()) {
            offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);
            if (canItemActAsTotem(offHandStack)) {
                return offHandStack;
            }
        }
        
        return ItemStack.EMPTY;
    }
    
    public static ItemStack findTotemInHotbar(Player player) {
        if (shouldCheckHotbar()) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (canItemActAsTotem(stack)) {
                    return stack;
                }
            }
        }
        
        return ItemStack.EMPTY;
    }
    
    public static ItemStack findTotemInInventory(Player player) {
        if (shouldCheckInventory()) {
            for (int i = 9; i < 36; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (canItemActAsTotem(stack)) {
                    return stack;
                }
            }
        }
        
        return ItemStack.EMPTY;
    }
    
    public static ItemStack findTotem(Player player) {
        if (!shouldCheckMainHand() && !shouldCheckOffHand() && 
            !shouldCheckHotbar() && !shouldCheckInventory()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack handTotem = findTotemInHands(player);
        if (!handTotem.isEmpty()) {
            return handTotem;
        }
        
        ItemStack hotbarTotem = findTotemInHotbar(player);
        if (!hotbarTotem.isEmpty()) {
            return hotbarTotem;
        }
        
        ItemStack inventoryTotem = findTotemInInventory(player);
        if (!inventoryTotem.isEmpty()) {
            return inventoryTotem;
        }
        
        return ItemStack.EMPTY;
    }
    
    public static boolean shouldCheckMainHand() {
        return Config.checkMainHand.get();
    }
    
    public static boolean shouldCheckOffHand() {
        return Config.checkOffHand.get();
    }
    
    public static boolean shouldCheckHotbar() {
        return Config.checkHotbar.get();
    }
    
    public static boolean shouldCheckInventory() {
        return Config.checkInventory.get();
    }
}