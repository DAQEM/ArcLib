package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.ComponentMatchType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class ItemInInventoryCondition extends AbstractCondition {

    private final ItemStackTemplate itemStackTemplate;
    private final ComponentMatchType componentMatchType;
    private final boolean checkCount;
    private final INumberProvider count;
    private final ComparisonType comparisonType;
    private ItemStack cachedItemStack;

    public ItemInInventoryCondition(boolean inverted, ItemStackTemplate itemStackTemplate, ComponentMatchType componentMatchType, boolean checkCount, INumberProvider count, ComparisonType comparisonType) {
        super(inverted);
        this.itemStackTemplate = itemStackTemplate;
        this.componentMatchType = componentMatchType;
        this.checkCount = checkCount;
        this.count = count;
        this.comparisonType = comparisonType;
        this.cachedItemStack = null;
    }

    @Override
    public Component getDescription() {
        return getDescription(count.getDescription(), getItemStack().getHoverName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Player player = actionData.getPlayer().arc$getPlayer();
        ItemStack targetStack = getItemStack();

        int totalFound = 0;
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (ItemStack.isSameItem(stack, targetStack)) {
                if (componentMatchType.matches(targetStack, stack)) {
                    totalFound += stack.getCount();
                }
            }
        }

        if (totalFound == 0) return false;

        return !checkCount || comparisonType.compare(totalFound, count.resolve(actionData));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM_IN_INVENTORY;
    }

    public ItemStack getItemStack() {
        if (cachedItemStack != null) {
            return cachedItemStack;
        }
        this.cachedItemStack = itemStackTemplate.create();
        return cachedItemStack;
    }

    public ItemStackTemplate getItemStackTemplate() {
        return itemStackTemplate;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getCount() {
        return count;
    }

    public ComponentMatchType getComponentMatchType() {
        return componentMatchType;
    }

    public boolean shouldCheckCount() {
        return checkCount;
    }

    public static class Serializer implements IConditionSerializer<ItemInInventoryCondition> {

        @Override
        public ItemInInventoryCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            ItemStackTemplate template = getItemStackTemplate(jsonObject, "item");
            int templateCount = template.count();

            return new ItemInInventoryCondition(
                    inverted,
                    template,
                    ComponentMatchType.fromJson(jsonObject, "check_components", ComponentMatchType.NONE),
                    GsonHelper.getAsBoolean(jsonObject, "check_count", templateCount > 1),
                    getNumberProvider(jsonObject, "count", new ConstantNumberProvider(templateCount)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.GREATER_THAN_OR_EQUAL)
            );
        }

        @Override
        public ItemInInventoryCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ItemInInventoryCondition(
                    inverted,
                    ItemStackTemplate.STREAM_CODEC.decode(buf),
                    buf.readEnum(ComponentMatchType.class),
                    buf.readBoolean(),
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemInInventoryCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            ItemStackTemplate.STREAM_CODEC.encode(buf, type.itemStackTemplate);
            buf.writeEnum(type.componentMatchType);
            buf.writeBoolean(type.checkCount);
            INumberProviderSerializer.toNetwork(type.count, buf);
            buf.writeEnum(type.comparisonType);
        }
    }
}