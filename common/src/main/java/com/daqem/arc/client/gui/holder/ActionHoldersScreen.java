package com.daqem.arc.client.gui.holder;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.api.action.holder.ActionHolderManager;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.client.gui.action.ActionScreen;
import com.daqem.uilib.api.client.gui.component.event.OnClickEvent;
import com.daqem.uilib.api.client.gui.component.selection.ISelectionItem;
import com.daqem.uilib.client.gui.AbstractScreen;
import com.daqem.uilib.client.gui.background.Backgrounds;
import com.daqem.uilib.client.gui.component.ButtonComponent;
import com.daqem.uilib.client.gui.component.selection.SelectionListComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.stream.Collectors;

public class ActionHoldersScreen extends AbstractScreen {

    private final List<IActionHolder> actionHolders;

    public ActionHoldersScreen(List<IActionHolder> actionHolders) {
        super(Arc.translatable("screen.action_holders"));
        this.actionHolders = actionHolders;
    }

    @Override
    public void startScreen() {
        this.setPauseScreen(false);
        this.setBackground(Backgrounds.getDefaultBackground(this.getWidth(), this.getHeight()));

        SelectionListComponent selectionListComponent = new SelectionListComponent(
                0, 0, 166, 200,
                this.getFont(), this.getTitle(),
                this.actionHolders.stream().map(iActionHolder -> new ISelectionItem<ButtonComponent>() {

                    @Override
                    public int getHeight() {
                        return 26;
                    }

                    @Override
                    public Component getName() {
                        return Arc.literal(iActionHolder.getLocation().toString());
                    }

                    @Override
                    public Component getDescription() {
                        return Arc.literal("Actions: " + iActionHolder.getActions().size());
                    }

                    @Override
                    public OnClickEvent<ButtonComponent> getOnClickEvent() {
                        return (clickedObject, screen, mouseX, mouseY, button) -> {
                            List<IAction> actions = ActionHolderManager.getInstance().getActions().stream()
                                    .filter(iAction -> iAction.getActionHolderLocation().equals(iActionHolder.getLocation()))
                                    .toList();
                            Minecraft minecraft = Minecraft.getInstance();
                            if (actions.isEmpty() && minecraft.player != null) {
                                minecraft.player.sendSystemMessage(Arc.translatable("message.no_actions"));
                            } else {
                                minecraft.setScreen(new ActionScreen(actions, actions.get(0)));
                            }
                        };
                    }
                }).collect(Collectors.toList()));

        selectionListComponent.center();
        this.addComponent(selectionListComponent);
    }

    @Override
    public void onTickScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

    }
}
