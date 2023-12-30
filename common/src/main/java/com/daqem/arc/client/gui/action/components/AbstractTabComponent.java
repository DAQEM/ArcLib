package com.daqem.arc.client.gui.action.components;

import com.daqem.uilib.api.client.gui.texture.INineSlicedTexture;
import com.daqem.uilib.client.gui.component.ButtonComponent;
import com.daqem.uilib.client.gui.component.IconComponent;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AbstractTabComponent extends ButtonComponent {

    private static final INineSlicedTexture TEXTURE = Textures.TAB;
    private static final int TAB_WIDTH = 28;
    private static final int TAB_HEIGHT = 26;

    private boolean selected;
    private final int defaultY;
    private final IconComponent iconComponent;

    public AbstractTabComponent(int x, int y, boolean selected, IconComponent iconComponent) {
        super(TEXTURE, x, y, TAB_WIDTH, TAB_HEIGHT);
        this.selected = selected;
        this.defaultY = y;
        this.iconComponent = iconComponent;
    }

    @Override
    public void startRenderable() {
        if (!isSelected()) {
            setZ(-1);
        } else {
            setZ(1);
        }
        iconComponent.setX(4);
        iconComponent.setY(2);
        this.addChild(iconComponent);
        setOnClickEvent((button, screen, mouseY, mouseX, delta) -> {
            if (getParent() instanceof ActionComponent actionComponent) {
                actionComponent.selectTab(this);
            }
        });

        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (isSelected()) {
            setZ(1);
            int targetY = defaultY - 3;
            if (getY() > targetY) {
                setY(getY() - 1);
                setHeight(getHeight() + 1);
            }
        } else {
            setZ(-1);
            if (getY() < defaultY) {
                setY(getY() + 1);
                setHeight(getHeight() - 1);
            }
        }

        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
