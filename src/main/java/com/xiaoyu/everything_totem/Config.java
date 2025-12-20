package com.xiaoyu.everything_totem;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    
    public static ForgeConfigSpec.BooleanValue consumeItemWhenEverythingIsTotem;
    public static ForgeConfigSpec.BooleanValue checkMainHand;
    public static ForgeConfigSpec.BooleanValue checkOffHand;
    public static ForgeConfigSpec.BooleanValue checkHotbar;
    public static ForgeConfigSpec.BooleanValue checkInventory;
    public static ForgeConfigSpec.BooleanValue checkCurios;
    public static ForgeConfigSpec.BooleanValue checkArmor;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableMainHandItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableMainHandItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableOffHandItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableOffHandItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableHotbarItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableHotbarItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableInventoryItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableInventoryItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableArmorItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableArmorItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableCuriosItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableCuriosItem;
    
    static {
        BUILDER.push("Everything Totem Config");

        consumeItemWhenEverythingIsTotem = BUILDER
                .comment("Whether to consume item when everything can act as totem")
                .define("consumeItemWhenEverythingIsTotem", true);
                
        checkMainHand = BUILDER
                .comment("Whether to check main hand item")
                .define("checkMainHand", true);
                
        checkOffHand = BUILDER
                .comment("Whether to check off hand item")
                .define("checkOffHand", true);
                
        checkHotbar = BUILDER
                .comment("Whether to check item in hotbar")
                .define("checkHotbar", true);
                
        checkInventory = BUILDER
                .comment("Whether to check item in inventory")
                .define("checkInventory", true);

        checkArmor = BUILDER
                .comment("Whether to check item in armor slots")
                .define("checkArmor", true);

        checkCurios = BUILDER
                .comment("Whether to check item in curios slot")
                .define("checkCurios", true);

        nonConsumableMainHandItem = BUILDER
                .comment("List of main hand items that will not be consumed after triggering totem effect")
                .defineList("nonConsumableMainHandItem", List.of(), obj -> obj instanceof String);
                
        consumableMainHandItem = BUILDER
                .comment("List of main hand items that will be consumed after triggering totem effect")
                .defineList("consumableMainHandItem", List.of(), obj -> obj instanceof String);
                
        nonConsumableOffHandItem = BUILDER
                .comment("List of off hand items that will not be consumed after triggering totem effect")
                .defineList("nonConsumableOffHandItem", List.of(), obj -> obj instanceof String);
                
        consumableOffHandItem = BUILDER
                .comment("List of off hand items that will be consumed after triggering totem effect")
                .defineList("consumableOffHandItem", List.of(), obj -> obj instanceof String);
                
        nonConsumableHotbarItem = BUILDER
                .comment("List of hotbar items that will not be consumed after triggering totem effect")
                .defineList("nonConsumableHotbarItem", List.of(), obj -> obj instanceof String);
                
        consumableHotbarItem = BUILDER
                .comment("List of hotbar items that will be consumed after triggering totem effect")
                .defineList("consumableHotbarItem", List.of(), obj -> obj instanceof String);
                
        nonConsumableInventoryItem = BUILDER
                .comment("List of inventory items that will not be consumed after triggering totem effect")
                .defineList("nonConsumableInventoryItem", List.of(), obj -> obj instanceof String);
                
        consumableInventoryItem = BUILDER
                .comment("List of inventory items that will be consumed after triggering totem effect")
                .defineList("consumableInventoryItem", List.of(), obj -> obj instanceof String);
                
        nonConsumableArmorItem = BUILDER
                .comment("List of armor item that will not be consumed after triggering totem effect")
                .defineList("nonConsumableArmorItem", List.of(), obj -> obj instanceof String);
                
        consumableArmorItem = BUILDER
                .comment("List of armor item that will be consumed after triggering totem effect")
                .defineList("consumableArmorItem", List.of(), obj -> obj instanceof String);
                
        nonConsumableCuriosItem = BUILDER
                .comment("List of curios item that will not be consumed after triggering totem effect")
                .defineList("nonConsumableCuriosItem", List.of(), obj -> obj instanceof String);
                
        consumableCuriosItem = BUILDER
                .comment("List of curios item that will be consumed after triggering totem effect")
                .defineList("consumableCuriosItem", List.of(), obj -> obj instanceof String);
                
        BUILDER.pop();
    }
}