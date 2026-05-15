package com.daqem.arc.client.gui.action.widgets;

import com.daqem.uilib.gui.widget.ButtonWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PageSwitchButtonWidget extends ButtonWidget {

    private final ResourceLocation spriteLocation;

    public PageSwitchButtonWidget(int x, int y, ResourceLocation spriteLocation, OnPress onPress, MutableComponent message) {
        super(x, y, 18, 18, Component.empty(), onPress, supplier -> CommonComponents.joinForNarration(message, supplier.get()));
        this.spriteLocation = spriteLocation;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
        guiGraphics.blitSprite(
                this.spriteLocation,
                getX(),
                getY(),
                getWidth(),
                getHeight()
        );
    }
}
