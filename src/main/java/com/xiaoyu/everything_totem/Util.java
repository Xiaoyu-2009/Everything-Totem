package com.xiaoyu.everything_totem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.registries.*;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class Util {
    public static final Map<ItemStack, String> itemSlotMapping = new WeakHashMap<>();

    public static class CheckResult {
        public boolean canTrigger;
        public boolean shouldConsume;
        
        public CheckResult(boolean canTrigger, boolean shouldConsume) {
            this.canTrigger = canTrigger;
            this.shouldConsume = shouldConsume;
        }
    }

    public static CheckResult checkItemStatusUniversal(ItemStack stack) {
        if (stack.isEmpty()) {
            return new CheckResult(false, false);
        }

        String slotType = itemSlotMapping.get(stack);
        
        return checkItemStatusForSpecificSlot(stack, slotType);
    }

    public static CheckResult checkItemStatusForSpecificSlot(ItemStack stack, String slotType) {
        if (stack.isEmpty()) {
            return new CheckResult(false, false);
        }

        if (slotType == null) {
            return new CheckResult(true, Config.consumeItemWhenEverythingIsTotem.get());
        }
        
        String itemName = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();

        switch (slotType) {
            case "mainhand":
                return checkItemInConfigList(itemName, 
                    Config.nonConsumableMainHandItem.get(), 
                    Config.consumableMainHandItem.get());
                
            case "offhand":
                return checkItemInConfigList(itemName, 
                    Config.nonConsumableOffHandItem.get(), 
                    Config.consumableOffHandItem.get());
                
            case "hotbar":
                return checkItemInConfigList(itemName, 
                    Config.nonConsumableHotbarItem.get(), 
                    Config.consumableHotbarItem.get());
                
            case "inventory":
                return checkItemInConfigList(itemName, 
                    Config.nonConsumableInventoryItem.get(), 
                    Config.consumableInventoryItem.get());
                
            case "armor":
                return checkItemInConfigList(itemName, 
                    Config.nonConsumableArmorItem.get(), 
                    Config.consumableArmorItem.get());
                
            case "curios":
                return checkItemInConfigList(itemName, 
                    Config.nonConsumableCuriosItem.get(), 
                    Config.consumableCuriosItem.get());
        }

        return new CheckResult(true, Config.consumeItemWhenEverythingIsTotem.get());
    }

    public static CheckResult checkItemInConfigList(String itemName, 
        List<? extends String> nonConsumableList, 
        List<? extends String> consumableList) {
        if (!nonConsumableList.isEmpty() && nonConsumableList.contains(itemName)) {
            return new CheckResult(true, false);
        }

        if (!consumableList.isEmpty() && consumableList.contains(itemName)) {
            return new CheckResult(true, true);
        }

        if (!nonConsumableList.isEmpty() || !consumableList.isEmpty()) {
            return new CheckResult(false, false);
        }

        return new CheckResult(true, Config.consumeItemWhenEverythingIsTotem.get());
    }
    
    public static boolean shouldConsumeItem(ItemStack stack) {
        CheckResult result = checkItemStatusUniversal(stack);
        return result.shouldConsume;
    }
    
    public static ItemStack findTotem(LivingEntity entity) {
        if (!Config.checkMainHand.get() && !Config.checkOffHand.get() && !Config.checkHotbar.get() &&
            !Config.checkInventory.get() && !Config.checkArmor.get() && !Config.checkCurios.get()) {
            return ItemStack.EMPTY;
        }

        if (Config.checkMainHand.get()) {
            ItemStack mainHandStack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            if (!mainHandStack.isEmpty()) {
                itemSlotMapping.put(mainHandStack, "mainhand");
                CheckResult result = checkItemStatusForSpecificSlot(mainHandStack, "mainhand");
                if (result.canTrigger) {
                    return mainHandStack;
                }
            }
        }

        if (Config.checkOffHand.get()) {
            ItemStack offHandStack = entity.getItemInHand(InteractionHand.OFF_HAND);
            if (!offHandStack.isEmpty()) {
                itemSlotMapping.put(offHandStack, "offhand");
                CheckResult result = checkItemStatusForSpecificSlot(offHandStack, "offhand");
                if (result.canTrigger) {
                    return offHandStack;
                }
            }
        }

        if (entity instanceof Player player) {
            if (Config.checkHotbar.get()) {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty()) {
                        itemSlotMapping.put(stack, "hotbar");
                        CheckResult result = checkItemStatusForSpecificSlot(stack, "hotbar");
                        if (result.canTrigger) {
                            return stack;
                        }
                    }
                }
            }

            if (Config.checkInventory.get()) {
                for (int i = 9; i < 36; i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty()) {
                        itemSlotMapping.put(stack, "inventory");
                        CheckResult result = checkItemStatusForSpecificSlot(stack, "inventory");
                        if (result.canTrigger) {
                            return stack;
                        }
                    }
                }
            }
        }

        if (Config.checkArmor.get()) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack stack = entity.getItemBySlot(slot);
                    if (!stack.isEmpty()) {
                        itemSlotMapping.put(stack, "armor");
                        CheckResult result = checkItemStatusForSpecificSlot(stack, "armor");
                        if (result.canTrigger) {
                            return stack;
                        }
                    }
                }
            }
        }

        if (Config.checkCurios.get() && ModList.get().isLoaded("curios")) {
            ItemStack curiosTotem = CuriosApi.getCuriosInventory(entity)
                .map(handler -> {
                    List<SlotResult> result = handler.findCurios(stack -> {
                        itemSlotMapping.put(stack, "curios");
                        CheckResult checkResult = checkItemStatusForSpecificSlot(stack, "curios");
                        return checkResult.canTrigger;
                    });
                    if (!result.isEmpty()) {
                        return result.get(0).stack();
                    }
                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);

            if (!curiosTotem.isEmpty()) {
                return curiosTotem;
            }
        }

        return ItemStack.EMPTY;
    }
}