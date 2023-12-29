package com.daqem.arc.client.gui.action;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.client.gui.action.components.BackgroundComponent;
import com.daqem.uilib.client.gui.AbstractScreen;
import com.daqem.uilib.client.gui.background.Backgrounds;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class ActionScreen extends AbstractScreen {

    public final BackgroundComponent backgroundComponent;
    private final List<IAction> actions;
    private final IAction selectedAction;


    public ActionScreen(List<IAction> actions, IAction selectedAction) {
        super(Arc.translatable("screen.action"));
        this.actions = actions;
        this.selectedAction = selectedAction;
        this.backgroundComponent = new BackgroundComponent(actions, selectedAction);
    }

    @Override
    public void startScreen() {
        setPauseScreen(false);
        setBackground(Backgrounds.getDefaultBackground(width, height));
        startComponents();
    }

    private void startComponents() {
        backgroundComponent.center();
        addComponent(backgroundComponent);
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

    }
}
