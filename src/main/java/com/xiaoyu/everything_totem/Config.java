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
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nonConsumableItems;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> consumableItems;
    
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
                .comment("Whether to check items in hotbar")
                .define("checkHotbar", true);
                
        checkInventory = BUILDER
                .comment("Whether to check items in inventory")
                .define("checkInventory", true);
                
        nonConsumableItems = BUILDER
                .comment("List of items that will not be consumed after triggering totem effect")
                .defineList("nonConsumableItems", List.of(), obj -> obj instanceof String);
                
        consumableItems = BUILDER
                .comment("List of items that will be consumed after triggering totem effect")
                .defineList("consumableItems", List.of(), obj -> obj instanceof String);
                
        BUILDER.pop();
    }
    
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}