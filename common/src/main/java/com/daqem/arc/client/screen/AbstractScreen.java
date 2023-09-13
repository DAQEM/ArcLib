package com.daqem.arc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractScreen extends Screen {

    protected AbstractScreen(Component component) {
        super(component);
    }

    public void playClientGUIClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public void drawDynamicComponent(GuiGraphics guiGraphics, Component component, float x, float y, int max_width, int color) {
        float scale = getScale(component, max_width);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawString(font, component, (int)(x / scale), (int)(y / scale), color);
        guiGraphics.pose().popPose();
    }

    private float getScale(Component component, int max_width) {
        float scale = 1F;
        int tries = 0;
        while (font.width(component) * scale > max_width && tries < 100) {
            scale -= 0.01F;
            tries++;
        }
        return scale;
    }

    public void drawRightAlignedString(GuiGraphics guiGraphics, @NotNull String text, int posX, int posY, int color) {
        guiGraphics.drawString(font, text, posX - font.width(text), posY, color);
    }

    public void drawCenteredString(GuiGraphics guiGraphics, @NotNull String text, int posX, int posY, int color) {
        guiGraphics.drawString(font, text, posX - font.width(text) / 2, posY, color);
    }

    public static void normal() {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void green() {
        RenderSystem.setShaderColor(0.35F, 1.0F, 0.35F, 1.0F);
    }

    public static void red() {
        RenderSystem.setShaderColor(1.0F, 0.35F, 0.35F, 1.0F);
    }

    public static void grayedOut() {
        RenderSystem.setShaderColor(0.7F, 0.7F, 0.7F, 1.0F);
    }


    public static void normalSelected() {
        RenderSystem.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);
    }

    public static void greenSelected() {
        RenderSystem.setShaderColor(0.5F, 1.0F, 0.5F, 1.0F);
    }

    public static void redSelected() {
        RenderSystem.setShaderColor(1.0F, 0.5F, 0.5F, 1.0F);
    }

    public static void grayedOutSelected() {
        RenderSystem.setShaderColor(0.6F, 0.6F, 0.6F, 1.0F);
    }


    public static void buttonHover() {
        RenderSystem.setShaderColor(0.6F, 0.6F, 1F, 1);
    }
}
