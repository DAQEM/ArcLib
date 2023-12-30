package com.daqem.arc.client.gui.action;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.client.gui.action.components.ActionComponent;
import com.daqem.arc.client.gui.icon.ArcIcons;
import com.daqem.uilib.client.gui.AbstractScreen;
import com.daqem.uilib.client.gui.background.Backgrounds;
import com.daqem.uilib.client.gui.component.TextureComponent;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ActionScreen extends AbstractScreen {

    private static final int KEY_BACK = GLFW.GLFW_KEY_LEFT;
    private static final int KEY_FORWARD = GLFW.GLFW_KEY_RIGHT;

    private final List<IAction> actions;
    private IAction selectedAction;
    public ActionComponent actionComponent;
    public TextureComponent arrowLeftComponent;
    public TextureComponent arrowRightComponent;

    public ActionScreen(List<IAction> actions, IAction selectedAction) {
        super(Arc.translatable("screen.action"));
        this.actions = actions;
        this.selectedAction = selectedAction;
    }

    @Override
    public void startScreen() {
        this.actionComponent = new ActionComponent(font, getCurrentIndex(), selectedAction);
        this.arrowLeftComponent = new TextureComponent(ArcIcons.ARROW_LEFT, 0, 0, 20, 20);
        this.arrowRightComponent = new TextureComponent(ArcIcons.ARROW_RIGHT, 0, 0, 20, 20);

        setPauseScreen(false);
        setBackground(Backgrounds.getDefaultBackground(width, height));

        startComponents();
    }

    @Override
    public void onResizeScreenRepositionComponents(int width, int height) {
        super.onResizeScreenRepositionComponents(width, height);
        positionComponents();
    }

    private void startComponents() {
        arrowLeftComponent.setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            moveToPreviousActionComponent();
        });
        arrowRightComponent.setOnClickEvent((clickedObject, screen, mouseX, mouseY, button) -> {
            moveToNextActionComponent();
        });
        addComponents(actionComponent, arrowLeftComponent, arrowRightComponent);
        positionComponents();
    }

    private void positionComponents() {
        actionComponent.center();
        arrowLeftComponent.setX(actionComponent.getX());
        arrowLeftComponent.setY(actionComponent.getY() + actionComponent.getHeight() + 5);
        arrowRightComponent.setX(actionComponent.getX() + actionComponent.getWidth() - arrowRightComponent.getWidth());
        arrowRightComponent.setY(actionComponent.getY() + actionComponent.getHeight() + 5);
    }

    private int getCurrentIndex() {
        return actions.indexOf(selectedAction);
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KEY_BACK) {
            moveToPreviousActionComponent();
            return true;
        } else if (keyCode == KEY_FORWARD) {
            moveToNextActionComponent();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void moveToActionComponent(ActionComponent actionComponent) {
        actionComponent.center();
        removeComponent(this.actionComponent);
        selectedAction = actionComponent.getAction();
        this.actionComponent = actionComponent;
        addComponent(this.actionComponent);
        this.actionComponent.startRenderable();
    }

    private ActionComponent getNextActionComponent() {
        int nextIndex = getCurrentIndex() + 1;
        if (nextIndex >= actions.size()) {
            nextIndex = 0;
        }
        if (nextIndex == getCurrentIndex()) {
            return actionComponent;
        }
        return new ActionComponent(font, nextIndex, actions.get(nextIndex));
    }

    private ActionComponent getPreviousActionComponent() {
        int previousIndex = getCurrentIndex() - 1;
        if (previousIndex < 0) {
            previousIndex = actions.size() - 1;
        }
        if (previousIndex == getCurrentIndex()) {
            return actionComponent;
        }
        return new ActionComponent(font, previousIndex, actions.get(previousIndex));
    }

    private void moveToPreviousActionComponent() {
        moveToActionComponent(getPreviousActionComponent());
    }

    private void moveToNextActionComponent() {
        moveToActionComponent(getNextActionComponent());
    }
}
