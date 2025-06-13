package com.xiaoyu.divinecreation.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class General {
        public final ForgeConfigSpec.BooleanValue consumeItems;
        public final ForgeConfigSpec.BooleanValue checkMainHandFirst;
        public final ForgeConfigSpec.BooleanValue checkOffHandFirst;
        public final ForgeConfigSpec.BooleanValue checkInventory;

        General(ForgeConfigSpec.Builder builder) {
            builder.comment("万物皆可图腾 - 通用设置").push("general");

            consumeItems = builder
                    .comment("当物品触发图腾效果后是否消耗物品")
                    .define("consumeItems", true);

            checkMainHandFirst = builder
                    .comment("是否优先检查主手物品")
                    .define("checkMainHandFirst", true);

            checkOffHandFirst = builder
                    .comment("是否优先检查副手物品（仅当主手检查关闭或主手无物品时生效）")
                    .define("checkOffHandFirst", true);

            checkInventory = builder
                    .comment("是否检查背包中的物品（仅当主手和副手都无物品或检查关闭时生效）")
                    .define("checkInventory", true);

            builder.pop();
        }
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(Type.COMMON, SPEC, "everything_totem.toml");
    }
}