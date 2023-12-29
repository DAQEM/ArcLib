package com.daqem.arc.client.gui.icon;

import com.daqem.arc.Arc;
import com.daqem.uilib.client.gui.texture.icon.IconTexture;
import net.minecraft.resources.ResourceLocation;

public class ArcIcons {

    private static final ResourceLocation ICONS_LOCATION = Arc.getId("textures/gui/action_screen.png");
    public static final IconTexture CONDITION = new IconTexture(ICONS_LOCATION, 342, 0, 20, 20, 362);
    public static final IconTexture REWARD = new IconTexture(ICONS_LOCATION, 342, 20, 20, 20, 362);
    public static final IconTexture ARROW_LEFT = new IconTexture(ICONS_LOCATION, 342, 40, 20, 20, 362);
    public static final IconTexture ARROW_RIGHT = new IconTexture(ICONS_LOCATION, 342, 60, 20, 20, 362);
}
