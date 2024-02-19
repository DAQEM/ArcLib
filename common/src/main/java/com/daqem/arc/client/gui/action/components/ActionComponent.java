package com.daqem.arc.client.gui.action.components;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.IAction;
import com.daqem.arc.client.gui.icon.ArcIcons;
import com.daqem.uilib.api.client.gui.texture.ITexture;
import com.daqem.uilib.client.gui.component.IconComponent;
import com.daqem.uilib.client.gui.component.SolidColorComponent;
import com.daqem.uilib.client.gui.component.TextComponent;
import com.daqem.uilib.client.gui.component.texture.TextureComponent;
import com.daqem.uilib.client.gui.text.Text;
import com.daqem.uilib.client.gui.text.TruncatedText;
import com.daqem.uilib.client.gui.text.multiline.MultiLineText;
import com.daqem.uilib.client.gui.texture.Texture;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionComponent extends TextureComponent {

    public static final ITexture BACKGROUND_TEXTURE = new Texture(Arc.getId("textures/gui/action_screen.png"), 0, 0, 326, 166, 362);

    private final Font font;
    private final int index;
    private final IAction action;
    private TextComponent titleComponent;
    private TextComponent indexComponent;
    private TextComponent descriptionComponent;
    private SolidColorComponent lineComponent;
    private ConditionsTabComponent conditionsTabComponent;
    private RewardsTabComponent rewardsTabComponent;
    private ScrollComponent scrollComponent;

    public ActionComponent(Font font, int index, IAction action) {
        super(BACKGROUND_TEXTURE, 0, 0, 326, 166);
        this.font = font;
        this.index = index;
        this.action = action;
    }

    @Override
    public void startRenderable() {
        String indexString = String.valueOf(index + 1);
        this.titleComponent = new TextComponent(7, 13, new TruncatedText(font, action.getName(), 0, 0, 130 - font.width(indexString), font.lineHeight));
        this.indexComponent = new TextComponent(140, 13, new Text(font, Arc.literal(indexString), -font.width(indexString), 0));
        this.descriptionComponent = new TextComponent(7, 13 + 5 + font.lineHeight, new MultiLineText(font, action.getDescription(), 0, 0, 132));
        this.lineComponent = new SolidColorComponent(7, 13 + font.lineHeight + 1, 132, 1, 0xFFFFFFFF);
        this.conditionsTabComponent = new ConditionsTabComponent(156, -22, true, new IconComponent(ArcIcons.CONDITION));
        this.rewardsTabComponent = new RewardsTabComponent(188, -22, false, new IconComponent(ArcIcons.REWARD));
        this.scrollComponent = new ScrollComponent(150, 0,100, 200, getScrollItemComponents(conditionsTabComponent));

        if (titleComponent.getText() != null) {
            titleComponent.getText().setTextColor(0x333333);
            titleComponent.getText().setBold(true);
        }
        if (indexComponent.getText() != null) {
            indexComponent.getText().setTextColor(ChatFormatting.GRAY);
        }
        if (descriptionComponent.getText() != null) {
            descriptionComponent.getText().setTextColor(ChatFormatting.DARK_GRAY);
        }

        addChildren(conditionsTabComponent, rewardsTabComponent,
                titleComponent, indexComponent, descriptionComponent,
                lineComponent, scrollComponent);

        super.startRenderable();
    }

    public void selectTab(AbstractTabComponent tabComponent) {
        getTabComponents().forEach(t -> t.setSelected(false));
        tabComponent.setSelected(true);
        scrollComponent.setItems(getScrollItemComponents(tabComponent));
    }

    public List<AbstractTabComponent> getTabComponents() {
        return Arrays.asList(conditionsTabComponent, rewardsTabComponent);
    }

    public List<ScrollItemComponent> getScrollItemComponents(AbstractTabComponent activeTabComponent) {
        if (action != null) {
            if (activeTabComponent == conditionsTabComponent) {
                return action.getConditions().stream()
                        .map(c -> new ScrollItemComponent(c.getName(), c.getDescription()))
                        .toList();
            } else if (activeTabComponent == rewardsTabComponent) {
                return action.getRewards().stream()
                        .map(c -> new ScrollItemComponent(c.getName(), c.getDescription()))
                        .toList();
            }
        }
        return new ArrayList<>();
    }

    public IAction getAction() {
        return action;
    }
}
