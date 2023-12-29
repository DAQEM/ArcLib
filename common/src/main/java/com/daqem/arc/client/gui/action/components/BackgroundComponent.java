package com.daqem.arc.client.gui.action.components;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.client.gui.icon.ArcIcons;
import com.daqem.uilib.api.client.gui.texture.ITexture;
import com.daqem.uilib.client.gui.component.IconComponent;
import com.daqem.uilib.client.gui.component.TextureComponent;
import com.daqem.uilib.client.gui.texture.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackgroundComponent extends TextureComponent {

    public static final ITexture BACKGROUND_TEXTURE = new Texture(Arc.getId("textures/gui/action_screen.png"), 0, 0, 326, 166, 362);

    public static final ConditionsTabComponent CONDITIONS_TAB_COMPONENT = new ConditionsTabComponent(156, -22, true, new IconComponent(ArcIcons.CONDITION));
    public static final RewardsTabComponent REWARDS_TAB_COMPONENT = new RewardsTabComponent(188, -22, false, new IconComponent(ArcIcons.REWARD));

    public final List<AbstractTabComponent> TAB_COMPONENTS = Arrays.asList(CONDITIONS_TAB_COMPONENT, REWARDS_TAB_COMPONENT);

    private final List<IAction> actions;
    private final IAction selectedAction;
    private final ScrollComponent scrollComponent;

    public BackgroundComponent(List<IAction> actions, IAction selectedAction) {
        super(BACKGROUND_TEXTURE, 0, 0, 326, 166);
        this.actions = actions;
        this.selectedAction = selectedAction;
        this.scrollComponent = new ScrollComponent(150, 0,100, 200, getScrollItemComponents(CONDITIONS_TAB_COMPONENT));
    }

    @Override
    public void startRenderable() {
        addChildren(CONDITIONS_TAB_COMPONENT, REWARDS_TAB_COMPONENT, scrollComponent);

        super.startRenderable();
    }

    public void selectTab(AbstractTabComponent tabComponent) {
        TAB_COMPONENTS.forEach(t -> t.setSelected(false));
        tabComponent.setSelected(true);
        scrollComponent.setItems(getScrollItemComponents(tabComponent));
    }

    public List<ScrollItemComponent> getScrollItemComponents(AbstractTabComponent activeTabComponent) {
        if (selectedAction != null) {
            if (activeTabComponent == CONDITIONS_TAB_COMPONENT) {
                return selectedAction.getConditions().stream()
                        .map(c -> new ScrollItemComponent(c.getName(), c.getDescription()))
                        .toList();
            } else if (activeTabComponent == REWARDS_TAB_COMPONENT) {
                return selectedAction.getRewards().stream()
                        .map(c -> new ScrollItemComponent(c.getName(), c.getDescription()))
                        .toList();
            }
        }
        return new ArrayList<>();
    }
}
