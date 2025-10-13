package com.daqem.arc.data.condition.item;

import com.daqem.arc.api.ComparisonType;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ItemDurabilityCondition extends AbstractCondition {

    private final double durability;
    private final ComparisonType comparisonType;
    private final boolean isPercentage;

    public ItemDurabilityCondition(boolean inverted, double durability, ComparisonType comparisonType, boolean isPercentage) {
        super(inverted);
        this.durability = durability;
        this.comparisonType = comparisonType;
        this.isPercentage = isPercentage;
    }

    @Override
    public Component getDescription() {
        return getDescription(comparisonType.getSymbol(), durability + (isPercentage ? "%" : ""));
    }

    @Override
    public boolean isMet(ActionData actionData) {
        ItemStack itemStack = actionData.getData(IActionDataType.ITEM_STACK);
        if (itemStack != null && itemStack.isDamageableItem()) {
            double currentDurability = itemStack.getMaxDamage() - itemStack.getDamageValue();
            if (isPercentage) {
                currentDurability = (currentDurability / itemStack.getMaxDamage()) * 100.0;
            }
            return comparisonType.compare(currentDurability, this.durability);
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.ITEM_DURABILITY;
    }

    public static class Serializer implements IConditionSerializer<ItemDurabilityCondition> {

        @Override
        public ItemDurabilityCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new ItemDurabilityCondition(
                    inverted,
                    GsonHelper.getAsDouble(jsonObject, "durability"),
                    getComparisonType(jsonObject, "comparison", ComparisonType.EQUAL),
                    GsonHelper.getAsBoolean(jsonObject, "is_percentage", false)
            );
        }

        @Override
        public ItemDurabilityCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new ItemDurabilityCondition(
                    inverted,
                    friendlyByteBuf.readDouble(),
                    friendlyByteBuf.readEnum(ComparisonType.class),
                    friendlyByteBuf.readBoolean()
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ItemDurabilityCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeDouble(type.durability);
            friendlyByteBuf.writeEnum(type.comparisonType);
            friendlyByteBuf.writeBoolean(type.isPercentage);
        }
    }
}