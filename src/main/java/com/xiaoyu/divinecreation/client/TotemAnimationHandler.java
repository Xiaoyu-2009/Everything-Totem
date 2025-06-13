package com.xiaoyu.divinecreation.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "everything_totem", value = Dist.CLIENT)
public class TotemAnimationHandler {

    private static final ResourceLocation TOTEM_LOCATION = new ResourceLocation("textures/misc/totem_of_undying.png");
    private static ItemStack lastUsedItem = ItemStack.EMPTY;
    private static long animationStartTime = 0;
    private static boolean showAnimation = false;

    /**
     * 开始显示图腾动画
     * @param item 触发图腾效果的物品
     */
    public static void startAnimation(ItemStack item) {
        lastUsedItem = item.copy();
        animationStartTime = System.currentTimeMillis();
        showAnimation = true;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!showAnimation) return;

        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
            Minecraft minecraft = Minecraft.getInstance();
            GuiGraphics guiGraphics = event.getGuiGraphics();
            PoseStack poseStack = guiGraphics.pose();

            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - animationStartTime;

            // 动画持续时间
            if (elapsedTime > 3000) {
                showAnimation = false;
                return;
            }

            int width = minecraft.getWindow().getGuiScaledWidth();
            int height = minecraft.getWindow().getGuiScaledHeight();

            float scale;
            float alpha;
            float rotation;
            float distance;

            // 动画阶段
            if (elapsedTime < 1000) {
                distance = (1000 - elapsedTime) / 1000.0F * 200.0F;
                scale = 0.5F + (elapsedTime / 1000.0F) * 1.0F;
                alpha = 0.3F + (elapsedTime / 1000.0F) * 0.7F;
                rotation = elapsedTime / 1000.0F * 1080.0F;
            } else if (elapsedTime < 2000) {
                distance = 0;
                scale = 1.5F + ((elapsedTime - 1000) / 1000.0F) * 1.0F;
                alpha = 1.0F;
                rotation = 1080.0F + ((elapsedTime - 1000) / 1000.0F) * 360.0F;
            } else {
                distance = 0;
                scale = 2.5F;
                alpha = 1.0F - ((elapsedTime - 2000) / 1000.0F);
                rotation = 1440.0F;
            }

            // 渲染图腾背景
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TOTEM_LOCATION);

            int texWidth = 256;
            int texHeight = 256;
            int drawWidth = (int)(texWidth * scale);
            int drawHeight = (int)(texHeight * scale);

            // 绘制图腾背景光效
            guiGraphics.blit(TOTEM_LOCATION,
                    width / 2 - drawWidth / 2,
                    height / 2 - drawHeight / 2,
                    0, 0,
                    drawWidth, drawHeight,
                    texWidth, texHeight);

            // 计算物品的位置
            double angle = Math.toRadians(elapsedTime / 10.0);
            double itemX = Math.sin(angle) * distance;
            double itemY = Math.cos(angle) * distance;

            // 绘制触发图腾效果的物品
            poseStack.pushPose();
            poseStack.translate(width / 2.0F + itemX, height / 2.0F + itemY, 0.0F);
            poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotation));
            poseStack.scale(scale * 3.0F, scale * 3.0F, 1.0F);

            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            guiGraphics.renderItem(lastUsedItem, -8, -8);

            poseStack.popPose();

            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
} 