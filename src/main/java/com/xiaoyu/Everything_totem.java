package com.xiaoyu;

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
import com.xiaoyu.config.ModConfig;
import com.xiaoyu.network.NetworkHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

@Mod("everything_totem")
public class Everything_totem {
    
    public static final String MOD_ID = "everything_totem";
    
    public Everything_totem() {
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
                totemSlot = 40;
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

            if (totemItem != null) {
                event.setCanceled(true);

                player.setHealth(1.0F);

                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

                if (player.level() instanceof ServerLevel) {
                    ServerLevel serverLevel = (ServerLevel) player.level();
                    serverLevel.sendParticles(
                        ParticleTypes.TOTEM_OF_UNDYING,
                        player.getX(), player.getY() + 1.0D, player.getZ(),
                        30, 0.3D, 0.3D, 0.3D, 0.5D
                    );
                }

                if (player instanceof ServerPlayer) {
                    NetworkHandler.sendToPlayer(
                        totemItem,
                        (ServerPlayer) player
                    );
                }

                if (totemHand != null) {
                    totemItem.shrink(1);
                    if (totemItem.isEmpty()) {
                        player.setItemInHand(totemHand, ItemStack.EMPTY);
                    }
                } 
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