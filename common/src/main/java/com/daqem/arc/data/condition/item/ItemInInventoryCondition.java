package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemInInventoryCondition extends AbstractCondition {

    private final ItemStack itemStack;
    private final boolean checkComponents;
    private final boolean checkCount;
    private final INumberProvider count;
    private final ComparisonType comparisonType;
    private ItemStack cachedItemStack;

    public ItemInInventoryCondition(boolean inverted, ItemStack itemStack, boolean checkComponents, boolean checkCount, INumberProvider count, ComparisonType comparisonType) {
        super(inverted);
        this.itemStack = itemStack;
        this.checkComponents = checkComponents;
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
        for (ItemStack stack : player.getInventory().items) {
            if (ItemStack.isSameItem(stack, targetStack)) {
                boolean hasComponents = !checkComponents || !targetStack.getComponents().isEmpty();
                boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(targetStack, stack);
                if (!checkComponents || !hasComponents || sameComponents) {
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
        return itemStack;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getCount() {
        return count;
    }

    public boolean shouldCheckComponents() {
        return checkComponents;
    }

    public boolean shouldCheckCount() {
        return checkCount;
    }

    public static class Serializer implements IConditionSerializer<ItemInInventoryCondition> {

        @Override
        public ItemInInventoryCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            ItemStack itemStack = getItemStack(jsonObject, "item");
            int templateCount = itemStack.getCount();

            return new ItemInInventoryCondition(
                    inverted,
                    itemStack,
                    GsonHelper.getAsBoolean(jsonObject, "check_components", false),
                    GsonHelper.getAsBoolean(jsonObject, "check_count", templateCount > 1),
                    getNumberProvider(jsonObject, "count", new ConstantNumberProvider(templateCount)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.GREATER_THAN_OR_EQUAL)
            );
        }

        @Override
        public ItemInInventoryCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ItemInInventoryCondition(
                    inverted,
                    ItemStack.STREAM_CODEC.decode(buf),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemInInventoryCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            ItemStack.STREAM_CODEC.encode(buf, type.itemStack);
            buf.writeBoolean(type.checkComponents);
            buf.writeBoolean(type.checkCount);
            INumberProviderSerializer.toNetwork(type.count, buf);
            buf.writeEnum(type.comparisonType);
        }
    }
}