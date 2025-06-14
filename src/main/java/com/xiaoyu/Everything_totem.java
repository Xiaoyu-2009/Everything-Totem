package com.xiaoyu;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.particle.ParticleTypes;
import com.xiaoyu.config.ModConfig;
import com.xiaoyu.network.NetworkHandler;

/**
 * 万物皆可图腾模组主类
 */
public class Everything_totem implements ModInitializer {
	public static final String MOD_ID = "everything_totem";

	@Override
	public void onInitialize() {
		// 注册配置
		ModConfig.register();
		
		// 初始化网络处理器
		NetworkHandler.init();

		net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, damageAmount) -> {
			if (entity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entity;
				PlayerInventory inventory = player.getInventory();
				
				ItemStack totemItem = null;
				int totemSlot = -1;
				Hand totemHand = null;

				if (ModConfig.GENERAL.checkMainHandFirst && !player.getMainHandStack().isEmpty()) {
					totemItem = player.getMainHandStack();
					totemHand = Hand.MAIN_HAND;
					totemSlot = inventory.selectedSlot;
				}
				else if (ModConfig.GENERAL.checkOffHandFirst && !player.getOffHandStack().isEmpty()) {
					totemItem = player.getOffHandStack();
					totemHand = Hand.OFF_HAND;
					totemSlot = 40;
				}
				else if (ModConfig.GENERAL.checkInventory) {
					for (int i = 0; i < inventory.size(); i++) {
						ItemStack stack = inventory.getStack(i);
						if (!stack.isEmpty()) {
							totemItem = stack;
							totemSlot = i;
							break;
						}
					}
				}

				if (totemItem != null) {
					player.setHealth(1.0F);

					player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
					
					player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

					if (player.getWorld() instanceof ServerWorld) {
						ServerWorld serverWorld = (ServerWorld) player.getWorld();
						serverWorld.spawnParticles(
							ParticleTypes.TOTEM_OF_UNDYING,
							player.getX(), player.getY() + 1.0D, player.getZ(),
							30, 0.3D, 0.3D, 0.3D, 0.5D
						);
					}

					if (player instanceof ServerPlayerEntity) {
						NetworkHandler.sendToPlayer(
							totemItem,
							(ServerPlayerEntity) player
						);
					}

					if (totemHand != null) {
						totemItem.decrement(1);
						if (totemItem.isEmpty()) {
							player.setStackInHand(totemHand, ItemStack.EMPTY);
						}
					} 
					else if (totemSlot != -1) {
						ItemStack slotStack = inventory.getStack(totemSlot);
						if (!slotStack.isEmpty()) {
							slotStack.decrement(1);
							if (slotStack.isEmpty()) {
								inventory.setStack(totemSlot, ItemStack.EMPTY);
							}
						}
					}
					
					return false;
				}
			}
			return true;
		});
	}
}