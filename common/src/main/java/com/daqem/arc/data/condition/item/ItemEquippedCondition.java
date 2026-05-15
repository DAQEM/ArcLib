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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemEquippedCondition extends AbstractCondition {

    private final ItemStack itemStack;
    private final boolean checkComponents;
    private final boolean checkCount;
    private final INumberProvider count;
    private final ComparisonType comparisonType;
    private ItemStack cachedItemStack;

    public ItemEquippedCondition(boolean inverted, ItemStack itemStack, boolean checkComponents, boolean checkCount, INumberProvider count, ComparisonType comparisonType) {
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

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty() && matches(stack, targetStack, actionData)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matches(ItemStack currentStack, ItemStack targetStack, ActionData actionData) {
        if (!ItemStack.isSameItem(currentStack, targetStack)) return false;

        boolean hasComponents = !checkComponents || !targetStack.getComponents().isEmpty();
        boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(targetStack, currentStack);
        boolean passOnComponents = !checkComponents || !hasComponents || sameComponents;

        if (!passOnComponents) return false;

        return !checkCount || comparisonType.compare(currentStack.getCount(), count.resolve(actionData));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM_EQUIPPED;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public ItemStack getItemStack() {
        return itemStack;
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

    public static class Serializer implements IConditionSerializer<ItemEquippedCondition> {

        @Override
        public ItemEquippedCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            ItemStack itemStack = getItemStack(jsonObject, "item");
            int itemStackCount = itemStack.getCount();

            return new ItemEquippedCondition(
                    inverted,
                    itemStack,
                    GsonHelper.getAsBoolean(jsonObject, "check_components", true),
                    GsonHelper.getAsBoolean(jsonObject, "check_count", itemStackCount > 1),
                    getNumberProvider(jsonObject, "count", new ConstantNumberProvider(itemStackCount)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.GREATER_THAN_OR_EQUAL)
            );
        }

        @Override
        public ItemEquippedCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ItemEquippedCondition(
                    inverted,
                    ItemStack.STREAM_CODEC.decode(buf),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemEquippedCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            ItemStack.STREAM_CODEC.encode(buf, type.itemStack);
            buf.writeBoolean(type.checkComponents);
            buf.writeBoolean(type.checkCount);
            INumberProviderSerializer.toNetwork(type.count, buf);
            buf.writeEnum(type.comparisonType);
        }
    }
}