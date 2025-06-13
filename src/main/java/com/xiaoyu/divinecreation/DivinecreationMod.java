package com.xiaoyu.divinecreation;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.xiaoyu.divinecreation.config.ModConfig;
import com.xiaoyu.divinecreation.network.NetworkHandler;
import com.xiaoyu.divinecreation.network.TotemAnimationMessage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

@Mod("everything_totem")
public class DivinecreationMod {

    public DivinecreationMod() {
        // 注册事件监听器
        MinecraftForge.EVENT_BUS.register(this);

        // 注册配置
        ModConfig.register();

        // 注册设置事件
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // 初始化网络处理器
        NetworkHandler.init();
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Inventory inventory = player.getInventory();

            ItemStack totemItem = null;
            int totemSlot = -1;
            InteractionHand totemHand = null;

            // 检查主手
            if (ModConfig.GENERAL.checkMainHandFirst.get() && !player.getMainHandItem().isEmpty()) {
                totemItem = player.getMainHandItem();
                totemHand = InteractionHand.MAIN_HAND;
                totemSlot = inventory.selected;
            }
            // 检查副手
            else if (ModConfig.GENERAL.checkOffHandFirst.get() && !player.getOffhandItem().isEmpty()) {
                totemItem = player.getOffhandItem();
                totemHand = InteractionHand.OFF_HAND;
                totemSlot = 40; // 副手槽位
            }
            // 检查整个背包
            else if (ModConfig.GENERAL.checkInventory.get()) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty()) {
                        totemItem = stack;
                        totemSlot = i;
                        break;
                    }
                }
            }

            // 如果找到任何物品，触发图腾效果
            if (totemItem != null) {
                // 取消死亡事件
                event.setCanceled(true);

                // 设置玩家生命值
                player.setHealth(1.0F);

                // 添加生命恢复效果
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));

                // 播放图腾使用音效
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

                // 创建图腾使用的粒子效果
                if (player.level() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) player.level();
                    serverLevel.sendParticles(
                            ParticleTypes.TOTEM_OF_UNDYING,
                            player.getX(), player.getY() + 1.0D, player.getZ(),
                            30, 0.3D, 0.3D, 0.3D, 0.5D);
                }

                // 发送网络消息到客户端，触发图腾动画
                if (player instanceof ServerPlayer) {
                    NetworkHandler.sendToPlayer(
                            new TotemAnimationMessage(totemItem),
                            (ServerPlayer) player);
                }

                // 如果是手持物品，则模拟原版图腾效果
                if (totemHand != null) {
                    // 消耗物品
                    totemItem.shrink(1);
                    if (totemItem.isEmpty()) {
                        player.setItemInHand(totemHand, ItemStack.EMPTY);
                    }
                }
                // 否则消耗背包中的物品
                else if (totemSlot != -1) {
                    ItemStack slotStack = inventory.getItem(totemSlot);
                    if (!slotStack.isEmpty()) {
                        slotStack.shrink(1);
                        if (slotStack.isEmpty()) {
                            inventory.setItem(totemSlot, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }
}