package com.xiaoyu.everything_totem;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    
    public static ForgeConfigSpec.BooleanValue consumeItem;
    public static ForgeConfigSpec.BooleanValue checkMainHand;
    public static ForgeConfigSpec.BooleanValue checkOffHand;
    public static ForgeConfigSpec.BooleanValue checkHotbar;
    public static ForgeConfigSpec.BooleanValue checkInventory;
    public static ForgeConfigSpec.BooleanValue checkCurios;
    public static ForgeConfigSpec.BooleanValue checkArmor;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableCuriosItem;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableCuriosItem;
    
    static {
        BUILDER.push("Everything Totem Config");
        
        consumeItem = BUILDER
                .comment("Whether to consume the item after triggering totem effect")
                .define("consumeItem", true);
                
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
                
        checkCurios = BUILDER
                .comment("Whether to check item in curios slot")
                .define("checkCurios", true);
                
        checkArmor = BUILDER
                .comment("Whether to check item in armor slots")
                .define("checkArmor", true);
                
        nonConsumableItem = BUILDER
                .comment("List of item that will not be consumed after triggering totem effect")
                .defineList("nonConsumableItem", List.of(), obj -> obj instanceof String);
                
        consumableItem = BUILDER
                .comment("List of item that will be consumed after triggering totem effect")
                .defineList("consumableItem", List.of(), obj -> obj instanceof String);
                
        nonConsumableCuriosItem = BUILDER
                .comment("List of curios item that will not be consumed after triggering totem effect")
                .defineList("nonConsumableCuriosItem", List.of(), obj -> obj instanceof String);
                
        consumableCuriosItem = BUILDER
                .comment("List of curios item that will be consumed after triggering totem effect")
                .defineList("consumableCuriosItem", List.of(), obj -> obj instanceof String);
                
        BUILDER.pop();
    }
}