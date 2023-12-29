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

    private final TruncatedText name;
    private final TruncatedText description;

    public ScrollItemComponent(Component name, Component description) {
        super(Textures.SCROLL_BAR_BACKGROUND, 0, 0, WIDTH, HEIGHT);
        this.name = new TruncatedText(FONT, name, PADDING, PADDING, WIDTH - (PADDING * 2), FONT.lineHeight);
        this.description = new TruncatedText(FONT, description, PADDING, PADDING + FONT.lineHeight + TEXT_SPACING, WIDTH - (PADDING * 2), FONT.lineHeight);

        this.description.setTextColor(ChatFormatting.DARK_GRAY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        name.renderBase(guiGraphics, mouseX, mouseY, delta);
        description.renderBase(guiGraphics, mouseX, mouseY, delta);
    }
}
