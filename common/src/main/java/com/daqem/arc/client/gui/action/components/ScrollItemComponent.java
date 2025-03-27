package com.daqem.arc.client.gui.action.components;

import com.daqem.uilib.client.gui.component.ButtonComponent;
import com.daqem.uilib.client.gui.text.TruncatedText;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ScrollItemComponent extends ButtonComponent {

    private static final int PADDING = 4;
    private static final int TEXT_SPACING = 2;
    private final static Font FONT = Minecraft.getInstance().font;
    private final static int HEIGHT = (FONT.lineHeight + PADDING) * 2 + TEXT_SPACING;
    private final static int WIDTH = 144;

    private final Component name;
    private final Component description;

    private final TruncatedText nameText;
    private final TruncatedText descriptionText;

    public ScrollItemComponent(Component name, Component description) {
        super(Textures.SCROLL_BAR_BACKGROUND, 0, 0, WIDTH, HEIGHT);
        this.name = name;
        this.description = description;
        this.nameText = new TruncatedText(FONT, name, PADDING, PADDING, WIDTH - (PADDING * 2), FONT.lineHeight);
        this.descriptionText = new TruncatedText(FONT, description, PADDING, PADDING + FONT.lineHeight + TEXT_SPACING, WIDTH - (PADDING * 2), FONT.lineHeight);

        this.descriptionText.setTextColor(ChatFormatting.DARK_GRAY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta, int color) {
        super.render(guiGraphics, mouseX, mouseY, delta, color);
        nameText.renderBase(guiGraphics, mouseX, mouseY, delta);
        descriptionText.renderBase(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (isTotalHovered(mouseX, mouseY)) {
            guiGraphics.renderTooltip(FONT, this.description, mouseX, mouseY);
        }
    }
}
