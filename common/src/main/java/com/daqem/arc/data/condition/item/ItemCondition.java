package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.arc.model.target.ArcItemTarget;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ItemCondition extends AbstractCondition {

    private final ItemStack itemStack;
    private final boolean checkComponents;
    private final boolean checkCount;
    private final INumberProvider count;
    private final ComparisonType comparisonType;
    private final ArcItemTarget target;

    public ItemCondition(boolean inverted, ItemStack itemStack, boolean checkComponents, boolean checkCount, INumberProvider count, ComparisonType comparisonType, ArcItemTarget target) {
        super(inverted);
        this.itemStack = itemStack;
        this.checkComponents = checkComponents;
        this.checkCount = checkCount;
        this.count = count;
        this.comparisonType = comparisonType;
        this.target = target;
    }

    @Override
    public Component getDescription() {
        return getDescription(count.getDescription(), getItemStack().getHoverName());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = target.getItemStack(actionData, actionData.getPlayer().arc$getPlayer());

        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }

        if (!ItemStack.isSameItem(getItemStack(), itemStack)) {
            return false;
        }

        boolean hasComponents = !checkComponents || !getItemStack().getComponents().isEmpty();
        boolean sameComponents = !checkComponents || ItemStack.isSameItemSameComponents(getItemStack(), itemStack);
        if (checkComponents && hasComponents && !sameComponents) {
            return false;
        }

        return !checkCount || comparisonType.compare(itemStack.getCount(), count.resolve(actionData));
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public INumberProvider getCount() {
        return count;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean shouldCheckComponents() {
        return checkComponents;
    }

    public boolean shouldCheckCount() {
        return checkCount;
    }

    public ArcItemTarget getTarget() {
        return target;
    }

    public static class Serializer implements IConditionSerializer<ItemCondition> {
        @Override
        public ItemCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            ItemStack itemStack = getItemStack(jsonObject, "item");
            int itemStackCount = itemStack.getCount();

            return new ItemCondition(
                    inverted,
                    itemStack,
                    GsonHelper.getAsBoolean(jsonObject, "check_components", true),
                    GsonHelper.getAsBoolean(jsonObject, "check_count", itemStackCount > 1),
                    getNumberProvider(jsonObject, "count", new ConstantNumberProvider(itemStackCount)),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    getItemTarget(jsonObject, "target", ArcItemTarget.ACTION)
            );
        }

        @Override
        public ItemCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf buf, boolean inverted) {
            return new ItemCondition(
                    inverted,
                    ItemStack.STREAM_CODEC.decode(buf),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    INumberProviderSerializer.fromNetworkStatic(buf),
                    buf.readEnum(ComparisonType.class),
                    buf.readEnum(ArcItemTarget.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, ItemCondition type) {
            IConditionSerializer.super.toNetwork(buf, type);
            ItemStack.STREAM_CODEC.encode(buf, type.itemStack);
            buf.writeBoolean(type.checkComponents);
            buf.writeBoolean(type.checkCount);
            INumberProviderSerializer.toNetwork(type.count, buf);
            buf.writeEnum(type.comparisonType);
            buf.writeEnum(type.target);
        }
    }
}