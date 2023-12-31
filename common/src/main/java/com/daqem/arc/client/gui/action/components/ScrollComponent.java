package com.daqem.arc.client.gui.action.components;

import com.daqem.uilib.api.client.gui.component.IComponent;
import com.daqem.uilib.api.client.gui.component.scroll.ScrollOrientation;
import com.daqem.uilib.client.gui.component.AbstractComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollBarComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollContentComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollPanelComponent;
import com.daqem.uilib.client.gui.component.scroll.ScrollWheelComponent;
import com.daqem.uilib.client.gui.texture.NineSlicedTexture;
import com.daqem.uilib.client.gui.texture.Textures;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class ScrollComponent extends AbstractComponent<ScrollComponent> {

    ScrollOrientation orientation = ScrollOrientation.VERTICAL;
    int scrollPaneX = 7;
    int scrollPaneY = 15;
    int scrollPaneWidth = 144;
    int scrollPaneHeight = 140;
    int scrollBarWidth = 12;
    int scrollBarXOffset = 5;
    int scrollBarYOffset = 1;

    NineSlicedTexture scrollPaneTexture = Textures.SCROLL_PANE;
    NineSlicedTexture scrollBarTexture = Textures.SCROLL_WHEEL;

    ScrollPanelComponent scrollPane;
    ScrollWheelComponent scrollWheelComponent = new ScrollWheelComponent(scrollBarTexture, 0, 0, scrollBarWidth);
    ScrollBarComponent scrollBarComponent = new ScrollBarComponent(scrollPaneWidth + scrollBarXOffset, scrollBarYOffset, scrollBarWidth, scrollPaneHeight - (scrollBarYOffset * 2), orientation, scrollWheelComponent);
    ScrollContentComponent content = new ScrollContentComponent(0, 0, 0, orientation);

    List<ScrollItemComponent> items;

    public ScrollComponent(int x, int y, int width, int height, List<ScrollItemComponent> items) {
        super(null, x, y, width, height);
        this.items = items;
    }

    @Override
    public void startRenderable() {

        items.forEach(content::addChild);

        this.scrollPane = new ScrollPanelComponent(scrollPaneTexture, scrollPaneX, scrollPaneY, scrollPaneWidth, scrollPaneHeight, orientation, content, scrollBarComponent);
        addChild(scrollPane);

        super.startRenderable();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }

    public List<ScrollItemComponent> getItems() {
        return items;
    }

    public void setItems(List<ScrollItemComponent> items) {
        this.items = items;
        content.getChildren().clear();
        items.forEach(content::addChild);
    }
}
